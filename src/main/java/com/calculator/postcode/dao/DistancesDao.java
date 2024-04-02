package com.calculator.postcode.dao;

import com.calculator.postcode.entity.DistancesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DistancesDao extends JpaRepository<DistancesEntity, BigInteger>, JpaSpecificationExecutor<DistancesEntity> {

}
