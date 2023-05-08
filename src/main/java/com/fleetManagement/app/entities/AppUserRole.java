package com.fleetManagement.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AppUserRole extends GenericEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private GenericEnum.RoleName name;

}
