package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.datalayer.entities.KioskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing kiosk entities. Extends {@link JpaRepository} for CRUD operations
 * and {@link PagingAndSortingRepository} for pagination and sorting support.
 * Provides methods to save, delete, find, and paginate {@link KioskEntity} records.
 */
@Repository
public interface KioskRepository extends JpaRepository<KioskEntity, Long>, PagingAndSortingRepository<KioskEntity, Long> {
}
