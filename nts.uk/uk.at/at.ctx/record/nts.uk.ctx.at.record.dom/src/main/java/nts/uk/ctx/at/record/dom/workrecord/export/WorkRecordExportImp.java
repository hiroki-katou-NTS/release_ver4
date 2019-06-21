package nts.uk.ctx.at.record.dom.workrecord.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.AffiliationStatus;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.EmpAffInfoExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.PeriodInformation;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApplicationTypeAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class WorkRecordExportImp implements WorkRecordExport{

	@Inject
	private ClosureService closureService;
	
	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private ApplicationTypeAdapter applicationTypeAdapter;
	
	@Override
	public EmpAffInfoExport getAffiliationPeriod(List<String> listSid, YearMonthPeriod YMPeriod, GeneralDate baseDate) {

		String companyId = AppContexts.user().companyId();
		// 社員(list)に対応する処理締めを取得する (Lấy closure xử lý ứng với employee(list))
		Map<String, ClosureId> emplClosureId = new HashMap<>();
		for (String employeeId : listSid) {
			Closure closure = closureService.getClosureDataByEmployee(employeeId, baseDate);
			emplClosureId.put(employeeId, closure!=null?closure.getClosureId():null);
		}
		List<Closure> listClosures = closureRepository.findAll(companyId).stream().filter(c->c.getClosureId()!=null).collect(Collectors.toList());
		List<ClosureId> closureIds = new ArrayList<>();
		for (Map.Entry<String, ClosureId> closureId : emplClosureId.entrySet()) {
			if(closureId.getValue() == null) {
				closureIds.add(ClosureId.RegularEmployee);
			}else {
				closureIds.add(closureId.getValue());
			}
		}
		closureIds = closureIds.stream().distinct().collect(Collectors.toList());
		Map<ClosureId, DatePeriod> closureIdDatePeriods = new HashMap<>();
		for (ClosureId closureId : closureIds) {
			//chắc chắn tồn tại  ?? ThanhPV
			Closure closure = listClosures.stream().filter(c ->c.getClosureId().value == closureId.value).findFirst().get();
			
			List<DatePeriod> listStartDate = closure.getPeriodByYearMonth(YMPeriod.start());
			GeneralDate startDate = listStartDate.stream().sorted((d1, d2) -> d1.start().compareTo(d2.start())).collect(Collectors.toList()).get(0).start();
			
			List<DatePeriod> listEndDate = closure.getPeriodByYearMonth(YMPeriod.end());
			GeneralDate endDate = listEndDate.stream().sorted((d1, d2) -> d2.end().compareTo(d1.end())).collect(Collectors.toList()).get(0).end();
			
			closureIdDatePeriods.put(closureId, new DatePeriod(startDate, endDate));
		}
		
		Map<String, List<DatePeriod>> listAffComHistByListSidAndPeriods = new HashMap<>();
		for (Map.Entry<ClosureId, DatePeriod> closureIdDatePeriod : closureIdDatePeriods.entrySet()) {
			List<String> sid = emplClosureId.entrySet().stream().filter(c -> (c.getValue()==null?ClosureId.RegularEmployee:c.getValue()).equals(closureIdDatePeriod.getKey())).map(e ->e.getKey()).collect(Collectors.toList());
			listAffComHistByListSidAndPeriods.putAll(applicationTypeAdapter.getListAffComHistByListSidAndPeriod(sid, closureIdDatePeriod.getValue()));
		}
		List<AffiliationStatus> affiliationStatus = new ArrayList<>();
		for (Map.Entry<String, List<DatePeriod>> listAffComHistByListSidAndPeriod : listAffComHistByListSidAndPeriods.entrySet()) {
			ClosureId closureId = emplClosureId.get(listAffComHistByListSidAndPeriod.getKey());
			Closure closure = null;
			if(closureId == null) {
				closure = listClosures.stream().filter(c ->c.getClosureId().value == ClosureId.RegularEmployee.value).findFirst().get();
			}else {
				closure = listClosures.stream().filter(c ->c.getClosureId().value == closureId.value).findFirst().get();
			}
			List<PeriodInformation> periodInformation = new ArrayList<>();
			for (DatePeriod datePeriod : listAffComHistByListSidAndPeriod.getValue()) {
				Optional<ClosurePeriod> start = closure.getClosurePeriodByYmd(datePeriod.start());
				Optional<ClosurePeriod> end = closure.getClosurePeriodByYmd(datePeriod.end());
				if(!start.isPresent() || !end.isPresent()) {
					// すいません。システムエラーの部類になると思います。業務上想定していない現象なので、exeption等で対処してもらえませんか？uichida  大貴
					throw new BusinessException("");
				}
				periodInformation.add(new PeriodInformation(datePeriod, new YearMonthPeriod(start.get().getYearMonth(), end.get().getYearMonth())));
			}
			affiliationStatus.add(new AffiliationStatus(listAffComHistByListSidAndPeriod.getKey(), periodInformation, emplClosureId.get(listAffComHistByListSidAndPeriod.getKey())==null));
		}		
		
		return new EmpAffInfoExport(affiliationStatus);
	}
	

}
