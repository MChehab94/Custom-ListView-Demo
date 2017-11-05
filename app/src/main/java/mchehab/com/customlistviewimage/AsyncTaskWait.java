package mchehab.com.customlistviewimage;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.ref.WeakReference;

/**
 * Created by muhammadchehab on 11/5/17.
 */

public class AsyncTaskWait extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> context;

    public AsyncTaskWait(WeakReference<Context> context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void...params){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void nothing){
        Intent intent = new Intent("result");
        LocalBroadcastManager.getInstance(context.get().getApplicationContext()).sendBroadcast
                (intent);
    }
}
