package nts.uk.ctx.basic.dom.organization.workplace;


import java.time.LocalDate;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;

@Getter
public class WorkPlace extends AggregateRoot{
	/* 会社コード */
	private String companyCode;
	/* 職場コード      */
	private WorkPlaceCode workPlaceCode;
	/* 履歴ID      */
	private String historyId;
	/*  終了年月日     */
	private GeneralDate endDate;
	/* 外部コード      */
	private WorkPlaceCode externalCode;
	/* 職場表示名      */	
	private WorkPlaceGenericName genericName;
	/* 内部階層CD      */
	private HierarchyCode hierarchyCode;
	/* 職場名称      */
	private WorkPlaceName name;
	/* 親子属性1       */
	private ParentChildAttribute parentChildAttribute1;
	/*  親子属性2     */
	private ParentChildAttribute parentChildAttribute2;
	/* 親職場コード1      */
	private WorkPlaceCode parentWorkCode1;
	/* 親職場コード2     */
	private WorkPlaceCode parentWorkCode2;

	private GeneralDate startDate;

	public WorkPlace(String companyCode, WorkPlaceCode workPlaceCode, String historyId, GeneralDate endDate,
			WorkPlaceCode externalCode, WorkPlaceGenericName genericName, HierarchyCode hierarchyCode, WorkPlaceName name,
			ParentChildAttribute parentChildAttribute1, ParentChildAttribute parentChildAttribute2,
			WorkPlaceCode parentWorkCode1, WorkPlaceCode parentWorkCode2,
			GeneralDate startDate) {
		this.companyCode = companyCode;
		this.workPlaceCode = workPlaceCode;
		this.historyId = historyId;
		this.endDate = endDate;
		this.externalCode = externalCode;
		this.genericName = genericName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.parentChildAttribute1 = parentChildAttribute1;
		this.parentChildAttribute2 = parentChildAttribute2;
		this.parentWorkCode1 = parentWorkCode1;
		this.parentWorkCode2 = parentWorkCode2;
		this.startDate = startDate;
	}
	
	public WorkPlace(String companyCode, WorkPlaceCode workPlaceCode, GeneralDate endDate,
			WorkPlaceCode externalCode, WorkPlaceGenericName genericName, HierarchyCode hierarchyCode, WorkPlaceName name,
			ParentChildAttribute parentChildAttribute1, ParentChildAttribute parentChildAttribute2,
			WorkPlaceCode parentWorkCode1, WorkPlaceCode parentWorkCode2,
			GeneralDate startDate) {
		this.companyCode = companyCode;
		this.workPlaceCode = workPlaceCode;
		this.historyId = IdentifierUtil.randomUniqueId();
		this.endDate = endDate;
		this.externalCode = externalCode;
		this.genericName = genericName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.parentChildAttribute1 = parentChildAttribute1;
		this.parentChildAttribute2 = parentChildAttribute2;
		this.parentWorkCode1 = parentWorkCode1;
		this.parentWorkCode2 = parentWorkCode2;
		this.startDate = startDate;
	}

	public WorkPlace(String companyCode, WorkPlaceCode workPlaceCode, GeneralDate endDate, WorkPlaceCode externalCode,
			WorkPlaceGenericName genericName, HierarchyCode hierarchyCode, WorkPlaceName name,
			GeneralDate startDate) {
		this.companyCode = companyCode;
		this.workPlaceCode = workPlaceCode;
		this.historyId = IdentifierUtil.randomUniqueId();
		this.endDate = endDate;
		this.externalCode = externalCode;
		this.genericName = genericName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.startDate = startDate;
	}

	public WorkPlace(String historyId, GeneralDate startDate, GeneralDate endDate) {
		this.historyId = historyId;
		this.endDate = endDate;
		this.startDate = startDate;
	}
	
	public static WorkPlace createSimpleFromJavaType(String startDate, String endDate, String historyId) {
		return new WorkPlace(historyId, GeneralDate.localDate(LocalDate.parse(startDate)),
				GeneralDate.localDate(LocalDate.parse(endDate)));
	}

}
