package nts.uk.screen.at.app.worktype;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkTypeProcessor {

	@Inject
	private WorkTypeQueryRepository workTypeQueryRepository;
	@Inject
	private SyEmployeePub syEmpPub;
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findHdShipKaf022(int oneDayAtr, int morningAtr2, List<Integer> afternoon2, List<Integer> morningAtr3, int afternoon3, int morningAtr4, List<Integer> afternoon4, List<Integer> morningAtr5, int afternoon5){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findHdShipKaf022(companyId, oneDayAtr, morningAtr2, afternoon2, morningAtr3, afternoon3, morningAtr4, afternoon4, morningAtr5, afternoon5);
	}
	
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findHdTimeKaf022(){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findHdTimeKaf022(companyId);
	}
	
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findBounceKaf022(List<Integer> halfDay){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findBounceKaf022(companyId, halfDay);
	}
	
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findWkChangeKaf022(){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findWkChangeKaf022(companyId);
	}
	
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findAbsenceKaf022(Integer oneDayAtr, Integer morningAtr, Integer afternoonAtr){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findAbsenceKaf022(companyId, oneDayAtr, morningAtr, afternoonAtr);
	}
	
	/**
	 * Find work type by company for KAF022
	 * @param companyId
	 * @return
	 * @author yennth
	 */
	public List<WorkTypeDto> findOtKaf022(){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findOtKaf022(companyId);
	}
	
	/**
	 * Find work type by company 
	 * @param companyId
	 * @return
	 */
	public WorkTypeOut findWorkTypeAll(){
		String companyId = AppContexts.user().companyId();
		List<WorkTypeDto> lstWorkType =  workTypeQueryRepository.findAllWorkType(companyId);
		//会社IDから使用する休業枠（休職を除く）を取得する - RQ546
		List<HdWorkDto> rq546 = syEmpPub.getTempAbsenceFrameByCid(companyId).stream()
				.map(c -> new HdWorkDto(c.getTempAbsenceFrNo(), c.getTempAbsenceFrName()))
				.collect(Collectors.toList());
		return new WorkTypeOut(lstWorkType, rq546);
	}
	
	/**
	 * Find work type by work type attribute
	 * @param workTypeAtr 分類
	 * @return
	 */
	public List<WorkTypeDto> findWorkTypeByDailyWorkType(List<Integer> workTypeAtr){
		String companyId = AppContexts.user().companyId();
		
		if (CollectionUtil.isEmpty(workTypeAtr)) {
			return Collections.emptyList();
		}
		
		return this.workTypeQueryRepository.findAllWorkType(companyId, workTypeAtr);
	}
	
	public List<WorkTypeDto> findWorkTypeDailyWorkType(List<Integer> workTypeAtr){
		String companyId = AppContexts.user().companyId();
		
		if (CollectionUtil.isEmpty(workTypeAtr)) {
			return Collections.emptyList();
		}
		
		return this.workTypeQueryRepository.findWorkType(companyId, workTypeAtr);
	}

	/**
	 * 
	 * @return
	 */
	public List<WorkTypeDto> findWorkTypeSpe(){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findAllWorkTypeSPE(companyId, DeprecateClassification.NotDeprecated.value, 
				WorkTypeClassification.SpecialHoliday.value, WorkTypeClassification.Absence.value);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<WorkTypeDto> findWorkTypeDisp(){
		String companyId = AppContexts.user().companyId();
		return this.workTypeQueryRepository.findAllWorkTypeDisp(companyId, DeprecateClassification.NotDeprecated.value);
	}
}
