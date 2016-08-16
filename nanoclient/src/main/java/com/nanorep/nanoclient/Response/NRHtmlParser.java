package com.nanorep.nanoclient.Response;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by nissimpardo on 15/08/2016.
 */

public class NRHtmlParser {
    private String mHtmlString;

    public NRHtmlParser(String htmlString) {
        mHtmlString = htmlString;
    }

    public String getParsedHtml() {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(mHtmlString));
            parser.setFeature(Xml.FEATURE_RELAXED, true);
            return parseHtml(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseHtml(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("a") && getValue("nanoreplinkid", parser) != null) {
                            String idStr = "nanorep://id/" + getValue("nanoreplinkid", parser);
                            mHtmlString.replace("javascript:void(0", idStr);
                            Log.d("test", getValue("nanoreplinkid", parser));
                            return mHtmlString;
                        }
                        break;
                }
                eventType = parser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHtmlString;
    }

    private String getValue(String attribute, XmlPullParser parser) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals(attribute)) {
                return parser.getAttributeValue(i);
            }
        }
        return null;
    }
}
