package nts.uk.ctx.at.record.app.find.application.realitystatus;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author dat.lh
 *
 */
@Data
public class RealityStatusActivityParam {
	private GeneralDate startDate;
	private GeneralDate endDate;
	private boolean confirmData;
	private List<String> listWorkplaceId;
	private List<String> listEmpCd;
	private Integer closureID;
}
