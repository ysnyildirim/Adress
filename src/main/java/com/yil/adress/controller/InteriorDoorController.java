package com.yil.adress.controller;

import com.yil.adress.base.Mapper;
import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.*;
import com.yil.adress.exception.ExteriorDoorNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.service.CountryService;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.InteriorDoorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/api/adr/v1/interior-doors")
public class InteriorDoorController {
    private final InteriorDoorService interiorDoorService;
    private final ExteriorDoorService exteriorDoorService;
    private final Mapper<InteriorDoor, InteriorDoorDto> mapper = new Mapper<>(InteriorDoorService::toDto);

    @Autowired
    public InteriorDoorController(InteriorDoorService interiorDoorService, ExteriorDoorService exteriorDoorService) {
        this.interiorDoorService = interiorDoorService;
        this.exteriorDoorService = exteriorDoorService;
    }

    @Operation(summary = "Id bazlı iç kapı/daire numarası bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InteriorDoorDto> findById(@PathVariable Long id) {
        InteriorDoor entity = interiorDoorService.findById(id);
        InteriorDoorDto dto = InteriorDoorService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Tüm iç kapı/daire numarası bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<InteriorDoorDto>> findAll(
            @RequestParam(required = false) Long exteriorDoorId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return ResponseEntity.ok(mapper.map(interiorDoorService.findAll(pageable)));
    }

    @Operation(summary = "Yeni bir iç kapı/daire numarası bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InteriorDoorResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                       @Valid @RequestBody CreateInteriorDoorDto request) {
        if (!exteriorDoorService.existsById(request.getExteriorDoorId()))
            throw new ExteriorDoorNotFoundException();

        InteriorDoor interiorDoor = interiorDoorService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InteriorDoorResponse.builder().id(interiorDoor.getId()).build());
    }

    @Operation(summary = "Id bazlı iç kapı/daire numarası bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody CreateInteriorDoorDto request) {
        if (!exteriorDoorService.existsById(request.getExteriorDoorId()))
            throw new ExteriorDoorNotFoundException();
        interiorDoorService.replace(id, request, authenticatedUserId);
        return ResponseEntity.ok().body("Interior door updated.");
    }

    @Operation(summary = "Id bazlı iç kapı/daire numarası bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        interiorDoorService.deleteById(id);
        return ResponseEntity.ok("Interior door deleted.");
    }
}
