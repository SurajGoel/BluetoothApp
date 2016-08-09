package cypher.test;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Connect extends Activity
        implements BluetoothDeviceListDialog.OnDeviceSelectedListener, View.OnClickListener,View.OnLongClickListener{

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private EditText editText;
    private BluetoothSerial bluetoothSerial;
    private final String match_1 = "Device Name";
    private static boolean  isThreadRunning = false;
    private String command="";

    private TextView text_connect,Testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        text_connect = (TextView) findViewById(R.id.text_connect);
        Testing = (TextView) findViewById(R.id.Testing);
        editText = (EditText) findViewById(R.id.editText);
        bluetoothSerial = ((BlueController)this.getApplicationContext()).bluetoothSerial;
    }

    //handling connection call
    public void Connect(View view) {
        showDeviceListDialog();
       // Intent intent = new Intent(Activity_Connect.this, Activity_Log.class);
      //  startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
//        bluetoothSerial.setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open a Bluetooth serial port and get ready to establish a connection
       /* if (bluetoothSerial.checkBluetooth() && bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
               // bluetoothSerial.start();
            }
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
       // bluetoothSerial.stop();
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothSerial.setup();
                }
                break;
            case 2:
                String id = data.getStringExtra("button_id");
                Intent intent = new Intent(this, Activity_Customization.class);
                switch (id) {
                    case "cal":
                        bluetoothSerial.write("Calibration Mode");
                        break;
                    case "cust":
                        bluetoothSerial.write("Customize");
                        while (isThreadRunning) {
                            continue;
                        }
                        Toast.makeText(this, "Hello there ",Toast.LENGTH_LONG).show();
                        text_connect.setText("Sending for Customization");
                        if(command.contains(match_1)) {
                            startActivity(intent);
                        }
                        else text_connect.setText(command);
                        command="";
                        break;
                    case "cancel":
                        break;
                }
                break;
            case 3:
                break;
        }
    }

    public void sendMessage(View v) {
        bluetoothSerial.write(editText.getText().toString());
        editText.setText("");
    }


    /* Implementation of BluetoothSerialListener */



    /*@Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        invalidateOptionsMenu();
        updateBluetoothState();
        ImageButton btn = (ImageButton) findViewById(R.id.blue_btn);
        btn.setClickable(false);
        btn.setVisibility(View.INVISIBLE);
        bluetoothSerial.write("Hello There Raspberry");
        Activity_Log.GetBluetoothSerial(bluetoothSerial);
      //  Parcelable log_class = Parcels.wrap(bluetoothSerial);
       // Bundle b = new Bundle();
        //b.putParcelable("forlog", log_class);
        Intent intent = new Intent(Activity_Connect.this, Activity_Log.class);
        //intent.putExtras(b);
        startActivity(intent);
        //finish();
    }


    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
      /*  tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getConnectedDeviceName(),
                message));
        command+=message;
       // if(!isThreadRunning) count.start();
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen;
        //svTerminal.post(scrollTerminalToBottom);
    }*/

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
        Intent intent = new Intent(this, Activity_Log.class);
        startActivity(intent);
        finish();
    }

    /* End of the implementation of listeners */

   /* @Override
    public void onBluetoothNotSupported() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_bluetooth)
                .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        // Display the current state on the app bar as the subtitle
        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:
                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }*/

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //bluetoothSerial.stop();
    }
}
