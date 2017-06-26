package nts.uk.ctx.sys.portal.app.toppage.find;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopPageDto {

	/** The top page code. */
	public String topPageCode;

	/** The top page name. */
	public String topPageName;

	/**
	 * Instantiates a new top page dto.
	 *
	 * @param topPageCode the top page code
	 * @param topPageName the top page name
	 */
	public TopPageDto(String topPageCode, String topPageName) {
		super();
		this.topPageCode = topPageCode;
		this.topPageName = topPageName;
	}
}
