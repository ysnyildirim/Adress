package com.yil.adress.repository;

import com.yil.adress.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findByNameAndAndCode(String name, String code);

    Page<City> findAllByName(Pageable pageable, String name);

    Page<City> findAllByCode(Pageable pageable, String code);

    Page<City> findAllByNameAndCode(Pageable pageable, String name, String code);


}
