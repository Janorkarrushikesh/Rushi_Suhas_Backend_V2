package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepositoryV2 extends JpaRepository<Country, Long> {


    List<Country> findByCountryCodeIn(List<String> countryCodes);
}
