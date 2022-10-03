package com.yil.adress.service;

import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.dto.CreateDistrictDto;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.exception.CityNotFoundException;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.District;
import com.yil.adress.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    private District getDistrict(CreateDistrictDto dto, long userId, District district)
            throws CityNotFoundException {
        district.setName(dto.getName());
        district.setCityId(dto.getCityId());
        district.setEnabled(dto.getEnabled());
        district.setCreatedUserId(userId);
        district.setCreatedDate(new Date());
        district.setLastModifyUserId(userId);
        district.setLastModifyDate(new Date());
        return districtRepository.save(district);
    }

    public District save(CreateDistrictDto dto, long userId) {
        District district = new District();
        return getDistrict(dto, userId, district);
    }

    public District replace(long id, CreateDistrictDto dto, long userId) {
        District district = findById(id);
        return getDistrict(dto, userId, district);
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
