package com.yil.adress.service;

import com.yil.adress.base.Mapper;
import com.yil.adress.base.PageDto;
import com.yil.adress.dto.CreateRegionDto;
import com.yil.adress.dto.RegionDto;
import com.yil.adress.exception.RegionNotFoundException;
import com.yil.adress.exception.RegionTypeNotFoundException;
import com.yil.adress.model.Region;
import com.yil.adress.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RegionService {


    public static RegionDto turkiye;
    private final RegionRepository regionRepository;
    private final RegionTypeService regionTypeService;

    private final Mapper<Region, RegionDto> mapper = new Mapper<>(RegionService::toDto);

    @Autowired
    public RegionService(RegionRepository regionRepository, RegionTypeService regionTypeService) {
        this.regionRepository = regionRepository;
        this.regionTypeService = regionTypeService;
    }

    public static RegionDto toDto(Region region) throws NullPointerException {
        RegionDto dto = new RegionDto();
        dto.setId(region.getId());
        dto.setName(region.getName());
        dto.setRegionTypeId(region.getRegionTypeId());
        dto.setParentId(region.getParentId());
        dto.setEnabled(region.getEnabled());
        dto.setCode(region.getCode());
        return dto;
    }

    public RegionDto save(CreateRegionDto dto, long userId) throws RegionNotFoundException, RegionTypeNotFoundException {
        if (!regionTypeService.existsById(dto.getRegionTypeId()))
            throw new RegionTypeNotFoundException();
        Region region = new Region();
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        region.setRegionTypeId(dto.getRegionTypeId());
        region.setEnabled(dto.getEnabled());
        region.setParentId(dto.getParentId());
        region.setCreatedDate(new Date());
        region.setCreatedUserId(userId);
        return mapper.map(regionRepository.save(region));
    }

    public RegionDto replace(long id, CreateRegionDto dto, long userId) throws RegionNotFoundException, RegionTypeNotFoundException {
        if (!regionTypeService.existsById(dto.getRegionTypeId()))
            throw new RegionTypeNotFoundException();
        Region region = regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        region.setEnabled(dto.getEnabled());
        region.setRegionTypeId(dto.getRegionTypeId());
        region.setParentId(dto.getParentId());
        region.setLastModifyDate(new Date());
        region.setLastModifyUserId(userId);
        return mapper.map(regionRepository.save(region));
    }

    public RegionDto findById(long id) throws RegionNotFoundException {
        return mapper.map(regionRepository.findById(id).orElseThrow(RegionNotFoundException::new));
    }

    public void deleteById(long id) {
        regionRepository.deleteById(id);
    }

    public PageDto<RegionDto> findAllByRegionId(Pageable pageable, long parentRegionId) {
        return mapper.map(regionRepository.findAllByParentId(pageable, parentRegionId));
    }

    public PageDto<RegionDto> findAll(Pageable pageable) {
        return mapper.map(regionRepository.findAll(pageable));
    }

    public boolean existsById(long id) {
        return regionRepository.existsById(id);
    }
}
