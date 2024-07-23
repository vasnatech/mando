package com.vasnatech.mando.expression;

import com.vasnatech.commons.type.VariableContainer;
import com.vasnatech.mando.expression.function.Functions;
import com.vasnatech.mando.model.Session;
import org.springframework.asm.MethodVisitor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.*;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class SpelExpressionResolver implements ExpressionResolver {

    final Session session;
    final ExpressionParser expressionParser;
    final StandardEvaluationContext evaluationContext;

    public SpelExpressionResolver(Session session) {
        this.session = session;
        this.expressionParser = new SpelExpressionParser();
        this.evaluationContext = new StandardEvaluationContext(session);
        this.evaluationContext.addPropertyAccessor(new VariableContainerAccessor());
        this.evaluationContext.addPropertyAccessor(new MapAccessor());
        Functions.methods().forEach(evaluationContext::registerFunction);
    }

    @Override
    public Object resolve(String expressionAsText) {
        Expression expression = expressionParser.parseExpression(expressionAsText);
        return expression.getValue(evaluationContext);
    }


    static class VariableContainerAccessor extends MapAccessor {

        @Override
        public Class<?>[] getSpecificTargetClasses() {
            return new Class<?>[] {VariableContainer.class};
        }

        @Override
        public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
            return target instanceof VariableContainer variableContainer && variableContainer.containsKey(name);
        }

        @Override
        public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
            Assert.state(target instanceof VariableContainer, "Target must be of type Scope");
            VariableContainer variableContainer = (VariableContainer) target;
            Object value = variableContainer.get(name);
            if (value == null && !variableContainer.containsKey(name)) {
                throw new AccessException("Scope does not contain a value for key '" + name + "'");
            }
            return new TypedValue(value);
        }

        @Override
        public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
            return true;
        }

        @Override
        public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
            Assert.state(target instanceof VariableContainer, "Target must be a Scope");
            VariableContainer variableContainer = (VariableContainer) target;
            variableContainer.put(name, newValue);
        }

        @Override
        public boolean isCompilable() {
            return true;
        }

        @Override
        public Class<?> getPropertyType() {
            return Object.class;
        }

        @Override
        public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
            String descriptor = cf.lastDescriptor();
            if (descriptor == null || !descriptor.equals("Lcom/vasnatech/commons/type/Scope")) {
                if (descriptor == null) {
                    cf.loadTarget(mv);
                }
                CodeFlow.insertCheckCast(mv, "Lcom/vasnatech/commons/type/Scope");
            }
            mv.visitLdcInsn(propertyName);
            mv.visitMethodInsn(INVOKESPECIAL, "com/vasnatech/commons/type/Scope", "get","(Ljava/lang/String;)Ljava/lang/Object;",true);
        }
    }
}
