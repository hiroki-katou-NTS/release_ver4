package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
@Getter
@Setter
@AllArgsConstructor
public class CommonCalculateOfAppReflectParam {
	private IntegrationOfDaily integrationOfDaily;
	private String sid;
	private GeneralDate ymd;
	private ApplicationType appType;
	private String workTypeCode;
	private Optional<String> workTimeCode;
	private Optional<Integer> startTime;
	private Optional<Integer> endTime;
	/**事前申請：　True、　事後申請：　False	 */
	private boolean preRequest;

}
