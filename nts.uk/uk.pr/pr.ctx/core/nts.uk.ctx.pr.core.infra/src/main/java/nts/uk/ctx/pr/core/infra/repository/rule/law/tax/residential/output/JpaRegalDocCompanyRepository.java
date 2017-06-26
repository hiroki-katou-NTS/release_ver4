package nts.uk.ctx.pr.core.infra.repository.rule.law.tax.residential.output;

import java.util.List;
import javax.ejb.Stateless;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.RegalDocCompany;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.RegalDocCompanyRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QcpmtRegalDocCom;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output.QcpmtRegalDocComPK;

@Stateless
public class JpaRegalDocCompanyRepository extends JpaRepository implements RegalDocCompanyRepository {
	private final String SEL_1 = "SELECT c FROM QcpmtRegalDocCom c WHERE c.qcpmtRegalDocComPK.ccd = :companyCd";

	private static QcpmtRegalDocCom toEntity(RegalDocCompany domain) {
		QcpmtRegalDocCom entity = new QcpmtRegalDocCom();
		entity.qcpmtRegalDocComPK = new QcpmtRegalDocComPK(domain.getCompanyCode().v(),
				domain.getReganDocCompanyCode());
		entity.regalDocCname = domain.getReganDocCompanyName();
		entity.regalDocCnameSjis = domain.getReganDocCompanyNameForStatutory();
		entity.regalDocAbCname = domain.getReganDocCompanyNameForStatutoryAbbreviation();
		entity.telNo = domain.getCorporateNumber();
		entity.presidentName = domain.getCompanyRepresentativeName();
		entity.presidentTitle = domain.getCompanyRepresentativeTitle();
		entity.linkDepcd = domain.getBindingDepartmentCode();
		entity.payerPostal = domain.getSalaryPayerZipCode();
		entity.payerAddress1 = domain.getSalaryPayerAddress1();
		entity.payerAddress2 = domain.getSalaryPayerAddress2();
		entity.payerKnAddress1 = domain.getSalaryPayerAddressKana1();
		entity.payerKnAddress2 = domain.getSalaryPayerAddressKana2();
		entity.payerTelNo = domain.getSalaryPayerTelephoneNumber();
		entity.accountingOfficer = domain.getAccountantFullName();
		entity.liaisonDep = domain.getContactPersonBelongingSectionStaff();
		entity.liaisonName = domain.getContactName();
		entity.liaisonTelNo = domain.getContactPhoneNumber();
		entity.accountingFirm = domain.getAccountingOfficeName();
		entity.accountingFirmTelNo = domain.getAccountingBusinessPhoneNumber();
		entity.paymentMethod1 = domain.getSalaryPaymentMethod1();
		entity.paymentMethod2 = domain.getSalaryPaymentMethod2();
		entity.paymentMethod3 = domain.getSalaryPaymentMethod3();
		entity.bizType1 = domain.getBusinessLine1();
		entity.bizType2 = domain.getBusinessLine2();
		entity.bizType3 = domain.getBusinessLine3();
		entity.taxOffice = domain.getJurisdictionTaxOffice();
		entity.bankName = domain.getNameOfFinancialInstitution();
		entity.bankAddress = domain.getFinancialInstitutionLocation();
		entity.payerResiTaxReportCd = domain.getInhabitantTaxReportDestinationCode();
		entity.memo = domain.getNotes();
		return entity;
	}

	/**
	 * Convert to Domain
	 * @param entity
	 * @return
	 */
	private static RegalDocCompany toDomain(QcpmtRegalDocCom entity) {
		val domain = RegalDocCompany.createFromJavaType(entity.qcpmtRegalDocComPK.ccd,
				entity.qcpmtRegalDocComPK.regalDocCcd, entity.regalDocCname, entity.regalDocCnameSjis,
				entity.regalDocAbCname, entity.telNo, entity.presidentName, entity.presidentTitle, entity.linkDepcd,
				entity.payerPostal, entity.payerAddress1, entity.payerAddress2, entity.payerKnAddress1,
				entity.payerKnAddress2, entity.payerTelNo, entity.accountingOfficer, entity.liaisonDep,
				entity.liaisonName, entity.liaisonTelNo, entity.accountingFirm, entity.accountingFirmTelNo,
				entity.paymentMethod1, entity.paymentMethod2, entity.paymentMethod3, entity.bizType1, entity.bizType2,
				entity.bizType3, entity.taxOffice, entity.bankName, entity.bankAddress, entity.payerResiTaxReportCd,
				entity.memo);
		return domain;
	}

	/**
	 * Find All Regal Doc Company
	 */
	@Override
	public List<RegalDocCompany> findAll(String companyCode) {
		List<RegalDocCompany> list = this.queryProxy().query(SEL_1, QcpmtRegalDocCom.class)
				.setParameter("companyCd", companyCode).getList(c -> toDomain(c));
		return list;
	}
}
