package com.crypfy.api;

import com.crypfy.api.json.ArbitrageData;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class Controller {

    public static final String ACCOUNT_SID = "AC52b0ef4dd5e4f338b2a64461b2417ca3";
    public static final String AUTH_TOKEN = "3b44422bdf05eb11469c71b723d8fa6f";

    @Autowired
    ArbitrageProcessor processor;
    @Autowired
    IndexProcessor indexProcessor;

    @RequestMapping("/api/{brlAmount}/{brlForLtc}")
    public ArbitrageData getApi(@PathVariable("brlAmount") double brlAmount,@PathVariable("brlForLtc") double brlForLtc){

        return processor.getMultipliers(brlAmount,brlForLtc);
    }

    @RequestMapping("/call")
    public void getCall(){

        Twilio.init("AC52b0ef4dd5e4f338b2a64461b2417ca3", "3b44422bdf05eb11469c71b723d8fa6f");

        Call call = null;
        try {
            call = Call.creator(new PhoneNumber("+5511964240737"), new PhoneNumber("+551945603713"),
                    new URI("https://handler.twilio.com/twiml/EH5c3a0456611c51965bb85048a90a97cb")).create();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println(call.getSid());

    }

    @RequestMapping("/get-linea-index")
    public String getLinea(){
        return indexProcessor.readIndex("linea");
    }

    @RequestMapping("/get-audace-index")
    public String getAudace(){
        return indexProcessor.readIndex("audace");
    }

    @RequestMapping("/set-linea-index/{value}")
    public void setLinea(@PathVariable("value") String value){
        indexProcessor.writeIndex(value,"linea");
    }

    @RequestMapping("/set-audace-index/{value}")
    public void setAudace(@PathVariable("value") String value){
        indexProcessor.writeIndex(value,"audace");
    }



}
