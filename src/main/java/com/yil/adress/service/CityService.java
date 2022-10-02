package com.yil.adress.service;

import com.yil.adress.dto.CityDto;
import com.yil.adress.exception.CityNotFoundException;
import com.yil.adress.model.City;
import com.yil.adress.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public static CityDto toDto(City city) {
        if (city == null)
            throw new NullPointerException("City is null");
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .code(city.getCode())
                .countryId(city.getCountryId())
                .phoneCode(city.getPhoneCode())
                .build();
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public void deleteById(long id) {
        cityRepository.deleteById(id);
    }

    public City findById(long id) throws CityNotFoundException {
        return cityRepository.findById(id).orElseThrow(CityNotFoundException::new);
    }

    public Page<City> findAllByCountryId(Pageable pageable, long countryId) {
        return cityRepository.findAllByCountryId(pageable, countryId);
    }

    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public boolean existsById(long id) {
        return cityRepository.existsById(id);
    }
}
