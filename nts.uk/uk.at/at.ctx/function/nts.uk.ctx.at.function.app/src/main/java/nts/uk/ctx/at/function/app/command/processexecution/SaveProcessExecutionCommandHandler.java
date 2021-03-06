package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.LastExecDateTime;
import nts.uk.ctx.at.function.dom.processexecution.UpdateProcessAutoExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionScopeItemRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.LastExecDateTimeRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The class Save process execution command handler.
 */
@Stateless
public class SaveProcessExecutionCommandHandler extends CommandHandlerWithResult<SaveProcessExecutionCommand, String> {

	/** The Process execution repository */
	@Inject
	private ProcessExecutionRepository procExecRepo;

	/** The Last exec date time repository */
	@Inject
	private LastExecDateTimeRepository lastDateTimeRepo;

	/** The Execution scope item repository */
	@Inject
	private ExecutionScopeItemRepository scopeItemRepo;

	/** The Process execution log manage repository */
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManRepo;

	/**
	 * UKDesign.UniversalK.就業.KBT_更新処理自動実行.KBT002_更新処理自動実行.B:実行設定.アルゴリズム.登録ボタン押下時処理.登録ボタン押下時処理
	 * 
	 * @param context the context
	 * @return the <code>String</code>
	 */
	@Override
	protected String handle(CommandHandlerContext<SaveProcessExecutionCommand> context) {
		// ログイン社員の社員ID
		String companyId = AppContexts.user().companyId();

		SaveProcessExecutionCommand command = context.getCommand();
		// 選択している項目の更新処理自動実行項目コード
		String execItemCd = command.getExecItemCode();
		UpdateProcessAutoExecution procExec = UpdateProcessAutoExecution.createFromMemento(companyId, command);
//		procExec.validateVer2();
		if (command.isNewMode()) {
			// 新規モード(new mode)
			// 新規登録処理
			Optional<UpdateProcessAutoExecution> procExecOpt = this.procExecRepo.getProcessExecutionByCidAndExecCd(companyId,
					execItemCd);
			if (procExecOpt.isPresent()) {
				throw new BusinessException("Msg_3");
			}
			// ドメインモデル「更新処理自動実行」に新規登録する
			this.procExecRepo.insert(procExec);

			// ドメインモデル「更新処理自動実行管理」に新規登録する
			ProcessExecutionLogManage processExecutionLogManage = new ProcessExecutionLogManage(
					new ExecutionCode(command.getExecItemCode()), companyId, EndStatus.NOT_IMPLEMENT,
					CurrentExecutionStatus.WAITING);
			this.processExecLogManRepo.insert(processExecutionLogManage);
			this.lastDateTimeRepo
					.insert(new LastExecDateTime(companyId, new ExecutionCode(command.getExecItemCode()), null));
		} else {
			// 更新モード(update mode)
			// ドメインモデル「更新処理自動実行管理」を取得し、現在の実行状態を判断する
			ProcessExecutionLogManage processExecutionLogManage = this.processExecLogManRepo
					.getLogByCIdAndExecCd(companyId, execItemCd).orElseThrow(() -> new BusinessException("Msg_3"));
			// 更新処理自動実行管理.現在の実行状態 ＝ 実行中
			if (processExecutionLogManage.getCurrentStatus().isPresent()
					&& processExecutionLogManage.getCurrentStatus().get().equals(CurrentExecutionStatus.RUNNING)) {
				throw new BusinessException("Msg_1318");
			}
			// 更新処理自動実行管理.現在の実行状態 ≠ 実行中
			this.scopeItemRepo.removeAllByCidAndExecCd(procExec.getCompanyId(), procExec.getExecItemCode().v());
			// ドメインモデル「更新処理自動実行」に更新登録する
			this.procExecRepo.update(procExec);
			// Todo ドメインモデル「実行タスク設定」を更新する

			this.scopeItemRepo.insert(procExec.getCompanyId(), procExec.getExecItemCode().v(),
					procExec.getExecScope().getWorkplaceIdList());
		}
		return procExec.getExecItemCode().v();
//		return null;
	}
}
