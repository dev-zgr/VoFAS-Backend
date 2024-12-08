package com.backend.vofasbackend.servicelayer.interfaces;

import com.backend.vofasbackend.exceptions.exceptions.InvalidFilterOptionException;
import com.backend.vofasbackend.exceptions.exceptions.ResourceNotFoundException;
import com.backend.vofasbackend.exceptions.exceptions.UnsupportedMediaTypeException;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.FeedbackDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FeedbackService {

    /**
     * This method just saves the feedback audio file to database, sets required fields in the FeedBackEntity & sends
     * audio file to openAI for transcription
     *
     * @param file that'll be saved to databased
     */
    Mono<Void> saveFeedback(MultipartFile file, UUID validationToken) throws UnsupportedMediaTypeException;

    Flux<FeedbackDTO> getFeedbackStream();

    /**
     * Retrieves feedback details by ID.
     *
     * @param feedbackID        the unique ID of the feedback
     * @param getFeedbackSource true to include source details, false otherwise
     * @param getValidation     true to validate the feedback, false otherwise
     * @return FeedbackDTO containing feedback information
     * @throws ResourceNotFoundException if feedback with the given ID is not found
     */
    FeedbackDTO getFeedbackById(Long feedbackID, boolean getFeedbackSource, boolean getValidation) throws ResourceNotFoundException;

    /**
     * Retrieves a paginated and optionally sorted list of feedbacks with filtering options.
     *
     * @param pageNumber   the page number to retrieve (zero-based)
     * @param sortBy       the field to sort by
     * @param ascending    true for ascending order, false for descending order
     * @param startDate    the start date for filtering feedbacks
     * @param endDate      the end date for filtering feedbacks
     * @param feedbackState the state of the feedback to filter
     * @param sentimentState       additional keyword-based filter (e.g., "feedbackState:positive")
     * @return a paginated list of {@link FeedbackDTO} objects
     * @throws InvalidFilterOptionException if the filter option is invalid
     */
    Page<FeedbackDTO> getFeedbacks(int pageNumber, String sortBy, boolean ascending, String startDate, String endDate, String feedbackState, String sentimentState) throws InvalidFilterOptionException;

}
