package com.yil.adress.service;

import com.yil.adress.dto.DistrictDto;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.model.District;
import com.yil.adress.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public static DistrictDto toDto(District entity) {
        if (entity == null)
            throw new NullPointerException("District is null");
        return DistrictDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cityId(entity.getCityId())
                .build();
    }

    public District save(District district) {
        return districtRepository.save(district);
    }

    public District findById(Long id) throws DistrictNotFoundException {
        return districtRepository.findById(id).orElseThrow(DistrictNotFoundException::new);
    }

    public Page<District> findAll(Pageable pageable) {
        return districtRepository.findAll(pageable);
    }

    public Page<District> findAllByCityId(Pageable pageable, Long cityId) {
        return districtRepository.findAllByCityId(pageable, cityId);
    }

    public void deleteById(long id) {
        districtRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return districtRepository.existsById(id);
    }
}
