package com.bustanil.myapp.demo.repository;

import com.bustanil.myapp.demo.model.EconomySector;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EconomySectorRepository extends PagingAndSortingRepository<EconomySector, Long> {
}
