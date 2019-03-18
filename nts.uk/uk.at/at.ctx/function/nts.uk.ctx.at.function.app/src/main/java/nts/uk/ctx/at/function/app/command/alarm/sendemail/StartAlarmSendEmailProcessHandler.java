package nts.uk.ctx.at.function.app.command.alarm.sendemail;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.sendemail.MailSettingsParamDto;
import nts.uk.ctx.at.function.dom.alarm.sendemail.SendEmailService;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StartAlarmSendEmailProcessHandler extends CommandHandlerWithResult<ParamAlarmSendEmailCommand, String> {

	@Inject
	private SendEmailService sendEmailService;
	
	@Override
	protected String handle(CommandHandlerContext<ParamAlarmSendEmailCommand> context) {
		
		ParamAlarmSendEmailCommand command = context.getCommand();
		String companyID = AppContexts.user().companyId(); // ログイン社員の会社ID
		GeneralDate executeDate = GeneralDate.today(); // システム日付
		List<String> listEmployeeTagetId = command.getListEmployeeSendTaget(); //本人送信対象：List<社員ID>
		List<String> listManagerTagetId=command.getListManagerSendTaget(); // 管理者送信対象：List<社員ID>
		List<ValueExtractAlarmDto> listValueExtractAlarmDto=command.getListValueExtractAlarmDto();//アラーム抽出結果
		MailSettingsParamDto mailSettingsParamDto=command.getMailSettingsParamDto();//メール送信設定
		String currentAlarmCode = command.getCurrentAlarmCode();
		return sendEmailService.alarmSendEmail(companyID, executeDate, listEmployeeTagetId, listManagerTagetId,
				listValueExtractAlarmDto, mailSettingsParamDto,currentAlarmCode);
	}

}
