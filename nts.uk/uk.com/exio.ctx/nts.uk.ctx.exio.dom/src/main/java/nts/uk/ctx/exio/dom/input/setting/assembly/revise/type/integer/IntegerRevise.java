package nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.integer;

import java.util.Optional;

import lombok.AllArgsConstructor;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.ReviseValue;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.codeconvert.ExternalImportCodeConvert;

/**
 * 整数型編集
 */
@AllArgsConstructor
public class IntegerRevise implements ReviseValue {
	
	/** コード変換 */
	private Optional<ExternalImportCodeConvert> codeConvert;
	
	@Override
	public Object revise(String target) {
		
		String strResult = target;
		
		if(codeConvert.isPresent()) {
			strResult = this.codeConvert.get().convert(strResult).toString();
		}
		
		return stringToInt(strResult);
	}
	
	// 文字列→整数変換
	private Long stringToInt(String resultStr) {
		return Long.parseLong(resultStr);
	}
}
