package com.yil.adress.service;

import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.dto.CreateStreetDto;
import com.yil.adress.dto.StreetDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.exception.StreetNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.Street;
import com.yil.adress.repository.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StreetService {
    private final StreetRepository streetRepository;

    @Autowired
    public StreetService(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    public static StreetDto toDto(Street street) throws NullPointerException {
        if (street == null)
            throw new NullPointerException("Street is null");
        StreetDto dto = new StreetDto();
        dto.setId(street.getId());
        dto.setName(street.getName());
        dto.setDistrictId(street.getDistrictId());
        dto.setPostCode(street.getPostCode());
        return dto;
    }

    private Street getStreet(CreateStreetDto dto, long userId, Street street)
            throws DistrictNotFoundException {
        street.setName(dto.getName());
        street.setDistrictId(dto.getDistrictId());
        street.setPostCode(dto.getPostCode());
        street.setEnabled(dto.getEnabled());
        street.setCreatedUserId(userId);
        street.setCreatedDate(new Date());
        street.setLastModifyUserId(userId);
        street.setLastModifyDate(new Date());
        return streetRepository.save(street);
    }

    public Street save(CreateStreetDto dto, long userId) {
        Street street = new Street();
        return getStreet(dto, userId, street);
    }

    public Street replace(long id, CreateStreetDto dto, long userId) {
        Street street = findById(id);
        return getStreet(dto, userId, street);
    }

    public void deleteById(long id) {
        streetRepository.deleteById(id);
    }

    public Street findById(Long id) throws StreetNotFoundException {
        return streetRepository.findById(id).orElseThrow(StreetNotFoundException::new);
    }

    public Page<Street> findAllByDistrictId(Pageable pageable, Long districtId) {
        return streetRepository.findAllByDistrictId(pageable, districtId);
    }

    public Page<Street> findAll(Pageable pageable) {
        return streetRepository.findAll(pageable);
    }

    public boolean existsById(long id) {
        return streetRepository.existsById(id);
    }
}
