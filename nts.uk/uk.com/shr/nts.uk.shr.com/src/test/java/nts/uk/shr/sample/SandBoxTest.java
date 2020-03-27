package nts.uk.shr.sample;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Test;

import lombok.val;

public class SandBoxTest {

	@Test
	public void test() {
		//val date = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.ofHours(9));
		val date = OffsetDateTime.now(ZoneOffset.UTC);
		date.toString();
	}
	
	@Test
	public void test2() {
		
		val date = OffsetDateTime.of(2020, 4, 25, 20, 45, 9, 0, ZoneOffset.UTC);
		
		val formatter = DateTimeFormatter.ofPattern(
				"EEE, d MMM uuuu kk:mm:ss",
				Locale.ENGLISH);
		String formated = date.format(formatter) + " GMT";
		
		
		assertThat(formated, is("Sat, 25 Apr 2020 20:45:09 GMT"));
	}

}
