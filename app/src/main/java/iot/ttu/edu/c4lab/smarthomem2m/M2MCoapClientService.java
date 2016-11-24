package iot.ttu.edu.c4lab.smarthomem2m;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by jhaowei on 2016-08-16.
 */
public class M2MCoapClientService extends Service {
    public static LoopTask loopTask = null;
    public final static int serviceId = 1;
    public final static String SERVICE_CONTINUE = "0";
    public final static String SERVICE_PAUSE = "1";
    public final static String SERVICE_STOP = "2";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 判斷要停止或執行SmartM2M
        Boolean isStop = intent.getBooleanExtra("isStop", false);
        Log.d("MainService", "service onStartCommand");

        if (isStop) {
            if (loopTask != null)
                loopTask.stop();

            loopTask = null;

            Log.d("MainService", "onStartCommand isStop");
        } else {
            M2MCoapClient.observeSensors();
            if (loopTask != null)
                loopTask.stop();

            loopTask = null;
            loopTask = new LoopTask();
            loopTask.execute();

            Log.d("MainService", "onStartCommand not isStop");
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class LoopTask extends AsyncTask<Void, Void, Void> {
        private boolean isRunning = false;

        public LoopTask() {
            restartTask();
        }

        public void stop() {
            isRunning = false;
            M2MCoapClient.checkRulesThread.stopIt();
        }

        public void restartTask() {
            M2MCoapClient.checkRulesThread.stopIt();
            M2MCoapClient.checkRulesThread = new M2MCoapClient.CheckRulesThread();
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            isRunning = true;
            M2MCoapClient.checkRulesThread.run();
            return null;
        }
    }
}
