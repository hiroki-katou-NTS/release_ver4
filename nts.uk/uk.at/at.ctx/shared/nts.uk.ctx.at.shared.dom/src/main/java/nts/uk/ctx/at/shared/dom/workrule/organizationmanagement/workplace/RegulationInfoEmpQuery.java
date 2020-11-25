package nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * 社員検索の規定条件
 * @author HieuLt
 *
 */
@Data
@NoArgsConstructor
public class RegulationInfoEmpQuery {

	/** The base date. */
	private GeneralDate baseDate; // 基準日

	/** The reference range. */
	private Integer referenceRange; // 検索参照範囲

	/** The filter by employment. */
	private Boolean filterByEmployment; // 雇用で絞り込む

	/** The employment codes. */
	private List<String> employmentCodes; // 雇用コード一覧

	/** The filter by department. */
	private Boolean filterByDepartment; // 部門で絞り込む

	/** The department codes. */
	private List<String> departmentCodes; // 部門ID一覧

	/** The filter by workplace. */
	private Boolean filterByWorkplace; // 職場で絞り込む

	/** The workplace codes. */
	private List<String> workplaceIds; // 職場ID一覧

	/** The filter by classification. */
	private Boolean filterByClassification; // 分類で絞り込む

	/** The classification codes. */
	private List<String> classificationCodes; // 分類コード一覧

	/** The filter by job title. */
	private Boolean filterByJobTitle; // 職位で絞り込む

	/** The job title codes. */
	private List<String> jobTitleCodes; // 職位ID一覧

	/** The filter by worktype. */
	private Boolean filterByWorktype;
	
	/** The worktype codes. */
	private List<String> worktypeCodes;

	/** The period start. */
	private GeneralDate periodStart; // 在職・休職・休業のチェック期間

	/** The period end. */
	private GeneralDate periodEnd; // 在職・休職・休業のチェック期間

	/** The include incumbents. */
	private Boolean includeIncumbents; // 在職者を含める

	/** The include workers on leave. */
	private Boolean includeWorkersOnLeave; // 休職者を含める

	/** The include occupancy. */
	private Boolean includeOccupancy; // 休業者を含める

	/** The include retirees. */
	private Boolean includeRetirees; // 退職者を含める

	/** The include are on loan. */
	private Boolean includeAreOnLoan; // 出向に来ている社員を含める

	/** The include going on loan. */
	private Boolean includeGoingOnLoan; // 出向に行っている社員を含める

	/** The retire start. */
	private GeneralDate retireStart; // 退職日のチェック期間

	/** The retire end. */
	private GeneralDate retireEnd; // 退職日のチェック期間

	/** The sort order no. */
	private Integer sortOrderNo; // 並び順NO

	/** The name type. */
	private String nameType; // 氏名の種類

	/** The system type. */
	private Integer systemType;
	
	private String roleId;
	
	/** The filter by closure. */
	private Boolean filterByClosure = false;

	/** The closure ids. */
	private List<Integer> closureIds;

	public RegulationInfoEmpQuery(GeneralDate baseDate, Integer referenceRange, Boolean filterByEmployment,
			List<String> employmentCodes, Boolean filterByDepartment, List<String> departmentCodes,
			Boolean filterByWorkplace, List<String> workplaceIds, Boolean filterByClassification,
			List<String> classificationCodes, Boolean filterByJobTitle, List<String> jobTitleCodes,
			Boolean filterByWorktype, List<String> worktypeCodes, GeneralDate periodStart, GeneralDate periodEnd,
			Boolean includeIncumbents, Boolean includeWorkersOnLeave, Boolean includeOccupancy, Boolean includeRetirees,
			Boolean includeAreOnLoan, Boolean includeGoingOnLoan, GeneralDate retireStart, GeneralDate retireEnd,
			Integer sortOrderNo, String nameType, Integer systemType, String roleId, Boolean filterByClosure,
			List<Integer> closureIds) {
		super();
		this.baseDate = baseDate;
		this.referenceRange = referenceRange;
		this.filterByEmployment = filterByEmployment;
		this.employmentCodes = employmentCodes;
		this.filterByDepartment = filterByDepartment;
		this.departmentCodes = departmentCodes;
		this.filterByWorkplace = filterByWorkplace;
		this.workplaceIds = workplaceIds;
		this.filterByClassification = filterByClassification;
		this.classificationCodes = classificationCodes;
		this.filterByJobTitle = filterByJobTitle;
		this.jobTitleCodes = jobTitleCodes;
		this.filterByWorktype = filterByWorktype;
		this.worktypeCodes = worktypeCodes;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.includeIncumbents = includeIncumbents;
		this.includeWorkersOnLeave = includeWorkersOnLeave;
		this.includeOccupancy = includeOccupancy;
		this.includeRetirees = includeRetirees;
		this.includeAreOnLoan = includeAreOnLoan;
		this.includeGoingOnLoan = includeGoingOnLoan;
		this.retireStart = retireStart;
		this.retireEnd = retireEnd;
		this.sortOrderNo = sortOrderNo;
		this.nameType = nameType;
		this.systemType = systemType;
		this.roleId = roleId;
		this.filterByClosure = filterByClosure;
		this.closureIds = closureIds;
	}
	
}