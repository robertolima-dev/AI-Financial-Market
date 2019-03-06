package com.mali.messages;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.stereotype.Component;

@Component
public class SlackMsg implements MessageSender {

    @Override
    public void sendMsg(String msg) {
        SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
        api.call(new SlackMessage(msg));
    }
}
