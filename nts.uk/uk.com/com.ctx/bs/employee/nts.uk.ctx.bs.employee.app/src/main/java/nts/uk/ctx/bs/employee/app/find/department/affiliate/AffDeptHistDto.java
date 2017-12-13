package nts.uk.ctx.bs.employee.app.find.department.affiliate;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistory;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryItem;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

/**
 * AffDeptHistDto
 * 所属部門履歴 and 所属部門履歴項目
 * CS00015
 * @author xuan vinh
 *
 */


@Getter
@Setter
public class AffDeptHistDto extends PeregDomainDto{
	
	//期間
	@PeregItem("IS00070")
	private String period;
	
	//発令日
	@PeregItem("IS00071")
	private GeneralDate startDate;
	
	//終了日
	@PeregItem("IS00072")
	private GeneralDate endDate;
	
	/** The department code. */
	/* 部門コード */
	@PeregItem("IS00073")
	private String departmentCode;

	/** The Affiliation History Transfer type. */
	// 所属履歴異動種類
	@PeregItem("IS00074")
	private String affHistoryTranfsType;

	// 分配率
	@PeregItem("IS00075")
	private String distributionRatio;
		
	private AffDeptHistDto(String recordId, String employeeId) {
		super(recordId, employeeId, null);
	}
	
	public static AffDeptHistDto getFirstFromDomain(AffDepartmentHistory affDeptHist, AffDepartmentHistoryItem affDeptHistItem){
		return getBaseOnDateHist(affDeptHistItem, affDeptHist.getHistoryItems().get(0).start(), affDeptHist.getHistoryItems().get(0).end());
	}
	
	public static List<AffDeptHistDto> getListFromDomain(AffDepartmentHistory affDeptHist, AffDepartmentHistoryItem affDeptHistItem){
		return affDeptHist.getHistoryItems().stream().map(item -> getBaseOnDateHist(affDeptHistItem, item.start(), item.end())).collect(Collectors.toList());
	}
	
	private static AffDeptHistDto getBaseOnDateHist( AffDepartmentHistoryItem affDeptHistItem, GeneralDate startDate, GeneralDate endDate){
		AffDeptHistDto dto = new AffDeptHistDto(affDeptHistItem.getHistoryId(), affDeptHistItem.getEmployeeId());
		dto.setRecordId(affDeptHistItem.getHistoryId());
		dto.setDepartmentCode(affDeptHistItem.getDepartmentId());
		dto.setAffHistoryTranfsType(affDeptHistItem.getAffHistoryTranfsType());
		dto.setDistributionRatio(affDeptHistItem.getDistributionRatio().v().trim());
		dto.setStartDate(startDate);
		return dto;
	}
}
