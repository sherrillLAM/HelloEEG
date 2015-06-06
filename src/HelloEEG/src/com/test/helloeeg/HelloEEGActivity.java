package com.test.helloeeg;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.ibm.mobile.services.core.*;
import com.neurosky.thinkgear.TGDevice;

import org.json.JSONException;
import org.json.JSONObject;

public class HelloEEGActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,  new ConnectFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}