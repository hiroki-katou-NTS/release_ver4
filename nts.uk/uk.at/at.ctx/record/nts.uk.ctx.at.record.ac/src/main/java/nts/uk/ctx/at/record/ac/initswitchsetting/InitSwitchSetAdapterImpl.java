package nts.uk.ctx.at.record.ac.initswitchsetting;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetPub;
import nts.uk.ctx.at.record.dom.adapter.initswitchsetting.DateProcessedRecord;
import nts.uk.ctx.at.record.dom.adapter.initswitchsetting.InitSwitchSetAdapter;
import nts.uk.ctx.at.record.dom.adapter.initswitchsetting.InitSwitchSetDto;

/**
 * [RQ609]ログイン社員のシステム日時点の処理対象年月を取得する
 * 
 * @author sonnh1
 *
 */
@Stateless
public class InitSwitchSetAdapterImpl implements InitSwitchSetAdapter {

	@Inject
	private InitDisplayPeriodSwitchSetPub initDisplayPeriodSwitchSetPub;

	@Override
	public InitSwitchSetDto targetDateFromLogin() {
		InitDisplayPeriodSwitchSetDto dtoPub = initDisplayPeriodSwitchSetPub.targetDateFromLogin();
		InitSwitchSetDto result = new InitSwitchSetDto(dtoPub.getCurrentOrNextMonth(),
				dtoPub.getListDateProcessed().stream()
						.map(x -> new DateProcessedRecord(x.getClosureID(), x.getTargetDate(), x.getDatePeriod()))
						.collect(Collectors.toList()));
		return result;
	}

}
