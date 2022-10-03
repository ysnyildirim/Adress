package com.yil.adress.service;

import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.dto.CreateExteriorDoorDto;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.exception.ExteriorDoorNotFoundException;
import com.yil.adress.exception.StreetNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.repository.ExteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExteriorDoorService {
    private final ExteriorDoorRepository exteriorDoorRepository;

    @Autowired
    public ExteriorDoorService(ExteriorDoorRepository exteriorDoorRepository) {
        this.exteriorDoorRepository = exteriorDoorRepository;
    }

    public static ExteriorDoorDto toDto(ExteriorDoor exteriorDoor) {
        if (exteriorDoor == null)
            throw new NullPointerException("Exterior door is null");
        ExteriorDoorDto dto = new ExteriorDoorDto();
        dto.setId(exteriorDoor.getId());
        dto.setName(exteriorDoor.getName());
        dto.setStreetId(exteriorDoor.getStreetId());
        dto.setEnabled(exteriorDoor.getEnabled());
        return dto;
    }

    private ExteriorDoor getExteriorDoor(CreateExteriorDoorDto dto, long userId, ExteriorDoor exteriorDoor)
            throws StreetNotFoundException {
        exteriorDoor.setName(dto.getName());
        exteriorDoor.setStreetId(dto.getStreetId());
        exteriorDoor.setEnabled(dto.getEnabled());
        exteriorDoor.setCreatedUserId(userId);
        exteriorDoor.setCreatedDate(new Date());
        exteriorDoor.setLastModifyUserId(userId);
        exteriorDoor.setLastModifyDate(new Date());
        return exteriorDoorRepository.save(exteriorDoor);
    }

    public ExteriorDoor save(CreateExteriorDoorDto dto, long userId) {
        ExteriorDoor exteriorDoor = new ExteriorDoor();
        return getExteriorDoor(dto, userId, exteriorDoor);
    }

    public ExteriorDoor replace(long id, CreateExteriorDoorDto dto, long userId) {
        ExteriorDoor exteriorDoor = findById(id);
        return getExteriorDoor(dto, userId, exteriorDoor);
    }

    public ExteriorDoor findById(Long id) throws ExteriorDoorNotFoundException {
        return exteriorDoorRepository.findById(id).orElseThrow(ExteriorDoorNotFoundException::new);
    }

    public Page<ExteriorDoor> findAll(Pageable pageable) {
        return exteriorDoorRepository.findAll(pageable);
    }

    public Page<ExteriorDoor> findAllByStreetId(Pageable pageable, Long streetId) {
        return exteriorDoorRepository.findAllByStreetId(pageable, streetId);
    }

    public void deleteById(long id) {
        exteriorDoorRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return exteriorDoorRepository.existsById(id);
    }
}
