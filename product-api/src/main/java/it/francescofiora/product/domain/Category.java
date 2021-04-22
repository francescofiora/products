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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CATEGORY", schema = "STORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
public class Category extends AbstractDomain implements Serializable {

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

  @Override
  public String toString() {
    return "Category{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
        + getDescription() + "'" + "}";
  }
}
