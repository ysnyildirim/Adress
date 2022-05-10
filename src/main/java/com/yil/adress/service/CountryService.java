package com.yil.adress.service;

import com.yil.adress.dto.CountryDto;
import com.yil.adress.model.Country;
import com.yil.adress.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Page<Country> findAll(Pageable pageable) {
        Page<Country> page = countryRepository.findAll(pageable);
        return page;
    }


    public Country save(Country country) {
        return countryRepository.save(country);
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }

    public Country findById(Long id) throws EntityNotFoundException {
        return countryRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Country not found");
        });
    }

    public static Country toEntity(CountryDto request) {
        Country country = new Country();
        if (request == null)
            return country;
        country.setId(request.getId());
        country.setCode(request.getCode());
        country.setName(request.getName());
        return country;
    }

    public static CountryDto toDto(Country country) {
        if (country == null)
            return CountryDto.builder().build();
        return CountryDto.builder()
                .id(country.getId())
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public Page<Country> findAllByName(Pageable pageable, String name) {
        return countryRepository.findAllByName(pageable, name);
    }

    public Page<Country> findAllByCode(Pageable pageable, String code) {
        return countryRepository.findAllByCode(pageable, code);
    }

    public Page<Country> findAllByNameAndCode(Pageable pageable, String name, String code) {
        return countryRepository.findAllByNameAndCode(pageable, name, code);
    }
}
