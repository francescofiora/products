package it.francescofiora.product.company.api.domain;

import it.francescofiora.product.common.domain.AbstractDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Company Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "COMPANY", schema = "COMPANY")
@ToString(callSuper = true, includeFieldNames = true)
public class Company extends AbstractDomain implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCompany")
  @SequenceGenerator(name = "seqCompany", sequenceName = "SEQ_COMPANY",
      schema = "COMPANY", allocationSize = 1)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @NotNull
  @Column(name = "web", nullable = false)
  private String web;

  @OneToMany(mappedBy = "company")
  private Set<Address> addresses = new HashSet<>();

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
