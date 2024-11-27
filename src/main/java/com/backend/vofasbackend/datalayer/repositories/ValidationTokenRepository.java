package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.ValidationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Repository for managing validation token entities. Provides methods to save, delete, and find
 * {@link ValidationTokenEntity} records. This interface will be implemented by Spring Data JPA
 * to handle the persistence operations.
 */
public interface ValidationTokenRepository extends JpaRepository<ValidationTokenEntity, UUID>, PagingAndSortingRepository<ValidationTokenEntity,UUID> {
}
