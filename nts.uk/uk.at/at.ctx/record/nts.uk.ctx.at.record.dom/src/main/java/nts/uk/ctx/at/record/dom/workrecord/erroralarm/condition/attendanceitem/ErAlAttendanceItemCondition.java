/**
 * 4:47:15 PM Nov 8, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckConditionValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * @author hungnm
 * @param <V>
 * @param <V>
 *
 */
// 勤怠項目のエラーアラーム条件
public class ErAlAttendanceItemCondition<V> extends AggregateRoot {

	// 会社ID
	@Getter
	private String companyId;

	// エラーアラームコード
	@Getter
	private ErrorAlarmWorkRecordCode errorAlarmCode;

	// NO
	@Getter
	private int targetNO;

	// 条件式の属性
	@Getter
	private ConditionAtr conditionAtr;

	// 使用する
	@Getter
	private Boolean useAtr;

	// チェック対象（可算）
	@Getter
	private CountableTarget countableTarget;

	// チェック対象（不可算）
	@Getter
	private UncountableTarget uncountableTarget;

	// 単一値との比較
	@Getter
	private CompareSingleValue<V> compareSingleValue;

	// 範囲との比較
	@Getter
	private CompareRange<V> compareRange;

	public ErAlAttendanceItemCondition(String companyId, String errorAlarmCode, int targetNO, int conditionAtr,
			Boolean useAtr) {
		super();
		this.companyId = companyId;
		this.errorAlarmCode = new ErrorAlarmWorkRecordCode(errorAlarmCode);
		this.targetNO = targetNO;
		this.conditionAtr = EnumAdaptor.valueOf(conditionAtr, ConditionAtr.class);
		this.useAtr = useAtr;
	}

	/**
	 * Set CountableTarget
	 * 
	 * @param additionAttendanceItems
	 * @param substractionAttendanceItems
	 * @return itself
	 */
	public ErAlAttendanceItemCondition<V> setCountableTarget(List<Integer> additionAttendanceItems,
			List<Integer> substractionAttendanceItems) {
		this.countableTarget = new CountableTarget(
				new AddSubAttendanceItems(additionAttendanceItems, substractionAttendanceItems));
		return this;
	}

	/**
	 * Set UnCountableTarget
	 * 
	 * @param attendanceItem
	 * @return itself
	 */
	public ErAlAttendanceItemCondition<V> setUncountableTarget(int attendanceItem) {
		this.uncountableTarget = new UncountableTarget(attendanceItem);
		return this;
	}

	/**
	 * 
	 * @param compareOpertor
	 * @param conditionType
	 * @param conditionValue
	 * @return
	 */
	public ErAlAttendanceItemCondition<V> setCompareSingleValue(int compareOpertor, int conditionType, V value) {
		this.compareSingleValue = new CompareSingleValue<V>(compareOpertor, conditionType);
		this.compareSingleValue.setValue(value);
		return this;
	}

	/**
	 * 
	 * @param compareOperator
	 * @param conditionValue
	 * @return
	 */
	public ErAlAttendanceItemCondition<V> setCompareRange(int compareOperator, V startValue, V endValue) {
		this.compareRange = new CompareRange<V>(compareOperator);
		this.compareRange.setStartValue(startValue);
		this.compareRange.setEndValue(endValue);
		return this;
	}

	public boolean checkTarget(Function<List<Integer>, List<Integer>> getItemValue) {
		V targetValue = calculateTargetValue(getItemValue);
		
		if (this.compareRange != null) {
			return this.compareRange.checkRange(targetValue);
		} else {
			if (this.compareSingleValue.getConditionType() == ConditionType.FIXED_VALUE) {
				return this.compareSingleValue.check(targetValue);
			} else {
				return this.compareSingleValue.checkWithAttendanceItem(targetValue, getItemValue);
			}
		}
	}

	private V calculateTargetValue(Function<List<Integer>, List<Integer>> getItemValue) {
		Integer target = 0;
		if (this.uncountableTarget != null) {
			target = getItemValue.apply(Arrays.asList(this.uncountableTarget.getAttendanceItem())).get(0);
		} else {
			target = this.countableTarget.getAddSubAttendanceItems().calculate(getItemValue);
		}

		return toCheckValue(target);
	}

	@SuppressWarnings("unchecked")
	private V toCheckValue(Integer target) {
		switch (this.conditionAtr) {
		case AMOUNT_VALUE:
			return (V) new CheckedAmountValue(target);
		case TIME_DURATION:
			return (V) new CheckedTimeDuration(target);
		case TIME_WITH_DAY:
			return (V) new TimeWithDayAttr(target);
		case TIMES:
			return (V) new CheckedTimesValue(target);
		default:
			throw new RuntimeException("invalid conditionAtr: " + conditionAtr);
		}
	}
}
