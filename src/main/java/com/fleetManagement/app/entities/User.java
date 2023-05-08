package com.fleetManagement.app.entities;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User extends GenericEntity implements UserDetails {
    private String firstName;
    private String lastName;
    @Column(name = "email", nullable = false, unique = true, length = 200)
    private String email;
    private String password;
    private LocalDate birthDay;

    private String profilePicture;

    private String verificationCode;

    private String bio;

    private boolean enabled;

    private String resetId;

    private String RefreshTokenId;

    private boolean profileCompleted;
    @Enumerated(EnumType.STRING)
    private GenericEnum.Gender gender;

    @Embedded
    private Address address;

    @Embedded
    private PhoneNumber phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppUserRole> roles;

    public void addRole(AppUserRole role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.isNull(this.roles) || this.roles.isEmpty())
            return Collections.emptyList();
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
