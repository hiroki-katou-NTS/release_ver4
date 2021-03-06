package nts.uk.ctx.at.function.app.command.attendancerecord.export.setting;

import javax.ejb.Stateless;
import javax.inject.Inject;
//import java.util.stream.Collectors;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.AttendanceRecordExportSetting;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.AttendanceRecordExportSettingRepository;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.ExportSettingCode;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.ExportSettingName;

/**
 * The Class AttendanceRecordExportSettingDeleteCommandHandler.
 */
@Stateless
public class AttendanceRecordExportSettingDeleteCommandHandler
		extends CommandHandler<AttendanceRecordExportSettingDeleteCommand> {

	/** The attendance rec exp set repo. */
	@Inject
	AttendanceRecordExportSettingRepository attendanceRecExpSetRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<AttendanceRecordExportSettingDeleteCommand> context) {

		AttendanceRecordExportSettingDeleteCommand command = context.getCommand();

		// convert to domain
		AttendanceRecordExportSetting domain = new AttendanceRecordExportSetting();
		domain.setLayoutId(command.getLayoutId());
		domain.setCode(new ExportSettingCode(String.valueOf(command.getCode())));
		domain.setName(new ExportSettingName(command.getName()));

		// Delete
		attendanceRecExpSetRepo.deleteAttendanceRecExpSet(domain);
	}

}
