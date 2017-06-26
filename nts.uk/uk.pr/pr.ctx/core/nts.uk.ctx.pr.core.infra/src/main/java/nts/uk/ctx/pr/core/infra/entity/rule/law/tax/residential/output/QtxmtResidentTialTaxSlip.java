package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "QTXMT_RESIDENTIAL_TAXSLIP")
@Entity
public class QtxmtResidentTialTaxSlip extends UkJpaEntity {
	@EmbeddedId
	public QtxmtResidentTialTaxSlipPk qtxmtResimentTialTaxSlipPk;
	
	@Column(name = "TAX_PAYROLL_MNY")
	public BigDecimal taxPayRollMoney;
	
	@Column(name = "TAX_RETIREMENT_MNY")
	public BigDecimal taxBonusMoney;
	
	@Column(name = "TAX_OVERDUE_MNY")
	public BigDecimal taxOverDueMoney;
	
	@Column(name = "TAX_DEMAND_CHARGE_MNY")
	public BigDecimal taxDemandChargeMoyney;
	
	@Column(name = "ADDRESS")
	public String address;
	
	@Column(name = "DUE_DATE")
	@Temporal(TemporalType.DATE)
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate dueDate;
	
	@Column(name = "HEADCOUNT")
	public BigDecimal headcount;
	
	@Column(name = "RETIREMENT_BONUS_AMOUNT")
	public BigDecimal retirementBonusAmout;
	
	@Column(name = "CITY_TAX_MNY")
	public BigDecimal cityTaxMoney;
	
	@Column(name = "PREFECTURE_TAX_MNY")
	public BigDecimal prefectureTaxMoney;

	@Override
	protected Object getKey() {
		return qtxmtResimentTialTaxSlipPk;
	}
}
