import text = nts.uk.resource.getText;

ko.components.register('kaf022-r', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListR1_4: ko.observableArray([
                    { code: 1, name: text('KAF022_44') },
                    { code: 0, name: text('KAF022_396') }
                ]),
                itemListR1_7: ko.observableArray([
                    { code: 1, name: text('KAF022_407') },
                    { code: 2, name: text('KAF022_408') },
                    { code: 0, name: text('KAF022_409') }

                ]),
                itemListR1_9: ko.observableArray([
                    { code: 0, name: text('KAF022_42') },
                    { code: 1, name: text('KAF022_43') },
                    { code: 2, name: text('KAF022_44') }
                ]),
                // xem lại với r2_11
                itemListR2_8: ko.observableArray([
                    { code: 1, name: text('KAF022_44') },
                    { code: 0, name: text('KAF022_396') }
                ]),
                itemListR3_4: ko.observableArray([
                    { code: 1, name: text('KAF022_416') },
                    { code: 0, name: text('KAF022_409') }

                ]),
                itemListR3_6: ko.observableArray([
                    { code: 1, name: text('KAF022_414') },
                    { code: 0, name: text('KAF022_85') }
                ]),
                itemListR3_8: ko.observableArray([
                    { code: 0, name: text('KAF022_84') },
                    { code: 1, name: text('KAF022_85') }
                ]),
                itemListR3_10: ko.observableArray([
                    { code: 0, name: text('KAF022_141') },
                    { code: 1, name: text('KAF022_396') }
                ]),
            };

            _.extend(vm, {
                selectedIdR1_4: params.selectedIdR1_4,
                selectedIdR1_7: params.selectedIdR1_7,
                selectedIdR1_9: params.selectedIdR1_9,
                selectedIdR1_11: params.selectedIdR1_11,
                selectedIdR2_8: params.selectedIdR2_8,
                selectedIdR2_11: params.selectedIdR2_11,
                selectedIdR2_15: params.selectedIdR2_15,
                selectedIdR2_18: params.selectedIdR2_18,
                selectedIdR2_21: params.selectedIdR2_21,
                itemListR2_22: params.itemListR2_22,
                selectedCodeR2_22: params.selectedCodeR2_22,
                selectedIdR3_4: params.selectedIdR3_4,
                selectedIdR3_6: params.selectedIdR3_6,
                selectedIdR3_8: params.selectedIdR3_8,
                selectedIdR3_10: params.selectedIdR3_10,
                selectedIdR4_6: params.selectedIdR4_6,
                selectedIdR4_10: params.selectedIdR4_10,
                selectedIdR4_13: params.selectedIdR4_13,
            });

            return vm;
        }
    },
    template: `
    <!-- ko let: { $rv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container" style = "width: 900px">
        <table id="fixed-table-r">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_77')"></th>
                </tr>
            </thead>
            <tbody style="max-height: 536px;">
                <tr>
                    <td style="width: 300px" class="color-header" id="r1_3" data-bind="text: text('KAF022_79')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_4,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR1_4,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="r1_5" data-bind="text: text('KAF022_405')"></td>  
                    <td style="width: 800px" class="k14-radio_1">
                        <div id="r1_6" data-bind="text: text('KAF022_406')"></div>
                        <div tabindex="12" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_7,   
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR1_7,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="r1_8" data-bind="text: text('KAF022_33')"></td>
                    <td style="width: 800px" class="k14-radio_1">
                        <div tabindex="12" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_9,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR1_9,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="r1_10" data-bind="text: text('KAF022_34')"></td>
                    <td style="width: 800px" class="k14-radio_1">
                        <div tabindex="12" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_9,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR1_11,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style = "width: 900px; margin-top: 20px;">
        <table id="fixed-table-k2">
            <thead>
                <tr>
                    <th class="color-header" id="r2_2" colspan="2" data-bind = "text: text('KAF022_410')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_5" data-bind = "text: text('KAF022_122')"></td>
                    <td style="width: 700px" class="k13-radio_1">
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_6" data-bind = "text: text('KAF022_123')"></td>
                    <td style="width: 700px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR2_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR2_8,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_9" data-bind = "text: text('KAF022_124')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR2_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR2_11,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_12" data-bind = "text: text('KAF022_125')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="r2_13" data-bind = "text: text('KAF022_126')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR2_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR2_15,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_16" data-bind = "text: text('KAF022_127')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR2_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR2_18,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r2_19" data-bind = "text: text('KAF022_128')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR2_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR2_21,
                                enable: false
                            }">
                        </div>
                        <div tabindex="17" id="r2_22"
                                            data-bind="ntsComboBox: {
                                            options: itemListR2_22,
                                            optionsValue: 'code',
                                            visibleItemsCount: 8,
                                            value: selectedCodeR2_22,
                                            optionsText: 'name',
                                            editable: false,
                                            enable: false,
                                            columns: [
                                                { prop: 'name', length: 2 },
                                            ]}"></div>
                    </td>
                </tr>
            </tbody>
        </table>   
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-k3">
            <thead>
                <tr>
                    <th class="color-header" id="r3_2" colspan="2" data-bind = "text: text('KAF022_412')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 300px" class="color-header" id="r3_3" data-bind = "text: text('KAF022_415')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR3_4,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR3_4,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r3_5" data-bind = "text: text('KAF022_413')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR3_6,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR3_6,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r3_7" data-bind = "text: text('KAF022_80')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR3_8,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR3_8,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r3_7" data-bind = "text: text('KAF022_78')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div class = "blockrowr310" tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR3_10,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR3_10,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-k4">
            <thead>
                <tr>
                    <th class="color-header" id="r4_2" colspan="2" data-bind = "text: text('KAF022_417')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 300px" class="color-header" id="r4_3" data-bind = "text: text('KAF022_208')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r4_4" data-bind = "text: text('KAF022_209')"></td>
                    <td style="width: 600px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_4,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR4_6,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r4_7" data-bind = "text: text('KAF022_210')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r4_8" data-bind = "text: text('KAF022_211')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_4,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR4_10,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 300px" class="color-header" id="r4_11" data-bind = "text: text('KAF022_212')"></td>
                    <td style="width: 800px" class="k13-radio_1">
                        <div tabindex="4" data-bind="ntsRadioBoxGroup: {
                                options: itemListR1_4,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdR4_13,
                                enable: false
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