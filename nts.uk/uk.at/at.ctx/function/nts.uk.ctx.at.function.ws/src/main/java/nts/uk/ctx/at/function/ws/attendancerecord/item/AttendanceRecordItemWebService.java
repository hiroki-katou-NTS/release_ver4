package nts.uk.ctx.at.function.ws.attendancerecord.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.function.app.command.attendancerecord.item.CalculateAttendanceRecordDeleteCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.CalculateAttendanceRecordDeleteCommandHandler;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.CalculateAttendanceRecordSaveCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.CalculateAttendanceRecordSaveCommandHandler;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.SingleAttendanceRecordDeleteCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.SingleAttendanceRecordDeleteCommandHandler;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.SingleAttendanceRecordSaveCommand;
import nts.uk.ctx.at.function.app.command.attendancerecord.item.SingleAttendanceRecordSaveCommandHandler;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.AttendanceRecordItemDto;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.AttendanceRecordItemFinder;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.AttendanceRecordKeyDto;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.CalculateAttendanceRecordDto;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.CalculateAttendanceRecordFinder;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.SingleAttendanceRecordDto;
import nts.uk.ctx.at.function.app.find.attendancerecord.item.SingleAttendanceRecordFinder;

// TODO: Auto-generated Javadoc
/**
 * The Class AttendanceRecordItemWebService.
 */
@Path("at/function/attendancerecord/item")
@Produces("application/json")
public class AttendanceRecordItemWebService {

	/** The single attendance record finder. */
	@Inject
	SingleAttendanceRecordFinder singleAttendanceRecordFinder;

	/** The single attendance record command. */
	@Inject
	SingleAttendanceRecordDeleteCommandHandler singleAttendanceRecordDeleteCommand;

	/** The single attendance record save command. */
	@Inject
	SingleAttendanceRecordSaveCommandHandler singleAttendanceRecordSaveCommand;

	/** The calculate attendance record finder. */
	@Inject
	CalculateAttendanceRecordFinder calculateAttendanceRecordFinder;

	/** The calculate attendance record save command. */
	@Inject
	CalculateAttendanceRecordSaveCommandHandler calculateAttendanceRecordSaveCommand;

	/** The calculate attendance record delete command. */
	@Inject
	CalculateAttendanceRecordDeleteCommandHandler calculateAttendanceRecordDeleteCommand;
	
	/** The attendance item finder. */
	@Inject
	AttendanceRecordItemFinder attendanceItemFinder;

	/**
	 * Gets the single attendance record info.
	 *
	 * @param attendanceRecordKey
	 *            the attendance record key
	 * @return the single attendance record info
	 */
	@POST
	@Path("getSingleAttndRecInfo")
	public SingleAttendanceRecordDto getSingleAttendanceRecordInfo(AttendanceRecordKeyDto attendanceRecordKey) {
		SingleAttendanceRecordDto result = this.singleAttendanceRecordFinder.getSingleAttendanceRecord(attendanceRecordKey);
		return result;
	}

	/**
	 * Update single attendance record.
	 *
	 * @param singleAttendanceRecordSaveCommand
	 *            the single attendance record save command
	 */
	@POST
	@Path("updateSingleAttndRec")
	public void updateSingleAttendanceRecord(SingleAttendanceRecordSaveCommand singleAttendanceRecordSaveCommand) {
		this.singleAttendanceRecordSaveCommand.handle(singleAttendanceRecordSaveCommand);
	}

	/**
	 * Delete single attendance record.
	 *
	 * @param singleAttendanceRecordDeleteCommand
	 *            the single attendance record command
	 */
	@POST
	@Path("deleteSingleAttndRec")
	public void deleteSingleAttendanceRecord(SingleAttendanceRecordDeleteCommand singleAttendanceRecordDeleteCommand) {
		this.singleAttendanceRecordDeleteCommand.handle(singleAttendanceRecordDeleteCommand);
	}

	/**
	 * Gets the calculate attendance record info.
	 *
	 * @param attendanceRecordKey
	 *            the attendance record key
	 * @return the calculate attendance record info
	 */
	@POST
	@Path("getCalculateAttndRecInfo")
	public CalculateAttendanceRecordDto getCalculateAttendanceRecordInfo(AttendanceRecordKeyDto attendanceRecordKey) {
		return this.calculateAttendanceRecordFinder.getCalculateAttendanceRecordDto(attendanceRecordKey);
	}

	/**
	 * Update calculate attendance record.
	 *
	 * @param calculateAttendanceRecordSaveCommand
	 *            the calculate attendance record save command
	 */
	@POST
	@Path("updateCalculateAttndRec")
	public void updateCalculateAttendanceRecord(
			CalculateAttendanceRecordSaveCommand calculateAttendanceRecordSaveCommand) {
		this.calculateAttendanceRecordSaveCommand.handle(calculateAttendanceRecordSaveCommand);
	}

	/**
	 * Delete calculate attendance record.
	 *
	 * @param calculateAttendanceRecordDeleteCommand
	 *            the calculate attendance record delete command
	 */
	@POST
	@Path("deleteCalculateAttndRec")
	public void deleteCalculateAttendanceRecord(
			CalculateAttendanceRecordDeleteCommand calculateAttendanceRecordDeleteCommand) {
		this.calculateAttendanceRecordDeleteCommand.handle(calculateAttendanceRecordDeleteCommand);
	}
	
	/**
	 * Gets the attendance record items by screen use atr.
	 *
	 * @param screenUseAtr the screen use atr
	 * @return the attendance record items by screen use atr
	 */
	@POST
	@Path("getAttndRecItem/{screenUseAtr}")
	public List<AttendanceRecordItemDto> getAttendanceRecordItemsByScreenUseAtr(@PathParam("screenUseAtr") int screenUseAtr){
		return this.attendanceItemFinder.getAttendanceItemsByScreenUseAtr(screenUseAtr);
	}


}
