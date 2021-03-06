/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.model.employee;

import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDateTime;

/**
 * The Class RegulationInfoEmployee.
 */
@Getter
@Setter
@Builder
public class RegulationInfoEmployee { // 社員のソート用の規定情報

	/** The employee ID. */
	private String employeeID; // 社員ID

	/** The employee code. */
	private String employeeCode; // 社員コード

	/** The hire date. */
	private Optional<GeneralDateTime> hireDate; // 入社日

	/** The classification code. */
	private Optional<String> classificationCode; // 分類コード

	/** The name. */
	private Optional<String> name; // 氏名

	/** The job title code. */
	private Optional<String> jobTitleCode; // 職位コード

	// private Integer oderOfJobTitle; //職位の序列の並び順

	/** The workplace id. */
	private Optional<String> workplaceId; // 職場ID

	/** The workplace code. */
	private Optional<String> workplaceCode; // 職場コード

	/** The workplace hierarchy code. */
	private Optional<String> workplaceHierarchyCode; // 職場の階層コード

	/** The department hierarchy code. */
	private Optional<String> departmentHierarchyCode; // 部門の階層コード

	/** The department code. */
	private Optional<String> departmentCode; // 部門コード

	/** The department id. */
	private Optional<String> departmentId; // 部門ID

	/** The department name. */
	private Optional<String> departmentName; // 部門氏名

	/** The employment code. */
	private Optional<String> employmentCode; // 雇用コード

	/** The workplace name. */
	private Optional<String> workplaceName; // 職場氏名

	/** The department delete flag. */
	private Optional<Boolean> departmentDeleteFlag; // 部門削除フラグ

	/** The workplace delete flag. */
	private Optional<Boolean> workplaceDeleteFlag; // 職場削除フラグ

}
