package com.mali.crypfy.indexmanager.integrations.slack;

import com.mali.crypfy.indexmanager.integrations.slack.exception.SlackException;

public interface SlackService {
    public void sendMessage(String message) throws SlackException;
}
