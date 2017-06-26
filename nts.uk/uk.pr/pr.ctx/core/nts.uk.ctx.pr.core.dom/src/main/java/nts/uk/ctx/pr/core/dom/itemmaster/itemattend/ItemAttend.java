package nts.uk.ctx.pr.core.dom.itemmaster.itemattend;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.AlRangeHigh;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.AlRangeLow;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ErrRangeHigh;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ErrRangeLow;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.RangeAtr;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class ItemAttend extends AggregateRoot {
	private ItemCode itemCode;
	private AvePayAtr avePayAtr;
	private ItemAtr itemAtr;
	private RangeAtr errRangeLowAtr;
	private ErrRangeLow errRangeLow;
	private RangeAtr errRangeHighAtr;
	private ErrRangeHigh errRangeHigh;
	private RangeAtr alRangeLowAtr;
	private AlRangeLow alRangeLow;
	private RangeAtr alRangeHighAtr;
	private AlRangeHigh alRangeHigh;
	private WorkDaysScopeAtr workDaysScopeAtr;
	private Memo memo;

	public ItemAttend(ItemCode itemCode, AvePayAtr avePayAtr, ItemAtr itemAtr, RangeAtr errRangeLowAtr,
			ErrRangeLow errRangeLow, RangeAtr errRangeHighAtr, ErrRangeHigh errRangeHigh, RangeAtr alRangeLowAtr,
			AlRangeLow alRangeLow, RangeAtr alRangeHighAtr, AlRangeHigh alRangeHigh, WorkDaysScopeAtr workDaysScopeAtr,
			Memo memo) {
		super();
		this.itemCode = itemCode;
		this.avePayAtr = avePayAtr;
		this.itemAtr = itemAtr;
		this.errRangeLowAtr = errRangeLowAtr;
		this.errRangeLow = errRangeLow;
		this.errRangeHighAtr = errRangeHighAtr;
		this.errRangeHigh = errRangeHigh;
		this.alRangeLowAtr = alRangeLowAtr;
		this.alRangeLow = alRangeLow;
		this.alRangeHighAtr = alRangeHighAtr;
		this.alRangeHigh = alRangeHigh;
		this.workDaysScopeAtr = workDaysScopeAtr;
		this.memo = memo;
	}

	public static ItemAttend createFromJavaType(String itemCode, int avePayAtr, int itemAtr, int errRangeLowAtr,
			BigDecimal errRangeLow, int errRangeHighAtr, BigDecimal errRangeHigh, int alRangeLowAtr,
			BigDecimal alRangeLow, int alRangeHighAtr, BigDecimal alRangeHigh, int workDaysScopeAtr, String memo) {
		return new ItemAttend(new ItemCode(itemCode), EnumAdaptor.valueOf(avePayAtr, AvePayAtr.class),
				EnumAdaptor.valueOf(itemAtr, ItemAtr.class), EnumAdaptor.valueOf(errRangeLowAtr, RangeAtr.class),
				new ErrRangeLow(errRangeLow), EnumAdaptor.valueOf(errRangeHighAtr, RangeAtr.class),
				new ErrRangeHigh(errRangeHigh), EnumAdaptor.valueOf(alRangeLowAtr, RangeAtr.class),
				new AlRangeLow(alRangeLow), EnumAdaptor.valueOf(alRangeHighAtr, RangeAtr.class),
				new AlRangeHigh(alRangeHigh), EnumAdaptor.valueOf(workDaysScopeAtr, WorkDaysScopeAtr.class),
				new Memo(memo));
	}

	/**
	 * Set new value ave payment attribute
	 * 
	 * @param value
	 */
	public void setAvePayAttribute(AvePayAtr ave) {
		this.avePayAtr = ave;
	}
}
