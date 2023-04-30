package tk.meceap.sos.constants;

public class Constants {
    public static String appID = "236914ad998d2997";
    public static String region = "us";
    public static String authKey = "8e52d193a446e17e4c69bd00a1ce888ca53b6b75";
    public static String googleMapKey = "AIzaSyAyuaQS-AyNX5H9B0tntKHL9tpzrOnnBFs";
    public static String apiServer = "http://192.168.126.213/api";
    public static String urlNearAgents = apiServer + "/tasks";
    public static String urlTasks = apiServer + "/tasks/tableData?filters=%7Btasks:%7Bcompleted:false,flag:null,allocated_to:null%7D%7D&intervals=%7Btasks:%7Breminder:%7Bmin:null,max:null%7D%7D%7D&params=%7BdateFilter:all,overdue:null%7D&meta=%7Bstart:0,length:5,sort:false,search:,forceInfo:false,searchMode:full%7D";
    public static String urlEvents1 = apiServer + "/administration/agents/events/tableData?filters=%7B%22calendar_event_user%22:%7B%22user_id%22:%22";
    public static String urlEvents2 = "%22%7D%7D&meta=%7B%22start%22:0,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";
    public static String urlLogin = apiServer+ "/login";
    public static String urlDeparment = apiServer+ "/system/departments/tableData?meta=%7B%22start%22:0,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";
    public static String urlOccurenceTypes = apiServer+ "/system/types/occurrences/tableData?meta=%7B%22start%22:0,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";
    public static String urlEventsStatus = apiServer+ "/system/types/emergencies/tableData?meta=%7B%22start%22:0,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";
    public static String urlUserData = apiServer+ "/administration/users/";
    public static String urlAgentsCalendar = apiServer+ "/administration/agents/events/tableData?filters=%7B%22calendar_event_user%22:%7B%22user_id%22:%225%22%7D%7D&meta=%7B%22start%22:0,%22length%22:10,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";
    public static String urlOccurencies = apiServer+ "/tasks/tableData?filters=%7B%22tasks%22:%7B%22completed%22:false,%22flag%22:null,%22allocated_to%22:2%7D%7D&intervals=%7B%22tasks%22:%7B%22reminder%22:%7B%22min%22:null,%22max%22:null%7D%7D%7D&params=%7B%22dateFilter%22:%22all%22,%22overdue%22:null%7D&meta=%7B%22start%22:0,%22length%22:15,%22sort%22:false,%22search%22:%22%22,%22forceInfo%22:false,%22searchMode%22:%22full%22%7D";


    public static int LOCATION_REFRESH_TIME = 15000;
    public static int LOCATION_REFRESH_DISTANCE = 500;
    public static int CAMERA_REQUEST_CODE = 100;

    public static final String REGISTER_TAG = "REGISTER_REQUEST";
    public static final String REQ_LOGIN = "LOGIN_REQUEST";
    public static final String REQ_OCCURENCES = "OCCURENCES_REQUEST";
    public static final String REQ_OCCURENCE_STATUS = "OCCURENCE_STATUS_REQUEST";
    public static final String REQ_OCCURENCE_TYPE = "OCCURENCE_TYPE_REQUEST";
    public static final String REQ_HELP = "HELP_REQUEST";
    public static final String REQ_EVENTS = "EVENTS_REQUEST";
    public static final String REQ_EVENTS_STATUS = "EVENTS_STATUS_REQUEST";
    public static final String REQ_DEPARTMENT = "DEPARTMENT_REQUEST";
    public static final String REQ_AGENT = "AGENT_REQUEST";
    public static final String REQ_CALL = "CALL_REQUEST";
    public static final String REQ_DISTRICT = "DISTRICT_REQUEST";
    public static final String REQ_ENTITY = "ENTITY_REQUEST";
    public static final String REQ_ENTITY_TYPES = "ENTITY_TYPES_REQUEST";
    public static final String REQ_PROVINCE = "PROVINCE_REQUEST";
    public static final String REQ_ = "_REQUEST";

}