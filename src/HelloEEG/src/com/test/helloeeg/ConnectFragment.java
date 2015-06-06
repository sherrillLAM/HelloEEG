package com.test.helloeeg;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;

import org.json.JSONException;
import org.json.JSONObject;

//import com.ibm.mobile.services.core.*;

public class ConnectFragment extends Fragment {
    private final static String TAG = "ConnectFragment";

	BluetoothAdapter bluetoothAdapter;
    View rootView;
	TextView tv;
	Button b1, b2;
	
	TGDevice tgDevice;
	final boolean rawEnabled = false;

    //all time is in milliseconds
    long starttime = 0, timestamp = 0;

    JSONObject jo = new JSONObject();
    long duration = 1 * 60000;
	
    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.connect_frag, container, false);

        tv = (TextView)rootView.findViewById(R.id.textView1);
        tv.setText("");
        tv.append("Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n" );

        b1 = (Button) rootView.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doConnect(v);
            }
        });

        b2 = (Button) rootView.findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDisconnect(v);
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
        	// Alert user that Bluetooth is not available
        	Toast.makeText(rootView.getContext(), "Bluetooth not available", Toast.LENGTH_LONG).show();
        	//finish();
        }else {
        	/* create the TGDevice */
        	tgDevice = new TGDevice(bluetoothAdapter, handler);
        }
        return rootView;
    }
    
    @Override
    public void onDestroy() {
    	tgDevice.close();
        super.onDestroy();
    }
    /**
     * Handles messages from TGDevice
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:

                switch (msg.arg1) {
	                case TGDevice.STATE_IDLE:
	                    break;
	                case TGDevice.STATE_CONNECTING:		                	
	                	tv.append("Connecting...\n");
	                	break;		                    
	                case TGDevice.STATE_CONNECTED:
	                	tv.append("Connected.\n");
	                	tgDevice.start();
	                    break;
	                case TGDevice.STATE_NOT_FOUND:
	                	tv.append("Can't find\n");
	                	break;
	                case TGDevice.STATE_NOT_PAIRED:
	                	tv.append("not paired\n");
	                	break;
	                case TGDevice.STATE_DISCONNECTED:
	                	tv.append("Disconnected mang\n");
                }

                break;
            case TGDevice.MSG_POOR_SIGNAL:
            		//signal = msg.arg1;
            		//tv.append("PoorSignal: " + msg.arg1 + "\n");
                break;
            case TGDevice.MSG_RAW_DATA:	  
            		//raw1 = msg.arg1;
            		//tv.append("Got raw: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_HEART_RATE:
        		//tv.append("Heart rate: " + msg.arg1 + "\n");
                break;
            case TGDevice.MSG_ATTENTION: {
                //att = msg.arg1;
                tv.append("Attention: " + msg.arg1 + "\n");
                timestamp = System.currentTimeMillis();
                try {
                    jo.put(timestamp + "", msg.arg1);
                    Log.v(TAG, "json: " + jo.toString() + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(timestamp - starttime >= duration) {
                    //send json object
                    tgDevice.close();
                    jo = new JSONObject();
                    Log.v(TAG, "timeout!\n");
                }
                //Log.v("HelloA", "Attention: " + att + "\n");
                break;
            }
            case TGDevice.MSG_MEDITATION:

            	break;
            case TGDevice.MSG_BLINK:
            		//tv.append("Blink: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_RAW_COUNT:
            		//tv.append("Raw Count: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_LOW_BATTERY:
            	Toast.makeText(rootView.getContext(), "Low battery!", Toast.LENGTH_SHORT).show();
            	break;
            case TGDevice.MSG_RAW_MULTI:
            	//TGRawMulti rawM = (TGRawMulti)msg.obj;
            	//tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
            default:
            	break;
        }
        }
    };
    
    public void doConnect(View view) {
    	if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
            starttime = System.currentTimeMillis();
            tgDevice.connect(rawEnabled);
        }
    	//tgDevice.ena
    }

    public void doDisconnect(View view) {
        if(tgDevice.getState() == TGDevice.STATE_CONNECTED)
            tgDevice.close();
        //tgDevice.ena
    }
}