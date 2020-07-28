package nts.uk.ctx.at.record.ws.kdp.kmp001.a;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.RegisterStampCardCommandHandler;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.a.CardInformationCommands;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.a.EmployeeCardInformationViewACommand;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.b.DeleteCardViewBCommand;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.b.DeleteCardViewBCommandHandler;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.b.RegisterCardViewBCommand;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.b.RegisterCardViewBCommandHandler;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.c.RegisterStampCardViewCCommand;
import nts.uk.ctx.at.record.app.command.kmp.kmp001.c.RegisterStampCardViewCCommandHandler;

/**
 * 
 * @author chungnt
 *
 */

@Path("at/record/register-stamp-card")
@Produces("application/json")
public class RegisterStampCardWs extends WebService {

	@Inject
	private RegisterStampCardCommandHandler commandHandler;
	
	@Inject
	private RegisterStampCardViewCCommandHandler registerSrampCard;
	
	@Inject
	private RegisterCardViewBCommandHandler handlerRegisterViewB;
	
	@Inject
	private DeleteCardViewBCommandHandler handlerDeleteViewB;
	
	/**新規モード時にIDカードNOの登録を行う */
	@POST
	@Path("view-a/save")
	public void saveStampCardViewA(EmployeeCardInformationViewACommand command) {
		commandHandler.saveStampCardViewA(command);
	}
	
	/**新規モード時にIDカードNOの登録を行う */
	@POST
	@Path("view-a/update")
	public void updateStampCardViewA(EmployeeCardInformationViewACommand command) {
		commandHandler.updateStampCardViewA(command);
	}
	
	/**新規モード時にIDカードNOの登録を行う */
	@POST
	@Path("view-a/delete")
	public void deleteStampCardViewA(CardInformationCommands command) {
		commandHandler.deleteCardInfomaiton(command);
	}
	
	@POST
	@Path("view-c/save")
	public void registerStampCardViewC(RegisterStampCardViewCCommand command) {
		this.registerSrampCard.handle(command);
	}
	
	@POST
	@Path("view-b/save")
	public void registerStampCardViewB(RegisterCardViewBCommand command) {
		this.handlerRegisterViewB.handle(command);
	}
	
	@POST
	@Path("view-b/delete")
	public void deleteStampCardViewB(DeleteCardViewBCommand command) {
		this.handlerDeleteViewB.handle(command);
	}
}
