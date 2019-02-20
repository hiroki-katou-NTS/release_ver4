package nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ResultReturnDCUpdateData;

@AllArgsConstructor
@Data
public class ErrorAfterCalcDaily {
	
	private Boolean hasError;
	
	private Map<Integer, List<DPItemValue>> resultErrorMonth;
	
	private Set<Pair<String, GeneralDate>> lstErrorEmpMonth;
	
	private Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> resultError;
	
	private FlexShortageRCDto flexShortage;

}
