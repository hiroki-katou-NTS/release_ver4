package nts.uk.ctx.at.request.ac.workplace;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.EmployeeBasicInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.EmploymentHistoryImported;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpInfo;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkPlaceHistBySIDImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WkpByEmpExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class ReqWorkplaceAdapterImpl implements WorkplaceAdapter {

	@Inject
	private SyWorkplacePub wkpPub;

	/** The emp pub. */
	@Inject
	private SyEmploymentPub empPub;

	@Inject
	private SyEmployeePub syEmpPub;

	/**
	 * アルゴリズム「社員から職場を取得する」を実行する
	 */
	@Override
	public WkpHistImport findWkpBySid(String sID, GeneralDate date) {
		Optional<SWkpHistExport> wkpExport = wkpPub.findBySid(sID, date);
		if (wkpExport.isPresent()) {
			return toImport(wkpExport.get());
		}
		return null;
	}

	private WkpHistImport toImport(SWkpHistExport export) {
		return new WkpHistImport(export.getDateRange(), export.getEmployeeId(), export.getWorkplaceId(),
				export.getWorkplaceCode(), export.getWorkplaceName(), export.getWkpDisplayName());
	}

	@Override
	public Optional<EmploymentHistoryImported> getEmpHistBySid(String companyId, String employeeId,
			GeneralDate baseDate) {
		return this.empPub.findSEmpHistBySid(companyId, employeeId, baseDate)
				.map(f -> new EmploymentHistoryImported(f.getEmployeeId(), f.getEmploymentCode(), f.getPeriod()));
	}

	@Override
	public List<EmployeeBasicInfoImport> findBySIds(List<String> sIds) {

		return this.syEmpPub.findBySIds(sIds).stream()
				.map(x -> new EmployeeBasicInfoImport(x.getPId(), x.getEmployeeId(), Objects.isNull(x.getPName()) ? "" : x.getPName(), x.getGender(),
						x.getBirthDay(), Objects.isNull(x.getPMailAddr()) ? "" : x.getPMailAddr().v() , x.getEmployeeCode(),
						x.getEntryDate(), x.getRetiredDate(), Objects.isNull(x.getCompanyMailAddr()) ? "" : x.getCompanyMailAddr().v()))
				.collect(Collectors.toList());
	}

	@Override
	public WorkPlaceHistBySIDImport findWpkBySIDandPeriod(String sID, DatePeriod datePeriod) {
		WkpByEmpExport wkp = wkpPub.getLstHistByEmpAndPeriod(sID, datePeriod.start(), datePeriod.end());
		List<WkpInfo> lstWkpInfo = wkp.getLstWkpInfo().stream()
				.map(c-> new WkpInfo(c.getDatePeriod(), c.getWpkID(), c.getWpkCD(), c.getWpkName()))
				.collect(Collectors.toList());
		return new WorkPlaceHistBySIDImport(wkp.getEmployeeID(), lstWkpInfo);
	}

}
