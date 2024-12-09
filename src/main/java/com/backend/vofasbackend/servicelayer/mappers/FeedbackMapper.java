package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.FeedbackDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.SentimentAnalysisDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.TranscriptionDTO;

import java.time.LocalTime;

public class FeedbackMapper {
    public static FeedbackDTO mapFeedbackEntityToFeedbackDTO(FeedbackEntity feedbackEntity, FeedbackDTO feedbackDTO, TranscriptionDTO transcriptionDTO, SentimentAnalysisDTO sentimentAnalysisDTO){
        feedbackDTO.setFeedbackId(feedbackEntity.getFeedbackID());
        feedbackDTO.setFilePath(feedbackEntity.getFilePath());
        feedbackDTO.setFileHash(feedbackEntity.getFile_hash());
        feedbackDTO.setFeedbackState(feedbackEntity.getFeedbackState().toString());
        feedbackDTO.setFeedbackReceivedAt(feedbackEntity.getFeedbackReceivedAt());
        feedbackDTO.setFeedbackDuration(feedbackEntity.getFeedbackDuration() != null ? LocalTime.ofSecondOfDay(feedbackEntity.getFeedbackDuration().getSeconds()) : LocalTime.ofSecondOfDay(0));
        if(feedbackEntity.getTranscription() != null){
            feedbackDTO.setTranscriptionDTO(TranscriptionMapper.mapTranscriptionEntityToTranscriptionDTO(feedbackEntity.getTranscription(),transcriptionDTO));
        }
        if(feedbackEntity.getSentimentAnalysis() != null){
            feedbackDTO.setSentimentAnalysisDTO(SentimentAnalysisMapper.mapSentimentAnalysisEntityToSentimentAnalysisDTO(feedbackEntity.getSentimentAnalysis(),sentimentAnalysisDTO));
        }
        return feedbackDTO;
    }
}
