package com.yil.adress.service;

import com.yil.adress.dto.CityDto;
import com.yil.adress.model.City;
import com.yil.adress.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(City city) {
        if (city.getId() == null) {
            City city1 = cityRepository.findByNameAndAndCode(city.getName(), city.getCode());
            if (city1 != null)
                return city1;
        }
        return cityRepository.save(city);
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }

    public City findById(Long id) throws EntityNotFoundException {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("City not found");
        });
    }

    public Page<City> findAllByName(Pageable pageable, String name) {
        return cityRepository.findAllByName(pageable, name);
    }

    public Page<City> findAll(Pageable pageable) {
        Page<City> cities = cityRepository.findAll(pageable);
        return cities;
    }

    public static CityDto toDto(City city) {
        if (city == null)
            return CityDto.builder().build();
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .code(city.getCode())
                .build();
    }

    public static City toEntity(CityDto dto) {
        City city = new City();
        if (dto == null)
            return city;
        city.setId(dto.getId());
        city.setCode(dto.getCode());
        city.setName(dto.getName());
        return city;
    }

    public Page<City> findAllByCode(Pageable pageable, String code) {
        return cityRepository.findAllByCode(pageable, code);
    }

    public Page<City> findAllByNameAndCode(Pageable pageable, String name, String code) {
        return cityRepository.findAllByNameAndCode(pageable, name, code);
    }
}
