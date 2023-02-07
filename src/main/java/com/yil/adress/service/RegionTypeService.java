package com.yil.adress.service;

import com.yil.adress.base.Mapper;
import com.yil.adress.dto.CreateRegionTypeRequest;
import com.yil.adress.dto.RegionTypeDto;
import com.yil.adress.exception.RegionTypeNotFoundException;
import com.yil.adress.model.RegionType;
import com.yil.adress.repository.RegionTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RegionTypeService {

    public static RegionTypeDto ulke;
    public static RegionTypeDto il;
    public static RegionTypeDto ilce;
    public static RegionTypeDto koy;
    public static RegionTypeDto bucak;
    public static RegionTypeDto mahalle;
    private final RegionTypeDao regionTypeDao;

    private final Mapper<RegionType, RegionTypeDto> mapper = new Mapper<>(RegionTypeService::toDto);

    @Autowired
    public RegionTypeService(RegionTypeDao regionTypeDao) {
        this.regionTypeDao = regionTypeDao;
    }

    public static RegionTypeDto toDto(RegionType f) {
        return RegionTypeDto.builder()
                .id(f.getId())
                .name(f.getName())
                .enabled(f.getEnabled())
                .parentId(f.getParentId())
                .build();
    }

    public boolean existsById(Integer id) {
        return regionTypeDao.existsById(id);
    }

    public RegionTypeDto save(CreateRegionTypeRequest request, long userId) {
        RegionType regionType = new RegionType();
        regionType.setName(request.getName());
        regionType.setEnabled(request.getEnabled());
        regionType.setParentId(request.getParentId());
        regionType.setCreatedTime(new Date());
        regionType.setCreatedUserId(userId);
        regionType = regionTypeDao.save(regionType);
        return mapper.map(regionType);
    }

    public RegionTypeDto findById(Integer id) throws RegionTypeNotFoundException {
        return mapper.map(regionTypeDao.findById(id).orElseThrow(RegionTypeNotFoundException::new));
    }

    public void deleteById(Integer id) {
        regionTypeDao.deleteById(id);
    }

    public List<RegionTypeDto> findAll() {
        return mapper.map(regionTypeDao.findAll());
    }
}
