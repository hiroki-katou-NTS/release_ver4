package nts.uk.ctx.at.request.infra.repository.setting.stamp;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.stamp.StampRequestSetting;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.stamp.StampRequestSettingRepository;
import nts.uk.ctx.at.request.infra.entity.setting.stamp.KrqstStampRequestSetting;

@Stateless
public class JpaStampRequestSettingRepository extends JpaRepository implements StampRequestSettingRepository {

	@Override
	public StampRequestSetting findByCompanyID(String companyID) {
		Optional<StampRequestSetting> stampRequestSetting = this.queryProxy().find(companyID, KrqstStampRequestSetting.class).map(x -> toDomain(x));
		if(stampRequestSetting.isPresent()) return stampRequestSetting.get();
		else return null;
	}

	private StampRequestSetting toDomain(KrqstStampRequestSetting krqstStampRequestSetting){
		return new StampRequestSetting(
				krqstStampRequestSetting.companyID, 
				krqstStampRequestSetting.topComment, 
				krqstStampRequestSetting.topCommentFontColor, 
				krqstStampRequestSetting.topCommentFontWeight, 
				krqstStampRequestSetting.bottomComment, 
				krqstStampRequestSetting.bottomCommentFontColor, 
				krqstStampRequestSetting.bottomCommentFontWeight, 
				krqstStampRequestSetting.resultDisp, 
				krqstStampRequestSetting.supFrameDispNO, 
				krqstStampRequestSetting.stampPlaceDisp, 
				krqstStampRequestSetting.stampAtr_Work_Disp, 
				krqstStampRequestSetting.stampAtr_GoOut_Disp, 
				krqstStampRequestSetting.stampAtr_Care_Disp, 
				krqstStampRequestSetting.stampAtr_Sup_Disp, 
				krqstStampRequestSetting.stampGoOutAtr_Private_Disp, 
				krqstStampRequestSetting.stampGoOutAtr_Public_Disp, 
				krqstStampRequestSetting.stampGoOutAtr_Compensation_Disp, 
				krqstStampRequestSetting.stampGoOutAtr_Union_Disp);
	}
	
	private KrqstStampRequestSetting toEntity(StampRequestSetting stampRequestSetting){
		return new KrqstStampRequestSetting(
				stampRequestSetting.getCompanyID(), 
				stampRequestSetting.getTopComment(), 
				stampRequestSetting.getTopCommentFontColor(), 
				stampRequestSetting.getTopCommentFontWeight(), 
				stampRequestSetting.getBottomComment(), 
				stampRequestSetting.getBottomCommentFontColor(), 
				stampRequestSetting.getBottomCommentFontWeight(), 
				stampRequestSetting.getResultDisp(), 
				stampRequestSetting.getSupFrameDispNO(), 
				stampRequestSetting.getStampPlaceDisp(), 
				stampRequestSetting.getStampAtr_Work_Disp(), 
				stampRequestSetting.getStampAtr_GoOut_Disp(), 
				stampRequestSetting.getStampAtr_Care_Disp(), 
				stampRequestSetting.getStampAtr_Sup_Disp(), 
				stampRequestSetting.getStampGoOutAtr_Private_Disp(), 
				stampRequestSetting.getStampGoOutAtr_Public_Disp(), 
				stampRequestSetting.getStampGoOutAtr_Compensation_Disp(), 
				stampRequestSetting.getStampGoOutAtr_Union_Disp());
	}
}
