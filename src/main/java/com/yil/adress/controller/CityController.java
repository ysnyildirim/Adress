package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.CityDto;
import com.yil.adress.model.City;
import com.yil.adress.service.CityService;
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
@RequestMapping("/v1/city")
public class CityController implements SecuredRestController {

    private final CityService cityService;
    private Logger logger = Logger.getLogger(CityController.class.getName());

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<CityDto>> findById(@PathVariable long id) {
        try {
            City city = cityService.findById(id);
            CityDto dto = CityService.toDto(city);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<CityDto>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
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
            Page<City> city = null;
            if (name != null && code == null)
                city = cityService.findAllByName(pageable, name);
            else if (name == null && code != null)
                city = cityService.findAllByCode(pageable, code);
            else if (name != null && code != null)
                city = cityService.findAllByNameAndCode(pageable, name, code);
            else
                city = cityService.findAll(pageable);
            PageDto<CityDto> pageDto = PageDto.toDto(city, CityService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<CityDto>>> save(@RequestBody List<CityDto> request) {
        try {
            List<CityDto> list = new ArrayList<>();
            for (CityDto dto : request) {
                City entity = CityService.toEntity(dto);
                entity = cityService.save(entity);
                CityDto countryDto = CityService.toDto(entity);
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
            cityService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("City deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
