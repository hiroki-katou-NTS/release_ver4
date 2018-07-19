package nts.uk.ctx.sys.assist.app.command.datarestoration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryMng;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryMngRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryResult;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryResultRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecovery;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecoveryRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.RecoveryMethod;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PerformDataRecoveryCommandHandler extends AsyncCommandHandler<PerformDataRecoveryCommand> {
	@Inject
	private PerformDataRecoveryRepository repoPerformDataRecovery;
	@Inject
	private DataRecoveryResultRepository repoDataRecoveryResult;
	@Inject
	private DataRecoveryMngRepository repoDataRecoveryMng;
	@Inject
	private RecoveryStogareAsysnCommandHandler recoveryStogareAsysnCommandHandler;

	public void handle(CommandHandlerContext<PerformDataRecoveryCommand> context) {
		PerformDataRecoveryCommand performDataCommand = context.getCommand();
		String dataRecoveryProcessId = performDataCommand.getRecoveryProcessingId();
		String recoveryDate = null;
		int categoryCnt = 0;
		int errorCount = 0;
		int categoryTotalCount = performDataCommand.getRecoveryCategoryList().size();
		String processTargetEmpCode = "";
		int suspendedState = 0;
		int numOfProcesses = 0;
		int totalNumOfProcesses = 0;
		int operatingCondition = 4;
		DataRecoveryMng dataRecoveryMng = new DataRecoveryMng(dataRecoveryProcessId, errorCount, categoryCnt,
				categoryTotalCount, totalNumOfProcesses, numOfProcesses, processTargetEmpCode, suspendedState,
				operatingCondition, recoveryDate);
		repoDataRecoveryMng.add(dataRecoveryMng);

		// ドメインモデル「データ復旧の結果」を登録する
		String cid                    = AppContexts.user().companyId();
		String saveSetCode            = performDataCommand.getSaveSetCode().isEmpty() ? null: performDataCommand.getSaveSetCode();
		String practitioner           = AppContexts.user().employeeId();
		String executionResult        = null;
		GeneralDateTime startDateTime = GeneralDateTime.now();
		GeneralDateTime endDateTime   = null;
		Integer saveForm              = performDataCommand.getSaveForm();
		String saveName               = performDataCommand.getSaveName();
		DataRecoveryResult dataRecoveryResult = new DataRecoveryResult(dataRecoveryProcessId, cid, saveSetCode,
				practitioner, executionResult, startDateTime, endDateTime, saveForm, saveName);
		repoDataRecoveryResult.add(dataRecoveryResult);

		// 復旧条件の調整
		// 画面の「復旧方法」をドメインモデル「データ復旧の実行」に更新する
		Optional<PerformDataRecovery> performData = repoPerformDataRecovery
				.getPerformDatRecoverById(dataRecoveryProcessId);
		performData.ifPresent(x -> {
			x.setRecoveryMethod(EnumAdaptor.valueOf(performDataCommand.getRecoveryMethodOptions(), RecoveryMethod.class));
			x.setNumPeopleBeRestore(context.getCommand().getEmployeeList().size());
			repoPerformDataRecovery.update(x);
		});
		// 復旧対象カテゴリ選別
		// 抽出した非対象カテゴリをドメインモデル「テーブル一覧」の「復旧対象選択」を0（；復旧しない）に更新
		List<String> listCheckCate = performDataCommand.getRecoveryCategoryList().stream().map(x -> x.categoryId)
				.collect(Collectors.toList());
		int selectionTarget = 1;
		repoPerformDataRecovery.updateCategorySelect(selectionTarget, dataRecoveryProcessId, listCheckCate);

		// 「復旧方法」の判別
		if (EnumAdaptor.valueOf(performDataCommand.getRecoveryMethodOptions(),
				RecoveryMethod.class) == RecoveryMethod.RESTORE_SELECTED_RANGE) {

			// 復旧期間の調整
			for (int i = 0; i < listCheckCate.size(); i++) {
				String checkCate = listCheckCate.get(i);
				String startOfPeriod = performDataCommand.getRecoveryCategoryList().get(i).startOfPeriod;
				String endOfPeriod = performDataCommand.getRecoveryCategoryList().get(i).endOfPeriod;
				repoPerformDataRecovery.updateCategorySelectByDateFromTo(startOfPeriod, endOfPeriod,
						dataRecoveryProcessId, checkCate);
			}

			// 対象社員の選別 performDataCommand.employeeList
			List<String> employeeIdList = performDataCommand.getEmployeeList().stream().map(x -> x.id)
					.collect(Collectors.toList());
			repoPerformDataRecovery.deleteEmployeeDataRecovery(dataRecoveryProcessId, employeeIdList);
		}
		recoveryStogareAsysnCommandHandler.handle(context);
	}
}
