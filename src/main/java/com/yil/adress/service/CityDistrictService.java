package com.yil.adress.service;

import com.yil.adress.model.CityDistrict;
import com.yil.adress.repository.CityDistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CityDistrictService {
    private final CityDistrictRepository cityDistrictRepository;

    @Autowired
    public CityDistrictService(CityDistrictRepository cityDistrictRepository) {
        this.cityDistrictRepository = cityDistrictRepository;
    }


    public CityDistrict save(CityDistrict cityDistrict) {
        return cityDistrictRepository.save(cityDistrict);
    }

    public void delete(CityDistrict.CityDistrictPk id) {
        cityDistrictRepository.deleteById(id);
    }

    public CityDistrict findById(CityDistrict.CityDistrictPk id) throws EntityNotFoundException {
        return cityDistrictRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Entity not found");
        });
    }

    public Page<CityDistrict> findAllByCityId(Pageable pageable, long cityId) {
        return cityDistrictRepository.findAllById_City_Id(pageable, cityId);
    }

}
