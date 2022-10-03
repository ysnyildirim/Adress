package com.yil.adress.service;

import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.dto.CreateInteriorDoorDto;
import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.exception.ExteriorDoorNotFoundException;
import com.yil.adress.exception.InteriorDoorNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.repository.InteriorDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InteriorDoorService {
    private final InteriorDoorRepository interiorDoorRepository;

    @Autowired
    public InteriorDoorService(InteriorDoorRepository interiorDoorRepository) {
        this.interiorDoorRepository = interiorDoorRepository;
    }

    public static InteriorDoorDto toDto(InteriorDoor entity) throws NullPointerException {
        if (entity == null)
            throw new NullPointerException("Interior door is null");
        return InteriorDoorDto.builder()
                .exteriorDoorId(entity.getExteriorDoorId())
                .id(entity.getId())
                .name(entity.getName()).build();
    }

    private InteriorDoor getInteriorDoor(CreateInteriorDoorDto dto, long userId, InteriorDoor interiorDoor)
            throws ExteriorDoorNotFoundException {
        interiorDoor.setName(dto.getName());
        interiorDoor.setExteriorDoorId(dto.getExteriorDoorId());
        interiorDoor.setEnabled(dto.getEnabled());
        interiorDoor.setCreatedUserId(userId);
        interiorDoor.setCreatedDate(new Date());
        interiorDoor.setLastModifyUserId(userId);
        interiorDoor.setLastModifyDate(new Date());
        return interiorDoorRepository.save(interiorDoor);
    }

    public InteriorDoor save(CreateInteriorDoorDto dto, long userId) {
        InteriorDoor interiorDoor = new InteriorDoor();
        return getInteriorDoor(dto, userId, interiorDoor);
    }

    public InteriorDoor replace(long id, CreateInteriorDoorDto dto, long userId) {
        InteriorDoor interiorDoor = findById(id);
        return getInteriorDoor(dto, userId, interiorDoor);
    }

    public InteriorDoor findById(Long id) throws InteriorDoorNotFoundException {
        return interiorDoorRepository.findById(id).orElseThrow(() -> {
            throw new InteriorDoorNotFoundException();
        });
    }

    public InteriorDoor findByIdAndDeletedTimeIsNull(Long id) throws InteriorDoorNotFoundException {
        return interiorDoorRepository.findById(id).orElseThrow(InteriorDoorNotFoundException::new);
    }

    public Page<InteriorDoor> findByExteriorDoorId(Pageable pageable, Long exteriorDoorId) {
        return interiorDoorRepository.findByExteriorDoorId(pageable, exteriorDoorId);
    }

    public Page<InteriorDoor> findAll(Pageable pageable) {
        return interiorDoorRepository.findAll(pageable);
    }

    public boolean existsById(long id) {
        return interiorDoorRepository.existsById(id);
    }

    public void deleteById(long id) {
        interiorDoorRepository.deleteById(id);
    }
}
