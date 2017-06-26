module nts.uk.pr.view.qpp007.j {
    export module service {
        var paths: any = {
            findSalaryAggregateItem: "ctx/pr/report/salary/aggregate/item/findSalaryAggregateItem",
            saveSalaryAggregateItem: "ctx/pr/report/salary/aggregate/item/save",
            findAllMasterItem: "ctx/pr/report/masteritem/findAll"
        };

        //connection server find Salary Aggregate Item
        export function findSalaryAggregateItem(salaryAggregateItemInDto: model.SalaryAggregateItemInDto)
            : JQueryPromise<model.SalaryAggregateItemFindDto> {
            //call server service
            return nts.uk.request.ajax(paths.findSalaryAggregateItem, salaryAggregateItemInDto);
        }

        //connection server find Salary Aggregate Item
        export function saveSalaryAggregateItem(salaryAggregateItemSaveDto: model.SalaryAggregateItemSaveDto)
            : JQueryPromise<void> {
            //call server service
            var data = { salaryAggregateItemSaveDto: salaryAggregateItemSaveDto };
            return nts.uk.request.ajax(paths.saveSalaryAggregateItem, data);
        }

        //connection server find all
        export function findAllMasterItem(): JQueryPromise<model.SalaryItemDto[]> {
            //call server service
            return nts.uk.request.ajax(paths.findAllMasterItem);
        }


        export module model {

            export class SalaryItemDto {
                code: string;
                name: string;
                category: string;
                
            }

            export class SalaryAggregateItemFindDto {

                salaryAggregateItemCode: string;
                salaryAggregateItemName: string;
                subItemCodes: SalaryItemDto[];
            }

            export class SalaryAggregateItemInDto {
                taxDivision: number;
                aggregateItemCode: string;
            }

            export class SalaryAggregateItemSaveDto {

                /** The salary aggregate item code. */
                salaryAggregateItemCode: string;

                /** The salary aggregate item name. */
                salaryAggregateItemName: string;

                /** The sub item codes. */
                subItemCodes: SalaryItemDto[];

                /** The tax division. */
                taxDivision: number;

            }
        }
    }
}