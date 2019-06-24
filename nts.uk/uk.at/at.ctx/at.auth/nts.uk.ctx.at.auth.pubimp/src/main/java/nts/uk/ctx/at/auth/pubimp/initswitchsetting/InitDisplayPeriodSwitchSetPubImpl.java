/**
 * 
 */
package nts.uk.ctx.at.auth.pubimp.initswitchsetting;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.app.find.employmentrole.InitDisplayPeriodSwitchSetFinder;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.auth.pub.initswitchsetting.DateProcessed;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetEx;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetPub;

/**
 * @author hieult
 *
 */
@Stateless
public class InitDisplayPeriodSwitchSetPubImpl implements InitDisplayPeriodSwitchSetPub {

	@Inject
	private InitDisplayPeriodSwitchSetFinder  finder;

	@Override
	public InitDisplayPeriodSwitchSetEx targetDateFromLogin() {
		InitDisplayPeriodSwitchSetDto dto = finder.targetDateFromLogin();
		
		//List<DateProcessed>
		InitDisplayPeriodSwitchSetEx data = new InitDisplayPeriodSwitchSetEx();
		data.setCurrentOrNextMonth(dto.getCurrentOrNextMonth());
		data.setListDateProcessed(dto.getListDateProcessed().stream()
				.map(c -> new DateProcessed(c.getClosureID(), c.getTargetDate(), c.getDatePeriod()))
				.collect(Collectors.toList()));
		return data;
		
	}

}
