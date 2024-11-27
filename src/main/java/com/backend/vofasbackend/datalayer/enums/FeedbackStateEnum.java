package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the possible states of feedback.
 * This enum categorizes feedback into three distinct states based on the sentiment of the feedback.
 * It is used to track the sentiment analysis result for a given piece of feedback.
 *
 * - POSITIVE: Indicates that the feedback is positive or favorable.
 * - NEUTRAL: Indicates that the feedback is neutral, neither positive nor negative.
 * - NEGATIVE: Indicates that the feedback is negative or unfavorable.
 */
public enum FeedbackStateEnum {
    /**
     * Feedback with a positive sentiment.
     */
    POSITIVE,

    /**
     * Feedback with a neutral sentiment, neither positive nor negative.
     */
    NEUTRAL,

    /**
     * Feedback with a negative sentiment.
     */
    NEGATIVE
}
