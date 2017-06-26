package nts.uk.ctx.pr.core.ws.retirement.payment;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import lombok.Data;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.retirement.payment.RegisterRetirementPaymentCommand;
import nts.uk.ctx.pr.core.app.command.retirement.payment.RegisterRetirementPaymentCommandHandler;
import nts.uk.ctx.pr.core.app.command.retirement.payment.RemoveRetirementPaymentCommand;
import nts.uk.ctx.pr.core.app.command.retirement.payment.RemoveRetirementPaymentCommandHandler;
import nts.uk.ctx.pr.core.app.command.retirement.payment.UpdateRetirementPaymentCommand;
import nts.uk.ctx.pr.core.app.command.retirement.payment.UpdateRetirementPaymentCommandHandler;
import nts.uk.ctx.pr.core.app.find.retirement.payment.RetirementPaymentFinder;
import nts.uk.ctx.pr.core.app.find.retirement.payment.dto.RetirementPaymentDto;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Path("pr/core/retirement/payment")
@Produces("application/json")
public class RetirementPaymentWebService extends WebService{
	@Inject
	private RegisterRetirementPaymentCommandHandler registerRetirementPaymentCommandHandler;
	
	@Inject
	private RetirementPaymentFinder retirementPaymentFinder;
	
	@Inject
	private UpdateRetirementPaymentCommandHandler updateRetirementPaymentCommandHandler;
	
	@Inject
	private RemoveRetirementPaymentCommandHandler removeRetirementPaymentCommandHandler;
	
	@Path("register")
	@POST
	public void register(RegisterRetirementPaymentCommand command){
		this.registerRetirementPaymentCommandHandler.handle(command);
	}
	
	@Path("findByCompanyCodeandPersonId/{personId}")
	@POST
	public List<RetirementPaymentDto> findByCompanyCodeandPersonId(@PathParam("personId") String personId){
		return this.retirementPaymentFinder.findByCompanyCodeAndPersonId(personId);
	}
	
	@Path("findRetirementPaymentInfo")
	@POST
	public RetirementPaymentDto findRetirementPaymentInfo(CommandData command){
		return this.retirementPaymentFinder.findRetirementPaymentInfo(command.getPersonId(), command.getDateTime());
	}
	
	@Path("update")
	@POST
	public void update(UpdateRetirementPaymentCommand command){
		this.updateRetirementPaymentCommandHandler.handle(command);
	}
	
	@Path("remove")
	@POST
	public void remove(RemoveRetirementPaymentCommand command){
		this.removeRetirementPaymentCommandHandler.handle(command);
	}
}

@Data
class CommandData {
	
	private String personId;
	private String dateTime;
	
	public CommandData(String personId, String dateTime) {
		super();
		this.personId = personId;
		this.dateTime = dateTime;
	}
	public CommandData() {
		super();
	}
}
