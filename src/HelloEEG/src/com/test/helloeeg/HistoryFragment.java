package com.test.helloeeg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Boss LAM on 2015/6/7.
 */
public class HistoryFragment extends ListFragment {
    private static final String TAG = "HistoryFragment";
    private static final int DELETE_ID = Menu.FIRST;
    private static final int UPLOAD_ALL_ID = Menu.FIRST+1;
    private static final int UPLOAD_ID = Menu.FIRST+1;
    private int currentID = 0;
    private ListView lview;
    private listviewAdapter adapter;
    private ArrayList<HashMap<String,String>>list;
    private ArrayList<String> IDList;
    private AlertDialog dialog = null;
    private JSONObject jobject = new JSONObject();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.history, container,
                false);
        IDList = new ArrayList<String>();
        lview = (ListView) rootView.findViewById(android.R.id.list);
        lview.setOnItemLongClickListener(new OnItemLongClickListener(){

            public boolean onItemLongClick(AdapterView<?> a, View v, int i, long l) {

                Log.i("====================", "Index = " + i);
                currentID = i;
                return false;
            }
        });

        try {
            JSONObject jo = new JSONObject();
            jo.put("duration", "23 min");
            jo.put("score", "70");

            JSONArray ja = new JSONArray();
            ja.put(jo);

            jobject.put("20150607 23:30:00", ja);

            jo = new JSONObject();
            jo.put("duration", "20 min");
            jo.put("score", "30");

            ja = new JSONArray();
            ja.put(jo);

            jobject.put("20150608 02:30:30", ja);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        populateList(jobject);
        return rootView;

    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        populateList(jobject);
        super.onResume();
    }

    private void populateList(JSONObject jo) {
//		 TODO Auto-generated method stub
        list = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> temp0 = new HashMap<String, String>();

        try {
            Log.i(TAG, "json num: " + jo.names().length());
            for(int i = 0; i<jo.names().length(); i++) {

                String dateColumn = jo.names().getString(i);
                JSONObject object = jo.getJSONArray(dateColumn).getJSONObject(0);
                String durationColumn = object.getString("duration");
                String scoreColumn = object.getString("score");

                IDList.add(dateColumn);
                temp0 = new HashMap<String, String>();
                temp0.put("FIRST", dateColumn);
                temp0.put("SECOND", durationColumn);
                temp0.put("THIRD", scoreColumn);
                list.add(temp0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new listviewAdapter(this.getActivity(), list);
        lview.setAdapter(adapter);
        //registerForContextMenu(getListView());
    }

    /*
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Cursor c = mDbHelper.getAll();

        c.moveToLast();
        for (int i = 1; i<=position; i++){
            c.moveToPrevious();
        }

        String dt = c.getString(0);
        String hr = c.getString(1);
        String filepath = c.getString(2);
        String device = c.getString(3);
        String status = c.getString(4);
        c.close();

        Intent i = new Intent(this, Graph.class);
        i.putExtra("dt", dt);
        i.putExtra("hr", hr);
        i.putExtra("filepath", filepath);
        i.putExtra("device", device);
        i.putExtra("status", status);
        startActivity(i);
    }
    */
}
