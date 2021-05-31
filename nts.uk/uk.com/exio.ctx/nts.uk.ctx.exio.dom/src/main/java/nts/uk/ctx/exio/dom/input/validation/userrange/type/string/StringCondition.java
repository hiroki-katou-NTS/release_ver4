package nts.uk.ctx.exio.dom.input.validation.userrange.type.string;

import java.util.Optional;

import lombok.AllArgsConstructor;
import nts.uk.ctx.exio.dom.input.DataItem;
import nts.uk.ctx.exio.dom.input.validation.CompareStringCondition;
import nts.uk.ctx.exio.dom.input.validation.Validation;

/**
 * 文字条件
 */
@AllArgsConstructor
public class StringCondition implements Validation{
	
	private CompareStringCondition condition;
	private Optional<ImportingStringCondition> value;
	
	@Override
	public boolean validate(DataItem target) {
		switch(this.condition) {
			case NOT_COND:
				return true;
			case COND1_EQUAL_VAL:
				return value.get().toString() == target.getString();
			case COND1_NOT_EQUAL_VAL:
				return value.get().toString() != target.getString();
			default:
				throw new RuntimeException("実装が存在しない文字の比較条件です。");
		}
	}
	
	/**
	 * 文字クラスへの変換 
	 */
	public static Validation create(String value1, int conditionNo) {
		Optional<ImportingStringCondition> result = value1 == null 
				? Optional.empty() 
				: Optional.of(new ImportingStringCondition(value1));
		
		return new StringCondition(
				CompareStringCondition.values()[conditionNo],
				result);
	}
}
