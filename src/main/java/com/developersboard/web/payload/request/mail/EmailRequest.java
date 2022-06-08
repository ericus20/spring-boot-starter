package com.developersboard.web.payload.request.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;

/**
 * The pojo used to conveniently transport email details to the java mail sender.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
public class EmailRequest {
  private String subject;
  private String message;
  private String to;
  private String from;

  private List<String> recipients = new ArrayList<>();
  private Map<String, String> urls = new ConcurrentHashMap<>();
}
