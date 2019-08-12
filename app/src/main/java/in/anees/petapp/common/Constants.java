package in.anees.petapp.common;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class Constants {

    private Constants() {
        // This utility class is not publicly instantiable
    }

    public static final String BASE_URL = "https://api.myjson.com/bins/";
    public static final String PET_LIST_URL = "https://api.myjson.com/bins/qq2b2";
    public static final String CONFIG_URL = "https://api.myjson.com/bins/mk12m";

//    public static final String PET_LIST_URL = "https://api.myjson.com/bins/xpdga";// Long list
//    public static final String PET_LIST_URL = "https://api.myjson.com/bins/rwtqi";// Empty pet list

//    public static final String CONFIG_URL = "https://api.myjson.com/bins/xdqe6";//Till 22:00
//    public static final String CONFIG_URL = "https://api.myjson.com/bins/xz5zy";//Till 22:00 and chat disabled

    public static final int CACHE_SIZE = 5 * 1024 * 1024;
    public static final String CACHE_DIR = "cachedir";
    public static final int MAX_STALE = 60 * 60 * 24 * 7; // Tolerate 7 days stale
    public static final int MAX_AGE = 60 * 30; // Read from cache for 30 minutes
}
