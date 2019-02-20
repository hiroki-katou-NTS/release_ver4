/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.ws.workplace.info;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.app.find.workplace.config.dto.WkpConfigInfoFindObject;
import nts.uk.ctx.bs.employee.app.find.workplace.dto.WkpInfoFindObject;
import nts.uk.ctx.bs.employee.app.find.workplace.dto.WorkplaceInfoDto;
import nts.uk.ctx.bs.employee.app.find.workplace.info.WorkplaceInfoFinder;
import nts.uk.ctx.bs.employee.app.find.workplace.personinfo.DisplayWorkplaceFinder;

/**
 * The Class WorkplaceInfoWebService.
 */
@Path("bs/employee/workplace/info")
@Produces(MediaType.APPLICATION_JSON)
public class WorkplaceInfoWebService extends WebService {

    /** The wkp info finder. */
    @Inject
    private WorkplaceInfoFinder wkpInfoFinder;
    
    @Inject
    private DisplayWorkplaceFinder displayWorkplaceFinder;
    
    /**
     * Gets the workplace info by history id.
     *
     * @param findObj the find obj
     * @return the workplace info by history id
     */
    @Path("findHistInfo")
    @POST
    public WorkplaceInfoDto getWorkplaceInfoByHistoryId(WkpInfoFindObject findObj) {
        return this.wkpInfoFinder.find(findObj);
    }
    
    /**
     * Gets the workplace info by cid and base date.
     *
     * @param object the object
     * @return the workplace info by cid and base date
     */
    @Path("findWorkplaceInfo")
    @POST
    public List<WorkplaceInfoDto> getWorkplaceInfoByCidAndBaseDate(WkpConfigInfoFindObject object) {
        return this.wkpInfoFinder.findWkpInfoByBaseDate(object.getBaseDate());
    }

    
    /**
     * Find by wkp id and base date.
     *
     * @param findObj the find obj
     * @return the workplace info dto
     */
    @Path("findDetail")
    @POST
    public WorkplaceInfoDto findByWkpIdAndBaseDate(WkpInfoFindObject findObj) {
        return this.wkpInfoFinder.findByWkpIdAndBaseDate(findObj);
    }
    
    @Path("display")
    @POST
    public List<String> getData(GeneralDate baseDate , List<String> listWorkPlaceID) {
        return this.displayWorkplaceFinder.getData(baseDate, listWorkPlaceID);
    }
}
