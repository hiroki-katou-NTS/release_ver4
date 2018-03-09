package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.SingleValueCompareType;

public class CompareSingleTest {

	@Test
	public void checkWithFixedValue_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.EQUAL);

		assertTrue(range.checkWithFixedValue(10, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.EQUAL);

		assertTrue(!range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_EQUAL_False2() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.EQUAL);

		assertTrue(!range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_OR_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(range.checkWithFixedValue(10, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_OR_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_OR_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(!range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_THAN_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_THAN);

		assertTrue(range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_THAN_False1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_THAN);

		assertTrue(!range.checkWithFixedValue(10, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_GREATER_THAN_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.GREATER_THAN);

		assertTrue(!range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_OR_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_OR_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(range.checkWithFixedValue(10, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_OR_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(!range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_THAN_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_THAN);

		assertTrue(range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_THAN_False1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_THAN);

		assertTrue(!range.checkWithFixedValue(10, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_LESS_THAN_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.LESS_THAN);

		assertTrue(!range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_NOT_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.NOT_EQUAL);

		assertTrue(range.checkWithFixedValue(9, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_NOT_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.NOT_EQUAL);

		assertTrue(range.checkWithFixedValue(11, c -> c));
	}
	
	@Test
	public void checkWithFixedValue_NOT_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.NOT_EQUAL);

		assertTrue(!range.checkWithFixedValue(10, c -> c));
	}


	@Test
	public void checkWithAttendanceItem_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.EQUAL);

		assertTrue(range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.EQUAL);

		assertTrue(!range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_EQUAL_False2() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.EQUAL);

		assertTrue(!range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_OR_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_OR_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_OR_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_OR_EQUAL);

		assertTrue(!range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_THAN_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_THAN);

		assertTrue(range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_THAN_False1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_THAN);

		assertTrue(!range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_GREATER_THAN_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.GREATER_THAN);

		assertTrue(!range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_OR_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_OR_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_OR_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_OR_EQUAL);

		assertTrue(!range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_THAN_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_THAN);

		assertTrue(range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_THAN_False1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_THAN);

		assertTrue(!range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_LESS_THAN_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.LESS_THAN);

		assertTrue(!range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_NOT_EQUAL_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.NOT_EQUAL);

		assertTrue(range.checkWithAttendanceItem(9, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_NOT_EQUAL_True1() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.NOT_EQUAL);

		assertTrue(range.checkWithAttendanceItem(11, c -> c, c -> c));
	}
	
	@Test
	public void checkWithAttendanceItem_NOT_EQUAL_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.NOT_EQUAL);

		assertTrue(!range.checkWithAttendanceItem(10, c -> c, c -> c));
	}
	
	@Test
	public void check_FIX_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.EQUAL);

		assertTrue(range.check(10, c -> c, c -> c));
	}
	
	@Test
	public void check_FIX_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.FIXED_VALUE, SingleValueCompareType.EQUAL);

		assertTrue(!range.check(11, c -> c, c -> c));
	}
	
	@Test
	public void check_ATTENDANCE_ITEM_True() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.EQUAL);

		assertTrue(range.check(10, c -> c, c -> c));
	}
	
	@Test
	public void check_ATTENDANCE_ITEM_False() {
		CompareSingleValue<Integer> range = createValue(ConditionType.ATTENDANCE_ITEM, SingleValueCompareType.EQUAL);

		assertTrue(!range.check(11, c -> c, c -> c));
	}
	
	private CompareSingleValue<Integer> createValue(ConditionType type, SingleValueCompareType compareOpertor) {
		CompareSingleValue<Integer> value = new CompareSingleValue<>(compareOpertor.value, type.value);
		value.setValue(10);
		return value;
	}
}
