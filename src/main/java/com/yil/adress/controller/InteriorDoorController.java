package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CreateInteriorDoorDto;
import com.yil.adress.dto.InteriorDoorDto;
import com.yil.adress.dto.InteriorDoorResponse;
import com.yil.adress.exception.ExteriorDoorNotFoundException;
import com.yil.adress.model.InteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.InteriorDoorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adr/v1/interior-doors")
public class InteriorDoorController {
    private final InteriorDoorService interiorDoorService;
    private final ExteriorDoorService exteriorDoorService;
    private final Mapper<InteriorDoor, InteriorDoorDto> mapper = new Mapper<>(InteriorDoorService::toDto);

    @Operation(summary = "Id bazlı iç kapı/daire numarası bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InteriorDoorDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.map(interiorDoorService.findById(id)));
    }

    @Operation(summary = "Tüm iç kapı/daire numarası bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<InteriorDoorDto>> findAll(
            @RequestParam(required = false) Long exteriorDoorId,
            @PageableDefault Pageable pageable) {
        if (exteriorDoorId == null)
            return ResponseEntity.ok(mapper.map(interiorDoorService.findAll(pageable)));
        else
            return ResponseEntity.ok(mapper.map(interiorDoorService.findByExteriorDoorId(pageable, exteriorDoorId)));
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
