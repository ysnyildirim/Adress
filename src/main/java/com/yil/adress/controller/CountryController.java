package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.model.Country;
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
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/api/address/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> findById(@NotNull @PathVariable Long id) {
        Country entity = countryService.findById(id);
        CountryDto dto = CountryService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<CountryDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name", "code"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Country> city = countryService.findAllByDeletedTimeIsNull(pageable);
        PageDto<CountryDto> pageDto = PageDto.toDto(city, CountryService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CountryDto> create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                             @RequestBody @Valid CreateCountryDto request) {
        Country entity = new Country();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setCreatedTime(new Date());
        entity.setCreatedUserId(authenticatedUserId);
        entity = countryService.save(entity);
        CountryDto dto = CountryService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CountryDto> replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                              @PathVariable Long id,
                                              @RequestBody @Valid CreateCountryDto request) {
        Country entity = countryService.findByIdAndDeletedTimeIsNull(id);
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity = countryService.save(entity);
        CountryDto dto = CountryService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        Country entity = countryService.findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedTime(new Date());
        entity.setDeletedUserId(authenticatedUserId);
        countryService.save(entity);
        return ResponseEntity.ok("Country deleted.");
    }


}
