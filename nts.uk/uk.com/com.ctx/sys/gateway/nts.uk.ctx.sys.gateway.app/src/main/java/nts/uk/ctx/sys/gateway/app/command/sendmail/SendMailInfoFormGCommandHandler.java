/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.sendmail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.sys.gateway.app.command.sendmail.dto.SendMailReturnDto;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoDtoImport;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.login.adapter.MailDestinationAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.MailDestinationImport;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.mail.SendMailFailedException;
import nts.uk.shr.com.url.RegisterEmbededURL;

/**
 * The Class SendMailInfoCommandHandler.
 */
@Stateless
@Transactional
public class SendMailInfoFormGCommandHandler
		extends CommandHandlerWithResult<SendMailInfoFormGCommand, List<SendMailReturnDto>> {

	/** The user adapter. */
	@Inject
	private UserAdapter userAdapter;

	/** The mail sender. */
	@Inject
	private MailSender mailSender;

	/** The register embeded URL. */
	@Inject
	private RegisterEmbededURL registerEmbededURL;

	/** The employee info adapter. */
	@Inject
	private EmployeeInfoAdapter employeeInfoAdapter;

	/** The mail destination adapter. */
	@Inject
	private MailDestinationAdapter mailDestinationAdapter;
	
	/** The Constant LOGIN_FUNCTION_ID. */
	private static final Integer LOGIN_FUNCTION_ID =1;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected List<SendMailReturnDto> handle(CommandHandlerContext<SendMailInfoFormGCommand> context) {
		// get command
		SendMailInfoFormGCommand command = context.getCommand();

		String companyId = command.getContractCode() + "-" + command.getCompanyCode();
		// Imported（GateWay）「社員」を取得する
		EmployeeInfoDtoImport employee = this.employeeInfoAdapter.getEmployeeInfo(companyId, command.getEmployeeCode());

		if (employee != null) {
			// 社員のメールアドレスを取得する
			MailDestinationImport mailDestinationImport = mailDestinationAdapter.getMailofEmployee(companyId,
					Arrays.asList(employee.getEmployeeId()), LOGIN_FUNCTION_ID);
			// get userInfo
			Optional<UserImportNew> user = this.userAdapter.findUserByAssociateId(employee.getPersonId());

			if (user.isPresent()) {
				if (mailDestinationImport.getOutGoingMails().isEmpty()) {
					// check mail present
					if (user.get().getMailAddress().get().isEmpty()) {
						throw new BusinessException("Msg_1129");
					} else {
						// Send Mail アルゴリズム「メール送信実行」を実行する
						return this.sendMail(Arrays.asList(user.get().getMailAddress().get()), user.get().getLoginId(),
								command, employee,companyId);
					}
					// return new SendMailReturnDto(null);
				} else {
					return this.sendMail(mailDestinationImport.getOutGoingMails(), user.get().getLoginId(), command,
							employee,companyId);
				}
			} else {
				return Arrays.asList(new SendMailReturnDto(null));
			}
		}
		// fixbug #101548 EA修正履歴 No.2891
		else {
			throw new BusinessException("Msg_176");
		}
	}

	/**
	 * CCG007_ログイン (Login).メール送信実行
	 * @param mailto
	 *            the mailto
	 * @param command
	 *            the command
	 * @return true, if successful
	 */
	// Send Mail アルゴリズム「メール送信実行」を実行する
	private List<SendMailReturnDto> sendMail(List<String> toMails, String loginId, SendMailInfoFormGCommand command,
			EmployeeInfoDtoImport employee,String companyId) {
		// get URL from CCG033
		String url = this.registerEmbededURL.embeddedUrlInfoRegis("CCG007", "H", 3, 24, employee.getEmployeeId(),
				command.getContractCode(), loginId, employee.getEmployeeCode(), 1, new ArrayList<>());
		//hoatt 2019.05.20 EA3451 #107795
		MailContents contents = new MailContents(I18NText.getText("CCG007_40"), I18NText.getText("CCG007_21") + " \n" + url);
		List<SendMailReturnDto> dtos = new ArrayList<>();
		try {
			toMails.stream().forEach(item -> {
				mailSender.sendFromAdmin(item, contents,companyId);
				SendMailReturnDto dto = new SendMailReturnDto(url);
				dtos.add(dto);
			});
			return dtos;
		} catch (SendMailFailedException e) {
			// Send mail fail
			throw new BusinessException("Msg_208");
		}
	}
}
