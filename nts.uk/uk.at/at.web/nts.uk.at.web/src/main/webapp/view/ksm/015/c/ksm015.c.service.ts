module nts.uk.at.view.ksm015.c.service {
    /**
     *  Service paths
     */
    var paths: any = {
        isForA
        getShiftMasterByWorkplace: 'ctx/at/shared/workrule/shiftmaster/getlistByWorkPlace',
        register: 'ctx/at/shared/workrule/shiftmaster/register/shiftmaster/org',
        delete: 'ctx/at/shared/workrule/shiftmaster/delete/org'
    }

    export function getShiftMasterByWorkplace(data): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getShiftMasterByWorkplace, data);
    }

    export function registerOrg(data): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.register, data);
    }

    export function deleteOrg(data): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.delete, data);
    }
    
    /**
    * saveAsExcel
    **/
    // export function saveAsExcel(languageId: string): JQueryPromise<any> {
    //     let program= nts.uk.ui._viewModel.kiban.programName().split(" ");
    //     let domainType = "KSM011";
    //     if (program.length > 1){
    //         program.shift();
    //         domainType = domainType + program.join(" ");
    //     }
    //     return nts.uk.request.exportFile('/masterlist/report/print', {domainId: "ScheFuncControl", domainType: domainType,languageId: 'ja', reportType: 0});    
    // }
}
