package nts.uk.ctx.sys.auth.app.command.roleset;

import java.util.List;

import lombok.Value;

@Value
public class RoleSetCommand {
	/** ロールセットコード. */
	private String roleSetCd;

	/** 会社ID */
	private String companyId;

	/** ロールセット名称*/
	private String roleSetName;

	/** 承認権限*/
	private boolean approvalAuthority;

	/** ロールID: オフィスヘルパーロール */
	private String officeHelperRoleId;

	/** ロールID: マイナンバーロール */
	private String myNumberRoleId;

	/** ロールID: 人事ロール */
	private String hRRoleId;

	/** ロールID: 個人情報ロール */
	private String personInfRoleId;

	/** ロールID: 就業ロール */
	private String employmentRoleId;

	/** ロールID: 給与ロール */
	private String salaryRoleId;
	
	/** List of web menu code **/
	private List<WebMenuCommand> webMenus;
}
