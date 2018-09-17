module nts.uk.pr.view.qmm012.h.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    var paths = {
        getValidityPeriodAndCycleSet: "ctx/pr/core/wageprovision/statementitem/validityperiodset/getValidityPeriodAndCycleSet/{0}",
        registerValidityPeriodAndCycleSet: "ctx/pr/core/wageprovision/statementitem/validityperiodset/getValidityPeriodAndCycleSet/{0}",
    }

    export function getValidityPeriodAndCycleSet(salaryItemId: string): JQueryPromise<any> {
        return ajax(format(paths.getValidityPeriodAndCycleSet, salaryItemId));
    }

    export function registerValidityPeriodAndCycleSet(command): JQueryPromise<any> {
        return ajax(paths.registerValidityPeriodAndCycleSet, command);
    }
}