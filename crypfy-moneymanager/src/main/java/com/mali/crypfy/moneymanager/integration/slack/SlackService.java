package com.mali.crypfy.moneymanager.integration.slack;

import com.mali.crypfy.moneymanager.integration.slack.exception.SlackException;

/**
 * Slack Service Interface
 */
public interface SlackService {

    /**
     * Send a simple message to a configured slack channel
     * @param message
     * @throws SlackException
     */
    public void sendMessage(String message) throws SlackException;
}
