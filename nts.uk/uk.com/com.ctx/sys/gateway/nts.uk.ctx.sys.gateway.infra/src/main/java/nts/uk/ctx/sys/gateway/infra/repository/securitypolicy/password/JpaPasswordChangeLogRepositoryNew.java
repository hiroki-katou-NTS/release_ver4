package nts.uk.ctx.sys.gateway.infra.repository.securitypolicy.password;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.password.changelog.PasswordChangeLog;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.password.changelog.PasswordChangeLogRepository;
import nts.uk.ctx.sys.gateway.infra.entity.securitypolicy.password.SgwdtPasswordChangeLog;

@Stateless
public class JpaPasswordChangeLogRepositoryNew extends JpaRepository implements PasswordChangeLogRepository{
	
	private final String BASIC_SELECT = "select * from SGWDT_PASSWORD_CHANGE_LOG ";

	@Override
	public PasswordChangeLog find(String userId) {
		String query = BASIC_SELECT 
				+ "where USER_ID = @userId ";
		
		val entities =  new NtsStatement(query, this.jdbcProxy())
				.paramString("userId", userId)
				.getList(rec -> SgwdtPasswordChangeLog.MAPPER.toEntity(rec));
		return SgwdtPasswordChangeLog.toDomain(userId, entities);
	}
}
