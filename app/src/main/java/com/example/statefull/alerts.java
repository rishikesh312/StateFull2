package com.example.statefull;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class alerts extends Application {
  public static final String welcome= "Welcome to statefull";

   @Override
    public void onCreate(){
        super.onCreate();
         createremainder();

    }
    private void createremainder(){
if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

    NotificationChannel wel=new NotificationChannel(
                 welcome,
                  " Statefull",
            NotificationManager.IMPORTANCE_HIGH
           );

}

    }
}
