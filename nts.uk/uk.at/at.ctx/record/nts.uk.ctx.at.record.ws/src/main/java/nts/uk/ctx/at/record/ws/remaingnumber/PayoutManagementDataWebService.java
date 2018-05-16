package nts.uk.ctx.at.record.ws.remaingnumber;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.record.app.command.remainingnumber.paymana1.DeletePayoutManagementDataCommand;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana1.DeletePayoutManagementDataCommandHandler;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana1.PayoutManagementDataCommand;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana1.UpdatePayoutManagementDataCommandHandler;
import nts.uk.ctx.at.record.app.find.remainingnumber.paymana.PayoutManagementDataDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.paymana.PayoutManagementDataFinder;

@Path("at/record/remainnumber/payoutmanagement")
@Produces("application/json")
public class PayoutManagementDataWebService {
   
	@Inject
    private DeletePayoutManagementDataCommandHandler deletePayout;
	
	@Inject
    private UpdatePayoutManagementDataCommandHandler updatePayout;
	
	@Inject
	private PayoutManagementDataFinder finder;

	@POST
	@Path("update")
	public void update(PayoutManagementDataCommand command){
		updatePayout.handle(command);
	}
	
	@POST
	@Path("delete")
	public void delete(DeletePayoutManagementDataCommand command){
		deletePayout.handle(command);
	}
	
	@POST
	@Path("getBysiDRemCod/{empId}/{state}")
	// get SubstitutionOfHDManagement by SID and remainsDays > 0
	public List<PayoutManagementDataDto> getBysiDRemCod(@PathParam("empId") String employeeId, @PathParam("state") int state) {
		return finder.getBysiDRemCod(employeeId, state);
	}
}
