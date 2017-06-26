package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import java.util.List;

import lombok.Getter;

@Getter
public class Qmm005bCommand {
	private int processingNo;

	private List<PayDayUpdateCommand> payDays;
}
