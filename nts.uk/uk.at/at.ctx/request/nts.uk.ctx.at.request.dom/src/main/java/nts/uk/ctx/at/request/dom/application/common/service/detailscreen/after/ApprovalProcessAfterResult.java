package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.request.dom.application.Application_New;

/**
 * @author ThanhNX
 *
 */
@AllArgsConstructor
@Data
public class ApprovalProcessAfterResult {

	private Application_New application;
	
	private Boolean allApprovalFlg;
	
	private Integer phaseNumber;
	
	private String reflectAppId;
}
