package tk.meceap.sos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.android.volley.Request;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.User;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.databinding.ActivityMainBinding;
import tk.meceap.sos.models.Agent;
import tk.meceap.sos.models.Occurrence;
import tk.meceap.sos.ui.calendar.CalendarFragment;
import tk.meceap.sos.ui.maps.MapsFragment;
import tk.meceap.sos.ui.profile.ProfileFragment;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public TextView nav_name, nav_email, nav_category;
    public NavController navController;
    public boolean viewIsAtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateCallID();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_calendar, R.id.nav_maps)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);

        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        nav_category = header.findViewById(R.id.nav_category);

        Core.getInstance().setContext(this);
        Core.getInstance().setMainActivity(this);
        Core.getInstance().setNavController(navController);
        Core.getInstance().isAgent(true);

        nav_email.setText(Core.getInstance().getUserLogged().getEmail());

        //Initialize events calendar of agents
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlUserData + Core.getInstance().getUserLogged().getUserId(), Core.getInstance().getParamsAgent(), Constants.REQ_USER_AGENT);
        Core.getInstance().serverRequest(Request.Method.POST, Constants.urlAgents, Core.getInstance().getParamsAgent(), Constants.REQ_AGENT);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlEventsStatus, null, Constants.REQ_EVENTS_STATUS);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlOccurrenceTypes, null, Constants.REQ_OCCURENCE_TYPE);
        //Initialize occurencies of agents
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlUserData + Core.getInstance().getUserLogged().getUserId(), null, Constants.REQ_AGENT);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlDepartment, null, Constants.REQ_DEPARTMENT);
        Core.getInstance().serverRequest(Request.Method.GET, Constants.urlDistricts, null, Constants.REQ_DISTRICT);
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
            Core.getInstance().setOccurency(new Occurrence());
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            navController.navigate(R.id.nav_home); //display the News fragment
            viewIsAtHome = true;
        } else {
            super.onBackPressed();
            startActivity(new Intent(MainActivity.this, EmergencyCall.class));
            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
            Core.getInstance().getUserLogged().setToken("");
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}