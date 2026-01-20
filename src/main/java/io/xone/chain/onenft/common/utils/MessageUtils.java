package io.xone.chain.onenft.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MessageUtils {

    private static MessageSource messageSource;
    private final MessageSource injectedMessageSource;

    public MessageUtils(MessageSource messageSource) {
        this.injectedMessageSource = messageSource;
    }

    @PostConstruct
    public void init() {
        messageSource = injectedMessageSource;
    }

    /**
     * Get message
     * @param code Message code
     * @return Localized message
     */
    public static String get(String code) {
        try {
            return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return code;
        }
    }

    /**
     * Get message with args
     * @param code Message code
     * @param args Arguments
     * @return Localized message
     */
    public static String get(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return code;
        }
    }
}
