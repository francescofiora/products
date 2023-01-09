package it.francescofiora.product.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.francescofiora.product.api.service.dto.enumeration.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "PRODUCT", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(callSuper = true, includeFieldNames = true)
public class Product extends AbstractDomain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProduct")
  @SequenceGenerator(name = "seqProduct", sequenceName = "SEQ_PRODUCT", schema = "STORE",
      allocationSize = 1)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "description", nullable = false)
  private String description;

  @NotNull
  @DecimalMin(value = "0")
  @Column(name = "price", precision = 21, scale = 2, nullable = false)
  private BigDecimal price;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "size", nullable = false)
  private Size size;

  @Column(name = "image")
  private String image;

  @Column(name = "image_content_type")
  private String imageContentType;

  @ManyToOne
  @JsonIgnoreProperties("products")
  private Category category;

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
