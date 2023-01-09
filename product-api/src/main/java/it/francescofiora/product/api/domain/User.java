package it.francescofiora.product.api.domain;

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
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "USER", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(callSuper = true, includeFieldNames = true)
public class User extends AbstractDomain implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
  @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_USER", schema = "STORE",
      allocationSize = 1)
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
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
