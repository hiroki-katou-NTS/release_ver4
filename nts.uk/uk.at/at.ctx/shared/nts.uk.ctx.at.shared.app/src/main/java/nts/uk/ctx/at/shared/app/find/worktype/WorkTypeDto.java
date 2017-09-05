package nts.uk.ctx.at.shared.app.find.worktype;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

@Data
@AllArgsConstructor
public class WorkTypeDto {

	/* 会社ID */
	private String companyId;
	/* 勤務種類コード */
	private String workTypeCode;
	/* 勤務種類名称 */
	private String name;
	/* 勤務種類略名 */
	private String abbreviationName;
	/* 勤務種類記号名 */
	private String symbolicName;
	/* 廃止区分 */
	private int abolishAtr;
	/* 勤務種類備考 */
	private String memo;
	/* 勤務の単位 */
	private int workAtr;
	/* 1日 */
	private int oneDayCls;
	/* 午前 */
	private int morningCls;
	/* 午後 */
	private int afternoonCls;
	/* 出勤率の計算方法 */
	private int calculatorMethod;

	private List<WorkTypeSetDto> workTypeSets;

	public static WorkTypeDto fromDomain(WorkType workType) {
		return new WorkTypeDto(workType.getCompanyId(), workType.getWorkTypeCode().v(), workType.getName().v(),
				workType.getAbbreviationName().v(), workType.getSymbolicName().v(), workType.getDeprecate().value,
				workType.getMemo().v(), workType.getDailyWork().getWorkTypeUnit().value,
				workType.getDailyWork().getOneDay().value, workType.getDailyWork().getMorning().value,
				workType.getDailyWork().getAfternoon().value, workType.getCalculateMethod().value, null);
	}

	public WorkTypeDto(String companyId, String workTypeCode, String name, String abbreviationName) {
		this.companyId = companyId;
		this.workTypeCode = workTypeCode;
		this.name = name;
		this.abbreviationName = abbreviationName;
	}

	public static WorkTypeDto fromDomainWorkTypeLanguage(WorkType workType) {
		return new WorkTypeDto(workType.getCompanyId(), workType.getWorkTypeCode().v(), workType.getName().v(),
				workType.getAbbreviationName().v());
	}
}
