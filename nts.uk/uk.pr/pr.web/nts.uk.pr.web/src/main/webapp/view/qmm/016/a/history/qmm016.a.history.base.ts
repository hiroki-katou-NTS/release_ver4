module nts.uk.pr.view.qmm016.a.history.base {
    /**
     * Base view model.
     */
    export abstract class BaseHistoryViewModel {
        /**
         * Element settings.
         */
        elementSettings: Array<model.ElementSettingDto>;

        /**
         * Html path.
         */
        htmlPath: string;

        /**
         * History
         */
        history: model.WageTableHistoryDto;

        constructor(htmlPath: string, history: model.WageTableHistoryDto) {
            var self = this;
            self.htmlPath = htmlPath;
            self.history = history;
            self.elementSettings = history.elements;
        }

        /**
         * On load.
         */
        abstract onLoad(): JQueryPromise<any>;

        /**
         * Refresh element settings.
         */
        refreshElementSettings(elementSettings: Array<model.ElementSettingDto>): void {
            var self = this;
            self.elementSettings = elementSettings;
            self.onRefreshElement();
        }

        /**
         * On refresh element.
         */
        abstract onRefreshElement(): void;
        
        /**
         * Get setting cell item.
         */
        abstract getCellItem(): Array<model.CellItemDto>;

        /**
         * Paste data from excel.
         */
        abstract pasteFromExcel(): void;
    }
}