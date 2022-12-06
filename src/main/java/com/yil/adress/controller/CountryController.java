package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CountryResponse;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adr/v1/countries")
public class CountryController {
    private final CountryService countryService;
    private final Mapper<Country, CountryDto> mapper = new Mapper<>(CountryService::toDto);

    @Operation(summary = "Id bazlı ülke bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> findById(@NotNull @PathVariable Long id) throws CountryNotFoundException {
        return ResponseEntity.ok(mapper.map(countryService.findById(id)));
    }

    @Operation(summary = "Tüm ülkelere ait bilgileri getirir.")
    @GetMapping
    public ResponseEntity<PageDto<CountryDto>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(mapper.map(countryService.findAll(pageable)));
    }

    @Operation(summary = "Yeni bir ülke bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CountryResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @RequestBody @Valid CreateCountryDto dto) {
        Country country = countryService.save(dto, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CountryResponse.builder().id(country.getId()).build());
    }

    @Operation(summary = "İd bazlı ülke bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long id,
                                          @RequestBody @Valid CreateCountryDto dto) throws CountryNotFoundException {
        countryService.replace(id, dto, authenticatedUserId);
        return ResponseEntity.ok().body("Country updated.");
    }

    @Operation(summary = "İd bazlı ülke bilgisi silmek için kullanılır.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        countryService.deleteById(id);
        return ResponseEntity.ok("Country deleted.");
    }

}
