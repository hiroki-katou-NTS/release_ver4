/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.manualsetting;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageMng;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageMngRepository;
import nts.uk.ctx.sys.assist.dom.storage.ManualSetOfDataSave;
import nts.uk.ctx.sys.assist.dom.storage.ManualSetOfDataSaveRepository;
import nts.uk.ctx.sys.assist.dom.storage.ManualSetOfDataSaveService;
import nts.uk.ctx.sys.assist.dom.storage.OperatingCondition;
import nts.uk.ctx.sys.assist.dom.storage.ResultOfSavingRepository;
import nts.uk.ctx.sys.assist.dom.storage.SaveStatus;
import nts.uk.ctx.sys.assist.dom.storage.SysEmployeeStorageAdapter;
import nts.uk.ctx.sys.assist.dom.storage.TargetCategory;
import nts.uk.ctx.sys.assist.dom.storage.TargetCategoryRepository;
import nts.uk.ctx.sys.assist.dom.storage.TargetEmployees;
import nts.uk.ctx.sys.assist.dom.storage.TargetEmployeesRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author nam.lh
 *
 */
@Stateless
public class AddManualSettingHandler extends AsyncCommandHandler<ManualSettingCommand> {
	@Inject
	private ManualSetOfDataSaveRepository manualSetOfDataSaveRepo;
	@Inject
	private TargetEmployeesRepository targetEmployeesRepo;
	@Inject
	private ManualSetOfDataSaveService manualSetOfDataSaveService;
	@Inject
	private DataStorageMngRepository dataStorageMngRepo;
	@Inject
	private TargetCategoryRepository repoTargetCat;
	@Inject
	private SysEmployeeStorageAdapter sysEmployeeStorageAdapter;
	@Inject
	private ResultOfSavingRepository repoResultSaving;

	@Override
	protected void handle(CommandHandlerContext<ManualSettingCommand> context) {

		String companyId = AppContexts.user().companyId();
		String employeeIDLogin = AppContexts.user().employeeId();
		ManualSettingCommand manualSetCmd = context.getCommand();
		String storeProcessingId = manualSetCmd.getStoreProcessingId();
		int totalTargetEmployees = 0;
		// ドメインモデル「データ保存動作管理」に登録する
		DataStorageMng dataStorageMng = new DataStorageMng(storeProcessingId, NotUseAtr.NOT_USE, 0, 0, 0,
				OperatingCondition.INPREPARATION);
		dataStorageMngRepo.add(dataStorageMng);
		try {
			List<TargetCategoryCommand> lstcategories = manualSetCmd.getCategory();
			List<TargetCategory> targetCategory = lstcategories.stream().map(item -> {
				return new TargetCategory(storeProcessingId, item.getCategoryId());
			}).collect(Collectors.toList());
			repoTargetCat.add(targetCategory);

			List<TargetEmployees> listTargetEmp = null;

			ManualSetOfDataSave domain = manualSetCmd.toDomain(companyId, storeProcessingId, employeeIDLogin);
			manualSetOfDataSaveRepo.addManualSetting(domain);

			// 画面の保存対象社員から「社員指定の有無」を判定する
			if (manualSetCmd.getPresenceOfEmployee() == 1) {
				// 指定社員の有無＝「する」
				listTargetEmp = domain.getEmployees();
			}

			if (manualSetCmd.getPresenceOfEmployee() == 0) {
				// 指定社員の有無＝「しない」の場合」
				listTargetEmp = sysEmployeeStorageAdapter.getListEmployeeByCompanyId(companyId);
				listTargetEmp.stream().map(x -> {
					x.setStoreProcessingId(storeProcessingId);
					return x;
				}).collect(Collectors.toList());
			}
			listTargetEmp.sort(Comparator.comparing(TargetEmployees::getScd));
			totalTargetEmployees = listTargetEmp.size();
			targetEmployeesRepo.addAll(listTargetEmp);
			manualSetOfDataSaveService.start(domain);
		} catch (Exception e) {
			e.printStackTrace();
			// ドメインモデル「データ保存動作管理」を更新する
			dataStorageMngRepo.update(storeProcessingId, OperatingCondition.ABNORMAL_TERMINATION);
			// ドメインモデル「データ保存の保存結果」を書き出し
			repoResultSaving.update(storeProcessingId, Optional.ofNullable(totalTargetEmployees), Optional.of(SaveStatus.FAILURE));
		}
	}
}
