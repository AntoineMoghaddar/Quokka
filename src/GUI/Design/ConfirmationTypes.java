package GUI.Design;

/**
 * @author Moose.
 * @Definition Acknowledge forms for accepting log messages of declining them
 */
public enum ConfirmationTypes {
    ACK, DEC;

    private static ConfirmationTypes[] vals = values();

    public ConfirmationTypes next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
