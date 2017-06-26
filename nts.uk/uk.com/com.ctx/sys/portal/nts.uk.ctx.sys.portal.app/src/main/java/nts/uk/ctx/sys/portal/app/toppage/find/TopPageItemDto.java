package nts.uk.ctx.sys.portal.app.toppage.find;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class TopPageItemDto.
 */
@Getter
@Setter
public class TopPageItemDto {
	
	/** The top page code. */
	public String topPageCode;

	/** The top page name. */
	public String topPageName;

	/**
	 * @param topPageCode
	 * @param topPageName
	 */
	public TopPageItemDto(String topPageCode, String topPageName) {
		super();
		this.topPageCode = topPageCode;
		this.topPageName = topPageName;
	}
}
