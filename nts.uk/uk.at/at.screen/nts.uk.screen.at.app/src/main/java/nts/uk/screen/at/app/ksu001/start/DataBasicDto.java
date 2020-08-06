package nts.uk.screen.at.app.ksu001.start;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.ksu001.getinfoofInitstartup.DataScreenQueryGetInforDto;

/**
 * @author laitv
 */
@Value
public class DataBasicDto {
	public GeneralDate startDate; // ・期間 A3_1_2
	public GeneralDate endDate;   // ・期間 A3_1_4
	public  int unit; //WORKPLACE(0), //WORKPLACE_GROUP(1);
	public  String workplaceId;
	public  String workplaceGroupId;
	public String designation ; // 
	public String targetOrganizationName ;   //  表示名  Aa1_2_2
	public DataBasicDto(DataScreenQueryGetInforDto resultStep1) {
		this.startDate = resultStep1.startDate;
		this.endDate = resultStep1.endDate;
		this.unit = resultStep1.targetOrgIdenInfor.unit;
		this.workplaceId = resultStep1.targetOrgIdenInfor.workplaceId;
		this.workplaceGroupId = resultStep1.targetOrgIdenInfor.workplaceGroupId;
		this.designation = resultStep1.displayInforOrganization.getDesignation();
		this.targetOrganizationName = resultStep1.displayInforOrganization.getDisplayName();
	}
	
	
}
