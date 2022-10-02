package com.yil.adress.service;

import com.yil.adress.dto.CountryDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public static CountryDto toDto(Country country) {
        if (country == null)
            throw new NullPointerException("Country is null");
        return CountryDto.builder()
                .id(country.getId())
                .code(country.getCode())
                .phoneCode(country.getPhoneCode())
                .name(country.getName())
                .build();
    }

    public Page<Country> findAll(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country save(Country country) {
        return countryRepository.save(country);
    }

    public Country findById(Long id) throws CountryNotFoundException {
        return countryRepository.findById(id).orElseThrow(CountryNotFoundException::new);
    }

    public boolean existsById(long id) {
        return countryRepository.existsById(id);
    }

    public void deleteById(long id) {
        countryRepository.deleteById(id);
    }
}
