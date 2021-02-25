package nts.uk.ctx.at.function.app.find.attendancerecord.export;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.attendancerecord.export.AttendanceRecordExport;
import nts.uk.ctx.at.function.dom.attendancerecord.export.AttendanceRecordExportRepository;

/**
 * The Class AttendanceRecordExportFinder.
 */
@Stateless
public class AttendanceRecordExportFinder {

	/** The attendance record exp repo. */
	@Inject
	AttendanceRecordExportRepository attendanceRecordExpRepo;

	/**
	 * Gets the all attendance record export.
	 *
	 * @param companyId
	 *            the company id
	 * @param code
	 *            the code
	 * @return the all attendance record export
	 */
	public List<AttendanceRecordExportDto> getAllAttendanceRecordExportDaily(String companyId, long code) {

		// Get list of domain
		List<AttendanceRecordExport> domainList = attendanceRecordExpRepo.getAllAttendanceRecordExportDaily(companyId,
				code);

		// Convert domain to Dto

		List<AttendanceRecordExportDto> dtoList = new ArrayList<AttendanceRecordExportDto>();

		if (!domainList.isEmpty())
			domainList.forEach(item -> {
				AttendanceRecordExportDto dto = new AttendanceRecordExportDto();
				dto.setColumnIndex(item.getColumnIndex());
				dto.setExportAtr(item.getExportAtr().value);
				dto.setUserAtr(item.getUseAtr());
				if (item.getUpperPosition().isPresent())
					dto.setUpperPosition(item.getUpperPosition().get().getNameDisplay());
				else
					dto.setUpperPosition("");
				if (item.getLowerPosition().isPresent())
					dto.setLowwerPosition(item.getLowerPosition().get().getNameDisplay());
				else
					dto.setLowwerPosition("");

				dtoList.add(dto);

			});

		return dtoList;
	}

	/**
	 * Gets the all attendance record export monthly.
	 *
	 * @param companyId
	 *            the company id
	 * @param code
	 *            the code
	 * @return the all attendance record export monthly
	 */
	public List<AttendanceRecordExportDto> getAllAttendanceRecordExportMonthly(String companyId, long code) {

		// Get list of domain
		List<AttendanceRecordExport> domainList = attendanceRecordExpRepo.getAllAttendanceRecordExportMonthly(companyId,
				code);

		// Convert domain to Dto

		List<AttendanceRecordExportDto> dtoList = new ArrayList<AttendanceRecordExportDto>();
		if (!domainList.isEmpty())
			domainList.forEach(item -> {
				AttendanceRecordExportDto dto = new AttendanceRecordExportDto();
				dto.setColumnIndex(item.getColumnIndex());
				dto.setExportAtr(item.getExportAtr().value);
				dto.setUserAtr(item.getUseAtr());
				
				if (item.getUpperPosition().isPresent())
					dto.setUpperPosition(item.getUpperPosition().get().getNameDisplay());
				else
					dto.setUpperPosition("");
				
				if (item.getLowerPosition().isPresent())
					dto.setLowwerPosition(item.getLowerPosition().get().getNameDisplay());
				else
					dto.setLowwerPosition("");

				dtoList.add(dto);

			});

		return dtoList;
	}

}
