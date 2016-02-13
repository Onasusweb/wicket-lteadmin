package com.bustanil.myapp.demo.repository;

import com.bustanil.myapp.demo.model.Occupation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OccupationRepository extends PagingAndSortingRepository<Occupation, Long> {
}
