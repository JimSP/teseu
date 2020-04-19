package br.com.cafebinario.teseu.infrastruct;

public interface TeseuConstants {

	public static final String EMPTY = "";
	
	public static final String FILENAME_KEY = "filename";
	public static final String OUTPUT_FILE_EXTENSION = ".response";
	public static final String INPUT_FILE_EXTENSION = ".request";
	public static final String DIR_NAME = "caa-regressive-tests";
	public static final String ORDERS_FILE_NAME = "execution-orders.teseu";
	public static final String TESSEU_CONTEXT_FILE_NAME = "context.teseu";
	
	public static final String REGEX_URI_SEPARATOR = "[,]";
	public static final String END_LIST_IDENTIFIER = "]";
	public static final String BEGIN_LIST_IDENTIFIER = "[";
	public static final String SEPARATOR = "=";
	public static final String VAR_SEPARATOR = "[:]";
	public static final String DECLARATION_SEPARATOR = "[|]";
	public static final String ITEM_SEPARATOR = ",";
	public static final String ENTRY_FORMAT = "%s=[%s]";
	public static final String ITEM_DECLARATION = "$";
	public static final String PATH_SEPARATOR = ".";
	public static final String REGEX_FILE_SEPARATOR = "[.]";
	public static final String REGEX_SPACE = "[ ]";
	
	public static final String HTTP_PROTOCOL = "http://";
	public static final String HOST = "host";
	public static final String URI = "uri";
	public static final String HEADERS = "headers";
	public static final String METHOD = "method";
	public static final String URI_VARIABLES = "uriVariables";
	public static final String BODY = "body";
	
	public static final String HTTP_STATUS = "httpStatus";
	public static final String RESPONSE_HEADERS = "responseHeaders";
	public static final String RESPONSE_BODY = "responseBody";

}
