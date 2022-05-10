package com.yil.adress.service;

import com.yil.adress.model.StreetExteriorDoor;
import com.yil.adress.repository.StreetExteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StreetExteriorDoorService {
    private final StreetExteriorDoorRepository streetExteriorDoorRepository;

    @Autowired
    public StreetExteriorDoorService(StreetExteriorDoorRepository streetExteriorDoorRepository) {
        this.streetExteriorDoorRepository = streetExteriorDoorRepository;
    }


    public StreetExteriorDoor save(StreetExteriorDoor streetExteriorDoor) {
        return streetExteriorDoorRepository.save(streetExteriorDoor);
    }

    public void delete(StreetExteriorDoor.StreetExteriorDoorPk id) {
        streetExteriorDoorRepository.deleteById(id);
    }

    public StreetExteriorDoor findById(StreetExteriorDoor.StreetExteriorDoorPk id) throws EntityNotFoundException {
        return streetExteriorDoorRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Entity not found");
        });
    }

    public Page<StreetExteriorDoor> findAllByStreetId(Pageable pageable, long streetId) {
        return streetExteriorDoorRepository.findAllById_Street_Id(pageable, streetId);
    }

    public void delete(long streetId, long exteriorDoorId) {
        streetExteriorDoorRepository.deleteById_Street_IdAndId_ExteriorDoor_Id(streetId, exteriorDoorId);

    }
}
