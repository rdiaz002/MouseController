package ronny.remotemouse;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText  ip, port;
    private TextView stats;
    private String LOCAL_IP = "";
    private int PORT = 444;
    private Socket soc;
    private PrintWriter out;

    private Thread connector;
    private Button conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stats = findViewById(R.id.Status_Con);
        ip = findViewById(R.id.Addr_IP);
        port = findViewById(R.id.portu);
        conn = findViewById(R.id.connec);

        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soc == null) {
                    connect();
                } else {
                    try {
                        out.close();
                        soc.close();
                        out = null;
                        soc = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    connect();
                }
            }
        });


    }

    private void connect() {
        LOCAL_IP = ip.getText().toString();
        PORT = Integer.parseInt(port.getText().toString());

        connector = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    soc = new Socket(LOCAL_IP,PORT);
                    out = new PrintWriter(soc.getOutputStream(), true);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        connector.start();
        try{
            connector.join();
            new ServerConnection(soc).execute("TRACK,");
            TrackPad f1 = new TrackPad();
            if(soc!=null){
                stats.setText("Connected");
            }
            f1.setSocket(soc);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, f1).commit();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        if (connector.isAlive()) {
            connector.interrupt();
        }
        connector = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.println("bye");
                    soc.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        connector.start();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
