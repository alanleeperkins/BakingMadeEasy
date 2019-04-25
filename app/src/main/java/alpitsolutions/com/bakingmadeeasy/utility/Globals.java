package alpitsolutions.com.bakingmadeeasy.utility;

public class Globals  {

    private static Globals instance;

    // variables for the auto-open recipe functionality
    // launched from an widget
    public Boolean isActiveAutoLoadRecipe;
    public Integer idAutoLoadRecipe;

    static {
        instance = new Globals();
    }

    private Globals() {
        isActiveAutoLoadRecipe = false;
        idAutoLoadRecipe = -1;
    }

    public static Globals getInstance() {
        return Globals.instance;
    }

}
