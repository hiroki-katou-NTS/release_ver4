package nts.uk.screen.at.app.ktgwidget.find.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.OptionalWidgetImport;

@Value
@AllArgsConstructor
public class OptionalWidgetDisplay {

	private DatePeriodDto datePeriodDto;
	
	private OptionalWidgetImport optionalWidgetImport;
	// 0: current month; 1: next month
	private int currentorNext;
}
