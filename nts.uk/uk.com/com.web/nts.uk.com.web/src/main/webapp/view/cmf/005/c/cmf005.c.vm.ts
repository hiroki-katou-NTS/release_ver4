module nts.uk.com.view.cmf005.c.viewmodel {
    import getText = nts.uk.resource.getText;
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import model = cmf005.share.model;
    import alertError = nts.uk.ui.dialog.alertError;

    export class ScreenModel {

        // swapList category
        listCategory: KnockoutObservableArray<model.ItemCategory> = ko.observableArray([]);
        listCategoryChosed: KnockoutObservableArray<model.ItemCategory> = ko.observableArray([]);
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCategorySelected: KnockoutObservableArray<any>;
        listCateIdIgnore: KnockoutObservableArray<string> = ko.observableArray([]);


        // comboBox system type
        systemTypes: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        currentItem: KnockoutObservable<model.ItemModel>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;
        headerCodeCategories: string = getText("CMF005_19");
        headerNameCategories: string = getText("CMF005_20");


        constructor() {
            var self = this;

            // get param from screen B 
            let listCategoryB = getShared('CMF005CParams_ListCategory');
            let systemTypeB = getShared('CMF005CParams_SystemType');


            self.currentCategorySelected = ko.observableArray([]);
            var systemIdSelected;
            self.systemTypes = ko.observableArray([]);
            self.isEnable = ko.observable(true);
            self.isEditable = ko.observable(true);
            self.listCategory = ko.observableArray([]);
            self.selectedCode = ko.observable('');

            service.getSysTypes().done(function(data: Array<any>) {
                if (data && data.length) {
                    _.forOwn(data, function(index) {
                        self.systemTypes.push(new model.ItemModel(index.systemTypeValue, index.systemTypeName));
                    });


                    if (systemTypeB != undefined) {
                        self.selectedCode = ko.observable(systemTypeB.code);
                    } else {
                        self.selectedCode = ko.observable(self.systemTypes()[0].code);
                    }
                    self.getListCategory(systemTypeB, listCategoryB);
                }
            }).fail(function(error) {
                alertError(error);

            }).always(() => {

            });



            if (self.listCategoryChosed().length > 0) {
                _.forEach(self.listCategoryChosed(), function(x) {
                    self.listCateIdIgnore.push(x.id);
                });
            }

            self.selectedCode.subscribe(value => {
                self.currentItem = _.find(self.systemTypes(), a => a.code === value);
                self.getListCategory(systemTypeB, listCategoryB);

            })

            self.columns = ko.observableArray([
                { headerText: self.headerCodeCategories, key: 'categoryId', width: 70 },
                { headerText: self.headerNameCategories, key: 'categoryName', width: 200 }
            ]);

            self.listCategoryChosed = self.currentCategorySelected;
        }

        getListCategory(systemTypeB: KnockoutObservable<model.ItemModel>, listCategoryB: KnockoutObservableArray<model.ItemCategory>) {
            var self = this;
            service.getCategoryListBySystem(self.selectedCode(), self.listCateIdIgnore).done(function(data: Array<any>) {
                if (systemTypeB != undefined) {
                    _.forOwn(listCategoryB, function(index) {
                        _.remove(data, function(e) {
                            return e.categoryId == index.categoryId;
                        });
                    });
                    self.currentCategorySelected(listCategoryB);
                }
                self.listCategory(data);
            }).fail(function(error) {
                alertError(error);
            }).always(() => {
                _.defer(() => {
                    $("#grd_Condition tr:first-child").focus();
                });
            });
        }

        closePopup() {
            close();
        }

        submit() {
            let self = this;
            if (self.currentCategorySelected().length == 0) {
                alertError({ messageId: "Msg_471" });
            } else {
                setShared("CMF005COutput_ListCategoryChose", self.currentCategorySelected());
                setShared("CMF005COutput_SystemTypeChose", self.currentItem);
                close();
            }
        }

    }

}






