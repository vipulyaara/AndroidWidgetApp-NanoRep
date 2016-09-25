package com.nanorep.nanoclient.Response;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nissopa on 9/13/15.
 */
public class NRConfiguration {
    private NRFAQData mFaqData;

    private HashMap<String, Object> mParams;
    private boolean mIsContextDependent = false;
    private NRTitle title;

    public NRConfiguration() {
        mParams = new HashMap<String, Object>();
    }


    /**
     * Converts the response JSON into NRConfiguration object
     *
     * @param HashMap contains all of the params from the JSON
     */
    public NRConfiguration(HashMap<String, Object> params) {
        mParams = params;
        if (params != null) {
            Object faq = mParams.get("faqData");
            if (faq != null && faq instanceof String) {
                mIsContextDependent = true;
            }
        }
    }

    public class NRTitle {

        public void setTitleBGColor(String titleBGColor) {
            mParams.put("titleBGColor", titleBGColor);
        }

        public String getTitleBGColor() {
            return (String)mParams.get("titleBGColor");
        }
    }

    public NRTitle getTitle(){
        if(title == null) {
            title = new NRTitle();
        }
        return title;
    }

    public class NRSearchBar {
        //initialText
        //voiceEnabled

        public void setInitialText(String initialText) {
            mParams.put("initialText", initialText);
        }

        public String getInitialText() {
            return (String)mParams.get("initialText");
        }

        public void setVoiceEnabled(String voiceEnabled) {
            mParams.put("voiceEnabled", voiceEnabled);
        }

        public String getVoiceEnabled() {
            return (String)mParams.get("voiceEnabled");
        }
    }

    public class NRAutoComplete {

        public void setChatConfiguration(String chatConfiguration) {
            mParams.put("chatConfiguration", chatConfiguration);
        }

        public String getChatConfiguration() {
            return (String)mParams.get("chatConfiguration");
        }
    }

    public class NRContent {

    }

    public class NRLike {

    }

    public class NRChanneling {

    }

    public class NRLogo {
        //hideBranding
        public void setHideBranding(String hideBranding) {
            mParams.put("hideBranding", hideBranding);
        }

        public String getHideBranding() {
            return (String)mParams.get("hideBranding");
        }
    }


    public String getCnfId() {
        return (String)mParams.get("id");
    }

    public String getKbId() {
        return (String)mParams.get("kbId");
    }

    public String getCacheVar() {
        return (String)mParams.get("cacheVar");
    }

    public String getKbLanguageCode() {
        return (String)mParams.get("kbLanguageCode");
    }

    //autocompleteEnabled
    //skinName

    public void setAutocompleteEnabled(String autocompleteEnabled) {
        mParams.put("autocompleteEnabled", autocompleteEnabled);
    }

    public String getAutocompleteEnabled() {
        return (String)mParams.get("autocompleteEnabled");
    }

    public void setSkinName(String skinName) {
        mParams.put("skinName", skinName);
    }

    public String getSkinName() {
        return (String)mParams.get("skinName");
    }

    //titleNormalText

    public void setTitleNormalText(String titleNormalText) {
        mParams.put("titleNormalText", titleNormalText);
    }

    public String getTitleNormalText() {
        return (String)mParams.get("titleNormalText");
    }


    public HashMap<String, Object> getmParams() {
        return mParams;
    }

    public void overrideCnfData(NRConfiguration cnf) {
        for (String key: cnf.mParams.keySet()) {
            mParams.put(key, cnf.mParams.get(key));
        }
    }

    public boolean getIsContextDependent() {
        return mIsContextDependent;
    }

    public String getCustomNoAnswersTextContext(String context) {
        String text = (String)mParams.get("customNoAnswersTextContext");
        if (text == null) {
            text = "No results for '{CONTEXT}'. Try a different phrasing or ask an agent.";
        }
        return  text.replace("{CONTEXT}", context);
    }

    public void setFaqData(ArrayList<HashMap<String, Object>> faqList) {
        mParams.put("faqData", faqList);
        mIsContextDependent = false;
    }

    public NRFAQData getFaqData() {
        if (mIsContextDependent) {
            return null;
        }
        if (mFaqData == null) {
            if (mParams == null) {
                return null;
            }
            ArrayList faq = (ArrayList)mParams.get("faqData");
            if (faq == null) {
                return null;
            }
            if (faq.get(0) == null) {
                return null;
            }
            mFaqData = new NRFAQData(faq);
        }
        return mFaqData;
    }

}
