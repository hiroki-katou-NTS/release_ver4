package nts.uk.ctx.at.record.app.command.remainingnumber.subhdmana;

import java.util.List;

import lombok.Getter;


@Getter
public class DayOffManaCommand {
	private String employeeId;
	private String leaveId;
	private List<ComDayOffManaComand> comDayOffManaDtos;
}
