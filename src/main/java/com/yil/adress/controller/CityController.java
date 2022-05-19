package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CityDto;
import com.yil.adress.dto.CreateCityDto;
import com.yil.adress.model.City;
import com.yil.adress.model.Country;
import com.yil.adress.service.CityService;
import com.yil.adress.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/v1/countries/{countryId}/cities")
public class CityController {

    private final CityService cityService;
    private final CountryService countryService;
    private Logger logger = Logger.getLogger(CityController.class.getName());

    @Autowired
    public CityController(CityService cityService, CountryService countryService) {
        this.cityService = cityService;
        this.countryService = countryService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable long id) {
        try {
            City city;
            try {
                city = cityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            CityDto dto = CityService.toDto(city);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<PageDto<CityDto>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size,
            @RequestParam(required = false) String[] sort) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
            Page<City> city = null;
            if (name != null)
                city = cityService.findAllByNameAndDeletedTimeIsNull(pageable, name);
            else
                city = cityService.findAllByDeletedTimeIsNull(pageable);
            PageDto<CityDto> pageDto = PageDto.toDto(city, CityService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateCityDto request) {
        try {
            Country country;
            try {
                country = countryService.findById(request.getCountryId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            City entity = new City();
            entity.setName(request.getName());
            entity.setCode(request.getCode());
            entity.setCountryId(country.getId());
            entity.setCreatedTime(new Date());
            entity.setCreatedUserId(authenticatedUserId);
            entity = cityService.save(entity);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateCityDto request) {
        try {
            Country country;
            try {
                country = countryService.findById(request.getCountryId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            City entity = null;
            try {
                entity = cityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setName(request.getName());
            entity.setCode(request.getCode());
            entity.setCountryId(country.getId());
            entity = cityService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        try {
            City city;
            try {
                city = cityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            city.setDeletedTime(new Date());
            city.setDeletedUserId(authenticatedUserId);
            cityService.save(city);
            return ResponseEntity.ok("City deleted.");
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

}
