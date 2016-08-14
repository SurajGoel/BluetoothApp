package cypher.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class Activity_Log extends Activity implements BlueController.setOnReadListener {
    private boolean  isThreadRunning = false;
    private TextView tvTerminal;
    private BlueController blueController;
    private Button btn_customization,btn_calibration,btn;
    private ScrollView svTerminal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_log);
        // Same bluetooth adapter from BlueController class.
        blueController = (BlueController) this.getApplicationContext();
        btn_calibration = (Button) findViewById(R.id.btn_calibration);
        btn_customization = (Button)  findViewById(R.id.btn_customization);
        blueController.getOnReadListener(this);
        svTerminal = (ScrollView) findViewById(R.id.svTermial);
        tvTerminal = (TextView) findViewById(R.id.tvTerminal);
    }

    //taking to calibration page
    public void Calibration(View view) {
        //nonClickable();
    }


    //When Customization is hit, sending "Customize" to connected device. This is where things should work,
    // After Successfully sending, we should see log on the space above, and Activity_Customization Activity
    // Should be automatically called.
    public void Customization(View view) {
        if(!isThreadRunning) {
            isThreadRunning=true;
            Write("Customize");
            new Reader().execute();
        }
        else Toast.makeText(this, "Already Sending Data", Toast.LENGTH_SHORT).show();
    }

    //disconnecting the connected bluetooth device
    public void Disconnect(View view) {
        Intent intent = new Intent(this, Activity_Connect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//clearing backstack to open connect page
        startActivity(intent);
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Writing through BlueController Class.
    private void Write(String message) {
        blueController.writeMessage(message);
    }

    private class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void o) {
            isThreadRunning=false;
            doAction();
            super.onPostExecute(o);
        }

        // Waiting for all the message reading untill 500ms time period ( Defined in BlueController class)
        @Override
        protected Void doInBackground(Void... params) {
            while (blueController.activityLock) {
            }
            return null;
        }
    }


    // Checking here if message stored in BluetoothController command's variable matches "C U S T..."
    // or not. If it, then shoot Customization class.
    private void doAction() {
        String match_1 = "C U S T O M   M O D E";
        Log.d("messg", blueController.command);
        if(blueController.command.contains(match_1)) {
            Intent intent = new Intent(this, Activity_Customization.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onReadListener(String message) {
        tvTerminal.append(message);
        svTerminal.post(scrollTerminalToBottom);
    }

    private final Runnable scrollTerminalToBottom = new Runnable() {
        @Override
        public void run() {
            // Scroll the terminal screen to the bottom
            svTerminal.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };

}
