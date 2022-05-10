package com.yil.adress.repository;

import com.yil.adress.model.CountryCity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCityRepository extends JpaRepository<CountryCity, CountryCity.CountryCityPk> {

    Page<CountryCity> findAllById_Country_Id(Pageable pageable, long countryId);

    void deleteById_City_IdAndId_Country_Id(long cityId, long countryId);
}
