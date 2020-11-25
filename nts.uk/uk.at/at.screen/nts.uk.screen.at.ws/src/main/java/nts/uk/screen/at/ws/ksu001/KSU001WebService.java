package nts.uk.screen.at.ws.ksu001;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.screen.at.app.ksu001.changepage.ChangePageParam;
import nts.uk.screen.at.app.ksu001.changepage.GetDataWhenChangePage;
import nts.uk.screen.at.app.ksu001.changepage.GetShiftPalChangePageResult;
import nts.uk.screen.at.app.ksu001.changeworkplace.ChangeWorkPlaceFinder;
import nts.uk.screen.at.app.ksu001.changeworkplace.ChangeWorkPlaceParam;
import nts.uk.screen.at.app.ksu001.getsendingperiod.ChangeMonthDto;
import nts.uk.screen.at.app.ksu001.getsendingperiod.ChangeMonthFinder;
import nts.uk.screen.at.app.ksu001.getsendingperiod.ChangePeriodModeFinder;
import nts.uk.screen.at.app.ksu001.getshiftpalette.GetShiftPalette;
import nts.uk.screen.at.app.ksu001.getshiftpalette.GetShiftPaletteParam;
import nts.uk.screen.at.app.ksu001.getshiftpalette.GetShiftPaletteResult;
import nts.uk.screen.at.app.ksu001.orderemployee.DataAfterSortEmpDto;
import nts.uk.screen.at.app.ksu001.orderemployee.GetDataAfterSortEmp;
import nts.uk.screen.at.app.ksu001.start.ChangeMonthParam;
import nts.uk.screen.at.app.ksu001.start.ChangePeriodModeParam;
import nts.uk.screen.at.app.ksu001.start.OrderEmployeeParam;
import nts.uk.screen.at.app.ksu001.start.StartKSU001;
import nts.uk.screen.at.app.ksu001.start.StartKSU001Dto;
import nts.uk.screen.at.app.ksu001.start.StartKSU001Param;
import nts.uk.screen.at.app.ksu001.validwhenedittime.ValidDataWhenEditTime;
import nts.uk.screen.at.app.ksu001.validwhenedittime.ValidDataWhenEditTimeParam;
import nts.uk.screen.at.app.ksu001.validwhenpaste.ValidDataWhenPaste;
import nts.uk.screen.at.app.ksu001.validwhenpaste.ValidDataWhenPasteParam;

/**
 * 
 * @author laitv
 *
 */
@Path("screen/at/schedule")
@Produces("application/json")
public class KSU001WebService extends WebService{

	@Inject
	private StartKSU001 startKSU001;
	@Inject
	private ChangeMonthFinder changeMonthFinder;
	@Inject
	private ChangePeriodModeFinder changePeriodModeFinder;
	@Inject
	private GetShiftPalette getShiftPalette;
	@Inject
	private GetDataWhenChangePage getDataWhenChangePage;
	@Inject
	private GetDataAfterSortEmp sortEmployees;
	@Inject
	private ValidDataWhenPaste valid;
	@Inject
	private ChangeWorkPlaceFinder changeWorkPlaceFinder;
	@Inject
	private ValidDataWhenEditTime validTime;
	
	@POST
	@Path("start")
	public StartKSU001Dto getDataStartScreen(StartKSU001Param param){
		StartKSU001Dto data = startKSU001.getData(param);
		return data;
	}
	
	@POST
	@Path("shift")
	public StartKSU001Dto getDataShiftMode(StartKSU001Param param){
		StartKSU001Dto data = startKSU001.getData(param);
		return data;
	}
	
	@POST
	@Path("shortname")
	public StartKSU001Dto getDataShortNameMode(StartKSU001Param param){
		StartKSU001Dto data = startKSU001.getData(param);
		return data;
	}
	
	@POST
	@Path("time")
	public StartKSU001Dto getDataTimeMode(StartKSU001Param param){
		StartKSU001Dto data = startKSU001.getData(param);
		return data;
	}
	
	@POST
	@Path("change-month")
	public ChangeMonthDto getDataNextMonth(ChangeMonthParam param){
		ChangeMonthDto data = changeMonthFinder.getData(param);
		return data;
	}
	
	@POST
	@Path("change-mode-period")
	public ChangeMonthDto getDataWhenChangeModePeriod(ChangePeriodModeParam param){
		ChangeMonthDto data = changePeriodModeFinder.getData(param);
		return data;
	}

	@POST
	@Path("getShiftPallets") // get cho cả 2 trường hợp company và workPlace , goi sau khi đ
	public GetShiftPaletteResult getShiftPallets(GetShiftPaletteParam param) {
		return getShiftPalette.getDataShiftPallet(param);
	}
	
	@POST
	@Path("change-page") // get cho cả 2 trường hợp company và workPlace , goi sau khi đ
	public GetShiftPalChangePageResult getShiftPallet(ChangePageParam param) {
		return getDataWhenChangePage.gatData(param);
	}

	@POST
	@Path("order-employee")
	public DataAfterSortEmpDto orderEmployee(OrderEmployeeParam param) {
		return sortEmployees.getData(param);
	}
	
	@POST
	@Path("valid-when-paste")
	public boolean validWhenPaste(List<ValidDataWhenPasteParam> shiftmasters) {
		return valid.valid(shiftmasters);
	}
	
	@POST
	@Path("valid-when-edit-time")
	public boolean validWhenEditTime(ValidDataWhenEditTimeParam param) {
		return validTime.valid(param);
	}
	
	@POST
	@Path("change-workplace") 
	public StartKSU001Dto getDataWhenChangeWkp(ChangeWorkPlaceParam param) {
		StartKSU001Dto data = changeWorkPlaceFinder.getData(param);
		return data;
	}
}