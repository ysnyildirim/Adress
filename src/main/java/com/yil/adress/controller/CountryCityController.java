package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.CityDto;
import com.yil.adress.model.City;
import com.yil.adress.model.Country;
import com.yil.adress.model.CountryCity;
import com.yil.adress.service.CityService;
import com.yil.adress.service.CountryCityService;
import com.yil.adress.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/v1/country-city")
public class CountryCityController implements SecuredRestController {

    private Logger logger = Logger.getLogger(CountryCityController.class.getName());
    private final CountryService countryService;
    private final CountryCityService countryCityService;
    private final CityService cityService;

    @Autowired
    public CountryCityController(CountryService countryService, CountryCityService countryCityService, CityService cityService) {
        this.countryService = countryService;
        this.countryCityService = countryCityService;
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<CityDto>>> findAll(@RequestParam long countryId,
                                                                 @RequestParam(defaultValue = "0") int pageNumber,
                                                                 @RequestParam(required = false, defaultValue = "1000") int pageSize) {
        try {
            if (pageNumber < 0)
                pageNumber = 0;
            if (pageSize <= 0 || pageSize > 1000)
                pageSize = 1000;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<CountryCity> page = countryCityService.findAllByCountryId(pageable, countryId);
            PageDto<CityDto> cityDtoPageDto = PageDto.toDto(page, f -> CityService.toDto(f.getId().getCity()));
            return ResponseEntity.ok(ApiResponce.ok(cityDtoPageDto));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponce<String>> delete(@RequestParam Long countryId,
                                                      @RequestParam Long cityId) {
        try {
            City city = cityService.findById(cityId);
            Country country = countryService.findById(countryId);
            CountryCity.CountryCityPk pk = new CountryCity.CountryCityPk();
            pk.setCountry(country);
            pk.setCity(city);
            countryCityService.delete(pk);
            return ResponseEntity.ok(ApiResponce.ok("Deleted."));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<String>> save(@RequestParam Long countryId,
                                                    @RequestParam Long cityId) {
        try {
            City city = cityService.findById(cityId);
            Country country = countryService.findById(countryId);
            CountryCity.CountryCityPk pk = new CountryCity.CountryCityPk();
            pk.setCountry(country);
            pk.setCity(city);
            CountryCity countryCity = new CountryCity();
            countryCity.setId(pk);
            countryCityService.save(countryCity);
            return ResponseEntity.ok(ApiResponce.ok("Saved"));
        } catch (Exception exception) {

            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }


}
