package nts.uk.shr.com.time.calendar.period;

import static nts.arc.time.GeneralDate.ymd;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.time.GeneralDate;

public class DatePeriodTest {

	@Test
	public void createFromList0() {
		List<GeneralDate> dates = Collections.emptyList();
		
		val actual = DatePeriod.create(dates);
		
		assertThat(actual.size(), is(0));
	}

	@Test
	public void createFromList1() {
		val dates = Arrays.asList(
				ymd(2000, 1, 5));
		
		val actual = DatePeriod.create(dates);
		
		assertThat(actual.size(), is(1));
		assertThat(actual.get(0).start(), is(ymd(2000, 1, 5)));
		assertThat(actual.get(0).end(), is(ymd(2000, 1, 5)));
	}
	
	@Test
	public void createFromList2() {
		val dates = Arrays.asList(
				ymd(2000, 1, 5),
				ymd(2000, 1, 1),
				ymd(2000, 1, 3),
				ymd(2000, 1, 10),
				ymd(2000, 1, 4),
				ymd(2000, 1, 11));
		
		val actual = DatePeriod.create(dates);
		
		assertThat(actual.size(), is(3));
		assertThat(actual.get(0).start(), is(ymd(2000, 1, 1)));
		assertThat(actual.get(0).end(), is(ymd(2000, 1, 1)));
		assertThat(actual.get(1).start(), is(ymd(2000, 1, 3)));
		assertThat(actual.get(1).end(), is(ymd(2000, 1, 5)));
		assertThat(actual.get(2).start(), is(ymd(2000, 1, 10)));
		assertThat(actual.get(2).end(), is(ymd(2000, 1, 11)));
	}

	@Test
	public void forEachByMonths1() {
		
		val target = new DatePeriod(ymd(2000, 1, 1), ymd(2000, 7, 31));
		List<DatePeriod> actuals = new ArrayList<>();
		
		target.forEachByMonths(3, p -> {
			actuals.add(p);
		});
		
		val expecteds = Arrays.asList(
				new DatePeriod(ymd(2000, 1, 1), ymd(2000, 3, 31)),
				new DatePeriod(ymd(2000, 4, 1), ymd(2000, 6, 30)),
				new DatePeriod(ymd(2000, 7, 1), ymd(2000, 7, 31)));
		
		assertThat(actuals.size(), is(expecteds.size()));
		
		for (int i = 0; i < actuals.size(); i++) {
			assertThat(actuals.get(i), is(expecteds.get(i)));
		}
	}

	@Test
	public void forEachByMonths2() {
		
		val target = new DatePeriod(ymd(2000, 1, 16), ymd(2000, 7, 31));
		List<DatePeriod> actuals = new ArrayList<>();
		
		target.forEachByMonths(3, p -> {
			actuals.add(p);
		});
		
		val expecteds = Arrays.asList(
				new DatePeriod(ymd(2000, 1, 16), ymd(2000, 4, 15)),
				new DatePeriod(ymd(2000, 4, 16), ymd(2000, 7, 15)),
				new DatePeriod(ymd(2000, 7, 16), ymd(2000, 7, 31)));
		
		assertThat(actuals.size(), is(expecteds.size()));
		
		for (int i = 0; i < actuals.size(); i++) {
			assertThat(actuals.get(i), is(expecteds.get(i)));
		}
	}

}
