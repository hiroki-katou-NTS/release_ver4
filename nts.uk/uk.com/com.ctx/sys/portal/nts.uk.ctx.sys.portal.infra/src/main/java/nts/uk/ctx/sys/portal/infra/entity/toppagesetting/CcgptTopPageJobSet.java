package nts.uk.ctx.sys.portal.infra.entity.toppagesetting;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author sonnh1
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CCGPT_TOPPAGE_JOB_SET")
public class CcgptTopPageJobSet extends UkJpaEntity {

	@EmbeddedId
	public CcgptTopPageJobSetPK ccgptTopPageJobSetPK;

	/** The top menu code. */
	@Column(name = "TOP_MENU_CD")
	public String topMenuCd;

	/** The login menu code. */
	@Column(name = "LOGIN_MENU_CD")
	public String loginMenuCd;

	/** The person mission set. */
	@Column(name = "PERSON_PERMISSION_SET")
	public int personPermissionSet;

	@Column(name = "LOGIN_SYSTEM")
	public int system;
	
	@Column(name = "LOGIN_MENU_CLS")
	public int loginMenuCls;

	@Override
	protected CcgptTopPageJobSetPK getKey() {
		return this.ccgptTopPageJobSetPK;
	}
}
