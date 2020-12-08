package nts.uk.screen.at.app.query.knr.knr002.f;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ModelEmpInfoTer;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.repo.EmpInfoTerminalRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocation;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * UKDesign.UniversalK.就業.KNR_就業情報端末.KNR002_就業情報端末の監視.Ｆ：復旧対象の選択ダイアログ.メニュー別OCD.Ｆ：復旧対象端末を抽出する
 * 
 * @author xuannt
 *
 */
@Stateless
public class ExtractRecoveryTargetTerminal {
	//	就業情報端末Repository.[7] 取得する
	@Inject
	private EmpInfoTerminalRepository empInfoTerRepo;
	@Inject
	private WorkLocationRepository workPlaceRepository;

	public List<ExtractRecoveryTargetTerminalDto> getRecoveryTargeTertList(int modelEmpInfoTer) {
		ContractCode contractCode = new ContractCode(AppContexts.user().contractCode());
		String companyID = AppContexts.user().companyId();

		List<EmpInfoTerminal> empInfoTerList = this.empInfoTerRepo.get(contractCode,
												ModelEmpInfoTer.valueOf(modelEmpInfoTer));
		if (null == empInfoTerList)
			return null;
		return empInfoTerList.stream().map(e -> {
			ExtractRecoveryTargetTerminalDto dto = new ExtractRecoveryTargetTerminalDto();
			String workLocationCD = e.getCreateStampInfo().getWorkLocationCd().isPresent()
					? e.getCreateStampInfo().getWorkLocationCd().get().v()
					: "";
			Optional<WorkLocation> workLocation = this.workPlaceRepository.findByCode(companyID, workLocationCD);
			dto.setWorkLocationCode(workLocationCD);
			dto.setEmpInfoTerCode(e.getEmpInfoTerCode().v());
			dto.setEmpInfoTerName(e.getEmpInfoTerName().v());
			dto.setModelEmpInfoTer(e.getModelEmpInfoTer().value);
			dto.setMacAddress(e.getMacAddress().v());
			dto.setIpAddress(e.getIpAddress().isPresent() ? e.getIpAddress().get().getFullIpAddress() : "");
			dto.setTerSerialNo(e.getTerSerialNo().isPresent() ? e.getTerSerialNo().get().v() : "");
			dto.setWorkLocationName(workLocation.isPresent() ? workLocation.get().getWorkLocationName().v() : "");
			dto.setIntervalTime(e.getIntervalTime().v());
			dto.setOutSupport(e.getCreateStampInfo().getConvertEmbCate().getOutSupport().value);
			dto.setReplace(e.getCreateStampInfo().getOutPlaceConvert().getReplace().value);
			dto.setGoOutReason(e.getCreateStampInfo().getOutPlaceConvert().getGoOutReason().isPresent()
					? e.getCreateStampInfo().getOutPlaceConvert().getGoOutReason().get().value
					: null);
			dto.setEntranceExit(e.getCreateStampInfo().getConvertEmbCate().getEntranceExit().value);
			dto.setMemo(e.getEmpInfoTerMemo().isPresent() ? e.getEmpInfoTerMemo().get().v() : "");
			return dto;
		}).collect(Collectors.toList());
	}

}
