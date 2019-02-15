package nts.uk.ctx.at.request.app.find.application.common.dto;

import lombok.Data;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
public class InputApproveData {
	ApplicationDto_New applicationDto;	
	String memo;
	String comboBoxReason;
    String textAreaReason;
}
