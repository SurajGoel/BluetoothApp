package cypher.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Activity_Calibration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calibration);
    }

    //back to parameters page
    public void Customization(View view){
        Intent intent=new Intent(this,Activity_Customization.class);
        startActivity(intent);
    }


    //disconnecting the connected bluetooth device
    public void Disconnect(View view){
        Intent intent=new Intent(this,Activity_Connect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //clearing backstack to open connect page
        startActivity(intent);
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();
        finish();
    }
}
