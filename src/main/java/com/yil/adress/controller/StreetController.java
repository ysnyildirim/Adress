package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateStreetDto;
import com.yil.adress.dto.StreetDto;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.model.District;
import com.yil.adress.model.Street;
import com.yil.adress.service.DistrictService;
import com.yil.adress.service.StreetService;
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
@RequestMapping("/api/address/v1/streets")
public class StreetController {

    private final StreetService streetService;
    private final DistrictService districtService;

    @Autowired
    public StreetController(StreetService streetService, DistrictService districtService) {
        this.streetService = streetService;
        this.districtService = districtService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StreetDto> findById(@PathVariable Long id) {
        Street entity = streetService.findById(id);
        StreetDto dto = StreetService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<StreetDto>> findAll(
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Street> entities;
        if (districtId != null)
            entities = streetService.findAllByDistrictId(pageable, districtId);
        else
            entities = streetService.findAll(pageable);
        PageDto<StreetDto> pageDto = PageDto.toDto(entities, StreetService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StreetDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                            @Valid @RequestBody CreateStreetDto request) {
        if (!districtService.existsById(request.getDistrictId()))
            throw new DistrictNotFoundException();
        Street entity = new Street();
        entity.setName(request.getName());
        entity.setDistrictId(request.getDistrictId());
        entity.setPostCode(request.getPostCode());
        entity = streetService.save(entity);
        StreetDto dto = StreetService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StreetDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                             @PathVariable Long id,
                                             @Valid @RequestBody CreateStreetDto request) {
        if (!districtService.existsById(request.getDistrictId()))
            throw new DistrictNotFoundException();
        Street entity = streetService.findById(id);
        entity.setName(request.getName());
        entity.setDistrictId(request.getDistrictId());
        entity.setPostCode(request.getPostCode());
        entity = streetService.save(entity);
        StreetDto dto = StreetService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        streetService.deleteById(id);
        return ResponseEntity.ok("Street deleted.");
    }


}
