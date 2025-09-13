package com.example.taskmanager.error;

import java.time.OffsetDateTime;

public class ErrorResponse {
    private final String error;
    private final String message;
    private final int status;
    private final OffsetDateTime timestamp;

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = OffsetDateTime.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
