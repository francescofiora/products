package it.francescofiora.product.company.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.francescofiora.product.common.domain.AbstractDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contact Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "CONTACT")
@ToString(callSuper = true)
public class Contact extends AbstractDomain implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqContact")
  @SequenceGenerator(name = "seqContact", sequenceName = "SEQ_CONTACT", allocationSize = 1)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "description", nullable = false)
  private String description;

  @NotNull
  @Column(name = "phone", nullable = false)
  private String phone;

  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("contacts")
  @NotNull
  private Address address;

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
