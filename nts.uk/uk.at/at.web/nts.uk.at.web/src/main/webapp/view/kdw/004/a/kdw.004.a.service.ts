module nts.uk.at.view.kdw004.a.service {
    var paths: any = {
        startscreen: "at/record/workrecord/approvalmanagement/startscreen",
        extractApprovalStatusData: "at/record/workrecord/approvalmanagement/extractApprovalStatusData",
        getDateRange: "at/record/workrecord/approvalmanagement/getdaterange/"
    }
    
    export function startscreen(param) {
        return nts.uk.request.ajax(paths.startscreen, param);
    }
    
    export function extractApprovalStatusData(param) {
        return nts.uk.request.ajax(paths.extractApprovalStatusData, param);
    }
    
    export function getDateRange(param,currentYearMonth) {
        return nts.uk.request.ajax(paths.getDateRange + param+"/"+currentYearMonth);
    }
}