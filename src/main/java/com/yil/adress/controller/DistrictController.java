package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateDistrictDto;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.model.City;
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
import java.util.Date;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/api/address/v1/districts")
public class DistrictController {

    private final DistrictService districtService;
    private final CityService cityService;

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
        Page<District> entities;
        if (cityId != null)
            entities = districtService.findAllByCityIdAndDeletedTimeIsNull(pageable, cityId);
        else
            entities = districtService.findAllByDeletedTimeIsNull(pageable);
        PageDto<DistrictDto> pageDto = PageDto.toDto(entities, DistrictService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DistrictDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                              @Valid @RequestBody CreateDistrictDto request) {
        City parent = cityService.findByIdAndDeletedTimeIsNull(request.getCityId());
        District entity = new District();
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setCityId(parent.getId());
        entity.setCreatedTime(new Date());
        entity.setCreatedUserId(authenticatedUserId);
        entity = districtService.save(entity);
        DistrictDto dto = DistrictService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DistrictDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CreateDistrictDto request) {
        City parent = cityService.findByIdAndDeletedTimeIsNull(request.getCityId());
        District entity = districtService.findByIdAndDeletedTimeIsNull(id);
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setCityId(parent.getId());
        entity = districtService.save(entity);
        DistrictDto dto = DistrictService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        District entity = districtService.findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedTime(new Date());
        entity.setDeletedUserId(authenticatedUserId);
       districtService.save(entity);
        return ResponseEntity.ok("Country deleted.");
    }

}
