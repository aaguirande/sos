package tk.meceap.sos.constants;

public class Constants {
    public static String appID = "236914ad998d2997";
    public static String region = "us";
    public static String authKey = "8e52d193a446e17e4c69bd00a1ce888ca53b6b75";
    public static String googleMapKey = "AIzaSyAyuaQS-AyNX5H9B0tntKHL9tpzrOnnBFs";
    public static String apiServer = "http://129.80.133.157/api";
    public static final String[] dateFilter = {"today","yesterday","thisWeek","lastWeek","thisMonth","lastMonth","thisYear","lastYear","all","custom"};
    public static String urlTasks = apiServer + "/tasks";
    public static String urlTasksOptions = apiServer + "/tasks/options";

    public static String urlAgents = apiServer + "/administration/agents/show";
    public static String urlUserData = apiServer+ "/administration/users/";
    public static String urlAgentsCalendar = apiServer+ "/administration/agents/eventCalendar";
    public static String urlAgentsLocation = apiServer+ "/administration/agents/eventLocation";

    public static String urlLogin = apiServer+ "/login";
    public static String urlDistricts = apiServer + "/system/provinces/districts/show";
    public static String urlDepartment = apiServer+ "/system/departments/show";
    public static String urlOccurrenceTypes = apiServer+ "/system/types/occurrences/show";
    public static String urlEventsStatus = apiServer+ "/system/types/emergencies/show";
    public static String urlOccurrencies = apiServer+ "/tasks/show";

    public static int LOCATION_REFRESH_TIME = 1;
    public static int LOCATION_REFRESH_DISTANCE = 5; //A cada 5 metros e 1 minuto actualiza na base de dados a localização
    public static int CAMERA_REQUEST_CODE = 100;

    public static final String REGISTER_TAG = "REGISTER_REQUEST";
    public static final String REQ_LOGIN = "LOGIN_REQUEST";
    public static final String REQ_OCCURENCES = "OCCURENCES_REQUEST";
    public static final String REQ_OCCURENCE_STATUS = "OCCURENCE_STATUS_REQUEST";
    public static final String REQ_EVENTS_STATUS = "EVENTS_STATUS_REQUEST";
    public static final String REQ_OCCURENCE_TYPE = "OCCURENCE_TYPE_REQUEST";
    public static final String REQ_HELP = "HELP_REQUEST";
    public static final String REQ_EVENTS = "EVENTS_REQUEST";
    public static final String REQ_DEPARTMENT = "DEPARTMENT_REQUEST";
    public static final String REQ_USER_AGENT = "USER_AGENT_REQUEST";
    public static final String REQ_AGENT = "AGENT_REQUEST";

    public static final String REQ_CALL = "CALL_REQUEST";
    public static final String REQ_DISTRICT = "DISTRICT_REQUEST";
    public static final String REQ_ENTITY = "ENTITY_REQUEST";
    public static final String REQ_ENTITY_TYPES = "ENTITY_TYPES_REQUEST";
    public static final String REQ_AGENT_CALENDAR = "AGENT_CALENDAR_REQUEST";
    public static final String REQ_UPDATE_OCCURRENCE = "UPDATE_OCCURRENCE_REQUEST";
    public static final String REQ_AGENT_LOCATION = "AGENT_LOCATION_REQUEST";
    public static final String REQ_ = "_REQUEST";

}