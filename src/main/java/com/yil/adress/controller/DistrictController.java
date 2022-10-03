package com.yil.adress.controller;

import com.yil.adress.base.Mapper;
import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CountryResponse;
import com.yil.adress.dto.CreateDistrictDto;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.dto.DistrictResponse;
import com.yil.adress.exception.CityNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.District;
import com.yil.adress.service.CityService;
import com.yil.adress.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/api/adr/v1/districts")
public class DistrictController {
    private final DistrictService districtService;
    private final CityService cityService;
    private final Mapper<District, DistrictDto> mapper = new Mapper<>(DistrictService::toDto);

    @Autowired
    public DistrictController(DistrictService districtService, CityService cityService) {
        this.districtService = districtService;
        this.cityService = cityService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DistrictDto> findById(@PathVariable long id) {
        District entity = districtService.findById(id);
        DistrictDto dto = DistrictService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<DistrictDto>> findAll(
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return ResponseEntity.ok(mapper.map(districtService.findAll(pageable)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DistrictResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                   @Valid @RequestBody CreateDistrictDto request) {
        if (!cityService.existsById(request.getCityId()))
            throw new CityNotFoundException();
        District district = districtService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(DistrictResponse.builder().id(district.getId()).build());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody CreateDistrictDto request) {
        if (!cityService.existsById(request.getCityId()))
            throw new CityNotFoundException();
        districtService.replace(id, request, authenticatedUserId);
        return ResponseEntity.ok().body("District updated.");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        districtService.deleteById(id);
        return ResponseEntity.ok("Country deleted.");
    }
}
