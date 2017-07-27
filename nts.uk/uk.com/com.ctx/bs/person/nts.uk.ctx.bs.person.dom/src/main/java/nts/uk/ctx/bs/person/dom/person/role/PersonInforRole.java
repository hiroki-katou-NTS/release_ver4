package nts.uk.ctx.bs.person.dom.person.role;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
/**
 * The Class PersonInfoRole
 * @author lanlt
 *
 */
public class PersonInforRole extends AggregateRoot {
	@Getter
	private String roleId;
	@Getter
	private String companyId;
	@Getter
	private RoleCode roleCode;
	@Getter
	private RoleName roleName;

	/**
	 * contructor PersonInforRole
	 * 
	 * @param roleId
	 * @param companyId
	 * @param roleCode
	 * @param roleName
	 */
	public PersonInforRole(String roleId, String companyId, RoleCode roleCode, RoleName roleName) {
		super();
		this.roleId = roleId;
		this.companyId = companyId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		
	}

	public static PersonInforRole createFromJavaType(String companyId, String roleId, String roleCode,
			String roleName) {
		if (roleId.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		return new PersonInforRole(roleId, companyId, new RoleCode(roleCode),
				new RoleName(roleName));
	}
}
