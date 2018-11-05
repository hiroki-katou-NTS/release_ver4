module nts.uk.pr.view.qmm020.f {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            getStateCorrelationHisPosition: "core/wageprovision/statementbindingsetting/getStateCorrelationHisPosition",
            registerCorrelationHisPosition: "core/wageprovision/statementbindingsetting/registerCorrelationHisPosition",
            getStateLinkSettingMasterPosition: "core/wageprovision/statementbindingsetting/getStateLinkMasterPosition/{0}/{1}"
        };

        export function getStateCorrelationHisPosition(): JQueryPromise<any> {
            return nts.uk.request.ajax(path.getStateCorrelationHisPosition);
        }
        export function registerCorrelationHisPosition(param: any): JQueryPromise<any> {
            return nts.uk.request.ajax(path.getStateCorrelationHisPosition);
        }

        export function getStateLinkSettingMasterPosition(hisId :string, start :number): JQueryPromise<any> {
            let _path = nts.uk.text.format(path.getStateLinkSettingMasterPosition, hisId, start);
            return nts.uk.request.ajax("pr", _path);
        }
    }
}