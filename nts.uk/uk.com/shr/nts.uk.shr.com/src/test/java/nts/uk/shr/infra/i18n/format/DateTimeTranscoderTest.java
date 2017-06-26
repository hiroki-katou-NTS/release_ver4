package nts.uk.shr.infra.i18n.format;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

public class DateTimeTranscoderTest {
	
	@Test
	public void shortYmd() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.shortYmd("2017/8/9"), is("2017/8/9"));
	}
	
	@Test
	public void shortYmdw() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.shortYmdw("2017/5/29"), is("2017/5/29(月)"));
	}
	
	@Test
	public void shortYm() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.shortYm("2017/6/29"), is("2017/6"));
	}
	
	@Test
	public void shortMd() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.shortMd("2017/12/1"), is("12/1"));
	}
	
	@Test
	public void longYmd() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.longYmd("2017/3/29"), is("2017年3月29日"));
	}
	
	@Test
	public void longYmdw() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.longYmdw("2017/3/29"), is("2017年3月29日(水)"));
	}
	
	@Test
	public void longF() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.longF("2017/3/29"), is("2016年度"));
	}
	
	@Test
	public void longJmd() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.longJmd("2017/3/29"), is("平成29年3月29日"));
	}
	
	@Test
	public void longJm() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.longJm("2017/3/29"), is("平成29年3月"));
	}
	
	@Test
	public void timeShortHms() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.timeShortHms("2017/3/29 103:42:33"), is("103:42:33"));
	}
	
	@Test
	public void timeShortHm() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.timeShortHm("2017/3/29 103:42:33"), is("103:42"));
	}
	
	@Test
	public void fullDateTimeShort() {
		DateTimeTranscoder t = new DateTimeTranscoderJP();
		Assert.assertThat(t.fullDateTimeShort("2017-3-29 103:42:33"), is("2017/3/29 103:42:33"));
	}
}