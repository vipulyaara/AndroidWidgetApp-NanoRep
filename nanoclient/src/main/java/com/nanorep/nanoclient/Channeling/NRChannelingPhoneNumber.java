package com.nanorep.nanoclient.Channeling;

import java.util.HashMap;

/**
 * Created by nissimpardo on 29/12/15.
 */
public class NRChannelingPhoneNumber extends NRChanneling {
    private String phoneNumber;
    private String customContent;

    public NRChannelingPhoneNumber(HashMap<String, Object> params) {
        super(params);
        phoneNumber = value("phoneNumber");
        customContent = value("customContent");
        this.type = NRChannelingType.PhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomContent() {
        return customContent;
    }
}
