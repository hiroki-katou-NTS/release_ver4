package nts.uk.ctx.at.request.dom.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.AtEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.EmployeeInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.EnvAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.MailDestinationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.OutGoingMailImport;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
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
	private AtEmployeeAdapter empAdapter;
	
	@Inject
	private EnvAdapter envAdapter;
	
	@Inject
	private AppDispNameRepository repoAppDispName;
	/**
	 * 送信・送信後チェック
	 */
	@Override
	public MailSenderResult doCheckTranmission(String appId, int appType, int prePostAtr, List<String> employeeIdList,
			String mailTitle, String mailBody, List<String> fileId, String appDate, String applicantID, boolean sendMailApplicaint) {
		String cid = AppContexts.user().companyId();
		Application_New application = applicationRepository.findByID(cid, appId).get();
		Optional<UrlEmbedded> urlEmbedded = urlEmbeddedRepo.getUrlEmbeddedById(cid);
		List<String> successList = new ArrayList<>();
		List<String> errorList = new ArrayList<>();
		//create title mail
		Optional<AppDispName> appDispName = repoAppDispName.getDisplay(appType);
		String appName = "";
		if(appDispName.isPresent()){
			appName = appDispName.get().getDispName().v();
		}
		String titleMail = appDate + " " + appName;
		String urlInfo = "";
		if (urlEmbedded.isPresent()) {
			int urlEmbeddedCls = urlEmbedded.get().getUrlEmbedded().value;
			NotUseAtr checkUrl = NotUseAtr.valueOf(urlEmbeddedCls);
			if (checkUrl == NotUseAtr.USE) {
				urlInfo = "\n" + I18NText.getText("KDL030_30") + "\n"
						+ registerEmbededURL.registerEmbeddedForApp(
						application.getAppID(), 
						application.getAppType().value, 
						application.getPrePostAtr().value, 
						"", 
						applicantID);
			}
		}
		String mailContent1 = mailBody + urlInfo;
		//※同一メール送信者に複数のメールが送られないよう
		//　一旦メール送信した先へのメールは送信しない。
		//list sID da gui
		List<String> lstMailContaint = new ArrayList<>();
		//2018/06/12　追加
		//QA#96551
		//申請者にメール送信かチェックする
		if(sendMailApplicaint && !Strings.isBlank(applicantID)){//チェックする
			//imported（申請承認）「社員メールアドレス」を取得する  - Rq225 (419)
			List<MailDestinationImport> lstApplicantMail = envAdapter.getEmpEmailAddress(cid, Arrays.asList(applicantID), 6);
			List<OutGoingMailImport> mailApplicant = lstApplicantMail.get(0).getOutGoingMails();
			if(mailApplicant.isEmpty() || mailApplicant.get(0) == null || mailApplicant.get(0).getEmailAddress() == null){
				//メールアドレスが取得できなかった場合
				//エラーメッセージを表示する（Msg_1309）
				throw new BusinessException("Msg_1309");
			}
			try {
				mailSender.sendFromAdmin(mailApplicant.get(0).getEmailAddress(), new MailContents(titleMail, mailContent1));
				successList.add(applicantID);
			} catch (Exception ex) {
				throw new BusinessException("Msg_1057");
			}
			lstMailContaint.add(applicantID);
		}
		//get list mail by list sID : rq419
		List<MailDestinationImport> lstMail = envAdapter.getEmpEmailAddress(cid, employeeIdList, 6);
		for(String employeeToSendId: employeeIdList){
			//check id da duoc gui mail
			if(lstMailContaint.contains(employeeToSendId)){//trung lap id thi bo qua
				continue;
			}
			//find mail by sID
			OutGoingMailImport mail = envAdapter.findMailBySid(lstMail, employeeToSendId);
			String employeeMail = mail == null || mail.getEmailAddress() == null ? "" : mail.getEmailAddress();
			if (mail == null) {//TH k co mail -> se k xay ra
				//imported（申請承認）「社員名（ビジネスネーム）」を取得する 
				List<EmployeeInfoImport> empObj = empAdapter.getByListSID(Arrays.asList(employeeToSendId));
				if(!empObj.isEmpty() && empObj.size() > 1){
					errorList.add(empObj.get(0).getBussinessName());
				}
			} else {
				try {
					if (urlEmbedded.isPresent()) {
						int urlEmbeddedCls = urlEmbedded.get().getUrlEmbedded().value;
						NotUseAtr checkUrl = NotUseAtr.valueOf(urlEmbeddedCls);
						if (checkUrl == NotUseAtr.USE) {
							urlInfo = "\n" + I18NText.getText("KDL030_30") + "\n"
									+ registerEmbededURL.registerEmbeddedForApp(
									application.getAppID(), 
									application.getAppType().value, 
									application.getPrePostAtr().value, 
									"", 
									employeeToSendId);
						}
					}
					String mailContent = mailBody + urlInfo;
					mailSender.sendFromAdmin(employeeMail, new MailContents(titleMail, mailContent));
					successList.add(employeeToSendId);
				} catch (Exception ex) {
					throw new BusinessException("Msg_1057");
				}
		
			}
			lstMailContaint.add(employeeToSendId);
		}
		return new MailSenderResult(successList, errorList);
	}
}
