module nts.uk.pr.view.qmm040.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths: any = {
        salIndAmountNameByCateIndicator: "ctx/pr/core/ws/wageprovision/individualwagecontract/allSalIndAmountNameflowCateIndicator/{0}",
        salIndAmountHisByPeValCode: "ctx/pr/core/ws/wageprovision/individualwagecontract/salIndAmountHisByPeValCode",
        salIndAmountUpdateAll: "ctx/pr/core/ws/wageprovision/individualwagecontract/salIndAmountUpdateAll",
        employeeReferenceDate: "ctx/pr/core/ws/wageprovision/individualwagecontract/employeeReferenceDate",
    };

    export function salIndAmountNameByCateIndicator(cateIndicator: number): JQueryPromise<any> {
        let _path = format(paths.salIndAmountNameByCateIndicator, cateIndicator);
        return ajax('pr', _path);
    }

    export function salIndAmountHisByPeValCode(command): JQueryPromise<any> {
        return ajax('pr', paths.salIndAmountHisByPeValCode, command);
    }

    export function employeeReferenceDate(): JQueryPromise<any> {
        let _path = format(paths.employeeReferenceDate);
        return ajax('pr', _path);
    }

    export function salIndAmountUpdateAll(command): JQueryPromise<any> {
        return ajax('pr', paths.salIndAmountUpdateAll, command);
    }
}