package it.francescofiora.product.company.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.francescofiora.product.common.domain.AbstractDomain;
import it.francescofiora.product.company.dto.enumeration.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
 * Address Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "ADDRESS")
@ToString(callSuper = true)
public class Address extends AbstractDomain implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAddress")
  @SequenceGenerator(name = "seqAddress", sequenceName = "SEQ_ADDRESS", allocationSize = 1)
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private AddressType type;

  @NotNull
  @Column(name = "address", nullable = false)
  private String address;

  @NotNull
  @Column(name = "zipcode", nullable = false)
  private String zipcode;

  @NotNull
  @Column(name = "country", nullable = false)
  private String country;

  @NotNull
  @Column(name = "currency", nullable = false)
  private String currency;

  @NotNull
  @Column(name = "tax_number", nullable = false)
  private String taxNumber;

  @NotNull
  @Column(name = "phone", nullable = false)
  private String phone;

  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("addresses")
  @NotNull
  private Company company;

  @OneToMany(mappedBy = "address")
  private Set<Contact> contacts = new HashSet<>();

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
