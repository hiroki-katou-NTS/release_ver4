package nts.uk.ctx.at.request.app.command.application.lateorleaveearly;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.service.FactoryLateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.service.LateOrLeaveEarlyService;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author hieult
 *
 */
@Stateless
@Transactional
public class UpdateLateOrLeaveEarlyCommandHandler
		extends CommandHandlerWithResult<UpdateLateOrLeaveEarlyCommand, ProcessResult> {

	@Inject
	private LateOrLeaveEarlyService lateOrLeaveEarlyService;

	@Inject
	private FactoryLateOrLeaveEarly factoryLateOrLeaveEarly;

	@Inject
	private DetailBeforeUpdate detailBeforeProcessRegisterService;

	@Inject
	private DetailAfterUpdate afterProcessDetailSerivce;

	@Inject
	private ApplicationRepository_New applicationRepository;

	@Inject
	private CreateLateOrLeaveEarlyCommandHandler createHandler;

	@Override
	protected ProcessResult handle(CommandHandlerContext<UpdateLateOrLeaveEarlyCommand> context) {
		String companyID = AppContexts.user().companyId();
		UpdateLateOrLeaveEarlyCommand command = context.getCommand();
		// アルゴリズム「遅刻早退申請理由登録内容生成」を実行する
		String appReason = this.createHandler.genReason(command.getReasonTemp(), command.getAppReason(), companyID);
		Long version = command.getVersion();
		Application_New application = applicationRepository.findByID(companyID, command.getAppID()).get();
		application.setAppReason(new AppReason(appReason));
		application.setVersion(version);
		LateOrLeaveEarly domainLateOrLeaveEarly = factoryLateOrLeaveEarly.buildLateOrLeaveEarly(application,
				command.getActualCancel(), command.getEarly1(), command.getEarlyTime1(), command.getLate1(),
				command.getLateTime1(), command.getEarly2(), command.getEarlyTime2(), command.getLate2(),
				command.getLateTime2());
		domainLateOrLeaveEarly.setVersion(version);

		// 「4-1.詳細画面登録前の処理」を実行する
		detailBeforeProcessRegisterService.processBeforeDetailScreenRegistration(companyID,
				// ApplicantSID = EmployeeID
				domainLateOrLeaveEarly.getApplication().getEmployeeID(),
				domainLateOrLeaveEarly.getApplication().getAppDate(),
				domainLateOrLeaveEarly.getApplication().getAppType().value,
				domainLateOrLeaveEarly.getApplication().getAppID(),
				domainLateOrLeaveEarly.getApplication().getPrePostAtr(), domainLateOrLeaveEarly.getVersion(), null,
				null);

		// ドメインモデル「遅刻早退取消申請」の更新する
		// Update the domain model 'Cancellation for late arrival cancellation'
		lateOrLeaveEarlyService.updateLateOrLeaveEarly(domainLateOrLeaveEarly);

		// 「4-2.詳細画面登録後の処理」を実行する
		// TODO: Waiting for common change
		return afterProcessDetailSerivce.processAfterDetailScreenRegistration(domainLateOrLeaveEarly.getApplication());
	}

}
