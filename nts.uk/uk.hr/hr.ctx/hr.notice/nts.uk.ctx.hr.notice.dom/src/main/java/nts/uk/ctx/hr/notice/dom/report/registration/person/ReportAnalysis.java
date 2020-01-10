/**
 * 
 */
package nts.uk.ctx.hr.notice.dom.report.registration.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.hr.notice.dom.report.registration.person.enu.CountClass;

/**
 * @author laitv
 * Domain 届出分析
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAnalysis {
	private String cid; //会社ID
	private int reportYearMonth; //届出年月
	private int  reportLayoutID; // 個別届出種類ID 
	private int CountClass; //カウント大区分
	private int countClassSmall; //カウント小区分
	private int reportCount;//カウント数量

}
