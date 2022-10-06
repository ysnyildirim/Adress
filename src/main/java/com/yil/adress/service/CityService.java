package com.yil.adress.service;

import com.yil.adress.dto.CityDto;
import com.yil.adress.dto.CreateCityDto;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.exception.CityNotFoundException;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.City;
import com.yil.adress.model.Country;
import com.yil.adress.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public static CityDto toDto(City city) throws NullPointerException {
        if (city == null)
            throw new NullPointerException("City is null");
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCode(city.getCode());
        dto.setPhoneCode(city.getPhoneCode());
        dto.setCountryId(city.getCountryId());
        return dto;
    }

    private City getCity(CreateCityDto dto, long userId, City city)
            throws CountryNotFoundException {
        city.setName(dto.getName());
        city.setCode(dto.getCode());
        city.setCountryId(dto.getCountryId());
        city.setPhoneCode(dto.getPhoneCode());
        city.setEnabled(dto.getEnabled());
        city.setCreatedUserId(userId);
        city.setCreatedDate(new Date());
        city.setLastModifyUserId(userId);
        city.setLastModifyDate(new Date());
        return cityRepository.save(city);
    }

    public City save(CreateCityDto dto, long userId) throws CityNotFoundException {
        City city = new City();
        return getCity(dto, userId, city);
    }

    public City replace(long id, CreateCityDto dto, long userId) throws CityNotFoundException {
        City city = findById(id);
        return getCity(dto, userId, city);
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
