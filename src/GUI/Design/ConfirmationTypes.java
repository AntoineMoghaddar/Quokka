package GUI.Design;

/**
 * @author Moose.
 */
public enum ConfirmationTypes {
    ACK, DEC;

    private static ConfirmationTypes[] vals = values();

    public ConfirmationTypes next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
