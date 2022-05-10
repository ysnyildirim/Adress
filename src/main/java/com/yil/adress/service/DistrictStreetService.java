package com.yil.adress.service;

import com.yil.adress.model.DistrictStreet;
import com.yil.adress.repository.DistrictStreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class DistrictStreetService {
    private final DistrictStreetRepository districtStreetRepository;

    @Autowired
    public DistrictStreetService(DistrictStreetRepository districtStreetRepository) {
        this.districtStreetRepository = districtStreetRepository;
    }


    public DistrictStreet save(DistrictStreet districtStreet) {
        return districtStreetRepository.save(districtStreet);
    }

    public void delete(DistrictStreet.DistrictStreetPk id) {
        districtStreetRepository.deleteById(id);
    }

    public DistrictStreet findById(DistrictStreet.DistrictStreetPk id) throws EntityNotFoundException {
        return districtStreetRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Entity not found");
        });
    }

    public Page<DistrictStreet> findAllByDistrictId(Pageable pageable, long districtId) {
        return districtStreetRepository.findAllById_District_Id(pageable, districtId);
    }

    public void delete(long districtId, long streetId) {
        districtStreetRepository.deleteById_District_IdAndAndId_Street_Id(districtId, streetId);
    }
}
