package com.yil.adress.repository;

import com.yil.adress.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findAllByCountryId(Pageable pageable, Long countryId);
}
