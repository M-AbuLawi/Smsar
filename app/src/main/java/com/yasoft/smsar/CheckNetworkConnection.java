package com.yasoft.smsar;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CheckNetworkConnection extends AsyncTask <Void,Void,Boolean> {
    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public  CheckNetworkConnection(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) {
        return true;
      }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}
