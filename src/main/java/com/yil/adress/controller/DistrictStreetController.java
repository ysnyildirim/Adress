package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.StreetDto;
import com.yil.adress.model.District;
import com.yil.adress.model.DistrictStreet;
import com.yil.adress.model.Street;
import com.yil.adress.service.DistrictService;
import com.yil.adress.service.DistrictStreetService;
import com.yil.adress.service.StreetService;
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
@RequestMapping("/v1/district-street")
public class DistrictStreetController implements SecuredRestController {

    private Logger logger = Logger.getLogger(DistrictStreetController.class.getName());

    private final DistrictStreetService districtStreetService;
    private final StreetService streetService;
    private final DistrictService districtService;

    @Autowired
    public DistrictStreetController(DistrictStreetService districtStreetService,
                                    StreetService streetService,
                                    DistrictService districtService) {
        this.districtStreetService = districtStreetService;
        this.streetService = streetService;
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<StreetDto>>> findAll(
            @RequestParam long districtId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "1000") int pageSize) {
        try {
            if (pageNumber < 0)
                pageNumber = 0;
            if (pageSize <= 0 || pageSize > 1000)
                pageSize = 1000;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<DistrictStreet> page = districtStreetService.findAllByDistrictId(pageable, districtId);
            PageDto<StreetDto> pageDto = PageDto.toDto(page, f -> StreetService.toDto(f.getId().getStreet()));
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponce<String>> delete(@RequestParam Long districtId,
                                                      @RequestParam Long streetId) {
        try {
            Street street = streetService.findById(streetId);
            District district = districtService.findById(districtId);
            DistrictStreet.DistrictStreetPk pk = new DistrictStreet.DistrictStreetPk();
            pk.setDistrict(district);
            pk.setStreet(street);
            districtStreetService.delete(pk);
            return ResponseEntity.ok(ApiResponce.ok("Deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<String>> save(@RequestParam Long districtId,
                                                    @RequestParam Long streetId) {
        try {
            Street street = streetService.findById(streetId);
            District district = districtService.findById(districtId);
            DistrictStreet.DistrictStreetPk pk = new DistrictStreet.DistrictStreetPk();
            pk.setDistrict(district);
            pk.setStreet(street);
            DistrictStreet entity = new DistrictStreet();
            entity.setId(pk);
            districtStreetService.save(entity);
            return ResponseEntity.ok(ApiResponce.ok("Saved"));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
