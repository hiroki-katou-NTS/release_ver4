package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.app.find.employmentrole.InitDisplayPeriodSwitchSetFinder;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.DateProcessed;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTargetItemDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTrackRecord;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class KTG030QueryProcessor {
	@Inject
	private CheckTrackRecord checkTrackRecord;

	@Inject
	private ClosureService closureService;
	@Inject
	private InitDisplayPeriodSwitchSetFinder initDisplayPeriodSwitchSetFinder; 


	/**
	 * 月別実績確認すべきデータ有無取得
	 * 
	 * @author tutk
	 */
	public boolean checkDataMonPerConfirm(String employeeId) {
		String CID = AppContexts.user().companyId();
		// [RQ609]ログイン社員のシステム日時点の処理対象年月を取得する
		InitDisplayPeriodSwitchSetDto initDisplayPeriodSwitchSetDto = initDisplayPeriodSwitchSetFinder
				.targetDateFromLogin();
		List<DateProcessed> listDateProcessed = initDisplayPeriodSwitchSetDto.getListDateProcessed();
		// 基準日の会社の締めをすべて取得する
		List<ClosureInfo> listClosure = closureService.getAllClosureInfo();

		// 取得した「締め」からチェック対象を作成する
		List<CheckTargetItem> listCheckTargetItem = new ArrayList<>();
		for (DateProcessed dateProcess : listDateProcessed) {
			listCheckTargetItem.add(new CheckTargetItem(dateProcess.getClosureID(), dateProcess.getTargetDate()));
		}
		// CheckTargetOutPut checkTargetOutPut = new
		// CheckTargetOutPut(listCheckTargetItem);

		// 承認すべき月の実績があるかチェックする
		boolean result = checkTrackRecord.checkTrackRecord(CID, employeeId, listCheckTargetItem.stream()
				.map(c -> new CheckTargetItemDto(c.getClosureId(), c.getYearMonth())).collect(Collectors.toList()));
		return result;
	}
}
