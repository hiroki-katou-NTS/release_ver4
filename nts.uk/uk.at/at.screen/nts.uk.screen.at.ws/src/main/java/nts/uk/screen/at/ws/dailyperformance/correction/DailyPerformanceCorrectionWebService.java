/**
 * 5:57:43 PM Aug 28, 2017
 */
package nts.uk.screen.at.ws.dailyperformance.correction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import lombok.val;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.ProducedRequest;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.find.dailyperformanceformat.DailyPerformanceAuthoritySetting;
import nts.uk.ctx.at.function.app.find.dailyperformanceformat.MonthlyPerfomanceAuthorityFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.screen.at.app.dailymodify.command.DailyCalculationCommandFacade;
import nts.uk.screen.at.app.dailymodify.command.DailyModifyResCommandFacade;
import nts.uk.screen.at.app.dailymodify.command.PersonalTightCommandFacade;
import nts.uk.screen.at.app.dailyperformance.correction.DPUpdateColWidthCommandHandler;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DisplayRemainingHolidayNumber;
import nts.uk.screen.at.app.dailyperformance.correction.UpdateColWidthCommand;
import nts.uk.screen.at.app.dailyperformance.correction.calctime.DailyCorrectCalcTimeService;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemParent;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCalculationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DataResultAfterIU;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpAndDate;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ErrorReferenceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.HolidayRemainNumberDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.calctime.DCCalcTime;
import nts.uk.screen.at.app.dailyperformance.correction.dto.calctime.DCCalcTimeParam;
import nts.uk.screen.at.app.dailyperformance.correction.flex.CalcFlexDto;
import nts.uk.screen.at.app.dailyperformance.correction.flex.CheckBeforeCalcFlex;
import nts.uk.screen.at.app.dailyperformance.correction.kdw003b.DailyPerformErrorReferDto;
import nts.uk.screen.at.app.dailyperformance.correction.kdw003b.DailyPerformErrorReferExportDto;
import nts.uk.screen.at.app.dailyperformance.correction.kdw003b.DailyPerformErrorReferExportService;
import nts.uk.screen.at.app.dailyperformance.correction.kdw003b.DailyPerformErrorReferFinder;
import nts.uk.screen.at.app.dailyperformance.correction.loadupdate.DPLoadRowProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.loadupdate.DPPramLoadRow;
import nts.uk.screen.at.app.dailyperformance.correction.lock.button.DPDisplayLockParam;
import nts.uk.screen.at.app.dailyperformance.correction.lock.button.DPDisplayLockProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.searchemployee.DPEmployeeSearchData;
import nts.uk.screen.at.app.dailyperformance.correction.searchemployee.FindEmployeeBase;
import nts.uk.screen.at.app.dailyperformance.correction.selecterrorcode.DailyPerformanceErrorCodeProcessor;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author hungnm
 *
 */
@Path("screen/at/correctionofdailyperformance")
@Produces("application/json")
public class DailyPerformanceCorrectionWebService {

	@Inject
	private DailyPerformanceCorrectionProcessor processor;
	
	@Inject
	private DailyPerformanceErrorCodeProcessor errorProcessor;
	
	@Inject
	private DPUpdateColWidthCommandHandler commandHandler;
	
	@Inject
	private DataDialogWithTypeProcessor dialogProcessor;
	
	@Inject
	private DailyModifyResCommandFacade dailyModifyResCommandFacade;
	
	@Inject
	private DataDialogWithTypeProcessor dataDialogWithTypeProcessor;
	
	@Inject
	private PersonalTightCommandFacade personalTightCommandFacade;
	
	@Inject
	private CheckBeforeCalcFlex checkBeforeCalcFlex;
	
	@Inject
	private DPLoadRowProcessor loadRowProcessor;
	
	@Inject
	private DailyPerformErrorReferExportService dailyPerformErrorExportService;

	@Inject
	private DailyPerformErrorReferFinder dailyPerforErrorReferFinder;
	
	@Inject
	private FindEmployeeBase findEmployeeBase;
	
	@Inject
	private DailyCorrectCalcTimeService dailyCorrectCalcTimeService;
	
	@Inject
	private DailyCalculationCommandFacade dailyCalculationService;
	
	@Inject
	private DisplayRemainingHolidayNumber remainNumberService;

	@Inject
	private DPDisplayLockProcessor dpDisplayLockProcessor;
	
	@Inject
	private DailyPerformanceAuthoritySetting dailyPerformanceAuthoritySetting;
	
	@Inject
	private MonthlyPerfomanceAuthorityFinder monthlyPerfomanceAuthorityFinder;
	
	@Inject
	@ProducedRequest
	private HttpServletRequest httpRequest;
	
	@POST
	@Path("startScreen")
	public DailyPerformanceCorrectionDto startScreen(DPParams params ) throws InterruptedException{
		DailyPerformanceCorrectionDto dtoResult = this.processor.generateData(params.dateRange, params.lstEmployee, params.initScreen, params.mode, params.displayFormat, params.correctionOfDaily, params.formatCodes, params.showError, params.showLock, params.objectShare);
		HttpSession session = httpRequest.getSession();
		session.setAttribute("domainOlds", dtoResult.getDomainOld());
		session.setAttribute("domainEdits", null);
		session.setAttribute("itemIdRCs", dtoResult.getLstControlDisplayItem() == null ? null : dtoResult.getLstControlDisplayItem().getMapDPAttendance());
		session.setAttribute("dataSource", dtoResult.getLstData());
		
		dtoResult.setDomainOld(Collections.emptyList());
		return dtoResult;
	}
	
	@POST
	@Path("errorCode")
	public DailyPerformanceCorrectionDto condition(DPParams params ) throws InterruptedException{
		val results = this.errorProcessor.generateData(params.dateRange, params.lstEmployee, params.initScreen, params.mode, params.displayFormat, params.correctionOfDaily, params.errorCodes, params.formatCodes, params.showLock);
		HttpSession session = httpRequest.getSession();
		session.setAttribute("domainOlds", results.getDomainOld());
		session.setAttribute("domainEdits", null);
		session.setAttribute("itemIdRCs", results.getLstControlDisplayItem() == null ? null : results.getLstControlDisplayItem().getMapDPAttendance());
		session.setAttribute("dataSource", results.getLstData());
		results.setDomainOld(Collections.emptyList());
		return results;
	}
	
	@POST
	@Path("getErrors")
	public List<ErrorReferenceDto> getError(DPParams params ) {
		return this.processor.getListErrorRefer(params.dateRange, params.lstEmployee);
	}
	
	@POST
	@Path("updatecolumnwidth")
	public void getError(UpdateColWidthCommand command){
		this.commandHandler.handle(command);
	}
	
	@POST
	@Path("findCodeName")
	public CodeName findCodeName(DPParamDialog param){
		return this.dialogProcessor.getTypeDialog(param.getTypeDialog(), param.getParam());
	}
	
	@POST
	@Path("findAllCodeName")
	public List<CodeName> findAllCodeName(DPParamDialog param){
		return this.dialogProcessor.getAllTypeDialog(param.getTypeDialog(), param.getParam());
	}
	
	@POST
	@Path("addAndUpdate")
	@SuppressWarnings("unchecked")
	public DataResultAfterIU addAndUpdate(DPItemParent dataParent) {
		HttpSession session = httpRequest.getSession();
		val domain  = session.getAttribute("domainEdits");
		List<DailyRecordDto> dailyEdits = new ArrayList<>();
		if(domain == null){
			dailyEdits = cloneListDto((List<DailyRecordDto>) session.getAttribute("domainOlds"));
		}else{
			dailyEdits = (List<DailyRecordDto>) domain;
		}
		dataParent.setDailyEdits(dailyEdits);
		dataParent.setDailyOlds((List<DailyRecordDto>) session.getAttribute("domainOlds"));
		dataParent.setLstAttendanceItem((Map<Integer, DPAttendanceItem>) session.getAttribute("itemIdRCs"));
        return dailyModifyResCommandFacade.insertItemDomain(dataParent);
	}
	
	@POST
	@Path("insertClosure")
	public void insertClosure(EmpAndDate empAndDate){
		personalTightCommandFacade.insertPersonalTight(empAndDate.getEmployeeId(), empAndDate.getDate());
	}
	
	@POST
	@Path("releaseClosure")
	public JavaTypeResult<String> releaseClosure(EmpAndDate empAndDate){
		return new JavaTypeResult<String>(personalTightCommandFacade.releasePersonalTight(empAndDate.getEmployeeId(), empAndDate.getDate()));
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    final Set<Object> seen = new HashSet<>();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	@POST
	@Path("getApplication")
	public List<EnumConstant> getApplicationName() {
		return dataDialogWithTypeProcessor.getNameAppliction();
	}
	
	@POST
	@Path("getFlexCheck")
	public JavaTypeResult<String>  getValueTimeFlex(CalcFlexDto calc) {
		return new JavaTypeResult<String>(checkBeforeCalcFlex.getConditionCalcFlex(calc));
	}
	
	@POST
	@Path("loadRow")
	@SuppressWarnings("unchecked")
	public DailyPerformanceCorrectionDto reloadRow(DPPramLoadRow param) {
		HttpSession session = httpRequest.getSession();
		val domain  = session.getAttribute("domainEdits");
		List<DailyRecordDto> dailyEdits = new ArrayList<>();
		if(domain == null){
			dailyEdits = (List<DailyRecordDto>) session.getAttribute("domainOlds");
		}else{
			dailyEdits = (List<DailyRecordDto>) domain;
		}
		param.setDailys(dailyEdits);
		val result = loadRowProcessor.reloadGrid(param);
		session.setAttribute("domainEdits", null);
		if(!param.getOnlyLoadMonth())session.setAttribute("domainOlds", result.getDomainOld());
		result.setDomainOld(Collections.emptyList());
		return result;
	}
	
	@POST
	@Path("exportCsv")
	public ExportServiceResult exportCsvErrorInfor(List<DailyPerformErrorReferExportDto> command) {
		return this.dailyPerformErrorExportService.start(command);
	}

	@POST
	@Path("getErrAndAppTypeCd")
	public DailyPerformErrorReferDto findByCidAndListErrCd(List<String> listErrorCode) {
		return this.dailyPerforErrorReferFinder.findByCidAndListErrCd(listErrorCode);
	}
	
	/**
	 * typeOfAttendanceItem = 0 to case is monthly
	 * 
	 * @param dailyAttendanceItemIds
	 * @return
	 */
	@POST
	@Path("getNameMonthlyAttItem")
	public List<AttItemName> getNameOfMonthlyAttendanceItem(List<Integer> monthlyAttendanceItemIds) {
		return this.monthlyPerfomanceAuthorityFinder.getListAttendanceItemName(monthlyAttendanceItemIds);
	}
	
	@POST
	@Path("getnameattItembytype/{type}")
	public List<AttItemName> getNameOfAttendanceItemByType(@PathParam(value = "type") int type) {
		return this.monthlyPerfomanceAuthorityFinder.getListAttendanceItemNameByType(type);
	}
	
	@POST
	@Path("getNamedailyAttItem")
	public List<AttItemName> getNameOfDailyAttendanceItem(List<Integer> dailyAttendanceItemIds) {
		return this.dailyPerformanceAuthoritySetting.getListAttendanceItemName(dailyAttendanceItemIds);
	}
	

	@POST
	@Path("get-info/{employeeId}")
	public DPEmployeeSearchData getInfo(@PathParam(value = "employeeId") String employeeId) {
		return findEmployeeBase.findInAllEmployee(employeeId, GeneralDate.today(), AppContexts.user().companyId()).orElse(null);
	}
	
	@POST
	@Path("calcTime")
	@SuppressWarnings("unchecked")
	public DCCalcTime calcTime(DCCalcTimeParam dcTimeParam) {
		HttpSession session = httpRequest.getSession();
		val domain  = session.getAttribute("domainEdits");
		List<DailyRecordDto> dailyEdits = new ArrayList<>();
		if(domain == null){
			dailyEdits = cloneListDto((List<DailyRecordDto>) session.getAttribute("domainOlds"));
		}else{
			dailyEdits = (List<DailyRecordDto>) domain;
		}
		
		val result = dailyCorrectCalcTimeService.calcTime(dailyEdits, dcTimeParam.getItemEdits(), dcTimeParam.getChangeSpr31(), dcTimeParam.getChangeSpr34());
		session.setAttribute("domainEdits", result.getDailyEdits());
		result.setDailyEdits(Collections.emptyList());
		return result;
	}
	
	@POST
	@Path("calculation")
	@SuppressWarnings("unchecked")
	public DailyPerformanceCalculationDto calculation(DPItemParent dataParent) {
		HttpSession session = httpRequest.getSession();
		val domain  = session.getAttribute("domainEdits");
		List<DailyRecordDto> dailyEdits = new ArrayList<>();
		if(domain == null){
			dailyEdits = cloneListDto((List<DailyRecordDto>) session.getAttribute("domainOlds"));
		}else{
			dailyEdits = (List<DailyRecordDto>) domain;
		}
		dataParent.setDailyEdits(dailyEdits);
		dataParent.setDailyOlds((List<DailyRecordDto>) session.getAttribute("domainOlds"));
		
		dataParent.setLstAttendanceItem((Map<Integer, DPAttendanceItem>) session.getAttribute("itemIdRCs"));
		dataParent.setLstData((List<DPDataDto>) session.getAttribute("dataSource"));
		
		val result = dailyCalculationService.calculateCorrectedResults(dataParent);
		session.setAttribute("domainEdits", result.getCalculatedRows());
		result.setCalculatedRows(Collections.emptyList());
		return result;
	}
	
	@POST
	@Path("getRemainNum/{employeeId}")
	public HolidayRemainNumberDto getRemainNumb(@PathParam(value = "employeeId") String employeeId) {
		return remainNumberService.getRemainingHolidayNumber(employeeId);
	}

	@POST
	@Path("lock")
	public DailyPerformanceCorrectionDto processLock(DPDisplayLockParam param) {
		return dpDisplayLockProcessor.processDisplayLock(param);
	}

	private List<DailyRecordDto> cloneListDto(List<DailyRecordDto> dtos){
		return dtos.stream().map(x -> x.clone()).collect(Collectors.toList());
	}
}
