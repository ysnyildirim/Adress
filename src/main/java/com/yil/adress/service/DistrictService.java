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
                .code(entity.getCode())
                .cityId(entity.getCityId())
                .build();
    }

    public District save(District district) {
        return districtRepository.save(district);
    }

    public District findById(Long id) throws DistrictNotFoundException {
        return districtRepository.findById(id).orElseThrow(() -> {
            throw new DistrictNotFoundException();
        });
    }

    public District findByIdAndDeletedTimeIsNull(Long id) throws DistrictNotFoundException {
        District district = districtRepository.findByIdAndDeletedTimeIsNull(id);
        if (district == null)
            throw new DistrictNotFoundException();
        return district;
    }

    public Page<District> findAllByDeletedTimeIsNull(Pageable pageable) {
        return districtRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public Page<District> findAllByCityIdAndDeletedTimeIsNull(Pageable pageable, Long cityId) {
        return districtRepository.findAllByCityIdAndDeletedTimeIsNull(pageable, cityId);
    }
}
