package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class ItemSalary extends AggregateRoot {

	private ItemCode itemCode;
	private TaxAtr taxAtr;
	private InsAtr socialInsAtr;
	private InsAtr laborInsAtr;
	private FixPayAtr fixPayAtr;
	private ApplyFor applyForAllEmpFlg;
	private ApplyFor applyForMonthlyPayEmp;
	private ApplyFor applyForDaymonthlyPayEmp;
	private ApplyFor applyForDaylyPayEmp;
	private ApplyFor applyForHourlyPayEmp;
	private AvePayAtr avePayAtr;
	private RangeAtr errRangeLowAtr;
	private ErrRangeLow errRangeLow;
	private RangeAtr errRangeHighAtr;
	private ErrRangeHigh errRangeHigh;
	private RangeAtr alRangeLowAtr;
	private AlRangeLow alRangeLow;
	private RangeAtr alRangeHighAtr;
	private AlRangeHigh alRangeHigh;
	private Memo memo;
	private LimitMnyAtr limitMnyAtr;
	private LimitMnyRefItemCd limitMnyRefItemCode;
	private LimitMny limitMny;

	public ItemSalary(ItemCode itemCode, TaxAtr taxAtr, InsAtr socialInsAtr, InsAtr laborInsAtr, FixPayAtr fixPayAtr,
			ApplyFor applyForAllEmpFlg, ApplyFor applyForMonthlyPayEmp, ApplyFor applyForDaymonthlyPayEmp,
			ApplyFor applyForDaylyPayEmp, ApplyFor applyForHourlyPayEmp, AvePayAtr avePayAtr, RangeAtr errRangeLowAtr,
			ErrRangeLow errRangeLow, RangeAtr errRangeHighAtr, ErrRangeHigh errRangeHigh, RangeAtr alRangeLowAtr,
			AlRangeLow alRangeLow, RangeAtr alRangeHighAtr, AlRangeHigh alRangeHigh, Memo memo, LimitMnyAtr limitMnyAtr,
			LimitMnyRefItemCd limitMnyRefItemCode, LimitMny limitMny) {
		super();

		this.itemCode = itemCode;
		this.taxAtr = taxAtr;
		this.socialInsAtr = socialInsAtr;
		this.laborInsAtr = laborInsAtr;
		this.fixPayAtr = fixPayAtr;
		this.applyForAllEmpFlg = applyForAllEmpFlg;
		this.applyForMonthlyPayEmp = applyForMonthlyPayEmp;
		this.applyForDaymonthlyPayEmp = applyForDaymonthlyPayEmp;
		this.applyForDaylyPayEmp = applyForDaylyPayEmp;
		this.applyForHourlyPayEmp = applyForHourlyPayEmp;
		this.avePayAtr = avePayAtr;
		this.errRangeLowAtr = errRangeLowAtr;
		this.errRangeLow = errRangeLow;
		this.errRangeHighAtr = errRangeHighAtr;
		this.errRangeHigh = errRangeHigh;
		this.alRangeLowAtr = alRangeLowAtr;
		this.alRangeLow = alRangeLow;
		this.alRangeHighAtr = alRangeHighAtr;
		this.alRangeHigh = alRangeHigh;
		this.memo = memo;
		this.limitMnyAtr = limitMnyAtr;
		this.limitMnyRefItemCode = limitMnyRefItemCode;
		this.limitMny = limitMny;
	}

	public static ItemSalary createFromJavaType(String itemCd, int taxAtr, int socialInsAtr, int laborInsAtr,
			int fixPayAtr, int applyForAllEmpFlg, int applyForMonthlyPayEmp, int applyForDaymonthlyPayEmp,
			int applyForDaylyPayEmp, int applyForHourlyPayEmp, int avePayAtr, int errRangeLowAtr,
			BigDecimal errRangeLow, int errRangeHighAtr, BigDecimal errRangeHigh, int alRangeLowAtr,
			BigDecimal alRangeLow, int alRangeHighAtr, BigDecimal alRangeHigh, String memo, int limitMnyAtr,
			String limitMnyRefItemCd, BigDecimal limitMny) {

		return new ItemSalary(new ItemCode(itemCd), EnumAdaptor.valueOf(taxAtr, TaxAtr.class),
				EnumAdaptor.valueOf(socialInsAtr, InsAtr.class), EnumAdaptor.valueOf(laborInsAtr, InsAtr.class),
				EnumAdaptor.valueOf(fixPayAtr, FixPayAtr.class), EnumAdaptor.valueOf(applyForAllEmpFlg, ApplyFor.class),
				EnumAdaptor.valueOf(applyForMonthlyPayEmp, ApplyFor.class),
				EnumAdaptor.valueOf(applyForDaymonthlyPayEmp, ApplyFor.class),
				EnumAdaptor.valueOf(applyForDaylyPayEmp, ApplyFor.class),
				EnumAdaptor.valueOf(applyForHourlyPayEmp, ApplyFor.class),
				EnumAdaptor.valueOf(avePayAtr, AvePayAtr.class), EnumAdaptor.valueOf(errRangeLowAtr, RangeAtr.class),
				errRangeLow == null ? null : new ErrRangeLow(errRangeLow),
				EnumAdaptor.valueOf(errRangeHighAtr, RangeAtr.class),
				errRangeHigh == null ? null : new ErrRangeHigh(errRangeHigh),
				EnumAdaptor.valueOf(alRangeLowAtr, RangeAtr.class),
				alRangeLow == null ? null : new AlRangeLow(alRangeLow),
				EnumAdaptor.valueOf(alRangeHighAtr, RangeAtr.class),
				alRangeHigh == null ? null : new AlRangeHigh(alRangeHigh)
				, new Memo(memo), EnumAdaptor.valueOf(limitMnyAtr, LimitMnyAtr.class),
				new LimitMnyRefItemCd(limitMnyRefItemCd), limitMny == null ? null : new LimitMny(limitMny));

	}

	/**
	 * Set new value ave payment attribute
	 * 
	 * @param value
	 */
	public void setAvePayAttribute(AvePayAtr avePayAtr) {
		this.avePayAtr = avePayAtr;
	}
}
