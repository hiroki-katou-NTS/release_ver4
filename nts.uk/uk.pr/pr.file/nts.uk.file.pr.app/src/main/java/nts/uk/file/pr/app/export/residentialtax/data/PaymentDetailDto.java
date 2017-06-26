package nts.uk.file.pr.app.export.residentialtax.data;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDetailDto {
	private String personId;
	private int ctgAtr;
	private String itemCode;
	private BigDecimal value;
	private int printLinePos; 
	private int itemPosColumn;
}
