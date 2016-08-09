package cypher.test;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_Connect extends Activity
        implements BluetoothDeviceListDialog.OnDeviceSelectedListener, View.OnClickListener,View.OnLongClickListener{

    private BlueController blueController;
    private ImageButton blue_btn;
    private TextView text_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        text_connect = (TextView) findViewById(R.id.text_connect);
        blueController = (BlueController) this.getApplicationContext();
        blue_btn = (ImageButton) findViewById(R.id.blue_btn);
    }

    //handling connection call
    public void Connect(View view) {
        blueController.bluetoothSerial.setup();
        blueController.bluetoothSerial.start();
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
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        text_connect.setText("Connecting...");
        blue_btn.setClickable(false);
        blueController.connecting = true;
        new Reader().execute();
        blueController.Connect(device);
    }

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
