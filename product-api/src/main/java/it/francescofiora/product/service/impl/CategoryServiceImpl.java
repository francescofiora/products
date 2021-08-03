package it.francescofiora.product.service.impl;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.repository.CategoryRepository;
import it.francescofiora.product.service.CategoryService;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.mapper.CategoryMapper;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

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
    if (!categoryOpt.isPresent()) {
      var id = String.valueOf(categoryDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var category = categoryOpt.get();
    categoryMapper.updateEntityFromDto(categoryDto, category);
    categoryRepository.save(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryDto> findAll(Pageable pageable) {
    log.debug("Request to get all ProductCategories");
    return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
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
