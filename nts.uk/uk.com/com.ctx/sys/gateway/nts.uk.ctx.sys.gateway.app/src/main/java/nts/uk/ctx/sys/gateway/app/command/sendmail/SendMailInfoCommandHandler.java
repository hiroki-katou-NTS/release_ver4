package nts.uk.ctx.sys.gateway.app.command.sendmail;

import java.util.ArrayList;
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
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.login.adapter.ListCompanyAdapter;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.mail.SendMailFailedException;
import nts.uk.shr.com.url.RegisterEmbededURL;

/**
 * The Class SendMailInfoCommandHandler.
 */
@Stateless
@Transactional
public class SendMailInfoCommandHandler extends CommandHandlerWithResult<SendMailInfoCommand, SendMailReturnDto> {

	/** The user adapter. */
	@Inject
	private UserAdapter userAdapter;

	/** The mail sender. */
	@Inject
	private MailSender mailSender;

	/** The register embeded URL. */
	@Inject
	private RegisterEmbededURL registerEmbededURL;
	
	@Inject
	private SysEmployeeAdapter sysEmployeeAdapter;
	
	/** The Constant FIST_COMPANY. */
	private static final Integer FIST_COMPANY = 0;
	
	@Inject
	private ListCompanyAdapter listCompanyAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected SendMailReturnDto handle(CommandHandlerContext<SendMailInfoCommand> context) {
		// get command
		SendMailInfoCommand command = context.getCommand();
		
		if (!command.getLoginId().isEmpty()){
			// Get RequestList 222
			Optional<UserImportNew> user = this.userAdapter.findUserByContractAndLoginIdNew(command.getContractCode(),
					command.getLoginId());
	
			if (user.isPresent()) {
				if (user.get().getMailAddress().get().isEmpty()) {
					throw new BusinessException("Msg_1129");
				} else {
					//get employeeId
					String employeeId = null;
					if (!user.get().getAssociatePersonId().get().isEmpty()){
						List<String> lstCompanyId = listCompanyAdapter.getListCompanyId(user.get().getUserId(), user.get().getAssociatePersonId().get());
						//get Employee
						Optional<EmployeeImport> employee = this.sysEmployeeAdapter.getByPid(lstCompanyId.get(FIST_COMPANY),
								user.get().getAssociatePersonId().get());
						
						if (employee.isPresent()){
							employeeId = employee.get().getEmployeeId();
						}
					}
					
					// Send Mail アルゴリズム「メール送信実行」を実行する
					return this.sendMail(user.get().getMailAddress().get(), command, employeeId);
				}
			}
		}
		
		return new SendMailReturnDto(null);
	}

	/**
	 * Send mail.
	 *
	 * @param mailto
	 *            the mailto
	 * @param command
	 *            the command
	 * @return true, if successful
	 */
	// Send Mail アルゴリズム「メール送信実行」を実行する
	private SendMailReturnDto sendMail(String mailto, SendMailInfoCommand command, String employeeId) {
		//Set param input
		String programId = "CCG007";
		String screenId = "H";
		int timePeriod = 3;
		int numberPeriod = 24;
		String employeeCode = null;
		
		// get URL from CCG033
		String url = this.registerEmbededURL.embeddedUrlInfoRegis(programId, screenId, timePeriod, numberPeriod, employeeId,
				command.getContractCode(), command.getLoginId(), employeeCode, 1, new ArrayList<>());
		// sendMail
		MailContents contents = new MailContents(I18NText.getText("CCG007_40"), I18NText.getText("CCG007_21") +" \n" + url);

		try {
			mailSender.sendFromAdmin(mailto, contents);
			SendMailReturnDto dto = new SendMailReturnDto(url);
			return dto;
		} catch (SendMailFailedException e) {
			// Send mail fail
			throw new BusinessException("Msg_208");
		}

	}

}
