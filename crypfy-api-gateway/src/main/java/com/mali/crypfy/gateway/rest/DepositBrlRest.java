package com.mali.crypfy.gateway.rest;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToConfirmedJSON;
import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToDeniedJSON;
import com.mali.crypfy.gateway.rest.json.DepositWithdrawBrlChangeStatusToDenied;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.services.moneymanager.DepositBrlService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.ChangeDepositStatusToCancelledJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.ChangeDepositStatusToWaitingApprovalJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploader;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploaderException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class DepositBrlRest {

    @Autowired
    private DepositBrlService depositBrlService;
    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;
    @Autowired
    @Qualifier("admin")
    private JWTAuthBuilder jwtAuthAdminBuilder;
    @Autowired
    private SimpleFileUploader simpleFileUploader;

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Value("${spring.aws.s3.temp-local}")
    private String tmpFolder;
    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${spring.aws.s3.folder-deposit-voucher-upload}")
    private String folderDepositVoucherUpload;

    @RequestMapping(value = "/deposits-brl",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> addDeposit(@RequestHeader(value="Authorization") String token,@RequestBody DepositWithdrawRequestlBrlJSON depositWithdrawlRequestlBrl) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            depositWithdrawlRequestlBrl.setEmail(userJSON.getEmail());
            depositWithdrawlRequestlBrl = depositBrlService.addDeposit(depositWithdrawlRequestlBrl);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Intenção de depósito adicionada com sucesso!");
            restResponseJSON.setResponse(depositWithdrawlRequestlBrl);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.CREATED);
        } catch (DepositBrlException e) {
            restResponseJSON.setStatus(400);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "deposits-brl/{id}/upload-voucher",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> uploadDepositVoucher(@RequestHeader(value = "Authorization") String token, @PathVariable Integer id, MultipartFile file) throws SimpleFileUploaderException {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = null;

            if(!isAllowedExtension(extension)) {
                restResponseJSON.setSuccess(false);
                restResponseJSON.setMessage("Extensão de arquivo inválida, verifique as extensões permitidas");
                restResponseJSON.setStatus(400);
                return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
            }

            if(extension.equals("pdf")) {
                fileName= StringUtils.getMD5(userJSON.getEmail()+ "-" + StringUtils.generateRandomString())+"."+extension;
                File pdf = new File(tmpFolder+fileName);
                file.transferTo(pdf);
                simpleFileUploader.upload(bucketName,folderDepositVoucherUpload+fileName,pdf);

            } else {
                InputStream inputStream =  new BufferedInputStream(file.getInputStream());
                BufferedImage orginalImage = ImageIO.read(inputStream);
                int ratio = orginalImage.getWidth()/500;
                if(ratio == 0)
                    ratio = 1;
                int width = orginalImage.getWidth()/ratio;
                int height = orginalImage.getHeight()/ratio;
                BufferedImage resizedImg = Scalr.resize(orginalImage,width,height);
                fileName = StringUtils.getMD5(userJSON.getEmail()+ "-" + StringUtils.generateRandomString())+"."+extension;
                File outputfile = new File(tmpFolder+fileName);
                ImageIO.write(resizedImg,extension,outputfile);
                simpleFileUploader.upload(bucketName,folderDepositVoucherUpload+fileName,outputfile);
            }

            depositBrlService.changeDepositStatusToWaitingApproval(id,userJSON.getEmail(),folderDepositVoucherUpload+fileName);

            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Upload feito com sucesso");
            restResponseJSON.setSuccess(true);
            restResponseJSON.setResponse(null);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);

        } catch (IOException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("Ocorreu um erro no upload do arquivo");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (SimpleFileUploaderException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("Ocorreu um erro no upload do arquivo");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (DepositBrlException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    private boolean isAllowedExtension(String extension) {
        extension = extension.toLowerCase();
        return (extension.equals("pdf") || extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")) ? true : false;
    }

    @RequestMapping(value = "admin/deposits-brl/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToConfirmed(@PathVariable  Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            DepositWithdrawRequestlBrlJSON depositWithdrawlRequestlBrl = depositBrlService.changeDepositStatusToConfirmed(id) ;
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(depositWithdrawlRequestlBrl);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "admin/deposits-brl/{id}/change-status-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToDenied(@PathVariable  Integer id, @RequestBody DepositWithdrawBrlChangeStatusToDenied depositWithdrawBrlChangeStatusToDenied) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            DepositWithdrawRequestlBrlJSON depositWithdrawlRequestlBrl = depositBrlService.changeDepositStatusToDenied(id,depositWithdrawBrlChangeStatusToDenied.getDeniedReason());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(depositWithdrawlRequestlBrl);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "deposits-brl/{id}/change-status-to-cancelled",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToCancelled(@RequestHeader(value="Authorization") String token,@PathVariable  Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON = depositBrlService.changeDepositStatusToCancelled(id,userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Status atualizado com sucesso!");
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(depositWithdrawRequestlBrlJSON);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "deposits-brl/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<RestResponseJSON> delete(@RequestHeader(value="Authorization") String token,@PathVariable  Integer id) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            depositBrlService.delete(id,userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Intenção de Depósito excluída com sucesso!");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/deposits-brl",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestHeader(value="Authorization") String token,@RequestParam(required = false) String status) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            List<DepositWithdrawRequestlBrlJSON> deposits = depositBrlService.list(userJSON.getEmail(),status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(deposits);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/admin/deposits-brl",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> listAdmin(@RequestParam(required = false) String email,@RequestParam(required = false) String status) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<DepositWithdrawRequestlBrlJSON> deposits = depositBrlService.list(email,status);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(deposits);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/deposits-brl/{id}/change-status-to-confirmed",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> secretChangeStatusToConfirmed(@RequestParam String secret,@PathVariable Integer id) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            depositBrlService.changeDepositStatusToConfirmed(id);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Depósito confirmado com sucesso");
            restResponseJSON.setResponse("Depósito confirmado com sucesso");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/secret/deposits-brl/{id}/change-status-to-denied",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> secretChangeStatusToDenied(@RequestParam String secret,@PathVariable Integer id,@RequestBody DepositWithdrawBrlChangeStatusToDenied depositWithdrawBrlChangeStatusToDenied) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();

        //temporary token solution until admin get ready
        Date todayPlusTenDays = DateUtils.addDaysFromDate(10,new Date());
        String token = "viniciusrobertofelipeulisses"+formatter.format(todayPlusTenDays);
        String mySecret = StringUtils.getMD5(token);

        if(!mySecret.equals(secret)) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(401);
            restResponseJSON.setMessage("Not Authorized");
            restResponseJSON.setResponse("Not Authorized");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }

        try {
            depositBrlService.changeDepositStatusToDenied(id,depositWithdrawBrlChangeStatusToDenied.getDeniedReason());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Depósito negado com sucesso");
            restResponseJSON.setResponse("Depósito negado com sucesso");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (DepositBrlException e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(400);
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        }
    }
}
