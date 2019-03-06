package com.mali.crypfy.moneymanager.persistence.repository;

import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.TypeDepositWithdraw;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Deposit Withdraw Brl Repository Interface
 */
public interface DepositWithdrawRequestBrlRepository extends CrudRepository<DepositWithdrawRequestBrl,Integer>,DepositWithdrawRequestBrlCustomRepository {
    /**
     * Find By Email and Type ordering by Last Updated Date Desc
     * @param email
     * @param type
     * @return List of Deposit Withdraw Brl Object
     */
    public List<DepositWithdrawRequestBrl> findByEmailAndTypeOrderByLastUpdatedDesc(String email, TypeDepositWithdraw type);

    /**
     * Find by Id, Type and Email
     * @param iddepositWithdrawlRequestBrl
     * @param type
     * @param email
     * @return Deposit Withdraw Brl Object
     */
    public DepositWithdrawRequestBrl findByIddepositWithdrawRequestBrlAndTypeAndEmail(Integer iddepositWithdrawlRequestBrl, TypeDepositWithdraw type, String email);

    /**
     * Sum Amount By Email, Type and Status
     * @param type
     * @param email
     * @param status
     * @return Deposit Withdraw Brl Object
     */
    @Query("SELECT sum(d.amount) from DepositWithdrawRequestBrl d where d.type = :type and d.email = :email and d.status = :status")
    public BigDecimal sumAmountByEmailAndStatus(@Param("type") TypeDepositWithdraw type, @Param("email") String email, @Param("status") StatusDepositWithdrawBrl status);

    /**
     * Count By Email, Type and Status
     * @param email
     * @param status
     * @param type
     * @return counter Long
     */
    public Long countAllByEmailAndStatusAndType(String email, StatusDepositWithdrawBrl status, TypeDepositWithdraw type);

    /**
     * Find All By Email, Created greater than, Type and status not in
     * @param email
     * @param date
     * @param type
     * @param statusDepositWithdrawBrl
     * @return List of Deposit Withdraw Brl Object
     */
    public List<DepositWithdrawRequestBrl> findAllByEmailAndCreatedGreaterThanAndTypeAndStatusNotIn(String email, Date date,TypeDepositWithdraw type,StatusDepositWithdrawBrl statusDepositWithdrawBrl);

    /**
     * Find By Id and Email
     * @param iddepositWithdrawlRequestBrl
     * @param email
     * @return Deposit Withdraw Brl Object
     */
    public DepositWithdrawRequestBrl findByIddepositWithdrawRequestBrlAndEmail(Integer iddepositWithdrawlRequestBrl, String email);
}
