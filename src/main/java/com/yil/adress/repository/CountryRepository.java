package com.yil.adress.repository;

import com.yil.adress.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Page<Country> findAllByCode(Pageable pageable, String code);

    Page<Country> findAllByName(Pageable pageable, String name);

    Page<Country> findAllByNameAndCode(Pageable pageable, String name, String code);
}
