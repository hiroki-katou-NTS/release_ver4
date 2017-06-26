module nts.uk.pr.view.qmm016.a {
    /**
     * Module service.
     */
    export module service {
        var path = {
            loadHistoryByUuid: 'pr/proto/wagetable/find/{0}',
            loadDemensionList: 'pr/proto/wagetable/demensions',
            loadElementTypeList: 'pr/proto/wagetable/elements',
            initWageTable: 'pr/proto/wagetable/init',
            genitem: 'pr/proto/wagetable/reference/genitem',
            updateHistory: 'pr/proto/wagetable/update',
            certificateInfo: 'pr/proto/wagetable/sp/certifies'
        }

        /**
         * Service class.
         */
        export class Service extends base.simplehistory.service.BaseService<model.WageTable, model.WageTableHistory> {
            /**
             * Load history by uuid.
             */
            loadHistoryByUuid(uuid: string): JQueryPromise<model.WageTableHistoryModel> {
                return nts.uk.request.ajax(nts.uk.text.format(path.loadHistoryByUuid, uuid));
            }

            /**
             * Load demension list.
             */
            loadDemensionList(): JQueryPromise<Array<model.DemensionItemDto>> {
                return nts.uk.request.ajax(path.loadDemensionList);
            }

            /**
             * Load elements type list.
             */
            loadElementList(): JQueryPromise<Array<model.ElementTypeDto>> {
                return nts.uk.request.ajax(path.loadElementTypeList);
            }

            /**
             * Init wage table. 
             */
            initWageTable(data: {wageTableHeadDto: model.WageTableHeadDto, startMonth: number}): JQueryPromise<base.simplehistory.model.HistoryModel> {
                return nts.uk.request.ajax(path.initWageTable, data);
            }

            /**
             * Update history.
             */
            updateHistory(data: {code: string, name: string, memo: string, wtHistoryDto: model.WageTableHistoryDto}): JQueryPromise<any> {
                return nts.uk.request.ajax(path.updateHistory, data);
            }
            
            /**
             * Certificate
             */
            loadCertificate(): JQueryPromise<model.CertifyGroupOutModel> {
                return nts.uk.request.ajax(path.certificateInfo);
            }

            /**
             * Generate item table.
             */
            genearetItemSetting(data: {historyId: string, settings: Array<model.ElementSettingDto>}): JQueryPromise<Array<model.ElementSettingDto>> {
                var dfd = $.Deferred();

                // Call service.
                nts.uk.request.ajax(path.genitem, data).done((res: any) => {
                    dfd.resolve(res.elementSettings);
                }).fail(function(res) {
                    dfd.reject(res);
                });
                    
                // Promise.
                return dfd.promise();
            }
        }

        /**
         * Instance.
         */
        export var instance: Service = new Service({
            historyMasterPath: 'pr/proto/wagetable/masterhistory',
            createHisotyPath: 'pr/proto/wagetable/history/create',
            deleteHistoryPath: 'pr/proto/wagetable/history/delete',
            updateHistoryStartPath: 'pr/proto/wagetable/history/update/start'
        });
    }
    
    /**
     * Model module.
     */
    export module model {
        /**
         * Wage table.
         */
        export interface WageTable extends base.simplehistory.model.MasterModel<WageTableHistory> {}
        
        /**
         * Wage table history model.
         */
        export interface WageTableHistory extends base.simplehistory.model.HistoryModel {}
        
        /**
         * Demension type.
         */
        export interface DemensionElementCountType {
            code: number;
            name: string;
            isCertification: boolean;
            isAttendance: boolean;
        }

        /**
         * Element type.
         */
        export interface ElementTypeDto {
            value: number;
            isCodeMode: boolean;
            isRangeMode: boolean;
            displayName: string;
        }

        /**
         * All Demession.
         */
        export var allDemension = [
            { code: 0, name: '一次元', isCertification: false, isAttendance: false },
            { code: 1, name: '二次元', isCertification: false, isAttendance: false },
            { code: 2, name: '三次元', isCertification: false, isAttendance: false },
            { code: 3, name: '資格', isCertification: true, isAttendance: false },
            { code: 4, name: '精皆勤手当', isCertification: false, isAttendance: true }
        ]

        /**
         * Normal demension.
         */
        export var normalDemension = _.filter(allDemension, (di) => {return !di.isCertification && !di.isAttendance});

        /**
         * Special demension.
         */
        export var specialDemension = _.filter(allDemension, (di) => {return di.isCertification || di.isAttendance});

        /**
         * Demension map.
         */
        export var demensionMap: {[index: number]: DemensionElementCountType} = new Array<any>();
        _.forEach(allDemension, (de) => {
            demensionMap[de.code] = de;
        })

        /**
         * Wage table history.
         */
        export interface WageTableHistoryModel {
            /** The head. */
            head: WageTableHeadDto
            
            /** The history. */
            history: WageTableHistoryDto;
        }

        /**
         * Wage table demension detail dto.
         */
        export interface DemensionItemDto {
            type: number;
            code: string;
            name: string;
        }

        /**
         * Element dto.
         */
        export interface ElementDto {
            demensionNo: number;
            demensionName: string;
            type: number;
            referenceCode: string;
        }

        /**
         * ELement - item.
         */
        export interface ItemDto {
            uuid: string;
            displayName: string;
            referenceCode?: string;
            orderNumber?: number;
            startVal?: number;
            endVal?: number
        }

        /**
         * Element setting.
         */
        export interface ElementSettingDto {
            demensionName: string;
            demensionNo: number;
            type: number;
            itemList: Array<ItemDto>
            upperLimit?: number;
            lowerLimit?: number;
            interval?: number;
        }

        /**
         * Cell item dto.
         */
        export interface CellItemDto {
            element1Id: string;
            element2Id?: string;
            element3Id?: string;
            amount: number;
        }

        /**
         * Wage table head dto.
         */
        export interface WageTableHeadDto {
            /** The code. */
            code: string;

            /** The name. */
            name: string;

            /** The demension set. */
            mode: number;

            /** The memo. */
            memo: string;

            /**
             * Elements list.
             */
            elements: Array<ElementDto>;
        }

        /**
         * Wgate table history dto.
         */
        export class WageTableHistoryDto {
            /** The head. */
            head: WageTableHeadDto;

            /** The history id. */
            historyId: string;
            
            /** The start month. */
            startMonth: number;
            
            /** The end month. */
            endMonth: number;
            
            /** The demension details. */
            elements: Array<ElementSettingDto>;

            /** The value items. */
            valueItems: Array<CellItemDto>;
            
            constructor() {
            }
        }
        
        /**
         * Wage table item dto.
         */
        export interface WageTableItemDto {
            /** The element 1 id. */
            element1Id: string;
            
            /** The element 2 id. */
            element2Id: string;
            
            /** The element 3 id. */
            element3Id: string;
            
            /** The amount. */
            amount: number;
        }
        

        export interface CertifyGroupOutModel {
            certifyGroups: Array<model.CertifyGroupDto>
        }
        
        export interface CertifyGroupDto {
            /** The code. */
            code: string;
            
            /** The name. */
            name: string;
            
            /** The memo. */
            multiApplySet: number
            
            /** The certify items. */
            certifyItems: Array<CertifyItemDto>;
        }

        export interface CertifyItemDto {
            /** The code. */
            code: string;

            /** The name. */
            name: string;

        }
    }
}