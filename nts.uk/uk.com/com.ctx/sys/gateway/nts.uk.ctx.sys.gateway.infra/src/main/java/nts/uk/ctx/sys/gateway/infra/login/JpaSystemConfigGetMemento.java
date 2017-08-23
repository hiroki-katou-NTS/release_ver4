/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.login;

import nts.uk.ctx.sys.gateway.dom.login.InstallForm;
import nts.uk.ctx.sys.gateway.dom.login.SystemConfigGetMemento;
import nts.uk.ctx.sys.gateway.entity.login.SgwstSystemConfig;

/**
 * The Class JpaSystemConfigGetMemento.
 */
public class JpaSystemConfigGetMemento implements SystemConfigGetMemento {

	/** The entity. */
	private SgwstSystemConfig entity;

	/**
	 * Instantiates a new jpa system config get memento.
	 *
	 * @param entity the entity
	 */
	public JpaSystemConfigGetMemento(SgwstSystemConfig entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.login.SystemConfigGetMemento#getInstallForm()
	 */
	@Override
	public InstallForm getInstallForm() {
		return InstallForm.valueOf(new Integer(this.entity.getInstallForm()));
	}

}
