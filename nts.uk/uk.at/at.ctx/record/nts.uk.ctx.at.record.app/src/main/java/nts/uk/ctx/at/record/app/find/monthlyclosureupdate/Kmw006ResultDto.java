package nts.uk.ctx.at.record.app.find.monthlyclosureupdate;

import java.util.List;

import lombok.Value;

/**
 * 
 * @author HungTT
 *
 */

@Value
public class Kmw006ResultDto {

	private List<MonthlyClosureErrorInforDto> listErrorInfor;

	private MonthlyClosureUpdateLogDto updateLog;

}
