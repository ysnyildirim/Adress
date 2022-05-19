package com.yil.adress.service;

import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.repository.ExteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ExteriorDoorService {

    private final ExteriorDoorRepository exteriorDoorRepository;

    @Autowired
    public ExteriorDoorService(ExteriorDoorRepository exteriorDoorRepository) {
        this.exteriorDoorRepository = exteriorDoorRepository;
    }

    public static ExteriorDoorDto toDto(ExteriorDoor entity) {
        if (entity == null)
            return ExteriorDoorDto.builder().build();
        return ExteriorDoorDto.builder()
                .id(entity.getId())
                .name(entity.getName()).build();
    }

    public static ExteriorDoor toEntity(ExteriorDoorDto dto) {
        ExteriorDoor exteriorDoor = new ExteriorDoor();
        if (dto == null)
            return exteriorDoor;
        exteriorDoor.setId(dto.getId());
        exteriorDoor.setName(dto.getName());
        return exteriorDoor;
    }

    public ExteriorDoor save(ExteriorDoor exteriorDoor) {
        return exteriorDoorRepository.save(exteriorDoor);
    }

    public ExteriorDoor findById(Long id) throws EntityNotFoundException {
        return exteriorDoorRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Exterior door not found");
        });
    }

    public Page<ExteriorDoor> findAllByNameAndDeletedTimeIsNull(Pageable pageable, String name) {
        return exteriorDoorRepository.findAllByNameAndDeletedTimeIsNull(pageable, name);
    }

    public Page<ExteriorDoor> findAllByDeletedTimeIsNull(Pageable pageable) {
        return exteriorDoorRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
