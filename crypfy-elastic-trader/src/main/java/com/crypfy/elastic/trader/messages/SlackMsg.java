package com.crypfy.elastic.trader.messages;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.stereotype.Service;

@Service
public class SlackMsg implements MessageSender {

    @Override
    public void sendMsg(String msg) {
        //SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
        SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
        api.call(new SlackMessage(msg));
    }
}
