/**
 * 
 */
package nts.uk.ctx.hr.develop.dom.databeforereflecting;

import lombok.AllArgsConstructor;

/**
 * 届出区分
 */
@AllArgsConstructor
public enum RequestFlag {

	Normal(0),

	Report(1);

	public final int value;

}
