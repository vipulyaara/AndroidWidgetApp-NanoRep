package nanorep.nanowidget.Components.ChannelPresenters;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Channeling.NRChannelingCustomScript;
import com.nanorep.nanoclient.Connection.NRUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by noat on 16/11/2016.
 */

public class NRCustomScriptChannelPresentor implements NRChannelPresentor {
    private Context mContext;
    private NRChanneling mChanneling;
    private String to;
    private String subject;

    public NRCustomScriptChannelPresentor(Context context) {
        mContext = context;
    }

    @Override
    public void present() {

        parseJson();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            mContext.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJson() {
        String jsonString = ((NRChannelingCustomScript) mChanneling).getScriptContent();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = NRUtilities.mapFromJson(jsonObject);

        subject = (String) map.get("title");
        to = (String) map.get("url");
    }

    @Override
    public void setChannel(NRChanneling channeling) {
        mChanneling = channeling;
    }

    @Override
    public NRResult getResult() {
        return null;
    }

    @Override
    public String getUrl() {
        return ((NRChannelingCustomScript)mChanneling).getScriptContent();
    }
}
