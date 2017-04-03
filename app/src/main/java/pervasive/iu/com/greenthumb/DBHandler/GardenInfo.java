package pervasive.iu.com.greenthumb.DBHandler;

import java.util.List;

/**
 * Created by Rashmi on 4/2/17.
 */

public class GardenInfo {

    public String gName;
    public String gId;
    public String gAddress;
    public String gOwner;
    public String gOwnerPhone;
    //public String gMembers;
    public List<String> gMembers;

    public GardenInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(GardenInfo.class)
    }

    public GardenInfo(String gId, String gName, String gAddress,String gOwner, String gOwnerPhone, List<String> gMembers) {
        this.gId = gId;
        this.gName = gName;
        this.gAddress = gAddress;
        this.gOwner = gOwner;
        this.gOwnerPhone = gOwnerPhone;
        this.gMembers = gMembers;
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

    /*public String getgMembers() {
        return gMembers;
    }

    public void setgMembers(String gMembers) {
        this.gMembers = gMembers;
    }*/
}
