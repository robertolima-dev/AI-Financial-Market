package com.mali.crypfy.indexmanager.core.impl;

import com.mali.crypfy.indexmanager.core.IndexService;
import com.mali.crypfy.indexmanager.core.PlanRequestProfitService;
import com.mali.crypfy.indexmanager.core.PlanService;
import com.mali.crypfy.indexmanager.core.PlanTakeProfitEmailInfo;
import com.mali.crypfy.indexmanager.core.exception.AccountNotVerifiedException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitAddAllException;
import com.mali.crypfy.indexmanager.core.exception.PlanRequestProfitException;
import com.mali.crypfy.indexmanager.core.exception.ServiceItemError;
import com.mali.crypfy.indexmanager.core.template.HtmlTemplateBuilder;
import com.mali.crypfy.indexmanager.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.indexmanager.integrations.auth.UserService;
import com.mali.crypfy.indexmanager.integrations.auth.exceptions.UserException;
import com.mali.crypfy.indexmanager.integrations.email.MailSender;
import com.mali.crypfy.indexmanager.integrations.email.exception.EmailException;
import com.mali.crypfy.indexmanager.integrations.slack.SlackService;
import com.mali.crypfy.indexmanager.integrations.slack.exception.SlackException;
import com.mali.crypfy.indexmanager.persistence.entity.IndexPlan;
import com.mali.crypfy.indexmanager.persistence.entity.PlanTakeProfitRequest;
import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.PlanRequestProfitStatus;
import com.mali.crypfy.indexmanager.persistence.repository.PlanRequestProfitRepository;
import com.mali.crypfy.indexmanager.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlanRequestProfitServiceImpl implements PlanRequestProfitService {

    final static Logger logger = LoggerFactory.getLogger(PlanRequestProfitServiceImpl.class);

    public static final int CODE_ERROR_AMOUNT_NOT_NULL = 1000;
    public static final int CODE_ERROR_USER_PLAN_NOT_NULL = 1001;
    public static final int CODE_ERROR_USER_PLAN_NOT_FOUND = 1002;
    public static final int CODE_ERROR_AMOUNT_INVALID = 1003;
    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1004;
    public static final int CODE_ERROR_ACCOUNT_NOT_VERIFIED = 1005;
    public static final int CODE_ERROR_DUPLICATED_REQUEST = 1006;
    public static final int CODE_ERROR_AMOUNT_MININUM = 1007;

    @Autowired
    private PlanRequestProfitRepository planRequestProfitRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private SlackService slackService;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private HtmlTemplateBuilder htmlTemplateBuilder;
    @Autowired
    private IndexService indexService;

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Value("${spring.email.no-reply-email}")
    private String noReplyEmail;

    @Override
    public List<PlanTakeProfitRequest> getIndexBreakPoints(String email, Integer iduserPlan, Date start, Date end) {
        return planRequestProfitRepository.findAllByEmailAndIduserPlanAndStatusAndIndexDateBetweenOrderByIndexDateAsc(email,iduserPlan,PlanRequestProfitStatus.DONE,start,end);
    }

    private void validateAdd(PlanTakeProfitRequest planTakeProfitRequest) throws PlanRequestProfitException,AccountNotVerifiedException {
        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        //first layer validation (is account verified)
        if(planTakeProfitRequest.getEmail() != null && !planTakeProfitRequest.getEmail().equals("")) {
            try {
                boolean isAccountVerified = userService.isAccountVerified(planTakeProfitRequest.getEmail());
                if(!isAccountVerified)
                    throw new AccountNotVerifiedException("Sua conta ainda não está verificada, por favor, vá até configurações e verifique sua conta!",400);
            } catch (UserException e) {
                logger.error("error on check verified acc",e);
                throw new PlanRequestProfitException(e.getMessage(),null,500);
            }
        }

        //throw first layer validation
        if(!errors.isEmpty())
            throw new PlanRequestProfitException("Sua conta ainda não está verificada, por favor, vá até configurações e verifique sua conta!",errors,400);

        //second layer validaiton
        if(planTakeProfitRequest.getAmount() == null || planTakeProfitRequest.getAmount().compareTo(BigDecimal.ZERO) == 0)
            errors.add(new ServiceItemError("Valor desejado não pode ser vazio ou zero",CODE_ERROR_AMOUNT_NOT_NULL));
        if(planTakeProfitRequest.getIduserPlan() == null || planTakeProfitRequest.getIduserPlan() == 0)
            errors.add(new ServiceItemError("Plano é obrigatório, escolha pelo menos um plano",CODE_ERROR_USER_PLAN_NOT_NULL));
        if(planTakeProfitRequest.getEmail() == null || planTakeProfitRequest.getEmail().equals(""))
            errors.add(new ServiceItemError("Email é obrigatório",CODE_ERROR_EMAIL_NOT_NULL));

        //throw second layer validation
        if(!errors.isEmpty())
            throw new PlanRequestProfitException("Ops, encontramos alguns erros",errors,400);

        //third layer validation
        UserPlan plan = planService.find(planTakeProfitRequest.getIduserPlan());
        if(plan == null) {
            errors.add(new ServiceItemError("Plano não encontrado",CODE_ERROR_USER_PLAN_NOT_FOUND));
            throw new PlanRequestProfitException("Ops, Plano não encontrado",errors,400);
        }

        if(planTakeProfitRequest.getAmount().compareTo(plan.getAvailableProfit())>0) {
            errors.add(new ServiceItemError("Você não possuí este lucro, seu lucro atual é " + StringUtils.toMoneyFormat(plan.getAvailableProfit()),CODE_ERROR_AMOUNT_INVALID));
            throw new PlanRequestProfitException("Ops, encontramos alguns erros",errors,400);
        }

        if(planTakeProfitRequest.getAmount().compareTo(new BigDecimal("10"))< 0) {
            errors.add(new ServiceItemError("O Valor mínimo de retirada de lucro é R$ 10,00",CODE_ERROR_AMOUNT_MININUM));
            throw new PlanRequestProfitException("Ops, encontramos alguns erros",errors,400);
        }

        //check if there is another request for that userplan in progress
        List<PlanRequestProfitStatus> statuses = new ArrayList<>();
        statuses.add(PlanRequestProfitStatus.WAITING);
        statuses.add(PlanRequestProfitStatus.PROCESSING);
        List<PlanTakeProfitRequest> foundTakeProfitRequests = planRequestProfitRepository.findAllByIduserPlanAndStatusIn(plan.getIduserPlan(),statuses);
        if(!foundTakeProfitRequests.isEmpty()){
            errors.add(new ServiceItemError("Já existe uma intencão de movimentação lucro em andamento para este plano, aguarde o processamento do mesmo!",CODE_ERROR_DUPLICATED_REQUEST));
            throw new PlanRequestProfitException("Já existe uma intencão de movimentação lucro em andamento para este plano, aguarde o processamento do mesmo!",errors,400);
        }
    }

    @Override
    public PlanTakeProfitRequest add(PlanTakeProfitRequest planTakeProfitRequest) throws PlanRequestProfitException,AccountNotVerifiedException {

        validateAdd(planTakeProfitRequest);

        //preparing to save
        UserPlan plan = planService.find(planTakeProfitRequest.getIduserPlan());
        planTakeProfitRequest.setUserPlan(plan);
        planTakeProfitRequest.setCreated(new Date());
        planTakeProfitRequest.setStatus(PlanRequestProfitStatus.WAITING);
        planTakeProfitRequest.setLastUpdated(new Date());

        try {
            this.planRequestProfitRepository.save(planTakeProfitRequest);
        } catch (Exception e) {
            logger.error("error on add plan take profit request",e);
            throw new PlanRequestProfitException("Ocorreu um erro no servidor",null,500);
        }

        return planTakeProfitRequest;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void addAll(List<PlanTakeProfitRequest> planTakeProfitRequests) throws PlanRequestProfitAddAllException,AccountNotVerifiedException {
        Map<String,List<ServiceItemError>> errors = new HashMap<>();

        for(PlanTakeProfitRequest planTakeProfitRequest : planTakeProfitRequests) {
            try {
                validateAdd(planTakeProfitRequest);
            } catch (PlanRequestProfitException e) {
                errors.put(planTakeProfitRequest.getIduserPlan().toString(),e.getErrors());
            }
        }
        if(!errors.isEmpty())
            throw new PlanRequestProfitAddAllException("Ops, encontramos alguns erros",400,errors);

        //data for email info
        List<PlanTakeProfitEmailInfo> planTakeProfitEmailInfos = new ArrayList<>();
        BigDecimal totalRequestedAmount = BigDecimal.ZERO;

        for(PlanTakeProfitRequest planTakeProfitRequest : planTakeProfitRequests) {

            //preparing to save
            UserPlan plan = planService.find(planTakeProfitRequest.getIduserPlan());
            planTakeProfitRequest.setUserPlan(plan);
            planTakeProfitRequest.setCreated(new Date());
            planTakeProfitRequest.setStatus(PlanRequestProfitStatus.WAITING);
            planTakeProfitRequest.setLastUpdated(new Date());

            //prepare for email info
            planTakeProfitEmailInfos.add(new PlanTakeProfitEmailInfo(plan.getPlanName(),df.format(plan.getStartDate()),df.format(plan.getEndDate()),StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount())));
            totalRequestedAmount = totalRequestedAmount.add(planTakeProfitRequest.getAmount());

            try {
                slackService.sendMessage("Nova intenção de MOVIMENTAÇÃO DE LUCRO no valor de *" + StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount())+"* Usuário *" + planTakeProfitRequest.getEmail() +"*");
            } catch (SlackException e) {
                logger.error("error on send slack message");
            }

            try {
                this.planRequestProfitRepository.save(planTakeProfitRequest);
            } catch (Exception e) {
                logger.error("error on add all take profits requests",e);
                throw new PlanRequestProfitAddAllException("Ocorreu um erro no servidor",500,null);
            }
        }

        //send notification email
        sendNewRequestEmail(planTakeProfitEmailInfos,totalRequestedAmount,planTakeProfitRequests.get(0).getEmail());
    }


    @Override
    public List<PlanTakeProfitRequest> list(String email) throws PlanRequestProfitException {
        try {
            return this.planRequestProfitRepository.findAllByEmailOrderByCreatedDesc(email);
        } catch (Exception e) {
            logger.error("error on list plan take profit",e);
            throw new PlanRequestProfitException("Ocorreu um erro no servidor",500);
        }
    }

    @Override
    public PlanTakeProfitRequest changeStatusToCancelled(String email, Integer idplanRequestProfit) throws PlanRequestProfitException {
        PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitRepository.findByEmailAndIdplanTakeProfitRequest(email,idplanRequestProfit);

        if(planTakeProfitRequest == null)
            throw new PlanRequestProfitException("Intenção de movimentação de lucro não encontrada",null,400);

        if(planTakeProfitRequest.getStatus() != PlanRequestProfitStatus.WAITING)
            throw new PlanRequestProfitException("Não foi possível cancelar a movimentação de lucro, pois seu status atual é " + PlanRequestProfitStatus.getHumanName(planTakeProfitRequest.getStatus()),400);

        planTakeProfitRequest.setStatus(PlanRequestProfitStatus.CANCELLED);
        planTakeProfitRequest.setLastUpdated(new Date());

        try {
            slackService.sendMessage("Inteção de MOVIMENTAÇÃO DE LUCRO cancelada no valor de *" + StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount())+"* Usuário *" + planTakeProfitRequest.getEmail() +"*");
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        try {
            return planRequestProfitRepository.save(planTakeProfitRequest);
        } catch (Exception e) {
            logger.error("error on change plan take profit request status",e);
            throw new PlanRequestProfitException("Ocorreu um erro ao mudar o status da movimentação de lucro");
        }
    }

    @Override
    public PlanTakeProfitRequest changeStatusToProcessing(String email, Integer idplanRequestProfit) throws PlanRequestProfitException {
        PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitRepository.findByEmailAndIdplanTakeProfitRequest(email,idplanRequestProfit);

        if(planTakeProfitRequest == null)
            throw new PlanRequestProfitException("Intenção de movimentação de lucro não encontrada",null,400);

        if(planTakeProfitRequest.getStatus() != PlanRequestProfitStatus.WAITING)
            throw new PlanRequestProfitException("Não foi possível mudar o status da movimentação de lucro, pois seu status atual é " + PlanRequestProfitStatus.getHumanName(planTakeProfitRequest.getStatus()),400);

        try {
            planTakeProfitRequest.setStatus(PlanRequestProfitStatus.PROCESSING);
            this.planRequestProfitRepository.save(planTakeProfitRequest);

            //dispatch email notification
            sendStatusChangeToProcessingEmail(planTakeProfitRequest);

            return planTakeProfitRequest;
        } catch (Exception e) {
            logger.error("error on change plan take profit request status",e);
            throw new PlanRequestProfitException("Ocorreu um erro ao mudar o status da movimentação de lucro");
        }
    }

    @Override
    public PlanTakeProfitRequest changeStatusToFailed(String email, Integer idplanRequestProfit, String failedReason) throws PlanRequestProfitException {
        PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitRepository.findByEmailAndIdplanTakeProfitRequest(email,idplanRequestProfit);
        planTakeProfitRequest.setFailedReason(failedReason);

        if(planTakeProfitRequest == null)
            throw new PlanRequestProfitException("Intenção de movimentação de lucro não encontrada",null,400);

        if(planTakeProfitRequest.getStatus() != PlanRequestProfitStatus.PROCESSING)
            throw new PlanRequestProfitException("Não foi possível mudar o status da movimentação de lucro, pois seu status atual é " + PlanRequestProfitStatus.getHumanName(planTakeProfitRequest.getStatus()),400);

        //failed reason is mandatory
        if(planTakeProfitRequest.getFailedReason() == null || planTakeProfitRequest.getFailedReason().equals(""))
            throw new PlanRequestProfitException("Motivo do status falha é obrigatório",400);

        try {
            planTakeProfitRequest.setStatus(PlanRequestProfitStatus.FAILED);
            this.planRequestProfitRepository.save(planTakeProfitRequest);

            //dispatch email notification
            sendStatusChangeToFailedEmail(planTakeProfitRequest,failedReason);

            return planTakeProfitRequest;
        } catch (Exception e) {
            logger.error("error on change plan take profit request status",e);
            throw new PlanRequestProfitException("Ocorreu um erro ao mudar o status da movimentação de lucro");
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public PlanTakeProfitRequest changeStatusToConfirmed(String email, Integer idplanRequestProfit, Date indexDate) throws PlanRequestProfitException {

        PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitRepository.findByEmailAndIdplanTakeProfitRequest(email,idplanRequestProfit);
        planTakeProfitRequest.setIndexDate(indexDate);

        if(planTakeProfitRequest == null)
            throw new PlanRequestProfitException("Intenção de movimentação de lucro não encontrada",null,400);

        if(planTakeProfitRequest.getStatus() != PlanRequestProfitStatus.PROCESSING)
            throw new PlanRequestProfitException("Não foi possível mudar o status da movimentação de lucro, pois seu status atual é " + PlanRequestProfitStatus.getHumanName(planTakeProfitRequest.getStatus()),400);

        if(planTakeProfitRequest.getIndexDate() == null)
            throw new PlanRequestProfitException("Data de índice é obrigatório" ,400);

        //index should exist
        IndexPlan idxPlan = indexService.findIndexByDate(planTakeProfitRequest.getUserPlan().getIdplan(),indexDate);
        if(idxPlan == null)
            throw new PlanRequestProfitException("Data de Índice inválida ou não existente");

        //move balance to FIAT wallet
        try {
            userService.addAvailableBalance(planTakeProfitRequest.getEmail(),planTakeProfitRequest.getAmount());
        } catch (UserException e) {
            logger.error("error on add balance to FIAT wallet (auth service)");
            throw new PlanRequestProfitException("Não foi possível confirmar a Movimentação de Lucro",500);
        }

        try {
            planTakeProfitRequest.setStatus(PlanRequestProfitStatus.DONE);
            planRequestProfitRepository.save(planTakeProfitRequest);

            //dispatch email notification
            sendStatusChangeToConfirmedEmail(planTakeProfitRequest);

            return planTakeProfitRequest;
        } catch (Exception e ) {
            logger.error("error on change plan take profit request status",e);
            throw new PlanRequestProfitException("Ocorreu um erro ao mudar o status da movimentação de lucro");
        }
    }

    @Override
    public void delete(String email, Integer idplanRequestProfit) throws PlanRequestProfitException {
        PlanTakeProfitRequest planTakeProfitRequest = planRequestProfitRepository.findByEmailAndIdplanTakeProfitRequest(email,idplanRequestProfit);

        if(planTakeProfitRequest == null)
            throw new PlanRequestProfitException("Intenção de movimentação de lucro não encontrada",null,400);

        if(planTakeProfitRequest.getStatus() == PlanRequestProfitStatus.CANCELLED || planTakeProfitRequest.getStatus() == PlanRequestProfitStatus.FAILED) {
            try {
                this.planRequestProfitRepository.delete(planTakeProfitRequest);
            } catch (Exception e) {
                logger.error("error on delete take profit request",e);
                throw new PlanRequestProfitException("Ocorreu um erro ao excluir a movimentação de lucro");
            }
        } else
            throw new PlanRequestProfitException("Não foi possível excluir a movimentação de lucro, pois seu status atual é " + PlanRequestProfitStatus.getHumanName(planTakeProfitRequest.getStatus()),400);
    }

    @Async
    public void sendStatusChangeToProcessingEmail(PlanTakeProfitRequest planTakeProfitRequest) {
        Map<String,Object> params = new HashMap<>();
        params.put("planName",planTakeProfitRequest.getUserPlan().getPlanName());
        params.put("amount",StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount()));
        try {
            String html = htmlTemplateBuilder.buildTemplateGeneric("processing_plan_take_profit_request.ftl",params);
            mailSender.sendSimpleMailHtml(planTakeProfitRequest.getEmail(),noReplyEmail,"Movimentação de Lucro em Processamento","Crypfy Plataforrma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build email template",e);
        } catch (EmailException e) {
            logger.error("error on dispatch email",e);
        }
    }


    @Async
    public void sendStatusChangeToConfirmedEmail(PlanTakeProfitRequest planTakeProfitRequest) {
        Map<String,Object> params = new HashMap<>();
        params.put("planName",planTakeProfitRequest.getUserPlan().getPlanName());
        params.put("amount",StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount()));
        try {
            String html = htmlTemplateBuilder.buildTemplateGeneric("confirmed_plan_take_profit_request.ftl",params);
            mailSender.sendSimpleMailHtml(planTakeProfitRequest.getEmail(),noReplyEmail,"Movimentação de Lucro Confirmada","Crypfy Plataforrma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build email template",e);
        } catch (EmailException e) {
            logger.error("error on dispatch email",e);
        }
    }

    @Async
    public void sendStatusChangeToFailedEmail(PlanTakeProfitRequest planTakeProfitRequest, String failedReason) {
        Map<String,Object> params = new HashMap<>();
        params.put("planName",planTakeProfitRequest.getUserPlan().getPlanName());
        params.put("amount",StringUtils.toMoneyFormat(planTakeProfitRequest.getAmount()));
        params.put("failedReason",failedReason);
        try {
            String html = htmlTemplateBuilder.buildTemplateGeneric("failed_plan_take_profit_request.ftl",params);
            mailSender.sendSimpleMailHtml(planTakeProfitRequest.getEmail(),noReplyEmail,"Falha na Movimentação de Lucro","Crypfy Plataforrma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build email template",e);
        } catch (EmailException e) {
            logger.error("error on dispatch email",e);
        }
    }

    @Async
    public void sendNewRequestEmail(List<PlanTakeProfitEmailInfo> planTakeProfitRequests, BigDecimal totalRequestedAmount,String targetDestination) {
        Map<String,Object> params = new HashMap<>();
        params.put("planTakeProfitRequests",planTakeProfitRequests);
        params.put("totalRequestedAmount",StringUtils.toMoneyFormat(totalRequestedAmount));
        try {
            String html = htmlTemplateBuilder.buildTemplateGeneric("new_plan_take_profit_request.ftl",params);
            mailSender.sendSimpleMailHtml(targetDestination,noReplyEmail,"Nova Solicitação de Movimentação de Lucro","Crypfy Plataforrma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build email template",e);
        } catch (EmailException e) {
            logger.error("error on dispatch email",e);
        }
    }
}
