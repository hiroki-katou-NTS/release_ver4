/**
 * 
 */
package nts.uk.ctx.at.auth.pubimp.initswitchsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSetRepo;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetPub;

/**
 * @author hieult
 *
 */
@Stateless
public class InitDisplayPeriodSwitchSetPubImpl implements InitDisplayPeriodSwitchSetPub{
 
	@Inject
	private InitDisplayPeriodSwitchSetRepo repo;
	
	/*@Inject
	private ClosureService closureService;*/
	@Override
	public Optional<InitDisplayPeriodSwitchSetDto> targetDateFromLogin() {
		//全締めの当月と期間を取得する
		
		return null;
	};
}
