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
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ErrorAlarmConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.InputCheckCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValueDay;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * @author hungnm
 * @param <V>
 * @param <V>
 *
 */
// 勤怠項目のエラーアラーム条件
@Getter
public class ErAlAttendanceItemCondition<V> extends AggregateRoot {

	// 会社ID
	private String companyId;

	// エラーアラームコード
	private ErrorAlarmWorkRecordCode errorAlarmCode;

	// NO
	private int targetNO;

	// 条件式の属性
	private ConditionAtr conditionAtr;

	// 使用する
	private boolean useAtr;

	// 種別
	private ErrorAlarmConditionType type;
	
	// チェック対象（可算）
	private CountableTarget countableTarget;

	// チェック対象（不可算）
	private UncountableTarget uncountableTarget;

	// 単一値との比較
	private CompareSingleValue<V> compareSingleValue;

	// 範囲との比較
	private CompareRange<V> compareRange;
	
	// 入力チェック
	private InputCheck inputCheck;

	public ErAlAttendanceItemCondition(String companyId, String errorAlarmCode, int targetNO, int conditionAtr,
			boolean useAtr, int type) {
		super();
		this.companyId = companyId;
		this.errorAlarmCode = new ErrorAlarmWorkRecordCode(errorAlarmCode);
		this.targetNO = targetNO;
		this.conditionAtr = EnumAdaptor.valueOf(conditionAtr, ConditionAtr.class);
		this.useAtr = useAtr;
		this.type = ErrorAlarmConditionType.of(type);	
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
	
	public ErAlAttendanceItemCondition<V> setInputCheck(int inputCheck) {
		this.inputCheck = new InputCheck(InputCheckCondition.of(inputCheck));
		return this;
	}

	public boolean checkTarget(Function<List<Integer>, List<Double>> getItemValue) {
		if (!isUse()) {
			return false;
		}
		Double targetValue = calculateTargetValue(getItemValue);

		if(this.inputCheck != null){
			if(this.inputCheck.getInputCheckCondition() == InputCheckCondition.INPUT_DONE){
				return targetValue != null;
			}
			return targetValue == null;
		} else if (this.compareRange != null) {
			return this.compareRange.checkRange(targetValue, c -> getVValue(c));
		} else {
			return this.compareSingleValue.check(targetValue, getItemValue, c -> getVValue(c));
		}
	}

	public Double sumCheckTarget(Function<List<Integer>, List<Double>> getItemValue) {
		if(checkTarget(getItemValue)) {
			return calculateTargetValue(getItemValue);
		}
		return null;
	}
	
	public boolean isUse() {
		return this.useAtr;
	}

	private Double calculateTargetValue(Function<List<Integer>, List<Double>> getItemValue) {
		if (this.uncountableTarget != null) {
			List<Integer> items = Arrays.asList(this.uncountableTarget.getAttendanceItem());
			if(items.isEmpty()){
				throw new RuntimeException("チェック対象（不可算）の項目が不正です。");
			}
			List<Double> values = getItemValue.apply(items);
			if(values.isEmpty()){
				throw new RuntimeException("チェック対象（不可算）の項目の値が不正です。");
			}
			return values.get(0);
		} else {
			return this.countableTarget.getAddSubAttendanceItems().calculate(getItemValue);
		}

		// return toCheckValue(target);
	}

	private Double getVValue(V target) {
		if (this.compareRange == null && this.compareSingleValue.isAttendanceItem()) {
			return toDoubleValue(((AttendanceItemId) target).v());
		}
		switch (this.conditionAtr) {
		case AMOUNT_VALUE:
			return toDoubleValue(((CheckedAmountValue) target).v());
		case TIME_DURATION:
			return toDoubleValue(((CheckedTimeDuration) target).valueAsMinutes());
		case TIME_WITH_DAY:
			return toDoubleValue(((TimeWithDayAttr) target).valueAsMinutes());
		case TIMES:
			return toDoubleValue(((CheckedTimesValue) target).v());
		case DAYS:
			return ((CheckedTimesValueDay) target).v();
		default:
			throw new RuntimeException("invalid conditionAtr: " + conditionAtr);
		}
	}

	private Double toDoubleValue(Integer target) {
		return Double.valueOf(target);
	}

	public void setTargetNO(int targetNO) {
		this.targetNO = targetNO;
	}

	public void setUseAtr(boolean useAtr) {
		this.useAtr = useAtr;
	}
	
	public void updateCode(ErrorAlarmWorkRecordCode errorAlarmCode){
		this.errorAlarmCode = errorAlarmCode;
	}

	// @SuppressWarnings("unchecked")
	// private V toCheckValue(Integer target) {
	// switch (this.conditionAtr) {
	// case AMOUNT_VALUE:
	// return (V) new CheckedAmountValue(target);
	// case TIME_DURATION:
	// return (V) new CheckedTimeDuration(target);
	// case TIME_WITH_DAY:
	// return (V) new TimeWithDayAttr(target);
	// case TIMES:
	// return (V) new CheckedTimesValue(target);
	// default:
	// throw new RuntimeException("invalid conditionAtr: " + conditionAtr);
	// }
	// }
	
	
	
}
