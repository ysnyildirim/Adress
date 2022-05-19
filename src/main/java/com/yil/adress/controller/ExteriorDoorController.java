package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CreateExteriorDoorDto;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.model.Street;
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
@RequestMapping("/v1/streets/{streetId}/exterior-doors")
public class ExteriorDoorController {

    private Logger logger = Logger.getLogger(ExteriorDoorController.class.getName());
    private final ExteriorDoorService exteriorDoorService;
    private final StreetService streetService;

    @Autowired
    public ExteriorDoorController(ExteriorDoorService exteriorDoorService, StreetService streetService) {
        this.exteriorDoorService = exteriorDoorService;
        this.streetService = streetService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ExteriorDoorDto> findById(@PathVariable long id) {
        try {
            ExteriorDoor entity;
            try {
                entity = exteriorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            ExteriorDoorDto dto = ExteriorDoorService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<PageDto<ExteriorDoorDto>> findAll(
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
            Page<ExteriorDoor> entities = null;
            if (name != null)
                entities = exteriorDoorService.findAllByNameAndDeletedTimeIsNull(pageable, name);
            else
                entities = exteriorDoorService.findAllByDeletedTimeIsNull(pageable);
            PageDto<ExteriorDoorDto> pageDto = PageDto.toDto(entities, ExteriorDoorService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateExteriorDoorDto request) {
        try {
            Street parent;
            try {
                parent = streetService.findById(request.getStreetId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            ExteriorDoor entity = new ExteriorDoor();
            entity.setName(request.getName());
            entity.setStreetId(parent.getId());
            entity.setCreatedTime(new Date());
            entity.setCreatedUserId(authenticatedUserId);
            entity = exteriorDoorService.save(entity);
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
                                  @Valid @RequestBody CreateExteriorDoorDto request) {
        try {
            Street parent;
            try {
                parent = streetService.findById(request.getStreetId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            ExteriorDoor entity = null;
            try {
                entity = exteriorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setName(request.getName());
            entity.setStreetId(parent.getId());
            entity = exteriorDoorService.save(entity);
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
            ExteriorDoor entity;
            try {
                entity = exteriorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setDeletedTime(new Date());
            entity.setDeletedUserId(authenticatedUserId);
            exteriorDoorService.save(entity);
            return ResponseEntity.ok("Exterior door deleted.");
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }
}
