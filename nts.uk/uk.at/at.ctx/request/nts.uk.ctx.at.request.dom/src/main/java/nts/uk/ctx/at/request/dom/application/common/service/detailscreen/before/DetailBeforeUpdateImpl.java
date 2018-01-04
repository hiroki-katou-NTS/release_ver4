package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;

@Stateless
public class DetailBeforeUpdateImpl implements DetailBeforeUpdate {
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithmService;
	
	@Inject
	private NewBeforeRegister newBeforeRegister;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	public void processBeforeDetailScreenRegistration(String companyID, String employeeID, GeneralDate appDate, int employeeRouteAtr, String appID, 
			PrePostAtr postAtr, Long version){

		// 選択した勤務種類の矛盾チェック(check sự mâu thuẫn của worktype đã chọn)
		// selectedWorkTypeConflictCheck();
		
		// アルゴリズム「確定チェック」を実施する(thực hiện xử lý 「確定チェック」)
		newBeforeRegister.confirmationCheck(companyID, employeeID, appDate);
		
		exclusiveCheck(companyID, appID, version);
	}
	
	public void exclusiveCheck(String companyID, String appID, Long version){
		Application_New application = applicationRepository.findByID(companyID, appID).get();
		if(!application.getVersion().equals(version)){
			throw new BusinessException("Msg_197");
		}
	}
}
