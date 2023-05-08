package com.fleetManagement.app.repositories;

import com.fleetManagement.app.entities.AppUserRole;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);

    long countByRoles_Name(GenericEnum.RoleName roleName);

    default List<User> searchByKeywordAndRole(String keyword, Pageable pageable, GenericEnum.RoleName roleName) {
        Specification<User> roleSpec = (root, query, criteriaBuilder) -> {
            Join<User, AppUserRole> rolesJoin = root.join("roles", JoinType.INNER);
            return criteriaBuilder.equal(rolesJoin.get("name"), roleName);
        };
        Specification<User> keywordSpec = Specification.where(hasKeyword(keyword));
        return findAll(Specification.where(roleSpec).and(keywordSpec), pageable).toList();
    }

}
