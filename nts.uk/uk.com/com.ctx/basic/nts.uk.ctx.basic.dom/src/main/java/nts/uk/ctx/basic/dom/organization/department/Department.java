package nts.uk.ctx.basic.dom.organization.department;

import java.time.LocalDate;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.basic.dom.company.CompanyCode;
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class Department extends AggregateRoot {
	/* 会社コード */
	private String companyCode;
	/* 部門コード */
	private DepartmentCode departmentCode;
	/* 履歴ID */
	private String historyId;
	/* 終了年月日 */
	private GeneralDate endDate;
	/* 外部コード */
	private DepartmentCode externalCode;
	/* 部門表示名 */
	private DepartmentGenericName fullName;
	/* 内部階層CD */
	private HierarchyCode hierarchyCode;
	/* 部門名称 */
	private DepartmentName departmentName;
	/* 開始年月日 */
	private GeneralDate startDate;

	public Department(String companyCode, DepartmentCode departmentCode, String historyId, GeneralDate endDate,
			DepartmentCode externalCode, DepartmentGenericName fullName, HierarchyCode hierarchyCode,
			DepartmentName name, GeneralDate startDate) {
		this.companyCode = companyCode;
		this.departmentCode = departmentCode;
		this.historyId = historyId;
		this.endDate = endDate;
		this.externalCode = externalCode;
		this.fullName = fullName;
		this.hierarchyCode = hierarchyCode;
		this.departmentName = name;
		this.startDate = startDate;
	}

	public Department(String companyCode, DepartmentCode departmentCode, GeneralDate endDate,
			DepartmentCode externalCode, DepartmentGenericName fullName, HierarchyCode hierarchyCode,
			DepartmentName name, GeneralDate startDate) {
		this.companyCode = companyCode;
		this.departmentCode = departmentCode;
		this.historyId = IdentifierUtil.randomUniqueId();
		this.endDate = endDate;
		this.externalCode = externalCode;
		this.fullName = fullName;
		this.hierarchyCode = hierarchyCode;
		this.departmentName = name;
		this.startDate = startDate;
	}

	public Department(String historyId, GeneralDate startDate, GeneralDate endDate) {
		super();
		this.historyId = historyId;
		this.endDate = endDate;
		this.startDate = startDate;
	}

	public static Department createSimpleFromJavaType(String startDate, String endDate, String historyId) {
		return new Department(historyId, GeneralDate.localDate(LocalDate.parse(startDate)),
				GeneralDate.localDate(LocalDate.parse(endDate)));
	}

}
