package nts.uk.ctx.at.shared.dom.workrule.shiftmaster;

import nts.uk.ctx.at.shared.dom.WorkInformation;

import java.util.Optional;

public class ShiftMasterInstanceHelper {
	public static ShiftMaster getShiftMaterEmpty() {
		return new ShiftMaster("companyId", new ShiftMasterCode("smc"), 
				new ShiftMasterDisInfor(new ShiftMasterName("name"),new ColorCodeChar6("color"),new ColorCodeChar6("color"), null), 
				Optional.of(new ShiftMasterImportCode("code")),"workTypeCode", "workTimeCode");
	}
	
	public static ShiftMaster getShiftMater(String companyId, String shiftMaterCode, ShiftMasterDisInfor displayInfor,
			String importCode, String workTypeCode, String workTimeCode) {
		return new ShiftMaster(companyId, new ShiftMasterCode("smc"), displayInfor, Optional.of(new ShiftMasterImportCode(importCode)), workTypeCode, workTimeCode);
	}
	
	public static ShiftMasterDisInfor getShiftMasterDisInforEmpty() {
		return new ShiftMasterDisInfor(new ShiftMasterName("name"),new ColorCodeChar6("color"),new ColorCodeChar6("color"), null);
	}
	
	public static boolean checkExist(boolean param){
		return param;
	}
	
	public static WorkInformation getWorkInformationWorkTimeIsNull() {
		return new WorkInformation( "workTypeCode", null);
	}


}
