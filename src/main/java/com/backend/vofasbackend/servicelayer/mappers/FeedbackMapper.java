package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.FeedbackDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.SentimentAnalysisDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.TranscriptionDTO;

public class FeedbackMapper {
    public static FeedbackDTO mapFeedbackEntityToFeedbackDTO(FeedbackEntity feedbackEntity, FeedbackDTO feedbackDTO, TranscriptionDTO transcriptionDTO, SentimentAnalysisDTO sentimentAnalysisDTO){
        feedbackDTO.setFeedbackId(feedbackEntity.getFeedbackId());
        feedbackDTO.setFilePath(feedbackEntity.getFilePath());
        feedbackDTO.setFeedbackState(feedbackEntity.getFeedbackState().toString());
        if(feedbackEntity.getTranscription() != null){
            feedbackDTO.setTranscriptionDTO(TranscriptionMapper.mapTranscriptionEntityToTranscriptionDTO(feedbackEntity.getTranscription(),transcriptionDTO));
        }
        if(feedbackEntity.getSentimentAnalysis() != null){
            feedbackDTO.setSentimentAnalysisDTO(SentimentAnalysisMapper.mapSentimentAnalysisEntityToSentimentAnalysisDTO(feedbackEntity.getSentimentAnalysis(),sentimentAnalysisDTO));
        }
        return feedbackDTO;
    }
}
