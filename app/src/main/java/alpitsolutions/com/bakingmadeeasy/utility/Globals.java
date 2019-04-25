package alpitsolutions.com.bakingmadeeasy.utility;

public class Globals  {

    private static Globals mInstance;

    // variables for the auto-open recipe functionality
    // launched from an widget
    public Boolean mIsActiveAutoLoadRecipe;
    public Integer mIdAutoLoadRecipe;

    static {
        mInstance = new Globals();
    }

    private Globals() {
        mIsActiveAutoLoadRecipe = false;
        mIdAutoLoadRecipe = -1;
    }

    public static Globals getInstance() {
        return Globals.mInstance;
    }

}
