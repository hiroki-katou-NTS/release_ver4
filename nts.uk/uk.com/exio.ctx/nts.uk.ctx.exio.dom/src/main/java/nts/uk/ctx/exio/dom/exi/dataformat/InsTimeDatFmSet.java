package nts.uk.ctx.exio.dom.exi.dataformat;

import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 時刻型データ形式設定
 */
@Getter
public class InsTimeDatFmSet extends DataFormatSetting {

	/**
	 * 区切り文字設定
	 */
	private DelimiterSetting delimiterSet;

	/**
	 * 固定値
	 */
	private NotUseAtr fixedValue;

	/**
	 * 時分/分選択
	 */
	private HourlySegment hourMinSelect;

	/**
	 * 有効桁長
	 */
	private NotUseAtr effectiveDigitLength;

	/**
	 * 端数処理
	 */
	private NotUseAtr roundProc;

	/**
	 * 進数選択
	 */
	private DecimalSelection decimalSelect;

	/**
	 * 固定値の値
	 */
	private Optional<DataSettingFixedValue> valueOfFixedValue;

	/**
	 * 有効桁数開始桁
	 */
	private Optional<AcceptedDigit> startDigit;

	/**
	 * 有効桁数終了桁
	 */
	private Optional<AcceptedDigit> endDigit;

	/**
	 * 端数処理区分
	 */
	private Optional<TimeRounding> roundProcCls;

	public InsTimeDatFmSet(int itemType, int delimiterSet, int fixedValue, int hourMinSelect,
			int effectiveDigitLength, int roundProc, int decimalSelect, String valueOfFixedValue,
			Integer startDigit, Integer endDigit, Integer roundProcCls) {
		super(itemType);
		this.delimiterSet = EnumAdaptor.valueOf(delimiterSet, DelimiterSetting.class);
		this.fixedValue = EnumAdaptor.valueOf(fixedValue, NotUseAtr.class);
		this.effectiveDigitLength = EnumAdaptor.valueOf(fixedValue, NotUseAtr.class);
		this.hourMinSelect = EnumAdaptor.valueOf(hourMinSelect, HourlySegment.class);
		this.roundProc = EnumAdaptor.valueOf(roundProc, NotUseAtr.class);
		this.decimalSelect = EnumAdaptor.valueOf(decimalSelect, DecimalSelection.class);
		if (null == valueOfFixedValue) {
			this.valueOfFixedValue = Optional.empty();
		} else {
			this.valueOfFixedValue = Optional.of(new DataSettingFixedValue(valueOfFixedValue));
		}
		if (null == startDigit) {
			this.startDigit = Optional.empty();
		} else {
			this.startDigit = Optional.of(new AcceptedDigit(startDigit));
		}
		if (null == endDigit) {
			this.endDigit = Optional.empty();
		} else {
			this.endDigit = Optional.of(new AcceptedDigit(endDigit));
		}
		if (null == roundProcCls) {
			this.roundProcCls = Optional.empty();
		} else {
			this.roundProcCls = Optional.of(EnumAdaptor.valueOf(roundProcCls, TimeRounding.class));
		}
	}
}
