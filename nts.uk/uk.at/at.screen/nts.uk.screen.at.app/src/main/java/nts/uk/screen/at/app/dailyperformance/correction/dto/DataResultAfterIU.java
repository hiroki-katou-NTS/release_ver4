package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.FlexShortageRCDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResultAfterIU {
	
	Map<Integer, List<DPItemValue>> errorMap = new HashMap<>();
	
	FlexShortageRCDto flexShortage;
	
	Boolean showErrorDialog;
	
	String messageAlert;
	
	List<Pair<String, GeneralDate>> lstSidDateDomainError = new ArrayList<>();
	
	Set<EmpAndDate> mapIndentityCheck = new HashSet<>(); 
	
	Set<EmpAndDate> mapApprovalCheck = new HashSet<>(); 
	
	boolean onlyLoadCheckBox = false;
		
	boolean errorAllSidDate = false;
	
	boolean canFlex = false;

	public DataResultAfterIU(Map<Integer, List<DPItemValue>> errorMap, FlexShortageRCDto flexShortage,
			Boolean showErrorDialog, String messageAlert) {
		super();
		this.errorMap = errorMap;
		this.flexShortage = flexShortage;
		this.showErrorDialog = showErrorDialog;
		this.messageAlert = messageAlert;
	}
	
	
}
