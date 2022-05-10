package com.yil.adress.controller;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SecuredRestController;
import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.model.ExteriorDoorInteriorDoor;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.service.ExteriorDoorInteriorDoorService;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.InteriorDoorService;
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
@RequestMapping("/v1/exterior-door-interior-door")
public class ExteriorDoorInteriorDoorController implements SecuredRestController {

    private Logger logger = Logger.getLogger(ExteriorDoorInteriorDoorController.class.getName());
    private final ExteriorDoorInteriorDoorService exteriorDoorInteriorDoorService;
    private final InteriorDoorService interiorDoorService;
    private final ExteriorDoorService exteriorDoorService;

    @Autowired
    public ExteriorDoorInteriorDoorController(ExteriorDoorInteriorDoorService exteriorDoorInteriorDoorService,
                                              InteriorDoorService interiorDoorService,
                                              ExteriorDoorService exteriorDoorService) {
        this.exteriorDoorInteriorDoorService = exteriorDoorInteriorDoorService;
        this.interiorDoorService = interiorDoorService;
        this.exteriorDoorService = exteriorDoorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponce<PageDto<InteriorDoorDto>>> findAll(
            @RequestParam long exteriorDoorId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "1000") int pageSize) {
        try {
            if (pageNumber < 0)
                pageNumber = 0;
            if (pageSize <= 0 || pageSize > 1000)
                pageSize = 1000;
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<ExteriorDoorInteriorDoor> page = exteriorDoorInteriorDoorService.findAllByExteriorDoorId(pageable, exteriorDoorId);
            PageDto<InteriorDoorDto> pageDto = PageDto.toDto(page, f -> InteriorDoorService.toDto(f.getId().getInteriorDoor()));
            return ResponseEntity.ok(ApiResponce.ok(pageDto));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ApiResponce<String>> delete(@RequestParam Long exteriorDoorId,
                                                      @RequestParam Long interiorDoorId) {
        try {
            InteriorDoor interiorDoor = interiorDoorService.findById(interiorDoorId);
            ExteriorDoor exteriorDoor = exteriorDoorService.findById(exteriorDoorId);
            ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk pk = new ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk();
            pk.setExteriorDoor(exteriorDoor);
            pk.setInteriorDoor(interiorDoor);
            exteriorDoorInteriorDoorService.delete(pk);
            return ResponseEntity.ok(ApiResponce.ok("Deleted."));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponce<String>> save(@RequestParam Long exteriorDoorId,
                                                    @RequestParam Long interiorDoorId) {
        try {
            InteriorDoor interiorDoor = interiorDoorService.findById(interiorDoorId);
            ExteriorDoor exteriorDoor = exteriorDoorService.findById(exteriorDoorId);
            ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk pk = new ExteriorDoorInteriorDoor.ExteriorDoorInteriorDoorPk();
            pk.setExteriorDoor(exteriorDoor);
            pk.setInteriorDoor(interiorDoor);
            ExteriorDoorInteriorDoor entity = new ExteriorDoorInteriorDoor();
            entity.setId(pk);
            exteriorDoorInteriorDoorService.save(entity);
            return ResponseEntity.ok(ApiResponce.ok("Saved"));
        } catch (Exception exception) {
            
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

}
