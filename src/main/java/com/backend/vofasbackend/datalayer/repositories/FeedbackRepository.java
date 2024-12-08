package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.datalayer.entities.KioskEntity;
import com.backend.vofasbackend.datalayer.enums.FeedbackStateEnum;
import com.backend.vofasbackend.datalayer.enums.SentimentStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Repository for managing feedback entities. Extends {@link ReactiveCrudRepository} for basic CRUD
 * operations and {@link PagingAndSortingRepository} to support pagination and sorting.
 * Provides methods to save, delete, find, and paginate {@link FeedbackEntity} records.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long>, PagingAndSortingRepository<FeedbackEntity, Long> {
    Optional<FeedbackEntity> getFeedbackEntityByFeedbackID(Long feedbackID);

    @Query("SELECT f FROM FeedbackEntity f " +
            "LEFT JOIN f.sentimentAnalysis s " +
            "WHERE (:feedbackState IS NULL OR f.feedbackState = :feedbackState) " +
            "AND (:feedbackSource IS NULL OR f.feedbackSource = :feedbackSource) " +
            "AND (:sentimentState IS NULL OR s.sentimentState = :sentimentState) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR f.feedbackReceivedAt BETWEEN :startDate AND :endDate)")
    Page<FeedbackEntity> findFeedbacksByCriteria(
            @Param("feedbackState") FeedbackStateEnum feedbackState,
            @Param("feedbackSource") KioskEntity feedbackSource,
            @Param("sentimentState") SentimentStateEnum sentimentState,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

}

