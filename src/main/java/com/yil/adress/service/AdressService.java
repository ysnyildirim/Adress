package com.yil.adress.service;

import com.yil.adress.dto.AdressDto;
import com.yil.adress.model.Adress;
import com.yil.adress.repository.AdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AdressService {

    private final AdressRepository adressRepository;

    @Autowired
    public AdressService(AdressRepository adressRepository) {
        this.adressRepository = adressRepository;
    }

    public static AdressDto toDto(Adress entity) {
        if (entity == null)
            throw new NullPointerException();
        return AdressDto.builder()
                .id(entity.getId())
                .country(CountryService.toDto(entity.getCountry()))
                .city(CityService.toDto(entity.getCity()))
                .district(DistrictService.toDto(entity.getDistrict()))
                .street(StreetService.toDto(entity.getStreet()))
                .exteriorDoor(ExteriorDoorService.toDto(entity.getExteriorDoor()))
                .interiorDoor(InteriorDoorService.toDto(entity.getInteriorDoor()))
                .build();
    }

    public static Adress toEntity(AdressDto dto) {
        Adress adress = new Adress();
        if (dto == null)
            throw new NullPointerException();
        adress.setId(dto.getId());
        adress.setCountry(adress.getCountry());
        adress.setCity(adress.getCity());
        adress.setDistrict(adress.getDistrict());
        adress.setStreet(adress.getStreet());
        adress.setExteriorDoor(adress.getExteriorDoor());
        adress.setInteriorDoor(adress.getInteriorDoor());
        return adress;
    }

    public Adress save(Adress adress) {
        return adressRepository.save(adress);
    }

    public void delete(Long id) {
        adressRepository.deleteById(id);
    }

    public Adress findById(Long id) throws EntityNotFoundException {
        return adressRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Adress not found");
        });
    }

}
