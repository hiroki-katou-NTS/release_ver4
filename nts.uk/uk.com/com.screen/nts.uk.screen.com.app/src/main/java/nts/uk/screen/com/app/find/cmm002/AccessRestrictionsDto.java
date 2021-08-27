package nts.uk.screen.com.app.find.cmm002;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.sys.gateway.dom.accessrestrictions.AccessRestrictions;

/**
 * @author thanhpv
 * @name アクセス制限
 */
@Getter
@AllArgsConstructor
public class AccessRestrictionsDto {

	/** アクセス制限機能管理区分  */
	public Integer accessLimitUseAtr;
	
	/** 契約コード  */
	public String tenantCode;
	
	/** 許可IPアドレス  */
	public List<AllowedIPAddressDto> whiteList;
	
	/** 利用PCのIPアドレス */
	public String userIpAddress;

	public AccessRestrictionsDto(AccessRestrictions domain) {
		super();
		this.accessLimitUseAtr = domain.getAccessLimitUseAtr().value;
		this.tenantCode = domain.getTenantCode().v();
		this.whiteList = domain.getWhiteList().stream().map(c->new AllowedIPAddressDto(c)).collect(Collectors.toList());
	}
	
}
