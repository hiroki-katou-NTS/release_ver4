module cmm009.a.service {
    var paths = {
        getAllDepartment: "basic/organization/getalldepartment",
        getAllDepartmentByHistId: "basic/organization/getalldepbyhistid/",
        getMemoByHistId: "basic/organization/getmemobyhistid/",
        addDepartment: "basic/organization/adddepartment",
        updateDepartment: "basic/organization/updatedepartment",
        updatelistDepartment: "basic/organization/updatedepartment",
        updateEndDate: "basic/organization/updateenddate",
        updateEndDateByHistId: "basic/organization/updateenddatebyhistoryid",
        deleteHistory: "basic/organization/deletehistory",
        updateStartDateandEndDate: "basic/organization/updatestartdateandenddate",
        deleteDep: "basic/organization/deletedep",
        getAllHistory : "basic/organization/getAllhistoryDepartment"
        
    }

    export function deleteDepartment(department: viewmodel.DepartmentDeleteDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.deleteDep, department).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function upDateStartDateandEndDate(updateYMD: viewmodel.updateDateMY) {
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

    export function addDepartment(department: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.addDepartment, department).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function upDateDepartment(department: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateDepartment, department).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function upDateEndDate(obj: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updateEndDate, obj).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function upDateListDepartment(listdepartment: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updatelistDepartment, listdepartment).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function addListDepartment(listdepartment: any) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.addDepartment, listdepartment).done(
            function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
    * Get list dapartment tแบกi listHistory[0].
    */
    export function getAllDepartment(): JQueryPromise<viewmodel.DepartmentQueryResult> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getAllDepartment)
            .done(function(res: any) {
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
     * get list dapartment theo historyID
     */
    export function getAllDepartmentByHistId(historyId: string): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getAllDepartmentByHistId + historyId)
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
    export function getMemoByHistId(historyId: string): JQueryPromise<viewmodel.MemoDto> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getMemoByHistId + historyId)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
   
    }
