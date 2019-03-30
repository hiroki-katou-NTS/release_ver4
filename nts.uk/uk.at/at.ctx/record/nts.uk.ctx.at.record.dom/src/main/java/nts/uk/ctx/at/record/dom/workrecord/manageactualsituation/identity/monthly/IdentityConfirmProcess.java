package nts.uk.ctx.at.record.dom.workrecord.manageactualsituation.identity.monthly;

import java.util.List;
//import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
//import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcessRepository;

@Stateless
public class IdentityConfirmProcess {

	@Inject
	IdentityProcessRepository identityProcessRepo;
	@Inject
	IdentificationRepository identificationRepository;
	/**
	 * 対象日の本人確認が済んでいるかチェックする
	 * @param employeeId: 社員ID
	 * @param lstDate: 対象日一覧：List＜年月日＞
	 * @return 対象日一覧の確認が済んでいる：boolean
	 */
	public boolean identityConfirmCheck(String cId, String employeeId, boolean checkIdentityOp,List<Identification> listIdentification, List<GeneralDate> lstDateCheck){
		//対応するドメインモデル「本人確認処理の利用設定」を取得する
		//取得したドメインモデル「本人確認処理の利用設定．日の本人確認を利用する」チェックする
//		Optional<IdentityProcess> identityOp = identityProcessRepo.getIdentityProcessById(cId);
//		if(!identityOp.isPresent())
		if(checkIdentityOp) {
			return true;
		}

		//対応するドメインモデル「日の本人確認」がすべて存在するかチェックする		
		return checkConfirm(employeeId,listIdentification, lstDateCheck);
	}
	
	public boolean checkConfirm(String employeeID, List<Identification> listIdentification,List<GeneralDate> lstDateCheck) {
		//ドメインモデル「日の本人確認」を取得する
		//List<Identification> listIdentification = identificationRepository.findByEmployeeID(employeeID, startDate, endDate);
		//取得した「日の本人確認」をもとにListを作成する
		
		if (listIdentification.size() >= lstDateCheck.size()) {
			return true;
		} else {
			return false;
		}
	}
}
