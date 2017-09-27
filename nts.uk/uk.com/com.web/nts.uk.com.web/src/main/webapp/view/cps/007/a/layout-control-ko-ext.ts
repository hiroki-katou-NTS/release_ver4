module nts.custombinding {

    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    import random = nts.uk.util.randomId;
    import text = nts.uk.resource.getText;
    import info = nts.uk.ui.dialog.info;
    import alert = nts.uk.ui.dialog.alert;
    import confirm = nts.uk.ui.dialog.confirm;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    //import writeConstraint = nts.uk.request.writeDynamicConstraint;

    export class LetControl implements KnockoutBindingHandler {
        init = (element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            // Make a modified binding context, with extra properties, and apply it to descendant elements
            ko.applyBindingsToDescendants(bindingContext.extend(valueAccessor), element);

            return { controlsDescendantBindings: true };
        }
    }

    export class LayoutControl implements KnockoutBindingHandler {
        private style = `<style type="text/css" rel="stylesheet" id="layout_style">
                    .layout-control.editable{
                        width: 1245px;
                    }
                    .layout-control .left-area,
                    .layout-control .right-area,
                    .layout-control .add-buttons,
                    .layout-control .drag-panel {
                        float: left;
                    }

                    .layout-control .left-area {
                        margin-right: 15px;
                    }

                    .layout-control .form-group {
                        margin-bottom: 5px;
                    }

                    .layout-control .control-group {
                        margin-top: 10px;
                        padding-left: 10px;
                    }

                    .layout-control #cps007_btn_add {
                        width: 140px;
                    }

                    .layout-control #cps007_cbx_control {
                        min-width: 248px;
                    }

                    .layout-control .ntsControl.radio-control {
                        width: 100%;
                        padding-bottom: 3px;
                    }

                    .layout-control .ntsControl.radio-control .ntsRadioBox {
                        width: 50%;
                        padding-right: 5px;
                        box-sizing: border-box;
                    }

                    .layout-control .ntsControl.search-control .nts-editor {
                        width: 178px !important;
                    }

                    .layout-control .ui-iggrid-scrolldiv {
                        background-color: #fff;
                    }

                    .layout-control .add-buttons {
                        margin-right: 15px;
                        padding-top: 220px;
                    }

                    .layout-control .drag-panel {
                        border: 1px solid #ccc;
                        border-radius: 10px;
                        width: 810px;
                        height: 615px;
                        padding: 10px;
                        box-sizing: border-box;
                    }

                    .layout-control div.ui-sortable {
                        overflow-x: hidden;
                        overflow-y: scroll;
                        padding-right: 10px;
                        box-sizing: border-box;
                    }

                    .layout-control.readonly div.ui-sortable {
                        height: 100%;
                    }

                    .layout-control.editable div.ui-sortable {
                        max-height: 94%;
                        margin-bottom: 3px;
                    }

                    .layout-control .item-classification {
                        padding: 3px;
                        position: relative;
                        box-sizing: border-box;
                        background-color: #fff;
                        border: 1px dashed transparent;
                    }

                    .layout-control .item-classification div.item-control>*,
                    .layout-control .item-classification div.item-controls>* {
                        overflow: hidden;
                        display: inline-block;
                        vertical-align: middle;
                    }

                    .layout-control .item-classification div.item-controls>* {
                        vertical-align: top;
                    }

                    .layout-control .item-classification div.set-item-list,
                    .layout-control .item-classification div.multiple-items {
                        margin-left: 5px;
                    }

                    .layout-control .item-classification div.set-item-list div.set-item {
                        display: inline-block;
                    }

                    .layout-control .item-classification div.item-controls table {
                        max-width: 600px;
                    }

                    .layout-control .item-classification div.item-controls table,
                    .layout-control .item-classification div.item-controls table th,
                    .layout-control .item-classification div.item-controls table td {
                        border: 1px solid #ccc;
                    }

                    .layout-control .item-classification div.item-controls table th {
                        padding: 3px;
                        line-height: 24px;
                        background-color: #E0F59E;
                    }

                    .layout-control .item-classification div.item-controls table td {
                        line-height: 24px;
                    }

                    .layout-control .item-classification div.item-controls table .nts-editor.nts-input{
                        max-width: 73px !important;
                    }

                    .layout-control .item-classification div.item-sperator>hr {
                        padding: 0;
                        margin: 6px 0;
                        margin-right: 20px;
                    }

                    .layout-control .item-classification.ui-sortable-helper {
                        cursor: pointer;
                    }

                    .layout-control .item-classification.ui-sortable-placeholder {
                        border: 1px dashed #ddd;
                        visibility: visible !important;
                    }

                    .layout-control.editable .item-classification:hover,
                    .layout-control.editable .item-classification.selected {
                        background-color: #eee;
                        border: 1px dashed #aaa;
                    }

                    .layout-control .item-classification textarea.nts-editor {
                        width: 280px;
                        height: 70px;
                    }

                    .layout-control .item-classification .form-label {
                        width: 125px;
                        line-height: 37px;
                        white-space: nowrap;
                    }

                    .layout-control .item-classification>.close-btn {
                        top: 0;
                        right: 5px;
                        display: none;
                        cursor: pointer;
                        position: absolute;
                    }

                    .layout-control .item-classification>.close-btn:hover {
                        color: #f00;
                    }

                    .layout-control.editable .item-classification:hover>.close-btn {
                        display: block;
                    }
                </style>`;

        private tmp = `<div class="left-area">
                    <div id="cps007_lbl_control"></div>
                    <div class="control-group">
                        <div class="form-group">
                            <div id="cps007_rdg_control" class="radio-control ntsControl"></div>
                        </div>
                        <div class="form-group">
                            <div id="cps007_cbx_control" class="combobox-control ntsControl"></div>
                        </div>
                        <div class="form-group">
                            <div id="cps007_sch_control" class="search-control ntsControl"></div>
                        </div>
                        <div class="form-group">
                            <div id="cps007_lst_control" class="listbox-control ntsControl"></div>
                        </div>
                    </div>
                </div>
                <div class="right-area cf">
                    <div class="add-buttons">
                        <button id="cps007_btn_add"></button>
                    </div>
                    <div class="drag-panel">
                        <div id="cps007_srt_control">
                            <div class="form-group item-classification">
                               <div data-bind="if: $data.layoutItemType == 0">
                                    <div data-bind="let: { item: $data.listItemDf[0] || {}, listItemDf: $data.listItemDf}" class="item-control">
                                        <div data-bind="ntsFormLabel: { /*constraint: item.itemCode,*/ required: !!_.find(listItemDf, function(x) { return !!x.isRequired; }) }, text: className || '#NA'"></div>
                                        <div data-bind="if: (item.itemTypeState || {}).itemType == 1" class="set-items">
                                            <div data-bind="foreach: _.filter(listItemDf, function(x, i) { return i != 0; })" class="set-item-list">
                                                <div data-bind="template: { 
                                                        name: 'itemtemplate', 
                                                        data: { 
                                                            itemCode: $data.itemCode, 
                                                            itemName: $data.itemName, 
                                                            info: $data.itemTypeState.dataTypeState
                                                        } 
                                                    }" class="set-item"></div>
                                            </div>            
                                        </div>
                                        <div data-bind="if: (item.itemTypeState || {}).itemType == 2" class="single-items">
                                            <div class="single-item-list">
                                                <div data-bind="template: { name: 'itemtemplate', data: { itemCode: item.itemCode, itemName: item.itemName, info: item.itemTypeState.dataTypeState } }" class="single-item"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div data-bind="if: $data.layoutItemType == 1" class="item-controls">
                                    <div data-bind="ntsFormLabel: { /*constraint: item.itemCode,*/ required: !!_.find($data.listItemDf, function(x) { return !!x.isRequired; }) }, text: className || '#NA'"></div>
                                    <div data-bind="let: { items: listItemDf }" class="multiple-items">
                                        <table>
                                            <thead>
                                                <tr data-bind="foreach: items">
                                                    <th data-bind="text: itemName"></th>
                                                </tr>
                                            </thead>
                                            <tbody data-bind="foreach:  { data: listItemDfValues, as: 'row' }">
                                                <tr data-bind="foreach: { data: row, as: 'column' }">
                                                    <td data-bind="template: { 
                                                            name: 'itemtemplate', 
                                                            data: {
                                                                itemCode: column.item.itemCode,
                                                                itemName: column.item.itemName, 
                                                                info: column.item.itemTypeState.dataTypeState,
                                                                value: column.item.value,
                                                            }
                                                        }, attr: {row: column.row, column: col }">
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div data-bind="if: $data.layoutItemType == 2" class="item-sperator">
                                    <hr />
                                </div>
                                <span class="close-btn" data-bind="click: function(item, event) { ko.bindingHandlers['ntsLayoutControl'].remove(item, event); }">✖</span>
                            </div>
                        </div>
                        <button id="cps007_btn_line"></button>
                    </div>
                </div>
                <script type="text/html" id="itemtemplate">
                    <div data-bind="if: $data.info.dataTypeValue == 1" class="string">
                        <div data-bind="if: $data.info.stringItemLength < 40">
                            <input data-bind="ntsTextEditor: {
                                    value: $data.info.value,
                                    /*constraint: $data.itemCode,*/
                                    required: false, 
                                    option: {
                                        textmode: 'text',
                                        placeholder: $data.itemName
                                    },
                                    enable: true,
                                    readonly: $data.info.readonly,
                                    immediate: false
                                }, 
                                attr: { title: $data.itemName }" />
                        </div>
                        <div data-bind="if: $data.info.stringItemLength >= 40">
                            <textarea data-bind="ntsMultilineEditor: {
                                value: $data.info.value,
                                /*constraint: $data.itemCode,*/
                                option: {
                                    textmode: 'text',
                                    placeholder: $data.itemName
                                },
                                enable: true,
                                readonly: $data.info.readonly,
                                immediate: false}" />
                        </div>
                    </div>
                    <div data-bind="if: $data.info.dataTypeValue == 2" class="numeric">
                        <input data-bind="ntsNumberEditor: { 
                                    value: $data.info.value,
                                    /*constraint: $data.itemCode,*/
                                    enable: true,
                                    readonly: $data.info.readonly }, attr: { title: $data.itemName }" />
                    </div>
                    <div data-bind="if: $data.info.dataTypeValue == 3" class="date">
                        <div data-bind="ntsDatePicker: {
                                    value: $data.info.value,
                                    /*constraint: $data.itemCode,*/
                                    dateFormat: 'YYYY/MM/DD',
                                    enable: $data.info.editable,
                                    readonly: $data.info.readonly
                                }, attr: { title: $data.itemName }"></div>
                    </div>
                    <div data-bind="if: $data.info.dataTypeValue == 4" class="time">
                        <input data-bind="ntsTimeEditor: {
                            value: $data.info.value,
                            /*constraint: $data.itemCode,*/
                            inputFormat: 'HH:mm',
                            enable: true,
                            readonly: $data.info.readonly }, attr: { placeholder: $data.itemName }" />
                    </div>
                    <div data-bind="if: $data.info.dataTypeValue == 5" class="timepoint">
                        <input data-bind="ntsTimeEditor: {
                            value: $data.info.value, 
                            /*constraint: $data.itemCode,*/
                            inputFormat: 'HH:mm',
                            enable: true,
                            readonly:  $data.info.readonly
                        }, attr: { placeholder: $data.itemName }" />
                    </div>
                    <div data-bind="if: $data.info.dataTypeValue == 6" class="selection">
                        <div id="combo-box" data-bind="ntsComboBox: {
                            options: [],
                            optionsValue: 'code',
                            visibleItemsCount: 5,
                            value: $data.info.value,
                            /*constraint: $data.itemCode,*/
                            optionsText: 'name',
                            editable: $data.info.editable,
                            enable: true,
                        columns: [{ prop: 'name', length: 10 }]}"></div>
                    </div>
                </script>`;

        private api = {
            getCat: 'ctx/bs/person/info/category/find/companyby/{0}',
            getCats: "ctx/bs/person/info/category/findby/company",
            getGroups: 'ctx/bs/person/groupitem/getAll',
            getItemCats: 'ctx/bs/person/info/ctgItem/layout/findby/categoryId/{0}',
            getItemGroups: 'ctx/bs/person/groupitem/getAllItemDf/{0}',
            getItemsById: 'ctx/bs/person/info/ctgItem/layout/findby/itemId/{0}',
            getItemsByIds: 'ctx/bs/person/info/ctgItem/layout/findby/listItemId',
        };

        private services = {
            getCat: (cid) => {
                let self = this,
                    api = self.api;

                return ajax(format(api.getCat, cid));
            },
            getCats: () => {
                let self = this,
                    api = self.api;

                return ajax(api.getCats);
            },
            getGroups: () => {
                let self = this,
                    api = self.api;

                return ajax(api.getGroups);
            },
            getItemByCat: (cid) => {
                let self = this,
                    api = self.api;

                return ajax(format(api.getItemCats, cid));
            },
            getItemByGroup: (gid) => {
                let self = this,
                    api = self.api;

                return ajax(format(api.getItemGroups, gid));
            },
            getItemsById: (id: string) => {
                let self = this,
                    api = self.api;

                return ajax(format(api.getItemsById, id));
            },
            getItemsByIds: (ids: Array<any>) => {
                let self = this,
                    api = self.api;

                return ajax(api.getItemsByIds, ids);
            }
        };

        remove = (item, sender) => {
            let target = $(sender.target),
                layout = target.parents('.layout-control'),
                opts = layout.data('options');

            opts.sortable.removeItem(item, false);
        };

        private _constructor = (element?: HTMLElement, valueAccessor?: any) => {
            let self = this,
                services = self.services,
                $element = $(element),
                opts = {
                    callback: () => {
                    },
                    radios: {
                        enable: ko.observable(true),
                        value: ko.observable(0),
                        options: ko.observableArray([{
                            id: CAT_OR_GROUP.CATEGORY,
                            name: text('CPS007_6'),
                            enable: ko.observable(true)
                        }, {
                                id: CAT_OR_GROUP.GROUP,
                                name: text('CPS007_7'),
                                enable: ko.observable(true)
                            }]),
                        optionsValue: 'id',
                        optionsText: 'name'
                    },
                    comboxbox: {
                        enable: ko.observable(true),
                        editable: ko.observable(false),
                        visibleItemsCount: 10,
                        value: ko.observable(''),
                        options: ko.observableArray([]),
                        optionsValue: 'id',
                        optionsText: 'categoryName',
                        columns: [{ prop: 'categoryName', length: 15 }]
                    },
                    searchbox: {
                        mode: 'listbox',
                        comId: 'cps007_lst_control',
                        items: ko.observableArray([]),
                        selected: ko.observableArray([]),
                        targetKey: 'id',
                        selectedKey: 'id',
                        fields: ['itemName']
                    },
                    listbox: {
                        enable: ko.observable(true),
                        multiple: ko.observable(true),
                        rows: 15,
                        options: ko.observableArray([]),
                        value: ko.observableArray([]),
                        optionsValue: 'id',
                        optionsText: 'itemName',
                        columns: [{ key: 'itemName', headerText: text('CPS007_9'), length: 15 }]
                    },
                    sortable: {
                        data: ko.observableArray([]),
                        isEnabled: ko.observable(true),
                        isEditable: ko.observable(0),
                        beforeMove: (data, evt, ui) => {
                            let sindex: number = data.sourceIndex,
                                tindex: number = data.targetIndex,
                                direct: boolean = sindex > tindex,
                                item: IItemClassification = data.item,
                                source: Array<IItemClassification> = ko.unwrap(opts.sortable.data);


                            // cancel drop if two line is sibling
                            if (item.layoutItemType == IT_CLA_TYPE.SPER) {
                                let front = source[tindex - 1] || { layoutID: '-1', layoutItemType: -1 },
                                    replc = source[tindex] || { layoutID: '-1', layoutItemType: -1 },
                                    next = source[tindex + 1] || { layoutID: '-1', layoutItemType: -1 };

                                if (!direct) { // drag from top to below
                                    if ([next.layoutItemType, replc.layoutItemType].indexOf(IT_CLA_TYPE.SPER) > -1) {
                                        data.cancelDrop = true;
                                    }
                                } else {  // drag from below to top
                                    if ([replc.layoutItemType, front.layoutItemType].indexOf(IT_CLA_TYPE.SPER) > -1) {
                                        data.cancelDrop = true;
                                    }
                                }
                            } else { // if item is list or object
                                let front = source[sindex - 1] || { layoutID: '-1', layoutItemType: -1 },
                                    next = source[sindex + 1] || { layoutID: '-1', layoutItemType: -1 };

                                if (front.layoutItemType == IT_CLA_TYPE.SPER && next.layoutItemType == IT_CLA_TYPE.SPER) {
                                    data.cancelDrop = true;
                                }
                            }
                        },
                        afterMove: (data, evt, ui) => {
                            /*let self = this,
                                opts = self.options,
                                source: Array<any> = ko.unwrap(opts.sortable.data),
                                maps: Array<number> = _(source).map((x, i) => (x.typeId == IT_CLA_TYPE.SPER) ? i : -1)
                                    .filter(x => x != -1).value();
        
                            // remove next line if two line is sibling
                            _.each(maps, (x, i) => {
                                if (maps[i + 1] == x + 1) {
                                    opts.sortable.data.remove(m => {
                                        let item = ko.unwrap(opts.sortable.data)[maps[i + 1]];
                                        return item.typeId == IT_CLA_TYPE.SPER && item.id == m.id;
                                    });
                                }
                            });*/
                        },
                        removeItem: (data: IItemClassification, byItemId?: boolean) => {
                            let items = opts.sortable.data;

                            if (!byItemId) { // remove item by classification id (virtual id)
                                items.remove((x: IItemClassification) => x.layoutID == data.layoutID);
                            } else if (data.listItemDf) { // remove item by item definition id
                                items.remove((x: IItemClassification) => x.listItemDf && x.listItemDf[0].id == data.listItemDf[0].id);
                            }

                            let source: Array<any> = ko.unwrap(items),
                                maps: Array<number> = _(source).map((x: IItemClassification, i) => (x.layoutItemType == IT_CLA_TYPE.SPER) ? i : -1)
                                    .filter(x => x != -1)
                                    .orderBy(x => x).value()

                            // remove next line if two line is sibling
                            _.each(maps, (x, i) => {
                                if (maps[i + 1] == x + 1) {
                                    items.remove((m: IItemClassification) => {
                                        let item: IItemClassification = ko.unwrap(items)[maps[i + 1]];
                                        return item && item.layoutItemType == IT_CLA_TYPE.SPER && item.layoutID == m.layoutID;
                                    });
                                }
                            });

                            return opts.sortable;
                        },
                        findExist: (ids: Array<string>) => {
                            let items = opts.sortable.data();

                            if (!ids || !ids.length) {
                                return [];
                            }

                            // return items if it's exist in list
                            return _(items)
                                .map((x: IItemClassification) => x.listItemDf)
                                .flatten()
                                .filter((x: IItemDefinition) => x && ids.indexOf(x.id) > -1)
                                .value();
                        },
                        pushItem: (data: IItemClassification) => {
                            let items: KnockoutObservableArray<IItemClassification> = opts.sortable.data;

                            switch (data.layoutItemType) {
                                case IT_CLA_TYPE.ITEM:
                                    let item = _.find(ko.unwrap(items), (x: IItemClassification) => x.layoutItemType == IT_CLA_TYPE.ITEM && x.listItemDf[0].id == data.listItemDf[0].id);
                                    if (!item) {
                                        items.push(data);
                                        return true;
                                    } else {
                                        return false;
                                    }
                                case IT_CLA_TYPE.LIST:
                                    items.push(data);
                                    return true;
                                case IT_CLA_TYPE.SPER:
                                    // add line to list sortable
                                    let last: any = _.last(ko.unwrap(items));

                                    if (last && last.layoutItemType != IT_CLA_TYPE.SPER) {
                                        items.push(data);
                                        return true;
                                    } else {
                                        return false;
                                    }
                            }
                        },
                        pushItems: (defs: Array<IItemDefinition>, groupMode?: boolean) => {
                            let self = this,
                                services = self.services,
                                removeItems = (data: Array<IItemClassification>) => {
                                    if (data && data.length) {
                                        _.each(data, x => opts.sortable.removeItem(x, true));
                                    }
                                },
                                pushItems = (defs: Array<IItemDefinition>) => {
                                    _(defs)
                                        .filter(x => !x.isAbolition) // remove all item if it's abolition
                                        .each(def => {
                                            let item: IItemClassification = {
                                                layoutID: random(),
                                                dispOrder: -1,
                                                personInfoCategoryID: undefined,
                                                layoutItemType: IT_CLA_TYPE.ITEM,
                                                listItemDf: []
                                            };

                                            def.dispOrder = -1;
                                            item.listItemDf = [def];
                                            item.className = def.itemName;
                                            item.personInfoCategoryID = def.perInfoCtgId;

                                            // setitem
                                            if (def.itemTypeState.itemType == ITEM_TYPE.SET) {
                                                services.getItemsByIds(def.itemTypeState.items).done((defs: Array<IItemDefinition>) => {
                                                    if (defs && defs.length) {
                                                        _(defs).filter(x => !x.isAbolition).orderBy(x => x.dispOrder).each((x, i) => { x.dispOrder = i + 1; item.listItemDf.push(x) });

                                                        opts.sortable.pushItem(item);
                                                    }
                                                });
                                            } else {
                                                opts.sortable.pushItem(item);
                                            }
                                        });
                                };

                            if (!defs || !defs.length) {
                                return;
                            }

                            // remove all item if it's cancelled by user
                            defs = _.filter(defs, x => !x.isAbolition);

                            // find duplicate items
                            let dups = opts.sortable.findExist(defs.map(x => x.id));

                            if (groupMode) {
                                if (dups && dups.length) {
                                    // 情報メッセージ（#Msg_204#,既に配置されている項目名,選択したグループ名）を表示する
                                    // Show Msg_204 if itemdefinition is exist
                                    info(dups.map((x: IItemDefinition) => x.itemName).join(', ') + ' ' + text('Msg_204'))
                                        .then(() => {
                                            removeItems(dups.map((x: IItemDefinition) => {
                                                return {
                                                    layoutID: random(),
                                                    dispOrder: -1,
                                                    personInfoCategoryID: undefined,
                                                    layoutItemType: IT_CLA_TYPE.ITEM,
                                                    listItemDf: [x]
                                                };
                                            }));
                                            pushItems(defs);
                                        });
                                } else {
                                    pushItems(defs);
                                }
                            } else {
                                let dupids = dups.map((x: IItemDefinition) => x.id),
                                    nodups = defs.filter((x: IItemDefinition) => dupids.indexOf(x.id) == -1);

                                if (dupids && dupids.length) {
                                    // 画面項目「選択可能項目一覧」で選択している項目が既に画面に配置されている場合
                                    // When the item selected in the screen item "selectable item list" has already been arranged on the screen
                                    alert(dups.map((x: IItemDefinition) => x.itemName).join(', ') + ' ' + text('Msg_202'));
                                }

                                pushItems(nodups);
                            }

                            // remove all item selected in list box
                            opts.listbox.value.removeAll();
                        }
                    }
                },
                ctrls = {
                    label: undefined,
                    radios: undefined,
                    combobox: undefined,
                    searchbox: undefined,
                    listbox: undefined,
                    button: undefined,
                    sortable: undefined,
                    line: undefined,
                },
                access = valueAccessor(),
                editable = (x: any) => {
                    if (typeof x == 'number') {
                        opts.sortable.isEditable(x);

                        if (x == 1) {
                            opts.sortable.isEnabled(true);
                        } else {
                            opts.sortable.isEnabled(false);
                        }
                    } else {
                        opts.sortable.isEditable(0);

                        if (x) {
                            opts.sortable.isEnabled(true);
                        } else {
                            opts.sortable.isEnabled(false);
                        }
                    }
                };

            $(() => {
                if (!$('#layout_style').length) {
                    $('head').append(self.style);
                }
            });


            $element
                .append(self.tmp)
                .addClass('ntsControl layout-control');

            // bindding callback function to control
            if (access.callback) {
                $.extend(opts, {
                    callback: access.callback
                });
            }

            // validate editAble
            if (ko.unwrap(access.editAble) != undefined) {
                if (typeof access.editAble == 'function') {
                    access.editAble.subscribe(editable);
                    access.editAble.valueHasMutated();
                } else {
                    editable(access.editAble);
                }
            }

            // sortable
            opts.sortable.isEnabled.subscribe(x => {
                if (!x) {
                    $element.find('.left-area, .add-buttons, #cps007_btn_line').hide();
                    $element
                        .addClass('readonly')
                        .removeClass('editable');
                } else {
                    $element
                        .addClass('editable')
                        .removeClass('readonly');

                    $element.find('.left-area, .add-buttons, #cps007_btn_line').show();
                }
            });
            opts.sortable.isEnabled.valueHasMutated();

            // inputable (editable)
            opts.sortable.isEditable.subscribe(x => {
                let data: Array<IItemClassification> = ko.unwrap(opts.sortable.data);
                _.each(data, icl => {
                    _.each(icl.listItemDf, (e: IItemDefinition) => {
                        if (e.itemTypeState && e.itemTypeState.dataTypeState) {
                            let state = e.itemTypeState.dataTypeState;
                            if (x == 2) {
                                if (state.editable && typeof state.editable == 'function') {
                                    state.editable(true);
                                } else {
                                    state.editable = ko.observable(true);
                                }

                                if (state.readonly && typeof state.readonly == 'function') {
                                    state.readonly(false);
                                } else {
                                    state.readonly = ko.observable(false);
                                }
                            } else {
                                if (state.editable && typeof state.editable == 'function') {
                                    state.editable(false);
                                } else {
                                    state.editable = ko.observable(false);
                                }

                                if (state.readonly && typeof state.readonly == 'function') {
                                    state.readonly(true);
                                } else {
                                    state.readonly = ko.observable(true);
                                }
                            }
                        }
                    });
                });
            });
            opts.sortable.isEditable.valueHasMutated();

            // extend option
            $.extend(opts.comboxbox, {
                enable: ko.computed(() => !opts.radios.value())
            });

            $.extend(opts.searchbox, {
                items: ko.computed(opts.listbox.options),
                selected: opts.listbox.value
            });

            // extend data of sortable with valueAccessor data prop
            $.extend(opts.sortable, { data: access.data });
            opts.sortable.data.subscribe((data: Array<IItemClassification>) => {
                // remove all sibling sperators
                let maps: Array<number> = _(data)
                    .map((x, i) => (x.layoutItemType == 2) ? i : -1)
                    .filter(x => x != -1).value(),
                    icls: Array<IItemClassification> = ko.unwrap(opts.sortable.data),
                    idfcs: Array<string> = _(icls)
                        .map(x => x.listItemDf)
                        .flatten()
                        .filter(x => !!x)
                        .map((x: IItemDefinition) => x.id)
                        .uniq()
                        .value();

                // write constraint to viewContext
                if (idfcs && idfcs.length) {
                    //writeConstraint(idfcs);
                }

                _.each(maps, (t, i) => {
                    if (maps[i + 1] == t + 1) {
                        _.remove(data, (m: IItemClassification) => {
                            let item: IItemClassification = data[maps[i + 1]];
                            return item && item.layoutItemType == 2 && item.layoutID == m.layoutID;
                        });
                    }
                });
                _.each(data, (x, i) => {
                    x.dispOrder = i + 1;
                    x.layoutID = random();

                    // observable value for edit mode
                    _.each(x.listItemDf, e => {
                        if (e.itemTypeState && e.itemTypeState.dataTypeState) {
                            if (!e.itemTypeState.dataTypeState.value) {
                                e.itemTypeState.dataTypeState.value = ko.observable('');
                            }
                        }
                    });

                    switch (x.layoutItemType) {
                        case IT_CLA_TYPE.ITEM:
                            let item = x.listItemDf && x.listItemDf[0];
                            if (item.itemTypeState.itemType == ITEM_TYPE.SINGLE) {
                                x.listItemDfValues = ko.observableArray([{
                                    item: $.extend({}, item),
                                    value: ko.observable(undefined)
                                }]);
                            } else {
                                x.listItemDfValues = ko.observableArray(_.map(Array((x.listItemDf || []).length), (_x, i) => {
                                    return {
                                        col: i,
                                        item: $.extend({}, x.listItemDf[i]),
                                        value: ko.observable(undefined)
                                    };
                                }));
                            }
                            break;
                        case IT_CLA_TYPE.LIST:
                            x.listItemDfValues = ko.observableArray(
                                _.map(Array(3), (_x, i) => {
                                    return _.map(Array((x.listItemDf || []).length), (__x, j) => {
                                        return {
                                            row: i,
                                            col: j,
                                            item: $.extend({}, x.listItemDf[j]),
                                            value: ko.observable(undefined)
                                        };
                                    });
                                }));
                            break;
                        case IT_CLA_TYPE.SPER:
                            x.listItemDfValues = undefined;
                            break;
                    }
                });

                opts.sortable.isEditable.valueHasMutated();
            });
            opts.sortable.data.valueHasMutated();

            // extend data of sortable with valueAccessor beforeMove prop
            if (access.beforeMove) {
                $.extend(opts.sortable, { beforeMove: access.beforeMove });
            }

            // extend data of sortable with valueAccessor afterMove prop
            if (access.afterMove) {
                $.extend(opts.sortable, { afterMove: access.afterMove });
            }

            // get all id of controls
            $.extend(ctrls, {
                label: $element.find('#cps007_lbl_control')[0],
                radios: $element.find('#cps007_rdg_control')[0],
                combobox: $element.find('#cps007_cbx_control')[0],
                searchbox: $element.find('#cps007_sch_control')[0],
                listbox: $element.find('#cps007_lst_control')[0],
                button: $element.find('#cps007_btn_add')[0],
                sortable: $element.find('#cps007_srt_control')[0],
                line: $element.find('#cps007_btn_line')[0]
            });

            // change text of label
            $(ctrls.label).text(text('CPS007_5'));
            $(ctrls.line).text(text('CPS007_19'));
            $(ctrls.button).text(text('CPS007_11'));


            // subscribe handle
            // load combobox data
            opts.radios.value.subscribe(mode => {
                // remove all data in listbox
                opts.listbox.options.removeAll();

                if (mode == CAT_OR_GROUP.CATEGORY) { // get item by category
                    opts.comboxbox.options.removeAll();
                    services.getCats().done((data: any) => {
                        if (data && data.categoryList && data.categoryList.length) {
                            let cats = _.filter(data.categoryList, (x: IItemCategory) => !x.isAbolition);

                            if (cats && cats.length) {
                                
                                opts.comboxbox.options(cats);
                                if (opts.comboxbox.value() == cats[0].id) {
                                    opts.comboxbox.value.valueHasMutated();
                                } else {
                                    opts.comboxbox.value(cats[0].id);
                                }
                            } else {
                                // show message if hasn't any category
                                if (ko.toJS(opts.sortable.isEnabled)) {
                                    alert(text('Msg_288')).then(opts.callback);
                                }
                            }
                        } else {
                            // show message if hasn't any category
                            if (ko.toJS(opts.sortable.isEnabled)) {
                                alert(text('Msg_288')).then(opts.callback);
                            }
                        }
                    });
                } else { // get item by group
                    // change text in add-button to [グループを追加　→]
                    $(ctrls.button).text(text('CPS007_20'));
                    services.getGroups().done((data: Array<IItemGroup>) => {
                        if (data && data.length) {
                            // map Array<IItemGroup> to Array<IItemDefinition>
                            // 「個人情報項目定義」が取得できなかった「項目グループ」以外を、画面項目「グループ一覧」に表示する
                            // remove groups when it does not contains any item definition (by hql)
                            _.each(data, group => {
                                services.getItemByGroup(group.personInfoItemGroupID).done((data: Array<IItemDefinition>) => {
                                    if (data && data.length) {
                                        opts.listbox.options.push({
                                            id: group.personInfoItemGroupID,
                                            itemName: group.fieldGroupName,
                                            itemTypeState: undefined,
                                            dispOrder: group.dispOrder
                                        });
                                    }
                                });
                            });
                        }
                    });
                }

                // remove listbox data
                opts.listbox.value.removeAll();
            });
            opts.radios.value.valueHasMutated();

            // load listbox data
            opts.comboxbox.value.subscribe(cid => {
                if (cid) {
                    let data: Array<IItemCategory> = ko.toJS(opts.comboxbox.options),
                        item: IItemCategory = _.find(data, x => x.id == cid);

                    // remove all item in list item for init new data
                    opts.listbox.options.removeAll();
                    if (item) {
                        switch (item.categoryType) {
                            case IT_CAT_TYPE.SINGLE:
                            case IT_CAT_TYPE.CONTINU:
                            case IT_CAT_TYPE.NODUPLICATE:
                                $(ctrls.button).text(text('CPS007_11'));
                                services.getItemByCat(item.id).done((data: Array<IItemDefinition>) => {
                                    if (data && data.length) {
                                        // get all item defined in category with abolition = 0
                                        // order by dispOrder asc
                                        data = _(data)
                                            .filter(m => !m.isAbolition)
                                            .orderBy(m => m.dispOrder).value();

                                        opts.listbox.options(data);
                                        opts.listbox.value.removeAll();
                                    }
                                });
                                break;
                            case IT_CAT_TYPE.MULTI:
                            case IT_CAT_TYPE.DUPLICATE:
                                $(ctrls.button).text(text('CPS007_10'));

                                // create item for listbox
                                // itemname: categoryname + text('CPS007_21')
                                let def: IItemDefinition = {
                                    id: item.id,
                                    itemName: item.categoryName + text('CPS007_21'),
                                    itemTypeState: undefined, // item.categoryType
                                };
                                opts.listbox.value.removeAll();
                                opts.listbox.options.push(def);
                                break;
                        }
                    } else {
                        // select undefine
                        opts.listbox.value.removeAll();
                    }
                }
            });

            opts.listbox.options.subscribe(x => {
                if (!x || !x.length) {
                    $(ctrls.button).prop('disabled', true);
                } else {
                    $(ctrls.button).prop('disabled', false);
                }
            });

            // disable group if not has any group
            services.getGroups().done((data: Array<any>) => {
                if (!data || !data.length) {
                    opts.radios.options().filter(x => x.id == CAT_OR_GROUP.GROUP).forEach(x => x.enable(false));
                }
            });

            // events handler
            $(ctrls.line).on('click', function() {
                let item: IItemClassification = {
                    layoutID: random(),
                    dispOrder: -1,
                    personInfoCategoryID: undefined,
                    listItemDf: undefined,
                    layoutItemType: IT_CLA_TYPE.SPER
                };

                // add line to list sortable
                opts.sortable.pushItem(item);
            });

            $(ctrls.button).on('click', () => {
                // アルゴリズム「項目追加処理」を実行する
                // Execute the algorithm "項目追加処理"
                let ids: Array<string> = ko.toJS(opts.listbox.value);
                if (!ids || !ids.length) {
                    alert(text('Msg_203'));
                    return;
                }

                // category mode
                if (ko.unwrap(opts.radios.value) == CAT_OR_GROUP.CATEGORY) {
                    let cid: string = ko.toJS(opts.comboxbox.value),
                        cats: Array<IItemCategory> = ko.toJS(opts.comboxbox.options),
                        cat: IItemCategory = _.find(cats, x => x.id == cid);

                    if (cat) {
                        // multiple items
                        if ([IT_CAT_TYPE.MULTI, IT_CAT_TYPE.DUPLICATE].indexOf(cat.categoryType) > -1) {
                            // 画面項目「カテゴリ選択」で選択している情報が、既に配置されているかチェックする
                            // if category is exist in sortable box.
                            let _catcls = _.find(ko.unwrap(opts.sortable.data), (x: IItemClassification) => x.personInfoCategoryID == cat.id);
                            if (_catcls) {
                                alert(text('Msg_202'));
                                return;
                            }

                            setShared('CPS007B_PARAM', { category: cat, chooseItems: [] });
                            modal('../../007/b/index.xhtml').onClosed(() => {
                                let data = getShared('CPS007B_VALUE') || { category: undefined, chooseItems: [] };

                                if (data.category && data.category.id && data.chooseItems && data.chooseItems.length) {
                                    services.getCat(data.category.id).done((_cat: IItemCategory) => {

                                        if (!_cat || !!_cat.isAbolition) {
                                            return;
                                        }

                                        let ids: Array<string> = data.chooseItems.map(x => x.id);
                                        services.getItemsByIds(ids).done((_data: Array<IItemDefinition>) => {
                                            // sort againt by ids
                                            _.each(_data, x => x.dispOrder = ids.indexOf(x.id) + 1);

                                            _data = _.orderBy(_data, x => x.dispOrder);

                                            let item: IItemClassification = {
                                                layoutID: random(),
                                                dispOrder: -1,
                                                className: _cat.categoryName,
                                                personInfoCategoryID: _cat.id,
                                                layoutItemType: IT_CLA_TYPE.LIST,
                                                listItemDf: _data
                                            };
                                            opts.sortable.data.push(item);
                                            opts.listbox.value.removeAll();
                                        });
                                    });
                                }
                            });
                        }
                        else { // set or single item
                            let idefid: Array<string> = ko.toJS(opts.listbox.value),
                                idefs = _.filter(ko.toJS(opts.listbox.options), (x: IItemDefinition) => idefid.indexOf(x.id) > -1);

                            if (idefs && idefs.length) {
                                services.getItemsByIds(idefs.map(x => x.id)).done((defs: Array<IItemDefinition>) => {
                                    if (defs && defs.length) {
                                        opts.sortable.pushItems(defs, false);
                                    }
                                });
                            }
                        }
                    }
                } else { // group mode
                    let ids: Array<string> = ko.toJS(opts.listbox.value),
                        groups: Array<any> = ko.unwrap(opts.listbox.options),
                        filters: Array<any> = _.filter(groups, x => ids.indexOf(x.id) > -1);

                    if (filters && filters.length) {
                        let dfds: Array<JQueryDeferred<any>> = [];

                        _.each(filters, group => {
                            let dfd = $.Deferred<any>();
                            services.getItemByGroup(group.id).done((data: Array<IItemDefinition>) => {
                                dfd.resolve(data);
                            }).fail(x => dfd.reject(false));

                            dfds.push(dfd);
                        });

                        // push all item to sortable when done
                        $.when.apply($, dfds).then(function() {
                            // remove all item if it's abolition
                            let items = _.filter(_.flatten(arguments) as Array<IItemDefinition>, x => !x.isAbolition);
                            if (items && items.length) {
                                opts.sortable.pushItems(items, true);
                            }
                        });
                    }
                }
            });

            // set data controls and option to element
            $element.data('options', opts);
            $element.data('controls', ctrls);
        }

        init = (element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext) => {

            // call private constructor
            this._constructor(element, valueAccessor);

            let $element = $(element),
                opts = $element.data('options'),
                ctrls = $element.data('controls');

            ko.bindingHandlers['ntsFormLabel'].init(ctrls.label, function() {
                return {};
            }, allBindingsAccessor, viewModel, bindingContext);
            // init radio box group
            ko.bindingHandlers['ntsRadioBoxGroup'].init(ctrls.radios, function() {
                return opts.radios;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsComboBox'].init(ctrls.combobox, function() {
                return opts.comboxbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSearchBox'].init(ctrls.searchbox, function() {
                return opts.searchbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsListBox'].init(ctrls.listbox, function() {
                return opts.listbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSortable'].init(ctrls.sortable, function() {
                return opts.sortable;
            }, allBindingsAccessor, viewModel, bindingContext);

            // Also tell KO *not* to bind the descendants itself, otherwise they will be bound twice
            return { controlsDescendantBindings: true };
        }

        update = (element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            let self = this,
                $element = $(element),
                opts = $element.data('options'),
                ctrls = $element.data('controls');

            ko.bindingHandlers['ntsFormLabel'].update(ctrls.label, function() {
                return {};
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsRadioBoxGroup'].update(ctrls.radios, function() {
                return opts.radios;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsComboBox'].update(ctrls.combobox, function() {
                return opts.comboxbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSearchBox'].update(ctrls.searchbox, function() {
                return opts.searchbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsListBox'].update(ctrls.listbox, function() {
                return opts.listbox;
            }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSortable'].update(ctrls.sortable, function() {
                return opts.sortable;
            }, allBindingsAccessor, viewModel, bindingContext);

            // Also tell KO *not* to bind the descendants itself, otherwise they will be bound twice
            return { controlsDescendantBindings: true };
        }
    }

    interface IItemCategory {
        id: string;
        categoryName: string;
        categoryType: IT_CAT_TYPE;
        isAbolition?: number;
    }

    interface IItemGroup {
        personInfoItemGroupID: string;
        fieldGroupName: string;
        dispOrder: number;
    }

    interface IItemClassification {
        layoutID?: string;
        dispOrder?: number;
        className?: string; // only for display if classification is set or duplication item
        personInfoCategoryID?: string;
        layoutItemType: IT_CLA_TYPE;
        listItemDf: Array<IItemDefinition>; // layoutItemType == 0 ? [1] : layoutItemType == 1 ? [A, B, C] : undefined;
        listItemDfValues?: any; // {value: } || [{c: 1, value: }, {c: 2, value: }], [[{r: 1, c: 1, value: }, {}], [{}, {}], [{}, {}], [{}, {}]] , undefined
    }

    interface IItemDefinition {
        id: string;
        dispOrder?: number;
        perInfoCtgId?: string;
        itemCode?: string;
        itemName: string;
        isAbolition?: number;
        isFixed?: number;
        isRequired?: number;
        systemRequired?: number;
        requireChangable?: number;
        itemTypeState: IItemTypeState;
    }

    interface IItemTypeState extends ISetItem, ISingleItem {
        itemType: ITEM_TYPE; // Set || Single
    }

    interface ISetItem {
        items?: Array<string>; // Set ids value
    }

    interface ISingleItem {
        dataTypeState?: IItemDefinitionData // Single item value
    }

    interface IItemDefinitionData extends IItemTime, IItemDate, IItemString, IItemTimePoint, IItemNumeric {
        value: KnockoutObservable<any>;
        dataTypeValue: ITEM_SINGLE_TYPE; // type of value of item
        editable?: KnockoutObservable<boolean>;
        readonly?: KnockoutObservable<boolean>;
    }

    interface IItemTime {
        min?: number;
        max?: number;
    }

    interface IItemDate {
        dateItemType?: number;
    }

    interface IItemString {
        stringItemDataType?: ITEM_STRING_DTYPE;
        stringItemLength?: number;
        stringItemType?: ITEM_STRING_TYPE;
    }

    interface IItemTimePoint {
        timePointItemMin?: number;
        timePointItemMax?: number;
    }

    interface IItemNumeric {
        numericItemMinus?: number;
        numericItemAmount?: number;
        integerPart?: number;
        decimalPart?: number;
        NumericItemMin?: number;
        NumericItemMax?: number;
    }

    interface IItemSelection extends IItemMasterSelection, IItemEnumSelection, IItemCodeNameSelection {
        referenceType?: number;
    }

    interface IItemMasterSelection {
        masterType?: string;
    }

    interface IItemEnumSelection {
        typeCode?: string;
    }

    interface IItemCodeNameSelection {
        enumName?: string;
    }

    // define ITEM_CLASSIFICATION_TYPE
    enum IT_CLA_TYPE {
        ITEM = 0, // single item
        LIST = 1, // list item
        SPER = 2 // line item
    }

    // define ITEM_CATEGORY_TYPE
    enum IT_CAT_TYPE {
        SINGLE = 1, // Single info
        MULTI = 2, // Multi info
        CONTINU = 3, // Continuos history
        NODUPLICATE = 4, //No duplicate history
        DUPLICATE = 5 // Duplicate history
    }

    // defined CATEGORY or GROUP mode
    enum CAT_OR_GROUP {
        CATEGORY = 0, // category mode
        GROUP = 1 // group mode
    }

    // define ITEM_TYPE is set or single item
    enum ITEM_TYPE {
        SET = 1, // List item info
        SINGLE = 2 // Single item info
    }

    // define ITEM_SINGLE_TYPE
    // type of item if it's single item
    enum ITEM_SINGLE_TYPE {
        STRING = 1,
        NUMERIC = 2,
        DATE = 3,
        TIME = 4,
        TIMEPOINT = 5,
        SELECTION = 6
    }

    // define ITEM_STRING_DATA_TYPE
    enum ITEM_STRING_DTYPE {
        FIXED_LENGTH = 1, // fixed length
        VARIABLE_LENGTH = 2 // variable length
    }

    enum ITEM_STRING_TYPE {
        ANY = 1,
        // 2:全ての半角文字(AnyHalfWidth)
        ANYHALFWIDTH = 2,
        // 3:半角英数字(AlphaNumeric)
        ALPHANUMERIC = 3,
        // 4:半角数字(Numeric)
        NUMERIC = 4,
        // 5:全角カタカナ(Kana)
        KANA = 5
    }

    // define ITEM_SELECT_TYPE
    // type of item if it's selection item
    enum ITEM_SELECT_TYPE {
        // 1:専用マスタ(DesignatedMaster)
        DESIGNATED_MASTER = 1,
        // 2:コード名称(CodeName)
        CODE_NAME = 2,
        // 3:列挙型(Enum)
        ENUM = 3
    }
}

ko.bindingHandlers['let'] = new nts.custombinding.LetControl();
ko.bindingHandlers["ntsLayoutControl"] = new nts.custombinding.LayoutControl();
