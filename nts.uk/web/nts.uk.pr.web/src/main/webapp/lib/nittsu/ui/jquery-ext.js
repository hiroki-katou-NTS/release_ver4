var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var jqueryExtentions;
            (function (jqueryExtentions) {
                var ntsError;
                (function (ntsError) {
                    var DATA_HAS_ERROR = 'hasError';
                    $.fn.ntsError = function (action, message) {
                        var $control = $(this);
                        if (action === DATA_HAS_ERROR) {
                            return _.some($control, function (c) { return hasError($(c)); });
                        }
                        else {
                            $control.each(function (index) {
                                var $item = $(this);
                                $item = processErrorOnItem($item, message, action);
                            });
                            return $control;
                        }
                    };
                    //function for set and clear error
                    function processErrorOnItem($control, message, action) {
                        switch (action) {
                            case 'set':
                                return setError($control, message);
                            case 'clear':
                                return clearErrors($control);
                        }
                    }
                    function setError($control, message) {
                        $control.data(DATA_HAS_ERROR, true);
                        ui.errors.add({
                            location: $control.data('name') || "",
                            message: message,
                            $control: $control
                        });
                        $control.parent().addClass('error');
                        return $control;
                    }
                    function clearErrors($control) {
                        $control.data(DATA_HAS_ERROR, false);
                        ui.errors.removeByElement($control);
                        $control.parent().removeClass('error');
                        return $control;
                    }
                    function hasError($control) {
                        return $control.data(DATA_HAS_ERROR) === true;
                    }
                })(ntsError || (ntsError = {}));
                var ntsPopup;
                (function (ntsPopup) {
                    var DATA_INSTANCE_NAME = 'nts-popup-panel';
                    $.fn.ntsPopup = function () {
                        if (arguments.length === 1) {
                            var p = arguments[0];
                            if (_.isPlainObject(p)) {
                                return init.apply(this, arguments);
                            }
                        }
                        if (typeof arguments[0] === 'string') {
                            return handleMethod.apply(this, arguments);
                        }
                    };
                    function init(param) {
                        var popup = new NtsPopupPanel($(this), param.position);
                        var dismissible = param.dismissible === false;
                        _.defer(function () {
                            if (!dismissible) {
                                $(window).mousedown(function (e) {
                                    //console.log(dismissible);
                                    if ($(e.target).closest(popup.$panel).length === 0) {
                                        popup.hide();
                                    }
                                });
                            }
                        });
                        return popup.$panel;
                    }
                    function handleMethod() {
                        var methodName = arguments[0];
                        var popup = $(this).data(DATA_INSTANCE_NAME);
                        switch (methodName) {
                            case 'show':
                                popup.show();
                                break;
                            case 'hide':
                                popup.hide();
                                break;
                            case 'destroy':
                                popup.hide();
                                popup.destroy();
                                break;
                            case 'toggle':
                                popup.toggle();
                                break;
                        }
                    }
                    var NtsPopupPanel = (function () {
                        function NtsPopupPanel($panel, position) {
                            this.position = position;
                            var parent = $panel.parent();
                            this.$panel = $panel
                                .data(DATA_INSTANCE_NAME, this)
                                .addClass('popup-panel')
                                .appendTo(parent);
                            this.$panel.css("z-index", 100);
                        }
                        NtsPopupPanel.prototype.show = function () {
                            this.$panel
                                .css({
                                visibility: 'hidden',
                                display: 'block'
                            })
                                .position(this.position)
                                .css({
                                visibility: 'visible'
                            });
                        };
                        NtsPopupPanel.prototype.hide = function () {
                            this.$panel.css({
                                display: 'none'
                            });
                        };
                        NtsPopupPanel.prototype.destroy = function () {
                            this.$panel = null;
                        };
                        NtsPopupPanel.prototype.toggle = function () {
                            var isDisplaying = this.$panel.css("display");
                            if (isDisplaying === 'none') {
                                this.show();
                            }
                            else {
                                this.hide();
                            }
                        };
                        return NtsPopupPanel;
                    }());
                })(ntsPopup || (ntsPopup = {}));
                var ntsGridList;
                (function (ntsGridList) {
                    $.fn.ntsGridList = function (action, param) {
                        var $grid = $(this);
                        switch (action) {
                            case 'setupSelecting':
                                return setupSelecting($grid);
                            case 'getSelected':
                                return getSelected($grid);
                            case 'setSelected':
                                return setSelected($grid, param);
                            case 'deselectAll':
                                return deselectAll($grid);
                        }
                    };
                    function getSelected($grid) {
                        if ($grid.igGridSelection('option', 'multipleSelection')) {
                            var selectedRows = $grid.igGridSelection('selectedRows');
                            if (selectedRows)
                                return _.map(selectedRows, convertSelected);
                            return [];
                        }
                        else {
                            var selectedRow = $grid.igGridSelection('selectedRow');
                            if (selectedRow)
                                return convertSelected(selectedRow);
                            return undefined;
                        }
                    }
                    function convertSelected(igGridSelectedRow) {
                        return {
                            id: igGridSelectedRow.id,
                            index: igGridSelectedRow.index
                        };
                    }
                    function setSelected($grid, selectedId) {
                        deselectAll($grid);
                        if ($grid.igGridSelection('option', 'multipleSelection')) {
                            selectedId.forEach(function (id) { return $grid.igGridSelection('selectRowById', id); });
                        }
                        else {
                            $grid.igGridSelection('selectRowById', selectedId);
                        }
                    }
                    function deselectAll($grid) {
                        $grid.igGridSelection('clearSelection');
                    }
                    function setupSelecting($grid) {
                        setupDragging($grid);
                        setupSelectingEvents($grid);
                        return $grid;
                    }
                    // this code was provided by Infragistics support
                    function setupDragging($grid) {
                        var dragSelectRange = [];
                        $grid.on('mousedown', function (e) {
                            // グリッド内がマウスダウンされていない場合は処理なしで終了
                            if ($(e.target).closest('.ui-iggrid-table').length === 0) {
                                return;
                            }
                            // http://jp.igniteui.com/help/api/2016.2/ui.iggrid#methods:getElementInfo
                            var trInfo = $grid.igGrid('getElementInfo', $(e.target).closest('tr'));
                            // ドラッグ開始位置を設定する
                            dragSelectRange.push(trInfo.rowIndex);
                        });
                        $grid.on('mousemove', function (e) {
                            // ドラッグ開始位置が設定されていない場合は処理なしで終了
                            if (dragSelectRange.length === 0) {
                                return;
                            }
                            var $tr = $(e.target).closest('tr'), 
                            // http://jp.igniteui.com/help/api/2016.2/ui.iggrid#methods:getElementInfo
                            trInfo = $grid.igGrid('getElementInfo', $tr);
                            // 無駄な処理をさせないためにドラッグ終了位置が同じかどうかをチェックする
                            if (trInfo.rowIndex === dragSelectRange[dragSelectRange.length - 1]) {
                                return;
                            }
                            // 新たにドラッグ選択を開始する場合、Ctrlキー押下されていない場合は以前の選択行を全てクリアする
                            if (dragSelectRange.length === 1 && !e.ctrlKey) {
                                $grid.igGridSelection('clearSelection');
                            }
                            // 以前のドラッグ範囲の選択を一旦解除する
                            for (var i = 0, i_len = dragSelectRange.length; i < i_len; i++) {
                                // http://jp.igniteui.com/help/api/2016.2/ui.iggridselection#methods:deselectRow
                                $grid.igGridSelection('deselectRow', dragSelectRange[i]);
                            }
                            var newDragSelectRange = [];
                            if (dragSelectRange[0] <= trInfo.rowIndex) {
                                for (var j = dragSelectRange[0]; j <= trInfo.rowIndex; j++) {
                                    // http://jp.igniteui.com/help/api/2016.2/ui.iggridselection#methods:selectRow
                                    $grid.igGridSelection('selectRow', j);
                                    newDragSelectRange.push(j);
                                }
                            }
                            else if (dragSelectRange[0] > trInfo.rowIndex) {
                                for (var j = dragSelectRange[0]; j >= trInfo.rowIndex; j--) {
                                    $grid.igGridSelection('selectRow', j);
                                    newDragSelectRange.push(j);
                                }
                            }
                            dragSelectRange = newDragSelectRange;
                        });
                        $grid.on('mouseup', function (e) {
                            // ドラッグを終了する
                            dragSelectRange = [];
                        });
                    }
                    function setupSelectingEvents($grid) {
                        $grid.bind('iggridselectioncellselectionchanging', function () {
                        });
                        $grid.bind('iggridselectionrowselectionchanged', function () {
                            $grid.triggerHandler('selectionchanged');
                        });
                        $grid.on('mouseup', function () {
                            $grid.triggerHandler('selectionchanged');
                        });
                    }
                })(ntsGridList || (ntsGridList = {}));
                var ntsListBox;
                (function (ntsListBox) {
                    $.fn.ntsListBox = function (action) {
                        var $grid = $(this);
                        switch (action) {
                            case 'deselectAll':
                                deselectAll($grid);
                                break;
                            case 'selectAll':
                                selectAll($grid);
                                break;
                            case 'validate':
                                return validate($grid);
                            default:
                                break;
                        }
                    };
                    function selectAll($list) {
                        $list.find('.nts-list-box > li').addClass("ui-selected");
                        $list.find("li").attr("clicked", "");
                        $list.find('.nts-list-box').data("ui-selectable")._mouseStop(null);
                    }
                    function deselectAll($list) {
                        $list.data('value', '');
                        $list.find('.nts-list-box > li').removeClass("ui-selected");
                        $list.find('.nts-list-box > li > div').removeClass("ui-selected");
                        $list.trigger("selectionChange");
                    }
                    function validate($list) {
                        var required = $list.data('required');
                        var $currentListBox = $list.find('.nts-list-box');
                        if (required) {
                            var itemsSelected = $list.data('value');
                            if (itemsSelected === undefined || itemsSelected === null || itemsSelected.length == 0) {
                                $currentListBox.ntsError('set', 'at least 1 item selection required');
                                return false;
                            }
                            else {
                                $currentListBox.ntsError('clear');
                                return true;
                            }
                        }
                    }
                })(ntsListBox || (ntsListBox = {}));
                var userGuide;
                (function (userGuide) {
                    $.fn.ntsUserGuide = function (action) {
                        var $controls = $(this);
                        if (nts.uk.util.isNullOrUndefined(action)) {
                            return init($controls);
                        }
                        else if (action === "show") {
                            return show($controls);
                        }
                        else {
                        }
                        ;
                    };
                    function init(controls) {
                        controls.each(function () {
                            // UserGuide container
                            var $control = $(this);
                            $control.remove();
                            if (!$control.hasClass("ntsUserGuide"))
                                $control.addClass("ntsUserGuide");
                            $($control).appendTo($("body")).show();
                            var target = $control.data('target');
                            var direction = $control.data('direction');
                            // Userguide Information Box
                            $control.children().each(function () {
                                var $box = $(this);
                                var boxDirection = $box.data("direction");
                                $box.addClass("userguide-box caret-" + getReveseDirection(boxDirection) + " caret-overlay");
                            });
                            // Userguide Overlay
                            var $overlay = $("<div class='userguide-overlay'></div>")
                                .addClass("overlay-" + direction)
                                .on("click", function () {
                                $control.hide();
                            })
                                .appendTo($control);
                            $control.hide();
                        });
                        return controls;
                    }
                    function show(controls) {
                        controls.each(function () {
                            var $control = $(this);
                            var target = $control.data('target');
                            var direction = $control.data('direction');
                            $control.show();
                            $control.children().each(function () {
                                var $box = $(this);
                                var boxTarget = $box.data("target");
                                $box.position({
                                    my: getReveseDirection(direction) + "+20",
                                    at: "right center",
                                    of: boxTarget
                                });
                            });
                            var $overlay = $control.find(".userguide-overlay").css(getReveseDirection(direction), $(target).offset().left + $(target).outerWidth());
                        });
                        return controls;
                    }
                    function getReveseDirection(direction) {
                        switch (direction) {
                            case "left":
                                return "right";
                            case "right":
                                return "left";
                            case "top":
                                return "bottom";
                            case "bottom":
                                return "top";
                        }
                    }
                })(userGuide || (userGuide = {}));
            })(jqueryExtentions = ui.jqueryExtentions || (ui.jqueryExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
