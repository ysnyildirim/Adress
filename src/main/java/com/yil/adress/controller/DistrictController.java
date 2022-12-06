package com.yil.adress.controller;

import com.yil.adress.base.Mapper;
import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CountryResponse;
import com.yil.adress.dto.CreateDistrictDto;
import com.yil.adress.dto.DistrictDto;
import com.yil.adress.dto.DistrictResponse;
import com.yil.adress.exception.CityNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.model.District;
import com.yil.adress.service.CityService;
import com.yil.adress.service.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adr/v1/districts")
public class DistrictController {
    private final DistrictService districtService;
    private final CityService cityService;
    private final Mapper<District, DistrictDto> mapper = new Mapper<>(DistrictService::toDto);

    @Operation(summary = "Id bazlı ilçe/bölge bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DistrictDto> findById(@PathVariable long id) {
        return ResponseEntity.ok(mapper.map(districtService.findById(id)));
    }

    @Operation(summary = "Tüm ilçe/bölge bilgilerini getirir.")
    @GetMapping
    public ResponseEntity<PageDto<DistrictDto>> findAll(
            @RequestParam(required = false) Long cityId,
            @PageableDefault Pageable pageable) {
        if (cityId == null)
            return ResponseEntity.ok(mapper.map(districtService.findAll(pageable)));
        else
            return ResponseEntity.ok(mapper.map(districtService.findAllByCityId(pageable, cityId)));
    }

    @Operation(summary = "Yeni bir ilçe/bölge bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DistrictResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                   @Valid @RequestBody CreateDistrictDto request) {
        if (!cityService.existsById(request.getCityId()))
            throw new CityNotFoundException();
        District district = districtService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(DistrictResponse.builder().id(district.getId()).build());
    }

    @Operation(summary = "İd bazlı ilçe/bölge bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @Valid @RequestBody CreateDistrictDto request) {
        if (!cityService.existsById(request.getCityId()))
            throw new CityNotFoundException();
        districtService.replace(id, request, authenticatedUserId);
        return ResponseEntity.ok().body("District updated.");
    }

    @Operation(summary = "İd bazlı ilçe/bölge bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        districtService.deleteById(id);
        return ResponseEntity.ok("Country deleted.");
    }
}
