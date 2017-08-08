module nts.uk.at.view.kmf004.a.service {
    var paths: any = {
        findDataOfTopPageJobSet: "sys/portal/toppagesetting/jobset/find",
        findBySystemMenuCls: "sys/portal/standardmenu/findBySystemMenuCls",
        findDataForAfterLoginDis: "sys/portal/standardmenu/findDataForAfterLoginDis",
        findByCId: "sys/portal/toppagesetting/findByCId",
        findTopPagePersonSet: "sys/portal/toppagesetting/personset/findBySid",
    }

    export function findDataOfTopPageJobSet(listJobId): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.findDataOfTopPageJobSet, listJobId);
    }
    
    export function findTopPagePersonSet(listSid: any): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.findTopPagePersonSet, listSid);
    }

    export function findByCId(): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.findByCId);
    }
    
    export function findBySystemMenuCls(): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.findBySystemMenuCls);
    }

    export function findDataForAfterLoginDis(): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.findDataForAfterLoginDis);
    }
}