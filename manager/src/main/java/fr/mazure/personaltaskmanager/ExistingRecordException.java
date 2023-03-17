package fr.mazure.personaltaskmanager;

/**
 * Exception when trying to create an already existing record
 */
public class ExistingRecordException  extends Exception {

    public ExistingRecordException(final String message) {
        super(message);
    }
}
