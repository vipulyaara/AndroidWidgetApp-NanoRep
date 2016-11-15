package com.nanorep.nanoclient.Connection;




import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.nanorep.nanoclient.Handlers.NRErrorHandler;
import com.nanorep.nanoclient.Log.NRLogger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nissopa on 9/12/15.
 */
public class NRConnection {
    private static String NRStatusKey = "status";
    private static String TAG_REQUEST = "nanoRepDebugRequest";
    private static String TAG_RESPONSE = "nanoRepDebugResponse";

    private ArrayList<NRDownloader> mConnections;

    private static NRConnection mInstance;

    public interface Listener {
        void response(Object responseParam, int status, NRError error);
        void log(String tag, String msg);
    }

    private NRConnection() {
    }

    public static NRConnection getInstance() {

        if  (mInstance == null) {
            synchronized (NRConnection.class) {
                if (mInstance == null) {
                    mInstance = new NRConnection();
                }
            }
        }

        return mInstance;
    }

    public void connectionWithRequest(Uri uri, final Listener listener) {

        NRErrorHandler.getInstance().reset();

        NRDownloader downloader = new NRDownloader(new NRDownloader.NRDownloaderListener() {
            @Override
            public void downloadCompleted(NRDownloader downloader, Object data, NRError error) {


                if (listener != null) {
                    if (error != null) {
                        listener.response(null, -1, error);
                        NRErrorHandler.getInstance().handleError(error.getCode());
                    } else if (data != null) {
                        String jsonString = new String((byte[])data);

                        //log
                        listener.log(TAG_RESPONSE, jsonString);

                        Object retMap = NRUtilities.jsonStringToPropertyList(jsonString);
                        listener.response(retMap, downloader.getResponseStatus(), null);
                    }
                }
                if (mConnections != null && mConnections.contains(downloader)) {
                    mConnections.remove(downloader);
                }
            }
        });

        //log
        listener.log(TAG_REQUEST, uri.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
        } else {
            downloader.execute(uri);
        }

    }

    public void cancelAllConnections() {
        ArrayList<NRDownloader> downloaders = new ArrayList<NRDownloader>(getConnections());
        for (NRDownloader downloader: downloaders) {
            downloader.cancel(true);
            getConnections().remove(downloader);
            downloader = null;
        }
        mConnections = null;
    }

    private ArrayList<NRDownloader> getConnections() {
        synchronized (mConnections) {
            if (mConnections == null) {
                mConnections = new ArrayList<NRDownloader>();
            }
            return mConnections;
        }
    }
}
