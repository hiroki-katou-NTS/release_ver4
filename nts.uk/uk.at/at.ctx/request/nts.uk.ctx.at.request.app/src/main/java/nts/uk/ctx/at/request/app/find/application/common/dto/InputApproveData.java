package nts.uk.ctx.at.request.app.find.application.common.dto;

import lombok.Data;
import nts.uk.ctx.at.request.app.find.application.ApplicationDto;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
public class InputApproveData {
	ApplicationDto applicationDto;	
	String memo;
	String comboBoxReason;
    String textAreaReason;
    Integer holidayAppType;
    Integer user;
    Integer reflectPerState;
    Boolean mobileCall;
}
