package tk.meceap.sos.constants;

import static android.content.Context.LOCATION_SERVICE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tk.meceap.sos.EmergencyCall;
import tk.meceap.sos.MainActivity;
import tk.meceap.sos.R;
import tk.meceap.sos.Splash;
import tk.meceap.sos.adapters.CalendarEventAdapter;
import tk.meceap.sos.models.Agent;
import tk.meceap.sos.models.Calendar;
import tk.meceap.sos.models.CalendarEventLog;
import tk.meceap.sos.models.CalendarEventStatus;
import tk.meceap.sos.models.Call;
import tk.meceap.sos.models.Department;
import tk.meceap.sos.models.Occurency;
import tk.meceap.sos.models.OccurencyStatus;
import tk.meceap.sos.models.OccurencyType;

public class Core {
    private MainActivity mainActivity;
    //private Splash mainActivity;
    private Context context;
    private View view;
    private Activity activity;
    private static final String TAG = Core.class.getName();
    private static volatile Core instance = null;

    private List<Agent> agents;
    private List<Occurency> occurencies = new LinkedList<>();
    private List<OccurencyStatus> occurencyStatuses = new LinkedList<>();
    private List<OccurencyType> occurencyTypes = new LinkedList<>();

    private List<Calendar> calendars = new LinkedList<>();
    private List<CalendarEventStatus> eventStatuses = new LinkedList<>();

    private List<Department> departments = new LinkedList<>();

    private Agent userLogged = new Agent();
    String location;
    private Occurency occurency;
    private boolean isAgent = false;
    private RequestQueue requestQueue;

    private Occurency selectedOccurency;
    private Calendar selectedCalendar;

    public Core() {
        this.agents = new LinkedList<>();
    }

    public static Core getInstance() {
        if (instance == null) {
            synchronized (Core.class) {
                if (instance == null) {
                    instance = new Core();
                }
            }
        }
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void displaySelectedScreen(int itemId) {
        mainActivity.navController.navigate(itemId);
    }

    public Occurency getSelectedOccurency() {
        return selectedOccurency;
    }

    public void setSelectedOccurency(Occurency selectedOccurency) {
        this.selectedOccurency = selectedOccurency;
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public Occurency getOccurency() {
        return occurency;
    }

    public void setOccurency(Occurency occurency) {
        this.occurency = occurency;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.context = mainActivity.getApplicationContext();
        this.view = mainActivity.getCurrentFocus();
        this.mainActivity = mainActivity;
    }

    public void setContext(Activity context) {
        this.context = context.getApplicationContext();
        this.activity = context;
        this.view = context.getCurrentFocus();
        this.requestQueue = Volley.newRequestQueue(this.context);

        LocationManager mLocationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{android.Manifest.permission.CAMERA}, Constants.CAMERA_REQUEST_CODE);
                }
            } else
                return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                Constants.LOCATION_REFRESH_DISTANCE, mLocationListener);

    }

    public Agent getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(Agent userLogged) {
        this.userLogged = userLogged;
    }


    public List<Occurency> getOccurencies() {
        return this.occurencies;
    }

    public Occurency getOccurency(String id) {
        for (Occurency data : this.occurencies) {
            if (data.getId() == id)
                return data;
        }
        return null;
    }

    public Occurency setOccurencies(JSONObject occurency) {
        Occurency data;
        try {
            data = getOccurency(occurency.getString("occurency_id"));
            if (data == null) {
                data = new Occurency(occurency);
                this.occurencies.add(data);
            }
        } catch (JSONException e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    public void setOccurencies() {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> taskParams = new HashMap<String, String>();
        Map<String, Map<String, String>> taskParams2 = new HashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> taskParams3 = new HashMap<String, Map<String, String>>();
        Map<String, String> reminderParams = new HashMap<String, String>();
        Map<String, String> metaParams = new HashMap<String, String>();
        Map<String, String> params2 = new HashMap<String, String>();

        taskParams.put("completed", String.valueOf(false));
        taskParams.put("flag", null);
        taskParams.put("allocated_to", null);

        reminderParams.put("min", null);
        reminderParams.put("max", null);

        metaParams.put("start", String.valueOf(130));
        metaParams.put("length", String.valueOf(10));
        metaParams.put("sort", String.valueOf(false));
        metaParams.put("search", "");
        metaParams.put("forceInfo", String.valueOf(false));
        metaParams.put("searchMode", "full");

        params2.put("dateFilter", "all");
        params2.put("overdue", null);

        taskParams2.put("task", taskParams);
        taskParams3.put("task", reminderParams);

        params.put("filters", String.valueOf(taskParams2));
        params.put("meta", String.valueOf(metaParams));
        params.put("params", String.valueOf(params2));
        params.put("intervals", String.valueOf(taskParams3));

        Core.getInstance().addToRequestQueue(new JsonObjectRequest(GET,
                Constants.urlNearAgents + "/tableData",//?filters=%7B%22tasks%22:%7B%22completed%22:false,%22flag%22:null,%22allocated_to%22:null%7D%7D&intervals=%7B%22tasks%22:%7B%22reminder%22:%7B%22min%22:null,%22max%22:null%7D%7D%7D&params=%7B%22dateFilter%22:%22all%22,%22overdue%22:null%7D&meta=%7B%22start%22:130,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Success Response: ", response.toString());
                        try {
                            System.out.println("/////////////////////////////////////////////////////////////");
                            System.out.println(response.getJSONArray("data"));
                            if (response.getString("status").equals("success")) {
                                Toast.makeText(context, "Data retrived", Toast.LENGTH_SHORT).show();
                                JSONArray occurencies = response.getJSONArray("occurencies");

                                for (int i = 0; i < occurencies.length(); i++)
                                    setOccurencies(occurencies.getJSONObject(i));
                            } else
                                Snackbar.make(view, response.getString("message"), Snackbar.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            Toast.makeText(context, "Invalid Response From Server", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response: ", String.valueOf(error));
                Toast.makeText(context, "Invalid Response From Server", Toast.LENGTH_LONG).show();
            }
        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("X-Requested-With", "XMLHttpRequest");
                params.put("Authorization", "Bearer " + Core.getInstance().getUserLogged().getToken());
                return params;
            }
/*
            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Map<String, String> taskParams = new HashMap<String, String>();
                Map<String, Map<String, String>> taskParams2 = new HashMap<String, Map<String, String>>();
                Map<String, Map<String, String>> taskParams3 = new HashMap<String, Map<String, String>>();
                Map<String, String> reminderParams = new HashMap<String, String>();
                Map<String, String> metaParams = new HashMap<String, String>();
                Map<String, String> params2 = new HashMap<String, String>();

                taskParams.put("completed", String.valueOf(false));
                taskParams.put("flag", null);
                taskParams.put("allocated_to", null);

                reminderParams.put("min", null);
                reminderParams.put("max", null);

                metaParams.put("start", String.valueOf(130));
                metaParams.put("length", String.valueOf(10));
                metaParams.put("sort", String.valueOf(false));
                metaParams.put("search", "");
                metaParams.put("forceInfo", String.valueOf(false));
                metaParams.put("searchMode", "full");

                params2.put("dateFilter", "all");
                params2.put("overdue", null);

                taskParams2.put("task", taskParams);
                taskParams3.put("task", reminderParams);

                params.put("filters", String.valueOf(taskParams2));
                params.put("meta", String.valueOf(metaParams));
                params.put("params", String.valueOf(params2));
                params.put("intervals", String.valueOf(taskParams3));

                return params;
            }*/
        }, Constants.REGISTER_TAG);
    }

    public void serverLogin(String email, String pass, boolean remember) {
        Core.getInstance().setAgent(remember);
        JSONObject params = new JSONObject();
        try {
            params.put("email", email)
                    .put("device_name", "mobile")
                    .put("login_type", "citizen")
                    .put("location", Core.getInstance().getLocation())
                    .put("password", pass)
                    .put("remember", remember);
            serverRequest(POST, Constants.urlLogin, params, Constants.REQ_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void serverRequest(int Method, String url, JSONObject params, String caso) {
        if (userLogged.getToken() == null)
            Core.getInstance().addToRequestQueue(new JsonObjectRequest(Method,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            serverResponse(caso, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Error Response: ", String.valueOf(error));
                            Toast.makeText(context, "Invalid Response From Server", Toast.LENGTH_LONG).show();
                        }
                    }), Constants.REGISTER_TAG);
        else
            Core.getInstance().addToRequestQueue(new JsonObjectRequest(Method,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            serverResponse(caso, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Error Response: ", String.valueOf(error));
                            Toast.makeText(context, "Invalid Response From Server", Toast.LENGTH_LONG).show();
                        }
                    }) {
                public Map<String, String> getHeaders() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + userLogged.getToken());
                    return params;
                }
            }, Constants.REGISTER_TAG);
    }

    public void serverResponse(String caso, JSONObject response) {
        Log.i("\n\n\n\nSuccess Response: " + caso, response.toString());
        switch (caso) {
            case Constants.REQ_LOGIN:
                try {
                    Core.getInstance().setUserLogged(new Agent(
                            response.getString("token"),
                            response.getJSONObject("user").getString("id"),
                            response.getJSONObject("user").getString("person_id"),
                            response.getJSONObject("user").getString("email"),
                            "",
                            ""
                    ));
                    if (isAgent())
                        activity.startActivity(new Intent(getContext(), MainActivity.class));
                    else
                        activity.startActivity(new Intent(getContext(), EmergencyCall.class));
                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    activity.finish();
                } catch (JSONException e) {
                    setAgent(false);
                    TextView text = activity.findViewById(R.id.error_txt);
                    text.setText(R.string.error_conect);
                    text.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_HELP:
                try {
                    activity.findViewById(R.id.pp_progressbar).setVisibility(View.GONE);
                    activity.findViewById(R.id.pt_progressbar).setVisibility(View.GONE);
                    activity.findViewById(R.id.sensap_progressbar).setVisibility(View.GONE);
                    JSONArray dados = response.getJSONArray("dados");

                    for (int i = 0; i < dados.length(); i++) {
                        occurency.setCallList(new Call(
                                dados.getJSONObject(i).getString("call_id"),
                                dados.getJSONObject(i).getString("location"),
                                dados.getJSONObject(i).getString("distance_value"),
                                dados.getJSONObject(i).getString("distance_text"),
                                dados.getJSONObject(i).getString("duration_value"),
                                dados.getJSONObject(i).getString("duration_text")
                        ));
                    }
                    occurency.setId(response.getJSONObject("param").getString("task"));
                    occurency.setAgents_address(response.getString("agents_address"));
                    occurency.setAgents_uid(response.getString("call_ids"));

                    Core.getInstance().setOccurency(occurency);
                    System.out.println("\n\n\n\n" + occurency.toString());
                    makeCall(occurency.getCallList().get(0).getCallId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_EVENTS:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isCalendarById(calendars, dados.getJSONObject(i).getString("id")))
                            calendars.add(new Calendar(dados.getJSONObject(i)));
                    }
                    System.out.println(calendars.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_OCCURENCES:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isOccurencyById(occurencies, dados.getJSONObject(i).getString("id")))
                            occurencies.add(new Occurency(dados.getJSONObject(i)));
                    }
                    System.out.println(occurencies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_AGENT:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("user");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isOccurencyById(occurencies, dados.getJSONObject(i).getString("id")))
                            occurencies.add(new Occurency(dados.getJSONObject(i)));
                    }
                    System.out.println(occurencies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                *
                * {"user":{"id":1,"person_id":1,"group_id":1,"role_id":1,
                * "email":"meceap.server@gmail.com","is_active":true,"created_by":null,
                * "updated_by":null,"created_at":"2023-04-23T06:57:25.000000Z",
                * "updated_at":"2023-04-23T06:57:25.000000Z","loginCount":58,
                * "actionLogCount":1301,"daysSinceMember":7,"rating":43,
                * "person":{"id":1,"name":"Ant\u00f3nio Adriano","appellative":
                * "Guirande","birthday":"1994-08-27T21:00:00.000000Z","phone":
                * "+258840415421"},"group":{"id":1,"name":"Administrators"},
                * "role":{"id":1,"name":"admin"},"avatar":{"id":1}}}
                 *
                *
                * */
                break;
            case Constants.REQ_EVENTS_STATUS:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isCalendarEventStatusById(eventStatuses, dados.getJSONObject(i).getString("id")))
                            eventStatuses.add(new CalendarEventStatus(dados.getJSONObject(i)));
                    } //after inititialization of Event calendar propety
                    Core.getInstance().serverRequest(Request.Method.GET, Constants.urlAgentsCalendar, null, Constants.REQ_EVENTS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_DEPARTMENT:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isDepartmentById(departments, dados.getJSONObject(i).getString("id")))
                            departments.add(new Department(dados.getJSONObject(i)));
                    } //after inititialization of occurency propriety
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_OCCURENCE_TYPE:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isOccurencyTypeById(occurencyTypes, dados.getJSONObject(i).getString("id")))
                            occurencyTypes.add(new OccurencyType(dados.getJSONObject(i)));
                    } //after inititialization of Event calendar propety
                    Core.getInstance().serverRequest(Request.Method.GET, Constants.urlOccurencies, null, Constants.REQ_OCCURENCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public boolean isOccurencyById(List<Occurency> data, String id) {
        for (Occurency d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean isDepartmentById(List<Department> data, String id) {
        for (Department d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean isOccurencyTypeById(List<OccurencyType> data, String id) {
        for (OccurencyType d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean isCalendarById(List<Calendar> data, String id) {
        for (Calendar d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean isCalendarEventStatusById(List<CalendarEventStatus> data, String id) {
        for (CalendarEventStatus d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public void makeCall(String userId) {
        CometChatCallListener.makeCall(getActivity(), userId, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.CALL_TYPE_AUDIO);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

    public static String getDeviceId(Context context) {
        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    public void alertFail(String s) {
        new AlertDialog.Builder(activity)
                .setTitle("Failed")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", ((dialog, which) -> {
                    dialog.dismiss();
                })).show();
    }

    RecyclerView itemsEvents;
    public void alertDialog(List<CalendarEventLog> eventLog) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle("Event log")
                .setPositiveButton("OK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setIcon(R.drawable.calendar_startblue);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_event_calendar, null);
        dialogBuilder.setView(dialogView);

        itemsEvents = dialogView.findViewById(R.id.events);
        itemsEvents.setLayoutManager(new LinearLayoutManager(activity));
        itemsEvents.setAdapter(new CalendarEventAdapter(eventLog));

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            System.out.println("\n\n\n\nLocation: " + String.format("%.6f", location.getLatitude()) + "," + String.format("%.6f", location.getLongitude()));
            setLocation(String.format("%.6f", location.getLatitude()) + "," + String.format("%.6f", location.getLongitude()));
        }
    };

    public void setOccurencies(List<Occurency> occurencies) {
        this.occurencies = occurencies;
    }

    public List<OccurencyStatus> getOccurencyStatuses() {
        return occurencyStatuses;
    }

    public void setOccurencyStatuses(List<OccurencyStatus> occurencyStatuses) {
        this.occurencyStatuses = occurencyStatuses;
    }

    public List<OccurencyType> getOccurencyTypes() {
        return occurencyTypes;
    }

    public void setOccurencyTypes(List<OccurencyType> occurencyTypes) {
        this.occurencyTypes = occurencyTypes;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public List<CalendarEventStatus> getEventStatuses() {
        return eventStatuses;
    }

    public void setEventStatuses(List<CalendarEventStatus> eventStatuses) {
        this.eventStatuses = eventStatuses;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}