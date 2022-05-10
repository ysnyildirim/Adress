package com.yil.adress.controller;

import com.yil.adress.base.*;
import com.yil.adress.dto.CountryDto;
import com.yil.adress.model.Country;
import com.yil.adress.service.CountryService;
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
@RequestMapping("/v1/country")
public class CountryController implements SecuredRestController {

    private Logger logger = Logger.getLogger(CountryController.class.getName());

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @GetMapping(value = "/id={id}")
    public ResponseEntity<ApiResponce<CountryDto>> findById(@PathVariable long id) {
        try {
            Country city = countryService.findById(id);
            CountryDto dto = CountryService.toDto(city);
            return ResponseEntity.ok(ApiResponce.ok(dto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<CountryDto>>> findAll(
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
            Page<Country> city = null;
            if (name != null && code == null)
                city = countryService.findAllByName(pageable, name);
            else if (name == null && code != null)
                city = countryService.findAllByCode(pageable, code);
            else if (name != null && code != null)
                city = countryService.findAllByNameAndCode(pageable, name, code);
            else
                city = countryService.findAll(pageable);
            PageDto<CountryDto> pageDto = PageDto.toDto(city, CountryService::toDto);
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<List<CountryDto>>> save(@RequestBody List<CountryDto> request) {
        try {
            List<CountryDto> list = new ArrayList<>();
            for (CountryDto dto : request) {
                Country entity = CountryService.toEntity(dto);
                entity = countryService.save(entity);
                CountryDto countryDto = CountryService.toDto(entity);
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
            countryService.delete(id);
            return ResponseEntity.ok(ApiResponce.ok("Country deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }


}
