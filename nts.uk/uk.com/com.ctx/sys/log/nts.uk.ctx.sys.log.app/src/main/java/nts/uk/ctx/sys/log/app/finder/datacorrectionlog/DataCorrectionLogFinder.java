package nts.uk.ctx.sys.log.app.finder.datacorrectionlog;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.dom.logbasicinfo.LogBasicInfoRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class DataCorrectionLogFinder {

	@Inject
	private DataCorrectionLogRepository correctionLogRepo;

	@Inject
	private LogBasicInfoRepository basicInfoRepo;

	public List<DataCorrectionLogDto> getDataLog(DataCorrectionLogParams params) {
		List<DataCorrectionLogDto> result = new ArrayList<>();
//		String companyId = AppContexts.user().companyId();
//		String loginEmpId = AppContexts.user().employeeId();
//		List<LogBasicInformation> listBasicInfo = basicInfoRepo.getAllLogBasicInfo(companyId, loginEmpId);
//		List<DataCorrectionLog> listCorrectionLog = new ArrayList<>();
//		List<String> listOperationId = listBasicInfo.stream().map(i -> i.getOperationId()).collect(Collectors.toList());
//		if (params.getStartYmd() != null && params.getEndYmd() != null) {
//			listCorrectionLog = correctionLogRepo.getAllLogData(listOperationId, params.getListEmployeeId(),
//					new DatePeriod(params.getStartYmd(), params.getEndYmd()));
//		} else if (params.getStartYm() != null && params.getEndYm() != null) {
//			listCorrectionLog = correctionLogRepo.getAllLogData(listOperationId, params.getListEmployeeId(),
//					new YearMonthPeriod(new YearMonth(params.getStartYm()), new YearMonth(params.getEndYm())));
//		} else {
//			listCorrectionLog = correctionLogRepo.getAllLogData(listOperationId, params.getListEmployeeId(),
//					Year.of(params.getStartY()), Year.of(params.getEndY()));
//		}
//		for (LogBasicInformation i : listBasicInfo) {
//			List<DataCorrectionLog> tmpList = listCorrectionLog.stream()
//					.filter(d -> d.getOperationId().equals(i.getOperationId())).collect(Collectors.toList());
//			for (DataCorrectionLog d : tmpList) {
//				DataCorrectionLogDto log = new DataCorrectionLogDto(d.getTargetDataKey().getDateKey().get(),
//						d.getTargetUser().getUserName(), d.getCorrectedItem().getName(),
//						d.getCorrectedItem().getValueBefore().getViewValue(),
//						d.getCorrectedItem().getValueAfter().getViewValue(), i.getUserInfo().getUserName(),
//						i.getModifiedDateTime(), d.getCorrectionAttr().value);
//				result.add(log);
//			}
//		}
		if (params.getDisplayFormat() == 0) {// by date
			for (int i = 0; i < 500; i++) {
				for (int j = 0; j < params.getListEmployeeId().size(); j++) {
					for (int k = 0; k < 4; k++) {
						DataCorrectionLogDto log = new DataCorrectionLogDto(GeneralDate.ymd(2018, 5, (i % 28) + 1),
								"User" + j, "Item" + k, "0900", "1508", "Admin", GeneralDateTime.now(), i % 3);
						result.add(log);
					}
				}
			}
		} else { // by individual
			for (int i = 0; i < 500; i++) {
				for (int j = 0; j < params.getListEmployeeId().size(); j++) {
					for (int k = 0; k < 4; k++) {
						DataCorrectionLogDto log = new DataCorrectionLogDto(GeneralDate.ymd(2018, 5, (i % 28) + 1),
								"User" + i, "Item" + k, "0900", "1508", "Admin", GeneralDateTime.now(), i % 3);
						result.add(log);

					}
				}
			}
		}
		return result;
	}
}
