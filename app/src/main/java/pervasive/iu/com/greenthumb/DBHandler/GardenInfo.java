package pervasive.iu.com.greenthumb.DBHandler;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rashmi on 4/2/17.
 */

public class GardenInfo implements Serializable {

    public String gName;
    public String gId;
    public String gAddress;
    public String gOwner;
    public String gOwnerPhone;
    public List<String> gMembers;
    public String gImagePath;
    public String gFireBasePath;

    public GardenInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(GardenInfo.class)
    }

    public GardenInfo(String gId, String gName, String gAddress,String gOwner, String gOwnerPhone, List<String> gMembers, String gImagePath,String gFireBasePath) {
        this.gId = gId;
        this.gName = gName;
        this.gAddress = gAddress;
        this.gOwner = gOwner;
        this.gOwnerPhone = gOwnerPhone;
        this.gMembers = gMembers;
        this.gImagePath = gImagePath;
        this.gFireBasePath = gFireBasePath;
    }


    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }


    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgAddress() {
        return gAddress;
    }

    public void setgAddress(String gAddress) {
        this.gAddress = gAddress;
    }

    public String getgOwner() {
        return gOwner;
    }

    public void setgOwner(String gOwner) {
        this.gOwner = gOwner;
    }

    public String getgOwnerPhone() {
        return gOwnerPhone;
    }

    public void setgOwnerPhone(String gOwnerPhone) {
        this.gOwnerPhone = gOwnerPhone;
    }

    public List<String> getgMembers() {
        return gMembers;
    }

    public void setgMembers(List<String> gMembers) {
        this.gMembers = gMembers;
    }

    public String getgImagePath() {
        return gImagePath;
    }

    public void setgImagePath(String gImagePath) {
        this.gImagePath = gImagePath;
    }

    public String getgFireBasePath() {
        return gFireBasePath;
    }

    public void setgFireBasePath(String gFireBasePath) {
        this.gFireBasePath = gFireBasePath;
    }

    /*public String getgMembers() {
        return gMembers;
    }

    public void setgMembers(String gMembers) {
        this.gMembers = gMembers;
    }*/
}
