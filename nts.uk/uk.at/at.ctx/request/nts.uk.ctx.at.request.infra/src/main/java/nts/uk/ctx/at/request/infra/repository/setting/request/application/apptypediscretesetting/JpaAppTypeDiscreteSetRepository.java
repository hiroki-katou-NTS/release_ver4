package nts.uk.ctx.at.request.infra.repository.setting.request.application.apptypediscretesetting;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.common.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.UseAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AllowAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.CheckMethod;
import nts.uk.ctx.at.request.dom.setting.request.application.common.PossibleAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RetrictDay;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RetrictPreTimeDay;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstAppTypeDiscrete;

@Stateless
public class JpaAppTypeDiscreteSetRepository extends JpaRepository implements AppTypeDiscreteSettingRepository {

	public final String SELECT_NO_WHERE = "SELECT c FROM KrqstAppTypeDiscrete c";

	public final String SELECT_WITH_CID = SELECT_NO_WHERE + " WHERE c.krqstAppTypeDiscretePK.companyID := companyID";

	public final String SELECT_WITH_APP_TYPE = SELECT_NO_WHERE
			+ " WHERE c.krqstAppTypeDiscretePK.companyID := companyID"
			+ " AND c.krqstAppTypeDiscretePK.appType := appType ";

//	private AppTypeDiscreteSetting toDomain(KrqstAppTypeDiscrete entity) {
//		return new AppTypeDiscreteSetting(
//				entity.krqstAppTypeDiscretePK.companyID, 
//				EnumAdaptor.valueOf(entity.krqstAppTypeDiscretePK.appType,ApplicationType.class),
//				EnumAdaptor.valueOf(entity.prePostInitAtr,InitValueAtr.class),
//				EnumAdaptor.valueOf(entity.prePostCanChangeFlg, AppCanAtr.class),
//				EnumAdaptor.valueOf(entity.typicalReasonDisplayFlg, AppDisplayAtr.class),
//				EnumAdaptor.valueOf(entity.sendMailWhenApprovalFlg,AppCanAtr.class), 
//				EnumAdaptor.valueOf(entity.sendMailWhenRegisterlFlg, AppCanAtr.class),
//				EnumAdaptor.valueOf(entity.displayReasonFlg, AppDisplayAtr.class),
//				EnumAdaptor.valueOf(entity.retrictPreMethodFlg, CheckMethod.class),
//				EnumAdaptor.valueOf(entity.retrictPreUseFlg, UseAtr.class),
//				EnumAdaptor.valueOf(entity.retrictPreDay, RetrictDay.class),
//				EnumAdaptor.valueOf(entity.retrictPreTimeDay, RetrictPreTimeDay.class),
//				EnumAdaptor.valueOf(entity.retrictPreCanAcceptFlg, PossibleAtr.class),
//				EnumAdaptor.valueOf(entity.retrictPostAllowFutureFlg,AllowAtr.class);
//	}

	@Override
	public Optional<AppTypeDiscreteSetting> getAppTypeDiscreteSettingByAppType(String companyID, int appType) {
		// TODO Auto-generated method stub
		return null;
	}

}
