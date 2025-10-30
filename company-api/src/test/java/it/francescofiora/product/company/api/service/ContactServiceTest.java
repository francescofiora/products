package it.francescofiora.product.company.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.product.company.api.domain.Contact;
import it.francescofiora.product.company.api.repository.ContactRepository;
import it.francescofiora.product.company.api.service.impl.ContactServiceImpl;
import it.francescofiora.product.company.api.service.mapper.ContactMapper;
import it.francescofiora.product.company.api.util.TestUtils;
import it.francescofiora.product.company.api.web.errors.NotFoundAlertException;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ContactServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var contact = new Contact();
    var contactMapper = mock(ContactMapper.class);
    when(contactMapper.toEntity(any(NewContactDto.class))).thenReturn(contact);

    var contactRepository = mock(ContactRepository.class);
    when(contactRepository.save(any(Contact.class))).thenReturn(contact);

    var expected = TestUtils.createContactDto(ID);
    when(contactMapper.toDto(any(Contact.class))).thenReturn(expected);

    var contactDto = new NewContactDto();
    var contactService = new ContactServiceImpl(contactRepository, contactMapper);
    var actual = contactService.create(contactDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var contactDto = TestUtils.createUpdatebleContactDto(ID);
    var contactService =
        new ContactServiceImpl(mock(ContactRepository.class), mock(ContactMapper.class));
    assertThrows(NotFoundAlertException.class, () -> contactService.update(contactDto));
  }

  @Test
  void testUpdate() {
    var contact = TestUtils.createContact(ID);
    var contactRepository = mock(ContactRepository.class);
    when(contactRepository.findById(ID)).thenReturn(Optional.of(contact));

    var contactDto = TestUtils.createUpdatebleContactDto(ID);
    var contactMapper = mock(ContactMapper.class);
    var contactService = new ContactServiceImpl(contactRepository, contactMapper);
    contactService.update(contactDto);
    verify(contactMapper).updateEntityFromDto(contactDto, contact);
    verify(contactRepository).save(contact);
  }

  @Test
  void testFindAll() {
    var contact = new Contact();
    var contactRepository = mock(ContactRepository.class);
    when(contactRepository.findAll(ArgumentMatchers.any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(contact)));
    var expected = new ContactDto();
    var contactMapper = mock(ContactMapper.class);
    when(contactMapper.toDto(any(Contact.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var contactService = new ContactServiceImpl(contactRepository, contactMapper);
    var page = contactService.findAll(null, pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var contactService =
        new ContactServiceImpl(mock(ContactRepository.class), mock(ContactMapper.class));
    var contactOpt = contactService.findOne(ID);
    assertThat(contactOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var contact = TestUtils.createContact(ID);
    var contactRepository = mock(ContactRepository.class);
    when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
    var expected = new ContactDto();
    var contactMapper = mock(ContactMapper.class);
    when(contactMapper.toDto(any(Contact.class))).thenReturn(expected);

    var contactService = new ContactServiceImpl(contactRepository, contactMapper);
    var contactOpt = contactService.findOne(ID);
    assertThat(contactOpt).isPresent();
    var actual = contactOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var contactRepository = mock(ContactRepository.class);
    var contactService = new ContactServiceImpl(contactRepository, mock(ContactMapper.class));
    contactService.delete(ID);
    verify(contactRepository).deleteById(ID);
  }
}
