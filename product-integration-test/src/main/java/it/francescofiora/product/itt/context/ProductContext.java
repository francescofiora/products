package it.francescofiora.product.itt.context;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Product Context.
 */
@Component
@Getter
@Setter
public class ProductContext {

  private NewProductDto newProductDto;
  private UpdatebleProductDto updatebleProductDto;
  private ProductDto productDto;
  private Long productId;
  private List<ProductDto> products;

}
