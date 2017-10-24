package nts.uk.ctx.bs.employee.pubimp.employment.statusemployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.Employee;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeRepository;
import nts.uk.ctx.bs.employee.dom.employeeinfo.JobEntryHistory;
import nts.uk.ctx.bs.employee.dom.temporaryAbsence.TemporaryAbsence;
import nts.uk.ctx.bs.employee.dom.temporaryAbsence.TemporaryAbsenceRepository;
import nts.uk.ctx.bs.employee.pub.employment.statusemployee.StatusOfEmployment;
import nts.uk.ctx.bs.employee.pub.employment.statusemployee.StatusOfEmploymentExport;
import nts.uk.ctx.bs.employee.pub.employment.statusemployee.StatusOfEmploymentPub;

@Stateless
public class StatusOfEmploymentPubImp implements StatusOfEmploymentPub {

	@Inject
	private EmployeeRepository empRepo;

	@Inject
	private TemporaryAbsenceRepository temporaryAbsenceRepo;

	@Override
	public StatusOfEmploymentExport getStatusOfEmployment(String employeeId, GeneralDate referenceDate) {

		Optional<Employee> empOpt = empRepo.getBySid(employeeId);

		if (!empOpt.isPresent()) {
			return null;
		}

		StatusOfEmploymentExport statusOfEmploymentExport = new StatusOfEmploymentExport();
		statusOfEmploymentExport.setEmployeeId(employeeId);
		statusOfEmploymentExport.setRefereneDate(referenceDate);
		
		Employee employee = empOpt.get();

		// kiểm tra listEntryJobHist vói điều kiện baseDate
		// between(joinDate,RetirementDate)
		List<JobEntryHistory> listEntryJobHist = employee.getListEntryJobHist().stream()
				.filter(x -> (x.getJoinDate().beforeOrEquals(referenceDate)
						&& x.getRetirementDate().afterOrEquals(referenceDate)))
				.collect(Collectors.toList());

		if (listEntryJobHist.size() == 0) {

			// TH khong co du lieu 「入社履歴」thoa man dieu kien : 入社年月日 joinDate
			// <=parameter「baseDate」 <= RetirementDate 退職年月日

			List<GeneralDate> listJointDate = new ArrayList<>();
			// lấy toàn bộ ngày vào cty của employee
			for (int i = 0; i < listEntryJobHist.size(); i++) {
				listJointDate.add(listEntryJobHist.get(i).getJoinDate());
			}
			// lấy ngày vào cty đầu tiên
			GeneralDate firtJointDate = Collections.min(listJointDate);

			if (referenceDate.before(firtJointDate)) {
				// nêu ngày vào cty trước ngày baseDate
				statusOfEmploymentExport.setStatusOfEmployment(StatusOfEmployment.BEFORE_JOINING.value);

			} else {
				// trương hop nghi hưu
				statusOfEmploymentExport.setStatusOfEmployment(StatusOfEmployment.RETIREMENT.value);

			}
		} else {
			// TH co du lieu 「入社履歴」thoa man dieu kien : 入社年月日 joinDate <=parameter「baseDate」
			// <= RetirementDate 退職年月日

			// lấy domain 休職休業 TemporaryAbsence theo employeeId và referenceDate
			Optional<TemporaryAbsence> temporaryAbsOpt = temporaryAbsenceRepo.getBySid(employeeId, referenceDate);
			if (temporaryAbsOpt.isPresent()) {
				// tốn tại domain 
				TemporaryAbsence temporaryAbsenceDomain = temporaryAbsOpt.get();
				// set LeaveHolidayType 
				statusOfEmploymentExport.setLeaveHolidayType(temporaryAbsenceDomain.getTempAbsenceType().value);

				if (temporaryAbsenceDomain.getTempAbsenceType().value == 1) {
					// trường hợp 休職休業区分＝休職  LeaveHolidayState = TEMP_LEAVE(1)
					
					// StatusOfEmployment = LEAVE_OF_ABSENCE
					statusOfEmploymentExport.setStatusOfEmployment(StatusOfEmployment.LEAVE_OF_ABSENCE.value);
				} else {
					// StatusOfEmployment = HOLIDAY
					statusOfEmploymentExport.setStatusOfEmployment(StatusOfEmployment.HOLIDAY.value);
				}

			} else {
				// StatusOfEmployment = INCUMBENT
				statusOfEmploymentExport.setStatusOfEmployment(StatusOfEmployment.INCUMBENT.value);

			}

		}

		return statusOfEmploymentExport;
	}

}
