package nts.uk.ctx.basic.dom.organization.position;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum EmpScopeAtr {
	/**
	 * EMP_SCOPE_ATR
	 */
		/**0: 全員*/
		AllMember(0),
		/**1: 所属部門配下全て*/
		AllDepartments(1),
		/**1: 所属部門のみ*/
		OnlyDepartmentAffiliation(2),
		/**1: 本人のみ */
		OnlyTheprincipal(3);
		
		public final int value;
}
