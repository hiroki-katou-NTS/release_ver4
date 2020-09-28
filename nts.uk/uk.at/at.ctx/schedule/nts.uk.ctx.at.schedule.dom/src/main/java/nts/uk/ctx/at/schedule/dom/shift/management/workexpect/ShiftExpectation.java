package nts.uk.ctx.at.schedule.dom.shift.management.workexpect;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Value;
import nts.arc.layer.dom.objecttype.DomainValue;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;

/**
 * シフトの勤務希望
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.シフト管理.シフト勤務.勤務希望
 * @author dan_pv
 *
 */
@Value
public class ShiftExpectation implements WorkExpectation, DomainValue  {

	/**
	 * 勤務可能なシフトのリスト
	 */
	private List<ShiftMasterCode> workableShiftCodeList;

	/**
	 * 作る
	 * @param workableShiftCodeList 社員の勤務希望シフトリスト
	 * @return
	 */
	public static ShiftExpectation create(List<ShiftMasterCode> workableShiftCodeList) {

		if (workableShiftCodeList.isEmpty()) {
			throw new RuntimeException("workable shift code list is empty!");
		}

		return new ShiftExpectation(workableShiftCodeList);
	}

	@Override
	public AssignmentMethod getAssignmentMethod() {
		return AssignmentMethod.SHIFT;
	}

	@Override
	public boolean isMatchingExpectation(Require require, WorkInformation workInformation,
			List<TimeSpanForCalc> timeZoneList) {
		
		Optional<ShiftMaster> shiftMaster = require.getShiftMasterByWorkInformation(
				workInformation.getWorkTypeCode(),
				workInformation.getWorkTimeCode());
		
		if( !shiftMaster.isPresent()) {
			return false;
		}
		
		return this.workableShiftCodeList.stream()
				.anyMatch( code -> code.equals(shiftMaster.get().getShiftMasterCode()));
	}

	@Override
	public WorkExpectDisplayInfo getDisplayInformation(Require require) {
		List<String> shiftMasterNameList = require.getShiftMaster(this.workableShiftCodeList)
												.stream().map(shiftmaster -> shiftmaster.getDisplayInfor().getName().v())
												.collect(Collectors.toList());
		
		AssignmentMethod asignmentMethod = this.getAssignmentMethod();
		return new WorkExpectDisplayInfo(asignmentMethod, shiftMasterNameList, Collections.emptyList());
	}
	
}
