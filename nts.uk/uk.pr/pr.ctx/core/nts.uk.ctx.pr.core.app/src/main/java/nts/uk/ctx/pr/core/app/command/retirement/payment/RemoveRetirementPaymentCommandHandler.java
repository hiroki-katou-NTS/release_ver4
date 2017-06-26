package nts.uk.ctx.pr.core.app.command.retirement.payment;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
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
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;
import nts.uk.shr.com.primitive.PersonId;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@RequestScoped
@Transactional
public class RemoveRetirementPaymentCommandHandler extends CommandHandler<RemoveRetirementPaymentCommand>{
	@Inject
	private RetirementPaymentRepository retirementPaymentRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemoveRetirementPaymentCommand> context) {
		// TODO Auto-generated method stub
		RemoveRetirementPaymentCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		Optional<RetirementPayment> reOptional = this.retirementPaymentRepository.findRetirementPaymentInfo(
						new CompanyCode(companyCode), 
						new PersonId(command.getPersonId().toString()), 
						GeneralDate.fromString(command.getPayDate(), "yyyy/MM/dd"));
		if(!reOptional.isPresent()) {
			throw new RuntimeException("Item do not exist");
		}
		RetirementPayment retirementPayment = new RetirementPayment(
				new CompanyCode(companyCode), 
				new PersonId(command.getPersonId()), 
				GeneralDate.fromString(command.getPayDate(), "yyyy/MM/dd"),
				EnumAdaptor.valueOf(command.getTrialPeriodSet(), TrialPeriodSet.class),
				new PaymentYear(command.getExclusionYears()), 
				new PaymentYear(command.getAdditionalBoardYears()), 
				new PaymentYear(command.getBoardYears()), 
				new PaymentMoney(command.getTotalPaymentMoney()),
				new PaymentMoney(command.getDeduction1Money()), 
				new PaymentMoney(command.getDeduction2Money()), 
				new PaymentMoney(command.getDeduction3Money()), 
				EnumAdaptor.valueOf(command.getRetirementPayOption(), RetirementPayOption.class), 
				EnumAdaptor.valueOf(command.getTaxCalculationMethod(), TaxCalculationMethod.class), 
				new PaymentMoney(command.getIncomeTaxMoney()), 
				new PaymentMoney(command.getCityTaxMoney()), 
				new PaymentMoney(command.getPrefectureTaxMoney()), 
				new PaymentMoney(command.getTotalDeclarationMoney()),  
				new PaymentMoney(command.getActualRecieveMoney()), 
				EnumAdaptor.valueOf(command.getBankTransferOption1(), BankTransferOption.class),
				new PaymentMoney(command.getOption1Money()),
				EnumAdaptor.valueOf(command.getBankTransferOption2(), BankTransferOption.class),
				new PaymentMoney(command.getOption2Money()),
				EnumAdaptor.valueOf(command.getBankTransferOption3(), BankTransferOption.class),
				new PaymentMoney(command.getOption3Money()),
				EnumAdaptor.valueOf(command.getBankTransferOption4(), BankTransferOption.class),
				new PaymentMoney(command.getOption4Money()),
				EnumAdaptor.valueOf(command.getBankTransferOption5(), BankTransferOption.class),
				new PaymentMoney(command.getOption5Money()),
				new Memo(command.getWithholdingMeno()),
				new Memo(command.getStatementMemo()));
		retirementPaymentRepository.remove(retirementPayment);
	}
}
