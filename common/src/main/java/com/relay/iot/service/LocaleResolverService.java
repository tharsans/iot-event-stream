package com.relay.iot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class LocaleResolverService extends AcceptHeaderLocaleResolver {

    List<Locale> locales = Arrays.asList(new Locale("en"), new Locale("nl"),
            new Locale("fr"));
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
        Locale locale = Locale.lookup(list, locales);
        return locale;
    }
}
