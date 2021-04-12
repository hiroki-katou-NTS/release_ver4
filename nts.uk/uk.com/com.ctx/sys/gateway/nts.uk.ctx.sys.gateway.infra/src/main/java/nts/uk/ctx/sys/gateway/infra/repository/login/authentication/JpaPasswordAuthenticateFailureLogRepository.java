package nts.uk.ctx.sys.gateway.infra.repository.login.authentication;

import java.util.List;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.gateway.dom.login.password.authenticate.PasswordAuthenticateFailureLog;
import nts.uk.ctx.sys.gateway.dom.login.password.authenticate.PasswordAuthenticateFailureLogRepository;
import nts.uk.ctx.sys.gateway.infra.entity.login.authnticate.SgwdtFailLogPasswordAuth;
import nts.uk.ctx.sys.gateway.infra.entity.login.authnticate.SgwdtFailLogPasswordAuthPK;

public class JpaPasswordAuthenticateFailureLogRepository extends JpaRepository implements PasswordAuthenticateFailureLogRepository{

	private final String BASIC_SELECT = "select * from SGWDT_FAIL_LOG_PASSWORD_AUTH ";
	
	private SgwdtFailLogPasswordAuth toEntity(PasswordAuthenticateFailureLog domain) {
		return new SgwdtFailLogPasswordAuth(
				new SgwdtFailLogPasswordAuthPK(
					domain.getFailureTimestamps(),
					domain.getTriedUserId(), 
					domain.getTriedPassword()));
	}
	
	public void insert(PasswordAuthenticateFailureLog log) {
		this.commandProxy().insert(toEntity(log));
	}

	@Override
	public List<PasswordAuthenticateFailureLog> find(String userId) {
		String query = BASIC_SELECT 
				+ " where TRIED_USER_ID = @userId ";
		
		return this.jdbcProxy().query(query)
				.paramString("userId", userId)
				.getList(rec -> SgwdtFailLogPasswordAuth.MAPPER.toEntity(rec).toDomain());
	}

	@Override
	public List<PasswordAuthenticateFailureLog> find(String userId, GeneralDateTime startDateTime, GeneralDateTime endDateTime) {
		String query = BASIC_SELECT 
				+ " where TRIED_USER_ID = @userId "
				+ " and FAILURE_DATE_TIME >= @startDateTime"
				+ " and FAILURE_DATE_TIME <= @endDateTime";
		
		return this.jdbcProxy().query(query)
				.paramString("userId", userId)
				.paramDateTime("startDateTime", startDateTime)
				.paramDateTime("endDateTime", endDateTime)
				.getList(rec -> SgwdtFailLogPasswordAuth.MAPPER.toEntity(rec).toDomain());
	}
}
