package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.AdressDto;
import com.yil.adress.dto.CreateAdressDto;
import com.yil.adress.model.*;
import com.yil.adress.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/v1/adress")
public class AdressController implements SecuredRestController {

    private Logger logger = Logger.getLogger(AdressController.class.getName());
    private final AdressService adressService;
    private final CountryService countryService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final StreetService streetService;
    private final ExteriorDoorService exteriorDoorService;
    private final InteriorDoorService interiorDoorService;

    @Autowired
    public AdressController(AdressService adressService,
                            CountryService countryService,
                            CityService cityService,
                            DistrictService districtService,
                            StreetService streetService,
                            ExteriorDoorService exteriorDoorService,
                            InteriorDoorService interiorDoorService) {
        this.adressService = adressService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.districtService = districtService;
        this.streetService = streetService;
        this.exteriorDoorService = exteriorDoorService;
        this.interiorDoorService = interiorDoorService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<AdressDto>> findById(@PathVariable long id) {
        try {
            Adress city = adressService.findById(id);
            AdressDto dto = AdressService.toDto(city);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<AdressDto>> save(@RequestBody CreateAdressDto dto) {
        try {
            Country country = null;
            if (dto.getCountryId() != null)
                country = countryService.findById(dto.getCountryId());
            City city = null;
            if (dto.getCityId() != null)
                city = cityService.findById(dto.getCityId());
            District district = null;
            if (dto.getDistrictId() != null)
                district = districtService.findById(dto.getDistrictId());
            Street street = null;
            if (dto.getStreetId() != null)
                street = streetService.findById(dto.getStreetId());
            ExteriorDoor exteriorDoor = null;
            if (dto.getExteriorDoorId() != null)
                exteriorDoor = exteriorDoorService.findById(dto.getExteriorDoorId());
            InteriorDoor interiorDoor = null;
            if (dto.getInteriorDoorId() != null)
                interiorDoor = interiorDoorService.findById(dto.getInteriorDoorId());
            Adress entity = new Adress();
            entity.setId(dto.getId());
            entity.setCountry(country);
            entity.setCity(city);
            entity.setDistrict(district);
            entity.setStreet(street);
            entity.setExteriorDoor(exteriorDoor);
            entity.setInteriorDoor(interiorDoor);
            entity = adressService.save(entity);
            AdressDto result = AdressService.toDto(entity);
            return ResponseEntity.ok(ApiResponce.ok(result));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete/id={id}")
    public ResponseEntity<ApiResponce<String>> delete(@PathVariable long id) {
        try {
            adressService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("Adress deleted."));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
