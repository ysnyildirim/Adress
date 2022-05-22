package com.yil.adress.service;

import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.exception.ExteriorDoorNotFoundException;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.repository.ExteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExteriorDoorService {

    private final ExteriorDoorRepository exteriorDoorRepository;

    @Autowired
    public ExteriorDoorService(ExteriorDoorRepository exteriorDoorRepository) {
        this.exteriorDoorRepository = exteriorDoorRepository;
    }

    public static ExteriorDoorDto toDto(ExteriorDoor entity) {
        if (entity == null)
            throw new NullPointerException("Exterior door is null");
        return ExteriorDoorDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .streetId(entity.getStreetId())
                .build();
    }

    public ExteriorDoor save(ExteriorDoor exteriorDoor) {
        return exteriorDoorRepository.save(exteriorDoor);
    }

    public ExteriorDoor findById(Long id) throws ExteriorDoorNotFoundException {
        return exteriorDoorRepository.findById(id).orElseThrow(() -> {
            throw new ExteriorDoorNotFoundException();
        });
    }

    public ExteriorDoor findByIdAndDeletedTimeIsNull(Long id) throws ExteriorDoorNotFoundException {
        ExteriorDoor exteriorDoor = exteriorDoorRepository.findByIdAndDeletedTimeIsNull(id);
        if (exteriorDoor == null)
            throw new ExteriorDoorNotFoundException();
        return exteriorDoor;
    }

    public Page<ExteriorDoor> findAllByDeletedTimeIsNull(Pageable pageable) {
        return exteriorDoorRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public Page<ExteriorDoor> findAllByStreetIdAndDeletedTimeIsNull(Pageable pageable, Long streetId) {
        return exteriorDoorRepository.findAllByStreetIdAndDeletedTimeIsNull(pageable, streetId);
    }
}
