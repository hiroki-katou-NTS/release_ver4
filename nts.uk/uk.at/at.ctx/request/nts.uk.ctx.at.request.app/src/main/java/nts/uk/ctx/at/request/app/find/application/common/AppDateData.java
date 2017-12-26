package nts.uk.ctx.at.request.app.find.application.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.request.app.find.application.requestofearch.OutputMessageDeadline;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Value
@AllArgsConstructor
public class AppDateData{
	private OutputMessageDeadline outputMessageDeadline;
	private List<ApprovalPhaseStateDto> listApprovalPhaseStateDto;
	private RecordWorkDto_New recordWorkDto;
	private Integer errorFlag;
}
