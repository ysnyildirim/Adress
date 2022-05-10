package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.service.InteriorDoorService;
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
@RequestMapping("/v1/interior-door")
public class InteriorDoorController implements SecuredRestController {

    private Logger logger = Logger.getLogger(InteriorDoorController.class.getName());
    private final InteriorDoorService interiorDoorService;

    @Autowired
    public InteriorDoorController(InteriorDoorService interiorDoorService) {
        this.interiorDoorService = interiorDoorService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<InteriorDoorDto>> findById(@PathVariable long id) {
        try {
            InteriorDoor entity = interiorDoorService.findById(id);
            InteriorDoorDto dto = InteriorDoorService.toDto(entity);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<InteriorDoorDto>>> findAll(
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
            Page<InteriorDoor> city = null;
            if (name != null)
                city = interiorDoorService.findAllByName(pageable, name);
            else
                city = interiorDoorService.findAll(pageable);
            PageDto<InteriorDoorDto> pageDto = PageDto.toDto(city, InteriorDoorService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<InteriorDoorDto>>> save(@RequestBody List<InteriorDoorDto> request) {
        try {
            List<InteriorDoorDto> list = new ArrayList<>();
            for (InteriorDoorDto dto : request) {
                InteriorDoor entity = InteriorDoorService.toEntity(dto);
                entity = interiorDoorService.save(entity);
                InteriorDoorDto countryDto = InteriorDoorService.toDto(entity);
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
            interiorDoorService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("InteriorDoor deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }
}
