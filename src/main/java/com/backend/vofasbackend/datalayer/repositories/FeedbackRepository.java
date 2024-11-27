package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing feedback entities. Extends {@link JpaRepository} for basic CRUD
 * operations and {@link PagingAndSortingRepository} to support pagination and sorting.
 * Provides methods to save, delete, find, and paginate {@link FeedbackEntity} records.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long>, PagingAndSortingRepository<FeedbackEntity, Long> {
}

