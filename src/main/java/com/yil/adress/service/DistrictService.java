package com.yil.adress.service;

import com.yil.adress.dto.DistrictDto;
import com.yil.adress.model.District;
import com.yil.adress.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public static DistrictDto toDto(District entity) {
        if (entity == null)
            return DistrictDto.builder().build();
        return DistrictDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }

    public static District toEntity(DistrictDto dto) {
        District district = new District();
        if (dto == null)
            return district;
        district.setId(dto.getId());
        district.setCode(dto.getCode());
        district.setName(dto.getName());
        return district;
    }

    public District save(District district) {
        return districtRepository.save(district);
    }

    public void delete(Long id) {
        districtRepository.deleteById(id);
    }

    public District findById(Long id) throws EntityNotFoundException {
        return districtRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("District not found");
        });
    }

    public Page<District> findAllByName(Pageable pageable, String name) {
        return districtRepository.findAllByName(pageable, name);
    }

    public Page<District> findAllByCode(Pageable pageable, String code) {
        return districtRepository.findAllByCode(pageable, code);
    }

    public Page<District> findAllByNameAndCode(Pageable pageable, String name, String code) {
        return districtRepository.findAllByNameAndCode(pageable, name, code);
    }

    public Page<District> findAll(Pageable pageable) {
        return districtRepository.findAll(pageable);
    }
}
