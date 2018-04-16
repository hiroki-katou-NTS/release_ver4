package nts.uk.ctx.at.request.dom.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbedded;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbeddedRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.shr.com.context.AppContexts;
//import nts.uk.ctx.sys.gateway.dom.mail.service.RegisterEmbededURL;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.url.RegisterEmbededURL;
import nts.uk.shr.com.url.UrlExecInfo;

/**
 * @author hiep.ld
 *
 */
@Stateless
public class CheckTranmissionImpl implements CheckTransmission {

	@Inject
	private UrlEmbeddedRepository urlEmbeddedRepo;
	@Inject
	private MailSender mailSender;

	@Inject
	private ApplicationRepository_New applicationRepository;

	@Inject
	private RegisterEmbededURL registerEmbededURL;
	
	@Override
	public MailSenderResult doCheckTranmission(String appId, int appType, int prePostAtr, List<String> employeeIdList,
			String mailTitle, String mailBody, List<String> fileId) {
		String cid = AppContexts.user().companyId();
		Application_New application = applicationRepository.findByID(cid, appId).get();
		Optional<UrlEmbedded> urlEmbedded = urlEmbeddedRepo.getUrlEmbeddedById(cid);
		List<String> successList = new ArrayList<>();
		List<String> errorList = new ArrayList<>();
		if (urlEmbedded.isPresent()) {
			int urlEmbeddedCls = urlEmbedded.get().getUrlEmbedded().value;
			NotUseAtr checkUrl = NotUseAtr.valueOf(urlEmbeddedCls);
			if (checkUrl == NotUseAtr.USE) {
				UrlExecInfo urlInfo = registerEmbededURL.obtainApplicationEmbeddedUrl(appId, application.getAppType().value,
						application.getPrePostAtr().value, application.getEmployeeID());
				if (!Objects.isNull(urlInfo)){
					mailBody = "\n" + urlInfo.getProgramId() + urlInfo.getEmbeddedId() + "\n" + mailBody ;
				}
			}
		}
		String loginName = "D00001";
		String loginMail = "D00001@nittsusystime.co.jp";
		String empName = "D00001 name";
		String mailContentToSend = I18NText.getText("Msg_703",
				loginName, mailBody,
				GeneralDate.today().toString(), application.getAppType().nameId,
				empName, application.getAppDate().toLocalDate().toString(),
				"appContent", loginName, loginMail);
		// TO - DO
		// request list 225
		// request list 228
		// get mail (may be get from client but re-get here)
		for(String employeeToSendId: employeeIdList){
			String mail = employeeToSendId;
			String employeeMail = "hiep.ld@3si.vn";
			if (Strings.isBlank(mail)) {
				errorList.add(mail);
			} else {
				mailSender.send("tarou@nittsusystem.co.jp", employeeMail, new MailContents("", mailBody));
				successList.add(employeeToSendId);
				
			}
		}
		return new MailSenderResult(successList, errorList);
	}
}
