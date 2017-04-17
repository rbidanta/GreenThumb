package pervasive.iu.com.greenthumb.DBHandler;

import java.util.List;

import pervasive.iu.com.greenthumb.Model.Plants;

/**
 * Created by deepakasinathan on 3/19/17.
 */

public class saveInfo {
    public String firstname;
    public String lastname;
    public String address;
    public String location;
    public String phone;
    public List<Plants> Plants;

    public saveInfo()
    {

    }

    public saveInfo(String firstname, String lastname, String address, String location, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.location = location;
        this.phone = phone;
     //   this.Plants = plants;
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
}
