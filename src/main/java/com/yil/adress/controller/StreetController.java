package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.StreetDto;
import com.yil.adress.model.Street;
import com.yil.adress.service.StreetService;
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
@RequestMapping("/v1/street")
public class StreetController implements SecuredRestController {

    private Logger logger = Logger.getLogger(StreetController.class.getName());
    private final StreetService streetService;

    @Autowired
    public StreetController(StreetService streetService) {
        this.streetService = streetService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<StreetDto>> findById(@PathVariable long id) {
        try {
            Street city = streetService.findById(id);
            StreetDto dto = StreetService.toDto(city);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<StreetDto>>> findAll(
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
            Page<Street> city = null;
            if (name != null)
                city = streetService.findAllByName(pageable, name);
            else
                city = streetService.findAll(pageable);
            PageDto<StreetDto> pageDto = PageDto.toDto(city, StreetService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<StreetDto>>> save(@RequestBody List<StreetDto> request) {
        try {
            List<StreetDto> list = new ArrayList<>();
            for (StreetDto dto : request) {
                Street entity = StreetService.toEntity(dto);
                entity = streetService.save(entity);
                StreetDto countryDto = StreetService.toDto(entity);
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
            streetService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("Street deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }


}
