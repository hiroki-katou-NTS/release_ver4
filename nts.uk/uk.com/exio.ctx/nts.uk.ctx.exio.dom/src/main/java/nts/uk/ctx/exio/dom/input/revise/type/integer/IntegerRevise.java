package nts.uk.ctx.exio.dom.input.revise.type.integer;

import java.util.Optional;

import lombok.AllArgsConstructor;
import nts.uk.ctx.exio.dom.input.revise.ReviseValue;
import nts.uk.ctx.exio.dom.input.revise.type.RangeOfValue;

/**
 * 整数型編集
 */
@AllArgsConstructor
public class IntegerRevise implements ReviseValue {
	
	/** 値の有効範囲を指定する */
	private boolean useSpecifyRange;
	
	/** 値の有効範囲 */
	private Optional<RangeOfValue> rangeOfValue;
	
	@Override
	public Object revise(String target) {
		
		String strResult = target;
		
		if (useSpecifyRange) {
			// 値の有効範囲を指定する場合
			strResult = this.rangeOfValue.get().extract(strResult);
		}
		
		return stringToInt(strResult);
	}
	
	// 文字列→整数変換
	private Long stringToInt(String resultStr) {
		return Long.parseLong(resultStr);
	}
}
