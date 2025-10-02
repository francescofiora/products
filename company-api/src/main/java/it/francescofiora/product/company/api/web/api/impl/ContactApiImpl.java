package it.francescofiora.product.company.api.web.api.impl;

import it.francescofiora.product.company.api.service.ContactService;
import it.francescofiora.product.company.api.web.api.ContactApi;
import it.francescofiora.product.company.api.web.errors.BadRequestAlertException;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Contact.
 */
@RestController
public class ContactApiImpl extends AbstractApi implements ContactApi {

  private static final String ENTITY_NAME = "ContactDto";

  private final ContactService contactService;

  protected ContactApiImpl(ContactService contactService) {
    super(ENTITY_NAME);
    this.contactService = contactService;
  }

  @Override
  public ResponseEntity<Void> createContact(NewContactDto contactDto) {
    var result = contactService.create(contactDto);
    return postResponse("/api/v1/contacts/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<Void> updateContact(Long id, UpdatebleContactDto contactDto) {
    if (!id.equals(contactDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(contactDto.getId()),
          "Invalid id");
    }
    contactService.update(contactDto);
    return putResponse(id);
  }

  @Override
  public ResponseEntity<List<ContactDto>> findContacts(String name, Pageable pageable) {
    return getResponse(contactService.findAll(name, pageable));
  }

  @Override
  public ResponseEntity<ContactDto> getContactById(Long id) {
    return getResponse(contactService.findOne(id), id);
  }

  @Override
  public ResponseEntity<Void> deleteContactById(Long id) {
    contactService.delete(id);
    return deleteResponse(id);
  }
}
