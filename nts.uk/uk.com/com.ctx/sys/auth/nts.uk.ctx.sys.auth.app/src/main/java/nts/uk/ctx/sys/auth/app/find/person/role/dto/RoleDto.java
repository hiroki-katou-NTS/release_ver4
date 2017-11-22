package nts.uk.ctx.sys.auth.app.find.person.role.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.sys.auth.dom.role.ContractCode;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleAtr;
import nts.uk.ctx.sys.auth.dom.role.RoleCode;
import nts.uk.ctx.sys.auth.dom.role.RoleName;
import nts.uk.ctx.sys.auth.dom.role.RoleType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
	/** The role id. */
	// Id
	private String roleId;

	/** The role code. */
	// コード
	private String roleCode;

	/** The role type. */
	// ロール種類
	private int roleType;

	/** The employee reference range. */
	// 参照範囲
	private int employeeReferenceRange;

	/** The name. */
	// ロール名称
	private String name;

	/** The contract code. */
	// 契約コード
	private String contractCode;

	/** The assign atr. */
	// 担当区分
	private int assignAtr;

	/** The company id. */
	// 会社ID
	private String companyId;
	
	public static RoleDto fromDomain(Role domain) {
		return new RoleDto(
				domain.getRoleId(),
				domain.getRoleCode().v(),
				domain.getRoleType().value,
				domain.getEmployeeReferenceRange().value,
				domain.getName().v(),
				domain.getContractCode().v(),
				domain.getAssignAtr().value,
				domain.getCompanyId()
				);
		
	}
}
