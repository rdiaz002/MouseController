package ronny.remotemouse;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import java.net.Socket;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackPad extends Fragment implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private Socket soc;
    private GestureDetector mGesture;
    private VelocityTracker mVel;

    public TrackPad() {
        // Required empty public constructor
    }

    public void setSocket(Socket soc) {
        this.soc = soc;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = (View) inflater.inflate(R.layout.fragment_track_pad, container, false);
        mGesture = new GestureDetector(root.getContext(), this);
        mGesture.setOnDoubleTapListener(this);
        mGesture.setIsLongpressEnabled(false);
        Log.d("TAG","Movement");
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGesture.onTouchEvent(motionEvent);
                return true;
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        new ServerConnection(soc).execute("bye");
        super.onDestroy();

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        if (mVel == null) {
            mVel = VelocityTracker.obtain();
        } else {
            mVel.clear();
        }
        mVel.addMovement(motionEvent);
        mVel.computeCurrentVelocity(10);
        new ServerConnection(soc).execute("UP");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        mVel.addMovement(motionEvent);
        mVel.computeCurrentVelocity(10);
        new ServerConnection(soc).execute("MOTION," + ((int) -v) + "," + ((int) -v1));
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        new ServerConnection(soc).execute("LEFT_CLICK");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        new ServerConnection(soc).execute("D_CLICK");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:

                mVel.addMovement(motionEvent);
                mVel.computeCurrentVelocity(10);
                new ServerConnection(soc).execute("MOTION," + ((int) mVel.getXVelocity()) + "," + ((int) mVel.getYVelocity()));
                mVel.clear();
                return true;
            case MotionEvent.ACTION_UP:

                new ServerConnection(soc).execute("D_CLICK_UP");
                mVel.clear();
                return true;
            default:

                break;
        }
        return true;
    }

}
