package nts.uk.ctx.at.record.app.find.realitystatus;

import java.util.List;

import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * @author dat.lh
 *
 */
@Value
public class EmpPerformanceParam {
	private String wkpId;
	private GeneralDate startDate;
	private GeneralDate endDate;
	private List<String> listEmpCd;
}
