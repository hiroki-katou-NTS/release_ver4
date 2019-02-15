package nts.uk.ctx.at.request.app.find.application.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalPhaseStateForAppDto;
import nts.uk.ctx.at.request.app.find.application.requestofearch.OutputMessageDeadline;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Value
@AllArgsConstructor
public class AppDateDataDto{
	private OutputMessageDeadline outputMessageDeadline;
	private List<ApprovalPhaseStateForAppDto> listApprovalPhaseStateDto;
	private AchievementOutput achievementOutput;
	private ApplicationDto_New applicationDto;
	private Integer errorFlag;
	private Integer defaultPrePostAtr;
	private String authorCmt;
}
