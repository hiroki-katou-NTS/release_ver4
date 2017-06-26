package nts.uk.ctx.pr.core.app.find.retirement.payment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.app.find.retirement.payment.dto.RetirementPaymentDto;
import nts.uk.ctx.pr.core.dom.retirement.payment.RetirementPayment;
import nts.uk.ctx.pr.core.dom.retirement.payment.RetirementPaymentRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.PersonId;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@RequestScoped
@Transactional
public class RetirementPaymentFinder {
	
	@Inject
	private RetirementPaymentRepository	retirementPaymentRepository;
	
	/**
	 * find list Retirement Payment by Company Code and Person ID
	 * @param personId Person ID
	 * @return list Retirement Payment by COmpant Code and Person ID
	 */
	public List<RetirementPaymentDto> findByCompanyCodeAndPersonId(String personId) {
		String companyCode = AppContexts.user().companyCode();
		return this.retirementPaymentRepository.findByCompanyCodeAndPersonId(new CompanyCode(companyCode), new PersonId(personId))
				.stream()
				.map(x -> new RetirementPaymentDto(
						x.getCompanyCode().v(), 
						x.getPersonId().v(),
						x.getPayDate(),
						x.getTrialPeriodSet().value,
						x.getExclusionYears().v(), 
						x.getAdditionalBoardYears().v(), 
						x.getBoardYears().v(), 
						x.getTotalPaymentMoney().v(), 
						x.getDeduction1Money().v(), 
						x.getDeduction2Money().v(), 
						x.getDeduction3Money().v(), 
						x.getRetirementPayOption().value, 
						x.getTaxCalculationMethod().value, 
						x.getIncomeTaxMoney().v(), 
						x.getCityTaxMoney().v(), 
						x.getPrefectureTaxMoney().v(), 
						x.getTotalDeclarationMoney().v(), 
						x.getActualRecieveMoney().v(), 
						x.getBankTransferOption1().value,
						x.getOption1Money().v(),
						x.getBankTransferOption2().value,
						x.getOption2Money().v(),
						x.getBankTransferOption3().value,
						x.getOption3Money().v(),
						x.getBankTransferOption4().value,
						x.getOption4Money().v(),
						x.getBankTransferOption5().value,
						x.getOption5Money().v(),
						x.getWithholdingMeno().v(),
						x.getStatementMemo().v()))
				.collect(Collectors.toList());
	}
	
	/**
	 * get single Retirement Payment by Company Code, Person ID and Date Time
	 * @param personId Person ID
	 * @param dateTime Date Time
	 * @return single Retirement Payment by Company Code, Person ID and Date Time
	 */
	public RetirementPaymentDto findRetirementPaymentInfo(String personId, String dateTime) {
		String companyCode = AppContexts.user().companyCode();
		GeneralDate date = GeneralDate.fromString(dateTime, "yyyy/MM/dd");
		Optional<RetirementPayment> retirementPayment = this.retirementPaymentRepository.findRetirementPaymentInfo(
				new CompanyCode(companyCode), 
				new PersonId(personId), 
				date);
		if(!retirementPayment.isPresent()) { 
			return null; 
		}
		RetirementPayment x = retirementPayment.get();
		return new RetirementPaymentDto(
						x.getCompanyCode().v(), 
						x.getPersonId().v(),
						x.getPayDate(),
						x.getTrialPeriodSet().value,
						x.getExclusionYears().v(), 
						x.getAdditionalBoardYears().v(), 
						x.getBoardYears().v(), 
						x.getTotalPaymentMoney().v(), 
						x.getDeduction1Money().v(), 
						x.getDeduction2Money().v(), 
						x.getDeduction3Money().v(), 
						x.getRetirementPayOption().value, 
						x.getTaxCalculationMethod().value, 
						x.getIncomeTaxMoney().v(), 
						x.getCityTaxMoney().v(), 
						x.getPrefectureTaxMoney().v(), 
						x.getTotalDeclarationMoney().v(), 
						x.getActualRecieveMoney().v(), 
						x.getBankTransferOption1().value,
						x.getOption1Money().v(),
						x.getBankTransferOption2().value,
						x.getOption2Money().v(),
						x.getBankTransferOption3().value,
						x.getOption3Money().v(),
						x.getBankTransferOption4().value,
						x.getOption4Money().v(),
						x.getBankTransferOption5().value,
						x.getOption5Money().v(),
						x.getWithholdingMeno().v(),
						x.getStatementMemo().v());
	}
}
