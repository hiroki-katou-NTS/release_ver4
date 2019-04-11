import text = nts.uk.resource.getText;

ko.components.register('kaf022-k', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListK12: ko.observableArray([
                    { code: 1, name: text('KAF022_100') },
                    { code: 0, name: text('KAF022_101') },
                    { code: 2, name: text('KAF022_270') }
                ]),
                itemListK13: ko.observableArray([
                    { code: 1, name: text('KAF022_292') },
                    { code: 0, name: text('KAF022_291') }
                ]),
                itemListK15: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                ]),
                itemListK14: ko.observableArray([
                    { code: 1, name: text('KAF022_272') },
                    { code: 0, name: text('KAF022_273') }
                ]),
                itemListK21: ko.observableArray([
                    { code: 1, name: text('KAF022_420') },
                    { code: 0, name: text('KAF022_421') }
                ]),
                
                itemListK16: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_175') }
                ]),
                itemListK22: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') },
                    { code: 2, name: text('KAF022_175') }
                ]),
            };

            _.extend(vm, {
                selectedIdK12: params.selectedIdK12,
                selectedIdK21: params.selectedIdK21,
                texteditorK17: params.texteditorK17,
                texteditorK20: params.texteditorK20,
                valueK18: params.valueK18,
                enableK19: params.enableK19,
                valueK23: params.valueK23,
                enableK24: params.enableK24,
                selectedIdK15: params.selectedIdK15,
                selectedIdK13: params.selectedIdK13,
                selectedIdK22: params.selectedIdK22,
                selectedIdK16: params.selectedIdK16,
                selectedIdK14: params.selectedIdK14,
            });

            return vm;
        }
    },
    template: `
    <!-- ko let: { $kv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container">
        <table id="fixed-table-k">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_259')"></th>
                </tr>
            </thead>
            <tbody style="max-height: 536px;">
                <tr>
                    <td style="width: 250px" class="color-header" id="k3" data-bind="text: text('KAF022_260')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListK12,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK12,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="k10" data-bind="text: text('KAF022_267')"></td>
                    <td class="k14-radio_1">
                        <div tabindex="12" data-bind="ntsRadioBoxGroup: {
                                options: itemListK21,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK21,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="k8">
                        <div data-bind="ntsFormLabel: {
                                    constraint: 'Comment', 
                                    inline: true,
                                    text: text('KAF022_265')
                                }"></div>
                    </td>
                    <td>
                        <input tabindex="6" data-bind="ntsTextEditor: {
                                name: text('KAF022_265'),
                                value: texteditorK17.value,
                                constraint: 'Comment',
                                required: false,
                                enable: true,
                                readonly: false
                            }" />
                        <div tabindex="7" id="colorpicker" data-bind="ntsColorPicker: {
                                value: $kv.valueK18, 
                                enable: true,
                                width: 40
                            }" />
                        <div tabindex="8" data-bind="ntsCheckBox: { 
                                checked: $kv.enableK19,
                                text: text('KAF022_185')
                            }"></div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="k9">
                        <div data-bind="ntsFormLabel: {
                                    constraint: 'Comment',
                                    inline: true,  
                                    text: text('KAF022_266')
                                }"></div>
                    </td>
                    <td>
                        <input tabindex="9" data-bind="ntsTextEditor: {
                                name: text('KAF022_266'),
                                value: texteditorK20.value,
                                constraint: 'Comment',
                                required: false,
                                enable: true,
                                readonly: false
                            }" />
                        <div tabindex="10" id="colorpicker" data-bind="ntsColorPicker: {
                                value: $kv.valueK23,
                                enable: true, width: 40
                            }" />
                        <div tabindex="11" data-bind="ntsCheckBox: { 
                                checked: $kv.enableK24,
                                text: text('KAF022_185')
                            }"></div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-k2">
            <thead>
                <tr>
                    <th class="color-header" id="k23" colspan="2" data-bind = "text: text('KAF022_142')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="k6" data-bind = "text: text('KAF022_263')"></td>
                    <td class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListK15,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK15,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-k2">
            <thead>
                <tr>
                    <th class="color-header" id="k24" colspan="2" data-bind = "text: text('KAF022_143')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="k4" data-bind = "text: text('KAF022_261')"></td>
                    <td class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListK13,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK13,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="k11" data-bind = "text: text('KAF022_394')"></td>
                    <td class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListK22,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK22,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="k7" data-bind = "text: text('KAF022_264')"></td>
                    <td class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListK16,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK16,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 270px" class="color-header" id="k5" data-bind = "text: text('KAF022_262')"></td>
                    <td class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListK14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdK14,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
    `
});