module cmm011.a.service {
    var paths = {
        getAllWorkPlace: "basic/organization/getallworkplace",
        getAllWorkPlaceByHistId: "basic/organization/getallwkpbyhistid/",
        getMemoWkpByHistId: "basic/organization/getmemowkpbyhistid/",
        updateEndDateofWkp: "basic/organization/updateenddateofwkp",
        updateEndDateByHistId: "basic/organization/updateenddatewkpbyhistoryid",
        deleteHistory: "basic/organization/deletehistorywkp",
        updateStartDateandEndDate: "basic/organization/updatestartdateandenddatewkp",
        deleteWorkPLace: "basic/organization/deleteworkplace",
        getAllWorkPLaceByHistId: "basic/organization/getallwkpbyhistid/",
        getMemoWorkPLaceByHistId: "basic/organization/getmemowkpbyhistid/",
        addWorkPlace: "basic/organization/addworkplace",
        updatelistWorkPLace: "basic/organization/updateworkplace",
        getAllHistory: "basic/organization/getAllHistoryWorkPlace"
    }

    export function upDateListWorkplace(listworkplace: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updatelistWorkPLace, listworkplace).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    // add single WorkPlace
    export function addWorkPlace(workplace: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.addWorkPlace, workplace).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    // add list WorkPlace
    export function addListWorkPlace(listworkplace: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.addWorkPlace, listworkplace).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * get list workplace theo historyID
     */
    export function getAllWorkPLaceByHistId(historyId: string): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getAllWorkPLaceByHistId + historyId)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * get Memo theo historyID
     */
    export function getMemoWorkPLaceByHistId(historyId: string): JQueryPromise<viewmodel.model.MemoDto> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getMemoWorkPLaceByHistId + historyId)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function deleteWorkPalce(workplace: viewmodel.model.WorkPlaceDeleteDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.deleteWorkPLace, workplace).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function upDateStartDateandEndDate(updateYMD: viewmodel.model.updateDateMY) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateStartDateandEndDate, updateYMD).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function deleteHistory(historyid: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.deleteHistory, historyid).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function updateEndDateByHistoryId(historyid: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateEndDateByHistId, historyid).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function upDateEndDateWkp(obj: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateEndDateofWkp, obj).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    /**
     * get list History
     */
    export function getAllHistory(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getAllHistory)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
   * Get list WorkPLace tแบกi listHistory[0].
   */
    export function getAllWorkplace(): JQueryPromise<viewmodel.model.WorkPlaceQueryResult> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getAllWorkPlace)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
    * get list dapartment theo historyID
    */
    export function getAllWorkPlaceByHistId(historyId: string): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getAllWorkPlaceByHistId + historyId)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /*
    * get Memo theo historyID
    */
    export function getMemoWkpByHistId(historyId: string): JQueryPromise<viewmodel.model.MemoDto> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getMemoWkpByHistId + historyId)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }





}