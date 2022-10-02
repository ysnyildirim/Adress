package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateExteriorDoorDto;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.exception.StreetNotFoundException;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
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
@RequestMapping("/api/address/v1/exterior-doors")
public class ExteriorDoorController {

    private final ExteriorDoorService exteriorDoorService;
    private final StreetService streetService;

    @Autowired
    public ExteriorDoorController(ExteriorDoorService exteriorDoorService, StreetService streetService) {
        this.exteriorDoorService = exteriorDoorService;
        this.streetService = streetService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ExteriorDoorDto> findById(@PathVariable Long id) {
        ExteriorDoor entity = exteriorDoorService.findById(id);
        ExteriorDoorDto dto = ExteriorDoorService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<ExteriorDoorDto>> findAll(
            @RequestParam(required = false) Long streetId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<ExteriorDoor> entities;
        if (streetId != null)
            entities = exteriorDoorService.findAllByStreetId(pageable, streetId);
        else
            entities = exteriorDoorService.findAll(pageable);
        PageDto<ExteriorDoorDto> pageDto = PageDto.toDto(entities, ExteriorDoorService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExteriorDoorDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @Valid @RequestBody CreateExteriorDoorDto request) {
        if (!streetService.existsById(request.getStreetId()))
            throw new StreetNotFoundException();
        ExteriorDoor entity = new ExteriorDoor();
        entity.setName(request.getName());
        entity.setStreetId(request.getStreetId());
        entity = exteriorDoorService.save(entity);
        ExteriorDoorDto dto = ExteriorDoorService.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExteriorDoorDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody CreateExteriorDoorDto request) {
        if (!streetService.existsById(request.getStreetId()))
            throw new StreetNotFoundException();
        ExteriorDoor entity = exteriorDoorService.findById(id);
        entity.setName(request.getName());
        entity.setStreetId(request.getStreetId());
        entity = exteriorDoorService.save(entity);
        ExteriorDoorDto dto = ExteriorDoorService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        exteriorDoorService.deleteById(id);
        return ResponseEntity.ok("Exterior door deleted.");
    }
}
