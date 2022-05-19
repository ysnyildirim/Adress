package com.yil.adress.controller;

import com.yil.adress.base.ApiHeaders;
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
@RequestMapping("/v1/exterior-doors/{exteriorDoorId}/interior-doors")
public class InteriorDoorController {

    private Logger logger = Logger.getLogger(InteriorDoorController.class.getName());
    private final InteriorDoorService interiorDoorService;
    private final ExteriorDoorService exteriorDoorService;

    @Autowired
    public InteriorDoorController(InteriorDoorService interiorDoorService, ExteriorDoorService exteriorDoorService) {
        this.interiorDoorService = interiorDoorService;
        this.exteriorDoorService = exteriorDoorService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InteriorDoorDto> findById(@PathVariable long id) {
        try {
            InteriorDoor entity;
            try {
                entity = interiorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            InteriorDoorDto dto = InteriorDoorService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<PageDto<InteriorDoorDto>> findAll(
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
            Page<InteriorDoor> entities = null;
            if (name != null)
                entities = interiorDoorService.findAllByNameAndDeletedTimeIsNull(pageable, name);
            else
                entities = interiorDoorService.findAllByDeletedTimeIsNull(pageable);
            PageDto<InteriorDoorDto> pageDto = PageDto.toDto(entities, InteriorDoorService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateInteriorDoorDto request) {
        try {
            ExteriorDoor parent;
            try {
                parent = exteriorDoorService.findById(request.getExteriorDoorId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            InteriorDoor entity = new InteriorDoor();
            entity.setName(request.getName());
            entity.setExteriorDoorId(parent.getId());
            entity.setCreatedTime(new Date());
            entity.setCreatedUserId(authenticatedUserId);
            entity = interiorDoorService.save(entity);
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
                                  @Valid @RequestBody CreateInteriorDoorDto request) {
        try {
            ExteriorDoor parent;
            try {
                parent = exteriorDoorService.findById(request.getExteriorDoorId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            InteriorDoor entity = null;
            try {
                interiorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setName(request.getName());
            entity.setExteriorDoorId(parent.getId());
            entity = interiorDoorService.save(entity);
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
            InteriorDoor entity;
            try {
                entity = interiorDoorService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            entity.setDeletedTime(new Date());
            entity.setDeletedUserId(authenticatedUserId);
            interiorDoorService.save(entity);
            return ResponseEntity.ok("Interior door deleted.");
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }
}
