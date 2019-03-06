package com.mali.crypfy.indexmanager.integrations.slack.impl;

import com.mali.crypfy.indexmanager.integrations.slack.SlackService;
import com.mali.crypfy.indexmanager.integrations.slack.exception.SlackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class SlackServiceImpl implements SlackService {

    @Value("${spring.slack.webhook}")
    private String slackResource;

    @Async
    @Override
    public void sendMessage(String message) throws SlackException {
        try {
            URL myUrl = new URL(slackResource);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            String json = "{\"text\": \""+message+"\"}";
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(json);
            osw.flush();
            osw.close();
            //if this not return ok
            if (conn.getResponseCode() != 200) {
                throw new SlackException("Ocorreu um erro ao integrar com o slack "
                        + conn.getResponseCode());
            }
        } catch (MalformedURLException e) {
            throw new SlackException("Ocorreu um erro ao integrar com o slack ");
        } catch (IOException e) {
            throw new SlackException("Ocorreu um erro ao integrar com o slack ");
        }
    }
}
