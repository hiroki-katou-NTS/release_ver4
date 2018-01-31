package nts.uk.ctx.sys.portal.app.find.flowmenu;

import lombok.Value;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenu;

/**
 * @author hieult
 */
@Value
public class FlowMenuDto {
			
	private String toppagePartID;

	private String topPageCode;
	
	private String topPageName;
				
	private int type;
	
	private int widthSize;
	
	private int heightSize;
	
	private String fileID;
	
	private String fileName;
	
	private int defClassAtr;
	
	public static FlowMenuDto fromDomain (FlowMenu domain, StoredFileInfo fileInfo) {
		 return new FlowMenuDto (
				 domain.getToppagePartID(),
				 domain.getCode().v(),
				 domain.getName().v(),
				 domain.getType().value,
				 domain.getSize().getWidth().v(),
				 domain.getSize().getHeight().v(),
				 domain.getFileID(),
				 fileInfo == null? "" : fileInfo.getOriginalName(),
				 domain.getDefClassAtr().value);
	}
}
