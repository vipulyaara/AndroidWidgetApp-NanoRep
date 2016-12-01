package com.nanorep.nanoclient.Response;

import org.json.JSONObject;

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
    private NRAutoComplete autoComplete;
    private NRSearchBar searchBar;
    private NRFaq faq;
    private NRContent content;
    private HashMap<String, String> customization;


    public NRConfiguration() {
        mParams = new HashMap<String, Object>();
        customization = new HashMap<String, String>();
    }


    /**
     * Converts the response JSON into NRConfiguration object
     *
     */
    public NRConfiguration(HashMap<String, Object> params) {
        mParams = params;

        if (params != null) {

            if(params.get("customization") != null) {
                customization = (HashMap<String, String>) params.get("customization");
            }

            if(customization == null) {
                customization = new HashMap<String, String>();
            }

            Object faq = mParams.get("faqData");
            if (faq != null && faq instanceof String) {
                if("context-dependent".toLowerCase().equals(((String) faq).toLowerCase())) {
                    mIsContextDependent = true;
                }
            } else if(faq == null) {
                mIsContextDependent = true;
            }
        }
    }

    public class NRTitle{

        private String titleRowHeight = "45";

        public NRTitle(NRTitle title) {
            this.titleRowHeight = title.getTitleRowHeight();
        }

        public NRTitle() {

        }

        public String getTitleRowHeight() {
            return titleRowHeight;
        }

        public void setTitleRowHeight(String titleRowHeight) {
            this.titleRowHeight = titleRowHeight;
        }

        public void setTitleBGColor(String titleBGColor) {
            mParams.put("titleBGColor", titleBGColor);
        }

        public String getTitleBGColor() {
            return (String)mParams.get("titleBGColor");
        }

        public void setTitle(String title) {
            mParams.put("titleNormalText", title);
        }

        public String getTitle() {

            return (String)mParams.get("titleNormalText");
        }

        public void setTitleFont(String titleFont) {
            customization.put("titleFont", titleFont);
        }

        public String getTitleFont() {
            return (String)customization.get("titleFont");
        }

        public void setTitleColor(String titleColor) {
            mParams.put("titleColor", titleColor);
        }

        public String getTitleColor() {
            return (String)mParams.get("titleColor");
        }

    }

    public class NRFaq{
        // "mobile.faqTextColor"

        public void setFaqTextColor(String faqTextColor) {
            mParams.put("mobile.faqTextColor", faqTextColor);
        }

        public String getFaqTextColor() {
            return (String)mParams.get("mobile.faqTextColor");
        }


        // "mobile.faqTextFont"

        public void setFaqTextFont(String faqTextFont) {
            mParams.put("mobile.faqTextFont", faqTextFont);
        }

        public String getFaqTextFont() {
            return (String)mParams.get("mobile.faqTextFont");
        }

        // "mobile.faqBackgroundColor"

        public void setFaqBackgroundColor(String faqBackgroundColor) {
            mParams.put("mobile.faqBackgroundColor", faqBackgroundColor);
        }

        public String getFaqBackgroundColor() {
//            return (String)mParams.get("mobile.faqBackgroundColor");
            return "#636161";
        }

    }

    public NRFaq getFaq() {
        if(faq == null) {
            faq = new NRFaq();
        }
        return faq;
    }

    public NRContent getContent() {
        if(content == null) {
            content = new NRContent();
        }
        return content;
    }


    public NRTitle getTitle(){
        if(title == null) {
            title = new NRTitle();
        }
        return title;
    }

    public NRAutoComplete getAutoComplete(){
        if(autoComplete == null) {
            autoComplete = new NRAutoComplete();
        }
        return autoComplete;
    }

    public class NRSearchBar{
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

    public NRSearchBar getSearchBar(){
        if(searchBar == null) {
            searchBar = new NRSearchBar();
        }
        return searchBar;
    }

    public class NRAutoComplete {

        private Integer suggestionRowHeight;
        private boolean dividerVisible;
        private Integer maxLines;

        public NRAutoComplete(NRAutoComplete autoComplete) {
            this.suggestionRowHeight = autoComplete.getSuggestionRowHeight();
            this.dividerVisible = autoComplete.isDividerVisible();
            this.maxLines = autoComplete.getMaxLines();
        }

        public NRAutoComplete() {

        }

        public boolean isDividerVisible() {
            return dividerVisible;
        }

        public void setDividerVisible(boolean dividerVisible) {
            this.dividerVisible = dividerVisible;
        }

        public Integer getMaxLines() {
            return maxLines;
        }

        public void setMaxLines(Integer maxLines) {
            this.maxLines = maxLines;
        }

        public void setChatConfiguration(String chatConfiguration) {
            mParams.put("chatConfiguration", chatConfiguration);
        }

        public String getChatConfiguration() {
            return (String)mParams.get("chatConfiguration");
        }

        public Integer getSuggestionRowHeight() {
            if(suggestionRowHeight == null){
                return 40;
            }
            return suggestionRowHeight;
        }

        public void setSuggestionRowHeight(Integer suggestionRowHeight) {
            this.suggestionRowHeight = suggestionRowHeight;
        }
    }

    public class NRContent {

        String marginTop;
        String marginRight;
        String marginLeft;
        String marginBottom;

        public NRContent(NRContent content) {
            this.marginTop = content.getContentMarginTop();
            this.marginRight = content.getContentMarginRight();
            this.marginLeft = content.getContentMarginLeft();
            this.marginBottom = content.getContentMarginBottom();
        }

        public NRContent() {

        }

        // "mobile.noResultsMessage"
        public void setNoResultsMessage(String noResultsMessage) {
            mParams.put("mobile.noResultsMessage", noResultsMessage);
        }

        public String getNoResultsMessage() {
            return (String)mParams.get("mobile.noResultsMessage");
        }

        // "mobile.answerTextColor"

        public void setAnswerTextColor(String answerTextColor) {
            mParams.put("mobile.answerTextColor", answerTextColor);
        }

        public String getAnswerTextColor() {
            return (String)mParams.get("mobile.answerTextColor");
        }

        // "mobile.answerTextFont"

        public void setAnswerTextFont(String answerTextFont) {
            mParams.put("mobile.answerTextFont", answerTextFont);
        }

        public String getAnswerTextFont() {
            return (String)mParams.get("mobile.answerTextFont");
        }

        // "mobile.answerTitleColor"

        public void setAnswerTitleColor(String answerTitleColor) {
            customization.put("answerTitleColor", answerTitleColor);
        }

        public String getAnswerTitleColor() {
            return (String)customization.get("answerTitleColor");
        }

        // "mobile.answerTitleFont"

        public void setAnswerTitleFont(String answerTitleFont) {
            customization.put("answerTitleFont", answerTitleFont);
        }

        public String getAnswerTitleFont() {
            return (String)customization.get("answerTitleFont");
        }

        // "mobile.widgetBackgroundColor"

        public void setWidgetBackgroundColor(String widgetBackgroundColor) {
            mParams.put("mobile.widgetBackgroundColor", widgetBackgroundColor);
        }

        public String getWidgetBackgroundColor() {
            return (String)mParams.get("mobile.widgetBackgroundColor");
        }

        public void setContentMarginTop(String marginTop) {
            this.marginTop = marginTop;
        }
        public void setContentMarginBottom(String marginBottom) {
            this.marginBottom = marginBottom;
        }
        public void setContentMarginRight(String marginRight) {
            this.marginRight = marginRight;
        }
        public void setContentMarginLeft(String marginLeft) {
            this.marginLeft = marginLeft;
        }

        public String getContentMarginTop() {
            if(marginTop == null){
                return "15";
            }
            return marginTop;
        }
        public String getContentMarginBottom() {
            return marginBottom;
        }
        public String getContentMarginRight() {
            return marginRight;
        }
        public String getContentMarginLeft() {
            return marginLeft;
        }
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

    public boolean getAutocompleteEnabled() {
        return (Boolean) mParams.get("autocompleteEnabled");
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

        for (String key: cnf.customization.keySet()) {
            customization.put(key, cnf.customization.get(key));
        }

        mIsContextDependent = cnf.mIsContextDependent;

        this.content = new NRContent(cnf.getContent());
        this.title = new NRTitle(cnf.getTitle());
        this.autoComplete = new NRAutoComplete(cnf.getAutoComplete());
    }

    public boolean getIsContextDependent() {
        return mIsContextDependent;
    }

    public String getCustomNoAnswersTextContext(String context) {
        String text = (String)mParams.get("customNoAnswersTextContext");
        if (text == null) {
            text = "We couldn't find any results matching '{CONTEXT}'. Please try rephrasing your request.";
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
