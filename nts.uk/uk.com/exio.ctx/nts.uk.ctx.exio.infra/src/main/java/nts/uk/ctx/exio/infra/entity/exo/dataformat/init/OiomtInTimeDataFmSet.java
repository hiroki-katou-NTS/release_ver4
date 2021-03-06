package nts.uk.ctx.exio.infra.entity.exo.dataformat.init;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.exo.base.ItemType;
import nts.uk.ctx.exio.dom.exo.dataformat.init.InTimeDataFmSet;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
* 外部出力時刻型データ形式設定（初期値）
*/
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_EX_OUT_FM_TIME_INIT")
public class OiomtInTimeDataFmSet extends ContractUkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public OiomtInTimeDataFmSetPk inTimeDataFmSetPk;

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
	 * 前日出力方法
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
		return inTimeDataFmSetPk;
	}

    public InTimeDataFmSet toDomain() {
        return new InTimeDataFmSet(ItemType.INS_TIME.value, this.inTimeDataFmSetPk.cid, this.nullValueSubs, this.valueOfNullValueSubs, this.outputMinusAsZero, this.fixedValue, this.valueOfFixedValue, this.timeSeletion, this.fixedLengthOutput, this.fixedLongIntegerDigit, this.fixedLengthEditingMethod, this.delimiterSetting, this.prevDayOutputMethod, this.nextDayOutputMethod, this.minuteFractionDigit, this.decimalSelection, this.minuteFractionDigitProcessCls);
    }
    
    public static OiomtInTimeDataFmSet toEntity(InTimeDataFmSet domain) {
        return new OiomtInTimeDataFmSet(
        		new OiomtInTimeDataFmSetPk(domain.getCid()),
        		domain.getNullValueReplace().value,
        		domain.getValueOfNullValueReplace().map(item -> item.v()).orElse(null),
        		domain.getOutputMinusAsZero().value,
        		domain.getFixedValue().value,
        		domain.getValueOfFixedValue().map(item -> item.v()).orElse(null),
        		domain.getTimeSeletion().value,
        		domain.getFixedLengthOutput().value,
        		domain.getFixedLongIntegerDigit().map(item -> item.v()).orElse(null),
        		domain.getFixedLengthEditingMethod().value,
        		domain.getDelimiterSetting().value,
        		domain.getPrevDayOutputMethod().value,
        		domain.getNextDayOutputMethod().value,
        		domain.getMinuteFractionDigit().map(item -> item.v()).orElse(null),
        		domain.getDecimalSelection().value,
        		domain.getMinuteFractionDigitProcessCls().value);
    }

	public OiomtInTimeDataFmSet(OiomtInTimeDataFmSetPk inTimeDataFmSetPk, int nullValueSubs,
			String valueOfNullValueSubs, int outputMinusAsZero, int fixedValue, String valueOfFixedValue,
			int timeSeletion, int fixedLengthOutput, Integer fixedLongIntegerDigit, int fixedLengthEditingMethod,
			int delimiterSetting, int prevDayOutputMethod, int nextDayOutputMethod, Integer minuteFractionDigit,
			int decimalSelection, int minuteFractionDigitProcessCls) {
		super();
		this.inTimeDataFmSetPk = inTimeDataFmSetPk;
		this.nullValueSubs = nullValueSubs;
		this.valueOfNullValueSubs = valueOfNullValueSubs;
		this.outputMinusAsZero = outputMinusAsZero;
		this.fixedValue = fixedValue;
		this.valueOfFixedValue = valueOfFixedValue;
		this.timeSeletion = timeSeletion;
		this.fixedLengthOutput = fixedLengthOutput;
		this.fixedLongIntegerDigit = fixedLongIntegerDigit;
		this.fixedLengthEditingMethod = fixedLengthEditingMethod;
		this.delimiterSetting = delimiterSetting;
		this.prevDayOutputMethod = prevDayOutputMethod;
		this.nextDayOutputMethod = nextDayOutputMethod;
		this.minuteFractionDigit = minuteFractionDigit;
		this.decimalSelection = decimalSelection;
		this.minuteFractionDigitProcessCls = minuteFractionDigitProcessCls;
	}
}
