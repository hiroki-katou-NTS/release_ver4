package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;

@Stateless
public class CheckConfirmService {
	
	@Inject
	private IdentificationRepository identificationRepository;
	
	public List<StateConfirm> checkConfirm(String employeeID,GeneralDate startDate,GeneralDate endDate) {
		
		List<StateConfirm> listStateConfirm = new  ArrayList<>();
		//ドメインモデル「日の本人確認」を取得する
		List<Identification> listIdentification = identificationRepository.findByEmployeeID(employeeID, startDate, endDate);
		//取得した「日の本人確認」をもとにListを作成する
		for(GeneralDate dateBegin = startDate;dateBegin.after(endDate);dateBegin.addDays(1)) {
			boolean checkExist = false;
			for(Identification identification : listIdentification) {
				if(dateBegin.equals(identification.getProcessingYmd())){
					checkExist = true;
					break;
				}
			}
			listStateConfirm.add(new StateConfirm(dateBegin,checkExist));
		}
		
		
		return listStateConfirm;
	}

}
