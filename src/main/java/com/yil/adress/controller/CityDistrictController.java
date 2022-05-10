package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.model.City;
import com.yil.adress.model.CityDistrict;
import com.yil.adress.model.District;
import com.yil.adress.service.CityDistrictService;
import com.yil.adress.service.CityService;
import com.yil.adress.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/v1/city-district")
public class CityDistrictController implements SecuredRestController {

    private Logger logger = Logger.getLogger(CityDistrictController.class.getName());
    private final CityDistrictService cityDistrictService;
    private final CityService cityService;
    private final DistrictService districtService;

    @Autowired
    public CityDistrictController(CityDistrictService cityDistrictService, CityService cityService, DistrictService districtService) {
        this.cityDistrictService = cityDistrictService;
        this.cityService = cityService;
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<DistrictDto>>> findAll(
            @RequestParam long cityId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "1000") int pageSize) {
        try {
            if (pageNumber < 0)
                pageNumber = 0;
            if (pageSize <= 0 || pageSize > 1000)
                pageSize = 1000;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<CityDistrict> page = cityDistrictService.findAllByCityId(pageable, cityId);
            PageDto<DistrictDto> pageDto = PageDto.toDto(page, f -> DistrictService.toDto(f.getId().getDistrict()));
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponce<String>> delete(@RequestParam Long cityId,
                                                      @RequestParam Long districtId) {
        try {
            City city = cityService.findById(cityId);
            District district = districtService.findById(districtId);
            CityDistrict.CityDistrictPk pk = new CityDistrict.CityDistrictPk();
            pk.setDistrict(district);
            pk.setCity(city);
            cityDistrictService.delete(pk);
            return ResponseEntity.ok(ApiResponce.ok("Deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<String>> save(@RequestParam Long cityId,
                                                    @RequestParam Long districtId) {
        try {
            City city = cityService.findById(cityId);
            District district = districtService.findById(districtId);
            CityDistrict.CityDistrictPk pk = new CityDistrict.CityDistrictPk();
            pk.setDistrict(district);
            pk.setCity(city);
            CityDistrict cityDistrict = new CityDistrict();
            cityDistrict.setId(pk);
            cityDistrictService.save(cityDistrict);
            return ResponseEntity.ok(ApiResponce.ok("Saved"));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
