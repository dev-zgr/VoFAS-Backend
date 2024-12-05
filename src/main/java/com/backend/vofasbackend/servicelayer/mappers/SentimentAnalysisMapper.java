package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.SentimentAnalysisEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.SentimentAnalysisDTO;

public class SentimentAnalysisMapper {
    public static SentimentAnalysisDTO mapSentimentAnalysisEntityToSentimentAnalysisDTO(SentimentAnalysisEntity sentimentAnalysisEntity, SentimentAnalysisDTO sentimentAnalysisDTO) {
        sentimentAnalysisDTO.setSentimentState(sentimentAnalysisEntity.getSentimentState().toString());
        sentimentAnalysisDTO.setAnalysisRequestedAt(sentimentAnalysisEntity.getAnalysisRequestedAt());
        sentimentAnalysisDTO.setAnalysisRequestedAt(sentimentAnalysisEntity.getAnalysisRequestedAt());
        return sentimentAnalysisDTO;
    }
}
