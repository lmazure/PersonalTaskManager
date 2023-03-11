package fr.mazure.personaltaskmanager;

public class ExistingRecordException  extends Exception {

    public ExistingRecordException(final String message) {
        super(message);
    }
}
