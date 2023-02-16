package com.yil.adress.controller;

import com.yil.adress.base.ApiConstant;
import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CreateRegionDto;
import com.yil.adress.dto.RegionDto;
import com.yil.adress.dto.RegionResponse;
import com.yil.adress.model.Region;
import com.yil.adress.repository.RegionRepository;
import com.yil.adress.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adr/v1/region")
public class RegionController {
    private final RegionService regionService;

    @Operation(summary = "Id bazlı bölge bilgilerini getirir.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<RegionDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @Operation(summary = "Tüm bölgelere ait bilgileri getirir.")
    @GetMapping
    public ResponseEntity<PageDto<RegionDto>> findAll(@RequestParam(required = false) Long parentRegionId,
                                                      @PageableDefault Pageable pageable) {
        if (parentRegionId == null)
            return ResponseEntity.ok(regionService.findAll(pageable));
        else
            return ResponseEntity.ok(regionService.findAllByRegionId(pageable, parentRegionId));
    }

    @Operation(summary = "Yeni bir bölge bilgisi eklemek için kullanılır.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegionResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @Valid @RequestBody CreateRegionDto request) {
        RegionDto regionDto = regionService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(RegionResponse.builder().id(regionDto.getId()).build());

    }

    @Operation(summary = "İd bazlı bölge bilgisi güncellemek için kullanılır.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegionResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody CreateRegionDto request) {
        RegionDto regionDto = regionService.replace(id, request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(RegionResponse.builder().id(regionDto.getId()).build());
    }

}
