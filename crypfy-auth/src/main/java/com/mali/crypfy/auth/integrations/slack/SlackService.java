package com.mali.crypfy.auth.integrations.slack;

import com.mali.crypfy.auth.integrations.slack.exception.SlackException;

public interface SlackService {
    public void sendMessage(String message) throws SlackException;
}
