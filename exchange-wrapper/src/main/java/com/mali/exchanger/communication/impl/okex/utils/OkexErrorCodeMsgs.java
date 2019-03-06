package com.mali.exchanger.communication.impl.okex.utils;

import org.springframework.stereotype.Service;

@Service
public class OkexErrorCodeMsgs {

    public String getCorrectMessage(Long errorCode){

        if (errorCode == 1002) return "The transaction amount exceed the balance";
        if (errorCode == 1003) return "The transaction amount is less than the minimum requirement";

        return "";

    }


}
