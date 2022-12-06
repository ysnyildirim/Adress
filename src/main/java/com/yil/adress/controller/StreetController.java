package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CreateStreetDto;
import com.yil.adress.dto.StreetDto;
import com.yil.adress.dto.StreetResponse;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.model.Street;
import com.yil.adress.service.DistrictService;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adr/v1/streets")
public class StreetController {
    private final StreetService streetService;
    private final DistrictService districtService;
    private final Mapper<Street, StreetDto> mapper = new Mapper<>(StreetService::toDto);

    @Operation(summary = "Id bazlı cadde/sokak bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<StreetDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.map(streetService.findById(id)));
    }

    @Operation(summary = "Tüm cadde/sokak bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<StreetDto>> findAll(
            @RequestParam(required = false) Long districtId,
            @PageableDefault Pageable pageable) {
        if (districtId == null)
            return ResponseEntity.ok(mapper.map(streetService.findAll(pageable)));
        else
            return ResponseEntity.ok(mapper.map(streetService.findAllByDistrictId(pageable, districtId)));
    }

    @Operation(summary = "Yeni bir cadde/sokak bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StreetResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @Valid @RequestBody CreateStreetDto dto) {
        if (!districtService.existsById(dto.getDistrictId()))
            throw new DistrictNotFoundException();
        Street street = streetService.save(dto, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(StreetResponse.builder().id(street.getId()).build());
    }

    @Operation(summary = "İd bazlı cadde/sokak bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody CreateStreetDto request) {
        if (!districtService.existsById(request.getDistrictId()))
            throw new DistrictNotFoundException();
        streetService.replace(id, request, authenticatedUserId);
        return ResponseEntity.ok().body("Street updated.");
    }

    @Operation(summary = "İd bazlı cadde/sokak bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        streetService.deleteById(id);
        return ResponseEntity.ok("Street deleted.");
    }
}
