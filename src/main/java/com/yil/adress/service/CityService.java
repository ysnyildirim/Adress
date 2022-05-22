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
                .build();
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public City findById(Long id) throws CityNotFoundException {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new CityNotFoundException();
        });
    }

    public City findByIdAndDeletedTimeIsNull(Long id) throws CityNotFoundException {
        City city = cityRepository.findByIdAndDeletedTimeIsNull(id);
        if (city == null)
            throw new CityNotFoundException();
        return city;
    }

    public Page<City> findAllByCountryIdAndDeletedTimeIsNull(Pageable pageable, Long countryId) {
        return cityRepository.findAllByCountryIdAndDeletedTimeIsNull(pageable, countryId);
    }

    public Page<City> findAllByDeletedTimeIsNull(Pageable pageable) {
        return cityRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
