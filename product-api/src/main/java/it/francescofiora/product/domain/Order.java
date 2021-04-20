package it.francescofiora.product.domain;

import it.francescofiora.product.service.dto.enumeration.OrderStatus;
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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ORDER", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
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

  public Order placedDate(Instant placedDate) {
    this.placedDate = placedDate;
    return this;
  }

  public Order status(OrderStatus status) {
    this.status = status;
    return this;
  }

  public Order code(String code) {
    this.code = code;
    return this;
  }

  public Order customer(String customer) {
    this.customer = customer;
    return this;
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
