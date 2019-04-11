import text = nts.uk.resource.getText;

ko.components.register('kaf022-f', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListE9: ko.observableArray([
                    { code: 0, name: text('KAF022_195') },
                    { code: 1, name: text('KAF022_196') }
                ]),
                itemListF11: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                ]),
                itemListE10: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 2, name: text('KAF022_174') },
                    { code: 1, name: text('KAF022_175') }
                ]),
                itemListE12: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 2, name: text('KAF022_174') },
                    { code: 1, name: text('KAF022_175') }
                ]),
                itemListF12: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 2, name: text('KAF022_174') },
                    { code: 1, name: text('KAF022_175') }
                ])
            };

            _.extend(vm, {
                selectedIdF10: params.selectedIdF10 || ko.observable(),
                selectedIdF11: params.selectedIdF11 || ko.observable(),
                selectedIdF12: params.selectedIdF12 || ko.observable(),
                selectedValueF13: params.selectedValueF13 || ko.observable(0),
                checkedF13_1: params.checkedF13_1 || ko.observable(false),
                enableF13_1: params.enableF13_1 || ko.observable(true),
                selectedIdF14: params.selectedIdF14 || ko.observable(0),
                texteditorF15: params.texteditorF15 || { value: ko.observable() },
                texteditorF16: params.texteditorF16 || { value: ko.observable() }
            });

            return vm;
        }
    }, template: `
    <!-- ko let: { $fv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container">
        <table id="fixed-table-f">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_203')"></th>
                </tr>
            </thead>
            <tbody style="max-height: 536px;">
                <tr>
                    <td style="width: 250px" class="color-header" id="f3" data-bind="text: text('KAF022_204')"></td>
                    <td style="width: 800px" class="e11-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListE9,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdF10,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="f6" data-bind="text: text('KAF022_192')"></td>
                    <td>
                        <div tabindex="4">
                            <div class="ntsControl blockrow">
                                <label  class="ntsRadioBox" >
                                    <input type="radio" name="textRadioF" data-bind="
                                            checkedValue: 1,
                                            checked: selectedValueF13,
                                            enable: true
                                        " />
                                    <span class="box"></span>
                                    <span class="label" data-bind="text: text('KAF022_198')"></span>
                                </label>
                            </div>
                            <div class="ntsControl blockrow">
                                <label  class="ntsRadioBox">
                                    <input type="radio" name="textRadioF" data-bind="
                                            checkedValue: 0, 
                                            checked: selectedValueF13,
                                            enable: true
                                        " />
                                    <span class="box"></span>
                                    <span class="label" data-bind="text: text('KAF022_199')"></span>
                                </label>
                            </div>
                            <div class="ntsControl blockrow">
                                <label class="ntsRadioBox">
                                    <input type="radio" name="textRadioF" data-bind="
                                              checkedValue: 3,
                                              checked: selectedValueF13,
                                              enable: true
                                          " />
                                    <span class="box"></span>
                                    <span class="label" data-bind="text: text('KAF022_200')"></span>
                                </label>
                            </div>
                            
                            <div class="ntsControl blockrow">
                                <label class="ntsRadioBox">
                                    <input type="radio" name="textRadioF" data-bind="
                                               checkedValue: 2,
                                               checked: selectedValueF13,
                                               enable: true
                                            " />
                                    <span class="box"></span>
                                    <span class="label" data-bind="text: text('KAF022_201')"></span>
                                </label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="f8">
                        <div data-bind="ntsFormLabel: { 
                                constraint: 'Comment',
                                inline: true,
                                text: text('KAF022_179')
                        }"></div>
                    </td>
                    <td>
                        <input tabindex="7" data-bind="ntsTextEditor: {
                                name: text('KAF022_179'),
                                value: $fv.texteditorF15.value,
                                constraint: 'Comment',
                                required: false,
                                enable: true,
                                readonly: false
                            }" />
                        <div tabindex="8" id="colorpicker" data-bind="ntsColorPicker: {
                                value: $parent.valueF15_1, 
                                enable: true, width: 40
                            }"/>
                        <div tabindex="9" data-bind="ntsCheckBox: {
                                checked: $parent.enableF15_2,
                                text: text('KAF022_185')
                            }"></div>
                    </td>
                </tr>
                <tr>
                    <td style="border-bottom: none" class="color-header" id="f9">
                        <div data-bind="ntsFormLabel: {
                                constraint: 'Comment',
                                inline: true,
                                text: text('KAF022_180')
                            }"></div>
                    </td>
                    <td style="border-bottom: none">
                        <input tabindex="10" data-bind="ntsTextEditor: {
                                name: text('KAF022_180'),
                                value: $fv.texteditorF16.value,
                                constraint: 'Comment',
                                required: false,
                                enable: true,
                                readonly: false
                            }" />
                        <div tabindex="11" id="colorpicker" data-bind="ntsColorPicker: {
                                value: $parent.valueF16_1,
                                enable: true,
                                width: 40
                            }"/>
                        <div tabindex="12" data-bind="ntsCheckBox: {
                                checked: $parent.enableF16_1,
                                text: text('KAF022_185')
                            }"></div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-f2">
            <thead>
                <tr>
                    <th class="color-header" id="f4" colspan="2" data-bind = "text: text('KAF022_142')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="f11" data-bind = "text: text('KAF022_424')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="f11" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $fv.itemListF11,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $fv.selectedIdF11,
                            enable: false}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-f2">
            <thead>
                <tr>
                    <th class="color-header" id="f4" colspan="2" data-bind = "text: text('KAF022_143')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="f5" data-bind = "text: text('KAF022_394')"></td>
                    <td style="width: 650px">
                        <div class="radio3Box" id="f12" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $fv.itemListF12,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $fv.selectedIdF12,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="f7" data-bind = "text: text('KAF022_193')"></td>
                    <td style="width: 650px">
                        <div class = "radio3Box" id="f14" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $fv.itemListF12,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $fv.selectedIdF14,
                            enable: false}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
`
});