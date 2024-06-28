package it.francescofiora.product.itt.context;

import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Category Context.
 */
@Component
@Getter
@Setter
public class CompanyContext {

  private NewCompanyDto newCompanyDto;
  private UpdatebleCompanyDto updatebleCompanyDto;
  private CompanyDto companyDto;
  private NewAddressDto newAddressDto;
  private UpdatebleAddressDto updatebleAddressDto;
  private AddressDto addressDto;
  private Long companyId;
  private Long addressId;
  private List<CompanyDto> companies;
  private List<AddressDto> addresses;
}
