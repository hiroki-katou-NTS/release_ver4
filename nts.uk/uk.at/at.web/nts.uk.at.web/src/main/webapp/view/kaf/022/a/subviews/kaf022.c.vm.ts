import text = nts.uk.resource.getText;

ko.components.register('kaf022-c', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListC27: ko.observableArray([
                    { code: 1, name: text('KAF022_100') },
                    { code: 0, name: text('KAF022_101') },
                    { code: 2, name: text('KAF022_171') }
                ]),
                itemListC51: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                ]),
                itemListC48: ko.observableArray([
                    { code: 1, name: text('KAF022_420') },
                    { code: 0, name: text('KAF022_421') }
                ]),
                itemListC29: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') },
                    { code: 2, name: text('KAF022_175') },
                ]),
                itemListC30: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_175') },
                ]),
            };

            _.extend(vm, {
                    selectedIdC51: params.selectedIdC51,
                    selectedIdC48: params.selectedIdC48,
                    selectedIdC38: params.selectedIdC38,
                    selectedIdC39: params.selectedIdC39,
                    selectedIdC40: params.selectedIdC40,
                    selectedIdC27: params.selectedIdC27,
                    selectedIdC28: params.selectedIdC28,
                    selectedIdC182: params.selectedIdC182,
                    
                    selectedIdC29: params.selectedIdC29,
                    selectedIdC30: params.selectedIdC30,
                    selectedIdC31: params.selectedIdC31,
                    selectedIdC32: params.selectedIdC32,
                    selectedIdC33: params.selectedIdC33,
                    selectedIdC34: params.selectedIdC34,
                    selectedIdC35: params.selectedIdC35,
                    selectedIdC36: params.selectedIdC36,
                    selectedIdC37: params.selectedIdC37,
                    selectedIdC49: params.selectedIdC49,
                    
                    texteditorC41: params.texteditorC41,
                    texteditorC42: params.texteditorC42,
                    texteditorC43: params.texteditorC43,
                    texteditorC44: params.texteditorC44,
                    texteditorC45: params.texteditorC45,
                    texteditorC46: params.texteditorC46,
                    texteditorC47: params.texteditorC47,
                    texteditorC51: params.texteditorC51,
            });

//            _.extend(vm.texteditorH22, {
//                value: params.texteditorH22.value
//            });


            return vm;
        }
    },
    template: `
    <!-- ko let: { $cv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container" style="width: 900px">
        <table id="fixed-table-c1">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind = "text: text('KAF022_148')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="c3" data-bind = "text: text('KAF022_149')"></td>
                    <td style="width: 650px">
                        <div id="c27" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC27,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC27,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c50" data-bind = "text: text('KAF022_401')"></td>
                    <td>
                        <div class="blockrow" id="c51" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC51,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c25" data-bind = "text: text('KAF022_168')"></td>
                    <td>
                        <div class="blockdeki" id="c48" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC48,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC48,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c15" data-bind = "text: text('KAF022_158')"></td>
                    <td>
                        <div class="blockrow" id="c38" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC38,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c16" data-bind = "text: text('KAF022_159')"></td>
                    <td>
                        <div class="blockrow" id="c39" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC39,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c17" data-bind = "text: text('KAF022_160')"></td>
                    <td>
                        <div class="blockrow" id="c40" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC40,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="c181" data-bind = "text: text('KAF022_661')"></td>
                    <td>
                        <div class="blockrow" id="c182" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC182,
                            enable: true}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-c2">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind = "text: text('KAF022_142')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="c4" data-bind = "text: text('KAF022_150')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="c28" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC51,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC28,
                            enable: false}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-c3">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind = "text: text('KAF022_143')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="c5" data-bind = "text: text('KAF022_145')"></td>
                    <td style="width: 650px">
                        <div class = "radio3Box" id="c29" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC29,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC29,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c6" data-bind = "text: text('KAF022_152')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c30" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC30,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c7" data-bind = "text: text('KAF022_153')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c31" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC31,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c8" data-bind = "text: text('KAF022_154')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c32" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC32,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c9" data-bind = "text: text('KAF022_155')"></td>
                    <td style="width: 650px">
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c10" data-bind = "text: text('KAF022_156')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c33" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC33,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c11" data-bind = "text: text('KAF022_48')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c34" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC34,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c12" data-bind = "text: text('KAF022_51')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c35" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC35,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c13" data-bind = "text: text('KAF022_157')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c36" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC36,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c14" data-bind = "text: text('KAF022_54')"></td>
                    <td style="width: 650px">
                        <div class= "radioboxrow" id="c37" data-bind="ntsRadioBoxGroup: {  
                            options: $cv.itemListC30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC37,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c26" data-bind = "text: text('KAF022_169')"></td>
                    <td style="width: 650px">
                        <div id="c49" data-bind="ntsRadioBoxGroup: {
                            options: $cv.itemListC29,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $cv.selectedIdC49,
                            enable: true}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-c2">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind = "text: text('KAF022_144')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="c18">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_161') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c41" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC41.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />   
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c19">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_162') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c42" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC42.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c20">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_163') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c43" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC43.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c21">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_164') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c44" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC44.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c22">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_165') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c45" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC45.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c23">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_166') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c46" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC46.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: false,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c24">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_167') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c47" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC47.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: false,
                            readonly: false
                        }" />
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="c50">
                        <div data-bind="ntsFormLabel: { constraint: 'ObstacleName', inline: true, text: text('KAF022_364') }"></div>
                    </td>
                    <td style="width: 650px">
                        <input id="c51" data-bind="ntsTextEditor: {
                            name: text('KAF022_177'),
                            value: $cv.texteditorC51.value,
                            constraint: 'ObstacleName',
                            required: false,
                            enable: true,
                            readonly: false
                        }" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
    `
});