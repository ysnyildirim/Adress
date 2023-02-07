package com.yil.adress.repository;

import com.yil.adress.model.RegionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionTypeDao extends JpaRepository<RegionType, Integer> {
}
