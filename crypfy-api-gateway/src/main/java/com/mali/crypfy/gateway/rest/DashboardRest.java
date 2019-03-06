package com.mali.crypfy.gateway.rest;

import com.mali.crypfy.gateway.rest.json.AdminDashboardDataJSON;
import com.mali.crypfy.gateway.rest.json.DashboardDataJSON;
import com.mali.crypfy.gateway.rest.json.RestResponseJSON;
import com.mali.crypfy.gateway.rest.json.TickerInfoJSON;
import com.mali.crypfy.gateway.services.indexmanager.IndexService;
import com.mali.crypfy.gateway.services.indexmanager.PlanService;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.IndexException;
import com.mali.crypfy.gateway.services.indexmanager.exceptions.PlanException;
import com.mali.crypfy.gateway.services.indexmanager.json.StatisticsJSON;
import com.mali.crypfy.gateway.services.indexmanager.json.UserPlanJSON;
import com.mali.crypfy.gateway.services.moneymanager.DepositBrlService;
import com.mali.crypfy.gateway.services.moneymanager.WithdrawBrlService;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.WithdrawBrlException;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import com.mali.crypfy.gateway.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class DashboardRest {

    @Autowired
    @Qualifier("user")
    private JWTAuthBuilder jwtAuthBuilder;
    @Autowired
    private PlanService planService;
    @Autowired
    private WithdrawBrlService withdrawBrlService;
    @Autowired
    private DepositBrlService depositBrlService;
    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "dashboard/data",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> data(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        DashboardDataJSON dashboardData = new DashboardDataJSON();
        UserJSON user = null;
        try {
            user = jwtAuthBuilder.getInfo(token);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }
        //get in progress plans
        try {
            List<UserPlanJSON> plans = planService.list(user.getEmail(),null);
            dashboardData.setTotalBlockedAmount(BigDecimal.ZERO);
            dashboardData.setTotalProfit(BigDecimal.ZERO);
            dashboardData.setTotalProfitCurrentMonth(BigDecimal.ZERO);
            dashboardData.setTotalProfitLastMonth(BigDecimal.ZERO);
            dashboardData.setTotalProfitLastThreeMonth(BigDecimal.ZERO);
            dashboardData.setTotalAvailableProfit(BigDecimal.ZERO);
            dashboardData.setTotalPlansInProgress(new Long(0));
            dashboardData.setTotalPlansProcessing(new Long(0));
            dashboardData.setTotalPlansFinished(new Long(0));
            dashboardData.setTotalPlansAudace(new Long(0));
            dashboardData.setTotalPlansLinea(new Long(0));
            dashboardData.setTotalPlans(new Long(0));
            dashboardData.setInProgressPlans(new ArrayList<UserPlanJSON>());
            for (UserPlanJSON userPlanJSON : plans) {
                dashboardData.setTotalPlans(dashboardData.getTotalPlans()+ new Long(1));

                if(userPlanJSON.getStatus().equals("IN_PROGRESS")) {
                    dashboardData.getInProgressPlans().add(userPlanJSON);
                    dashboardData.getTotalBlockedAmount().add(userPlanJSON.getCurrentBalance());
                    dashboardData.setTotalPlansInProgress(dashboardData.getTotalPlansInProgress() + new Long(1));
                }
                if(userPlanJSON.getStatus().equals("PROCESSING"))
                    dashboardData.setTotalPlansProcessing(dashboardData.getTotalPlansProcessing() + new Long(1));
                if(userPlanJSON.getStatus().equals("FINISHED"))
                    dashboardData.setTotalPlansFinished(dashboardData.getTotalPlansFinished() + new Long(1));
                if(userPlanJSON.getIdplan() == 1)
                    dashboardData.setTotalPlansLinea(dashboardData.getTotalPlansLinea() + new Long(1));
                if(userPlanJSON.getIdplan() == 2)
                    dashboardData.setTotalPlansAudace(dashboardData.getTotalPlansAudace() + new Long(1));
            }
        } catch (PlanException e) {
            //do nothing
        }
        //get balance evolution per point
        try {
            dashboardData.setBalanceEvolution(planService.getAllBalanceEvolutionPerPoint(user.getEmail()));
        } catch (PlanException e) {
            //do nothing
        }

        String statusConfirmed = "CONFIRMED";
        String statusWaitingApproval = "WAITING_APPROVAL";
        String statusWaitingPhotoUpload = "WAITING_PHOTO_UPLOAD";
        String statusDenied = "DENIED";
        String statusCancelled = "CANCELLED";
        String statusProcessing = "PROCESSING";

        //withdraws
        try {
            dashboardData.setTotalWithdrawConfirmed(this.withdrawBrlService.count(user.getEmail(),statusConfirmed));
            dashboardData.setTotalWithdrawProcessing(this.withdrawBrlService.count(user.getEmail(),statusProcessing));
            dashboardData.setTotalWithdrawWaitingApproval(this.withdrawBrlService.count(user.getEmail(),statusWaitingApproval));
            dashboardData.setTotalWithdrawCancelled(this.withdrawBrlService.count(user.getEmail(),statusCancelled));
            dashboardData.setTotalWithdrawDenied(this.withdrawBrlService.count(user.getEmail(),statusDenied));
        } catch (WithdrawBrlException e) {
            //do nothing
        }

        //deposits
        try {
            dashboardData.setTotalDepositConfirmed(this.depositBrlService.count(user.getEmail(),statusConfirmed));
            dashboardData.setTotalDepositWaitingPhotoUpload(this.depositBrlService.count(user.getEmail(),statusWaitingPhotoUpload));
            dashboardData.setTotalDepositWaitingApproval(this.depositBrlService.count(user.getEmail(),statusWaitingApproval));
            dashboardData.setTotalDepositCancelled(this.depositBrlService.count(user.getEmail(),statusCancelled));
            dashboardData.setTotalDepositDenied(this.depositBrlService.count(user.getEmail(),statusDenied));
        } catch (DepositBrlException e) {
            //do nothing
        }

        //total withdraw
        try {
            dashboardData.setTotalWithdraw(withdrawBrlService.getDoneSumAmount(user.getEmail()));
        } catch (WithdrawBrlException e) {
            //do nothing
        }

        //total deposit
        try {
            dashboardData.setTotalDeposit(depositBrlService.getDoneSumAmount(user.getEmail()));
        } catch (DepositBrlException e) {
            e.printStackTrace();
        }

        restResponseJSON.setSuccess(true);
        restResponseJSON.setStatus(200);
        restResponseJSON.setResponse(dashboardData);
        return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
    }

    @RequestMapping(value = "/dashboard/tickers",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getTickers(@RequestHeader(value="Authorization") String token) {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        UserJSON user = null;
        try {
            user = jwtAuthBuilder.getInfo(token);
        } catch (JWTAuthBuilderException e) {
            restResponseJSON.setStatus(401);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("acesso negado");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.UNAUTHORIZED);
        }

        List<TickerInfoJSON> tickers = new ArrayList<TickerInfoJSON>();
        try {
            List<UserPlanJSON> plans = planService.list(user.getEmail(),null);

            BigDecimal oneHundred = new BigDecimal(100);
            BigDecimal totalProfit = BigDecimal.ZERO;
            BigDecimal totalAvaiableProfit = BigDecimal.ZERO;

            BigDecimal lastThreeMonthsTotal = BigDecimal.ZERO;
            BigDecimal lastMonthTotal = BigDecimal.ZERO;
            BigDecimal currentMonthTotal = BigDecimal.ZERO;
            BigDecimal currentWeekTotal = BigDecimal.ZERO;
            BigDecimal lastWeekTotal = BigDecimal.ZERO;
            BigDecimal todayTotal = BigDecimal.ZERO;
            BigDecimal yesterdayTotal = BigDecimal.ZERO;

            BigDecimal baseAmountTotal = BigDecimal.ZERO;
            BigDecimal baseAmountThreeMonth = BigDecimal.ZERO;
            BigDecimal baseAmountLastMonth = BigDecimal.ZERO;
            BigDecimal baseAmountCurrentMonth = BigDecimal.ZERO;
            BigDecimal baseAmountCurrentWeek = BigDecimal.ZERO;
            BigDecimal baseAmountLastWeek = BigDecimal.ZERO;
            BigDecimal baseAmountToday = BigDecimal.ZERO;
            BigDecimal baseAmountYesterday = BigDecimal.ZERO;

            for(UserPlanJSON plan : plans) {
                lastThreeMonthsTotal = lastThreeMonthsTotal.add(plan.getTotalProfitLastThreeMonth());
                lastMonthTotal = lastMonthTotal.add(plan.getTotalProfitLastMonth());
                currentMonthTotal = currentMonthTotal.add(plan.getTotalProfitCurrentMonth());
                currentWeekTotal = currentWeekTotal.add(plan.getTotalProfitCurrentWeek());
                lastWeekTotal = lastWeekTotal.add(plan.getTotalProfitLastWeek());
                todayTotal = todayTotal.add(plan.getTotalProfitToday());
                yesterdayTotal = yesterdayTotal.add(plan.getTotalProfitYesterday());
                totalAvaiableProfit = totalAvaiableProfit.add(plan.getAvailableProfit());

                baseAmountTotal = baseAmountTotal.add(plan.getInitialBalance());
                baseAmountThreeMonth = baseAmountThreeMonth.add(plan.getBaseAmountLastThreeMonth());
                baseAmountLastMonth = baseAmountLastMonth.add(plan.getBaseAmountLastMonth());
                baseAmountCurrentMonth = baseAmountCurrentMonth.add(plan.getBaseAmountCurrentMonth());
                baseAmountCurrentWeek = baseAmountCurrentWeek.add(plan.getBaseAmountCurrentWeek());
                baseAmountLastWeek = baseAmountLastWeek.add(plan.getBaseAmountLastWeek());
                baseAmountToday = baseAmountToday.add(plan.getBaseAmountToday());
                baseAmountYesterday = baseAmountYesterday.add(plan.getBaseAmountYesterday());

                totalProfit = totalProfit.add(plan.getTotalProfit());
            }

            //plans stats
            int currentMonth = DateUtils.getCurrentMonth();
            int currentYear = DateUtils.getCurrentYear();
            Date firstDayOfCurrentMonth = DateUtils.getDateWithoutHour(DateUtils.getFirstDayOfCurrentMonth());
            Date firstDayOfLastMonth = DateUtils.getDateWithoutHour(DateUtils.getFirstDayOfMonth(currentMonth-2,currentYear));

            BigDecimal currentMonthLinea = BigDecimal.ZERO;
            BigDecimal currentMonthAudace = BigDecimal.ZERO;
            BigDecimal currentMonthCadenza = BigDecimal.ZERO;

            BigDecimal lastMonthLinea = BigDecimal.ZERO;
            BigDecimal lastMonthAudace = BigDecimal.ZERO;
            BigDecimal lastMonthCadenza = BigDecimal.ZERO;

            currentMonthLinea = indexService.getPerfomanceByDate(1,firstDayOfCurrentMonth,new Date());
            currentMonthAudace = indexService.getPerfomanceByDate(2,firstDayOfCurrentMonth,new Date());
            currentMonthCadenza = indexService.getPerfomanceByDate(3,firstDayOfCurrentMonth,new Date());

            lastMonthLinea = indexService.getPerfomanceByDate(1,firstDayOfLastMonth,firstDayOfCurrentMonth);
            lastMonthAudace = indexService.getPerfomanceByDate(2,firstDayOfLastMonth,firstDayOfCurrentMonth);
            lastMonthCadenza = indexService.getPerfomanceByDate(3,firstDayOfLastMonth,firstDayOfCurrentMonth);

            BigDecimal totalProfitPercent = (totalProfit.compareTo(BigDecimal.ZERO) != 0) ? totalProfit.divide(baseAmountTotal,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentLastThreeMonths = (lastThreeMonthsTotal.compareTo(BigDecimal.ZERO) != 0) ? lastThreeMonthsTotal.divide(baseAmountThreeMonth,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentLastMonth = (lastMonthTotal.compareTo(BigDecimal.ZERO) != 0) ? lastMonthTotal.divide(baseAmountLastMonth,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentCurrentMonth = (currentMonthTotal.compareTo(BigDecimal.ZERO) != 0) ? currentMonthTotal.divide(baseAmountCurrentMonth,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentCurrentWeek = (currentWeekTotal.compareTo(BigDecimal.ZERO) != 0) ? currentWeekTotal.divide(baseAmountCurrentWeek,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentLastWeek = (lastWeekTotal.compareTo(BigDecimal.ZERO) != 0) ? lastWeekTotal.divide(baseAmountLastWeek,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentYesterday = (yesterdayTotal.compareTo(BigDecimal.ZERO) != 0) ? yesterdayTotal.divide(baseAmountYesterday,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;
            BigDecimal totalProfitPercentToday = (todayTotal.compareTo(BigDecimal.ZERO) != 0) ? todayTotal.divide(baseAmountToday,4,RoundingMode.DOWN).multiply(oneHundred).setScale(2,RoundingMode.DOWN) : BigDecimal.ZERO;

            tickers.add(new TickerInfoJSON("Total Lucro",totalProfit,totalProfitPercent,"disponível retirada",totalAvaiableProfit,null));
            tickers.add(new TickerInfoJSON("Últimos 3 meses",lastThreeMonthsTotal,totalProfitPercentLastThreeMonths,null,null,null));
            tickers.add(new TickerInfoJSON("Mês Atual",currentMonthTotal,totalProfitPercentCurrentMonth,"último mês",lastMonthTotal,totalProfitPercentLastMonth));
            tickers.add(new TickerInfoJSON("Semana Atual",currentWeekTotal,totalProfitPercentCurrentWeek,"semana anterior",lastWeekTotal,totalProfitPercentLastWeek));
            tickers.add(new TickerInfoJSON("Hoje",todayTotal,totalProfitPercentToday,"ontem",yesterdayTotal,totalProfitPercentYesterday));
            tickers.add(new TickerInfoJSON("Línea Mês",null,currentMonthLinea,"mês anterior",null,lastMonthLinea));
            tickers.add(new TickerInfoJSON("Audace Mês",null,currentMonthAudace,"mês anterior",null,lastMonthAudace));
            tickers.add(new TickerInfoJSON("Cadenza Mês",null,currentMonthCadenza,"mês anterior",null,lastMonthCadenza));

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(tickers);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
        } catch (PlanException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("ocorreu um erro no servidor");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (IndexException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("ocorreu um erro no servidor");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (ParseException e) {
            restResponseJSON.setStatus(500);
            restResponseJSON.setSuccess(false);
            restResponseJSON.setResponse(null);
            restResponseJSON.setMessage("ocorreu um erro no servidor");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    //admin dashboard
    @RequestMapping(value = "/admin/dashboard/data",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> adminData() {
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        AdminDashboardDataJSON dashboardData = new AdminDashboardDataJSON();
        StatisticsJSON stats = new StatisticsJSON();

        //get month statistics
        try {
            stats = planService.getPlansStats();
            //fulfill
            dashboardData.setTotalCustody(stats.getPlans().get(0).getPlanTotalCustody().add(stats.getPlans().get(0).getPlanTotalFeeCurrentMonth()));
            dashboardData.setTotalProfit(stats.getPlans().get(0).getPlanTotalProfit());
            dashboardData.setMonthProfit(stats.getPlans().get(0).getPlanTotalProfitCurrentMonth());
            dashboardData.setLineaCustody(stats.getPlans().get(1).getPlanTotalCustody());
            dashboardData.setAudaceCustody(stats.getPlans().get(2).getPlanTotalCustody());
            dashboardData.setLineaPercent((dashboardData.getLineaCustody().divide(stats.getPlans().get(0).getPlanTotalCustody(),4, RoundingMode.HALF_DOWN)).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_DOWN));
            dashboardData.setAudacePercent((dashboardData.getAudaceCustody().divide(stats.getPlans().get(0).getPlanTotalCustody(),4, RoundingMode.HALF_DOWN)).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_DOWN));
            dashboardData.setMonthFees(stats.getPlans().get(0).getPlanTotalFeeCurrentMonth());
            dashboardData.setMonthDepositTotal(depositBrlService.getMonthSumAmount(depositBrlService.list(null,"CONFIRMED")));
            dashboardData.setMaliMoney(stats.getMaliCustody().add(stats.getPlans().get(0).getPlanTotalFeeCurrentMonth()));
            dashboardData.setDeposits(depositBrlService.list(null,"CONFIRMED"));
            dashboardData.setWithdraws(withdrawBrlService.list(null,"CONFIRMED"));
            dashboardData.setTotalPlans(stats.getPlans().get(0).getActivePlans());
        } catch (PlanException e) {
            //do nothing
        } catch (DepositBrlException e) {
            e.printStackTrace();
        } catch (WithdrawBrlException e) {
            e.printStackTrace();
        }

        restResponseJSON.setSuccess(true);
        restResponseJSON.setStatus(200);
        restResponseJSON.setResponse(dashboardData);
        return new ResponseEntity<RestResponseJSON>(restResponseJSON,HttpStatus.OK);
    }
}
