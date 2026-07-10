package br.com.wakax.wakax_ecommerce.handler;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
  private final MessageSource messageSource;

  public MessageUtil(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public String getMessage(ErrorCode errorCode, Object... args) {
    return messageSource.getMessage(errorCode.getCode(), args, Locale.getDefault());
  }
}
