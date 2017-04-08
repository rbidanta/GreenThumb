package pervasive.iu.com.greenthumb.Utility;

import java.io.File;

/**
 * Created by Rashmi on 3/17/17.
 */
abstract public class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}