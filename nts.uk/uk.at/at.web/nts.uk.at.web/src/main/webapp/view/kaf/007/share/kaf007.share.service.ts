module nts.uk.at.view.kaf007.share {
    export module service {
        import ajax = nts.uk.request.ajax;
        import common = nts.uk.at.view.kaf007.share.common;
        import format = nts.uk.text.format;
        var paths = {
                getWorkChangeCommonSetting: "/at/request/application/workchange/getWorkChangeCommonSetting",
                getWorkchangeByAppID: "/at/request/application/workchange/getWorkchangeByAppID/{0}",
                addWorkChange: "/at/request/application/workchange/addworkchange",
                updateWorkChange: "/at/request/application/workchange/updateworkchange",
                getRecordWorkInfoByDate: "/at/request/application/workchange/getRecordWorkInfoByDate",
        }
        
        /**
         * 起動する
         * アルゴリズム「勤務変更申請画面初期（新規）」を実行する
         */
        export function getWorkChangeCommonSetting(): JQueryPromise<any> {
            return ajax("at", paths.getWorkChangeCommonSetting, {});
        }
        /**
         * 勤務変更申請の登録を実行する
         */
        export function addWorkChange(workchange: common.AppWorkChangeCommand): JQueryPromise<any> {
            return ajax("at", paths.addWorkChange, workchange);
        }
        /**
         * 
         */
        export function getWorkchangeByAppID(appId: string): JQueryPromise<any> {
            return ajax("at", format(paths.getWorkchangeByAppID, appId));
        }
        /**
         * 
         */
        export function updateWorkChange(workchange: common.AppWorkChangeCommand): JQueryPromise<any> {
            return ajax("at", paths.updateWorkChange, workchange);
        }
        /**
         * 
         */
        export function getRecordWorkInfoByDate(appDate: any): JQueryPromise<any> {
            return ajax("at", paths.getRecordWorkInfoByDate, appDate);
        }
    }
}
