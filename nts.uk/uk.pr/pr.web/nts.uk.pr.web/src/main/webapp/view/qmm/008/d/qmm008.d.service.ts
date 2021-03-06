module nts.uk.pr.view.qmm008.d.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    var paths = {
        defaultData: "ctx/pr/core/socialinsurance/socialinsuranceoffice/start",
        findByCode: "ctx/pr/core/socialinsurance/socialinsuranceoffice/findByCode/{0}",
        update: "ctx/pr/core/socialinsurance/socialinsuranceoffice/update",
        create: "ctx/pr/core/socialinsurance/socialinsuranceoffice/create",
        deleteOffice: "ctx/pr/core/socialinsurance/socialinsuranceoffice/remove",
        exportExcel: "file/core/socialinsurance/socialinsuranceoffice/export"
    }

    export function exportExcel(): JQueryPromise<any> {
        return nts.uk.request.exportFile( paths.exportExcel);
    }

    export function defaultData(): JQueryPromise<any> {
        return ajax("pr",paths.defaultData);
    }
    export function findByCode(code : string) : JQueryPromise<any>{
        let _path = format(paths.findByCode, code);
        return ajax('pr', _path);
    }
    export function update(command) : JQueryPromise<any>{
        return ajax('pr', paths.update, command);
    }
    export function create(command) : JQueryPromise<any>{
        return ajax('pr', paths.create, command);
    }
    export function deleteOffice(command: any){
        return ajax('pr', paths.deleteOffice, command);
    }
}