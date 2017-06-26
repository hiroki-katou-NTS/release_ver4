package nts.uk.pr.file.infra.residentialtax;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.infra.entity.company.CmnmtCompany;
import nts.uk.ctx.basic.infra.entity.company.CmnmtCompanyPK;
import nts.uk.ctx.basic.infra.entity.report.CmnmtCalled;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QcpmtRegalDocCom;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QcpmtRegalDocComPK;
import nts.uk.file.pr.app.export.residentialtax.ResidentialTaxReportRepository;
import nts.uk.file.pr.app.export.residentialtax.data.CompanyDto;
import nts.uk.file.pr.app.export.residentialtax.data.PaymentDetailDto;
import nts.uk.file.pr.app.export.residentialtax.data.PersonResitaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxSlipDto;
import nts.uk.file.pr.app.export.residentialtax.data.RetirementPaymentDto;

@Stateless
public class JpaResidentialTaxReportRepository extends JpaRepository implements ResidentialTaxReportRepository {

	//private String CMNMT_COMPANY_SEL_3 = "SELECT e FROM ReportCmnmtCompany e WHERE e.cmnmtCompanyPk.companyCd = :companyCd";
	private String QTXMT_RESIDENTIAL_TAX_SEL_4 = "SELECT NEW " + ResidentialTaxDto.class.getName() + ""
			+ "(c.qtxmtResidentialTaxPk.resiTaxCode, c.resiTaxAutonomy, c.registeredName, c.companyAccountNo, c.companySpecifiedNo, c.cordinatePostalCode, c.cordinatePostOffice, c.resiTaxAutonomyKana) "
			+ "FROM QtxmtResidentialTax c WHERE c.qtxmtResidentialTaxPk.companyCd =:companyCd AND c.qtxmtResidentialTaxPk.resiTaxCode IN :resiTaxCode";
	private String QTXMT_RESIDENTIAL_TAXSLIP_SEL_2 = "SELECT NEW " + ResidentialTaxSlipDto.class.getName() + ""
			+ "(a.qtxmtResimentTialTaxSlipPk.residentTaxCode, a.qtxmtResimentTialTaxSlipPk.yearMonth, a.taxPayRollMoney, a.taxBonusMoney, a.taxOverDueMoney, a.taxDemandChargeMoyney, a.address, a.dueDate, a.headcount, a.retirementBonusAmout, a.cityTaxMoney, a.prefectureTaxMoney) "
			+ "FROM QtxmtResidentTialTaxSlip a WHERE a.qtxmtResimentTialTaxSlipPk.companyCode =:companyCd AND a.qtxmtResimentTialTaxSlipPk.yearMonth =:yearMonth AND a.qtxmtResimentTialTaxSlipPk.residentTaxCode IN :resiTaxCode";

	private String PPRMT_PERSON_RESITAX_SEL_5 = "SELECT NEW " + PersonResitaxDto.class.getName() + ""
			+ "(b.pprmtPersonResiTaxPK.personId, b.residenceCode)" + "FROM PprmtPersonResiTax b "
			+ "WHERE b.pprmtPersonResiTaxPK.companyCode = :companyCd" + " AND b.residenceCode IN :residenceCode"
			+ " AND b.pprmtPersonResiTaxPK.yearKey = :yearKey";

	private String QSTDT_PAYMENT_DETAIL_SEL_6 = "SELECT NEW " + PaymentDetailDto.class.getName() + ""
			+ "(d.qstdtPaymentDetailPK.personId, d.qstdtPaymentDetailPK.categoryATR, d.qstdtPaymentDetailPK.itemCode, d.value, d.printLinePosition, d.columnPosition)"
			+ "FROM QstdtPaymentDetail d " 
			+ "WHERE d.qstdtPaymentDetailPK.companyCode =:companyCd "
			+ "AND d.qstdtPaymentDetailPK.personId IN :personId "
			+ "AND d.qstdtPaymentDetailPK.payBonusAttribute =:payBonusAttribute "
			+ "AND d.qstdtPaymentDetailPK.processingYM =:processingYM AND d.qstdtPaymentDetailPK.categoryATR =:categoryATR AND d.qstdtPaymentDetailPK.itemCode =:itemCode";

	private String QREDT_RETIREMENT_PAYMENT_SEL_2 = "SELECT NEW " + RetirementPaymentDto.class.getName() + ""
			+ "(z.qredtRetirementPaymentPK.personId, z.qredtRetirementPaymentPK.payDate, z.trialPeriodSet, z.exclusionYears, z.additionalBoardYears, z.boardYears, z.totalPaymentMoney, z.deduction1Money, z.deduction2Money, z.deduction3Money, z.retirementPayOption, z.taxCalculationMethod, z.incomeTaxMoney, z.cityTaxMoney, z.prefectureTaxMoney, z.totalDeclarationMoney, z.actualRecieveMoney, z.withholdingMeno)"
			+ "FROM QredtRetirementPayment z "
			+ "WHERE z.qredtRetirementPaymentPK.companyCode =:companyCode "
			+ "AND z.qredtRetirementPaymentPK.personId IN :personId "
			+ "AND z.qredtRetirementPaymentPK.payDate >=:StartYearMonth "
			+ "AND z.qredtRetirementPaymentPK.payDate <=:EndYearMonth";
	
	private String CMNMT_CALLED = "SELECT b.person FROM CmnmtCalled b WHERE b.ccd = :companyCode ";

	@Override
	public List<ResidentialTaxDto> findResidentTax(String companyCode, List<String> residentTaxCodeList) {
		return this.queryProxy().query(QTXMT_RESIDENTIAL_TAX_SEL_4, ResidentialTaxDto.class)
				.setParameter("companyCd", companyCode).setParameter("resiTaxCode", residentTaxCodeList).getList();
	}

	@Override
	public CompanyDto findCompany(String companyCode) {
		CmnmtCompanyPK key = new CmnmtCompanyPK(companyCode);
		return this.queryProxy().find(key, CmnmtCompany.class)
				.map(x-> new CompanyDto(x.cmnmtCompanyPk.companyCd, x.cName, x.postal, x.address1, x.address2)).get();
	}

	@Override
	public CompanyDto findRegalCompany(String companyCode, String regalDocCompanyCode) {
		QcpmtRegalDocComPK key = new QcpmtRegalDocComPK(companyCode, regalDocCompanyCode);
		return this.queryProxy().find(key, QcpmtRegalDocCom.class)
				.map(x -> new CompanyDto(companyCode, x.regalDocCname, String.valueOf(x.telNo), x.payerAddress1, x.payerAddress2))
				.get();
	}

	@Override
	public List<ResidentialTaxSlipDto> findResidentialTaxSlip(String companyCode, int yearMonth,
			List<String> residentTaxCodeList) {
		return this.queryProxy().query(QTXMT_RESIDENTIAL_TAXSLIP_SEL_2, ResidentialTaxSlipDto.class)
				.setParameter("companyCd", companyCode).setParameter("yearMonth", yearMonth)
				.setParameter("resiTaxCode", residentTaxCodeList).getList();
	}

	@Override
	public List<PaymentDetailDto> findPaymentDetail(String companyCode, List<String> personIdList,
			PayBonusAtr payBonusAttribute, int processingYearMonth, CategoryAtr categoryATR, String itemCode) {
		return this.queryProxy().query(QSTDT_PAYMENT_DETAIL_SEL_6, PaymentDetailDto.class)
				.setParameter("companyCd", companyCode)
				.setParameter("personId", personIdList)
				.setParameter("payBonusAttribute", payBonusAttribute.value)
				.setParameter("processingYM", processingYearMonth).setParameter("categoryATR", categoryATR.value)
				.setParameter("itemCode", itemCode).getList();
	}

	@Override
	public List<RetirementPaymentDto> findRetirementPaymentList(String companyCode, List<String> personIdList,
			GeneralDate baseRangeStartYearMonth, GeneralDate baseRangeEndYearMonth) {
		return this.queryProxy()
				.query(QREDT_RETIREMENT_PAYMENT_SEL_2, RetirementPaymentDto.class)
		        .setParameter("companyCode", companyCode)
		        .setParameter("personId", personIdList)
		        .setParameter("StartYearMonth", baseRangeStartYearMonth)
		        .setParameter("EndYearMonth", baseRangeEndYearMonth)
		        .getList();	        
	}

	@Override
	public List<PersonResitaxDto> findPersonResidentTax(String companyCode, int yearMonth,
			List<String> residentTaxCodeList) {
		return this.queryProxy().query(PPRMT_PERSON_RESITAX_SEL_5, PersonResitaxDto.class)
				.setParameter("companyCd", companyCode)
				.setParameter("residenceCode", residentTaxCodeList)
				.setParameter("yearKey", yearMonth)
				.getList();
	}

	@Override
	public Optional<String> findPersonText(String companyCode) {	
		return this.queryProxy().query(CMNMT_CALLED, String.class )
				.setParameter("companyCode", companyCode)
				.getSingle();
	}
}
