package com.yil.adress.service;

import com.yil.adress.model.ExteriorDoorInteriorDoor;
import com.yil.adress.repository.ExteriorDoorInteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ExteriorDoorInteriorDoorService {
    private final ExteriorDoorInteriorDoorRepository exteriorDoorInteriorDoorRepository;

    @Autowired
    public ExteriorDoorInteriorDoorService(ExteriorDoorInteriorDoorRepository exteriorDoorInteriorDoorRepository) {
        this.exteriorDoorInteriorDoorRepository = exteriorDoorInteriorDoorRepository;
    }

    public ExteriorDoorInteriorDoor save(ExteriorDoorInteriorDoor exteriorDoorInteriorDoor) {
        return exteriorDoorInteriorDoorRepository.save(exteriorDoorInteriorDoor);
    }

    public void delete(ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk id) {
        exteriorDoorInteriorDoorRepository.deleteById(id);
    }

    public ExteriorDoorInteriorDoor findById(ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk id) throws EntityNotFoundException {
        return exteriorDoorInteriorDoorRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Entity not found");
        });
    }

    public Page<ExteriorDoorInteriorDoor> findAllByExteriorDoorId(Pageable pageable, long exteriorDoorId) {
        return exteriorDoorInteriorDoorRepository.findAllById_ExteriorDoor_Id(pageable, exteriorDoorId);
    }

    public void delete(long exteriorDoorId, long interiorDoorId) {
        exteriorDoorInteriorDoorRepository.deleteById_ExteriorDoor_IdAndId_InteriorDoor_Id(exteriorDoorId, interiorDoorId);
    }
}
