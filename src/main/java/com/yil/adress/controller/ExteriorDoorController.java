package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CreateExteriorDoorDto;
import com.yil.adress.dto.ExteriorDoorDto;
import com.yil.adress.dto.ExteriorDoorResponse;
import com.yil.adress.exception.StreetNotFoundException;
import com.yil.adress.model.ExteriorDoor;
import com.yil.adress.service.ExteriorDoorService;
import com.yil.adress.service.StreetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/adr/v1/exterior-doors")
public class ExteriorDoorController {
    private final ExteriorDoorService exteriorDoorService;
    private final StreetService streetService;
    private final Mapper<ExteriorDoor, ExteriorDoorDto> mapper = new Mapper<>(ExteriorDoorService::toDto);

    @Operation(summary = "Id bazlı dış kapı/bina numarası bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ExteriorDoorDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.map(exteriorDoorService.findById(id)));
    }

    @Operation(summary = "Tüm dış kapı/bina numarası bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<ExteriorDoorDto>> findAll(
            @RequestParam(required = false) Long streetId,
            @PageableDefault Pageable pageable) {
        if (streetId == null)
            return ResponseEntity.ok(mapper.map(exteriorDoorService.findAll(pageable)));
        else
            return ResponseEntity.ok(mapper.map(exteriorDoorService.findAllByStreetId(pageable, streetId)));
    }

    @Operation(summary = "Yeni bir dış kapı/bina numarası bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExteriorDoorResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                       @Valid @RequestBody CreateExteriorDoorDto request) {
        if (!streetService.existsById(request.getStreetId()))
            throw new StreetNotFoundException();
        ExteriorDoor exteriorDoor = exteriorDoorService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ExteriorDoorResponse.builder().id(exteriorDoor.getId()).build());
    }

    @Operation(summary = "İd bazlı dış kapı/bina numarası bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody CreateExteriorDoorDto request) {
        if (!streetService.existsById(request.getStreetId()))
            throw new StreetNotFoundException();
        exteriorDoorService.replace(id, request, authenticatedUserId);
        return ResponseEntity.ok().body("Exterior door updated.");
    }

    @Operation(summary = "İd bazlı dış kapı/bina numarası bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        exteriorDoorService.deleteById(id);
        return ResponseEntity.ok("Exterior door deleted.");
    }
}
