package nts.uk.file.at.app.export.setworkinghoursanddays;

import java.util.List;

import nts.uk.shr.infra.file.report.masterlist.data.MasterData;

public interface SetWorkingHoursAndDaysExRepository {
	List<MasterData> getCompanyExportData(int startDate, int endDate);
	
	List<MasterData> getEmploymentExportData(int startDate, int endDate);
	
	List<MasterData> getWorkPlaceExportData(int startDate, int endDate);
	
	List<MasterData> getEmployeeData(int startDate, int endDate);
	
	Object[] getUsage();
}