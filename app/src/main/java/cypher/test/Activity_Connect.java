package cypher.test;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class Activity_Connect extends AppCompatActivity
        implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener, View.OnClickListener,View.OnLongClickListener{

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private static BluetoothSerial bluetoothSerial;

    private ScrollView svTerminal;
    private TextView text_connect,Testing;

    private boolean crlf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        text_connect = (TextView) findViewById(R.id.text_connect);
        Testing = (TextView) findViewById(R.id.Testing);
        bluetoothSerial = new BluetoothSerial(this, this);
    }

    public static void ActionStatic(String text) {
        bluetoothSerial.write(text);
    }

    public static String ProvideString() {
        return command;
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
        bluetoothSerial.setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open a Bluetooth serial port and get ready to establish a connection
        if (bluetoothSerial.checkBluetooth() && bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
                bluetoothSerial.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
        bluetoothSerial.stop();
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
                        text_connect.setText("Sending for Customization");
                        //startActivity(intent);
                        break;
                    case "cancel":

                        break;
                }
                break;
            case 3:


                break;
        }
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
    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    /* Implementation of BluetoothSerialListener */

    @Override
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

    @Override
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
        Intent intent = new Intent(this, Activity_Customization.class);
        startActivity(intent);
       /* Intent intent = new Intent(Activity_Connect.this, Activity_Log.class);
        Bundle b = new Bundle();
        intent.putExtra("blueObject", bluetoothSerial);
        startActivityForResult(intent, 2);*/
    }

    private boolean isFirstMess=true;
    private int commandNumber = 1;
    private long currTime,timeout=0;
    private boolean isThreadRunning = false;
    private static String command="";
    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
      /*  tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getConnectedDeviceName(),
                message));*/
        timeout=System.currentTimeMillis()+250;
        command+=message;
        if(isThreadRunning == false) countDown.start();

       /* char[] mess = message.toCharArray();
        for(int index=0 ; index<mess.length ; index++) {
            if(Character.getNumericValue(mess[index]) == commandNumber) {
                addButtontoGUI(command);
                commandNumber++;
                command="";
            }
            else command+=mess[index];
        }
        svTerminal.post(scrollTerminalToBottom);*/
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen;
        //svTerminal.post(scrollTerminalToBottom);
    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

    /* End of the implementation of listeners */

    private final Runnable scrollTerminalToBottom = new Runnable() {
        @Override
        public void run() {
            // Scroll the terminal screen to the bottom
            svTerminal.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };

    private final Thread countDown = new Thread(new Runnable() {
        @Override
        public void run() {
            isThreadRunning=true;
            while (System.currentTimeMillis()<timeout) {
            }
            isThreadRunning = false;
            //addtoGUI();
            //ProvideString();
        }
    });

    private String match = "\n\n\t C U S T O M    M O D E \n\n";
    private void addtoGUI() {
        String temp = command;
        command="";
        Testing.setText(temp);
        String test = temp.substring(0,30);
        if(test.equals(match)) {
            Intent intent = new Intent(this,Activity_Customization.class);
            startActivity(intent);
        }
        /*char[] array = temp.toCharArray();
        boolean onePassed=false;
        String line="";
        for(int i=0 ; i<array.length ; i++) {

            if(array[i] == '\n') {
                if(onePassed==false) onePassed=true;
                else {
                    addButtonToGUI(line);
                    line="";
                }
                continue;
            }
            line+=array[i];
        }*/
    }

    private int idNumber=0;
    private void addButtonToGUI(String line) {
        Button dynamicBtn = new Button(this);
        dynamicBtn.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        dynamicBtn.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        dynamicBtn.setId(idNumber);
        dynamicBtn.setOnClickListener(this);
    }


}
