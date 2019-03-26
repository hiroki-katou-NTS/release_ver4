package nts.uk.shr.infra.i18n.resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.regex.Matcher;

import org.junit.Test;

import lombok.val;
import nts.uk.shr.com.i18n.LanguageConsts;

public class I18NResourceContentProcessorTest {

	@Test
	public void noNestNoParam() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "TEXT1", Arrays.asList());
		assertThat(result, is("TEXT1"));
	}

	@Test
	public void nestedNoParam() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "TEXT2 has {#T1}!!", Arrays.asList());
		assertThat(result, is("TEXT2 has TEXT1!!"));
	}

	@Test
	public void nestedNoParam2() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "<{#T2}>", Arrays.asList());
		assertThat(result, is("<TEXT2 has TEXT1!!>"));
	}

	@Test
	public void param2() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "test {0} - {1}", Arrays.asList("ABC", "hello"));
		assertThat(result, is("test ABC - hello"));
	}
	
	@Test
	public void param3_resourced_param() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "test {2} - {0} - {1}", Arrays.asList("ABC", "<{#T1}>", "にほんご"));
		assertThat(result, is("test にほんご - ABC - <TEXT1>"));
	}
	
	@Test
	public void backslash() {
		val target = new I18NResourceContentProcessor(id -> localize(id));
		
		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "AB{0}C", Arrays.asList("\\"));
		assertThat(result, is("AB\\C"));
	}
	
	@Test
	public void dollar() {
		val target = new I18NResourceContentProcessor(id -> localize(id));

		String result = target.process(LanguageConsts.DEFAULT_LANGUAGE_ID, "AB{0}C", Arrays.asList("$"));
		assertThat(result, is("AB$C"));
	}
	
	private static String localize(String id) {
		switch (id) {
		case "T1": return "TEXT1";
		case "T2": return "TEXT2 has {#T1}!!";
		case "T3": return "TEXT3 has {0}";
		default: return "NO TEXT";
		}
	}
	
}
