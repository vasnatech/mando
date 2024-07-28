package com.vasnatech.mando.config;

import com.vasnatech.mando.expression.ExpressionResolver;
import com.vasnatech.mando.expression.SpelExpressionResolver;
import com.vasnatech.mando.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpressionResolverConfig {

    @Bean
    public ExpressionResolver expressionResolver(Session session) {
        return new SpelExpressionResolver(session.scope());
    }
}
