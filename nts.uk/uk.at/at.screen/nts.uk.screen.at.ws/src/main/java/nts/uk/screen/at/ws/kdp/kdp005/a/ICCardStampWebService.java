package nts.uk.screen.at.ws.kdp.kdp005.a;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import lombok.AllArgsConstructor;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.kdp.kdp005.a.ICCardStampCommand;
import nts.uk.ctx.at.record.app.command.kdp.kdp005.a.ICCardStampCommandHandler;
import nts.uk.ctx.sys.gateway.app.command.loginkdp.TimeStampInputLoginDto;
import nts.uk.ctx.sys.gateway.app.command.loginkdp.TimeStampLoginCommand;
import nts.uk.screen.at.app.query.kdp.KDP005.a.ICCardStampFinder;
import nts.uk.screen.at.app.query.kdp.kdp003.f.AuthenStampEmployee;

/**
 * @author thanhpv
 *
 */
@Path("at/record/stamp/ICCardStamp")
@Produces("application/json")
public class ICCardStampWebService extends WebService {

	@Inject
	private ICCardStampCommandHandler commandHanler;
	
	@Inject
	private ICCardStampFinder iCCardStampFinder;
	
	@Inject
	private AuthenStampEmployee authenStampEmployee;

	@POST
	@Path("checks")
	public GeneralDate registerICCardStamp(ICCardStampCommand command) {
		return this.commandHanler.handle(command);
	}
	
	@POST
	@Path("getEmployeeIdByICCard")
	public EmployeeIdDTO getEmployeeIdByICCard(CardNumberParam param) {
		Optional<String> employeeId = this.iCCardStampFinder.getEmployeeIdByICCard(param.cardNumber);
		if(employeeId.isPresent()) {
			return new EmployeeIdDTO(employeeId.get());
		}else {
			return new EmployeeIdDTO(null);
		}
	}
	
	@POST
	@Path("authenticateOnlyStamped")
	public TimeStampInputLoginDto authenticateOnlyStamped(@Context HttpServletRequest request, TimeStampLoginCommand command) {
		return this.authenStampEmployee.authenticateStampedEmployees(command.getCompanyId(), Optional.ofNullable(command.getEmployeeCode()), Optional.ofNullable(command.getEmployeeId()), Optional.ofNullable(command.getPassword()), command.isPasswordInvalid() , command.isPasswordInvalid(), command.isAdminMode(), request);
	}

}

class CardNumberParam {
	public String cardNumber;
}
@AllArgsConstructor
class EmployeeIdDTO {
	public String employeeId;
}
