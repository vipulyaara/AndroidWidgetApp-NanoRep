package com.nanorep.nanoclient.Channeling;

import java.util.HashMap;

/**
 * Created by nissimpardo on 29/12/15.
 */
public class NRChannelingCustomScript extends NRChanneling {
    private String scriptContent;
    private NRChanneling mChanneling;

    public NRChannelingCustomScript(HashMap<String, Object> params) {
        super(params);
        scriptContent = value("scriptContent");
        this.type = NRChannelingType.CustomScript;
    }

    public String getScriptContent() {
        return scriptContent;
    }
}
