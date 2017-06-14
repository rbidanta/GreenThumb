package pervasive.iu.com.greenthumb.Model;

import java.io.Serializable;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class TasksPOJO implements Serializable{

    private int sno = 0;
    private String taskDescription = "";
    private String taskPriority = "";
    private String taskDeadLine = "" ;
    private String taskAssignedTo = "";
    private String taskstatus = "";
    private String gardenname = "";
    private String gardenid = "";
    private String plotid = "";
    private String plotname = "";
    private String taskID = "";

    public TasksPOJO(){

    }

    public TasksPOJO(int sno, String taskDescription, String taskPriority, String taskDeadLine,
                     String taskAssignedTo, String taskstatus, String gardenname,String gardenid, String plotid, String plotname, String taskID){
        this.sno = sno;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.taskDeadLine = taskDeadLine;
        this.taskAssignedTo = taskAssignedTo;
        this.taskstatus = taskstatus;
        this.gardenname = gardenname;
        this.gardenid = gardenid;
        this.plotid = plotid;
        this.plotname = plotname;
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskDeadLine() {
        return taskDeadLine;
    }

    public void setTaskDeadLine(String taskDeadLine) {
        this.taskDeadLine = taskDeadLine;
    }

    public String getTaskAssignedTo() {
        return taskAssignedTo;
    }

    public void setTaskAssignedTo(String taskAssignedTo) {
        this.taskAssignedTo = taskAssignedTo;
    }

    public String getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(String taskstatus) {
        this.taskstatus = taskstatus;
    }

    public String getGardenname() {
        return gardenname;
    }

    public void setGardenname(String gardenname) {
        this.gardenname = gardenname;
    }

    public String getGardenid() {
        return gardenid;
    }

    public void setGardenid(String gardenid) {
        this.gardenid = gardenid;
    }

    public String getPlotid() {
        return plotid;
    }

    public void setPlotid(String plotid) {
        this.plotid = plotid;
    }

    public String getPlotname() {
        return plotname;
    }

    public void setPlotname(String plotname) {
        this.plotname = plotname;
    }
}
