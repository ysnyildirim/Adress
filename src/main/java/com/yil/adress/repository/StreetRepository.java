package com.yil.adress.repository;

import com.yil.adress.model.Street;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    Page<Street> findAllByDistrictId(Pageable pageable, Long district);
}
