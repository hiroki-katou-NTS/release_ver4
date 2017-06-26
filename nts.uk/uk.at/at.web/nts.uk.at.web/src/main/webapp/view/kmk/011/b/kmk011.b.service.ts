module kmk011.b.service {
     var paths = {
         getAllDivReason: "at/record/divergencetime/getalldivreason/",
         addDivReason: "at/record/divergencetime/adddivreason",
         updateDivReason: "at/record/divergencetime/updatedivreason",
         deleteDivReason: "at/record/divergencetime/deletedivreason"
    }

    /**
    * get all divergence reason
    */
    export function getAllDivReason(divTimeId: string): JQueryPromise<Array<viewmodel.model.Item>>{
        return nts.uk.request.ajax("at", paths.getAllDivReason + divTimeId);
    }  
    /**
    * add divergence reason
    */ 
    export function addDivReason(divReason: viewmodel.model.Item): JQueryPromise<Array<viewmodel.model.Item>>{
        return nts.uk.request.ajax("at", paths.addDivReason,divReason);
    }
    /**
    * update divergence reason
    */ 
    export function updateDivReason(divReason: viewmodel.model.Item): JQueryPromise<Array<viewmodel.model.Item>>{
        return nts.uk.request.ajax("at", paths.updateDivReason,divReason);
    }
    /**
    * delete divergence reason
    */ 
    export function deleteDivReason(divReason: viewmodel.model.Item): JQueryPromise<Array<viewmodel.model.Item>>{
        return nts.uk.request.ajax("at", paths.deleteDivReason,divReason);
    }
}