package com.yil.adress.service;

import com.yil.adress.dto.StreetDto;
import com.yil.adress.exception.StreetNotFoundException;
import com.yil.adress.model.Street;
import com.yil.adress.repository.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return StreetDto.builder()
                .id(street.getId())
                .name(street.getName())
                .districtId(street.getDistrictId())
                .postCode(street.getPostCode())
                .build();
    }

    public Street save(Street street) {
        return streetRepository.save(street);
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
