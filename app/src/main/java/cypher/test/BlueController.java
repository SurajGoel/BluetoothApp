package cypher.test;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class BlueController extends Application implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener {


    public interface setOnReadListener {
        public void onReadListener(String message);
    }

    private setOnReadListener mListener;
    public BluetoothSerial bluetoothSerial;
    public static String command="";
    public boolean activityLock=false;
    private boolean countStarted=false;
    public boolean connecting = false;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("started", " Bluetooth Application is started/");
        bluetoothSerial = new BluetoothSerial(this, this);
        bluetoothSerial.setup();
        bluetoothSerial.start();
    }

    public void getOnReadListener (BlueController.setOnReadListener listener) {
        this.mListener = listener;
    }

    public void writeMessage(String message) {
        activityLock=true;
        bluetoothSerial.write(message);
    }

    public void Connect(BluetoothDevice device) {
        bluetoothSerial.connect(device);
        connecting=true;
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        updateBluetoothState();
        connecting=false;
        Log.d("voila","voila");
    }


    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
      /*  tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getConnectedDeviceName(),
                message));*/
        mListener.onReadListener(message);
        command+=message;
        if(!countStarted) {
            countStarted=true;
            count.start();
        }
        // if(!isThreadRunning) count.start();
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen;
        //svTerminal.post(scrollTerminalToBottom);
        //count.start();
    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

    /* End of the implementation of listeners */

    @Override
    public void onBluetoothNotSupported() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_bluetooth)
                .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        updateBluetoothState();
    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
       /* final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;*/

    }

    private CountDownTimer count = new CountDownTimer(1000, 50) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.d("messageis", "The message is + " + command);
        }

        @Override
        public void onFinish() {
            activityLock=false;
            countStarted=false;
        }
    };

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }
}
