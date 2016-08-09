package cypher.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class Activity_Customization extends Activity implements View.OnClickListener {


    private LinearLayout apn,host,bid,device,cyc,reset;
    private RelativeLayout cal;
    private TextView tv_device_name;
    private String match_device = "Enter new Device Name",match_result = "Done",setString="";
    private BlueController blueController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customization);
        tv_device_name = (TextView) findViewById(R.id.tv_device_name);
        apn = (LinearLayout) findViewById(R.id.apn);
        host = (LinearLayout) findViewById(R.id.host);
        bid = (LinearLayout) findViewById(R.id.bid);
        device = (LinearLayout) findViewById(R.id.device);
        cal = (RelativeLayout) findViewById(R.id.cal);
        cyc = (LinearLayout) findViewById(R.id.cyc);
        reset = (LinearLayout) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        apn.setOnClickListener(this);
        host.setOnClickListener(this);
        bid.setOnClickListener(this);
        device.setOnClickListener(this);
        cal.setOnClickListener(this);
        cyc.setOnClickListener(this);
        blueController = (BlueController)this.getApplicationContext();
    }

    //back to the logs page
    public void Overview(View view){
        Intent intent=new Intent(this,Activity_Log.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device:
                Write("Device Name");
                new Reader().execute();
                break;
        }
    }

    private void ShowDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        alert.setMessage("Please Set the Device Name");
        alert.setTitle("Note");
        alert.setView(editText);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Write(setString=editText.getText().toString());
                new Reader().execute();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    //disconnecting the connected bluetooth device
    public void Disconnect(View view){
        Intent intent=new Intent(this,Activity_Connect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//clearing backstack to open connect page
        startActivity(intent);
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();
        finish();
    }

    private class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void o) {
            doAction();
            super.onPostExecute(o);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (blueController.activityLock) {
                continue;
            }
            return null;
        }
    }

    private void doAction() {
        if(blueController.command.contains(match_device)) {
            blueController.command = "";
            ShowDialog();
        }
        else if(blueController.command.contains(match_result)) {
            blueController.command = "";
            Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
            tv_device_name.setText(setString);
        }
    }

    private void Write(String message) {
        blueController.writeMessage(message);
    }
}
