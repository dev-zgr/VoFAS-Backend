package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the processing states of feedback.
 * This enum tracks the various stages in the processing lifecycle of feedback.
 *
 *   <ul>
 *     <li>FEEDBACK_RECEIVED: Feedback has just received and is awaiting to be transcribed to text.</li>
   *      <li>FEEDBACK_PROCESSING: Feedback is currently being processed (e.g., transcribed or analyzed).</li>
   *      <li>FEEDBACK_TRANCRIBED: Feedback has been transcribed and awaiting for sentiment analysis(e.g., from audio to text).</li>
          <li>FEEDBACK_READY: Feedback processing is complete all stt & sentiment operations are has performed</li>
   *  </ul>
 */
public enum ProcessingStateEnum {
    /**
     * Feedback has been received and is awaiting further processing.
     */
    FEEDBACK_RECEIVED,

    /**
     * Feedback is in the process of being handled (e.g., transcribed or analyzed).
     */
    FEEDBACK_PROCESSING,

    /**
     * Feedback has been successfully transcribed, such as audio-to-text conversion.
     */
    FEEDBACK_TRANCRIBED,

    /**
     * Feedback processing is complete and is ready for subsequent actions.
     */
    FEEDBACK_READY
}
