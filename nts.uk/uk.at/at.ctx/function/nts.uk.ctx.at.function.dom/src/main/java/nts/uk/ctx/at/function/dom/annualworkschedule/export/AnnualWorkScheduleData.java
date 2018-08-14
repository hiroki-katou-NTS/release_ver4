package nts.uk.ctx.at.function.dom.annualworkschedule.export;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeOfManagePeriodImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.AttendanceItemValueImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceResultImport;
import nts.uk.ctx.at.function.dom.annualworkschedule.CalcFormulaItem;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBook;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.ValueOuputFormat;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;

@Setter
@Getter
public class AnnualWorkScheduleData {
	/**
	 * 値の出力形式
	 */
	private ValueOuputFormat valOutFormat;
	private String headingName;
	private YearMonth startYm;
	private int numMonth;

	private ItemData month1st;
	private ItemData month2nd;
	private ItemData month3rd;
	private ItemData month4th;
	private ItemData month5th;
	private ItemData month6th;
	private ItemData month7th;
	private ItemData month8th;
	private ItemData month9th;
	private ItemData month10th;
	private ItemData month11th;
	private ItemData month12th;

	private BigDecimal average;
	private BigDecimal sum;
	private Integer monthsExceeded;
	private Integer monthsRemaining;

	private ItemData period1st;
	private ItemData period2nd;
	private ItemData period3rd;
	private ItemData period4th;
	private ItemData period5th;
	private ItemData period6th;
	private ItemData period7th;

	private Integer maxDigitAfterDecimalPoint = null;

	private boolean agreementTime;

	public void setMonthlyData(ItemData item, YearMonth ym) {
		int monthIndex = (int) this.startYm.until(ym, ChronoUnit.MONTHS);
		switch (monthIndex) {
		case 0:
			this.month1st = item;
			break;
		case 1:
			this.month2nd = item;
			break;
		case 2:
			this.month3rd = item;
			break;
		case 3:
			this.month4th = item;
			break;
		case 4:
			this.month5th = item;
			break;
		case 5:
			this.month6th = item;
			break;
		case 6:
			this.month7th = item;
			break;
		case 7:
			this.month8th = item;
			break;
		case 8:
			this.month9th = item;
			break;
		case 9:
			this.month10th = item;
			break;
		case 10:
			this.month11th = item;
			break;
		case 11:
			this.month12th = item;
			break;
		}
	}

	public void setPeriodMonthData(ItemData item, int index) {
		switch (index) {
		case 1:
			this.period1st = item;
			break;
		case 2:
			this.period2nd = item;
			break;
		case 3:
			this.period3rd = item;
			break;
		case 4:
			this.period4th = item;
			break;
		case 5:
			this.period5th = item;
			break;
		case 6:
			this.period6th = item;
			break;
		case 7:
			this.period7th = item;
			break;
		}
	}

	/**
	 * 月平均の算出(Calculate monthly average)
	 */
	public AnnualWorkScheduleData calc() {
		// 取得した実績の合計を算出する
		this.sum = new BigDecimal(0);
		this.sum = this.sum.add(this.getItemValueByNullOrZero(this.month1st))
				.add(this.getItemValueByNullOrZero(this.month2nd)).add(this.getItemValueByNullOrZero(this.month3rd))
				.add(this.getItemValueByNullOrZero(this.month4th)).add(this.getItemValueByNullOrZero(this.month5th))
				.add(this.getItemValueByNullOrZero(this.month6th)).add(this.getItemValueByNullOrZero(this.month7th))
				.add(this.getItemValueByNullOrZero(this.month8th)).add(this.getItemValueByNullOrZero(this.month9th))
				.add(this.getItemValueByNullOrZero(this.month10th)).add(this.getItemValueByNullOrZero(this.month11th))
				.add(this.getItemValueByNullOrZero(this.month12th));
		// 月平均を算出する
		this.average = this.sum.divide(BigDecimal.valueOf(this.numMonth), this.getMaxDigitAfterDecimalPoint(),
				RoundingMode.HALF_UP);
		return this;
	}

	private int getMaxDigitAfterDecimalPoint() {
		if (maxDigitAfterDecimalPoint == null) {
			maxDigitAfterDecimalPoint = Collections
					.max(Arrays.asList(this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month1st)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month2nd)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month3rd)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month4th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month5th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month6th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month7th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month8th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month9th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month10th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month11th)),
							this.getDigitAfterDecimalPoint(this.getItemValueByNullOrDefault(this.month12th))));
		}
		return maxDigitAfterDecimalPoint;
	}

	private int getDigitAfterDecimalPoint(BigDecimal value) {
		if (value == null)
			return 0;
		String valueStr = value.toString();
		int indexPoint = valueStr.indexOf('.');
		if (indexPoint < 0)
			return 0;
		return valueStr.length() - (indexPoint + 1);
	}

	private BigDecimal getItemValueByNullOrZero(ItemData item) {
		return item == null ? BigDecimal.valueOf(0) : item.getValue();
	}

	private BigDecimal getItemValueByNullOrDefault(ItemData item) {
		return item == null ? null : item.getValue();
	}

	private String formatItemData(ItemData item) {
		if (item == null)
			return "";
		return formatBigDecimal(item.getValue());
	}

	private String formatBigDecimal(BigDecimal value) {
		if (value == null)
			return "";
		if (valOutFormat.equals(ValueOuputFormat.TIME)) {
			return String.format("%d:%02d", value.longValue() / 60, Math.abs(value.longValue() % 60));
		}
		if (valOutFormat.equals(ValueOuputFormat.TIMES)) {
			return String.valueOf(value.longValue());
		}
		return String.valueOf(value.floatValue());
	}

	public String formatMonth1st() {
		return this.formatItemData(this.month1st);
	}

	public String formatMonth2nd() {
		return this.formatItemData(this.month2nd);
	}

	public String formatMonth3rd() {
		return this.formatItemData(this.month3rd);
	}

	public String formatMonth4th() {
		return this.formatItemData(this.month4th);
	}

	public String formatMonth5th() {
		return this.formatItemData(this.month5th);
	}

	public String formatMonth6th() {
		return this.formatItemData(this.month6th);
	}

	public String formatMonth7th() {
		return this.formatItemData(this.month7th);
	}

	public String formatMonth8th() {
		return this.formatItemData(this.month8th);
	}

	public String formatMonth9th() {
		return this.formatItemData(this.month9th);
	}

	public String formatMonth10th() {
		return this.formatItemData(this.month10th);
	}

	public String formatMonth11th() {
		return this.formatItemData(this.month11th);
	}

	public String formatMonth12th() {
		return this.formatItemData(this.month12th);
	}

	public String formatSum() {
		return this.formatBigDecimal(this.sum);
	}

	public String formatAverage() {
		return this.formatBigDecimal(this.average);
	}

	public String formatMonthsExceeded() {
		return this.formatMonths(this.monthsExceeded);
	}

	public String formatMonthsRemaining() {
		return this.formatMonths(this.monthsRemaining);
	}

	private String formatMonths(Integer value) {
		if (value == null) {
			return "";
		}
		return "'" + value + "回";
	}

	public String formatMonthPeriod1st() {
		return this.formatItemData(this.period1st);
	}

	public String formatMonthPeriod2nd() {
		return this.formatItemData(this.period2nd);
	}

	public String formatMonthPeriod3rd() {
		return this.formatItemData(this.period3rd);
	}

	public String formatMonthPeriod4th() {
		return this.formatItemData(this.period4th);
	}

	public String formatMonthPeriod5th() {
		return this.formatItemData(this.period5th);
	}

	public String formatMonthPeriod6th() {
		return this.formatItemData(this.period6th);
	}

	public String formatMonthPeriod7th() {
		return this.formatItemData(this.period7th);
	}

	private Integer getColor(ItemData item) {
		if (item == null || item.getStatus() == null)
			return null;
		switch (item.getStatus()) {
		case EXCESS_LIMIT_ERROR:
		case EXCESS_EXCEPTION_LIMIT_ERROR:
		case EXCESS_LIMIT_ERROR_SP:
			// #FD4D4D = 16600397
			return 16600397;
		case EXCESS_LIMIT_ALARM:
		case EXCESS_EXCEPTION_LIMIT_ALARM:
		case EXCESS_LIMIT_ALARM_SP:
			// #F6F636 = 16184886
			return 16184886;
		case NORMAL_SPECIAL:
			// #EB9152 = 15438162
			return 15438162;
		default:
			return null;
		}
	}

	public Integer getColorMonth1st() {
		return this.getColor(this.month1st);
	}

	public Integer getColorMonth2nd() {
		return this.getColor(this.month2nd);
	}

	public Integer getColorMonth3rd() {
		return this.getColor(this.month3rd);
	}

	public Integer getColorMonth4th() {
		return this.getColor(this.month4th);
	}

	public Integer getColorMonth5th() {
		return this.getColor(this.month5th);
	}

	public Integer getColorMonth6th() {
		return this.getColor(this.month6th);
	}

	public Integer getColorMonth7th() {
		return this.getColor(this.month7th);
	}

	public Integer getColorMonth8th() {
		return this.getColor(this.month8th);
	}

	public Integer getColorMonth9th() {
		return this.getColor(this.month9th);
	}

	public Integer getColorMonth10th() {
		return this.getColor(this.month10th);
	}

	public Integer getColorMonth11th() {
		return this.getColor(this.month11th);
	}

	public Integer getColorMonth12th() {
		return this.getColor(this.month12th);
	}

	public Integer getColorPeriodMonth1st() {
		return this.getColor(this.period1st);
	}

	public Integer getColorPeriodMonth2nd() {
		return this.getColor(this.period2nd);
	}

	public Integer getColorPeriodMonth3rd() {
		return this.getColor(this.period3rd);
	}

	public Integer getColorPeriodMonth4th() {
		return this.getColor(this.period4th);
	}

	public Integer getColorPeriodMonth5th() {
		return this.getColor(this.period5th);
	}

	public Integer getColorPeriodMonth6th() {
		return this.getColor(this.period6th);
	}

	public Integer getColorPeriodMonth7th() {
		return this.getColor(this.period7th);
	}

	public static AnnualWorkScheduleData fromMonthlyAttendanceList(ItemOutTblBook itemOut,
			List<MonthlyAttendanceResultImport> monthlyAttendanceResult, YearMonth startYm, int numMonth) {
		final Map<Integer, Integer> operationMap = itemOut.getListOperationSetting().stream()
				.collect(Collectors.toMap(CalcFormulaItem::getAttendanceItemId, CalcFormulaItem::getOperation));

		AnnualWorkScheduleData annualWorkScheduleData = new AnnualWorkScheduleData();
		annualWorkScheduleData.setHeadingName(itemOut.getHeadingName().v());
		annualWorkScheduleData.setValOutFormat(itemOut.getValOutFormat());
		annualWorkScheduleData.setStartYm(startYm);
		annualWorkScheduleData.setNumMonth(numMonth);
		annualWorkScheduleData.setAgreementTime(false);
		monthlyAttendanceResult.forEach(m -> {
			annualWorkScheduleData.setMonthlyData(
					AnnualWorkScheduleData.sumAttendanceData(operationMap, m.getAttendanceItems()),
					YearMonth.of(m.getYearMonth().year(), m.getYearMonth().month()));
		});
		return annualWorkScheduleData;
	}

	private static ItemData sumAttendanceData(Map<Integer, Integer> operationMap,
			List<AttendanceItemValueImport> attendanceItemsValue) {
		BigDecimal sum = BigDecimal.valueOf(0);
		for (AttendanceItemValueImport attendanceItem : attendanceItemsValue) {
			// 0: integer, 2 decimal
			if (attendanceItem.isNumber()) {
				BigDecimal val;
				try {
					val = new BigDecimal(attendanceItem.getValue());
				} catch (Exception e) {
					continue;
				}
				// 0: subtract, 1: plus
				if (operationMap.get(attendanceItem.getItemId()) == 0) {
					sum = sum.subtract(val);
				} else {
					sum = sum.add(val);
				}
			}
		}
		return new ItemData(sum, null);
	}

	public static AnnualWorkScheduleData fromAgreementTimeList(ItemOutTblBook itemOut,
			List<AgreementTimeOfManagePeriodImport> listAgreementTime,
			List<AgreementTimeByPeriodImport> listExcesMonths, YearMonth startYm, int numMonth, Integer monthsExceeded,
			Integer monthLimit) {
		AnnualWorkScheduleData annualWorkScheduleData = new AnnualWorkScheduleData();
		annualWorkScheduleData.setHeadingName(itemOut.getHeadingName().v());
		annualWorkScheduleData.setValOutFormat(itemOut.getValOutFormat());
		annualWorkScheduleData.setStartYm(startYm);
		annualWorkScheduleData.setNumMonth(numMonth);
		annualWorkScheduleData.setMonthsExceeded(monthsExceeded);
		annualWorkScheduleData.setMonthsRemaining(monthLimit - monthsExceeded);
		annualWorkScheduleData.setAgreementTime(true);
		listAgreementTime.forEach(m -> {
			BigDecimal value = new BigDecimal(m.getAgreementTime().getAgreementTime().v());
			AgreementTimeStatusOfMonthly status = m.getAgreementTime().getStatus();
			ItemData item = new ItemData(value, status);
			annualWorkScheduleData.setMonthlyData(item,
					YearMonth.of(m.getYearMonth().year(), m.getYearMonth().month()));
		});

		listExcesMonths = listExcesMonths.stream().sorted((excesMonth1, excesMonth2) -> Integer
				.compare(excesMonth1.getStartMonth().v(), excesMonth2.getStartMonth().v()))
				.collect(Collectors.toList());
		int periodIndex = 1;
		for (AgreementTimeByPeriodImport m : listExcesMonths) {
			BigDecimal value = new BigDecimal(m.getAgreementTime().v());
			AgreementTimeStatusOfMonthly status = m.getStatus();
			ItemData item = new ItemData(value, status);
			annualWorkScheduleData.setPeriodMonthData(item, periodIndex);
			periodIndex = periodIndex + 1;
		}
		return annualWorkScheduleData;
	}

	public boolean hasItemData() {
		if (this.month1st != null || this.month2nd != null || this.month3rd != null || this.month4th != null
				|| this.month5th != null || this.month6th != null || this.month7th != null || this.month8th != null
				|| this.month9th != null || this.month10th != null || this.month11th != null
				|| this.month12th != null) {
			return true;
		}
		return false;
	}
}