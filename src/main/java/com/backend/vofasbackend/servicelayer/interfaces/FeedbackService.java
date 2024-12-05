package com.backend.vofasbackend.servicelayer.interfaces;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.exceptions.exceptions.UnsupportedMediaTypeException;
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
}
