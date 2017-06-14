package pervasive.iu.com.greenthumb.Model;

import java.io.Serializable;


/**
 * Created by Rashmi on 4/16/17.
 */

public class TasksInfo implements Serializable{

    //public String gName;
    public String taskId;

    public String taskname;
    public long deadline;
    public String assignedto;
    public String completedby;

    public TasksInfo(){

    }

    public TasksInfo(String taskId, String taskname, long deadline, String assignedto, String completedby) {
        this.taskId= taskId;
        this.taskname = taskname;
        this.deadline = deadline;
        this.assignedto = assignedto;
        this.completedby = completedby;

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(String assignedto) {
        this.assignedto = assignedto;
    }

    public String getCompletedby() {
        return completedby;
    }

    public void setCompletedby(String completedby) {
        this.completedby = completedby;
    }
}
