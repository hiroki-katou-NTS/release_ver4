package nts.uk.ctx.at.function.app.command.alarm.sendemail;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.find.alarm.alarmlist.ErAlExtractResultFinder;
import nts.uk.ctx.at.function.app.find.alarm.mailsettings.MailAutoAndNormalDto;
import nts.uk.ctx.at.function.app.find.alarm.mailsettings.MailSettingFinder;
import nts.uk.ctx.at.function.app.find.alarm.mailsettings.MailSettingsDto;
import nts.uk.ctx.at.function.dom.alarm.sendemail.MailSettingsParamDto;
import nts.uk.ctx.at.function.dom.alarm.sendemail.SendEmailService;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StartAlarmSendEmailProcessHandler extends CommandHandlerWithResult<ParamAlarmSendEmailCommand, String> {

	@Inject
	private SendEmailService sendEmailService;

	@Inject
	private ErAlExtractResultFinder extractResultFinder;

	@Inject
	private MailSettingFinder mailSettingFinder;

	@Override
	protected String handle(CommandHandlerContext<ParamAlarmSendEmailCommand> context) {

		MailAutoAndNormalDto mailSetting = mailSettingFinder.findMailSet();
		if (isNotHaveMailSetting(mailSetting)) {
			throw new BusinessException("Msg_1169");
		}
		ParamAlarmSendEmailCommand command = context.getCommand();
		String companyID = AppContexts.user().companyId(); // ログイン社員の会社ID
		GeneralDate executeDate = GeneralDate.today(); // システム日付
		List<String> listEmployeeTagetId = command.getListEmployeeSendTaget(); // 本人送信対象：List<社員ID>
		List<String> listManagerTagetId = command.getListManagerSendTaget(); // 管理者送信対象：List<社員ID>
		List<ValueExtractAlarmDto> listValueExtractAlarmDto = extractResultFinder.getResultDto(command.getProcessId());// アラーム抽出結果
		MailSettingsParamDto mailSettingsParamDto = buildMailSend(mailSetting);// メール送信設定

		return sendEmailService.alarmSendEmail(companyID, executeDate, listEmployeeTagetId, listManagerTagetId,
				listValueExtractAlarmDto, mailSettingsParamDto);
	}

	private MailSettingsParamDto buildMailSend(MailAutoAndNormalDto mailSetting) {

		MailSettingsDto mailSetings = mailSetting.getMailSettingNormalDto().getMailSettings(),
				mailSetingAdmins = mailSetting.getMailSettingNormalDto().getMailSettingAdmins();
		String subject = "", text = "", subjectAdmin = "", textAdmin = "";
		if (mailSetings != null) {
			subject = mailSetings.getSubject();
			text = mailSetings.getText();
		}
		if (mailSetingAdmins != null) {
			subjectAdmin = mailSetingAdmins.getSubject();
			textAdmin = mailSetingAdmins.getText();
		}
		// setting subject , body mail
		return new MailSettingsParamDto(subject, text, subjectAdmin, textAdmin);
	}

	private boolean isNotHaveMailSetting(MailAutoAndNormalDto mailSetting) {
		return mailSetting == null || mailSetting.getMailSettingNormalDto() == null
				|| (mailSetting.getMailSettingNormalDto().getMailSettings() == null
						&& mailSetting.getMailSettingNormalDto().getMailSettingAdmins() == null);
	}

}
