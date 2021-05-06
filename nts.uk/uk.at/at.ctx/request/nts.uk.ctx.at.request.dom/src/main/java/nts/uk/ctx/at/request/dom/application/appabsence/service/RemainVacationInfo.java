package nts.uk.ctx.at.request.dom.application.appabsence.service;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 休暇残数情報
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class RemainVacationInfo {
	
    // 年休管理
	private AnualLeaveManagement annualLeaveManagement;
	
	// 積休管理
	private AccumulatedRestManagement accumulatedRestManagement;
	
	// 代休管理
	private SubstituteLeaveManagement substituteLeaveManagement;
	
	// 振休管理
	private HolidayManagement holidayManagement;
	
	// 60H超休管理
	private Overtime60HManagement overtime60hManagement;
	
	// 介護看護休暇管理
	private NursingCareLeaveManagement nursingCareLeaveManagement;
	
	//年休残数
    private Optional<Integer> yearRemain;

    // 年休残時間
    private Optional<Integer> yearHourRemain;
    
    //代休残数
    private Optional<Integer> subHdRemain;
    
    //振休残数
    private Optional<Integer> subVacaRemain;
    
    // 代休残時間
    private Optional<Integer> subVacaHourRemain;
    
    // 積休残数
    private Optional<Integer> remainingHours;
    
    // 60H超休残時間
    private Optional<Integer> over60HHourRemain;
    
    // 子看護残数
    private Optional<Integer> childNursingRemain;
    
    // 子看護残時間
    private Optional<Integer> childNursingHourRemain;
    
    // 介護残数
    private Optional<Integer> nursingRemain;
    
    // 介護残時間
    private Optional<Integer> nirsingHourRemain;
}
