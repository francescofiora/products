package it.francescofiora.product.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * OrderItem Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "ORDER_ITEM", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(callSuper = true, includeFieldNames = true)
public class OrderItem extends AbstractDomain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrderItem")
  @SequenceGenerator(name = "seqOrderItem", sequenceName = "SEQ_ORDER_ITEM", schema = "STORE",
      allocationSize = 1)
  private Long id;

  @Column(name = "quantity", nullable = false)
  @NotNull
  @Positive
  private Integer quantity;

  @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal totalPrice;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("orderItems")
  @NotNull
  private Product product;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("orderItems")
  @NotNull
  private Order order;

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
