package nts.uk.screen.at.app.query.knr.knr002.f;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ModelEmpInfoTer;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.repo.EmpInfoTerminalRepository;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.TimeRecordSetFormatList;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.repo.TimeRecordSetFormatListRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
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
	//	タイムレコード設定フォーマットリストRepository.[3]  タイムレコード設定フォーマットリストを取得する
	@Inject
	private TimeRecordSetFormatListRepository timeRecordSetFormatListRepository;

	/**
	 * @param modelEmpInfoTer
	 * @return
	 */
	public List<ExtractRecoveryTargetTerminalDto> getRecoveryTargetList(int modelEmpInfoTer) {
		ContractCode contractCode = new ContractCode(AppContexts.user().contractCode());
		//	1. get*(契約コード、機種): 就業情報端末<List>
		List<EmpInfoTerminal> empInfoTerList = this.empInfoTerRepo.get(contractCode,
												ModelEmpInfoTer.valueOf(modelEmpInfoTer));
		if (empInfoTerList.isEmpty())
			return null;
		//	2. get*(契約コード、コード（List）): <機種名、ROMバージョン、機種区分>(List)
		
		return empInfoTerList.stream().map(e -> {
			ExtractRecoveryTargetTerminalDto dto = new ExtractRecoveryTargetTerminalDto();
			Optional<TimeRecordSetFormatList> timeRecordSetFormatList = this.timeRecordSetFormatListRepository.findSetFormat(e.getEmpInfoTerCode(), contractCode);
			
			dto.setWorkLocationCode(e.getCreateStampInfo().getWorkLocationCd().isPresent()?
									e.getCreateStampInfo().getWorkLocationCd().get().v() : "");
			dto.setEmpInfoTerCode(e.getEmpInfoTerCode().v());
			dto.setEmpInfoTerName(e.getEmpInfoTerName().v());
			dto.setModelEmpInfoTer(e.getModelEmpInfoTer().value);
			dto.setMacAddress(e.getMacAddress().v());
			dto.setIpAddress(e.getIpAddress().isPresent()?
							 e.getIpAddress().get().getFullIpAddress() : "");
			dto.setTerSerialNo(e.getTerSerialNo().isPresent() ?
							   e.getTerSerialNo().get().v() : "");
			dto.setIntervalTime(e.getIntervalTime().v());
			dto.setOutSupport(e.getCreateStampInfo().getConvertEmbCate().getOutSupport().value);
			dto.setReplace(e.getCreateStampInfo().getOutPlaceConvert().getReplace().value);
			dto.setGoOutReason(e.getCreateStampInfo().getOutPlaceConvert().getGoOutReason().isPresent()?
							   e.getCreateStampInfo().getOutPlaceConvert().getGoOutReason().get().value : null);
			dto.setEntranceExit(e.getCreateStampInfo().getConvertEmbCate().getEntranceExit().value);
			dto.setMemo(e.getEmpInfoTerMemo().isPresent()?
						e.getEmpInfoTerMemo().get().v() : "");
			if(!timeRecordSetFormatList.isPresent())
				return dto;
			TimeRecordSetFormatList timeRecordSetFormatListVal = timeRecordSetFormatList.get();
			dto.setModelClassification(timeRecordSetFormatListVal.getModelEmpInfoTer().value);
			dto.setModelName(ModelEmpInfoTer.valueOf(dto.getModelEmpInfoTer()).toString());
			dto.setRomVersion(timeRecordSetFormatListVal.getRomVersion().v());
			return dto;
		}).collect(Collectors.toList());
	}

}
