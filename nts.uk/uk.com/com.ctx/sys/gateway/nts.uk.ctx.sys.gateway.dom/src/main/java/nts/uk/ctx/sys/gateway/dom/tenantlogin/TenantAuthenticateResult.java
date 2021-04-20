package nts.uk.ctx.sys.gateway.dom.tenantlogin;

import java.util.Optional;

import lombok.Value;
import nts.arc.task.tran.AtomTask;

/**
 * テナント認証結果
 * @author hiroki_katou
 *
 */
@Value
public class TenantAuthenticateResult {
	private boolean success;
	
	private Optional<AtomTask> atomTask;
	
	/**
	 * テナント認証に成功
	 * @return
	 */
	public static TenantAuthenticateResult success() {
		return new TenantAuthenticateResult(true, Optional.empty());
	}
	
	/**
	 * テナント認証に失敗
	 * @param failureLog
	 * @return
	 */
	public static TenantAuthenticateResult failed(AtomTask atomTask) {
		return new TenantAuthenticateResult(false, Optional.of(atomTask));
	}
	
	public boolean isSuccess() {
		return this.success;
	}
	
	public boolean isFailure() {
		return !this.success;
	}
}