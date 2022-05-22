package com.yil.adress.repository;

import com.yil.adress.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Page<Country> findAllByDeletedTimeIsNull(Pageable pageable);

    Country findByIdAndDeletedTimeIsNull(Long id);
}
