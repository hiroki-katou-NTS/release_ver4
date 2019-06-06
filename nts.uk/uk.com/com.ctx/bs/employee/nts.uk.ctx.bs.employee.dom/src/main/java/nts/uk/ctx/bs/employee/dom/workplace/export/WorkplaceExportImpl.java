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
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;

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

	@Inject
	private WorkplaceConfigInfoRepository workplaceConfigInfoRepository;

	@Override
	public List<WkpInfoDto> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate) {
		List<WkpInfoDto> results = new ArrayList<>();
		// ドメインモデル「職場構成」を取得する
		Optional<WorkplaceConfig> optWorkplaceConfig = this.workplaceConfigRepository.findByBaseDate(companyId,
				baseDate);
		if (!optWorkplaceConfig.isPresent()) {
			return results;
		}
		// tai 1 ngay nhat dinh se chi lay duoc 1 histId
		String histId = optWorkplaceConfig.get().getWkpConfigHistory().get(0).identifier();
		// get WorkplaceConfigInfo de lay ra hierarchyCd
		// query theo companyId, histId va listWkpId nen
		// List<WorkplaceConfigInfo> se chi co toi da 1 data
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = this.workplaceConfigInfoRepository
				.findByHistoryIdsAndWplIds(companyId, Arrays.asList(histId), listWkpId);
		// ドメインモデル「職場情報」を取得する
		List<WorkplaceInfo> lstWorkplaceInfo = this.workplaceInfoRepository.findByWkpIdsAndHistIds(companyId, listWkpId,
				Arrays.asList(histId));

		if (listWkpId.size() != lstWorkplaceInfo.size()) {
			// 情報が取得できていない職場が存在する場合 TH tồn tại workplace chưa get được thông tin
			// get wkpId chua lay dc info
			List<String> lstWkpIdNoInfo = new ArrayList<>();
			listWkpId.forEach(wkpId -> {
				if (!lstWorkplaceInfo.stream().filter(x -> x.getWorkplaceId().equals(wkpId)).findFirst().isPresent()) {
					lstWkpIdNoInfo.add(wkpId);
				}
			});
			// [No.561]過去の職場の情報を取得する
			List<WkpInfoDto> lstWkpInfoDto = this.getPastWkpInfo(companyId, lstWkpIdNoInfo, histId);
			results.addAll(lstWkpInfoDto);
		}

		lstWorkplaceInfo.forEach(wkpInfo -> {
			Optional<WorkplaceHierarchy> optWorkplaceHierarchy = lstWorkplaceConfigInfo.get(0).getLstWkpHierarchy()
					.stream().filter(x -> x.getWorkplaceId().equals(wkpInfo.getWorkplaceId())).findFirst();
			String hierarchyCd = optWorkplaceHierarchy.isPresent() ? optWorkplaceHierarchy.get().getHierarchyCode().v()
					: null;
			WkpInfoDto wkpInfoDto = new WkpInfoDto(wkpInfo.getWorkplaceId(), wkpInfo.getWorkplaceCode().v(),
					wkpInfo.getWorkplaceName().v(), wkpInfo.getWkpGenericName().v(), wkpInfo.getWkpDisplayName().v(),
					wkpInfo.getOutsideWkpCode().v(), hierarchyCd);
			results.add(wkpInfoDto);
		});
		// 階層コード ASC
		results.sort((s1, s2) -> {
			if (s1.getHierarchyCd() == null) {
				return -1;
			}
			return s1.getHierarchyCd().compareTo(s2.getHierarchyCd());
		});

		return results;
	}

	@Override
	public List<WkpInfoDto> getPastWkpInfo(String companyId, List<String> listWkpId, String histId) {
		List<WkpInfoDto> results = new ArrayList<>();
		List<WorkplaceInfo> lstWorkplaceInfoAll = new ArrayList<>();
		// ドメインモデル「職場構成」を取得する
		Optional<WorkplaceConfig> optWorkplaceConfig = this.workplaceConfigRepository.findByHistId(companyId, histId);
		if (!optWorkplaceConfig.isPresent()) {
			return results;
		}
		// EAP yeu cau loop theo tu tuong lai -> qua khu
		optWorkplaceConfig.get().getWkpConfigHistory().sort((s1, s2) -> s1.span().isAfter(s2.span()) ? 1 : -1);
		List<String> listHistId = optWorkplaceConfig.get().getWkpConfigHistory().stream().map(x -> x.identifier())
				.collect(Collectors.toList());
		// query WorkplaceConfigInfo de lay hierarchyCd
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = this.workplaceConfigInfoRepository
				.findByHistoryIdsAndWplIds(companyId, listHistId, listWkpId);

		for (String hId : listHistId) {
			// ドメインモデル「職場情報」を取得する
			List<WorkplaceInfo> lstWorkplaceInfo = this.workplaceInfoRepository.findByWkpIdsAndHistIds(companyId,
					listWkpId, Arrays.asList(hId));
			lstWorkplaceInfoAll.addAll(lstWorkplaceInfo);
			// remove nhung wkpId da lay dc info
			lstWorkplaceInfo.forEach(wkpInfo -> {
				listWkpId.remove(wkpInfo.getWorkplaceId());
			});
			if (listWkpId.size() == 0) {
				break; // すべて取得できた / Get được tất cả
			}
		}
		// convert to WkpInfoDto
		lstWorkplaceInfoAll.forEach(wkpInfo -> {
			lstWorkplaceConfigInfo.forEach(x -> {
				if (x.getHistoryId().equals(wkpInfo.getHistoryId())) {
					// get hierarchyCd
					Optional<WorkplaceHierarchy> optWorkplaceHierarchy = x.getLstWkpHierarchy().stream()
							.filter(y -> y.getWorkplaceId().equals(wkpInfo.getWorkplaceId())).findFirst();
					String hierarchyCd = optWorkplaceHierarchy.isPresent()
							? optWorkplaceHierarchy.get().getHierarchyCode().v() : null;
					WkpInfoDto wkpInfoDto = new WkpInfoDto(wkpInfo.getWorkplaceId(), wkpInfo.getWorkplaceCode().v(),
							wkpInfo.getWorkplaceName().v(), wkpInfo.getWkpGenericName().v(),
							wkpInfo.getWkpDisplayName().v(), wkpInfo.getOutsideWkpCode().v(), hierarchyCd);
					results.add(wkpInfoDto);
				}
			});
		});

		if (listWkpId.size() > 0) {
			listWkpId.forEach(wkpId -> {
				// 職場名称、職場表示名、職場総称には「マスタ未登録」を固定でセットする
				// この処理でも取得できなかった職場には「コード削除済」を固定でセットする
				results.add(new WkpInfoDto(wkpId, "コード削除済", "マスタ未登録", "マスタ未登録", "マスタ未登録", "コード削除済", null));
			});
		}
		// 階層コード ASC
		results.sort((s1, s2) -> {
			if (s1.getHierarchyCd() == null) {
				return -1;
			}
			return s1.getHierarchyCd().compareTo(s2.getHierarchyCd());
		});

		return results;
	}
}
