package nts.uk.shr.com.menu.adapter;

import java.util.List;

public interface ShareMenuAdapter {

	public List<ProgramNameDto> getProgramName(String pgId, String companyId);
	
	public String userName();
	
	public boolean showManual();
}
