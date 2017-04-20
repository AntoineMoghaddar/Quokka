package GUI.Main;

/**
 * @author Moose.
 */
public class QuokkaModel {

    private static QuokkaModel instance;

    private QuokkaModel() {
    }

    public static QuokkaModel getInstance() {
        if (instance == null) {
            instance = new QuokkaModel();
        }
        return instance;
    }

}
