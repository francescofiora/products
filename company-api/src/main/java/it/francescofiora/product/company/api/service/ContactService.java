package it.francescofiora.product.company.api.service;

import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contact Service.
 */
public interface ContactService {

  String ENTITY_NAME = "ContactDto";

  /**
   * Create a new contact.
   *
   * @param contactDto the entity to create
   * @return the persisted entity
   */
  ContactDto create(NewContactDto contactDto);

  /**
   * Update a contact.
   *
   * @param contactDto the entity to update
   */
  void update(UpdatebleContactDto contactDto);

  /**
   * Get all the contacts.
   *
   * @param name the name
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ContactDto> findAll(String name, Pageable pageable);

  /**
   * Get a contact.
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<ContactDto> findOne(Long id);

  /**
   * Delete a contact.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
