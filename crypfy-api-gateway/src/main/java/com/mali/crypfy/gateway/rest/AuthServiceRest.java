package com.mali.crypfy.gateway.rest;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.mali.crypfy.gateway.aws.s3.SimpleS3FileUploader;
import com.mali.crypfy.gateway.aws.s3.enums.S3UploadPermission;
import com.mali.crypfy.gateway.rest.json.ImageCoordsJSON;
import com.mali.crypfy.gateway.rest.json.LoginJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.SignupJSON;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.UserService;
import com.mali.crypfy.gateway.services.user.exceptions.*;
import com.mali.crypfy.gateway.services.user.json.RedefinePasswordJSON;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.StringUtils;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploader;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploaderException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

@RestController
public class AuthServiceRest {

    final static Logger logger = LoggerFactory.getLogger(AuthServiceRest.class);

    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;
    @Autowired
    private SimpleFileUploader simpleFileUploader;
    @Value("${spring.aws.s3.temp-local}")
    private String tmpFolder;
    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${spring.aws.s3.folder-identity-verification-upload}")
    private String folderIdentityVerificationUpload;
    @Value("${spring.aws.s3.folder-document-verification-upload}")
    private String folderDocumentVerificationUpload;
    @Value("${spring.aws.s3.folder-avatar-upload}")
    private String folderAvatarUpload;
    @Value("${spring.aws.s3.folder-avatar-upload-tmp}")
    private String folderAvatarUploadTmp;

    @RequestMapping(value = "signin" ,method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody LoginJSON loginJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            String token = userService.login(loginJSON.getEmail(),loginJSON.getPassword());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Login efetuado com sucesso!");
            restResponseJSON.setResponse(token);
            return new ResponseEntity<>(restResponseJSON,HttpStatus.CREATED);
        } catch (LoginException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "admin/signin" ,method = RequestMethod.POST)
    public ResponseEntity<?> loginSignIn(@RequestBody LoginJSON loginJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            String token = userService.loginAdmin(loginJSON.getEmail(),loginJSON.getPassword());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Login efetuado com sucesso!");
            restResponseJSON.setResponse(token);
            return new ResponseEntity<>(restResponseJSON,HttpStatus.CREATED);
        } catch (LoginException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }
    @RequestMapping(value = "auth/is-valid-token",method = RequestMethod.GET)
    public ResponseEntity<Boolean> isValidToken(@RequestParam String token) {
        Boolean validToken = userService.isValidToken(token);
        return new ResponseEntity<Boolean>(validToken,HttpStatus.OK);
    }

    @RequestMapping(value = "signup" ,method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody SignupJSON signupJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            userService.signup(signupJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Conta criada com sucesso!");
            restResponseJSON.setResponse("Foi enviado um email de confirmação ! Confirme o email e complete seu cadastro");
            return new ResponseEntity<>(restResponseJSON,HttpStatus.CREATED);
        } catch (SignupException e) {
            restResponseJSON.setStatus(400);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(e.getErrors());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "email-confirmation" ,method = RequestMethod.PUT)
    public ResponseEntity<?> emailConfirmation(@RequestParam(value = "token") String emailConfirmationToken) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            String token = userService.emailConfirmation(emailConfirmationToken);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Conta confirmada com sucesso!");
            restResponseJSON.setResponse(token);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (EmailConfirmationException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/user/{email:.+}/resend-email-confirmation" ,method = RequestMethod.POST)
    public ResponseEntity<?> resendEmailConfirmation(@PathVariable String email) {
        RestResponseJSON restResponse = new RestResponseJSON();
        try {
            userService.resendEmailConfirmation(email);
            restResponse.setSuccess(true);
            restResponse.setStatus(201);
            restResponse.setMessage("Email reenviado com sucesso!");
            restResponse.setResponse(null);
            return new ResponseEntity<>(restResponse,HttpStatus.CREATED);
        } catch (UserException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(e.getStatus());
            restResponse.setMessage(e.getMessage());
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "user/redefine-password" ,method = RequestMethod.PUT)
    public ResponseEntity<?> redefinePassword(@RequestHeader(value="Authorization") String token,@RequestBody RedefinePasswordJSON redefinePasswordJSON) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            userService.redefinePassword(userJSON.getEmail(),redefinePasswordJSON);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Senha Alterada com Sucesso!");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "user/get-info" ,method = RequestMethod.GET)
    public ResponseEntity<?> getInfo(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            userJSON = userService.getInfo(userJSON.getEmail());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage(null);
            restResponseJSON.setResponse(userJSON);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "user/is-account-confirmed" ,method = RequestMethod.GET)
    public ResponseEntity<?> isAccountConfirmed(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            userJSON = userService.getInfo(userJSON.getEmail());

            boolean isAccountConfirmed = (userJSON.getIdentityVerificationStatus().equals("CONFIRMED") && userJSON.getDocumentVerificationStatus().equals("CONFIRMED")) ? true : false;

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(isAccountConfirmed);
            restResponseJSON.setMessage(null);
            restResponseJSON.setResponse(userJSON);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "user/upload-identity-verification" ,method = RequestMethod.POST)
    public ResponseEntity<?> uploadIdentityVerification(@RequestHeader(value="Authorization") String token,MultipartFile file) {
        RestResponseJSON restResponseJSON = null;
        try {
            restResponseJSON = new RestResponseJSON();

            UserJSON userJSON = jwtAuthBuilder.getInfo(token);

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = null;

            if(!isAllowedExtension(extension)) {
                restResponseJSON.setSuccess(false);
                restResponseJSON.setMessage("Extensão de arquivo inválida, verifique as extensões permitidas");
                restResponseJSON.setStatus(400);
                return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
            }

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
            simpleFileUploader.upload(bucketName,folderIdentityVerificationUpload+fileName,outputfile);

            userJSON = userService.updateIdentityVerificationStatusToWaitingApproval(userJSON.getEmail(),folderIdentityVerificationUpload+fileName);

            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Upload Feito com sucesso! Sua Identidade está sendo verificada!");
            restResponseJSON.setResponse(userJSON);
            restResponseJSON.setStatus(201);

            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (SimpleFileUploaderException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (IOException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "user/crop-avatar" ,method = RequestMethod.POST)
    public ResponseEntity<?> cropAvatar(@RequestHeader(value="Authorization") String token,@RequestBody ImageCoordsJSON imageCoordsJSON) {
        RestResponseJSON restResponseJSON = null;
        try {
            restResponseJSON = new RestResponseJSON();

            UserJSON userJSON = jwtAuthBuilder.getInfo(token);

            URL url = new URL(imageCoordsJSON.getImage());
            BufferedImage img = ImageIO.read(url);

            BigDecimal oneHundred = new BigDecimal(100);
            BigDecimal wPercebt = new BigDecimal(imageCoordsJSON.getW());
            BigDecimal hPercent = new BigDecimal(imageCoordsJSON.getH());
            BigDecimal xPercent = new BigDecimal(imageCoordsJSON.getX());
            BigDecimal yPercent = new BigDecimal(imageCoordsJSON.getY());

            BigDecimal w = new BigDecimal(img.getWidth());
            BigDecimal h = new BigDecimal(img.getHeight());

            int newWidth = wPercebt.divide(oneHundred,2,BigDecimal.ROUND_HALF_DOWN).multiply(w).intValue();
            int newHeight = hPercent.divide(oneHundred,2,BigDecimal.ROUND_HALF_DOWN).multiply(h).intValue();
            int newX = xPercent.divide(oneHundred,2,BigDecimal.ROUND_HALF_DOWN).multiply(w).intValue();
            int newY = yPercent.divide(oneHundred,2,BigDecimal.ROUND_HALF_DOWN).multiply(h).intValue();

            BufferedImage out = img.getSubimage(newX,newY,newWidth, newHeight);

            String filename = getFileNameFromUrl(imageCoordsJSON.getImage());
            File image = new File(tmpFolder+filename);
            String[] splited = filename.split("\\.");
            String ext = splited[splited.length-1];
            ImageIO.write(out,ext,image);
            simpleFileUploader.upload(bucketName,folderAvatarUpload+filename,image, S3UploadPermission.PUBLIC);

            String imgResoruce = SimpleS3FileUploader.S3_SA_EAST_RESOURCE+bucketName+"/"+folderAvatarUpload+filename;

            userService.updateAvatar(userJSON.getEmail(),imgResoruce);

            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Avatar atualizado com sucesso!");
            restResponseJSON.setResponse(imgResoruce);
            restResponseJSON.setStatus(201);

            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (SimpleFileUploaderException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (IOException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }



    @RequestMapping(value = "user/avatar-upload" ,method = RequestMethod.POST)
    public ResponseEntity<?> uploadAvatar(@RequestHeader(value="Authorization") String token, MultipartFile file) {
        RestResponseJSON restResponseJSON = null;
        try {
            restResponseJSON = new RestResponseJSON();

            UserJSON userJSON = jwtAuthBuilder.getInfo(token);

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = null;

            if(!isAllowedExtension(extension)) {
                restResponseJSON.setSuccess(false);
                restResponseJSON.setMessage("Extensão de arquivo inválida, verifique as extensões permitidas");
                restResponseJSON.setStatus(400);
                return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
            }

            InputStream inputStream =  new BufferedInputStream(file.getInputStream());
            BufferedImage orginalImage = ImageIO.read(inputStream);
            int ratio = orginalImage.getWidth()/500;
            if(ratio > 0) {
                int width = orginalImage.getWidth()/ratio;
                int height = orginalImage.getHeight()/ratio;
                orginalImage = Scalr.resize(orginalImage,width,height);
            }

            fileName = StringUtils.getMD5(userJSON.getEmail()+ "-" + StringUtils.generateRandomString())+"."+extension;
            File outputfile = new File(tmpFolder+fileName);
            ImageIO.write(orginalImage,extension,outputfile);
            simpleFileUploader.upload(bucketName,folderAvatarUploadTmp+fileName,outputfile, S3UploadPermission.PUBLIC);

            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Upload Feito com sucesso!");
            restResponseJSON.setResponse(SimpleS3FileUploader.S3_SA_EAST_RESOURCE+bucketName+"/"+folderAvatarUploadTmp+fileName);
            restResponseJSON.setStatus(201);

            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (SimpleFileUploaderException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (IOException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "user/upload-document-verification" ,method = RequestMethod.POST)
    public ResponseEntity<?> uploadDocumentVerification(@RequestHeader(value="Authorization") String token,MultipartFile file) {
        RestResponseJSON restResponseJSON = null;
        try {
            restResponseJSON = new RestResponseJSON();

            UserJSON userJSON = jwtAuthBuilder.getInfo(token);

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = null;

            if(!isAllowedExtension(extension)) {
                restResponseJSON.setSuccess(false);
                restResponseJSON.setMessage("Extensão de arquivo inválida, verifique as extensões permitidas");
                restResponseJSON.setStatus(400);
                return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
            }

            InputStream inputStream =  new BufferedInputStream(file.getInputStream());
            BufferedImage orginalImage = ImageIO.read(inputStream);
            int ratio = orginalImage.getWidth()/500;
            if(ratio > 0) {
                int width = orginalImage.getWidth()/ratio;
                int height = orginalImage.getHeight()/ratio;
                orginalImage = Scalr.resize(orginalImage,width,height);
            }
            fileName = StringUtils.getMD5(userJSON.getEmail()+ "-" + StringUtils.generateRandomString())+"."+extension;
            File outputfile = new File(tmpFolder+fileName);
            ImageIO.write(orginalImage,extension,outputfile);
            simpleFileUploader.upload(bucketName,folderDocumentVerificationUpload+fileName,outputfile);

            userJSON = userService.updateDocumentVerificationStatusToWaitingApproval(userJSON.getEmail(),folderDocumentVerificationUpload+fileName);

            restResponseJSON.setSuccess(true);
            restResponseJSON.setMessage("Upload Feito com sucesso! Seu Documento está sendo verificado!");
            restResponseJSON.setResponse(userJSON);
            restResponseJSON.setStatus(201);

            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (SimpleFileUploaderException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (IOException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage("Ocorreu um erro no upload da foto");
            restResponseJSON.setResponse(null);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    private boolean isAllowedExtension(String extension) {
        extension = extension.toLowerCase();
        return (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")) ? true : false;
    }

    @RequestMapping(value = "user" ,method = RequestMethod.PUT)
    public ResponseEntity<?> updateProfile(@RequestHeader(value="Authorization") String token,@RequestBody UserJSON user) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            UserJSON userJSON = jwtAuthBuilder.getInfo(token);
            user.setEmail(userJSON.getEmail());
            userJSON = userService.updateProfile(user);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Perfil atualizado com sucesso!");
            restResponseJSON.setResponse(userJSON);
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        } catch (UserException e) {
            restResponseJSON.setStatus(e.getStatus());
            restResponseJSON.setSuccess(false);
            restResponseJSON.setMessage(e.getMessage());
            restResponseJSON.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponseJSON, HttpStatus.OK);
        }
    }

    private String getFileNameFromUrl(String url) {
        String[] splited = url.split("/");
        return splited[splited.length-1];
    }
}
