package nts.uk.ctx.at.schedule.dom.adapter;

import java.util.List;

public interface ScTimeAdapter {
	/**
	 * Request List #91
	 * @param param
	 * @return
	 */
	List<ScTimeImport> calculation(List<ScTimeParam> param);
	
	/**
	 * Request List #91
	 * @param param
	 * @return
	 */
	ScTimeImport calculation(ScTimeParam param);
}
