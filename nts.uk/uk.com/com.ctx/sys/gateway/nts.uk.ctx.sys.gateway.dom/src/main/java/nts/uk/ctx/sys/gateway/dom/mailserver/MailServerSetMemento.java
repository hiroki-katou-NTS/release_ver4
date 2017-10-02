package nts.uk.ctx.sys.gateway.dom.mailserver;

import nts.uk.ctx.sys.gateway.dom.common.CompanyId;

public interface MailServerSetMemento {
	/**
	 * Set the company id.
	 *
	 */
	public void setCompanyId(CompanyId companyId);

	/**
	 * Set use authentication
	 * 
	 */
	public void setUseAuthentication(UseAuthentication useAuthentication);

	/**
	 * Set Encryption Method
	 * 
	 */
	public void setEncryptionMethod(EncryptionMethod encryptionMethod);

	/**
	 * Set Authentication Method
	 * 
	 */
	public void setAuthenticationMethod(AuthenticationMethod authenticationMethod);

	/**
	 * Set Email Authentication
	 * 
	 */
	public void setEmailAuthentication(EmailAuthentication emailAuthentication);

	/**
	 * Set password
	 * 
	 */
	public void setPassword(Password password);
	
	/**
	 * Set SMTP Information
	 * 
	 */
	public void setSmtpInfo(SmtpInfo smtpInfo);
	
	/**
	 * Set IMAP Information
	 * 
	 */
	public void setImapInfo(ImapInfo imapInfo);
	
	/**
	 * Set POP Information
	 * 
	 */
	public void setPopInfo(PopInfo popInfo);
}
