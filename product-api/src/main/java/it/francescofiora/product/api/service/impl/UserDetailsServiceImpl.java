package it.francescofiora.product.api.service.impl;

import it.francescofiora.product.api.repository.UserRepository;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetails Service Impl.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String ENTITY_NAME = "User";

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var option = userRepository.findByUsername(username);
    if (!option.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME, username, "'username' not found");
    }
    return option.get();
  }
}
