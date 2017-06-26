module nts.uk.pr.view.base.simplehistory {
    export module model {
        /**
         * Contains master model.
         */
        export interface MasterModel<T extends HistoryModel> {
            /**
             * Contains code master.
             */
            code: string;

            /**
             * Contains name master.
             */
            name: string;

            /**
             * History list.
             */
            historyList: T[];
        }
        
        
        export interface HistoryModel {
            /**
             * Hisotry uuid.
             */
            uuid: string;

            /**
             * Contains start of history.
             */
            start: number;

            /**
             * Contain end of history.
             */
            end: number;
        }
    }
    
    export module service {
        /**
         * Services.
         */
        export interface Service<T extends model.MasterModel<V>, V extends model.HistoryModel> {
            /**
             * Load master model list.
             */
            loadMasterModelList(): JQueryPromise<Array<T>>;

            /**
             * Create year month.
             * Return: Promise of created history uuid.
             */
            createHistory(masterCode: string, startYearMonth: number, isCopyFromLatest: boolean): JQueryPromise<V>;
            
            /**
             * Delete history.
             * @param historyUuid history uuid.
             */
            deleteHistory(masterCode: string, historyUuid: string): JQueryPromise<void>;

            /**
             * Update history start.
             */
            updateHistoryStart(masterCode: string, historyUuid: string, newStart: number): JQueryPromise<void>;
        }

        /**
         * Contains path.
         */
        export interface Path {
            historyMasterPath: string;
            createHisotyPath: string;
            deleteHistoryPath: string;
            updateHistoryStartPath: string;
        }

        /**
         * Simple base service.
         * Provide load master with path.
         */
        export abstract class BaseService<T extends model.MasterModel<V>, V extends model.HistoryModel> implements Service<T, V> {
            path: Path;
            constructor(path: Path) {
                var self = this;
                self.path = path;
            }

            /**
             * Load master model list.
             */
            loadMasterModelList(): JQueryPromise<Array<T>> {
                var self = this;
                return nts.uk.request.ajax(self.path.historyMasterPath);
            }

            /**
             * @see Service.
             */
            createHistory(masterCode: string, startYearMonth: number, isCopyFromLatest: boolean): JQueryPromise<V> {
                var self = this;
                return nts.uk.request.ajax(self.path.createHisotyPath, {
                    masterCode: masterCode,
                    startYearMonth: startYearMonth,
                    copyFromLatest: isCopyFromLatest 
                });
            }

            /**
             * @see Service.
             */
            deleteHistory(masterCode: string, historyUuid: string): JQueryPromise<void> {
                var self = this;
                return nts.uk.request.ajax(self.path.deleteHistoryPath, {
                    historyId: historyUuid
                });
            }

            /**
             * @see Service
             */
            updateHistoryStart(masterCode: string, historyUuid: string, newStart: number): JQueryPromise<void> {
                var self = this;
                return nts.uk.request.ajax(self.path.updateHistoryStartPath, {
                    historyId: historyUuid,
                    newYearMonth: newStart
                })
            }
        }
    }
}