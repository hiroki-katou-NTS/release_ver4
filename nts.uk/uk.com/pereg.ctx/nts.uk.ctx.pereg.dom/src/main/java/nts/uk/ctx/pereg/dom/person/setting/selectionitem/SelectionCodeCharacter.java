package nts.uk.ctx.pereg.dom.person.setting.selectionitem;

import lombok.AllArgsConstructor;
import nts.arc.primitive.constraint.IntegerRange;

@AllArgsConstructor
@IntegerRange(max = 1, min = 0)
/**
 * 
 * @author tuannv
 *
 */

// 選択肢コードの文字種
public enum SelectionCodeCharacter {

	NUMBER_TYPE(0), // 半角数字
	CHARATERS_TYPE(1);// 半角英数字

	public final int value;
}
