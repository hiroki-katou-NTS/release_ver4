module nts.uk.at.view.kdp010.c.service {
    let paths: any = {
        getData: "at/record/stamp/timestampinputsetting/settingssmartphonestamp/get",
        save: "at/record/stamp/timestampinputsetting/settingssmartphonestamp/save"
    }

    export function save(param: any): void {
        return nts.uk.request.ajax("at", paths.save, param);
    }

    export function getData(): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getData);
    }
}