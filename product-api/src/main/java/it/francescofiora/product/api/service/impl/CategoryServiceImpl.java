package it.francescofiora.product.api.service.impl;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.repository.CategoryRepository;
import it.francescofiora.product.api.service.CategoryService;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.mapper.CategoryMapper;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
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
 * Service Implementation for managing Category.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  public CategoryDto create(NewCategoryDto categoryDto) {
    log.debug("Request to create a new Category : {}", categoryDto);
    var category = categoryMapper.toEntity(categoryDto);
    category = categoryRepository.save(category);
    return categoryMapper.toDto(category);
  }

  @Override
  public void update(CategoryDto categoryDto) {
    log.debug("Request to save Category : {}", categoryDto);
    var categoryOpt = categoryRepository.findById(categoryDto.getId());
    if (categoryOpt.isEmpty()) {
      var id = String.valueOf(categoryDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var category = categoryOpt.get();
    categoryMapper.updateEntityFromDto(categoryDto, category);
    categoryRepository.save(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryDto> findAll(String name, String description, Pageable pageable) {
    log.debug("Request to get all ProductCategories");
    var category = new Category();
    category.setName(name);
    category.setDescription(description);
    var exampleMatcher = ExampleMatcher.matchingAll().withMatcher("name", PROPERTY_MATCHER_DEFAULT)
        .withMatcher("description", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(category, exampleMatcher);
    return categoryRepository.findAll(example, pageable).map(categoryMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CategoryDto> findOne(Long id) {
    log.debug("Request to get Category : {}", id);
    return categoryRepository.findById(id).map(categoryMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Category : {}", id);
    categoryRepository.deleteById(id);
  }
}
