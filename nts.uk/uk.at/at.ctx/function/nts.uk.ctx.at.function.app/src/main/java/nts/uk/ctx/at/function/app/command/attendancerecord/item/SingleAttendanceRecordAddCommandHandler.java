package nts.uk.ctx.at.function.app.command.attendancerecord.item;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.attendancerecord.item.ItemName;
import nts.uk.ctx.at.function.dom.attendancerecord.item.SingleAttendanceRecord;
import nts.uk.ctx.at.function.dom.attendancerecord.item.SingleAttendanceRecordRepository;
import nts.uk.ctx.at.function.dom.attendancerecord.item.SingleItemAttributes;


/**
 * The type SingleAttendanceRecordAddCommandHandler.
 *
 * @author locph
 *
 */
@Stateless
public class SingleAttendanceRecordAddCommandHandler extends CommandHandler<SingleAttendanceRecordAddCommand>{

	/**
	 * The SingleAttendanceRecordRepository.
	 */
	@Inject
	SingleAttendanceRecordRepository singleAttendanceRecordRepository;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 *
	 * Handle add SingleAttendanceRecord Item
	 *
	 * @param context
	 */
	@Override
	protected void handle(CommandHandlerContext<SingleAttendanceRecordAddCommand> context) {
		SingleAttendanceRecordAddCommand command = context.getCommand();
		//convert to domain
		SingleAttendanceRecord singleAttendanceRecord = new SingleAttendanceRecord(
																		SingleItemAttributes.valueOf(command.getAttribute()),
																		new ItemName(command.getName()),
																		command.getTimeItemId());
		//update
		this.singleAttendanceRecordRepository.updateSingleAttendanceRecord(
																		command.getLayoutId(),
																		command.getColumnIndex(),
																		command.getPosition(),
																		command.getExportAtr(),
																		command.isUseAtr(),
																		singleAttendanceRecord);
		
	}

}
