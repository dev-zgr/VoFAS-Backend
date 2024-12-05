package com.backend.vofasbackend.servicelayer.mappers;

import com.backend.vofasbackend.datalayer.entities.KioskEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.KioskDTO;

public class KioskMapper {
    public static KioskDTO mapKioskEntityToKioskDTO(KioskEntity kioskEntity, KioskDTO kioskDTO) {
        kioskDTO.setKioskId(kioskEntity.getKioskID());
        kioskDTO.setKioskName(kioskEntity.getKioskName());
        kioskDTO.setKioskDescription(kioskEntity.getKioskDescription());
        kioskDTO.setKioskState(kioskEntity.getKioskStateEnum().toString());
        return kioskDTO;
    }
}
