package tk.meceap.sos.constants;

import static android.content.Context.LOCATION_SERVICE;
import static com.android.volley.Request.Method.PATCH;
import static com.android.volley.Request.Method.POST;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tk.meceap.sos.EmergencyCall;
import tk.meceap.sos.MainActivity;
import tk.meceap.sos.R;
import tk.meceap.sos.adapters.CalendarEventAdapter;
import tk.meceap.sos.models.Agent;
import tk.meceap.sos.models.Calendar;
import tk.meceap.sos.models.CalendarEventLog;
import tk.meceap.sos.models.CalendarEventStatus;
import tk.meceap.sos.models.Call;
import tk.meceap.sos.models.Department;
import tk.meceap.sos.models.Occurrence;
import tk.meceap.sos.models.OccurrenceFilter;
import tk.meceap.sos.models.OccurrenceStatus;
import tk.meceap.sos.models.OccurrenceType;
import tk.meceap.sos.models.Province;

public class Core {
    private MainActivity mainActivity;
    private Context context;
    private View view;
    private Activity activity;
    private static final String TAG = Core.class.getName();
    private static volatile Core instance = null;

    private List<Agent> agents;
    private List<Occurrence> occurencies = new LinkedList<>();
    private List<OccurrenceStatus> occurrenceStatuses = new LinkedList<>();
    private List<OccurrenceType> occurrenceTypes = new LinkedList<>();

    private List<Calendar> calendars = new LinkedList<>();
    private List<CalendarEventStatus> eventStatuses = new LinkedList<>();

    private List<Department> departments = new LinkedList<>();
    private List<Province> provinces = new LinkedList<>();

    private Agent userLogged = new Agent();
    String location;
    private Occurrence occurrence;
    private OccurrenceFilter occurrenceFilter = new OccurrenceFilter();
    private boolean isAgent = false;
    private boolean isLoadingRequest = false;
    private RequestQueue requestQueue;

    private Occurrence selectedOccurrence;
    private int callAttempts = 0;
    private Calendar selectedCalendar;
    public boolean occurrencePagination = false;

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

    private NavController navController;

    public void displaySelectedScreen(int itemId) {
        navController.navigate(itemId);
    }

    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public Occurrence getSelectedOccurency() {
        return selectedOccurrence;
    }

    public void setSelectedOccurency(Occurrence selectedOccurrence) {
        this.selectedOccurrence = selectedOccurrence;
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public Occurrence getOccurency() {
        return occurrence;
    }

    public void setOccurency(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void isAgent(boolean isAgent) {
        this.isAgent = isAgent;
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
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                    Constants.LOCATION_REFRESH_DISTANCE, mLocationListener);

        } catch (Exception e){
            alertFailLocation();
        }

    }

    public Agent getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(Agent userLogged) {
        this.userLogged = userLogged;
    }

    public List<Occurrence> getOccurencies() {
        return this.occurencies;
    }

    public void serverLogin(String email, String pass, boolean remember, String login) {
        Core.getInstance().setAgent(remember);
        userLogged.setToken("");
        JSONObject params = new JSONObject();
        try {
            params.put("email", email)
                    .put("device_name", "mobile")
                    .put("login_type", login)
                    .put("call_id", CometChat.getLoggedInUser().getUid())
                    .put("location", Core.getInstance().getLocation())
                    .put("password", pass)
                    .put("remember", remember);
            serverRequest(POST, Constants.urlLogin, params, Constants.REQ_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoadingRequest() {
        return isLoadingRequest;
    }

    public void serverRequest(int Method, String url, JSONObject params, String caso) {
        isLoadingRequest = true;
        if (userLogged.getToken() == null)
            Core.getInstance().addToRequestQueue(new JsonObjectRequest(Method,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            serverResponse(caso, response);
                            isLoadingRequest = false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isLoadingRequest = false;
                            Log.i("Error Response: " + caso, String.valueOf(error.getMessage()));
                            Toast.makeText(context, "Invalid Response From Server: " + caso, Toast.LENGTH_LONG).show();
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
                            isLoadingRequest = false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isLoadingRequest = false;
                            Log.i("Error Response: " + caso, String.valueOf(error));
                            Toast.makeText(context, "Invalid Response From Server: " + caso, Toast.LENGTH_LONG).show();
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
        System.out.println(response.toString());
        switch (caso) {
            case Constants.REQ_AGENT_LOCATION:
                System.out.println("======================================================");
                Log.i("\n\n\n\nSuccess Response: " + caso, response.toString());
                System.out.println(response.toString());
                System.out.println("======================================================");
                break;
            case Constants.REQ_LOGIN:
                try {
                    Core.getInstance().setUserLogged(new Agent(
                            response.getString("token"),
                            response.getJSONObject("user").getString("id"),
                            response.getJSONObject("user").getString("person_id"),
                            response.getJSONObject("user").getString("email"),
                            response.getString("event_id")
                    ));
                    if (isAgent())
                        activity.startActivity(new Intent(getContext(), MainActivity.class));
                    else
                        activity.startActivity(new Intent(getContext(), EmergencyCall.class));
                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    activity.finish();
                } catch (JSONException e) {
                    setAgent(false);
                    try {
                        alertFail(response.getString("error"));
                    } catch (JSONException ex) {
                        alertFail("Unable to proccess this request, contact administrator");
                    }
                }
                break;
            case Constants.REQ_HELP:
                try {
                    activity.findViewById(R.id.pp_progressbar).setVisibility(View.GONE);
                    activity.findViewById(R.id.pt_progressbar).setVisibility(View.GONE);
                    activity.findViewById(R.id.sensap_progressbar).setVisibility(View.GONE);
                    JSONArray dados = response.getJSONArray("dados");
                    System.out.println(dados);

                    if (!dados.isNull(0)) {
                        for (int i = 0; i < dados.length(); i++) {
                            occurrence.setCallList(new Call(
                                    dados.getJSONObject(i).getString("call_id"),
                                    dados.getJSONObject(i).getString("location"),
                                    dados.getJSONObject(i).getString("distance_value"),
                                    dados.getJSONObject(i).getString("distance_text"),
                                    dados.getJSONObject(i).getString("duration_value"),
                                    dados.getJSONObject(i).getString("duration_text")
                            ));
                        }
                        occurrence.setId(response.getJSONObject("param").getString("task"));
                        occurrence.setAgentsAddress(response.getString("agents_address"));
                        occurrence.setAgentsUid(response.getString("call_ids"));

                        Core.getInstance().setOccurency(occurrence);
                        System.out.println("\n\n\n\n" + occurrence.toString());
                        Core.getInstance().setCallAttempts(0);
                        makeCall(occurrence.getCallList().get(0).getCallId());
                    } else {
                        alertFail(activity.getResources().getString(R.string.error_agents));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_AGENT_CALENDAR:
                System.out.println(caso + "\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isCalendarById(calendars, dados.getJSONObject(i).getString("event_id") + dados.getJSONObject(i).getString("event_id")))
                            calendars.add(new Calendar(dados.getJSONObject(i)));
                    }
                    System.out.println(calendars.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_OCCURENCES:
                try {
                    System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.getJSONObject("data").getString("current_page"));
                    System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.getJSONObject("data").getString("last_page"));
                    System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.getJSONObject("data").getString("per_page"));
                    System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.getJSONObject("data").getString("total"));
                    Core.getInstance().getOccurrenceFilter().setOccurrenceFilterPagination(
                            response.getJSONObject("data").getInt("total"),
                            response.getJSONObject("data").getInt("per_page"),
                            response.getJSONObject("data").getInt("last_page"),
                            response.getJSONObject("data").getInt("current_page")
                    );
                    JSONArray dados = response.getJSONObject("data").getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isOccurrenceById(occurencies, dados.getJSONObject(i).getString("id")))
                            occurencies.add(new Occurrence(dados.getJSONObject(i)));
                    }
                    System.out.println(occurencies.toString());
                    this.mainActivity.navController.navigate(R.id.nav_home);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_USER_AGENT:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONObject dados = response.getJSONObject("user");
                    Core.getInstance().getUserLogged().setUserAgent(dados);

                    System.out.println(userLogged.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_AGENT:
                System.out.println("\n\n\nVendo a resposta do agente\n\n\n\n" + response.toString());
                try {
                    JSONObject dados = response.getJSONArray("data").getJSONObject(0);
                    Core.getInstance().getUserLogged().setAgent(dados);

                    this.mainActivity.nav_category.setText(Core.getInstance().getUserLogged().getAgentPatent());
                    this.mainActivity.nav_name.setText(Core.getInstance().getUserLogged().getName());

                    System.out.println(userLogged.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_EVENTS_STATUS:
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isCalendarEventStatusById(eventStatuses, dados.getJSONObject(i).getString("id")))
                            eventStatuses.add(new CalendarEventStatus(dados.getJSONObject(i)));
                    } //after inititialization of Event calendar propety
                    System.out.println(eventStatuses.toString());
                    Core.getInstance().serverRequest(Request.Method.POST, Constants.urlAgentsCalendar, Core.getInstance().getParamsAgent(), Constants.REQ_AGENT_CALENDAR);
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
            case Constants.REQ_DISTRICT:
                System.out.println("==========================================================");
                Log.i("\n\n\n\nSuccess Response Province: " + caso, response.toString());
                System.out.println("\n\n\nVendo a resposta\n\n\n\n" + response.toString());
                try {
                    JSONArray dados = response.getJSONArray("data");

                    for (int i = 0; i < dados.length(); i++) {
                        if (!isProvinceById(provinces, dados.getJSONObject(i).getString("id")))
                            provinces.add(new Province(dados.getJSONObject(i)));
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
                        if (!isOccurencyTypeById(occurrenceTypes, dados.getJSONObject(i).getString("id")))
                            occurrenceTypes.add(new OccurrenceType(dados.getJSONObject(i)));
                    } //after inititialization of Event calendar propety
                    Core.getInstance().serverRequest(POST, Constants.urlTasksOptions, getParamsOccurrence(false, 1, "", "all", "", ""), Constants.REQ_OCCURENCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_UPDATE_OCCURRENCE:
                try {
                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public JSONObject getParamsOccurrence(Boolean completed, int page, String occurrenceType, String dateType, String startDate, String endDate) {
        JSONObject params = new JSONObject();
        try {

            /*
            * filters: {"tasks":{"completed":null,"flag":null,"allocated_to":null}}
              intervals: {"tasks":{"reminder":{"min":"2023-05-01 00:00:00","max":"2023-05-31 23:59:59"}}}
                params: {"dateFilter":"thisMonth","overdue":null}
                meta: {"start":0,"length":10,"sort":false,"search":"","forceInfo":false,"searchMode":"full"}
            * */
            params
                    .put("completed", completed)
                    .put("page", page)
                    .put("department", 3)
                    .put("flag", 3)
                    .put("occurrence_type", occurrenceType)
                    .put("user_id", Core.getInstance().getUserLogged().getUserId())
                    .put("start_date", startDate)
                    .put("end_date", endDate)
                    .put("date_type", dateType) //today, yesterday, this_week, this_month, all
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public JSONObject getParamsAgent() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("person_id", Core.getInstance().getUserLogged().getPersonId())
                    .put("user_id", Core.getInstance().getUserLogged().getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public JSONObject getParamsAgentLocation() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("user_id", Core.getInstance().getUserLogged().getUserId())
                    .put("event_id", Core.getInstance().getUserLogged().getEventId())
                    .put("location", Core.getInstance().getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public boolean isOccurrenceById(List<Occurrence> data, String id) {
        for (Occurrence d : data) {
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

    public boolean isProvinceById(List<Province> data, String id) {
        for (Province d : data) {
            if (d.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean isOccurencyTypeById(List<OccurrenceType> data, String id) {
        for (OccurrenceType d : data) {
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
                /*.setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.dismiss();
                })*/
                .setPositiveButton("Ok", ((dialog, which) -> {
                    dialog.dismiss();
                })).show();
    }

    public void alertFailLocation() {
        new AlertDialog.Builder(activity)
                .setTitle("GPS is settings")
                .setIcon(R.drawable.logo)
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Settings", ((dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(intent);
                })).show();
    }

    RecyclerView itemsEvents;

    public void eventCalendarLogs(List<CalendarEventLog> eventLog) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle("Event log")
                .setPositiveButton("OK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setIcon(R.drawable.logo);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_event_calendar, null);
        dialogBuilder.setView(dialogView);

        itemsEvents = dialogView.findViewById(R.id.events);
        itemsEvents.setLayoutManager(new LinearLayoutManager(activity));
        itemsEvents.setAdapter(new CalendarEventAdapter(eventLog));

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void showOccurrenceFilter() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle("Filter occurrence")
                .setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Search", (dialog, i) -> {
                    dialog.dismiss();
                })
                .setIcon(R.drawable.logo);

        SwitchCompat complete;
        RadioButton error, warning, info, success;
        ArrayAdapter<String> adapterOccurrenceType, adapterDateType;
        AutoCompleteTextView completeTextView, completeDateType;
        Button btnFrom, btnTo;

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.occurrence_filter_dialog, null);

        completeTextView = dialogView.findViewById(R.id.autoCompleteTextView);
        completeDateType = dialogView.findViewById(R.id.autoCompleteDate);

        complete = dialogView.findViewById(R.id.occurrence_completed);
        error = dialogView.findViewById(R.id.radio_error);
        warning = dialogView.findViewById(R.id.radio_warning);
        info = dialogView.findViewById(R.id.radio_info);
        success = dialogView.findViewById(R.id.radio_success);

        btnFrom = dialogView.findViewById(R.id.btn_from);
        btnTo = dialogView.findViewById(R.id.btn_to);

        if(getOccurrenceFilter().getFlag() != null)
        switch (getOccurrenceFilter().getFlag()) {
            case "1":
                error.setChecked(true);
                break;
            case "2":
                warning.setChecked(true);
                break;
            case "3":
                info.setChecked(true);
                break;
            case "4":
                success.setChecked(true);
                break;
        }

        if(getOccurrenceFilter().getCompleted() != null)
        switch (getOccurrenceFilter().getCompleted()) {
            case "0":
                complete.setChecked(false);
                break;
            case "1":
                complete.setChecked(true);
                break;
        }

        if(getOccurrenceFilter().getDateType() != null && getOccurrenceFilter().getDateType().equals("custom")){
            btnFrom.setVisibility(View.VISIBLE);
            btnTo.setVisibility(View.VISIBLE);
            btnFrom.setText(getOccurrenceFilter().getDateFrom());
            btnTo.setText(getOccurrenceFilter().getDateTo());
        } else {
            btnFrom.setVisibility(View.GONE);
            btnTo.setVisibility(View.GONE);
        }

        //Initializing form to update occurrence
        adapterOccurrenceType = new ArrayAdapter<String>(dialogView.getContext(), R.layout.dropdown_list, getListOccurrenceType());

        completeTextView.setAdapter(adapterOccurrenceType);
        completeTextView.setText(getOccurrenceFilter().getOccurrenceType(), false);
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("\n\n\n\n++++++++++++++++++++++++\n\n\n\n\n" + parent.getItemAtPosition(position).toString());
                getOccurrenceFilter().setOccurrenceType(parent.getItemAtPosition(position).toString());
            }
        });

        //Initializing form to update occurrence
        adapterDateType = new ArrayAdapter<String>(dialogView.getContext(), R.layout.dropdown_list, Constants.dateFilter);

        completeDateType.setAdapter(adapterDateType);
        completeDateType.setText(getOccurrenceFilter().getDateType(), false);
        completeDateType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("\n\n\n\n++++++++++++++++++++++++\n\n\n\n\n" + parent.getItemAtPosition(position).toString());
                getOccurrenceFilter().setDateType(parent.getItemAtPosition(position).toString());
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("custom")) {
                    btnFrom.setVisibility(View.VISIBLE);
                    btnTo.setVisibility(View.VISIBLE);
                } else {
                    btnFrom.setVisibility(View.GONE);
                    btnTo.setVisibility(View.GONE);
                }
            }
        });

        error.setOnClickListener(v -> getOccurrenceFilter().setFlag("1"));
        warning.setOnClickListener(v -> getOccurrenceFilter().setFlag("2"));
        info.setOnClickListener(v -> getOccurrenceFilter().setFlag("3"));
        success.setOnClickListener(v -> getOccurrenceFilter().setFlag("4"));

        complete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) getOccurrenceFilter().setCompleted("1");
            else getOccurrenceFilter().setCompleted("0");
        });

        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar c = java.util.Calendar.getInstance();

                int year = c.get(java.util.Calendar.YEAR);
                int month = c.get(java.util.Calendar.MONTH);
                int day = c.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                btnFrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                getOccurrenceFilter().setDateFrom(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar c = java.util.Calendar.getInstance();

                int year = c.get(java.util.Calendar.YEAR);
                int month = c.get(java.util.Calendar.MONTH);
                int day = c.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                btnTo.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                getOccurrenceFilter().setDateTo(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void updateOccurrenceDialog(int index) {
        EditText victimName, occurrenceDetails;
        SwitchCompat complete;
        RadioButton error, warning, info, success;
        ArrayAdapter<String> adapter;
        AutoCompleteTextView completeTextView;


        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.occurrence_update_dialog, null);

        completeTextView = dialogView.findViewById(R.id.autoCompleteTextView);
        victimName = dialogView.findViewById(R.id.victim_name);
        occurrenceDetails = dialogView.findViewById(R.id.occurrence_details);

        complete = dialogView.findViewById(R.id.occurrence_completed);
        error = dialogView.findViewById(R.id.radio_error);
        warning = dialogView.findViewById(R.id.radio_warning);
        info = dialogView.findViewById(R.id.radio_info);
        success = dialogView.findViewById(R.id.radio_success);

        //Initializing form to update occurrence
        adapter = new ArrayAdapter<String>(dialogView.getContext(), R.layout.dropdown_list, getListOccurrenceType());

        completeTextView.setAdapter(adapter);
        completeTextView.setText(getSelectedOccurency().getOccurencyType().getName(), false);
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("\n\n\n\n++++++++++++++++++++++++\n\n\n\n\n" + parent.getItemAtPosition(position).toString());
                getSelectedOccurency().setOccurrenceType(getOccurrenceTypeByName(parent.getItemAtPosition(position).toString()));
            }
        });

        switch (getSelectedOccurency().getFlag()) {
            case "1":
                error.setChecked(true);
                break;
            case "2":
                warning.setChecked(true);
                break;
            case "3":
                info.setChecked(true);
                break;
            case "4":
                success.setChecked(true);
                break;
        }

        switch (getSelectedOccurency().getCompleted()) {
            case "0":
                complete.setChecked(false);
                break;
            case "1":
                complete.setChecked(true);
                break;
        }

        victimName.setText(getSelectedOccurency().getVictimName());
        occurrenceDetails.setText(getSelectedOccurency().getDetails());

        //
        error.setOnClickListener(v -> getSelectedOccurency().setFlag("1"));
        warning.setOnClickListener(v -> getSelectedOccurency().setFlag("2"));
        info.setOnClickListener(v -> getSelectedOccurency().setFlag("3"));
        success.setOnClickListener(v -> getSelectedOccurency().setFlag("4"));

        complete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) getSelectedOccurency().setCompleted("1");
            else getSelectedOccurency().setCompleted("0");
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle("Update occurrence")
                .setNegativeButton("Cancel", (dialog, i) -> {
                    getSelectedOccurency().setVictimName(victimName.getText().toString());
                    getSelectedOccurency().setDetails(occurrenceDetails.getText().toString());

                    occurencies.get(index).setVictimName(getSelectedOccurency().getVictimName());
                    occurencies.get(index).setDetails(getSelectedOccurency().getDetails());
                    occurencies.get(index).setCompleted(getSelectedOccurency().getCompleted());
                    occurencies.get(index).setFlag(getSelectedOccurency().getFlag());
                    occurencies.get(index).setOccurrenceType(getSelectedOccurency().getOccurrenceType());

                    System.out.println(occurencies.get(index).toString());
                    dialog.dismiss();

                    mainActivity.navController.navigate(R.id.nav_home);
                })
                .setPositiveButton("Save", (dialog, i) -> {
                    getSelectedOccurency().setVictimName(victimName.getText().toString());
                    getSelectedOccurency().setDetails(occurrenceDetails.getText().toString());

                    occurencies.get(index).setVictimName(getSelectedOccurency().getVictimName());
                    occurencies.get(index).setDetails(getSelectedOccurency().getDetails());
                    occurencies.get(index).setCompleted(getSelectedOccurency().getCompleted());
                    occurencies.get(index).setFlag(getSelectedOccurency().getFlag());
                    occurencies.get(index).setOccurrenceType(getSelectedOccurency().getOccurrenceType());

                    System.out.println(occurencies.get(index).toString());
                    System.out.println(Constants.urlTasks + "/" + occurencies.get(index).getId());
                    System.out.println(occurencies.get(index).getUpdateParams());
                    Core.getInstance().serverRequest(Request.Method.PATCH, Constants.urlTasks + "/" + occurencies.get(index).getId(), occurencies.get(index).getUpdateParams(), Constants.REQ_UPDATE_OCCURRENCE);

                    mainActivity.navController.navigate(R.id.nav_home);
                    dialog.dismiss();
                })
                .setIcon(R.drawable.logo);

        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public List<String> getListOccurrenceType() {
        List<String> items = new ArrayList<String>();
        for (OccurrenceType d : occurrenceTypes) {
            items.add(d.getName());
        }
        return items;
    }

    public OccurrenceType getOccurrenceTypeByName(String name) {
        for (OccurrenceType d : occurrenceTypes) {
            System.out.println(d.getName().equalsIgnoreCase(name));
            if (d.getName().equalsIgnoreCase(name)) return d;
        }
        return null;
    }

    private LatLng userLocation;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            System.out.println("\n\n\n\nLocation: " + String.format("%.6f", location.getLatitude()) + "," + String.format("%.6f", location.getLongitude()));
            setLocation(
                    String.format("%.6f", location.getLatitude()).replace(",", ".")
                            + "," +
                            String.format("%.6f", location.getLongitude()).replace(",", ".")
            );
            userLocation = new LatLng(
                    Double.valueOf(String.format("%.6f", location.getLatitude()).replace(",", ".")),
                    Double.valueOf(String.format("%.6f", location.getLongitude()).replace(",", "."))
            );

            if (isAgent)
                Core.getInstance().serverRequest(Request.Method.POST, Constants.urlAgentsLocation, getParamsAgentLocation(), Constants.REQ_AGENT_LOCATION);
        }
    };

    public LatLng getLocaLatLng(String[] s) {
        System.out.println(Arrays.toString(s));
        return new LatLng(
                Double.valueOf(s[0]),
                Double.valueOf(s[1])
        );
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setOccurencies(List<Occurrence> occurencies) {
        this.occurencies = occurencies;
    }

    public List<OccurrenceStatus> getOccurencyStatuses() {
        return occurrenceStatuses;
    }

    public void setOccurencyStatuses(List<OccurrenceStatus> occurrenceStatuses) {
        this.occurrenceStatuses = occurrenceStatuses;
    }

    public List<OccurrenceType> getOccurencyTypes() {
        return occurrenceTypes;
    }

    public void setOccurencyTypes(List<OccurrenceType> occurrenceTypes) {
        this.occurrenceTypes = occurrenceTypes;
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

    public int getCallAttempts() {
        return callAttempts;
    }

    public void setCallAttempts(int callAttempts) {
        this.callAttempts = callAttempts;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public OccurrenceFilter getOccurrenceFilter() {
        return occurrenceFilter;
    }

    public void setOccurrenceFilter(OccurrenceFilter occurrenceFilter) {
        this.occurrenceFilter = occurrenceFilter;
    }
}