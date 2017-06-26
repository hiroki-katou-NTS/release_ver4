package nts.uk.ctx.pr.core.dom.itemmaster.itemdeductperiod;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.UsageClassification;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.Year;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.PeriodAtr;

@Getter
public class ItemDeductPeriod extends AggregateRoot {
	private ItemCode itemCode;
	private PeriodAtr periodAtr;
	private Year strY;
	private Year endY;
	private UsageClassification cycleAtr;
	private UsageClassification cycle01Atr;
	private UsageClassification cycle02Atr;
	private UsageClassification cycle03Atr;
	private UsageClassification cycle04Atr;
	private UsageClassification cycle05Atr;
	private UsageClassification cycle06Atr;
	private UsageClassification cycle07Atr;
	private UsageClassification cycle08Atr;
	private UsageClassification cycle09Atr;
	private UsageClassification cycle10Atr;
	private UsageClassification cycle11Atr;
	private UsageClassification cycle12Atr;

	public ItemDeductPeriod(ItemCode itemCode, PeriodAtr periodAtr, Year strY, Year endY, UsageClassification cycleAtr,
			UsageClassification cycle01Atr, UsageClassification cycle02Atr, UsageClassification cycle03Atr,
			UsageClassification cycle04Atr, UsageClassification cycle05Atr, UsageClassification cycle06Atr,
			UsageClassification cycle07Atr, UsageClassification cycle08Atr, UsageClassification cycle09Atr,
			UsageClassification cycle10Atr, UsageClassification cycle11Atr, UsageClassification cycle12Atr) {
		super();
		this.itemCode = itemCode;
		this.periodAtr = periodAtr;
		this.strY = strY;
		this.endY = endY;
		this.cycleAtr = cycleAtr;
		this.cycle01Atr = cycle01Atr;
		this.cycle02Atr = cycle02Atr;
		this.cycle03Atr = cycle03Atr;
		this.cycle04Atr = cycle04Atr;
		this.cycle05Atr = cycle05Atr;
		this.cycle06Atr = cycle06Atr;
		this.cycle07Atr = cycle07Atr;
		this.cycle08Atr = cycle08Atr;
		this.cycle09Atr = cycle09Atr;
		this.cycle10Atr = cycle10Atr;
		this.cycle11Atr = cycle11Atr;
		this.cycle12Atr = cycle12Atr;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtil.isNullOrEmpty(this.itemCode.v(), true) || (this.strY.v() > 9999 && this.strY.v() < 1900)
				|| (this.endY.v() > 9999 && this.endY.v() < 1900)) {
			throw new BusinessException("ER001");
		}
		if (this.strY.v() > this.endY.v()) {
			throw new BusinessException("ER024");
		}
		if (this.cycle01Atr.value == 0 && this.cycle12Atr.value == 0 && this.cycleAtr.value == 1) {
			throw new BusinessException("ER007");
		}

	}

	public static ItemDeductPeriod createFromJavaType(String itemCode, int periodAtr, int strY, int endY, int cycleAtr,
			int cycle01Atr, int cycle02Atr, int cycle03Atr, int cycle04Atr, int cycle05Atr, int cycle06Atr,
			int cycle07Atr, int cycle08Atr, int cycle09Atr, int cycle10Atr, int cycle11Atr, int cycle12Atr) {

		return new ItemDeductPeriod(new ItemCode(itemCode), EnumAdaptor.valueOf(periodAtr, PeriodAtr.class),
				new Year(strY), new Year(endY), EnumAdaptor.valueOf(cycleAtr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle01Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle02Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle03Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle04Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle05Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle06Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle07Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle08Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle09Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle10Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle11Atr, UsageClassification.class),
				EnumAdaptor.valueOf(cycle12Atr, UsageClassification.class));
	}

}
