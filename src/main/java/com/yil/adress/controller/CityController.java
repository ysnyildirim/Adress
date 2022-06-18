package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/address/v1/cities")
public class CityController {

    private final CityService cityService;
    private final CountryService countryService;

    @Autowired
    public CityController(CityService cityService, CountryService countryService) {
        this.cityService = cityService;
        this.countryService = countryService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable Long id) {
        City city = cityService.findById(id);
        CityDto dto = CityService.toDto(city);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<CityDto>> findAll(@RequestParam(required = false) Long countryId,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
                                                    @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<City> city;
        if (countryId != null)
            city = cityService.findAllByCountryIdAndDeletedTimeIsNull(pageable, countryId);
        else
            city = cityService.findAllByDeletedTimeIsNull(pageable);
        PageDto<CityDto> pageDto = PageDto.toDto(city, CityService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CityDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @Valid @RequestBody CreateCityDto request) {
        Country country = countryService.findByIdAndDeletedTimeIsNull(request.getCountryId());
        City entity = new City();
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setCountryId(country.getId());
        entity.setPhoneCode(request.getPhoneCode());
        entity.setCreatedTime(new Date());
        entity.setCreatedUserId(authenticatedUserId);
        entity = cityService.save(entity);
        CityDto dto = CityService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CityDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CreateCityDto request) {
        Country country = countryService.findByIdAndDeletedTimeIsNull(request.getCountryId());
        City entity = cityService.findById(id);
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setPhoneCode(request.getPhoneCode());
        entity.setCountryId(country.getId());
        entity = cityService.save(entity);
        CityDto dto = CityService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        City city = cityService.findByIdAndDeletedTimeIsNull(id);
        city.setDeletedTime(new Date());
        city.setDeletedUserId(authenticatedUserId);
        cityService.save(city);
        return ResponseEntity.ok("City deleted.");
    }

}
