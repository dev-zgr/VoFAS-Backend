package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.TranscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link TranscriptionEntity}, providing CRUD and pagination/sorting operations.
 */
@Repository
public interface TranscriptionRepository extends JpaRepository<TranscriptionEntity, Long>, PagingAndSortingRepository<TranscriptionEntity, Long> {
}
