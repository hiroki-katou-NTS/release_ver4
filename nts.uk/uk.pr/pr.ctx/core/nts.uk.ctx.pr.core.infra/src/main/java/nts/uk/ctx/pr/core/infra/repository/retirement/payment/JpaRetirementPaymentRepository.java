package nts.uk.ctx.pr.core.infra.repository.retirement.payment;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.retirement.payment.BankTransferOption;
import nts.uk.ctx.pr.core.dom.retirement.payment.PaymentMoney;
import nts.uk.ctx.pr.core.dom.retirement.payment.PaymentYear;
import nts.uk.ctx.pr.core.dom.retirement.payment.RetirementPayOption;
import nts.uk.ctx.pr.core.dom.retirement.payment.RetirementPayment;
import nts.uk.ctx.pr.core.dom.retirement.payment.RetirementPaymentRepository;
import nts.uk.ctx.pr.core.dom.retirement.payment.TaxCalculationMethod;
import nts.uk.ctx.pr.core.dom.retirement.payment.TrialPeriodSet;
import nts.uk.ctx.pr.core.infra.entity.retirement.payment.QredtRetirementPayment;
import nts.uk.ctx.pr.core.infra.entity.retirement.payment.QredtRetirementPaymentPK;
import nts.uk.shr.com.primitive.Memo;
import nts.uk.shr.com.primitive.PersonId;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@RequestScoped
public class JpaRetirementPaymentRepository extends JpaRepository implements RetirementPaymentRepository {
	
	private final String FindByCompanyCodeAndPersonId = "Select a From QredtRetirementPayment a "
											+ "where a.qredtRetirementPaymentPK.companyCode = :companyCode "
											+ "and a.qredtRetirementPaymentPK.personId = :personId ";
	
	@Override
	public void add(RetirementPayment retirementPayment) {
		this.commandProxy().insert(convertToEntity(retirementPayment));
	}
	
	@Override
	public List<RetirementPayment> findByCompanyCodeAndPersonId(CompanyCode companyCode, PersonId personId) {
		return this.queryProxy().query(FindByCompanyCodeAndPersonId, QredtRetirementPayment.class)
				.setParameter("companyCode", companyCode.v())
				.setParameter("personId", personId.v())
				.getList(x -> convertToDomain(x));
	}
	
	@Override
	public Optional<RetirementPayment> findRetirementPaymentInfo(CompanyCode companyCode, PersonId personId, GeneralDate dateTime) {
		return this.queryProxy().find(new QredtRetirementPaymentPK(companyCode.v(), personId.v(), dateTime), QredtRetirementPayment.class)
				.map(x -> convertToDomain(x));
	}
	
	@Override
	public void update(RetirementPayment retirementPayment) {
		this.commandProxy().update(convertToEntity(retirementPayment));
	}
	
	@Override
	public void remove(RetirementPayment retirementPayment) {
		this.commandProxy().remove(
				QredtRetirementPayment.class,
				new QredtRetirementPaymentPK(
						retirementPayment.getCompanyCode().v(), 
						retirementPayment.getPersonId().v(),
						retirementPayment.getPayDate()
				)
		);
	}
	
	/**
	 * Convert Domain Item to Entity Item
	 * @param retirementPayment Domain Item
	 * @return Entiry Item
	 */
	private QredtRetirementPayment convertToEntity(RetirementPayment retirementPayment){
		return new QredtRetirementPayment(
					new QredtRetirementPaymentPK(
							retirementPayment.getCompanyCode().v(), 
							retirementPayment.getPersonId().v(), 
							retirementPayment.getPayDate()),
					retirementPayment.getTrialPeriodSet().value,
					retirementPayment.getExclusionYears().v(),
					retirementPayment.getAdditionalBoardYears().v(),
					retirementPayment.getBoardYears().v(),
					retirementPayment.getTotalPaymentMoney().v(),
					retirementPayment.getDeduction1Money().v(),
					retirementPayment.getDeduction2Money().v(),
					retirementPayment.getDeduction3Money().v(),
					retirementPayment.getRetirementPayOption().value,
					retirementPayment.getTaxCalculationMethod().value,
					retirementPayment.getIncomeTaxMoney().v(),
					retirementPayment.getCityTaxMoney().v(),
					retirementPayment.getPrefectureTaxMoney().v(),
					retirementPayment.getTotalDeclarationMoney().v(),
					retirementPayment.getActualRecieveMoney().v(),
					retirementPayment.getBankTransferOption1().value,
					retirementPayment.getOption1Money().v(),
					retirementPayment.getBankTransferOption2().value,
					retirementPayment.getOption2Money().v(),
					retirementPayment.getBankTransferOption3().value,
					retirementPayment.getOption3Money().v(),
					retirementPayment.getBankTransferOption4().value,
					retirementPayment.getOption4Money().v(),
					retirementPayment.getBankTransferOption5().value,
					retirementPayment.getOption5Money().v(),
					retirementPayment.getWithholdingMeno().v(),
					retirementPayment.getStatementMemo().v()
				);
	}
	
	/**
	 * Convert Entity Item to Domain Item
	 * @param qredtRetirementPayment Entity Item
	 * @return Domain Item
	 */
	private RetirementPayment convertToDomain(QredtRetirementPayment qredtRetirementPayment){
		return new RetirementPayment(
				new CompanyCode(qredtRetirementPayment.qredtRetirementPaymentPK.companyCode), 
				new PersonId(qredtRetirementPayment.qredtRetirementPaymentPK.personId), 
				qredtRetirementPayment.qredtRetirementPaymentPK.payDate,
				EnumAdaptor.valueOf(qredtRetirementPayment.trialPeriodSet, TrialPeriodSet.class),
				new PaymentYear(qredtRetirementPayment.exclusionYears),
				new PaymentYear(qredtRetirementPayment.additionalBoardYears), 
				new PaymentYear(qredtRetirementPayment.boardYears), 
				new PaymentMoney(qredtRetirementPayment.totalPaymentMoney),
				new PaymentMoney(qredtRetirementPayment.deduction1Money), 
				new PaymentMoney(qredtRetirementPayment.deduction2Money), 
				new PaymentMoney(qredtRetirementPayment.deduction3Money), 
				EnumAdaptor.valueOf(qredtRetirementPayment.retirementPayOption, RetirementPayOption.class), 
				EnumAdaptor.valueOf(qredtRetirementPayment.taxCalculationMethod, TaxCalculationMethod.class), 
				new PaymentMoney(qredtRetirementPayment.incomeTaxMoney),
				new PaymentMoney(qredtRetirementPayment.cityTaxMoney), 
				new PaymentMoney(qredtRetirementPayment.prefectureTaxMoney), 
				new PaymentMoney(qredtRetirementPayment.totalDeclarationMoney), 
				new PaymentMoney(qredtRetirementPayment.actualRecieveMoney), 
				EnumAdaptor.valueOf(qredtRetirementPayment.bankTransferOption1, BankTransferOption.class),
				new PaymentMoney(qredtRetirementPayment.option1Money),
				EnumAdaptor.valueOf(qredtRetirementPayment.bankTransferOption2, BankTransferOption.class),
				new PaymentMoney(qredtRetirementPayment.option2Money),
				EnumAdaptor.valueOf(qredtRetirementPayment.bankTransferOption3, BankTransferOption.class),
				new PaymentMoney(qredtRetirementPayment.option3Money),
				EnumAdaptor.valueOf(qredtRetirementPayment.bankTransferOption4, BankTransferOption.class),
				new PaymentMoney(qredtRetirementPayment.option4Money),
				EnumAdaptor.valueOf(qredtRetirementPayment.bankTransferOption5, BankTransferOption.class),
				new PaymentMoney(qredtRetirementPayment.option5Money),
				new Memo(qredtRetirementPayment.withholdingMeno),
				new Memo(qredtRetirementPayment.statementMemo));
	}
}
