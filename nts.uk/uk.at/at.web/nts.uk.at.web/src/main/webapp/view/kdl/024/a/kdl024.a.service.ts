module kdl024.a.service {
    var paths = {
        getExternalBudgetList: "at/schedule/budget/external/findallexternalbudget",
        insertExternalBudget: "at/schedule/budget/external/insertexternalbudget",
        updateExternalBudget: "at/schedule/budget/external/updateexternalbudget",
        deleteExternalBudget: "at/schedule/budget/external/deleteexternalbudget"
    }
    /**
     * get list External Budget
     */
    export function getListExternalBudget(): JQueryPromise<Array<viewmodel.ExternalBudgetDto>> {
        return nts.uk.request.ajax("at", paths.getExternalBudgetList);
    }
    
    /**
     * Update Budget
     * 
     */
    export function updateExternalBudget(updateBudgetCmd: any) {
        let command = {} as model.ExternalBudgetDto;
        command.externalBudgetCode = updateBudgetCmd.externalBudgetCode();
        command.externalBudgetName = updateBudgetCmd.externalBudgetName();
        command.budgetAtr = Number(updateBudgetCmd.budgetAtr());
        command.unitAtr = updateBudgetCmd.unitAtr();
        return nts.uk.request.ajax(paths.updateExternalBudget, command);
    }

    //Insert 
    export function insertExternalBudget(insertBudgetCmd: any) {
        let command = {} as model.ExternalBudgetDto;
        command.externalBudgetCode = insertBudgetCmd.externalBudgetCode();
        command.externalBudgetName = insertBudgetCmd.externalBudgetName();
        command.budgetAtr = Number(insertBudgetCmd.budgetAtr());
        command.unitAtr = insertBudgetCmd.unitAtr();
        return nts.uk.request.ajax(paths.insertExternalBudget, command);
    }
    
    //Delete
    export function deleteExternalBudget(deleteBudgetCmd: any) {
        let command = {} as model.ExternalBudgetDto;
        command.externalBudgetCode = deleteBudgetCmd.externalBudgetCode();
        command.externalBudgetName = deleteBudgetCmd.externalBudgetName();
        command.budgetAtr = Number(deleteBudgetCmd.budgetAtr());
        command.unitAtr = deleteBudgetCmd.unitAtr();
        return nts.uk.request.ajax(paths.deleteExternalBudget, command);
    }

    export module model {
        export class ExternalBudgetDto {
            externalBudgetCode: string;
            externalBudgetName: string;
            budgetAtr: number;
            unitAtr: number;
        }
    }
}