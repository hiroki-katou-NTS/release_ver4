package nts.uk.ctx.at.request.app.find.application.applicationlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppListParamFilter {

	private AppListExtractConditionDto condition;
	private boolean spr;
	private int extractCondition;
}
