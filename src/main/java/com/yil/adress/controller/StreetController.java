package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateStreetDto;
import com.yil.adress.dto.StreetDto;
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
@RequestMapping("/v1/districts/{districtId}/streets")
public class StreetController {

    private Logger logger = Logger.getLogger(StreetController.class.getName());
    private final StreetService streetService;
    private final DistrictService districtService;

    @Autowired
    public StreetController(StreetService streetService, DistrictService districtService) {
        this.streetService = streetService;
        this.districtService = districtService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StreetDto> findById(@PathVariable Long id) {
        try {
            Street entity;
            try {
                entity = streetService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            StreetDto dto = StreetService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<PageDto<StreetDto>> findAll(
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
            Page<Street> entities = null;
            if (name != null)
                entities = streetService.findAllByNameAndDeletedTimeIsNull(pageable, name);
            else
                entities = streetService.findAllByDeletedTimeIsNull(pageable);
            PageDto<StreetDto> pageDto = PageDto.toDto(entities, StreetService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateStreetDto request) {
        try {
            District parent;
            try {
                parent = districtService.findById(request.getDistrictId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            Street entity = new Street();
            entity.setName(request.getName());
            entity.setDistrictId(parent.getId());
            entity.setCreatedTime(new Date());
            entity.setCreatedUserId(authenticatedUserId);
            entity = streetService.save(entity);
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
                                  @Valid @RequestBody CreateStreetDto request) {
        try {
            District parent;
            try {
                parent = districtService.findById(request.getDistrictId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            Street entity = null;
            try {
                entity = streetService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setName(request.getName());
            entity.setDistrictId(parent.getId());
            entity = streetService.save(entity);
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
            Street entity;
            try {
                entity = streetService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setDeletedTime(new Date());
            entity.setDeletedUserId(authenticatedUserId);
            streetService.save(entity);
            return ResponseEntity.ok("Street deleted.");
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }


}
