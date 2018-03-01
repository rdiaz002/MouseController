package ronny.remotemouse;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * Created by Ronny on 1/28/2018.
 */

public class ServerConnection extends AsyncTask{
    private Socket soc;
    private PrintWriter out;
    public ServerConnection(Socket soc) {
        this.soc=soc;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            this.out = new PrintWriter(soc.getOutputStream(),true);
            out.println(objects[0].toString().toUpperCase());
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }
    }
}


