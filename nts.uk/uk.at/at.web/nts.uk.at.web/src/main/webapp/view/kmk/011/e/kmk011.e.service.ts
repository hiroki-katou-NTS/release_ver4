module nts.uk.at.view.kmk011.e {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            saveAllSetting: "at/record/divergence/time/workTypeDivergenceRefTime/save",
            findAllItemSetting: "at/record/divergence/time/workTypeDivergenceRefTime/find",
            deleteHist: "at/record/divergence/time/history/workTypeDivergenceRefTime/remove",
            findListHistory: "at/record/divergence/time/history/workTypeDivergenceRefTime/findAll",
            findListDivergenceTime: "at/record/divergencetime/setting/getalldivtime",
            findListWorkType: "at/record/businesstype/findAll"
        };

        export function save(data: model.WorkTypeDivergenceRefTimeSaveCommand): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveAllSetting, data);
        }

        export function getAllItemSetting(wkTypeCode: string, historyId: string): JQueryPromise<any> {
            if (nts.uk.text.isNullOrEmpty(historyId)) {
                historyId = "0";
            }
            if (nts.uk.text.isNullOrEmpty(wkTypeCode)) {
                wkTypeCode = "0";
            }
            return nts.uk.request.ajax("at", path.findAllItemSetting + "/" + wkTypeCode + "/" + historyId);
        }

        export function getAllHistory(workTypeCode: string): JQueryPromise<any> {
            if (nts.uk.text.isNullOrEmpty(workTypeCode)){
                    workTypeCode = "0";
            }
            return nts.uk.request.ajax("at", path.findListHistory+ "/" + workTypeCode);
        }

        export function deleteHistory(command: any): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.deleteHist, command)
        }

        export function getAllDivergenceTime(): JQueryPromise<model.DivergenceTimeDto[]> {
            return nts.uk.request.ajax("at", path.findListDivergenceTime);
        }
        
        export function getAllWorkType(): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.findListWorkType);
        }
    }
    export module model {
        export class WorkTypeDivergenceReferenceTimeHistoryDto {
            historyId: string;
            startDate: string;
            endDate: string;
        }

        export class DivergenceTimeDto {
            divergenceTimeNo: number;
            divergenceTimeName: string;
            divergenceTimeUseSet: number;

            constructor(divergenceTimeNo: number, divergenceTimeName: string, divergenceTimeUseSet: number) {
                this.divergenceTimeNo = divergenceTimeNo
                this.divergenceTimeName = divergenceTimeName
                this.divergenceTimeUseSet = divergenceTimeUseSet
            }

        }

        export class WorkTypeDivergenceRefTimeSaveDto {
            workTypeCodes: string;
            divergenceTimeNo: number;
            notUseAtr: number;
            historyId: string;
            alarmTime: number;
            errorTime: number;

            constructor(workTypeCodes: string, divergenceTimeNo: number, notUseAtr: number, historyId: string, alarmTime: number, errorTime: number) {
                this.workTypeCodes = workTypeCodes;
                this.divergenceTimeNo = divergenceTimeNo;
                this.notUseAtr = notUseAtr;
                this.historyId = historyId;
                this.alarmTime = alarmTime;
                this.errorTime = errorTime;
            }
        }

        export class WorkTypeDivergenceRefTimeSaveCommand {
            listDataSetting: WorkTypeDivergenceTimeSettingDto[];

            constructor(listComDivergenceTimeSetting: WorkTypeDivergenceTimeSettingDto[]) {
                this.listDataSetting = listComDivergenceTimeSetting;
            }
        }

        export class WorkTypeDivergenceTimeSettingDto {
            divergenceTimeNo: KnockoutObservable<number>;
            notUseAtr: KnockoutObservable<number>;
            historyId: KnockoutObservable<string>;
            alarmTime: KnockoutObservable<number>;
            errorTime: KnockoutObservable<number>;

            constructor() {
                this.divergenceTimeNo = ko.observable(0);
                this.notUseAtr = ko.observable(0);
                this.historyId = ko.observable(null);
                this.alarmTime = ko.observable(null);
                this.errorTime = ko.observable(null);
            }

            updateData(data: any) {
                this.divergenceTimeNo(data.divergenceTimeNo);
                this.notUseAtr(data.notUseAtr);
                this.alarmTime(data.divergenceReferenceTimeValue.alarmTime);
                this.errorTime(data.divergenceReferenceTimeValue.errorTime);
            }

        }
    }
}