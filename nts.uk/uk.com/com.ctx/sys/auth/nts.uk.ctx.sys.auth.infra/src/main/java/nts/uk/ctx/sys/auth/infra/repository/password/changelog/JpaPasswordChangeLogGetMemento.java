/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.infra.repository.password.changelog;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.auth.dom.password.changelog.LoginId;
import nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogGetMemento;
import nts.uk.ctx.sys.auth.dom.user.HashPassword;

/**
 * The Class JpaPasswordChangeLogGetMemento.
 */
public class JpaPasswordChangeLogGetMemento implements PasswordChangeLogGetMemento {

	/** The entity. */
	private SacdtPasswordChangeLog entity;

	/**
	 * Instantiates a new jpa password change log get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaPasswordChangeLogGetMemento(SacdtPasswordChangeLog entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogGetMemento#
	 * getLoginId()
	 */
	@Override
	public LoginId getLoginId() {
		return new LoginId(this.entity.getSacdtPasswordChangeLogPK().getLoginId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogGetMemento#
	 * getUserId()
	 */
	@Override
	public String getUserId() {
		return this.entity.getSacdtPasswordChangeLogPK().getUserId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogGetMemento#
	 * getModifiedDateTime()
	 */
	@Override
	public GeneralDateTime getModifiedDateTime() {
		return this.entity.getSacdtPasswordChangeLogPK().getModifiedDatetime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogGetMemento#
	 * getPassword()
	 */
	@Override
	public HashPassword getPassword() {
		return new HashPassword(this.entity.getPassword());
	}

}
