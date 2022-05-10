package com.yil.adress.repository;

import com.yil.adress.model.CityDistrict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDistrictRepository extends JpaRepository<CityDistrict, CityDistrict.CityDistrictPk> {
    Page<CityDistrict> findAllById_City_Id(Pageable pageable, long cityId);

}
