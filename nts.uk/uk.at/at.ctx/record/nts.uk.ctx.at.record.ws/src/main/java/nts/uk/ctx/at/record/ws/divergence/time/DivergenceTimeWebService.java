package nts.uk.ctx.at.record.ws.divergence.time;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.DivergenceTimeInputMethodSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.DivergenceTimeInputMethodSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceAttendanceItemFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeInputMethodDto;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeInputMethodFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeSettingFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTypeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTypeFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.OtsukaOptionFinder;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.AttendanceNameDivergenceDto;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.AttendanceTypeDivergenceAdapterDto;

/**
 * The Class DivergenceTimeWebService.
 */
@Path("at/record/divergencetime/setting")
@Produces("application/json")
public class DivergenceTimeWebService extends WebService {

	// @Inject
	// private DivergenceTimeInputMethodFinder divTimeInputFinder;

	/** The div time finder. */
	@Inject
	private DivergenceTimeSettingFinder divTimeFinder;

	/** The div time inputmethod finder. */
	@Inject
	private DivergenceTimeInputMethodFinder divTimeInputmethodFinder;

	/** The div time save command handler. */
	@Inject
	private DivergenceTimeInputMethodSaveCommandHandler divTimeSaveCommandHandler;	

	/** The div time attendance finder. */
	@Inject
	private DivergenceAttendanceItemFinder divTimeAttendanceFinder;

	/** The div type finder. */
	@Inject
	private DivergenceTypeFinder divTypeFinder;
	
	@Inject
	private OtsukaOptionFinder optionFinder;

	/**
	 * get all divergence time.
	 *
	 * @return the all div time
	 */
	@POST
	@Path("getalldivtime")
	public List<DivergenceTimeDto> getAllDivTime() {
		return this.divTimeFinder.getAllDivTime();
	}

	/**
	 * get all divergence time.
	 *
	 * @return the all div time reason input
	 */
	@POST
	@Path("getalldivtimereasoninput")
	public List<DivergenceTimeInputMethodDto> getAllDivTimeReasonInput() {
		return this.divTimeInputmethodFinder.getAllDivTime();
	}

	/**
	 * Gets the div time info.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the div time info
	 */
	@POST
	@Path("getdivtimeinfo/{divTimeId}")
	public DivergenceTimeInputMethodDto getDivTimeInfo(@PathParam("divTimeId") int divTimeNo) {
		DivergenceTimeInputMethodDto result = this.divTimeInputmethodFinder.getDivTimeInputMethodInfo(divTimeNo);
		return result;
	}

	/**
	 * update divergence time.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("updatedivtime")
	public void updateDivTime(DivergenceTimeInputMethodSaveCommand command) {
		this.divTimeSaveCommandHandler.handle(command);
	}

	/**
	 * Gets the div time.
	 *
	 * @return the div time
	 */
	@POST
	@Path("getDivTypeList")
	public List<DivergenceTypeDto> getDivTime() {
		return this.divTypeFinder.getDivergenceTypeList();
	}

	/**
	 * Gets the at type.
	 *
	 * @return the at type
	 */
	@POST
	@Path("getAttendanceDivergenceItem/{divTimeNo}")
	public List<AttendanceTypeDivergenceAdapterDto> getAtType(@PathParam("divTimeNo") int divTimeNo) {

		return this.divTimeAttendanceFinder.getAllAtType(divTimeNo);
	}

	/**
	 * Gets the at name.
	 *
	 * @param dailyAttendanceItemIds
	 *            the daily attendance item ids
	 * @return the at name
	 */
	@POST
	@Path("AttendanceDivergenceName")
	public List<AttendanceNameDivergenceDto> getAtName(List<Integer> dailyAttendanceItemIds) {
		return this.divTimeAttendanceFinder.getAtName(dailyAttendanceItemIds);

	}
	
	@POST
	@Path("getMonthlyAttendanceDivergenceName")
	public List<AttendanceNameDivergenceDto> getMonthlyAttendanceDivergenceName(List<Integer> monthlyAttendanceItemIds) {
		return this.divTimeAttendanceFinder.getMonthlyAtName(monthlyAttendanceItemIds).stream()
				.sorted((o1, o2) -> o1.getAttendanceItemDisplayNumber() - o2.getAttendanceItemDisplayNumber())
				.collect(Collectors.toList());
	}

	@POST
	@Path("getOtsukaOption")
	public boolean getOtsukaOption() {
		return this.optionFinder.getOtsukaOption();
	}
}
