package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemMasterQueryParam {
	private int categoryAtr; 
	private List<String> itemCodes;
}
