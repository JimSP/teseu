package br.com.cafebinario.teseu.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.api.TeseuExpectedProcessor;

@Component
public class TeseuSpelExpectedProcessor implements TeseuExpectedProcessor{
	
	@Autowired
	private TeseuContext teseuContext;
	
	public Boolean parseExpression(final String expressionString, final Map<String, String> tesseuRequestContext) {
		
		final String condition = expressionString.substring(0, expressionString.indexOf("="));
		
		teseuContext.setContext(tesseuRequestContext);
		
		final ExpressionParser expressionParser = new SpelExpressionParser();
		
		final ParserContext parserContext = ParserContext.TEMPLATE_EXPRESSION;
		
		final Expression expression = expressionParser.parseExpression("#{context.get(\"" + expressionString.substring(expressionString.indexOf("$") + 1) + "\")}", parserContext);

		return condition.equals(String.valueOf(expression.getValue(teseuContext)));
	}
}
