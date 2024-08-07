package it.francescofiora.product.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ORDER_ITEM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(callSuper = true)
public class OrderItem extends AbstractDomain implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrderItem")
  @SequenceGenerator(name = "seqOrderItem", sequenceName = "SEQ_ORDER_ITEM", allocationSize = 1)
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
