package nts.uk.ctx.at.record.dom.remainingnumber.paymana;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class FurikyuMngDataExtractionService {
	@Inject
	private SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepository;
	
	@Inject
	private PayoutManagementDataRepository payoutManagementDataRepository;
	
	@Inject
	private SysEmploymentHisAdapter sysEmploymentHisAdapter;
	
	@Inject
	private EmpSubstVacationRepository empSubstVacationRepository;
	
	@Inject
	private ComSubstVacationRepository comSubstVacationRepository;
	
	@Inject 
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	public FurikyuMngDataExtractionData getFurikyuMngDataExtraction(String sid, GeneralDate startDate, GeneralDate endDate, boolean isPeriod) {
		String cid = AppContexts.user().companyId();
		List<PayoutManagementData> payoutManagementData;
		List<SubstitutionOfHDManagementData> substitutionOfHDManagementData;
		Double numberOfDayLeft;
		int expirationDate;
		Integer closureId;
		
		// select 過去の結果
		if(isPeriod) {
			payoutManagementData = payoutManagementDataRepository.getBySidDatePeriodNoDigestion(sid, startDate, endDate);
			substitutionOfHDManagementData = substitutionOfHDManaDataRepository.getBySidDatePeriodNoRemainDay(sid, startDate, endDate);
		// select 現在の残数状況
		} else {
			payoutManagementData = payoutManagementDataRepository.getSidWithCod(cid, sid, 0);
			substitutionOfHDManagementData = substitutionOfHDManaDataRepository.getBySidRemainDayAndInPayout(sid);
		}
		
		numberOfDayLeft = getNumberOfDayLeft(sid);
		expirationDate = getExpirationDate(sid);
		closureId = getClosureId(sid);
		
		return new FurikyuMngDataExtractionData(payoutManagementData, substitutionOfHDManagementData, expirationDate, numberOfDayLeft, closureId);
	}
	
	public Double getNumberOfDayLeft(String sID) {
		String cid = AppContexts.user().companyId();
		Double totalUnUseDay = 0.0;
		Double  totalUndeliveredDay = 0.0;
		
		List<PayoutManagementData> payoutManagementData = payoutManagementDataRepository.getSidWithCod(cid, sID, 0);
		List<SubstitutionOfHDManagementData> substitutionOfHDManagementData = substitutionOfHDManaDataRepository.getBysiDRemCod(cid, sID);
		
		for (PayoutManagementData item : payoutManagementData) {
			totalUnUseDay += item.getUnUsedDays().v();
		}
		
		for (SubstitutionOfHDManagementData item : substitutionOfHDManagementData) {
			totalUndeliveredDay += item.getRemainDays().v();
		}
		
		return totalUndeliveredDay - totalUnUseDay;
	}
	
	public int getExpirationDate(String sid) {
		String cid = AppContexts.user().companyId();
		String sCD;
		EmpSubstVacation empSubstVacation;
		ComSubstVacation comSubstVacation;
		int expirationDate = 0;
		
		// get scd
		if (sysEmploymentHisAdapter.findSEmpHistBySid(cid, sid, GeneralDate.legacyDate(new Date())).isPresent()) {
			sCD = sysEmploymentHisAdapter.findSEmpHistBySid(cid, sid, GeneralDate.legacyDate(new Date())).get().getEmploymentCode();
			
			if(empSubstVacationRepository.findById(cid, sCD).isPresent()) {
				empSubstVacation = empSubstVacationRepository.findById(cid, sCD).get();
				expirationDate = empSubstVacation.getSetting().getExpirationDate().value;
			} else if (comSubstVacationRepository.findById(cid).isPresent()){
				comSubstVacation = comSubstVacationRepository.findById(cid).get();
				expirationDate = comSubstVacation.getSetting().getExpirationDate().value;
			}
		} else {
			if (comSubstVacationRepository.findById(cid).isPresent()){
				comSubstVacation = comSubstVacationRepository.findById(cid).get();
				expirationDate = comSubstVacation.getSetting().getExpirationDate().value;
			}
		}
		
		return expirationDate;
	}
	
	public Integer getClosureId(String sid) {
		String cid = AppContexts.user().companyId();
		String sCD;
		Integer closureId = null;
		
		if (sysEmploymentHisAdapter.findSEmpHistBySid(cid, sid, GeneralDate.legacyDate(new Date())).isPresent()) {
			sCD = sysEmploymentHisAdapter.findSEmpHistBySid(cid, sid, GeneralDate.legacyDate(new Date())).get().getEmploymentCode();
			
			if(closureEmploymentRepository.findByEmploymentCD(cid, sCD).isPresent()) {
				closureId = closureEmploymentRepository.findByEmploymentCD(cid, sCD).get().getClosureId();
			}
		}
		
		return closureId;
	}
}
