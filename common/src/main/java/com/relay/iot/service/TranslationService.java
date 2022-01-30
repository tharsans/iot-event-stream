package com.relay.iot.service;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TranslationService {
    private ReloadableResourceBundleMessageSource messageSource;

    public String translate(String code)
    {
        return translate(code, Locale.getDefault());
    }

    public String translate(String code, Locale locale)
    {
        return messageSource.getMessage(code, null, locale);
    }

    @Bean
    public MessageSource messageSource()
    {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
