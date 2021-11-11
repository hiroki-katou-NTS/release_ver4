/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.scherec.optitem;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.scherec.optitem.calculation.CalcResultOfAnyItem;
import nts.uk.shr.com.i18n.TextResource;

/**
 * The Class CalculationResultRange.
 */
// 計算結果の範囲
// 責務 : 計算結果の有効範囲をきめる。
// Responsibility: determine the effective range of the calculation result.
@Getter
public class CalcResultRange extends DomainObject {

	/** The upper limit. */
	// 上限値チェック
	private CalcRangeCheck upperLimit;

	/** The lower limit. */
	// 下限値チェック
	private CalcRangeCheck lowerLimit;

	// ===================== Optional ======================= //
	/** The number range. */
	// 回数範囲
	private Optional<NumberRange> numberRange;

	/** The time range. */
	// 時間範囲
	private Optional<TimeRange> timeRange;

	/** The amount range. */
	// 金額範囲
	private Optional<AmountRange> amountRange;

	/**
	 * Instantiates a new calculation result range.
	 *
	 * @param memento the memento
	 */
	public CalcResultRange(CalcResultRangeGetMemento memento) {
		this.upperLimit = memento.getUpperLimit();
		this.lowerLimit = memento.getLowerLimit();
		this.numberRange = memento.getNumberRange();
		this.timeRange = memento.getTimeRange();
		this.amountRange = memento.getAmountRange();
	}

	/**
	 * Checks for both limit.
	 *
	 * @return true, if successful
	 */
	public boolean hasBothLimit() {
		return this.lowerLimit == CalcRangeCheck.SET && this.upperLimit == CalcRangeCheck.SET;
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(CalcResultRangeSetMemento memento) {
		memento.setUpperLimit(this.upperLimit);
		memento.setLowerLimit(this.lowerLimit);
		memento.setNumberRange(this.numberRange);
		memento.setTimeRange(this.timeRange);
		memento.setAmountRange(this.amountRange);
	}
	
	/**
	 * 上限下限チェック
	 * @return
	 */
	public CalcResultOfAnyItem checkRange(CalcResultOfAnyItem calcResultOfAnyItem, OptionalItem optionalItem) {
		if(this.upperLimit.isSET()) {
			BigDecimal upperValue = getUpperLimitValue(calcResultOfAnyItem, optionalItem);
			calcResultOfAnyItem = calcResultOfAnyItem.reCreateCalcResultOfAnyItem(upperValue, optionalItem.getOptionalItemAtr());
		}
		if(this.lowerLimit.isSET()) {
			BigDecimal lowerValue = getLowerLimitValue(calcResultOfAnyItem, optionalItem);
			calcResultOfAnyItem = calcResultOfAnyItem.reCreateCalcResultOfAnyItem(lowerValue, optionalItem.getOptionalItemAtr());
		}
		return calcResultOfAnyItem;
	}
	
		
	/**
	 * 上限値の制御
	 * @param calcResultOfAnyItem
	 * @param optionalItem
	 * @return
	 */
	public BigDecimal getUpperLimitValue(CalcResultOfAnyItem calcResultOfAnyItem, OptionalItem optionalItem) {
		switch(optionalItem.getOptionalItemAtr()) {
		case TIME:
			return this.timeRange.map(range -> {
				
				return getValueOrUpper(() -> calcResultOfAnyItem.getTime(), 
										() -> range.getUpper(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		case NUMBER:
			return this.numberRange.map(range -> {
				
				return getValueOrUpper(() -> calcResultOfAnyItem.getCount(), 
										() -> range.getUpper(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		case AMOUNT:
			return this.amountRange.map(range -> {
				
				return getValueOrUpper(() -> calcResultOfAnyItem.getMoney(), 
										() -> range.getUpper(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		default:
			throw new RuntimeException("unknown value of enum OptionalItemAtr");
		}
	}
	
	/**
	 * 下限値の制御
	 * @param calcResultOfAnyItem
	 * @param optionalItem
	 * @return
	 */
	public BigDecimal getLowerLimitValue(CalcResultOfAnyItem calcResultOfAnyItem, OptionalItem optionalItem) {
		switch(optionalItem.getOptionalItemAtr()) {
		case TIME:
			return this.timeRange.map(range -> {
				
				return getValueOrLower(() -> calcResultOfAnyItem.getTime(),
										() -> range.getLower(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		case NUMBER:
			return this.numberRange.map(range -> {
				
				return getValueOrLower(() -> calcResultOfAnyItem.getCount(),
										() -> range.getLower(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		case AMOUNT:
			return this.amountRange.map(range -> {
				
				return getValueOrLower(() -> calcResultOfAnyItem.getMoney(),
										() -> range.getLower(optionalItem.getPerformanceAtr()));
			}).orElse(BigDecimal.ZERO);
			
		default:
			throw new RuntimeException("unknown value of enum OptionalItemAtr");
		}
	}
	
	private BigDecimal getValueOrUpper(Supplier<Optional<BigDecimal>> target, Supplier<Optional<BigDecimal>> limit) {
		BigDecimal upperLimit = limit.get().orElse(BigDecimal.ZERO);
			
		return target.get().map(c -> {
			/** 値 > 上限値　の場合　値←上限値とする。 */
			if (c.compareTo(upperLimit) > 0) {
				return upperLimit;
			}
			return c;
		}).orElse(BigDecimal.ZERO);
	}
	
	private BigDecimal getValueOrLower(Supplier<Optional<BigDecimal>> target, Supplier<Optional<BigDecimal>> limit) {
		BigDecimal lowerLimit = limit.get().orElse(BigDecimal.ZERO);
			
		return target.get().map(c -> {
			/** 値 < 下限値　の場合　値←下限値とする。 */
			if (c.compareTo(lowerLimit) < 0) {
				return lowerLimit;
			}
			return c;
		}).orElse(BigDecimal.ZERO);
	}
	
	/**
	 * 入力範囲チェック
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).任意項目.関数アルゴリズム.計算結果の範囲.<<Public>>入力範囲チェック.入力範囲チェック
	 * 
	 * @param inputValue      入力値
	 * @param performanceAtr  実績区分
	 * @param optionalItemAtr 任意項目の属性
	 * @return
	 */
	public ValueCheckResult checkInputRange(BigDecimal inputValue, PerformanceAtr performanceAtr,
			OptionalItemAtr optionalItemAtr) {
		//「@上限値チェック」と「@下限値チェック」の内容を確認する
		//上限値チェック = 設定する || 下限値チェック = 設定する
		if(this.upperLimit == CalcRangeCheck.SET || this.lowerLimit == CalcRangeCheck.SET  ) {
			//上限値、下限値を取得
			ControlRangeValue controlRangeValue = this.getUpperLimit(performanceAtr, optionalItemAtr);
			//入力範囲チェック
			boolean checkRange = controlRangeValue.checkInputRange(inputValue);
			//チェック結果
			if(!checkRange) {
				//入力範囲エラーメッセージ作成する
				String errorContent = this.createInputRangeErrorMsg(controlRangeValue, optionalItemAtr);
				return new ValueCheckResult(false, Optional.of(errorContent));
			}
		}
		return new ValueCheckResult(true, Optional.empty());
	}
	
	/**
	 * 入力範囲エラーメッセージ作成する
	 * @param controlRangeValue  制御範囲値
	 */
	public String createInputRangeErrorMsg(ControlRangeValue controlRangeValue,OptionalItemAtr optionalItemAtr) {
		String upperLimit =""; 
		String lowerLimit = ""; 
		if(optionalItemAtr == OptionalItemAtr.TIME) {
			if(this.upperLimit == CalcRangeCheck.SET) {
				upperLimit = convertTime(controlRangeValue.getUpperLimit().get().intValue());
			}
			if(this.lowerLimit == CalcRangeCheck.SET) {
				lowerLimit = convertTime(controlRangeValue.getLowerLimit().get().intValue());
			}
		}else {
			if(this.upperLimit == CalcRangeCheck.SET) {
				upperLimit = String.valueOf(controlRangeValue.getUpperLimit().get().doubleValue());
			}
			if(this.lowerLimit == CalcRangeCheck.SET) {
				lowerLimit = String.valueOf(controlRangeValue.getLowerLimit().get().doubleValue());
			}
		}
		//@上限値チェック = 設定する && @下限値チェック = 設定しない
		if(this.upperLimit == CalcRangeCheck.SET && this.lowerLimit == CalcRangeCheck.NOT_SET) {
			return TextResource.localize("Msg_2293",upperLimit);
		}
		//@上限値チェック = 設定しない && @下限値チェック = 設定する
		if (this.upperLimit == CalcRangeCheck.NOT_SET && this.lowerLimit == CalcRangeCheck.SET) {
			return TextResource.localize("Msg_2292", lowerLimit);
		}
		// @上限値チェック = 設定する && @下限値チェック = 設定する
		return TextResource.localize("Msg_2291", lowerLimit,
				upperLimit);
	}

	/**
	 * 上限値を取得
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).任意項目.関数アルゴリズム.計算結果の範囲.上限下限チェック.上限値、下限値を取得
	 */
	public ControlRangeValue getUpperLimit(PerformanceAtr performanceAtr,OptionalItemAtr optionalItemAtr) {
		switch(optionalItemAtr) {
		case TIME:
			return this.timeRange.map(range -> {
				
				return new ControlRangeValue(range.getUpper(performanceAtr),
						range.getLower(performanceAtr));
			}).orElse(new ControlRangeValue(Optional.empty(),Optional.empty()));
			
		case NUMBER:
			return this.numberRange.map(range -> {
				
				return new ControlRangeValue(range.getUpper(performanceAtr),
						range.getLower(performanceAtr));
			}).orElse(new ControlRangeValue(Optional.empty(),Optional.empty()));
			
		default: //金額
			return this.amountRange.map(range -> {
				
				return new ControlRangeValue(range.getUpper(performanceAtr),
						range.getLower(performanceAtr));
			}).orElse(new ControlRangeValue(Optional.empty(),Optional.empty()));
		}
	}
	
	private String convertTime(Integer time) {
		if (time == null) {
			return "";
		}
		String m = String.valueOf(time % 60).length() > 1 ? String.valueOf(time % 60) : 0 + String.valueOf(time % 60);
		String timeString = String.valueOf(time / 60) + ":" + m;
		return timeString;
	}

	public CalcResultRange(CalcRangeCheck upperLimit, CalcRangeCheck lowerLimit, Optional<NumberRange> numberRange,
			Optional<TimeRange> timeRange, Optional<AmountRange> amountRange) {
		super();
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		this.numberRange = numberRange;
		this.timeRange = timeRange;
		this.amountRange = amountRange;
	}
}
