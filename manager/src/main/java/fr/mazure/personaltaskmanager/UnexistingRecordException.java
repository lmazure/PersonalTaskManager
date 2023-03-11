package fr.mazure.personaltaskmanager;

public class UnexistingRecordException extends Exception {

    public UnexistingRecordException(final String message) {
        super(message);
    }
}
