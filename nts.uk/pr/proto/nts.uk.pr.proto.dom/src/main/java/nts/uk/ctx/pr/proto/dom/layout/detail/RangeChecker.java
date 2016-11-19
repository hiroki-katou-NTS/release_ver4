package nts.uk.ctx.pr.proto.dom.layout.detail;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.gul.util.Range;
import nts.uk.ctx.pr.proto.dom.enums.UseOrNot;
public class RangeChecker extends DomainObject {
	//範囲上限
	@Getter
	private UseOrNot isUseHigh;
	
	//範囲下限
	@Getter
	private UseOrNot isUseLow;
	
	@Getter
	private Range<Integer> range;
	
	public RangeChecker(UseOrNot isUseHigh, UseOrNot isUseLow, Range<Integer> range) {
		super();
		this.isUseHigh = isUseHigh;
		this.isUseLow = isUseLow;
		this.range = range;
		Integer defaultValue = new Integer(0);
		if (isUseHigh == UseOrNot.DO_NOT_USE) {
			this.range = Range.between(defaultValue, range.min()); 
		}
		if (isUseLow == UseOrNot.DO_NOT_USE) {
			this.range = Range.between(range.max(), defaultValue);
		}
	}
	
}
