package nanorep.nanowidget.Utilities;

import android.net.Uri;

/**
 * Created by nissimpardo on 27/10/2016.
 */

public enum NRChannelDismissType {
    Unknown,
    Cancelled,
    Sent,
    ValidatioFailed,
    SendingFailed,
    SendGeneralError,
    Exit;

    public static NRChannelDismissType fromUrl(String url) {
        Uri uri = Uri.parse(url);
        String[] hostParams = uri.getHost().split("\\.");
        String action = hostParams[1];
        if (action.equals("cancel")) {
            return Cancelled;
        } else if (action.equals("send") && uri.getQueryParameter("result") != null) {
            return values()[Integer.parseInt(uri.getQueryParameter("result"))];
        } else if (action.equals("exit")) {
            return Exit;
        }
        return Unknown;
    }
}
