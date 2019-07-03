package nts.uk.file.at.app.export.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.export.WorkRecordExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.AffiliationStatus;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.EmpAffInfoExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.PeriodInformation;
import nts.uk.screen.at.app.schedule.service.ScreenService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class FileServiceImpl implements FileService{

	@Inject
	private ScreenService service;
	
	@Inject
	private WorkRecordExport workRecordExport;
	
	@Override
	public Optional<GeneralDate> getProcessingYM(String cId, int closureId) {
		return service.getProcessingYM(cId, closureId);
	}

	@Override
	public Map<String, YearMonthPeriod> getAffiliationPeriod(List<String> listSid, YearMonthPeriod period, GeneralDate baseDate) {
		EmpAffInfoExport e = workRecordExport.getAffiliationPeriod(listSid, period, baseDate);
		Map<String, YearMonthPeriod> result = new HashMap<>();
		for (AffiliationStatus emp : e.getAffiliationStatus()) {
			nts.arc.time.YearMonth start = emp.getPeriodInformation().get(0).getYearMonthPeriod().start();
			nts.arc.time.YearMonth end = emp.getPeriodInformation().get(0).getYearMonthPeriod().end();
			for (PeriodInformation infor : emp.getPeriodInformation()) {
				if(infor.getYearMonthPeriod().start().lessThan(start)) {
					start = infor.getYearMonthPeriod().start();
				}
				if(infor.getYearMonthPeriod().end().lessThan(end)) {
					end = infor.getYearMonthPeriod().end();
				}
			}
			result.put(emp.getEmployeeID(), new YearMonthPeriod(start, end));
		}
		
		return result;
	}

	@Override
	public Map<String, DatePeriod> getAffiliationDatePeriod(List<String> listSid, YearMonthPeriod period, GeneralDate baseDate) {
		EmpAffInfoExport e = workRecordExport.getAffiliationPeriod(listSid, period, baseDate);
		Map<String, DatePeriod> result = new HashMap<>();
		for (AffiliationStatus emp : e.getAffiliationStatus()) {
			GeneralDate start = emp.getPeriodInformation().get(0).getDatePeriod().start();
			GeneralDate end = emp.getPeriodInformation().get(0).getDatePeriod().end();
			for (PeriodInformation infor : emp.getPeriodInformation()) {
				if(infor.getDatePeriod().start().before(start)) {
					start = infor.getDatePeriod().start();
				}
				if(infor.getDatePeriod().end().after(end)) {
					end = infor.getDatePeriod().end();
				}
			}
			result.put(emp.getEmployeeID(), new DatePeriod(start, end));
		}
		
		return result;
	}
	
	

}
