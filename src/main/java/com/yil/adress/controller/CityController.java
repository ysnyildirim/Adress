package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CityDto;
import com.yil.adress.dto.CityResponse;
import com.yil.adress.dto.CreateCityDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.City;
import com.yil.adress.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adr/v1/cities")
public class CityController {
    private final CityService cityService;
    private final Mapper<City, CityDto> mapper = new Mapper<>(CityService::toDto);

    @Operation(summary = "Id bazlı şehir bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.map(cityService.findById(id)));
    }

    @Operation(summary = "Tüm şehirlere ait bilgileri getirir.")
    @GetMapping
    public ResponseEntity<PageDto<CityDto>> findAll(@RequestParam(required = false) Long countryId,
                                                    @PageableDefault Pageable pageable) {
        if (countryId == null)
            return ResponseEntity.ok(mapper.map(cityService.findAll(pageable)));
        else
            return ResponseEntity.ok(mapper.map(cityService.findAllByCountryId(pageable, countryId)));
    }

    @Operation(summary = "Yeni bir şehir bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CityResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody CreateCityDto request) throws CountryNotFoundException {
        City city = cityService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CityResponse.builder().id(city.getId()).build());

    }

    @Operation(summary = "İd bazlı şehir bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CityResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody CreateCityDto request) throws CountryNotFoundException {
        City city = cityService.replace(id, request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CityResponse.builder().id(city.getId()).build());
    }

    @Operation(summary = "İd bazlı şehir bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        cityService.deleteById(id);
        return ResponseEntity.ok("City deleted.");

    }
}
