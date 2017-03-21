module nts.uk.pr.view.qpp018.c {
    export module service {
        
        // Service paths.
        let paths: any = {
            findCheckListPrintSetting: "ctx/pr/report/insurance/checklist/find",
            saveCheckListPrintSetting: "ctx/pr/report/insurance/checklist/save"
        };
        
        /**
         * get All CheckList Print Setting
         */
        export function findCheckListPrintSetting(): JQueryPromise<model.CheckListPrintSettingDto> {
            return nts.uk.request.ajax(paths.findCheckListPrintSetting);
        }
        
        /**
         * save checklist print setting
         */
        export function saveCheckListPrintSetting(command: any): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.saveCheckListPrintSetting, command)
        }
        
        /**
         * model
         */
        export module model {
            
            export interface CheckListPrintSettingDto {
                
                showCategoryInsuranceItem: boolean;
                showDetail: boolean;
                showOffice: boolean;
                showTotal: boolean;
                showDeliveryNoticeAmount: boolean;
            }
        }
    }
}
