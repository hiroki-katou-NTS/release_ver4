package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.LogicalOperator;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.RangeCompareType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.SingleValueCompareType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;

public class ErAlConditionsAttendanceItemTest {

	@Test
	public void test() {
		ErAlConditionsAttendanceItem condition = createCondition(LogicalOperator.OR);
		assertTrue(condition.check(c -> c));
	}
	
	@Test
	public void test2() {
		ErAlConditionsAttendanceItem condition = createCondition(LogicalOperator.AND);
		assertTrue(!condition.check(c -> c));
	}

	private ErAlConditionsAttendanceItem createCondition(LogicalOperator logic){
		ErAlConditionsAttendanceItem condition = ErAlConditionsAttendanceItem.init(logic.value);
		condition.addAtdItemConditions(createCondition(ConditionAtr.AMOUNT_VALUE, true, true, v -> new CheckedAmountValue(v)));
		if(logic == LogicalOperator.OR){
			condition.addAtdItemConditions(createCondition(ConditionAtr.TIME_DURATION, true, true, v -> new CheckedTimeDuration(v)));
		} else {
			condition.addAtdItemConditions(createCondition2(ConditionAtr.TIME_DURATION, true, true, v -> new CheckedTimeDuration(v)));
		}
		
		return condition;
	}
	

	
	private <V> ErAlAttendanceItemCondition<V> createCondition(ConditionAtr type, boolean isRange, boolean calcAble,
			Function<Integer, V> toValue) {
		ErAlAttendanceItemCondition<V> condition = new ErAlAttendanceItemCondition<>("1", "1", 1, type.value,
				true);
		if (isRange) {
			condition.setCompareRange(RangeCompareType.BETWEEN_RANGE_CLOSED.value, toValue.apply(1), toValue.apply(10));
		} else {
			condition.setCompareSingleValue(SingleValueCompareType.EQUAL.value, ConditionType.FIXED_VALUE.value, toValue.apply(5));
		}
		if (calcAble) {
			condition.setCountableTarget(Arrays.asList(1, 2, 3, 4), Arrays.asList(5));
		} else {
			condition.setUncountableTarget(5);
		}
		return condition;
	}
	
	private <V> ErAlAttendanceItemCondition<V> createCondition2(ConditionAtr type, boolean isRange, boolean calcAble,
			Function<Integer, V> toValue) {
		ErAlAttendanceItemCondition<V> condition = new ErAlAttendanceItemCondition<>("1", "1", 1, type.value,
				true);
		if (isRange) {
			condition.setCompareRange(RangeCompareType.BETWEEN_RANGE_CLOSED.value, toValue.apply(1), toValue.apply(10));
		} else {
			condition.setCompareSingleValue(SingleValueCompareType.EQUAL.value, ConditionType.FIXED_VALUE.value, toValue.apply(5));
		}
		if (calcAble) {
			condition.setCountableTarget(Arrays.asList(1, 2, 3, 4), Arrays.asList(5, 5));
		} else {
			condition.setUncountableTarget(0);
		}
		return condition;
	}
}
