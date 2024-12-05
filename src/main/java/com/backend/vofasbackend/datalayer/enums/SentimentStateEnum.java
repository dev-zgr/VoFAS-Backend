package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the sentiment classification of feedback.
 * Categorizes feedback into three possible sentiment states based on its analysis.
 *
 * <ul>
 *   <li>{@link #POSITIVE}: Indicates positive or favorable feedback.</li>
 *   <li>{@link #NEUTRAL}: Indicates neutral feedback, neither positive nor negative.</li>
 *   <li>{@link #NEGATIVE}: Indicates negative or unfavorable feedback.</li>
 * </ul>
 */
public enum SentimentStateEnum {

    /**
     * Feedback with a positive sentiment, indicating approval or satisfaction.
     */
    POSITIVE,

    /**
     * Feedback with a neutral sentiment, indicating no strong positive or negative opinion.
     */
    NEUTRAL,

    /**
     * Feedback with a negative sentiment, indicating dissatisfaction or disapproval.
     */
    NEGATIVE
}
