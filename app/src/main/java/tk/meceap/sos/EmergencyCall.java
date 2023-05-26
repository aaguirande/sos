package tk.meceap.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Agent;
import tk.meceap.sos.models.Occurrence;

public class EmergencyCall extends AppCompatActivity {
    private String TAG = "MainActivity";

    private MaterialButton loginBtn;
    private MaterialCardView pp;
    private MaterialCardView pt;
    private MaterialCardView sensap;
    private AppCompatImageView ivLogo;
    private TextView text;

    boolean loading = false;
    Occurrence occurrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        Core.getInstance().setContext(this);
        Core.getInstance().isAgent(false);

        loginBtn = findViewById(R.id.login);
        pp = findViewById(R.id.pp);
        pt = findViewById(R.id.pt);
        sensap = findViewById(R.id.sensap);
        ivLogo = findViewById(R.id.ivLogo);

        Core.getInstance().setAgent(false);
        Core.getInstance().setOccurency(new Occurrence());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmergencyCall.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        pp.setOnClickListener(view -> {
            if(loading) return;
            findViewById(R.id.pp_progressbar).setVisibility(View.VISIBLE);
            Core.getInstance().getOccurency().initCall("1", "1", Core.getInstance().getLocation(), CometChat.getLoggedInUser().getUid());
            Core.getInstance().serverRequest(Request.Method.POST, Constants.urlTasks, Core.getInstance().getOccurency().getParams(), Constants.REQ_HELP);

        });
        pt.setOnClickListener(view -> {
            if(loading) return;
            findViewById(R.id.pt_progressbar).setVisibility(View.VISIBLE);
            Core.getInstance().getOccurency().initCall("2", "2", Core.getInstance().getLocation(), CometChat.getLoggedInUser().getUid());
            Core.getInstance().serverRequest(Request.Method.POST, Constants.urlTasks, Core.getInstance().getOccurency().getParams(), Constants.REQ_HELP);
        });
        sensap.setOnClickListener(view -> {
            if(loading) return;
            findViewById(R.id.sensap_progressbar).setVisibility(View.VISIBLE);
            Core.getInstance().getOccurency().initCall("3", "3", Core.getInstance().getLocation(), CometChat.getLoggedInUser().getUid());
            Core.getInstance().serverRequest(Request.Method.POST, Constants.urlTasks, Core.getInstance().getOccurency().getParams(), Constants.REQ_HELP);
        });
    }

    public void makeCall(String userId) {
        CometChatCallListener.makeCall(EmergencyCall.this, userId, CometChatConstants.RECEIVER_TYPE_USER,CometChatConstants.CALL_TYPE_AUDIO);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EmergencyCall.this, Splash.class));
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
        Core.getInstance().setUserLogged(new Agent());
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().setContext(this);

        loginBtn = findViewById(R.id.login);
        pp = findViewById(R.id.pp);
        pt = findViewById(R.id.pt);
        sensap = findViewById(R.id.sensap);
        ivLogo = findViewById(R.id.ivLogo);

        Core.getInstance().setAgent(false);
        Core.getInstance().setOccurency(new Occurrence());
    }
}