package cypher.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
    private String match_device = "\n\nEnter the new Device Name";
    private String confirm = "\nDone\n";
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
                Activity_Connect.ActionStatic("Device Name");
                // VERY BAD PRACTICE, NEED TO IMPLEMENT A THREAD METHOD FOR THIS
                try {
                    Thread.sleep(500);
                } catch (Exception e) {

                }
                String new_command = Activity_Connect.ProvideString();
                if(new_command.equals(match_device)) {
                    ShowDialog(device);
                }
                else Toast.makeText(this, "Could not convey message correctly", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public boolean ShowDialog(View v) {
        switch (v.getId()) {

            case R.id.device:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText editText = new EditText(this);
                alert.setMessage("Please Set the Device Name");
                alert.setTitle("Note");
                alert.setView(editText);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TextView text = (TextView) device.findViewById(R.id.tv_device_name);
                        text.setText(editText.getText().toString());
                        Activity_Connect.ActionStatic(editText.getText().toString());
                        // VERY BAD PRACTICE, NEED TO IMPLEMENT A THREAD METHOD FOR THIS
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {

                        }
                        String new_command = Activity_Connect.ProvideString(),temp="";
                        if(new_command.equals(confirm)) temp = "Successful";
                        else temp = "UnSuccessful";
                        Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });
                alert.show();
        }
        return true;
    }

    //disconnecting the connected bluetooth device
    public void Disconnect(View view){
        Intent intent=new Intent(this,Activity_Connect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//clearing backstack to open connect page
        startActivity(intent);
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();
        finish();
    }
}
