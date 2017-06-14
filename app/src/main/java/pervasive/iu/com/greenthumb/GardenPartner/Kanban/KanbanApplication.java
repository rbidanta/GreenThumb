package pervasive.iu.com.greenthumb.GardenPartner.Kanban;

import android.app.Application;

import pervasive.iu.com.greenthumb.DBHandler.DataBaseManager;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class KanbanApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        DataBaseManager.setContext(this);
    }

}
