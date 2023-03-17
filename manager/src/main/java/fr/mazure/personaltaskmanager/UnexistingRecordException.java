package fr.mazure.personaltaskmanager;

/**
 * Exception when trying to manipulate a non-existing record
 */
public class UnexistingRecordException extends Exception {

    public UnexistingRecordException(final String message) {
        super(message);
    }
}
