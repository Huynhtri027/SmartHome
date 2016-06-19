package com.seu.smarthome.ui.intro;import android.content.Intent;import android.content.SharedPreferences;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.EditText;import android.widget.TextView;import android.widget.Toast;import org.json.JSONException;import org.json.JSONObject;import java.util.HashMap;import java.util.Map;import com.seu.smarthome.APP;import com.seu.smarthome.R;import com.seu.smarthome.ui.base.BaseActivity;import com.seu.smarthome.ui.main.ActivityMain;import com.seu.smarthome.util.LogUtils;import com.seu.smarthome.util.OkHttpUtils;import com.seu.smarthome.util.StrUtils;import com.seu.smarthome.widgt.LoadingView;/** * Created by Liujilong on 16/1/22. * liujilong.me@gmail.com */public class AtyLogin extends BaseActivity {    private final static String TAG = "AtyLogin";    EditText mEtUser, mEtPassword;    Button mBtnLogin;    TextView mTvRegister;    TextView mTvForgetPasswd;    LoadingView mLoadingView;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.aty_login);        bindView();    }    private void bindView(){        mEtUser = (EditText) findViewById(R.id.login_edit_text_user);        mEtPassword = (EditText) findViewById(R.id.login_edit_text_password);        mBtnLogin = (Button) findViewById(R.id.login_button_login);        mTvRegister = (TextView) findViewById(R.id.login_text_view_register);        mTvForgetPasswd=(TextView)findViewById(R.id.login_text_view_forgetpasswd);        mBtnLogin.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                login();            }        });        mTvRegister.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Intent i = new Intent(AtyLogin.this,AtyRegister.class);                startActivity(i);            }        });        mTvForgetPasswd.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                Intent i=new Intent(AtyLogin.this,AtyResetPasswd.class);                startActivity(i);            }        });        mLoadingView = new LoadingView(this);    }    private void login(){        //Intent i = new Intent(AtyLogin.this,ActivityMain.class);        //startActivity(i);        //finish();        final String userName = mEtUser.getText().toString();        String passWord = mEtPassword.getText().toString();        if(userName.isEmpty()||passWord.isEmpty()){            Toast.makeText(APP.context(),R.string.account_or_password_not_null,Toast.LENGTH_SHORT).show();            return;        }        if(!APP.networkConnected){            Toast.makeText(APP.context(),"请连接网络",Toast.LENGTH_SHORT).show();            return;        }        String passMd5 = StrUtils.md5(passWord);        Map<String,String> map = new HashMap<>();        map.put("username", userName);        map.put("password", passMd5);//        WindowManager windowManager = getWindowManager();//        windowManager.addView(mLoadingView,LoadingView.mWindowParams);        OkHttpUtils.post(StrUtils.LOGIN_URL, map, TAG, new OkHttpUtils.SimpleOkCallBack() {            @Override            public void onResponse(String s) {                LogUtils.i(TAG, s);                JSONObject j;                try {                    j = new JSONObject(s);                } catch (JSONException e) {                    e.printStackTrace();                    Toast.makeText(APP.context(), R.string.login_fail, Toast.LENGTH_SHORT).show();                    return;                }                String state = j.optString("state", "");                if (state.equals("successful")) {                    String token = j.optString("token", "");                    String id = j.optString("id", "");                    SharedPreferences sp = APP.context().getSharedPreferences(StrUtils.SP_USER, MODE_PRIVATE);                    sp.edit().putString(StrUtils.SP_USER_TOKEN, token).putString(StrUtils.SP_USER_ID, id).putString(StrUtils.SP_UEER_NAME, userName).apply();                    Toast.makeText(APP.context(), R.string.login_success, Toast.LENGTH_SHORT).show();                    startActivity(new Intent(AtyLogin.this, ActivityMain.class));                    finish();                } else {                    String reason = j.optString("reason");                    Toast.makeText(APP.context(), reason, Toast.LENGTH_SHORT).show();                }            }        });    }    @Override    protected String tag() {        return TAG;    }}