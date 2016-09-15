package com.nanorep.nanoclient.Channeling;

import java.util.HashMap;

/**
 * Created by nissimpardo on 29/12/15.
 */
public class NRChannelingContactForm extends NRChanneling {

    private String contactForms;
    private String ticketingInterface;
    private Boolean showInArticle;
    private String thankYouMessage;

    public NRChannelingContactForm(HashMap<String, Object> params) {
        super(params);
        contactForms = value("contactForms");
        ticketingInterface = value("ticketingInterface");
        showInArticle = booleanValue("showInArticle");
        thankYouMessage = value("thankYouMessage");
        this.type = NRChannelingType.ContactForm;
    }

    public String getContactForms() {
        return contactForms;
    }

    public String getTicketingInterface() {
        return ticketingInterface;
    }

    public Boolean getShowInArticle() {
        return showInArticle;
    }

    public String getThankYouMessage() {
        return thankYouMessage;
    }
}
