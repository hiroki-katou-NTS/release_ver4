module nts.uk.at.view.daily.importing.service {
    var paths: any = {
        importData: "daily/import/import",
        exportErrors: "daily/import/export/errors"
    }
    
    export function importData(command): JQueryPromise<any> {
        return nts.uk.request.ajax('at', paths.importData, command);
    }

    export function exportErrors(data): JQueryPromise<any> {
        return nts.uk.request.ajax('at', paths.exportErrors, data);
    }
}