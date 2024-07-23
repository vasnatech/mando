package com.vasnatech.mando.expression;

public class MirrorExpressionResolver implements ExpressionResolver {

    @Override
    public Object resolve(String expression) {
        return expression;
    }
}
