package nts.uk.ctx.sys.assist.dom.datarestoration.common;

import nts.uk.ctx.sys.assist.dom.datarestoration.*;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ServerPreparationService {

	@Inject
	private DataExtractionService dataExtractionService;

	@Inject
	private TableListRestorationService tableListRestorationService;

	@Inject
	private ThresholdConfigurationCheck thresholdConfigurationCheck;

	@Inject
	private CompanyDeterminationProcess companyDeterminationProcess;

	@Inject
	private TableItemValidation tableItemValidation;

	@Inject
	private EmployeeRestoration employeeRestoration;
	@Inject
	private ServerPrepareMngRepository serverPrepareMngRepository;

	// アルゴリズム「サーバー準備処理」を実行する
	@SuppressWarnings("unchecked")
	public ServerPrepareMng serverPreparationProcessing(ServerPrepareMng serverPrepareMng) {
		// アルゴリズム「ファイル解凍処理」を実行する
		serverPrepareMng = dataExtractionService.extractData(serverPrepareMng);
		if (checkNormalFile(serverPrepareMng)) {
			// ドメインモデル「データ復旧の実行」に新規に書き出す
			PerformDataRecovery performDataRecovery = new PerformDataRecovery(
					serverPrepareMng.getDataRecoveryProcessId(), AppContexts.user().companyId(),
					serverPrepareMng.getFileId().get(), serverPrepareMng.getUploadFileName().get());
			// アルゴリズム「テーブル一覧の復元」を実行する
			List<Object> restoreTableResult = tableListRestorationService.restoreTableList(serverPrepareMng);
			serverPrepareMng = (ServerPrepareMng) restoreTableResult.get(0);
			List<TableList> tableList = (List<TableList>) (restoreTableResult.get(1));
			if (checkNormalFile(serverPrepareMng)) {
				if (!tableList.isEmpty()) {
					if (tableList.get(0).getSurveyPreservation() == NotUseAtr.USE){
						serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.CAN_NOT_SAVE_SURVEY);
						serverPrepareMngRepository.update(serverPrepareMng);
					}
				}
				if (checkNormalFile(serverPrepareMng)) {
					// アルゴリズム「テーブル一覧の復元」を実行する
					serverPrepareMng = thresholdConfigurationCheck.checkFileConfiguration(serverPrepareMng, tableList);
					if (checkNormalFile(serverPrepareMng)) {
						// アルゴリズム「別会社判定処理」を実行する
						List<Object> sperateCompanyResult = companyDeterminationProcess
								.sperateCompanyDeterminationProcess(serverPrepareMng, performDataRecovery, tableList);
						serverPrepareMng = (ServerPrepareMng) sperateCompanyResult.get(0);
						performDataRecovery = (PerformDataRecovery) sperateCompanyResult.get(1);
						tableList = (List<TableList>) (sperateCompanyResult.get(2));
						if (checkNormalFile(serverPrepareMng)) {
							// アルゴリズム「テーブル項目チェック」を実行する
							serverPrepareMng = tableItemValidation.checkTableItem(serverPrepareMng, tableList);
							if (checkNormalFile(serverPrepareMng)) {
								// アルゴリズム「対象社員の復元」を実行する
								serverPrepareMng = employeeRestoration.restoreTargerEmployee(serverPrepareMng, performDataRecovery,
										tableList);
							}
						}
					}
				}
			}
		}
		serverPrepareMngRepository.update(serverPrepareMng);
		return serverPrepareMng;
	}

	private boolean checkNormalFile(ServerPrepareMng serverPrepareMng) {
		return serverPrepareMng.getOperatingCondition() == ServerPrepareOperatingCondition.CHECKING_FILE_STRUCTURE || serverPrepareMng.getOperatingCondition() ==ServerPrepareOperatingCondition.CHECKING_TABLE_ITEMS || serverPrepareMng.getOperatingCondition() ==ServerPrepareOperatingCondition.EXTRACTING;
	}
}
