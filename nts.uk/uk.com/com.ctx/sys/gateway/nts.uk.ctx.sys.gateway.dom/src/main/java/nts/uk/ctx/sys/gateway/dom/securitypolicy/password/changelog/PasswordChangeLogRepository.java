package nts.uk.ctx.sys.gateway.dom.securitypolicy.password.changelog;

public interface PasswordChangeLogRepository {
	PasswordChangeLog find(String userId);
}
