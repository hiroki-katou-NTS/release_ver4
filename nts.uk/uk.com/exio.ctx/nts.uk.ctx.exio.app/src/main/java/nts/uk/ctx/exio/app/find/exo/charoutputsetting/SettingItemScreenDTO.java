package nts.uk.ctx.exio.app.find.exo.charoutputsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.exio.dom.exo.dataformat.ChacDataFmSet;

@Setter
@Getter
@AllArgsConstructor
public class SettingItemScreenDTO {

	/**
	 * NULL値置換
	 */
	private int nullValueReplace;

	/**
	 * NULL値置換の値
	 */
	private String valueOfNullValueReplace;

	/**
	 * コード編集
	 */
	private int cdEditting;

	/**
	 * 固定値
	 */
	private int fixedValue;

	/**
	 * コード編集方法
	 */
	private int cdEdittingMethod;

	/**
	 * コード編集桁
	 */
	private int cdEditDigit;

	/**
	 * コード変換コード
	 */
	private String cdConvertCd;

	/**
	 * スペース編集
	 */
	private int spaceEditting;

	/**
	 * 有効桁数
	 */
	private int effectDigitLength;

	/**
	 * 有効桁数開始桁
	 */
	private int startDigit;

	/**
	 * 有効桁数終了桁
	 */
	private int endDigit;

	/**
	 * 固定値の値
	 */
	private String valueOfFixedValue;
	
	public static SettingItemScreenDTO fromDomain(ChacDataFmSet domain){
		String valueOfNullValueReplace = domain.getValueOfNullValueReplace().isPresent() ? domain.getValueOfNullValueReplace().get().v() : null;
		Integer cdEditDigit = domain.getCdEditDigit().isPresent() ? domain.getCdEditDigit().get().v() : null;
		String cdConvertCd = domain.getConvertCode().isPresent() ? domain.getConvertCode().get().v() : null;
		Integer startDigit = domain.getStartDigit().isPresent() ? domain.getStartDigit().get().v() : null;
		Integer endDigit = domain.getEndDigit().isPresent() ? domain.getEndDigit().get().v() : null;
		String valueOfFixedValue = domain.getValueOfFixedValue().isPresent() ? domain.getValueOfFixedValue().get().v() : null;

		return new SettingItemScreenDTO(domain.getNullValueReplace().value, valueOfNullValueReplace, domain.getCdEditting().value, domain.getFixedValue().value, domain.getCdEdittingMethod().value, cdEditDigit, cdConvertCd, domain.getSpaceEditting().value, domain.getEffectDigitLength().value, startDigit, endDigit, valueOfFixedValue);
	}
}
