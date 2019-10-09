package nts.uk.ctx.at.record.ac.initswitchsetting;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.app.find.employmentrole.InitDisplayPeriodSwitchSetFinder;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.InitDisplayPeriodSwitchSetDto;
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
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class InitSwitchSetAdapterImpl implements InitSwitchSetAdapter {

	@Inject
	private InitDisplayPeriodSwitchSetPub initDisplayPeriodSwitchSetPub;

	@Inject
	private InitDisplayPeriodSwitchSetFinder  finder;
	/*@Inject
	private InitDisplayPeriodSwitchSetPubImpl initDisplayPeriodSwitchSetPubImpl;*/
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public InitSwitchSetDto targetDateFromLogin() {
		InitDisplayPeriodSwitchSetDto dtoPub = finder.targetDateFromLogin();
		InitSwitchSetDto result = new InitSwitchSetDto(dtoPub.getCurrentOrNextMonth(),
				dtoPub.getListDateProcessed().stream()
						.map(x -> new DateProcessedRecord(x.getClosureID(), x.getTargetDate(), x.getDatePeriod()))
						.collect(Collectors.toList()));
		return result;
		
	}

}
