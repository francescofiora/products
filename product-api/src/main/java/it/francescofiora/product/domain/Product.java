package it.francescofiora.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.francescofiora.product.service.dto.enumeration.Size;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "PRODUCT", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProduct")
  @SequenceGenerator(name = "seqProduct", sequenceName = "SEQ_PRODUCT", schema = "STORE")
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Product name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public Product description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Product price(BigDecimal price) {
    this.price = price;
    return this;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Size getSize() {
    return size;
  }

  public Product size(Size size) {
    this.size = size;
    return this;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  public String getImage() {
    return image;
  }

  public Product image(String image) {
    this.image = image;
    return this;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getImageContentType() {
    return imageContentType;
  }

  public Product imageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
    return this;
  }

  public void setImageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
  }

  public Category getCategory() {
    return category;
  }

  public Product category(Category category) {
    this.category = category;
    return this;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Product)) {
      return false;
    }
    return id != null && id.equals(((Product) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Product{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
        + getDescription() + "'" + ", price=" + getPrice() + ", size='" + getSize() + "'"
        + ", image='" + getImage() + "'" + ", imageContentType='" + getImageContentType() + "'"
        + "}";
  }
}
