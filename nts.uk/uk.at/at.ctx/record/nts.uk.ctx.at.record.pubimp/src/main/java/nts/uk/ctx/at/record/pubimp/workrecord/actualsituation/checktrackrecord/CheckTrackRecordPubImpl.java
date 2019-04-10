package nts.uk.ctx.at.record.pubimp.workrecord.actualsituation.checktrackrecord;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTargetItemDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTrackRecord;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.checktrackrecord.CheckTargetItemExport;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.checktrackrecord.CheckTrackRecordPub;

/**
 * 
 * @author tutk
 *
 */
@Stateless
public class CheckTrackRecordPubImpl implements CheckTrackRecordPub {

	@Inject
	private CheckTrackRecord checkTrackRecord;

	@Override
	public boolean checkTrackRecord(String companyId, String employeeId,
			List<CheckTargetItemExport> listCheckTargetItemExport) {
		return checkTrackRecord.checkTrackRecord(companyId, employeeId, listCheckTargetItemExport.stream()
				.map(c -> convertToCheckTargetItemExport(c)).collect(Collectors.toList()));
	}

	private CheckTargetItemDto convertToCheckTargetItemExport(CheckTargetItemExport checkTargetItemExport) {
		return new CheckTargetItemDto(checkTargetItemExport.getClosureId(), checkTargetItemExport.getYearMonth());
	}
}
