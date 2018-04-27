package nts.uk.ctx.at.request.dom.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeInfoImport;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbedded;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbeddedRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.url.RegisterEmbededURL;

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
	
	@Inject
	private AtEmployeeAdapter employeeRequestAdapter;
	
	@Override
	public MailSenderResult doCheckTranmission(String appId, int appType, int prePostAtr, List<String> employeeIdList,
			String mailTitle, String mailBody, List<String> fileId) {
		String cid = AppContexts.user().companyId();
		Application_New application = applicationRepository.findByID(cid, appId).get();
		Optional<UrlEmbedded> urlEmbedded = urlEmbeddedRepo.getUrlEmbeddedById(cid);
		List<String> successList = new ArrayList<>();
		List<String> errorList = new ArrayList<>();
		// URL is pending
		if (urlEmbedded.isPresent()) {
			int urlEmbeddedCls = urlEmbedded.get().getUrlEmbedded().value;
			NotUseAtr checkUrl = NotUseAtr.valueOf(urlEmbeddedCls);
			if (checkUrl == NotUseAtr.USE) {
				String urlInfo = registerEmbededURL.obtainApplicationEmbeddedUrl(appId, application.getAppType().value,
						application.getPrePostAtr().value, application.getEmployeeID());
				if (!Strings.isEmpty(urlInfo)){
//					appContent += "\n" + I18NText.getText("KDL030_30") + " " + application.getAppID() + "\n" + urlInfo;
				}
			}
		}
		// TO - DO
		// request list 419 - get mail
		List<EmployeeInfoImport> listEmpName = employeeRequestAdapter.getByListSID(employeeIdList);
		for(String employeeToSendId: employeeIdList){
			List<EmployeeInfoImport> listEmpName1 = employeeRequestAdapter.getByListSID(Arrays.asList(employeeToSendId));
			String empName = (!listEmpName1.isEmpty() ? listEmpName1.get(0).getBussinessName() : "");
			String mail = employeeToSendId;
			String employeeMail = "hiep.ld@3si.vn";
			if (Strings.isBlank(mail)) {
				errorList.add(empName);
			} else {
				mailSender.send("mailadmin@uk.com", employeeMail, new MailContents("", mailBody));
				successList.add(empName);
				
			}
		}
		return new MailSenderResult(successList, errorList);
	}
}
