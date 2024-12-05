package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for managing feedback entities. Extends {@link ReactiveCrudRepository} for basic CRUD
 * operations and {@link PagingAndSortingRepository} to support pagination and sorting.
 * Provides methods to save, delete, find, and paginate {@link FeedbackEntity} records.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long>, PagingAndSortingRepository<FeedbackEntity, Long> {
    Optional<FeedbackEntity> getFeedbackEntityByFeedbackId(Long feedbackId);
}

