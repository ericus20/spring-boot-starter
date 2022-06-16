package com.developersboard.shared.util.core;

import com.developersboard.constant.ContactConstants;
import com.developersboard.constant.EmailConstants;
import com.developersboard.constant.ErrorConstants;
import com.developersboard.constant.HomeConstants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * This utility class holds all common methods used in the web layer.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class WebUtils {

  public static final String TOKEN = "token";

  private WebUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Generates a uri dynamically by constructing url.
   *
   * @param path the custom path
   * @param publicUserId the publicUserId
   * @return a dynamically formulated uri
   */
  public static String getGenericUri(String path, String publicUserId) {

    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path)
        .queryParam(TOKEN, publicUserId)
        .build()
        .toUriString();
  }

  /**
   * Generates a uri dynamically by constructing url.
   *
   * @param path the custom path
   * @return a dynamically formulated uri
   */
  public static String getGenericUri(String path) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
  }

  /**
   * Generates a URI dynamically by constructing url from the request.
   *
   * <p>For instance a request from origin <a href="http://localhost:30000">...</a> will be
   * retrieved this way.
   *
   * @param request the request
   * @param path the custom path
   * @param publicUserId the publicUserId
   * @return a dynamically formulated uri
   */
  public static String getUri(HttpServletRequest request, String path, String publicUserId) {
    return ServletUriComponentsBuilder.fromOriginHeader(request.getHeader(HttpHeaders.ORIGIN))
        .path(path)
        .queryParam(TOKEN, publicUserId)
        .build()
        .toUriString();
  }

  /**
   * Generates a URI dynamically by constructing url from the request.
   *
   * <p>For instance a request from origin <a href="http://localhost:30000">...</a> will be
   * retrieved this way.
   *
   * @param request the request
   * @return a dynamically formulated uri
   */
  public static String getUri(HttpServletRequest request) {
    return ServletUriComponentsBuilder.fromOriginHeader(request.getHeader(HttpHeaders.ORIGIN))
        .toUriString();
  }

  /**
   * Get general links used in email definitions.
   *
   * @return default links
   */
  public static Map<String, String> getDefaultEmailUrls() {
    Map<String, String> links = new ConcurrentHashMap<>();
    links.put(EmailConstants.HOME_LINK, getGenericUri(HomeConstants.INDEX_URL_MAPPING));
    links.put(EmailConstants.CONTACT_US_LINK, getGenericUri(ContactConstants.CONTACT_URL_MAPPING));

    return links;
  }
}
