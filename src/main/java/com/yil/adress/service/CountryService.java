package com.yil.adress.service;

import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public static CountryDto toDto(Country country) throws NullPointerException {
        if (country == null)
            throw new NullPointerException("Country is null");
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCode(country.getCode());
        dto.setPhoneCode(country.getPhoneCode());
        return dto;

    }

    private Country getCountry(CreateCountryDto dto, long userId, Country country)
            throws CountryNotFoundException {
        country.setName(dto.getName());
        country.setCode(dto.getCode());
        country.setPhoneCode(dto.getPhoneCode());
        country.setEnabled(dto.getEnabled());
        country.setCreatedUserId(userId);
        country.setCreatedDate(new Date());
        country.setLastModifyUserId(userId);
        country.setLastModifyDate(new Date());
        return countryRepository.save(country);
    }

    public Page<Country> findAll(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country save(CreateCountryDto dto, long userId) {
        Country country = new Country();
        return getCountry(dto, userId, country);
    }

    public Country replace(long id, CreateCountryDto dto, long userId) {
        Country country = findById(id);
        return getCountry(dto, userId, country);
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
