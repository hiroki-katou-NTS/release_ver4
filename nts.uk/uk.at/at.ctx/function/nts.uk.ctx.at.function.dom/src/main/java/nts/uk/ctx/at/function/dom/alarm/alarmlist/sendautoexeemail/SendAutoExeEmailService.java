package nts.uk.ctx.at.function.dom.alarm.alarmlist.sendautoexeemail;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractedAlarmDto;

public interface SendAutoExeEmailService {
	public Optional<OutputSendAutoExe> sendAutoExeEmail(String companyId,GeneralDateTime executionDate,List<ExtractedAlarmDto> listExtractedAlarmDto,boolean sendMailPerson,boolean sendMailAdmin,String alarmCode);
	
}
