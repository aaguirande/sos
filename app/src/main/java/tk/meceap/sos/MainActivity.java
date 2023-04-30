package tk.meceap.sos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.constants.Http;
import tk.meceap.sos.constants.LocalStorage;
import tk.meceap.sos.constants.UIKitApplication;
import tk.meceap.sos.databinding.ActivityMainBinding;
import tk.meceap.sos.models.Agent;
import tk.meceap.sos.models.Occurency;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    LocalStorage storage;
    public TextView nav_name, nav_email, nav_category;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Core.getInstance().setContext(this);

        updateCallID();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_calendar, R.id.nav_maps)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);

        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        nav_category = header.findViewById(R.id.nav_category);
        nav_email.setText(Core.getInstance().getUserLogged().getEmail());


        System.out.println("+++++++++++++++++++++++=====================================++++++++++++++++++++++++++++++");
        //Initialize events calendar of agents
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlEventsStatus, null, Constants.REQ_EVENTS_STATUS);
        //Initialize occurencies of agents
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlUserData + Core.getInstance().getUserLogged().getUserId(), null, Constants.REQ_AGENT);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlDeparment, null, Constants.REQ_DEPARTMENT);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlOccurenceTypes, null, Constants.REQ_OCCURENCE_TYPE);
    }

    private void initialize() {
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlEventsStatus, null, Constants.REQ_EVENTS_STATUS);
    }

    private void updateCallID() {
        if (CometChat.getLoggedInUser().getName() != Core.getInstance().getUserLogged().getEmail()) {
            CometChat.getLoggedInUser().setName(Core.getInstance().getUserLogged().getEmail());
            CometChat.updateCurrentUserDetails(CometChat.getLoggedInUser(), new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.d("updateUser", user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e("updateUser", e.getMessage());
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CometChat.getLoggedInUser() == null) {
            startActivity(new Intent(MainActivity.this, Splash.class));
        } else {
            Core.getInstance().setContext(this);
            Core.getInstance().setAgent(true);
            Core.getInstance().setOccurency(new Occurency());
        }
    }

    public void makeCall(String receiverID) {
        String receiverType = CometChatConstants.RECEIVER_TYPE_USER;
        String callType = CometChatConstants.CALL_TYPE_VIDEO;

        Call call = new Call(receiverID, receiverType, callType);

        CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                Log.d("TAG", "Call initiated successfully: " + call.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("TAG", "Call initialization failed with exception: " + e.getMessage());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, Splash.class));
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
        Core.getInstance().setUserLogged(new Agent());
        finish();
    }

}