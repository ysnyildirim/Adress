package com.yil.adress.repository;

import com.yil.adress.model.InteriorDoor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteriorDoorRepository extends JpaRepository<InteriorDoor, Long> {

    Page<InteriorDoor> findAllByDeletedTimeIsNull(Pageable pageable);

    InteriorDoor findByIdAndDeletedTimeIsNull(Long id);

    Page<InteriorDoor> findByExteriorDoorIdAndDeletedTimeIsNull(Pageable pageable, Long exteriorDoorId);
}
