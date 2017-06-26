package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.ApplySetting;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.SettingType;
import nts.uk.shr.com.primitive.Memo;

public class PersonalUnitPrice extends AggregateRoot {
	@Getter
	private String CompanyCode;

	/**
	 * Personal Unit Price
	 */
	@Getter
	private PersonalUnitPriceCode personalUnitPriceCode;

	/**
	 * Personal Unit Price Name
	 */
	@Getter
	private PersonalUnitPriceName personalUnitPriceName;

	@Getter
	private PersonalUnitPriceShortName personalUnitPriceAbName;

	/**
	* 
	*/
	@Getter
	private UniteCode uniteCode;

	@Getter
	private Memo memo;

	@Getter
	private ApplySetting fixPaymentAtr;

	@Getter
	private ApplySetting fixPaymentMonthly;

	@Getter
	private ApplySetting fixPaymentDayMonth;

	@Getter
	private ApplySetting fixPaymentDaily;

	@Getter
	private ApplySetting fixPaymentHoursly;

	@Getter
	private DisplaySet displaySet;

	@Getter
	private SettingType paymentSettingType;

	@Getter
	private UnitpriceAtr unitPriceAtr;

	@Override
	public void validate() {
		super.validate();
		
		if(this.personalUnitPriceCode == null || StringUtil.isNullOrEmpty(this.personalUnitPriceCode.v(), true)) { 
			throw new BusinessException("ER001");
		}
		
		if(this.personalUnitPriceName == null || StringUtil.isNullOrEmpty(this.personalUnitPriceName.v(), true)) { 
			throw new BusinessException("ER001");
		}
	}
	
	public PersonalUnitPrice(String companyCode, PersonalUnitPriceCode personalUnitPriceCode,
			PersonalUnitPriceName personalUnitPriceName, PersonalUnitPriceShortName personalUnitPriceAbName,
			UniteCode uniteCode, Memo memo, ApplySetting fixPaymentAtr, ApplySetting fixPaymentMonthly,
			ApplySetting fixPaymentDayMonth, ApplySetting fixPaymentDaily, ApplySetting fixPaymentHoursly,
			DisplaySet displaySet, SettingType paymentSettingType, UnitpriceAtr unitPriceAtr) {
		super();
		CompanyCode = companyCode;
		this.personalUnitPriceCode = personalUnitPriceCode;
		this.personalUnitPriceName = personalUnitPriceName;
		this.personalUnitPriceAbName = personalUnitPriceAbName;
		this.uniteCode = uniteCode;
		this.memo = memo;
		this.fixPaymentAtr = fixPaymentAtr;
		this.fixPaymentMonthly = fixPaymentMonthly;
		this.fixPaymentDayMonth = fixPaymentDayMonth;
		this.fixPaymentDaily = fixPaymentDaily;
		this.fixPaymentHoursly = fixPaymentHoursly;
		this.displaySet = displaySet;
		this.paymentSettingType = paymentSettingType;
		this.unitPriceAtr = unitPriceAtr;
	}
    
	public static PersonalUnitPrice createFromJavaType(String companyCode,String personalUnitPriceCode,String personalUnitPriceName,String personalUnitPriceAbName,
			                                           String uniteCode,String memo,int fixPaymentAtr,int fixPaymentMonthly,int fixPaymentDayMonth,int fixPaymentDaily,
			                                           int fixPaymentHoursly,int displaySet,int paymentSettingType,int unitPriceAtr){
         return new PersonalUnitPrice(
        		 companyCode, 
        		 new PersonalUnitPriceCode(personalUnitPriceCode), 
        		 new PersonalUnitPriceName(personalUnitPriceName), 
        		 new PersonalUnitPriceShortName(personalUnitPriceAbName),
        		 new UniteCode(uniteCode),
        		 new Memo(memo),
        		 EnumAdaptor.valueOf(fixPaymentAtr, ApplySetting.class),
        		 EnumAdaptor.valueOf(fixPaymentMonthly, ApplySetting.class),
        		 EnumAdaptor.valueOf(fixPaymentDayMonth, ApplySetting.class),
        		 EnumAdaptor.valueOf(fixPaymentDaily, ApplySetting.class),
        		 EnumAdaptor.valueOf(fixPaymentHoursly, ApplySetting.class),
        		 EnumAdaptor.valueOf(displaySet, DisplaySet.class),
        		 EnumAdaptor.valueOf(paymentSettingType, SettingType.class),
        		 EnumAdaptor.valueOf(unitPriceAtr, UnitpriceAtr.class));
	}	   
}
