module nts.uk.pr.view.qpp007.c {
    export module service {
        import OutputSettingDto =  nts.uk.pr.view.qpp007.c.viewmodel.OutputSettingDto;
        import OutputSettingHeader = nts.uk.pr.view.qpp007.c.viewmodel.OutputSettingDto;

        /**
         *  Service paths
         */
        var paths: any = {
            save: "ctx/pr/report/salary/outputsetting/save",
            remove: "ctx/pr/report/salary/outputsetting/remove",
            findOutputSettingDetail: "ctx/pr/report/salary/outputsetting/find",
            findAllOutputSettings: "ctx/pr/report/salary/outputsetting/findall",
            findAllAggregateItems: "ctx/pr/report/salary/aggregate/item/findall",
            findAllMasterItems: "ctx/pr/report/masteritem/findAll"
        };

        /**
         *  Update
         */
        export function save(data: OutputSettingDto): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.save, data);
        }
        /**
         *  Delete
         */
        export function remove(code: string): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.remove, {code: code});
        }
        /**
         *  Find outputSetting detail
         */
        export function findOutputSettingDetail(id: string): JQueryPromise<OutputSettingDto> {
            return nts.uk.request.ajax(paths.findOutputSettingDetail + "/" + id);
        }
        /**
         *  Find all outputSettings
         */
        export function findAllOutputSettings(): JQueryPromise<Array<OutputSettingHeader>> {
            return nts.uk.request.ajax(paths.findAllOutputSettings);
        }
        /**
         *  Find all aggregateItems.
         */
        export function findAllAggregateItems(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.findAllAggregateItems);
        }
        /**
         * Find all masterItems.
         */
        export function findAllMasterItems(): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            return nts.uk.request.ajax(paths.findAllMasterItems);
        }

    }
}
