package com.calculator.postcode.dao;

import com.calculator.postcode.entity.PostcodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PostcodesDao extends JpaRepository<PostcodesEntity, BigInteger>, JpaSpecificationExecutor<PostcodesEntity> {

    List<PostcodesEntity> findByPostcodeIn(List<String> postcode);

}
