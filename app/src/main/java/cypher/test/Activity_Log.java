package cypher.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Activity_Log extends Activity implements BlueController.setOnReadListener {
    private boolean  isThreadRunning = false,lockDown=false;
    private TextView textShow,tvTerminal;
    private EditText textedit;
    private String match_1 = "C U S T O M   M O D E";
    private BlueController blueController;
    private BluetoothSerial bluetoothSerialthis;
    private Button btn_customization,btn_calibration,btn;
    private ScrollView svTerminal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_log);
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


    //taking to the parameters page
    public void Customization(View view) {
        Write("Customize");
        nonClickable();
        new Reader().execute();
        //Toast.makeText(this, blueController.command, Toast.LENGTH_LONG).show();
    }

    //disconnecting the connected bluetooth device
    public void Disconnect(View view) {
        Intent intent = new Intent(this, Activity_Connect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//clearing backstack to open connect page
        startActivity(intent);
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();
        finish();
    }


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
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {
            while (blueController.activityLock) {
                continue;
            }
            doAction();
            return null;
        }
    }

    private void doAction() {
        String temp = blueController.command;
        Log.d("messg", blueController.command);
        if(blueController.command.contains(match_1)) {
            blueController.command = "";
            Intent intent = new Intent(this, Activity_Customization.class);
            startActivity(intent);
        }
        finish();
    }

    private void nonClickable() {
        btn_calibration.setClickable(false);
        btn_customization.setClickable(false);
    }

    @Override
    public void onReadListener(String message) {
        tvTerminal.append(message + " - ");
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
