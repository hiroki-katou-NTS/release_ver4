package nts.uk.ctx.at.request.dom.application.common.service.other.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class ApproveProcessResult {
	private boolean isProcessDone;
	private boolean isAutoSendMail;
	private List<String> autoSuccessMail;
	private List<String> autoFailMail;
	private String appID;
	private String reflectAppId;
	private String appReason;
}
