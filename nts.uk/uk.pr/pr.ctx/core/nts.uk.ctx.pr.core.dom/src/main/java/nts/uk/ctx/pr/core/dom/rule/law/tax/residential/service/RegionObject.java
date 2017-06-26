/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lanlt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class RegionObject {
	@Getter
	@Setter
	private String regionCode;
	@Getter
	@Setter
	private String regionName;
	@Getter
	@Setter
	private List<PrefectureObject> prefectures;

}
