package br.com.picarauto.model.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -5611894968496351778L;
    private final String title;
    private final Severity severity;
    private final String motive;

    public BaseException(String title, String message, Severity severity, String motive) {
        super(message);
        this.title = title;
        this.severity = severity;
        this.motive = motive;
    }

    public String getTitle() { return title; }
    public Severity getSeverity() { return severity; }
    public String getMotive() { return motive; }
}