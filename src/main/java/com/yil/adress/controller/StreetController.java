package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.*;
import com.yil.adress.exception.DistrictNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.Street;
import com.yil.adress.service.CountryService;
import com.yil.adress.service.DistrictService;
import com.yil.adress.service.StreetService;
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
@RequestMapping("/api/adr/v1/streets")
public class StreetController {
    private final StreetService streetService;
    private final DistrictService districtService;
    private final Mapper<Street, StreetDto> mapper = new Mapper<>(StreetService::toDto);

    @Autowired
    public StreetController(StreetService streetService, DistrictService districtService) {
        this.streetService = streetService;
        this.districtService = districtService;
    }

    @Operation(summary = "Id bazlı cadde/sokak bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<StreetDto> findById(@PathVariable Long id) {
        Street entity = streetService.findById(id);
        StreetDto dto = StreetService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Tüm cadde/sokak bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<StreetDto>> findAll(
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return ResponseEntity.ok(mapper.map(streetService.findAll(pageable)));
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
