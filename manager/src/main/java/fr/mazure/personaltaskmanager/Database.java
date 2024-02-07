package fr.mazure.personaltaskmanager;

public class Database {

    static TaskDataAccess access;

    public static void initialize() {
        if (access != null) {
            throw new IllegalStateException("Database has already been initialized");
        }

        access = new TaskDataAccess();
    }

    public static void reset() {
        if (access == null) {
            throw new IllegalStateException("Database is not initialized");
        }
        access = null;
    }

    public static TaskDataAccess get() {
        return access;
    }
}
