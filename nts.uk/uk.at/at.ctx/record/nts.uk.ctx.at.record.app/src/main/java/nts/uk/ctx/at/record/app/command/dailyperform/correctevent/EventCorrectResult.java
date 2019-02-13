package nts.uk.ctx.at.record.app.command.dailyperform.correctevent;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.DailyModifyRCResult;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.enu.DailyDomainGroup;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

@NoArgsConstructor
@Getter
public class EventCorrectResult {

	private DailyRecordDto base;
	
	@Setter
	private DailyRecordDto corrected;
	
	private DailyModifyRCResult updated;
	
	private List<DailyDomainGroup> correctedType;
	
	public List<ItemValue> getCanBeCorrectedItems(){
		List<Integer> canBeUpdated = AttendanceItemIdContainer.getItemIdByDailyDomains(correctedType);
		
		return AttendanceItemUtil.toItemValues(corrected, canBeUpdated);
	}
	
	public List<ItemValue> getCorrectedItemsWithStrict(){
		List<Integer> canBeUpdated = AttendanceItemIdContainer.getItemIdByDailyDomains(correctedType, (type, path) -> {
			if(type == DailyDomainGroup.BREAK_TIME) {
				return path.contains(ItemConst.E_WORK_REF);
			}
			
			return true;
		});
		
		List<ItemValue> canBeCorrected = AttendanceItemUtil.toItemValues(corrected, canBeUpdated);
		List<ItemValue> beforeCorrect = AttendanceItemUtil.toItemValues(base, canBeUpdated);
		
		canBeCorrected.removeAll(beforeCorrect);
		
		return canBeCorrected;
	}

	public EventCorrectResult(DailyRecordDto base, DailyRecordDto corrected, DailyModifyRCResult updated,
			List<DailyDomainGroup> correctedType) {
		super();
		this.base = base;
		this.corrected = corrected;
		this.updated = updated;
		this.correctedType = correctedType;
	}
	
	public void removeEditStatesForCorrectedItem() {
		List<Integer> canbeCorrected = getCorrectedItemsWithStrict().stream().map(bc -> bc.getItemId()).collect(Collectors.toList());
		
		this.corrected.getEditStates().removeIf(es -> canbeCorrected.contains(es.getAttendanceItemId()));
	}
}
