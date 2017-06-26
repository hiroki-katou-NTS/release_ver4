module qet001.i {
    export module service {
        import WageLedgerOutputSetting = qet001.a.service.model.WageLedgerOutputSetting;

        // Service paths.
        var servicePath = {
            findAggregateItemsByCategory: 'ctx/pr/report/wageledger/aggregateitem/findByCate',
            findAggregateItemDetail: 'ctx/pr/report/wageledger/aggregateitem/findBySubject',
            findMasterItems: 'ctx/pr/report/masteritem/findAll',
            saveAggregateItem: 'ctx/pr/report/wageledger/aggregateitem/save',
            removeAggegateItem: 'ctx/pr/report/wageledger/aggregateitem/remove'
        }
        
        /**
         * Find master items.
         */
        export function findMasterItems() : JQueryPromise<Item[]> {
            return nts.uk.request.ajax(servicePath.findMasterItems);
        }
        
        /**
         * Find Aggregate item by category and payment type.
         */
        export function findAggregateItemsByCategory(category: string, paymentType: string): JQueryPromise<Item[]> {
            return nts.uk.request.ajax(servicePath.findAggregateItemsByCategory + '/' + category + '/' + paymentType);
        }
        
        /**
         * Find aggregate item detail.
         */
        export function findAggregateItemDetail(category: string, paymentType: string, code: string) : JQueryPromise<Item> {
            var subject = {
                code: code,
                category: category,
                paymentType: paymentType
            }
            return nts.uk.request.ajax(servicePath.findAggregateItemDetail, subject);
        }
        
        /**
         * Save aggregate item.
         */
        export function save(data: viewmodel.AggregateItemDetail): JQueryPromise<void> {
            // Convert to json data.
            var dataJson = {
                subject: {
                    category: data.category,
                    paymentType: data.paymentType,
                    code: data.code(),
                },
                name: data.name(),
                showNameZeroValue: data.showNameZeroValue(),
                showValueZeroValue: data.showValueZeroValue(),
                createMode: data.createMode(),
                subItems: data.subItems().map(item => item.code)
            }
            return nts.uk.request.ajax(servicePath.saveAggregateItem, dataJson);
        }
        
        /**
         * Remove aggregate item.
         */
        export function remove(category: string, paymentType: string, code: string): JQueryPromise<void> {
            var subject = {
                code: code,
                category: category,
                paymentType: paymentType
            }
            return nts.uk.request.ajax(servicePath.removeAggegateItem, {subject});
        }
        
        /**
         * Aggregate item class.
         */
        export interface Item {
            code: string;
            name: string;
            paymentType?: string;
            category: string;
            orderNumber? : number;
            showNameZeroValue?: boolean;
            showValueZeroValue?: boolean;
            subItems?: SubItem[];
        }
        
        /**
         * Sub item class.
         */
        export interface SubItem {
            code: string;
            name: string;
        }
    }
}
