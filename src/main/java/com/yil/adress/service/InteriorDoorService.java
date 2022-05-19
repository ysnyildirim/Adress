package com.yil.adress.service;

import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.repository.InteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class InteriorDoorService {

    private final InteriorDoorRepository interiorDoorRepository;

    @Autowired
    public InteriorDoorService(InteriorDoorRepository interiorDoorRepository) {
        this.interiorDoorRepository = interiorDoorRepository;
    }

    public static InteriorDoorDto toDto(InteriorDoor entity) {
        if (entity == null)
            return InteriorDoorDto.builder().build();
        return InteriorDoorDto.builder().id(entity.getId())
                .name(entity.getName()).build();
    }

    public static InteriorDoor toEntity(InteriorDoorDto dto) {
        InteriorDoor interiorDoor = new InteriorDoor();
        if (dto == null)
            return interiorDoor;
        interiorDoor.setId(dto.getId());
        interiorDoor.setName(dto.getName());
        return interiorDoor;
    }

    public InteriorDoor save(InteriorDoor interiorDoor) {
        return interiorDoorRepository.save(interiorDoor);
    }

    public InteriorDoor findById(Long id) throws EntityNotFoundException {
        return interiorDoorRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Interior door not found");
        });
    }

    public Page<InteriorDoor> findAllByNameAndDeletedTimeIsNull(Pageable pageable, String name) {
        return interiorDoorRepository.findAllByNameAndDeletedTimeIsNull(pageable,name);
    }

    public Page<InteriorDoor> findAllByDeletedTimeIsNull(Pageable pageable) {
        return interiorDoorRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
