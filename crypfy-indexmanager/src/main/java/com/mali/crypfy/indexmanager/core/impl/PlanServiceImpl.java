package com.mali.crypfy.indexmanager.core.impl;

import com.mali.crypfy.indexmanager.core.*;
import com.mali.crypfy.indexmanager.core.exception.PlanException;
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
import com.mali.crypfy.indexmanager.persistence.entity.Plan;
import com.mali.crypfy.indexmanager.persistence.entity.PlanTakeProfitRequest;
import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;
import com.mali.crypfy.indexmanager.persistence.repository.IndexPlanRepository;
import com.mali.crypfy.indexmanager.persistence.repository.PlanRepository;
import com.mali.crypfy.indexmanager.persistence.repository.UserPlanRepository;
import com.mali.crypfy.indexmanager.utils.DateUtils;
import com.mali.crypfy.indexmanager.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {

    final static Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    public static BigDecimal PLAN_AMOUNT_LIMIT = new BigDecimal("1000");


    public static int CODE_ERROR_PLAN_EMAIL_NOT_NULL = 1000;
    public static int CODE_ERROR_PLAN_NAME_NOT_NULL = 1001;
    public static int CODE_ERROR_PLAN_DURATION_NOT_NULL = 1002;
    public static int CODE_ERROR_PLAN_NOT_NULL = 1003;
    public static int CODE_ERROR_PLAN_AMOUNT_NOT_NULL = 1004;
    public static int CODE_ERROR_PLAN_AMOUNT_LIMIT = 1005;
    public static int CODE_ERROR_PLAN_DURATION_INVALID = 1006;
    public static int CODE_ERROR_PLAN_INSUFFICIENT_BALANCE = 1007;
    public static int CODE_ERROR_PLAN_ACCOUNT_NOT_VERIFIED = 1008;

    @Autowired
    private UserPlanRepository userPlanRepository;
    @Autowired
    private IndexPlanRepository indexPlanRepository;
    @Autowired
    private PlanRequestProfitService planRequestProfitService;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SlackService slackService;
    @Autowired
    private HtmlTemplateBuilder htmlTemplateBuilder;
    @Autowired
    private MailSender mailSender;
    @Value("${spring.email.no-reply-email}")
    private String noReplyEmail;

    @Override
    public List<UserPlan> list(String email,UserPlanStatus status) throws PlanException {
        List<UserPlan> plans = userPlanRepository.list(email,status);
        plans.forEach((plan) -> {
            if(!plan.getStatus().equals(UserPlanStatus.PROCESSING))
                plan = calcCurrentBalancePlan(plan);
        });
        return plans;
    }

    private List<IndexPlan> mapUserTakeProfitToIndexPlan(List<IndexPlan> userIndexProfitBreakPoints, List<PlanTakeProfitRequest> requestProfitBreakPoints) {
        //map take profit amount in index
        int control = 0;
        for (IndexPlan indexProfitBreakPoint : userIndexProfitBreakPoints) {
            for (PlanTakeProfitRequest requestProfitBreakPoint : requestProfitBreakPoints)
                if (indexProfitBreakPoint.getUpdated().compareTo(requestProfitBreakPoint.getIndexDate()) == 0) {
                    IndexPlan newTakeProfitIndexPlan = IndexPlan.copy(indexProfitBreakPoint);
                    newTakeProfitIndexPlan.setTakeProfitAmount(requestProfitBreakPoint.getAmount());
                    userIndexProfitBreakPoints.set(control,newTakeProfitIndexPlan);
                }
            control++;
        }
        return userIndexProfitBreakPoints;
    }

    private List<IndexPlan> putIndexesTogetherAndSort(List<IndexPlan> indexBreakPoints, List<IndexPlan> indexProfitBreakPoints) {
        //put all together remove duplicated and sort
        if(indexProfitBreakPoints.isEmpty()){
            Collections.sort(indexBreakPoints, (i1, i2) -> i1.getUpdated().compareTo(i2.getUpdated()));
            return indexBreakPoints;
        }
        List<IndexPlan> newList = new ArrayList<>();
        for(IndexPlan idx : indexBreakPoints){
            IndexPlan newCp = null;
            for(IndexPlan idxTakeProfit : indexProfitBreakPoints) {
                if(idx.getIdindexPlan().equals(idxTakeProfit.getIdindexPlan())){
                    newCp = IndexPlan.copy(idx);
                    newCp.setTakeProfitAmount(idxTakeProfit.getTakeProfitAmount());
                }
            }
            if(newCp == null)
                newCp = IndexPlan.copy(idx);
            newList.add(newCp);
        }
        Collections.sort(newList, (i1, i2) -> i1.getUpdated().compareTo(i2.getUpdated()));
        return newList;
    }

    public UserPlan calcCurrentBalancePlan(UserPlan plan) {

        Date today = new Date();
        Date dateEnd = (plan.getEndDate().compareTo(today) == 1) ? today : plan.getEndDate();

        //get monthly break point plan index
        List<IndexPlan> indexBreakPoints = indexPlanRepository.getMonthlyIndexBreakPoints(plan.getIdplan(), plan.getStartDate(), dateEnd);

        //get user take profit done requests
        List<PlanTakeProfitRequest> requestProfitBreakPoints = planRequestProfitService.getIndexBreakPoints(plan.getEmail(), plan.getIduserPlan(), plan.getStartDate(), plan.getEndDate());

        //getting user take profit plan indexes (withdraws)
        List<Date> indexProfitDates = requestProfitBreakPoints.stream().map((p) -> p.getIndexDate()).collect(Collectors.toList());
        List<IndexPlan> indexProfitBreakPoints = indexPlanRepository.findAllByIdplanAndUpdatedIn(plan.getIdplan(), indexProfitDates);

        indexProfitBreakPoints = mapUserTakeProfitToIndexPlan(indexProfitBreakPoints,requestProfitBreakPoints);
        indexBreakPoints = putIndexesTogetherAndSort(indexBreakPoints,indexProfitBreakPoints);

        BigDecimal baseBalance = plan.getInitialBalance();
        BigDecimal currentBalance = plan.getInitialBalance();
        BigDecimal sumWithdraw = BigDecimal.ZERO;
        IndexPlan baseIdx = indexBreakPoints.get(0);

        PreparedData preparedData = prepareData(indexBreakPoints,plan.getStartDate());
        IndexPlan lastIndexPlan = indexBreakPoints.get(indexBreakPoints.size()-1);

        BigDecimal balanceLastThreeMonths = BigDecimal.ZERO;
        BigDecimal balanceLastMonth = BigDecimal.ZERO;
        BigDecimal balanceCurrentMonth = BigDecimal.ZERO;
        BigDecimal balanceYesterday = BigDecimal.ZERO;
        BigDecimal balanceToday = BigDecimal.ZERO;
        BigDecimal balanceCurrentWeek = BigDecimal.ZERO;
        BigDecimal balanceLastWeek= BigDecimal.ZERO;

        BigDecimal sumTotal = plan.getInitialBalance();
        BigDecimal feeCurrentMonth = BigDecimal.ZERO;

        List<BalanceIndex> indexes = new ArrayList<BalanceIndex>();
        List<BigDecimal> evolutionPoints = new ArrayList<>();
        for (IndexPlan indexPlan : indexBreakPoints) {
            BigDecimal percentWithFee = calcProfitPercent(baseIdx,indexPlan,plan.getPerfomanceFee());
            BigDecimal percentWithoutFee = calcProfitPercent(baseIdx,indexPlan,BigDecimal.ZERO);

            if(indexPlan.hasTakeProfit()) {
                currentBalance = baseBalance.multiply(percentWithFee.add(BigDecimal.ONE)).subtract(indexPlan.getTakeProfitAmount());

                baseBalance = currentBalance;
                baseIdx = indexPlan;

                sumWithdraw = sumWithdraw.add(indexPlan.getTakeProfitAmount());
                sumTotal = sumTotal.multiply(percentWithoutFee.add(BigDecimal.ONE)).subtract(indexPlan.getTakeProfitAmount());
            } else {
                currentBalance = baseBalance.multiply(percentWithFee.add(BigDecimal.ONE));
                sumTotal = sumTotal.multiply(percentWithoutFee.add(BigDecimal.ONE));

                if(DateUtils.isFirstDayOfMonth(indexPlan.getUpdated()) && isFirstIndexOfDay(indexPlan,indexBreakPoints)) {
                    baseBalance = currentBalance;
                    baseIdx = indexPlan;
                }
            }

            //last three months
            if(indexPlan.getUpdated().compareTo(preparedData.getLastDayOfPastFourMonths()) >= 0 && indexPlan.getUpdated().compareTo(preparedData.getLastDayOfLastMonth()) <= 0) {
                if(balanceLastThreeMonths.compareTo(BigDecimal.ZERO) == 0) {
                    balanceLastThreeMonths = currentBalance;
                    plan.setBaseAmountLastThreeMonth(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitLastThreeMonth(plan.getTotalProfitLastThreeMonth().add(currentBalance.subtract(balanceLastThreeMonths)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitLastThreeMonth(plan.getTotalProfitLastThreeMonth().add(currentBalance.subtract(balanceLastThreeMonths)).setScale(2,BigDecimal.ROUND_DOWN));
                    balanceLastThreeMonths = currentBalance;
                }
            }

            //last month
            if(indexPlan.getUpdated().compareTo(preparedData.getLastDayOfPastTwoMonths()) >= 0 && indexPlan.getUpdated().compareTo(preparedData.getLastDayOfLastMonth()) <= 0) {
                if(balanceLastMonth.compareTo(BigDecimal.ZERO) == 0) {
                    balanceLastMonth = currentBalance;
                    plan.setBaseAmountLastMonth(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitLastMonth(plan.getTotalProfitLastMonth().add(currentBalance.subtract(balanceLastMonth)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitLastMonth(plan.getTotalProfitLastMonth().add(currentBalance.subtract(balanceLastMonth)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                balanceLastMonth = currentBalance;
            }

            //current month
            if(indexPlan.getUpdated().compareTo(preparedData.getLastDayOfLastMonth()) >= 0 && indexPlan.getUpdated().compareTo(lastIndexPlan.getUpdated()) <= 0) {
                if(balanceCurrentMonth.compareTo(BigDecimal.ZERO) == 0) {
                    balanceCurrentMonth = currentBalance;
                    plan.setBaseAmountCurrentMonth(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitCurrentMonth(plan.getTotalProfitCurrentMonth().add(currentBalance.subtract(balanceCurrentMonth)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitCurrentMonth(plan.getTotalProfitCurrentMonth().add(currentBalance.subtract(balanceCurrentMonth)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                balanceCurrentMonth = currentBalance;
            }

            //yesterday
            if(indexPlan.getUpdated().compareTo(preparedData.getLastTwoDays()) >= 0 && indexPlan.getUpdated().compareTo(preparedData.getYesterday()) <= 0) {
                if(balanceYesterday.compareTo(BigDecimal.ZERO) == 0) {
                    balanceYesterday= currentBalance;
                    plan.setBaseAmountYesterday(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitYesterday(plan.getTotalProfitYesterday().add(currentBalance.subtract(balanceYesterday)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitYesterday(plan.getTotalProfitYesterday().add(currentBalance.subtract(balanceYesterday)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                balanceYesterday = currentBalance;
            }

            //current week
            if(indexPlan.getUpdated().compareTo(preparedData.getLastDayOfLastWeek()) >= 0 && indexPlan.getUpdated().compareTo(lastIndexPlan.getUpdated()) <= 0) {
                if(balanceCurrentWeek.compareTo(BigDecimal.ZERO) == 0) {
                    balanceCurrentWeek= currentBalance;
                    plan.setBaseAmountCurrentWeek(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitCurrentWeek(plan.getTotalProfitCurrentWeek().add(currentBalance.subtract(balanceCurrentWeek)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitCurrentWeek(plan.getTotalProfitCurrentWeek().add(currentBalance.subtract(balanceCurrentWeek)).setScale(2,BigDecimal.ROUND_DOWN));
                }

                balanceCurrentWeek = currentBalance;
            }

            //last week
            if(indexPlan.getUpdated().compareTo(preparedData.getLastDayOfTwoPastWeeks()) >= 0 && indexPlan.getUpdated().compareTo(preparedData.getLastDayOfLastWeek()) <= 0) {
                if(balanceLastWeek.compareTo(BigDecimal.ZERO) == 0) {
                    balanceLastWeek= currentBalance;
                    plan.setBaseAmountLastWeek(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitLastWeek(plan.getTotalProfitLastWeek().add(currentBalance.subtract(balanceLastWeek)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitLastWeek(plan.getTotalProfitLastWeek().add(currentBalance.subtract(balanceLastWeek)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                balanceLastWeek = currentBalance;
            }

            //today
            if(indexPlan.getUpdated().compareTo(preparedData.getYesterday()) >= 0 && indexPlan.getUpdated().compareTo(lastIndexPlan.getUpdated()) <= 0) {
                if(balanceToday.compareTo(BigDecimal.ZERO) == 0) {
                    balanceToday= currentBalance;
                    plan.setBaseAmountToday(currentBalance);
                }
                else {
                    if(indexPlan.hasTakeProfit())
                        plan.setTotalProfitToday(plan.getTotalProfitToday().add(currentBalance.subtract(balanceToday)).add(indexPlan.getTakeProfitAmount()).setScale(2,BigDecimal.ROUND_DOWN));
                    else
                        plan.setTotalProfitToday(plan.getTotalProfitToday().add(currentBalance.subtract(balanceToday)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                balanceToday = currentBalance;
            }
            evolutionPoints.add(currentBalance);
            indexes.add(new BalanceIndex(currentBalance,indexPlan.getUpdated()));
        }

        plan.setSumWithdraw(sumWithdraw);
        plan.setCurrentBalance(currentBalance.setScale(2,BigDecimal.ROUND_DOWN));
        plan.setTotalFeeAmount(sumTotal.subtract(currentBalance).setScale(2,BigDecimal.ROUND_DOWN));
        plan.setTotalProfit(currentBalance.add(sumWithdraw).subtract(plan.getInitialBalance()).setScale(2,BigDecimal.ROUND_DOWN));
        plan.setTotalProfitPercent(plan.getTotalProfit().divide(plan.getInitialBalance(), 4, RoundingMode.DOWN).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN));
        plan.setTotalProfitWithdraw(plan.getTotalProfit().subtract(plan.getSumWithdraw()));
        plan.setEvolutionPoints(evolutionPoints);

        plan.calcProfitPercentPeriods();

        plan.setAvailableProfit(plan.getTotalProfit().subtract(plan.getSumWithdraw()).setScale(2,BigDecimal.ROUND_DOWN));
        plan.setIndexes(indexes);
        return plan;
    }

    @Override
    public boolean checkProfit(Integer id, BigDecimal targetProfit) throws PlanException {
        UserPlan userPlan = find(id);

        if(userPlan == null)
            throw new PlanException("user plan not found");

        userPlan = calcCurrentBalancePlan(userPlan);
        return (userPlan.getAvailableProfit().compareTo(targetProfit) < 0) ? false : true;
    }

    private PreparedData prepareData(List<IndexPlan> indexes,Date planStartDate)  {
        PreparedData preparedData = new PreparedData();

        preparedData.setLastDayOfPastFourMonths(DateUtils.setLastDay(DateUtils.getFirstDayOfCurrentMonth(DateUtils.addMonthFromNow(-4))));
        if(preparedData.getLastDayOfPastFourMonths().compareTo(planStartDate) < 0)
            preparedData.setLastDayOfPastFourMonths(planStartDate);

        preparedData.setLastDayOfLastMonth(DateUtils.setLastDay(DateUtils.getFirstDayOfCurrentMonth(DateUtils.addMonthFromNow(-1))));
        if(preparedData.getLastDayOfLastMonth().compareTo(planStartDate) < 0)
            preparedData.setLastDayOfLastMonth(planStartDate);

        preparedData.setLastDayOfPastTwoMonths(DateUtils.setLastDay(DateUtils.getFirstDayOfCurrentMonth(DateUtils.addMonthFromNow(-2))));
        if(preparedData.getLastDayOfLastMonth().compareTo(planStartDate) < 0)
            preparedData.setLastDayOfPastTwoMonths(planStartDate);

        preparedData.setLastTwoDays(DateUtils.getDateWithoutTime(DateUtils.addHoursFromDate(new Date(),-48)));
        preparedData.setYesterday(DateUtils.getDateWithoutTime(DateUtils.addHoursFromDate(new Date(),-24)));
        preparedData.setToday(DateUtils.getDateWithoutTime(new Date()));
        preparedData.setLastDayOfLastWeek(DateUtils.getDateWithoutTime(DateUtils.getLastSunday()));
        preparedData.setLastDayOfTwoPastWeeks(DateUtils.getDateWithoutTime(DateUtils.setLastDayOfWeek(DateUtils.addDaysFromDate(new Date(),-7))));

        for(IndexPlan idx : indexes) {
            //last four months
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfPastFourMonths()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfPastFourMonths()) >= 0)
                    preparedData.setLastDayOfPastFourMonths(idx.getUpdated());
            //last month
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfLastMonth()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfLastMonth()) >= 0)
                    preparedData.setLastDayOfLastMonth(idx.getUpdated());
            //last two months
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfPastTwoMonths()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfPastTwoMonths()) >= 0)
                    preparedData.setLastDayOfPastTwoMonths(idx.getUpdated());
            //past two weeks
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfTwoPastWeeks()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfTwoPastWeeks()) >= 0)
                    preparedData.setLastDayOfTwoPastWeeks(idx.getUpdated());
            //past two days
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastTwoDays()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastTwoDays()) >= 0)
                    preparedData.setLastTwoDays(idx.getUpdated());
            //yesterday
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getYesterday()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getYesterday()) >= 0)
                    preparedData.setYesterday(idx.getUpdated());
            //today
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getToday()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getToday()) >= 0)
                    preparedData.setToday(idx.getUpdated());
            //last day of last week
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfLastWeek()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfLastWeek()) >= 0)
                    preparedData.setLastDayOfLastWeek(idx.getUpdated());
            //last dat of two past weeks
            if(DateUtils.getDateWithoutTime(idx.getUpdated()).compareTo(preparedData.getLastDayOfTwoPastWeeks()) == 0)
                if(idx.getUpdated().compareTo(preparedData.getLastDayOfTwoPastWeeks()) >= 0)
                    preparedData.setLastDayOfTwoPastWeeks(idx.getUpdated());
        }

        return preparedData;
    }

    private boolean isFirstIndexOfDay(IndexPlan indexPlan,List<IndexPlan> indexBreakPoints) {
        boolean isFirst = true;
        Date indexPlanDate = DateUtils.getDateWithoutTime(indexPlan.getUpdated());
        for(IndexPlan idx : indexBreakPoints) {
            Date idxDate = DateUtils.getDateWithoutTime(idx.getUpdated());
            if(indexPlanDate.compareTo(idxDate) == 0 && idx.getUpdated().compareTo(indexPlan.getUpdated()) < 0)
                isFirst = false;
        }
        return isFirst;
    }

    private BigDecimal calcProfitPercent(IndexPlan firstIndex, IndexPlan lastIndex, BigDecimal perfomanceFee) {
        BigDecimal percent = lastIndex.getIndex().subtract(firstIndex.getIndex()).divide(firstIndex.getIndex(), 4, RoundingMode.DOWN);
        //perfomance fee condition
        if (percent.compareTo(BigDecimal.ZERO) == 1)
            percent = BigDecimal.ONE.subtract(perfomanceFee).multiply(percent);
        return percent;
    }

    private BigDecimal calcProfitPercent(BigDecimal firstIndex, BigDecimal lastIndex, BigDecimal perfomanceFee) {
        BigDecimal percent = lastIndex.subtract(firstIndex).divide(firstIndex, 4, RoundingMode.DOWN);
        //perfomance fee condition
        if (percent.compareTo(BigDecimal.ZERO) == 1)
            percent = BigDecimal.ONE.subtract(perfomanceFee).multiply(percent);
        return percent;
    }

    @Override
    public List<UserPlan> getInProgressPlans(String email) throws PlanException {
        List<UserPlan> plans = userPlanRepository.findByEmailAndStatusOrderByStartDateDesc(email, UserPlanStatus.IN_PROGRESS);
        plans.forEach((plan) -> {
            plan = calcCurrentBalancePlan(plan);
        });
        return plans;
    }

    @Override
    public List<BalancePerDate> getAllBalanceEvolutionPerPoint(String email) throws PlanException {

        List<UserPlanStatus> status = new ArrayList<UserPlanStatus>();
        status.add(UserPlanStatus.IN_PROGRESS);
        status.add(UserPlanStatus.FINISHED);

        List<UserPlan> plans = userPlanRepository.findAllByEmailAndStatusInOrderByCreatedDesc(email,status);

        Map<Date, BigDecimal> balance = new TreeMap<Date, BigDecimal>();
        List<IndexPlan> indexes = null;
        for (UserPlan plan : plans) {

            BigDecimal baseAmount = plan.getInitialBalance();
            indexes = indexPlanRepository.findAllByIdplanAndUpdatedBetweenOrderByUpdatedAsc(plan.getIdplan(),plan.getStartDate(),plan.getEndDate());

            //get user take profit done requests
            List<PlanTakeProfitRequest> requestProfitBreakPoints = planRequestProfitService.getIndexBreakPoints(plan.getEmail(), plan.getIduserPlan(), plan.getStartDate(), plan.getEndDate());

            IndexPlan firstIndex = indexes.get(0);
            BigDecimal sumAmount = null;
            BigDecimal profit = null;
            BigDecimal takeProfitAmount = null;
            for (IndexPlan index : indexes) {
                BigDecimal percent = calcProfitPercent(firstIndex, index, plan.getPerfomanceFee());

                sumAmount = balance.get(index.getUpdated());
                profit = baseAmount.multiply(percent.add(BigDecimal.ONE));

                takeProfitAmount = getTakeProfitAmount(index.getUpdated(),requestProfitBreakPoints);
                if (takeProfitAmount != null) {
                    profit = profit.subtract(takeProfitAmount);
                    baseAmount = profit;
                    firstIndex = index;
                } else if (DateUtils.isFirstDayOfMonth(index.getUpdated())) {
                    baseAmount = profit;
                    firstIndex = index;
                }

                if (sumAmount != null)
                    balance.put(index.getUpdated(), sumAmount.add(profit));
                else
                    balance.put(index.getUpdated(), profit);
            }
        }
        return balance.entrySet()
                .stream()
                .map(e -> new BalancePerDate(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private BigDecimal getTakeProfitAmount(Date indexDate, List<PlanTakeProfitRequest> planTakeProfitRequests) {
        for (PlanTakeProfitRequest planTakeProfitRequest : planTakeProfitRequests) {
            if(planTakeProfitRequest.getIndexDate().compareTo(indexDate) == 0)
                return planTakeProfitRequest.getAmount();
        }
        return null;
    }

    @Override
    public BigDecimal getTotalProfit(String email) throws PlanException {

        List<UserPlanStatus> status = new ArrayList<UserPlanStatus>();
        status.add(UserPlanStatus.IN_PROGRESS);
        status.add(UserPlanStatus.FINISHED);

        List<UserPlan> plans = userPlanRepository.findAllByEmailAndStatusInOrderByCreatedDesc(email,status);

        BigDecimal totalProfit = BigDecimal.ZERO;

        for (UserPlan plan : plans) {
            plan = calcCurrentBalancePlan(plan);
            totalProfit = totalProfit.add(plan.getTotalProfit());

        }
        return totalProfit;
    }

    @Override
    public Long countAllByEmail(String email) throws PlanException {
        return userPlanRepository.countAllByEmail(email);
    }

    @Override
    public UserPlan add(UserPlan userPlan) throws PlanException {

        try {
            boolean isAccountVerified = userService.isAccountVerified(userPlan.getEmail());
            if(!isAccountVerified) {
                List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
                errors.add(new ServiceItemError("Sua conta não está verificada! Por favor faça a verificação",CODE_ERROR_PLAN_ACCOUNT_NOT_VERIFIED));
                throw new PlanException("Sua conta não está verificada! Por favor faça a verificação",errors,500);
            }
        } catch (UserException e) {
            logger.error("error on check if account is verified",e);
            throw new PlanException("Ocorreu um erro ao contratar plano",500);
        }

        List<ServiceItemError> errors = validateAdd(userPlan);

        if(!errors.isEmpty())
            throw new PlanException("Ocorreu um erro ao contratar plano",errors,400);

        Plan plan = planRepository.findOne(userPlan.getIdplan());

        if(plan == null)
            throw new PlanException("Plano não encontrado",400);

        //check if user has enough money
        BigDecimal availableBalance = null;
        try {
            availableBalance = userService.getAvaiableBalance(userPlan.getEmail());
        } catch (UserException e) {
            logger.error("error on check user balance",e);
            throw new PlanException("Ocorreu um erro ao contratar plano",500);
        }

        if(availableBalance.compareTo(userPlan.getInitialBalance()) < 0) {
            errors.add(new ServiceItemError("Saldo insuficiente",CODE_ERROR_PLAN_INSUFFICIENT_BALANCE));
            throw new PlanException("Você não possuí saldo, por favor, faça um depósito",errors,400);
        }

        try {
            userService.addAvailableBalance(userPlan.getEmail(), userPlan.getInitialBalance().negate());
        } catch (UserException e) {
            logger.error("error on update available balance",e);
            throw new PlanException("Ocorreu um erro ao contratar o plano",500);
        }

        userPlan.setPlan(plan);
        userPlan.setStatus(UserPlanStatus.PROCESSING);
        userPlan.setCreated(new Date());

        //calc input fee
        BigDecimal fullInitialBalance = userPlan.getInitialBalance();
        BigDecimal percent = new BigDecimal(100).subtract(plan.getInputFee()).divide(new BigDecimal(100),2,BigDecimal.ROUND_CEILING);
        userPlan.setInitialBalance(userPlan.getInitialBalance().multiply(percent));
        userPlan.setInputFee(fullInitialBalance.subtract(userPlan.getInitialBalance()));

        if(userPlan.getDuration() == 6)
            userPlan.setPerfomanceFee(plan.getFee6().divide(new BigDecimal(100)));
        else
            userPlan.setPerfomanceFee(plan.getFee12().divide(new BigDecimal(100)));

        try {
            userPlanRepository.save(userPlan);
        } catch (Exception e) {
            logger.error("error on save user plan",e);

            //send back available balance to user
            try {
                userService.addAvailableBalance(userPlan.getEmail(), userPlan.getInitialBalance());
            } catch (UserException e1) {
                logger.error("error on update available balance",e);
                throw new PlanException("Ocorreu um erro ao contratar o plano",500);
            }

            throw new PlanException("Ocorreu um erro ao contratar o plano",500);
        }

        try {
            slackService.sendMessage("Nova Intenção de contratação de PLANO *"+plan.getName()+"*("+userPlan.getDuration()+") no VALOR de *"+ StringUtils.toMoneyFormat(userPlan.getInitialBalance())+"* Usuário *"+userPlan.getEmail()+"*");
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        sendProcessingPlanEmail(userPlan);

        return userPlan;
    }


    @Override
    public UserPlan changeStatusToInProgress(Integer iduserPlan) throws PlanException {
        UserPlan userPlan = userPlanRepository.findOne(iduserPlan);
        IndexPlan indexPlan = indexPlanRepository.findFirst1ByIdplanOrderByUpdatedDesc(userPlan.getIdplan());

        if (userPlan == null)
            throw new PlanException("Plano não encontrado!",400);

        userPlan.setStatus(UserPlanStatus.IN_PROGRESS);
        userPlan.setStartDate(indexPlan.getUpdated());
        userPlan.setEndDate(DateUtils.addMonthFromDate(indexPlan.getUpdated(),userPlan.getDuration()));

        try {
            userPlanRepository.save(userPlan);
        } catch (Exception e) {
            logger.error("error on update plan",e);
            throw new PlanException("Ocorreu um erro ao atualizar o plano",500);
        }

        //send email notification
        sendInProgressPlanEmail(userPlan);

        return userPlan;
    }

    @Override
    public UserPlan calcDetailedBalancePlan(UserPlan plan) {
        return null;
    }

    private List<ServiceItemError> validateAdd(UserPlan userPlan) {
        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(userPlan.getEmail() == null || userPlan.getEmail().equals(""))
            errors.add(new ServiceItemError("Email não pode ser vazio",CODE_ERROR_PLAN_EMAIL_NOT_NULL));
        if(userPlan.getPlanName() == null || userPlan.getPlanName().equals(""))
            errors.add(new ServiceItemError("Nome Pessoal do plano não pode ser vazio",CODE_ERROR_PLAN_NAME_NOT_NULL));
        if(userPlan.getDuration() == 0 || userPlan.getDuration() == null)
            errors.add(new ServiceItemError("Duração do plano não pode ser vazio",CODE_ERROR_PLAN_DURATION_NOT_NULL));
        if(userPlan.getIdplan() == 0 || userPlan.getIdplan() == null)
            errors.add(new ServiceItemError("Plano é obrigatório",CODE_ERROR_PLAN_NOT_NULL));
        if(userPlan.getInitialBalance() == null || userPlan.getInitialBalance().compareTo(BigDecimal.ZERO) <= 0)
            errors.add(new ServiceItemError("Valor do plano não pode ser vazio ou zero",CODE_ERROR_PLAN_AMOUNT_NOT_NULL));
        if(userPlan.getInitialBalance() != null && userPlan.getInitialBalance().compareTo(PLAN_AMOUNT_LIMIT)<0)
            errors.add(new ServiceItemError("Valor do plano não pode ser menor que R$ 1.000,00",CODE_ERROR_PLAN_AMOUNT_LIMIT));
        if(userPlan.getDuration() != 6 && userPlan.getDuration() != 12)
            errors.add(new ServiceItemError("Duração do plano inválida",CODE_ERROR_PLAN_DURATION_INVALID));

        return errors;
    }


    @Override
    public Statistics getPlansStats() throws PlanException {

        List<UserPlan> plans = userPlanRepository.findByStatusOrderByStartDateDesc(UserPlanStatus.IN_PROGRESS);
        Map<Integer,PlanStatistics> map = new HashMap<Integer,PlanStatistics>();
        Statistics statistics = new Statistics();
        BigDecimal maliCustody = BigDecimal.ZERO;
        BigDecimal totalFee = BigDecimal.ZERO;

        for (UserPlan plan : plans) {
            plan = calcCurrentBalancePlan(plan);
            if(map.containsKey(plan.getIdplan())) {
                map.get(plan.getIdplan()).setActivePlans(map.get(plan.getIdplan()).getActivePlans()+1);
                map.get(plan.getIdplan()).setPlanTotalProfit(map.get(plan.getIdplan()).getPlanTotalProfit().add(plan.getTotalProfit()));
                map.get(plan.getIdplan()).setPlanTotalCustody(map.get(plan.getIdplan()).getPlanTotalCustody().add(plan.getCurrentBalance()));
                map.get(plan.getIdplan()).setPlanTotalProfitCurrentMonth(map.get(plan.getIdplan()).getPlanTotalProfitCurrentMonth().add(plan.getTotalProfitCurrentMonth()));
                map.get(plan.getIdplan()).setPlanTotalFeeCurrentMonth(map.get(plan.getIdplan()).getPlanTotalFeeCurrentMonth().add(plan.getTotalFeeCurrentMonth()));
            } else {
                PlanStatistics stats = new PlanStatistics();
                stats.setId(plan.getIdplan());
                stats.setPlanName(plan.getPlan().getName());
                stats.setActivePlans(1);
                stats.setPlanTotalProfit(plan.getTotalProfit());
                stats.setPlanTotalCustody(plan.getCurrentBalance());
                stats.setPlanTotalProfitCurrentMonth(plan.getTotalProfitCurrentMonth());
                stats.setPlanTotalFeeCurrentMonth(plan.getTotalFeeCurrentMonth());
                map.put(plan.getIdplan(),stats);
            }
            if (plan.getEmail().equals("contato@maliexchange.com")) maliCustody = maliCustody.add(plan.getCurrentBalance());
            totalFee = totalFee.add(plan.getTotalFeeAmount());
        }
        //set data
        List<PlanStatistics> planStatistics = new ArrayList<>();
        PlanStatistics stats = new PlanStatistics();
        stats.setId(0);
        stats.setPlanName("Geral");
        stats.setActivePlans(0);
        stats.setPlanTotalProfit(new BigDecimal("0"));
        stats.setPlanTotalCustody(new BigDecimal("0"));
        stats.setPlanTotalProfitCurrentMonth(new BigDecimal("0"));
        stats.setPlanTotalFeeCurrentMonth(new BigDecimal("0"));
        planStatistics.add(stats);

        for (Map.Entry<Integer, PlanStatistics> entry : map.entrySet()) {
            planStatistics.add(entry.getValue());
            planStatistics.get(0).setActivePlans(planStatistics.get(0).getActivePlans()+entry.getValue().getActivePlans());
            planStatistics.get(0).setPlanTotalProfit(planStatistics.get(0).getPlanTotalProfit().add(entry.getValue().getPlanTotalProfit()));
            planStatistics.get(0).setPlanTotalCustody(planStatistics.get(0).getPlanTotalCustody().add(entry.getValue().getPlanTotalCustody()));
            planStatistics.get(0).setPlanTotalProfitCurrentMonth(planStatistics.get(0).getPlanTotalProfitCurrentMonth().add(entry.getValue().getPlanTotalProfitCurrentMonth()));
            planStatistics.get(0).setPlanTotalFeeCurrentMonth(planStatistics.get(0).getPlanTotalFeeCurrentMonth().add(entry.getValue().getPlanTotalFeeCurrentMonth()));
        }
        statistics.setMaliCustody(maliCustody);
        statistics.setFeesTotal(totalFee);
        statistics.setPlans(planStatistics);

        return statistics;
    }

    @Override
    public List<MonthlyPerfomance> getPerfomancePlanMonthly(int idplan, Date start, Date end) throws PlanException {
        List<IndexPlan> indexes = indexPlanRepository.findAllByIdplanAndUpdatedBetweenOrderByUpdatedAsc(idplan,start,end);

        if(indexes.isEmpty())
            return null;

        List<MonthlyPerfomance> monthlyPerfomances = new ArrayList<MonthlyPerfomance>();
        IndexPlan firstIndex = indexes.get(0);
        IndexPlan lastIndex = null;
        int i = 1;
        firstIndex = indexes.get(0);
        for(IndexPlan index : indexes) {
            if(DateUtils.isFirstDayOfMonth(index.getUpdated()) && firstIndex.getUpdated().compareTo(index.getUpdated()) != 0){
                lastIndex = index;
                MonthlyPerfomance monthlyPerfomance = new MonthlyPerfomance();
                monthlyPerfomance.setMonth(DateUtils.getMonthAsInteger(DateUtils.addMonthFromDate(index.getUpdated(),-1)));
                monthlyPerfomance.setMonthName(DateUtils.getIntMonthAsString(monthlyPerfomance.getMonth()));
                monthlyPerfomance.setPerfomance(calcProfitPercent(firstIndex,lastIndex,BigDecimal.ZERO));
                monthlyPerfomance.setPerfomance(monthlyPerfomance.getPerfomance().multiply(new BigDecimal("100")));
                monthlyPerfomances.add(monthlyPerfomance);
                firstIndex = lastIndex;
            }
            i++;
        }
        return monthlyPerfomances;
    }

    @Override
    public UserPlan find(Integer id) {
        return calcCurrentBalancePlan(this.userPlanRepository.findOne(id));
    }

    @Override
    public List<TickerInfo> buidTickerInfo(String email) throws PlanException {
        return null;
    }

    @Async
    public void sendProcessingPlanEmail(UserPlan userPlan) {
        try {
            Map<String,String> params = new HashMap<>();

            params.put("planName",userPlan.getPlan().getName());
            params.put("planDuration",userPlan.getDuration().toString());
            params.put("planImg",userPlan.getPlan().getLogo2());
            params.put("initialBalance",StringUtils.toMoneyFormat(userPlan.getInitialBalance()));
            params.put("planCustomName",userPlan.getPlanName());

            String html = htmlTemplateBuilder.buildTemplate("processing_plan.ftl",params);
            String subject = "Plano " + userPlan.getPlan().getName() + " " + userPlan.getDuration().toString() + " em Processamento";
            mailSender.sendSimpleMailHtml(userPlan.getEmail(),noReplyEmail,subject,"Crypfy Plataforma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build html",e);
        } catch (EmailException e) {
            logger.error("error on send email",e);
        }
    }

    @Async
    public void sendInProgressPlanEmail(UserPlan userPlan) {
        try {
            Map<String,String> params = new HashMap<>();

            params.put("planName",userPlan.getPlan().getName());
            params.put("planDuration",userPlan.getDuration().toString());
            params.put("planImg",userPlan.getPlan().getLogo2());
            params.put("initialBalance",StringUtils.toMoneyFormat(userPlan.getInitialBalance()));
            params.put("planCustomName",userPlan.getPlanName());

            String html = htmlTemplateBuilder.buildTemplate("in_progress_plan.ftl",params);
            String subject = "Plano " + userPlan.getPlan().getName() + " " + userPlan.getDuration().toString() + " Vigente";
            mailSender.sendSimpleMailHtml(userPlan.getEmail(),noReplyEmail,subject,"Crypfy Plataforma",html);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build html",e);
        } catch (EmailException e) {
            logger.error("error on send email",e);
        }
    }
}
