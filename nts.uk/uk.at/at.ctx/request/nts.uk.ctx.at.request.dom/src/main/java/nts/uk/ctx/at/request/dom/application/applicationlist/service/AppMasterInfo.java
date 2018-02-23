package nts.uk.ctx.at.request.dom.application.applicationlist.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AppMasterInfo {

	// 申請ID
	private String appID;
	private int appType;
	private String dispName;
	private String empName;
	private String workplaceName;
	@Setter
	private boolean statusFrameAtr;
}
