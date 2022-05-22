package com.yil.adress.repository;

import com.yil.adress.model.ExteriorDoor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExteriorDoorRepository extends JpaRepository<ExteriorDoor, Long> {

    Page<ExteriorDoor> findAllByDeletedTimeIsNull(Pageable pageable);

    ExteriorDoor findByIdAndDeletedTimeIsNull(Long id);

    Page<ExteriorDoor> findAllByStreetIdAndDeletedTimeIsNull(Pageable pageable, Long streetId);
}
