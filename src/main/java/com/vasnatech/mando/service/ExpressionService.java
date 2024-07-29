package com.vasnatech.mando.service;

import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import com.vasnatech.mando.expression.ExpressionResolver;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.Stream;

@Service
public class ExpressionService {

    final ExpressionResolver expressionResolver;
    final Tokenizer<TokenType> tokenizer;

    public ExpressionService(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
        this.tokenizer = new Tokenizer<>(
                Stream.of(TokenType.values())
                        .map(tokenType -> new Token<>(tokenType.match, tokenType))
                        .toList()
        );
    }

    public Object resolve(String expression) {
        return expressionResolver.resolve(normalize(expression));
    }

    private String normalize(String expression) {
        StringBuilder builder = new StringBuilder(expression.length());
        Iterator<Token<TokenType>> iterator = tokenizer.tokenize(expression);
        while (iterator.hasNext()) {
            Token<TokenType> token = iterator.next();
            TokenType tokenType = token.getValue();
            builder.append(tokenType == null ? token.getMatch() : tokenType.replace);
        }
        return builder.toString();
    }

    enum TokenType {
        BACK_SLASH("<b>", "\\"),
        DASH("<d>", "-"),
        NEW_LINE("<n>", "\n"),
        QUOTE("<q>", "'"),
        CARRIAGE_RETURN("<r>", "\r"),
        SPACE("<s>", " "),
        TAB("<t>", "\t");

        TokenType(String match, String replace) {
            this.match = match;
            this.replace = replace;
        }

        final String match;
        final String replace;
    }
}
