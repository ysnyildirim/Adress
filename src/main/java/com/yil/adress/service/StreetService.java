package com.yil.adress.service;

import com.yil.adress.dto.StreetDto;
import com.yil.adress.model.Street;
import com.yil.adress.repository.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StreetService {

    private final StreetRepository streetRepository;

    @Autowired
    public StreetService(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    public static StreetDto toDto(Street street) {
        if (street == null)
            return StreetDto.builder().build();
        return StreetDto.builder()
                .id(street.getId())
                .name(street.getName())
                .build();
    }

    public static Street toEntity(StreetDto request) {
        Street street = new Street();
        if (request == null)
            return street;
        street.setId(request.getId());
        street.setName(request.getName());
        return street;
    }

    public Street save(Street street) {
        return streetRepository.save(street);
    }

    public Street findById(Long id) throws EntityNotFoundException {
        return streetRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Street not found");
        });
    }

    public Page<Street> findAllByNameAndDeletedTimeIsNull(Pageable pageable, String name) {
        return streetRepository.findAllByNameAndDeletedTimeIsNull(pageable, name);
    }

    public Page<Street> findAllByDeletedTimeIsNull(Pageable pageable) {
        return streetRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
