package it.francescofiora.product.company.api.service.impl;

import it.francescofiora.product.company.api.domain.Contact;
import it.francescofiora.product.company.api.repository.ContactRepository;
import it.francescofiora.product.company.api.service.ContactService;
import it.francescofiora.product.company.api.service.mapper.ContactMapper;
import it.francescofiora.product.company.api.web.errors.NotFoundAlertException;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contact}.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private final ContactRepository contactRepository;
  private final ContactMapper contactMapper;

  @Override
  public ContactDto create(NewContactDto contactDto) {
    log.debug("Request to create a new Contact : {}", contactDto);
    var contact = contactMapper.toEntity(contactDto);
    contact = contactRepository.save(contact);
    return contactMapper.toDto(contact);
  }

  @Override
  public void update(UpdatebleContactDto contactDto) {
    log.debug("Request to save Contact : {}", contactDto);
    var contactOpt = contactRepository.findById(contactDto.getId());
    if (!contactOpt.isPresent()) {
      var id = String.valueOf(contactDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var contact = contactOpt.get();
    contactMapper.updateEntityFromDto(contactDto, contact);
    contactRepository.save(contact);
  }

  @Override
  public Page<ContactDto> findAll(String name, Pageable pageable) {
    log.debug("Request to get all Contacts");
    var contact = new Contact();
    contact.setName(name);
    var exampleMatcher = ExampleMatcher.matchingAll().withMatcher("name", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(contact, exampleMatcher);
    return contactRepository.findAll(example, pageable).map(contactMapper::toDto);
  }

  @Override
  public Optional<ContactDto> findOne(Long id) {
    log.debug("Request to get Contact : {}", id);
    return contactRepository.findById(id).map(contactMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    contactRepository.deleteById(id);
  }
}
