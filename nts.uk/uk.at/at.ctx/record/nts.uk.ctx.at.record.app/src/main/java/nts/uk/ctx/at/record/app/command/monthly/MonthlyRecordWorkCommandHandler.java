package nts.uk.ctx.at.record.app.command.monthly;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.command.monthly.affliation.AffiliationInfoOfMonthlyCommandHandler;
import nts.uk.ctx.at.record.app.command.monthly.attendancetime.AttendanceTimeOfMonthlyCommandHandler;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

@Stateless
public class MonthlyRecordWorkCommandHandler {

	/** 月別実績の所属情報： 月別実績の所属情報 */
	@Inject
	@AttendanceItemLayout(layout = "A", jpPropertyName = "", index = 1)
	private AffiliationInfoOfMonthlyCommandHandler affiliationHandler;

	/** 月別実績の勤怠時間： 月別実績の勤怠時間 */
	@Inject
	@AttendanceItemLayout(layout = "B", jpPropertyName = "", index = 2)
	private AttendanceTimeOfMonthlyCommandHandler attendanceTimeHandler;

	public void handleAdd(MonthlyRecordWorkCommand command) {
		handler(command, false);
	}
	
	public void handleUpdate(MonthlyRecordWorkCommand command) {
		handler(command, true);
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends MonthlyWorkCommonCommand> void handler(MonthlyRecordWorkCommand command, boolean isUpdate) {
		Set<String> mapped = command.itemValues().stream().map(c -> getGroup(c))
				.distinct().collect(Collectors.toSet());
		mapped.stream().forEach(c -> {
			CommandFacade<T> handler = (CommandFacade<T>) getHandler(c, isUpdate);
			if(handler != null){
				handler.handle((T) command.getCommand(c));
			}
		});
	}
	
	private CommandFacade<?> getHandler(String group, boolean isUpdate) {
		CommandFacade<?> handler = null;
		switch (group) {
		case "A":
			handler = this.affiliationHandler;
			break;
		case "B":
			handler = this.attendanceTimeHandler;
			break;
		default:
			break;
		}
		return handler;
	}

	private String getGroup(ItemValue c) {
		return String.valueOf(c.layoutCode().charAt(0));
	}

}
