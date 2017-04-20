package pervasive.iu.com.greenthumb.DBHandler;

import java.io.Serializable;

/**
 * Created by Rashmi on 4/17/17.
 */

public class MemberInfo implements Serializable {
    
    public String firstname;
    public String lastname;
    public String address;
    public String location;
    public String phone;
    public String userId;

    public MemberInfo()
    {

    }

    public MemberInfo(String firstname, String lastname, String address, String location, String phone, String userId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.location = location;
        this.phone = phone;
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
