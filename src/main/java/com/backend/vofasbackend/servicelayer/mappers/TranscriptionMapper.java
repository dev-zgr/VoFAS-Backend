package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.TranscriptionEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.TranscriptionDTO;

public class TranscriptionMapper {
    public static TranscriptionDTO mapTranscriptionEntityToTranscriptionDTO(TranscriptionEntity transcriptionEntity, TranscriptionDTO transcriptionDTO) {
        transcriptionDTO.setTranscription(transcriptionEntity.getTranscription());
        transcriptionDTO.setTranscriptionRequestedAt(transcriptionEntity.getTranscriptionRequestedAt());
        transcriptionDTO.setTranscriptionReceivedAt(transcriptionEntity.getTranscriptionReceivedAt());
        return transcriptionDTO;
    }
}
