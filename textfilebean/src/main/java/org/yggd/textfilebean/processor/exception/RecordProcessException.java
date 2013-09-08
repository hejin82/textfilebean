package org.yggd.textfilebean.processor.exception;

public class RecordProcessException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4113428562505633116L;

    public RecordProcessException() {
        super();
    }

    public RecordProcessException(String message) {
        super(message);
    }

    public RecordProcessException(Throwable t) {
        super(t);
    }
}
