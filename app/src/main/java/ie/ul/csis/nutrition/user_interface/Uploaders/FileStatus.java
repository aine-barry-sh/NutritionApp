package ie.ul.csis.nutrition.user_interface.Uploaders;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by ruppe on 10/07/2016.
 * Modified by Aine
 */

public class FileStatus extends Observable implements Runnable {

    private List<Observer> observerList;
    private Context context;
    private AtomicBoolean doIt = new AtomicBoolean(true);
    private int time;
    //max time 1 hour
    private final int maxTime = 3600000;

    public FileStatus(Context context) {
        this.context = context;
        observerList = new ArrayList<Observer>();
        //one minute
        time = 60000;
         }

    public void addObserver(Observer myObserver) {
        observerList.add(myObserver);
    }
    public void detachObserver(Observer o) { observerList.remove(o); }

    public static boolean isFilePresent(Context context) {

        boolean haveFile = true;

        File file = (File) context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files.length == 0) {
                //directory is empty
                haveFile = false;
            }
        }

        return haveFile;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);



        while (true) {
            Log.d("FileStatuss", "Checking if present: " + time);
            if (isFilePresent(context)){
                Log.d("FileStatus", "Folder is not empty, files are existing");
                resetTime();
                updateObserver();
            }
            else{
                Log.d("FileStatus", "Folder is empty, no files");
                doIt.set(false);
                time = time +  (60000*5) ;
                if (time >= maxTime) {
                    time = maxTime;
                }

            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Log.d("FileStatus", "FileStatus is interupted");
                e.printStackTrace();
            }

        }
    }
    public void update(Observable o, Object arg) {

        Log.d("ConnectivityObserver", "File checked, Uploading Points!");


    }

    public void updateObserver() {
        for (Observer o: observerList) {
            o.update(this, this);
        }
    }


    public void setTimeChecker(int time) {
        if ( time <=0 ) {
            this.time = 60000;
            return;
        }
        this.time = time;
    }

    public void resetTime() {
        this.time = 60000;
    }


}
