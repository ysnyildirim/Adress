package com.yil.adress.repository;

import com.yil.adress.model.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    Page<District> findAllByNameAndDeletedTimeIsNull(Pageable pageable, String name);

    Page<District> findAllByDeletedTimeIsNull(Pageable pageable);
}
