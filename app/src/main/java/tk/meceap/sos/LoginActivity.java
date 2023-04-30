package tk.meceap.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cometchat.pro.core.CometChat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.constants.Http;
import tk.meceap.sos.constants.LocalStorage;
import tk.meceap.sos.models.Agent;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username, password;
    String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Core.getInstance().setContext(this);

        login = (Button) findViewById(R.id.loginBtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
                //Core.getInstance().serverRequest(Request.Method.GET, Constants.urlEventsStatus, null, Constants.REQ_EVENTS_STATUS);
            }
        });
    }

    private void checkLogin() {
        email = username.getText().toString();
        pass = password.getText().toString();

        if(email.isEmpty() || pass.isEmpty())
            Core.getInstance().alertFail("Email and password is required.");
        else{
            Core.getInstance().setUserLogged(new Agent());
            Core.getInstance().serverLogin(email, pass, true);
        }
    }
}