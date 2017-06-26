package nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemName;
import nts.uk.ctx.pr.core.dom.itemmaster.UniteCode;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.AlRangeHigh;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.AlRangeLow;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ErrRangeHigh;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ErrRangeLow;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.RangeAtr;

@Getter
public class ItemSalaryBD extends AggregateRoot {

	private ItemCode itemCode;
	private ItemCode itemBreakdownCode;
	private ItemName itemBreakdownName;
	private ItemName itemBreakdownAbName;
	private UniteCode uniteCode;
	private DisplayAtr zeroDispSet;
	private DisplayAtr itemDispAtr;
	private RangeAtr errRangeLowAtr;
	private ErrRangeLow errRangeLow;
	private RangeAtr errRangeHighAtr;
	private ErrRangeHigh errRangeHigh;
	private RangeAtr alRangeLowAtr;
	private AlRangeLow alRangeLow;
	private RangeAtr alRangeHighAtr;
	private AlRangeHigh alRangeHigh;

	@Override
	public void validate() {
		super.validate();
		if (StringUtil.isNullOrEmpty(this.itemCode.v(), true)
				|| StringUtil.isNullOrEmpty(this.itemBreakdownCode.v(), true)
				|| StringUtil.isNullOrEmpty(this.itemBreakdownName.v(), true)
				|| StringUtil.isNullOrEmpty(this.itemBreakdownAbName.v(), true)) {
			throw new BusinessException("ER001");
		}

	}

	public static ItemSalaryBD createFromJavaType(String itemCode, String itemBreakdownCode, String itemBreakdownName,
			String itemBreakdownAbName, String uniteCode, int zeroDispSet, int itemDispAtr, int errRangeLowAtr,
			BigDecimal errRangeLow, int errRangeHighAtr, BigDecimal errRangeHigh, int alRangeLowAtr,
			BigDecimal alRangeLow, int alRangeHighAtr, BigDecimal alRangeHigh) {
		return new ItemSalaryBD(new ItemCode(itemCode), new ItemCode(itemBreakdownCode),
				new ItemName(itemBreakdownName), new ItemName(itemBreakdownAbName), new UniteCode(uniteCode),
				EnumAdaptor.valueOf(zeroDispSet, DisplayAtr.class), EnumAdaptor.valueOf(itemDispAtr, DisplayAtr.class),
				EnumAdaptor.valueOf(errRangeLowAtr, RangeAtr.class), new ErrRangeLow(errRangeLow),
				EnumAdaptor.valueOf(errRangeHighAtr, RangeAtr.class), new ErrRangeHigh(errRangeHigh),
				EnumAdaptor.valueOf(alRangeLowAtr, RangeAtr.class), new AlRangeLow(alRangeLow),
				EnumAdaptor.valueOf(alRangeHighAtr, RangeAtr.class), new AlRangeHigh(alRangeHigh));

	}

	public ItemSalaryBD(ItemCode itemCode, ItemCode itemBreakdownCode, ItemName itemBreakdownName,
			ItemName itemBreakdownAbName, UniteCode uniteCode, DisplayAtr zeroDispSet, DisplayAtr itemDispAtr,
			RangeAtr errRangeLowAtr, ErrRangeLow errRangeLow, RangeAtr errRangeHighAtr, ErrRangeHigh errRangeHigh,
			RangeAtr alRangeLowAtr, AlRangeLow alRangeLow, RangeAtr alRangeHighAtr, AlRangeHigh alRangeHigh) {
		super();
		this.itemCode = itemCode;
		this.itemBreakdownCode = itemBreakdownCode;
		this.itemBreakdownName = itemBreakdownName;
		this.itemBreakdownAbName = itemBreakdownAbName;
		this.uniteCode = uniteCode;
		this.zeroDispSet = zeroDispSet;
		this.itemDispAtr = itemDispAtr;
		this.errRangeLowAtr = errRangeLowAtr;
		this.errRangeLow = errRangeLow;
		this.errRangeHighAtr = errRangeHighAtr;
		this.errRangeHigh = errRangeHigh;
		this.alRangeLowAtr = alRangeLowAtr;
		this.alRangeLow = alRangeLow;
		this.alRangeHighAtr = alRangeHighAtr;
		this.alRangeHigh = alRangeHigh;
	}

}
