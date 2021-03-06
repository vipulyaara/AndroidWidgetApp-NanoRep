package com.nanorep.nanoclient.Channeling;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.HashMap;


/**
 * Created by nissimpardo on 29/12/15.
 */
public class NRChanneling {
    protected String buttonText;
    protected String channelDescription;
    protected NRChannelingType type;
    protected NRQueryResult mQueryResult;
    protected HashMap<String, Object> mParams;

    public enum NRChannelingType {
        PhoneNumber,
        OpenCustomURL,
        CustomScript,
        ContactForm,
        ChatForm
    }

    public NRChanneling(HashMap <String, ?> params) {
        mParams = (HashMap<String, Object>) params;
        buttonText = value("buttonText");
        channelDescription = value("description");
    }

    protected String value(String key) {
        if (key != null) {
            return (String) mParams.get(key);
        }
        return null;
    }

    protected Boolean booleanValue(String key) {
        if (key != null) {
            return (Boolean) mParams.get(key);
        }
        return null;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public NRChannelingType getType() {
        return type;
    }

    public void setQueryResult(NRQueryResult queryResult) {
        mQueryResult = queryResult;
    }

    public NRQueryResult getQueryResult() {
        return mQueryResult;
    }

    public static NRChanneling channelForParams(HashMap <String, Object> params) {
        int actionEsc = Integer.parseInt((String)params.get("actionEsc"));
        switch (actionEsc) {
            case 1:
                return new NRChannelingOpenCustomURL(params);
            case 2:
                return new NRChannelingCustomScript(params);
            case 0:
                switch (Integer.parseInt((String)params.get("channel"))) {
                    case 5:
                        return new NRChannelingPhoneNumber(params);
                    case 3:
                        return new NRChannelingChatForm(params);
                    case 1:
                        return new NRChannelingContactForm(params);
                }
        }
        return null;
    }
}
