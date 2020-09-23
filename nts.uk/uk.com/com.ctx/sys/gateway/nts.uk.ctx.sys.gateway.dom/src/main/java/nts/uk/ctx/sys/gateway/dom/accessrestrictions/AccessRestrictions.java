package nts.uk.ctx.sys.gateway.dom.accessrestrictions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.sys.gateway.dom.login.ContractCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author thanhpv
 * @name アクセス制限
 */
@Getter
@AllArgsConstructor
public class AccessRestrictions extends AggregateRoot{

	/** アクセス制限機能管理区分  */
	private NotUseAtr accessLimitUseAtr = NotUseAtr.NOT_USE;
	
	/** 契約コード  */
	private ContractCode contractCode;
	
	/** 許可IPアドレス  */
	private List<AllowedIPAddress> allowedIPaddress;
	
	/** [1] 許可IPアドレスを追加する */
	public void addIPAddress(AllowedIPAddress e) {
		for (AllowedIPAddress ip : this.allowedIPaddress) {
			if(ip.getStartAddress().equals(e.getStartAddress())) {
				throw new BusinessException("Msg_1835");
			}
		}
		this.allowedIPaddress.add(e);
	}
	
	/** [2] 許可IPアドレスを更新する */
	public void updateIPAddress(AllowedIPAddress oldIp, AllowedIPAddress newIp) {
		this.allowedIPaddress.removeIf(c->c.getStartAddress().equals(oldIp.getStartAddress()));
		this.addIPAddress(newIp);
		this.allowedIPaddress.sort((AllowedIPAddress x, AllowedIPAddress y) -> x.getStartAddress().toString().compareTo(y.getStartAddress().toString()));
	}
	
	/** [3] 許可IPアドレスを削除する */
	public void deleteIPAddress(IPAddressSetting e) {
		this.allowedIPaddress.removeIf(c->c.getStartAddress().equals(e));
	}

	public AccessRestrictions(int accessLimitUseAtr, String contractCode,
			List<AllowedIPAddress> allowedIPaddress) {
		super();
		this.accessLimitUseAtr = NotUseAtr.valueOf(accessLimitUseAtr);
		this.contractCode = new ContractCode(contractCode);
		this.allowedIPaddress = allowedIPaddress;
	}
	
}
