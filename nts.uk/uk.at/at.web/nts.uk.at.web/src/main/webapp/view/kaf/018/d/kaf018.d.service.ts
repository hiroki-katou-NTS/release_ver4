module nts.uk.at.view.kaf018.d.service {
    var paths: any = {
        initApprovalSttRequestContentDis: "at/request/application/approvalstatus/initApprovalSttRequestContentDis"
    }
    export function initApprovalSttRequestContentDis(obj) : JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.initApprovalSttRequestContentDis, obj);
    }
}