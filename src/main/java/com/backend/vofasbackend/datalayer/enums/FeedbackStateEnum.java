package com.backend.vofasbackend.datalayer.enums;

import lombok.Getter;

/**
 * Enum representing the processing states of feedback.
 * Tracks the various stages in the feedback lifecycle, from reception to final processing.
 *
 * <ul>
 *   <li>{@link #RECEIVED}: Feedback has been received and is awaiting transcription.</li>
 *   <li>{@link #WAITING_FOR_TRANSCRIPTION}: Feedback is waiting to be transcribed.</li>
 *   <li>{@link #TRANSCRIBED}: Feedback has been transcribed and is awaiting sentiment analysis.</li>
 *   <li>{@link #WAITING_FOR_SENTIMENT_ANALYSIS}: Transcribed feedback is waiting for sentiment analysis.</li>
 *   <li>{@link #COMPLETED}: Feedback processing is complete, including transcription and sentiment analysis.</li>
 * </ul>
 */
@Getter
public enum FeedbackStateEnum {

    /**
     * Feedback has been received and is awaiting transcription or analysis.
     */
    RECEIVED("RECEIVED"),

    /**
     * Feedback is waiting to be transcribed.
     */
    WAITING_FOR_TRANSCRIPTION("WAITING_FOR_TRANSCRIPTION"),

    /**
     * Feedback has been successfully transcribed.
     */
    TRANSCRIBED("TRANSCRIBED"),

    /**
     * Transcribed feedback is waiting for sentiment analysis.
     */
    WAITING_FOR_SENTIMENT_ANALYSIS("WAITING_FOR_SENTIMENT_ANALYSIS"),

    /**
     * Feedback processing is complete, including transcription and sentiment analysis.
     */
    COMPLETED("COMPLETED");

    private final String state;
    FeedbackStateEnum(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }

}
