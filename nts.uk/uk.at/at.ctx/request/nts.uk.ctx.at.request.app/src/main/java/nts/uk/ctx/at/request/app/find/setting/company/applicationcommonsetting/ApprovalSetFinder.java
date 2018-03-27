package nts.uk.ctx.at.request.app.find.setting.company.applicationcommonsetting;

import java.util.Optional;

import javax.ejb.Stateless;
/**
 * 
 * @author yennth
 *
 */
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.ApprovalSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.ApprovalSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
@Stateless
public class ApprovalSetFinder {
	@Inject
	private ApplicationSettingRepository approRep;
	public ApprovalSetDto findByComId(){
		Optional<AppReflectAfterConfirm> appSet = approRep.getAppRef();
		if(appSet.isPresent()){
			return ApprovalSetDto.convertToDto(appSet.get());
		}
		return null;
	}
}
