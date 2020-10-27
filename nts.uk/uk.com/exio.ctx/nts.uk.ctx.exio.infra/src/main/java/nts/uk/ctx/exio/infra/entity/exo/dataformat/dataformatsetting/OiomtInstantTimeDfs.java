package nts.uk.ctx.exio.infra.entity.exo.dataformat.dataformatsetting;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.InstantTimeDataFmSetting;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 外部出力時刻型データ形式設定（項目単位）
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_EX_OUT_FM_TIME")
public class OiomtInstantTimeDfs extends ContractUkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public OiomtInstantTimeDfsPk instantTimeDfsPk;

	/**
	 * 時分/分選択
	 */
	@Basic(optional = false)
	@Column(name = "HOUR_MIN_SELECT")
	public int timeSeletion;

	/**
	 * データ形式小数桁
	 */
	@Basic(optional = true)
	@Column(name = "MINUTE_FRACTION_DIGIT")
	public Integer minuteFractionDigit;

	/**
	 * 分/小数処理端数区分
	 */
	@Basic(optional = false)
	@Column(name = "DECIMAL_FRACTION")
	public int minuteFractionDigitProcessCls;

	/**
	 * 進数選択
	 */
	@Basic(optional = false)
	@Column(name = "DECIMAL_SELECT")
	public int decimalSelection;

	/**
	 * マイナス値を0で出力
	 */
	@Basic(optional = false)
	@Column(name = "OUTPUT_MINUS_AS_ZERO")
	public int outputMinusAsZero;

	/**
	 * 区切り文字設定
	 */
	@Basic(optional = false)
	@Column(name = "DELIMITER_SET")
	public int delimiterSetting;

	/**
	 * 翌日出力方法
	 */
	@Basic(optional = false)
	@Column(name = "NEXT_DAY_OUTPUT_METHOD")
	public int nextDayOutputMethod;

	/**
	 * 前日出力方法
	 */
	@Basic(optional = false)
	@Column(name = "PREV_DAY_OUTPUT_METHOD")
	public int prevDayOutputMethod;

	/**
	 * 固定長出力
	 */
	@Basic(optional = false)
	@Column(name = "FIXED_LENGTH_OUTPUT")
	public int fixedLengthOutput;

	/**
	 * 固定長整数桁
	 */
	@Basic(optional = true)
	@Column(name = "FIXED_LENGTH_INTEGER_DIGIT")
	public Integer fixedLongIntegerDigit;

	/**
	 * 固定長編集方法
	 */
	@Basic(optional = false)
	@Column(name = "FIXED_LENGTH_EDIT_METHOD")
	public int fixedLengthEditingMethod;

	/**
	 * NULL値置換
	 */
	@Basic(optional = false)
	@Column(name = "NULL_REPLACE_VAL_ATR")
	public int nullValueSubs;
	
	/**
	 * NULL値置換の値
	 */
	@Basic(optional = true)
	@Column(name = "NULL_REPLACE_VAL")
	public String valueOfNullValueSubs;

	/**
	 * 固定値
	 */
	@Basic(optional = false)
	@Column(name = "FIXED_VAL_ATR")
	public int fixedValue;

	/**
	 * 固定値の値
	 */
	@Basic(optional = true)
	@Column(name = "FIXED_VAL")
	public String valueOfFixedValue;

	@Override
	protected Object getKey() {
		return instantTimeDfsPk;
	}

	public InstantTimeDataFmSetting toDomain() {
		return new InstantTimeDataFmSetting(this.instantTimeDfsPk.cid, this.nullValueSubs, this.valueOfNullValueSubs,
				this.outputMinusAsZero, this.fixedValue, this.valueOfFixedValue, this.timeSeletion,
				this.fixedLengthOutput, this.fixedLongIntegerDigit, this.fixedLengthEditingMethod,
				this.delimiterSetting, this.prevDayOutputMethod, this.nextDayOutputMethod, this.minuteFractionDigit,
				this.decimalSelection, this.minuteFractionDigitProcessCls, this.instantTimeDfsPk.condSetCd,
				this.instantTimeDfsPk.outItemCd);
	}

	public static OiomtInstantTimeDfs toEntity(InstantTimeDataFmSetting domain) {
		return new OiomtInstantTimeDfs(
				new OiomtInstantTimeDfsPk(
						domain.getCid(), 
						domain.getConditionSettingCode().v(),
						domain.getOutputItemCode().v()),
				domain.getTimeSeletion().value,
				domain.getMinuteFractionDigit().isPresent() ? domain.getMinuteFractionDigit().get().v() : null,
				domain.getMinuteFractionDigitProcessCls().value,
				domain.getDecimalSelection().value,
				domain.getOutputMinusAsZero().value,
				domain.getDelimiterSetting().value, 
				domain.getNextDayOutputMethod().value,  
				domain.getPrevDayOutputMethod().value,
				domain.getFixedLengthOutput().value,
				domain.getFixedLongIntegerDigit().isPresent() ? domain.getFixedLongIntegerDigit().get().v() : null,
				domain.getFixedLengthEditingMethod().value, 
				domain.getNullValueReplace().value, 
				domain.getValueOfNullValueReplace().isPresent() ? domain.getValueOfNullValueReplace().get().v() : null,
				domain.getFixedValue().value, 
				domain.getValueOfFixedValue().isPresent() ? domain.getValueOfFixedValue().get().v() : null);
	}
}
