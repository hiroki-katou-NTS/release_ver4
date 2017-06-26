package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/** 按分設定 */
public class Distribute extends DomainObject {
	@Getter
	private DistributeWay method;
	@Getter
	private DistributeSet setting;
	public Distribute(DistributeWay method, DistributeSet setting) {
		super();
		this.method = method;
		this.setting = setting;
	}
	
}
