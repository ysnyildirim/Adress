package com.yil.adress.service;

import com.yil.adress.model.CountryCity;
import com.yil.adress.repository.CountryCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CountryCityService {
    private final CountryCityRepository countryCityRepository;

    @Autowired
    public CountryCityService(CountryCityRepository countryCityRepository) {
        this.countryCityRepository = countryCityRepository;
    }


    public CountryCity save(CountryCity countryCity) {
        return countryCityRepository.save(countryCity);
    }

    public List<CountryCity> save(List<CountryCity> countryCities) {
        return countryCityRepository.saveAll(countryCities);
    }

    public void delete(CountryCity.CountryCityPk id) {
        countryCityRepository.deleteById(id);
    }

    public void delete(long countryId, long cityId) {
        countryCityRepository.deleteById_City_IdAndId_Country_Id(cityId, countryId);
    }

    public CountryCity findById(CountryCity.CountryCityPk id) throws EntityNotFoundException {
        return countryCityRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Entity not found");
        });
    }

    public Page<CountryCity> findAll(Pageable pageable) {
        return countryCityRepository.findAll(pageable);
    }


    public Page<CountryCity> findAllByCountryId(Pageable pageable, long countryId) {
        return countryCityRepository.findAllById_Country_Id(pageable, countryId);
    }
}
