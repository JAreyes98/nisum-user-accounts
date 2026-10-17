package com.jreyes.accountsapi.domain;

import com.jreyes.accountsapi.dto.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class User extends AuditModel implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotBlank(message = "Username is required")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", length = 250, unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!._-)"
    )
    @Column(name = "password", length = 250)
    private String password;

    @Column(name = "active")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "token")
    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    public void addPhone(Phone phone) {
        if (this.phones == null) {
            this.phones = new java.util.ArrayList<>();
        }
        this.phones.add(phone);
        phone.setUser(this);
    }

    public void removePhone(Phone phone) {
        if (this.phones != null) {
            this.phones.remove(phone);
            phone.setUser(null);
        }
    }

    /**
     * Creates a new instance of User
     * @param dto request object
     * @return User instance
     */
    public static User fromRequest(UserRequestDto dto) {
        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setPhones(new java.util.ArrayList<>());
        if (dto.phones() != null) {
            dto.phones().forEach(phoneReq -> {
                var phone = Phone.fromRequest(phoneReq, user);
                phone.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
                user.addPhone(phone);
            });
        }
        return user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
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
        return getIsActive();
    }
}