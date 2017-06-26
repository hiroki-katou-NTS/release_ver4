package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.gul.util.Range;
import nts.uk.ctx.pr.core.dom.enums.UseOrNot;
public class RangeChecker extends DomainObject {
	//範囲上限
	@Getter
	private UseOrNot isUseHigh;
	
	//範囲下限
	@Getter
	private UseOrNot isUseLow;
	
	@Getter
	private Range<BigDecimal> range;
	
	public RangeChecker(UseOrNot isUseHigh, UseOrNot isUseLow, Range<BigDecimal> range) {
		super();
		this.isUseHigh = isUseHigh;
		this.isUseLow = isUseLow;
		this.range = range;
		BigDecimal defaultValue = new BigDecimal(0);
		if (isUseHigh == UseOrNot.DO_NOT_USE) {
			this.range = Range.between(defaultValue, range.min()); 
		}
		if (isUseLow == UseOrNot.DO_NOT_USE) {
			this.range = Range.between(range.max(), defaultValue);
		}
	}
	
}
