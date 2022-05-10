package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/v1/exterior-door")
public class ExteriorDoorController implements SecuredRestController {

    private Logger logger = Logger.getLogger(ExteriorDoorController.class.getName());
    private final ExteriorDoorService exteriorDoorService;

    @Autowired
    public ExteriorDoorController(ExteriorDoorService exteriorDoorService) {
        this.exteriorDoorService = exteriorDoorService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<ExteriorDoorDto>> findById(@PathVariable long id) {
        try {
            ExteriorDoor entity = exteriorDoorService.findById(id);
            ExteriorDoorDto dto = ExteriorDoorService.toDto(entity);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<ExteriorDoorDto>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size,
            @RequestParam(required = false) String[] sort) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            List<Sort.Order> orders = new SortOrderConverter(new String[]{"name", "code"}).convert(sort);
            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
            Page<ExteriorDoor> city = null;
            if (name != null)
                city = exteriorDoorService.findAllByName(pageable, name);
            else
                city = exteriorDoorService.findAll(pageable);
            PageDto<ExteriorDoorDto> pageDto = PageDto.toDto(city, ExteriorDoorService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<ExteriorDoorDto>>> save(@RequestBody List<ExteriorDoorDto> request) {
        try {
            List<ExteriorDoorDto> list = new ArrayList<>();
            for (ExteriorDoorDto dto : request) {
                ExteriorDoor entity = ExteriorDoorService.toEntity(dto);
                entity = exteriorDoorService.save(entity);
                ExteriorDoorDto countryDto = ExteriorDoorService.toDto(entity);
                list.add(countryDto);
            }
            return ResponseEntity.ok(ApiResponce.ok(list));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete/id={id}")
    public ResponseEntity<ApiResponce<String>> delete(@PathVariable long id) {
        try {
            exteriorDoorService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("ExteriorDoor deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }
}
