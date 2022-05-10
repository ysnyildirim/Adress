package com.yil.adress.repository;

import com.yil.adress.model.StreetExteriorDoor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetExteriorDoorRepository extends JpaRepository<StreetExteriorDoor, StreetExteriorDoor.StreetExteriorDoorPk> {
    Page<StreetExteriorDoor> findAllById_Street_Id(Pageable pageable, long streetId);

    void deleteById_Street_IdAndId_ExteriorDoor_Id(long streetId, long exteriorDoorId);
}
