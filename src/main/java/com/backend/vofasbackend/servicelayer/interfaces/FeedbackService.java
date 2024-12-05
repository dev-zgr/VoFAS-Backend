package com.backend.vofasbackend.servicelayer.interfaces;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.exceptions.exceptions.ResourceNotFoundException;
import com.backend.vofasbackend.exceptions.exceptions.UnsupportedMediaTypeException;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.FeedbackDTO;
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
     * @return true if operation performed succesfully
     */
    Mono<Void> saveFeedback(MultipartFile file, UUID validationToken) throws UnsupportedMediaTypeException;

    Flux<FeedbackEntity> getFeedbackStream();

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
}
