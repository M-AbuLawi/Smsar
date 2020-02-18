package com.yasoft.aqarkom;

import android.os.AsyncTask;

public class CheckNetworkConnection extends AsyncTask <Void,Void,Boolean> {
    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public  CheckNetworkConnection(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) {
        return true;
      }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}
