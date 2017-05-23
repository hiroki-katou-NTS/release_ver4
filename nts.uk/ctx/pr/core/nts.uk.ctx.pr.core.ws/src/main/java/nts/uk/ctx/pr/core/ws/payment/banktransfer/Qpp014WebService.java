package nts.uk.ctx.pr.core.ws.payment.banktransfer;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.payment.banktransfer.AddBankTransferCommand;
import nts.uk.ctx.pr.core.app.command.payment.banktransfer.AddBankTransferCommandHandler;
import nts.uk.ctx.pr.core.app.command.payment.banktransfer.RemoveBankTransferCommandHandler;
import nts.uk.ctx.pr.core.app.find.payment.banktransfer.BankTransferFinder;
import nts.uk.ctx.pr.core.app.find.payment.banktransfer.BankTransferDto;
import nts.uk.shr.find.employment.processing.yearmonth.IPaydayProcessingFinder;
import nts.uk.shr.find.employment.processing.yearmonth.PaydayProcessingDto;

@Path("pr/proto/payment/banktransfer")
@Produces("application/json")
public class Qpp014WebService extends WebService {

	@Inject
	private IPaydayProcessingFinder iPaydayProcessingFinder;

	@Inject
	private AddBankTransferCommandHandler addBankTransferCommandHandler;

	@Inject
	private RemoveBankTransferCommandHandler removeBankTransferCommandHandler;

	@Inject
	private BankTransferFinder bankTransferFinder;

	@POST
	@Path("findPayDayProcessing")
	public List<PaydayProcessingDto> findPayDayProcessing() {
		return iPaydayProcessingFinder.getPaydayProcessing();
	}

	@POST
	@Path("add")
	public void add(AddBankTransferCommand command) {
		this.addBankTransferCommandHandler.handle(command);
	}

	@POST
	@Path("remove")
	public void remove(AddBankTransferCommand command) {
		this.removeBankTransferCommandHandler.handle(command);
	}

	@POST
	@Path("findDataScreenD/{processingNo}")
	public BankTransferDto findData(@PathParam("processingNo") int processingNo) {
		return bankTransferFinder.findDataForScreenD(processingNo);
	}
}
