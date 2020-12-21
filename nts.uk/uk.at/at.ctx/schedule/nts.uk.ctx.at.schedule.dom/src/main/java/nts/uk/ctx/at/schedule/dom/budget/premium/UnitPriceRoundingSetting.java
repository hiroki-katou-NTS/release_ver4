package nts.uk.ctx.at.schedule.dom.budget.premium;

import lombok.AllArgsConstructor;
import org.eclipse.persistence.internal.xr.ValueObject;


/**
 * VO: 人件費単価丸め
 */
@AllArgsConstructor
public class UnitPriceRoundingSetting extends ValueObject{
    //1 : 端数処理 :人件費単価端数処理

    private  UnitPriceRounding priceRounding;

}
