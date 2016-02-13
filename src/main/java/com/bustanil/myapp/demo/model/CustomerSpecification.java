package com.bustanil.myapp.demo.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class CustomerSpecification implements Specification<Customer> {

    private String name;
    private Character gender;
    private Date birthDate;

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate mainPredicate = cb.and();
        if (!StringUtils.isBlank(name)) {
            Predicate predicate = cb.like(root.<String>get("name"), "%" + this.name + "%");
            mainPredicate = cb.and(mainPredicate, predicate);
        }
        if (gender != null) {
            Predicate predicate = cb.equal(root.<String>get("gender"), this.gender);
            mainPredicate = cb.and(mainPredicate, predicate);
        }
        if (birthDate != null) {
            Predicate predicate = cb.equal(root.<String>get("birthDate"), this.birthDate);
            mainPredicate = cb.and(mainPredicate, predicate);
        }
        return mainPredicate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
