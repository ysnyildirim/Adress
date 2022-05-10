package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.model.District;
import com.yil.adress.service.DistrictService;
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
@RequestMapping("/v1/district")
public class DistrictController implements SecuredRestController {

    private Logger logger = Logger.getLogger(DistrictController.class.getName());
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<DistrictDto>> findById(@PathVariable long id) {
        try {
            District entity = districtService.findById(id);
            DistrictDto dto = DistrictService.toDto(entity);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<DistrictDto>>> findAll(
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
            Page<District> city = null;
            if (name != null && code == null)
                city = districtService.findAllByName(pageable, name);
            else if (name == null && code != null)
                city = districtService.findAllByCode(pageable, code);
            else if (name != null && code != null)
                city = districtService.findAllByNameAndCode(pageable, name, code);
            else
                city = districtService.findAll(pageable);
            PageDto<DistrictDto> pageDto = PageDto.toDto(city, DistrictService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<DistrictDto>>> save(@RequestBody List<DistrictDto> request) {
        try {
            List<DistrictDto> list = new ArrayList<>();
            for (DistrictDto dto : request) {
                District entity = DistrictService.toEntity(dto);
                entity = districtService.save(entity);
                DistrictDto countryDto = DistrictService.toDto(entity);
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
            districtService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("District deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }


}
