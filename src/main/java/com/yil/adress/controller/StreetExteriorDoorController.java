package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.model.Street;
import com.yil.adress.model.StreetExteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.StreetExteriorDoorService;
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
@RequestMapping("/v1/street-exterior-door")
public class StreetExteriorDoorController implements SecuredRestController {

    private Logger logger = Logger.getLogger(StreetExteriorDoorController.class.getName());
    private final StreetExteriorDoorService streetExteriorDoorService;
    private final StreetService streetService;
    private final ExteriorDoorService exteriorDoorService;

    @Autowired
    public StreetExteriorDoorController(StreetExteriorDoorService streetExteriorDoorService,
                                        StreetService streetService,
                                        ExteriorDoorService exteriorDoorService) {
        this.streetExteriorDoorService = streetExteriorDoorService;
        this.streetService = streetService;
        this.exteriorDoorService = exteriorDoorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<ExteriorDoorDto>>> findAll(
            @RequestParam long districtId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "1000") int pageSize) {
        try {
            if (pageNumber < 0)
                pageNumber = 0;
            if (pageSize <= 0 || pageSize > 1000)
                pageSize = 1000;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<StreetExteriorDoor> page = streetExteriorDoorService.findAllByStreetId(pageable, districtId);
            PageDto<ExteriorDoorDto> pageDto = PageDto.toDto(page, f -> ExteriorDoorService.toDto(f.getId().getExteriorDoor()));
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponce<String>> delete(@RequestParam Long streetId,
                                                      @RequestParam Long exteriorDoorId) {
        try {
            Street street = streetService.findById(streetId);
            ExteriorDoor exteriorDoor = exteriorDoorService.findById(exteriorDoorId);
            StreetExteriorDoor.StreetExteriorDoorPk pk = new StreetExteriorDoor.StreetExteriorDoorPk();
            pk.setExteriorDoor(exteriorDoor);
            pk.setStreet(street);
            streetExteriorDoorService.delete(pk);
            return ResponseEntity.ok(ApiResponce.ok("Deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<String>> save(@RequestParam Long streetId,
                                                    @RequestParam Long exteriorDoorId) {
        try {
            Street street = streetService.findById(streetId);
            ExteriorDoor exteriorDoor = exteriorDoorService.findById(exteriorDoorId);
            StreetExteriorDoor.StreetExteriorDoorPk pk = new StreetExteriorDoor.StreetExteriorDoorPk();
            pk.setExteriorDoor(exteriorDoor);
            pk.setStreet(street);
            StreetExteriorDoor entity = new StreetExteriorDoor();
            entity.setId(pk);
            streetExteriorDoorService.save(entity);
            return ResponseEntity.ok(ApiResponce.ok("Saved"));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
