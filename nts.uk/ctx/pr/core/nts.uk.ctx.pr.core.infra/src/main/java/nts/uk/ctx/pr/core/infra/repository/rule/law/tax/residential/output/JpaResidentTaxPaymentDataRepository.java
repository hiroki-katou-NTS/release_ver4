package nts.uk.ctx.pr.core.infra.repository.rule.law.tax.residential.output;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentData;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentDataRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QtxmtResidentTialTaxSlip;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QtxmtResidentTialTaxSlipPk;

@RequestScoped
public class JpaResidentTaxPaymentDataRepository extends JpaRepository implements ResidentTaxPaymentDataRepository {

	@Override
	public Optional<ResidentTaxPaymentData> find(String companyCode, String residentTaxCode, int yearMonth) {
		
		QtxmtResidentTialTaxSlipPk primaryKey = new QtxmtResidentTialTaxSlipPk(companyCode, residentTaxCode, yearMonth);
		
		return this.queryProxy().find(primaryKey, QtxmtResidentTialTaxSlip.class)
				.map(x -> ResidentTaxPaymentData.createFromJavaType(
						x.qtxmtResimentTialTaxSlipPk.residentTaxCode, 
						x.taxPayRollMoney, 
						x.taxBonusMoney, 
						x.taxOverDueMoney,
						x.taxDemandChargeMoyney, 
						x.address, 
						x.dueDate, 
						x.headcount, 
						x.retirementBonusAmout, 
						x.cityTaxMoney, 
						x.prefectureTaxMoney,
						x.qtxmtResimentTialTaxSlipPk.yearMonth));
	}

	@Override
	public void add(String companyCode, ResidentTaxPaymentData domain) {
		QtxmtResidentTialTaxSlip entity = toEntity(companyCode, domain);
		this.commandProxy().insert(entity);
	}

	@Override
	public void update(String companyCode, ResidentTaxPaymentData domain) {
		QtxmtResidentTialTaxSlip entity = toEntity(companyCode, domain);
		this.commandProxy().update(entity);
	}
	
	private QtxmtResidentTialTaxSlip toEntity(String companyCode, ResidentTaxPaymentData domain) {
		QtxmtResidentTialTaxSlipPk qtxmtResimentTialTaxSlipPk = new QtxmtResidentTialTaxSlipPk(companyCode, domain.getCode().v(), domain.getYearMonth().v());
		
		QtxmtResidentTialTaxSlip entity = new QtxmtResidentTialTaxSlip(
				qtxmtResimentTialTaxSlipPk, 
				domain.getTaxPayrollMoney().v(), 
				domain.getTaxBonusMoney().v(), 
				domain.getTaxOverdueMoney().v(), 
				domain.getTaxDemandChargeMoney().v(), 
				domain.getAddress().v(), 
				domain.getDueDate(), 
				domain.getStaffNo().v(),
				domain.getRetirementAmount().v(), 
				domain.getCityTaxMoney().v(), 
				domain.getPrefectureTaxMoney().v());
		return entity;
	}

}
