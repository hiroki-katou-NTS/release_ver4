package nts.uk.ctx.pr.report.app.payment.comparing.settingoutputitem.command;

import java.util.List;

import lombok.Data;

@Data
public class UpdateComparingFormCommand {
	private String formCode;
	private String formName;
	private List<BaseEntityCommand> comparingFormDetailList;
}
