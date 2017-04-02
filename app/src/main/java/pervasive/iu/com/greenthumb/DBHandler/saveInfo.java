package pervasive.iu.com.greenthumb.DBHandler;

/**
 * Created by deepakasinathan on 3/19/17.
 */

public class saveInfo {
    public String firstname;
    public String lastname;
    public String address;
    public String location;
    public String phone;
    public saveInfo()
    {

    }

    public saveInfo(String firstname, String lastname, String address, String location, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.location = location;
        this.phone = phone;
    }
}
