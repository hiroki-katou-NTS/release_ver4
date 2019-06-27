package nts.uk.ctx.sys.assist.dom.datarestoration.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareMng;
import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareMngRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.ServerPrepareOperatingCondition;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.dom.tablelist.TableListRepository;

@Stateless
public class TableListRestorationService {
	private static final String TABLELIST_CSV = "保存対象テーブル一覧";

	@Inject
	private ServerPrepareMngRepository serverPrepareMngRepository;

	@Inject
	private TableListRepository tableListRepository;
	
	@Inject
	private TableListCrudService tableListCrudService;

	// アルゴリズム「テーブル一覧の復元」を実行する
	public List<Object> restoreTableList(ServerPrepareMng serverPrepareMng) {
		List<TableList> tableList = new ArrayList<>();
		List<List<String>> tableListContent = CsvFileUtil.getAllRecord(serverPrepareMng.getFileId().get(),
				TABLELIST_CSV);
		// Csv no content or only header
		if (tableListContent.size() < 2) {
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.TABLE_LIST_FAULT);
			serverPrepareMngRepository.update(serverPrepareMng);
			return Arrays.asList(serverPrepareMng, tableList);
		}
		try {
			TableList tableListData = null;
			for (List<String> tableListSetting : tableListContent.subList(1, tableListContent.size())) {
				tableListData = TableList.createFromCsvData(tableListSetting);
				tableListData
						.setDataRecoveryProcessId(Optional.ofNullable(serverPrepareMng.getDataRecoveryProcessId()));
				tableList.add(tableListData);
				tableListCrudService.crudTableList(tableListData);
			}

		} catch (Exception e) {
			serverPrepareMng.setOperatingCondition(ServerPrepareOperatingCondition.TABLE_LIST_FAULT);
			serverPrepareMngRepository.update(serverPrepareMng);
		}
		return Arrays.asList(serverPrepareMng, tableList);
	}
}
