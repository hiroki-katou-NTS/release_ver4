package nts.uk.ctx.bs.employee.dom.workplace.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.info.OutsideWorkplaceCode;
import nts.uk.ctx.bs.employee.dom.workplace.info.WkpCode;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceDisplayName;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceGenericName;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceName;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class WorkplaceExportImpl implements WorkplaceExport {

	@Inject
	private WorkplaceConfigRepository workplaceConfigRepository;

	@Inject
	private WorkplaceInfoRepository workplaceInfoRepository;

	@Override
	public List<WorkplaceInfo> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate) {
		// ドメインモデル「職場構成」を取得する
		Optional<WorkplaceConfig> optWorkplaceConfig = this.workplaceConfigRepository.findByBaseDate(companyId,
				baseDate);
		if (!optWorkplaceConfig.isPresent()) {
			return new ArrayList<>();
		}
		// truyen baseDate nen se chi lay duoc 1 histId
		String histId = optWorkplaceConfig.get().getWkpConfigHistory().get(0).identifier();
		// ドメインモデル「職場情報」を取得する
		List<WorkplaceInfo> lstWorkplaceInfo = this.workplaceInfoRepository.findByWkpIdsAndHistIds(companyId, listWkpId,
				Arrays.asList(histId));
		if (listWkpId.size() == lstWorkplaceInfo.size()) {
			// すべての職場の情報が取得できた場合 / TH get được thông tin của tất cả workplace
			return lstWorkplaceInfo;
		}
		// 情報が取得できていない職場が存在する場合 / TH tồn tại workplace chưa get được thông tin
		// get wkpId chua lay dc info
		List<String> lstWkpIdNoInfo = new ArrayList<>();
		listWkpId.forEach(wkpId -> {
			if (!lstWorkplaceInfo.stream().filter(x -> x.getWorkplaceId().equals(wkpId)).findFirst().isPresent()) {
				lstWkpIdNoInfo.add(wkpId);
			}
		});
		// [No.561]過去の職場の情報を取得する
		List<WorkplaceInfo> lstWkpInfo = this.getPastWkpInfo(companyId, lstWkpIdNoInfo, histId);
		lstWorkplaceInfo.addAll(lstWkpInfo);
		// 階層コード ASC - Do k co 階層コード nen k sap xep

		return lstWorkplaceInfo;
	}

	@Override
	public List<WorkplaceInfo> getPastWkpInfo(String companyId, List<String> listWkpId, String histId) {
		// ドメインモデル「職場構成」を取得する
		Optional<WorkplaceConfig> optWorkplaceConfig = this.workplaceConfigRepository.findByHistId(companyId, histId);
		if (!optWorkplaceConfig.isPresent()) {
			return new ArrayList<>();
		}
		// EAP yeu cau loop theo tu tuong lai -> qua khu
		optWorkplaceConfig.get().getWkpConfigHistory().sort((s1, s2) -> s1.span().isAfter(s2.span()) ? 1 : -1);
		List<String> listHistId = optWorkplaceConfig.get().getWkpConfigHistory().stream().map(x -> x.identifier())
				.collect(Collectors.toList());
		List<WorkplaceInfo> results = new ArrayList<>();

		for (String hId : listHistId) {
			// ドメインモデル「職場情報」を取得する
			List<WorkplaceInfo> lstWorkplaceInfo = this.workplaceInfoRepository.findByWkpIdsAndHistIds(companyId,
					listWkpId, Arrays.asList(hId));
			results.addAll(lstWorkplaceInfo);
			// remove nhung wkpId da lay dc info
			lstWorkplaceInfo.forEach(wkpInfo -> {
				listWkpId.remove(wkpInfo.getWorkplaceId());
			});
			if (listWkpId.size() == 0) {
				break; // すべて取得できた / Get được tất cả
			}
		}

		if (listWkpId.size() > 0) {
			listWkpId.forEach(wkpId -> {
				// 職場名称、職場表示名、職場総称には「マスタ未登録」を固定でセットする
				// この処理でも取得できなかった職場には「コード削除済」を固定でセットする
				results.add(new WorkplaceInfo(companyId, histId, wkpId, new WkpCode("コード削除済"),
						new WorkplaceName("マスタ未登録"), new WorkplaceGenericName("マスタ未登録"),
						new WorkplaceDisplayName("マスタ未登録"), new OutsideWorkplaceCode("コード削除済")));
			});
		}
		// 階層コード ASC - Do k co 階層コード nen k sap xep
		return results;
	}
}
