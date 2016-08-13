package cypher.test;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_Connect extends Activity
        implements BluetoothDeviceListDialog.OnDeviceSelectedListener, View.OnClickListener,View.OnLongClickListener{

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BlueController blueController;
    private ImageButton blue_btn;
    private TextView text_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        text_connect = (TextView) findViewById(R.id.text_connect);
        // Global Application variable to access bluetooth among all classes, all the traffic is handled through BlueController class.
        blueController = (BlueController) this.getApplicationContext();
        blue_btn = (ImageButton) findViewById(R.id.blue_btn);// Button to Connect to particular device.
    }

    //Checking if Bluetooth is Supported, Available but off or on.
    public void Connect(View view) {
        if(!blueController.bluetoothSerial.checkState()) {
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
        if(!blueController.bluetoothSerial.checkAvailability()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
        }
        else {
            blueController.bluetoothSerial.setup();
            blueController.bluetoothSerial.start();
            showDeviceListDialog();
        }
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
    // Activate Bluetooth in device activity.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    blueController.bluetoothSerial.setup();
                    blueController.bluetoothSerial.start();
                    showDeviceListDialog();
                }
                break;
        }
    }

    @Override
    // After selecting bluetooth device calling connect to that device in an AsyncTask define below.
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        text_connect.setText("Connecting...");
        blue_btn.setClickable(false);
        blueController.connecting = true;
        new Reader().execute();
        blueController.Connect(device);
    }

    // Dialog box to select device, and connect after selecting
    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(blueController.bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //bluetoothSerial.stop();
    }

    // Thread to connect to bluetooth and Redirect to Activity_Log class after successful connection.
    // Currently not doing anything if not able to connect.
    private class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            doAction();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Actual connection taking place in BlueController class, and waiting here before that.
            while (blueController.connecting) {

            }
            return null;
        }
    }

    private void doAction () {
        Intent intent = new Intent(this, Activity_Log.class);
        startActivity(intent);
        finish();
    }
}
