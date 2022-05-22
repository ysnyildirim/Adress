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
                .build();
    }


    public Street save(Street street) {
        return streetRepository.save(street);
    }

    public Street findById(Long id) throws StreetNotFoundException {
        return streetRepository.findById(id).orElseThrow(() -> {
            throw new StreetNotFoundException("Street not found");
        });
    }

    public Street findByIdAndDeletedTimeIsNull(Long id) throws StreetNotFoundException {
        Street street = streetRepository.findByIdAndDeletedTimeIsNull(id);
        if (street == null)
            throw new StreetNotFoundException();
        return street;
    }

    public Page<Street> findAllByDistrictIdAndDeletedTimeIsNull(Pageable pageable, Long districtId) {
        return streetRepository.findAllByDistrictIdAndDeletedTimeIsNull(pageable, districtId);
    }

    public Page<Street> findAllByDeletedTimeIsNull(Pageable pageable) {
        return streetRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
