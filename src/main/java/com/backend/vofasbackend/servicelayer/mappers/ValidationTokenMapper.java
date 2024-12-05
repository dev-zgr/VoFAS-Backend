package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.ValidationTokenEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ValidationTokenDTO;

public class ValidationTokenMapper {
    public static ValidationTokenDTO mapValidationTokenEntityToValidationTokenDTO(ValidationTokenEntity validationTokenEntity, ValidationTokenDTO validationTokenDTO) {
        validationTokenDTO.setValidationToken(validationTokenEntity.getValidationToken().toString());
        validationTokenDTO.setValidationTokenState(validationTokenEntity.getValidationTokenStateEnum().toString());
        validationTokenDTO.setTokenCreateAt(validationTokenEntity.getTokenCreateAt());
        validationTokenDTO.setTokenUsedAt(validationTokenDTO.getTokenUsedAt());
        validationTokenDTO.setKiosk(validationTokenEntity.getParentKiosk().getKioskID());
        return validationTokenDTO;
    }
}
