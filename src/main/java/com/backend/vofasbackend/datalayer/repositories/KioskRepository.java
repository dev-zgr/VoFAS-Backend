package com.backend.vofasbackend.datalayer.repositories;

import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.datalayer.entities.KioskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskRepository extends JpaRepository<KioskEntity, Long>, PagingAndSortingRepository<KioskEntity,Long> {

}