package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum ReferenceMasterNo {
//	0:条件分岐を利用しない場合にセットする
	CONDITIONAL_BRANCHING_IS_NOT_USED(0),
//	1:雇用マスタ
	EMPLOYMENT_MASTER(1),
//	2:部門マスタ
	DEPARTMENT_MASTER(2),
//	3:分類マスタ
	CLASSIFICATION_MASTER(3),
//	4:給与分類マスタ
	PAYROLL_CLASSIFICATION_MASTER(4),
//	5:職位マスタ
	JOB_TITLE_MASTER(5),
//	6:給与区分
	SALARY_CLASSIFICATION(6);
	
	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "条件分岐を利用しない場合にセットする";
			break;
		case 1:
			name = "雇用マスタ";
			break;
		case 2:
			name = "部門マスタ";
			break;
		case 3:
			name = "分類マスタ";
			break;
		case 4:
			name = "給与分類マスタ";
			break;
		case 5:
			name = "職位マスタ";
			break;
		case 6:
			name = "給与区分";
			break;
		default:
			name = "条件分岐を利用しない場合にセットする";
			break;
		}

		return name;
	}
}
