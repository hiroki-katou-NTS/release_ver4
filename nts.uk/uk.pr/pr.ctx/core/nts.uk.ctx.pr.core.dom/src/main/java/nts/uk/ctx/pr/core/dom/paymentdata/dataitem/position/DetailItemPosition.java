package nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * 行表示位置
 * 
 * @author vunv
 *
 */
public class DetailItemPosition extends DomainObject {

	/**
	 * 行
	 */
	@Getter
	private final LinePosition linePosition;

	/**
	 * 列
	 */
	@Getter
	private final ColumnPosition columnPosition;

	public DetailItemPosition(LinePosition linePosition, ColumnPosition columnPosition) {
		super();
		this.linePosition = linePosition;
		this.columnPosition = columnPosition;
	}
	
}
