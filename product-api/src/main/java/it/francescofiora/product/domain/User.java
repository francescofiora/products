package it.francescofiora.product.domain;

import java.util.Arrays;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USER", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
public class User extends AbstractDomain implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrder")
  @SequenceGenerator(name = "seqOrder", sequenceName = "SEQ_USER", schema = "STORE")
  private Long id;

  @NotNull
  @Column(name = "username", nullable = false)
  private String username;

  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @Column(name = "role", nullable = false)
  private String role;

  @Column(name = "enabled", columnDefinition = "smallint default 1")
  private boolean enabled;

  @Column(name = "account_non_expired", columnDefinition = "smallint default 1")
  private boolean accountNonExpired;

  @Column(name = "account_non_locked", columnDefinition = "smallint default 1")
  private boolean accountNonLocked;

  @Column(name = "credentials_non_expired", columnDefinition = "smallint default 1")
  private boolean credentialsNonExpired;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + getRole()));
  }

  @Override
  public String toString() {
    return "User{" + "id=" + getId() + ", username='" + getUsername() + "'" + ", password='"
        + (getPassword() != null ? getPassword().hashCode() : "null") + "'" + ", role='" + getRole()
        + "'" + ", enabled='" + isEnabled() + "'" + ", accountNonExpired='" + isAccountNonExpired()
        + "'" + ", accountNonLocked='" + isAccountNonLocked() + "'" + ", credentialsNonExpired='"
        + isCredentialsNonExpired() + "'" + "}";
  }
}
