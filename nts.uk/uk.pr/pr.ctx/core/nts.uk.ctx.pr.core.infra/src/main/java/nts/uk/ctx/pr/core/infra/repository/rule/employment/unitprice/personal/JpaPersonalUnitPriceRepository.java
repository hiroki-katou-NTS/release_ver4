package nts.uk.ctx.pr.core.infra.repository.rule.employment.unitprice.personal;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPriceRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.personal.QupmtPUnitprice;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.personal.QupmtPUnitpricePK;

@RequestScoped
public class JpaPersonalUnitPriceRepository extends JpaRepository implements PersonalUnitPriceRepository{
    private final String SEL_1 = "SELECT a FROM QupmtPUnitprice a WHERE a.qupmtPUnitpricePK.companyCode = :companyCode ";
    private final String SEL_1_1 = "SELECT a FROM QupmtPUnitprice a WHERE a.qupmtPUnitpricePK.companyCode = :companyCode AND a.qupmtPUnitpricePK.personalUnitPriceCode IN :unitPriceCodeList ";
    
    @Override
    public void add(PersonalUnitPrice personalUnitPrice){
    	this.commandProxy().insert(toEntity(personalUnitPrice));
    }

	@Override
	public Optional<PersonalUnitPrice> find(String companyCode, String personalUnitPriceCode) {
		QupmtPUnitpricePK primaryKey = new QupmtPUnitpricePK(companyCode, personalUnitPriceCode);
		return this.queryProxy().find(primaryKey, QupmtPUnitprice.class)
				.map(x -> PersonalUnitPrice.createFromJavaType(
						x.qupmtPUnitpricePK.companyCode,
						x.qupmtPUnitpricePK.personalUnitPriceCode,
						x.personalUnitPriceName,
						x.personalUnitPriceShortName,
						x.uniteCode,
						x.memo,
						x.fixPaymentAtr,
						x.fixPaymentMonthly,
						x.fixPaymentDayMonth,
						x.fixPaymentDaily,
						x.fixPaymentHoursly,
						x.displaySet,
						x.paymentSettingType,
						x.unitPriceAtr));
	}

	@Override
	public List<PersonalUnitPrice> findAll (String companyCode) {
		return this.queryProxy().query(SEL_1, QupmtPUnitprice.class)
				.setParameter("companyCode", companyCode)
				.getList(x -> PersonalUnitPrice.createFromJavaType(
						x.qupmtPUnitpricePK.companyCode,
						x.qupmtPUnitpricePK.personalUnitPriceCode,
						x.personalUnitPriceName,
						x.personalUnitPriceShortName,
						x.uniteCode,
						x.memo,
						x.fixPaymentAtr,
						x.fixPaymentMonthly,
						x.fixPaymentDayMonth,
						x.fixPaymentDaily,
						x.fixPaymentHoursly,
						x.displaySet,
						x.paymentSettingType,
						x.unitPriceAtr));
	}
	
	@Override
	public List<PersonalUnitPrice> findAll(String companyCode, List<String> unitPriceCodeList) {
		return this.queryProxy().query(SEL_1_1, QupmtPUnitprice.class)
				.setParameter("companyCode", companyCode)
				.setParameter("unitPriceCodeList", unitPriceCodeList)
				.getList(x -> PersonalUnitPrice.createFromJavaType(
						x.qupmtPUnitpricePK.companyCode,
						x.qupmtPUnitpricePK.personalUnitPriceCode,
						x.personalUnitPriceName,
						x.personalUnitPriceShortName,
						x.uniteCode,
						x.memo,
						x.fixPaymentAtr,
						x.fixPaymentMonthly,
						x.fixPaymentDayMonth,
						x.fixPaymentDaily,
						x.fixPaymentHoursly,
						x.displaySet,
						x.paymentSettingType,
						x.unitPriceAtr));
	}

	@Override
	public void update(PersonalUnitPrice personalUnitPrice) {
		this.commandProxy().update(toEntity(personalUnitPrice));
		
	}

	@Override
	public void remove(String companyCode, String code) {
		QupmtPUnitpricePK key = new QupmtPUnitpricePK(companyCode, code);
		this.commandProxy().remove(QupmtPUnitprice.class, key);;
		
	}
	
	 private static QupmtPUnitprice toEntity(PersonalUnitPrice domain){
	    	QupmtPUnitpricePK key = new QupmtPUnitpricePK(domain.getCompanyCode(), domain.getPersonalUnitPriceCode().v());
	    	
	    	QupmtPUnitprice entity = new QupmtPUnitprice(
	    			key,
	    			domain.getPersonalUnitPriceName().v(), 
	    			domain.getPersonalUnitPriceAbName().v(),
	    			domain.getDisplaySet().value,
	    			domain.getUniteCode().v(),
	    			domain.getPaymentSettingType().value,
	    			domain.getFixPaymentAtr().value,
	    			domain.getFixPaymentMonthly().value,
	    			domain.getFixPaymentDayMonth().value,
	    			domain.getFixPaymentDaily().value,
	    			domain.getFixPaymentHoursly().value,
	    			domain.getUnitPriceAtr().value,
	    			domain.getMemo().v());
	    	return entity;
	    }
}
