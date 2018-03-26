/**
 * 3:12:24 PM Sep 5, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

/**
 * @author hungnm
 *
 */
@Getter
@Setter
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
    private Set<DPCellDataDto> cellDatas;
    
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
	
}
