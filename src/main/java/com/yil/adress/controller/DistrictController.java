package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
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
@RequestMapping("/v1/cities/{cityId}/districts")
public class DistrictController {

    private Logger logger = Logger.getLogger(DistrictController.class.getName());
    private final DistrictService districtService;
    private final CityService cityService;

    @Autowired
    public DistrictController(DistrictService districtService, CityService cityService) {
        this.districtService = districtService;
        this.cityService = cityService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DistrictDto> findById(@PathVariable long id) {
        try {
            District entity;
            try {
                entity = districtService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            DistrictDto dto = DistrictService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<PageDto<DistrictDto>> findAll(
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
            Page<District> entities = null;
            if (name != null)
                entities = districtService.findAllByNameAndDeletedTimeIsNull(pageable, name);
            else
                entities = districtService.findAllByDeletedTimeIsNull(pageable);
            PageDto<DistrictDto> pageDto = PageDto.toDto(entities, DistrictService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<DistrictDto>> create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                    @Valid @RequestBody CreateDistrictDto request) {
        try {
            City parent;
            try {
                parent = cityService.findById(request.getCityId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            District entity = new District();
            entity.setName(request.getName());
            entity.setCode(request.getCode());
            entity.setCityId(parent.getId());
            entity.setCreatedTime(new Date());
            entity.setCreatedUserId(authenticatedUserId);
            entity = districtService.save(entity);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DistrictDto>> replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody CreateDistrictDto request) {
        try {
            City parent;
            try {
                parent = cityService.findById(request.getCityId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            District entity = null;
            try {
                entity = districtService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setName(request.getName());
            entity.setCode(request.getCode());
            entity.setCityId(parent.getId());
            entity = districtService.save(entity);
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
            District entity;
            try {
                entity = districtService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setDeletedTime(new Date());
            entity.setDeletedUserId(authenticatedUserId);
            districtService.save(entity);
            return ResponseEntity.ok("Country deleted.");
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }


}
