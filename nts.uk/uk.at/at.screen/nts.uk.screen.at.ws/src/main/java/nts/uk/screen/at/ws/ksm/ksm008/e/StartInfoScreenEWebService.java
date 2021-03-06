package nts.uk.screen.at.ws.ksm.ksm008.e;

import nts.uk.screen.at.app.ksm008.company.WorkingHoursAndWorkMethodDto;
import nts.uk.screen.at.app.ksm008.organization.*;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("screen/at/ksm008/e")
@Produces(MediaType.APPLICATION_JSON)
public class StartInfoScreenEWebService {

    @Inject
    Ksm008EStartupInfoProcessor startupInfoProcessor;

    @Inject
    GetRelationshipDetailsProcessor relationshipDetailsProcessor;

    @Inject
    GetLstRelshipsBetweenOgrWorkProcessor betweenOgrWorkProcessor;

    /**
     * 初期起動の情報取得する
     */
    @POST
    @Path("getStartupInfo")
    public Ksm008EStartInfoDto get(StartInfoPrams startInfoPrams) {
        return startupInfoProcessor.getStartupInfo(startInfoPrams);
    }

    /**
     * 組織の勤務方法の関係性明細を表示する
     */
    @POST
    @Path("getDetails")
    public RelationshipDetailDto get(RequestPrams requestPrams) {
        return relationshipDetailsProcessor.getRelationshipDetails(requestPrams);
    }

    /**
     * 組織の勤務方法の関係性リストを取得する
     */
    @POST
    @Path("getLstRelships")
    public List<WorkingHoursAndWorkMethodDto> get(RequestRelshipPrams requestPrams) {
        return betweenOgrWorkProcessor.getLstRelshipsBetweenOgrWork(requestPrams);
    }
}
