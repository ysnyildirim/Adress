package com.yil.adress.repository;

import com.yil.adress.model.InteriorDoor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteriorDoorRepository extends JpaRepository<InteriorDoor, Long> {
    Page<InteriorDoor> findByExteriorDoorId(Pageable pageable, Long exteriorDoorId);
}
