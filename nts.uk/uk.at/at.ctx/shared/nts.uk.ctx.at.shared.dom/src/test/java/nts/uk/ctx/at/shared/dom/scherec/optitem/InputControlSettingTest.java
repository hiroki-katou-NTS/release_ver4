package nts.uk.ctx.at.shared.dom.scherec.optitem;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.shr.com.i18n.TextResource;

/**
 * 
 * @author tutk
 *
 */
@RunWith(JMockit.class)
public class InputControlSettingTest {

	@Test
	public void getters() {
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.NOT_SET, Optional.empty(), Optional.empty(),Optional.empty());
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.ONE_MINUTE),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(true, calcResultRange, Optional.of(dailyResultInputUnit));
		NtsAssert.invokeGetters(inputControlSetting);

	}
	
	/**
	 * チェックボックスで入力する = false && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent ==true
	 * 任意項目の属性 = 時間
	 */
	@Test
	public void testCheckValueInputCorrect_1() {
		
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.NOT_SET, Optional.empty(), Optional.empty(),Optional.empty());
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(false, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.TIME;
		BigDecimal inputValue = new BigDecimal(600);
		
		
		ValueCheckResult dailyResultInputUnitOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<DailyResultInputUnit>() {
			@Mock
			public ValueCheckResult checkInputUnit(BigDecimal value, OptionalItemAtr optionalItemAtr) {
				return dailyResultInputUnitOutput;
			}
		};
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isTrue();
		assertThat(dataResult.getErrorContent()).isEmpty();
	}
	
	/**
	 * チェックボックスで入力する = true && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent == true
	 * 任意項目の属性 = 時間
	 */
	@Test
	public void testCheckValueInputCorrect_2() {
		
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.NOT_SET, Optional.empty(), Optional.empty(),Optional.empty());
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(true, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.TIME;
		BigDecimal inputValue = new BigDecimal(600);
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isTrue();
		assertThat(dataResult.getErrorContent()).isEmpty();
	}
	
	/**
	 * チェックボックスで入力する = false && INPUT「実績区分」 = 月別実績  && 「@日別実績の入力単位」.isPresent == true
	 * 任意項目の属性 = 時間
	 */
	@Test
	public void testCheckValueInputCorrect_3() {
		
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.NOT_SET, Optional.empty(), Optional.empty(),Optional.empty());
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(false, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.MONTHLY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.TIME;
		BigDecimal inputValue = new BigDecimal(600);
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isTrue();
		assertThat(dataResult.getErrorContent()).isEmpty();
	}
	
	/**
	 * チェックボックスで入力する = false && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent == false
	 * 任意項目の属性 = 時間
	 */
	@Test
	public void testCheckValueInputCorrect_4() {
		
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.NOT_SET, Optional.empty(), Optional.empty(),Optional.empty());
		Optional<DailyResultInputUnit> dailyResultInputUnit = Optional.empty();
		InputControlSetting inputControlSetting = new InputControlSetting(false, calcResultRange, dailyResultInputUnit);
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.TIME;
		BigDecimal inputValue = new BigDecimal(600);
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isTrue();
		assertThat(dataResult.getErrorContent()).isEmpty();
	}
	
	/**
	 * チェックボックスで入力する = true && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent == true
	 * 任意項目の属性 = 金額
	 * 入力範囲チェック error
	 */
	@Test
	public void testCheckValueInputCorrect_5() {
		
		DailyAmountRange dailyAmountRange = new DailyAmountRange(888,666);
		AmountRange amountRange = new AmountRange(Optional.of(dailyAmountRange), Optional.empty());
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.SET, Optional.empty(), Optional.empty(),Optional.of(amountRange));
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(true, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.AMOUNT;
		BigDecimal inputValue = new BigDecimal(900);
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(false, Optional.of("666.0以上の値で入力してください。")); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isFalse();
		assertThat(dataResult.getErrorContent().get(0)).isEqualTo("666.0以上の値で入力してください。");
	}
	
	
	/**
	 * チェックボックスで入力する = false && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent == true
	 * 任意項目の属性 = 金額
	 * 入力単位チェック error
	 */
	@Test
	public void testCheckValueInputCorrect_6() {
		
		DailyAmountRange dailyAmountRange = new DailyAmountRange(888,666);
		AmountRange amountRange = new AmountRange(Optional.of(dailyAmountRange), Optional.empty());
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.SET, Optional.empty(), Optional.empty(),Optional.of(amountRange));
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(false, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.AMOUNT;
		BigDecimal inputValue = new BigDecimal(506);
		
		ValueCheckResult dailyResultInputUnitOutput = new ValueCheckResult(false, Optional.of("506.0の単位で入力してください。")); 
		new MockUp<DailyResultInputUnit>() {
			@Mock
			public ValueCheckResult checkInputUnit(BigDecimal value, OptionalItemAtr optionalItemAtr) {
				return dailyResultInputUnitOutput;
			}
		};
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(true, Optional.empty()); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isFalse();
		assertThat(dataResult.getErrorContent().get(0)).isEqualTo("506.0の単位で入力してください。");
	}
	
	/**
	 * チェックボックスで入力する = false && INPUT「実績区分」 = 日別実績 && 「@日別実績の入力単位」.isPresent == true
	 * 任意項目の属性 = 金額
	 * 入力範囲チェック error
	 * 入力単位チェック error
	 */
	@Test
	public void testCheckValueInputCorrect_7() {
		
		DailyAmountRange dailyAmountRange = new DailyAmountRange(888,666);
		AmountRange amountRange = new AmountRange(Optional.of(dailyAmountRange), Optional.empty());
		CalcResultRange calcResultRange = new CalcResultRange(CalcRangeCheck.NOT_SET, CalcRangeCheck.SET, Optional.empty(), Optional.empty(),Optional.of(amountRange));
		DailyResultInputUnit dailyResultInputUnit = new DailyResultInputUnit(Optional.of(TimeItemInputUnit.TEN_MINUTES),
				Optional.empty(), Optional.empty());
		InputControlSetting inputControlSetting = new InputControlSetting(false, calcResultRange, Optional.of(dailyResultInputUnit));
		PerformanceAtr performanceAtr = PerformanceAtr.DAILY_PERFORMANCE;
		OptionalItemAtr optionalItemAtr = OptionalItemAtr.AMOUNT;
		BigDecimal inputValue = new BigDecimal(906);
		
		ValueCheckResult dailyResultInputUnitOutput = new ValueCheckResult(false, Optional.of("906.0の単位で入力してください。")); 
		new MockUp<DailyResultInputUnit>() {
			@Mock
			public ValueCheckResult checkInputUnit(BigDecimal value, OptionalItemAtr optionalItemAtr) {
				return dailyResultInputUnitOutput;
			}
		};
		
		ValueCheckResult calcResultRangeOutput = new ValueCheckResult(false, Optional.of("666.0以上の値で入力してください。")); 
		new MockUp<CalcResultRange>() {
			@Mock
			public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
					OptionalItemAtr optionalItemAtr) {
				return calcResultRangeOutput;
			}
		};
		
		CheckValueInputCorrectOuput dataResult = inputControlSetting.checkValueInputCorrect(inputValue, performanceAtr, optionalItemAtr);
		assertThat(dataResult.isCheckResult()).isFalse();
		assertThat(dataResult.getErrorContent().get(0)).isEqualTo("906.0の単位で入力してください。");
		assertThat(dataResult.getErrorContent().get(1)).isEqualTo("666.0以上の値で入力してください。");
	}
	
	

}
