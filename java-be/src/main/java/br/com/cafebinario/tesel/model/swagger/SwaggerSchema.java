package br.com.cafebinario.tesel.model.swagger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerSchema implements Serializable{

	private static final long serialVersionUID = 7591589494356013232L;

	private String swagger;
	private Info info;
	private String basePath;
	private List<Tag> tags;
	private List<String> schemes;
	private Map<String, Map<String, Request>> paths;
	private Map<String, Definition> definitions;

}
