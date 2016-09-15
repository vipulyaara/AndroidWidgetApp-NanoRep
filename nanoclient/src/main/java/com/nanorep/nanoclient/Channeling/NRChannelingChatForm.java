package com.nanorep.nanoclient.Channeling;

import java.util.HashMap;

/**
 * Created by nissimpardo on 29/12/15.
 */
public class NRChannelingChatForm extends NRChanneling {
    private String chatProvider;
    private String initialiseStatus;
    private String agentSkill;
    private String waitTime;
    private Boolean preChat;
    private Boolean postChat;
    private Boolean hideSendToEmail;
    private Boolean isPopup;
    private String popupSize;
    private String otherChatProviderValues;
    private String accountNum;

    public NRChannelingChatForm(HashMap<String, Object> params) {
        super(params);
        chatProvider = value("chatProvider");
        accountNum = value("accountNum");
        initialiseStatus = value("initialiseStatus");
        agentSkill = value("agentSkill");
        waitTime = value("waitTime");
        preChat = booleanValue("preChat");
        postChat = booleanValue("postChat");
        hideSendToEmail = booleanValue("hideSendToEmail");
        isPopup = booleanValue("isPopup");
        popupSize = value("popupSize");
        otherChatProviderValues = value("otherChatProviderValues");
        accountNum = value("accountNum");
        this.type = NRChannelingType.ChatForm;
    }
    
    

    public String getChatProvider() {
        return chatProvider;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getInitialiseStatus() {
        return initialiseStatus;
    }

    public String getAgentSkill() {
        return agentSkill;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public Boolean getPreChat() {
        return preChat;
    }

    public Boolean getPostChat() {
        return postChat;
    }

    public boolean getHideSendToEmail() {
        return hideSendToEmail;
    }

    public boolean getIsPopup() {
        return isPopup;
    }

    public String getPopupSize() {
        return popupSize;
    }

    public String getOtherChatProviderValues() {
        return otherChatProviderValues;
    }
}
