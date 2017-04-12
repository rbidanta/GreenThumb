package pervasive.iu.com.greenthumb.Model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class Plants {
    private String PlantId;
    private String KitId;
    private String UserId;
    private String PlantName;
    private String Location;
    private String NotificationTime;
    private Date LastModified;
    private HashMap<String, String> ThresholdValues;

    public String getPlantId() {
        return PlantId;
    }

    public void setPlantId(String plantId){
        PlantId = plantId;
    }

    public String getKitId() {
        return KitId;
    }

    public void setKitId(String kitId){
        KitId = kitId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId){
        UserId = userId;
    }

    public void setPlantName(String plantName){
        PlantName = plantName;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setLocation(String location){
        Location = location;
    }

    public String getLocation() {
        return Location;
    }

    public void setNotificationTime(String notificationTime){
        NotificationTime = notificationTime;
    }

    public String getNotificationTime() {
        return NotificationTime;
    }

    public void setLastModified(Date lastModified){
        LastModified = lastModified;
    }

    public Date getLastModified() {
        return LastModified;
    }

    public void setThresholdValues(HashMap<String, String>  threshold){
        ThresholdValues = threshold;
    }

    public HashMap<String, String> getThresholdValues() {
        return ThresholdValues;
    }

    public Plants(String plantId, String kitId, String userId, String plantName, String location,
                  String notificationTime, Date lastModified, HashMap<String, String> thresholdValues) {
        this.PlantId = plantId;
        this.KitId = kitId;
        this.UserId = userId;
        this.PlantName = plantName;
        this.Location = location;
        this.NotificationTime = notificationTime;
        this.LastModified = lastModified;
        this.ThresholdValues = thresholdValues;
    }
}