package it.francescofiora.product.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "CATEGORY", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCategory")
  @SequenceGenerator(name = "seqCategory", sequenceName = "SEQ_CATEGORY",
      schema = "STORE")
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "category")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Product> products = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Category name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public Category description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Product> getProducts() {
    return products;
  }

  public Category products(Set<Product> products) {
    this.products = products;
    return this;
  }

  /**
   * add Product.
   * @param product Product
   * @return Category
   */
  public Category addProduct(Product product) {
    this.products.add(product);
    product.setCategory(this);
    return this;
  }

  /**
   * remove Product.
   * @param product Product
   * @return Category
   */
  public Category removeProduct(Product product) {
    this.products.remove(product);
    product.setCategory(null);
    return this;
  }

  public void setProducts(Set<Product> products) {
    this.products = products;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Category)) {
      return false;
    }
    return id != null && id.equals(((Category) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
        + getDescription() + "'" + "}";
  }
}
