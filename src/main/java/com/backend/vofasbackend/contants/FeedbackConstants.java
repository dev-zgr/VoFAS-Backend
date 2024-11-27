package com.backend.vofasbackend.contants;

/**
 * A utility class that holds constant values representing HTTP status codes and their corresponding messages.
 * This class is used to provide standardized status codes (e.g., 200, 404, 500) and associated messages for HTTP responses
 * throughout the application.
 */
public final class FeedbackConstants {

    public static final String STATUS_200 = "200";
    public static final String STATUS_400 = "400";
    public static final String STATUS_404 = "404";
    public static final String STATUS_500 = "500";

    public static final String MESSAGE_200 = "Request successful";
    public static final String MESSAGE_400 = "Bad Request";
    public static final String MESSAGE_404 = "Feedback Not Found";
    public static final String MESSAGE_500 = "Internal Server Error";

    private FeedbackConstants() {
    }
}
