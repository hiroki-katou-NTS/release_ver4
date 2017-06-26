/*!@license
* Infragistics.Web.ClientUI infragistics.chart_polarchart.js 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/

(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["AbstractEnumerable:a", 
"Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Func$1:ba", 
"MulticastDelegate:bb", 
"IntPtr:bc", 
"AbstractEnumerator:bd", 
"Array:bo", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Func$2:bf", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Delegate:bj", 
"Interlocked:bk", 
"Script:cc", 
"Action$1:ch", 
"EventArgs:cl", 
"UIElement:cm", 
"DependencyObject:cn", 
"Dictionary:co", 
"DependencyProperty:cp", 
"PropertyMetadata:cq", 
"PropertyChangedCallback:cr", 
"DependencyPropertyChangedEventArgs:cs", 
"DependencyPropertiesCollection:ct", 
"UnsetValue:cu", 
"Binding:cv", 
"PropertyPath:cw", 
"Transform:cx", 
"List$1:cz", 
"IList$1:c0", 
"ICollection$1:c1", 
"IArray:c2", 
"IArrayList:c3", 
"Array:c4", 
"CompareCallback:c5", 
"Func$3:c6", 
"Comparer$1:c7", 
"IComparer:c8", 
"IComparer$1:c9", 
"DefaultComparer$1:da", 
"Comparison$1:db", 
"ReadOnlyCollection$1:dc", 
"Predicate$1:dd", 
"NotImplementedException:de", 
"NotifyCollectionChangedEventArgs:d1", 
"NotifyCollectionChangedAction:d2", 
"Dictionary$2:d8", 
"IDictionary$2:d9", 
"IDictionary:ea", 
"KeyValuePair$2:eb", 
"Enumerable:ec", 
"Thread:ed", 
"ThreadStart:ee", 
"IOrderedEnumerable$1:ef", 
"SortedList$1:eg", 
"ArgumentNullException:eh", 
"IEqualityComparer$1:ei", 
"EqualityComparer$1:ej", 
"IEqualityComparer:ek", 
"DefaultEqualityComparer$1:el", 
"InvalidOperationException:em", 
"ArgumentException:en", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"Geometry:e1", 
"GeometryType:e2", 
"DataTemplateRenderInfo:e7", 
"DataTemplatePassInfo:e8", 
"ContentControl:e9", 
"Control:fa", 
"Thickness:fb", 
"HorizontalAlignment:fc", 
"VerticalAlignment:fd", 
"DataTemplate:fe", 
"DataTemplateRenderHandler:ff", 
"DataTemplateMeasureHandler:fg", 
"DataTemplateMeasureInfo:fh", 
"DataTemplatePassHandler:fi", 
"GeometryGroup:fx", 
"GeometryCollection:fy", 
"FillRule:fz", 
"PathGeometry:f0", 
"PathFigureCollection:f1", 
"PathFigure:f9", 
"PathSegmentCollection:ga", 
"GeometryUtil:gf", 
"Tuple$2:gg", 
"INotifyPropertyChanged:gn", 
"PropertyChangedEventHandler:go", 
"PropertyChangedEventArgs:gp", 
"RectUtil:g2", 
"MathUtil:g3", 
"RuntimeHelpers:g4", 
"RuntimeFieldHandle:g5", 
"ObservableCollection$1:hb", 
"INotifyCollectionChanged:hc", 
"NotifyCollectionChangedEventHandler:hd", 
"ISmartPlaceable:hu", 
"SmartPosition:hv", 
"SmartPlaceableWrapper$1:hw", 
"SmartPlacer:hx", 
"Func$1:ib", 
"Extensions:jf", 
"Panel:jg", 
"UIElementCollection:jh", 
"RearrangedList$1:js", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('SmartPosition', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "LeftTop";
			case 1: return "CenterTop";
			case 2: return "RightTop";
			case 3: return "LeftCenter";
			case 4: return "CenterCenter";
			case 5: return "RightCenter";
			case 6: return "LeftBottom";
			case 7: return "CenterBottom";
			case 8: return "RightBottom";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('SmartPosition', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('ISmartPlaceable', 'Object', {
	$type: new $.ig.Type('ISmartPlaceable', null)
}, true);

$.ig.util.defType('SmartPlaceableWrapper$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this._elementDesiredSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
		this.noWiggle(false);
	},
	_noWiggle: false,
	noWiggle: function (value) {
		if (arguments.length === 1) {
			this._noWiggle = value;
			return value;
		} else {
			return this._noWiggle;
		}
	}
	,
	__element: null,
	element: function (value) {
		if (arguments.length === 1) {
			this.__element = value;
			return value;
		} else {
			return this.__element;
		}
	}
	,
	_elementLocationResult: null,
	elementLocationResult: function (value) {
		if (arguments.length === 1) {
			this._elementLocationResult = value;
			return value;
		} else {
			return this._elementLocationResult;
		}
	}
	,
	_originalLocation: null,
	originalLocation: function (value) {
		if (arguments.length === 1) {
			this._originalLocation = value;
			return value;
		} else {
			return this._originalLocation;
		}
	}
	,
	getSmartPositions: function () {
		if (this.noWiggle()) {
			return this.$type.getStaticFields($.ig.SmartPlaceableWrapper$1.prototype.$type)._smartPositionDefault;
		} else {
			return this.$type.getStaticFields($.ig.SmartPlaceableWrapper$1.prototype.$type)._smartPositions;
		}
	}
	,
	_elementDesiredSize: null,
	elementDesiredSize: function (value) {
		if (arguments.length === 1) {
			this._elementDesiredSize = value;
			return value;
		} else {
			return this._elementDesiredSize;
		}
	}
	,
	getSmartElementSize: function () {
		return this.elementDesiredSize();
	}
	,
	getSmartBounds: function (position) {
		var s = this.getSmartElementSize();
		var w = s.width();
		var h = s.height();
		var d;
		{
			d = this.getOffset(position, w, h);
		}
		return new $.ig.Rect(0, this.originalLocation().__x + d.__x, this.originalLocation().__y + d.__y, w, h);
	}
	,
	opacity: function (value) {
		if (arguments.length === 1) {
			this.element().__opacity = value;
			return value;
		} else {
			return this.element().__opacity;
		}
	}
	,
	smartPosition: function (value) {
		if (arguments.length === 1) {
			this._smartPosition = value;
			var s = this.getSmartElementSize();
			var h = s.height();
			var w = s.width();
			var d;
			{
				d = this.getOffset(this._smartPosition, w, h);
			}
			this.elementLocationResult({ __x: this.originalLocation().__x + d.__x, __y: this.originalLocation().__y + d.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return value;
		} else {
			return this._smartPosition;
		}
	}
	,
	_smartPosition: 0,
	getOffset: function (position, w, h) {
		var c = 0.25;
		switch (position) {
			case $.ig.SmartPosition.prototype.leftTop: return { __x: -w * c, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerTop: return { __x: 0, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.rightTop: return { __x: w * c, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.leftCenter: return { __x: -w * c, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerCenter: return { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.rightCenter: return { __x: w * c, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.leftBottom: return { __x: -w * c, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerBottom: return { __x: 0, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			default: return { __x: w * c, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	$type: new $.ig.Type('SmartPlaceableWrapper$1', $.ig.Object.prototype.$type, [$.ig.ISmartPlaceable.prototype.$type], function () {
		this._smartPositionDefault = [ $.ig.SmartPosition.prototype.centerCenter ];
		this._smartPositions = [ $.ig.SmartPosition.prototype.centerCenter, $.ig.SmartPosition.prototype.rightCenter, $.ig.SmartPosition.prototype.rightTop, $.ig.SmartPosition.prototype.centerTop, $.ig.SmartPosition.prototype.rightBottom, $.ig.SmartPosition.prototype.centerBottom, $.ig.SmartPosition.prototype.leftTop, $.ig.SmartPosition.prototype.leftCenter, $.ig.SmartPosition.prototype.leftBottom ];
	})
}, true);

$.ig.util.defType('SmartPlacer', 'Object', {
	init: function () {
		this._placed = new $.ig.List$1($.ig.Rect.prototype.$type, 0);
		$.ig.Object.prototype.init.call(this);
		this.bounds($.ig.Rect.prototype.empty());
		this.overlap(0.3);
		this.fade(2);
	},
	_bounds: null,
	bounds: function (value) {
		if (arguments.length === 1) {
			this._bounds = value;
			return value;
		} else {
			return this._bounds;
		}
	}
	,
	_overlap: 0,
	overlap: function (value) {
		if (arguments.length === 1) {
			this._overlap = value;
			return value;
		} else {
			return this._overlap;
		}
	}
	,
	_fade: 0,
	fade: function (value) {
		if (arguments.length === 1) {
			this._fade = value;
			return value;
		} else {
			return this._fade;
		}
	}
	,
	place: function (smartPlaceable) {
		if (smartPlaceable == null) {
			return;
		}
		var minScore = 1.7976931348623157E+308;
		var minBounds = $.ig.Rect.prototype.empty();
		var minPosition = $.ig.SmartPosition.prototype.centerBottom;
		var hasMinPosition = false;
		var $t = smartPlaceable.getSmartPositions();
		for (var i = 0; i < $t.length; i++) {
			var position = $t[i];
			var bounds = smartPlaceable.getSmartBounds(position);
			if (this.bounds().isEmpty() || this.bounds().containsRect(bounds)) {
				var score = 0;
				var en = this._placed.getEnumerator();
				while (en.moveNext()) {
					var rect = en.current();
					score += $.ig.RectUtil.prototype.intersectionArea(bounds, rect);
				}
				if (score == 0) {
					minScore = score;
					minPosition = position;
					minBounds = bounds;
					hasMinPosition = true;
					break;
				}
				if (score < minScore) {
					minScore = score;
					minPosition = position;
					minBounds = bounds;
					hasMinPosition = true;
				}
			}
		}
		var overlap = 0;
		if (hasMinPosition) {
			overlap = minScore / $.ig.RectUtil.prototype.getArea(minBounds);
		}
		if (!hasMinPosition || overlap > this.overlap()) {
			smartPlaceable.opacity(0);
		} else {
			if (minScore > 0) {
				smartPlaceable.opacity(Math.pow(1 - $.ig.MathUtil.prototype.clamp(0, overlap, 1), this.fade()));
			} else {
				smartPlaceable.opacity(1);
			}
			smartPlaceable.smartPosition(minPosition);
			this._placed.add(minBounds);
		}
	}
	,
	_placed: null,
	$type: new $.ig.Type('SmartPlacer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Extensions', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	reset1: function (geom) {
		geom.figures().clear();
		geom.figures().add(new $.ig.PathFigure());
		geom.figures().removeAt(0);
	}
	,
	reset: function (geom) {
		geom.children().clear();
		geom.children().add(new $.ig.PathGeometry());
		geom.children().removeAt(0);
	}
	,
	detach: function (child) {
		if (child == null) {
			return;
		}
		var parent = $.ig.util.cast($.ig.Panel.prototype.$type, child.parent());
		if (parent != null) {
			parent.children().remove(child);
			return;
		}
		var cont = $.ig.util.cast($.ig.ContentControl.prototype.$type, child.parent());
		if (cont != null) {
			cont.content(null);
			return;
		}
	}
	,
	transferChildrenTo: function (from, to) {
		var transfer = new $.ig.List$1($.ig.UIElement.prototype.$type, 0);
		var en = $.ig.Enumerable.prototype.ofType$1($.ig.UIElement.prototype.$type, from.children()).getEnumerator();
		while (en.moveNext()) {
			var child = en.current();
			transfer.add(child);
		}
		var en1 = transfer.getEnumerator();
		while (en1.moveNext()) {
			var child1 = en1.current();
			from.children().remove(child1);
			to.children().add(child1);
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	isPlottable1: function (rect) {
		return !$.ig.util.isNaN(rect.left()) && !$.ig.util.isNaN(rect.right()) && !$.ig.util.isNaN(rect.top()) && !$.ig.util.isNaN(rect.bottom()) && !Number.isInfinity(rect.left()) && !Number.isInfinity(rect.right()) && !Number.isInfinity(rect.top()) && !Number.isInfinity(rect.bottom());
	}
	,
	$type: new $.ig.Type('Extensions', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RearrangedList$1', 'Object', {
	$t: null,
	__inner: null,
	__indexes: null,
	init: function ($t, inner, indexes) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__inner = inner;
		this.__indexes = indexes;
	},
	indexOf: function (item) {
		var innerIndex = this.__inner.indexOf(item);
		if (innerIndex == -1) {
			return -1;
		}
		return this.__indexes.indexOf(innerIndex);
	}
	,
	insert: function (index, item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	removeAt: function (index) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			return this.__inner.item(this.__indexes.item(index));
		}
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		this.__indexes.clear();
	}
	,
	contains: function (item) {
		return this.__inner.contains(item);
	}
	,
	copyTo: function (array, arrayIndex) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	count: function () {
		return this.__indexes.count();
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		var d__ = new $.ig.RearrangedList___GetEnumerator__IteratorClass$1(this.$t, 0);
		d__.__4__this = this;
		return d__;
	}
	,
	getEnumerator: function () {
		var d__ = new $.ig.RearrangedList___GetEnumerator__IteratorClass1$1(this.$t, 0);
		d__.__4__this = this;
		return d__;
	}
	,
	$type: new $.ig.Type('RearrangedList$1', $.ig.Object.prototype.$type, [$.ig.IList$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('RearrangedList___GetEnumerator__IteratorClass$1', 'Object', {
	$t: null,
	__1__state: 0,
	__2__current: null,
	_indEnumerator: null,
	__ind_5_0: 0,
	__4__this: null,
	init: function ($t, _1__state) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	_m_Finally0: function () {
		this.__1__state = -1;
		if (this._indEnumerator != null) {
			this._indEnumerator.dispose();
		}
	}
	,
	moveNext: function () {
		var __hasError__ = false;
		try {
			while (this.__1__state >= 0) {
				switch (this.__1__state) {
					case 0:
						this.__1__state = 1;
						this._indEnumerator = (this.__4__this.__indexes).getEnumerator();
						this.__1__state = 2;
						break;
					case 2:
						this.__1__state = 1;
						if (this._indEnumerator.moveNext()) {
							this.__ind_5_0 = this._indEnumerator.current();
							this.__2__current = this.__4__this.__inner.item(this.__ind_5_0);
							this.__1__state = 2;
							return true;
						}
						this._m_Finally0();
						break;
				}
			}
		}
		catch (e) {
			__hasError__ = true;
			throw e;
		}
		finally {
			if (__hasError__) {
				(this).dispose();
			}
		}
		return false;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
		switch (this.__1__state) {
			case 1:
			case 2:
				this._m_Finally0();
				break;
		}
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return $.ig.util.getBoxIfEnum(this.$t, this.__2__current);
	}
	,
	$type: new $.ig.Type('RearrangedList___GetEnumerator__IteratorClass$1', $.ig.Object.prototype.$type, [$.ig.IEnumerator$1.prototype.$type.specialize(0), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('RearrangedList___GetEnumerator__IteratorClass1$1', 'Object', {
	$t: null,
	__1__state: 0,
	__2__current: null,
	_indEnumerator: null,
	__ind_5_0: 0,
	__4__this: null,
	init: function ($t, _1__state) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	_m_Finally0: function () {
		this.__1__state = -1;
		if (this._indEnumerator != null) {
			this._indEnumerator.dispose();
		}
	}
	,
	moveNext: function () {
		var __hasError__ = false;
		try {
			while (this.__1__state >= 0) {
				switch (this.__1__state) {
					case 0:
						this.__1__state = 1;
						this._indEnumerator = (this.__4__this.__indexes).getEnumerator();
						this.__1__state = 2;
						break;
					case 2:
						this.__1__state = 1;
						if (this._indEnumerator.moveNext()) {
							this.__ind_5_0 = this._indEnumerator.current();
							this.__2__current = $.ig.util.getBoxIfEnum(this.$t, this.__4__this.__inner.item(this.__ind_5_0));
							this.__1__state = 2;
							return true;
						}
						this._m_Finally0();
						break;
				}
			}
		}
		catch (e) {
			__hasError__ = true;
			throw e;
		}
		finally {
			if (__hasError__) {
				(this).dispose();
			}
		}
		return false;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
		switch (this.__1__state) {
			case 1:
			case 2:
				this._m_Finally0();
				break;
		}
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('RearrangedList___GetEnumerator__IteratorClass1$1', $.ig.Object.prototype.$type, [$.ig.IEnumerator$1.prototype.$type.specialize($.ig.Object.prototype.$type), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.SmartPosition.prototype.leftTop = 0;
$.ig.SmartPosition.prototype.centerTop = 1;
$.ig.SmartPosition.prototype.rightTop = 2;
$.ig.SmartPosition.prototype.leftCenter = 3;
$.ig.SmartPosition.prototype.centerCenter = 4;
$.ig.SmartPosition.prototype.rightCenter = 5;
$.ig.SmartPosition.prototype.leftBottom = 6;
$.ig.SmartPosition.prototype.centerBottom = 7;
$.ig.SmartPosition.prototype.rightBottom = 8;

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["IProvidesViewport:a", 
"Void:b", 
"ValueType:c", 
"Object:d", 
"Type:e", 
"Boolean:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Series:be", 
"Control:bf", 
"FrameworkElement:bg", 
"UIElement:bh", 
"DependencyObject:bi", 
"Dictionary:bj", 
"DependencyProperty:bk", 
"PropertyMetadata:bl", 
"PropertyChangedCallback:bm", 
"MulticastDelegate:bn", 
"IntPtr:bo", 
"DependencyPropertyChangedEventArgs:bp", 
"DependencyPropertiesCollection:bq", 
"UnsetValue:br", 
"Script:bs", 
"Binding:bt", 
"PropertyPath:bu", 
"Transform:bv", 
"Visibility:bw", 
"Style:bx", 
"Thickness:by", 
"HorizontalAlignment:bz", 
"VerticalAlignment:b0", 
"INotifyPropertyChanged:b1", 
"PropertyChangedEventHandler:b2", 
"PropertyChangedEventArgs:b3", 
"SeriesView:b4", 
"ISchedulableRender:b5", 
"SeriesViewer:b6", 
"SeriesViewerView:b7", 
"CanvasRenderScheduler:b8", 
"List$1:b9", 
"IList$1:ca", 
"ICollection$1:cb", 
"IArray:cc", 
"IArrayList:cd", 
"Array:ce", 
"CompareCallback:cf", 
"Func$3:cg", 
"Action$1:ch", 
"Comparer$1:ci", 
"IComparer:cj", 
"IComparer$1:ck", 
"DefaultComparer$1:cl", 
"Comparison$1:cm", 
"ReadOnlyCollection$1:cn", 
"Predicate$1:co", 
"NotImplementedException:cp", 
"Callback:cq", 
"window:cr", 
"RenderingContext:cs", 
"IRenderer:ct", 
"Rectangle:cu", 
"Shape:cv", 
"Brush:cw", 
"Color:cx", 
"ArgumentException:cy", 
"DoubleCollection:cz", 
"Path:c0", 
"Geometry:c1", 
"GeometryType:c2", 
"TextBlock:c3", 
"Polygon:c4", 
"PointCollection:c5", 
"Polyline:c6", 
"DataTemplateRenderInfo:c7", 
"DataTemplatePassInfo:c8", 
"ContentControl:c9", 
"DataTemplate:da", 
"DataTemplateRenderHandler:db", 
"DataTemplateMeasureHandler:dc", 
"DataTemplateMeasureInfo:dd", 
"DataTemplatePassHandler:de", 
"Line:df", 
"FontInfo:dg", 
"XamOverviewPlusDetailPane:dh", 
"XamOverviewPlusDetailPaneView:di", 
"XamOverviewPlusDetailPaneViewManager:dj", 
"JQueryObject:dk", 
"Element:dl", 
"ElementAttributeCollection:dm", 
"ElementCollection:dn", 
"WebStyle:dp", 
"ElementNodeType:dq", 
"Document:dr", 
"EventListener:ds", 
"IElementEventHandler:dt", 
"ElementEventHandler:du", 
"ElementAttribute:dv", 
"JQueryPosition:dw", 
"JQueryCallback:dx", 
"JQueryEvent:dy", 
"JQueryUICallback:dz", 
"EventProxy:d0", 
"ModifierKeys:d1", 
"Func$2:d2", 
"MouseWheelHandler:d3", 
"Delegate:d4", 
"Interlocked:d5", 
"GestureHandler:d6", 
"ZoomGestureHandler:d7", 
"FlingGestureHandler:d8", 
"ContactHandler:d9", 
"TouchHandler:ea", 
"MouseOverHandler:eb", 
"MouseHandler:ec", 
"KeyHandler:ed", 
"Key:ee", 
"JQuery:ef", 
"JQueryDeferred:eg", 
"JQueryPromise:eh", 
"Action:ei", 
"CanvasViewRenderer:ej", 
"CanvasContext2D:ek", 
"CanvasContext:el", 
"TextMetrics:em", 
"ImageData:en", 
"CanvasElement:eo", 
"Gradient:ep", 
"LinearGradientBrush:eq", 
"GradientStop:er", 
"GeometryGroup:es", 
"GeometryCollection:et", 
"FillRule:eu", 
"PathGeometry:ev", 
"PathFigureCollection:ew", 
"LineGeometry:ex", 
"RectangleGeometry:ey", 
"EllipseGeometry:ez", 
"ArcSegment:e0", 
"PathSegment:e1", 
"PathSegmentType:e2", 
"SweepDirection:e3", 
"PathFigure:e4", 
"PathSegmentCollection:e5", 
"LineSegment:e6", 
"PolyLineSegment:e7", 
"BezierSegment:e8", 
"PolyBezierSegment:e9", 
"GeometryUtil:fa", 
"Tuple$2:fb", 
"TransformGroup:fc", 
"TransformCollection:fd", 
"TranslateTransform:fe", 
"RotateTransform:ff", 
"ScaleTransform:fg", 
"DivElement:fh", 
"BaseDOMEventProxy:fi", 
"DOMEventProxy:fj", 
"MSGesture:fk", 
"MouseEventArgs:fl", 
"EventArgs:fm", 
"DoubleAnimator:fn", 
"EasingFunctionHandler:fo", 
"ImageElement:fp", 
"RectUtil:fq", 
"MathUtil:fr", 
"RuntimeHelpers:fs", 
"RuntimeFieldHandle:ft", 
"PropertyChangedEventArgs$1:fu", 
"InteractionState:fv", 
"OverviewPlusDetailPaneMode:fw", 
"IOverviewPlusDetailControl:fx", 
"EventHandler$1:fy", 
"ArgumentNullException:fz", 
"OverviewPlusDetailViewportHost:f0", 
"XamDataChart:f1", 
"GridMode:f2", 
"BrushCollection:f3", 
"ObservableCollection$1:f4", 
"INotifyCollectionChanged:f5", 
"NotifyCollectionChangedEventHandler:f6", 
"NotifyCollectionChangedEventArgs:f7", 
"NotifyCollectionChangedAction:f8", 
"InterpolationMode:f9", 
"Random:ga", 
"ColorUtil:gb", 
"AxisCollection:gc", 
"XamDataChartView:gd", 
"SeriesViewerViewManager:ge", 
"AxisTitlePosition:gf", 
"PointerTooltipStyle:gg", 
"Dictionary$2:gh", 
"IDictionary$2:gi", 
"IDictionary:gj", 
"KeyValuePair$2:gk", 
"Enumerable:gl", 
"Thread:gm", 
"ThreadStart:gn", 
"IOrderedEnumerable$1:go", 
"SortedList$1:gp", 
"IEqualityComparer$1:gq", 
"EqualityComparer$1:gr", 
"IEqualityComparer:gs", 
"DefaultEqualityComparer$1:gt", 
"InvalidOperationException:gu", 
"CanvasGestureDOMEventProxy:gv", 
"TouchPointInfo:gw", 
"DOMExecutionContext:gx", 
"IExecutionContext:gy", 
"ExecutionContextExecuteCallback:gz", 
"TouchGestureRecognizer:g0", 
"TouchGestureState:g1", 
"TouchVelocityTracker:g2", 
"TouchHistoryItem:g3", 
"TouchVelocityReading:g4", 
"TouchGestureEventHandler:g5", 
"TouchGestureEventArgs:g6", 
"CancelableTouchGestureEventHandler:g7", 
"CssHelper:g8", 
"CssGradientUtil:g9", 
"FontUtil:ha", 
"TileZoomTile:hb", 
"TileZoomTileInfo:hc", 
"TileZoomTileCache:hd", 
"TileZoomManager:he", 
"RectChangedEventHandler:hf", 
"RectChangedEventArgs:hg", 
"Debug:hh", 
"TileZoomInfo:hi", 
"LinkedList$1:hj", 
"LinkedListNode$1:hk", 
"RenderSurface:hl", 
"FragmentBase:hm", 
"HorizontalAnchoredCategorySeries:hn", 
"AnchoredCategorySeries:ho", 
"CategorySeries:hp", 
"MarkerSeries:hq", 
"MarkerSeriesView:hr", 
"Marker:hs", 
"DataContext:ht", 
"MarkerTemplates:hu", 
"HashPool$2:hv", 
"IHashPool$2:hw", 
"IPool$1:hx", 
"Func$1:hy", 
"Pool$1:hz", 
"IIndexedPool$1:h0", 
"MarkerType:h1", 
"SeriesVisualData:h2", 
"PrimitiveVisualDataList:h3", 
"IVisualData:h4", 
"PrimitiveVisualData:h5", 
"PrimitiveAppearanceData:h6", 
"BrushAppearanceData:h7", 
"StringBuilder:h8", 
"Environment:h9", 
"AppearanceHelper:ia", 
"LinearGradientBrushAppearanceData:ib", 
"GradientStopAppearanceData:ic", 
"SolidBrushAppearanceData:id", 
"GeometryData:ie", 
"GetPointsSettings:ig", 
"EllipseGeometryData:ih", 
"RectangleGeometryData:ii", 
"LineGeometryData:ij", 
"PathGeometryData:ik", 
"PathFigureData:il", 
"SegmentData:im", 
"LineSegmentData:io", 
"PolylineSegmentData:ip", 
"ArcSegmentData:iq", 
"PolyBezierSegmentData:ir", 
"BezierSegmentData:is", 
"LabelAppearanceData:it", 
"ShapeTags:iu", 
"PointerTooltipVisualDataList:iv", 
"MarkerVisualDataList:iw", 
"MarkerVisualData:ix", 
"PointerTooltipVisualData:iy", 
"RectangleVisualData:iz", 
"PolygonVisualData:i0", 
"PolyLineVisualData:i1", 
"IFastItemsSource:i2", 
"IFastItemColumn$1:i3", 
"IFastItemColumnPropertyName:i4", 
"FastItemsSourceEventArgs:i5", 
"FastItemsSourceEventAction:i6", 
"IHasCategoryModePreference:i7", 
"IHasCategoryAxis:i8", 
"CategoryAxisBase:i9", 
"Axis:ja", 
"AxisView:jb", 
"StackedSeriesBase:jc", 
"IIsCategoryBased:jd", 
"CategoryMode:je", 
"ICategoryScaler:jf", 
"IScaler:jg", 
"ScalerParams:jh", 
"IBucketizer:ji", 
"IDetectsCollisions:jj", 
"StackedSeriesView:jk", 
"CategorySeriesView:jl", 
"ISupportsMarkers:jm", 
"CategoryBucketCalculator:jn", 
"ISortingAxis:jo", 
"CategoryFrame:jp", 
"Frame:jq", 
"BrushUtil:jr", 
"Canvas:js", 
"Panel:jt", 
"UIElementCollection:ju", 
"StackedBucketCalculator:jv", 
"StackedSeriesManager:jw", 
"StackedSeriesCollection:jx", 
"StackedFragmentSeries:jy", 
"PenLineCap:jz", 
"PropertyUpdatedEventHandler:j0", 
"PropertyUpdatedEventArgs:j1", 
"StackedAreaSeries:j2", 
"HorizontalStackedSeriesBase:j3", 
"NumericYAxis:j4", 
"StraightNumericAxisBase:j5", 
"NumericAxisBase:j6", 
"NumericAxisBaseView:j7", 
"NumericAxisRenderer:j8", 
"AxisRendererBase:j9", 
"ShouldRenderHandler:ka", 
"ScaleValueHandler:kb", 
"AxisRenderingParametersBase:kc", 
"RangeInfo:kd", 
"TickmarkValues:ke", 
"TickmarkValuesInitializationParameters:kf", 
"Func$4:kg", 
"GetGroupCenterHandler:kh", 
"GetUnscaledGroupCenterHandler:ki", 
"PathRenderingInfo:kj", 
"RenderStripHandler:kk", 
"RenderLineHandler:kl", 
"ShouldRenderLinesHandler:km", 
"ShouldRenderContentHandler:kn", 
"RenderAxisLineHandler:ko", 
"DetermineCrossingValueHandler:kp", 
"ShouldRenderLabelHandler:kq", 
"GetLabelLocationHandler:kr", 
"LabelPosition:ks", 
"TransformToLabelValueHandler:kt", 
"AxisLabelManager:ku", 
"AxisLabelPanelBase:kv", 
"AxisLabelPanelBaseView:kw", 
"AxisLabelSettings:kx", 
"AxisLabelsLocation:ky", 
"TitleSettings:kz", 
"GetLabelForItemHandler:k0", 
"CreateRenderingParamsHandler:k1", 
"SnapMajorValueHandler:k2", 
"AdjustMajorValueHandler:k3", 
"CategoryAxisRenderingParameters:k4", 
"LogarithmicTickmarkValues:k5", 
"LogarithmicNumericSnapper:k6", 
"Snapper:k7", 
"LinearTickmarkValues:k8", 
"LinearNumericSnapper:k9", 
"AxisRangeChangedEventArgs:la", 
"AxisRange:lb", 
"AutoRangeCalculator:lc", 
"NumericRadiusAxis:ld", 
"NumericRadiusAxisView:le", 
"NumericAngleAxis:lf", 
"IAngleScaler:lg", 
"NumericAngleAxisView:lh", 
"PolarAxisRenderingManager:li", 
"ViewportUtils:lj", 
"PolarAxisRenderingParameters:lk", 
"NumericAxisRenderingParameters:ll", 
"IPolarRadialRenderingParameters:lm", 
"RadialAxisRenderingParameters:ln", 
"AxisOrientation:lo", 
"AngleAxisLabelPanel:lp", 
"AngleAxisLabelPanelView:lq", 
"Extensions:lr", 
"CategoryAngleAxis:ls", 
"CategoryAngleAxisView:lt", 
"CategoryAxisBaseView:lu", 
"CategoryAxisRenderer:lv", 
"LinearCategorySnapper:lw", 
"CategoryTickmarkValues:lx", 
"RadialAxisLabelPanel:ly", 
"HorizontalAxisLabelPanelBase:lz", 
"HorizontalAxisLabelPanelBaseView:l0", 
"RadialAxisLabelPanelView:l1", 
"NumericScaler:l2", 
"StraightNumericAxisBaseView:l3", 
"NumericScaleMode:l4", 
"LogarithmicScaler:l5", 
"NumericXAxis:l6", 
"NumericXAxisView:l7", 
"HorizontalSmartAxisLabelPanel:l8", 
"AxisExtentType:l9", 
"SmartAxisLabelDisplayType:ma", 
"HorizontalSmartAxisLabelPanelView:mb", 
"FontMappingInfo:mc", 
"CategoryDateTimeXAxis:md", 
"CategoryDateTimeXAxisView:me", 
"TimeAxisDisplayType:mf", 
"FastItemDateTimeColumn:mg", 
"IFastItemColumnInternal:mh", 
"FastItemColumn:mi", 
"FastReflectionHelper:mj", 
"HorizontalAxisLabelPanel:mk", 
"CoercionInfo:ml", 
"SortedListView$1:mm", 
"ArrayUtil:mn", 
"HorizontalLogarithmicScaler:mo", 
"HorizontalLinearScaler:mp", 
"LinearScaler:mq", 
"NumericYAxisView:mr", 
"VerticalAxisLabelPanel:ms", 
"VerticalAxisLabelPanelView:mt", 
"VerticalLogarithmicScaler:mu", 
"VerticalLinearScaler:mv", 
"CategoryFramePreparerBase:mw", 
"FramePreparer:mx", 
"ISupportsErrorBars:my", 
"DefaultSupportsMarkers:mz", 
"DefaultProvidesViewport:m0", 
"DefaultSupportsErrorBars:m1", 
"PreparationParams:m2", 
"CategoryYAxis:m3", 
"CategoryYAxisView:m4", 
"SyncSettings:m5", 
"ValuesHolder:m6", 
"LineSeries:m7", 
"LineSeriesView:m8", 
"AnchoredCategorySeriesView:m9", 
"CategoryTrendLineManagerBase:na", 
"TrendLineManagerBase$1:nb", 
"TrendLineType:nc", 
"Clipper:nd", 
"EdgeClipper:ne", 
"LeftClipper:nf", 
"BottomClipper:ng", 
"RightClipper:nh", 
"TopClipper:ni", 
"TrendResolutionParams:nj", 
"Flattener:nk", 
"Stack$1:nl", 
"ReverseArrayEnumerator$1:nm", 
"SpiralTodo:nn", 
"FlattenerSettings:no", 
"IPreparesCategoryTrendline:np", 
"SortingTrendLineManager:nq", 
"TrendFitCalculator:nr", 
"LeastSquaresFit:ns", 
"Numeric:nt", 
"TrendAverageCalculator:nu", 
"CategoryTrendLineManager:nv", 
"AnchoredCategoryBucketCalculator:nw", 
"UnknownValuePlotting:nx", 
"CategoryLineRasterizer:ny", 
"Action$5:nz", 
"PathVisualData:n0", 
"CategorySeriesRenderManager:n1", 
"AssigningCategoryStyleEventArgs:n2", 
"AssigningCategoryStyleEventArgsBase:n3", 
"GetCategoryItemsHandler:n4", 
"HighlightingInfo:n5", 
"HighlightingState:n6", 
"AssigningCategoryMarkerStyleEventArgs:n7", 
"HighlightingManager:n8", 
"SplineSeriesBase:n9", 
"SplineSeriesBaseView:oa", 
"SplineType:ob", 
"CollisionAvoider:oc", 
"SafeSortedReadOnlyDoubleCollection:od", 
"SafeReadOnlyDoubleCollection:oe", 
"SafeEnumerable:of", 
"AreaSeries:og", 
"AreaSeriesView:oh", 
"LegendTemplates:oi", 
"PieChartBase:oj", 
"PieChartBaseView:ok", 
"PieChartViewManager:ol", 
"PieChartVisualData:om", 
"PieSliceVisualDataList:on", 
"PieSliceVisualData:oo", 
"PieSliceDataContext:op", 
"Slice:oq", 
"SliceView:or", 
"PieLabel:os", 
"LabelsPosition:ot", 
"MouseButtonEventArgs:ou", 
"FastItemsSource:ov", 
"ColumnReference:ow", 
"FastItemObjectColumn:ox", 
"FastItemIntColumn:oy", 
"LeaderLineType:oz", 
"OthersCategoryType:o0", 
"IndexCollection:o1", 
"LegendBase:o2", 
"LegendBaseView:o3", 
"LegendBaseViewManager:o4", 
"GradientData:o5", 
"GradientStopData:o6", 
"DataChartLegendMouseButtonEventArgs:o7", 
"DataChartMouseButtonEventArgs:o8", 
"ChartLegendMouseEventArgs:o9", 
"ChartMouseEventArgs:pa", 
"DataChartLegendMouseButtonEventHandler:pb", 
"DataChartLegendMouseEventHandler:pc", 
"LegendVisualData:pd", 
"LegendVisualDataList:pe", 
"LegendItemVisualData:pf", 
"FunnelSliceDataContext:pg", 
"PieChartFormatLabelHandler:ph", 
"LabelClickEventHandler:pi", 
"LabelClickEventArgs:pj", 
"SliceClickEventHandler:pk", 
"SliceClickEventArgs:pl", 
"ItemLegend:pm", 
"ItemLegendView:pn", 
"LegendItemInfo:po", 
"BubbleSeries:pp", 
"ScatterBase:pq", 
"ScatterBaseView:pr", 
"MarkerManagerBase:ps", 
"OwnedPoint:pt", 
"MarkerManagerBucket:pu", 
"ScatterTrendLineManager:pv", 
"NumericMarkerManager:pw", 
"CollisionAvoidanceType:px", 
"SmartPlacer:py", 
"ISmartPlaceable:pz", 
"SmartPosition:p0", 
"SmartPlaceableWrapper$1:p1", 
"ScatterAxisInfoCache:p2", 
"ScatterErrorBarSettings:p3", 
"ErrorBarSettingsBase:p4", 
"EnableErrorBars:p5", 
"ErrorBarCalculatorReference:p6", 
"IErrorBarCalculator:p7", 
"ErrorBarCalculatorType:p8", 
"ScatterFrame:p9", 
"ScatterFrameBase$1:qa", 
"DictInterpolator$3:qb", 
"Action$6:qc", 
"SeriesHitTestMode:qd", 
"SyncLink:qe", 
"IFastItemsSourceProvider:qf", 
"ChartCollection:qg", 
"FastItemsSourceReference:qh", 
"SyncManager:qi", 
"SyncLinkManager:qj", 
"ErrorBarsHelper:qk", 
"BubbleSeriesView:ql", 
"BubbleMarkerManager:qm", 
"SizeScale:qn", 
"BrushScale:qo", 
"ScaleLegend:qp", 
"ScaleLegendView:qq", 
"CustomPaletteBrushScale:qr", 
"BrushSelectionMode:qs", 
"ValueBrushScale:qt", 
"RingSeriesBase:qu", 
"XamDoughnutChart:qv", 
"RingCollection:qw", 
"Ring:qx", 
"RingControl:qy", 
"RingControlView:qz", 
"Arc:q0", 
"ArcView:q1", 
"ArcItem:q2", 
"SliceItem:q3", 
"RingSeriesBaseView:q4", 
"Nullable$1:q5", 
"RingSeriesCollection:q6", 
"SliceCollection:q7", 
"XamDoughnutChartView:q8", 
"Action$2:q9", 
"DoughnutChartVisualData:ra", 
"RingSeriesVisualDataList:rb", 
"RingSeriesVisualData:rc", 
"RingVisualDataList:rd", 
"RingVisualData:re", 
"ArcVisualDataList:rf", 
"ArcVisualData:rg", 
"SliceVisualDataList:rh", 
"SliceVisualData:ri", 
"DoughnutChartLabelVisualData:rj", 
"HoleDimensionsChangedEventHandler:rk", 
"HoleDimensionsChangedEventArgs:rl", 
"XamFunnelChart:rm", 
"IItemProvider:rn", 
"MessageHandler:ro", 
"MessageHandlerEventHandler:rp", 
"Message:rq", 
"ServiceProvider:rr", 
"MessageChannel:rs", 
"MessageEventHandler:rt", 
"Queue$1:ru", 
"XamFunnelConnector:rv", 
"XamFunnelController:rw", 
"SliceInfoList:rx", 
"SliceInfo:ry", 
"SliceAppearance:rz", 
"PointList:r0", 
"FunnelSliceVisualData:r1", 
"PointData:r2", 
"SliceInfoUnaryComparison:r3", 
"Bezier:r4", 
"BezierPoint:r5", 
"BezierOp:r6", 
"BezierPointComparison:r7", 
"DoubleColumn:r8", 
"ObjectColumn:r9", 
"XamFunnelView:sa", 
"IOuterLabelWidthDecider:sb", 
"IFunnelLabelSizeDecider:sc", 
"MouseLeaveMessage:sd", 
"InteractionMessage:se", 
"MouseMoveMessage:sf", 
"MouseButtonMessage:sg", 
"MouseButtonAction:sh", 
"MouseButtonType:si", 
"SetAreaSizeMessage:sj", 
"RenderingMessage:sk", 
"RenderSliceMessage:sl", 
"RenderOuterLabelMessage:sm", 
"TooltipValueChangedMessage:sn", 
"TooltipUpdateMessage:so", 
"FunnelDataContext:sp", 
"PropertyChangedMessage:sq", 
"ConfigurationMessage:sr", 
"ClearMessage:ss", 
"ClearTooltipMessage:st", 
"ContainerSizeChangedMessage:su", 
"ViewportChangedMessage:sv", 
"ViewPropertyChangedMessage:sw", 
"OuterLabelAlignment:sx", 
"FunnelSliceDisplay:sy", 
"SliceSelectionManager:sz", 
"DataUpdatedMessage:s0", 
"ItemsSourceAction:s1", 
"FunnelFrame:s2", 
"UserSelectedItemsChangedMessage:s3", 
"LabelSizeChangedMessage:s4", 
"FrameRenderCompleteMessage:s5", 
"IntColumn:s6", 
"IntColumnComparison:s7", 
"Convert:s8", 
"SelectedItemsChangedMessage:s9", 
"ModelUpdateMessage:ta", 
"SliceClickedMessage:tb", 
"FunnelSliceClickedEventHandler:tc", 
"FunnelSliceClickedEventArgs:td", 
"FunnelChartVisualData:te", 
"FunnelSliceVisualDataList:tf", 
"RingSeries:tg", 
"WaterfallSeries:th", 
"WaterfallSeriesView:ti", 
"CategoryTransitionInMode:tj", 
"FinancialSeries:tk", 
"FinancialSeriesView:tl", 
"FinancialBucketCalculator:tm", 
"CategoryTransitionSourceFramePreparer:tn", 
"TransitionInSpeedType:to", 
"FinancialCalculationDataSource:tp", 
"CalculatedColumn:tq", 
"FinancialEventArgs:tr", 
"FinancialCalculationSupportingCalculations:ts", 
"ColumnSupportingCalculation:tt", 
"SupportingCalculation$1:tu", 
"SupportingCalculationStrategy:tv", 
"DataSourceSupportingCalculation:tw", 
"ProvideColumnValuesStrategy:tx", 
"AssigningCategoryStyleEventHandler:ty", 
"FinancialValueList:tz", 
"CategoryXAxis:t0", 
"CategoryXAxisView:t1", 
"FinancialEventHandler:t2", 
"StepLineSeries:t3", 
"StepLineSeriesView:t4", 
"StepAreaSeries:t5", 
"StepAreaSeriesView:t6", 
"RangeAreaSeries:t7", 
"HorizontalRangeCategorySeries:t8", 
"RangeCategorySeries:t9", 
"IHasHighLowValueCategory:ua", 
"RangeCategorySeriesView:ub", 
"RangeCategoryBucketCalculator:uc", 
"RangeCategoryFramePreparer:ud", 
"IHasCategoryTrendline:ue", 
"IHasTrendline:uf", 
"DefaultCategoryTrendlineHost:ug", 
"DefaultCategoryTrendlinePreparer:uh", 
"DefaultHighLowValueProvider:ui", 
"HighLowValuesHolder:uj", 
"CategoryMarkerManager:uk", 
"RangeValueList:ul", 
"RangeAreaSeriesView:um", 
"LineFragment:un", 
"LineFragmentView:uo", 
"LineFragmentBucketCalculator:up", 
"IStacked100Series:uq", 
"AreaFragment:ur", 
"AreaFragmentView:us", 
"AreaFragmentBucketCalculator:ut", 
"StackedSplineAreaSeries:uu", 
"SplineAreaFragment:uv", 
"SplineFragmentBase:uw", 
"SplineAreaFragmentView:ux", 
"StackedColumnSeries:uy", 
"StackedColumnSeriesView:uz", 
"StackedColumnBucketCalculator:u0", 
"ColumnFragment:u1", 
"ColumnFragmentView:u2", 
"StackedBarSeries:u3", 
"VerticalStackedSeriesBase:u4", 
"IBarSeries:u5", 
"StackedBarSeriesView:u6", 
"StackedBarBucketCalculator:u7", 
"BarFragment:u8", 
"StackedLineSeries:u9", 
"StackedSplineSeries:va", 
"SplineFragment:vb", 
"SplineFragmentView:vc", 
"SplineFragmentBucketCalculator:vd", 
"StackedSeriesFramePreparer:ve", 
"CategoryFramePreparer:vf", 
"IHasSingleValueCategory:vg", 
"DefaultSingleValueProvider:vh", 
"SingleValuesHolder:vi", 
"StackedSeriesCreatedEventHandler:vj", 
"StackedSeriesCreatedEventArgs:vk", 
"StackedSeriesVisualData:vl", 
"SeriesVisualDataList:vm", 
"AxisComponentsForView:vn", 
"AxisComponentsFromView:vo", 
"AxisFormatLabelHandler:vp", 
"VisualExportHelper:vq", 
"ContentInfo:vr", 
"LabelFontHeuristics:vs", 
"AxisRangeChangedEventHandler:vt", 
"ChartContentManager:vu", 
"ChartContentType:vv", 
"RenderRequestedEventArgs:vw", 
"ChartTitleVisualData:vx", 
"VisualDataSerializer:vy", 
"AxisVisualData:vz", 
"AxisLabelVisualDataList:v0", 
"AxisLabelVisualData:v1", 
"AssigningCategoryMarkerStyleEventHandler:v2", 
"SeriesComponentsForView:v3", 
"CategorySeriesMarkerCollisionAvoidance:v4", 
"NonCollisionAvoider:v5", 
"DataChartAxisRangeChangedEventHandler:v6", 
"ChartAxisRangeChangedEventArgs:v7", 
"ChartVisualData:v8", 
"AxisVisualDataList:v9", 
"RadialBase:wa", 
"RadialBaseView:wb", 
"RadialBucketCalculator:wc", 
"SeriesRenderer$2:wd", 
"SeriesRenderingArguments:we", 
"RadialFrame:wf", 
"RadialAxes:wg", 
"PolarBase:wh", 
"PolarBaseView:wi", 
"PolarTrendLineManager:wj", 
"PolarLinePlanner:wk", 
"AngleRadiusPair:wl", 
"PolarAxisInfoCache:wm", 
"PolarFrame:wn", 
"PolarAxes:wo", 
"SeriesCollection:wp", 
"SeriesViewerComponentsFromView:wq", 
"SeriesViewerSurfaceViewer:wr", 
"LabelPanelArranger:ws", 
"LabelPanelsArrangeState:wt", 
"ChartHitTestMode:wu", 
"WindowResponse:wv", 
"ViewerSurfaceUsage:ww", 
"SeriesViewerComponentsForView:wx", 
"DataChartCursorEventHandler:wy", 
"ChartCursorEventArgs:wz", 
"DataChartMouseButtonEventHandler:w0", 
"DataChartMouseEventHandler:w1", 
"AnnotationLayer:w2", 
"AnnotationLayerView:w3", 
"RefreshCompletedEventHandler:w4", 
"SeriesComponentsFromView:w5", 
"EasingFunctions:w6", 
"PolygonUtil:w7", 
"TrendCalculators:w8", 
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"PathRenderer:yv", 
"IFlattener:yw", 
"NaNSegmenter:yx", 
"DefaultFlattener:yy", 
"RearrangedList$1:yz", 
"PolarLineSeriesBase:abw", 
"PolarLineSeriesBaseView:abx", 
"PolarAreaSeries:aby", 
"PolarAreaSeriesView:abz", 
"PolarLineSeries:ab0", 
"PolarLineSeriesView:ab1", 
"PolarScatterSeries:ab2", 
"PolarScatterSeriesView:ab3", 
"PolarSplineAreaSeries:ab4", 
"PolarSplineAreaSeriesView:ab5", 
"PolarSplineSeries:ab6", 
"PolarSplineSeriesView:ab7", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('CollisionAvoidanceType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Omit";
			case 2: return "Fade";
			case 3: return "OmitAndShift";
			case 4: return "FadeAndShift";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('CollisionAvoidanceType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('AngleAxisLabelPanel', 'AxisLabelPanelBase', {
	createView: function () {
		return new $.ig.AngleAxisLabelPanelView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.AxisLabelPanelBase.prototype.onViewCreated.call(this, view);
		this.angleView(view);
	}
	,
	_angleView: null,
	angleView: function (value) {
		if (arguments.length === 1) {
			this._angleView = value;
			return value;
		} else {
			return this._angleView;
		}
	}
	,
	init: function () {
		$.ig.AxisLabelPanelBase.prototype.init.call(this);
	},
	_clipLabelsToBounds: false,
	clipLabelsToBounds: function (value) {
		if (arguments.length === 1) {
			this._clipLabelsToBounds = value;
			return value;
		} else {
			return this._clipLabelsToBounds;
		}
	}
	,
	_getPoint: null,
	getPoint: function (value) {
		if (arguments.length === 1) {
			this._getPoint = value;
			return value;
		} else {
			return this._getPoint;
		}
	}
	,
	shouldDisplay: function (index, bounds) {
		if (!$.ig.Extensions.prototype.isPlottable1(bounds)) {
			return false;
		}
		if (this.axis() == null || this.axis().viewportRect().isEmpty()) {
			return $.ig.AxisLabelPanelBase.prototype.shouldDisplay.call(this, index, bounds);
		}
		return true;
	}
	,
	_largestWidth: 0,
	largestWidth: function (value) {
		if (arguments.length === 1) {
			this._largestWidth = value;
			return value;
		} else {
			return this._largestWidth;
		}
	}
	,
	_largestHeight: 0,
	largestHeight: function (value) {
		if (arguments.length === 1) {
			this._largestHeight = value;
			return value;
		} else {
			return this._largestHeight;
		}
	}
	,
	_largestRenderWidth: 0,
	largestRenderWidth: function (value) {
		if (arguments.length === 1) {
			this._largestRenderWidth = value;
			return value;
		} else {
			return this._largestRenderWidth;
		}
	}
	,
	_largestRenderHeight: 0,
	largestRenderHeight: function (value) {
		if (arguments.length === 1) {
			this._largestRenderHeight = value;
			return value;
		} else {
			return this._largestRenderHeight;
		}
	}
	,
	createBoundsRectangles: function () {
		var rectangles = new $.ig.List$1($.ig.Rect.prototype.$type, 0);
		if (this.textBlocks().count() != this.labelPositions().count()) {
			return rectangles;
		}
		this.angleView().determineLargestLabels(rectangles);
		var extentChanged = false;
		this.foundCollisions(this.detectCollisions(rectangles));
		if (!this.axis().hasUserExtent()) {
			extentChanged = true;
			this.extent(this.largestWidth() / 2);
			this.extent(this.extent() + this.otherExtentValues());
		} else {
			extentChanged = true;
			this.angleView().bindExtentToSettings();
		}
		if (extentChanged) {
			for (var i = 0; i < rectangles.count(); i++) {
				var currentTextBlock = this.textBlocks().__inner[i];
				var position = this.labelPositions().__inner[i];
				var currentRect = rectangles.__inner[i];
				var point = this.getPoint()(position.value());
				var x = point.__x - this.getDesiredWidth(currentTextBlock) / 2;
				var y = point.__y - this.getDesiredHeight(currentTextBlock) / 2;
				currentRect.x(x);
				currentRect.y(y);
				rectangles.__inner[i] = currentRect;
			}
		}
		return rectangles;
	}
	,
	otherExtentValues: function () {
		var $self = this;
		var owningChart;
		var axis;
		axis = this.axis();
		owningChart = null;
		if (axis != null) {
			owningChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, axis.seriesViewer());
		}
		if (owningChart == null || axis == null) {
			return 0;
		}
		var angleAxes = $.ig.Enumerable.prototype.toList$1($.ig.Axis.prototype.$type, ($.ig.Enumerable.prototype.where$1($.ig.Axis.prototype.$type, (owningChart.axes()), function (a) { return ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, a) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, a) !== null) && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, a.labelPanel()) !== null && (a.labelPanel()).textBlocks().count() > 0 && (!a.hasUserExtent()) && !a.hasCrossingValue() && (a.crossingAxis() == null || $self.axis().crossingAxis() == null || (a.crossingAxis()).actualRadiusExtentScale() == (axis.crossingAxis()).actualRadiusExtentScale()); })));
		var index = angleAxes.indexOf(axis);
		if (index == -1) {
			return 0;
		}
		var extent = 0;
		var extentOffset = 5;
		if (index == 0) {
			extent += extentOffset;
		} else {
			extent += angleAxes.__inner[index - 1].labelPanel().extent();
			extent += angleAxes.__inner[index].labelPanel().extent() * 2 + extentOffset;
		}
		return extent;
	}
	,
	getDefaultLabelsLocation: function () {
		return $.ig.AxisLabelsLocation.prototype.insideTop;
	}
	,
	validLocation: function (location) {
		return location == $.ig.AxisLabelsLocation.prototype.insideTop || location == $.ig.AxisLabelsLocation.prototype.insideBottom;
	}
	,
	$type: new $.ig.Type('AngleAxisLabelPanel', $.ig.AxisLabelPanelBase.prototype.$type)
}, true);

$.ig.util.defType('CategoryAxisRenderer', 'AxisRendererBase', {
	init: function (labelManager) {
		$.ig.AxisRendererBase.prototype.init.call(this, labelManager);
	},
	getSnapperInfoInternal: function (renderingParams, interval, minorCount, first, last) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		var mode = $.ig.CategoryMode.prototype.mode0;
		if (catParams != null) {
			mode = catParams.categoryMode();
		}
		var snapper = new $.ig.LinearCategorySnapper(1, renderingParams.currentRangeInfo().visibleMinimum(), renderingParams.currentRangeInfo().visibleMaximum(), renderingParams.currentRangeInfo().resolution(), renderingParams.interval(), mode, renderingParams.heuristicResolution());
		interval = snapper.interval();
		if (renderingParams.currentRangeInfo().intervalOverride() != -1) {
			interval = renderingParams.currentRangeInfo().intervalOverride();
		}
		var firstValue = Math.floor((renderingParams.currentRangeInfo().visibleMinimum() - renderingParams.actualMinimumValue()) / interval);
		var lastValue = Math.ceil((renderingParams.currentRangeInfo().visibleMaximum() - renderingParams.actualMinimumValue()) / interval);
		first = $.ig.truncate(firstValue);
		last = $.ig.truncate(lastValue);
		minorCount = $.ig.truncate(snapper.minorCount());
		if (renderingParams.currentRangeInfo().minorCountOverride() != -1) {
			minorCount = renderingParams.currentRangeInfo().minorCountOverride();
		}
		return {
			p1: interval,
			p2: minorCount,
			p3: first,
			p4: last
		};
	}
	,
	getLabel: function (renderingParams, unscaledValue, index, interval) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		if (catParams == null) {
			return null;
		}
		var itemIndex = 0;
		if (interval >= 1) {
			itemIndex = index * $.ig.truncate(Math.floor(interval));
		} else {
			if ((index * interval) * 2 % 2 == 0) {
				itemIndex = $.ig.truncate(Math.floor(index * interval));
			} else {
				itemIndex = -1;
			}
		}
		var label = null;
		if ((catParams.count() > 0 && itemIndex < catParams.count() && itemIndex >= 0) || catParams.wrapAround()) {
			while (itemIndex >= catParams.count() && catParams.wrapAround()) {
				itemIndex -= catParams.count();
			}
			label = this.getLabelForItem()(itemIndex);
		}
		return label;
	}
	,
	renderMinorLines: function (renderingParams, interval, minorCount, majorValue, i, nextMajorValue) {
		var catParams = $.ig.util.cast($.ig.CategoryAxisRenderingParameters.prototype.$type, renderingParams);
		if (catParams.categoryMode() != $.ig.CategoryMode.prototype.mode0 && catParams.mode2GroupCount() != 0) {
			for (var categoryNumber = 0; categoryNumber < $.ig.truncate(interval); categoryNumber++) {
				for (var groupNumber = 0; groupNumber < catParams.mode2GroupCount(); groupNumber++) {
					var center = this.getGroupCenter()(groupNumber, renderingParams.windowRect(), renderingParams.viewportRect(), renderingParams.effectiveViewportRect());
					if (catParams.isInverted()) {
						center = -center;
					}
					var minorValue = this.scaling()(renderingParams, categoryNumber + i * interval) + center;
					renderingParams.currentRenderingInfo(renderingParams.minorRenderingInfo());
					this.line()(renderingParams, renderingParams.minor(), minorValue);
					renderingParams.currentRenderingInfo(null);
				}
			}
		}
	}
	,
	$type: new $.ig.Type('CategoryAxisRenderer', $.ig.AxisRendererBase.prototype.$type)
}, true);

$.ig.util.defType('IAngleScaler', 'Object', {
	$type: new $.ig.Type('IAngleScaler', null)
}, true);

$.ig.util.defType('CategoryAngleAxis', 'CategoryAxisBase', {
	createView: function () {
		return new $.ig.CategoryAngleAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.CategoryAxisBase.prototype.onViewCreated.call(this, view);
		this.categoryAngleView(view);
	}
	,
	_categoryAngleView: null,
	categoryAngleView: function (value) {
		if (arguments.length === 1) {
			this._categoryAngleView = value;
			return value;
		} else {
			return this._categoryAngleView;
		}
	}
	,
	isAngular: function () {
		return true;
	}
	,
	_renderingManager: null,
	init: function () {
		this.__preventReentry = false;
		this.__lastCrossing = NaN;
		this.__startAngleOffsetRadians = 0;
		this.__actualMaximum = 1;
		$.ig.CategoryAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.CategoryAngleAxis.prototype.$type);
		this.renderer(this.createRenderer());
		this._renderingManager = new $.ig.PolarAxisRenderingManager();
	},
	createLabelPanel: function () {
		var $self = this;
		var panel = new $.ig.AngleAxisLabelPanel();
		panel.getPoint(function (v) {
			var windowRect = $self.seriesViewer() != null ? $self.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
			var viewportRect = !windowRect.isEmpty() ? $self.viewportRect() : $.ig.Rect.prototype.empty();
			return $self.getLabelLocationPoint(v, { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, windowRect, viewportRect, $self.labelPanel().extent());
		});
		return panel;
	}
	,
	_renderer: null,
	renderer: function (value) {
		if (arguments.length === 1) {
			this._renderer = value;
			return value;
		} else {
			return this._renderer;
		}
	}
	,
	__preventReentry: false,
	round10: function (value) {
		return Math.round(value * Math.pow(10, 10)) / Math.pow(10, 10);
	}
	,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var labelManager = (function () {
			var $ret = new $.ig.AxisLabelManager();
			$ret.axis($self);
			$ret.labelPositions($self.labelPositions());
			$ret.labelDataContext($self.labelDataContext());
			$ret.targetPanel($self.labelPanel());
			return $ret;
		}());
		if (this.labelSettings() != null) {
			this.labelSettings().registerAxis(this);
		}
		var renderer = new $.ig.CategoryAxisRenderer(labelManager);
		renderer.clear(function () {
			var axisGeometry = $self.view().getAxisLinesGeometry();
			var stripsGeometry = $self.view().getStripsGeometry();
			var majorGeometry = $self.view().getMajorLinesGeometry();
			var minorGeometry = $self.view().getMinorLinesGeometry();
			$self.updateLineVisibility();
			$self.clearMarks(axisGeometry);
			$self.clearMarks(stripsGeometry);
			$self.clearMarks(majorGeometry);
			$self.clearMarks(minorGeometry);
		});
		renderer.shouldRender(function (viewport, window) {
			return !window.isEmpty() && !viewport.isEmpty() && $self.radiusAxis() != null;
		});
		renderer.createRenderingParams(function (viewport, window, effectiveViewportRect, contentViewport) {
			return $self.createRenderingParams(viewport, window, effectiveViewportRect, contentViewport);
		});
		renderer.onRendering(function () {
			if (!$self.__preventReentry) {
				$self.__preventReentry = true;
				$self.radiusAxis().updateRange();
				$self.__preventReentry = false;
			}
		});
		renderer.getLabelForItem(function (item) {
			var index = $.ig.util.getValue(item);
			if (index > $self.fastItemsSource().count() - 1) {
				index -= $self.fastItemsSource().count();
			}
			var dataItem = $self.fastItemsSource().item(index);
			return $self.getLabel(dataItem);
		});
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.radiusAxis() != null && $self.__lastCrossing != crossing) {
				var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, $self.seriesViewer());
				if (dataChart == null) {
					return;
				}
				$self.__lastCrossing = crossing;
				$self.labelPanel().crossingValue(crossing);
				dataChart.invalidatePanels();
				var en = dataChart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis = en.current();
					if (axis != $self && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, axis.labelPanel()) !== null) {
						axis.view().labelNeedRearrange();
					}
				}
			}
		});
		renderer.line(function (p, g, value) {
			var r2 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialLine(g, value, p.viewportRect(), p.windowRect(), r2.minLength(), r2.maxLength(), r2.center());
		});
		renderer.strip(function (p, g, start, end) {
			var r3 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialStrip(g, start, end, r3.viewportRect(), p.windowRect(), r3.minLength(), r3.maxLength(), r3.center());
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledAngle(unscaled);
		});
		renderer.shouldRenderLines(function (p, value) {
			if ($self.round10(value - $self.__startAngleOffsetRadians) < 0) {
				return false;
			}
			if ($self.round10(value - $self.__startAngleOffsetRadians - (2 * Math.PI)) > 0) {
				return false;
			}
			return true;
		});
		renderer.axisLine(function (p) {
			var r4 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			if (r4.currentRangeInfo() == r4.rangeInfos().__inner[0]) {
				$self._renderingManager.concentricLine(p.axisGeometry(), p.crossingValue(), p.viewportRect(), p.windowRect(), r4.center(), r4.minAngle(), r4.maxAngle());
			}
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.getCrossingValue());
			p.relativeCrossingValue(p.crossingValue());
		});
		renderer.shouldRenderLabel(function (p, value, last) {
			var r5 = $.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, p);
			if (last) {
				return false;
			}
			var labelPoint = $self.getLabelLocationPoint(value, r5.center(), p.windowRect(), p.viewportRect(), 0);
			if (labelPoint.__x < p.viewportRect().right() && labelPoint.__x >= p.viewportRect().left() && labelPoint.__y < p.viewportRect().bottom() && labelPoint.__y >= p.viewportRect().top()) {
				return true;
			}
			return false;
		});
		renderer.adjustMajorValue(function (p, value, i, interval) {
			var effectiveViewportRect = p.effectiveViewportRect();
			var sParams = new $.ig.ScalerParams(0, p.windowRect(), p.viewportRect(), $self.isInverted(), effectiveViewportRect);
			var categoryValue = value;
			if ($self.categoryMode() != $.ig.CategoryMode.prototype.mode0) {
				var unscaledValue = (i * interval) + 1;
				unscaledValue = Math.min(unscaledValue, $self._cachedItemsCount);
				var nextCategoryValue = $self.getScaledValue(unscaledValue, sParams);
				categoryValue = (value + nextCategoryValue) / 2;
			}
			return categoryValue;
		});
		renderer.getGroupCenter(this.getGroupCenter.runOn(this));
		renderer.getUnscaledGroupCenter(this.getUnscaledGroupCenter.runOn(this));
		return renderer;
	}
	,
	getLabelLocationPoint: function (angleValue, center, windowRect, viewportRect, extent) {
		var crossingValue = this.getCrossingValue();
		var extentValue = $.ig.ViewportUtils.prototype.transformXFromViewportLength(extent, windowRect, viewportRect);
		if (this.labelSettings() != null && (this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom || this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideBottom)) {
			extentValue *= -1;
		}
		var x = center.__x + (crossingValue + extentValue) * Math.cos(angleValue);
		var y = center.__y + (crossingValue + extentValue) * Math.sin(angleValue);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getCrossingValue: function () {
		if (this.radiusAxis() == null) {
			return 0;
		}
		if (!this.hasCrossingValue()) {
			return this.radiusAxis().getEffectiveMaximumLength();
		} else {
			return this.radiusAxis().getScaledValue3((this.crossingValue()));
		}
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		if (this.fastItemsSource() == null) {
			return null;
		}
		var renderingParams = new $.ig.RadialAxisRenderingParameters();
		var max = this.fastItemsSource().count() - 1;
		var axisGeometry = this.view().getAxisLinesGeometry();
		var stripsGeometry = this.view().getStripsGeometry();
		var majorGeometry = this.view().getMajorLinesGeometry();
		var minorGeometry = this.view().getMinorLinesGeometry();
		renderingParams.axisGeometry(axisGeometry);
		renderingParams.strips(stripsGeometry);
		renderingParams.major(majorGeometry);
		renderingParams.minor(minorGeometry);
		renderingParams.actualMaximumValue(max);
		renderingParams.actualMinimumValue(0);
		renderingParams.hasUserMax(false);
		renderingParams.viewportRect(viewportRect);
		renderingParams.effectiveViewportRect(effectiveViewportRect);
		renderingParams.contentViewport(contentViewport);
		renderingParams.windowRect(windowRect);
		renderingParams.hasUserInterval(this.hasUserInterval());
		renderingParams.interval(this.interval());
		renderingParams.heuristicResolution(this.resolveHeuristicResolution());
		renderingParams.minorInterval(this.minorInterval());
		renderingParams.label(this.label());
		var closestRadius = this._renderingManager.getClosestRadiusValue(windowRect);
		var furthestRadius = this._renderingManager.getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.radiusAxis().actualRadiusExtentScale();
		var minRadius = 0.5 * this.radiusAxis().actualInnerRadiusExtentScale();
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var effectiveMaximum = this.radiusAxis().getEffectiveMaximumLength();
		if ($.ig.util.isNaN(effectiveMaximum) || Number.isInfinity(effectiveMaximum)) {
			return null;
		}
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		var resolution = viewportRect.width();
		this._renderingManager.determineView(windowRect, renderingParams, 0, this.fastItemsSource().count(), this.isInverted(), this.getUnscaledAngle.runOn(this), resolution);
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		renderingParams.center(center);
		renderingParams.maxLength(maxLen);
		renderingParams.minLength(minLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		renderingParams.count(this.fastItemsSource().count());
		renderingParams.categoryMode(this.categoryMode());
		renderingParams.wrapAround(true);
		renderingParams.isInverted(this.isInverted());
		renderingParams.mode2GroupCount(this.mode2GroupCount());
		renderingParams.tickmarkValues(new $.ig.CategoryTickmarkValues());
		renderingParams.shouldRenderMinorLines(this.shouldRenderMinorLines());
		return renderingParams;
	}
	,
	getMinMaxAngle: function (windowRect, visibleMinimum, visibleMaximum) {
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, visibleMinimum, visibleMaximum);
		visibleMinimum = $ret.p1;
		visibleMaximum = $ret.p2;
		return {
			p1: visibleMinimum,
			p2: visibleMaximum
		};
	}
	,
	onApplyTemplate: function () {
		$.ig.CategoryAxisBase.prototype.onApplyTemplate.call(this);
		this.renderAxis1(false);
	}
	,
	__radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this.__radiusAxis = value;
			return value;
		} else {
			if (this.__radiusAxis != null) {
				return this.__radiusAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				for (var i = 0; i < dataChart.axes().count(); i++) {
					if ($.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, dataChart.axes().__inner[i]) !== null) {
						return dataChart.axes().__inner[i];
					}
				}
			}
			return this.__radiusAxis;
		}
	}
	,
	startAngleOffset: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty);
		}
	}
	,
	__startAngleOffsetRadians: 0,
	getCategorySize: function (windowRect, viewportRect, effectiveViewportRect) {
		return 2 * Math.PI / this._cachedItemsCount;
	}
	,
	getGroupSize: function (windowRect, viewportRect, effectiveViewportRect) {
		var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
		var overlap = 0;
		if (!$.ig.util.isNaN(this.overlap())) {
			overlap = Math.min(this.overlap(), 1);
		}
		var categorySpace = 1 - 0.5 * gap;
		return this.getCategorySize(windowRect, viewportRect, effectiveViewportRect) * categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
	}
	,
	getGroupCenter: function (groupIndex, windowRect, viewportRect, effectiveViewportRect) {
		var groupCenter = 0.5;
		if (this.mode2GroupCount() > 1) {
			var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
			var overlap = 0;
			if (!$.ig.util.isNaN(this.overlap())) {
				overlap = Math.min(this.overlap(), 1);
			}
			var categorySpace = 1 - 0.5 * gap;
			var groupWidth = categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
			var groupSep = (categorySpace - groupWidth) / (this.mode2GroupCount() - 1);
			groupCenter = 0.25 * gap + 0.5 * groupWidth + groupIndex * groupSep;
		}
		return this.getCategorySize(windowRect, viewportRect, effectiveViewportRect) * groupCenter;
	}
	,
	getUnscaledGroupCenter: function (groupIndex) {
		var groupCenter = 0.5;
		if (this.mode2GroupCount() > 1) {
			var gap = !$.ig.util.isNaN(this.gap()) ? $.ig.MathUtil.prototype.clamp(this.gap(), 0, 1) : 0;
			var overlap = 0;
			if (!$.ig.util.isNaN(this.overlap())) {
				overlap = Math.min(this.overlap(), 1);
			}
			var categorySpace = 1 - 0.5 * gap;
			var groupWidth = categorySpace / (this.mode2GroupCount() - (this.mode2GroupCount() - 1) * overlap);
			var groupSep = (categorySpace - groupWidth) / (this.mode2GroupCount() - 1);
			groupCenter = 0.25 * gap + 0.5 * groupWidth + groupIndex * groupSep;
		}
		return groupCenter;
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.CategoryAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	getMinimumViewable: function (viewportRect, windowRect) {
		var minAngle;
		var maxAngle;
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		if (minAngle == 0) {
			if (this.isInverted()) {
				return this._cachedItemsCount;
			} else {
				return 0;
			}
		}
		var value = this.getUnscaledAngle(minAngle);
		if (value < 0 || value > this._cachedItemsCount) {
			value = this.getUnscaledAngle(minAngle + Math.PI * 2);
		}
		return value;
	}
	,
	getMaximumViewable: function (viewportRect, windowRect) {
		var minAngle;
		var maxAngle;
		var $ret = this._renderingManager.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		if (maxAngle > Math.PI * 2) {
			maxAngle = maxAngle - Math.PI * 2;
		}
		if (maxAngle == Math.PI * 2) {
			if (this.isInverted()) {
				return 0;
			} else {
				return this._cachedItemsCount;
			}
		}
		var value = this.getUnscaledAngle(maxAngle);
		if (value < 0 || value > this._cachedItemsCount) {
			value = this.getUnscaledAngle(maxAngle + Math.PI * 2);
		}
		return value;
	}
	,
	getScaledAngle: function (unscaledAngle) {
		var itemsCount = this._cachedItemsCount;
		var scaledValue = itemsCount >= 2 ? (unscaledAngle) / (itemsCount) : itemsCount == 1 ? 0.5 : NaN;
		if (this.isInvertedCached()) {
			scaledValue = 1 - scaledValue;
		}
		return (scaledValue * 2 * Math.PI) + this.__startAngleOffsetRadians;
	}
	,
	getUnscaledAngle: function (scaledAngle) {
		if (scaledAngle < this.__startAngleOffsetRadians) {
			scaledAngle += 2 * Math.PI;
		}
		var unscaledValue = (scaledAngle - this.__startAngleOffsetRadians) / (2 * Math.PI);
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		return unscaledValue * (this._cachedItemsCount);
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return (this).getScaledAngle(unscaledValue);
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return (this).getUnscaledAngle(scaledValue);
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.CategoryAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var radiusAxis = $.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, newValue);
				this.onRadiusAxisChanged(radiusAxis);
				if (radiusAxis != null) {
					radiusAxis.onAngleAxisChanged(this);
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
			case $.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName:
				this.__startAngleOffsetRadians = this.startAngleOffset();
				while (this.__startAngleOffsetRadians < 0) {
					this.__startAngleOffsetRadians += 360;
				}
				while (this.__startAngleOffsetRadians >= 360) {
					this.__startAngleOffsetRadians -= 360;
				}
				this.__startAngleOffsetRadians = (this.__startAngleOffsetRadians * Math.PI) / 180;
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				var en = this.series().getEnumerator();
				while (en.moveNext()) {
					var s = en.current();
					s.renderSeries(false);
					s.notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.labelPropertyName:
				if (dataChart != null) {
					var en1 = dataChart.axes().getEnumerator();
					while (en1.moveNext()) {
						var axis = en1.current();
						axis.mustInvalidateLabels(true);
						axis.renderAxis();
					}
				}
				break;
			case $.ig.Axis.prototype.crossingValuePropertyName:
				if (dataChart != null) {
					var en2 = dataChart.axes().getEnumerator();
					while (en2.moveNext()) {
						var axis1 = en2.current();
						if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, axis1) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, axis1) !== null) {
							axis1.mustInvalidateLabels(true);
							axis1.renderAxis();
						}
					}
				}
				break;
			case $.ig.Axis.prototype.labelSettingsPropertyName:
				this.renderer(this.createRenderer());
				this.forcePanelRefloat();
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
		}
	}
	,
	forcePanelRefloat: function () {
		this.__lastCrossing = NaN;
	}
	,
	onRadiusAxisChanged: function (radiusAxis) {
		this.radiusAxis(radiusAxis);
	}
	,
	interval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.intervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.intervalProperty);
		}
	}
	,
	_actualInterval: 0,
	actualInterval: function (value) {
		if (arguments.length === 1) {
			if (this.actualInterval() != value) {
				var oldValue = this._actualInterval;
				this._actualInterval = value;
				this.raisePropertyChanged($.ig.CategoryAngleAxis.prototype.actualIntervalPropertyName, oldValue, this.actualInterval());
			}
			return value;
		} else {
			return this._actualInterval;
		}
	}
	,
	minorInterval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CategoryAngleAxis.prototype.minorIntervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.CategoryAngleAxis.prototype.minorIntervalProperty);
		}
	}
	,
	_actualMinorInterval: 0,
	actualMinorInterval: function (value) {
		if (arguments.length === 1) {
			if (this.actualMinorInterval() != value) {
				var oldValue = this._actualMinorInterval;
				this._actualMinorInterval = value;
				this.raisePropertyChanged($.ig.CategoryAngleAxis.prototype.actualMinorIntervalPropertyName, oldValue, this.actualMinorInterval());
			}
			return value;
		} else {
			return this._actualMinorInterval;
		}
	}
	,
	hasUserInterval: function () {
		return false;
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.angular;
	}
	,
	updateRangeOverride: function () {
		if (this.mustInvalidateLabels() && this.useEnhancedIntervalManagement()) {
			this.labelFontHeuristics(this.gatherLabelFontHeuristics());
		}
		if (this.fastItemsSource() == null) {
			return false;
		}
		var max = this.fastItemsSource().count();
		if (max != this.actualMaximum()) {
			var ea = new $.ig.AxisRangeChangedEventArgs(1, 1, this.actualMaximum(), max);
			this.actualMaximum(max);
			this.raiseRangeChanged(ea);
			return true;
		}
		return false;
	}
	,
	__actualMaximum: 0,
	actualMaximum: function (value) {
		if (arguments.length === 1) {
			this.__actualMaximum = value;
			return value;
		} else {
			return this.__actualMaximum;
		}
	}
	,
	$type: new $.ig.Type('CategoryAngleAxis', $.ig.CategoryAxisBase.prototype.$type, [$.ig.IAngleScaler.prototype.$type])
}, true);

$.ig.util.defType('NumericAngleAxis', 'NumericAxisBase', {
	createView: function () {
		return new $.ig.NumericAngleAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.NumericAxisBase.prototype.onViewCreated.call(this, view);
		this.numericAngleView(view);
	}
	,
	_numericAngleView: null,
	numericAngleView: function (value) {
		if (arguments.length === 1) {
			this._numericAngleView = value;
			return value;
		} else {
			return this._numericAngleView;
		}
	}
	,
	_renderingManager: null,
	renderingManager: function (value) {
		if (arguments.length === 1) {
			this._renderingManager = value;
			return value;
		} else {
			return this._renderingManager;
		}
	}
	,
	isAngular: function () {
		return true;
	}
	,
	init: function () {
		this.__preventReentry = false;
		this.__lastCrossing = NaN;
		this.__startAngleOffsetRadians = 0;
		$.ig.NumericAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.NumericAngleAxis.prototype.$type);
		this.renderingManager(new $.ig.PolarAxisRenderingManager());
		this.renderer(this.createRenderer());
	},
	createLabelPanel: function () {
		var $self = this;
		var panel = new $.ig.AngleAxisLabelPanel();
		panel.getPoint(function (v) {
			var windowRect = $self.seriesViewer() != null ? $self.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
			var viewportRect = !windowRect.isEmpty() ? $self.viewportRect() : $.ig.Rect.prototype.empty();
			return $self.getLabelLocationPoint(v, { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, windowRect, viewportRect, $self.labelPanel().extent());
		});
		return panel;
	}
	,
	getCrossingValue: function () {
		if (this.radiusAxis() == null) {
			return 0;
		}
		if (!this.hasCrossingValue()) {
			return this.radiusAxis().getEffectiveMaximumLength();
		} else {
			return this.radiusAxis().getScaledValue3((this.crossingValue()));
		}
	}
	,
	round10: function (value) {
		return Math.round(value * Math.pow(10, 10)) / Math.pow(10, 10);
	}
	,
	__preventReentry: false,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var renderer = $.ig.NumericAxisBase.prototype.createRenderer.call(this);
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.radiusAxis() != null && $self.__lastCrossing != crossing) {
				var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, $self.seriesViewer());
				if (dataChart == null) {
					return;
				}
				$self.__lastCrossing = crossing;
				$self.labelPanel().crossingValue(crossing);
				dataChart.invalidatePanels();
				var en = dataChart.axes().getEnumerator();
				while (en.moveNext()) {
					var axis = en.current();
					if (axis != $self && $.ig.util.cast($.ig.AngleAxisLabelPanel.prototype.$type, axis.labelPanel()) !== null) {
						axis.view().labelNeedRearrange();
					}
				}
			}
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.getCrossingValue());
			p.relativeCrossingValue(p.crossingValue());
		});
		renderer.axisLine(function (p) {
			var r = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			if (r.currentRangeInfo() == r.rangeInfos().__inner[0]) {
				$self.renderingManager().concentricLine(p.axisGeometry(), p.crossingValue(), p.viewportRect(), p.windowRect(), r.center(), r.minAngle(), r.maxAngle());
			}
		});
		renderer.line(function (p, g, value) {
			var r2 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self.renderingManager().radialLine(g, value, p.viewportRect(), p.windowRect(), r2.minLength(), r2.maxLength(), r2.center());
		});
		renderer.strip(function (p, g, start, end) {
			var r3 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self.renderingManager().radialStrip(g, start, end, r3.viewportRect(), p.windowRect(), r3.minLength(), r3.maxLength(), r3.center());
		});
		renderer.createRenderingParams(function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
			var r4 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $self.createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport));
			return r4;
		});
		renderer.onRendering(function () {
			if (!$self.__preventReentry) {
				$self.__preventReentry = true;
				$self.radiusAxis().updateRange();
				$self.__preventReentry = false;
			}
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledAngle(unscaled);
		});
		renderer.shouldRender(function (viewport, window) {
			return !window.isEmpty() && !viewport.isEmpty() && $self.radiusAxis() != null;
		});
		renderer.shouldRenderLines(function (p, value) {
			if ($self.round10(value - $self.__startAngleOffsetRadians) < 0) {
				return false;
			}
			if ($self.round10(value - $self.__startAngleOffsetRadians - (2 * Math.PI)) > 0) {
				return false;
			}
			return true;
		});
		renderer.shouldRenderLabel(function (p, value, last) {
			var r5 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			var endPoint = $self.getLabelLocationPoint($self.getScaledAngle(r5.actualMaximumValue()), r5.center(), p.windowRect(), p.viewportRect(), 0);
			var labelPoint = $self.getLabelLocationPoint(value, r5.center(), p.windowRect(), p.viewportRect(), 0);
			if (last && $.ig.MathUtil.prototype.hypot(endPoint.__x - labelPoint.__x, endPoint.__y - labelPoint.__y) < 2) {
				return false;
			}
			if (labelPoint.__x < p.viewportRect().right() && labelPoint.__x >= p.viewportRect().left() && labelPoint.__y < p.viewportRect().bottom() && labelPoint.__y >= p.viewportRect().top()) {
				return true;
			}
			return false;
		});
		renderer.snapMajorValue(function (p, value, i, interval) {
			if (value < p.actualMinimumValue() && $.ig.util.cast($.ig.LogarithmicTickmarkValues.prototype.$type, p.tickmarkValues()) !== null) {
				return p.actualMinimumValue();
			} else if (value > p.actualMaximumValue() && ($.ig.util.cast($.ig.LogarithmicTickmarkValues.prototype.$type, p.tickmarkValues()) !== null || p.hasUserMax())) {
				return p.actualMaximumValue();
			}
			return value;
		});
		return renderer;
	}
	,
	getLabelLocationPoint: function (angleValue, center, windowRect, viewportRect, extent) {
		var crossingValue = this.getCrossingValue();
		var extentValue = $.ig.ViewportUtils.prototype.transformXFromViewportLength(extent, windowRect, viewportRect);
		if (this.labelSettings() != null && (this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom || this.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideBottom)) {
			extentValue *= -1;
		}
		var x = center.__x + (crossingValue + extentValue) * Math.cos(angleValue);
		var y = center.__y + (crossingValue + extentValue) * Math.sin(angleValue);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return this.getScaledAngle(unscaledValue);
	}
	,
	getScaledAngle1: function (unscaledValue, isLogarithmic, isInverted) {
		var scaledValue = 0;
		if (isLogarithmic) {
			scaledValue = (Math.log(unscaledValue) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
		} else {
			scaledValue = (unscaledValue - this.actualMinimumValue()) / (this.actualMaximumValue() - this.actualMinimumValue());
		}
		if (isInverted) {
			scaledValue = 1 - scaledValue;
		}
		return (scaledValue * 2 * Math.PI) + this.__startAngleOffsetRadians;
	}
	,
	getScaledAngle: function (unscaledValue) {
		return this.getScaledAngle1(unscaledValue, this.isReallyLogarithmic(), this.isInvertedCached());
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return this.getUnscaledAngle(scaledValue);
	}
	,
	getUnscaledAngle: function (scaledValue) {
		var unscaledValue = (scaledValue - this.__startAngleOffsetRadians) / (2 * Math.PI);
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		if (this.isReallyLogarithmic()) {
			return Math.exp(unscaledValue * (this.logActualMaximumValue() - this.logActualMinimumValue()) + this.logActualMinimumValue());
		} else {
			return this.actualMinimumValue() + unscaledValue * (this.actualMaximumValue() - this.actualMinimumValue());
		}
	}
	,
	startAngleOffset: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericAngleAxis.prototype.startAngleOffsetProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericAngleAxis.prototype.startAngleOffsetProperty);
		}
	}
	,
	__startAngleOffsetRadians: 0,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.NumericAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var radiusAxis = $.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, newValue);
				this.onRadiusAxisChanged(radiusAxis);
				if (radiusAxis != null) {
					radiusAxis.onAngleAxisChanged(this);
				}
				this.renderAxis1(false);
				break;
			case $.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName:
				this.__startAngleOffsetRadians = this.startAngleOffset();
				while (this.__startAngleOffsetRadians < 0) {
					this.__startAngleOffsetRadians += 360;
				}
				while (this.__startAngleOffsetRadians >= 360) {
					this.__startAngleOffsetRadians -= 360;
				}
				this.__startAngleOffsetRadians = (this.startAngleOffset() * Math.PI) / 180;
				this.renderAxis1(false);
				var en = this.directSeries().getEnumerator();
				while (en.moveNext()) {
					var series = en.current();
					series.renderSeries(false);
					series.notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.labelPropertyName:
				if (dataChart != null) {
					var en1 = dataChart.axes().getEnumerator();
					while (en1.moveNext()) {
						var axis = en1.current();
						axis.renderAxis();
					}
				}
				break;
			case $.ig.Axis.prototype.crossingValuePropertyName:
				if (dataChart != null) {
					var en2 = dataChart.axes().getEnumerator();
					while (en2.moveNext()) {
						var axis1 = en2.current();
						if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, axis1) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, axis1) !== null) {
							axis1.renderAxis();
						}
					}
				}
				break;
			case $.ig.Axis.prototype.labelSettingsPropertyName:
				this.renderer(this.createRenderer());
				this.forcePanelRefloat();
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
		}
	}
	,
	forcePanelRefloat: function () {
		this.__lastCrossing = NaN;
	}
	,
	createRenderingParamsInstance: function () {
		return new $.ig.PolarAxisRenderingParameters();
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $.ig.NumericAxisBase.prototype.createRenderingParams.call(this, viewportRect, windowRect, effectiveViewportRect, contentViewport));
		var closestRadius = this.renderingManager().getClosestRadiusValue(windowRect);
		var furthestRadius = this.renderingManager().getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.radiusAxis().actualRadiusExtentScale();
		var minRadius = 0.5 * this.radiusAxis().actualInnerRadiusExtentScale();
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var effectiveMaximum = this.radiusAxis().getEffectiveMaximumLength();
		if ($.ig.util.isNaN(effectiveMaximum) || Number.isInfinity(effectiveMaximum)) {
			return null;
		}
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		var resolution = viewportRect.width();
		this.renderingManager().determineView(windowRect, renderingParams, this.actualMinimumValue(), this.actualMaximumValue(), this.isInverted(), this.getUnscaledAngle.runOn(this), resolution);
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		renderingParams.center(center);
		renderingParams.maxLength(maxLen);
		renderingParams.minLength(minLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		return renderingParams;
	}
	,
	getMinMaxAngle: function (windowRect, minAngle, maxAngle) {
		var $ret = this.renderingManager().getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		return {
			p1: minAngle,
			p2: maxAngle
		};
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.NumericAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	__radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this.__radiusAxis = value;
			return value;
		} else {
			if (this.__radiusAxis != null) {
				return this.__radiusAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				return $.ig.Enumerable.prototype.firstOrDefault$1($.ig.NumericRadiusAxis.prototype.$type, $.ig.Enumerable.prototype.ofType$1($.ig.NumericRadiusAxis.prototype.$type, dataChart.axes()));
			}
			return this.__radiusAxis;
		}
	}
	,
	onRadiusAxisChanged: function (numericRadiusAxis) {
		this.radiusAxis(numericRadiusAxis);
	}
	,
	viewportChangedOverride: function (oldRect, newRect) {
		$.ig.NumericAxisBase.prototype.viewportChangedOverride.call(this, oldRect, newRect);
		if (newRect.height() != oldRect.height() || newRect.width() != oldRect.width()) {
			this.updateRange();
		}
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.angular;
	}
	,
	$type: new $.ig.Type('NumericAngleAxis', $.ig.NumericAxisBase.prototype.$type, [$.ig.IAngleScaler.prototype.$type])
}, true);

$.ig.util.defType('NumericRadiusAxis', 'NumericAxisBase', {
	createView: function () {
		return new $.ig.NumericRadiusAxisView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.NumericAxisBase.prototype.onViewCreated.call(this, view);
		this.numericRadiusView(view);
	}
	,
	_numericRadiusView: null,
	numericRadiusView: function (value) {
		if (arguments.length === 1) {
			this._numericRadiusView = value;
			return value;
		} else {
			return this._numericRadiusView;
		}
	}
	,
	isRadial: function () {
		return true;
	}
	,
	_renderingManager: null,
	init: function () {
		this.__lastCrossing = NaN;
		$.ig.NumericAxisBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.NumericRadiusAxis.prototype.$type);
		this.actualRadiusExtentScale(this.radiusExtentScale());
		this.actualInnerRadiusExtentScale(this.innerRadiusExtentScale());
		this._renderingManager = new $.ig.PolarAxisRenderingManager();
		this.renderer(this.createRenderer());
	},
	createLabelPanel: function () {
		return new $.ig.RadialAxisLabelPanel();
	}
	,
	_suppress: false,
	suppress: function (value) {
		if (arguments.length === 1) {
			this._suppress = value;
			return value;
		} else {
			return this._suppress;
		}
	}
	,
	convertToDouble: function (x) {
		if (x == null) {
			return 0;
		}
		return x;
	}
	,
	__lastCrossing: 0,
	createRenderer: function () {
		var $self = this;
		var renderer = $.ig.NumericAxisBase.prototype.createRenderer.call(this);
		renderer.labelManager().floatPanelAction(function (crossing) {
			if (($self.labelSettings() == null || $self.labelSettings().visibility() == $.ig.Visibility.prototype.visible) && $self.angleAxis() != null) {
				if (($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom)) && $self.__lastCrossing != crossing) {
					$self.__lastCrossing = crossing;
					$self.labelPanel().crossingValue(crossing);
					$self.seriesViewer().invalidatePanels();
				}
			}
		});
		renderer.line(function (p, g, value) {
			var r = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.concentricLine(g, value, r.viewportRect(), r.windowRect(), r.center(), r.minAngle(), r.maxAngle());
		});
		renderer.strip(function (p, g, start, end) {
			if (start == end) {
				return;
			}
			var r2 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.concentricStrip(g, start, end, r2.viewportRect(), r2.windowRect(), r2.center(), r2.minAngle(), r2.maxAngle());
		});
		renderer.scaling(function (p, unscaled) {
			return $self.getScaledValue3(unscaled);
		});
		renderer.shouldRenderLines(function (p, value) {
			var r3 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return value <= r3.effectiveMaximum();
		});
		renderer.shouldRenderContent(function (p, value) {
			var r4 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return value <= r4.effectiveMaximum();
		});
		renderer.axisLine(function (p) {
			var r5 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			$self._renderingManager.radialLine(p.axisGeometry(), r5.crossingAngleRadians(), p.viewportRect(), p.windowRect(), r5.minLength(), r5.maxLength(), r5.center());
		});
		renderer.determineCrossingValue(function (p) {
			p.crossingValue($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.outsideTop) ? p.viewportRect().top() : p.viewportRect().bottom());
			p.relativeCrossingValue(p.crossingValue());
			var r6 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			r6.crossingAngleRadians(($self.convertToDouble($self.crossingValue()) * Math.PI) / 180);
			if ($self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom)) {
				r6.crossingValue($.ig.ViewportUtils.prototype.transformYToViewport(0.5, r6.windowRect(), r6.viewportRect()) - p.viewportRect().top());
				r6.relativeCrossingValue(r6.crossingValue());
				var panel = $.ig.util.cast($.ig.RadialAxisLabelPanel.prototype.$type, $self.labelPanel());
				if (panel != null) {
					var yVal = 0;
					if ($self.labelSettings() != null && $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop) {
						yVal = 1;
					}
					panel.rotationCenter({ __x: $.ig.ViewportUtils.prototype.transformXToViewport(0.5, r6.windowRect(), r6.viewportRect()), __y: yVal, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					panel.crossingAngle(r6.crossingAngleRadians());
				}
			}
		});
		renderer.shouldRenderLabel(function (p, v, last) {
			var r7 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			if ($self.angleAxis() == null) {
				return false;
			}
			if (v > r7.effectiveMaximum()) {
				return false;
			}
			var embedded = false;
			embedded = $self.labelSettings() == null || ($self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop || $self.labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideBottom);
			var panelAngle = 0;
			if (embedded) {
				panelAngle = $self.crossingValueRadians();
			}
			var x = r7.center().__x + v * Math.cos(panelAngle);
			var y = r7.center().__y + v * Math.sin(panelAngle);
			x = $.ig.ViewportUtils.prototype.transformXToViewport(x, r7.windowRect(), r7.viewportRect());
			y = $.ig.ViewportUtils.prototype.transformYToViewport(y, r7.windowRect(), r7.viewportRect());
			if (x <= p.viewportRect().right() && x >= p.viewportRect().left() && ((y <= p.viewportRect().bottom() && y >= p.viewportRect().top()) || !embedded)) {
				return true;
			}
			return false;
		});
		renderer.getLabelLocation(function (p, value) {
			var r8 = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, p);
			return new $.ig.LabelPosition($.ig.ViewportUtils.prototype.transformXToViewport(r8.center().__x + value, r8.windowRect(), r8.viewportRect()));
		});
		renderer.snapMajorValue(function (p, value, i, interval) {
			if (value < p.actualMinimumValue()) {
				return p.actualMinimumValue();
			} else if (value > p.actualMaximumValue()) {
				return p.actualMaximumValue();
			}
			return value;
		});
		return renderer;
	}
	,
	getScaledValue: function (unscaledValue, p) {
		return this.getScaledValue3(unscaledValue);
	}
	,
	getScaledValue4: function (unscaledValue, isLogarithmic, isInverted, radiusExtentScale, innerRadiusExtentScale) {
		var scaledValue = 0;
		if (isLogarithmic) {
			if (unscaledValue <= 0) {
				scaledValue = (Math.log(this.actualMinimumValue()) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
			} else {
				scaledValue = (Math.log(unscaledValue) - this.logActualMinimumValue()) / (this.logActualMaximumValue() - this.logActualMinimumValue());
			}
		} else {
			scaledValue = (unscaledValue - this.actualMinimumValue()) / (this.actualMaximumValue() - this.actualMinimumValue());
		}
		if (isInverted) {
			scaledValue = 1 - scaledValue;
		}
		scaledValue = innerRadiusExtentScale + (scaledValue * (radiusExtentScale - innerRadiusExtentScale));
		scaledValue /= 2;
		return scaledValue;
	}
	,
	getScaledValue3: function (unscaledValue) {
		return this.getScaledValue4(unscaledValue, this.isReallyLogarithmic(), this.isInvertedCached(), this.actualRadiusExtentScale(), this.actualInnerRadiusExtentScale());
	}
	,
	getUnscaledValue3: function (scaledValue) {
		var unscaledValue = scaledValue * 2;
		unscaledValue = (unscaledValue - this.actualInnerRadiusExtentScale()) / (this.actualRadiusExtentScale() - this.actualInnerRadiusExtentScale());
		if (this.isInverted()) {
			unscaledValue = 1 - unscaledValue;
		}
		if (this.isReallyLogarithmic()) {
			return Math.exp(unscaledValue * (this.logActualMaximumValue() - this.logActualMinimumValue()) + this.logActualMinimumValue());
		} else {
			return this.actualMinimumValue() + unscaledValue * (this.actualMaximumValue() - this.actualMinimumValue());
		}
	}
	,
	getUnscaledValue: function (scaledValue, p) {
		return this.getUnscaledValue3(scaledValue);
	}
	,
	radiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty);
		}
	}
	,
	_actualRadiusExtentScale: 0,
	actualRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._actualRadiusExtentScale = value;
			return value;
		} else {
			return this._actualRadiusExtentScale;
		}
	}
	,
	innerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty);
		}
	}
	,
	_actualInnerRadiusExtentScale: 0,
	actualInnerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._actualInnerRadiusExtentScale = value;
			return value;
		} else {
			return this._actualInnerRadiusExtentScale;
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.Axis.prototype.crossingValuePropertyName:
				this.crossingValueRadians(this.convertToDouble(this.crossingValue()) * Math.PI / 180);
				break;
		}
		$.ig.NumericAxisBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName:
				this.actualRadiusExtentScale(this.radiusExtentScale());
				if (this.actualRadiusExtentScale() < 0) {
					this.actualRadiusExtentScale(0.1);
				}
				if (this.actualRadiusExtentScale() > 1) {
					this.actualRadiusExtentScale(1);
				}
				if (this.actualInnerRadiusExtentScale() >= this.actualRadiusExtentScale()) {
					this.actualInnerRadiusExtentScale(this.actualRadiusExtentScale() - 0.01);
					if (this.actualInnerRadiusExtentScale() < 0) {
						this.actualInnerRadiusExtentScale(0);
						this.actualRadiusExtentScale(0.01);
					}
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				if (this.angleAxis() != null) {
					this.angleAxis().renderAxis();
				}
				var en = this.directSeries().getEnumerator();
				while (en.moveNext()) {
					var s = en.current();
					s.renderSeries(false);
				}
				break;
			case $.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName:
				this.actualInnerRadiusExtentScale(this.innerRadiusExtentScale());
				if (this.actualInnerRadiusExtentScale() < 0) {
					this.actualInnerRadiusExtentScale(0.1);
				}
				if (this.actualInnerRadiusExtentScale() > 1) {
					this.actualInnerRadiusExtentScale(1);
				}
				if (this.actualInnerRadiusExtentScale() >= this.actualRadiusExtentScale()) {
					this.actualInnerRadiusExtentScale(this.actualRadiusExtentScale() - 0.01);
					if (this.actualInnerRadiusExtentScale() < 0) {
						this.actualInnerRadiusExtentScale(0);
						this.actualRadiusExtentScale(0.01);
					}
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				if (this.angleAxis() != null) {
					this.angleAxis().renderAxis();
				}
				var en1 = this.directSeries().getEnumerator();
				while (en1.moveNext()) {
					var s1 = en1.current();
					s1.thumbnailDirty(true);
					s1.renderSeries(false);
				}
				if (this.seriesViewer() != null) {
					this.seriesViewer().notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.Axis.prototype.crossingAxisPropertyName:
				var angleAxis = $.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, newValue);
				var catAxis = $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, newValue);
				if (angleAxis == null && catAxis == null) {
					this.onAngleAxisChanged(null);
				}
				if (angleAxis != null) {
					this.onAngleAxisChanged(angleAxis);
					angleAxis.onRadiusAxisChanged(this);
				}
				if (catAxis != null) {
					this.onAngleAxisChanged(catAxis);
					catAxis.onRadiusAxisChanged(this);
				}
				this.mustInvalidateLabels(true);
				this.renderAxis1(false);
				break;
			case $.ig.Axis.prototype.isInvertedPropertyName:
				if (this.angleAxis() != null) {
					this.angleAxis().mustInvalidateLabels(true);
					this.angleAxis().renderAxis1(false);
				}
				break;
		}
	}
	,
	_crossingValueRadians: 0,
	crossingValueRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingValueRadians = value;
			return value;
		} else {
			return this._crossingValueRadians;
		}
	}
	,
	onAngleAxisChanged: function (angleAxis) {
		this.angleAxis(angleAxis);
	}
	,
	__angleAxis: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this.__angleAxis = value;
			return value;
		} else {
			if (this.__angleAxis != null) {
				return this.__angleAxis;
			}
			var dataChart = $.ig.util.cast($.ig.XamDataChart.prototype.$type, this.seriesViewer());
			if (dataChart != null) {
				for (var i = 0; i < dataChart.axes().count(); i++) {
					if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, dataChart.axes().__inner[i]) !== null || $.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, dataChart.axes().__inner[i]) !== null) {
						return dataChart.axes().__inner[i];
					}
				}
			}
			return this.__angleAxis;
		}
	}
	,
	createRenderingParamsInstance: function () {
		return new $.ig.PolarAxisRenderingParameters();
	}
	,
	createRenderingParams: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, $.ig.NumericAxisBase.prototype.createRenderingParams.call(this, viewportRect, windowRect, effectiveViewportRect, contentViewport));
		var closestRadius = this._renderingManager.getClosestRadiusValue(windowRect);
		var furthestRadius = this._renderingManager.getFurthestRadiusValue(windowRect);
		var maxRadius = 0.5 * this.actualRadiusExtentScale();
		var minRadius = 0.5 * this.actualInnerRadiusExtentScale();
		var visibleMinimum, visibleMaximum;
		if ($.ig.Rect.prototype.l_op_Equality(windowRect, $.ig.SeriesViewer.prototype.standardRect)) {
			visibleMaximum = this.actualMaximumValue();
			visibleMinimum = this.actualMinimumValue();
		} else {
			visibleMaximum = Math.min(furthestRadius, maxRadius);
			visibleMinimum = this.getUnscaledValue3(closestRadius);
			visibleMaximum = this.getUnscaledValue3(visibleMaximum);
			var $ret = this.snapVisibleExtents(viewportRect, windowRect, visibleMinimum, visibleMaximum);
			visibleMinimum = $ret.p2;
			visibleMaximum = $ret.p3;
		}
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var minLen = closestRadius;
		var maxLen = furthestRadius;
		var resolution = Math.min(viewportRect.width(), viewportRect.height()) * (this.actualRadiusExtentScale() - this.actualInnerRadiusExtentScale()) / 2;
		renderingParams.center(center);
		var trueMaxLen = Math.max(maxLen, minLen);
		var trueMinLen = Math.min(minLen, maxLen);
		renderingParams.maxLength(trueMaxLen);
		renderingParams.minLength(trueMinLen);
		var trueVisibleMinimum = Math.min(visibleMinimum, visibleMaximum);
		var trueVisibleMaximum = Math.max(visibleMinimum, visibleMaximum);
		if (trueVisibleMinimum < this.actualMinimumValue()) {
			trueVisibleMinimum = this.actualMinimumValue();
		}
		if (trueVisibleMaximum > this.actualMaximumValue()) {
			trueVisibleMaximum = this.actualMaximumValue();
		}
		this.actualVisibleMinimumValue(trueVisibleMinimum);
		this.actualVisibleMaximumValue(trueVisibleMaximum);
		renderingParams.rangeInfos().add((function () {
			var $ret = new $.ig.RangeInfo();
			$ret.visibleMinimum(trueVisibleMinimum);
			$ret.visibleMaximum(trueVisibleMaximum);
			$ret.resolution(resolution);
			return $ret;
		}()));
		var angleAxis = $.ig.util.cast($.ig.IAngleScaler.prototype.$type, this.angleAxis());
		if (angleAxis != null) {
			var minAngle;
			var maxAngle;
			var $ret1 = angleAxis.getMinMaxAngle(windowRect, minAngle, maxAngle);
			minAngle = $ret1.p1;
			maxAngle = $ret1.p2;
			renderingParams.minAngle(Math.min(minAngle, maxAngle));
			renderingParams.maxAngle(Math.max(minAngle, maxAngle));
		}
		var effectiveMaximum = this.getEffectiveMaximumLength();
		if (maxLen >= maxRadius) {
			maxLen = effectiveMaximum;
		}
		if (minLen < minRadius) {
			minLen = minRadius;
		}
		renderingParams.minLength(minLen);
		renderingParams.maxLength(maxLen);
		renderingParams.effectiveMaximum(effectiveMaximum);
		renderingParams.tickmarkValues(this.actualTickmarkValues());
		var linearTicks = $.ig.util.cast($.ig.LinearTickmarkValues.prototype.$type, renderingParams.tickmarkValues());
		if (linearTicks != null) {
			linearTicks.minTicks(this.getMinTicks(center, renderingParams.minLength(), renderingParams.maxLength(), windowRect, viewportRect));
		}
		return renderingParams;
	}
	,
	snapVisibleExtents: function (viewportRect, windowRect, visibleMinimum, visibleMaximum) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var extent = 0;
		if (this.isInverted()) {
			extent = this.getScaledValue3(this.actualMinimumValue());
		} else {
			extent = this.getScaledValue3(this.actualMaximumValue());
		}
		var crossingValue = 0;
		if (this.crossingValue() != null) {
			crossingValue = this.crossingValueRadians();
		}
		var x = center.__x + extent * Math.cos(crossingValue);
		var y = center.__y + extent * Math.sin(crossingValue);
		center.__x = $.ig.ViewportUtils.prototype.transformXToViewport(center.__x, windowRect, viewportRect);
		center.__y = $.ig.ViewportUtils.prototype.transformYToViewport(center.__y, windowRect, viewportRect);
		x = $.ig.ViewportUtils.prototype.transformXToViewport(x, windowRect, viewportRect);
		y = $.ig.ViewportUtils.prototype.transformYToViewport(y, windowRect, viewportRect);
		if (x >= viewportRect.left() && x <= viewportRect.right() && y >= viewportRect.top() && y <= viewportRect.bottom()) {
			if (this.isInverted()) {
				visibleMaximum = this.actualMinimumValue();
			} else {
				visibleMaximum = this.actualMaximumValue();
			}
		}
		if (center.__x >= viewportRect.left() && center.__x <= viewportRect.right() && center.__y >= viewportRect.top() && center.__y <= viewportRect.bottom()) {
			if (this.isInverted()) {
				visibleMinimum = this.actualMaximumValue();
			} else {
				visibleMinimum = this.actualMinimumValue();
			}
		}
		return {
			p2: visibleMinimum,
			p3: visibleMaximum
		};
	}
	,
	getMinTicks: function (center, minLen, maxLen, windowRect, viewportRect) {
		var radViewportLength = $.ig.ViewportUtils.prototype.transformXToViewportLength(maxLen - minLen, windowRect, viewportRect);
		var viewportRatio = radViewportLength / Math.min(viewportRect.width(), viewportRect.height());
		if (viewportRatio > 0.7) {
			return 10;
		}
		return 5;
	}
	,
	getEffectiveMaximumLength: function () {
		var value = 0;
		if (!this.isInverted()) {
			value = this.getScaledValue3(this.actualMaximumValue());
		} else {
			value = this.getScaledValue3(this.actualMinimumValue());
		}
		return value;
	}
	,
	onRangeChanged: function (ea) {
		if (this.angleAxis() != null) {
			this.angleAxis().renderAxis();
		}
	}
	,
	renderAxisOverride: function (animate) {
		$.ig.NumericAxisBase.prototype.renderAxisOverride.call(this, animate);
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = !windowRect.isEmpty() ? this.viewportRect() : $.ig.Rect.prototype.empty();
		var effectiveViewportRect = this.getCurrentEffectiveViewport();
		var contentViewport = this.getContentViewport();
		var renderParams = this.renderer().render(animate, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		if (renderParams != null) {
			this.actualInterval(renderParams.tickmarkValues().interval());
			this.actualMinorInterval(renderParams.tickmarkValues().actualMinorInterval());
		}
	}
	,
	viewportChangedOverride: function (oldRect, newRect) {
		$.ig.NumericAxisBase.prototype.viewportChangedOverride.call(this, oldRect, newRect);
		if (newRect.height() != oldRect.height() || newRect.width() != oldRect.width()) {
			this.updateRange();
		}
	}
	,
	defineClipRegion: function (geom, viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		var renderingParams = $.ig.util.cast($.ig.IPolarRadialRenderingParameters.prototype.$type, this.createRenderingParams(viewportRect, windowRect, effectiveViewportRect, contentViewport));
		if (renderingParams == null) {
			return;
		}
		this._renderingManager.concentricStrip(geom.children(), renderingParams.minLength(), renderingParams.maxLength(), viewportRect, windowRect, renderingParams.center(), renderingParams.minAngle(), renderingParams.maxAngle());
	}
	,
	orientation: function () {
		return $.ig.AxisOrientation.prototype.radial;
	}
	,
	$type: new $.ig.Type('NumericRadiusAxis', $.ig.NumericAxisBase.prototype.$type)
}, true);

$.ig.util.defType('PolarAxes', 'Object', {
	_radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this._radiusAxis = value;
			return value;
		} else {
			return this._radiusAxis;
		}
	}
	,
	_angleAxis: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this._angleAxis = value;
			return value;
		} else {
			return this._angleAxis;
		}
	}
	,
	init: function (radiusAxis, angleAxis) {
		this._center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		$.ig.Object.prototype.init.call(this);
		this.radiusAxis(radiusAxis);
		this.angleAxis(angleAxis);
	},
	_center: null,
	getXValue: function (angle, radius, windowRect, viewportRect, cosStrategy) {
		var X = this._center.__x + (radius * cosStrategy(angle));
		return $.ig.ViewportUtils.prototype.transformXToViewport(X, windowRect, viewportRect);
	}
	,
	getScaledPoint: function (angleValue, radiusValue, windowRect, viewportRect, angleLogarithmic, angleInverted, radiusLogarithmic, radiusInverted, radiusExtentScale, innerRadiusExtentScale) {
		var scaledAngle = this.angleAxis().getScaledAngle1(angleValue, angleLogarithmic, angleInverted);
		var scaledRadius = this.radiusAxis().getScaledValue4(radiusValue, radiusLogarithmic, radiusInverted, radiusExtentScale, innerRadiusExtentScale);
		var cX = this._center.__x;
		var cY = this._center.__y;
		var x = cX + (scaledRadius * Math.cos(scaledAngle));
		var y = cY + (scaledRadius * Math.sin(scaledAngle));
		x = viewportRect.left() + viewportRect.width() * (x - windowRect.left()) / windowRect.width();
		y = viewportRect.top() + viewportRect.height() * (y - windowRect.top()) / windowRect.height();
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getScaledPoints: function (points, angleColumn, radiusColumn, windowRect, viewportRect, cosStrategy, sinStrategy, excludeOutOfRange) {
		var count = Math.min(angleColumn != null ? angleColumn.count() : 0, radiusColumn != null ? radiusColumn.count() : 0);
		points.clear();
		var scaledAngle;
		var scaledRadius;
		var cX = this._center.__x;
		var cY = this._center.__y;
		var X;
		var Y;
		var angleMin = Math.min(this.angleAxis().actualMinimumValue(), this.angleAxis().actualMaximumValue());
		var angleMax = Math.max(this.angleAxis().actualMinimumValue(), this.angleAxis().actualMaximumValue());
		var radiusMin = Math.min(this.radiusAxis().actualMaximumValue(), this.radiusAxis().actualMinimumValue());
		var radiusMax = Math.max(this.radiusAxis().actualMaximumValue(), this.radiusAxis().actualMinimumValue());
		var angleLogarithmic = this.angleAxis().isReallyLogarithmic();
		var angleInverted = this.angleAxis().isInverted();
		var radiusLogarithmic = this.radiusAxis().isReallyLogarithmic();
		var radiusInverted = this.radiusAxis().isInverted();
		var radiusExtentScale = this.radiusAxis().actualRadiusExtentScale();
		var innerRadiusExtentScale = this.radiusAxis().actualInnerRadiusExtentScale();
		for (var i = 0; i < count; i++) {
			if ((angleColumn.item(i) <= angleMax && angleColumn.item(i) >= angleMin && radiusColumn.item(i) <= radiusMax && radiusColumn.item(i) >= radiusMin) || ($.ig.util.isNaN(angleColumn.item(i)) || $.ig.util.isNaN(radiusColumn.item(i)))) {
				scaledAngle = this.angleAxis().getScaledAngle1(angleColumn.item(i), angleLogarithmic, angleInverted);
				scaledRadius = this.radiusAxis().getScaledValue4(radiusColumn.item(i), radiusLogarithmic, radiusInverted, radiusExtentScale, innerRadiusExtentScale);
				X = cX + (scaledRadius * cosStrategy(i, scaledAngle));
				Y = cY + (scaledRadius * sinStrategy(i, scaledAngle));
				X = viewportRect.left() + viewportRect.width() * (X - windowRect.left()) / windowRect.width();
				Y = viewportRect.top() + viewportRect.height() * (Y - windowRect.top()) / windowRect.height();
				points.add({ __x: X, __y: Y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			} else {
				points.add({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
	}
	,
	getUnscaledValues: function (scaledXValue, scaledYValue, windowRect, viewportRect, unscaledAngle, unscaledRadius) {
		var X = $.ig.ViewportUtils.prototype.transformXFromViewport(scaledXValue, windowRect, viewportRect);
		var Y = $.ig.ViewportUtils.prototype.transformYFromViewport(scaledYValue, windowRect, viewportRect);
		var scaledRadius = Math.sqrt(Math.pow(X - this._center.__x, 2) + Math.pow(Y - this._center.__y, 2));
		var scaledAngle = Math.acos((X - this._center.__x) / scaledRadius);
		if ((Y - this._center.__y) < 0) {
			scaledAngle = (2 * Math.PI) - scaledAngle;
		}
		unscaledAngle = this.angleAxis().getUnscaledAngle(scaledAngle);
		unscaledRadius = this.radiusAxis().getUnscaledValue3(scaledRadius);
		return {
			p4: unscaledAngle,
			p5: unscaledRadius
		};
	}
	,
	getYValue: function (angle, radius, windowRect, viewportRect, sinStrategy) {
		var Y = this._center.__y + (radius * sinStrategy(angle));
		return $.ig.ViewportUtils.prototype.transformYToViewport(Y, windowRect, viewportRect);
	}
	,
	$type: new $.ig.Type('PolarAxes', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolarAxisRenderingManager', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	drawEllipse: function (radius, center, minAngle, maxAngle, windowRect, viewportRect) {
		var radiusX = $.ig.ViewportUtils.prototype.transformXToViewportLength(radius, windowRect, viewportRect);
		var radiusY = $.ig.ViewportUtils.prototype.transformYToViewportLength(radius, windowRect, viewportRect);
		var centerX = $.ig.ViewportUtils.prototype.transformXToViewport(center.__x, windowRect, viewportRect);
		var centerY = $.ig.ViewportUtils.prototype.transformYToViewport(center.__y, windowRect, viewportRect);
		if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
			var startPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + radius * Math.cos(minAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + radius * Math.sin(minAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var endPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + radius * Math.cos(maxAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + radius * Math.sin(maxAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var pf = new $.ig.PathFigure();
			pf.__startPoint = startPoint;
			pf.__isClosed = false;
			pf.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point(endPoint);
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			return pf;
		} else {
			var pf1 = new $.ig.PathFigure();
			pf1.__startPoint = { __x: centerX, __y: centerY - radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pf1.__isClosed = true;
			pf1.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point({ __x: centerX, __y: centerY + radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			pf1.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.isLargeArc(false);
				$ret.point({ __x: centerX, __y: centerY - radiusY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.size(new $.ig.Size(1, radiusX, radiusY));
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				return $ret;
			}()));
			return pf1;
		}
	}
	,
	concentricStrip: function (geometry, radius0, radius1, viewportRect, windowRect, center, minAngle, maxAngle) {
		var minRadius = Math.min(radius0, radius1);
		var maxRadius = Math.max(radius0, radius1);
		var strip = new $.ig.PathGeometry();
		var innerFigure = null;
		var connector1 = null;
		var outerFigure = null;
		var connector2 = null;
		if (minRadius > 0) {
			innerFigure = this.drawEllipse(minRadius, center, minAngle, maxAngle, windowRect, viewportRect);
			if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
				var seg = $.ig.util.cast($.ig.ArcSegment.prototype.$type, innerFigure.__segments.__inner[0]);
				if (seg != null) {
					var startPoint = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + maxRadius * Math.cos(maxAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + maxRadius * Math.sin(maxAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					connector1 = (function () {
						var $ret = new $.ig.LineSegment(1);
						$ret.point(startPoint);
						return $ret;
					}());
				}
			}
		}
		if (maxRadius > 0) {
			outerFigure = this.drawEllipse(maxRadius, center, minAngle, maxAngle, windowRect, viewportRect);
		}
		if (minRadius > 0) {
			if (maxAngle - minAngle < Math.PI && maxAngle - minAngle > 0) {
				var swap = outerFigure.__startPoint;
				var seg1 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, outerFigure.__segments.__inner[0]);
				if (seg1 != null) {
					outerFigure.__startPoint = seg1.point();
					seg1.point(swap);
					seg1.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
					var startPoint1 = { __x: $.ig.ViewportUtils.prototype.transformXToViewport(center.__x + minRadius * Math.cos(minAngle), windowRect, viewportRect), __y: $.ig.ViewportUtils.prototype.transformYToViewport(center.__y + minRadius * Math.sin(minAngle), windowRect, viewportRect), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					connector2 = (function () {
						var $ret = new $.ig.LineSegment(1);
						$ret.point(startPoint1);
						return $ret;
					}());
				}
			}
		}
		if (connector1 != null && connector2 != null) {
			innerFigure.__segments.add(connector1);
			var seg2 = outerFigure.__segments.__inner[0];
			outerFigure.__segments.remove(seg2);
			innerFigure.__segments.add(seg2);
			innerFigure.__segments.add(connector2);
			innerFigure.__isClosed = true;
			strip.figures().add(innerFigure);
		} else {
			if (innerFigure != null) {
				strip.figures().add(innerFigure);
				this.reverseArcFigure(outerFigure);
			}
			if (outerFigure != null) {
				strip.figures().add(outerFigure);
			}
		}
		geometry.add(strip);
	}
	,
	reverseArcFigure: function (figure) {
		if (figure.__segments.count() > 1) {
			var seg1 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[0]);
			var seg2 = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[1]);
			figure.__segments.__inner[0] = seg2;
			figure.__segments.__inner[1] = seg1;
			var startPoint = seg2.point();
			var seg2Point = figure.__startPoint;
			var seg1Point = seg1.point();
			figure.__startPoint = startPoint;
			seg2.point(seg1Point);
			seg1.point(seg2Point);
			seg1.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			seg2.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
		} else {
			var swap = figure.__startPoint;
			var seg = $.ig.util.cast($.ig.ArcSegment.prototype.$type, figure.__segments.__inner[0]);
			if (seg != null) {
				figure.__startPoint = seg.point();
				seg.point(swap);
				seg.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			}
		}
	}
	,
	concentricLine: function (geometry, radius, viewportRect, windowRect, center, startAngle, endAngle) {
		if (radius > 0) {
			var line = new $.ig.PathGeometry();
			line.figures().add(this.drawEllipse(radius, center, startAngle, endAngle, windowRect, viewportRect));
			geometry.add(line);
		}
	}
	,
	radialStrip: function (geometry, startAngle, endAngle, viewportRect, windowRect, minLength, maxLength, center) {
		var angleMin = Math.min(startAngle, endAngle);
		var angleMax = Math.max(startAngle, endAngle);
		var isLargeArc = false;
		if (angleMax - angleMin > Math.PI) {
			isLargeArc = true;
		}
		var cosAngleMin = Math.cos(angleMin);
		var sinAngleMin = Math.sin(angleMin);
		var startXmin = center.__x + cosAngleMin * minLength;
		var startYmin = center.__y + sinAngleMin * minLength;
		var endXmin = center.__x + cosAngleMin * maxLength;
		var endYmin = center.__y + sinAngleMin * maxLength;
		var cosAngleMax = Math.cos(angleMax);
		var sinAngleMax = Math.sin(angleMax);
		var startXmax = center.__x + cosAngleMax * minLength;
		var startYmax = center.__y + sinAngleMax * minLength;
		var endXmax = center.__x + cosAngleMax * maxLength;
		var endYmax = center.__y + sinAngleMax * maxLength;
		startXmin = $.ig.ViewportUtils.prototype.transformXToViewport(startXmin, windowRect, viewportRect);
		startYmin = $.ig.ViewportUtils.prototype.transformYToViewport(startYmin, windowRect, viewportRect);
		endXmin = $.ig.ViewportUtils.prototype.transformXToViewport(endXmin, windowRect, viewportRect);
		endYmin = $.ig.ViewportUtils.prototype.transformYToViewport(endYmin, windowRect, viewportRect);
		startXmax = $.ig.ViewportUtils.prototype.transformXToViewport(startXmax, windowRect, viewportRect);
		startYmax = $.ig.ViewportUtils.prototype.transformYToViewport(startYmax, windowRect, viewportRect);
		endXmax = $.ig.ViewportUtils.prototype.transformXToViewport(endXmax, windowRect, viewportRect);
		endYmax = $.ig.ViewportUtils.prototype.transformYToViewport(endYmax, windowRect, viewportRect);
		var pf = new $.ig.PathFigure();
		pf.__startPoint = { __x: startXmin, __y: startYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		pf.__isClosed = true;
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point({ __x: endXmin, __y: endYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: endXmax, __y: endYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.size(new $.ig.Size(1, $.ig.ViewportUtils.prototype.transformXToViewportLength(maxLength, windowRect, viewportRect), $.ig.ViewportUtils.prototype.transformYToViewportLength(maxLength, windowRect, viewportRect)));
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.isLargeArc(isLargeArc);
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.LineSegment(1);
			$ret.point({ __x: startXmax, __y: startYmax, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pf.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: startXmin, __y: startYmin, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.size(new $.ig.Size(1, $.ig.ViewportUtils.prototype.transformXToViewportLength(minLength, windowRect, viewportRect), $.ig.ViewportUtils.prototype.transformYToViewportLength(minLength, windowRect, viewportRect)));
			$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
			$ret.isLargeArc(isLargeArc);
			return $ret;
		}()));
		var pg = new $.ig.PathGeometry();
		pg.figures().add(pf);
		geometry.add(pg);
	}
	,
	radialLine: function (geometry, angle, viewportRect, windowRect, minLength, maxLength, center) {
		var radialLine = new $.ig.LineGeometry();
		var cosX = Math.cos(angle);
		var sinX = Math.sin(angle);
		var startX = center.__x + cosX * minLength;
		var startY = center.__y + sinX * minLength;
		var endX = center.__x + cosX * maxLength;
		var endY = center.__y + sinX * maxLength;
		startX = $.ig.ViewportUtils.prototype.transformXToViewport(startX, windowRect, viewportRect);
		startY = $.ig.ViewportUtils.prototype.transformYToViewport(startY, windowRect, viewportRect);
		endX = $.ig.ViewportUtils.prototype.transformXToViewport(endX, windowRect, viewportRect);
		endY = $.ig.ViewportUtils.prototype.transformYToViewport(endY, windowRect, viewportRect);
		radialLine.startPoint({ __x: startX, __y: startY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		radialLine.endPoint({ __x: endX, __y: endY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		geometry.add(radialLine);
	}
	,
	inXBand: function (center, bounds) {
		return center.__x >= bounds.left() && center.__x <= bounds.right();
	}
	,
	inYBand: function (center, bounds) {
		return center.__y >= bounds.top() && center.__y <= bounds.bottom();
	}
	,
	closestCorner: function (center, bounds) {
		var dist1 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist2 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist3 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		var dist4 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		return Math.min(dist1, Math.min(dist2, Math.min(dist3, dist4)));
	}
	,
	furthestCorner: function (center, bounds) {
		var dist1 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist2 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.top(), 2));
		var dist3 = Math.sqrt(Math.pow(center.__x - bounds.right(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		var dist4 = Math.sqrt(Math.pow(center.__x - bounds.left(), 2) + Math.pow(center.__y - bounds.bottom(), 2));
		return Math.max(dist1, Math.max(dist2, Math.max(dist3, dist4)));
	}
	,
	getClosestRadiusValue: function (windowRect) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.inXBand(center, windowRect) && this.inYBand(center, windowRect)) {
			return 0;
		}
		if (this.inXBand(center, windowRect)) {
			if (center.__y < windowRect.top()) {
				return windowRect.top() - center.__y;
			} else {
				return center.__y - windowRect.bottom();
			}
		}
		if (this.inYBand(center, windowRect)) {
			if (center.__x < windowRect.left()) {
				return windowRect.left() - center.__x;
			} else {
				return center.__x - windowRect.right();
			}
		}
		return this.closestCorner(center, windowRect);
	}
	,
	getFurthestRadiusValue: function (windowRect) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return this.furthestCorner(center, windowRect);
	}
	,
	getAngleTo: function (center, toPoint) {
		var radius = Math.sqrt(Math.pow(toPoint.__x - center.__x, 2) + Math.pow(toPoint.__y - center.__y, 2));
		var angle = Math.acos((toPoint.__x - center.__x) / radius);
		if ((toPoint.__y - center.__y) < 0) {
			angle = (2 * Math.PI) - angle;
		}
		return angle;
	}
	,
	lineCheck: function (maxValueRadiusPoint, rectPoint) {
		return ((maxValueRadiusPoint.__y - 0.5) * rectPoint.__x) + ((0.5 - maxValueRadiusPoint.__x) * rectPoint.__y) + ((maxValueRadiusPoint.__x * 0.5) - (0.5 * maxValueRadiusPoint.__y));
	}
	,
	getMinMaxAngle: function (windowRect, minAngle, maxAngle) {
		var center = { __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.inXBand(center, windowRect) && this.inYBand(center, windowRect)) {
			minAngle = 0;
			maxAngle = Math.PI * 2;
			return {
				p1: minAngle,
				p2: maxAngle
			};
		}
		var angle1 = this.getAngleTo(center, { __x: windowRect.left(), __y: windowRect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle2 = this.getAngleTo(center, { __x: windowRect.right(), __y: windowRect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle3 = this.getAngleTo(center, { __x: windowRect.right(), __y: windowRect.bottom(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var angle4 = this.getAngleTo(center, { __x: windowRect.left(), __y: windowRect.bottom(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		if (this.inYBand(center, windowRect) && windowRect.left() > center.__x) {
			minAngle = angle1;
			maxAngle = 2 * Math.PI + angle4;
			return {
				p1: minAngle,
				p2: maxAngle
			};
		}
		minAngle = Math.min(angle1, Math.min(angle2, Math.min(angle3, angle4)));
		maxAngle = Math.max(angle1, Math.max(angle2, Math.max(angle3, angle4)));
		return {
			p1: minAngle,
			p2: maxAngle
		};
	}
	,
	determineView: function (windowRect, renderingParams, actualMinimumValue, actualMaximumValue, isInverted, getUnscaledAngle, resolution) {
		var minAngle;
		var maxAngle;
		var $ret = this.getMinMaxAngle(windowRect, minAngle, maxAngle);
		minAngle = $ret.p1;
		maxAngle = $ret.p2;
		var trueMinAngle = Math.min(minAngle, maxAngle);
		var trueMaxAngle = Math.max(minAngle, maxAngle);
		if ($.ig.util.cast($.ig.PolarAxisRenderingParameters.prototype.$type, renderingParams) !== null) {
			(renderingParams).minAngle(trueMinAngle);
			(renderingParams).maxAngle(trueMaxAngle);
		} else if ($.ig.util.cast($.ig.RadialAxisRenderingParameters.prototype.$type, renderingParams) !== null) {
			(renderingParams).minAngle(trueMinAngle);
			(renderingParams).maxAngle(trueMaxAngle);
		}
		if (minAngle == 0 && maxAngle == Math.PI * 2) {
			var visibleMinimum = actualMinimumValue;
			var visibleMaximum = actualMaximumValue;
			var trueVisibleMinimum = Math.min(visibleMinimum, visibleMaximum);
			var trueVisibleMaximum = Math.max(visibleMinimum, visibleMaximum);
			renderingParams.rangeInfos().add((function () {
				var $ret = new $.ig.RangeInfo();
				$ret.visibleMinimum(trueVisibleMinimum);
				$ret.visibleMaximum(trueVisibleMaximum);
				$ret.resolution(resolution);
				return $ret;
			}()));
			return;
		} else {
			if (maxAngle > Math.PI * 2) {
				maxAngle = maxAngle - Math.PI * 2;
			}
			var visibleMinimum1 = getUnscaledAngle(minAngle);
			var visibleMaximum1 = getUnscaledAngle(maxAngle);
			if (visibleMinimum1 < actualMinimumValue || visibleMinimum1 > actualMaximumValue) {
				visibleMinimum1 = getUnscaledAngle(minAngle + Math.PI * 2);
			}
			if (visibleMaximum1 < actualMinimumValue || visibleMaximum1 > actualMaximumValue) {
				visibleMaximum1 = getUnscaledAngle(maxAngle + Math.PI * 2);
			}
			var trueVisibleMinimum1 = Math.min(visibleMinimum1, visibleMaximum1);
			var trueVisibleMaximum1 = Math.max(visibleMinimum1, visibleMaximum1);
			if ((!isInverted && visibleMinimum1 > visibleMaximum1) || (isInverted && visibleMinimum1 < visibleMaximum1)) {
				var range1 = (actualMaximumValue - trueVisibleMaximum1);
				var range2 = (trueVisibleMinimum1 - actualMinimumValue);
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(trueVisibleMaximum1);
					$ret.visibleMaximum(actualMaximumValue);
					$ret.resolution((range1 / (range1 + range2)) * resolution);
					return $ret;
				}()));
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(actualMinimumValue);
					$ret.visibleMaximum(trueVisibleMinimum1);
					$ret.resolution((range2 / (range1 + range2)) * resolution);
					return $ret;
				}()));
			} else {
				renderingParams.rangeInfos().add((function () {
					var $ret = new $.ig.RangeInfo();
					$ret.visibleMinimum(trueVisibleMinimum1);
					$ret.visibleMaximum(trueVisibleMaximum1);
					$ret.resolution(resolution);
					return $ret;
				}()));
			}
		}
	}
	,
	$type: new $.ig.Type('PolarAxisRenderingManager', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IPolarRadialRenderingParameters', 'Object', {
	$type: new $.ig.Type('IPolarRadialRenderingParameters', null)
}, true);

$.ig.util.defType('PolarAxisRenderingParameters', 'NumericAxisRenderingParameters', {
	init: function () {
		$.ig.NumericAxisRenderingParameters.prototype.init.call(this);
	},
	_minLength: 0,
	minLength: function (value) {
		if (arguments.length === 1) {
			this._minLength = value;
			return value;
		} else {
			return this._minLength;
		}
	}
	,
	_maxLength: 0,
	maxLength: function (value) {
		if (arguments.length === 1) {
			this._maxLength = value;
			return value;
		} else {
			return this._maxLength;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	_minAngle: 0,
	minAngle: function (value) {
		if (arguments.length === 1) {
			this._minAngle = value;
			return value;
		} else {
			return this._minAngle;
		}
	}
	,
	_maxAngle: 0,
	maxAngle: function (value) {
		if (arguments.length === 1) {
			this._maxAngle = value;
			return value;
		} else {
			return this._maxAngle;
		}
	}
	,
	_crossingAngleRadians: 0,
	crossingAngleRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingAngleRadians = value;
			return value;
		} else {
			return this._crossingAngleRadians;
		}
	}
	,
	_effectiveMaximum: 0,
	effectiveMaximum: function (value) {
		if (arguments.length === 1) {
			this._effectiveMaximum = value;
			return value;
		} else {
			return this._effectiveMaximum;
		}
	}
	,
	$type: new $.ig.Type('PolarAxisRenderingParameters', $.ig.NumericAxisRenderingParameters.prototype.$type, [$.ig.IPolarRadialRenderingParameters.prototype.$type])
}, true);

$.ig.util.defType('RadialAxisRenderingParameters', 'CategoryAxisRenderingParameters', {
	init: function () {
		$.ig.CategoryAxisRenderingParameters.prototype.init.call(this);
	},
	_minLength: 0,
	minLength: function (value) {
		if (arguments.length === 1) {
			this._minLength = value;
			return value;
		} else {
			return this._minLength;
		}
	}
	,
	_maxLength: 0,
	maxLength: function (value) {
		if (arguments.length === 1) {
			this._maxLength = value;
			return value;
		} else {
			return this._maxLength;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	_crossingAngleRadians: 0,
	crossingAngleRadians: function (value) {
		if (arguments.length === 1) {
			this._crossingAngleRadians = value;
			return value;
		} else {
			return this._crossingAngleRadians;
		}
	}
	,
	_minAngle: 0,
	minAngle: function (value) {
		if (arguments.length === 1) {
			this._minAngle = value;
			return value;
		} else {
			return this._minAngle;
		}
	}
	,
	_maxAngle: 0,
	maxAngle: function (value) {
		if (arguments.length === 1) {
			this._maxAngle = value;
			return value;
		} else {
			return this._maxAngle;
		}
	}
	,
	_effectiveMaximum: 0,
	effectiveMaximum: function (value) {
		if (arguments.length === 1) {
			this._effectiveMaximum = value;
			return value;
		} else {
			return this._effectiveMaximum;
		}
	}
	,
	$type: new $.ig.Type('RadialAxisRenderingParameters', $.ig.CategoryAxisRenderingParameters.prototype.$type, [$.ig.IPolarRadialRenderingParameters.prototype.$type])
}, true);

$.ig.util.defType('RadialAxisLabelPanel', 'HorizontalAxisLabelPanelBase', {
	init: function () {
		this.__toHide = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		$.ig.HorizontalAxisLabelPanelBase.prototype.init.call(this);
	},
	createView: function () {
		return new $.ig.RadialAxisLabelPanelView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAxisLabelPanelBase.prototype.onViewCreated.call(this, view);
		this.radialView(view);
	}
	,
	_radialView: null,
	radialView: function (value) {
		if (arguments.length === 1) {
			this._radialView = value;
			return value;
		} else {
			return this._radialView;
		}
	}
	,
	_rotationCenter: null,
	rotationCenter: function (value) {
		if (arguments.length === 1) {
			this._rotationCenter = value;
			return value;
		} else {
			return this._rotationCenter;
		}
	}
	,
	_crossingAngle: 0,
	crossingAngle: function (value) {
		if (arguments.length === 1) {
			this._crossingAngle = value;
			return value;
		} else {
			return this._crossingAngle;
		}
	}
	,
	axisIsNotEmbedded: function () {
		return this.axis().labelSettings() != null && (this.axis().labelSettings().actualLocation() != $.ig.AxisLabelsLocation.prototype.insideBottom && this.axis().labelSettings().actualLocation() != $.ig.AxisLabelsLocation.prototype.insideTop);
	}
	,
	axisIsEmbedded: function () {
		return !this.axisIsNotEmbedded();
	}
	,
	shouldTryStagger: function () {
		return this.foundCollisions() && (!this.useRotation() || this.getEffectiveAngle() == -180);
	}
	,
	shouldVerticalAlign: function () {
		return !this.useStaggering();
	}
	,
	applyPanelRotation: function (finalSize) {
		$.ig.HorizontalAxisLabelPanelBase.prototype.applyPanelRotation.call(this, finalSize);
		if (this.crossingAngle() % 360 == 0 || this.axisIsNotEmbedded()) {
			this.radialView().clearPanelRotation();
		} else {
			this.radialView().applyPanelRotation(finalSize);
		}
	}
	,
	getEffectiveAngle: function () {
		var angle = $.ig.HorizontalAxisLabelPanelBase.prototype.getEffectiveAngle.call(this);
		if (this.axisIsEmbedded()) {
			angle -= (this.crossingAngle() * 180) / Math.PI;
		}
		return angle;
	}
	,
	shouldRotate: function () {
		return this.getEffectiveAngle() % 360 != 0;
	}
	,
	shouldClip: function () {
		if (this.axisIsNotEmbedded()) {
			return true;
		}
		var angle = this.crossingAngle() * 180 / Math.PI;
		if (angle < 30 || angle > 330 || (angle > 150 && angle < 210)) {
			return true;
		}
		return false;
	}
	,
	__toHide: null,
	setLabelRotationTransform: function (label, angle) {
		var angleRadians = angle * Math.PI / 180;
		var yFactor = Math.abs(Math.sin(angleRadians));
		if (this.axis().labelSettings() != null && this.axis().labelSettings().actualLocation() == $.ig.AxisLabelsLocation.prototype.insideTop) {
			yFactor = yFactor * -1;
		}
		this.radialView().setLabelRotationalTransform(label, angle, yFactor);
	}
	,
	minimumPosition: function () {
		var min = 1.7976931348623157E+308;
		var en = this.labelPositions().getEnumerator();
		while (en.moveNext()) {
			var pos = en.current();
			min = Math.min(pos.value(), min);
		}
		return min;
	}
	,
	maximumPosition: function () {
		var max = -1.7976931348623157E+308;
		var en = this.labelPositions().getEnumerator();
		while (en.moveNext()) {
			var pos = en.current();
			max = Math.max(pos.value(), max);
		}
		return max;
	}
	,
	hideOptionalLabels: function (rectangles) {
		var $self = this;
		var val;
		if (this.axis().isInverted()) {
			val = this.minimumPosition();
		} else {
			val = this.maximumPosition();
		}
		var toHide = $.ig.Enumerable.prototype.select$2($.ig.LabelPosition.prototype.$type, $.ig.Number.prototype.$type, ($.ig.Enumerable.prototype.where$1($.ig.LabelPosition.prototype.$type, (this.labelPositions()), function (pos) { return pos.value() == val; })), function (pos) { return $self.labelPositions().indexOf(pos); });
		this.__toHide = $.ig.Enumerable.prototype.toList$1($.ig.Number.prototype.$type, toHide);
		this.foundCollisions(this.detectCollisions($.ig.Enumerable.prototype.toList$1($.ig.Rect.prototype.$type, $.ig.Enumerable.prototype.where$11($.ig.Rect.prototype.$type, rectangles, function (rect, index) { return !$self.__toHide.contains(index); }))));
	}
	,
	shouldDisplay: function (index, bounds) {
		if (this.__toHide.contains(index)) {
			return false;
		} else {
			return $.ig.HorizontalAxisLabelPanelBase.prototype.shouldDisplay.call(this, index, bounds);
		}
	}
	,
	showOptionalLabels: function () {
		this.__toHide = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		$.ig.HorizontalAxisLabelPanelBase.prototype.showOptionalLabels.call(this);
	}
	,
	getDefaultLabelsLocation: function () {
		return $.ig.AxisLabelsLocation.prototype.insideBottom;
	}
	,
	validLocation: function (location) {
		return location == $.ig.AxisLabelsLocation.prototype.insideBottom || location == $.ig.AxisLabelsLocation.prototype.insideTop || location == $.ig.AxisLabelsLocation.prototype.outsideBottom || location == $.ig.AxisLabelsLocation.prototype.outsideTop;
	}
	,
	createTicks: function () {
	}
	,
	$type: new $.ig.Type('RadialAxisLabelPanel', $.ig.HorizontalAxisLabelPanelBase.prototype.$type)
}, true);

$.ig.util.defType('CategoryTickmarkValues', 'TickmarkValues', {
	init: function () {
		this.__majorValues = null;
		$.ig.TickmarkValues.prototype.init.call(this);
	},
	_mode: 0,
	mode: function (value) {
		if (arguments.length === 1) {
			this._mode = value;
			return value;
		} else {
			return this._mode;
		}
	}
	,
	_mode2GroupCount: 0,
	mode2GroupCount: function (value) {
		if (arguments.length === 1) {
			this._mode2GroupCount = value;
			return value;
		} else {
			return this._mode2GroupCount;
		}
	}
	,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	_window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this._window = value;
			return value;
		} else {
			return this._window;
		}
	}
	,
	_isInverted: false,
	isInverted: function (value) {
		if (arguments.length === 1) {
			this._isInverted = value;
			return value;
		} else {
			return this._isInverted;
		}
	}
	,
	_getUnscaledGroupCenter: null,
	getUnscaledGroupCenter: function (value) {
		if (arguments.length === 1) {
			this._getUnscaledGroupCenter = value;
			return value;
		} else {
			return this._getUnscaledGroupCenter;
		}
	}
	,
	initialize: function (initializationParameters) {
		$.ig.TickmarkValues.prototype.initialize.call(this, initializationParameters);
		var mode = initializationParameters.mode();
		this.mode(mode);
		this.mode2GroupCount(initializationParameters.mode2GroupCount());
		this.viewport(initializationParameters.viewport());
		this.window(initializationParameters.window());
		this.isInverted(initializationParameters.isInverted());
		this.getUnscaledGroupCenter(initializationParameters.getUnscaledGroupCenter());
		var snapper = new $.ig.LinearCategorySnapper(1, initializationParameters.visibleMinimum(), initializationParameters.visibleMaximum(), initializationParameters.resolution(), initializationParameters.userInterval(), mode, initializationParameters.heuristicResolution());
		var interval = snapper.interval();
		if (initializationParameters.intervalOverride() != -1) {
			interval = initializationParameters.intervalOverride();
		}
		var firstValue = Math.floor((initializationParameters.visibleMinimum() - initializationParameters.actualMinimum()) / interval);
		var lastValue = Math.ceil((initializationParameters.visibleMaximum() - initializationParameters.actualMinimum()) / interval);
		var first = $.ig.truncate(firstValue);
		var last = $.ig.truncate(lastValue);
		var minorCount = $.ig.truncate(snapper.minorCount());
		if (initializationParameters.minorCountOverride() != -1) {
			minorCount = initializationParameters.minorCountOverride();
		}
		this.interval(interval);
		this.minorInterval(initializationParameters.minorInterval());
		this.firstIndex(first);
		this.lastIndex(last);
		this.minorCount(minorCount);
		this.actualMinimum(initializationParameters.actualMinimum());
	}
	,
	_actualMinimum: 0,
	actualMinimum: function (value) {
		if (arguments.length === 1) {
			this._actualMinimum = value;
			return value;
		} else {
			return this._actualMinimum;
		}
	}
	,
	__majorValues: null,
	majorValuesArray: function () {
		var firstIndex = this.firstIndex();
		var count = this.lastIndex() - firstIndex + 1;
		if (count < 0) {
			count = 0;
		}
		if (this.__majorValues == null || this.__majorValues.length != count) {
			this.__majorValues = new Array(count);
		}
		var array = this.__majorValues;
		for (var i = 0; i < count; ++i) {
			var major = this.actualMinimum() + (i + firstIndex) * this.interval();
			array[i] = major;
		}
		return array;
	}
	,
	__minorValues: null,
	minorValuesArray: function () {
		var interval = this.interval();
		var lastIndex = this.lastIndex();
		var firstIndex = this.firstIndex();
		var mode2GroupCount = this.mode2GroupCount();
		var mode = this.mode();
		interval = Math.min(interval, 20);
		var count = 0;
		for (var i = firstIndex; i < lastIndex; ++i) {
			if (mode != $.ig.CategoryMode.prototype.mode0 && mode2GroupCount != 0) {
				for (var categoryNumber = 0; categoryNumber < $.ig.truncate(interval); categoryNumber++) {
					for (var groupNumber = 0; groupNumber < mode2GroupCount; groupNumber++) {
						count++;
					}
				}
			}
		}
		if (this.__minorValues == null || this.__minorValues.length != count) {
			this.__minorValues = new Array(count);
		}
		var array = this.__minorValues;
		var pos = 0;
		for (var i1 = firstIndex; i1 < lastIndex; ++i1) {
			if (mode != $.ig.CategoryMode.prototype.mode0 && mode2GroupCount != 0) {
				for (var categoryNumber1 = 0; categoryNumber1 < $.ig.truncate(interval); categoryNumber1++) {
					for (var groupNumber1 = 0; groupNumber1 < mode2GroupCount; groupNumber1++) {
						var center = this.getUnscaledGroupCenter()(groupNumber1);
						var minorValue = categoryNumber1 + (i1 * interval) + center;
						array[pos] = minorValue;
						pos++;
					}
				}
			}
		}
		return this.__minorValues;
	}
	,
	$type: new $.ig.Type('CategoryTickmarkValues', $.ig.TickmarkValues.prototype.$type)
}, true);

$.ig.util.defType('ViewportUtils', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	transformXToViewport: function (xValue, windowRect, viewportRect) {
		return viewportRect.left() + viewportRect.width() * (xValue - windowRect.left()) / windowRect.width();
	}
	,
	transformXFromViewport: function (xValue, windowRect, viewportRect) {
		return ((xValue - viewportRect.left()) * windowRect.width() / viewportRect.width()) + windowRect.left();
	}
	,
	transformYToViewport: function (yValue, windowRect, viewportRect) {
		return viewportRect.top() + viewportRect.height() * (yValue - windowRect.top()) / windowRect.height();
	}
	,
	transformYFromViewport: function (yValue, windowRect, viewportRect) {
		return ((yValue - viewportRect.top()) * windowRect.height() / viewportRect.height()) + windowRect.top();
	}
	,
	transformXToViewportLength: function (xValue, windowRect, viewportRect) {
		return viewportRect.width() * xValue / windowRect.width();
	}
	,
	transformXFromViewportLength: function (xValue, windowRect, viewportRect) {
		return windowRect.width() * xValue / viewportRect.width();
	}
	,
	transformYToViewportLength: function (yValue, windowRect, viewportRect) {
		return viewportRect.height() * yValue / windowRect.height();
	}
	,
	$type: new $.ig.Type('ViewportUtils', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MarkerManagerBase', 'Object', {
	_getItemLocationsStrategy: null,
	getItemLocationsStrategy: function (value) {
		if (arguments.length === 1) {
			this._getItemLocationsStrategy = value;
			return value;
		} else {
			return this._getItemLocationsStrategy;
		}
	}
	,
	_provideMarkerStrategy: null,
	provideMarkerStrategy: function (value) {
		if (arguments.length === 1) {
			this._provideMarkerStrategy = value;
			return value;
		} else {
			return this._provideMarkerStrategy;
		}
	}
	,
	_removeUnusedMarkers: null,
	removeUnusedMarkers: function (value) {
		if (arguments.length === 1) {
			this._removeUnusedMarkers = value;
			return value;
		} else {
			return this._removeUnusedMarkers;
		}
	}
	,
	_provideItemStrategy: null,
	provideItemStrategy: function (value) {
		if (arguments.length === 1) {
			this._provideItemStrategy = value;
			return value;
		} else {
			return this._provideItemStrategy;
		}
	}
	,
	_activeMarkerIndexesStrategy: null,
	activeMarkerIndexesStrategy: function (value) {
		if (arguments.length === 1) {
			this._activeMarkerIndexesStrategy = value;
			return value;
		} else {
			return this._activeMarkerIndexesStrategy;
		}
	}
	,
	_useDeterministicSelection: false,
	useDeterministicSelection: function (value) {
		if (arguments.length === 1) {
			this._useDeterministicSelection = value;
			return value;
		} else {
			return this._useDeterministicSelection;
		}
	}
	,
	init: function (provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy) {
		$.ig.Object.prototype.init.call(this);
		this.provideMarkerStrategy(provideMarkerStrategy);
		this.provideItemStrategy(provideItemStrategy);
		this.removeUnusedMarkers(removeUnusedMarkers);
		this.getItemLocationsStrategy(getItemLocationsStrategy);
		this.activeMarkerIndexesStrategy(activeMarkerIndexesStrategy);
	},
	winnowMarkers: function (markers, maximumMarkers, windowRect, viewportRect, currentResolution) {
	}
	,
	render: function (markers, lightweight) {
	}
	,
	activeFirstKeys: function (buckets, keys) {
		var first = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var second = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var en = keys.getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (buckets.item(key).priorityItems().count() > 0) {
				first.add(key);
			} else {
				second.add(key);
			}
		}
		var ret = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		ret.addRange(first);
		ret.addRange(second);
		return ret;
	}
	,
	selectMarkerItems: function (numToSelect, buckets, keys, markerItems) {
		while (numToSelect > 0) {
			if (numToSelect < keys.count()) {
				if (!$.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
					var ikeys = keys;
					$.ig.ArrayUtil.prototype.shuffle$1($.ig.Number.prototype.$type, ikeys);
				}
				keys = this.activeFirstKeys(buckets, keys);
				var count = numToSelect;
				for (var i = 0; i < count; i++) {
					var keyIndex = i;
					var bucket = buckets.item(keys.__inner[keyIndex]);
					var wasPriority;
					var index = (function () { var $ret = bucket.getItem(wasPriority); wasPriority = $ret.p0; return $ret.ret; }());
					markerItems.add(index);
					numToSelect--;
					if (bucket.isEmpty()) {
						buckets.remove(keys.__inner[keyIndex]);
					}
				}
			} else {
				var en = keys.getEnumerator();
				while (en.moveNext()) {
					var key = en.current();
					var bucket1 = buckets.item(key);
					var wasPriority1;
					var index1 = (function () { var $ret = bucket1.getItem(wasPriority1); wasPriority1 = $ret.p0; return $ret.ret; }());
					markerItems.add(index1);
					numToSelect--;
					if (bucket1.isEmpty()) {
						buckets.remove(key);
					}
				}
				keys = new $.ig.List$1($.ig.Number.prototype.$type, 1, buckets.keys());
			}
		}
	}
	,
	getVisibleItems: function (windowRect, viewportRect, itemLocations, visibleItems) {
		var left = viewportRect.left();
		var right = viewportRect.right();
		var top = viewportRect.top();
		var bottom = viewportRect.bottom();
		if (!windowRect.isEmpty() && !viewportRect.isEmpty()) {
			for (var i = 0; i < itemLocations.count(); ++i) {
				var x = itemLocations.item(i).__x;
				if ($.ig.util.isNaN(x)) {
					continue;
				}
				var y = itemLocations.item(i).__y;
				if ($.ig.util.isNaN(y)) {
					continue;
				}
				if (x < left || x > right) {
					continue;
				}
				if (y < top || y > bottom) {
					continue;
				}
				visibleItems.add(i);
			}
		}
	}
	,
	getBuckets: function (viewportRect, visibleItems, resolution, itemLocations) {
		var wasActive = new Array(itemLocations.count());
		var en = this.activeMarkerIndexesStrategy()().getEnumerator();
		while (en.moveNext()) {
			var index = en.current();
			if (index != -1) {
				wasActive[index] = true;
			}
		}
		var rowSize = $.ig.truncate(Math.floor(viewportRect.width() / resolution));
		var buckets = new $.ig.Dictionary$2($.ig.Number.prototype.$type, $.ig.MarkerManagerBucket.prototype.$type, 0);
		var en1 = visibleItems.getEnumerator();
		while (en1.moveNext()) {
			var index1 = en1.current();
			var xVal = itemLocations.item(index1).__x;
			var yVal = itemLocations.item(index1).__y;
			var rowNumber = $.ig.truncate(Math.floor(yVal / resolution));
			var colNumber = $.ig.truncate(Math.floor(xVal / resolution));
			var offset = (rowNumber * rowSize) + colNumber;
			var bucket;
			if (!(function () { var $ret = buckets.tryGetValue(offset, bucket); bucket = $ret.p1; return $ret.ret; }())) {
				bucket = new $.ig.MarkerManagerBucket();
				buckets.add(offset, bucket);
			}
			if (wasActive[index1]) {
				bucket.priorityItems().add(index1);
			} else {
				bucket.items().add(index1);
			}
		}
		return buckets;
	}
	,
	$type: new $.ig.Type('MarkerManagerBase', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('NumericMarkerManager', 'MarkerManagerBase', {
	init: function (initNumber, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.NumericMarkerManager.prototype.init1.call(this, 1, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy, function () {
			return $.ig.CollisionAvoidanceType.prototype.none;
		});
	},
	init1: function (initNumber, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy, getCollisionAvoidanceStrategy) {
		$.ig.MarkerManagerBase.prototype.init.call(this, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy);
		this.populateColumnValues(false);
		this.getColumnValues(function (i) { return { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }; });
		this.getCollisionAvoidanceStrategy(getCollisionAvoidanceStrategy);
	},
	_populateColumnValues: false,
	populateColumnValues: function (value) {
		if (arguments.length === 1) {
			this._populateColumnValues = value;
			return value;
		} else {
			return this._populateColumnValues;
		}
	}
	,
	_getColumnValues: null,
	getColumnValues: function (value) {
		if (arguments.length === 1) {
			this._getColumnValues = value;
			return value;
		} else {
			return this._getColumnValues;
		}
	}
	,
	_getCollisionAvoidanceStrategy: null,
	getCollisionAvoidanceStrategy: function (value) {
		if (arguments.length === 1) {
			this._getCollisionAvoidanceStrategy = value;
			return value;
		} else {
			return this._getCollisionAvoidanceStrategy;
		}
	}
	,
	_getMarkerDesiredSize: null,
	getMarkerDesiredSize: function (value) {
		if (arguments.length === 1) {
			this._getMarkerDesiredSize = value;
			return value;
		} else {
			return this._getMarkerDesiredSize;
		}
	}
	,
	winnowMarkers: function (markers, maximumMarkers, windowRect, viewportRect, currentResolution) {
		var itemLocations = this.getItemLocationsStrategy()();
		markers.clear();
		var visibleItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		maximumMarkers = Math.max(0, maximumMarkers);
		var markerItems = null;
		this.getVisibleItems(windowRect, viewportRect, itemLocations, visibleItems);
		if (maximumMarkers >= visibleItems.count()) {
			markerItems = visibleItems;
		} else {
			markerItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var resolution = Math.max(8, currentResolution);
			var buckets = this.getBuckets(viewportRect, visibleItems, resolution, itemLocations);
			var keys = new $.ig.List$1($.ig.Number.prototype.$type, 1, buckets.keys());
			if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
				keys.sort();
			}
			this.selectMarkerItems(maximumMarkers, buckets, keys, markerItems);
		}
		this.assignMarkers(markers, itemLocations, markerItems);
	}
	,
	assignMarkers: function (markers, itemLocations, markerItems) {
		for (var i = 0; i < markerItems.count(); ++i) {
			var index = markerItems.__inner[i];
			var point = itemLocations[index];
			var item = this.provideItemStrategy()(index);
			var marker = this.provideMarkerStrategy()(item);
			if (marker.content() != null) {
				(marker.content()).item(item);
			}
			var mp = new $.ig.OwnedPoint();
			if (this.populateColumnValues()) {
				mp.columnValues(this.getColumnValues()(index));
			}
			mp.ownerItem(item);
			mp.point({ __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			if (!markers.containsKey(item)) {
				markers.add(item, mp);
			}
		}
	}
	,
	render: function (markers, lightweight) {
		var keys = markers.keys();
		if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
			var keysList = new $.ig.List$1($.ig.Object.prototype.$type, 1, markers.keys());
			keysList.sort2(function (o1, o2) {
				var point1 = markers.item(o1);
				var point2 = markers.item(o2);
				var dist1 = Math.pow(point1.point().__x, 2) + Math.pow(point1.point().__y, 2);
				var dist2 = Math.pow(point2.point().__x, 2) + Math.pow(point2.point().__y, 2);
				return (dist1).compareTo(dist2);
			});
			keys = keysList;
		}
		var smartPlacer = null;
		var wrapper = null;
		switch (this.getCollisionAvoidanceStrategy()()) {
			case $.ig.CollisionAvoidanceType.prototype.none: break;
			case $.ig.CollisionAvoidanceType.prototype.omit:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.3);
					$ret.fade(0);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				wrapper.noWiggle(true);
				break;
			case $.ig.CollisionAvoidanceType.prototype.fade:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.6);
					$ret.fade(2);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				wrapper.noWiggle(true);
				break;
			case $.ig.CollisionAvoidanceType.prototype.omitAndShift:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.3);
					$ret.fade(0);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				break;
			case $.ig.CollisionAvoidanceType.prototype.fadeAndShift:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.6);
					$ret.fade(2);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				break;
		}
		var en = keys.getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var point = markers.item(key);
			var marker = this.provideMarkerStrategy()(point.ownerItem());
			if (smartPlacer != null && wrapper != null) {
				wrapper.element(marker);
				wrapper.elementDesiredSize(this.getMarkerDesiredSize()(marker));
				wrapper.originalLocation(point.point());
				smartPlacer.place(wrapper);
				if (wrapper.opacity() == 0) {
					wrapper.smartPosition(wrapper.smartPosition());
				}
				point.point(wrapper.elementLocationResult());
			} else {
				marker.__opacity = 1;
				marker.__visibility = $.ig.Visibility.prototype.visible;
			}
			this.updateMarkerPosition(marker, point, lightweight);
		}
		this.removeUnusedMarkers()(markers);
	}
	,
	updateMarkerPosition: function (marker, point, lightweight) {
		marker.canvasLeft(point.point().__x);
		marker.canvasTop(point.point().__y);
	}
	,
	_series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this._series = value;
			return value;
		} else {
			return this._series;
		}
	}
	,
	$type: new $.ig.Type('NumericMarkerManager', $.ig.MarkerManagerBase.prototype.$type)
}, true);

$.ig.util.defType('MarkerManagerBucket', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	__items: null,
	items: function () {
		if (this.__items == null) {
			this.__items = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		}
		return this.__items;
	}
	,
	__priorityItems: null,
	priorityItems: function () {
		if (this.__priorityItems == null) {
			this.__priorityItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		}
		return this.__priorityItems;
	}
	,
	getItem: function (wasPriority) {
		if (this.priorityItems().count() > 0) {
			var priorityIndex = this.priorityItems().__inner[this.priorityItems().count() - 1];
			this.priorityItems().removeAt(this.priorityItems().count() - 1);
			wasPriority = true;
			return {
				ret: priorityIndex,
				p0: wasPriority
			};
		}
		var index = this.items().__inner[this.items().count() - 1];
		this.items().removeAt(this.items().count() - 1);
		wasPriority = false;
		return {
			ret: index,
			p0: wasPriority
		};
	}
	,
	isEmpty: function () {
		return this.items().count() == 0 && this.priorityItems().count() == 0;
	}
	,
	$type: new $.ig.Type('MarkerManagerBucket', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PathRenderer', 'Object', {
	_flattener: null,
	flattener: function (value) {
		if (arguments.length === 1) {
			this._flattener = value;
			return value;
		} else {
			return this._flattener;
		}
	}
	,
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.unknownValuePlotting($.ig.UnknownValuePlotting.prototype.dontPlot);
	},
	init1: function (initNumber, flattener) {
		$.ig.Object.prototype.init.call(this);
		this.flattener(flattener);
	},
	_unknownValuePlotting: 0,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this._unknownValuePlotting = value;
			return value;
		} else {
			return this._unknownValuePlotting;
		}
	}
	,
	render: function (path, points, resolution) {
		var segments = $.ig.NaNSegmenter.prototype.segments(points, this.unknownValuePlotting());
		var pathFigures = this.getFigures(segments, resolution);
		var data = new $.ig.PathGeometry();
		var en = pathFigures.getEnumerator();
		while (en.moveNext()) {
			var figure = en.current();
			data.figures().add(figure);
		}
		path.data(data);
	}
	,
	getFigures: function (segments, resolution) {
		var d__ = new $.ig.PathRenderer___GetFigures__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__segments = segments;
		d__.__3__resolution = resolution;
		return d__;
	}
	,
	createPolylineSegment: function (segment, resolution) {
		var polySegment = new $.ig.PolyLineSegment();
		if (this.flattener() == null) {
			var en = $.ig.Enumerable.prototype.skip$1($.ig.Point.prototype.$type, segment, 1).getEnumerator();
			while (en.moveNext()) {
				var point = en.current();
				polySegment.__points.add(point);
			}
		} else {
			var en1 = $.ig.Enumerable.prototype.skip$1($.ig.Point.prototype.$type, this.flattener().flatten(segment, resolution), 1).getEnumerator();
			while (en1.moveNext()) {
				var point1 = en1.current();
				polySegment.__points.add(point1);
			}
		}
		return polySegment;
	}
	,
	$type: new $.ig.Type('PathRenderer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('NaNSegmenter', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	segments: function (points, mode) {
		var d__ = new $.ig.NaNSegmenter___Segments__IteratorClass(-2);
		d__.__3__points = points;
		d__.__3__mode = mode;
		return d__;
	}
	,
	shouldSkip: function (p) {
		return ($.ig.util.isNaN(p.__x) || $.ig.util.isNaN(p.__y) || Number.isInfinity(p.__x) || Number.isInfinity(p.__y));
	}
	,
	shouldTake: function (p) {
		return !$.ig.NaNSegmenter.prototype.shouldSkip(p);
	}
	,
	$type: new $.ig.Type('NaNSegmenter', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IFlattener', 'Object', {
	$type: new $.ig.Type('IFlattener', null)
}, true);

$.ig.util.defType('DefaultFlattener', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	flattenHelper: function (result, X, Y, b, e, E) {
		var indices = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var start = b;
		var end = e;
		var toFlatten = end - start + 1;
		while (toFlatten > 0) {
			if (toFlatten <= $.ig.DefaultFlattener.prototype._flattenerChunking) {
				$.ig.Flattener.prototype.flatten2(indices, X, Y, start, end, E);
				start = end + 1;
			} else {
				var currentEnd = start + $.ig.DefaultFlattener.prototype._flattenerChunking - 1;
				$.ig.Flattener.prototype.flatten2(indices, X, Y, start, currentEnd, E);
				start = currentEnd + 1;
			}
			toFlatten = end - start + 1;
		}
		return indices;
	}
	,
	fastFlattenHelper: function (X, Y, b, e, E) {
		var indices = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var start = b;
		var end = e;
		var toFlatten = end - start + 1;
		while (toFlatten > 0) {
			if (toFlatten <= $.ig.DefaultFlattener.prototype._flattenerChunking) {
				$.ig.Flattener.prototype.fastFlatten2(indices, X, Y, start, end, E);
				start = end + 1;
			} else {
				var currentEnd = start + $.ig.DefaultFlattener.prototype._flattenerChunking - 1;
				$.ig.Flattener.prototype.fastFlatten2(indices, X, Y, start, currentEnd, E);
				start = currentEnd + 1;
			}
			toFlatten = end - start + 1;
		}
		return indices;
	}
	,
	flatten: function (points, resolution) {
		var $self = this;
		var x = function (i) { return $self.getX(points, i); };
		var y = function (i) { return $self.getY(points, i); };
		return this.getFlattened(points, resolution, x, y);
	}
	,
	fastFlatten: function (x, y, count, resolution) {
		return this.getFastFlattened(x, y, count, resolution);
	}
	,
	getFlattened: function (pointsList, resolution, x, y) {
		var indices = this.flattenHelper(new $.ig.List$1($.ig.Number.prototype.$type, 0), x, y, 0, pointsList.count() - 1, resolution);
		var reordered = new $.ig.RearrangedList$1($.ig.Point.prototype.$type, pointsList, indices);
		return reordered;
	}
	,
	getFastFlattened: function (x, y, count, resolution) {
		var indices = this.fastFlattenHelper(x, y, 0, count - 1, resolution);
		var ret = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		for (var i = 0; i < indices.count(); i++) {
			ret.add({ __x: x[indices.__inner[i]], __y: y[indices.__inner[i]], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		return ret;
	}
	,
	getX: function (list, i) {
		return list.item(i).__x;
	}
	,
	getY: function (list, i) {
		return list.item(i).__y;
	}
	,
	$type: new $.ig.Type('DefaultFlattener', $.ig.Object.prototype.$type, [$.ig.IFlattener.prototype.$type])
}, true);

$.ig.util.defType('SeriesRenderingArguments', 'Object', {
	init: function (series, viewport, window, animate, skipPrepare) {
		$.ig.Object.prototype.init.call(this);
		this.transitionDuration(series.transitionDuration());
		this.container(series);
		this.viewport(viewport);
		this.window(window);
		this.animate(animate);
		this.skipPrepare(skipPrepare);
	},
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	_window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this._window = value;
			return value;
		} else {
			return this._window;
		}
	}
	,
	_transitionDuration: 0,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this._transitionDuration = value;
			return value;
		} else {
			return this._transitionDuration;
		}
	}
	,
	_animate: false,
	animate: function (value) {
		if (arguments.length === 1) {
			this._animate = value;
			return value;
		} else {
			return this._animate;
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	_skipPrepare: false,
	skipPrepare: function (value) {
		if (arguments.length === 1) {
			this._skipPrepare = value;
			return value;
		} else {
			return this._skipPrepare;
		}
	}
	,
	$type: new $.ig.Type('SeriesRenderingArguments', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SeriesRenderer$2', 'Object', {
	$tFrame: null,
	$tView: null,
	_prepareFrame: null,
	prepareFrame: function (value) {
		if (arguments.length === 1) {
			this._prepareFrame = value;
			return value;
		} else {
			return this._prepareFrame;
		}
	}
	,
	_renderFrame: null,
	renderFrame: function (value) {
		if (arguments.length === 1) {
			this._renderFrame = value;
			return value;
		} else {
			return this._renderFrame;
		}
	}
	,
	_calculateBuckets: null,
	calculateBuckets: function (value) {
		if (arguments.length === 1) {
			this._calculateBuckets = value;
			return value;
		} else {
			return this._calculateBuckets;
		}
	}
	,
	_startupAnimation: null,
	startupAnimation: function (value) {
		if (arguments.length === 1) {
			this._startupAnimation = value;
			return value;
		} else {
			return this._startupAnimation;
		}
	}
	,
	_animationActive: null,
	animationActive: function (value) {
		if (arguments.length === 1) {
			this._animationActive = value;
			return value;
		} else {
			return this._animationActive;
		}
	}
	,
	_checkFlush: null,
	checkFlush: function (value) {
		if (arguments.length === 1) {
			this._checkFlush = value;
			return value;
		} else {
			return this._checkFlush;
		}
	}
	,
	init: function ($tFrame, $tView, initNumber, prepare, render, animationActive, startupAnimation, checkFlush) {
		this.$tFrame = $tFrame;
		this.$tView = $tView;
		this.$type = this.$type.specialize(this.$tFrame, this.$tView);
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.prepareFrame(prepare);
		this.renderFrame(render);
		this.calculateBuckets(function (f) {
		});
		this.animationActive(animationActive);
		this.startupAnimation(startupAnimation);
		this.checkFlush(checkFlush);
	},
	init1: function ($tFrame, $tView, initNumber, prepare, render, animationActive, startupAnimation, checkFlush, calculateBuckets) {
		this.$tFrame = $tFrame;
		this.$tView = $tView;
		this.$type = this.$type.specialize(this.$tFrame, this.$tView);
		$.ig.Object.prototype.init.call(this);
		this.prepareFrame(prepare);
		this.renderFrame(render);
		this.calculateBuckets(calculateBuckets);
		this.animationActive(animationActive);
		this.startupAnimation(startupAnimation);
		this.checkFlush(checkFlush);
	},
	render: function (arguments, previousFrame, currentFrame, transitionFrame, view) {
		var totalMilliseconds = 0;
		totalMilliseconds = arguments.transitionDuration();
		if (arguments.animate() && totalMilliseconds > 0 && !arguments.skipPrepare()) {
			var prevFrame = previousFrame;
			if (this.animationActive()()) {
				this.checkFlush()();
				previousFrame = transitionFrame;
				transitionFrame = prevFrame;
			} else {
				previousFrame = currentFrame;
				currentFrame = prevFrame;
			}
			this.calculateBuckets()(currentFrame);
			this.prepareFrame()(currentFrame, view);
			this.startupAnimation()();
		} else {
			if (!arguments.skipPrepare()) {
				this.calculateBuckets()(currentFrame);
				this.prepareFrame()(currentFrame, view);
			}
			this.renderFrame()(currentFrame, view);
		}
		return {
			p1: previousFrame,
			p2: currentFrame,
			p3: transitionFrame
		};
	}
	,
	$type: new $.ig.Type('SeriesRenderer$2', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolarTrendLineManager', 'TrendLineManagerBase$1', {
	init: function () {
		$.ig.TrendLineManagerBase$1.prototype.init.call(this, $.ig.Point.prototype.$type);
	},
	prepareLine: function (flattenedPoints, trendLineType, angleColumn, radiusColumn, period, getScaledAngleValue, getScaledRadiusValue, trendResolutionParams, clipper, min, max) {
		var xmin = min;
		var xmax = max;
		var trend = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		if (!trendResolutionParams.window().isEmpty() && !trendResolutionParams.viewport().isEmpty()) {
			var angleList = null;
			var radiusList = null;
			if (angleColumn != null) {
				angleList = $.ig.Enumerable.prototype.toList$1(Number, angleColumn);
			}
			if (radiusColumn != null) {
				radiusList = $.ig.Enumerable.prototype.toList$1(Number, radiusColumn);
			}
			var count = 0;
			if (angleList != null) {
				count = angleList.count();
			}
			if (radiusList != null) {
				count = Math.min(count, radiusList.count());
			}
			if (trendLineType == $.ig.TrendLineType.prototype.none) {
				this.trendCoefficients(null);
				this.trendColumn().clear();
				return;
			}
			if (this.isFit(trendLineType)) {
				this.trendColumn().clear();
				this.trendCoefficients($.ig.TrendFitCalculator.prototype.calculateFit(trend, trendLineType, trendResolutionParams, this.trendCoefficients(), count, function (i) { return angleList.item(i); }, function (i) { return radiusList.item(i); }, getScaledAngleValue, getScaledRadiusValue, xmin, xmax));
			}
			if (this.isAverage(trendLineType)) {
				this.trendCoefficients(null);
				$.ig.TrendAverageCalculator.prototype.calculateXYAverage(trendLineType, this.trendColumn(), angleColumn, radiusColumn, period);
				var en = this.trendColumn().getEnumerator();
				while (en.moveNext()) {
					var point = en.current();
					var xi = getScaledAngleValue(point.__x);
					var yi = getScaledRadiusValue(point.__y);
					if (!$.ig.util.isNaN(xi) && !$.ig.util.isNaN(yi)) {
						trend.add({ __x: xi, __y: yi, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					}
				}
			}
			if (trend.count() > 0) {
				this.flattenTrendLine1(trend, trendResolutionParams, flattenedPoints, clipper);
			}
		}
	}
	,
	_useCartesianInterpolation: false,
	useCartesianInterpolation: function (value) {
		if (arguments.length === 1) {
			this._useCartesianInterpolation = value;
			return value;
		} else {
			return this._useCartesianInterpolation;
		}
	}
	,
	_unknownValuePlotting: 0,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this._unknownValuePlotting = value;
			return value;
		} else {
			return this._unknownValuePlotting;
		}
	}
	,
	_radiusExtentScale: 0,
	radiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._radiusExtentScale = value;
			return value;
		} else {
			return this._radiusExtentScale;
		}
	}
	,
	_innerRadiusExtentScale: 0,
	innerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._innerRadiusExtentScale = value;
			return value;
		} else {
			return this._innerRadiusExtentScale;
		}
	}
	,
	_projectX: null,
	projectX: function (value) {
		if (arguments.length === 1) {
			this._projectX = value;
			return value;
		} else {
			return this._projectX;
		}
	}
	,
	_projectY: null,
	projectY: function (value) {
		if (arguments.length === 1) {
			this._projectY = value;
			return value;
		} else {
			return this._projectY;
		}
	}
	,
	flattenTrendLine1: function (trend, trendResolutionParams, flattenedPoints, clipper) {
		var $self = this;
		if (clipper != null) {
			clipper.target(flattenedPoints);
		}
		var planner = (function () {
			var $ret = new $.ig.PolarLinePlanner();
			$ret.angleProvider(function (i) { return trend.item(i).__x; });
			$ret.radiusProvider(function (i2) { return trend.item(i2).__y; });
			$ret.clipper(clipper);
			$ret.count(trend.count());
			$ret.resolution(trendResolutionParams.resolution());
			$ret.transformedXProvider(function (i3) { return $self.projectX()(trend.item(i3).__x, trend.item(i3).__y); });
			$ret.transformedYProvider(function (i4) { return $self.projectY()(trend.item(i4).__x, trend.item(i4).__y); });
			$ret.useCartesianInterpolation($self.useCartesianInterpolation());
			$ret.unknownValuePlotting($self.unknownValuePlotting());
			$ret.viewport(trendResolutionParams.viewport());
			$ret.window(trendResolutionParams.window());
			return $ret;
		}());
		planner.prepareLine1(null, null);
	}
	,
	$type: new $.ig.Type('PolarTrendLineManager', $.ig.TrendLineManagerBase$1.prototype.$type.specialize($.ig.Point.prototype.$type))
}, true);

$.ig.util.defType('PolarBase', 'MarkerSeries', {
	createView: function () {
		return new $.ig.PolarBaseView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.MarkerSeries.prototype.onViewCreated.call(this, view);
		this.polarView(view);
	}
	,
	_polarView: null,
	polarView: function (value) {
		if (arguments.length === 1) {
			this._polarView = value;
			return value;
		} else {
			return this._polarView;
		}
	}
	,
	__operatingFrame: null,
	isPolar: function () {
		return true;
	}
	,
	init: function () {
		this._previousFrame = new $.ig.PolarFrame();
		this._transitionFrame = new $.ig.PolarFrame();
		this._currentFrame = new $.ig.PolarFrame();
		$.ig.MarkerSeries.prototype.init.call(this);
		this.thumbnailFrame(new $.ig.PolarFrame());
		this._transitionFrame.retransform(this.retransformPoint.runOn(this));
		this._previousFrame.retransform(this.retransformPoint.runOn(this));
		this._currentFrame.retransform(this.retransformPoint.runOn(this));
		this.seriesRenderer(new $.ig.SeriesRenderer$2($.ig.PolarFrame.prototype.$type, $.ig.PolarBaseView.prototype.$type, 0, this.prepareFrame.runOn(this), this.renderFrame.runOn(this), this.animationActive.runOn(this), this.startAnimation.runOn(this), this.checkFlush.runOn(this)));
	},
	checkFlush: function () {
		if (this.animator().needsFlush()) {
			this.animator().flush();
		}
	}
	,
	doGetScaledPoints: function () {
		this._polarAxes.getScaledPoints(this.__operatingFrame.transformed(), this.angleColumn(), this.radiusColumn(), this.__operatingWindowRect, this.__operatingViewportRect, function (j, a) { return Math.cos(a); }, function (j, a) { return Math.sin(a); }, true);
	}
	,
	_locations: null,
	locations: function (value) {
		if (arguments.length === 1) {
			this._locations = value;
			return value;
		} else {
			return this._locations;
		}
	}
	,
	_axisInfoCache: null,
	axisInfoCache: function (value) {
		if (arguments.length === 1) {
			this._axisInfoCache = value;
			return value;
		} else {
			return this._axisInfoCache;
		}
	}
	,
	__operatingWindowRect: null,
	__operatingViewportRect: null,
	_seriesRenderer: null,
	seriesRenderer: function (value) {
		if (arguments.length === 1) {
			this._seriesRenderer = value;
			return value;
		} else {
			return this._seriesRenderer;
		}
	}
	,
	angleMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.angleMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.angleMemberPathProperty);
		}
	}
	,
	radiusMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.radiusMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.radiusMemberPathProperty);
		}
	}
	,
	angleColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__angleColumn != value) {
				var oldXColumn = this.angleColumn();
				this.__angleColumn = value;
				this.raisePropertyChanged($.ig.PolarBase.prototype.angleColumnPropertyName, oldXColumn, this.angleColumn());
			}
			return value;
		} else {
			return this.__angleColumn;
		}
	}
	,
	__angleColumn: null,
	radiusColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__radiusColumn != value) {
				var oldXColumn = this.radiusColumn();
				this.__radiusColumn = value;
				this.raisePropertyChanged($.ig.PolarBase.prototype.radiusColumnPropertyName, oldXColumn, this.radiusColumn());
			}
			return value;
		} else {
			return this.__radiusColumn;
		}
	}
	,
	__radiusColumn: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.angleAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.angleAxisProperty);
		}
	}
	,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.radiusAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.radiusAxisProperty);
		}
	}
	,
	useCartesianInterpolation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.useCartesianInterpolationProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.useCartesianInterpolationProperty);
		}
	}
	,
	maximumMarkers: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.maximumMarkersProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PolarBase.prototype.maximumMarkersProperty));
		}
	}
	,
	trendLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineTypeProperty, $.ig.TrendLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PolarBase.prototype.trendLineTypeProperty));
		}
	}
	,
	trendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.trendLineBrushProperty);
		}
	}
	,
	actualTrendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.actualTrendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.actualTrendLineBrushProperty);
		}
	}
	,
	trendLineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.trendLineThicknessProperty);
		}
	}
	,
	trendLineDashCap: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineDashCapProperty, $.ig.PenLineCap.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PolarBase.prototype.trendLineDashCapProperty));
		}
	}
	,
	trendLineDashArray: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineDashArrayProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.trendLineDashArrayProperty);
		}
	}
	,
	trendLinePeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLinePeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PolarBase.prototype.trendLinePeriodProperty));
		}
	}
	,
	trendLineZIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.trendLineZIndexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PolarBase.prototype.trendLineZIndexProperty));
		}
	}
	,
	clipSeriesToBounds: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarBase.prototype.clipSeriesToBoundsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarBase.prototype.clipSeriesToBoundsProperty);
		}
	}
	,
	_previousFrame: null,
	_transitionFrame: null,
	_currentFrame: null,
	_polarAxes: null,
	invalidateAxes: function () {
		$.ig.MarkerSeries.prototype.invalidateAxes.call(this);
		if (this.radiusAxis() != null) {
			this.radiusAxis().renderAxis1(false);
		}
		if (this.angleAxis() != null) {
			this.angleAxis().renderAxis1(false);
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		var refresh = false;
		this.polarView().trendLineManager().dataUpdated(action, position, count, propertyName);
		if (this.angleAxis() != null && !this.angleAxis().updateRange()) {
			refresh = true;
		}
		if (this.radiusAxis() != null && !this.radiusAxis().updateRange()) {
			refresh = true;
		}
		if (refresh) {
			this.renderSeries(true);
		}
	}
	,
	viewportRectChangedOverride: function (oldViewportRect, newViewportRect) {
		this.renderSeries(false);
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.renderSeries(false);
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = true;
		if (!$.ig.MarkerSeries.prototype.validateSeries.call(this, viewportRect, windowRect, view) || !view.hasSurface() || windowRect.isEmpty() || viewportRect.isEmpty() || this.angleAxis() == null || this.radiusAxis() == null || this.angleColumn() == null || this.radiusColumn() == null || this.angleColumn().count() == 0 || this.radiusColumn().count() == 0 || this.fastItemsSource() == null || this.fastItemsSource().count() != this.angleColumn().count() || this.fastItemsSource().count() != this.radiusColumn().count() || this._polarAxes == null || this.angleAxis().seriesViewer() == null || this.radiusAxis().seriesViewer() == null || this.angleAxis().actualMinimumValue() == this.angleAxis().actualMaximumValue() || this.radiusAxis().actualMinimumValue() == this.radiusAxis().actualMaximumValue()) {
			isValid = false;
		}
		return isValid;
	}
	,
	renderSeriesOverride: function (animate) {
		var windowRect;
		var viewportRect;
		var $ret = this.getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		if (!this.validateSeries(viewportRect, windowRect, this.view())) {
			this.clearRendering(true, this.view());
			return;
		}
		this.__operatingWindowRect = windowRect;
		this.__operatingViewportRect = viewportRect;
		this.axisInfoCache(new $.ig.PolarAxisInfoCache(this.angleAxis(), this.radiusAxis(), this.fastItemsSource()));
		var args = new $.ig.SeriesRenderingArguments(this, viewportRect, windowRect, animate, this.skipPrepare());
		var $ret1 = this.seriesRenderer().render(args, this._previousFrame, this._currentFrame, this._transitionFrame, this.polarView());
		this._previousFrame = $ret1.p1;
		this._currentFrame = $ret1.p2;
		this._transitionFrame = $ret1.p3;
	}
	,
	retransformPoint: function (from) {
		var newPoint = this._polarAxes.getScaledPoint(from.__x, from.__y, this.__operatingWindowRect, this.__operatingViewportRect, this.axisInfoCache().angleAxisIsLogarithmic(), this.axisInfoCache().angleAxisIsInverted(), this.axisInfoCache().radiusAxisIsLogarithmic(), this.axisInfoCache().radiusAxisIsInverted(), this.axisInfoCache().radiusExtentScale(), this.axisInfoCache().innerRadiusExtentScale());
		return newPoint;
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.angleAxis() && this.angleColumn() != null) {
			return new $.ig.AxisRange(this.angleColumn().minimum(), this.angleColumn().maximum());
		}
		if (axis != null && axis == this.radiusAxis() && this.radiusColumn() != null) {
			return new $.ig.AxisRange(this.radiusColumn().minimum(), this.radiusColumn().maximum());
		}
		return null;
	}
	,
	scrollIntoView: function (item) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		if (this.angleAxis() == null || this.angleColumn() == null || this.radiusAxis() == null || this.radiusColumn() == null) {
			return false;
		}
		if (index < 0 || index > this.angleColumn().count() - 1 || index > this.radiusColumn().count() - 1) {
			return false;
		}
		var scaledAngle = this.angleAxis().getScaledAngle(this.angleColumn().item(index));
		var scaledRadius = this.radiusAxis().getScaledValue3(this.radiusColumn().item(index));
		var cx = 0.5 + (Math.cos(scaledAngle) * scaledRadius);
		var cy = 0.5 + (Math.sin(scaledAngle) * scaledRadius);
		if (!$.ig.util.isNaN(cx)) {
			if (cx < windowRect.left() + 0.1 * windowRect.width()) {
				cx = cx + 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (cx > windowRect.right() - 0.1 * windowRect.width()) {
				cx = cx - 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
		}
		if (!$.ig.util.isNaN(cy)) {
			if (cy < windowRect.top() + 0.1 * windowRect.height()) {
				cy = cy + 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
				cy = cy - 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
		}
		if (this.syncLink() != null) {
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	getItem: function (world) {
		return null;
	}
	,
	getItemIndex: function (world) {
		return -1;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.MarkerSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (this.polarView().trendLineManager().propertyUpdated(sender, propertyName, oldValue, newValue)) {
			this.renderSeries(false);
			this.notifyThumbnailAppearanceChanged();
		}
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					(oldValue).deregisterColumn(this.angleColumn());
					(oldValue).deregisterColumn(this.radiusColumn());
					this.angleColumn(null);
					this.radiusColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					this.angleColumn(this.registerDoubleColumn(this.angleMemberPath()));
					this.radiusColumn(this.registerDoubleColumn(this.radiusMemberPath()));
				}
				if ((this.radiusAxis() != null && !this.radiusAxis().updateRange()) || (this.angleAxis() != null && !this.angleAxis().updateRange())) {
					this.renderSeries(false);
				}
				break;
			case $.ig.PolarBase.prototype.angleAxisPropertyName:
				if (this.angleAxis() != null && this.radiusAxis() != null) {
					this._polarAxes = new $.ig.PolarAxes(this.radiusAxis(), this.angleAxis());
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if (this.angleAxis() != null && !this.angleAxis().updateRange()) {
					this.renderSeries(false);
				} else if (oldValue != null && newValue == null) {
					this.clearRendering(true, this.view());
				}
				break;
			case $.ig.PolarBase.prototype.radiusAxisPropertyName:
				if (this.angleAxis() != null && this.radiusAxis() != null) {
					this._polarAxes = new $.ig.PolarAxes(this.radiusAxis(), this.angleAxis());
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if (this.radiusAxis() != null && !this.radiusAxis().updateRange()) {
					this.renderSeries(false);
				} else if (oldValue != null && newValue == null) {
					this.clearRendering(true, this.view());
				}
				if (this.angleAxis() != null && !this.angleAxis().updateRange()) {
					this.angleAxis().refresh();
				}
				break;
			case $.ig.PolarBase.prototype.angleMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.angleColumn());
					this.angleColumn(this.registerDoubleColumn(this.angleMemberPath()));
				}
				break;
			case $.ig.PolarBase.prototype.angleColumnPropertyName:
				this.polarView().trendLineManager().reset();
				if (this.angleAxis() != null && !this.angleAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.PolarBase.prototype.radiusMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.radiusColumn());
					this.radiusColumn(this.registerDoubleColumn(this.radiusMemberPath()));
				}
				break;
			case $.ig.PolarBase.prototype.radiusColumnPropertyName:
				this.polarView().trendLineManager().reset();
				if (this.radiusAxis() != null && !this.radiusAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.PolarBase.prototype.useCartesianInterpolationPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.PolarBase.prototype.maximumMarkersPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.transitionProgressPropertyName:
				this.axisInfoCache(new $.ig.PolarAxisInfoCache(this.angleAxis(), this.radiusAxis(), this.fastItemsSource()));
				this.__operatingWindowRect = this.view().windowRect();
				this.__operatingViewportRect = this.view().viewport();
				this._transitionFrame.useCartesianInterpolation(this.useCartesianInterpolation());
				this._transitionFrame.interpolate3(this.transitionProgress(), this._previousFrame, this._currentFrame);
				if (this.clearAndAbortIfInvalid1(this.view())) {
					return;
				}
				if (this.transitionProgress() == 1) {
					this.renderFrame(this._currentFrame, this.polarView());
				} else {
					this.renderFrame(this._transitionFrame, this.polarView());
				}
				break;
			case $.ig.Series.prototype.trendLineBrushPropertyName:
				this.updateIndexedProperties();
				break;
			case $.ig.PolarBase.prototype.clipSeriesToBoundsPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.trendLineTypePropertyName:
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	canUseAsRadiusAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericRadiusAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsAngleAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericAngleAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.MarkerSeries.prototype.clearRendering.call(this, wipeClean, view);
		var polarView = view;
		if (wipeClean) {
			polarView.markers().clear();
		}
		polarView.trendLineManager().clearPoints();
	}
	,
	getTrendlineClipper: function (viewportRect, target) {
		var top = viewportRect.top() - 10;
		var bottom = viewportRect.bottom() + 10;
		var left = viewportRect.left() - 10;
		var right = viewportRect.right() + 10;
		var clipper = (function () {
			var $ret = new $.ig.Clipper(1, left, bottom, right, top, false);
			$ret.target(target);
			return $ret;
		}());
		return clipper;
	}
	,
	calculateCachedPoints: function (frame, count, windowRect, viewportRect) {
		frame.cachedPoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 1, count));
		var itemsSource = this.fastItemsSource();
		for (var i = 0; i < count; i++) {
			var point = frame.transformed().__inner[i];
			if (!Number.isInfinity(point.__x) && !Number.isInfinity(point.__y)) {
				var columnValues = { __x: this.angleColumn().item(i), __y: this.radiusColumn().item(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				var p = { __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				frame.cachedPoints().add(itemsSource.item(i), (function () {
					var $ret = new $.ig.OwnedPoint();
					$ret.ownerItem(itemsSource.item(i));
					$ret.columnValues(columnValues);
					$ret.point(p);
					return $ret;
				}()));
			}
		}
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		frame.markers().clear();
		frame.trendLine().clear();
		var count = Math.min(this.angleColumn() != null ? this.angleColumn().count() : 0, this.radiusColumn() != null ? this.radiusColumn().count() : 0);
		if (count < 1) {
			return;
		}
		this.axisInfoCache(new $.ig.PolarAxisInfoCache(this.angleAxis(), this.radiusAxis(), this.fastItemsSource()));
		this.__operatingFrame = frame;
		this.__operatingViewportRect = viewportRect;
		this.__operatingWindowRect = windowRect;
		view.markerManager().winnowMarkers(frame.markers(), this.maximumMarkers(), windowRect, viewportRect, this.resolution());
		if (count <= this.maximumMarkers()) {
			this.calculateCachedPoints(frame, count, windowRect, viewportRect);
		}
		var clipper = this.getTrendlineClipper(viewportRect, frame.trendLine());
		var angleMin = Math.min(this.angleAxis().actualMinimumValue(), this.angleAxis().actualMaximumValue());
		var angleMax = Math.max(this.angleAxis().actualMaximumValue(), this.angleAxis().actualMinimumValue());
		view.trendLineManager().useCartesianInterpolation(this.useCartesianInterpolation());
		view.trendLineManager().unknownValuePlotting($.ig.UnknownValuePlotting.prototype.linearInterpolate);
		view.trendLineManager().radiusExtentScale(this.radiusAxis().actualRadiusExtentScale());
		view.trendLineManager().innerRadiusExtentScale(this.radiusAxis().actualInnerRadiusExtentScale());
		view.trendLineManager().projectX(function (angle, radius) {
			return $self._polarAxes.getXValue(angle, radius, windowRect, viewportRect, Math.cos);
		});
		view.trendLineManager().projectY(function (angle, radius) {
			return $self._polarAxes.getYValue(angle, radius, windowRect, viewportRect, Math.sin);
		});
		view.trendLineManager().prepareLine(frame.trendLine(), this.trendLineType(), ($.ig.Enumerable.prototype.where$1(Number, (this.angleColumn()), function (angle) { return angle <= angleMax && angle >= angleMin; })), this.radiusColumn(), this.trendLinePeriod(), this.angleAxis().getScaledAngle.runOn(this.angleAxis()), this.radiusAxis().getScaledValue3.runOn(this.radiusAxis()), (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.resolution($self.resolution());
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			return $ret;
		}()), clipper, angleMin, angleMax);
	}
	,
	renderFrame: function (frame, view) {
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var contentViewport = this.getContentViewport(view);
		this.axisInfoCache(new $.ig.PolarAxisInfoCache(this.angleAxis(), this.radiusAxis(), this.fastItemsSource()));
		view.markerManager().render(frame.markers(), this.useLightweightMarkers());
		view.renderMarkers();
		var clipper = this.getTrendlineClipper(viewportRect, view.trendLineManager().trendPolyline().points());
		view.trendLineManager().rasterizeTrendLine1(frame.trendLine(), clipper);
		this.applyClipping(viewportRect, windowRect, effectiveViewportRect, contentViewport, view);
	}
	,
	applyClipping: function (viewportRect, windowRect, effectiveViewportRect, contentViewport, view) {
		view.applyClipping(viewportRect, windowRect, effectiveViewportRect, contentViewport);
	}
	,
	updateIndexedProperties: function () {
		$.ig.MarkerSeries.prototype.updateIndexedProperties.call(this);
		if (this.index() < 0) {
			return;
		}
		this.polarView().updateTrendlineBrush();
	}
	,
	_thumbnailFrame: null,
	thumbnailFrame: function (value) {
		if (arguments.length === 1) {
			this._thumbnailFrame = value;
			return value;
		} else {
			return this._thumbnailFrame;
		}
	}
	,
	renderThumbnail: function (viewportRect, surface) {
		$.ig.MarkerSeries.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!this.thumbnailDirty()) {
			this.view().prepSurface(surface);
			return;
		}
		this.view().prepSurface(surface);
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		var thumbnailView = this.thumbnailView();
		if (!this.skipThumbnailPrepare()) {
			this.thumbnailFrame(new $.ig.PolarFrame());
			this.prepareFrame(this.thumbnailFrame(), thumbnailView);
		}
		this.skipThumbnailPrepare(false);
		this.renderFrame(this.thumbnailFrame(), thumbnailView);
		this.thumbnailDirty(false);
	}
	,
	_alternateFrame: null,
	alternateFrame: function (value) {
		if (arguments.length === 1) {
			this._alternateFrame = value;
			return value;
		} else {
			return this._alternateFrame;
		}
	}
	,
	renderAlternateView: function (viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio) {
		$.ig.MarkerSeries.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var polarBaseView = view;
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(polarBaseView)) {
			return;
		}
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.PolarFrame());
		}
		this.prepareFrame(this.alternateFrame(), polarBaseView);
		this.renderFrame(this.alternateFrame(), polarBaseView);
	}
	,
	removeUnusedMarkers: function (list, markers) {
		var remove = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var en = markers.activeKeys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (!list.containsKey(key)) {
				remove.add1(key);
			}
		}
		var en1 = remove.getEnumerator();
		while (en1.moveNext()) {
			var key1 = en1.current();
			markers.remove(key1);
		}
	}
	,
	getMarkerLocations: function (Markers, WindowRect, Viewport) {
		this.doGetScaledPoints();
		return this.__operatingFrame.transformed().toArray();
	}
	,
	getActiveIndexes: function (Markers) {
		var indexes = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var source = this.fastItemsSource();
		var en = this.polarView().markers().activeKeys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			indexes.add(source.indexOf(key));
		}
		return indexes;
	}
	,
	getColumnValues: function (i) {
		return { __x: this.angleColumn().item(i), __y: this.radiusColumn().item(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	exportVisualDataOverride: function (svd) {
		$.ig.MarkerSeries.prototype.exportVisualDataOverride.call(this, svd);
		var trendShape = new $.ig.PolyLineVisualData(1, "trendLine", this.polarView().trendLineManager().trendPolyline());
		trendShape.tags().add("Trend");
		svd.shapes().add(trendShape);
	}
	,
	$type: new $.ig.Type('PolarBase', $.ig.MarkerSeries.prototype.$type)
}, true);

$.ig.util.defType('PolarLineSeriesBase', 'PolarBase', {
	createView: function () {
		return new $.ig.PolarLineSeriesBaseView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarBase.prototype.onViewCreated.call(this, view);
		this.polarLineBaseView(view);
	}
	,
	_polarLineBaseView: null,
	polarLineBaseView: function (value) {
		if (arguments.length === 1) {
			this._polarLineBaseView = value;
			return value;
		} else {
			return this._polarLineBaseView;
		}
	}
	,
	init: function () {
		$.ig.PolarBase.prototype.init.call(this);
		this.cartesianRenderer(new $.ig.PathRenderer(1, new $.ig.DefaultFlattener()));
		this.polarRenderer(new $.ig.PathRenderer(0));
	},
	_cartesianRenderer: null,
	cartesianRenderer: function (value) {
		if (arguments.length === 1) {
			this._cartesianRenderer = value;
			return value;
		} else {
			return this._cartesianRenderer;
		}
	}
	,
	_polarRenderer: null,
	polarRenderer: function (value) {
		if (arguments.length === 1) {
			this._polarRenderer = value;
			return value;
		} else {
			return this._polarRenderer;
		}
	}
	,
	getUnknownValuePlotting: function () {
		return $.ig.UnknownValuePlotting.prototype.dontPlot;
	}
	,
	clippingDisabled: function () {
		return false;
	}
	,
	isClosed: function () {
		return false;
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		$.ig.PolarBase.prototype.prepareFrame.call(this, frame, view);
		frame.points().clear();
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var angleCount = this.angleColumn() != null ? this.angleColumn().count() : 0;
		var radiusCount = this.radiusColumn() != null ? this.radiusColumn().count() : 0;
		var count = Math.min(angleCount, radiusCount);
		var planner = (function () {
			var $ret = new $.ig.PolarLinePlanner();
			$ret.angleProvider(function (i) {
				return $self.axisInfoCache().angleAxis().getScaledAngle1($self.angleColumn().item(i), $self.axisInfoCache().angleAxisIsLogarithmic(), $self.axisInfoCache().angleAxisIsInverted());
			});
			$ret.count(count);
			$ret.radiusProvider(function (i2) {
				return $self.radiusAxis().getScaledValue4($self.radiusColumn().item(i2), $self.axisInfoCache().radiusAxisIsLogarithmic(), $self.axisInfoCache().radiusAxisIsInverted(), $self.axisInfoCache().radiusExtentScale(), $self.axisInfoCache().innerRadiusExtentScale());
			});
			$ret.resolution($self.resolution());
			$ret.useCartesianInterpolation($self.useCartesianInterpolation());
			$ret.unknownValuePlotting($self.getUnknownValuePlotting());
			$ret.transformedXProvider(function (i3) { return $self.getTransformedX(frame, i3); });
			$ret.transformedYProvider(function (i4) { return $self.getTransformedY(frame, i4); });
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			$ret.isClosed($self.isClosed());
			$ret.clippingDisabled($self.clippingDisabled());
			return $ret;
		}());
		var viableIndices = ($.ig.Enumerable.prototype.where$1($.ig.Number.prototype.$type, ($.ig.Enumerable.prototype.range(0, count)), function (i) { return $self.indexViable(i); }));
		if (this.isClosed()) {
			var first = new $.ig.List$1($.ig.Number.prototype.$type, 1, $.ig.Enumerable.prototype.take$1($.ig.Number.prototype.$type, $.ig.Enumerable.prototype.where$1($.ig.Number.prototype.$type, $.ig.Enumerable.prototype.range(0, count), this.indexViable.runOn(this)), 1));
			viableIndices = $.ig.Enumerable.prototype.concat$1($.ig.Number.prototype.$type, viableIndices, first);
		}
		planner.prepareLine1(frame.points(), viableIndices);
	}
	,
	getTransformedX: function (frame, i) {
		return frame.transformed().__inner[i].__x;
	}
	,
	getTransformedY: function (frame, i) {
		return frame.transformed().__inner[i].__y;
	}
	,
	centerVisible: function () {
		return !this.centerNotVisible();
	}
	,
	centerNotVisible: function () {
		var window = this.seriesViewer().actualWindowRect();
		return !window.containsPoint({ __x: 0.5, __y: 0.5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	indexViable: function (i) {
		return (this.angleColumn().item(i) >= this.axisInfoCache().angleAxis().actualMinimumValue() && this.angleColumn().item(i) <= this.axisInfoCache().angleAxis().actualMaximumValue() && this.radiusColumn().item(i) >= this.axisInfoCache().radiusAxis().actualMinimumValue() && this.radiusColumn().item(i) <= this.axisInfoCache().radiusAxis().actualMaximumValue()) || ($.ig.util.isNaN(this.angleColumn().item(i)) || $.ig.util.isNaN(this.radiusColumn().item(i)));
	}
	,
	clearPoints: function (view) {
	}
	,
	renderPoints: function (frame, view) {
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.PolarBase.prototype.clearRendering.call(this, wipeClean, view);
		this.clearPoints(view);
	}
	,
	_currentRenderer: null,
	currentRenderer: function (value) {
		if (arguments.length === 1) {
			this._currentRenderer = value;
			return value;
		} else {
			return this._currentRenderer;
		}
	}
	,
	renderFrame: function (frame, view) {
		$.ig.PolarBase.prototype.renderFrame.call(this, frame, view);
		if (this.useCartesianInterpolation()) {
			this.currentRenderer(this.cartesianRenderer());
		} else {
			this.currentRenderer(this.polarRenderer());
		}
		this.currentRenderer().unknownValuePlotting(this.getUnknownValuePlotting());
		this.clearPoints(view);
		this.renderPoints(frame, view);
	}
	,
	$type: new $.ig.Type('PolarLineSeriesBase', $.ig.PolarBase.prototype.$type)
}, true);

$.ig.util.defType('PolarAreaSeries', 'PolarLineSeriesBase', {
	createView: function () {
		return new $.ig.PolarAreaSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarLineSeriesBase.prototype.onViewCreated.call(this, view);
		this.polarAreaView(view);
	}
	,
	_polarAreaView: null,
	polarAreaView: function (value) {
		if (arguments.length === 1) {
			this._polarAreaView = value;
			return value;
		} else {
			return this._polarAreaView;
		}
	}
	,
	init: function () {
		$.ig.PolarLineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.PolarAreaSeries.prototype.$type);
	},
	clearPoints: function (view) {
		var areaView = view;
		areaView.clearPolarArea();
	}
	,
	renderPoints: function (frame, view) {
		if ($.ig.Enumerable.prototype.any$1($.ig.Point.prototype.$type, frame.points()) && $.ig.Point.prototype.l_op_Inequality($.ig.Enumerable.prototype.first$1($.ig.Point.prototype.$type, frame.points()), $.ig.Enumerable.prototype.last$1($.ig.Point.prototype.$type, frame.points()))) {
			frame.points().add($.ig.Enumerable.prototype.first$1($.ig.Point.prototype.$type, frame.points()));
		}
		var areaView = view;
		areaView.polygon().__opacity = this.actualAreaFillOpacity();
		areaView.renderPolarArea(frame.points(), this.resolution());
	}
	,
	isClosed: function () {
		return true;
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarAreaSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PolarAreaSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	getUnknownValuePlotting: function () {
		return this.unknownValuePlotting();
	}
	,
	$type: new $.ig.Type('PolarAreaSeries', $.ig.PolarLineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('PolarAxisInfoCache', 'Object', {
	_angleAxis: null,
	angleAxis: function (value) {
		if (arguments.length === 1) {
			this._angleAxis = value;
			return value;
		} else {
			return this._angleAxis;
		}
	}
	,
	_radiusAxis: null,
	radiusAxis: function (value) {
		if (arguments.length === 1) {
			this._radiusAxis = value;
			return value;
		} else {
			return this._radiusAxis;
		}
	}
	,
	_angleAxisIsLogarithmic: false,
	angleAxisIsLogarithmic: function (value) {
		if (arguments.length === 1) {
			this._angleAxisIsLogarithmic = value;
			return value;
		} else {
			return this._angleAxisIsLogarithmic;
		}
	}
	,
	_radiusAxisIsLogarithmic: false,
	radiusAxisIsLogarithmic: function (value) {
		if (arguments.length === 1) {
			this._radiusAxisIsLogarithmic = value;
			return value;
		} else {
			return this._radiusAxisIsLogarithmic;
		}
	}
	,
	_angleAxisIsInverted: false,
	angleAxisIsInverted: function (value) {
		if (arguments.length === 1) {
			this._angleAxisIsInverted = value;
			return value;
		} else {
			return this._angleAxisIsInverted;
		}
	}
	,
	_radiusAxisIsInverted: false,
	radiusAxisIsInverted: function (value) {
		if (arguments.length === 1) {
			this._radiusAxisIsInverted = value;
			return value;
		} else {
			return this._radiusAxisIsInverted;
		}
	}
	,
	_fastItemsSource: null,
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	_radiusExtentScale: 0,
	radiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._radiusExtentScale = value;
			return value;
		} else {
			return this._radiusExtentScale;
		}
	}
	,
	_innerRadiusExtentScale: 0,
	innerRadiusExtentScale: function (value) {
		if (arguments.length === 1) {
			this._innerRadiusExtentScale = value;
			return value;
		} else {
			return this._innerRadiusExtentScale;
		}
	}
	,
	init: function (numAxis, radAxis, itemsSource) {
		$.ig.Object.prototype.init.call(this);
		this.angleAxis(numAxis);
		this.radiusAxis(radAxis);
		this.angleAxisIsLogarithmic(this.angleAxis().isReallyLogarithmic());
		this.angleAxisIsInverted(this.angleAxis().isInverted());
		this.radiusAxisIsLogarithmic(this.radiusAxis().isReallyLogarithmic());
		this.radiusAxisIsInverted(this.radiusAxis().isInverted());
		this.radiusExtentScale(this.radiusAxis().actualRadiusExtentScale());
		this.innerRadiusExtentScale(this.radiusAxis().actualInnerRadiusExtentScale());
		this.fastItemsSource(itemsSource);
	},
	$type: new $.ig.Type('PolarAxisInfoCache', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScatterFrameBase$1', 'Frame', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Frame.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.cachedPoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.markers(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.trendLine(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.horizontalErrorBars(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.verticalErrorBars(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.horizontalErrorBarWidths(new $.ig.Dictionary$2($.ig.Object.prototype.$type, Array, 0));
		this.verticalErrorBarHeights(new $.ig.Dictionary$2($.ig.Object.prototype.$type, Array, 0));
		this.getNewMinValue(function (maxPoint, minFrame, maxFrame) { return maxPoint; });
		this.ownedPointDictInterpolator(new $.ig.DictInterpolator$3($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, this.$t, this.interpolatePoint.runOn(this), function (p) { return p.ownerItem(); }, function (p) { return !$.ig.util.isNaN(p.point().__x) && !$.ig.util.isNaN(p.point().__y); }, function () { return new $.ig.OwnedPoint(); }));
	},
	_ownedPointDictInterpolator: null,
	ownedPointDictInterpolator: function (value) {
		if (arguments.length === 1) {
			this._ownedPointDictInterpolator = value;
			return value;
		} else {
			return this._ownedPointDictInterpolator;
		}
	}
	,
	interpolate3: function (p, minFrame, maxFrame) {
		var min = minFrame;
		var max = maxFrame;
		if ($.ig.util.getBoxIfEnum(this.$t, min) == null || $.ig.util.getBoxIfEnum(this.$t, max) == null) {
			return;
		}
		this.ownedPointDictInterpolator().interpolate(this.cachedPoints(), p, min.cachedPoints(), max.cachedPoints(), min, max);
		$.ig.Frame.prototype.interpolate(this.points(), p, min.points(), max.points());
		this.ownedPointDictInterpolator().interpolate(this.markers(), p, min.markers(), max.markers(), min, max);
		this.ownedPointDictInterpolator().interpolate(this.horizontalErrorBars(), p, min.horizontalErrorBars(), max.horizontalErrorBars(), min, max);
		this.ownedPointDictInterpolator().interpolate(this.verticalErrorBars(), p, min.verticalErrorBars(), max.verticalErrorBars(), min, max);
		this.addPointsThatSweepThroughTheView(this.markers(), p, min, max);
		$.ig.Frame.prototype.interpolate(this.trendLine(), p, min.trendLine(), max.trendLine());
		this.horizontalErrorBarWidths(max.horizontalErrorBarWidths());
		this.verticalErrorBarHeights(max.verticalErrorBarHeights());
		this.interpolateOverride(p, min, max);
	}
	,
	interpolateOverride: function (p, minFrame, maxFrame) {
	}
	,
	addPointsThatSweepThroughTheView: function (markers, p, minFrame, maxFrame) {
		var en = $.ig.Enumerable.prototype.where$1($.ig.OwnedPoint.prototype.$type, minFrame.cachedPoints().values(), function (changedPoint) { return !markers.containsKey(changedPoint.ownerItem()); }).getEnumerator();
		while (en.moveNext()) {
			var changedPoint = en.current();
			var maxPoint;
			if (!(function () { var $ret = maxFrame.cachedPoints().tryGetValue(changedPoint.ownerItem(), maxPoint); maxPoint = $ret.p1; return $ret.ret; }()) || (maxPoint.columnValues().__x == changedPoint.columnValues().__x && maxPoint.columnValues().__y == changedPoint.columnValues().__y)) {
				continue;
			}
			var newPoint = new $.ig.OwnedPoint();
			this.interpolatePoint(newPoint, p, changedPoint, maxPoint, minFrame, maxFrame);
			if ($.ig.util.isNaN(newPoint.point().__x) || $.ig.util.isNaN(newPoint.point().__y)) {
				continue;
			}
			markers.add(newPoint.ownerItem(), newPoint);
		}
	}
	,
	_getNewMinValue: null,
	getNewMinValue: function (value) {
		if (arguments.length === 1) {
			this._getNewMinValue = value;
			return value;
		} else {
			return this._getNewMinValue;
		}
	}
	,
	interpolateColumnValues: function (point, p, minPoint, maxPoint) {
		if (minPoint != null) {
			point.columnValues({ __x: minPoint.columnValues().__x, __y: minPoint.columnValues().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		} else if (maxPoint != null) {
			point.columnValues({ __x: maxPoint.columnValues().__x, __y: maxPoint.columnValues().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	interpolatePoint: function (point, p, minPoint, maxPoint, minFrame, maxFrame) {
		var min;
		var max;
		if (minPoint == null) {
			if (maxPoint != null) {
				var minValue;
				if ((function () { var $ret = minFrame.cachedPoints().tryGetValue(maxPoint.ownerItem(), minValue); minValue = $ret.p1; return $ret.ret; }())) {
					min = minValue;
				} else {
					min = this.getNewMinValue()(maxPoint, minFrame, maxFrame);
				}
			} else {
				point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return;
			}
		} else {
			min = minPoint;
			if (point.ownerItem() == null) {
				point.ownerItem(minPoint.ownerItem());
			}
		}
		if (maxPoint == null) {
			if (minPoint != null) {
				var maxValue;
				if ((function () { var $ret = maxFrame.cachedPoints().tryGetValue(minPoint.ownerItem(), maxValue); maxValue = $ret.p1; return $ret.ret; }())) {
					max = maxValue;
				} else {
					point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return;
				}
			} else {
				point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return;
			}
		} else {
			max = maxPoint;
			if (point.ownerItem() == null) {
				point.ownerItem(maxPoint.ownerItem());
			}
		}
		this.interpolateColumnValues(point, p, min, max);
		if ($.ig.util.isNaN(min.point().__x) || $.ig.util.isNaN(min.point().__y)) {
			min = max;
		}
		this.interpolatePointOverride(point, p, min, max);
	}
	,
	interpolatePointOverride: function (point, p, min, max) {
		var q = 1 - p;
		point.point({ __x: min.point().__x * q + max.point().__x * p, __y: min.point().__y * q + max.point().__y * p, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	_markers: null,
	markers: function (value) {
		if (arguments.length === 1) {
			this._markers = value;
			return value;
		} else {
			return this._markers;
		}
	}
	,
	_cachedPoints: null,
	cachedPoints: function (value) {
		if (arguments.length === 1) {
			this._cachedPoints = value;
			return value;
		} else {
			return this._cachedPoints;
		}
	}
	,
	_points: null,
	points: function (value) {
		if (arguments.length === 1) {
			this._points = value;
			return value;
		} else {
			return this._points;
		}
	}
	,
	_trendLine: null,
	trendLine: function (value) {
		if (arguments.length === 1) {
			this._trendLine = value;
			return value;
		} else {
			return this._trendLine;
		}
	}
	,
	_horizontalErrorBars: null,
	horizontalErrorBars: function (value) {
		if (arguments.length === 1) {
			this._horizontalErrorBars = value;
			return value;
		} else {
			return this._horizontalErrorBars;
		}
	}
	,
	_verticalErrorBars: null,
	verticalErrorBars: function (value) {
		if (arguments.length === 1) {
			this._verticalErrorBars = value;
			return value;
		} else {
			return this._verticalErrorBars;
		}
	}
	,
	_horizontalErrorBarWidths: null,
	horizontalErrorBarWidths: function (value) {
		if (arguments.length === 1) {
			this._horizontalErrorBarWidths = value;
			return value;
		} else {
			return this._horizontalErrorBarWidths;
		}
	}
	,
	_verticalErrorBarHeights: null,
	verticalErrorBarHeights: function (value) {
		if (arguments.length === 1) {
			this._verticalErrorBarHeights = value;
			return value;
		} else {
			return this._verticalErrorBarHeights;
		}
	}
	,
	$type: new $.ig.Type('ScatterFrameBase$1', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('PolarFrame', 'ScatterFrameBase$1', {
	init: function () {
		$.ig.ScatterFrameBase$1.prototype.init.call(this, $.ig.PolarFrame.prototype.$type);
		this.transformed(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.useCartesianInterpolation(true);
	},
	_transformed: null,
	transformed: function (value) {
		if (arguments.length === 1) {
			this._transformed = value;
			return value;
		} else {
			return this._transformed;
		}
	}
	,
	_useCartesianInterpolation: false,
	useCartesianInterpolation: function (value) {
		if (arguments.length === 1) {
			this._useCartesianInterpolation = value;
			return value;
		} else {
			return this._useCartesianInterpolation;
		}
	}
	,
	_retransform: null,
	retransform: function (value) {
		if (arguments.length === 1) {
			this._retransform = value;
			return value;
		} else {
			return this._retransform;
		}
	}
	,
	interpolateOverride: function (p, minFrame, maxFrame) {
		var min = $.ig.util.cast($.ig.PolarFrame.prototype.$type, minFrame);
		var max = $.ig.util.cast($.ig.PolarFrame.prototype.$type, maxFrame);
		if (min == null || max == null) {
			return;
		}
		$.ig.Frame.prototype.interpolate(this.transformed(), p, min.transformed(), max.transformed());
	}
	,
	interpolateColumnValues: function (point, p, minPoint, maxPoint) {
		if (this.useCartesianInterpolation()) {
			$.ig.ScatterFrameBase$1.prototype.interpolateColumnValues.call(this, point, p, minPoint, maxPoint);
		} else {
			$.ig.ScatterFrameBase$1.prototype.interpolateColumnValues.call(this, point, p, minPoint, maxPoint);
			if (minPoint != null && maxPoint != null && (minPoint.columnValues().__x != maxPoint.columnValues().__x || minPoint.columnValues().__y != maxPoint.columnValues().__y)) {
				var q = 1 - p;
				point.columnValues({ __x: minPoint.columnValues().__x * q + maxPoint.columnValues().__x * p, __y: minPoint.columnValues().__y * q + maxPoint.columnValues().__y * p, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	interpolatePointOverride: function (point, p, min, max) {
		if (this.useCartesianInterpolation()) {
			$.ig.ScatterFrameBase$1.prototype.interpolatePointOverride.call(this, point, p, min, max);
		} else {
			if (min != null && max != null && (min.point().__x != max.point().__x || min.point().__y != max.point().__y) && this.isPlottable(point.columnValues())) {
				point.point(this.retransform()(point.columnValues()));
			} else {
				$.ig.ScatterFrameBase$1.prototype.interpolatePointOverride.call(this, point, p, min, max);
			}
		}
	}
	,
	$type: new $.ig.Type('PolarFrame', $.ig.ScatterFrameBase$1.prototype.$type.specialize(-1))
}, true);

$.ig.PolarFrame.prototype.$type.initSelfReferences();

$.ig.util.defType('AngleRadiusPair', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_angle: 0,
	angle: function (value) {
		if (arguments.length === 1) {
			this._angle = value;
			return value;
		} else {
			return this._angle;
		}
	}
	,
	_radius: 0,
	radius: function (value) {
		if (arguments.length === 1) {
			this._radius = value;
			return value;
		} else {
			return this._radius;
		}
	}
	,
	$type: new $.ig.Type('AngleRadiusPair', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolarLinePlanner', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_useCartesianInterpolation: false,
	useCartesianInterpolation: function (value) {
		if (arguments.length === 1) {
			this._useCartesianInterpolation = value;
			return value;
		} else {
			return this._useCartesianInterpolation;
		}
	}
	,
	_unknownValuePlotting: 0,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this._unknownValuePlotting = value;
			return value;
		} else {
			return this._unknownValuePlotting;
		}
	}
	,
	_angleProvider: null,
	angleProvider: function (value) {
		if (arguments.length === 1) {
			this._angleProvider = value;
			return value;
		} else {
			return this._angleProvider;
		}
	}
	,
	_radiusProvider: null,
	radiusProvider: function (value) {
		if (arguments.length === 1) {
			this._radiusProvider = value;
			return value;
		} else {
			return this._radiusProvider;
		}
	}
	,
	_transformedXProvider: null,
	transformedXProvider: function (value) {
		if (arguments.length === 1) {
			this._transformedXProvider = value;
			return value;
		} else {
			return this._transformedXProvider;
		}
	}
	,
	_transformedYProvider: null,
	transformedYProvider: function (value) {
		if (arguments.length === 1) {
			this._transformedYProvider = value;
			return value;
		} else {
			return this._transformedYProvider;
		}
	}
	,
	_resolution: 0,
	resolution: function (value) {
		if (arguments.length === 1) {
			this._resolution = value;
			return value;
		} else {
			return this._resolution;
		}
	}
	,
	_count: 0,
	count: function (value) {
		if (arguments.length === 1) {
			this._count = value;
			return value;
		} else {
			return this._count;
		}
	}
	,
	__viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this.__viewport = value;
			return value;
		} else {
			return this.__viewport;
		}
	}
	,
	__window: null,
	window: function (value) {
		if (arguments.length === 1) {
			this.__window = value;
			return value;
		} else {
			return this.__window;
		}
	}
	,
	_clipper: null,
	clipper: function (value) {
		if (arguments.length === 1) {
			this._clipper = value;
			return value;
		} else {
			return this._clipper;
		}
	}
	,
	_clippingDisabled: false,
	clippingDisabled: function (value) {
		if (arguments.length === 1) {
			this._clippingDisabled = value;
			return value;
		} else {
			return this._clippingDisabled;
		}
	}
	,
	_isClosed: false,
	isClosed: function (value) {
		if (arguments.length === 1) {
			this._isClosed = value;
			return value;
		} else {
			return this._isClosed;
		}
	}
	,
	_target: null,
	target: function (value) {
		if (arguments.length === 1) {
			this._target = value;
			return value;
		} else {
			return this._target;
		}
	}
	,
	valid: function () {
		if (this.angleProvider() == null || this.radiusProvider() == null || this.transformedXProvider() == null || this.transformedYProvider() == null || $.ig.Rect.prototype.l_op_Equality(this.viewport(), $.ig.Rect.prototype.empty()) || $.ig.Rect.prototype.l_op_Equality(this.window(), $.ig.Rect.prototype.empty())) {
			return false;
		}
		return true;
	}
	,
	measure: function (X, Y, a, b) {
		var x = X(b) - X(a);
		var y = Y(b) - Y(a);
		return x * x + y * y;
	}
	,
	reduce: function (viableIndices) {
		var list = new $.ig.List$1($.ig.AngleRadiusPair.prototype.$type, 0);
		var measure = this.resolution() * this.resolution();
		var indices;
		if (viableIndices != null) {
			indices = viableIndices;
		} else {
			indices = $.ig.Enumerable.prototype.range(0, this.count());
		}
		var indicesEnumerator = indices.getEnumerator();
		var notDone = true;
		notDone = indicesEnumerator.moveNext();
		var i = 0;
		if (notDone) {
			i = indicesEnumerator.current();
		}
		while (notDone) {
			var j = i;
			notDone = indicesEnumerator.moveNext();
			i = indicesEnumerator.current();
			while (notDone && this.measure(this.transformedXProvider(), this.transformedYProvider(), j, i) < measure) {
				notDone = indicesEnumerator.moveNext();
				i = indicesEnumerator.current();
			}
			var pair = new $.ig.AngleRadiusPair();
			pair.index(j);
			if (!this.useCartesianInterpolation()) {
				pair.angle(this.angleProvider()(j));
				pair.radius(this.radiusProvider()(j));
			}
			list.add(pair);
		}
		return list;
	}
	,
	prepareCartesian: function (viableIndices) {
		var en = this.reduce(viableIndices).getEnumerator();
		while (en.moveNext()) {
			var pair = en.current();
			if ($.ig.util.isNaN(pair.angle()) || Number.isInfinity(pair.angle()) || $.ig.util.isNaN(pair.radius()) || Number.isInfinity(pair.radius())) {
				this.addPoint({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				continue;
			}
			this.addPoint({ __x: this.transformedXProvider()(pair.index()), __y: this.transformedYProvider()(pair.index()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	getErrorTolerance: function () {
		return Math.pow(this.resolution() / (Math.max(this.viewport().width() / this.window().width(), this.viewport().height() / this.window().height())), 2);
	}
	,
	addFromPolar: function (angle, radius) {
		var x = 0.5 + radius * Math.cos(angle);
		var y = 0.5 + radius * Math.sin(angle);
		x = this.__viewport.left() + this.__viewport.width() * (x - this.__window.left()) / this.__window.width();
		y = this.__viewport.top() + this.__viewport.height() * (y - this.__window.top()) / this.__window.height();
		this.addPoint({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	preparePolar: function (viableIndices) {
		var error = this.getErrorTolerance();
		var pairs = this.reduce(viableIndices);
		var a0 = pairs.item(0).angle();
		var r0 = pairs.item(0).radius();
		var i0 = pairs.item(0).index();
		for (var i = 1; i < pairs.count(); i++) {
			var ai = pairs.item(i).angle();
			var ri = pairs.item(i).radius();
			var ii = pairs.item(i).index();
			if ($.ig.util.isNaN(ai) || Number.isInfinity(ai) || $.ig.util.isNaN(ri) || Number.isInfinity(ri)) {
				this.addPoint({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				if (this.unknownValuePlotting() != $.ig.UnknownValuePlotting.prototype.linearInterpolate) {
					i++;
					if (i < pairs.count()) {
						a0 = pairs.item(i).angle();
						r0 = pairs.item(i).radius();
					}
				}
				continue;
			}
			this.createSpiralPoints(ai, ri, a0, r0, i, error, i0 > ii);
			a0 = ai;
			r0 = ri;
			i0 = ii;
		}
	}
	,
	createSpiralPoints: function (ai, ri, a0, r0, index, error, wrapAround) {
		var swapped = false;
		if ((ai < a0 && !wrapAround) || (ai > a0 && wrapAround)) {
			swapped = true;
			var swap = ai;
			ai = a0;
			a0 = swap;
			swap = ri;
			ri = r0;
			r0 = swap;
		}
		var ps = $.ig.Flattener.prototype.spiral(a0, r0, ai, ri, error);
		if (swapped) {
			ps = $.ig.Enumerable.prototype.reverse$1(Number, ps);
		}
		var en = ps.getEnumerator();
		while (en.moveNext()) {
			var p = en.current();
			var a = a0 + p * (ai - a0);
			var r = r0 + p * (ri - r0);
			this.addFromPolar(a, r);
		}
	}
	,
	ensureClipper: function (target) {
		var $self = this;
		var top = this.viewport().top() - 10;
		var bottom = this.viewport().bottom() + 10;
		var left = this.viewport().left() - 10;
		var right = this.viewport().right() + 10;
		if (this.clipper() == null) {
			this.clipper((function () {
				var $ret = new $.ig.Clipper(1, left, bottom, right, top, $self.isClosed());
				$ret.target(target);
				return $ret;
			}()));
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	addPoint: function (point) {
		if (this.unknownValuePlotting() == $.ig.UnknownValuePlotting.prototype.linearInterpolate && !this.isPlottable(point)) {
			return;
		}
		if (this.clippingDisabled()) {
			this.target().add(point);
		} else {
			this.clipper().add(point);
		}
	}
	,
	prepareLine: function (viableIndices) {
		this.prepareLine1(null, null);
	}
	,
	prepareLine1: function (target, viableIndices) {
		this.target(target);
		if (!this.valid()) {
			return;
		}
		if (this.count() > 1) {
			this.ensureClipper(target);
			if (this.useCartesianInterpolation()) {
				this.prepareCartesian(viableIndices);
			} else {
				this.preparePolar(viableIndices);
			}
			this.clipper().target(null);
		}
	}
	,
	$type: new $.ig.Type('PolarLinePlanner', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolarLineSeries', 'PolarLineSeriesBase', {
	createView: function () {
		return new $.ig.PolarLineSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarLineSeriesBase.prototype.onViewCreated.call(this, view);
		this.polarLineView(view);
	}
	,
	_polarLineView: null,
	polarLineView: function (value) {
		if (arguments.length === 1) {
			this._polarLineView = value;
			return value;
		} else {
			return this._polarLineView;
		}
	}
	,
	init: function () {
		$.ig.PolarLineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.PolarLineSeries.prototype.$type);
	},
	clearPoints: function (view) {
		var lineView = view;
		lineView.clearPolarLine();
	}
	,
	renderPoints: function (frame, view) {
		var lineView = view;
		lineView.renderPolarLine(frame.points(), this.resolution());
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarLineSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PolarLineSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	getUnknownValuePlotting: function () {
		return this.unknownValuePlotting();
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.PolarLineSeriesBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.PolarLineSeries.prototype._unknownValuePlottingPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	$type: new $.ig.Type('PolarLineSeries', $.ig.PolarLineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('PolarScatterSeries', 'PolarBase', {
	createView: function () {
		return new $.ig.PolarScatterSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarBase.prototype.onViewCreated.call(this, view);
		this.polarScatterView(view);
	}
	,
	_polarScatterView: null,
	polarScatterView: function (value) {
		if (arguments.length === 1) {
			this._polarScatterView = value;
			return value;
		} else {
			return this._polarScatterView;
		}
	}
	,
	init: function () {
		$.ig.PolarBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.PolarScatterSeries.prototype.$type);
	},
	$type: new $.ig.Type('PolarScatterSeries', $.ig.PolarBase.prototype.$type)
}, true);

$.ig.util.defType('PolarSplineAreaSeries', 'PolarLineSeriesBase', {
	createView: function () {
		return new $.ig.PolarSplineAreaSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarLineSeriesBase.prototype.onViewCreated.call(this, view);
		this.polarSplineAreaView(view);
	}
	,
	_polarSplineAreaView: null,
	polarSplineAreaView: function (value) {
		if (arguments.length === 1) {
			this._polarSplineAreaView = value;
			return value;
		} else {
			return this._polarSplineAreaView;
		}
	}
	,
	init: function () {
		$.ig.PolarLineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.PolarSplineAreaSeries.prototype.$type);
	},
	clearPoints: function (view) {
		var splineAreaView = view;
		splineAreaView.clearPolarSplineArea();
	}
	,
	renderPoints: function (frame, view) {
		this.clearPoints(view);
		if (frame.points().count() < 1) {
			return;
		}
		var polarSplineAreaView = view;
		polarSplineAreaView.polygon().__opacity = this.actualAreaFillOpacity();
		polarSplineAreaView.renderPolarSplineArea(frame.points(), this.stiffness());
	}
	,
	isClosed: function () {
		return true;
	}
	,
	stiffness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarSplineAreaSeries.prototype.stiffnessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarSplineAreaSeries.prototype.stiffnessProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.PolarLineSeriesBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.PolarSplineAreaSeries.prototype.stiffnessPropertyName:
				this.renderSeries(false);
				break;
		}
	}
	,
	$type: new $.ig.Type('PolarSplineAreaSeries', $.ig.PolarLineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('PolarSplineSeries', 'PolarLineSeriesBase', {
	createView: function () {
		return new $.ig.PolarSplineSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PolarLineSeriesBase.prototype.onViewCreated.call(this, view);
		this.polarSplineView(view);
	}
	,
	_polarSplineView: null,
	polarSplineView: function (value) {
		if (arguments.length === 1) {
			this._polarSplineView = value;
			return value;
		} else {
			return this._polarSplineView;
		}
	}
	,
	init: function () {
		$.ig.PolarLineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.PolarSplineSeries.prototype.$type);
	},
	stiffness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PolarSplineSeries.prototype.stiffnessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PolarSplineSeries.prototype.stiffnessProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.PolarLineSeriesBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.PolarSplineSeries.prototype.stiffnessPropertyName:
				this.renderSeries(false);
				break;
		}
	}
	,
	clearPoints: function (view) {
		var polarSplineView = view;
		polarSplineView.clearPolarSpline();
	}
	,
	renderPoints: function (frame, view) {
		this.clearPoints(view);
		var polarSplineView = view;
		polarSplineView.renderPolarSpline(frame.points(), this.stiffness());
	}
	,
	$type: new $.ig.Type('PolarSplineSeries', $.ig.PolarLineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('DictInterpolator$3', 'Object', {
	$tKey: null,
	$tValue: null,
	$tFrame: null,
	init: function ($tKey, $tValue, $tFrame, interpolatePointStrat, getKeyStrat, validPointStrat, createValueStrat) {
		this.$tKey = $tKey;
		this.$tValue = $tValue;
		this.$tFrame = $tFrame;
		this.$type = this.$type.specialize(this.$tKey, this.$tValue, this.$tFrame);
		$.ig.Object.prototype.init.call(this);
		this.interpolatePointStrat(interpolatePointStrat);
		this.getKeyStrat(getKeyStrat);
		this.validPointStrat(validPointStrat);
		this.createValueStrat(createValueStrat);
	},
	_interpolatePointStrat: null,
	interpolatePointStrat: function (value) {
		if (arguments.length === 1) {
			this._interpolatePointStrat = value;
			return value;
		} else {
			return this._interpolatePointStrat;
		}
	}
	,
	_getKeyStrat: null,
	getKeyStrat: function (value) {
		if (arguments.length === 1) {
			this._getKeyStrat = value;
			return value;
		} else {
			return this._getKeyStrat;
		}
	}
	,
	_validPointStrat: null,
	validPointStrat: function (value) {
		if (arguments.length === 1) {
			this._validPointStrat = value;
			return value;
		} else {
			return this._validPointStrat;
		}
	}
	,
	_createValueStrat: null,
	createValueStrat: function (value) {
		if (arguments.length === 1) {
			this._createValueStrat = value;
			return value;
		} else {
			return this._createValueStrat;
		}
	}
	,
	interpolate: function (target, p, min, max, minFrame, maxFrame) {
		var removeFromTarget = new $.ig.List$1(this.$tKey, 0);
		var en = target.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var minValue;
			var maxValue;
			var targetValue = target.item(key);
			var minContains = (function () { var $ret = min.tryGetValue(key, minValue); minValue = $ret.p1; return $ret.ret; }());
			var maxContains = (function () { var $ret = max.tryGetValue(key, maxValue); maxValue = $ret.p1; return $ret.ret; }());
			if (!minContains && !maxContains) {
				removeFromTarget.add(key);
			} else {
				this.interpolatePointStrat()(targetValue, p, minValue, maxValue, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue)) {
					removeFromTarget.add(key);
				}
			}
		}
		var en1 = removeFromTarget.getEnumerator();
		while (en1.moveNext()) {
			var key1 = en1.current();
			target.remove(key1);
		}
		var en2 = min.keys().getEnumerator();
		while (en2.moveNext()) {
			var key2 = en2.current();
			var minValue1 = min.item(key2);
			var maxValue1;
			var targetValue1;
			var $ret = max.tryGetValue(key2, maxValue1);
			maxValue1 = $ret.p1;
			var targetContains = (function () { var $ret = target.tryGetValue(key2, targetValue1); targetValue1 = $ret.p1; return $ret.ret; }());
			if (!targetContains) {
				targetValue1 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue1, p, minValue1, maxValue1, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue1)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue1), targetValue1);
			}
		}
		var en3 = max.keys().getEnumerator();
		while (en3.moveNext()) {
			var key3 = en3.current();
			var maxValue2 = max.item(key3);
			var minValue2;
			var targetValue2;
			var minContains1 = (function () { var $ret = min.tryGetValue(key3, minValue2); minValue2 = $ret.p1; return $ret.ret; }());
			var targetContains1 = (function () { var $ret = target.tryGetValue(key3, targetValue2); targetValue2 = $ret.p1; return $ret.ret; }());
			if (!targetContains1 && !minContains1) {
				targetValue2 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue2, p, minValue2, maxValue2, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue2)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue2), targetValue2);
			}
		}
	}
	,
	$type: new $.ig.Type('DictInterpolator$3', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('OwnedPoint', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.columnValues({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	},
	_point: null,
	point: function (value) {
		if (arguments.length === 1) {
			this._point = value;
			return value;
		} else {
			return this._point;
		}
	}
	,
	_ownerItem: null,
	ownerItem: function (value) {
		if (arguments.length === 1) {
			this._ownerItem = value;
			return value;
		} else {
			return this._ownerItem;
		}
	}
	,
	_columnValues: null,
	columnValues: function (value) {
		if (arguments.length === 1) {
			this._columnValues = value;
			return value;
		} else {
			return this._columnValues;
		}
	}
	,
	$type: new $.ig.Type('OwnedPoint', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Legend', 'LegendBase', {
	createView: function () {
		return new $.ig.LegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.legendView(view);
	}
	,
	_legendView: null,
	legendView: function (value) {
		if (arguments.length === 1) {
			this._legendView = value;
			return value;
		} else {
			return this._legendView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Legend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.legendView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.legendView().addItemVisual(item1);
				}
			}
		});
	},
	getSeriesIndex: function (series) {
		if ($.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null || $.ig.util.cast($.ig.SplineFragmentBase.prototype.$type, series) !== null) {
			var parentSeries = $.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null ? (series).parentSeries() : (series).parentSeries();
			if (parentSeries.reverseLegendOrder()) {
				return -1 + parentSeries.index() + parentSeries.actualSeries().count() - parentSeries.stackedSeriesManager().seriesVisual().indexOf($.ig.util.cast($.ig.AnchoredCategorySeries.prototype.$type, series));
			}
		}
		return series.index();
	}
	,
	addChildInOrder: function (legendItem, series) {
		if ($.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, series) !== null) {
			return;
		}
		if (!series.isUsableInLegend()) {
			return;
		}
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			var itemChart;
			var itemSeries;
			var itemItem;
			var $ret = this.view().fetchLegendEnvironment(item, itemChart, itemSeries, itemItem);
			itemChart = $ret.p1;
			itemSeries = $ret.p2;
			itemItem = $ret.p3;
			if (series.seriesViewer() != null && itemChart != null && (this.getSortOrder(series.seriesViewer()) < this.getSortOrder(itemChart) || (this.getSortOrder(series.seriesViewer()) == -1 && this.getSortOrder(itemChart) == -1 && series.seriesViewer().getHashCode() < itemChart.getHashCode()))) {
				break;
			}
			if (series.seriesViewer() != null && itemChart != null && series.seriesViewer() == itemChart && itemSeries != null) {
				var indexOfSeries = this.getSeriesIndex(series);
				var indexOfItemSeries = this.getSeriesIndex(itemSeries);
				var sortOrderSeries = this.getSortOrder(series);
				var sortOrderItemSeries = this.getSortOrder(itemSeries);
				if ($.ig.util.cast($.ig.BarSeries.prototype.$type, itemSeries) !== null) {
					if (sortOrderItemSeries == -1 && sortOrderSeries == -1) {
						index = 0;
						break;
					}
					if (sortOrderSeries < sortOrderItemSeries || sortOrderItemSeries == -1) {
						break;
					}
				}
				if (indexOfSeries <= indexOfItemSeries) {
					break;
				}
			}
			index++;
		}
		this.children().insert(index, legendItem);
		var info = new $.ig.LegendItemInfo();
		info.legendItem(legendItem);
		info.series(series);
		var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
		if (contentControl != null && contentControl.content() != null) {
			var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
			if (context != null) {
				info.dataContext(context);
				info.text(context.itemLabel());
			}
		}
	}
	,
	getSortOrder: function (target) {
		return -1;
	}
	,
	$type: new $.ig.Type('Legend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('AngleAxisLabelPanelView', 'AxisLabelPanelBaseView', {
	_angleModel: null,
	angleModel: function (value) {
		if (arguments.length === 1) {
			this._angleModel = value;
			return value;
		} else {
			return this._angleModel;
		}
	}
	,
	init: function (model) {
		$.ig.AxisLabelPanelBaseView.prototype.init.call(this, model);
		this.angleModel(model);
	},
	onInit: function () {
		$.ig.AxisLabelPanelBaseView.prototype.onInit.call(this);
		this.angleModel().clipLabelsToBounds(true);
	}
	,
	determineLargestLabels: function (rectangles) {
		this.angleModel().largestWidth(-1.7976931348623157E+308);
		this.angleModel().largestHeight(-1.7976931348623157E+308);
		this.angleModel().largestRenderWidth(-1.7976931348623157E+308);
		this.angleModel().largestRenderHeight(-1.7976931348623157E+308);
		for (var i = 0; i < this.model().textBlocks().count(); i++) {
			var currentTextBlock = this.model().textBlocks().__inner[i];
			var position = this.model().labelPositions().__inner[i];
			var point = this.angleModel().getPoint()(position.value());
			var width = this.getDesiredWidth(currentTextBlock);
			var height = this.getDesiredHeight(currentTextBlock);
			var fullWidth = width + this.getLabelLeftMargin() + this.getLabelRightMargin();
			var fullHeight = height + this.getLabelTopMargin() + this.getLabelBottomMargin();
			var x = point.__x - fullWidth / 2;
			var y = point.__y - fullHeight / 2;
			this.angleModel().largestRenderWidth(Math.max(width, this.angleModel().largestRenderWidth()));
			this.angleModel().largestRenderHeight(Math.max(height, this.angleModel().largestRenderHeight()));
			this.angleModel().largestWidth(Math.max(fullWidth, this.angleModel().largestWidth()));
			this.angleModel().largestHeight(Math.max(fullHeight, this.angleModel().largestHeight()));
			var rect = new $.ig.Rect(0, x, y, fullWidth, fullHeight);
			rectangles.add(rect);
		}
	}
	,
	getLabelBottomMargin: function () {
		return 0;
	}
	,
	getLabelLeftMargin: function () {
		return 0;
	}
	,
	getLabelRightMargin: function () {
		return 0;
	}
	,
	getLabelTopMargin: function () {
		return 0;
	}
	,
	$type: new $.ig.Type('AngleAxisLabelPanelView', $.ig.AxisLabelPanelBaseView.prototype.$type)
}, true);

$.ig.util.defType('CategoryAngleAxisView', 'CategoryAxisBaseView', {
	_categoryAngleModel: null,
	categoryAngleModel: function (value) {
		if (arguments.length === 1) {
			this._categoryAngleModel = value;
			return value;
		} else {
			return this._categoryAngleModel;
		}
	}
	,
	init: function (model) {
		$.ig.CategoryAxisBaseView.prototype.init.call(this, model);
		this.categoryAngleModel(model);
	},
	$type: new $.ig.Type('CategoryAngleAxisView', $.ig.CategoryAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('NumericAngleAxisView', 'NumericAxisBaseView', {
	_numericAngleModel: null,
	numericAngleModel: function (value) {
		if (arguments.length === 1) {
			this._numericAngleModel = value;
			return value;
		} else {
			return this._numericAngleModel;
		}
	}
	,
	init: function (model) {
		$.ig.NumericAxisBaseView.prototype.init.call(this, model);
		this.numericAngleModel(model);
	},
	$type: new $.ig.Type('NumericAngleAxisView', $.ig.NumericAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('NumericRadiusAxisView', 'NumericAxisBaseView', {
	_numericRadiusModel: null,
	numericRadiusModel: function (value) {
		if (arguments.length === 1) {
			this._numericRadiusModel = value;
			return value;
		} else {
			return this._numericRadiusModel;
		}
	}
	,
	init: function (model) {
		$.ig.NumericAxisBaseView.prototype.init.call(this, model);
		this.numericRadiusModel(model);
	},
	$type: new $.ig.Type('NumericRadiusAxisView', $.ig.NumericAxisBaseView.prototype.$type)
}, true);

$.ig.util.defType('RadialAxisLabelPanelView', 'HorizontalAxisLabelPanelBaseView', {
	_radialModel: null,
	radialModel: function (value) {
		if (arguments.length === 1) {
			this._radialModel = value;
			return value;
		} else {
			return this._radialModel;
		}
	}
	,
	init: function (model) {
		$.ig.HorizontalAxisLabelPanelBaseView.prototype.init.call(this, model);
		this.radialModel(model);
	},
	setLabelRotationalTransform: function (label, angle, yFactor) {
		var width = this.getDesiredWidth(label);
		var height = this.getDesiredHeight(label);
		var g = new $.ig.TransformGroup();
		g.children().add((function () {
			var $ret = new $.ig.RotateTransform();
			$ret.angle(angle);
			$ret.centerX(width / 2);
			$ret.centerY(height / 2);
			return $ret;
		}()));
		g.children().add((function () {
			var $ret = new $.ig.TranslateTransform();
			$ret.x(width / -2);
			$ret.y(width / 2 * yFactor);
			return $ret;
		}()));
		label.renderTransform(g);
	}
	,
	clearPanelRotation: function () {
		this.model().renderTransform(null);
	}
	,
	applyPanelRotation: function (finalSize) {
		var $self = this;
		var panelTop = $.ig.ViewportUtils.prototype.transformYToViewport(0.5, this.model().windowRect(), this.model().viewportRect());
		this.model().renderTransform((function () {
			var $ret = new $.ig.RotateTransform();
			$ret.angle(($self.radialModel().crossingAngle() * 180) / Math.PI);
			$ret.centerX($self.radialModel().rotationCenter().__x);
			$ret.centerY(($self.radialModel().rotationCenter().__y * finalSize.height()) + panelTop);
			return $ret;
		}()));
	}
	,
	onExtentChangedAfterArrange: function () {
		$.ig.HorizontalAxisLabelPanelBaseView.prototype.onExtentChangedAfterArrange.call(this);
		this.model().arrangeLabels(new $.ig.Size(1, this.model().labelViewport().width(), this.model().labelViewport().height()));
	}
	,
	$type: new $.ig.Type('RadialAxisLabelPanelView', $.ig.HorizontalAxisLabelPanelBaseView.prototype.$type)
}, true);

$.ig.util.defType('LegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.legendModel(model);
	},
	_legendModel: null,
	legendModel: function (value) {
		if (arguments.length === 1) {
			this._legendModel = value;
			return value;
		} else {
			return this._legendModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	$type: new $.ig.Type('LegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarBaseView', 'MarkerSeriesView', {
	_polarModel: null,
	polarModel: function (value) {
		if (arguments.length === 1) {
			this._polarModel = value;
			return value;
		} else {
			return this._polarModel;
		}
	}
	,
	_markerManager: null,
	markerManager: function (value) {
		if (arguments.length === 1) {
			this._markerManager = value;
			return value;
		} else {
			return this._markerManager;
		}
	}
	,
	init: function (model) {
		$.ig.MarkerSeriesView.prototype.init.call(this, model);
		this.polarModel(model);
		this.markers(new $.ig.HashPool$2($.ig.Object.prototype.$type, $.ig.Marker.prototype.$type));
		this.initMarkers(this.markers());
		this.trendLineManager(new $.ig.PolarTrendLineManager());
	},
	_trendLineManager: null,
	trendLineManager: function (value) {
		if (arguments.length === 1) {
			this._trendLineManager = value;
			return value;
		} else {
			return this._trendLineManager;
		}
	}
	,
	onInit: function () {
		$.ig.MarkerSeriesView.prototype.onInit.call(this);
		this.markerManager(this.createMarkerManager());
		if (!this.isThumbnailView()) {
			this.polarModel().maximumMarkers(1000);
			this.markerModel().markerType($.ig.MarkerType.prototype.automatic);
		}
	}
	,
	createMarkerManager: function () {
		var $self = this;
		var m = new $.ig.NumericMarkerManager(0, function (o) { return $self.markers().item(o); }, function (i) { return $self.polarModel().axisInfoCache().fastItemsSource().item(i); }, this.removeUnusedMarkers.runOn(this), this.getMarkerLocations.runOn(this), this.getActiveIndexes.runOn(this));
		m.populateColumnValues(true);
		m.getColumnValues(this.polarModel().getColumnValues.runOn(this.polarModel()));
		return m;
	}
	,
	removeUnusedMarkers: function (list) {
		this.polarModel().removeUnusedMarkers(list, this.markers());
	}
	,
	getMarkerLocations: function () {
		return this.polarModel().getMarkerLocations(this.markers(), this.windowRect(), this.viewport());
	}
	,
	getActiveIndexes: function () {
		return this.polarModel().getActiveIndexes(this.markers());
	}
	,
	applyClipping: function (viewportRect, windowRect, effectiveViewportRect, contentViewport) {
		if (this.polarModel().clipSeriesToBounds()) {
			var geom = new $.ig.GeometryGroup();
			this.polarModel().radiusAxis().defineClipRegion(geom, viewportRect, windowRect, effectiveViewportRect, contentViewport);
		} else {
		}
	}
	,
	updateTrendlineBrush: function () {
		if (this.polarModel().trendLineBrush() != null) {
			this.polarModel().actualTrendLineBrush(this.polarModel().trendLineBrush());
		} else {
			this.polarModel().actualTrendLineBrush(this.polarModel().actualBrush());
		}
	}
	,
	_markers: null,
	markers: function (value) {
		if (arguments.length === 1) {
			this._markers = value;
			return value;
		} else {
			return this._markers;
		}
	}
	,
	doToAllMarkers: function (action) {
		this.markers().doToAll(action);
	}
	,
	renderMarkersOverride: function (context, isHitContext) {
		if (context.shouldRender()) {
			if (this.polarModel().trendLineType() != $.ig.TrendLineType.prototype.none && !isHitContext) {
				var polyline = this.trendLineManager().trendPolyline();
				polyline.strokeThickness(this.polarModel().trendLineThickness());
				polyline.__stroke = this.polarModel().actualTrendLineBrush();
				polyline.strokeDashCap(this.polarModel().trendLineDashCap());
				polyline.strokeDashArray(this.polarModel().trendLineDashArray());
				context.renderPolyline(polyline);
			}
		}
		$.ig.MarkerSeriesView.prototype.renderMarkersOverride.call(this, context, isHitContext);
	}
	,
	getGradientBounds: function () {
		return $.ig.Rect.prototype.empty();
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipTemplate = "<div class='ui-chart-default-tooltip-content'><span>" + "${item." + this.polarModel().angleMemberPath() + "}" + "</span><br/><span";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipTemplate += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipTemplate += ">" + this.polarModel().title() + ": </span><span class='ui-priority-primary'>" + "${item." + this.polarModel().radiusMemberPath() + "}</span></div>";
		return tooltipTemplate;
	}
	,
	$type: new $.ig.Type('PolarBaseView', $.ig.MarkerSeriesView.prototype.$type)
}, true);

$.ig.util.defType('PolarLineSeriesBaseView', 'PolarBaseView', {
	_polarLineBaseModel: null,
	polarLineBaseModel: function (value) {
		if (arguments.length === 1) {
			this._polarLineBaseModel = value;
			return value;
		} else {
			return this._polarLineBaseModel;
		}
	}
	,
	init: function (model) {
		$.ig.PolarBaseView.prototype.init.call(this, model);
		this.polarLineBaseModel(model);
	},
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('PolarLineSeriesBaseView', $.ig.PolarBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarAreaSeriesView', 'PolarLineSeriesBaseView', {
	_polarAreaModel: null,
	polarAreaModel: function (value) {
		if (arguments.length === 1) {
			this._polarAreaModel = value;
			return value;
		} else {
			return this._polarAreaModel;
		}
	}
	,
	init: function (model) {
		this._polyline = new $.ig.Path();
		this._polygon = new $.ig.Path();
		$.ig.PolarLineSeriesBaseView.prototype.init.call(this, model);
		this.polarAreaModel(model);
	},
	onInit: function () {
		$.ig.PolarLineSeriesBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polyline: null,
	_polygon: null,
	polygon: function () {
		return this._polygon;
	}
	,
	clearPolarArea: function () {
		this._polygon.data(null);
		this._polyline.data(null);
		this.makeDirty();
	}
	,
	renderPolarArea: function (points, Resolution) {
		var pointsList = new $.ig.List$1($.ig.Point.prototype.$type, 1, points);
		this.polarAreaModel().currentRenderer().render(this._polyline, pointsList, Resolution);
		this.polarAreaModel().currentRenderer().render(this._polygon, pointsList, Resolution);
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupAppearanceOverride.call(this);
		this._polyline.__stroke = this.model().actualOutline();
		this._polyline.strokeThickness(this.model().thickness());
		this._polyline.strokeDashArray(this.model().dashArray());
		this._polyline.strokeDashCap(this.model().dashCap());
		this._polygon.__fill = this.model().actualBrush();
		this._polygon.__opacity = this.model().actualAreaFillOpacity();
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline.__stroke = hitBrush;
		this._polyline.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this._polygon.__fill = hitBrush;
		this._polygon.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.PolarLineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polygon);
			context.renderPath(this._polyline);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.PolarLineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var polarShape = new $.ig.PathVisualData(1, "polarShape", this._polyline);
		polarShape.tags().add("Main");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon);
		fill.tags().add("Fill");
		svd.shapes().add(polarShape);
		svd.shapes().add(fill);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(5);
		this.model().shadowOffsetY(5);
	}
	,
	$type: new $.ig.Type('PolarAreaSeriesView', $.ig.PolarLineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarLineSeriesView', 'PolarLineSeriesBaseView', {
	_polarLineModel: null,
	polarLineModel: function (value) {
		if (arguments.length === 1) {
			this._polarLineModel = value;
			return value;
		} else {
			return this._polarLineModel;
		}
	}
	,
	init: function (model) {
		this._polyline = new $.ig.Path();
		$.ig.PolarLineSeriesBaseView.prototype.init.call(this, model);
		this.polarLineModel(model);
	},
	onInit: function () {
		$.ig.PolarLineSeriesBaseView.prototype.onInit.call(this);
	}
	,
	_polyline: null,
	clearPolarLine: function () {
		this._polyline.data(null);
		this.makeDirty();
	}
	,
	renderPolarLine: function (points, resolution) {
		this.polarLineModel().currentRenderer().render(this._polyline, points, resolution);
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupAppearanceOverride.call(this);
		this._polyline.__stroke = this.model().actualBrush();
		this._polyline.strokeThickness(this.model().thickness());
		this._polyline.strokeDashArray(this.model().dashArray());
		this._polyline.strokeDashCap(this.model().dashCap());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline.__stroke = hitBrush;
		this._polyline.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.PolarLineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		context.renderPath(this._polyline);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.PolarLineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var polarShape = new $.ig.PathVisualData(1, "polarShape", this._polyline);
		polarShape.tags().add("Main");
		svd.shapes().add(polarShape);
	}
	,
	$type: new $.ig.Type('PolarLineSeriesView', $.ig.PolarLineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarScatterSeriesView', 'PolarBaseView', {
	_polarScatterModel: null,
	polarScatterModel: function (value) {
		if (arguments.length === 1) {
			this._polarScatterModel = value;
			return value;
		} else {
			return this._polarScatterModel;
		}
	}
	,
	init: function (model) {
		$.ig.PolarBaseView.prototype.init.call(this, model);
		this.polarScatterModel(model);
	},
	onInit: function () {
		$.ig.PolarBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.pointBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(2);
		this.model().shadowOffsetY(2);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('PolarScatterSeriesView', $.ig.PolarBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarSplineAreaSeriesView', 'PolarLineSeriesBaseView', {
	_polarSplineAreaModel: null,
	polarSplineAreaModel: function (value) {
		if (arguments.length === 1) {
			this._polarSplineAreaModel = value;
			return value;
		} else {
			return this._polarSplineAreaModel;
		}
	}
	,
	init: function (model) {
		this._polyline = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._polygon = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		$.ig.PolarLineSeriesBaseView.prototype.init.call(this, model);
		this.polarSplineAreaModel(model);
	},
	onInit: function () {
		$.ig.PolarLineSeriesBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polyline: null,
	_polygon: null,
	polygon: function () {
		return this._polygon;
	}
	,
	clearPolarSplineArea: function () {
		$.ig.Extensions.prototype.reset1((this._polygon.data()));
		$.ig.Extensions.prototype.reset1((this._polyline.data()));
		this.makeDirty();
	}
	,
	renderPolarSplineArea: function (points, stiffness) {
		(this._polyline.data()).figures($.ig.Numeric.prototype.spline2D1(points.count() + 1, function (i) { return i < points.count() ? points.__inner[i].__x : points.__inner[i - points.count()].__x; }, function (i) { return i < points.count() ? points.__inner[i].__y : points.__inner[i - points.count()].__y; }, stiffness));
		(this._polygon.data()).figures($.ig.Numeric.prototype.spline2D1(points.count() + 1, function (i) { return i < points.count() ? points.__inner[i].__x : points.__inner[i - points.count()].__x; }, function (i) { return i < points.count() ? points.__inner[i].__y : points.__inner[i - points.count()].__y; }, stiffness));
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupAppearanceOverride.call(this);
		this._polygon.__fill = this.model().actualBrush();
		this._polygon.__opacity = this.model().actualAreaFillOpacity();
		this._polyline.__stroke = this.model().actualOutline();
		this._polyline.strokeThickness(this.model().thickness());
		this._polyline.strokeDashArray(this.model().dashArray());
		this._polyline.strokeDashCap(this.model().dashCap());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polygon.__fill = hitBrush;
		this._polygon.__opacity = 1;
		this._polyline.__stroke = hitBrush;
		this._polyline.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.PolarLineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polygon);
			context.renderPath(this._polyline);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.PolarLineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var polarShape = new $.ig.PathVisualData(1, "polarShape", this._polyline);
		polarShape.tags().add("Main");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon);
		fill.tags().add("Fill");
		svd.shapes().add(polarShape);
		svd.shapes().add(fill);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(5);
		this.model().shadowOffsetY(5);
	}
	,
	$type: new $.ig.Type('PolarSplineAreaSeriesView', $.ig.PolarLineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('PolarSplineSeriesView', 'PolarLineSeriesBaseView', {
	_polarSplineModel: null,
	polarSplineModel: function (value) {
		if (arguments.length === 1) {
			this._polarSplineModel = value;
			return value;
		} else {
			return this._polarSplineModel;
		}
	}
	,
	init: function (model) {
		this._path = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		$.ig.PolarLineSeriesBaseView.prototype.init.call(this, model);
		this.polarSplineModel(model);
	},
	onInit: function () {
		$.ig.PolarLineSeriesBaseView.prototype.onInit.call(this);
		var pathGeometry = new $.ig.PathGeometry();
		pathGeometry.figures().add((function () {
			var $ret = new $.ig.PathFigure();
			return $ret;
		}()));
		this._path.data(pathGeometry);
	}
	,
	_path: null,
	clearPolarSpline: function () {
		$.ig.Extensions.prototype.reset1((this._path.data()));
		this.makeDirty();
	}
	,
	renderPolarSpline: function (points, stiffness) {
		(this._path.data()).figures($.ig.Numeric.prototype.spline2D1(points.count(), function (i) { return points.__inner[i].__x; }, function (i) { return points.__inner[i].__y; }, stiffness));
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupAppearanceOverride.call(this);
		this._path.__stroke = this.model().actualBrush();
		this._path.strokeThickness(this.model().thickness());
		this._path.strokeDashArray(this.model().dashArray());
		this._path.strokeDashCap(this.model().dashCap());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.PolarLineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		var getHitBrush = this.getHitBrush();
		this._path.__stroke = getHitBrush;
		this._path.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.PolarLineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		context.renderPath(this._path);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.PolarLineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var polarShape = new $.ig.PathVisualData(1, "polarShape", this._path);
		polarShape.tags().add("Main");
		svd.shapes().add(polarShape);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('PolarSplineSeriesView', $.ig.PolarLineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('PathRenderer___GetFigures__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: null,
	_segmentEnumerator: null,
	__segment_5_0: null,
	_segments: null,
	__3__segments: null,
	_resolution: 0,
	__3__resolution: 0,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	_m_Finally0: function () {
		this.__1__state = -1;
		if (this._segmentEnumerator != null) {
			this._segmentEnumerator.dispose();
		}
	}
	,
	moveNext: function () {
		var __hasError__ = false;
		try {
			while (this.__1__state >= 0) {
				switch (this.__1__state) {
					case 0:
						this.__1__state = 1;
						this._segmentEnumerator = this._segments.getEnumerator();
						this.__1__state = 2;
						break;
					case 2:
						this.__1__state = 1;
						if (this._segmentEnumerator.moveNext()) {
							this.__segment_5_0 = this._segmentEnumerator.current();
							if ($.ig.Enumerable.prototype.count$1($.ig.Point.prototype.$type, $.ig.Enumerable.prototype.take$1($.ig.Point.prototype.$type, this.__segment_5_0, 2)) >= 2) {
								var figure = new $.ig.PathFigure();
								var first = $.ig.Enumerable.prototype.first$1($.ig.Point.prototype.$type, this.__segment_5_0);
								figure.__startPoint = first;
								var seg = this.__4__this.createPolylineSegment(this.__segment_5_0, this._resolution);
								figure.__segments.add(seg);
								this.__2__current = figure;
								this.__1__state = 2;
								return true;
							}
							this.__1__state = 2;
							break;
						}
						this._m_Finally0();
						break;
				}
			}
		}
		catch (e) {
			__hasError__ = true;
			throw e;
		}
		finally {
			if (__hasError__) {
				(this).dispose();
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.PathRenderer___GetFigures__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._segments = this.__3__segments;
		d__._resolution = this.__3__resolution;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
		switch (this.__1__state) {
			case 1:
			case 2:
				this._m_Finally0();
				break;
		}
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('PathRenderer___GetFigures__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize($.ig.PathFigure.prototype.$type), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize($.ig.PathFigure.prototype.$type), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('NaNSegmenter___Segments__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: null,
	__pointEnumerator_5_0: null,
	__curr_5_1: null,
	__header_5_2: false,
	_points: null,
	__3__points: null,
	_mode: 0,
	__3__mode: 0,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__pointEnumerator_5_0 = this._points.getEnumerator();
					this.__curr_5_1 = new $.ig.List$1($.ig.Point.prototype.$type, 0);
					this.__header_5_2 = true;
					this.__1__state = 1;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__pointEnumerator_5_0.moveNext()) {
						if ($.ig.NaNSegmenter.prototype.shouldSkip(this.__pointEnumerator_5_0.current())) {
							if (!this.__header_5_2 && this._mode == $.ig.UnknownValuePlotting.prototype.dontPlot) {
								this.__header_5_2 = true;
								var toReturn = this.__curr_5_1;
								this.__curr_5_1 = new $.ig.List$1($.ig.Point.prototype.$type, 0);
								this.__2__current = new $.ig.ReadOnlyCollection$1($.ig.Point.prototype.$type, 1, toReturn);
								this.__1__state = 1;
								return true;
							}
						} else {
							this.__header_5_2 = false;
							this.__curr_5_1.add(this.__pointEnumerator_5_0.current());
						}
						this.__1__state = 1;
						break;
					}
					this.__2__current = new $.ig.ReadOnlyCollection$1($.ig.Point.prototype.$type, 1, this.__curr_5_1);
					this.__1__state = 2;
					return true;
				case 2:
					this.__1__state = -1;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.NaNSegmenter___Segments__IteratorClass(0);
		}
		d__._points = this.__3__points;
		d__._mode = this.__3__mode;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('NaNSegmenter___Segments__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize($.ig.IList$1.prototype.$type.specialize($.ig.Point.prototype.$type)), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize($.ig.IList$1.prototype.$type.specialize($.ig.Point.prototype.$type)), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.CollisionAvoidanceType.prototype.none = 0;
$.ig.CollisionAvoidanceType.prototype.omit = 1;
$.ig.CollisionAvoidanceType.prototype.fade = 2;
$.ig.CollisionAvoidanceType.prototype.omitAndShift = 3;
$.ig.CollisionAvoidanceType.prototype.fadeAndShift = 4;

$.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName = "StartAngleOffset";
$.ig.CategoryAngleAxis.prototype._intervalPropertyName = "Interval";
$.ig.CategoryAngleAxis.prototype.actualIntervalPropertyName = "ActualInterval";
$.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName = "MinorInterval";
$.ig.CategoryAngleAxis.prototype.actualMinorIntervalPropertyName = "ActualMinorInterval";
$.ig.CategoryAngleAxis.prototype.startAngleOffsetProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryAngleAxis.prototype.startAngleOffsetPropertyName, e.oldValue(), e.newValue());
}));
$.ig.CategoryAngleAxis.prototype.intervalProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype._intervalPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (sender, e) {
	(sender).raisePropertyChanged($.ig.CategoryAngleAxis.prototype._intervalPropertyName, e.oldValue(), e.newValue());
	(sender).renderAxis1(false);
}));
$.ig.CategoryAngleAxis.prototype.minorIntervalProperty = $.ig.DependencyProperty.prototype.register($.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName, Number, $.ig.CategoryAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (sender, e) {
	($.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, sender)).raisePropertyChanged($.ig.CategoryAngleAxis.prototype._minorIntervalPropertyName, e.oldValue(), e.newValue());
	($.ig.util.cast($.ig.CategoryAngleAxis.prototype.$type, sender)).renderAxis1(false);
}));

$.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName = "StartAngleOffset";
$.ig.NumericAngleAxis.prototype.startAngleOffsetProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName, Number, $.ig.NumericAngleAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericAngleAxis.prototype.startAngleOffsetPropertyName, e.oldValue(), e.newValue());
}));

$.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName = "RadiusExtentScale";
$.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName = "InnerRadiusExtentScale";
$.ig.NumericRadiusAxis.prototype.radiusExtentScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName, Number, $.ig.NumericRadiusAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0.75, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericRadiusAxis.prototype.radiusExtentScalePropertyName, e.oldValue(), e.newValue());
}));
$.ig.NumericRadiusAxis.prototype.innerRadiusExtentScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName, Number, $.ig.NumericRadiusAxis.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.NumericRadiusAxis.prototype.innerRadiusExtentScalePropertyName, e.oldValue(), e.newValue());
}));

$.ig.DefaultFlattener.prototype._flattenerChunking = 512;

$.ig.PolarBase.prototype.angleMemberPathPropertyName = "AngleMemberPath";
$.ig.PolarBase.prototype.radiusMemberPathPropertyName = "RadiusMemberPath";
$.ig.PolarBase.prototype.angleColumnPropertyName = "AngleColumn";
$.ig.PolarBase.prototype.radiusColumnPropertyName = "RadiusColumn";
$.ig.PolarBase.prototype.angleAxisPropertyName = "AngleAxis";
$.ig.PolarBase.prototype.radiusAxisPropertyName = "RadiusAxis";
$.ig.PolarBase.prototype.useCartesianInterpolationPropertyName = "UseCartesianInterpolation";
$.ig.PolarBase.prototype.maximumMarkersPropertyName = "MaximumMarkers";
$.ig.PolarBase.prototype.clipSeriesToBoundsPropertyName = "ClipSeriesToBounds";
$.ig.PolarBase.prototype.angleMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.angleMemberPathPropertyName, String, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.angleMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.radiusMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.radiusMemberPathPropertyName, String, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.radiusMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.angleAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.angleAxisPropertyName, $.ig.NumericAngleAxis.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.angleAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.radiusAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.radiusAxisPropertyName, $.ig.NumericRadiusAxis.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.radiusAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.useCartesianInterpolationProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.useCartesianInterpolationPropertyName, $.ig.Boolean.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.useCartesianInterpolationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.maximumMarkersProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.maximumMarkersPropertyName, $.ig.Number.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, 400, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.maximumMarkersPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineTypePropertyName, $.ig.TrendLineType.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.TrendLineType.prototype.getBox($.ig.TrendLineType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.actualTrendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineActualBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineActualBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineThicknessPropertyName, Number, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, 1.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineDashCapProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashCapPropertyName, $.ig.PenLineCap.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PenLineCap.prototype.getBox($.ig.PenLineCap.prototype.flat), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashCapPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineDashArrayProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashArrayPropertyName, $.ig.DoubleCollection.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashArrayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLinePeriodProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLinePeriodPropertyName, $.ig.Number.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, 7, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLinePeriodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.trendLineZIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineZIndexPropertyName, $.ig.Number.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineZIndexPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PolarBase.prototype.clipSeriesToBoundsProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarBase.prototype.clipSeriesToBoundsPropertyName, $.ig.Boolean.prototype.$type, $.ig.PolarBase.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarBase.prototype.clipSeriesToBoundsPropertyName, e.oldValue(), e.newValue());
}));

$.ig.PolarAreaSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.PolarAreaSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarAreaSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.PolarAreaSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarAreaSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.PolarLineSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.PolarLineSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarLineSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.PolarLineSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarLineSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.PolarSplineAreaSeries.prototype.stiffnessPropertyName = "Stiffness";
$.ig.PolarSplineAreaSeries.prototype.stiffnessProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarSplineAreaSeries.prototype.stiffnessPropertyName, Number, $.ig.PolarSplineAreaSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarSplineAreaSeries.prototype.stiffnessPropertyName, e.oldValue(), e.newValue());
}));

$.ig.PolarSplineSeries.prototype.stiffnessPropertyName = "Stiffness";
$.ig.PolarSplineSeries.prototype.stiffnessProperty = $.ig.DependencyProperty.prototype.register($.ig.PolarSplineSeries.prototype.stiffnessPropertyName, Number, $.ig.PolarSplineSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PolarSplineSeries.prototype.stiffnessPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


