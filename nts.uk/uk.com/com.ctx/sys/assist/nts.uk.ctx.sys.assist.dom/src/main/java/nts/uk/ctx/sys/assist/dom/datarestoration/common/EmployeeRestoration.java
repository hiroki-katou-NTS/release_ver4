package nts.uk.ctx.sys.assist.dom.datarestoration.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.google.common.base.Strings;

import nts.gul.security.crypt.commonkey.CommonKeyCrypt;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecovery;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecoveryRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.RestorationTarget;
import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareMng;
import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareMngRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareOperatingCondition;
import nts.uk.ctx.sys.assist.dom.datarestoration.Target;
import nts.uk.ctx.sys.assist.dom.storage.ResultOfSaving;
import nts.uk.ctx.sys.assist.dom.storage.ResultOfSavingRepository;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;

@Stateless
public class EmployeeRestoration {
	@Inject
	private PerformDataRecoveryRepository performDataRecoveryRepository;
	@Inject
	private ResultOfSavingRepository resultOfSavingRepository;
	@Inject
	private ServerPrepareMngRepository serverPrepareMngRepository;
	private static final String TARGET_CSV = "対象社員";
	private static final int FIRST_LINE = 0;

	// アルゴリズム「対象社員の復元」を実行する
	public ServerPrepareMng restoreTargerEmployee(ServerPrepareMng serverPrepareMng,
			PerformDataRecovery performDataRecovery, List<TableList> tableList) {
		List<List<String>> targetEmployee = CsvFileUtil.getAllRecord(serverPrepareMng.getFileId().get(), TARGET_CSV);
		if (targetEmployee.isEmpty()) {
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.EM_LIST_ABNORMALITY);
			serverPrepareMngRepository.update(serverPrepareMng);
			return serverPrepareMng;
		}

		// Restore to database
		List<Target> listTarget = new ArrayList<Target>();
		try {
			for (List<String> employeeInfo : targetEmployee.subList(1, targetEmployee.size())) {
				if(Strings.isNullOrEmpty(employeeInfo.get(2).trim())){
					listTarget.add(new Target(serverPrepareMng.getDataRecoveryProcessId(), employeeInfo.get(0),
							employeeInfo.get(1), employeeInfo.get(2)));
				}else{
					listTarget.add(new Target(serverPrepareMng.getDataRecoveryProcessId(), employeeInfo.get(0),
							employeeInfo.get(1), CommonKeyCrypt.decrypt(employeeInfo.get(2))));
				}
			}
		} catch (Exception e) {
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.EM_LIST_ABNORMALITY);
			serverPrepareMngRepository.update(serverPrepareMng);
			return serverPrepareMng;
		}
		try{
			performDataRecoveryRepository.addAllTargetEmployee(listTarget);
			int numOfPeopleRestore = 0;
			// Count number of employee and set
			int numPeopleSave = targetEmployee.size() - 1;
			Optional<String> saveProcessId = Optional.empty();
			Optional<ResultOfSaving> savingInfo = resultOfSavingRepository
					.getResultOfSavingById(tableList.get(FIRST_LINE).getDataStorageProcessingId());
			if (savingInfo.isPresent()) {
				numOfPeopleRestore = savingInfo.get().getTargetNumberPeople().orElse(null);
				saveProcessId = Optional.ofNullable(savingInfo.get().getStoreProcessingId());
			}
			performDataRecovery.setSaveProcessId(saveProcessId);
			performDataRecovery.setNumPeopleBeRestore(numOfPeopleRestore);
			performDataRecovery.setNumPeopleSave(numPeopleSave);
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.CHECK_COMPLETED);
			performDataRecoveryRepository.add(performDataRecovery);
			List<RestorationTarget> listRestorationTarget = RestorationTarget.createFromTableList(tableList,
					serverPrepareMng.getDataRecoveryProcessId());
			for (RestorationTarget restoreTarget : listRestorationTarget) {
				performDataRecoveryRepository.addRestorationTarget(restoreTarget);
			}
			serverPrepareMngRepository.update(serverPrepareMng);

			return serverPrepareMng;
		} catch (Exception e) {
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.EM_LIST_ABNORMALITY);
			return serverPrepareMng;
		}
	}
}
