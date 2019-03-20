package nts.uk.ctx.at.function.dom.alarm.sendemail;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface SendEmailService {
	public String alarmSendEmail(String companyID, GeneralDate executeDate, List<String> listEmployeeTagetId,
			List<String> listManagerTagetId, List<ValueExtractAlarmDto> listValueExtractAlarmDto,
			MailSettingsParamDto mailSettingsParamDto,String currentAlarmCode);
}
