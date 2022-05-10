package com.yil.adress.repository;

import com.yil.adress.model.ExteriorDoorInteriorDoor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExteriorDoorInteriorDoorRepository extends JpaRepository<ExteriorDoorInteriorDoor, ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk> {
    Page<ExteriorDoorInteriorDoor> findAllById_ExteriorDoor_Id(Pageable pageable, long exteriorDoorId);

    void deleteById_ExteriorDoor_IdAndId_InteriorDoor_Id(long exteriorDoorId, long interiorDoorId);
}
