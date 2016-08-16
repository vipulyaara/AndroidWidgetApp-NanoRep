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
        contactForms = (String)params.get("contactForms");
        ticketingInterface = (String)params.get("ticketingInterface");
        showInArticle = (Boolean) params.get("showInArticle");
        thankYouMessage = (String)params.get("thankYouMessage");
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
