package pervasive.iu.com.greenthumb.DBHandler;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class DataBaseQuery {

    public static final String TABLE_TASKS = "TASKSDETAILS";

    public static final String S_NO = "SNO";
    public static final String TASK_DESCRIPTION = "TASKDESCRIPTION";
    public static final String TASK_PRIORITY = "TASKPRIORITY";
    public static final String TASK_DEADLINE = "TASKDEADLINE";
    public static final String TASK_ASSIGNEDTO = "TASKASSIGNEDTO";
    public static final String TASK_STATUS = "TASKSTATUS";
    public static final String GARDEN_NAME = "GARDENNAME";
    public static final String GARDEN_ID = "GARDENID";
    public static final String PLOT_ID = "PLOTID";
    public static final String PLOT_NAME = "PLOTNAME";

    public static final String CREATE_TABLE_TASKS = "CREATE TABLE "+TABLE_TASKS+" ("+S_NO +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TASK_DESCRIPTION + " TEXT, "+ TASK_PRIORITY +" TEXT, "+TASK_DEADLINE+" TEXT, "+TASK_ASSIGNEDTO +" TEXT, "+TASK_STATUS
            +" TEXT, "+GARDEN_NAME +" TEXT, "+GARDEN_ID +" TEXT, "+PLOT_ID +" TEXT,"+PLOT_NAME+" TEXT)";

}
