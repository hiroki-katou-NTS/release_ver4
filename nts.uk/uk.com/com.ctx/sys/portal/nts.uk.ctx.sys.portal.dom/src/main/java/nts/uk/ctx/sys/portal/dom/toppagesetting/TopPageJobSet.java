package nts.uk.ctx.sys.portal.dom.toppagesetting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.sys.portal.dom.enums.System;

/**
 * 
 * @author sonnh1
 *
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class TopPageJobSet extends AggregateRoot {

	/** The company id. */
	private String companyId;

	/** The top menu code. */
	private TopMenuCode topMenuCode;

	/** The login menu code. */
	private TopMenuCode loginMenuCode;

	/** The job id. */
	private String jobId;

	/** The person permission set. */
	private PersonPermissionSetting personPermissionSet;

	/** System */
	private System system;

	public TopPageJobSet(String companyId, TopMenuCode topMenuCode, TopMenuCode loginMenuCode, String jobId,
			PersonPermissionSetting personPermissionSet, System system) {
		super();
		this.companyId = companyId;
		this.topMenuCode = topMenuCode;
		this.loginMenuCode = loginMenuCode;
		this.jobId = jobId;
		this.personPermissionSet = personPermissionSet;
		this.system = system;
	}

	/**
	 * Convert from java type to TopPageJobSetting
	 *
	 */
	public static TopPageJobSet createFromJavaType(String companyId, String topMenuCode, String loginMenuCode,
			String jobId, int personPermissionSet, int system) {
		return new TopPageJobSet(companyId, new TopMenuCode(topMenuCode), new TopMenuCode(loginMenuCode), jobId,
				EnumAdaptor.valueOf(personPermissionSet, PersonPermissionSetting.class),
				EnumAdaptor.valueOf(system, System.class));
	}

}
