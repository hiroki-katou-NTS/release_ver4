package nts.uk.ctx.pr.core.app.command.retirement.payment;

import java.util.Optional;

import javax.ejb.Stateless;
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
@Stateless
@Transactional
public class RemoveRetirementPaymentCommandHandler extends CommandHandler<RemoveRetirementPaymentCommand>{
	@Inject
	private RetirementPaymentRepository retirementPaymentRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemoveRetirementPaymentCommand> context) {

		RemoveRetirementPaymentCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		Optional<RetirementPayment> reOptional = this.retirementPaymentRepository.findRetirementPaymentInfo(
						new CompanyCode(companyCode), 
						new PersonId(command.getPersonId().toString()), 
						GeneralDate.fromString(command.getPayDate(), "yyyy/MM/dd"));
		if(!reOptional.isPresent()) {
			throw new RuntimeException("Item do not exist");
		}
		GeneralDate payDate = GeneralDate.fromString(command.getPayDate(), "yyyy/MM/dd");

		retirementPaymentRepository.remove(companyCode,command.getPersonId(), payDate);
	}
}
