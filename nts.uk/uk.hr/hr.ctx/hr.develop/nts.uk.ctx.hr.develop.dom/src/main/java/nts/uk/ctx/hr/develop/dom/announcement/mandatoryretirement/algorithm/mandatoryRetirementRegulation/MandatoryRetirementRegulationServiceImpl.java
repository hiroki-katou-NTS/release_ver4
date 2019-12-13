package nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.mandatoryRetirementRegulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.MandatoryRetireTerm;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.MandatoryRetirementRegulation;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.MandatoryRetirementRegulationRepository;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.PlanCourseApplyTerm;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.RetireDateTerm;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.RetirePlanCource;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.EmploymentInfoImport;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.EvaluationInfoDto;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.RetireDateTermParam;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.RetirePlanParam;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.RetirementCourseInformationDto;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.RetirementDateDto;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.dto.RetirementPlannedPersonDto;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.retirePlanCource.RetirePlanCourceService;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.primitiveValue.RetirementAge;
import nts.uk.ctx.hr.develop.dom.empregulationhistory.algorithm.EmploymentRegulationHistoryInterface;
import nts.uk.ctx.hr.shared.dom.dateTerm.DateCaculationTerm;
import nts.uk.ctx.hr.shared.dom.referEvaluationItem.ReferEvaluationItem;

@Stateless
public class MandatoryRetirementRegulationServiceImpl implements MandatoryRetirementRegulationService {

	@Inject
	private MandatoryRetirementRegulationRepository repo;
	
	@Inject
	private EmploymentRegulationHistoryInterface employmentRegulationHistoryInterface;
	
	@Inject
	private RetirePlanCourceService retirePlanCourceService;
	
	@Inject
	private SyEmploymentService syEmploymentService;
	
	@Override
	public Optional<MandatoryRetirementRegulation> getMandatoryRetirementRegulation(String companyId, String historyId) {
		return repo.getMandatoryRetirementRegulation(historyId, companyId);
	}

	@Override
	public void addMandatoryRetirementRegulation(String companyId, String historyId, int reachedAgeTerm,
			DateCaculationTerm publicTerm, RetireDateTerm retireDateTerm, boolean planCourseApplyFlg,
			List<MandatoryRetireTerm> mandatoryRetireTerm, List<ReferEvaluationItem> referEvaluationTerm,
			PlanCourseApplyTerm planCourseApplyTerm) {
		MandatoryRetirementRegulation domain = MandatoryRetirementRegulation.createFromJavaType(
				companyId, 
				historyId, 
				reachedAgeTerm, 
				publicTerm, 
				retireDateTerm, 
				planCourseApplyFlg, 
				mandatoryRetireTerm, 
				referEvaluationTerm, 
				planCourseApplyTerm);
		repo.add(domain);
	}

	@Override
	public void updateMandatoryRetirementRegulation(String companyId, String historyId, int reachedAgeTerm,
			DateCaculationTerm publicTerm, RetireDateTerm retireDateTerm, boolean planCourseApplyFlg,
			List<MandatoryRetireTerm> mandatoryRetireTerm, List<ReferEvaluationItem> referEvaluationTerm,
			PlanCourseApplyTerm planCourseApplyTerm) {
		MandatoryRetirementRegulation domain = MandatoryRetirementRegulation.createFromJavaType(
				companyId, 
				historyId, 
				reachedAgeTerm, 
				publicTerm, 
				retireDateTerm, 
				planCourseApplyFlg, 
				mandatoryRetireTerm, 
				referEvaluationTerm, 
				planCourseApplyTerm);
		repo.update(domain);
		
	}

	@Override
	public List<ReferEvaluationItem> getReferEvaluationItemByDate(String companyId, GeneralDate baseDate) {
		Optional<String> historyId = employmentRegulationHistoryInterface.getHistoryIdByDate(companyId, baseDate);
		if(!historyId.isPresent()) {
			throw new BusinessException("MsgJ_JMM018_2");
		}
		Optional<MandatoryRetirementRegulation> result = repo.getMandatoryRetirementRegulation(historyId.get(), companyId);
		if(!result.isPresent()) {
			throw new BusinessException("MsgJ_JMM018_2");
		}
		return result.get().getReferEvaluationTerm();
	}

	@Override
	public List<RetirementCourseInformationDto> getAppliedRetireCourseByDate(String companyId, GeneralDate baseDate) {
		Optional<String> historyId = employmentRegulationHistoryInterface.getHistoryIdByDate(companyId, baseDate);
		if(!historyId.isPresent()) {
			throw new BusinessException("MsgJ_JMM018_2");
		}
		Optional<MandatoryRetirementRegulation> mandatoryRetirementRegulation = repo.getMandatoryRetirementRegulation(historyId.get(), companyId);
		if(!mandatoryRetirementRegulation.isPresent()) {
			throw new BusinessException("MsgJ_JMM018_2");
		}
		mandatoryRetirementRegulation.get().getMandatoryRetireTerm().removeIf(c->c.isUsageFlg());
		
		//退職日条件
		RetireDateTerm retireDateTerm = mandatoryRetirementRegulation.get().getRetireDateTerm();
		
		//List<定年退職コース情報>{定年退職条件．共通マスタ項目ID、選択可能定年退職コース．定年退職コースID}
		List<MandatoryRetireTerm> mandatoryRetireTerm = mandatoryRetirementRegulation.get().getMandatoryRetireTerm();
		
		//List<定年退職コース>{定年退職コースID、定年退職コースCD、定年退職コース名、定年退職コース区分、定年年齢、継続区分}
		List<RetirePlanCource> retirePlanCource = retirePlanCourceService.getAllRetirePlanCource(companyId);
		if(retirePlanCource.isEmpty()) {
			throw new BusinessException("MsgJ_JMM018_6");
		}
		
		//・List<雇用情報>{雇用コード、雇用名称、グループ会社共通マスタ項目ID}
		List<EmploymentInfoImport> employmentInfoImport = syEmploymentService.getEmploymentInfo(companyId, Optional.of(true), Optional.of(false), Optional.of(false), Optional.of(false), Optional.of(true));
		if(employmentInfoImport.isEmpty()) {
			throw new BusinessException("MsgJ_JMM018_9");
		}
		
		for (EmploymentInfoImport employmentInfo : employmentInfoImport) {
			if(employmentInfo.empCommonMasterItemId == null) {
				throw new BusinessException("MsgJ_JMM018_10");
			}
		}
		
		List<RetirementCourseInformationDto> result = new ArrayList<>();
		for (EmploymentInfoImport employmentInfo : employmentInfoImport) {
			
			Optional<MandatoryRetireTerm> mandatoryRetireTer = mandatoryRetireTerm.stream().filter(c->c.getEmpCommonMasterItemId().equals(employmentInfo.empCommonMasterItemId)).findFirst();
			if(mandatoryRetireTer.isPresent()){
				break;
			}
			
		}
		
		return new ArrayList<>();
	}

	@Override
	public List<RetirementPlannedPersonDto> getMandatoryRetirementListByPeriodDepartmentEmployment(String companyId, GeneralDate baseDate,
			GeneralDate endDate, Optional<RetirementAge> retirementAge, List<String> departmentId,
			List<String> employmentCode) {
		return new ArrayList<>();
	}

	@Override
	public List<RetirementDateDto> getRetireDateBySidList(List<RetirePlanParam> retirePlan, RetirementAge retirementAge,
			RetireDateTermParam retireDateTerm, Optional<GeneralDate> endDate, List<GeneralDate> closingDate,
			List<GeneralDate> attendanceDate) {
		return new ArrayList<>();
	}

	@Override
	public EvaluationInfoDto getEvaluationInfoBySidList(List<String> retiredEmployeeId,
			List<ReferEvaluationItem> evaluationReferInfo) {
		return null;
		
	}

}
