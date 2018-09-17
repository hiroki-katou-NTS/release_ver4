package nts.uk.ctx.pr.core.app.find.laborinsurance;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.laborinsurance.NameOfEachBusiness;
import nts.uk.ctx.pr.core.dom.laborinsurance.OccAccInsurBus;
import nts.uk.ctx.pr.core.dom.laborinsurance.OccAccInsurBusinessName;


import java.util.Comparator;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* 労災保険事業
*/
@AllArgsConstructor
@Value
public class OccAccInsurBusDto
{

    /**
     * 会社ID
     */
    private String cid;

    /**
     * 労災保険事業No
     */
    private int occAccInsurBusNo;

    /**
     * 利用する
     */
    private int toUse;

    /**
     * 名称
     */
    private String name;
    
    public static List<OccAccInsurBusDto> fromDomain(OccAccInsurBus domain)
    {
        List<OccAccInsurBusDto> occAccIsHisDtoList = domain.getEachBusiness().stream().map(item -> {
            return new OccAccInsurBusDto(domain.getCid(),item.getOccAccInsurBusNo(),item.getToUse(),item.getName().get().v());
        }).sorted(Comparator.comparing(OccAccInsurBusDto::getOccAccInsurBusNo)).collect(Collectors.toList());

        return occAccIsHisDtoList;
    }
    
}
