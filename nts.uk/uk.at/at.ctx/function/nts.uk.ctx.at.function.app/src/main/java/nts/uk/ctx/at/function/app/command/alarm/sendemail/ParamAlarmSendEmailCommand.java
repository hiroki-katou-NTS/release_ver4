package nts.uk.ctx.at.function.app.command.alarm.sendemail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.dom.alarm.sendemail.MailSettingsParamDto;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamAlarmSendEmailCommand {
	private List<String> listEmployeeSendTaget;
	private List<String> listManagerSendTaget;
	private List<ValueExtractAlarmDto> listValueExtractAlarmDto;
	private MailSettingsParamDto mailSettingsParamDto;
	private String currentAlarmCode;
}
