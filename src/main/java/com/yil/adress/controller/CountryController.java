package com.yil.adress.controller;

import com.yil.adress.base.Mapper;
import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.PageDto;
import com.yil.adress.base.SortOrderConverter;
import com.yil.adress.dto.CountryDto;
import com.yil.adress.dto.CountryResponse;
import com.yil.adress.dto.CreateCountryDto;
import com.yil.adress.exception.CountryNotFoundException;
import com.yil.adress.model.Country;
import com.yil.adress.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@RestController
@RequestMapping("/api/adr/v1/countries")
public class CountryController {
    private final CountryService countryService;
    private final Mapper<Country, CountryDto> mapper = new Mapper<>(CountryService::toDto);

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Id bazlı ülke bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> findById(@NotNull @PathVariable Long id) throws CountryNotFoundException {
        Country entity = countryService.findById(id);
        CountryDto dto = CountryService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Tüm ülkelere ait bilgileri getirir.")
    @GetMapping
    public ResponseEntity<PageDto<CountryDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size,
            @RequestParam(required = false) String[] sort) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        List<Sort.Order> orders = new SortOrderConverter(new String[]{"name", "code"}).convert(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
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
