﻿/*!@license
 * Infragistics.Web.ClientUI Grid Column Moving 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	jquery.ui.mouse.js
 *	jquery.ui.draggable.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.tree.js
 *	infragistics.ui.shared.js
 *	infragistics.dataSource.js
 *	infragistics.util.js
 *	infragistics.ui.grid.shared.js
 *	infragistics.ui.grid.featurechooser.js
 */

/*global jQuery */
if (typeof jQuery !== "function") {
	throw new Error("jQuery is undefined");
}
(function ($) {
	"use strict";
	$.widget("ui.igGridColumnMoving", {
		renderInFeatureChooser: true,
		options: {
			/* type="array" A list of column settings that specifies moving options on a per column basis. */
			columnSettings: [
				{
					/* type="string" Column key. this is a required property in every column setting if columnIndex is not set. */
					columnKey: null,
					/* type="number" Column index. Can be used in place of column key. The preferred way of populating a column setting is to always use the column keys as identifiers. */
					columnIndex: null,
					/* type="bool" Allows the column to be moved. */
					allowMoving: true
				}
			],
			/* type="deferred|immediate" Specify the drag-and-drop mode for the feature
			immediate type="string" Column headers will rearange as you drag with a space opening under the cursor for the header to be dropped on
			deferred type="string" A clone of the header dragged will be created and indicators will be shown between columns to help navigate the drop.
			*/
			mode: "immediate",
			/* type="dom|render" Specify the way columns will be rearranged
			dom type="string" Columns will be rearranged through dom manipulation
			render type="string" Columns will not be rearranged but the grid will be rendered again with the new column order. Please note this option is incompatible with immediate move mode.
			*/
			moveType: "dom",
			/* type="bool" Specifies if header cells should include an additional button that opens a moving helper dropdown. */
			addMovingDropdown: true,
			/* type="number" Specifies width of column moving dialog */
			movingDialogWidth: 400,
			/* type="number" Specifies height of column moving dialog */
			movingDialogHeight: "",
			/* type="number" Specifies time in milliseconds for animation duration to show/hide modal dialog */
			movingDialogAnimationDuration: 200,
			/* type="number" Specifies the length (in pixels) between the dragged column and the column edges below which the move operation is accepted */
			movingAcceptanceTolerance: 20,
			/* type="number" Specifies the length (in pixels) between the dragged column and the grid edges below which horizontal scrolling occurs */
			movingScrollTolerance: 20,
			/* type="number" Specifies a multiplier for the delay between subsequent scroll operations. The larger this number is, the slower scrolling will appear to be. */
			scrollSpeedMultiplier: 2.0,
			/* type="number" Specifies the length (in pixels) of each individual scroll operation */
			scrollDelta: 2,
			/* type="bool" Specifies whether the contents of the column being dragged will get hidden. The option is only
			relevant in immediate moving mode. */
			hideHeaderContentsDuringDrag: true,
			/* type="number" Specifies the opacity of the drag markup, while a column header is being dragged.
			The value must be between 0 and 1. When GroupBy is enabled, the corresponding option in the GroupBy configuration
			will be used with priority over this one. */
			dragHelperOpacity: 1.0,
			/* type="string" Specifies caption for each move down button in the column moving dialog */
			movingDialogCaptionButtonDesc: $.ig.ColumnMoving.locale.movingDialogCaptionButtonDesc,
			/* type="string" Specifies caption for each move up button in the column moving dialog */
			movingDialogCaptionButtonAsc: $.ig.ColumnMoving.locale.movingDialogCaptionButtonAsc,
			/* type="string" Specifies caption text for the column moving dialog */
			movingDialogCaptionText: $.ig.ColumnMoving.locale.movingDialogCaptionText,
			/* type="string" Specifies caption text for the feature chooser entry */
			movingDialogDisplayText: $.ig.ColumnMoving.locale.movingDialogDisplayText,
			/* type="string" Specifies text for drop tooltip in column moving dialog */
			movingDialogDropTooltipText: $.ig.ColumnMoving.locale.movingDialogDropTooltipText,
			/* type="string" Specifies markup for drop tooltip in column moving dialog */
			movingDialogDropTooltipMarkup: "<div><p><span></span><strong>{text}</strong></p></div>",
			/* type="string" Specifies caption for the move left dropdown button */
			dropDownMoveLeftText: $.ig.ColumnMoving.locale.dropDownMoveLeftText,
			/* type="string" Specifies caption for the move right dropdown button */
			dropDownMoveRightText: $.ig.ColumnMoving.locale.dropDownMoveRightText,
			/* type="string" Specifies caption for the move first dropdown button */
			dropDownMoveFirstText: $.ig.ColumnMoving.locale.dropDownMoveFirstText,
			/* type="string" Specifies caption for the move last dropdown button */
			dropDownMoveLastText: $.ig.ColumnMoving.locale.dropDownMoveLastText,
			/* type="string" Specifies tooltip text for the move indicator*/
			movingToolTipMove: $.ig.ColumnMoving.locale.movingToolTipMove,
			/* type="string" Specifies caption text for the feature chooser submenu button */
			featureChooserSubmenuText: $.ig.ColumnMoving.locale.featureChooserSubmenuText,
			/* type="string" Controls containment behavior of column moving dialog.
                owner type="string" The dialog will be draggable only in the grid area
                window type="string" The dialog will be draggable in the whole window area
            */
			columnMovingDialogContainment: "owner",
			/* type="bool" Enables/disables feature inheritance for the child layouts. NOTE: It only applies for igHierarchicalGrid. */
			inherit: false
		},
		css: {
			/* Classes applied to the table header cells when the mouse hovers on them */
			headerCellMouseOver: "ui-state-hover",
			/* Classes applied to the table header button expanding the moving dropdown */
			dropDownButton: "ui-iggrid-moving-indicator",
			/* Classes applied to the container for the drop down header button */
			dropDownIndicatorContainer: "ui-iggrid-indicatorcontainer",
			/* Classes applied to the top column moving indicator in deferred mode */
			dragIndicatorTop: "ui-iggrid-columnmoving-dragtop ui-icon ui-icon-triangle-1-s",
			/* Classes applied to the bottom column moving indicator in deferred mode */
			dragIndicatorBottom: "ui-iggrid-columnmoving-dragbottom ui-icon ui-icon-triangle-1-n",
			/* Classes applied to the column moving indicators in deferred mode when they are tilted to the left */
			dragIndicatorLeft: "ui-iggrid-columnmoving-dragleft",
			/* Classes applied to the column moving indicators in deferred mode when they are tilted to the right */
			dragIndicatorRight: "ui-iggrid-columnmoving-dragright",
			/* Classes applied to the feature chooser icon container */
			dropDownIconContainer: "ui-iggrid-columnmovingiconcontainer",
			/* Classes applied to the feature chooser icon for column moving feature */
			featureChooserMovingDialogIcon: "ui-icon ui-iggrid-icon-move",
			/* Classes applied to the move left icon for the column moving dialog */
			featureChooserLeftIcon: "ui-iggrid-featurechooser-li-iconcontainer ui-icon ui-iggrid-icon-left",
			/* Classes applied to the move right icon for the column moving dialog */
			featureChooserRightIcon:
				"ui-iggrid-featurechooser-li-iconcontainer ui-icon ui-iggrid-icon-right",
			/* Classes applied to the move first icon for the column moving dialog */
			featureChooserFirstIcon:
				"ui-iggrid-featurechooser-li-iconcontainer ui-icon ui-iggrid-icon-first",
			/* Classes applied to the move last icon for the column moving dialog */
			featureChooserLastIcon:
				"ui-iggrid-featurechooser-li-iconcontainer ui-icon ui-iggrid-icon-last",
			/* Classes applied to the feature chooser submenu containing the moving items */
			featureChooserSubmenu:
				"ui-iggrid-featurechooser-list-submenu ui-menu ui-widget ui-widget-content ui-corner-all",
			/* Classes applied to the feature chooser submenu moving items */
			featureChooserSubmenuItem:
				"ui-iggrid-featurechooser-list-submenu-item ui-state-default",
			/* Classes applied to the feature chooser submenu moving items when hovering over them with the mouse */
			featureChooserSubmenuItemMouserOver: "ui-state-hover",
			/* Classes applied to the column moving dropdown dialog */
			columnMovingDropDown:
				"ui-iggrid-columnmoving-dropdown-dialog ui-widget ui-widget-content ui-corner-all",
			/* Classes applied to the column moving dropdown list */
			columnMovingDropDownList: "ui-iggrid-columnmoving-dropdown-list ui-menu",
			/* Classes applied to each item in the column moving dropdown list */
			columnMovingDropDownItem: "ui-iggrid-columnmoving-dropdown-ddlistitemicons ui-state-default",
			/* Classes applied to each item in the column moving dropdown list when mouse hovers over them */
			columnMovingDropDownItemHover:
				"ui-iggrid-columnmoving-dropdown-listitem-hover ui-state-active ui-state-hover",
			/* Classes applied to the text of each item in the column moving dropdown list when mouse hovers over them */
			columnMovingDropDownItemText: "ui-iggrid-columnmoving-dropdown-ddlistitemtext",
			/* Classes applied to the column indicators of the moving drop down */
			columnMovingIndicatorSelected: "ui-iggrid-hiding-indicator-selected",
			/* Classes applied to the lists of items in the moving dialog */
			movingDialogItemList: "ui-iggrid-moving-dialog-columns",
			/* Classes applied to the items in the item lists in the moving dialog */
			movingDialogListItem: "ui-widget-content",
			/* Classes applied to the move up arrow of each list item in the moving dialog */
			movingDialogListItemArrowUp: "ui-icon-arrowthick-1-n ui-button-icon-primary ui-icon",
			/* Classes applied to the move down arrow of each list item in the moving dialog */
			movingDialogListItemArrowDown: "ui-icon-arrowthick-1-s ui-button-icon-primary ui-icon",
			/* Classes applied to the inner button elements of the arrow indicators in the moving dialog */
			movingDialogListItemArrowButton:
				"ui-button ui-corner-all ui-button-icon-only ig-sorting-indicator",
			/* Classes applied to the text of each list item in the moving dialog */
			movingDialogListItemText: "ui-iggrid-dialog-text",
			/* Classes applied to the close button for the moving dialog */
			movingDialogCloseButton: "ui-icon ui-icon-close"
		},
		events: {
			/* cancel="true" Event which is fired when a drag operation begins on a column header
			use args.columnKey to get the column key of the column being dragged
			use args.columnIndex to get the column index of the column being dragged
			use args.owner to get a reference to the widget
			use args.header to get a reference to the orginal th being dragged
			use args.helper to get a reference to cloned DOM element that's actually being dragged
			*/
			columnDragStart: "columnDragStart",
			/* Event which is fired when a drag operation ends on a column header
			use args.columnKey to get the column key of the column that was being dragged
			use args.columnIndex to get the column index of the column that was being dragged
			use args.owner to get a reference to the widget
			use args.header to get a reference to the orginal th that was being dragged
			use args.helper to get a reference to cloned DOM element that was actually being dragged
			*/
			columnDragEnd: "columnDragEnd",
			/* Event which is fired when a drag operation is canceled
			use args.columnKey to get the column key of the column that was being dragged
			use args.columnIndex to get the column index of the column that was being dragged
			use args.owner to get a reference to the widget
			use args.header to get a reference to the orginal th that was being dragged
			use args.helper to get a reference to cloned DOM element that was actually being dragged
			*/
			columnDragCanceled: "columnDragCanceled",
			/* cancel="true" Event which is fired when a column moving operation is initiated
			use args.columnKey to get the column key of the column that was being moved
			use args.columnIndex to get the column index of the column that was being moved
			use args.targetIndex to get the new column index of the column that was being moved
			use args.owner to get a reference to the widget
			*/
			columnMoving: "columnMoving",
			/* Event which is fired when a column moving operation completes
			use args.columnKey to get the column key of the column that was moved
			use args.oldIndex to get the previous column index of the column that was moved
			use args.newIndex to get the new column index of the column that was being moved
			use args.owner to get a reference to the widget
			*/
			columnMoved: "columnMoved",
			/* cancel="true" event fired before the moving dialog is opened.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialogElement to get a reference to the moving dialog element. This is a jQuery object.
			*/
			movingDialogOpening: "movingDialogOpening",
			/* event fired after the column chooser is already opened.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialogElement to get a reference to the moving dialog element. This is a jQuery object.
			*/
			movingDialogOpened: "movingDialogOpened",
			/* event fired every time the moving dialog changes its position.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialogElement to get a reference to the moving dialog element. This is a jQuery object.
			Use ui.originalPosition to get the original position of the moving dialog div as { top, left } object, relative to the page.
			Use ui.position to get the current position of the moving dialog div as { top, left } object, relative to the page.
			*/
			movingDialogDragged: "movingDialogDragged",
			/* cancel="true" event fired before the moving dialog is closed.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialogElement to get a reference to the moving dialog element. This is a jQuery object.
			*/
			movingDialogClosing: "movingDialogClosing",
			/* event fired after the moving dialog has been closed.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialogElement to get a reference to the moving dialog element. This is a jQuery object.
			*/
			movingDialogClosed: "movingDialogClosed",
			/* cancel="true" event fired before the contents of the model dialog are rendered.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			*/
			movingDialogContentsRendering: "movingDialogContentsRendering",
			/* event fired after the contents of the model dialog are rendered.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			*/
			movingDialogContentsRendered: "movingDialogContentsRendered",
			/* event fired when move up button is pressed in the moving dialog
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			Use ui.columnKey to get the column key of the column that was being moved
			Use ui.columnIndex to get the column index of the column that was being moved
			Use ui.targetIndex to get the new column index of the column that was being moved
			*/
			movingDialogMoveUpButtonPressed: "movingDialogMoveUpButtonPressed",
			/* event fired when move down button is pressed in the moving dialog
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			Use ui.columnKey to get the column key of the column that was being moved
			Use ui.columnIndex to get the column index of the column that was being moved
			Use ui.targetIndex to get the new column index of the column that was being moved
			*/
			movingDialogMoveDownButtonPressed: "movingDialogMoveDownButtonPressed",
			/* cancel="true" event fired when column moving is initiated through dragging it in the moving dialog
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			Use ui.columnKey to get the column key of the column that was being moved
			Use ui.columnIndex to get the column index of the column that was being moved
			Use ui.targetIndex to get the new column index of the column that was being moved
			*/
			movingDialogDragColumnMoving: "movingDialogDragColumnMoving",
			/* event fired when column moving is completed through dragging it in the moving dialog
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridColumnMoving widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.movingDialog to get a reference to the Moving Dialog element. This is a jQuery object.
			Use ui.columnKey to get the column key of the column that was being moved
			Use ui.columnIndex to get the column index of the column that was being moved
			Use ui.targetIndex to get the new column index of the column that was being moved
			*/
			movingDialogDragColumnMoved: "movingDialogDragColumnMoved"
		},
		_createWidget: function () {
			/* !Strip dummy objects from options, because they are defined for documentation purposes only! */
			this.options.columnSettings = [];
			$.Widget.prototype._createWidget.apply(this, arguments);
		},
		_create: function () {
			/* stores the left coordinate of all unfixed column borders per table header row */
			this._cache = {};
			/* stores the currently dragged column key */
			this._cKey = "";
			/* stores the currently dragged column index */
			this._cIdx = -1;
			/* stores the target key for the currently dragged column */
			this._tKey = "";
			/* stores the target index for the currently dragged column */
			this._tIdx = -1;
			/* stores the original header in immediate mode */
			this._oTh = null;
			/* stores the bottom indivator in deferred mode */
			this._cmib = null;
			/* stores the top indicator in deferred mode */
			this._cmit = null;
			/* stores the modal dialog for the control */
			this._movingDialog = null;
			/* the groupby feature if enabled */
			this._groupBy = null;
			/* stores info about the dropdown buttons */
			this._ddButtons = {
				first: {
					text: this.options.dropDownMoveFirstText,
					func: $.proxy(this._moveFirst, this),
					icon: this.css.featureChooserFirstIcon
				},
				left: {
					text: this.options.dropDownMoveLeftText,
					func: $.proxy(this._moveLeft, this),
					icon: this.css.featureChooserLeftIcon
				},
				right: {
					text: this.options.dropDownMoveRightText,
					func: $.proxy(this._moveRight, this),
					icon: this.css.featureChooserRightIcon
				},
				last: {
					text: this.options.dropDownMoveLastText,
					func: $.proxy(this._moveLast, this),
					icon: this.css.featureChooserLastIcon
				}
			};
			/* scrolling container */
			this._hscroller = null;
			/* scrolling parameters */
			this._scroller = {
				on: false,
				delay: -1
			};
			/* dummy check */
			if (this.options.mode !== "immediate" && this.options.mode !== "deferred") {
				this.options.mode = "immediate";
			}
			if (this.options.mode === "immediate" && this.options.moveType === "render") {
				this.options.moveType = "dom";
			}
			if (this.options.addMovingDropdown === false) {
				this.renderInFeatureChooser = false;
			}
		},
		_setOption: function (key) {
			// we don't support changing the mode type for moving as it involves heavy modifications to the dom
			if (key === "mode" || key === "columnSettings" || key === "addMovingDropdown") {
				throw new Error($.ig.Grid.locale.optionChangeNotSupported.replace("{optionName}", key));
			}
			$.Widget.prototype._setOption.apply(this, arguments);
		},
		/* public methods */
		destroy: function () {
			// restoring overwritten functions
			this.grid.moveColumn = this._gridMoveColumn;
			this._unregisterEvents();
			this._removeMoving();
			/* destroys the column moving widget */
			$.Widget.prototype.destroy.call(this);
			return this;
		},
		moveColumn: function (column, target, after, inDom, callback) {
			/* Moves a visible column at a specified place, in front or behind a target column or at a target index
			Note: This method is asynchronous which means that it returns immediately and any subsequent code will execute in parallel. This may lead to runtime errors. To avoid them put the subsequent code in the callback parameter provided by the method.
			paramType="number|string" An identifier of the column to be moved. It can be a key, a Multi-Column Header identificator, or an index in a number format. The latter is not supported when the grid contains multi-column headers.
			paramType="number|string" An identifier of a column where the moved column should move to or an index at which the moved column should be moved to. In the case of a column identifier the column will be moved after it by default.
			paramType="bool" optional="true" Specifies whether the column moved should be moved after or before the target column.
			paramType="bool" optional="true" Specifies whether the column moving will be enacted through DOM manipulation or through rerendering of the grid.
			paramType="function" optional="true" Specifies a custom function to be called when the column is moved.
			*/
			this._moveColumn(column, target, after, inDom, true, callback);
		},
		/* grid event handlers */
		_headerCellRendered: function (event, args) {
			if (this.grid.id() !== args.owner.id()) {
				return;
			}
			this._thRendered(args.th);
		},
		_headerRendering: function () {
			var i;
			for (i = 0; i < this.grid.options.features.length; i++) {
				if (this.grid.options.features[ i ].name === "GroupBy") {
					this._groupBy = this.grid.element.data("igGridGroupBy");
					break;
				}
			}
		},
		_headerRendered: function (event, args) {
			if (this.grid.id() !== args.owner.id()) {
				return;
			}
			this._thsRendered(args.table);
		},
		_gridFullyRendered: function () {
			this.grid.scrollContainer().bind("scroll", this._containerScrolledHandler);
			this._updateLayout();
		},
		_containerScrolled: function (event, args, fromParent) {
			// handles when the scroll container is scrolled. This handler should not do anything if the scroll is triggered
			// by the autoScroll so that the moving elements are not fixed twice. It's also important to propagate to all children
			var grids, i, cmi;
			if (!this._autoScrolled) {
				/* updated for this grid */
				if (this.options.mode === "deferred") {
					this._adjustIndicators(event.originalEvent.detail, fromParent);
				}
			}
			/* propagate to child grids */
			grids = this.grid.element.find(".ui-iggrid-table");
			for (i = 0; i < grids.length; i++) {
				cmi = grids.eq(i).data("igGridColumnMoving");
				if (cmi) {
					cmi._containerScrolled(event, args, true);
				}
			}
		},
		_fixedColumnsChanged: function () {
			this._updateLayout();
		},
		/* ui draggable moving event handlers */
		_headerPicked: function (event, args) {
			var $th = $(event.target).closest("th"), id = this._getIdOfTh($th), noCancel = true;
			args = {
				columnKey: id,
				columnIndex: $th.index(),
				owner: this,
				header: $th,
				helper: args.helper
			};
			noCancel = this._trigger(this.events.columnDragStart, event, args);
			if (noCancel) {
				/* M.H. 3 Oct 2013 Fix for bug #153904: When you open feature chooser and
				move column by dragging the feature chooser does not close. */
				if (this.grid._focusedElement) {
					this.grid._focusedElement.blur();
				}
				this._thPicked($th, id);
			}
			return noCancel;
		},
		_headerDragged: function (event, args) {
			var left;
			if (this._oPos) {
				if (this._oPos.left === args.position.left) {
					return;
				}
				left = this._oPos.left > args.position.left;
			} else {
				left = args.originalPosition.left > args.position.left;
			}
			this._oPos = { left: args.position.left };
			/* in order to support groupby's invalid revert, we need to update the original position for the
			drag and drop operation, so dropping the header will result in a correct animation */
			if (this.options.mode === "deferred") {
				this._thDragedDeferred(args.helper, args.offset, left);
			} else if (this._thDragged(args.helper, args.offset, left)) {
				args.originalPosition.left = args.position.left;
			}
		},
		_headerDropped: function (event, args) {
			var $th = $(event.target).closest("th"), id = this._getIdOfTh($th), noCancel = true;
			args = {
				columnKey: id,
				columnIndex: $th.index(),
				owner: this,
				header: $th,
				helper: args.helper
			};
			noCancel = this._trigger(this.events.columnDragEnd, event, args);
			if (noCancel) {
				this._thDropped();
			} else {
				this._trigger(this.events.columnDragCanceled, event, args);
			}
			return noCancel;
		},
		/* moving dialog event handlers */
		_dialogUpPressed: function (event) {
			var tar = $(event.target),
				item = tar.closest("li"),
				parent = item.parent(),
				idx = item.index(),
				iItem = idx === 0 ?
					item.parent().children().last().attr("data-value") : item.prev().attr("data-value"),
				iDir = idx === 0 ? true : false,
				col = tar.closest("span").attr("datakey"),
				a, oc;
			a = {
				owner: this,
				movingDialog: this._movingDialog,
				columnKey: col,
				columnIndex: idx,
				targetIndex: idx === 0 ? parent.children().length - 1 : idx - 1
			};
			this._cKey = col;
			this._cIdx = a.columnIndex;
			this._tIdx = a.targetIndex;
			if (this._cIdx === this._tIdx) {
				return;
			}
			oc = this._moveColumn(col, iItem, iDir, this.options.moveType === "dom", false);
			if (!oc) {
				return false;
			}
			if (idx === 0) {
				item.detach().insertAfter(parent.children("li:last"));
			} else {
				item.detach().insertBefore(parent.children("li:eq(" + (idx - 1) + ")"));
			}
			this._trigger(this.events.movingDialogMoveUpButtonPressed, event, a);
		},
		_dialogDownPressed: function (event) {
			var tar = $(event.target),
				item = tar.closest("li"),
				parent = item.parent(),
				idx = item.index(),
				iItem = idx === item.siblings().length ?
					item.parent().children().first().attr("data-value") :
					item.next().attr("data-value"),
				iDir = idx === item.siblings().length ? false : true,
				col = tar.closest("span").attr("datakey"),
				a, oc;
			a = {
				owner: this,
				movingDialog: this._movingDialog,
				columnKey: col,
				columnIndex: idx,
				targetIndex: idx === parent.children().length - 1 ? 0 : idx + 1
			};
			this._cKey = col;
			this._cIdx = a.columnIndex;
			this._tIdx = a.targetIndex;
			if (this._cIdx === this._tIdx) {
				return;
			}
			oc = this._moveColumn(col, iItem, iDir, this.options.moveType === "dom", false);
			if (!oc) {
				return false;
			}
			if (idx === item.parent().children().length - 1) {
				item.detach().insertBefore(parent.children("li:first"));
			} else {
				item.detach().insertAfter(parent.children("li:eq(" + idx + ")"));
			}
			this._trigger(this.events.movingDialogMoveDownButtonPressed, event, a);
		},
		_dialogRearranging: function (event, args) {
			var noCancel, a, oc,
				column = $(args.draggable),
				target = $(args.element),
				cid = column.attr("data-value"),
				tid = target.attr("data-value"),
				aft = $(args.element)
					.closest("ul[data-depth=0]")
					.data("igTree")._validationObject.dropAfter,
				cix = column.index(),
				tix = target.index();
			/* depending on other params the target index needs to be changed */
			tix = tix > cix ? (aft ? tix : tix - 1) : (aft ? tix + 1 : tix);
			a = {
				owner: this,
				movingDialog: this._movingDialog,
				columnKey: cid,
				columnIndex: cix,
				targetIndex: tix
			};
			if (cix === tix) {
				return false;
			}
			noCancel = this._trigger(this.events.movingDialogDragColumnMoving, event, a);
			if (noCancel) {
				this._cKey = cid;
				this._cIdx = a.columnIndex;
				this._tIdx = a.targetIndex;
				oc = this._moveColumn(cid, tid, aft, this.options.moveType === "dom", false);
				if (!oc) {
					return false;
				}
				a.movingDialog.find("div.ui-state-highlight").remove();
				this._trigger(this.events.movingDialogDragColumnMoved, event, a);
				return true;
			}
			return false;
		},
		_dialogRearranged: function (event, args) {
			this._bindUpDownDialogButtons(args.element.parent());
		},
		_dialogDragged: function (event, args) {
			this._trigger(this.events.movingDialogDragged, null,
				{
					movingDialogElement: event.target,
					owner: this,
					originalPosition: args.originalPosition,
					position: args.position
				});
		},
		_dialogOpening: function (event, args) {
			var noCancel;
			noCancel = this._trigger(this.events.movingDialogOpening,
				null, { movingDialogElement: event.target, owner: this });
			if (noCancel) {
				this._renderMovingDialogContent(event, args);
				this._trigger(this.events.movingDialogOpened,
					null, { movingDialogElement: event.target, owner: this });
			}
			return noCancel;
		},
		_dialogClosing: function (event) {
			return this._trigger(this.events.movingDialogClosing, null,
				{ movingDialogElement: event.target, owner: this });
		},
		_dialogClosed: function (event) {
			this._trigger(this.events.movingDialogClosed, null,
				{ movingDialogElement: event.target, owner: this });
		},
		/* dropdown event handlers */
		_dropDownMouseDown: function (event) {
			var ind = $(event.target);
			this._toggleDropDown(ind, $("div[data-moving-inddropdown='" + this.grid.id() + "']"));
			this._cancelEvent(event);
		},
		_dropDownKeyDown: function (event) {
			var sItem,
				nItem,
				nItemB,
				dropdown = $("div[data-moving-inddropdown='" + this.grid.id() + "']");
			if (event.keyCode === $.ui.keyCode.ENTER || event.keyCode === $.ui.keyCode.SPACE) {
				sItem = dropdown.find("ul .ui-state-hover:first");
				if (dropdown.is(":visible") && sItem.length > 0) {
					sItem.trigger("mousedown");
				}
				this._toggleDropDown($(event.target), dropdown, false);
				this._cancelEvent(event);
			} else if (event.keyCode === $.ui.keyCode.ESCAPE) {
				this._toggleDropDown($(event.target), dropdown, false);
				this._cancelEvent(event);
			} else if (event.keyCode === $.ui.keyCode.DOWN || event.keyCode === $.ui.keyCode.UP) {
				if (dropdown.is(":visible")) {
					sItem = dropdown.find("ul .ui-state-hover:first").closest("li");
					if (sItem.length === 0) {
						nItem = dropdown.find("ul li:eq(0)");
					} else {
						if (sItem.index() === 0 && event.keyCode === $.ui.keyCode.UP) {
							nItem = sItem.parent().children(":last");
						} else {
							nItem = event.keyCode === $.ui.keyCode.DOWN ? sItem.next() : sItem.prev();
						}
					}
					dropdown.find("ul .ui-state-hover").removeClass(this.css.columnMovingDropDownItemHover);
					nItemB = nItem.find("[role='button']");
					if (nItemB.length > 0) {
						nItem = nItemB;
					}
					nItem.addClass(this.css.columnMovingDropDownItemHover);
				}
				this._cancelEvent(event);
			}
		},
		_dropDownButtonMouseOver: function (event) {
			$(event.currentTarget).addClass(this.css.columnMovingDropDownItemHover);
		},
		_dropDownButtonMouseOut: function (event) {
			$(event.currentTarget).removeClass(this.css.columnMovingDropDownItemHover);
		},
		_moveLeft: function (event, args) {
			var sib = this._getAllSiblings(args), cid, tid, i;
			for (i = 0; i < sib.length; i++) {
				cid = sib[ i ].key || sib[ i ].identifier;
				if (cid === args) {
					if (i > 0) {
						this._cKey = args;
						this._cIdx = i;
						this._tIdx = i - 1;
						tid = sib[ i - 1 ].key || sib[ i - 1 ].identifier;
						this._moveColumn(cid, tid, false, this.options.moveType === "dom", false);
					}
					break;
				}
			}
		},
		_moveRight: function (event, args) {
			var sib = this._getAllSiblings(args), cid, tid, i;
			for (i = 0; i < sib.length; i++) {
				cid = sib[ i ].key || sib[ i ].identifier;
				if (cid === args) {
					if (i < sib.length - 1) {
						this._cKey = args;
						this._cIdx = i;
						this._tIdx = i + 1;
						tid = sib[ i + 1 ].key || sib[ i + 1 ].identifier;
						this._moveColumn(cid, tid, true, this.options.moveType === "dom", false);
					}
					break;
				}
			}
		},
		_moveFirst: function (event, args) {
			var sib = this._getAllSiblings(args), cid, tid, i;
			for (i = 0; i < sib.length; i++) {
				cid = sib[ i ].key || sib[ i ].identifier;
				if (cid === args) {
					if (i !== 0) {
						this._cKey = args;
						this._cIdx = i;
						this._tIdx = 0;
						tid = sib[ 0 ].key || sib[ 0 ].identifier;
						this._moveColumn(cid, tid, false, this.options.moveType === "dom", false);
					}
					break;
				}
			}
		},
		_moveLast: function (event, args) {
			var sib = this._getAllSiblings(args), cid, tid, i;
			for (i = 0; i < sib.length; i++) {
				cid = sib[ i ].key || sib[ i ].identifier;
				if (cid === args) {
					if (i !== sib.length - 1) {
						this._cKey = args;
						this._cIdx = i;
						this._tIdx = sib.length - 1;
						tid = sib[ sib.length - 1 ].key || sib[ sib.length - 1 ].identifier;
						this._moveColumn(cid, tid, true, this.options.moveType === "dom", false);
					}
					break;
				}
			}
		},
		/* style event handlers */
		_headerMouseOver: function (event) {
			$(event.target).closest("th").addClass(this.css.headerCellMouseOver);
		},
		_headerMouseOut: function (event) {
			$(event.target).closest("th").removeClass(this.css.headerCellMouseOver);
		},
		/* render functions */
		_renderHelpers: function () {
			var existingBottomIndicator = this.grid._rootContainer()
					.children("[id$='_moving_indicator_bottom']"),
				existingTopIndicator = this.grid._rootContainer()
					.children("[id$='_moving_indicator_top']");
			if (existingBottomIndicator.length > 0 && existingTopIndicator.length > 0) {
				this._cmib = existingBottomIndicator;
				this._cmib.data("users", this._cmib.data("users") + 1);
				this._cmit = existingTopIndicator;
				this._cmit.data("users", this._cmit.data("users") + 1);
			} else {
				this._cmib = $("<div>&nbsp;</div>")
					.attr("id", this.grid.id() + "_moving_indicator_bottom")
					.addClass(this.css.dragIndicatorBottom)
					.css({
						"position": "absolute",
						"z-index": "1000000",
						"width": "16px",
						"height": "16px",
						"display": "none"
					})
					.appendTo(this.grid._rootContainer());
				this._cmit = $("<div>&nbsp;</div>")
					.attr("id", this.grid.id() + "_moving_indicator_top")
					.addClass(this.css.dragIndicatorTop)
					.css({
						"position": "absolute",
						"z-index": "1000000",
						"width": "16px",
						"height": "16px",
						"display": "none"
					})
					.appendTo(this.grid._rootContainer());
			}
		},
		_renderFeatureChooser: function (col, th) {
			var fc = this.grid.element.data("igGridFeatureChooser");
			if (fc && this.renderInFeatureChooser) {
				if (fc._shouldRenderInFeatureChooser(col) === true) {
					this._addMoveButtonsInFeatureChooser(fc, col);
				} else {
					this._renderMovingDropDownIndicator(th, col);
					/* S.S. September 20, 2013 Fix for bug #152681 - We need to add a specific class to the drop down
					indicator so that the header cell is always rendered in one line */
					this.grid._enableHeaderCellFeature(th);
				}
			}
		},
		_renderMovingDropDownIndicator: function (th, col) {
			var $button, $anchor, $container, self = this;
			$button = $("<span></span>").addClass(this.css.dropDownButton);
			th.attr("th-remove-focus", "");
			$anchor = $("<a></a>")
				.attr("href", "#")
				.attr("title", this.options.movingToolTipMove)
				.attr("id", this.grid.id() + "_moving_headerButton_" + col)
				.bind({
					keydown: this._dropDownKeyHandler,
					blur: function (e) {
						self._toggleDropDown($(e.target),
							$("div[data-moving-inddropdown='" + self.grid.id() + "']"), true);
					},
					mousedown: this._dropDownButtonHandler,
					mouseup: this._cancelEventHandler,
					click: this._cancelEventHandler
				});
			$container = $("<div></div>")
				.addClass(this.css.dropDownIndicatorContainer).appendTo(th);
			$button.appendTo($anchor);
			$anchor.appendTo($container);
			this._needToRenderDropDown = true;
		},
		_renderMovingDialogContent: function () {
			/* Render moving modal dialog and its content */
			var $content = this._movingDialog.igGridModalDialog("getContent"),
				movingDialog = this._movingDialog,
				$tree = $content.find("#" + this.grid.id() + "_dialog_tree"),
				noCancel;
			noCancel = this._trigger(this.events.movingDialogContentsRendering,
				null, { movingDialogElement: movingDialog, owner: this });
			if (noCancel) {
				if ($tree.length > 0) {
					$tree.igTree("option", "dataSource",
						this._getClonedDataSource(this.grid._visibleMchColumns() ||
						this.grid._visibleColumns()));
				} else {
					$tree = this._initializeTree($content);
				}
				this._bindUpDownDialogButtons($tree);
				this._trigger(this.events.movingDialogContentsRendered,
					null, { movingDialogElement: movingDialog, owner: this });
			}
		},
		_initializeTree: function (content) {
			var self = this, markup = self.options.movingDialogDropTooltipMarkup
				.replace("{text}", self.options.movingDialogDropTooltipText);
			return $("<ul id='" + this.grid.id() + "_dialog_tree'></ul>")
				.appendTo(content)
				.igTree({
					dataSourceType: "json",
					dataSource: this._getClonedDataSource(this.grid._visibleMchColumns() ||
						this.grid._visibleColumns()),
					bindings: {
						primaryKey: "key",
						textKey: "headerText",
						valueKey: "key",
						childDataProperty: "group",
						nodeContentTemplate: this._getTreeTemplate()
					},
					dragAndDrop: true,
					dragAndDropSettings: {
						dragAndDropMode: "move",
						revert: true,
						zIndex: 1000001,
						moveBetweenMarkup: markup,
						customDropValidation: function (col) {
							var context = $(this), $col = $(col);
							if (self._isColumnMovable($col.attr("data-value"))) {
								if (context.is("li") && context.parent()[ 0 ] === $col.parent()[ 0 ]) {
									return true;
								}
							}
							return false;
						},
						containment: false
					},
					nodeDropping: this._dialogRearrangingHandler,
					nodeDropped: this._dialogRearrangedHandler,
					selectionChanging: function () { return false; }
				});
		},
		_getClonedDataSource: function (ds) {
			return jQuery.extend(true, [], ds, this._cloneMovable(ds));
		},
		_cloneMovable: function (ds) {
			var self = this, movableClone = [], idx = 0;
			$.each(ds, function () {
				if (this.group) {
					movableClone.push({
						movable: self._isColumnMovable(this.key, idx++),
						group: self._cloneMovable(this.group),
						key: this.key || this.identifier || idx
					});
				} else {
					movableClone.push({
						movable: self._isColumnMovable(this.key, idx++),
						key: this.key || idx
					});
				}
			});
			return movableClone;
		},
		_getTreeTemplate: function () {
			var tmpl = "", up, down, wrapper, data;
			data = "<span>${headerText}</span>";
			wrapper = "<span class='" + this.css.movingDialogListItemArrowButton + "'>";
			up = wrapper + "<span id='" + this.grid.id() +
				"_moving_dialog_${key}_up' title='" +
				this.options.movingDialogCaptionButtonAsc +
				"' role='button' datakey='${key}' class='" +
				this.css.movingDialogListItemArrowUp + "' style='" +
				"margin-top:-10px;'></span></span>";
			down = wrapper + "<span id='" + this.grid.id() +
				"_moving_dialog_${key}_down' title='" +
				this.options.movingDialogCaptionButtonDesc +
				"' role='button' datakey='${key}' class='" +
				this.css.movingDialogListItemArrowDown +
				"' style='margin-top:-10px;'></span></span>";
			wrapper += "</span>";
			tmpl += "{{if ${movable} }}";
			tmpl += up + down + data;
			tmpl += "{{else}}";
			tmpl += wrapper + wrapper + data;
			tmpl += "{{/if}}";
			return tmpl;
		},
		_renderMovingDialog: function () {
			var containment,
				o = this.options,
				$captionButtonContainer,
				$closeButton,
				movingDialog;
			if (this.options.columnMovingDialogContainment === "owner") {
				containment = this.grid.container();
			} else {
				containment = "window";
			}
			movingDialog = $("<div></div>")
					.appendTo(this.grid._rootContainer())
					.attr("id", this.grid.id() + "_moving_movingDialog");
			this._movingDialog = movingDialog;
			movingDialog.igGridModalDialog({
				containment: containment,
				renderFooterButtons: false,
				modalDialogCaptionText: o.movingDialogCaptionText,
				modalDialogWidth: o.movingDialogWidth,
				modalDialogHeight: o.movingDialogHeight,
				animationDuration: o.movingDialogAnimationDuration,
				gridContainer: this.grid.container(),
				modalDialogOpening: this._dialogOpeningHandler,
				modalDialogMoving: this._dialogDraggedHandler,
				modalDialogClosing: this._dialogClosingHandler,
				modalDialogClosed: this._dialogClosedHandler
			});
			/* render close button */
			$captionButtonContainer = movingDialog.igGridModalDialog("getCaptionButtonContainer");
			$closeButton = $("<button></button>")
				.attr("id", this.grid.id() + "_moving_movingDialog_closeButton")
				.appendTo($captionButtonContainer);
			$closeButton.igButton({
				onlyIcons: true,
				icons: {
					primary: this.css.movingDialogCloseButton
				},
				width: "20px",
				height: "20px",
				click: this._dialogCloseButtonHandler,
				title: $.ig.ColumnMoving.locale.movingDialogCloseButtonTitle
			});
		},
		_renderDropDown: function (element) {
			var self = this, dropDown, list;
			dropDown = $("<div data-moving-inddropdown='" + this.grid.id() + "'></div>")
				.css("position", "absolute")
				.css("display", "none")
				.addClass(this.css.columnMovingDropDown)
				.appendTo(this.grid._rootContainer());
			list = $("<ul tabindex=\"0\"></ul>")
				.addClass(this.css.columnMovingDropDownList)
				.appendTo(dropDown);
			$.each(this._ddButtons, function (key, val) {
				$("<li></li>")
					.addClass(self.css.columnMovingDropDownItem)
					.attr("data-key", key)
					.bind({
						mouseover: self._dropDownButtonMouseOverHandler,
						mouseout: self._dropDownButtonMouseOutHandler
					})
					.append(
						$("<span></span>")
							.addClass(self.css.dropDownIconContainer)
							.append(
								$("<span></span>")
									.addClass(val.icon)
							)
					)
					.append(
						$("<span></span>")
							.addClass(self.css.columnMovingDropDownItemText)
							.text(val.text)
					)
					.appendTo(list);
			});
			/* moving dialog button */
			$("<a></a>")
				.appendTo($("<li></li>").appendTo(list))
				.igButton({
					labelText: this.options.movingDialogCaptionText,
					mousedown: function (event) {
						if (event.target) {
							setTimeout(function () { $(event.target).removeClass("ui-state-active"); }, 0);
						}
						self._toggleDropDown(element, dropDown, true);
						self._openMovingDialog(element, dropDown, true);
					}
				});
			return dropDown;
		},
		_toggleDropDown: function (element, dropDown, isCalledFromBlur) {
			var th,
				id,
				isLast,
				offset,
				rOffset,
				left,
				isVisible = (dropDown.is(":visible") === true),
				nth = element ? element.closest("th") : null,
				nid = nth ? nth.attr("id") || nth.attr("data-mch-id") : null,
				self = this;
			if (dropDown.data("isAnimating") === true ||
				(nid !== null && this._currentlyToggled !== nid && isCalledFromBlur === true) ||
				(isVisible === false && isCalledFromBlur === true)) {
				return;
			}
			if (!isVisible) {
				th = nth;
				th.find("a[title='" + this.options.movingToolTipMove + "']").focus();
				isLast = th.parent().children().length - th.index() <= 2;
				offset = $.ig.util.offset(th);
				if (isLast) {
					left = offset.left + th.outerWidth() - dropDown.outerWidth();
				} else {
					left = offset.left;
				}
				rOffset = $.ig.util.getRelativeOffset(dropDown);
				left = Math.max(0, left - rOffset.left);
				dropDown.css("top", offset.top + th.outerHeight() - rOffset.top);
				dropDown.css("left", left);
				$.each(this._ddButtons, function (key, value) {
					dropDown.find("li[data-key='" + key + "']")
						.bind("mousedown.temp", function (event) {
							self._toggleDropDown(element, dropDown, true);
							id = th.attr("id");
							if (id) {
								id = id.replace(self.grid.id() + "_", "");
							} else {
								id = th.attr("data-mch-id");
							}
							value.func(null, id);
							event.stopPropagation();
							event.preventDefault();
						});
				});
				this._currentlyToggled = th.attr("id") || th.attr("data-mch-id");
			} else {
				$.each(this._ddButtons, function (key) {
					dropDown.find("li[data-key='" + key + "']").unbind("mousedown.temp");
				});
			}
			dropDown.data("isAnimating", true);
			dropDown.toggle(200, function () {
				if (dropDown.is(":visible") === true) {
					if (element) {
						element.addClass(self.css.columnMovingIndicatorSelected);
						element.attr("data-indicator-selected", "true");
					}
				} else {
					if (element) {
						element.removeClass(self.css.columnMovingIndicatorSelected);
						element.removeAttr("data-indicator-selected");
					}
				}
				dropDown.data("isAnimating", false);
			});
		},
		_renderSubmenuFC: function (columnKey, $submenu) {
			var $ul, moveColumns, moveLeft, moveRight, moveFirst, moveLast;
			$ul = $("<ul class=\"" + this.css.featureChooserSubmenu + "\"></ul>").appendTo($submenu);
			moveColumns = {
				name: "ColumnMovingDialog",
				text: this.options.movingDialogDisplayText,
				iconClass: this.css.featureChooserMovingDialogIcon,
				method: this._openMovingDialogHandler
			};
			moveLeft = {
				name: "Left",
				text: this.options.dropDownMoveLeftText,
				iconClass: this.css.featureChooserLeftIcon,
				method: $.proxy(this._moveLeft, this)
			};
			moveRight = {
				name: "Right",
				text: this.options.dropDownMoveRightText,
				iconClass: this.css.featureChooserRightIcon,
				method: $.proxy(this._moveRight, this)
			};
			moveFirst = {
				name: "First",
				text: this.options.dropDownMoveFirstText,
				iconClass: this.css.featureChooserFirstIcon,
				method: $.proxy(this._moveFirst, this)
			};
			moveLast = {
				name: "Last",
				text: this.options.dropDownMoveLastText,
				iconClass: this.css.featureChooserLastIcon,
				method: $.proxy(this._moveLast, this)
			};
			this._renderSubmenuFCItem(moveColumns, $ul, columnKey, $submenu);
			this._renderSubmenuFCItem(moveFirst, $ul, columnKey, $submenu);
			this._renderSubmenuFCItem(moveLeft, $ul, columnKey, $submenu);
			this._renderSubmenuFCItem(moveRight, $ul, columnKey, $submenu);
			this._renderSubmenuFCItem(moveLast, $ul, columnKey, $submenu);
		},
		_renderSubmenuFCItem: function (obj, $ul, columnKey, $submenu) {
			var $li, text = obj.text, html, fnBlur, $next,
				css = this.css,
				id = this.grid.id() + "_featurechooser_dd_li_" + columnKey + "_" + obj.name,
				method = obj.method;
			fnBlur = function ($e) {
				$e.blur();
			};
			html = "<li tabindex=\"0\" data-fc-item=\"1\" class=\"" +
				this.css.featureChooserSubmenuItem + "\" id=\"" + id +
				"\" title=\"" + text + "\">" + text + "</li>";
			$li = $(html).appendTo($ul);
			$li.bind({
				keydown: function (event) {
					var keyCode = event.keyCode;
					switch (keyCode) {
						case $.ui.keyCode.ENTER:
						case $.ui.keyCode.SPACE:
							method(event, columnKey);
							fnBlur($(event.target));
							break;
						case $.ui.keyCode.DOWN:
							$next = $li.next();
							if ($next.length === 0) {
								$ul.find("li:first").focus();
							} else {
								$next.focus();
							}
							break;
						case $.ui.keyCode.UP:
							$li.prev().focus();
							if ($li.prev().length === 0) {
								$("#" + $submenu.data("buttonId")).focus();
							}
							break;
						case $.ui.keyCode.ESCAPE:
							fnBlur($(event.target));
							break;
					}
				},
				mousedown: function (e) {
					method(e, columnKey);
					fnBlur($(e.target));
					e.preventDefault();
				},
				mouseover: function () {
					$(this).addClass(css.featureChooserSubmenuItemMouserOver);
				},
				mouseleave: function () {
					$(this).removeClass(css.featureChooserSubmenuItemMouserOver);
				}
			});
		},
		_addMoveButtonsInFeatureChooser: function (fc, key) {
			fc._renderInFeatureChooser(key,
				{
					name: "ColumnMovingDropDown",
					text: this.options.featureChooserSubmenuText,
					iconClass: "ui-iggrid-featurechooser-li-iconcontainer ui-icon ui-iggrid-icon-move", //featureChooserIconClass: 'ui-icon ui-icon-calculator',
					methodRenderSubmenu: $.proxy(this._renderSubmenuFC, this),
					order: 1, // order in group
					groupName: "dropdown",
					groupOrder: 4,
					type: "dropdown",
					state: "hide"
				}
			);
		},
		/* private function */
		_moveColumn: function (column, target, after, inDom, fromApi, callback) {
			var noCancel = true, args;
			if (fromApi === false) {
				args = {
					columnKey: this._cKey,
					columnIndex: this._cIdx,
					targetIndex: this._tIdx,
					owner: this
				};
				noCancel = this._trigger(this.events.columnMoving, null, args);
			}
			if (noCancel) {
				this._movingDirty = true;
				this.grid.moveColumn(column, target, after, inDom, callback);
				if (fromApi === false) {
					delete args.columnIndex;
					delete args.targetIndex;
					args.oldIndex = this._cIdx;
					args.newIndex = this._tIdx;
					this._triggerColumnMovedAsync(args);
				}
				return true;
			}
			return false;
		},
		_moveColumnOverwrite: function (column, target, after, inDom, callback) {
			var mp, fixing, isgh, i, hcPreserve;
			this._oldColsSave = this.grid._oldCols ?
				jQuery.extend(true, [], this.grid._oldCols) : null;
			this._colsSave = this.grid.options.columns ?
				jQuery.extend(true, [], this.grid.options.columns) : null;
			mp = this._gridMoveColumn.apply(this.grid, [ column, target, after, inDom, callback ]);
			if (mp) {
				this.grid._oldCols = this._oldColsSave;
				if (this.grid._oldCols) {
					hcPreserve = jQuery.extend(true, {}, this.grid._hiddenColumns);
					this.grid._generateColumnFlatStructure(this.grid._oldCols);
					this.grid._hiddenColumns = hcPreserve;
				} else {
					this.grid.options.columns = this._colsSave;
				}
				fixing = this.grid.element.data("igGridColumnFixing");
				isgh = !!this.grid._getMultiHeaderColumnById(mp.column);
				this.grid._columnMovingResets();
				if (mp.columnFixed) {
					fixing._unfixColumnInternal(mp.column, mp.target, mp.after);
				}
				if (mp.targetFixed) {
					fixing._fixColumnInternal(mp.column, mp.target, mp.after);
				}
				this.grid._fixedColumns = [];
				for (i = 0; i < this.grid.options.columns.length; i++) {
					if (this.grid.options.columns[ i ].fixed) {
						this.grid._fixedColumns.push(this.grid.options.columns[ i ]);
					}
				}
				this._updateLayout();
				if (callback) {
					$.ig.util.invokeCallback(callback, [ this.grid.options.columns ]);
				}
			}
		},
		_getAllSiblings: function (col) {
			var fixing = this.grid.hasFixedColumns(),
				fixingDir = this.grid.fixingDirection(),
				sib = this._cache.siblings[ col ],
				fsib = fixing ? this._cache.fixedSiblings[ col ] : null;
			if (fixing && fsib && fsib.length > 0) {
				if (fixingDir === "left") {
					return fsib.concat(sib);
				}
				return sib.concat(fsib);
			}
			return sib;
		},
		_fixingFilter: function (cols, fixed) {
			if (fixed) {
				return $.grep(cols, this._isColFixed);
			}
			return $.grep(cols, this._isColUnfixed);
		},
		_isColFixed: function (col) {
			return col.fixed;
		},
		_isColUnfixed: function (col) {
			return !col.fixed;
		},
		_thRendered: function (th) {
			var cid = this._getIdOfTh(th), cix = th.index("not[data-skip='true']");
			if (this._isColumnMovable(cid, cix) === true) {
				this._markForMoving(th);
				if (this.options.addMovingDropdown === true) {
					this._renderFeatureChooser(cid, th);
				}
			}
		},
		_thsRendered: function () {
			if (this.options.mode === "deferred") {
				// in deferred mode we need the moving helpers
				this._renderHelpers();
			}
			if (this.options.addMovingDropdown === true) {
				/* check if the moving dialog isn't alreay rendered */
				if (!this._movingDialog ||
					(this._movingDialog instanceof jQuery && this._movingDialog.length === 0)) {
					this._renderMovingDialog();
				}
				/* check if the dropdown isn't already rendered */
				if ($("div[data-moving-inddropdown='" + this.grid.id() + "']").length === 0) {
					this._renderDropDown();
				}
			}
		},
		_thPicked: function (th, id) {
			this._cKey = id;
			this._cIdx = th.index();
			this._currentlyDraggedFixed = this.grid._isFixedElement(th);
			/* the grid might have changed visibility or other attributes
			which forces us to update layout on each pick as opposed to only
			relying on responding to databinding/columns modified events. */
			this._updateLayout();
			if (this.options.mode === "immediate") {
				if (this.options.hideHeaderContentsDuringDrag === true) {
					th.children().not("div[data-hiddencolindicator]").hide();
				}
				this._oTh = th;
			}
		},
		_thDragedDeferred: function (helper, pos, left) {
			var movingOpts = this._moArrays[ this._cKey ], mo, targetLeft, targetPosition,
				scrollLeft = 0, dragX, i;
			if (this._hscroller.length > 0) {
				scrollLeft = this._hscroller.scrollLeft();
			}
			/* depending on the direction we are moving the helper we get the front we'll try moving for */
			dragX = left === true ? pos.left : pos.left + helper.width();
			dragX += this._getAbsoluteScroll(this.grid.headersTable());
			if (!this._gridReady() || this._movingDirty) {
				return;
			}
			for (i = 0; i < movingOpts.length; i++) {
				mo = movingOpts[ i ];
				/* the moving opt should be visible (can be hidden under the fixed area */
				if (this._movingOptionNotVisible(mo)) {
					continue;
				}
				targetLeft = mo.position;
				/* we have the two X coordinates to test against
				(dragX for the helper; targetLeft for the column) */
				if (dragX > targetLeft - this.options.movingAcceptanceTolerance &&		// left acceptance border
					dragX < targetLeft + this.options.movingAcceptanceTolerance) {		// right acceptance border
					this._activeMO = mo;
					/* moving will  be accepted, check the moving arg */
					if (dragX - targetLeft > 0) {
						/* border is left of drag edge */
						this._tKey = mo.after || mo.before;
						this._aft = mo.after ? false : true;
						targetPosition = this._cache.columns[ this._tKey ];
						this._showIndicators(targetLeft - this._getAbsoluteScroll(this.grid.headersTable()),
							targetPosition.element.offset().top,
							targetPosition.dimensions,
							mo.fixing ? "right" : null);
					} else {
						this._tKey = mo.before || mo.after;
						this._aft = mo.before ? true : false;
						targetPosition = this._cache.columns[ this._tKey ];
						this._showIndicators(targetLeft - this._getAbsoluteScroll(this.grid.headersTable()),
							targetPosition.element.offset().top,
							targetPosition.dimensions,
							mo.fixing ? "left" : null);
					}
					break;
				}
			}
			if (this._hscroller.length > 0) {
				this._checkScroll(scrollLeft, dragX, left);
			}
			return false;
		},
		_thDragged: function (helper, pos, left) {
			var movingOpts = this._moArrays[ this._cKey ], mo, targetLeft,
				scrollLeft = 0, dragX, i, after, updateEvent = false;
			if (this._hscroller.length > 0) {
				scrollLeft = this._hscroller.scrollLeft();
			}
			/* depending on the direction we are moving the
			helper we get the front we'll try moving for */
			dragX = left === true ? pos.left : pos.left + helper.width();
			dragX += this._getAbsoluteScroll(this.grid.headersTable());
			if (!this._gridReady() || this._movingDirty) {
				return;
			}
			for (i = 0; i < movingOpts.length; i++) {
				mo = movingOpts[ i ];
				targetLeft = mo.position;
				/* we have the two X coordinates to test against
				(dragX for the helper; targetLeft for the column) */
				if (dragX > targetLeft - this.options.movingAcceptanceTolerance &&		// left acceptance border
					dragX < targetLeft + this.options.movingAcceptanceTolerance) {		// right acceptance border
					this._activeMO = mo;
					after = dragX - targetLeft <= 0 && mo.before !== null;
					if (!mo.fixing && ((mo.after === this._cKey && after) ||
						(mo.before === this._cKey && !after))) {
						continue;
					}
					this._tKey = after ? mo.before : mo.after;
					if ((this._cKey !== this._tKey && this._cKey !== this._activeMO.after) ||
						(this._activeMO.fixing === true && this._tKey !== this._cKey)) {
						if (mo.fixing && (mo.after === this._cKey || mo.before === this._cKey)) {
							/* protect from changing fixing prop of currently dragged
							col when the drag position doesn't have large enough delta */
							if (Math.abs(dragX - targetLeft) < this._cache.columns[ this._tKey ].dimensions.width / 3) {
								continue;
							}
						}
						if (this._cKey && this._tKey) {
							this._tIdx = this.grid._getCellIndexByColumnKey(this._tKey);
							this._moveColumn(this._cKey, this._tKey, after, true, false);
						}
						updateEvent = true;
						break;
					}
				}
			}
			if (this._hscroller.length > 0) {
				this._checkScroll(scrollLeft, dragX, left);
			}
			return updateEvent;
		},
		_thDropped: function () {
			if (this.options.mode === "immediate") {
				if (this.options.hideHeaderContentsDuringDrag === true) {
					this._oTh.children().show();
				}
				this._scroller.on = false;
				return;
			}
			this._hideIndicators();
			/* tar = this._activeMO.after || this._activeMO.before;
			after = this._activeMO.after ? false : true; */
			if (this._activeMO &&
				((this._cKey !== this._tKey && this._cKey !== this._activeMO.after) ||
				(this._activeMO.fixing === true && this._tKey !== this._cKey))) {
				this._moveColumn(this._cKey, this._tKey, this._aft, this.options.moveType === "dom", false);
			}
			delete this._cKey;
			delete this._tKey;
			delete this._aft;
			delete this._activeMO;
			$(".ui-draggable-dragging").hide();
			this._scroller.on = false;
		},
		_isColumnMovable: function (col, idx) {
			var i, cs;
			for (i = 0; i < this.options.columnSettings.length; i++) {
				cs = this.options.columnSettings[ i ];
				if (cs.columnIndex) {
					if (i === idx) {
						return cs.allowMoving;
					}
				}
				if (cs.columnKey) {
					if (col === cs.columnKey) {
						return cs.allowMoving;
					}
				}
			}
			return true;
		},
		_isColumnGroupable: function (col) {
			var gbColumnSetting = this._groupBy._findColumnSetting(this._getIdOfTh(col));
			return gbColumnSetting && gbColumnSetting.allowGrouping;
		},
		_getIdOfTh: function (th) {
			var mchid = th.attr("data-mch-id");
			if (mchid) {
				return mchid;
			}
			return th.attr("id").replace(this.grid.id() + "_", "");
		},
		_getThById: function (id) {
			return $("th[data-mch-id='" + id + "'],th[id='" + this.grid.id() + "_" + id + "']");
		},
		_adjustIndicators: function (delta, fromParent) {
			var newl = parseInt(this._cmib.css("left"), 10) - delta, fhr, fhrt;
			this._cmib.css("left", newl);
			this._cmit.css("left", newl);
			if (fromParent) {
				fhr = this.grid.headersTable().find("tr").first();
				fhrt = fhr.offset().top;
				this._cmib.css("top", fhrt - 16 - $.ig.util.getRelativeOffset(this._cmib).top);
				this._cmit.css("top", fhrt + fhr.height() - $.ig.util.getRelativeOffset(this._cmit).top);
			}
		},
		_showIndicators: function (left, top, dim, tilt) {
			if (tilt === "left") {
				this._cmib.removeClass(this.css.dragIndicatorRight).addClass(this.css.dragIndicatorLeft);
				this._cmit.removeClass(this.css.dragIndicatorRight).addClass(this.css.dragIndicatorLeft);
				this._cmit.css("left", left - 12 - $.ig.util.getRelativeOffset(this._cmib).left).show();
				this._cmit.css("top", top - 14 - $.ig.util.getRelativeOffset(this._cmib).top);
				this._cmib.css("left", left - 12 - $.ig.util.getRelativeOffset(this._cmit).left).show();
				this._cmib.css("top", top + dim.height - 2 - $.ig.util.getRelativeOffset(this._cmit).top);
			} else if (tilt === "right") {
				this._cmib.removeClass(this.css.dragIndicatorLeft).addClass(this.css.dragIndicatorRight);
				this._cmit.removeClass(this.css.dragIndicatorLeft).addClass(this.css.dragIndicatorRight);
				this._cmit.css("left", left - 4 - $.ig.util.getRelativeOffset(this._cmib).left).show();
				this._cmit.css("top", top - 14 - $.ig.util.getRelativeOffset(this._cmib).top);
				this._cmib.css("left", left - 4 - $.ig.util.getRelativeOffset(this._cmit).left).show();
				this._cmib.css("top", top + dim.height - 2 - $.ig.util.getRelativeOffset(this._cmit).top);
			} else {
				this._cmib.removeClass(this.css.dragIndicatorRight).removeClass(this.css.dragIndicatorLeft);
				this._cmit.removeClass(this.css.dragIndicatorRight).removeClass(this.css.dragIndicatorLeft);
				this._cmit.css("left", left - 8 - $.ig.util.getRelativeOffset(this._cmib).left).show();
				this._cmit.css("top", top - 16 - $.ig.util.getRelativeOffset(this._cmib).top);
				this._cmib.css("left", left - 8 - $.ig.util.getRelativeOffset(this._cmit).left).show();
				this._cmib.css("top", top + dim.height - $.ig.util.getRelativeOffset(this._cmit).top);
			}
		},
		_hideIndicators: function () {
			this._cmib.hide();
			this._cmit.hide();
		},
		_markForMoving: function (col) {
			// check if another feature created a draggable
			// on the column prior to ColumnMoving
			if (col.data("draggable") || this._groupBy) {
				if (this._isColumnGroupable(col)) {
					col.bind("drag", this._moveHandler);
					col.bind("dragstop", this._dropHandler);
					col.bind("dragstart", this._dragHandler);
					return;
				}
				col.attr("groupby-disabled", "true");
			}
			/* otherwise we'll create a new draggable on the column */
			col.draggable({
				containment: this.grid._rootContainer(),
				appendTo: this.grid.container(),
				distance: 5,
				scroll: false, // will need to disable scroll so the helper doesn't scroll parent layouts automatically
				revert: "valid",
				helper: function (event) {
					var th, clone;
					th = $(event.target).closest("th");
					clone = th.clone()
						.css("overflow", "hidden")
						.css("z-index", 10000)
						.width(th.width())
						.height(th.height())
						.attr("data-mch-id", null)
						.attr("id", null)
						.addClass("ui-widget ui-iggrid")
						.wrap($("<div class=\"ui-iggrid-dragMarkup\"></div>")
							.width(th.outerWidth()).height(th.outerHeight()))
						.parent();
					clone.find("div[data-hiddencolindicator]").remove();
					return clone;
				},
				opacity: this.options.dragHelperOpacity,
				drag: this._moveHandler,
				stop: this._dropHandler,
				start: this._dragHandler
			});
			/* finally add style */
			col.unbind("mouseover.moving").bind("mouseover.moving", this._headerMouseOverHandler);
			col.unbind("mouseout.moving").bind("mouseout.moving", this._headerMouseOutHandler);
		},
		_updateLayout: function () {
			var cols, self = this;
			if (!this._gridReady()) {
				setTimeout(function () {
					self._updateLayout();
				}, 50);
				return;
			}
			if (this.grid._oldCols) {
				cols = $.grep(this.grid._oldCols, this.grid._columnVisible);
			} else {
				cols = this.grid._visibleColumns();
			}
			/* update hscroller (if the grid is resized) */
			this._hscroller = this.grid._hscrollbarcontent();
			this._hscroller = this._hscroller.length > 0 ? this._hscroller : this.grid.scrollContainer();
			/* rebuild cache */
			this._cache.columns = {};
			this._cache.siblings = {};
			this._cache.fixedSiblings = {};
			this._updateLayoutPerLevel(cols, true);
			this._createMovingOptions();
			delete this._movingDirty;
		},
		_updateLayoutPerLevel: function (cols, firstLevel) {
			var i, id, $th, os;
			for (i = 0; i < cols.length; i++) {
				id = cols[ i ].key || cols[ i ].identifier;
				$th = this._getThById(id);
				os = $th.offset();
				if (!os || $.type(os.left) !== "number") {
					/* jQuery's API docs state that offset of a hidden element is undefined
					however it still returns 0, 0 in every tested case - this is just for safety */
					os = { left: 0, top: 0 };
				}
				os.left = os.left + this._getAbsoluteScroll($th);
				this._cache.columns[ id ] = {};
				this._cache.columns[ id ].element = $th;
				this._cache.columns[ id ].offset = os;
				this._cache.columns[ id ].dimensions = { width: $th.outerWidth(), height: $th.outerHeight() };
				if (firstLevel) {
					this._cache.fixedSiblings[ id ] = this._fixingFilter(cols, true);
					this._cache.siblings[ id ] = this._fixingFilter(cols, false);
				} else {
					this._cache.siblings[ id ] = cols;
				}
			}
			for (i = 0; i < cols.length; i++) {
				if (cols[ i ].group && cols[ i ].hidden !== true) {
					this._updateLayoutPerLevel($.grep(cols[ i ].group, this.grid._columnVisible));
				}
			}
		},
		_createMovingOptions: function () {
			var col,
				us = this._cache.siblings,			// all unfixed siblings
				fs = this._cache.fixedSiblings,		// all fixed siblings
				fixingDir = this.grid.fixingDirection(),
				umo, fmo, mo;
			this._moArrays = {};
			for (col in us) {
				if (us.hasOwnProperty(col)) {
					umo = this._movingOptsForSiblings(us[ col ]);
					if (fs.hasOwnProperty(col)) {
						fmo = this._movingOptsForSiblings(fs[ col ]);
					} else {
						fmo = [];
					}
					if (umo.length && fmo.length) {
						mo = this._combineMovingOptions(umo, fmo, fixingDir);
					} else if (umo.length) {
						mo = umo;
					}
					this._moArrays[ col ] = mo;
				}
			}
			for (col in fs) {
				if (fs.hasOwnProperty(col) && !this._moArrays[ col ]) {
					fmo = this._movingOptsForSiblings(fs[ col ]);
					this._moArrays[ col ] = fmo;
				}
			}
		},
		_movingOptsForSiblings: function (siblings) {
			var mo = [], pkey, ckey, i;
			if (siblings && siblings.length) {
				pkey = siblings[ 0 ].key || siblings[ 0 ].identifier;
				/* add first moving option */
				mo.push({
					before: null,
					after: pkey,
					position: this._cache.columns[ pkey ].offset.left,
					fixing: false
				});
				for (i = 1; i < siblings.length; i++) {
					ckey = siblings[ i ].key || siblings[ i ].identifier;
					mo.push({
						before: pkey,
						after: ckey,
						position: this._cache.columns[ ckey ].offset.left,
						fixing: false
					});
					pkey = ckey;
				}
				/* add last moving option */
				mo.push({
					before: pkey,
					after: null,
					position: this._cache.columns[ pkey ].offset.left +
						this._cache.columns[ pkey ].dimensions.width,
					fixing: false
				});
			}
			return mo;
		},
		_combineMovingOptions: function (umo, fmo, fixingDir) {
			var combined, left, right;
			if (fixingDir === "left") {
				left = fmo;
				right = umo;
			} else {
				left = umo;
				right = fmo;
			}
			combined = left.slice(0, left.length - 1);
			combined.push({
				before: left[ left.length - 1 ].before,
				after: right[ 0 ].after,
				position: right[ 0 ].position,
				fixing: true
			});
			combined = combined.concat(right.slice(1, right.length));
			return combined;
		},
		_getAbsoluteScroll: function (el) {
			var cel = el, abs = 0;
			while (cel.length > 0 && cel.attr("id") !== this.grid._rootContainer().attr("id")) {
				abs += cel.scrollLeft();
				cel = cel.parent();
			}
			return abs;
		},
		_openMovingDialog: function () {
			this._movingDialog.igGridModalDialog("openModalDialog");
		},
		_closeMovingDialog: function (e) {
			this._movingDialog.igGridModalDialog("closeModalDialog");
			if (e) {
				e.preventDefault();
			}
		},
		_triggerColumnMovedAsync: function (args) {
			var self = this;
			if (!this._gridReady()) {
				setTimeout(function () {
					self._triggerColumnMovedAsync(args);
				}, 50);
				return;
			}
			this._trigger(this.events.columnMoved, null, args);
		},
		_gridReady: function () {
			return !(this.grid._loadingIndicator && this.grid._loadingIndicator._indicator.is(":visible"));
		},
		_bindUpDownDialogButtons: function (list) {
			list.find("span[class='" + this.css.movingDialogListItemArrowDown + "']")
				.unbind("mousedown", this._dialogDownHandler)
				.bind("mousedown", this._dialogDownHandler);
			list.find("span[class='" + this.css.movingDialogListItemArrowUp + "']")
				.unbind("mousedown", this._dialogUpHandler)
				.bind("mousedown", this._dialogUpHandler);
		},
		_checkScroll: function (scrollLeft, dragX, left) {
			var sbw = this._verticalScrollBarWidth();
			if (this._currentlyDraggedFixed) {
				/* the currently dragged column is fixed - > therefore moving should trigger first
				to unfixed area before changing the scroll position is available */
				return;
			}
			/* we need to consider having a fixed area
			the scroll area is not part of it */
			dragX -= this._hscroller.offset().left;
			if (scrollLeft > 0 &&
				left === true &&
				Math.abs(dragX - scrollLeft) < this.options.movingScrollTolerance) {
				this._updateScroller(true, dragX - scrollLeft + 1);
			} else if (scrollLeft < this._hscroller[ 0 ].scrollWidth + sbw - this._hscroller.outerWidth() &&
				left === false &&
				dragX - scrollLeft > this._hscroller.outerWidth() - this.options.movingScrollTolerance &&
				dragX - scrollLeft < this._hscroller.outerWidth() + this.options.movingScrollTolerance) {
				this._updateScroller(false, this._hscroller.outerWidth() - dragX + scrollLeft);
			} else {
				this._stopScroller();
			}
		},
		_startScroller: function (left, delta) {
			this._scroller.on = true;
			this._scroller.delay = delta * this.options.scrollSpeedMultiplier;
			this._scroller.direction = left === true ? "left" : "right";
			this._autoScroll();
		},
		_updateScroller: function (left, delta) {
			if (this._scroller.on === false) {
				this._startScroller(left, delta);
				return;
			}
			this._scroller.delay = delta;
		},
		_stopScroller: function () {
			this._scroller.on = false;
		},
		_scrollGrid: function () {
			var sl = this._hscroller.scrollLeft(), delta, res, sbw;
			delta = this._scroller.direction === "left" ?
				this.options.scrollDelta * -1 : this.options.scrollDelta;
			res = sl + delta;
			sbw = this._verticalScrollBarWidth();
			this._hscroller.scrollLeft(res);
			if ((res <= 0 && delta < 0) ||
					(res >= this._hscroller[ 0 ].scrollWidth + sbw - this._hscroller.outerWidth() && delta > 0)) {
				this._stopScroller();
			}
			if (this._hscroller[ 0 ].id.indexOf("_hscroller") > 0) {
				this.grid._synchronizeHScroll();
			}
			if (this.options.mode === "deferred") {
				if (this._activeMO && this._movingOptionNotVisible(this._activeMO)) {
					this._hideIndicators();
				} else {
					this._adjustIndicators(delta);
				}
			}
			if (this._scroller.on === true) {
				setTimeout(this._autoScroll, this._scroller.delay);
			}
		},
		_cancelEvent: function (e) {
			e.stopPropagation();
			e.preventDefault();
		},
		_columnMap: function () {
			/* function used by the FeatureChooser */
			return $.map(this.grid.options.columns, function (col) {
				return { "columnKey": col.key, "enabled": true };
			});
		},
		_movingOptionNotVisible: function (mo) {
			var fdir, sAdjLeft, fbodyL, fbodyW, sl, scW, scL;
			if (!this.grid.hasFixedColumns() ||
				this.grid.isFixedColumn(mo.before) ||
				this.grid.isFixedColumn(mo.after) ||
				!this._hscroller.is(":visible")) {
				return false;
			}
			fdir = this.grid.fixingDirection();
			sl = this._hscroller.scrollLeft();
			sAdjLeft = mo.position - sl;
			fbodyL = this.grid.fixedBodyContainer().offset().left;
			fbodyW = this.grid.fixedBodyContainer().outerWidth();
			scL = this._hscroller.offset().left;
			scW = this._hscroller.outerWidth();
			return (fdir === "left" && (sAdjLeft - fbodyL < fbodyW || sAdjLeft > scW + scL)) ||
				(fdir === "right" && (sAdjLeft < scL || sAdjLeft - scL > scW));
		},
		_verticalScrollBarWidth: function () {
			return this.grid._hasVerticalScrollbar === true ? this.grid._scrollbarWidth() : 0;
		},
		/* service */
		_removeMoving: function () {
			var ths = this.grid.headersTable().find(">thead th"), i, th, mdl, removeContainer, fc;
			/* selects all moving elements and removes them */
			if (this._cmit && this._cmit.length > 0) {
				if (this._cmit.data("users") === 1) {
					this._cmit.remove();
				} else {
					this._cmit.data("users", this._cmit.data("users") - 1);
				}
			}
			if (this._cmib && this._cmib.length > 0) {
				if (this._cmib.data("users") === 1) {
					this._cmib.remove();
				} else {
					this._cmib.data("users", this._cmib.data("users") - 1);
				}
			}
			if (this.options.addMovingDropdown === true) {
				mdl = this._movingDialog;
				if (mdl && mdl instanceof jQuery) {
					mdl.igGridModalDialog("destroy");
					mdl.remove();
				}
				$("div[data-moving-inddropdown='" + this.grid.id() + "']").remove();
			}
			fc = this.grid.element.data("igGridFeatureChooser");
			removeContainer = true;
			if (this.renderInFeatureChooser && fc) {
				fc._removeFeature("ColumnMovingDropDown", true);
				removeContainer = false;
			}
			for (i = 0; i < ths.length; ++i) {
				th = $(ths[ i ]);
				if (removeContainer) {
					th.find(">div").find("span").unbind("mousedown", this._dropDownButtonHandler);
					th.find(">div").remove();
				}
				th.unbind("mouseover.moving", this._headerMouseOverHandler);
				th.unbind("mouseout.moving", this._headerMouseOutHandler);
				if (!this._grpByEnabled) {
					if (th.data && th.data("draggable")) {
						th.draggable("destroy");
					}
				} else {
					th.unbind("draggabledrag", this._moveHandler);
					th.unbind("draggablestop", this._dropHandler);
					th.unbind("draggablestart", this._dragHandler);
				}
			}
		},
		_unregisterEvents: function () {
			this.grid.element.unbind("iggridheadercellrendered", this._headerCellRenderedHandler);
			this.grid.element.unbind("iggridheaderrendering", this._headerRenderingHandler);
			this.grid.element.unbind("iggridheaderrendered", this._headerRenderedHandler);
			this.grid.element.unbind("iggridrendered", this._gridFullyRenderedHandler);
			this.grid.element.unbind("iggridresizingcolumnresized", this._columnStateChanged);
			this.grid.element.unbind("iggridcolumnscollectionmodified", this._columnStateChanged);
			this.grid.element.unbind("iggridpagingpagingdropdownrendered", this._columnStateChanged);
			this.grid.element.unbind("iggrid_columnsmoved", this._columnStateChanged);
			this.grid.headersTable().find("th").unbind("moving");
		},
		_registerEvents: function () {
			this.grid.element.bind("iggridheadercellrendered", this._headerCellRenderedHandler);
			this.grid.element.bind("iggridheaderrendering", this._headerRenderingHandler);
			this.grid.element.bind("iggridheaderrendered", this._headerRenderedHandler);
			this.grid.element.bind("iggridrendered", this._gridFullyRenderedHandler);
			/* column state change events */
			this.grid.element.bind("iggridresizingcolumnresized", this._columnStateChanged);
			this.grid.element.bind("iggridcolumnscollectionmodified", this._columnStateChanged);
			this.grid.element.bind("iggridpagingpagingdropdownrendered", this._columnStateChanged);
			this.grid.element.bind("iggrid_columnsmoved", this._columnStateChanged);
		},
		_createHandlers: function () {
			this._headerCellRenderedHandler = $.proxy(this._headerCellRendered, this);
			this._headerRenderingHandler = $.proxy(this._headerRendering, this);
			this._headerRenderedHandler = $.proxy(this._headerRendered, this);
			this._gridFullyRenderedHandler = $.proxy(this._gridFullyRendered, this);
			/* draggable */
			this._dragHandler = $.proxy(this._headerPicked, this);
			this._moveHandler = $.proxy(this._headerDragged, this);
			this._dropHandler = $.proxy(this._headerDropped, this);
			/* header mouseover */
			this._headerMouseOverHandler = $.proxy(this._headerMouseOver, this);
			this._headerMouseOutHandler = $.proxy(this._headerMouseOut, this);
			/* dropdown */
			this._dropDownButtonHandler = $.proxy(this._dropDownMouseDown, this);
			this._dropDownKeyHandler = $.proxy(this._dropDownKeyDown, this);
			this._openMovingDialogHandler = $.proxy(this._openMovingDialog, this);
			this._dropDownButtonMouseOverHandler = $.proxy(this._dropDownButtonMouseOver, this);
			this._dropDownButtonMouseOutHandler = $.proxy(this._dropDownButtonMouseOut, this);
			/* dialog */
			this._dialogOpeningHandler = $.proxy(this._dialogOpening, this);
			this._dialogDraggedHandler = $.proxy(this._dialogDragged, this);
			this._dialogClosingHandler = $.proxy(this._dialogClosing, this);
			this._dialogClosedHandler = $.proxy(this._dialogClosed, this);
			this._dialogCloseButtonHandler = $.proxy(this._closeMovingDialog, this);
			this._dialogUpHandler = $.proxy(this._dialogUpPressed, this);
			this._dialogDownHandler = $.proxy(this._dialogDownPressed, this);
			this._dialogRearrangingHandler = $.proxy(this._dialogRearranging, this);
			this._dialogRearrangedHandler = $.proxy(this._dialogRearranged, this);
			/* cache rebuild */
			this._columnStateChanged = $.proxy(this._updateLayout, this);
			/* cancel events */
			this._cancelEventHandler = $.proxy(this._cancelEvent, this);
			/* auto scroller */
			this._autoScroll = $.proxy(this._scrollGrid, this);
			/* scroller events */
			this._containerScrolledHandler = $.proxy(this._containerScrolled, this);
		},
		/* inject grid */
		_injectGrid: function (gridInstance, isRebind) {
			this.grid = gridInstance;
			if (isRebind === true) {
				return;
			}
			/* init igGridFeatureChooser */
			if (this.grid.element.igGridFeatureChooser !== undefined) {
				this.grid.element.igGridFeatureChooser();
			} else {
				throw new Error($.ig.ColumnMoving.locale.featureChooserNotReferenced);
			}
			this._gridMoveColumn = this.grid.moveColumn;
			this.grid.moveColumn = $.proxy(this._moveColumnOverwrite, this);
			this._createHandlers();
			this._unregisterEvents();
			this._registerEvents();
		}
	});
	$.extend($.ui.igGridColumnMoving, { version: "16.1.20161.2145" });
}(jQuery));
