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
    import parseTime = nts.uk.time.parseTime;
    import nou = nts.uk.util.isNullOrUndefined;

    let __viewContext: any = window['__viewContext'] || {},
        rmError = window['nts']['uk']['ui']['errors']['removeByCode'],
        getError = window['nts']['uk']['ui']['errors']['getErrorList'],
        clearError = window['nts']['uk']['ui']['errors']['clearAll'],
        writeConstraint = window['nts']['uk']['ui']['validation']['writeConstraint'],
        writeConstraints = window['nts']['uk']['ui']['validation']['writeConstraints'],
        parseTimeWidthDay = window['nts']['uk']['time']['minutesBased']['clock']['dayattr']['create'];

    export class PropControl implements KnockoutBindingHandler {
        init = (element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            let $element = $(element),
                accessor = valueAccessor();

            window.setInterval(() => {
                if (_.has(accessor, "width") && ko.isObservable(accessor.width)) {
                    accessor.width(element.offsetWidth);
                }

                if (_.has(accessor, "height") && ko.isObservable(accessor.height)) {
                    accessor.height(element.offsetHeight);
                }

                if (_.has(accessor, "hasScrollX") && ko.isObservable(accessor.hasScrollX)) {
                    accessor.hasScrollX(element.scrollWidth > element.clientWidth);
                }

                if (_.has(accessor, "hasScrollY") && ko.isObservable(accessor.hasScrollY)) {
                    accessor.hasScrollY(element.scrollHeight > element.clientHeight);
                }

                if (_.has(accessor, "top") && ko.isObservable(accessor.top)) {
                    accessor.top($element.offset().top);
                }

                if (_.has(accessor, "left") && ko.isObservable(accessor.left)) {
                    accessor.left($element.offset().left);
                }

                let _down = ko.toJS(accessor.scrollDown);
                if (!!_down) {
                    let _len = $element.find(_down).length,
                        _olen = $element.data('length');

                    $element.data('length', _len);
                    if (_olen < _len) {
                        $element.scrollTop($element.prop("scrollHeight"));
                    }
                }

                if (_.has(accessor, "maxHeight")) {
                    let m = ko.toJS(accessor.maxHeight),
                        c = m.byChild,
                        l = m.length,
                        r = $element.find(c),
                        h = r.height();

                    if (r.length <= 5) {
                        $element.css('overflow-y', 'hidden');
                    } else {
                        $element.css('overflow-y', 'auto');
                    }

                    if (element.scrollWidth > element.clientWidth) {
                        $element.css('max-height', ((h * l) + 17) + 'px');
                    } else {
                        $element.css('max-height', ((h * l) + 4) + 'px');
                    }
                }
            }, 0);
        }
    }

    export class LayoutControl implements KnockoutBindingHandler {
        private style = `<style type="text/css" rel="stylesheet" id="layout_style">
                    .layout-control.dragable{
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

                    .layout-control.dragable div.ui-sortable {
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

                    .layout-control .item-classification div.item-control>.ntsControl .nts-input,
                    .layout-control .item-classification div.item-control div.childs-row>.ntsControl .nts-input {
                        margin-top: 3px;
                    }

                    .layout-control .item-classification div.item-control>*,
                    .layout-control .item-classification div.item-controls>* {
                        vertical-align: top;
                    }
                    
                    .layout-control .item-classification div.item-control~.item-control {
                        margin-top: 15px;
                    }

                    .layout-control .item-classification div.item-control>.set-items,
                    .layout-control .item-classification div.item-control>.single-items {
                        margin-top: 3px;
                        max-width: 535px;
                    }

                    .layout-control .item-classification div.item-control>.set-items .set-group {
                        min-height: 34px;
                    }

                    .layout-control .item-classification div.item-control>.set-items .set-group>* {
                        vertical-align: top;
                        display: inline-block;
                    }

                    .layout-control .item-classification div.item-control>.set-items .set-group.math-title {
                        line-height: 30px;
                        font-weight: bold;
                    }

                    .layout-control .item-classification div.item-control>.set-items .set-group:not(:first-child) {
                        margin-left: -165px;
                        padding-top: 5px;
                        padding-bottom: 5px;
                    }

                    .layout-control .item-classification div.item-control>.set-items .childs-row .ntsControl .nts-input {
                        width: 85px;
                    }

                    .layout-control .item-classification div.item-control>.set-items .childs-row {
                        width: 530px;
                    }
                    
                    .layout-control .item-classification div.item-control>.set-items .childs-row~.childs-row {
                        display: block;
                        margin-top: 10px;
                        margin-left: 165px;
                    }

                    .layout-control .item-classification div.item-control .child-label {
                        width: 160px;
                        vertical-align: top;
                        line-height: 35px;
                        display: inline-block;
                    }

                    .layout-control .item-classification div.item-control .childs-row .child-label {
                        width: 130px;
                    }
                    
                    .layout-control .item-classification div.item-control .childs-row .ntsControl~.child-label {
                        padding-left: 25px;
                    } 

                    .layout-control .item-classification div.multiple-items {
                        overflow: hidden;
                    }

                    .layout-control .item-classification div.item-control .set-group:first-child .child-label {
                        display: none;
                    }

                    .layout-control .item-classification div.set-item,
                    .layout-control .item-classification div.item-control>div {
                        display: inline-block;
                    }

                    .layout-control .item-controls .table-container {
                        color: #000;
                        overflow: hidden;
                        padding-top: 31px;
                        position: relative;
                        border: 1px solid #aaa;
                        display: inline-block;
                        max-width: calc(100% - 240px);
                        background-color: #CFF1A5;
                        background: -webkit-repeating-linear-gradient(#CFF1A5, #CFF1A5 31px, #757575 31px, #CFF1A5 32px);
                        background: -o-repeating-linear-gradient(#CFF1A5, #CFF1A5 31px, #757575 31px, #CFF1A5 32px);
                        background: -moz-repeating-linear-gradient(#CFF1A5, #CFF1A5 31px, #757575 31px, #CFF1A5 32px);
                        background: repeating-linear-gradient(#CFF1A5, #CFF1A5 31px, #757575 31px, #CFF1A5 32px);
                    }

                    .layout-control .item-controls .table-container .ntsCheckBox {
                        padding: 0;
                        width: 30px;
                        height: 29px;
                        box-sizing: border-box;
                        margin: 0;
                    }

                    .layout-control .item-controls .table-container .ntsCheckBox:focus {
                        box-shadow: none;
                        outline: none;
                    }

                    .layout-control .item-controls .table-container .ntsCheckBox label{
                        width: 28px;
                        height: 26px;
                        display: block;
                        margin-top: -1px;
                        text-align: center;
                        padding-top: 3px;
                    }

                    .layout-control .item-controls .table-container .ntsCheckBox:focus label {
                        outline: 1px dashed #0096f2;
                    }
                
                    .layout-control .item-controls .table-container.header-1rows {
                        padding-top: 31px;
                    }
                
                    .layout-control .item-controls .table-container.header-2rows {
                        padding-top: 62px;
                    }
                
                    .layout-control .item-controls .table-container.header-3rows {
                        padding-top: 105px;
                    }
                
                    .layout-control .item-controls .table-container>div {
                        overflow-y: auto;
                        max-height: 159px;
                        border-top: 1px solid #aaa;
                    }
                
                    .layout-control .item-controls .table-container>div table {
                        border-collapse: collapse;
                    }
                
                    .layout-control .item-controls td {
                        background-color: #fff;
                        border-left: 1px solid #aaa;
                    }

                    .layout-control .item-control td div,
                    .layout-control .item-controls td div {
                        background-color: rgb(217, 217, 217);
                        height: 31px;
                        width: 100%;
                        display: block;
                    }
                
                    .layout-control .item-control td,
                    .layout-control .item-control th,
                    .layout-control .item-controls td,
                    .layout-control .item-controls th {
                        padding: 0px;
                        border: 1px solid #aaa;
                    }

                    .layout-control .item-control td,
                    .layout-control .item-control th {
                        background-color: #CFF1A5;
                    }
                
                    .layout-control .item-controls td:first-child {
                        border-left: none;
                    }
                
                    .layout-control .item-controls td:last-child {
                        border-right: none;
                    }
                
                    .layout-control .item-controls th {
                        height: 0;
                        line-height: 0;
                        border: none;
                        color: transparent;
                        white-space: nowrap;
                    }
                
                    .layout-control .item-controls th div {
                        top: 0;
                        height: 31px;
                        color: #000;
                        padding: 0;
                        overflow: hidden;
                        line-height: 31px;
                        text-align: center;
                        position: absolute;
                        background: #CFF1A5;
                        box-sizing: border-box;
                        border-left: 1px solid #aaa;
                    }

                    .layout-control .item-controls th div>i,
                    .layout-control .item-controls th div>label {
                        line-height: 31px;                        
                    }

                    .layout-control .item-controls th div.required {
                        background-color: #FAC002;
                    }
                
                    .layout-control .item-controls thead>tr:first-child div {
                        top: 0;
                    }
                
                    .layout-control .item-controls thead>tr:nth-child(2) div {
                        top: 35px;
                    }
                
                    .layout-control .item-controls thead>tr:nth-child(3) div {
                        top: 70px;
                    }

                    .layout-control .item-control td input,
                    .layout-control .item-control td textarea,
                    .layout-control .item-controls td input,
                    .layout-control .item-controls td textarea {
                        border: 1px solid transparent;
                        border-radius: 0;
                    }

                    .layout-control .item-control td .ntsControl.error input,
                    .layout-control .item-control td .ntsControl.error textarea{
                        border-style: dashed;
                    }

                    .layout-control .item-control td input:focus,
                    .layout-control .item-control td textarea:focus,
                    .layout-control .item-controls td input:focus,
                    .layout-control .item-controls td textarea:focus {
                        border: 1px dashed #0096f2;
                        box-shadow: none;
                    }
                
                    .layout-control .item-controls th:first-child div {
                        border: none;
                    }
                
                    .layout-control .item-controls tbody tr:first-child td {
                        border-top: none;
                    }
                
                    .layout-control .item-controls tbody tr:last-child td {
                        border-bottom: none;
                    }
                
                    .layout-control .set-table-items thead th {
                        padding: 0 3px;
                        line-height: 32px;
                        background-color: #CFF1A5;
                    }

                    .layout-control .item-classification div.item-sperator>hr {
                        padding: 0;
                        margin: 6px 2px 6px 0;
                    }

                    .layout-control.dragable .item-classification div.item-sperator>hr {
                        margin-right: 20px;
                    }

                    .layout-control .item-classification.ui-sortable-helper {
                        cursor: pointer;
                    }

                    .layout-control .item-classification.ui-sortable-placeholder {
                        border: 1px dashed #ddd;
                        visibility: visible !important;
                    }

                    .layout-control.dragable .item-classification:hover,
                    .layout-control.dragable .item-classification.selected {
                        background-color: #eee;
                        border: 1px dashed #aaa;
                    }

                    .layout-control .item-classification .item-control textarea.nts-editor,
                    .layout-control .item-classification .item-controls textarea.nts-editor {
                        width: 368px;
                        height: 70px;
                        overflow-y: scroll;
                    }

                    .layout-control .item-classification .ntsControl.radio-wrapper {
                        line-height: 30px;
                        margin-bottom: 10px;
                    }

                    .layout-control .item-classification .value-text {
                        padding-left: 20px;
                        display: inline-block;
                    }

                    .layout-control .item-classification .ntsRadioBox {
                        margin-right: 25px;
                    }

                    .layout-control .item-classification .item-controls .ntsControl,
                    .layout-control .item-classification .item-controls textarea.nts-editor,
                    .layout-control .item-classification .item-controls .ui-igcombo-wrapper {
                        display: block;
                    }

                    .layout-control .item-classification .numeric-button .nts-editor.nts-input {
                        width: 65px;
                    }

                    .layout-control .item-classification .set-item-sperator {
                        text-align: center;
                        min-width: 25px !important;
                        line-height: 30px !important;
                    }

                    .layout-control .item-classification .value-text.readonly,
                    .layout-control .item-classification .relate-button .value-text,
                    .layout-control .item-classification .readonly-button .value-text {
                        padding: 0;
                        min-width: 65px;
                        line-height: 35px;
                    }

                    .layout-control .item-classification .set-table-items .value-text.readonly {
                        padding-left: 15px;
                        width: 100%;
                        line-height: 30px;
                    }

                    .layout-control .item-classification .ui-igcombo-wrapper {
                        width: auto;
                        width: initial;                            
                    }

                    .layout-control .item-classification .form-label {
                        width: 210px;
                        white-space: nowrap;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        padding-right: 0;
                    }

                    .layout-control .item-classification .set-group>.form-label {
                        width: 160px;
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

                    .layout-control.dragable .item-classification:hover>.close-btn {
                        display: block;
                    }

                    .layout-control.readonly:not(.inputable) [disabled],
                    .layout-control.dragable:not(.inputable) [disabled] {
                        background-color: #fff;
                    }

                    .layout-control .index .remove-btn,
                    .layout-control.dragable .index:hover .remove-btn {
                        display: none;
                    }

                    .layout-control.inputable tr:hover .number,
                    .layout-control.inputable .index:hover .number {
                        display: block;
                    }

                    .layout-control.inputable tr:hover .remove-btn,
                    .layout-control.inputable .index:hover .remove-btn {
                        display: none;
                    }

                    .layout-control.inputable .remove-btn:hover {
                        color: #f00;
                        cursor: pointer;
                        -webkit-touch-callout: none;
                        -webkit-user-select: none;
                        -khtml-user-select: none;
                        -moz-user-select: none;
                        -ms-user-select: none;
                        user-select: none;
                    }

                    .layout-control.dragable .add-rows,
                    .layout-control.dragable .add-rows button,
                    .layout-control.inputable .add-rows,
                    .layout-control.inputable .add-rows button,
                    .layout-control.readonly:not(.inputable) .add-rows,
                    .layout-control.readonly:not(.inputable) .add-rows button {
                        display: none;
                    }

                    .layout-control.dragable .color-operation-case-character,
                    .layout-control.readonly:not(.inputable) .color-operation-case-character {
                        color: #000 !important;
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
                            <div class="form-group item-classification" 
                                    data-bind="let: {
                                        text: nts.uk.resource.getText,
                                        LAYOUT_TYPE: {
                                            ITEM: 'ITEM',
                                            LIST: 'LIST',
                                            SEPRL: 'SeparatorLine'
                                        },
                                        DISP_TYPE: {
                                            SINGLE: 'SINGLE',
                                            SET_TABLE: 'SET_TABLE',
                                            SET_INLINE: 'SET_INLINE',
                                            SET_MULTILINE: 'SET_MULTILINE',
                                            SET_MULTILINE_W_RADIO: 'SET_MULTILINE_W_RADIO',
                                            SET_MULTILINE_W_TITLE: 'SET_MULTILINE_W_TITLE'
                                        }
                                    }">
                                <!-- ko if: layoutItemType == LAYOUT_TYPE.ITEM -->
                                    <!-- ko foreach: { data: renders(), as: '_item' } -->
                                        <div class="item-control" data-bind="let: { _item: _item }">
                                            <div data-bind="ntsFormLabel: { 
                                                text: _item.itemName || '',
                                                cssClass: cssClass,
                                                required: _item.required,
                                                constraint: _item.constraint }"></div>
            
                                            <!-- ko if: _item.dispType == DISP_TYPE.SINGLE -->
                                                <!-- ko template: { data: _item, name: 'ctr_template' } --><!-- /ko -->
                                            <!-- /ko -->
    
                                            <!-- ko if: _item.dispType == DISP_TYPE.SET_INLINE -->
                                                <!-- ko template: { data: _item.childs[0], name: 'ctr_template' } --><!-- /ko -->
                                                <span class="value-text readonly set-item-sperator" data-bind="text: text('CPS001_89')"></span>
                                                <!-- ko template: { data: _item.childs[1], name: 'ctr_template' } --><!-- /ko -->
                                            <!-- /ko -->
            
                                            <!-- ko if: [
                                                            DISP_TYPE.SET_MULTILINE, 
                                                            DISP_TYPE.SET_MULTILINE_W_RADIO,
                                                            DISP_TYPE.SET_MULTILINE_W_TITLE
                                                        ].indexOf(_item.dispType) > -1 -->
                                                <div class="set-items">
                                                    <!-- ko if: _item.dispType == DISP_TYPE.SET_MULTILINE -->
                                                        <div class="set-group"></div>
                                                    <!-- /ko -->
        
                                                    <!-- ko ifnot: _item.dispType == DISP_TYPE.SET_MULTILINE_W_TITLE -->
                                                        <!-- ko foreach: { data: _item.childs, as: '_sitem' } -->
                                                            <div class="set-group">
                                                                <span class="child-label" data-bind="text: _sitem.itemName"></span>
                                                                <!-- ko if: _sitem.dispType == DISP_TYPE.SINGLE -->
                                                                    <!-- ko template: { data: _sitem, name: 'ctr_template' } --><!-- /ko -->
                                                                <!-- /ko -->
                
                                                                <!-- ko if: _sitem.dispType == DISP_TYPE.SET_INLINE -->
                                                                    <!-- ko template: { data: _sitem.childs[0], name: 'ctr_template' } --><!-- /ko -->
                                                                    <span class="value-text readonly set-item-sperator" data-bind="text: text('CPS001_89')"></span>
                                                                    <!-- ko template: { data: _sitem.childs[1], name: 'ctr_template' } --><!-- /ko -->
                                                                <!-- /ko -->
                                                            </div>
                                                        <!-- /ko -->
                                                    <!-- /ko -->
        
                                                    <!-- ko if: _item.dispType == DISP_TYPE.SET_MULTILINE_W_TITLE -->
                                                        <div class="set-group math-title" data-bind="text: text('CPS001_114')"></div>
                                                        <!-- ko foreach: { data: _item.childs, as: '_sitem' } -->
                                                            <div class="set-group">
                                                                <div data-bind="ntsFormLabel: { 
                                                                        text: _sitem.itemName,
                                                                        cssClass: cssClass,
                                                                        required: _sitem.required 
                                                                    }"></div>
                                                                <!-- ko if: _sitem.dispType == DISP_TYPE.SINGLE -->
                                                                    <div class="childs-row" data-bind="template: { data: _sitem, name: 'ctr_template' }"></div>
                                                                <!-- /ko -->
                
                                                                <!-- ko if: _sitem.dispType == DISP_TYPE.SET_INLINE -->
                                                                    <!-- ko foreach: {
                                                                            data: _(_sitem.childs).map(function(v, i) { 
                                                                                return { 
                                                                                    i: Math.floor(i / 2),
                                                                                    v: v
                                                                                }
                                                                            })
                                                                            .groupBy(function(x) { return x.i; })
                                                                            .map(function(x) {  
                                                                                return x.map(function(k) { 
                                                                                    return k.v;
                                                                                }); 
                                                                            })
                                                                            .value(), as: '_group' } -->
                                                                        <div class="childs-row">
                                                                            <!-- ko foreach: { data: _group, as: 'young' } -->
                                                                                <!-- ko if: young.title -->
                                                                                    <span class="child-label" data-bind="text: young.itemName"></span>
                                                                                <!-- /ko -->
                                                                                <!-- ko template: { data: young, name: 'ctr_template' } --><!-- /ko -->
                                                                            <!-- /ko -->
                                                                        </div>
                                                                    <!-- /ko -->
                                                                <!-- /ko -->
                                                            </div>
                                                        <!-- /ko -->
                                                    <!-- /ko -->
                                                </div>
                                            <!-- /ko -->
            
                                            <!-- ko if: _item.dispType == DISP_TYPE.SET_TABLE -->
                                                <div class="set-table-items" data-bind="template: { data: _item.childs, name: 'set_table_template' }"></div>
                                            <!-- /ko -->
                                        </div>
                                    <!-- /ko -->
                                <!-- /ko -->

                                <!-- ko if: layoutItemType == LAYOUT_TYPE.LIST -->
                                    <div class="item-controls">
                                        <div data-bind="ntsFormLabel: { required: false, text: className || '' }"></div>
                                        <div class="table-container header-1rows" data-bind="let: {
                                                    __lft: ko.observable(0),
                                                    __flft: ko.observable(0)
                                                }">
                                            <div class="table-scroll" data-bind="ntsProp: {
                                                        left: __lft,
                                                        scrollDown: 'tbody>tr',
                                                        maxHeight: {
                                                            byChild: 'tbody>tr',
                                                            length: 5
                                                        }
                                                    }">
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th data-bind="ntsProp: { left: __flft }">
                                                                <div data-bind="style: { 'margin-left': (__flft() - __lft()) + 'px' }, text: text('CPS001_146')"></div>
                                                            </th>
                                                            <!-- ko foreach: { data: _.first(renders()).items, as: 'header' } -->
                                                                <!-- ko let: { __wdt: ko.observable(0) } -->
                                                                <th data-bind="ntsProp: { width: __wdt }">
                                                                    <div data-bind="ntsFormLabel: { 
                                                                            text: header.itemName,
                                                                            required: header.required,
                                                                            constraint: header.constraint,
                                                                            inline: true
                                                                        },
                                                                        style: {
                                                                            'width': __wdt() + 'px',
                                                                            'margin-left': (__flft() - __lft()) + 'px'
                                                                        }"></div>
                                                                </th>
                                                                <!-- /ko -->
                                                            <!-- /ko -->
                                                        </tr>
                                                    </thead>
                                                    <tbody data-bind="foreach: { data: renders(), as: 'row', afterRender: function(element, data) { let _renders = _.map(ko.toJS(renders), function(m) { return m.recordId; }); if(_.indexOf(_renders, data.recordId) == _renders.length - 1) { setTimeout(function() { $(element[1]).find('input').unbind('blur'); }, 100) } } }">
                                                        <tr data-bind="attr: { 'data-id': row.recordId }">
                                                            <td>
                                                                <span data-bind="ntsCheckBox: { checked: row.checked, enable: row.enable }"></span>
                                                            </td>
                                                            <!-- ko foreach: { data: row.items, as: 'col' } -->
                                                            <td data-bind="template: { data: col, name: 'ctr_template' }"></td>
                                                            <!-- /ko -->
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                <!-- /ko -->

                                <!-- ko if: layoutItemType == LAYOUT_TYPE.SEPRL -->
                                    <div class="item-sperator"><hr /></div>
                                <!-- /ko -->

                                <span class="close-btn" data-bind="click: function($data, event) { ko.bindingHandlers['ntsLayoutControl'].remove($data, event); }">✖</span>
                            </div>
                        </div>
                        <button id="cps007_btn_line"></button>
                    </div>
                </div>
                <script type="text/html" id="set_table_template">
                    <table>
                        <thead>
                            <tr data-bind="foreach: { data: $data, as: '_column' }">
                                <th data-bind="text: _column.itemName"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr data-bind="foreach: { data: $data, as: '_column' }">
                                <td data-bind="template: { 
                                            data: _column,
                                            name: 'ctr_template'
                                        }"></td>
                            </tr>
                        </tbody>
                    </table>
                </script>
                <script type="text/html" id="ctr_template">
                    <!-- ko if: resourceId -->
                        <button class="inline" data-bind="attr: { title: resourceId }, text: text('？')">？</button>
                    <!-- /ko -->                    
                    <!-- ko let: { 
                                nameid : itemDefId.replace(/[-_]/g, ''),
                                DATE_TYPE: {
                                    YYYYMMDD: 1,
                                    YYYYMM: 2,
                                    YYYY: 3
                                },
                                STRING_TYPE: {
                                    ANY: 1,
                                    ANYHALFWIDTH: 2,
                                    ALPHANUMERIC: 3,
                                    NUMERIC: 4,
                                    KANA: 5,
                                    CARDNO: 6
                                },
                                CAT_TYPE: {  
                                    SINGLE : 1,
                                    MULTI: 2,
                                    CONTI: 3, /* continuos history hasn't end date */
                                    NODUP: 4,
                                    DUPLI: 5,
                                    CONTIWED: 6 /* continuos history has end date */
                                },
                                ITEM_TYPE: {
                                    STRING: 1,
                                    NUMERIC: 2,
                                    DATE: 3,
                                    TIME: 4,
                                    TIMEPOINT: 5,
                                    SELECTION: 6,
                                    SEL_RADIO: 7,
                                    SEL_BUTTON: 8,
                                    READONLY: 9,
                                    RELATE_CATEGORY: 10,
                                    NUMBERIC_BUTTON: 11,
                                    READONLY_BUTTON: 12
                                }
                            } -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.STRING -->
                        <!-- ko if: item.stringItemType == STRING_TYPE.NUMERIC || item.stringItemLength < 40 || ([STRING_TYPE.ANY, STRING_TYPE.ANYHALFWIDTH, STRING_TYPE.ALPHANUMERIC, STRING_TYPE.KANA, STRING_TYPE.CARDNO].indexOf(item.stringItemType) > -1 && item.stringItemLength <= 80) -->
                        <input data-bind=" ntsTextEditor: {
                                name: itemName,
                                value: value,
                                constraint: nameid,
                                required: required,
                                option: {
                                    textmode: 'text'
                                },
                                enable: editable,
                                readonly: readonly,
                                immediate: false
                            },  attr: {
                                id: nameid,
                                nameid: nameid,
                                title: itemName,
                                'data-title': itemName,
                                'data-code': itemCode,
                                'data-category': categoryCode,
                                'data-required': required,
                                'data-defv': defValue
                            }," />
                        <!-- /ko -->
                        <!-- ko if: item.stringItemType != STRING_TYPE.NUMERIC && (([STRING_TYPE.ANY, STRING_TYPE.ANYHALFWIDTH, STRING_TYPE.ALPHANUMERIC, STRING_TYPE.KANA, STRING_TYPE.CARDNO].indexOf(item.stringItemType) == -1 && item.stringItemLength >= 40) || ([STRING_TYPE.ANY, STRING_TYPE.ANYHALFWIDTH, STRING_TYPE.ALPHANUMERIC, STRING_TYPE.KANA, STRING_TYPE.CARDNO].indexOf(item.stringItemType) > -1 && item.stringItemLength > 80)) -->
                        <textarea data-bind="ntsMultilineEditor: {
                                name: itemName,
                                value: value,
                                constraint: nameid,
                                required: required,
                                option: {
                                    textmode: 'text'
                                },
                                enable: editable,
                                readonly: readonly,
                                immediate: false 
                            }, attr: { 
                                id: nameid, 
                                nameid: nameid,
                                title: itemName,
                                'data-title': itemName,
                                'data-code': itemCode,
                                'data-category': categoryCode,
                                'data-required': required,
                                'data-defv': defValue
                            }" />
                        <!-- /ko -->
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.NUMERIC -->
                        <input data-bind="ntsNumberEditor: { 
                                    name: itemName,
                                    value: value,
                                    constraint: nameid,
                                    required: required,
                                    option: {
                                        textalign: 'left',
                                        decimallength: Number(item.decimalPart),
                                        grouplength: item.numericItemAmount && 3
                                    },
                                    enable: editable,
                                    readonly: readonly
                                }, attr: {
                                    id: nameid, 
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }" />
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.DATE -->
                        <!-- ko if: index != 2 -->
                        <div data-bind="ntsDatePicker: {
                                name: itemName,
                                value: value,
                                startDate: startDate,
                                endDate: endDate,
                                constraint: nameid,
                                dateFormat: item.dateItemType == DATE_TYPE.YYYYMMDD ? 'YYYY/MM/DD' : (item.dateItemType == DATE_TYPE.YYYYMM ? 'YYYY/MM' : 'YYYY'),
                                enable: editable,
                                readonly: readonly
                            }, attr: { 
                                id: nameid, 
                                nameid: nameid,
                                title: itemName,
                                'data-title': itemName,
                                'data-code': itemCode,
                                'data-category': categoryCode,
                                'data-required': required,
                                'data-defv': defValue
                            }"></div>
                        <!-- /ko -->
                        <!-- ko if: index == 2 -->
                        <!-- ko if: typeof ctgType !== 'undefined' -->
                            <!-- ko if: [CAT_TYPE.CONTI].indexOf(ctgType) > -1 -->
                            <div class="value-text readonly" data-bind="text: value, attr: { title: itemName}"></div>
                            <!-- /ko -->
                            <!-- ko if: [CAT_TYPE.CONTI].indexOf(ctgType) == -1 -->
                            <div data-bind="ntsDatePicker: {
                                    name: itemName,
                                    value: value,
                                    startDate: startDate,
                                    endDate: endDate,
                                    constraint: nameid,
                                    dateFormat: item.dateItemType == DATE_TYPE.YYYYMMDD ? 'YYYY/MM/DD' : (item.dateItemType == DATE_TYPE.YYYYMM ? 'YYYY/MM' : 'YYYY'),
                                    enable: editable,
                                    readonly: readonly
                                }, attr: { 
                                    id: nameid, 
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }"></div>
                            <!-- /ko -->
                        <!-- /ko -->
                        <!-- ko if: typeof ctgType === 'undefined' -->
                            <div data-bind="ntsDatePicker: {
                                    name: itemName,
                                    value: value,
                                    startDate: startDate,
                                    endDate: endDate,
                                    constraint: nameid,
                                    dateFormat: item.dateItemType == DATE_TYPE.YYYYMMDD ? 'YYYY/MM/DD' : (item.dateItemType == DATE_TYPE.YYYYMM ? 'YYYY/MM' : 'YYYY'),
                                    enable: editable,
                                    readonly: readonly
                                }, attr: { 
                                    id: nameid, 
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }"></div>
                        <!-- /ko -->
                        <!-- /ko -->
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.TIME -->
                        <input data-bind="ntsTimeEditor: {
                                    name: itemName,
                                    value: value,
                                    constraint: nameid,
                                    required: required,
                                    inputFormat: 'time',
                                    enable: editable,
                                    mode: 'time',
                                    readonly: readonly
                                }, attr: {
                                    id: nameid, 
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }" />
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.TIMEPOINT -->
                        <input data-bind="ntsTimeWithDayEditor: { 
                                    name: itemName,
                                    constraint: nameid,
                                    value: value,
                                    enable: editable, 
                                    readonly: readonly,
                                    required: required
                                }, attr: {
                                    id: nameid, 
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }" />
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.SELECTION -->
                        <div data-bind="ntsComboBox: {
                                    name: itemName,
                                    value: value,
                                    options: lstComboBoxValue,
                                    optionsText: 'optionText',
                                    optionsValue: 'optionValue',
                                    editable: true,
                                    enable: editable,
                                    required: required,
                                    visibleItemsCount: 5,
                                    selectFirstIfNull: false,
                                    dropDownAttachedToBody: true,
                                    columns: [{ prop: 'optionText', length: 10, lengthDropDown: 20 }]
                                }, attr: {
                                    id: nameid,
                                    nameid: nameid,
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                }"></div>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.SEL_RADIO -->
                            <div data-bind="ntsRadioBoxGroup: {
                                name: itemName,
                                value: value,
                                options: lstComboBoxValue,
                                optionsText: 'optionText',
                                optionsValue: 'optionValue',
                                enable: editable
                            }, attr: {
                                id: nameid,
                                'data-code': itemCode,
                                'data-category': categoryCode,
                                'data-required': required,
                                'data-defv': defValue,
                                title: itemName,
                                'data-title': itemName,
                            }"></div>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.SEL_BUTTON -->
                            <button data-bind="attr: { 
                                id: nameid,
                                title: itemName,
                                'data-title': itemName,
                                'data-code': itemCode,
                                'data-category': categoryCode,
                                'data-required': required,
                                'data-defv': defValue
                             }, text: text('CPS001_106'), enable: editable">選択</button>
                            <label class="value-text readonly" data-bind="html: textValue"></label>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.READONLY -->
                            <label class="value-text readonly" data-bind="
                                text: value,
                                attr: { 
                                    id: nameid, 
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                 }"></label>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.RELATE_CATEGORY -->
                            <div class="relate-button">
                                <label class="value-text readonly" data-bind="text: value"></label>
                                <button data-bind="attr: { 
                                    id: nameid, 
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                 }, text: text('CPS001_127'), enable: editable">選択</button>
                            </div>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.NUMBERIC_BUTTON -->
                            <div class="numeric-button">
                                <input data-bind="ntsNumberEditor: { 
                                            name: itemName,
                                            value: value,
                                            constraint: nameid,
                                            required: required,
                                            option: {
                                                textalign: 'left',
                                                decimallength: Number(item.decimalPart),
                                                grouplength: item.numericItemAmount && 3
                                            },
                                            enable: editable() && numberedit(),
                                            readonly: readonly
                                        }, attr: {
                                            id: nameid, 
                                            nameid: nameid,
                                            title: itemName,
                                            'data-title': itemName,
                                            'data-code': itemCode,
                                            'data-category': categoryCode,
                                            'data-required': required,
                                            'data-defv': defValue
                                        }" />                            
                                <button data-bind="attr: { 
                                    id: nameid, 
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                 }, text: text('CPS001_127'), enable: editable">選択</button>
                            </div>
                        <!-- /ko -->
                        <!-- ko if: item.dataTypeValue == ITEM_TYPE.READONLY_BUTTON -->
                            <div class="readonly-button">
                                <label class="value-text readonly" class="value-text" data-bind="text: value"></label>
                                <button data-bind="attr: { 
                                    id: nameid, 
                                    title: itemName,
                                    'data-title': itemName,
                                    'data-code': itemCode,
                                    'data-category': categoryCode,
                                    'data-required': required,
                                    'data-defv': defValue
                                 }, text: text('CPS001_127'), enable: editable">選択</button>
                            </div>
                        <!-- /ko -->
                    <!-- /ko -->
                </script>`;

        private services = {
            getCat: (cid) => ajax(`ctx/pereg/person/info/category/find/companyby/${cid}`),
            getCats: () => ajax(`ctx/pereg/person/info/category/findby/companyv2`),
            getGroups: () => ajax(`ctx/pereg/person/groupitem/getAll`),
            getItemByCat: (cid) => ajax(`ctx/pereg/person/info/ctgItem/layout/findby/categoryId/${cid}`),
            getItemByGroup: (gid) => ajax(`ctx/pereg/person/groupitem/getAllItemDf/${gid}`),
            getItemByGroups: (gids: Array<any>) => ajax(`ctx/pereg/person/groupitem/findby/listgroupId`, gids),
            getItemsById: (id: string) => ajax(`ctx/pereg/person/info/ctgItem/layout/findby/itemId/${id}`),
            getItemsByIds: (ids: Array<any>) => ajax(`ctx/pereg/person/info/ctgItem/layout/findby/listItemIdv2`, ids)
        };

        remove = (item, sender) => {
            let target = $(sender.target),
                layout = target.parents('.layout-control'),
                opts = layout.data('options'),
                ctrls = layout.data('controls'),
                clst = $(ctrls.sortable).scrollTop();

            opts.sortable.removeItem(item, false);

            setTimeout(() => {
                $(ctrls.sortable).scrollTop(clst);
            }, 0);
        };

        private _constructor = (element: HTMLElement, valueAccessor: any) => {
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
                    combobox: {
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
                        fields: ['itemName'],
                        placeHolder: '名称で検索…'
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
                        outData: ko.observableArray([]),
                        isEnabled: ko.observable(true),
                        isEditable: ko.observable(0),
                        showColor: ko.observable(false),
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
                        removeItem: (data: IItemClassification, byItemId?: boolean) => {
                            let items: Array<any> = _(ko.toJS(opts.sortable.data))
                                .map((x: IItemClassification) => _.omit(x, "items"))
                                .value();

                            if (!byItemId) { // remove item by classification id (virtual id)
                                items = _.filter(items, x => x.layoutID != data.layoutID);
                            } else if (data.listItemDf) { // remove item by item definition id
                                items = _.filter(items, (x: IItemClassification) => x.listItemDf && x.listItemDf[0].id == data.listItemDf[0].id);
                            }

                            let maps: Array<number> = _(items).map((x: IItemClassification, i) => (x.layoutItemType == IT_CLA_TYPE.SPER) ? i : -1)
                                .filter(x => x != -1)
                                .orderBy(x => x).value()

                            // remove next line if two line is sibling
                            _.each(maps, (x, i) => {
                                if (maps[i + 1] == x + 1) {
                                    _.remove(items, m => {
                                        let item: IItemClassification = ko.unwrap(items)[maps[i + 1]];
                                        return item && item.layoutItemType == IT_CLA_TYPE.SPER && item.layoutID == m.layoutID;
                                    });
                                }
                            });

                            opts.sortable.data(items);

                            return opts.sortable;
                        },
                        removeItems: (data: Array<IItemClassification>) => {
                            if (data && data.length) {
                                _.each(data, x => opts.sortable.removeItem(x, true));
                            }
                        },
                        findExist: (ids: Array<string>) => {
                            if (!ids || !ids.length) {
                                return [];
                            }

                            let items: Array<IItemClassification> = opts.sortable.data();

                            // return items if it's exist in list
                            return _(items)
                                .map(x => x.listItemDf)
                                .flatten()
                                .filter((x: IItemDefinition) => x && ids.indexOf(x.id) > -1)
                                .value();
                        },
                        pushItem: (data: IItemClassification) => {
                            if (data.layoutItemType == IT_CLA_TYPE.SPER) {
                                // add line to list sortable
                                let last: any = _.last(ko.unwrap(opts.sortable.data));

                                if (last && last.layoutItemType != IT_CLA_TYPE.SPER) {
                                    opts.sortable.data.push(data);
                                }
                            }
                        },
                        pushItems: (defs: Array<IItemDefinition>) => {
                            let is_new = location.href.indexOf('/view/cps/007') > -1,
                                relates: Array<string> = _(defs).map((x: any) => ((x.itemTypeState || {}).dataTypeState || {}).relatedCtgCode)
                                    .filter(x => x)
                                    .value();

                            let items1 = _(ko.toJS(opts.sortable.data))
                                .map(x => _.omit(x, "items"))
                                .value(),
                                items2 = _(defs)
                                    .filter(def => {
                                        return !def.itemParentCode
                                            && relates.indexOf(def.perInfoCtgId) == -1;
                                    })
                                    .map(def => {
                                        let is_relate = ((def.itemTypeState || {}).dataTypeState || {}).relatedCtgCode,
                                            new_mode = !!is_relate && !!is_new,
                                            dispOrder: number = ko.toJS(opts.sortable.data).length,
                                            item: IItemClassification = {
                                                layoutID: random(),
                                                dispOrder: -1,
                                                personInfoCategoryID: undefined,
                                                layoutItemType: IT_CLA_TYPE.ITEM,
                                                listItemDf: []
                                            };

                                        def.dispOrder = dispOrder + 1;
                                        item.listItemDf = !new_mode ? [def] : _(defs).filter(f => f.perInfoCtgId == is_relate).value();
                                        item.className = !new_mode ? def.itemName : undefined;
                                        item.personInfoCategoryID = def.perInfoCtgId;
                                        // setitem
                                        if ([ITEM_TYPE.SET, ITEM_TYPE.SET_TABLE].indexOf(def.itemTypeState.itemType) > -1) {
                                            let childs = _(defs)
                                                .filter(x => x.itemParentCode == def.itemCode)
                                                .orderBy(x => x.dispOrder)
                                                .value();

                                            item.listItemDf = _.concat(item.listItemDf, childs);
                                            _.each(childs, c => {
                                                // setitem
                                                if ([ITEM_TYPE.SET, ITEM_TYPE.SET_TABLE].indexOf(c.itemTypeState.itemType) > -1) {
                                                    let newchilds = _(defs)
                                                        .filter(x => x.itemParentCode == c.itemCode)
                                                        .orderBy(x => x.dispOrder)
                                                        .value();

                                                    item.listItemDf = _.concat(item.listItemDf, newchilds);
                                                }
                                            });
                                        }

                                        if (item.listItemDf[0] && item.personInfoCategoryID != item.listItemDf[0].perInfoCtgId) {
                                            item.personInfoCategoryID = item.listItemDf[0].perInfoCtgId;
                                        }

                                        if (item.listItemDf[0] && item.personInfoCategoryID != item.listItemDf[0].perInfoCtgId) {
                                            item.personInfoCategoryID = item.listItemDf[0].perInfoCtgId;
                                        }

                                        return item;
                                    })
                                    .filter(x => x.listItemDf && x.listItemDf.length)
                                    .value();

                            opts.sortable.data(_.concat(items1, items2));

                            scrollDown();
                        },
                        pushAllItems: (defs: Array<IItemDefinition>, groupMode?: boolean) => {
                            let self = this,
                                services = self.services;

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
                                    info({
                                        messageId: 'Msg_204',
                                        messageParams: dups.map((x: IItemDefinition) => x.itemName)
                                    })
                                        .then(() => {
                                            opts.sortable.removeItems(dups.map((x: IItemDefinition) => {
                                                return {
                                                    layoutID: random(),
                                                    dispOrder: -1,
                                                    personInfoCategoryID: undefined,
                                                    layoutItemType: IT_CLA_TYPE.ITEM,
                                                    listItemDf: [x]
                                                };
                                            }));
                                            opts.sortable.pushItems(defs);
                                        });
                                } else {
                                    opts.sortable.pushItems(defs);
                                }
                            } else {
                                let dupids = dups.map((x: IItemDefinition) => x.id),
                                    nodups = defs.filter((x: IItemDefinition) => dupids.indexOf(x.id) == -1);

                                if (dupids && dupids.length) {
                                    // 画面項目「選択可能項目一覧」で選択している項目が既に画面に配置されている場合
                                    // When the item selected in the screen item "selectable item list" has already been arranged on the screen
                                    alert({
                                        messageId: 'Msg_202',
                                        messageParams: dups.map((x: IItemDefinition) => x.itemName)
                                    });
                                }

                                if (dups.length == 1 && ((dups[0].itemTypeState || {}).dataTypeState || {}).dataTypeValue == ITEM_SINGLE_TYPE.RELATE_CATEGORY) {
                                    return;
                                }

                                opts.sortable.pushItems(nodups);
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

                        if (x == 2) {
                            $element.addClass("inputable");
                        }
                    } else {
                        opts.sortable.isEditable(0);

                        if (x) {
                            opts.sortable.isEnabled(true);
                        } else {
                            opts.sortable.isEnabled(false);
                        }
                    }
                },
                exceptConsts: Array<string> = [],
                // render primative value to viewContext
                primitiveConst = (x) => {
                    let dts = x.item,
                        constraint: any = {
                            itemName: x.itemName,
                            itemCode: x.itemDefId.replace(/[-_]/g, ""),
                            required: x.required// !!x.isRequired
                        };

                    if (dts) {
                        switch (dts.dataTypeValue) {
                            default:
                            case ITEM_SINGLE_TYPE.STRING:
                                constraint.valueType = "String";
                                constraint.maxLength = dts.stringItemLength || dts.maxLength;
                                constraint.stringExpression = /(?:)/;

                                switch (dts.stringItemType) {
                                    default:
                                    case ITEM_STRING_TYPE.ANY:
                                        constraint.charType = 'Any';
                                        break;
                                    case ITEM_STRING_TYPE.CARDNO:
                                    case ITEM_STRING_TYPE.ANYHALFWIDTH:
                                        constraint.charType = 'AnyHalfWidth';
                                        break;
                                    case ITEM_STRING_TYPE.ALPHANUMERIC:
                                        constraint.charType = 'AlphaNumeric';
                                        break;
                                    case ITEM_STRING_TYPE.NUMERIC:
                                        constraint.charType = 'Numeric';
                                        break;
                                    case ITEM_STRING_TYPE.KANA:
                                        constraint.charType = 'Kana';
                                        break;
                                }
                                break;
                            case ITEM_SINGLE_TYPE.NUMERIC:
                                constraint.charType = 'Numeric';
                                if (dts.decimalPart == 0) {
                                    constraint.valueType = "Integer";
                                } else {
                                    constraint.valueType = "Decimal";
                                    constraint.mantissaMaxLength = dts.decimalPart;
                                }

                                let max = (Math.pow(10, dts.integerPart) - Math.pow(10, -(dts.decimalPart || 0)));
                                constraint.min = dts.numericItemMin || 0;
                                constraint.max = dts.numericItemMax || max;
                                break;
                            case ITEM_SINGLE_TYPE.DATE:
                                constraint.valueType = "Date";
                                constraint.max = parseTime(dts.max, true).format() || '';
                                constraint.min = parseTime(dts.min, true).format() || '';
                                break;
                            case ITEM_SINGLE_TYPE.TIME:
                                constraint.valueType = "Time";
                                constraint.max = parseTime(dts.max, true).format();
                                constraint.min = parseTime(dts.min, true).format();
                                break;
                            case ITEM_SINGLE_TYPE.TIMEPOINT:
                                constraint.valueType = "Clock";
                                constraint.max = parseTimeWidthDay(dts.timePointItemMax).shortText;
                                constraint.min = parseTimeWidthDay(dts.timePointItemMin).shortText;
                                break;
                            case ITEM_SINGLE_TYPE.SELECTION:
                                constraint.valueType = "Selection";
                                break;
                            case ITEM_SINGLE_TYPE.SEL_RADIO:
                                constraint.valueType = "Radio";
                                break;
                            case ITEM_SINGLE_TYPE.SEL_BUTTON:
                                constraint.valueType = "Button";
                                break;
                            case ITEM_SINGLE_TYPE.READONLY:
                                constraint.valueType = "READONLY";
                                break;
                            case ITEM_SINGLE_TYPE.RELATE_CATEGORY:
                                constraint.valueType = "RELATE_CATEGORY";
                                break;
                            case ITEM_SINGLE_TYPE.NUMBERIC_BUTTON:
                                constraint.valueType = "NUMBERIC_BUTTON";
                                break;
                            case ITEM_SINGLE_TYPE.READONLY_BUTTON:
                                constraint.valueType = "READONLY_BUTTON";
                                break;
                        }
                    }
                    return constraint;
                },
                primitiveConsts = () => {
                    let constraints = _(ko.unwrap(opts.sortable.data))
                        .map((x: any) => _.has(x, "items") && x.items)
                        .flatten()
                        .flatten()
                        .filter((x: any) => _.has(x, "item") && !_.isEqual(x.item, {}))
                        .map((x: any) => primitiveConst(x))
                        .filter((x: any) => exceptConsts.indexOf(x.itemCode) == -1)
                        .value();

                    if (constraints && constraints.length) {
                        exceptConsts = [];
                        writeConstraints(constraints);
                    }
                },
                dateTimeConsts = () => {
                    let range = [
                        ITEM_SINGLE_TYPE.DATE,
                        ITEM_SINGLE_TYPE.TIME,
                        ITEM_SINGLE_TYPE.TIMEPOINT
                    ], controls = _(ko.unwrap(opts.sortable.data))
                        .filter(x => _.has(x, "items") && !!x.items)
                        .map(x => x.items)
                        .flatten()
                        .flatten()
                        .filter(x => !!x)
                        .value();

                    // validate for singe date
                    _(controls)
                        .filter((x: any) => {
                            return x.item
                                && x.item.dataTypeValue == ITEM_SINGLE_TYPE.DATE;
                        })
                        .each((x: any) => {
                            x.startDate = ko.observable();
                            x.endDate = ko.observable();
                        });

                    // validate date, time, timepoint range
                    _(controls)
                        .filter((x: any) => x.type == ITEM_TYPE.SET)
                        .each((x: any) => {
                            let childs: Array<any> = _(controls)
                                .filter((c: any) => c.itemParentCode == x.itemCode)
                                //.orderBy(c => c)
                                .value(),
                                is_range = _.filter(childs, (c: any) => c.item && range.indexOf(c.item.dataTypeValue) > -1);

                            if (childs.length == 2 && childs.length == is_range.length) {
                                let first: any = childs[0],
                                    second: any = childs[1],
                                    validate = (prev: any, next: any) => {
                                        let id1 = '#' + prev.itemDefId.replace(/[-_]/g, ""),
                                            id2 = '#' + next.itemDefId.replace(/[-_]/g, "");

                                        let _bind = $(document).data('_nts_bind') || {};

                                        if (!_bind[`BLUR_${id1}_${id2}`]) {
                                            _bind[`BLUR_${id1}_${id2}`] = true;
                                            $(document).data('_nts_bind', _bind);

                                            $(document).on('blur', `${id1}, ${id2}`, (evt) => {
                                                setTimeout(() => {
                                                    let dom1 = $(id1),
                                                        dom2 = $(id2),
                                                        pv = ko.toJS(prev.value),
                                                        nv = ko.toJS(next.value),
                                                        tpt = _.isNumber(pv),
                                                        tnt = _.isNumber(nv);

                                                    if (!tpt && tnt) {
                                                        if (!dom1.is(':disabled') && !dom1.ntsError('hasError')) {
                                                            dom1.ntsError('set', { messageId: "Msg_858" });
                                                        }
                                                    } else {
                                                        rmError(dom1, "Msg_858");
                                                    }

                                                    if (tpt && !tnt) {
                                                        if (!dom2.is(':disabled') && !dom2.ntsError('hasError')) {
                                                            dom2.ntsError('set', { messageId: "Msg_858" });
                                                        }
                                                    } else {
                                                        rmError(dom2, "Msg_858");
                                                    }
                                                }, 5);
                                            });
                                        }
                                    };

                                if (first.item.dataTypeValue == second.item.dataTypeValue) {
                                    switch (first.item.dataTypeValue) {
                                        case ITEM_SINGLE_TYPE.DATE:
                                            first.startDate = ko.observable();
                                            first.endDate = ko.computed(() => {
                                                return moment.utc(ko.toJS(second.value) || '9999/12/31', "YYYY/MM/DD")
                                                    //.add(ko.toJS(second.value) ? -1 : 0, "days")
                                                    .toDate();
                                            });

                                            second.startDate = ko.computed(() => {
                                                return moment.utc(ko.toJS(first.value) || '1900/01/01', "YYYY/MM/DD")
                                                    //.add(ko.toJS(first.value) ? 1 : 0, "days")
                                                    .toDate();
                                            });
                                            second.endDate = ko.observable();
                                            break;
                                        case ITEM_SINGLE_TYPE.TIME:
                                            validate(first, second);
                                            first.value.subscribe(v => {
                                                let t = typeof v == 'number',
                                                    clone = _.cloneDeep(second);

                                                clone.item.min = first.value() + 1;

                                                let primi = primitiveConst(t ? clone : second);

                                                exceptConsts.push(primi.itemCode);
                                                writeConstraint(primi.itemCode, primi);
                                            });
                                            first.value.valueHasMutated();

                                            second.value.subscribe(v => {
                                                let t = typeof v == 'number',
                                                    clone = _.cloneDeep(first);

                                                clone.item.max = second.value() - 1;

                                                let primi = primitiveConst(t ? clone : first);

                                                exceptConsts.push(primi.itemCode);
                                                writeConstraint(primi.itemCode, primi);
                                            });
                                            second.value.valueHasMutated();
                                            break;
                                        case ITEM_SINGLE_TYPE.TIMEPOINT:
                                            validate(first, second);
                                            first.value.subscribe(v => {
                                                let t = typeof v == 'number',
                                                    clone = _.cloneDeep(second);

                                                clone.item.timePointItemMin = first.value() + 1;

                                                let primi = primitiveConst(t ? clone : second);

                                                exceptConsts.push(primi.itemCode);
                                                writeConstraint(primi.itemCode, primi);
                                            });
                                            first.value.valueHasMutated();

                                            second.value.subscribe(v => {
                                                let t = typeof v == 'number',
                                                    clone = _.cloneDeep(first);

                                                clone.item.timePointItemMax = second.value() - 1;

                                                let primi = primitiveConst(t ? clone : first);

                                                exceptConsts.push(primi.itemCode);
                                                writeConstraint(primi.itemCode, primi);
                                            });
                                            second.value.valueHasMutated();
                                            break;
                                    }
                                }
                            }
                        });
                },
                // define common function for init new item value
                isStr = (item: any) => {
                    if (!item) {
                        return false;
                    }

                    switch (item.dataTypeValue) {
                        default:
                            return false;
                        case ITEM_SINGLE_TYPE.STRING:
                        case ITEM_SINGLE_TYPE.SELECTION:
                            return true;
                    }
                },
                modifitem = (def: any, item?: any) => {
                    let lstItem = [
                        { optionValue: '1', optionText: text('CPS001_100') },
                        { optionValue: '0', optionText: text('CPS001_99') }
                    ];

                    if (!item) {
                        item = {};
                    }

                    def.itemCode = _.has(def, "itemCode") && def.itemCode || item.itemCode;
                    def.itemName = _.has(def, "itemName") && def.itemName || item.itemName;
                    def.itemDefId = _.has(def, "itemDefId") && def.itemDefId || item.id;
                    def.required = _.has(def, "required") && def.required || !!item.isRequired;

                    def.resourceId = _.has(def, "resourceId") && def.resourceId || undefined;

                    def.itemParentCode = _.has(def, "itemParentCode") && def.itemParentCode || item.itemParentCode;

                    def.categoryCode = _.has(def, "categoryCode") && def.categoryCode || '';

                    def.lstComboBoxValue = _.has(def, "lstComboBoxValue") ? (ko.isObservable(def.lstComboBoxValue) ? def.lstComboBoxValue : ko.observableArray(def.lstComboBoxValue || lstItem)) : ko.observableArray(lstItem);

                    def.hidden = _.has(def, "actionRole") ? def.actionRole == ACTION_ROLE.HIDDEN : true;
                    def.readonly = ko.observable(_.has(def, "actionRole") ? def.actionRole == ACTION_ROLE.VIEW_ONLY : !!opts.sortable.isEnabled());
                    def.editable = ko.observable(_.has(def, "actionRole") ? def.actionRole == ACTION_ROLE.EDIT : !!opts.sortable.isEditable());
                    def.numberedit = ko.observable(false);

                    def.showColor = _.has(def, "showColor") ? (ko.isObservable(def.showColor) ? def.showColor : ko.observable(def.showColor)) :
                        (ko.isObservable(opts.sortable.showColor) ? opts.sortable.showColor : ko.observable(opts.sortable.showColor));

                    def.type = _.has(def, "type") ? def.type : (item.itemTypeState || <any>{}).itemType;
                    def.item = _.has(def, "item") ? def.item : $.extend({}, ((item || <any>{}).itemTypeState || <any>{}).dataTypeState || {});

                    def.value = ko.isObservable(def.value) ? def.value : ko.observable(isStr(def.item) && def.value ? String(def.value) : def.value);
                    def.textValue = ko.observable('');

                    def.defValue = ko.toJS(def.value);

                    //def.editable.subscribe(x => { if (!x) { def.value(def.defValue); } });

                    if (def.item && def.item.dataTypeValue == ITEM_SINGLE_TYPE.SELECTION) {
                        let data = ko.toJS(def.lstComboBoxValue),
                            selected = _.find(data, f => f.optionValue == def.value());

                        if (!selected) {
                            def.value(undefined);
                        }
                    }

                    if (def.item && def.item.dataTypeValue == ITEM_SINGLE_TYPE.SEL_BUTTON) {
                        def.value.subscribe(v => {
                            if (v) {
                                let data = ko.toJS(def.lstComboBoxValue),
                                    selected = _.find(data, f => f.optionValue == v);
                                if (selected) {
                                    def.textValue(selected.optionText);
                                } else {
                                    def.textValue(`${v}&nbsp;&nbsp;&nbsp;${text('CPS001_107')}`);
                                }
                            } else {
                                def.textValue('');
                            }
                        });
                    }

                    if (ko.toJS(access.editAble) == 2) {
                        def.value.subscribe(x => {
                            calc_data();
                        });
                    }

                    def.editable.subscribe(x => {
                        def.value.valueHasMutated();
                    });
                    def.editable.valueHasMutated();
                },
                calc_data = () => {
                    if (ko.toJS(access.editAble) == 2) {
                        let inputs = [],
                            proc = function(data: any): any {
                                if (!data.item) {
                                    return {
                                        value: String(data.value),
                                        typeData: 1
                                    };
                                }

                                switch (data.item.dataTypeValue) {
                                    default:
                                    case ITEM_SINGLE_TYPE.STRING:
                                        return {
                                            value: !nou(data.value) ? String(data.value) : undefined,
                                            typeData: 1
                                        };
                                    case ITEM_SINGLE_TYPE.TIME:
                                    case ITEM_SINGLE_TYPE.NUMERIC:
                                    case ITEM_SINGLE_TYPE.TIMEPOINT:
                                        return {
                                            value: !nou(data.value) ? String(data.value).replace(/:/g, '') : undefined,
                                            typeData: 2
                                        };
                                    case ITEM_SINGLE_TYPE.DATE:
                                        return {
                                            value: !nou(data.value) ? moment.utc(data.value, "YYYY/MM/DD").format("YYYY/MM/DD") : undefined,
                                            typeData: 3
                                        };
                                    case ITEM_SINGLE_TYPE.SELECTION:
                                    case ITEM_SINGLE_TYPE.SEL_RADIO:
                                    case ITEM_SINGLE_TYPE.SEL_BUTTON:
                                        switch (data.item.referenceType) {
                                            case ITEM_SELECT_TYPE.ENUM:
                                                return {
                                                    value: !nou(data.value) ? (String(data.value) || undefined) : undefined,
                                                    typeData: 2
                                                };
                                            case ITEM_SELECT_TYPE.CODE_NAME:
                                                return {
                                                    value: !nou(data.value) ? (String(data.value) || undefined) : undefined,
                                                    typeData: 1
                                                };
                                            case ITEM_SELECT_TYPE.DESIGNATED_MASTER:
                                                let value: number = !nou(data.value) ? Number(data.value) : undefined;
                                                if (!nou(value)) {
                                                    if (String(value) == String(data.value)) {
                                                        return {
                                                            value: !nou(data.value) ? (String(data.value) || undefined) : undefined,
                                                            typeData: 2
                                                        };
                                                    } else {
                                                        return {
                                                            value: !nou(data.value) ? (String(data.value) || undefined) : undefined,
                                                            typeData: 1
                                                        };
                                                    }
                                                } else {
                                                    return {
                                                        value: !nou(data.value) ? (String(data.value) || undefined) : undefined,
                                                        typeData: 1
                                                    };
                                                }
                                        }
                                    case ITEM_SINGLE_TYPE.READONLY:
                                    case ITEM_SINGLE_TYPE.RELATE_CATEGORY:
                                        return null;
                                    case ITEM_SINGLE_TYPE.NUMBERIC_BUTTON:
                                        return {
                                            value: !nou(data.value) ? String(data.value) : undefined,
                                            typeData: 2
                                        };
                                    case ITEM_SINGLE_TYPE.READONLY_BUTTON:
                                        return null;
                                }
                            },
                            grbc = _(opts.sortable.data())
                                .filter(x => _.has(x, "items") && !!x.items)
                                .map(x => ko.toJS(x.items))
                                .flatten()
                                .filter((x: any) => _.has(x, "item") && !!x.item)
                                .map((x: any) => {
                                    let data = proc(x);
                                    return data ? {
                                        checked: x.checked,
                                        recordId: x.recordId,
                                        categoryCd: x.categoryCode,
                                        definitionId: x.itemDefId,
                                        itemCode: x.itemCode,
                                        value: data.value,
                                        dvalue: x.defValue,
                                        'type': data.typeData
                                    } : null;
                                })
                                .filter(x => !!x)
                                .groupBy((x: any) => x.categoryCd)
                                .value(),
                            categoryCds = _.keys(grbc);

                        _(categoryCds).each(categoryCd => {
                            let group = _.groupBy(grbc[categoryCd], (m: any) => m.recordId),
                                recordIds = _.keys(group);

                            _.each(recordIds, recordId => {
                                let _recordId = ["undefined", "null"].indexOf(recordId) > -1 ? undefined : recordId.indexOf("NID_") > -1 ? undefined : recordId,
                                    _categoryCd = ["undefined", "null"].indexOf(categoryCd) > -1 ? undefined : categoryCd,
                                    deleted = group[recordId].map(m => m.checked).filter(m => !m).length == 0;

                                if (_recordId || (!_recordId && !deleted)) {
                                    // delete check for CARD_NO
                                    if (_categoryCd == "CS00069" && !group[recordId][0].value) {
                                        deleted = true;
                                    }

                                    inputs.push({
                                        recordId: _recordId,
                                        categoryCd: _categoryCd,
                                        'delete': deleted,
                                        items: group[recordId].map(m => {
                                            return {
                                                definitionId: m.definitionId,
                                                itemCode: m.itemCode,
                                                value: deleted ? m.dvalue : m.value,
                                                'type': m.type
                                            };
                                        })
                                    });
                                }
                            });
                        });

                        inputs = _(inputs).filter(f => {
                            return f.items.filter(m => !!m.value).length > 0 || (f.recordId && f.delete);
                        }).value();

                        // change value
                        opts.sortable.outData(inputs);
                    }
                },
                def_type = (items: Array<any>) => {
                    let SET_MULTILINE_W_TITLE = _.filter(items, f => !f.itemParentCode).length > 1;

                    _(items).each((x, i) => {
                        let single = _.map(x.childs, m => m.childs.length).filter(m => m != 0).length == 0;

                        x.index = i;
                        x.cssClass = ko.toJS(x.showColor) && 'color-operation-case-character';

                        if (x.childs.length == 0 || x.type == ITEM_TYPE.SINGLE) {
                            x.dispType = DISP_TYPE.SINGLE;

                            if (x.parent && x.parent.parent) {
                                let parent: any = x.parent,
                                    grandp: any = parent.parent,
                                    has_single = _.map(grandp.childs, m => m.childs.length).filter(m => m == 0).length > 0,
                                    has_multiple = _.map(grandp.childs, m => m.childs.length).filter(m => m > 0).length > 0;

                                if (has_single && has_multiple && parent.childs.indexOf(x) == 0) {
                                    x.title = false;
                                } else {
                                    x.title = true;
                                }
                            } else {
                                x.title = true;
                            }
                        } else if (x.childs.length == 1) {
                            if ((x.childs[0].item || {}).dataTypeValue == ITEM_SINGLE_TYPE.SEL_RADIO) {
                                x.dispType = DISP_TYPE.SET_MULTILINE_W_RADIO;
                            } else if (single && SET_MULTILINE_W_TITLE) {
                                x.dispType = DISP_TYPE.SET_INLINE;
                            } else {
                                x.dispType = DISP_TYPE.SET_MULTILINE;
                            }
                        } else if (x.childs.length == 2 && x.type == ITEM_TYPE.SET) {
                            if (single) {
                                x.dispType = DISP_TYPE.SET_INLINE;
                            } else {
                                if ((x.childs[0].item || {}).dataTypeValue == ITEM_SINGLE_TYPE.SEL_RADIO) {
                                    x.dispType = DISP_TYPE.SET_MULTILINE_W_RADIO;
                                } else {
                                    if (!SET_MULTILINE_W_TITLE) {
                                        x.dispType = DISP_TYPE.SET_MULTILINE;
                                    } else {
                                        x.dispType = DISP_TYPE.SET_MULTILINE_W_TITLE;
                                    }
                                }
                            }
                        } else if (x.type == ITEM_TYPE.SET) {
                            if (single && SET_MULTILINE_W_TITLE) {
                                x.dispType = DISP_TYPE.SET_INLINE;
                            } else {
                                if ((x.childs[0].item || {}).dataTypeValue == ITEM_SINGLE_TYPE.SEL_RADIO) {
                                    x.dispType = DISP_TYPE.SET_MULTILINE_W_RADIO;
                                } else {
                                    if (!SET_MULTILINE_W_TITLE) {
                                        x.dispType = DISP_TYPE.SET_MULTILINE;
                                    } else {
                                        x.dispType = DISP_TYPE.SET_MULTILINE_W_TITLE;
                                    }
                                }
                            }
                        } else if (x.type == ITEM_TYPE.SET_TABLE) {
                            x.dispType = DISP_TYPE.SET_TABLE;
                        }
                    });
                },
                hierarchies = (cls: IItemClassification) => {
                    if (cls.layoutItemType == IT_CLA_TYPE.ITEM) {
                        cls.renders = ko.observableArray(_(cls.items)
                            .map(x => {
                                let parent = _.find(cls.items, f => f.itemCode == x.itemParentCode),
                                    childs = _.filter(cls.items, f => f.itemParentCode == x.itemCode),
                                    constraint = !childs.length &&
                                        [
                                            ITEM_SINGLE_TYPE.STRING,
                                            ITEM_SINGLE_TYPE.NUMERIC,
                                            ITEM_SINGLE_TYPE.TIME,
                                            ITEM_SINGLE_TYPE.TIMEPOINT
                                        ].indexOf((x.item || {}).dataTypeValue) > -1 &&
                                        x.itemDefId.replace(/[-_]/g, '');

                                return _.extend(x, {
                                    parent: parent,
                                    childs: childs,
                                    constraint: constraint || undefined
                                });
                            })
                            .orderBy(o => o.dispOrder)
                            .filter(x => !x.itemParentCode)
                            .value());

                        // define type of item definition
                        def_type(cls.items);
                    } else if (cls.layoutItemType == IT_CLA_TYPE.LIST) {
                        let editable = opts.sortable.isEditable() == 2,
                            rows = _.groupBy(cls.items, r => r.recordId),
                            keys = _.keys(rows),
                            renders = ko.observableArray(_.map(keys, k => ({
                                recordId: ["undefined", "null"].indexOf(k) > -1 ? undefined : k,
                                items: rows[k],
                                checked: ko.observable(false),
                                enable: ko.observable(editable)
                            }))),
                            clone = (row: any) => {
                                let recordId = 'NID_' + random(),
                                    _row = {
                                        recordId: recordId,
                                        checked: ko.observable(false),
                                        enable: ko.observable(true),
                                        items: []
                                    };

                                _row.checked.subscribe(c => {
                                    calc_data();
                                });

                                _(row.items).each(r => {
                                    let c = ko.toJS(r),
                                        _r = _.omit(c, [
                                            "readonly",
                                            "editable",
                                            "lstComboBoxValue",
                                            "numberedit",
                                            "showColor",
                                            "textValue",
                                            "value",
                                            "recordId",
                                            "checked",
                                            "defValue"
                                        ]);

                                    ko.utils.extend(_r, {
                                        checked: _row.checked,
                                        checkable: _row.enable,
                                        recordId: recordId,
                                        readonly: ko.observable(c.readonly),
                                        editable: ko.observable(c.editable),
                                        lstComboBoxValue: ko.observableArray(c.lstComboBoxValue),
                                        numberedit: ko.observable(c.numberedit),
                                        showColor: ko.observable(c.showColor),
                                        textValue: ko.observable(c.textValue),
                                        value: ko.observable(undefined),
                                        defValue: undefined
                                    });

                                    _r.value.subscribe(v => {
                                        calc_data();
                                        if (!!v) {
                                            let rids = _.map(cls.renders(), m => m.recordId);
                                            if (rids.indexOf(_row.recordId) == rids.length - 1) {
                                                clone(_row);
                                            }
                                        }
                                    });

                                    cls.items.push(_r);
                                    _row.items.push(_r);
                                });

                                row_render(_row);

                                renders.push(_row);
                            };

                        let _rows = ko.toJS(renders),
                            _row = _.last(_rows);

                        if (!editable) {
                            renders.removeAll();
                            _.each([1, 2, 3], r => {
                                clone(_row);
                            });
                        } else {
                            if (_row) {
                                if (!_row.recordId && _rows.length == 1) {
                                    renders.removeAll();
                                } else {
                                    _.each(renders(), (row, rid) => {
                                        row.checked.subscribe(c => {
                                            calc_data();
                                        });
                                        _(row.items).each(r => {
                                            ko.utils.extend(r, {
                                                checked: row.checked,
                                                checkable: row.enable
                                            });
                                        });
                                    });
                                }

                                clone(_row);
                            }
                        }

                        _.each(renders(), (row, rid) => {
                            row_render(row);
                        });

                        cls.renders = renders;
                    } else {
                        cls.renders = undefined;
                    }
                },
                row_render = (row: any) => {
                    row.renders = _(row.items).map(col => {
                        let parent = _.find(row.items, f => f.itemCode == col.itemParentCode),
                            childs = _.filter(row.items, f => f.itemParentCode == col.itemCode),
                            constraint = !childs.length &&
                                [
                                    ITEM_SINGLE_TYPE.STRING,
                                    ITEM_SINGLE_TYPE.NUMERIC,
                                    ITEM_SINGLE_TYPE.TIME,
                                    ITEM_SINGLE_TYPE.TIMEPOINT
                                ].indexOf((col.item || {}).dataTypeValue) > -1 &&
                                col.itemDefId.replace(/[-_]/g, '');

                        return _.extend(col, {
                            parent: parent,
                            childs: childs,
                            constraint: constraint || undefined
                        });
                    })
                        .orderBy(o => o.dispOrder)
                        .filter(x => !x.itemParentCode)
                        .value();

                    def_type(row.items);
                },
                scrollDown = () => {
                    // remove old selected items
                    $(ctrls.sortable)
                        .find('.form-group.item-classification')
                        .removeClass('selected');
                    // scroll to bottom
                    $(ctrls.sortable).scrollTop($(ctrls.sortable).prop("scrollHeight"));
                    // select lastest item
                    setTimeout(() => {
                        $(ctrls.sortable)
                            .find('.form-group.item-classification:last-child')
                            .addClass('selected');
                    }, 0);
                };

            // add style to <head> on first run
            if (!$('#layout_style').length) {
                $('head').append(self.style);
            }

            $element
                .append(self.tmp)
                .addClass('ntsControl layout-control');

            // binding callback function to control
            if (access.callback) {
                $.extend(opts, { callback: access.callback });
            }

            // binding output data value 
            if (access.outData) {
                $.extend(opts.sortable, { outData: access.outData });
            }

            // change color text
            if (_.has(access, "showColor")) {
                $.extend(opts.sortable, { showColor: access.showColor });
            }

            // validate editAble
            if (ko.unwrap(access.editAble) != undefined) {
                if (ko.isObservable(access.editAble)) {
                    access.editAble.subscribe(editable);
                    access.editAble.valueHasMutated();
                } else {
                    editable(access.editAble);
                }
            }

            // sortable
            opts.sortable.isEnabled.subscribe(x => {
                if (!x) {
                    $element
                        .addClass('readonly')
                        .removeClass('dragable');

                    $element.find('.left-area, .add-buttons, #cps007_btn_line').hide();
                } else {
                    $element
                        .addClass('dragable')
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
                                if (state.editable && ko.isObservable(state.editable)) {
                                    state.editable(true);
                                } else {
                                    state.editable = ko.observable(true);
                                }

                                if (state.readonly && ko.isObservable(state.readonly)) {
                                    state.readonly(false);
                                } else {
                                    state.readonly = ko.observable(false);
                                }
                            } else {
                                if (state.editable && ko.isObservable(state.editable)) {
                                    state.editable(false);
                                } else {
                                    state.editable = ko.observable(false);
                                }

                                if (state.readonly && ko.isObservable(state.readonly)) {
                                    state.readonly(true);
                                } else {
                                    state.readonly = ko.observable(true);
                                }
                            }
                            state.editable.valueHasMutated();
                        }
                    });
                });
            });
            opts.sortable.isEditable.valueHasMutated();

            // extend option
            $.extend(opts.combobox, { enable: ko.computed(() => !opts.radios.value()) });

            $.extend(opts.searchbox, {
                items: ko.computed(opts.listbox.options),
                selected: opts.listbox.value
            });

            // extend data of sortable with valueAccessor data prop
            $.extend(opts.sortable, { data: access.data });

            opts.sortable.data.subscribe((data: Array<IItemClassification>) => {
                opts.sortable.isEditable.valueHasMutated();

                _.each(data, (x, i) => {
                    x.dispOrder = i + 1;
                    x.layoutID = random();

                    if ((!_.has(x, "items") || !x.items)) {
                        if (x.layoutItemType != IT_CLA_TYPE.SPER) {
                            x.items = [];

                            if (_.has(x, "listItemDf")) {
                                _.each((x.listItemDf || []), (item, i) => {
                                    let def = _.find(x.items, (m: any) => m.itemDefId == item.id);

                                    if (!def) {
                                        def = {
                                            categoryCode: x.categoryCode || x.personInfoCategoryID, // miss categoryCode;
                                            itemCode: item.itemCode,
                                            itemName: item.itemName,
                                            itemDefId: item.id,
                                            value: undefined
                                        };

                                        x.items.push(def);
                                    }
                                    modifitem(def, item);
                                });
                            }
                        } else {
                            x.items = undefined;
                        }
                    } else {
                        switch (x.layoutItemType) {
                            case IT_CLA_TYPE.ITEM:
                            case IT_CLA_TYPE.LIST:
                                _.each(x.items, (def, i) => modifitem(def));
                                break;
                            case IT_CLA_TYPE.SPER:
                                x.items = undefined;
                                break;
                        }
                    }

                    hierarchies(x);
                });

                // clear all error on switch new layout
                clearError();

                // write date/time/timepoint 
                // primitive constraint to viewContext
                dateTimeConsts();

                // write primitive constraints to viewContext
                primitiveConsts();

                // init data for save layout
                if (ko.toJS(access.editAble) != 2) {
                    opts.sortable.outData(_(data || []).map((item, i) => {
                        return {
                            dispOrder: Number(i) + 1,
                            personInfoCategoryID: item.personInfoCategoryID,
                            layoutItemType: _(IT_CLA_TYPE).map(x => x).indexOf(item.layoutItemType),
                            listItemClsDf: _(_.map(item.listItemDf, m => m) || []).map((def, j) => {
                                return {
                                    dispOrder: Number(j) + 1,
                                    personInfoItemDefinitionID: def.id
                                };
                            }).value()
                        };
                    }).value());
                }
            });

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

                if (opts.sortable.isEditable() != 0) {
                    return;
                }

                if (mode == CAT_OR_GROUP.CATEGORY) { // get item by category
                    opts.combobox.options.removeAll();
                    services.getCats().done((data: any) => {
                        if (data && data.categoryList && data.categoryList.length) {
                            let cats = _.filter(data.categoryList, (x: IItemCategory) => !x.isAbolition && !x.categoryParentCode);

                            if (location.href.indexOf('/view/cps/007/a/') > -1) {
                                cats = _.filter(cats, (c: IItemCategory) => c.categoryCode != 'CS00069');
                            }

                            if (cats && cats.length) {
                                opts.combobox.options(cats);

                                // set first id
                                let options = ko.toJS(opts.combobox.options);

                                if (options[0]) {
                                    if (ko.toJS(opts.combobox.value) != options[0].id) {
                                        opts.combobox.value(options[0].id);
                                    } else {
                                        opts.combobox.value.valueHasMutated();
                                    }
                                } else {
                                    opts.combobox.value(undefined);
                                }
                            } else {
                                // show message if hasn't any category
                                if (ko.toJS(opts.sortable.isEnabled)) {
                                    alert({ messageId: 'Msg_288' }).then(opts.callback);
                                }
                            }
                        } else {
                            // show message if hasn't any category
                            if (ko.toJS(opts.sortable.isEnabled)) {
                                alert({ messageId: 'Msg_288' }).then(opts.callback);
                            }
                        }
                    });
                } else { // get item by group
                    // change text in add-button to [グループを追加　→]
                    $(ctrls.button).text(text('CPS007_20'));
                    services.getGroups().done((data: Array<IItemGroup>) => {
                        // prevent if slow networks
                        if (opts.radios.value() != CAT_OR_GROUP.GROUP) {
                            return;
                        }

                        if (data && data.length) {
                            // map Array<IItemGroup> to Array<IItemDefinition>
                            // 「個人情報項目定義」が取得できなかった「項目グループ」以外を、画面項目「グループ一覧」に表示する
                            // remove groups when it does not contains any item definition (by hql)
                            let _opts = _.map(data, group => {
                                return {
                                    id: group.personInfoItemGroupID,
                                    itemName: group.fieldGroupName,
                                    itemTypeState: undefined,
                                    dispOrder: group.dispOrder
                                }
                            });

                            opts.listbox.options(_opts);
                        }
                    });
                }

                // remove listbox data
                opts.listbox.value.removeAll();
            });
            opts.radios.value.valueHasMutated();

            // load listbox data
            opts.combobox.value.subscribe(cid => {
                if (opts.sortable.isEditable() != 0) {
                    return;
                }

                if (cid) {
                    let data: Array<IItemCategory> = ko.toJS(opts.combobox.options),
                        item: IItemCategory = _.find(data, x => x.id == cid);

                    // remove all item in list item for init new data
                    opts.listbox.options.removeAll();
                    if (item) {
                        switch (item.categoryType) {
                            case IT_CAT_TYPE.SINGLE:
                            case IT_CAT_TYPE.CONTINU:
                            case IT_CAT_TYPE.CONTINUWED:
                            case IT_CAT_TYPE.NODUPLICATE:
                                $(ctrls.button).text(text('CPS007_11'));
                                services.getItemByCat(item.id).done((data: Array<IItemDefinition>) => {
                                    // prevent if slow networks
                                    if (opts.radios.value() != CAT_OR_GROUP.CATEGORY) {
                                        return;
                                    }
                                    if (data && data.length) {
                                        // get all item defined in category with abolition = 0
                                        // order by dispOrder asc
                                        data = _(data)
                                            .filter(m => !m.isAbolition)
                                            .filter(f => {
                                                if (location.href.indexOf('/view/cps/007/a/') > -1) {
                                                    if (item.id === "COM1_00000000000000000000000_CS00001") {
                                                        return f.id !== "COM1_000000000000000_CS00001_IS00001";
                                                    }

                                                    if (item.id === "COM1_00000000000000000000000_CS00002") {
                                                        return f.id !== "COM1_000000000000000_CS00002_IS00003";
                                                    }

                                                    if (item.id === "COM1_00000000000000000000000_CS00003") {
                                                        return f.id !== "COM1_000000000000000_CS00003_IS00020";
                                                    }
                                                }

                                                return true;
                                            })
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
                scrollDown();
            });

            $(ctrls.sortable)
                .on('click', (evt) => {
                    setTimeout(() => {
                        $(ctrls.sortable)
                            .find('.form-group.item-classification')
                            .removeClass('selected');
                    }, 0);
                })
                .on('mouseover', '.form-group.item-classification', (evt) => {
                    $(evt.target)
                        .removeClass('selected');
                });


            $(ctrls.button).on('click', () => {
                // アルゴリズム「項目追加処理」を実行する
                // Execute the algorithm "項目追加処理"
                let ids: Array<string> = ko.toJS(opts.listbox.value);

                if (!ids || !ids.length) {
                    alert({ messageId: 'Msg_203' });
                    return;
                }

                // category mode
                if (ko.unwrap(opts.radios.value) == CAT_OR_GROUP.CATEGORY) {
                    let cid: string = ko.toJS(opts.combobox.value),
                        cats: Array<IItemCategory> = ko.toJS(opts.combobox.options),
                        cat: IItemCategory = _.find(cats, x => x.id == cid);

                    if (cat) {
                        // multiple items
                        if ([IT_CAT_TYPE.MULTI, IT_CAT_TYPE.DUPLICATE].indexOf(cat.categoryType) > -1) {
                            // 画面項目「カテゴリ選択」で選択している情報が、既に配置されているかチェックする
                            // if category is exist in sortable box.
                            let _catcls = _.find(ko.unwrap(opts.sortable.data), (x: IItemClassification) => x.personInfoCategoryID == cat.id);
                            if (_catcls) {
                                alert({
                                    messageId: 'Msg_202',
                                    messageParams: [cat.categoryName]
                                });
                                return;
                            }

                            setShared('CPS007B_PARAM', { category: cat, chooseItems: [] });
                            modal('../../007/b/index.xhtml').onClosed(() => {
                                let dfds: Array<JQueryDeferred<any>> = [],
                                    data = getShared('CPS007B_VALUE') || { category: undefined, chooseItems: [] };

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

                                            // get set item
                                            _.each(_data, x => {
                                                let dfd = $.Deferred<any>();
                                                if (x.itemTypeState.itemType == ITEM_TYPE.SET) {
                                                    services.getItemsByIds(x.itemTypeState.items).done((_items: Array<IItemDefinition>) => {
                                                        dfd.resolve([x].concat(_items));
                                                    }).fail(msg => {
                                                        dfd.resolve(x);
                                                    });
                                                } else {
                                                    dfd.resolve(x);
                                                }

                                                dfds.push(dfd);
                                            });

                                            $.when.apply($, dfds).then(function() {
                                                let args = _.flatten(arguments),
                                                    items = _(args)
                                                        .filter(x => !!x)
                                                        .map((x: IItemDefinition) => {
                                                            if (ids.indexOf(x.id) > -1) {
                                                                x.dispOrder = (ids.indexOf(x.id) + 1) * 1000;
                                                            } else {
                                                                let parent = _.find(args, p => p.itemCode == x.itemParentCode);
                                                                if (parent) {
                                                                    x.dispOrder += (ids.indexOf(parent.id) + 1) * 1000;
                                                                }
                                                            }
                                                            return x;
                                                        })
                                                        .orderBy(x => x.dispOrder)
                                                        .value(),
                                                    item: IItemClassification = {
                                                        layoutID: random(),
                                                        dispOrder: -1,
                                                        className: _cat.categoryName,
                                                        personInfoCategoryID: _cat.id,
                                                        layoutItemType: IT_CLA_TYPE.LIST,
                                                        listItemDf: items
                                                    };

                                                opts.sortable.data.push(item);
                                                opts.listbox.value.removeAll();
                                                scrollDown();
                                            });
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
                                        opts.sortable.pushAllItems(defs, false);
                                        scrollDown();
                                    }
                                });
                            }
                        }
                    }
                } else { // group mode
                    let ids: Array<string> = ko.toJS(opts.listbox.value),
                        groups: Array<any> = ko.unwrap(opts.listbox.options),
                        filters: Array<any> = _(groups)
                            .filter(x => ids.indexOf(x.id) > -1)
                            .map(x => x.id)
                            .value();

                    if (filters && filters.length) {
                        services.getItemByGroups(filters).done((defs: Array<IItemDefinition>) => {
                            if (defs && defs.length) {
                                opts.sortable.pushAllItems(defs, true);
                                scrollDown();
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
                opts: any = $element.data('options'),
                ctrls: any = $element.data('controls');

            ko.bindingHandlers['ntsFormLabel'].init(ctrls.label, () => { return {} }, allBindingsAccessor, viewModel, bindingContext);
            // init radio box group
            ko.bindingHandlers['ntsRadioBoxGroup'].init(ctrls.radios, () => opts.radios, allBindingsAccessor, viewModel, bindingContext);

            //ko.bindingHandlers['ntsComboBox'].init(ctrls.combobox, () => opts.combobox, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers['ntsDropDownList'].init(ctrls.combobox, () => ({
                value: opts.combobox.value,
                dataSource: opts.combobox.options,
                textKey: opts.combobox.optionsText,
                valueKey: opts.combobox.optionsValue,
                visibleItemsCount: 10,
                enable: opts.combobox.enable,
                columns: [
                    { prop: 'categoryCode', 'class': 'hidden' },
                    { prop: 'categoryName' }
                ]
            }), allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSearchBox'].init(ctrls.searchbox, () => opts.searchbox, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsListBox'].init(ctrls.listbox, () => opts.listbox, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSortable'].init(ctrls.sortable, () => opts.sortable, allBindingsAccessor, viewModel, bindingContext);

            // Also tell KO *not* to bind the descendants itself, otherwise they will be bound twice
            return { controlsDescendantBindings: true };
        }

        update = (element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            let $element = $(element),
                opts: any = $element.data('options'),
                ctrls: any = $element.data('controls');

            ko.bindingHandlers['ntsFormLabel'].update(ctrls.label, () => { return {} }, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsRadioBoxGroup'].update(ctrls.radios, () => opts.radios, allBindingsAccessor, viewModel, bindingContext);

            //ko.bindingHandlers['ntsComboBox'].update(ctrls.combobox, () => opts.combobox, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers['ntsDropDownList'].update(ctrls.combobox, () => ({
                value: opts.combobox.value,
                dataSource: opts.combobox.options,
                textKey: opts.combobox.optionsText,
                valueKey: opts.combobox.optionsValue,
                visibleItemsCount: 10,
                enable: opts.combobox.enable,
                columns: [
                    { prop: 'categoryCode', 'class': 'hidden' },
                    { prop: 'categoryName' }
                ]
            }), allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSearchBox'].update(ctrls.searchbox, () => opts.searchbox, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsListBox'].update(ctrls.listbox, () => opts.listbox, allBindingsAccessor, viewModel, bindingContext);

            ko.bindingHandlers['ntsSortable'].update(ctrls.sortable, () => opts.sortable, allBindingsAccessor, viewModel, bindingContext);
            // Also tell KO *not* to bind the descendants itself, otherwise they will be bound twice
            return { controlsDescendantBindings: true };
        }
    }

    interface IItemCategory {
        id: string;
        categoryCode?: string;
        categoryName: string;
        categoryType: IT_CAT_TYPE;
        isAbolition?: number;
        categoryParentCode?: string;
    }

    interface IItemGroup {
        personInfoItemGroupID: string;
        fieldGroupName: string;
        dispOrder: number;
    }

    interface IItemClassification {
        layoutID?: string;
        dispOrder?: number;
        categoryCode?: string;
        className?: string; // only for display if classification is set or duplication item
        personInfoCategoryID?: string;
        layoutItemType: IT_CLA_TYPE;
        listItemDf: Array<IItemDefinition>; // layoutItemType == 0 ? [1] : layoutItemType == 1 ? [A, B, C] : undefined;
        items?: Array<any>; // [{value: }] || [{c: 1, value: }, {c: 2, value: }], [[{r: 1, c: 1, value: }, {}], [{}, {}], [{}, {}], [{}, {}]] , undefined
        renders?: KnockoutObservableArray<any>;
    }

    interface IItemDefinition {
        id: string;
        dispOrder?: number;
        perInfoCtgId?: string;
        itemCode?: string;
        itemParentCode?: string;
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

    interface IItemDefinitionData extends IItemTime, IItemDate, IItemString, IItemTimePoint, IItemNumeric, IItemSelection {
        dataTypeValue: ITEM_SINGLE_TYPE; // type of value of item
        editable?: KnockoutObservable<boolean>;
        readonly?: KnockoutObservable<boolean>;
    }

    interface IItemTime {
        min?: number;
        max?: number;
    }

    interface IItemDate {
        dateItemType?: DateType;
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
        numericItemMin?: number;
        numericItemMax?: number;
    }

    interface IItemSelection extends IItemMasterSelection, IItemEnumSelection, IItemCodeNameSelection {
        referenceType?: ITEM_SELECT_TYPE;
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
        ITEM = <any>"ITEM", // single item
        LIST = <any>"LIST", // list item
        SPER = <any>"SeparatorLine" // line item
    }

    // define ITEM_CATEGORY_TYPE
    enum IT_CAT_TYPE {
        SINGLE = 1, // Single info
        MULTI = 2, // Multi info
        CONTINU = 3, // Continuos history
        NODUPLICATE = 4, //No duplicate history
        DUPLICATE = 5, // Duplicate history
        CONTINUWED = 6 // Continuos history with end date
    }

    // defined CATEGORY or GROUP mode
    enum CAT_OR_GROUP {
        CATEGORY = 0, // category mode
        GROUP = 1 // group mode
    }

    // define ITEM_TYPE is set or single item
    enum ITEM_TYPE {
        SET = 1, // List item info
        SINGLE = 2, // Single item info
        SET_TABLE = 3
    }

    // define ITEM_SINGLE_TYPE
    // type of item if it's single item
    enum ITEM_SINGLE_TYPE {
        STRING = 1,
        NUMERIC = 2,
        DATE = 3,
        TIME = 4,
        TIMEPOINT = 5,
        SELECTION = 6,
        SEL_RADIO = 7,
        SEL_BUTTON = 8,
        READONLY = 9,
        RELATE_CATEGORY = 10,
        NUMBERIC_BUTTON = 11,
        READONLY_BUTTON = 12
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
        KANA = 5,
        // 6: カードNO
        CARDNO = 6
    }

    // define ITEM_SELECT_TYPE
    // type of item if it's selection item
    enum ITEM_SELECT_TYPE {
        // 1:専用マスタ(DesignatedMaster)
        DESIGNATED_MASTER = <any>"DESIGNATED_MASTER",
        // 2:コード名称(CodeName)
        CODE_NAME = <any>"CODE_NAME",
        // 3:列挙型(Enum)
        ENUM = <any>"ENUM"
    }

    enum DateType {
        YEARMONTHDAY = 1,
        YEARMONTH = 2,
        YEAR = 3
    }

    enum ACTION_ROLE {
        HIDDEN = <any>"HIDDEN",
        VIEW_ONLY = <any>"VIEW_ONLY",
        EDIT = <any>"EDIT"
    }

    enum DISP_TYPE {
        SINGLE = <any>"SINGLE",
        SET_TABLE = <any>"SET_TABLE",
        SET_INLINE = <any>"SET_INLINE",
        SET_MULTILINE = <any>"SET_MULTILINE",
        SET_MULTILINE_W_RADIO = <any>"SET_MULTILINE_W_RADIO",
        SET_MULTILINE_W_TITLE = <any>"SET_MULTILINE_W_TITLE"
    }
}

let scripts = $("script[src*='cbx-control-ko-ext.js']");
if (scripts.length == 0) {
    $('head').append($('<script>', {
        'type': 'text/javascript',
        'src': '/nts.uk.com.web/view/cps/shr/cbx-control-ko-ext.js'
    }));
}

ko.bindingHandlers["ntsProp"] = new nts.custombinding.PropControl();
ko.bindingHandlers["ntsLayoutControl"] = new nts.custombinding.LayoutControl();
