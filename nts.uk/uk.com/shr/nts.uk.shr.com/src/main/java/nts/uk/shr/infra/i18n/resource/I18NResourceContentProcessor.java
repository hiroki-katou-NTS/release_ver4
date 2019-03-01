package nts.uk.shr.infra.i18n.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.uk.shr.infra.i18n.format.DateTimeFormatProvider;

/**
 * Process nested resource (ex. "{#Com_Employemnt}コード") and parameters (ex. "{0}は既に存在します")
 * @author m_kitahira
 *
 */
@RequiredArgsConstructor
public class I18NResourceContentProcessor {
	
	private static final Pattern NESTED_RESOURCE_PATTERN = Pattern.compile("\\{#(\\w*)\\}");
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{([0-9])+(:\\w+)?\\}");

	private static final String ID_MARK = "#";

	private final Function<String, String> localizer;
	
	public String process(String languageId, String content, List<String> params) {
		
		String nestProcessed = this.processNestedResource(new ProcessingContext(content));
		
		return this.processParameters(
				languageId,
				nestProcessed,
				params.stream().map(p -> this.processParam(p)).collect(Collectors.toList()));
	}
	
	private String processParam(String param) {
		return this.processNestedResource(new ProcessingContext(param));
	}
	
	private String processNestedResource(ProcessingContext context) {
		Matcher matcher = NESTED_RESOURCE_PATTERN.matcher(context.currentContent());
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String nestedResourceId = matcher.group(1);
			String nestedResourceContent = this.localizer.apply(nestedResourceId);
			String processedContent = this.processNestedResource(context.nextProcessing(nestedResourceContent));
			
			matcher.appendReplacement(sb, processedContent);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	private String processParameters(String languageId, String content, List<String> params) {
		if (params == null || params.size() == 0)
			return content;

		Matcher matcher = PARAMETER_PATTERN.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String paramIndexMark = matcher.group(1);
			String format = matcher.group(2);

			int paramIndex = Integer.valueOf(paramIndexMark);
			if (paramIndex >= params.size()) {
				// このケースはバグではあるが、例外スローで処理を停止させるほど深刻ではない
				continue;
			}
			
			String param;
			if (format != null) {
				val transcoder = DateTimeFormatProvider.LocaleTranscoderMap.get(languageId);
				param = transcoder.create().get(format.substring(1)).apply(params.get(paramIndex));
			} else {
				param = this.getArgument(params.get(paramIndex));
			}
			
			matcher.appendReplacement(sb, Matcher.quoteReplacement(param));
		}
		
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	private String getArgument(String param) {
		if (param.indexOf(ID_MARK) != 0) {
			return param;
		}
		
		if (param.length() == 1) {
			return param;
		}
		
		return localizer.apply(param.substring(1));
	}
	
	private static class ProcessingContext {
		private final List<String> processedResourceContents;
		
		public ProcessingContext(String startingResourceContent) {
			this.processedResourceContents = Arrays.asList(startingResourceContent);
		}
		
		private ProcessingContext(List<String> processed) {
			this.processedResourceContents = new ArrayList<>(processed);
		}
		
		public String currentContent() {
			return this.processedResourceContents.get(this.processedResourceContents.size() - 1);
		}
		
		public ProcessingContext nextProcessing(String processingResourceContent) {
			if (this.processedResourceContents.contains(processingResourceContent)) {
				throw new RuntimeException(
						"circulated resource was detected: "
						+ String.join(" -> ", this.processedResourceContents)
						+ " -> "
						+ processingResourceContent);
			}
			
			val newContext = new ProcessingContext(this.processedResourceContents);
			newContext.processedResourceContents.add(processingResourceContent);
			return newContext;
		}
	}
}
