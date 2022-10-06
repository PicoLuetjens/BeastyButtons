package beastybuttons;

//package private class for own defined Runtime Errors
class CostumRuntimeError extends RuntimeException{
    // Parameterless Constructor
    public CostumRuntimeError() {}

    // Constructor that accepts a message
    public CostumRuntimeError(String message)
    {
        super(message);
    }
}

