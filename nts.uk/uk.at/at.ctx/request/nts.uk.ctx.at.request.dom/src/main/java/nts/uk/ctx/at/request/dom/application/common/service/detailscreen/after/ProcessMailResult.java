package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ThanhNX
 *
 */
@AllArgsConstructor
@Data
public class ProcessMailResult {
	
	private List<String> autoSuccessMail = new ArrayList<>();
	
	private List<String> autoFailMail = new ArrayList<>();
	
	boolean isAutoSendMail = false;
	
}
