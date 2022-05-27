package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateInteriorDoorDto;
import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.InteriorDoorService;
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
@RequestMapping("/api/address/v1/interior-doors")
public class InteriorDoorController {

    private final InteriorDoorService interiorDoorService;
    private final ExteriorDoorService exteriorDoorService;

    @Autowired
    public InteriorDoorController(InteriorDoorService interiorDoorService, ExteriorDoorService exteriorDoorService) {
        this.interiorDoorService = interiorDoorService;
        this.exteriorDoorService = exteriorDoorService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InteriorDoorDto> findById(@PathVariable Long id) {
        InteriorDoor entity = interiorDoorService.findById(id);
        InteriorDoorDto dto = InteriorDoorService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageDto<InteriorDoorDto>> findAll(
            @RequestParam(required = false) Long exteriorDoorId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<InteriorDoor> entities;
        if (exteriorDoorId != null)
            entities = interiorDoorService.findByExteriorDoorIdAndDeletedTimeIsNull(pageable, exteriorDoorId);
        else
            entities = interiorDoorService.findAllByDeletedTimeIsNull(pageable);
        PageDto<InteriorDoorDto> pageDto = PageDto.toDto(entities, InteriorDoorService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InteriorDoorDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @Valid @RequestBody CreateInteriorDoorDto request) {
        ExteriorDoor parent = exteriorDoorService.findByIdAndDeletedTimeIsNull(request.getExteriorDoorId());
        InteriorDoor entity = new InteriorDoor();
        entity.setName(request.getName());
        entity.setExteriorDoorId(parent.getId());
        entity.setCreatedTime(new Date());
        entity.setCreatedUserId(authenticatedUserId);
        entity = interiorDoorService.save(entity);
        InteriorDoorDto dto = InteriorDoorService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InteriorDoorDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody CreateInteriorDoorDto request) {
        ExteriorDoor parent = exteriorDoorService.findByIdAndDeletedTimeIsNull(request.getExteriorDoorId());
        InteriorDoor entity = interiorDoorService.findByIdAndDeletedTimeIsNull(id);
        entity.setName(request.getName());
        entity.setExteriorDoorId(parent.getId());
        entity = interiorDoorService.save(entity);
        InteriorDoorDto dto = InteriorDoorService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        InteriorDoor entity = interiorDoorService.findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedTime(new Date());
        entity.setDeletedUserId(authenticatedUserId);
        interiorDoorService.save(entity);
        return ResponseEntity.ok("Interior door deleted.");
    }
}
