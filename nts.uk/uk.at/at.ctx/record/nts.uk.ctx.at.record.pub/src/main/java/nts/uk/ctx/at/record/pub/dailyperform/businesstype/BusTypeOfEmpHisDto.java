package nts.uk.ctx.at.record.pub.dailyperform.businesstype;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusTypeOfEmpHisDto {
	private BusinessTypeOfEmployeeHistory buHistory;
	private List<BusinessTypeOfEmployee> lsBusinessTypeOfEmpl;
	
}
