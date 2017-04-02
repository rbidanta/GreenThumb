package pervasive.iu.com.greenthumb.Model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class Plants {
    private long PlantId;
    private long KitId;
    private String PlantName;
    private String ImageName;
    private String Location;
    private int NotificationTime;
    private Date LastModified;
    private HashMap<String, String> ThresholdValues;

    public long getPlantId() {
        return PlantId;
    }

    public void setPlantId(long plantId){
        PlantId = plantId;
    }

    public long getKitId() {
        return KitId;
    }

    public void setKitId(long kitId){
        KitId = kitId;
    }

    public void setPlantName(String plantName){
        PlantName = plantName;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setImageName(String imageName){
        ImageName = imageName;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setLocation(String location){
        Location = location;
    }

    public String getLocation() {
        return Location;
    }

    public void setNotificationTime(int notificationTime){
        NotificationTime = notificationTime;
    }

    public int getNotificationTime() {
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

    public Plants(long kitId, String plantName, String imageName, String location,
                  int notificationTime, Date lastModified, HashMap<String, String> thresholdValues) {
        this.KitId = kitId;
        this.PlantName = plantName;
        this.ImageName = imageName;
        this.Location = location;
        this.NotificationTime = notificationTime;
        this.LastModified = lastModified;
        this.ThresholdValues = thresholdValues;
    }
}