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
		
	public ExpectedResult parseExpression(final String expressionString, final Map<String, String> teseuRequestContext) {

		final String condition = extractCondition(expressionString);
		
		final String actual = getProcessValue(expressionString, teseuRequestContext);
		
		final Boolean sucess = condition.equals(actual);
		
		return ExpectedResult
					.builder()
					.expected(condition)
					.actual(actual)
					.sucess(sucess)
					.build();
	}

	private String getProcessValue(final String expressionString, final Map<String, String> teseuRequestContext) {
		
		teseuContext.setContext(teseuRequestContext);
		
		final ExpressionParser expressionParser = new SpelExpressionParser();
		
		final ParserContext parserContext = ParserContext.TEMPLATE_EXPRESSION;
		
		parse(expressionString, expressionParser, parserContext);
		
		final Expression expression = parse(expressionString, expressionParser, parserContext);
		
		return String.valueOf(expression.getValue(teseuContext));
	}

	private String extractCondition(final String expressionString) {
		return expressionString.substring(0, expressionString.indexOf("="));
	}

	private Expression parse(final String expressionString, final ExpressionParser expressionParser,
			final ParserContext parserContext) {
		return expressionParser.parseExpression("#{context.get(\"" + expressionString.substring(expressionString.indexOf("$") + 1) + "\")}", parserContext);
	}
}
