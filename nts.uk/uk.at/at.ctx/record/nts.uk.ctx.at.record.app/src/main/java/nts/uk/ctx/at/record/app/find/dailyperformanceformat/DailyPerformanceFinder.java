package nts.uk.ctx.at.record.app.find.dailyperformanceformat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.dailyperformanceformat.dto.AttendanceItemDto;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.dto.BusinessTypeDetailDto;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.dto.BusinessTypeFormatDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.dto.BusinessTypeFormatDetailDto;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatMonthly;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypeFormatMonthlyRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
public class DailyPerformanceFinder {

	@Inject
	private AttendanceItemsFinder attendanceItemsFinder;

	@Inject
	private BusinessTypeDailyDetailFinder businessTypeDailyDetailFinder;

	@Inject
	private BusinessTypeFormatMonthlyRepository workTypeFormatMonthlyRepository;

	public BusinessTypeDetailDto findAll(String businessTypeCode, BigDecimal sheetNo) {
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();

		// 勤怠項目 - find attendance item
		List<AttendanceItemDto> attendanceItemDtos = this.attendanceItemsFinder.find();
		if(attendanceItemDtos.isEmpty()){
			BusinessTypeDetailDto businessTypeDetailDto = new BusinessTypeDetailDto(null, null, null);
			return businessTypeDetailDto;
		}
		Map<Integer, AttendanceItemDto> attendanceItemMaps = attendanceItemDtos.stream().collect(
				Collectors.toMap(AttendanceItemDto::getAttendanceItemId, x->x));

		// find daily detail
		// BusinessTypeFormatDailyDto businessTypeFormatDailyDto = new
		// BusinessTypeFormatDailyDto(null, null, null);
		// if (sheetNo == null) {
		// businessTypeFormatDailyDto = new BusinessTypeFormatDailyDto(null,
		// null, null);
		// } else {
		BusinessTypeFormatDailyDto businessTypeFormatDailyDto = businessTypeDailyDetailFinder
				.getDetail(businessTypeCode, sheetNo);
		// }

		// find monthly detail
		List<BusinessTypeFormatMonthly> businessTypeFormatMonthlies = this.workTypeFormatMonthlyRepository
				.getMonthlyDetail(companyId, businessTypeCode);
		List<BusinessTypeFormatDetailDto> businessTypeFormatMonthlyDtos = new ArrayList<BusinessTypeFormatDetailDto>();
		if (businessTypeFormatMonthlies.isEmpty()) {
			businessTypeFormatMonthlyDtos = new ArrayList<>();
		}

		businessTypeFormatMonthlyDtos = businessTypeFormatMonthlies.stream().map(f -> {
			if (attendanceItemMaps.containsKey(f.getAttendanceItemId()))
				return new BusinessTypeFormatDetailDto(f.getAttendanceItemId(), attendanceItemMaps.get(f.getAttendanceItemId()).getAttendanceItemDisplayNumber(),
						attendanceItemMaps.get(f.getAttendanceItemId()).getAttendanceItemName(), f.getOrder(), f.getColumnWidth());
			return null;
		}).collect(Collectors.toList());

		BusinessTypeDetailDto businessTypeDetail = new BusinessTypeDetailDto(attendanceItemDtos,
				businessTypeFormatDailyDto, businessTypeFormatMonthlyDtos);

		return businessTypeDetail;
	}

}
