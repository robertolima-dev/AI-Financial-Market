package com.mali.crypfy.auth.integrations.queue.consumer;

import com.mali.crypfy.auth.core.user.UserService;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Add Brl Balance Listener
 */
@Component
public class AddBrlBalanceConsumer {

    @Autowired
    private UserService userService;

    /**
     * Add Brl Balance Listener
     * @param newBrlBalanceInfo
     * @throws UserException
     */
    @KafkaListener(topics = "${spring.crypfy.queue.topics.auth-add-brl-balance}", groupId = "auth-add-brl-balance")
    public void onAddBrlBalance(@Payload Map<String,Object> newBrlBalanceInfo) throws UserException {

        //add brl balance to target user
        userService.addAvailableBalanceBrl(newBrlBalanceInfo.get("email").toString(),new BigDecimal(newBrlBalanceInfo.get("balance").toString()));

        System.out.println("new brl balance has been updated");
    }

}
