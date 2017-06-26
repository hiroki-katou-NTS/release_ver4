module cmm013.a.service {
    var paths = {
        findAllJobTitle: "basic/organization/position/findAllJobTitle/",
        deleteJobTitle: "basic/organization/position/deleteJobTitle",
        getAllHistory: "basic/organization/position/getAllHist",
        regitry: "basic/organization/position/registryPosition",
        getAllUseKt: "ctx/proto/company/findByUseKtSet/",
        getAuth: "basic/organization/position/getAllJobRefAuth/",
        deleteJobTitleRef: "basic/organization/position/deleteJobTitleRef/",
        findAuth :"basic/organization/position/findAuth/"
    }
    /**
     * find all position
     */
    export function findAllJobTitle(historyId: string): JQueryPromise<Array<viewmodel.model.ListPositionDto>> {
        var dfd = $.Deferred<Array<viewmodel.model.ListPositionDto>>();
        return nts.uk.request.ajax("com", paths.findAllJobTitle + historyId);
    }
    /**
     * find by Use Kt
     */
    export function findByUseKt(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        return nts.uk.request.ajax("com", paths.getAllUseKt + "1")
    }
    /**
     * delete Position select
     */
    export function deleteJobTitle(position: viewmodel.model.DeleteJobTitle) {
        var dfd = $.Deferred<viewmodel.model.DeleteJobTitle>();
        return nts.uk.request.ajax("com", paths.deleteJobTitle, position)
    }
    /**
    * get all history
    */
    export function getAllHistory(): JQueryPromise<Array<viewmodel.model.ListHistoryDto>> {
        var dfd = $.Deferred<Array<viewmodel.model.ListHistoryDto>>();
        return nts.uk.request.ajax("com", paths.getAllHistory)
    }
    /**
     * add & update position && add & update history
     */
    export function registry(addHandler: viewmodel.model.registryCommand) {
        var dfd = $.Deferred<Array<any>>();
        return nts.uk.request.ajax("com", paths.regitry, addHandler)

    }
    /**
     * get all jobtitle ref auth
     */
    export function getAllJobTitleAuth(historyId: string, jobCode: string): JQueryPromise<Array<GetJobAuth>> {
        var dfd = $.Deferred<Array<any>>();
        return nts.uk.request.ajax("com", paths.getAuth + historyId + "/" + jobCode)
    /**
     * delete jobtitle ref auth
     */
    }
    export function deleteJobRefAuth(jobRefAuth: viewmodel.model.DeleteobRefAuth) {
        var dfd = $.Deferred<viewmodel.model.DeleteobRefAuth>();
        return nts.uk.request.ajax("com", paths.deleteJobTitleRef, jobRefAuth)
    }
    export function findAuth(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        return nts.uk.request.ajax("com", paths.findAuth + "KT")
    }

    export class GetJobAuth {
        historyId: string;
        jobCode: string;
        authCode: string;
        authName: string;
        referenceSettings: number;
        authScopeAtr: string;
        constructor(historyId: string, jobCode: string, authCode: string,
            authName: string, referenceSettings: number, authScopeAtr: string) {
            this.historyId = historyId;
            this.jobCode = jobCode;
            this.authCode = authCode;
            this.authName = authName;
            this.referenceSettings = referenceSettings;
            this.authScopeAtr = authScopeAtr;
        }
    }
}