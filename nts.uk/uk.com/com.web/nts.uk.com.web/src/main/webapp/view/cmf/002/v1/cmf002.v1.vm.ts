module nts.uk.com.view.cmf002.v1.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import model = cmf002.share.model;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        listCategoryItem: KnockoutObservableArray<Category> = ko.observableArray([]);
        selectedCategoryCode: KnockoutObservable<Category> = ko.observable();
        currentCode: KnockoutObservable<string> = ko.observable('');
        constructor() {
            var self = this;
            
            let category = getShared("CMF002_T_PARAMS");
            if (category.categoryId !== '') {
                    self.currentCode(category.categoryId);
                }
            self.currentCode.subscribe((cate) => {
                getCategory = _.find(self.listCategoryItem(), function (x) { return x.categoryId == cate; });
                self.selectedCategoryCode(getCategory);
            });
            
        }
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            block.invisible();

            self.roleAuthority = getShared("CMF002B_PARAMS");
            service.getCategory(self.roleAuthority).done((data: Array<Category>) => {
                if (data && data.length) {
                    sortCategory = _.sortBy(data, ['categoryId']);
                    sortCategory.forEach(x => self.listCategoryItem.push(x));
                    if (self.currentCode() == '') {
                        self.currentCode(self.listCategoryItem()[0].categoryId);
                    }
                }
                dfd.resolve();
            }).fail(err => {
                alertError(err);
                dfd.reject();
            }).always(function() {
                nts.uk.ui.block.clear();
            });
           
            return dfd.promise();
        }
        selectCategoryItem() {
            let self = this;
            setShared('CMF002_B_PARAMS', {
                categoryName: self.selectedCategoryCode().categoryName,
                categoryId: self.selectedCategoryCode().categoryId
            });
            nts.uk.ui.windows.close();
        }
        cancelSelectCategoryItem() {
            nts.uk.ui.windows.close();
         }

        getCategoryName(cateId){
            let self = this;
            for (let i = 0 ; i < self.listCategoryItem().length ; i++) {
                if ( cateId == self.listCategoryItem()[i].categoryId){
                    return self.listCategoryItem()[i];
                }
            }
        }
 }
 export class Category {
     categoryId: number;
     officeHelperSysAtr: number;
     categoryName: string;
     categorySet: number;
     personSysAtr: number;
     attendanceSysAtr: number;
     payrollSysAtr: number;
     functionNo: number;
     functionName: string;
     explanation: string;
     displayOrder: number;
     defaultValue: boolean;
     constructor(categoryId: number, officeHelperSysAtr: number, categoryName: string,
         categorySet: number, personSysAtr: number, attendanceSysAtr: number,
         payrollSysAtr: number, functionNo: number, functionName: string,
         explanation: string, displayOrder: number, defaultValue: boolean) {
         this.categoryId = categoryId;
         this.officeHelperSysAtr = officeHelperSysAtr;
         this.categoryName = categoryName;
         this.categorySet = categorySet;
         this.personSysAtr = personSysAtr;
         this.attendanceSysAtr = attendanceSysAtr;
         this.payrollSysAtr = payrollSysAtr;
         this.functionNo = functionNo;
         this.functionName = functionName;
         this.explanation = explanation;
         this.displayOrder = displayOrder;
         this.defaultValue = defaultValue;
     }

    }
}