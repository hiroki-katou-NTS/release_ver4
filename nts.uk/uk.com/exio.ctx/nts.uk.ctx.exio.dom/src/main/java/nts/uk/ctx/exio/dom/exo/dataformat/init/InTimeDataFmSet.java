package nts.uk.ctx.exio.dom.exo.dataformat.init;

import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.exio.dom.exo.base.ItemType;
import nts.uk.shr.com.enumcommon.NotUseAtr;


/**
* 時刻型データ形式設定
*/
@Getter
public class InTimeDataFmSet extends DataFormatSetting
{
    
    /**
    * 会社ID
    */
    private String cid;
    
    /**
    * マイナス値を0で出力する
    */
    private NotUseAtr outputMinusAsZero;
    
    /**
    * 時分/分選択
    */
    private HourMinuteClassification timeSeletion;
    
    /**
    * 固定長出力
    */
    private NotUseAtr fixedLengthOutput;
    
    /**
    * 固定長整数桁
    */
    private Optional<DataFormatIntegerDigit> fixedLongIntegerDigit;
    
    /**
    * 固定長編集方法
    */
    private FixedLengthEditingMethod fixedLengthEditingMothod;
    
    /**
    * 区切り文字設定
    */
    private DelimiterSetting delimiterSetting;
    
    /**
    * 前日出力方法
    */
    private PreviousDayOutputMethod prevDayOutputMethod;
    
    /**
    * 翌日出力方法
    */
    private NextDayOutputMethod nextDayOutputMethod;
    
    /**
    * データ形式小数桁
    */
    private Optional<DataFormatDecimalDigit> minuteFractionDigit;
    
    /**
    * 進数選択
    */
    private DecimalSelection decimalSelection;
    
    /**
    * 分/小数処理端数区分
    */
    private Rounding minuteFractionDigitProcessCls;

	public InTimeDataFmSet(int itemType, String cid, int nullValueSubs, String valueOfNullValueSubs,
			int outputMinusAsZero, int fixedValue, String valueOfFixedValue, int timeSeletion, int fixedLengthOutput,
			Integer fixedLongIntegerDigit, int fixedLengthEditingMothod, int delimiterSetting, int prevDayOutputMethod,
			int nextDayOutputMethod, int minuteFractionDigit, int decimalSelection, int minuteFractionDigitProcessCls) {
		super(itemType, fixedValue, valueOfFixedValue, nullValueSubs, valueOfNullValueSubs);
		this.cid = cid;
		this.outputMinusAsZero = EnumAdaptor.valueOf(outputMinusAsZero, NotUseAtr.class);
		this.timeSeletion = EnumAdaptor.valueOf(timeSeletion, HourMinuteClassification.class);
		this.fixedLengthOutput = EnumAdaptor.valueOf(fixedLengthOutput, NotUseAtr.class);
		this.fixedLongIntegerDigit = Optional.of(new DataFormatIntegerDigit(fixedLongIntegerDigit));
		this.fixedLengthEditingMothod = EnumAdaptor.valueOf(fixedLengthEditingMothod, FixedLengthEditingMethod.class);
		this.delimiterSetting = EnumAdaptor.valueOf(delimiterSetting, DelimiterSetting.class);
		this.prevDayOutputMethod = EnumAdaptor.valueOf(prevDayOutputMethod, PreviousDayOutputMethod.class);
		this.nextDayOutputMethod = EnumAdaptor.valueOf(nextDayOutputMethod, NextDayOutputMethod.class);
		this.minuteFractionDigit = Optional.of(new DataFormatDecimalDigit(minuteFractionDigit));
		this.decimalSelection = EnumAdaptor.valueOf(decimalSelection, DecimalSelection.class);
		this.minuteFractionDigitProcessCls = EnumAdaptor.valueOf(minuteFractionDigitProcessCls, Rounding.class);
	}

	public InTimeDataFmSet(ItemType itemType, String cid, NotUseAtr nullValueSubs,
			Optional<DataFormatNullReplacement> valueOfNullValueSubs, NotUseAtr outputMinusAsZero, NotUseAtr fixedValue,
			Optional<DataTypeFixedValue> valueOfFixedValue, HourMinuteClassification timeSeletion,
			NotUseAtr fixedLengthOutput, Optional<DataFormatIntegerDigit> fixedLongIntegerDigit,
			FixedLengthEditingMethod fixedLengthEditingMothod, DelimiterSetting delimiterSetting,
			PreviousDayOutputMethod prevDayOutputMethod, NextDayOutputMethod nextDayOutputMethod,
			Optional<DataFormatDecimalDigit> minuteFractionDigit, DecimalSelection decimalSelection,
			Rounding minuteFractionDigitProcessCls) {
		super(itemType, fixedValue, valueOfFixedValue, nullValueSubs, valueOfNullValueSubs);
		this.cid = cid;
		this.outputMinusAsZero = outputMinusAsZero;
		this.timeSeletion = timeSeletion;
		this.fixedLengthOutput = fixedLengthOutput;
		this.fixedLongIntegerDigit = fixedLongIntegerDigit;
		this.fixedLengthEditingMothod = fixedLengthEditingMothod;
		this.delimiterSetting = delimiterSetting;
		this.prevDayOutputMethod = prevDayOutputMethod;
		this.nextDayOutputMethod = nextDayOutputMethod;
		this.minuteFractionDigit = minuteFractionDigit;
		this.decimalSelection = decimalSelection;
		this.minuteFractionDigitProcessCls = minuteFractionDigitProcessCls;
	}

	
    
}
