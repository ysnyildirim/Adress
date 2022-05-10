package com.yil.adress.repository;

import com.yil.adress.model.DistrictStreet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictStreetRepository extends JpaRepository<DistrictStreet, DistrictStreet.DistrictStreetPk> {

    Page<DistrictStreet> findAllById_District_Id(Pageable pageable, Long id);

    void deleteById_District_IdAndAndId_Street_Id(long districtId, long streetId);
}
