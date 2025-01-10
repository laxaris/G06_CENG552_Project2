package banking;

public class Status {
    private boolean success;
    private boolean invalidPIN;
    private String message;

    // Private constructor for controlled instantiation
    private Status(boolean success, boolean invalidPIN, String message) {
        this.success = success;
        this.invalidPIN = invalidPIN;
        this.message = message;
    }

    // Factory methods for creating Status instances
    public static Status success() {
        return new Status(true, false, "Operation successful.");
    }



    public static Status failure(String message) {
        return new Status(false, false, message);
    }

    // Methods for status checking
    public boolean isSuccess() {
        return success;
    }

    public boolean isInvalidPIN() {
        return invalidPIN;
    }

    public String getMessage() {
        return message;
    }

    // ToString override for better logging and display
    @Override
    public String toString() {
        if (success)
            return "SUCCESS: " + message;

        else
            return "FAILURE: " + message;
    }
}
