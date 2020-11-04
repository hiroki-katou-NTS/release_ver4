package nts.uk.ctx.at.schedule.app.command.schedule.employeeinfo.sortsetting;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.app.find.schedule.employeeinfo.sortsetting.OrderListDto;

@Setter
@Getter
@AllArgsConstructor
public class RegisterSortSettingCommand {

	private List<OrderListDto> lstOrderListDto;

}

