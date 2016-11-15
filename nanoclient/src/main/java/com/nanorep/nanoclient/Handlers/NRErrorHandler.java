package com.nanorep.nanoclient.Handlers;

import com.nanorep.nanoclient.Connection.NRDownloader;
import com.nanorep.nanoclient.Connection.NRError;

/**
 * Created by noat on 15/11/2016.
 */

public class NRErrorHandler {

    private static NRErrorHandler instance;



    public enum ErrorType {
        TIMEOUT_UPPER_LINE
    }

    private Listener listener;

    public interface Listener {
        void show(ErrorType errorType);
        void dismiss();
    }

    public static NRErrorHandler getInstance() {
        if(instance == null) {
            synchronized (NRErrorHandler.class) {
                if(instance == null) {
                    instance = new NRErrorHandler();
                }
            }
        }
        return instance;
    }

    public void handleError(int code) {

        if(listener == null) {
            return;
        }

        switch (code) {
            case NRDownloader.TIMEOUT: // timeout
                listener.show(ErrorType.TIMEOUT_UPPER_LINE);
                break;
        }
    }

    public void reset() {

        if(listener == null) {
            return;
        }

        listener.dismiss();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
