/**
 * 3:12:24 PM Sep 5, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DPDataDto {
    private String id;
	private String state;
    private String error;
    private GeneralDate date;
    private boolean sign;
    private boolean approval;
    private String employeeId;
    private String employeeCode;
    private String employeeName;
    private String workplaceId;
    private String employmentCode;
    private String typeGroup;
    private DatePeriod datePriod;
    private Set<DPCellDataDto> cellDatas;
    private boolean errorOther;
    
	public DPDataDto(String id, String state, String error, GeneralDate date, boolean sign, boolean approval, String employeeId,
			String employeeCode, String employeeName, String workplaceId, String employmentCode, String typeGroup) {
		this.id = id;
		this.state = state;
		this.error = error;
		this.date = date;
		this.sign = sign;
		this.employeeId = employeeId;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.workplaceId = workplaceId;
		this.employmentCode = employmentCode;
		this.typeGroup = typeGroup;
		this.cellDatas = new HashSet<DPCellDataDto>();
	}
	
	public void setCellDatas(Set<DPCellDataDto> lstCellData) {
		this.cellDatas = lstCellData;
	}
	
	public void addCellData(DPCellDataDto cellData) {
		this.cellDatas.add(cellData);
	}
	
	public void resetData(){
//		this.state = "";
//		this.error = "";
//		this.sign = false;
//		this.approval = false;
//		this.typeGroup = "";
		this.cellDatas = new HashSet<>();
	}
	
}
