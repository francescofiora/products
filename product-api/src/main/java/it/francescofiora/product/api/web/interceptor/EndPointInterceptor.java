package it.francescofiora.product.api.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * EndPoint Interceptor.
 */
@Slf4j
@Profile("Logging")
@Component
public class EndPointInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    log.debug("{} {}", request.getMethod(), request.getRequestURI());
    return true;
  }
}
