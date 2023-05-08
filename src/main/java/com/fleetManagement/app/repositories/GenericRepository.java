package com.fleetManagement.app.repositories;

import com.fleetManagement.app.entities.GenericEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_SORT_PROPERTY;

public interface GenericRepository<T extends GenericEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    default Specification<T> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // loop through all attributes of the entity and check if the attribute is a string
            // if it is, add a "like" predicate for that attribute
            for (Field field : root.getJavaType().getDeclaredFields()) {
                if (field.getType().equals(String.class)) {
                    predicates.add(cb.like(cb.lower(root.get(field.getName())), "%" + keyword.toLowerCase() + "%"));
                }
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    default Page<T> searchByKeyword(String keyword, Pageable pageable) {
        Specification<T> spec = Specification.where(hasKeyword(keyword));
        if (pageable.getSort().isUnsorted()) {
            // If no sort order is specified, use the entity's created_at attribute as the default sort order
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DEFAULT_SORT_PROPERTY).descending());
        }
        return findAll(spec, pageable);
    }
}

