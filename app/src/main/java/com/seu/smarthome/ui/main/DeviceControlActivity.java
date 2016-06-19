package com.seu.smarthome.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.seu.smarthome.APP;
import com.seu.smarthome.R;
import com.seu.smarthome.ui.base.BaseActivity;
import com.seu.smarthome.util.OkHttpUtils;
import com.seu.smarthome.util.StrUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-04-20.
 */
public class DeviceControlActivity extends BaseActivity implements Button.OnClickListener{

    private Button historyButton;
    private Button timeTaskButton;
    private Button infoButton;
    private Button deleteButton;
    private ToggleButton controlButton;

    private CharSequence deviceName;
    private int deviceID;

    private final static String TAG = "DeviceControlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_device_control);
        setTitle("");
        Toolbar toolbar=(Toolbar)findViewById(R.id.aty_device_control_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title=(TextView)findViewById(R.id.aty_device_control_title);
        Intent intent=getIntent();
        deviceName=intent.getCharSequenceExtra("deviceName");
        deviceID = intent.getIntExtra("deviceid", 0);
        title.setText(deviceName);

        historyButton=(Button) findViewById(R.id.aty_device_history_button);
        historyButton.setOnClickListener(this);
        timeTaskButton=(Button)findViewById(R.id.aty_device_time_task_button);
        timeTaskButton.setOnClickListener(this);
        infoButton=(Button)findViewById(R.id.aty_device_info_button);
        infoButton.setOnClickListener(this);
        deleteButton=(Button)findViewById(R.id.aty_device_delete_button);
        controlButton=(ToggleButton)findViewById(R.id.aty_device_control_button);
        controlButton.setOnClickListener(this);

    }

    private void getDeviceSrate(){
        if(!APP.networkConnected){
            Toast.makeText(APP.context(), "请连接网络", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("token", StrUtils.token());
        map.put("deviceid", Integer.toString(deviceID));
        OkHttpUtils.post(StrUtils.GET_DEVICE_STATE_URL, map, TAG, new OkHttpUtils.SimpleOkCallBack() {
            @Override
            public void onResponse(String s) {
                JSONObject j = OkHttpUtils.parseJSON(APP.context(), s);
                if (j == null)
                    return;
                if(j.optString("devicestate").equals("on")){
                    controlButton.setChecked(true);
                }
                else{
                    controlButton.setChecked(false);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v){
        Intent intent=new Intent();
        intent.putExtra("deviceid", deviceID);
        switch (v.getId()){
            case R.id.aty_device_time_task_button:
                intent.setClass(this,TimeTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.aty_device_info_button:
                intent.setClass(this,DeviceInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.aty_device_history_button:
                intent.setClass(this,HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.aty_device_delete_button:
                break;
            case R.id.aty_device_control_button:
                break;

        }
    }


    @Override
    protected String tag() {
        return TAG;
    }
}
