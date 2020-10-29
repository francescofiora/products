package it.francescofiora.product.domain;

import it.francescofiora.product.domain.enumeration.OrderStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ORDER", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrder")
  @SequenceGenerator(name = "seqOrder", sequenceName = "SEQ_ORDER", schema = "STORE")
  private Long id;

  @NotNull
  @Column(name = "placed_date", nullable = false)
  private Instant placedDate;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status;

  @NotNull
  @Column(name = "code", nullable = false)
  private String code;

  @NotNull
  @Column(name = "customer", nullable = false)
  private String customer;

  @OneToMany(mappedBy = "order")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<OrderItem> orderItems = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getPlacedDate() {
    return placedDate;
  }

  public Order placedDate(Instant placedDate) {
    this.placedDate = placedDate;
    return this;
  }

  public void setPlacedDate(Instant placedDate) {
    this.placedDate = placedDate;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Order status(OrderStatus status) {
    this.status = status;
    return this;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public Order code(String code) {
    this.code = code;
    return this;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCustomer() {
    return customer;
  }

  public Order customer(String customer) {
    this.customer = customer;
    return this;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public Set<OrderItem> getOrderItems() {
    return orderItems;
  }

  public Order orderItems(Set<OrderItem> orderItems) {
    this.orderItems = orderItems;
    return this;
  }

  /**
   * add Item.
   * @param orderItem OrderItem
   * @return Order
   */
  public Order addOrderItem(OrderItem orderItem) {
    this.orderItems.add(orderItem);
    orderItem.setOrder(this);
    return this;
  }

  /**
   * remove Item.
   * @param orderItem OrderItem
   * @return Order
   */
  public Order removeOrderItem(OrderItem orderItem) {
    this.orderItems.remove(orderItem);
    orderItem.setOrder(null);
    return this;
  }

  public void setOrderItems(Set<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Order)) {
      return false;
    }
    return id != null && id.equals(((Order) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Order{" + "id=" + getId() + ", placedDate='" + getPlacedDate() + "'"
        + ", status='" + getStatus() + "'" + ", code='" + getCode() + "'"
        + ", customer='" + getCustomer() + "'" + "}";
  }
}
