package nts.uk.ctx.at.request.app.command.application.workchange;

import lombok.Value;
import nts.uk.ctx.at.request.app.command.application.common.CreateApplicationCommand;

@Value
public class AddAppWorkChangeCommand {
	/**
	 * 勤務変更申請
	 */
	AppWorkChangeCommand workChange;
		
	/**
	 * 申請
	 */
	CreateApplicationCommand application;
	
	
	
	String employeeID;
	
	Integer user;
    Integer reflectPerState;

    boolean checkOver1Year;
}
