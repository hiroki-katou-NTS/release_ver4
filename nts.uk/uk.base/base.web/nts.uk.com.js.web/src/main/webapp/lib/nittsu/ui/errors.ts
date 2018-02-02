/// <reference path="../reference.ts"/>

module nts.uk.ui.errors {
    
    export interface ErrorListItem {
        errorCode: string;
        /** OBSOLATE! using messageText instead */
        messageText?: string;
        message?: any;
        location?: string;
        tab?: string;
        $control?: JQuery;
        businessError?: boolean;
    }
    
    export interface ErrorMessage {
        message: string;
        messageId?: string;
        parameterIds?: string[];
    }
    
    export interface GridCellError {
        grid: JQuery;
        rowId: any;
        columnKey: string;
        message: string;
    }

    export class ErrorsViewModel {
        title: string;
        errors: KnockoutObservableArray<ErrorListItem>;
        gridErrors: KnockoutObservableArray<GridCellError>;
        displayErrors: KnockoutObservableArray<any>;
        option: KnockoutObservable<any>;
        occurs: KnockoutComputed<boolean>;
        allResolved: JQueryCallback;
        allCellsResolved: JQueryCallback;

        constructor(dialogOptions?: any) {
            this.title = "エラー一覧"; 
            this.errors = ko.observableArray([]);
            this.errors.extend({ rateLimit: 1 });
            this.gridErrors = ko.observableArray([]);
            this.displayErrors = !util.isNullOrUndefined(dialogOptions) && dialogOptions.forGrid ? this.gridErrors : this.errors;
            this.option = ko.observable(ko.mapping.fromJS(new option.ErrorDialogOption(dialogOptions)));
            this.occurs = ko.computed(() => this.errors().length !== 0 || this.gridErrors().length !== 0);
            this.allResolved = $.Callbacks();
            this.allCellsResolved = $.Callbacks();

             this.option().show.extend({notify:"always"});
            
            this.errors.subscribe(() => {
                if (this.errors().length === 0) {
                    this.allResolved.fire();
                }
            });

            this.allResolved.add(() => {
                this.hide();
            });
            
            // Grid
            this.gridErrors.subscribe(() => {
                if (this.gridErrors().length === 0) {
                    this.allCellsResolved.fire();
                }
            });
            
            this.allCellsResolved.add(() => {
                this.hide();
            });
        }
        
        closeButtonClicked() {
             this.option().show(false);
        }

        open() {
            this.option().show(true);
        }

        hide() {
            this.option().show(false);
        }

        addError(error: ErrorListItem) {
            var duplicate = _.filter(this.errors(), e => e.$control.is(error.$control) 
                            && (typeof error.message === "string" ? e.messageText === error.message : e.messageText === error.messageText));
            
            if (duplicate.length == 0) {
                if (typeof error.message === "string") {
                    error.messageText = error.message;
                } else {
                    // Business Exception
                    if (error.message.message) {
                        error.messageText = error.message.message;
                        error.errorCode = error.message.messageId != null && error.message.messageId.length > 0 ? error.message.messageId : "";
                    } else {
                        if (error.$control.length > 0) {
                            let controlNameId = error.$control.eq(0).attr("data-name");
                            if (controlNameId) {
                                error.messageText = nts.uk.resource.getMessage(error.message.messageId, nts.uk.resource.getText(controlNameId), error.message.messageParams);
                            } else {
                                error.messageText = nts.uk.resource.getMessage(error.message.messageId, error.message.messageParams);
                            }
                        } else {
                            error.messageText = nts.uk.resource.getMessage(error.message.messageId);

                        }
                        error.errorCode = error.message.messageId;
                    }

                }
                this.errors.push(error);
            }
        }

        hasError() {
            return this.errors().length > 0;
        }

        clearError() {
            $(".error").children().each((index, element) => {
                if ($(element).data("hasError"))
                    $(element).data("hasError", false);
            });
            $(".error").removeClass('error');
            this.errors.removeAll();
        }

        removeErrorByElement($element: JQuery) {
            this.errors.remove((error) => {
                return error.$control.is($element);
            });
        }
        
        removeErrorByCode($element: JQuery, errorCode: string) {
            this.errors.remove((error) => {
                return error.$control.is($element) && error.errorCode === errorCode;
            });
        }
        
        removeKibanError($element: JQuery) {
            this.errors.remove((error) => {
                return error.$control.is($element) && error.businessError == false;
            });
        }
        
        getErrorByElement($element: JQuery): ErrorListItem[] {
            return _.filter(this.errors(), e => { 
                return e.$control.is($element)
            });
        }
        
        // Grid errors
        addCellError(error: GridCellError) {
            let self = this;
            let exists = _.filter(this.gridErrors(), function(err: GridCellError) {
                return self.sameCells(error, err);
            });
            if (exists.length > 0) return;
            this.gridErrors.push(error);
        }
        
        removeCellError($grid: JQuery, rowId: any, columnKey: string) {
            this.gridErrors.remove(function(err: GridCellError) {
                return err.grid.is($grid) && err.rowId === rowId && err.columnKey === columnKey;
            });
        }
        
        gridHasError() {
            return this.gridErrors().length > 0;
        }
        
        sameCells(one: GridCellError, other: GridCellError) {
            if (!one.grid.is(other.grid)) return false;
            if (one.rowId !== other.rowId) return false;
            if (one.columnKey !== other.columnKey) return false;
            return true;
        }
        
        stashMemento(): ErrorViewModelMemento {
            let memento = new ErrorViewModelMemento();
            memento.setErrors(ko.unwrap(this.errors));
            memento.setGridErrors(ko.unwrap(this.gridErrors));
            memento.option = ko.unwrap(this.option);
            memento.allResolved = this.allResolved;
            memento.allCellsResolved = this.allCellsResolved;
            
            memento.setErrorElements();
            
            this.clearError();
            
            return memento;
        }
        
        restoreFrom(memento: ErrorViewModelMemento) {
            this.errors(memento.errors);
            this.gridErrors(memento.gridErrors);
            this.option(memento.option);
            this.allResolved = memento.allResolved;
            this.allCellsResolved = memento.allCellsResolved;
            memento.restoreErrorElements();
        }
    }
    
    export class ErrorViewModelMemento {
        
        errors: ErrorListItem[];
        gridErrors: GridCellError[];
        option: any;
        allResolved: JQueryCallback;
        allCellsResolved: JQueryCallback;
        errorElements: JQuery[];
        
        setErrors(errors: ErrorListItem[]) {
            if (!_.isArray(errors)) {
                return;
            }
            
            this.errors = [];
            errors.forEach(e => {
                this.errors.push(e);
            });
        }
        
        setGridErrors(gridErrors: GridCellError[]) {
            if (!_.isArray(gridErrors)) {
                return;
            }
            
            this.gridErrors = [];
            gridErrors.forEach(e => {
                this.gridErrors.push(e);
            });
        }
        
        setErrorElements() {
            this.errorElements = $(".error").removeClass("error");
        }

        restoreErrorElements() {
            this.errorElements.addClass("error");
        }
    }

    export class ErrorHeader {
        name: string;
        text: string;
        width: any;
        visible: boolean;

        constructor(name: string, text: string, width: any, visible: boolean) {
            this.name = name;
            this.text = text;
            this.width = width;
            this.visible = visible;
        }
    }

    /** 
     *  Public API
    **/
    export function errorsViewModel(): ErrorsViewModel {
        return nts.uk.ui._viewModel.kiban.errorDialogViewModel;
    }

    export function show(): void {
        errorsViewModel().open();
    }

    export function hide(): void {
        errorsViewModel().hide();
    }

    export function add(error: ErrorListItem): void {
        errorsViewModel().addError(error);
    }

    export function hasError(): boolean {
        return errorsViewModel().hasError();
    }

    export function clearAll(): void {
        if (nts.uk.ui._viewModel !== undefined)
            errorsViewModel().clearError();
    }

    export function removeByElement($control: JQuery): void {
        errorsViewModel().removeErrorByElement($control);
    }
    
    export function removeByCode($control: JQuery, errorCode: string): void {
        errorsViewModel().removeErrorByCode($control, errorCode);
    }
    
    export function removeCommonError($control: JQuery): void {
        errorsViewModel().removeKibanError($control);
    }
    
    export function getErrorByElement($element: JQuery): ErrorListItem[] {
        return errorsViewModel().getErrorByElement($element);    
    }
    
    // Grid errors
    export function addCell(error: GridCellError): void {
        errorsViewModel().addCellError(error);
    }
    
    export function removeCell($grid: JQuery, rowId: any, columnKey: string): void {
        errorsViewModel().removeCellError($grid, rowId, columnKey);
    }
    
    export function gridHasError(): boolean {
        return errorsViewModel().gridHasError();
    }
    
    export function getErrorList() {
        return errorsViewModel().displayErrors();
    }
}