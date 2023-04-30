package tk.meceap.sos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.util.Date;

import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.constants.Http;
import tk.meceap.sos.constants.UIKitApplication;
import tk.meceap.sos.models.Agent;

public class Splash extends AppCompatActivity {

    User user = new User();
    Boolean firstTime = true;
    Boolean loading = true;
    TextView text;
    AppCompatImageView logo;
    final int REQUEST_CODE = 101;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ContextCompat.checkSelfPermission(Splash.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(Splash.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(Splash.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
        }
            initApp();
    }

    public void initApp() {
        Core.getInstance().setContext(this);

        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = String.valueOf(wm.getConnectionInfo().getIpAddress());

        text = findViewById(R.id.error_txt);
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loading){
                    Core.getInstance().serverLogin("citizen@gmail.com", "password", false);
                }
                text.setText(R.string.error_process);
                text.setVisibility(View.VISIBLE);
            }
        });

        user.setUid("citizen_" + Core.getInstance().getDeviceId(this));
        user.setName(ip);

        initCalls(user);
    }

    public void initCalls(User user) {
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.region).build();

        CometChat.init(this, Constants.appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d("TAG", "Initialization completed successfully");
                login(user);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("TAG", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void createUser(User user) {
        CometChat.createUser(user, Constants.authKey, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                login(user);
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(getCurrentFocus(), "Unable to setup you app, verify you network", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login(user);
                    }
                }).show();
            }
        });
    }

    private void login(User user) {
        CometChat.login(user.getUid(), Constants.authKey, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CometChat.getLoggedInUser() != null) {
                            Core.getInstance().serverLogin("citizen@gmail.com", "password", false);
                        }
                    }
                }, 2000);

                UIKitSettings.setAppID(Constants.appID);
                UIKitSettings.setAuthKey(Constants.authKey);
                CometChat.setSource("ui-kit", "android", "java");
                UIKitApplication.initListener(Splash.this);
            }

            @Override
            public void onError(CometChatException e) {
                if (firstTime){
                    firstTime = false;
                    createUser(user);
                }
                else
                    Snackbar.make(getCurrentFocus(), "Unable to login, verify you network", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Splash.this, LoginActivity.class));
                        }
                    }).show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        System.out.println("=======================================================");
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(Splash.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }
            case REQUEST_CODE: {
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    initApp();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initApp();
    }
}