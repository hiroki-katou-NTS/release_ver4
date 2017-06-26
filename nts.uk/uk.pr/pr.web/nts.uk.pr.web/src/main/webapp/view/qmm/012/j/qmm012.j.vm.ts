module qmm012.j.viewmodel {
    export class ScreenModel {
        //gridlist
        dataSource: KnockoutObservableArray<qmm012.b.service.model.ItemMaster> = ko.observableArray([]);
        columns: KnockoutObservableArray<any>;
        updateSource: KnockoutObservableArray<qmm012.b.service.model.ItemMaster> = ko.observableArray([]);
        currentGroupCode: KnockoutObservable<number> = ko.observable(0);
        oldGroupCode: KnockoutObservable<number> = ko.observable(0);
        columnSettings: KnockoutObservableArray<any>;
        dirty: nts.uk.ui.DirtyChecker;
        constructor() {
            let self = this;
            //gridlist
            self.columns = ko.observableArray([
                { headerText: "コード", key: "itemCode", dataType: "string", width: "40px" },
                { headerText: "名称", key: "itemName", dataType: "string", width: "180px" },
                { headerText: "略名", key: "itemAbName", dataType: "string", width: "160px" },
                { headerText: "英語", key: "itemAbNameE", dataType: "string", width: "140px" },
                { headerText: "略名多言語", key: "itemAbNameO", dataType: "string", width: "160px" }
            ]);
            self.columnSettings = ko.observableArray([
                { columnKey: "itemCode", editorOptions: { type: "numeric", disabled: true } },
                { columnKey: "itemName", editorOptions: { type: "string", disabled: true } },
                { columnKey: "itemAbName", editorOptions: { type: "string", disabled: true } },
                { columnKey: "itemAbNameE", validation: true },
                { columnKey: "itemAbNameO", validation: true }
            ]);
            self.currentGroupCode.subscribe(function(newValue) {
                $("#J_Lst_ItemList").igGridUpdating("endEdit", true, true);
                if (newValue != self.oldGroupCode()) {
                    self.activeDirty(
                        function() {
                            self.reLoadGridData();
                            $(".title").text(self.genTitleText(newValue));
                        },
                        function() {
                            self.reLoadGridData();
                            $(".title").text(self.genTitleText(newValue));
                        },
                        function() {
                            self.currentGroupCode(self.oldGroupCode());
                            $("#sidebar").ntsSideBar("active", self.oldGroupCode());
                        });
                }
            });
            self.LoadGridData();
        }

        genTitleText(GroupCode) {
            let result = ""
            switch (GroupCode) {
                case 0:
                    result = "支給項目";
                    break;
                case 1:
                    result = "控除項目";
                    break;
                case 2:
                    result = "勤怠項目";
                    break;
                case 3:
                    result = "記事項目";
                    break;
                case 9:
                    result = "その他";
                    break;
            }
            return result
        }

        reLoadGridData() {
            let self = this;
            service.findAllItemMasterByCategory(self.currentGroupCode()).done(function(MasterItems: Array<qmm012.b.service.model.ItemMaster>) {
                self.dataSource(MasterItems);
                $("#J_Lst_ItemList").igGrid("option", "dataSource", self.dataSource());
                self.dirty = new nts.uk.ui.DirtyChecker(self.dataSource);
                self.oldGroupCode(self.currentGroupCode());
            })
        }

        LoadGridData() {
            let self = this;
            service.findAllItemMasterByCategory(self.currentGroupCode()).done(function(MasterItems: Array<qmm012.b.service.model.ItemMaster>) {
                self.dataSource(MasterItems);
                self.dirty = new nts.uk.ui.DirtyChecker(self.dataSource);
                self.BindGrid();
                self.oldGroupCode(self.currentGroupCode());
            })
        }

        ChangeGroup(GroupCode) {
            let self = this;
            self.currentGroupCode(GroupCode);
        }

        BindGrid() {
            let self = this;
            $("#J_Lst_ItemList").igGrid({
                primaryKey: "itemCode",
                columns: self.columns(),
                dataSource: self.dataSource(),
                width: "760px",
                height: "500px",
                autoCommit: true,
                features: [
                    {
                        name: "Updating",
                        editCellEnding: self.saveData.bind(self),
                        enableAddRow: false,
                        editMode: "cell",
                        enableDeleteRow: false,
                        cancelTooltip: "Click to cancel",
                        columnSettings: self.columnSettings(),
                        editCellStarting: self.edited 
                    }]
            });
        }
        
        edited(evt, ui){ 
            window.setTimeout(function(){ 
                $(".ui-iggrid-table").parent().scrollLeft(0);          
            }, 0);
//                $(".ui-iggrid-table").parent().scrollLeft(0);        
//            }, 300);
        }

        saveData(evt, ui) {
            let self = this;
            if (ui.columnKey == "itemAbNameE" || ui.columnKey == "itemAbNameO") {
                let item = _.find(self.dataSource(), function(ItemModel: qmm012.b.service.model.ItemMaster) {
                    return ItemModel.itemCode == ui.rowID;
                });
                if (item) {
                    if (self.validate(ui.value)) {
                        let itemUpdate = _.find(self.updateSource(), function(ItemModel: qmm012.b.service.model.ItemMaster) {
                            return ItemModel.itemCode == ui.rowID;
                        });
                        if (itemUpdate) {
                            let index = self.updateSource().indexOf(itemUpdate);
                            itemUpdate[ui.columnKey] = ui.value;
                        } else {
                            item[ui.columnKey] = ui.value;
                            self.updateSource().push(item);
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        validate(value) {
            let result = true;
            if (value != "") {
                var n = 0;
                $('#J_Lst_ItemList').ntsError('clear');
                $('.ui-igedit').removeClass("errorValidate");
                for (let char of value) {
                    let p = value.charCodeAt(value.indexOf(char));
                    if (p < 128) {
                        n++;
                    } else
                        n += 2;
                }
                if (n > 12) {
                    $('#J_Lst_ItemList').ntsError('set', 'Max length for this input is 12');
                    $('.ui-igedit').addClass("errorValidate");
                    result = false;
                }
            }
            return result;
        }

        updateData() {
            let self = this;
            $("#J_Lst_ItemList").igGridUpdating("endEdit", true, true);
            if (self.updateSource().length) {
                service.updateNameItemMaster(self.updateSource()).done(function(res: any) {
                    //after update, need clear array
                    self.updateSource([]);
                    self.reLoadGridData();
                }).fail(function(res: any) {
                    nts.uk.ui.dialog.alert(res);
                });
            }
        }
        activeDirty(MainFunction, YesFunction?, NoFunction?) {
            let self = this;
            if (self.dirty ? !self.dirty.isDirty() : true) {
                MainFunction();
            } else {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。  ").ifYes(function() {
                    if (YesFunction)
                        YesFunction();
                }).ifNo(function() {
                    if (NoFunction)
                        NoFunction();
                })
            }
        }
        CloseDialog() {
            let self = this;
            $("#J_Lst_ItemList").igGridUpdating("endEdit", true, true);
            self.activeDirty(function() { nts.uk.ui.windows.close(); }, function() { nts.uk.ui.windows.close(); });
        }
    }
}