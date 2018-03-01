package ronny.remotemouse;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NotificationSender extends NotificationListenerService {
    private String LOCAL_IP = "192.168.0.2";
    private Socket soc;
    private PrintWriter out;
    private Thread connector;

    private void connect() {

        connector =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    soc = new Socket(LOCAL_IP, 444);
                    out = new PrintWriter(soc.getOutputStream(), true);
                    Log.d("tagy","Here");
                    new ServerConnection(soc).execute("NOTI");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        connector.start();
        new ServerConnection(soc).execute("NOTI,");
        Log.d("tag","Notiservice");

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("tag","started");
        connect();
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("tagy","nOTI SENT");
        new ServerConnection(soc).execute("NOTI,"+sbn.getTag());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

}
