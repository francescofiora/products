package it.francescofiora.product.service.impl;

import it.francescofiora.product.repository.UserRepository;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String ENTITY_NAME = "User";

  private final UserRepository userRepository;

  /**
   * Constructor.
   *
   * @param userRepository UserRepository
   */
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var option = userRepository.findByUsername(username);
    if (!option.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME, username, "'username' not found");
    }
    return option.get();
  }

}
