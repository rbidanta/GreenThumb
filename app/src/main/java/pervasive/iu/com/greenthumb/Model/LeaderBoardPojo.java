package pervasive.iu.com.greenthumb.Model;

/**
 * Created by madrinathapa on 4/18/17.
 */

public class LeaderBoardPojo {
    String firstName = "";
    String lastName = "";
    long rewardpoints = 0;
    String uid = "";
    int position = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getRewardpoints() {
        return rewardpoints;
    }

    public void setRewardpoints(long rewardpoints) {
        this.rewardpoints = rewardpoints;
    }
}
