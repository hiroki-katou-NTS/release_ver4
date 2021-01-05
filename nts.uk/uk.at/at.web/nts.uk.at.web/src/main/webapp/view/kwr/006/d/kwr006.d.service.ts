module nts.uk.at.view.kwr006.d {
    export module service {
        const SLASH = "/";

        var paths = {
            getDataStartPage: "at/function/monthlyworkschedule/findCopy",
            executeCopy: "at/function/monthlyworkschedule/executeCopy"
        }

        export function getDataStartPage(): JQueryPromise<any> {
            return nts.uk.request.ajax('at', paths.getDataStartPage);
        }

        export function executeCopy(command: any): JQueryPromise<any> {
            return nts.uk.request.ajax('at', paths.executeCopy, command);
        }
    }
}