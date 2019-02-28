package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.EnvAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.MailDestinationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.sys.dto.OutGoingMailImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.application.IApplicationContentService;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbedded;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailcontenturlsetting.UrlEmbeddedRepository;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.remandsetting.ContentOfRemandMail;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.remandsetting.ContentOfRemandMailRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.url.RegisterEmbededURL;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DetailAfterRemandImpl implements DetailAfterRemand {

	@Inject
	private ApplicationRepository_New applicationRepository;

	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;

	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Inject
	private EmployeeRequestAdapter employeeAdapter;

	@Inject
	private MailSender mailsender;

	@Inject
	private RegisterEmbededURL registerEmbededURL;

	@Inject
	private ContentOfRemandMailRepository remandRepo;

	@Inject
	private UrlEmbeddedRepository urlEmbeddedRepo;

	@Inject 
	private IApplicationContentService appContentService;
	
	@Inject
	private EnvAdapter envAdapter;
	
	@Inject
	private AppDispNameRepository repoAppDispName;
	/**
	 * 11-2.詳細画面差し戻し後の処理
	 */
	@Override
	public MailSenderResult doRemand(String companyID, RemandCommand remandCm) {
		List<String> successList = new ArrayList<>();
		List<String> errorList = new ArrayList<>();
		boolean isSendMail = true;
		for (String appID : remandCm.getAppID()) {
			Application_New application = applicationRepository.findByID(companyID, appID).get();
			//ドメインモデル「申請」の差し戻し理由を画面のコメントで更新する-(update lý do trả về của domain 「申請」 bằng comment trên màn hình)
			//Bug#101502
			//差し戻しの日付(Short_YMD) + 全角スペース +差し戻しを行った承認者の名前 + "⇒" + 差し戻し先 + "：" + 改行 + 差し戻しコメント(C1-5)
			Integer order = remandCm.getOrder();
			String destination = order != null && order > 0 ? "フェーズ" + order + "の承認者" : remandCm.getApplicaintName();
			//差し戻しを行った承認者の名前
			String reSname = employeeAdapter.getEmployeeName(AppContexts.user().employeeId());
			String remandReason = GeneralDate.today().toString() + "　" + reSname + "⇒" + destination + "：" + "\n" + remandCm.getRemandReason();
			application.setReversionReason(new AppReason(remandReason));
			AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository
					.getAppTypeDiscreteSettingByAppType(companyID, application.getAppType().value).get();
			MailSenderResult mailResult = new MailSenderResult(new ArrayList<>(), new ArrayList<>());
			if (order != null) {// 差し戻し先が承認者の場合
				//Imported（承認申請）「差し戻しする（承認者まで）」-(Imported（approvalApplication）「trả về（đến  approver）」)
				//RequestList482
				List<String> employeeList = approvalRootStateAdapter.doRemandForApprover(companyID, appID, order);
				//承認処理時に自動でメールを送信するが　trueの場合(check sendMailWhenApprove trong domain Applicationsetting)
				if (appTypeDiscreteSetting.getSendMailWhenApprovalFlg().equals(AppCanAtr.CAN)) {
					//「申請種類別設定」．新規登録時に自動でメールを送信するがtrue
					mailResult = this.getMailSenderResult(application, employeeList, remandReason, isSendMail);
				}
			} else {// 差し戻し先が申請本人の場合
				//Imported（承認申請）「差し戻しする（本人まで）」-(Trả về bản thân người làm đơn)
				//RequestList No.480
				approvalRootStateAdapter.doRemandForApplicant(companyID, appID);
				//Imported（承認申請）「差し戻し反映情報更新」-(Update thông tin phản ánh trả về) - RequestList No.481
				//「反映情報」．実績反映状態を「差し戻し」にする
				application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.REMAND);
				//「反映情報」．予定反映状態を「差し戻し」にする
				application.getReflectionInformation().setStateReflection(ReflectedState_New.REMAND);
				//ドメインモデル「申請種類別設定」．承認処理時に自動でメールを送信するをチェックする-(Check 「申請種類別設定」．Tự động gửi mail khi approve)
				if (appTypeDiscreteSetting.getSendMailWhenApprovalFlg().equals(AppCanAtr.CAN)) {
					//申請者本人にメール送信する-(Send mail đến bản thân người làm đơn)
					mailResult = this.getMailSenderResult(application, Arrays.asList(application.getEmployeeID()), remandReason, isSendMail);
				}
			}
			successList.addAll(mailResult.getSuccessList());
			errorList.addAll(mailResult.getErrorList());
			//UPDATE ドメインモデル「申請」
			applicationRepository.updateWithVersion(application);
			isSendMail = false;
		}
		return new MailSenderResult(successList, errorList);
	}

	@Override
	public MailSenderResult getMailSenderResult(Application_New application, List<String> employeeList, String returnReason, boolean isSendMail) {
		//doi ung kaf011 - tranh spam mail
		if(!isSendMail){
			return new MailSenderResult(new ArrayList<>(), new ArrayList<>());
		}
		String applicantID = application.getEmployeeID();
		String mailTitle = "";
		String mailBody = "";
		String cid = AppContexts.user().companyId();
		String sidLogin = AppContexts.user().employeeId();
		String appContent = appContentService.getApplicationContent(application);	
		ContentOfRemandMail remandTemp = remandRepo.getRemandMailById(cid).orElse(null);
		if (!Objects.isNull(remandTemp)) {
			mailTitle = remandTemp.getMailTitle().v();
			mailBody = remandTemp.getMailBody().v();
		}
		Optional<UrlEmbedded> urlEmbedded = urlEmbeddedRepo.getUrlEmbeddedById(AppContexts.user().companyId());
		List<String> successList = new ArrayList<>();
		List<String> errorList = new ArrayList<>();
		
		// Using RQL 419 instead (1 not have mail)
		//get list mail by list sID
		List<MailDestinationImport> lstMail = envAdapter.getEmpEmailAddress(cid, employeeList, 6);
		Optional<AppDispName> appDispName = repoAppDispName.getDisplay(application.getAppType().value);
		String appName = "";
		if(appDispName.isPresent()){
			appName = appDispName.get().getDispName().v();
		}
		//get mail login
		List<MailDestinationImport> lstMailLogin = envAdapter.getEmpEmailAddress(cid, Arrays.asList(sidLogin), 6);
		List<OutGoingMailImport> mailLogin = lstMailLogin.get(0).getOutGoingMails();
		String loginMail = mailLogin.isEmpty() ||  mailLogin.get(0) == null || mailLogin.get(0).getEmailAddress() == null ? "" :
					mailLogin.get(0).getEmailAddress();
		//get name login
		String nameLogin = employeeAdapter.getEmployeeName(sidLogin);
		for (String employee : employeeList) {
			String employeeName = employeeAdapter.getEmployeeName(employee);
			OutGoingMailImport mail = envAdapter.findMailBySid(lstMail, employee);
			String employeeMail = mail == null || mail.getEmailAddress() == null ? "" : mail.getEmailAddress();
			// TODO
			String urlInfo = "";
			if (urlEmbedded.isPresent()) {
				int urlEmbeddedCls = urlEmbedded.get().getUrlEmbedded().value;
				NotUseAtr checkUrl = NotUseAtr.valueOf(urlEmbeddedCls);
				if (checkUrl == NotUseAtr.USE) {
					urlInfo = registerEmbededURL.registerEmbeddedForApp(
							application.getAppID(), 
							application.getAppType().value, 
							application.getPrePostAtr().value, 
							"", 
							employee);
				}
			}
			if (!Strings.isBlank(urlInfo)) {
				appContent += "\n" + I18NText.getText("KDL030_30") + "\n" + urlInfo;
			}
			String mailContentToSend = I18NText.getText("Msg_1060",
					//｛0｝氏名 - ログイン者
					nameLogin,
					//｛1｝メール本文 - 差し戻しメールテンプレート
					mailBody,
					//｛2｝システム日付
					GeneralDate.today().toString(),
					//｛3｝申請種類（名称） - 申請
					appName,
					//｛4｝申請者の氏名 - 申請
					employeeAdapter.getEmployeeName(applicantID),
					//｛5｝申請日付 - 申請
					application.getAppDate().toLocalDate().toString(),
					//｛6｝申請内容 - 申請
					appContent,
					//｛7｝氏名 - ログイン者
					nameLogin,
					//｛8｝メールアドレス - ログイン者
					loginMail,
					//{9}差し戻し理由 //ver2
					returnReason);
			if (Strings.isBlank(employeeMail)) {
				errorList.add(I18NText.getText("Msg_768", employeeName));
				continue;
			} else {
				try {
					mailsender.sendFromAdmin(employeeMail, new MailContents(mailTitle, mailContentToSend));
					successList.add(employeeName);
				} catch (Exception ex) {
					throw new BusinessException("Msg_1057");
				}
			}
		}
		return new MailSenderResult(successList, errorList);
	}
}
