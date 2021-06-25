package nts.uk.pub.spr;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.Value;
import lombok.val;
import nts.arc.primitive.TimeClockPrimitiveValue;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * このクラスは大塚商会に提供するスタブのためのコードです。
 * 実際の処理を開発するときには削除してください。
 */
public class SprStubHelper {

	
	static String formatParam(String source) {
		return formatParam(source, s -> s);
	}
	
	static String formatParam(String source, Function<String, String> mapper) {
		return Optional.ofNullable(source)
				.map(mapper)
				.orElse("<i>NULL</i>");
	}
	
	static String formatParamTime(String source) {
		return formatParam(source, s -> {
			if (!NumberUtils.isDigits(source)) {
				return "<i style=\"color: red\">invalid value: " + source + "</i>";
			}
			
			int value = Integer.valueOf(source);
			val clock = new InputClock(value);
			return clock.hour() + ":" + StringUtil.padLeft(String.valueOf(clock.minute()), 2, '0');
		});
	}
	
	static class InputClock extends TimeClockPrimitiveValue<InputClock> {
		/** serialVersionUID */
		private static final long serialVersionUID = 1L;

		public InputClock(int minutesFromZeroOClock) {
			super(minutesFromZeroOClock);
		}
	}
	
	@Value
	static class ApplicationStatusQuery {
		String loginemployeeCode;
		String employeeCode;
		String startdate;
		String enddate;
		
		public DatePeriod getDatePeriod() {
			return new DatePeriod(
					GeneralDate.fromString(this.startdate, "yyyy/MM/dd"),
					GeneralDate.fromString(this.enddate, "yyyy/MM/dd"));
		}
	}
	
	@Value
	static class StatusContainer<T> {
		List<T> status;
	}
	
	@Value
	static class RequestApplicationStatusResult {
		String date;
		int status1;
		int status2;
		int status3;
		String applicationID1;
		String applicationID2;
		String applicationID3;
		
		public static List<RequestApplicationStatusResult> create() {
			
			return Arrays.asList(
					new RequestApplicationStatusResult("2018/04/01", 0, 0,0, null, null,null),
					new RequestApplicationStatusResult("2018/04/02", 1, 0, 0,"56f9bf80-2dea-46b8-adf9-58a03ab87902", null, null),
					new RequestApplicationStatusResult("2018/04/03", 0, 4, 0,null, "b401f47e-bbfc-454b-ac15-86acbe0509ac",null),
					new RequestApplicationStatusResult("2018/04/04", 2, 3, 0,"12c4abc6-3cbd-4e07-ace9-f1d5dfbf8b23", "6cb57689-941a-4fe8-b1fe-7ea8cbde7473",null));
		}
	}
	
	@Value
	static class RecordApplicationStatusResult {
		String date;
		int status1;
		int status2;

		public static List<RecordApplicationStatusResult> create() {
			return Arrays.asList(
					new RecordApplicationStatusResult("2018/04/01", 0, 0),
					new RecordApplicationStatusResult("2018/04/02", 1, 0),
					new RecordApplicationStatusResult("2018/04/03", 1, 1));
		}
	}
	
	@Value
	static class ApplicationTargetQuery {
		String loginemployeeCode;
		String date;
	}

	@Value
	static class EmployeesContainer<T> {
		List<T> employees;
	}
	
	@Value
	static class ApplicationTargetResult {
		String employeeCode;
		int status1;
		int status2;

		public static List<ApplicationTargetResult> create() {
			return Arrays.asList(
					new ApplicationTargetResult("000010", 0, 0),
					new ApplicationTargetResult("221001", 1, 0),
					new ApplicationTargetResult("123456", 0, 1),
					new ApplicationTargetResult("999999", 1, 1));
		}
	}
}

