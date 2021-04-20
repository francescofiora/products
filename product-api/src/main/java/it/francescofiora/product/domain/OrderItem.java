package it.francescofiora.product.domain;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ORDER_ITEM", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrderItem")
  @SequenceGenerator(name = "seqOrderItem", sequenceName = "SEQ_ORDER_ITEM", schema = "STORE")
  private Long id;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
  private BigDecimal totalPrice;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("orderItems")
  private Product product;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("orderItems")
  private Order order;

  @NotNull
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  @Positive
  public Integer getQuantity() {
    return quantity;
  }

  public OrderItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @NotNull
  @DecimalMin(value = "0")
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public OrderItem totalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  @NotNull
  public Product getProduct() {
    return product;
  }

  public OrderItem product(Product product) {
    this.product = product;
    return this;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @NotNull
  public Order getOrder() {
    return order;
  }

  public OrderItem order(Order order) {
    this.order = order;
    return this;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItem)) {
      return false;
    }
    return id != null && id.equals(((OrderItem) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "OrderItem{" + "id=" + getId() + ", quantity=" + getQuantity() + ", totalPrice="
        + getTotalPrice() + "}";
  }
}
