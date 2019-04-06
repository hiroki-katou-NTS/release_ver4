package nts.uk.ctx.at.record.pub.workrecord.actualsituation.checktrackrecord;

import java.util.List;

public interface CheckTrackRecordPub {
	
	/**
	 * RQ 594 : 承認すべき月の実績があるかチェックする
	 * @param companyId
	 * @param employeeId
	 * @param listCheckTargetItemExport
	 * @return
	 */
	boolean checkTrackRecord(String companyId, String employeeId,List<CheckTargetItemExport> listCheckTargetItemExport);
}
