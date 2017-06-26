/*!@license
* Infragistics.Web.ClientUI infragistics.chart_funnelchart.js 16.1.20161.2145
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
"IArray:bi", 
"List$1:bj", 
"IList$1:bk", 
"ICollection$1:bl", 
"Script:bm", 
"IArrayList:bn", 
"Array:bo", 
"CompareCallback:bp", 
"Func$3:bq", 
"Action$1:br", 
"Comparer$1:bs", 
"IComparer:bt", 
"IComparer$1:bu", 
"DefaultComparer$1:bv", 
"Comparison$1:bw", 
"ReadOnlyCollection$1:bx", 
"Predicate$1:by", 
"NotImplementedException:bz", 
"Math:cb", 
"Dictionary_EnumerableCollection$3:ch", 
"InvalidOperationException:ci", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck", 
"INotifyCollectionChanged:cm", 
"NotifyCollectionChangedEventHandler:cn", 
"NotifyCollectionChangedEventArgs:co", 
"EventArgs:cp", 
"NotifyCollectionChangedAction:cq", 
"ObservableCollection$1:ct", 
"INotifyPropertyChanged:cu", 
"PropertyChangedEventHandler:cv", 
"PropertyChangedEventArgs:cw", 
"Delegate:cx", 
"Interlocked:cy", 
"Queue$1:cz", 
"Stack$1:c2", 
"ReverseArrayEnumerator$1:c3", 
"DependencyObject:de", 
"Dictionary:df", 
"DependencyProperty:dg", 
"PropertyMetadata:dh", 
"PropertyChangedCallback:di", 
"DependencyPropertyChangedEventArgs:dj", 
"DependencyPropertiesCollection:dk", 
"UnsetValue:dl", 
"Binding:dm", 
"PropertyPath:dn", 
"Point:dt", 
"Convert:d3", 
"Environment:d5", 
"StringBuilder:eu", 
"Element:fe", 
"ElementAttributeCollection:ff", 
"ElementCollection:fg", 
"WebStyle:fh", 
"ElementNodeType:fi", 
"Document:fj", 
"EventListener:fk", 
"IElementEventHandler:fl", 
"ElementEventHandler:fm", 
"ElementAttribute:fn", 
"UIElement:gd", 
"Transform:ge", 
"Key:g1", 
"ModifierKeys:g2", 
"MouseEventArgs:g3", 
"MouseButtonEventArgs:g4"]);


$.ig.util.defType('ModifierKeys', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Alt";
			case 2: return "Control";
			case 4: return "Shift";
			case 8: return "Windows";
			case 8: return "Apple";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ModifierKeys', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('Key', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Back";
			case 2: return "Tab";
			case 3: return "Enter";
			case 4: return "Shift";
			case 5: return "Ctrl";
			case 6: return "Alt";
			case 7: return "CapsLock";
			case 8: return "Escape";
			case 9: return "Space";
			case 10: return "PageUp";
			case 11: return "PageDown";
			case 12: return "End";
			case 13: return "Home";
			case 14: return "Left";
			case 15: return "Up";
			case 16: return "Right";
			case 17: return "Down";
			case 18: return "Insert";
			case 19: return "Delete";
			case 20: return "D0";
			case 21: return "D1";
			case 22: return "D2";
			case 23: return "D3";
			case 24: return "D4";
			case 25: return "D5";
			case 26: return "D6";
			case 27: return "D7";
			case 28: return "D8";
			case 29: return "D9";
			case 30: return "A";
			case 31: return "B";
			case 32: return "C";
			case 33: return "D";
			case 34: return "E";
			case 35: return "F";
			case 36: return "G";
			case 37: return "H";
			case 38: return "I";
			case 39: return "J";
			case 40: return "K";
			case 41: return "L";
			case 42: return "M";
			case 43: return "N";
			case 44: return "O";
			case 45: return "P";
			case 46: return "Q";
			case 47: return "R";
			case 48: return "S";
			case 49: return "T";
			case 50: return "U";
			case 51: return "V";
			case 52: return "W";
			case 53: return "X";
			case 54: return "Y";
			case 55: return "Z";
			case 56: return "F1";
			case 57: return "F2";
			case 58: return "F3";
			case 59: return "F4";
			case 60: return "F5";
			case 61: return "F6";
			case 62: return "F7";
			case 63: return "F8";
			case 64: return "F9";
			case 65: return "F10";
			case 66: return "F11";
			case 67: return "F12";
			case 68: return "NumPad0";
			case 69: return "NumPad1";
			case 70: return "NumPad2";
			case 71: return "NumPad3";
			case 72: return "NumPad4";
			case 73: return "NumPad5";
			case 74: return "NumPad6";
			case 75: return "NumPad7";
			case 76: return "NumPad8";
			case 77: return "NumPad9";
			case 78: return "Multiply";
			case 79: return "Add";
			case 80: return "Subtract";
			case 81: return "Decimal";
			case 82: return "Divide";
			case 255: return "Unknown";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('Key', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('INotifyCollectionChanged', 'Object', {
	$type: new $.ig.Type('INotifyCollectionChanged', null)
}, true);

$.ig.util.defType('ObservableCollection$1', 'List$1', {
	$t: null,
	init: function ($t, initNumber) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.List$1.prototype.init.call(this, this.$t, 0);
	},
	init1: function ($t, initNumber, source) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.List$1.prototype.init1.call(this, this.$t, 1, source);
	},
	init2: function ($t, initNumber, capacity) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.List$1.prototype.init2.call(this, this.$t, 2, capacity);
	},
	setItem: function (index, newItem) {
		var oldItem = this.__inner[index];
		$.ig.List$1.prototype.setItem.call(this, index, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(2, $.ig.NotifyCollectionChangedAction.prototype.replace, $.ig.util.getBoxIfEnum(this.$t, newItem), $.ig.util.getBoxIfEnum(this.$t, oldItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	clearItems: function () {
		$.ig.List$1.prototype.clearItems.call(this);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset);
			this.onCollectionChanged(args);
		}
	}
	,
	insertItem: function (index, newItem) {
		$.ig.List$1.prototype.insertItem.call(this, index, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, $.ig.util.getBoxIfEnum(this.$t, newItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	addItem: function (newItem) {
		$.ig.List$1.prototype.addItem.call(this, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, $.ig.util.getBoxIfEnum(this.$t, newItem), this.count() - 1);
			this.onCollectionChanged(args);
		}
	}
	,
	removeItem: function (index) {
		var oldItem = this.__inner[index];
		$.ig.List$1.prototype.removeItem.call(this, index);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.remove, $.ig.util.getBoxIfEnum(this.$t, oldItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	collectionChanged: null,
	propertyChanged: null,
	onPropertyChanged: function (args) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, args);
		}
	}
	,
	onCollectionChanged: function (args) {
		if (this.collectionChanged != null) {
			this.collectionChanged(this, args);
		}
	}
	,
	$type: new $.ig.Type('ObservableCollection$1', $.ig.List$1.prototype.$type.specialize(0), [$.ig.INotifyCollectionChanged.prototype.$type, $.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('Queue$1', 'Object', {
	$t: null,
	__count: 0,
	__head: 0,
	__tail: 0,
	__items: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__items = new Array(4);
	},
	count: function () {
		return this.__count;
	}
	,
	copyTo: function (array, index) {
		if (this.__head < this.__tail) {
			for (var i = this.__head; i < this.__tail; i++) {
				array[index++] = $.ig.util.getBoxIfEnum(this.$t, this.__items[i]);
			}
		} else {
			for (var i1 = this.__head; i1 < this.__items.length; i1++) {
				array[index++] = $.ig.util.getBoxIfEnum(this.$t, this.__items[i1]);
			}
			for (var i2 = 0; i2 < this.__tail; i2++) {
				array[index++] = $.ig.util.getBoxIfEnum(this.$t, this.__items[i2]);
			}
		}
	}
	,
	isSynchronized: function () {
		return false;
	}
	,
	syncRoot: function () {
		return null;
	}
	,
	getEnumerator: function () {
		var d__ = new $.ig.Queue___GetEnumerator__IteratorClass$1(this.$t, 0);
		d__.__4__this = this;
		return d__;
	}
	,
	enqueue: function (item) {
		if (this.__count == this.__items.length) {
			var newItems = new Array(Math.max(2, this.__items.length * 2));
			this.copyTo(newItems, 0);
			this.__head = 0;
			this.__tail = this.__items.length;
			this.__items = newItems;
		}
		this.__items[this.__tail] = item;
		this.__tail = (this.__tail + 1) % this.__items.length;
		this.__count++;
	}
	,
	dequeue: function () {
		if (this.__count == 0) {
			throw new $.ig.InvalidOperationException(1, "The Queue is empty.");
		}
		var result = this.__items[this.__head];
		this.__items[this.__head] = null;
		this.__head = (this.__head + 1) % this.__items.length;
		this.__count--;
		return result;
	}
	,
	peek: function () {
		if (this.__count == 0) {
			throw new $.ig.InvalidOperationException(1, "The Queue is empty.");
		}
		return this.__items[this.__head];
	}
	,
	$type: new $.ig.Type('Queue$1', $.ig.Object.prototype.$type, [$.ig.ICollection.prototype.$type, $.ig.IEnumerable$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('Stack$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this.__inner = new $.ig.Array();
		$.ig.Object.prototype.init.call(this);
	},
	__inner: null,
	push: function (item) {
		this.__inner.add($.ig.util.getBoxIfEnum(this.$t, item));
	}
	,
	peek: function () {
		if (this.__inner.length < 1) {
			return $.ig.util.getDefaultValue(this.$t);
		}
		return $.ig.util.castObjTo$t(this.$t, this.__inner[this.__inner.length - 1]);
	}
	,
	pop: function () {
		var ret = this.__inner[this.__inner.length - 1];
		this.__inner.removeAt(this.__inner.length - 1);
		return $.ig.util.castObjTo$t(this.$t, ret);
	}
	,
	count: function () {
		return this.__inner.length;
	}
	,
	clear: function () {
		this.__inner.clear();
	}
	,
	contains: function (item) {
		return this.__inner.contains($.ig.util.getBoxIfEnum(this.$t, item));
	}
	,
	getEnumerator: function () {
		return new $.ig.ReverseArrayEnumerator$1(this.$t, this.__inner);
	}
	,
	getEnumerator: function () {
		return new $.ig.ReverseArrayEnumerator$1(this.$t, this.__inner);
	}
	,
	$type: new $.ig.Type('Stack$1', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('ReverseArrayEnumerator$1', 'Object', {
	$t: null,
	__index: 0,
	__array: null,
	init: function ($t, array) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__array = array;
		this.__index = array.length;
	},
	current: function () {
		return $.ig.util.castObjTo$t(this.$t, this.__array[this.__index]);
	}
	,
	current: function () {
		return this.__array[this.__index];
	}
	,
	moveNext: function () {
		this.__index--;
		return this.__index >= 0;
	}
	,
	reset: function () {
		this.__index = this.__array.length;
	}
	,
	dispose: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	$type: new $.ig.Type('ReverseArrayEnumerator$1', $.ig.Object.prototype.$type, [$.ig.IEnumerator$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('Convert', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	toDouble5: function (value) {
		return value;
	}
	,
	toDouble1: function (value) {
		return value;
	}
	,
	toDouble: function (value) {
		return value;
	}
	,
	toDouble2: function (value) {
		return value;
	}
	,
	toDecimal: function (value) {
		return value;
	}
	,
	toDecimal2: function (value) {
		return value;
	}
	,
	toDecimal1: function (value) {
		return value;
	}
	,
	toInt32: function (value) {
		if (value >= 0) {
			var ret = $.ig.truncate(Math.floor(value));
			var diff1 = value - ret;
			var diff2 = Math.ceil(value) - value;
			if (diff1 > diff2 || ((diff1 == diff2) && (ret & 1) > 0)) {
				ret++;
			}
			return ret;
		} else {
			var ret1 = $.ig.truncate(Math.ceil(value));
			var diff11 = ret1 - value;
			var diff21 = value - Math.floor(value);
			if (diff11 > diff21 || ((diff11 == diff21) && (ret1 & 1) > 0)) {
				ret1--;
			}
			return ret1;
		}
	}
	,
	toInt322: function (value) {
		return parseInt(value);
	}
	,
	toDouble3: function (value) {
		return $.ig.Convert.prototype.toDouble4(value, $.ig.CultureInfo.prototype.currentCulture());
	}
	,
	toDouble4: function (value, provider) {
		var valueResolved = $.ig.util.getValue($.ig.util.unwrapNullable(value));
		if (valueResolved == null) {
			return 0;
		}
		var result = +valueResolved;
		if ($.ig.util.isNaN(result)) {
			return (valueResolved).toDouble(provider);
		}
		return result;
	}
	,
	toInt321: function (value) {
		var valueResolved = $.ig.util.getValue($.ig.util.unwrapNullable(value));
		if (valueResolved == null) {
			return 0;
		}
		var result = +valueResolved;
		if ($.ig.util.isNaN(result)) {
			return (valueResolved).toInt32($.ig.CultureInfo.prototype.currentCulture());
		}
		return result;
	}
	,
	toByte: function (value) {
		return (value ? 1 : 0);
	}
	,
	toByte1: function (value) {
		var valueResolved = $.ig.util.getValue($.ig.util.unwrapNullable(value));
		if (valueResolved == null) {
			return 0;
		}
		var result = +valueResolved;
		if ($.ig.util.isNaN(result)) {
			return (valueResolved).toByte($.ig.CultureInfo.prototype.currentCulture());
		}
		return result;
	}
	,
	toChar: function (value) {
		return String.fromCharCode(value);
	}
	,
	toChar1: function (value) {
		return String.fromCharCode(value);
	}
	,
	toDouble6: function (value, provider) {
		return $.ig.util.parseNumber(value, provider);
	}
	,
	toUInt16: function (value) {
		return (value ? 1 : 0);
	}
	,
	toBoolean: function (value) {
		return value != 0;
	}
	,
	toUInt32: function (value) {
		return value;
	}
	,
	fromBase64String: function (s) {
		return $.ig.util.b64toUint8Array(s);
	}
	,
	toBase64String: function (inArray) {
		return $.ig.util.uint8ArraytoB64(inArray);
	}
	,
	toByte2: function (value, fromBase) {
		throw new $.ig.NotImplementedException(0);
		return 0;
	}
	,
	$type: new $.ig.Type('Convert', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Environment', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	newLine: function () {
		return "\n";
	}
	,
	$type: new $.ig.Type('Environment', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('StringBuilder', 'Object', {
	_internal: null,
	internal: function (value) {
		if (arguments.length === 1) {
			this._internal = value;
			return value;
		} else {
			return this._internal;
		}
	}
	,
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.internal("");
	},
	init1: function (initNumber, capacity) {
		$.ig.StringBuilder.prototype.init.call(this, 0);
	},
	init2: function (initNumber, value) {
		$.ig.Object.prototype.init.call(this);
		this.internal(value);
	},
	append4: function (obj) {
		if (obj != null) {
			this.append5(obj.toString());
		}
		return this;
	}
	,
	append5: function (str_) {
		if (str_ != null)
        {
            this._internal = this._internal.concat(str_);
        };
		return this;
	}
	,
	append7: function (builder) {
		var str_ = builder.toString();
		this._internal = this._internal.concat(str_);
		return this;
	}
	,
	append1: function (chr_) {
		this._internal = this._internal.concat(chr_);
		return this;
	}
	,
	append2: function (chr_, count_) {
		this._internal = this._internal.concat(chr_.repeat(count_));
		return this;
	}
	,
	append3: function (value_) {
		this._internal = this._internal.concat(value_);
		return this;
	}
	,
	append6: function (value_, startIndex_, count_) {
		this._internal = this._internal.concat(value_.substr(startIndex_, count_));
		return this;
	}
	,
	append: function (value_, startIndex_, charCount_) {
		this._internal = this._internal.concat(value_.slice(startIndex_, startIndex_ + charCount_).join(''));
		return this;
	}
	,
	appendLine: function () {
		return this.appendLine1("");
	}
	,
	appendLine1: function (str_) {
		if (str_ != null)
        {
            this._internal = this._internal.concat(str_);
        }
        this._internal = this._internal.concat($.ig.Environment.prototype.newLine());;
		return this;
	}
	,
	clear: function () {
		this.internal("");
		return this;
	}
	,
	insert: function (index_, chr_) {
		if (index_ == this.length()) {
			this.append1(chr_);
		} else {
			this._internal = this._internal.substring(0, index_).concat(chr_).concat(this._internal.substring(index_, this._internal.length));
		}
		return this;
	}
	,
	insert1: function (index_, str_) {
		if (index_ == this.length()) {
			this.append5(str_);
		} else {
			this._internal = this._internal.substring(0, index_).concat(str_).concat(this._internal.substring(index_, this._internal.length));
		}
		return this;
	}
	,
	remove: function (startIndex_, length_) {
		this._internal = this._internal.substring(0, startIndex_).concat(this._internal.substring(startIndex_ + length_, this._internal.length));
		return this;
	}
	,
	toString: function () {
		return this.internal();
	}
	,
	toString1: function (startIndex, length) {
		return this.internal().substr(startIndex, length);
	}
	,
	length: function (value) {
		if (arguments.length === 1) {
			if (value <= this.length()) {
				this._internal = this._internal.substring(0, value);
			} else {
				throw new $.ig.NotImplementedException(0);
			}
			return value;
		} else {
			return this.internal().length;
		}
	}
	,
	item: function (index_, value) {
		if (arguments.length === 2) {
			this._internal = this._internal.substring(0, index_).concat(value).concat(this._internal.substring(index_ + 1, this._internal.length));
			return value;
		} else {
			return this.internal().charAt(index_);
		}
	}
	,
	appendFormat2: function (format, arg0) {
		return this.append5($.ig.util.stringFormat(format, arg0));
	}
	,
	appendFormat1: function (format, args) {
		return this.append5($.ig.util.stringFormat1(format, args));
	}
	,
	appendFormat: function (provider, format, args) {
		return this.append5($.ig.util.stringFormat2(provider, format, args));
	}
	,
	appendFormat3: function (format, arg0, arg1) {
		return this.append5($.ig.util.stringFormat(format, arg0, arg1));
	}
	,
	appendFormat4: function (format, arg0, arg1, arg2) {
		return this.append5($.ig.util.stringFormat(format, arg0, arg1, arg2));
	}
	,
	_capacity: 0,
	capacity: function (value) {
		if (arguments.length === 1) {
			this._capacity = value;
			return value;
		} else {
			return this._capacity;
		}
	}
	,
	$type: new $.ig.Type('StringBuilder', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MouseEventArgs', 'EventArgs', {
	init: function () {
		$.ig.EventArgs.prototype.init.call(this);
	},
	_position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
		}
	}
	,
	_originalSource: null,
	originalSource: function (value) {
		if (arguments.length === 1) {
			this._originalSource = value;
			return value;
		} else {
			return this._originalSource;
		}
	}
	,
	getPosition: function (relativeTo) {
		return this.position();
	}
	,
	$type: new $.ig.Type('MouseEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('MouseButtonEventArgs', 'MouseEventArgs', {
	init: function () {
		$.ig.MouseEventArgs.prototype.init.call(this);
	},
	_handled: false,
	handled: function (value) {
		if (arguments.length === 1) {
			this._handled = value;
			return value;
		} else {
			return this._handled;
		}
	}
	,
	$type: new $.ig.Type('MouseButtonEventArgs', $.ig.MouseEventArgs.prototype.$type)
}, true);

$.ig.util.defType('Queue___GetEnumerator__IteratorClass$1', 'Object', {
	$t: null,
	__1__state: 0,
	__2__current: null,
	__i_5_0: 0,
	__i_5_1: 0,
	__i_5_2: 0,
	__4__this: null,
	init: function ($t, _1__state) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					if (this.__4__this.__head < this.__4__this.__tail) {
						this.__i_5_0 = this.__4__this.__head;
						this.__1__state = 1;
						break;
					}
					this.__i_5_1 = this.__4__this.__head;
					this.__1__state = 3;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__i_5_0 < this.__4__this.__tail) {
						this.__2__current = this.__4__this.__items[this.__i_5_0];
						this.__1__state = 2;
						return true;
					}
					break;
				case 2:
					this.__1__state = -1;
					this.__i_5_0++;
					this.__1__state = 1;
					break;
				case 3:
					this.__1__state = -1;
					if (this.__i_5_1 < this.__4__this.__items.length) {
						this.__2__current = this.__4__this.__items[this.__i_5_1];
						this.__1__state = 4;
						return true;
					}
					this.__i_5_2 = 0;
					this.__1__state = 5;
					break;
				case 4:
					this.__1__state = -1;
					this.__i_5_1++;
					this.__1__state = 3;
					break;
				case 5:
					this.__1__state = -1;
					if (this.__i_5_2 < this.__4__this.__tail) {
						this.__2__current = this.__4__this.__items[this.__i_5_2];
						this.__1__state = 6;
						return true;
					}
					break;
				case 6:
					this.__1__state = -1;
					this.__i_5_2++;
					this.__1__state = 5;
					break;
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
	$type: new $.ig.Type('Queue___GetEnumerator__IteratorClass$1', $.ig.Object.prototype.$type, [$.ig.IEnumerator$1.prototype.$type.specialize(0), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.ModifierKeys.prototype.none = 0;
$.ig.ModifierKeys.prototype.alt = 1;
$.ig.ModifierKeys.prototype.control = 2;
$.ig.ModifierKeys.prototype.shift = 4;
$.ig.ModifierKeys.prototype.windows = 8;
$.ig.ModifierKeys.prototype.apple = 8;

$.ig.Key.prototype.none = 0;
$.ig.Key.prototype.back = 1;
$.ig.Key.prototype.tab = 2;
$.ig.Key.prototype.enter = 3;
$.ig.Key.prototype.shift = 4;
$.ig.Key.prototype.ctrl = 5;
$.ig.Key.prototype.alt = 6;
$.ig.Key.prototype.capsLock = 7;
$.ig.Key.prototype.escape = 8;
$.ig.Key.prototype.space = 9;
$.ig.Key.prototype.pageUp = 10;
$.ig.Key.prototype.pageDown = 11;
$.ig.Key.prototype.end = 12;
$.ig.Key.prototype.home = 13;
$.ig.Key.prototype.left = 14;
$.ig.Key.prototype.up = 15;
$.ig.Key.prototype.right = 16;
$.ig.Key.prototype.down = 17;
$.ig.Key.prototype.insert = 18;
$.ig.Key.prototype.del = 19;
$.ig.Key.prototype.d0 = 20;
$.ig.Key.prototype.d1 = 21;
$.ig.Key.prototype.d2 = 22;
$.ig.Key.prototype.d3 = 23;
$.ig.Key.prototype.d4 = 24;
$.ig.Key.prototype.d5 = 25;
$.ig.Key.prototype.d6 = 26;
$.ig.Key.prototype.d7 = 27;
$.ig.Key.prototype.d8 = 28;
$.ig.Key.prototype.d9 = 29;
$.ig.Key.prototype.a = 30;
$.ig.Key.prototype.b = 31;
$.ig.Key.prototype.c = 32;
$.ig.Key.prototype.d = 33;
$.ig.Key.prototype.e = 34;
$.ig.Key.prototype.f = 35;
$.ig.Key.prototype.g = 36;
$.ig.Key.prototype.h = 37;
$.ig.Key.prototype.i = 38;
$.ig.Key.prototype.j = 39;
$.ig.Key.prototype.k = 40;
$.ig.Key.prototype.l = 41;
$.ig.Key.prototype.m = 42;
$.ig.Key.prototype.n = 43;
$.ig.Key.prototype.o = 44;
$.ig.Key.prototype.p = 45;
$.ig.Key.prototype.q = 46;
$.ig.Key.prototype.r = 47;
$.ig.Key.prototype.s = 48;
$.ig.Key.prototype.t = 49;
$.ig.Key.prototype.u = 50;
$.ig.Key.prototype.v = 51;
$.ig.Key.prototype.w = 52;
$.ig.Key.prototype.x = 53;
$.ig.Key.prototype.y = 54;
$.ig.Key.prototype.z = 55;
$.ig.Key.prototype.f1 = 56;
$.ig.Key.prototype.f2 = 57;
$.ig.Key.prototype.f3 = 58;
$.ig.Key.prototype.f4 = 59;
$.ig.Key.prototype.f5 = 60;
$.ig.Key.prototype.f6 = 61;
$.ig.Key.prototype.f7 = 62;
$.ig.Key.prototype.f8 = 63;
$.ig.Key.prototype.f9 = 64;
$.ig.Key.prototype.f10 = 65;
$.ig.Key.prototype.f11 = 66;
$.ig.Key.prototype.f12 = 67;
$.ig.Key.prototype.numPad0 = 68;
$.ig.Key.prototype.numPad1 = 69;
$.ig.Key.prototype.numPad2 = 70;
$.ig.Key.prototype.numPad3 = 71;
$.ig.Key.prototype.numPad4 = 72;
$.ig.Key.prototype.numPad5 = 73;
$.ig.Key.prototype.numPad6 = 74;
$.ig.Key.prototype.numPad7 = 75;
$.ig.Key.prototype.numPad8 = 76;
$.ig.Key.prototype.numPad9 = 77;
$.ig.Key.prototype.multiply = 78;
$.ig.Key.prototype.add = 79;
$.ig.Key.prototype.subtract = 80;
$.ig.Key.prototype.decimal = 81;
$.ig.Key.prototype.divide = 82;
$.ig.Key.prototype.unknown = 255;

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["EventProxy:a", 
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
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"ModifierKeys:be", 
"Func$2:bf", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"MouseWheelHandler:bi", 
"Delegate:bj", 
"Interlocked:bk", 
"GestureHandler:bl", 
"ZoomGestureHandler:bm", 
"FlingGestureHandler:bn", 
"ContactHandler:bo", 
"TouchHandler:bp", 
"MouseOverHandler:bq", 
"MouseHandler:br", 
"KeyHandler:bs", 
"Key:bt", 
"BaseDOMEventProxy:bu", 
"JQueryObject:bv", 
"Element:bw", 
"ElementAttributeCollection:bx", 
"ElementCollection:by", 
"WebStyle:bz", 
"ElementNodeType:b0", 
"Document:b1", 
"EventListener:b2", 
"IElementEventHandler:b3", 
"ElementEventHandler:b4", 
"ElementAttribute:b5", 
"JQueryPosition:b6", 
"JQueryCallback:b7", 
"JQueryEvent:b8", 
"JQueryUICallback:b9", 
"DOMEventProxy:ca", 
"MSGesture:cb", 
"Script:cc", 
"JQuery:cd", 
"JQueryDeferred:ce", 
"JQueryPromise:cf", 
"Action:cg", 
"Action$1:ch", 
"Callback:ci", 
"window:cj", 
"MouseEventArgs:ck", 
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
"IFastItemColumnInternal:dt", 
"IFastItemColumnPropertyName:du", 
"IFastItemColumn$1:dv", 
"FastItemColumn:dw", 
"IFastItemsSource:dx", 
"EventHandler$1:dy", 
"FastItemsSourceEventArgs:dz", 
"FastItemsSourceEventAction:d0", 
"NotifyCollectionChangedEventArgs:d1", 
"NotifyCollectionChangedAction:d2", 
"FastReflectionHelper:d3", 
"FastItemDateTimeColumn:d4", 
"FastItemObjectColumn:d5", 
"FastItemIntColumn:d6", 
"FastItemsSource:d7", 
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
"ColumnReference:eo", 
"Shape:et", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"Brush:ex", 
"Color:ey", 
"DoubleCollection:ez", 
"Path:e0", 
"Geometry:e1", 
"GeometryType:e2", 
"TextBlock:e3", 
"PointCollection:e5", 
"Line:fj", 
"FontInfo:fk", 
"LinearGradientBrush:fv", 
"GradientStop:fw", 
"GeometryGroup:fx", 
"GeometryCollection:fy", 
"FillRule:fz", 
"PathGeometry:f0", 
"PathFigureCollection:f1", 
"LineGeometry:f2", 
"RectangleGeometry:f3", 
"EllipseGeometry:f4", 
"ArcSegment:f5", 
"PathSegment:f6", 
"PathSegmentType:f7", 
"SweepDirection:f8", 
"PathFigure:f9", 
"PathSegmentCollection:ga", 
"LineSegment:gb", 
"PolyLineSegment:gc", 
"BezierSegment:gd", 
"PolyBezierSegment:ge", 
"GeometryUtil:gf", 
"Tuple$2:gg", 
"TransformGroup:gh", 
"TransformCollection:gi", 
"RotateTransform:gk", 
"INotifyPropertyChanged:gn", 
"PropertyChangedEventHandler:go", 
"PropertyChangedEventArgs:gp", 
"DivElement:gy", 
"DoubleAnimator:gz", 
"EasingFunctionHandler:g0", 
"RectUtil:g2", 
"MathUtil:g3", 
"RuntimeHelpers:g4", 
"RuntimeFieldHandle:g5", 
"BrushUtil:g6", 
"ColorUtil:g7", 
"Random:g8", 
"InterpolationMode:g9", 
"BrushCollection:ha", 
"ObservableCollection$1:hb", 
"INotifyCollectionChanged:hc", 
"NotifyCollectionChangedEventHandler:hd", 
"CssHelper:he", 
"CssGradientUtil:hf", 
"Stack$1:hj", 
"ReverseArrayEnumerator$1:hk", 
"Convert:h9", 
"Func$1:ib", 
"StringBuilder:ih", 
"Environment:ii", 
"IIndexedPool$1:jd", 
"IPool$1:je", 
"Pool$1:jp", 
"IVisualData:jt", 
"PrimitiveVisualData:jv", 
"PrimitiveAppearanceData:jw", 
"BrushAppearanceData:jx", 
"AppearanceHelper:jy", 
"LinearGradientBrushAppearanceData:jz", 
"GradientStopAppearanceData:j0", 
"SolidBrushAppearanceData:j1", 
"GeometryData:j2", 
"GetPointsSettings:j3", 
"EllipseGeometryData:j4", 
"RectangleGeometryData:j5", 
"LineGeometryData:j6", 
"PathGeometryData:j7", 
"PathFigureData:j8", 
"SegmentData:j9", 
"LineSegmentData:ka", 
"PolylineSegmentData:kb", 
"ArcSegmentData:kc", 
"PolyBezierSegmentData:kd", 
"BezierSegmentData:ke", 
"LabelAppearanceData:kf", 
"ShapeTags:kg", 
"PointData:kh", 
"PathVisualData:ko", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('FastItemsSourceEventAction', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Remove";
			case 1: return "Insert";
			case 2: return "Replace";
			case 3: return "Change";
			case 4: return "Reset";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('FastItemsSourceEventAction', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('InterpolationMode', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "RGB";
			case 1: return "HSV";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('InterpolationMode', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('EventProxy', 'Object', {
	init: function () {
		this.__isInteractionDisabled = false;
		$.ig.Object.prototype.init.call(this);
	},
	onMouseWheel: null,
	onPinchStarted: null,
	onPinchDelta: null,
	onGestureCompleted: null,
	onZoomStarted: null,
	onZoomDelta: null,
	onZoomCompleted: null,
	onFlingStarted: null,
	onContactStarted: null,
	onDragStarted: null,
	onContactMoved: null,
	onDragDelta: null,
	onContactCompleted: null,
	onDragCompleted: null,
	onMouseLeave: null,
	onMouseOver: null,
	onMouseEnter: null,
	onMouseDown: null,
	onMouseUp: null,
	onDoubleTap: null,
	onHold: null,
	onKeyDown: null,
	onKeyUp: null,
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
	_currentModifiers: 0,
	currentModifiers: function (value) {
		if (arguments.length === 1) {
			this._currentModifiers = value;
			return value;
		} else {
			return this._currentModifiers;
		}
	}
	,
	_rightButton: false,
	rightButton: function (value) {
		if (arguments.length === 1) {
			this._rightButton = value;
			return value;
		} else {
			return this._rightButton;
		}
	}
	,
	_shouldInteract: null,
	shouldInteract: function (value) {
		if (arguments.length === 1) {
			this._shouldInteract = value;
			return value;
		} else {
			return this._shouldInteract;
		}
	}
	,
	clone: function () {
	}
	,
	destroy: function () {
	}
	,
	supportsNonIsometricZoom: function () {
		return false;
	}
	,
	_originalEvent: null,
	originalEvent: function (value) {
		if (arguments.length === 1) {
			this._originalEvent = value;
			return value;
		} else {
			return this._originalEvent;
		}
	}
	,
	raiseOnMouseWheel: function (point, delta) {
		if (this.onMouseWheel != null && !this.isInteractionDisabled()) {
			return this.onMouseWheel(point, delta);
		}
		return false;
	}
	,
	raiseOnPinchStarted: function (point, scale) {
		if (this.onPinchStarted != null && !this.isInteractionDisabled()) {
			this.onPinchStarted(point, scale);
		}
	}
	,
	raiseOnZoomStarted: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomStarted != null && !this.isInteractionDisabled()) {
			this.onZoomStarted(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnFlingStarted: function (point, velocityX, velocityY) {
		if (this.onFlingStarted != null && !this.isInteractionDisabled()) {
			return this.onFlingStarted(point, velocityX, velocityY);
		}
		return true;
	}
	,
	raiseOnPinchDelta: function (point, scale) {
		if (this.onPinchDelta != null && !this.isInteractionDisabled()) {
			this.onPinchDelta(point, scale);
		}
	}
	,
	raiseOnZoomDelta: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomDelta != null && !this.isInteractionDisabled()) {
			this.onZoomDelta(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnZoomCompleted: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomCompleted != null && !this.isInteractionDisabled()) {
			this.onZoomCompleted(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnGestureCompleted: function (point, scale) {
		if (this.onGestureCompleted != null && !this.isInteractionDisabled()) {
			this.onGestureCompleted(point, scale);
		}
	}
	,
	raiseOnContactStarted: function (point, isFinger) {
		if (this.onContactStarted != null && !this.isInteractionDisabled()) {
			this.onContactStarted(point, isFinger);
		}
	}
	,
	raiseOnDragStarted: function (point) {
		if (this.onDragStarted != null && !this.isInteractionDisabled()) {
			this.onDragStarted(point);
		}
	}
	,
	raiseOnContactMoved: function (point, isFinger) {
		if (this.onContactMoved != null && !this.isInteractionDisabled()) {
			this.onContactMoved(point, isFinger);
		}
	}
	,
	raiseOnDragDelta: function (point) {
		if (this.onDragDelta != null && !this.isInteractionDisabled()) {
			this.onDragDelta(point);
		}
	}
	,
	raiseOnContactCompleted: function (point, isFinger) {
		if (this.onContactCompleted != null && !this.isInteractionDisabled()) {
			this.onContactCompleted(point, isFinger);
		}
	}
	,
	raiseOnDragCompleted: function (point) {
		if (this.onDragCompleted != null && !this.isInteractionDisabled()) {
			this.onDragCompleted(point);
		}
	}
	,
	raiseOnMouseLeave: function (point) {
		if (this.onMouseLeave != null && !this.isInteractionDisabled()) {
			this.onMouseLeave(point);
		}
	}
	,
	raiseOnMouseOver: function (point, onMouseMove, isFinger) {
		if (this.onMouseOver != null && !this.isInteractionDisabled()) {
			this.onMouseOver(point, onMouseMove, isFinger);
		}
	}
	,
	raiseOnMouseEnter: function (point) {
		if (this.onMouseEnter != null && !this.isInteractionDisabled()) {
			this.onMouseEnter(point);
		}
	}
	,
	raiseOnMouseDown: function (point) {
		if (this.onMouseDown != null && !this.isInteractionDisabled()) {
			this.onMouseDown(point);
		}
	}
	,
	raiseOnMouseUp: function (point) {
		if (this.onMouseUp != null && !this.isInteractionDisabled()) {
			this.onMouseUp(point);
		}
	}
	,
	raiseOnKeyDown: function (key) {
		if (this.onKeyDown != null && !this.isInteractionDisabled()) {
			return this.onKeyDown(key);
		}
		return false;
	}
	,
	raiseOnKeyUp: function (key) {
		if (this.onKeyUp != null && !this.isInteractionDisabled()) {
			return this.onKeyUp(key);
		}
		return false;
	}
	,
	raiseOnDoubleTap: function (point) {
		if (this.onDoubleTap != null && !this.isInteractionDisabled()) {
			this.onDoubleTap(point);
		}
	}
	,
	raiseOnHold: function (point) {
		if (this.onHold != null && !this.isInteractionDisabled()) {
			this.onHold(point);
		}
	}
	,
	getSourceOffsets: function () {
	}
	,
	__isInteractionDisabled: false,
	isInteractionDisabled: function (value) {
		if (arguments.length === 1) {
			this.__isInteractionDisabled = value;
			this.onIsInteractionDisabledChanged();
			return value;
		} else {
			return this.__isInteractionDisabled;
		}
	}
	,
	_deferredTouchStartMode: false,
	deferredTouchStartMode: function (value) {
		if (arguments.length === 1) {
			this._deferredTouchStartMode = value;
			return value;
		} else {
			return this._deferredTouchStartMode;
		}
	}
	,
	_shouldInteractForDirection: null,
	shouldInteractForDirection: function (value) {
		if (arguments.length === 1) {
			this._shouldInteractForDirection = value;
			return value;
		} else {
			return this._shouldInteractForDirection;
		}
	}
	,
	onIsInteractionDisabledChanged: function () {
	}
	,
	bindToSource: function (source, sourceId) {
	}
	,
	unbindFromSource: function (source, sourceId) {
	}
	,
	$type: new $.ig.Type('EventProxy', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BaseDOMEventProxy', 'EventProxy', {
	init: function () {
		this.__touchCaptureEnabled = false;
		this.__mouseX = 0;
		this.__mouseY = 0;
		this.__containerMouseX = 0;
		this.__containerMouseY = 0;
		this.__numTouches = 0;
		this.__suppressMouseEvents = false;
		this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		this.__touchStartDeferred = false;
		this.__mouseCaptured = false;
		$.ig.EventProxy.prototype.init.call(this);
	},
	_eventSource: null,
	eventSource: function (value) {
		if (arguments.length === 1) {
			this._eventSource = value;
			return value;
		} else {
			return this._eventSource;
		}
	}
	,
	clone: function () {
		var ret = new $.ig.DOMEventProxy(this.eventSource());
		ret.deferredTouchStartMode(this.deferredTouchStartMode());
		ret.shouldInteractForDirection(this.shouldInteractForDirection());
		return ret;
	}
	,
	preventDefault: function (e) {
		if (!this.isInteractionDisabled()) {
			if (this.isCancelable(e)) {
				e.preventDefault();
			}
		}
	}
	,
	isCancelable: function (e_) {
		return e_.originalEvent.cancelable || e_.originalEvent.cancelable == undefined;
	}
	,
	__touchCaptureEnabled: false,
	enableTouchCapture: function () {
		var source_ = this.eventSource()[0];
		$.ig.BaseDOMEventProxy.prototype.grabTouches(source_);
	}
	,
	grabTouches: function (source_) {
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			source_.style.touchAction = 'none';
		} else {
			source_.style.msTouchAction = 'none';
		}
		source_.style.msUserSelect = 'none';
		source_.style.webkitTouchCallout = 'none';
		source_.style.webkitUserSelect = 'none';
		source_.style.khtmlUserSelect = 'none';
		source_.style.mozUserSelect = 'none';
		source_.style.userSelect = 'none';
	}
	,
	deferTouches: function (source_) {
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			source_.style.touchAction = 'auto';
		} else {
			source_.style.msTouchAction = 'auto';
		}
		source_.style.msUserSelect = 'auto';
		source_.style.webkitTouchCallout = 'none';
		source_.style.webkitUserSelect = 'auto';
		source_.style.khtmlUserSelect = 'auto';
		source_.style.mozUserSelect = 'auto';
		source_.style.userSelect = 'auto';
	}
	,
	disableTouchCapture: function () {
		var source_ = this.eventSource()[0];
		$.ig.BaseDOMEventProxy.prototype.deferTouches(source_);
	}
	,
	onIsInteractionDisabledChanged: function () {
		$.ig.EventProxy.prototype.onIsInteractionDisabledChanged.call(this);
		if (!this.isInteractionDisabled()) {
			this.enableTouchCapture();
		} else {
			this.disableTouchCapture();
		}
	}
	,
	getTridentVersion: function () {
		var ver_ = -1;
		var matchIE_ = /Trident\/([\d.]+)/;
		if (matchIE_.exec(navigator.userAgent) != null) {
			ver_ = parseFloat(RegExp.$1);
		}
		return ver_;
	}
	,
	getEdgeVersion: function () {
		var ver_ = -1;
		var matchIE_ = /Edge\/([\d.]+)/;
		if (matchIE_.exec(navigator.userAgent) != null) {
			ver_ = parseFloat(RegExp.$1);
		}
		return ver_;
	}
	,
	fixEvent: function (e_) {
		var ieHack = $.ig.BaseDOMEventProxy.prototype.tridentVersion >= 6;
		var oe_ = e_.originalEvent;
		if (((typeof e_.pageX == 'undefined') || ieHack) && oe_.clientX != null) {
			var od_ = e_.target.ownerDocument;
			var ed_ = od_ ? od_ : document;
			var doc_ = ed_.documentElement;
			var body_ = ed_.body;
			var clientX_ = oe_.clientX;
			var clientY_ = oe_.clientY;
			var scrollLeft_ = doc_ && doc_.scrollLeft || body_ && body_.scrollLeft || 0;
			var scrollTop_ = doc_ && doc_.scrollTop || body_ && body_.scrollTop || 0;
			var clientLeft_ = doc_ && doc_.clientLeft || body_ && body_.clientLeft || 0;
			var clientTop_ = doc_ && doc_.clientTop || body_ && body_.clientTop || 0;
			e_.pageX = clientX_ + (scrollLeft_ - clientLeft_);
			e_.pageY = clientY_ + (scrollTop_ - clientTop_);
		}
		if (!e_.pageX) {
			e_.pageX = e_.originalEvent.pageX;
		}
		if (!e_.pageY) {
			e_.pageY = e_.originalEvent.pageY;
		}
	}
	,
	getOffset: function (source) {
		var ieHack = $.ig.BaseDOMEventProxy.prototype.tridentVersion >= 6;
		var source_ = source[0];
		var d_ = source_.ownerDocument;
		var doc_ = d_ ? d_.documentElement : null;
		var z_ = doc_ ? doc_.msContentZoomFactor : null;
		var body_ = doc_.body;
		if (z_ && z_ > 1 || ieHack) {
			var rect_ = source_.getBoundingClientRect();
			var x_ = rect_.left;
			var y_ = rect_.top;
			var scrollLeft_ = doc_ && doc_.scrollLeft || body_ && body_.scrollLeft || 0;
			var scrollTop_ = doc_ && doc_.scrollTop || body_ && body_.scrollTop || 0;
			var clientLeft_ = doc_ && doc_.clientLeft || body_ && body_.clientLeft || 0;
			var clientTop_ = doc_ && doc_.clientTop || body_ && body_.clientTop || 0;
			var left = x_ + scrollLeft_ - clientLeft_;
			var top = y_ + scrollTop_ - clientTop_;
			return new $.ig.Rect(0, left, top, 0, 0);
		} else {
			var offset = source.offset();
			return new $.ig.Rect(0, offset.left, offset.top, 0, 0);
		}
	}
	,
	__mouseX: 0,
	__mouseY: 0,
	__containerMouseX: 0,
	__containerMouseY: 0,
	__numTouches: 0,
	_mousePoint: null,
	mousePoint: function (value) {
		if (arguments.length === 1) {
			this._mousePoint = value;
			return value;
		} else {
			return this._mousePoint;
		}
	}
	,
	_containerMousePoint: null,
	containerMousePoint: function (value) {
		if (arguments.length === 1) {
			this._containerMousePoint = value;
			return value;
		} else {
			return this._containerMousePoint;
		}
	}
	,
	numTouches: function (value) {
		if (arguments.length === 1) {
			this.__numTouches = value;
			return value;
		} else {
			return this.__numTouches;
		}
	}
	,
	updateMousePosition: function (e) {
		this.originalEvent(e);
		$.ig.BaseDOMEventProxy.prototype.fixEvent(e);
		var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
		this.__mouseX = e.pageX - offset.left();
		this.__mouseY = e.pageY - offset.top();
		this.__containerMouseX = this.__mouseX - this.viewport().left();
		this.__containerMouseY = this.__mouseY - this.viewport().top();
		this.mousePoint({ __x: this.__mouseX, __y: this.__mouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.containerMousePoint({ __x: this.__containerMouseX, __y: this.__containerMouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	updateTouchPosition: function (e_) {
		this.originalEvent(e_);
		$.ig.BaseDOMEventProxy.prototype.fixEvent(e_);
		if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			var pageX = e_.pageX;
			var pageY = e_.pageY;
			var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
			this.__mouseX = pageX - offset.left();
			this.__mouseY = pageY - offset.top();
		} else {
			this.__numTouches = 0;
			if (!e_.originalEvent.targetTouches || e_.originalEvent.targetTouches.length < 1) { return; };
			this.__numTouches = e_.originalEvent.targetTouches.length;
			var pageX1 = e_.originalEvent.targetTouches[0].pageX;
			var pageY1 = e_.originalEvent.targetTouches[0].pageY;
			var offset1 = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
			this.__mouseX = pageX1 - offset1.left();
			this.__mouseY = pageY1 - offset1.top();
		}
		this.__containerMouseX = this.__mouseX - this.viewport().left();
		this.__containerMouseY = this.__mouseY - this.viewport().top();
		this.mousePoint({ __x: this.__mouseX, __y: this.__mouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.containerMousePoint({ __x: this.__containerMouseX, __y: this.__containerMouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	canvasMouseScroll: function (e_) {
		this.updateModifiers(e_);
		if (this.shouldInteract()(this.mousePoint())) {
			var delta_ = 0;
			if (e_.wheelDelta) { delta_ = e_.wheelDelta/120; };
			if (e_.originalEvent && e_.originalEvent.wheelDelta) { delta_ = e_.originalEvent.wheelDelta/120; };
			if (e_.detail) { delta_ = -e_.detail/3; };
			if (e_.originalEvent && e_.originalEvent.detail) { delta_ = -e_.originalEvent.detail/3; };
			delta_ = delta_ / 10;
			var handled = this.raiseOnMouseWheel(this.containerMousePoint(), delta_);
			if (handled) {
				this.preventDefault(e_);
			}
		}
	}
	,
	__suppressMouseEvents: false,
	__suppressId: 0,
	endMouseSuppress: function () {
		if (this.__suppressMouseEvents) {
			if (this.__suppressId == $.ig.BaseDOMEventProxy.prototype.nullTimer) {
				this.__suppressId = window.setTimeout(this.doEndSuppress.runOn(this), 500);
			} else {
				window.clearTimeout(this.__suppressId);
				this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
				this.__suppressId = window.setTimeout(this.doEndSuppress.runOn(this), 500);
			}
		}
	}
	,
	beginMouseSuppress: function () {
		this.__suppressMouseEvents = true;
		if (this.__suppressId != $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			window.clearTimeout(this.__suppressId);
			this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		}
	}
	,
	doEndSuppress: function () {
		this.__suppressMouseEvents = false;
	}
	,
	_mouseIsOver: false,
	mouseIsOver: function (value) {
		if (arguments.length === 1) {
			this._mouseIsOver = value;
			return value;
		} else {
			return this._mouseIsOver;
		}
	}
	,
	__touchStartDeferred: false,
	touchStartDeferred: function (value) {
		if (arguments.length === 1) {
			this.__touchStartDeferred = value;
			return value;
		} else {
			return this.__touchStartDeferred;
		}
	}
	,
	canvasMouseLeave: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateMousePosition(e);
		if (this.mouseIsOver()) {
			this.mouseIsOver(false);
			this.raiseOnMouseLeave(this.containerMousePoint());
		}
	}
	,
	canvasMouseMove: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateMousePosition(e);
		this.updateModifiers(e);
		var me = new $.ig.MouseEventArgs();
		me.position(this.mousePoint());
		if (this.shouldInteract()(this.mousePoint())) {
			if (!this.mouseIsOver()) {
				this.mouseIsOver(true);
				this.raiseOnMouseEnter(this.mousePoint());
			}
			this.raiseOnMouseOver(this.mousePoint(), true, false);
			this.raiseOnContactMoved(this.containerMousePoint(), false);
		} else if (this.mouseIsOver()) {
			this.canvasMouseLeave(e);
		}
	}
	,
	__mouseCaptured: false,
	canvasMouseDown: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.eventSource().focus();
		this.updateMousePosition(e);
		this.updateModifiers(e);
		if (this.shouldInteract()(this.mousePoint())) {
			this.__mouseCaptured = true;
			this.raiseOnMouseDown(this.mousePoint());
			this.raiseOnContactStarted(this.containerMousePoint(), false);
			this.preventDefault(e);
		}
	}
	,
	windowMouseUp: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		if (this.__mouseCaptured) {
			this.canvasMouseUp(e);
		}
	}
	,
	canvasMouseUp: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateModifiers(e);
		this.__mouseCaptured = false;
		this.raiseOnMouseUp(this.mousePoint());
		this.raiseOnContactCompleted(this.containerMousePoint(), false);
		this.preventDefault(e);
	}
	,
	canvasKeyDown: function (e) {
		this.canvasKeyEvent(e, true);
	}
	,
	canvasKeyUp: function (e) {
		this.canvasKeyEvent(e, false);
	}
	,
	getKey: function (e) {
		var downKey = $.ig.Key.prototype.none;
		switch (e.which) {
			case 33:
				downKey = $.ig.Key.prototype.pageUp;
				break;
			case 34:
				downKey = $.ig.Key.prototype.pageDown;
				break;
			case 36:
				downKey = $.ig.Key.prototype.home;
				break;
			case 37:
				downKey = $.ig.Key.prototype.left;
				break;
			case 38:
				downKey = $.ig.Key.prototype.up;
				break;
			case 39:
				downKey = $.ig.Key.prototype.right;
				break;
			case 40:
				downKey = $.ig.Key.prototype.down;
				break;
			case 9:
				downKey = $.ig.Key.prototype.tab;
				break;
			case 32:
				downKey = $.ig.Key.prototype.space;
				break;
			case 13:
				downKey = $.ig.Key.prototype.enter;
				break;
			case 27:
				downKey = $.ig.Key.prototype.escape;
				break;
			case 16:
				downKey = $.ig.Key.prototype.shift;
				break;
			case 17:
				downKey = $.ig.Key.prototype.ctrl;
				break;
			case 18:
				downKey = $.ig.Key.prototype.alt;
				break;
			default:
				if (112 <= e.which && e.which <= 123) {
					downKey = ($.ig.Key.prototype.f1 + e.which - 112);
				}
				break;
		}
		return downKey;
	}
	,
	canvasKeyEvent: function (e, isDown) {
		var downKey = $.ig.BaseDOMEventProxy.prototype.getKey(e);
		var send = downKey != $.ig.Key.prototype.none;
		this.updateModifiers(e);
		if (send && this.shouldInteract()(this.mousePoint())) {
			var handled = false;
			if (isDown) {
				handled = this.raiseOnKeyDown(downKey);
			} else {
				handled = this.raiseOnKeyUp(downKey);
			}
			if (handled) {
				this.preventDefault(e);
			}
		}
	}
	,
	updateModifiers: function (e) {
		this.originalEvent(e);
		this.currentModifiers($.ig.ModifierKeys.prototype.none);
		if (e.shiftKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.shift);
		}
		if (e.altKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.alt);
		}
		if (e.ctrlKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.control);
		}
		this.rightButton(e.button == 2);
	}
	,
	getSourceOffsets: function () {
		var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
		var x = offset.left();
		var y = offset.top();
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	$type: new $.ig.Type('BaseDOMEventProxy', $.ig.EventProxy.prototype.$type)
}, true);

$.ig.util.defType('DOMEventProxy', 'BaseDOMEventProxy', {
	__proxyID: 0,
	__eventNS: null,
	__pinching: false,
	_mGesture: null,
	mGesture: function (value) {
		if (arguments.length === 1) {
			this._mGesture = value;
			return value;
		} else {
			return this._mGesture;
		}
	}
	,
	init: function (DOMEventSource) {
		this.__proxyID = 0;
		this.__eventNS = "";
		this.__pinching = false;
		this.__lastScale = 1;
		this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		this.__tapArea = null;
		this.__holdStartX = 0;
		this.__holdStartY = 0;
		this.__holdCancelDistance = 5;
		this.__dragging = false;
		this.__dragStart = null;
		$.ig.BaseDOMEventProxy.prototype.init.call(this);
		this.shouldInteractForDirection(function (p) { return true; });
		this.deferredTouchStartMode(false);
		$.ig.DOMEventProxy.prototype.__proxyCount++;
		this.__proxyID = $.ig.DOMEventProxy.prototype.__proxyCount;
		this.__eventNS = ".DOMProxy" + this.__proxyID.toString();
		this.currentModifiers($.ig.ModifierKeys.prototype.none);
		this.eventSource(DOMEventSource);
		$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = false;
		try {
			$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = window.navigator.msPointerEnabled && MSGesture !== undefined;
		}
		catch (e) {
		}
		$.ig.BaseDOMEventProxy.prototype.pointerEnabled = false;
		try {
			$.ig.BaseDOMEventProxy.prototype.pointerEnabled = window.navigator.pointerEnabled;
		}
		catch (e1) {
		}
		$.ig.BaseDOMEventProxy.prototype.tridentVersion = this.getTridentVersion();
		$.ig.BaseDOMEventProxy.prototype.edgeVersion = this.getEdgeVersion();
		this.bindToSource(this.eventSource(), "");
		this.shouldInteract(function (p) { return true; });
	},
	bindToSource: function (objSource, sourceID) {
		var source = objSource;
		var ns = this.__eventNS + sourceID;
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			source.bind("mousemove" + ns, this.canvasMouseMove.runOn(this));
			source.bind("mouseleave" + ns, this.canvasMouseLeave.runOn(this));
			source.bind("mousedown" + ns, this.canvasMouseDown.runOn(this));
			source.bind("mouseup" + ns, this.canvasMouseUp.runOn(this));
			$(window).bind("mouseup" + ns, this.windowMouseUp.runOn(this));
		}
		source.bind("keydown" + ns, this.canvasKeyDown.runOn(this));
		source.bind("keyup" + ns, this.canvasKeyUp.runOn(this));
		var source_ = this.eventSource()[0];
		if (!this.isInteractionDisabled()) {
			$.ig.BaseDOMEventProxy.prototype.grabTouches(source_);
		}
		if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			var container = this.eventSource()[0];
			if (window.MSGesture !== undefined) {
				var gesture = new MSGesture();
				gesture.target = container;
				this.mGesture(gesture);
				source.bind("MSGestureStart" + ns, this.canvasGestureStart.runOn(this));
				source.bind("MSGestureChange" + ns, this.canvasGestureChange.runOn(this));
				source.bind("MSGestureEnd" + ns, this.canvasGestureEnd.runOn(this));
			}
			if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
				source.bind("pointerdown" + ns, this.canvasMSPointerDown.runOn(this));
				source.bind("pointerup" + ns, this.canvasMSPointerUp.runOn(this));
				source.bind("pointercancel" + ns, this.canvasMSPointerCancel.runOn(this));
				source.bind("pointermove" + ns, this.canvasMSPointerMove.runOn(this));
				source.bind("pointerout" + ns, this.canvasMSPointerOut.runOn(this));
				source.bind("lostpointercapture" + ns, this.canvasMSLostPointerCapture.runOn(this));
			} else {
				source.bind("MSPointerDown" + ns, this.canvasMSPointerDown.runOn(this));
				source.bind("MSPointerUp" + ns, this.canvasMSPointerUp.runOn(this));
				source.bind("MSPointerCancel" + ns, this.canvasMSPointerCancel.runOn(this));
				source.bind("MSPointerMove" + ns, this.canvasMSPointerMove.runOn(this));
				source.bind("MSPointerOut" + ns, this.canvasMSPointerOut.runOn(this));
				source.bind("MSLostPointerCapture" + ns, this.canvasMSLostPointerCapture.runOn(this));
			}
		}
		source.bind("DOMMouseScroll" + ns, this.canvasMouseScroll.runOn(this));
		source.bind("mousewheel" + ns, this.canvasMouseScroll.runOn(this));
		source.bind("gesturestart" + ns, this.canvasGestureStart.runOn(this));
		source.bind("gesturechange" + ns, this.canvasGestureChange.runOn(this));
		source.bind("gestureend" + ns, this.canvasGestureEnd.runOn(this));
		source.bind("touchstart" + ns, this.canvasTouchStart.runOn(this));
		source.bind("touchmove" + ns, this.canvasTouchMove.runOn(this));
		source.bind("touchend" + ns, this.canvasTouchEnd.runOn(this));
	}
	,
	__lastScale: 0,
	canvasMSPointerUp: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			this.numTouches(this.numTouches() - 1);
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		} else {
			if (isFinger) {
				this.canvasTouchEnd(e_);
			} else {
				this.canvasMouseUp(e_);
			}
		}
	}
	,
	canvasMSLostPointerCapture: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value - 1); return $value; }(this));
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasMSPointerCancel: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value - 1); return $value; }(this));
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasMSPointerDown: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.mGesture() != null && isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value + 1); return $value; }(this));
			this.mGesture().addPointer(e_.originalEvent.pointerId);
		}
		var eventSource_ = this.eventSource()[0];
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			eventSource_.setPointerCapture(e_.originalEvent.pointerId);
		} else {
			eventSource_.msSetPointerCapture(e_.originalEvent.pointerId);
		}
		if (this.numTouches() > 1 && !this.__pinching && isFinger) {
			this.__pinching = true;
			this.updateTouchPosition(e_);
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchStarted(this.containerMousePoint(), scale);
		} else {
			if (isFinger) {
				this.canvasTouchStart(e_);
			} else {
				this.canvasMouseDown(e_);
			}
		}
	}
	,
	isFinger: function (e_) {
		var pointerEvent_ = e_.originalEvent;
		var isFinger = false;
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			isFinger = pointerEvent_.pointerType == 'touch';
		} else {
			isFinger = pointerEvent_.pointerType == pointerEvent_.MSPOINTER_TYPE_TOUCH;
		}
		return isFinger;
	}
	,
	canvasMSPointerMove: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.__pinching) {
			return;
		}
		if (isFinger) {
			this.canvasTouchMove(e_);
		} else {
			this.canvasMouseMove(e_);
		}
	}
	,
	canvasMSPointerOut: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.__pinching) {
			return;
		}
		if (isFinger) {
		} else {
			this.canvasMouseLeave(e_);
		}
	}
	,
	canvasGestureStart: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		this.dragStopHoldTimer();
		if (this.shouldInteract()(this.mousePoint())) {
			this.preventDefault(e_);
			if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
				this.__lastScale = 1;
			}
			if (($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) && this.numTouches() < 2) {
				return;
			}
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchStarted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasGestureChange: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		if (this.shouldInteract()(this.mousePoint())) {
			this.preventDefault(e_);
			if (($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) && this.numTouches() < 2) {
				return;
			}
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchDelta(this.containerMousePoint(), scale);
		}
	}
	,
	canvasGestureEnd: function (e_) {
		this.preventDefault(e_);
		var scale = e_.originalEvent.scale;
		this.mouseIsOver(false);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled) || this.__pinching) {
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	__touchContainerStart: null,
	canvasTouchStart: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		this.__touchContainerStart = this.containerMousePoint();
		if (this.shouldInteract()(this.mousePoint())) {
			if (!this.deferredTouchStartMode()) {
				this.preventDefault(e_);
			} else {
				this.touchStartDeferred(true);
			}
			this.raiseOnMouseOver(this.mousePoint(), false, true);
			this.raiseOnMouseDown(this.mousePoint());
			this.raiseOnContactStarted(this.containerMousePoint(), true);
			this.startHoldTimer();
		}
	}
	,
	__holdId: 0,
	__tapArea: null,
	__holdStartX: 0,
	__holdStartY: 0,
	__holdCancelDistance: 0,
	startHoldTimer: function () {
		if (this.__holdId == $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			this.__holdStartX = this.mousePoint().__x;
			this.__holdStartY = this.mousePoint().__y;
			this.__holdId = window.setTimeout(this.onHoldTimer.runOn(this), 1500);
		}
	}
	,
	dragStopHoldTimer: function () {
		if (Math.abs(this.__holdStartX - this.mousePoint().__x) > this.__holdCancelDistance || Math.abs(this.__holdStartY - this.mousePoint().__y) > this.__holdCancelDistance) {
			this.stopHoldTimer();
		}
	}
	,
	stopHoldTimer: function () {
		if (this.__holdId != $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			window.clearTimeout(this.__holdId);
			this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		}
	}
	,
	onHoldTimer: function () {
		this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		if (this.touchStartDeferred()) {
			this.touchStartDeferred(false);
			this.beginMouseSuppress();
		}
		this.raiseOnHold(this.containerMousePoint());
	}
	,
	__dragging: false,
	__dragStart: null,
	canvasTouchMove: function (e) {
		this.mouseIsOver(true);
		this.updateModifiers(e);
		this.updateTouchPosition(e);
		if (this.touchStartDeferred()) {
			if (this.shouldInteract()(this.mousePoint()) && this.shouldAllowTouchDrag()) {
				this.touchStartDeferred(false);
				this.beginMouseSuppress();
			}
		}
		this.dragStopHoldTimer();
		this.stopTapTimer();
		if (this.shouldInteract()(this.mousePoint()) && this.numTouches() == 1 && !this.touchStartDeferred()) {
			this.preventDefault(e);
			if (!this.__dragging) {
				this.__dragging = true;
				this.__dragStart = this.containerMousePoint();
				this.raiseOnDragStarted(this.__dragStart);
			} else {
				this.raiseOnMouseOver(this.mousePoint(), true, true);
				this.raiseOnContactMoved(this.containerMousePoint(), true);
				this.raiseOnDragDelta(this.containerMousePoint());
			}
		}
	}
	,
	shouldAllowTouchDrag: function () {
		var direction = { __x: this.__touchContainerStart.__x - this.containerMousePoint().__x, __y: this.__touchContainerStart.__y - this.containerMousePoint().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (Math.abs(direction.__x) < 2 && Math.abs(direction.__y) < 2) {
			return false;
		}
		if (Math.abs(direction.__x) > Math.abs(direction.__y)) {
			direction.__y = 0;
		}
		if (Math.abs(direction.__y) > Math.abs(direction.__x)) {
			direction.__x = 0;
		}
		return this.shouldInteractForDirection()(direction);
	}
	,
	canvasTouchEnd: function (e) {
		this.mouseIsOver(false);
		this.updateModifiers(e);
		this.updateTouchPosition(e);
		this.stopHoldTimer();
		if (this.touchStartDeferred()) {
			this.touchStartDeferred(false);
			this.beginMouseSuppress();
		}
		this.preventDefault(e);
		this.raiseOnMouseOver(this.mousePoint(), false, true);
		this.raiseOnMouseUp(this.mousePoint());
		if (this.numTouches() == 0) {
			this.raiseDoubleTap(this.mousePoint());
			this.endMouseSuppress();
		}
		if (this.__dragging && this.numTouches() == 0) {
			this.__dragging = false;
			this.__dragStart = null;
			this.raiseOnDragCompleted(this.containerMousePoint());
			this.endMouseSuppress();
		}
		this.raiseOnContactCompleted(this.containerMousePoint(), true);
	}
	,
	raiseDoubleTap: function (pt) {
		if ($.ig.Rect.prototype.l_op_Equality(this.__tapArea, null)) {
			this.__tapArea = new $.ig.Rect(0, pt.__x - 50, pt.__y - 50, 100, 100);
			window.setTimeout(this.stopTapTimer.runOn(this), 500);
		} else {
			if (pt.__x >= this.__tapArea.x() && pt.__x <= this.__tapArea.right() && pt.__y >= this.__tapArea.y() && pt.__y <= this.__tapArea.bottom()) {
				this.stopTapTimer();
				this.raiseOnDoubleTap(pt);
			}
		}
	}
	,
	stopTapTimer: function () {
		this.__tapArea = null;
	}
	,
	unbindFromSource: function (objSource, sourceID) {
		var source = objSource;
		var ns = this.__eventNS + sourceID;
		source.unbind(ns);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			$(window).unbind(ns);
		}
	}
	,
	destroy: function () {
		if (this.eventSource() == null) {
			return;
		}
		this.eventSource().unbind(this.__eventNS);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			$(window).unbind(this.__eventNS);
		}
		this.eventSource(null);
	}
	,
	$type: new $.ig.Type('DOMEventProxy', $.ig.BaseDOMEventProxy.prototype.$type)
}, true);

$.ig.util.defType('IFastItemColumnInternal', 'Object', {
	$type: new $.ig.Type('IFastItemColumnInternal', null)
}, true);

$.ig.util.defType('IFastItemColumnPropertyName', 'Object', {
	$type: new $.ig.Type('IFastItemColumnPropertyName', null)
}, true);

$.ig.util.defType('IFastItemColumn$1', 'Object', {
	$type: new $.ig.Type('IFastItemColumn$1', null, [$.ig.IList$1.prototype.$type.specialize(0), $.ig.IFastItemColumnPropertyName.prototype.$type])
}, true);

$.ig.util.defType('FastItemColumn', 'Object', {
	__coerceValue: null,
	__expectFunctions: false,
	init: function (fastItemsSource, propertyName, coerceValue, expectFunctions) {
		this.__coerceValue = null;
		this.__expectFunctions = false;
		this.__propertyName = null;
		$.ig.Object.prototype.init.call(this);
		this.__coerceValue = coerceValue;
		this.__expectFunctions = expectFunctions;
		this.propertyName(propertyName);
		this.fastItemsSource(fastItemsSource);
	},
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			this.reset();
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	_fastItemsSource: null,
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	minimum: function (value) {
		if (arguments.length === 1) {
			this._minimum = value;
			return value;
		} else {
			if ($.ig.util.isNaN(this._minimum) && this.values() != null) {
				this._minimum = Number.POSITIVE_INFINITY;
				var en = this.values().getEnumerator();
				while (en.moveNext()) {
					var value = en.current();
					if (!$.ig.util.isNaN(value)) {
						this._minimum = Math.min(this._minimum, value);
					}
				}
			}
			return this._minimum;
		}
	}
	,
	_minimum: 0,
	maximum: function (value) {
		if (arguments.length === 1) {
			this._maximum = value;
			return value;
		} else {
			if ($.ig.util.isNaN(this._maximum) && this.values() != null) {
				this._maximum = Number.NEGATIVE_INFINITY;
				var en = this.values().getEnumerator();
				while (en.moveNext()) {
					var value = en.current();
					if (!$.ig.util.isNaN(value)) {
						this._maximum = Math.max(this._maximum, value);
					}
				}
			}
			return this._maximum;
		}
	}
	,
	_maximum: 0,
	item: function (index, value) {
		if (arguments.length === 2) {
			this.values().__inner[index] = value;
			return value;
		} else {
			return this.values().__inner[index];
		}
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	contains: function (item) {
		return this.values().contains(item);
	}
	,
	copyTo: function (array, arrayIndex) {
		this.values().copyTo(array, arrayIndex);
	}
	,
	count: function () {
		return this.values().count();
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	indexOf: function (item) {
		return this.values().indexOf(item);
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
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
	reset: function () {
		this.values(null);
		this.minimum(NaN);
		this.maximum(NaN);
		return this.fastItemsSource() != null ? this.insertRange(0, this.fastItemsSource().count()) : true;
	}
	,
	insertRange: function (position, count) {
		var newValues = new Array(count);
		var source_ = this._fastItemsSource.asArray();
		var item_;
		var minimum = this.minimum();
		var maximum = this.maximum();
		var minimumIsNaN = $.ig.util.isNaN(this.minimum());
		var maximumIsNaN = $.ig.util.isNaN(this.maximum());
		var newCount = 0;
		var newValue;
		var coerce = this.__coerceValue;
		var sourceItem_ = null;
		if (this.__coerceValue != null || this.__expectFunctions) {
			for (var i_ = position; i_ < position + count; ++i_) {
				sourceItem_ = source_[i_];
				if (sourceItem_ == null) {
					item_ = NaN;
				} else {
					item_ = sourceItem_[this.__propertyName];
				}
				if (this.__expectFunctions) {
					if (typeof(item_) == 'function') {
						item_ = item_();
					}
				}
				if (coerce != null) {
					item_ = coerce(item_);
				}
				newValue = item_ == null ? NaN : item_;
				var valIsNaN = (newValue != newValue);
				if (minimumIsNaN || newValue < minimum) {
					minimum = newValue;
					minimumIsNaN = valIsNaN;
				}
				if (maximumIsNaN || newValue > maximum) {
					maximum = newValue;
					maximumIsNaN = valIsNaN;
				}
				newValues[newCount] = newValue;
				newCount++;
			}
		} else {
			for (var i_ = position; i_ < position + count; ++i_) {
				sourceItem_ = source_[i_];
				if (sourceItem_ == null) {
					item_ = NaN;
				} else {
					item_ = sourceItem_[this.__propertyName];
				}
				newValue = item_ == null ? NaN : item_;
				var valIsNaN1 = (newValue != newValue);
				if (minimumIsNaN || newValue < minimum) {
					minimum = newValue;
					minimumIsNaN = valIsNaN1;
				}
				if (maximumIsNaN || newValue > maximum) {
					maximum = newValue;
					maximumIsNaN = valIsNaN1;
				}
				newValues[newCount] = newValue;
				newCount++;
			}
		}
		this.minimum(minimum);
		this.maximum(maximum);
		if (this.values() == null) {
			this.values(new $.ig.List$1(Number, 1, newValues));
		} else {
			this.values().insertRange(position, newValues);
		}
		return true;
	}
	,
	removeRange: function (position, count) {
		for (var i = position; i < position + count && !$.ig.util.isNaN(this.minimum()) && !$.ig.util.isNaN(this.maximum()); ++i) {
			if (this.item(i) == this.minimum()) {
				this.minimum(NaN);
			}
			if (this.item(i) == this.maximum()) {
				this.maximum(NaN);
			}
		}
		this.values().removeRange(position, count);
		return true;
	}
	,
	replaceMinMax: function (oldValue, newValue) {
		if ($.ig.util.isNaN(oldValue)) {
			if (!$.ig.util.isNaN(newValue)) {
				if (!$.ig.util.isNaN(this.minimum())) {
					this.minimum(Math.min(newValue, this.minimum()));
				}
				if (!$.ig.util.isNaN(this.maximum())) {
					this.maximum(Math.max(newValue, this.maximum()));
				}
			}
			return;
		}
		if ($.ig.util.isNaN(newValue)) {
			this.minimum(!$.ig.util.isNaN(this.minimum()) && oldValue == this.minimum() ? NaN : this.minimum());
			this.maximum(!$.ig.util.isNaN(this.maximum()) && oldValue == this.maximum() ? NaN : this.maximum());
			return;
		}
		if (!$.ig.util.isNaN(this.minimum())) {
			if (oldValue == this.minimum() && newValue > this.minimum()) {
				this.minimum(NaN);
			} else {
				this.minimum(Math.min(newValue, this.minimum()));
			}
		}
		if (!$.ig.util.isNaN(this.maximum())) {
			if (oldValue == this.maximum() && newValue < this.maximum()) {
				this.maximum(NaN);
			} else {
				this.maximum(Math.max(newValue, this.maximum()));
			}
		}
	}
	,
	replaceRange: function (position, count) {
		var ret = false;
		for (var i = 0; i < count; ++i) {
			var oldValue = this.values().__inner[position + i];
			var newValue = this.toDouble(this.fastItemsSource().item(position + i));
			if (oldValue != newValue) {
				this.values().__inner[position + i] = newValue;
				ret = true;
				this.replaceMinMax(oldValue, newValue);
			}
		}
		return ret;
	}
	,
	_fastReflectionHelper: null,
	toDouble: function (item) {
		if (item == null) {
			return NaN;
		}
		var from_ = item;
		item = from_[this.__propertyName];
		if (this.__expectFunctions) {
			from_ = item;
			if (typeof(from_) == 'function') {
				item = from_();
			}
		}
		if (this.__coerceValue != null) {
			item = this.__coerceValue(item);
		}
		if (item == null) {
			return NaN;
		}
		return item;
		if (typeof item === 'number') {
			return item;
		}
	}
	,
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	quickSort: function (list, comparison) {
		$.ig.FastItemColumn.prototype.quickSort1(list, 0, list.count() - 1, comparison);
	}
	,
	quickSort1: function (list, low, high, comparison) {
		if (low < high) {
			var pp = $.ig.FastItemColumn.prototype.partition(list, low, high, comparison);
			$.ig.FastItemColumn.prototype.quickSort1(list, low, pp - 1, comparison);
			$.ig.FastItemColumn.prototype.quickSort1(list, pp + 1, high, comparison);
		}
	}
	,
	partition: function (list, low, high, comparison) {
		var pivot = list.item(high);
		var ii = low;
		for (var jj = low; jj < high; jj++) {
			if (comparison(list.item(jj), pivot) <= 0) {
				var swapA = list.item(ii);
				list.item(ii, list.item(jj));
				list.item(jj, swapA);
				ii++;
			}
		}
		var swapB = list.item(ii);
		list.item(ii, list.item(high));
		list.item(high, swapB);
		return ii;
	}
	,
	getSortedIndices1: function (values, comparison) {
		var result = new $.ig.List$1($.ig.Number.prototype.$type, 2, values.count());
		for (var i = 0; i < values.count(); i++) {
			result.add(i);
		}
		if (values.count() < 22) {
			$.ig.FastItemColumn.prototype.quickSort(result, function (i1, i2) {
				var v1 = values.item($.ig.util.getValue(i1));
				var v2 = values.item($.ig.util.getValue(i2));
				return comparison(v1, v2);
			});
		} else {
			result.sort2(function (i1, i2) {
				var v1 = values.item(i1);
				var v2 = values.item(i2);
				return comparison(v1, v2);
			});
		}
		return result;
	}
	,
	getSortedIndices: function () {
		return $.ig.FastItemColumn.prototype.getSortedIndices1(this.values(), function (o1, o2) {
			var d1 = o1;
			var d2 = o2;
			if (d1 < d2) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		});
	}
	,
	asArray: function () {
		return this.values().asArray();
	}
	,
	$type: new $.ig.Type('FastItemColumn', $.ig.Object.prototype.$type, [$.ig.IFastItemColumnInternal.prototype.$type, $.ig.IFastItemColumn$1.prototype.$type.specialize(Number)])
}, true);

$.ig.util.defType('FastItemDateTimeColumn', 'Object', {
	__coerceValue: null,
	__expectFunctions: false,
	init: function (fastItemsSource, propertyName, coerceValue, expectFunctions) {
		this.__propertyName = null;
		this._hasMinimum = false;
		this._hasMaximum = false;
		$.ig.Object.prototype.init.call(this);
		this.__coerceValue = coerceValue;
		this.__expectFunctions = expectFunctions;
		this.propertyName(propertyName);
		this.fastItemsSource(fastItemsSource);
	},
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			this.reset();
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	_fastItemsSource: null,
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	_hasMinimum: false,
	_hasMaximum: false,
	minimum: function (value) {
		if (arguments.length === 1) {
			this._minimum = value;
			return value;
		} else {
			if (!this._hasMinimum && this.values() != null) {
				var en = this.values().getEnumerator();
				while (en.moveNext()) {
					var value = en.current();
					if (value < this._minimum) {
						this._minimum = value;
					}
				}
				if (this.values().count() > 0) {
					this._hasMinimum = true;
				}
			}
			return this._minimum;
		}
	}
	,
	_minimum: new Date(),
	maximum: function (value) {
		if (arguments.length === 1) {
			this._maximum = value;
			return value;
		} else {
			if (!this._hasMaximum && this.values() != null) {
				var en = this.values().getEnumerator();
				while (en.moveNext()) {
					var value = en.current();
					if (value > this._maximum) {
						this._maximum = value;
					}
				}
				if (this.values().count() > 0) {
					this._hasMaximum = true;
				}
			}
			return this._maximum;
		}
	}
	,
	_maximum: new Date(),
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			return this.values().__inner[index];
		}
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	contains: function (item) {
		return this.values().contains(item);
	}
	,
	copyTo: function (array, arrayIndex) {
		this.values().copyTo(array, arrayIndex);
	}
	,
	count: function () {
		return this.values().count();
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	indexOf: function (item) {
		return this.values().indexOf(item);
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
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
	reset: function () {
		this.values(null);
		this._hasMinimum = false;
		this._hasMaximum = false;
		return this.fastItemsSource() != null ? this.insertRange(0, this.fastItemsSource().count()) : true;
	}
	,
	insertRange: function (position, count) {
		var newValues = new Array(count);
		var source_ = this._fastItemsSource.asArray();
		var item_;
		var minimum = this.minimum();
		var maximum = this.maximum();
		var newValue;
		var newCount = 0;
		var coerce = this.__coerceValue;
		if (this.__coerceValue != null || this.__expectFunctions) {
			for (var i_ = position; i_ < position + count; ++i_) {
				item_ = source_[i_][this.__propertyName];
				if (this.__expectFunctions) {
					if (typeof(item_) == 'function') {
						item_ = item_();
					}
				}
				if (coerce != null) {
					item_ = coerce(item_);
				}
				newValue = item_ == null ? new Date() : item_;
				if (!this._hasMinimum) {
					minimum = newValue;
					this._hasMinimum = true;
				} else if (newValue < minimum) {
					minimum = newValue;
				}
				if (!this._hasMaximum) {
					maximum = newValue;
					this._hasMaximum = true;
				} else if (newValue > maximum) {
					maximum = newValue;
				}
				newValues[newCount] = newValue;
				newCount++;
			}
		} else {
			for (var i_ = position; i_ < position + count; ++i_) {
				item_ = source_[i_][this.__propertyName];
				newValue = item_ == null ? new Date() : item_;
				if (!this._hasMinimum) {
					minimum = newValue;
					this._hasMinimum = true;
				} else if (newValue < minimum) {
					minimum = newValue;
				}
				if (!this._hasMaximum) {
					maximum = newValue;
					this._hasMaximum = true;
				} else if (newValue > maximum) {
					maximum = newValue;
				}
				newValues[newCount] = newValue;
				newCount++;
			}
		}
		this.minimum(minimum);
		this.maximum(maximum);
		if (this.values() == null) {
			this.values(new $.ig.List$1($.ig.Date.prototype.$type, 1, newValues));
		} else {
			this.values().insertRange(position, newValues);
		}
		return true;
	}
	,
	removeRange: function (position, count) {
		for (var i = position; i < position + count; ++i) {
			if (+(this.item(i)) == +(this.minimum())) {
				this._hasMinimum = false;
			}
			if (+(this.item(i)) == +(this.maximum())) {
				this._hasMaximum = false;
			}
		}
		this.values().removeRange(position, count);
		return true;
	}
	,
	replaceMinMax: function (oldValue, newValue) {
		if (+oldValue != +($.ig.Date.prototype.minValue())) {
			if (+newValue != +($.ig.Date.prototype.minValue())) {
				this.minimum(newValue < this.minimum() ? newValue : this.minimum());
				this.maximum(newValue > this.maximum() ? newValue : this.maximum());
			}
			return;
		}
		this.minimum(newValue < this.minimum() ? newValue : this.minimum());
		this.maximum(newValue > this.maximum() ? newValue : this.maximum());
	}
	,
	replaceRange: function (position, count) {
		var ret = false;
		for (var i = 0; i < count; ++i) {
			var oldValue = this.values().__inner[position + i];
			var newValue = this.toDateTime(this.fastItemsSource().item(position + i));
			if (+oldValue != +newValue) {
				this.values().__inner[position + i] = newValue;
				ret = true;
				this.replaceMinMax(oldValue, newValue);
			}
		}
		return ret;
	}
	,
	_fastReflectionHelper: null,
	toDateTime: function (item) {
		if (item == null) {
			return $.ig.Date.prototype.minValue();
		}
		var from_ = item;
		item = from_[this.__propertyName];
		if (this.__expectFunctions) {
			from_ = item;
			if (typeof(from_) == 'function') {
				item = from_();
			}
		}
		if (this.__coerceValue != null) {
			item = this.__coerceValue(item);
		}
		if (item == null) {
			return $.ig.Date.prototype.minValue();
		}
		return item;
	}
	,
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	getSortedIndices: function () {
		return $.ig.FastItemColumn.prototype.getSortedIndices1(this.values(), function (o1, o2) {
			var d1 = o1;
			var d2 = o2;
			if (d1 < d2) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		});
	}
	,
	asArray: function () {
		return this.values().asArray();
	}
	,
	$type: new $.ig.Type('FastItemDateTimeColumn', $.ig.Object.prototype.$type, [$.ig.IFastItemColumnInternal.prototype.$type, $.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Date.prototype.$type)])
}, true);

$.ig.util.defType('FastItemObjectColumn', 'Object', {
	__coerceValue: null,
	__expectFunctions: false,
	init: function (fastItemsSource, propertyName, coerceValue, expectFunctions) {
		$.ig.Object.prototype.init.call(this);
		this.__coerceValue = coerceValue;
		this.__expectFunctions = expectFunctions;
		this.propertyName(propertyName);
		this.fastItemsSource(fastItemsSource);
	},
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			this.reset();
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	_fastItemsSource: null,
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	minimum: function (value) {
		if (arguments.length === 1) {
			this.__minimum = value;
			return value;
		} else {
			return this.__minimum;
		}
	}
	,
	__minimum: null,
	maximum: function (value) {
		if (arguments.length === 1) {
			this.__maximum = value;
			return value;
		} else {
			return this.__maximum;
		}
	}
	,
	__maximum: null,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			return this.values().__inner[index];
		}
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	contains: function (item) {
		return this.values().contains1(item);
	}
	,
	copyTo: function (array, arrayIndex) {
		this.values().copyTo(array, arrayIndex);
	}
	,
	count: function () {
		return this.values().count();
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	indexOf: function (item) {
		return this.values().indexOf1(item);
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
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
	reset: function () {
		this.values(null);
		return this.fastItemsSource() != null ? this.insertRange(0, this.fastItemsSource().count()) : true;
	}
	,
	insertRange: function (position, count) {
		var newValues = (function () {
			var $ret = new $.ig.List$1($.ig.Object.prototype.$type, 0);
			$ret.capacity(count);
			return $ret;
		}());
		for (var i = position; i < position + count; ++i) {
			var newValue = this.toObject(this.fastItemsSource().item(i));
			newValues.add1(newValue);
		}
		if (this.values() == null) {
			this.values(newValues);
		} else {
			this.values().insertRange(position, newValues);
		}
		return true;
	}
	,
	replaceRange: function (position, count) {
		var ret = false;
		for (var i = 0; i < count; ++i) {
			var oldValue = this.values().__inner[position + i];
			var newValue = this.toObject(this.fastItemsSource().item(position + i));
			if (oldValue != newValue) {
				this.values().__inner[position + i] = newValue;
				ret = true;
			}
		}
		return ret;
	}
	,
	removeRange: function (position, count) {
		this.values().removeRange(position, count);
		return true;
	}
	,
	_fastReflectionHelper: null,
	toObject: function (item) {
		if (item == null) {
			return null;
		}
		var from_ = item;
		item = from_[this.__propertyName];
		if (this.__expectFunctions) {
			from_ = item;
			if (typeof(from_) == 'function') {
				item = from_();
			}
		}
		if (this.__coerceValue != null) {
			item = this.__coerceValue(item);
		}
		return item;
	}
	,
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	getSortedIndices: function () {
		return $.ig.FastItemColumn.prototype.getSortedIndices1(this.values(), function (o1, o2) {
			var d1 = parseFloat(o1);
			var d2 = parseFloat(o2);
			if (d1 < d2) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		});
	}
	,
	asArray: function () {
		return this.values().asArray();
	}
	,
	$type: new $.ig.Type('FastItemObjectColumn', $.ig.Object.prototype.$type, [$.ig.IFastItemColumnInternal.prototype.$type, $.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Object.prototype.$type)])
}, true);

$.ig.util.defType('FastItemIntColumn', 'Object', {
	__coerceValue: null,
	__expectFunctions: false,
	init: function (fastItemsSource, propertyName, coerceValue, expectFunctions) {
		this.__propertyName = null;
		$.ig.Object.prototype.init.call(this);
		this.__coerceValue = coerceValue;
		this.__expectFunctions = expectFunctions;
		this.propertyName(propertyName);
		this.fastItemsSource(fastItemsSource);
	},
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			this.reset();
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	_fastItemsSource: null,
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	minimum: function (value) {
		if (arguments.length === 1) {
			this.__minimum = value;
			return value;
		} else {
			return this.__minimum;
		}
	}
	,
	__minimum: 0,
	maximum: function (value) {
		if (arguments.length === 1) {
			this.__maximum = value;
			return value;
		} else {
			return this.__maximum;
		}
	}
	,
	__maximum: 0,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			return this.values().__inner[index];
		}
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	contains: function (item) {
		return this.values().contains(item);
	}
	,
	copyTo: function (array, arrayIndex) {
		this.values().copyTo(array, arrayIndex);
	}
	,
	count: function () {
		return this.values().count();
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	indexOf: function (item) {
		return this.values().indexOf(item);
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
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
	reset: function () {
		this.values(null);
		return this.fastItemsSource() != null ? this.insertRange(0, this.fastItemsSource().count()) : true;
	}
	,
	insertRange: function (position, count) {
		var newValues = new Array(count);
		var source_ = this._fastItemsSource.asArray();
		var item_;
		var minimum = this.minimum();
		var maximum = this.maximum();
		var newCount = 0;
		var coerce = this.__coerceValue;
		if (this.__coerceValue != null || this.__expectFunctions) {
			for (var i_ = position; i_ < position + count; ++i_) {
				item_ = source_[i_][this.__propertyName];
				if (this.__expectFunctions) {
					if (typeof(item_) == 'function') {
						item_ = item_();
					}
				}
				if (coerce != null) {
					item_ = coerce(item_);
				}
				var newValue = item_ == null ? 0 : $.ig.util.getValue(item_);
				newValues[newCount] = newValue;
				newCount++;
			}
		} else {
			for (var i_ = position; i_ < position + count; ++i_) {
				item_ = source_[i_][this.__propertyName];
				var newValue1 = item_ == null ? 0 : $.ig.util.getValue(item_);
				newValues[newCount] = newValue1;
				newCount++;
			}
		}
		if (this.values() == null) {
			this.values(new $.ig.List$1($.ig.Number.prototype.$type, 1, newValues));
		} else {
			this.values().insertRange(position, newValues);
		}
		return true;
	}
	,
	replaceRange: function (position, count) {
		var ret = false;
		for (var i = 0; i < count; ++i) {
			var oldValue = this.values().__inner[position + i];
			var newValue = this.toInt(this.fastItemsSource().item(position + i));
			if (oldValue != newValue) {
				this.values().__inner[position + i] = newValue;
				ret = true;
			}
		}
		return ret;
	}
	,
	removeRange: function (position, count) {
		this.values().removeRange(position, count);
		return true;
	}
	,
	_fastReflectionHelper: null,
	toInt: function (item) {
		var from_ = item;
		item = from_[this.__propertyName];
		if (this.__expectFunctions) {
			from_ = item;
			if (typeof(from_) == 'function') {
				item = from_();
			}
		}
		if (this.__coerceValue != null) {
			item = this.__coerceValue(item);
		}
		if (item == null) {
			return 0;
		}
		return $.ig.util.getValue(item);
	}
	,
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	getSortedIndices: function () {
		return $.ig.FastItemColumn.prototype.getSortedIndices1(this.values(), function (o1, o2) {
			var d1 = $.ig.util.getValue(o1);
			var d2 = $.ig.util.getValue(o2);
			if (d1 < d2) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		});
	}
	,
	asArray: function () {
		return this.values().asArray();
	}
	,
	$type: new $.ig.Type('FastItemIntColumn', $.ig.Object.prototype.$type, [$.ig.IFastItemColumnInternal.prototype.$type, $.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Number.prototype.$type)])
}, true);

$.ig.util.defType('IFastItemsSource', 'Object', {
	$type: new $.ig.Type('IFastItemsSource', null)
}, true);

$.ig.util.defType('FastItemsSource', 'Object', {
	init: function () {
		this._columns = new $.ig.Dictionary$2(String, $.ig.ColumnReference.prototype.$type, 0);
		this._contents = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		this._index = null;
		$.ig.Object.prototype.init.call(this);
	},
	event: null,
	raiseDataSourceEvent: function (action, position, count) {
		if (this.event != null) {
			this.event(this, new $.ig.FastItemsSourceEventArgs(0, action, position, count));
		}
	}
	,
	raiseDataSourceEvent1: function (position, propertyName) {
		if (this.event != null) {
			this.event(this, new $.ig.FastItemsSourceEventArgs(1, position, propertyName));
		}
	}
	,
	itemsSource: function (value) {
		if (arguments.length === 1) {
			if (this._itemsSource == value) {
				return;
			}
			this.detach();
			this._itemsSource = value;
			this._contents.clear();
			this._index = null;
			this.attach();
			var en = this._columns.values().getEnumerator();
			while (en.moveNext()) {
				var referencedColumn = en.current();
				referencedColumn._fastItemColumn.reset();
			}
			this.raiseDataSourceEvent($.ig.FastItemsSourceEventAction.prototype.insert, 0, this._contents.count());
			return value;
		} else {
			return this._itemsSource;
		}
	}
	,
	detach: function () {
	}
	,
	onItemsSourceEventProxyWeakCollectionChanged: function (fastItemsSource, sender, e) {
		var fis = fastItemsSource;
		switch (e.action()) {
			case $.ig.NotifyCollectionChangedAction.prototype.add:
				fis.dataSourceAdd(e.newStartingIndex(), e.newItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.remove:
				fis.dataSourceRemove(e.oldStartingIndex(), e.oldItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.replace:
				fis.dataSourceReplace(e.newStartingIndex(), e.oldItems(), e.newItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.reset:
				fis.dataSourceReset();
				break;
		}
	}
	,
	attach: function () {
		this._contents.insertRange1(this._contents.count(), this._itemsSource);
	}
	,
	dataSourceAdd: function (position, newItems) {
		if (this._index != null) {
			for (var i = 0; i < newItems.count(); ++i) {
				this._index.add(newItems.item(i), position + i);
			}
			for (var i1 = position; i1 < this._contents.count(); ++i1) {
				this._index.item(this._contents.__inner[i1], i1 + newItems.count());
			}
		}
		this._contents.insertRange1(position, newItems);
		var en = this._columns.values().getEnumerator();
		while (en.moveNext()) {
			var referencedColumn = en.current();
			referencedColumn._fastItemColumn.insertRange(position, newItems.count());
		}
		this.raiseDataSourceEvent($.ig.FastItemsSourceEventAction.prototype.insert, position, newItems.count());
	}
	,
	dataSourceRemove: function (position, oldItems) {
		this._contents.removeRange(position, oldItems.count());
		if (this._index != null) {
			var en = oldItems.getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				this._index.remove(item);
			}
			for (var i = position; i < this._contents.count(); ++i) {
				this._index.item(this._contents.__inner[i], i);
			}
		}
		var en1 = this._columns.values().getEnumerator();
		while (en1.moveNext()) {
			var referencedColumn = en1.current();
			referencedColumn._fastItemColumn.removeRange(position, oldItems.count());
		}
		this.raiseDataSourceEvent($.ig.FastItemsSourceEventAction.prototype.remove, position, oldItems.count());
	}
	,
	dataSourceReplace: function (position, oldItems, newItems) {
		for (var i = 0; i < newItems.count(); ++i) {
			this._contents.__inner[position + i] = newItems.item(i);
		}
		if (this._index != null) {
			var en = oldItems.getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				this._index.remove(item);
			}
			for (var i1 = 0; i1 < newItems.count(); ++i1) {
				this._index.add(newItems.item(i1), position + i1);
			}
		}
		var en1 = this._columns.values().getEnumerator();
		while (en1.moveNext()) {
			var referencedColumn = en1.current();
			referencedColumn._fastItemColumn.replaceRange(position, newItems.count());
		}
		this.raiseDataSourceEvent($.ig.FastItemsSourceEventAction.prototype.replace, position, oldItems.count());
	}
	,
	dataSourceReset: function () {
		this._contents.clear();
		this._index = null;
		this._contents.insertRange1(0, this._itemsSource);
		var en = this._columns.values().getEnumerator();
		while (en.moveNext()) {
			var referencedColumn = en.current();
			referencedColumn._fastItemColumn.reset();
		}
		this.raiseDataSourceEvent($.ig.FastItemsSourceEventAction.prototype.reset, 0, this._contents.count());
	}
	,
	dataSourceChange: function (item, propertyName) {
		var $self = this;
		var columnReference = null;
		var position = this.indexOf(item);
		if (position == -1) {
			throw new $.ig.ArgumentException(1, "item");
		}
		if (String.isNullOrEmpty(propertyName)) {
			var en = this._columns.getEnumerator();
			while (en.moveNext()) {
				var col = en.current();
				col.value()._fastItemColumn.replaceRange(position, 1);
				this.raiseDataSourceEvent1(position, col.value()._fastItemColumn.propertyName());
			}
		} else {
			if ((function () { var $ret = $self._columns.tryGetValue(propertyName, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
				columnReference._fastItemColumn.replaceRange(position, 1);
			}
			if ((function () { var $ret = $self._columns.tryGetValue(propertyName + "_object", columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
				columnReference._fastItemColumn.replaceRange(position, 1);
			}
			this.raiseDataSourceEvent1(position, propertyName);
		}
	}
	,
	count: function () {
		return this._contents.count();
	}
	,
	item: function (i) {
		return this._contents.__inner[i];
	}
	,
	getEnumerator: function () {
		return this._contents.getEnumerator();
	}
	,
	indexOf: function (item) {
		var $self = this;
		var ret;
		if (this._index == null && this._contents.count() > 0) {
			this._index = new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.Number.prototype.$type, 0);
			var contents_ = this._contents;
			var index_ = this._index;
			for (var j_ = 0; j_ < this._contents.count(); j_++) {
				var o = contents_.__inner[j_];
				if (!this._index.containsKey(o)) {
					this._index.add(o, j_);
				}
			}
		}
		if ((function () { var $ret = $self._index.tryGetValue(item, ret); ret = $ret.p1; return $ret.ret; }())) {
			return ret;
		} else {
			return -1;
		}
	}
	,
	registerColumnDateTime: function (propertyName, coerceValue, expectFunctions) {
		var $self = this;
		var fastItemColumn = null;
		if (propertyName != null) {
			var columnReference = null;
			if (!(function () { var $ret = $self._columns.tryGetValue(propertyName, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
				columnReference = new $.ig.ColumnReference(new $.ig.FastItemDateTimeColumn(this, propertyName, coerceValue, expectFunctions));
				this._columns.add(propertyName, columnReference);
			}
			columnReference.references(columnReference.references() + 1);
			fastItemColumn = $.ig.util.cast($.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Date.prototype.$type), columnReference._fastItemColumn);
		}
		return fastItemColumn;
	}
	,
	registerColumnObject: function (propertyName, coerceValue, expectFunctions) {
		var $self = this;
		var fastItemColumn = null;
		var key = propertyName + "_object";
		if (propertyName != null) {
			var columnReference = null;
			if (!(function () { var $ret = $self._columns.tryGetValue(key, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
				columnReference = new $.ig.ColumnReference(new $.ig.FastItemObjectColumn(this, propertyName, coerceValue, expectFunctions));
				this._columns.add(key, columnReference);
			}
			columnReference.references(columnReference.references() + 1);
			fastItemColumn = $.ig.util.cast($.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Object.prototype.$type), columnReference._fastItemColumn);
		}
		return fastItemColumn;
	}
	,
	registerColumnInt: function (propertyName, coerceValue, expectFunctions) {
		var $self = this;
		var fastItemColumn = null;
		if (propertyName == null) {
			propertyName = "";
		}
		var columnReference = null;
		if (!(function () { var $ret = $self._columns.tryGetValue(propertyName, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
			columnReference = new $.ig.ColumnReference(new $.ig.FastItemIntColumn(this, propertyName, coerceValue, expectFunctions));
			this._columns.add(propertyName, columnReference);
		}
		columnReference.references(columnReference.references() + 1);
		fastItemColumn = $.ig.util.cast($.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Number.prototype.$type), columnReference._fastItemColumn);
		return fastItemColumn;
	}
	,
	registerColumn: function (propertyName, coerceValue, expectFunctions) {
		var $self = this;
		var fastItemColumn = null;
		if (propertyName == null) {
			propertyName = "";
		}
		var columnReference = null;
		if (!(function () { var $ret = $self._columns.tryGetValue(propertyName, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
			columnReference = new $.ig.ColumnReference(new $.ig.FastItemColumn(this, propertyName, coerceValue, expectFunctions));
			this._columns.add(propertyName, columnReference);
		}
		columnReference.references(columnReference.references() + 1);
		fastItemColumn = $.ig.util.cast($.ig.IFastItemColumn$1.prototype.$type.specialize(Number), columnReference._fastItemColumn);
		return fastItemColumn;
	}
	,
	deregisterColumn: function (fastItemColumn) {
		var $self = this;
		var propertyName = fastItemColumn != null ? fastItemColumn.propertyName() : null;
		var key = propertyName;
		if ($.ig.util.cast($.ig.IFastItemColumn$1.prototype.$type.specialize($.ig.Object.prototype.$type), fastItemColumn) !== null) {
			key += "_object";
		}
		if (propertyName != null) {
			var columnReference = null;
			if ((function () { var $ret = $self._columns.tryGetValue(propertyName, columnReference); columnReference = $ret.p1; return $ret.ret; }())) {
				columnReference.references(columnReference.references() - 1);
				if (columnReference.references() == 0) {
					this._columns.remove(key);
				}
			}
		}
	}
	,
	_columns: null,
	_itemsSource: null,
	_contents: null,
	_index: null,
	asArray: function () {
		return this._contents.asArray();
	}
	,
	handleCollectionChanged: function (e) {
		switch (e.action()) {
			case $.ig.NotifyCollectionChangedAction.prototype.add:
				this.dataSourceAdd(e.newStartingIndex(), e.newItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.remove:
				this.dataSourceRemove(e.oldStartingIndex(), e.oldItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.replace:
				this.dataSourceReplace(e.newStartingIndex(), e.oldItems(), e.newItems());
				break;
			case $.ig.NotifyCollectionChangedAction.prototype.reset:
				this.dataSourceReset();
				break;
		}
	}
	,
	$type: new $.ig.Type('FastItemsSource', $.ig.Object.prototype.$type, [$.ig.IEnumerable.prototype.$type, $.ig.IFastItemsSource.prototype.$type])
}, true);

$.ig.util.defType('ColumnReference', 'Object', {
	init: function (fastItemColumn) {
		$.ig.Object.prototype.init.call(this);
		this._fastItemColumn = fastItemColumn;
		this.references(0);
	},
	_fastItemColumn: null,
	_references: 0,
	references: function (value) {
		if (arguments.length === 1) {
			this._references = value;
			return value;
		} else {
			return this._references;
		}
	}
	,
	$type: new $.ig.Type('ColumnReference', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FastReflectionHelper', 'Object', {
	init: function (useTraditionalReflection, propertyName) {
		$.ig.Object.prototype.init.call(this);
		this.useTraditionalReflection(useTraditionalReflection);
		this.updatePropertyName(propertyName);
	},
	__propertyName: null,
	updatePropertyName: function (propertyName) {
		this.__propertyName = propertyName;
	}
	,
	_useTraditionalReflection: false,
	useTraditionalReflection: function (value) {
		if (arguments.length === 1) {
			this._useTraditionalReflection = value;
			return value;
		} else {
			return this._useTraditionalReflection;
		}
	}
	,
	getPropertyValue: function (item) {
		var from_ = item;
		return from_[this.__propertyName];
	}
	,
	invalid: function () {
		return false;
	}
	,
	$type: new $.ig.Type('FastReflectionHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BrushUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	getLightened: function (brush, interpolation) {
		if (brush == null) {
			return brush;
		}
		if (brush._isGradient) {
			var newBrush = (brush).clone();
			for (var i = 0; i < newBrush._gradientStops.length; i++) {
				var currentStop = newBrush._gradientStops[i];
				currentStop.color($.ig.ColorUtil.prototype.getLightened(currentStop.color(), interpolation));
			}
			return newBrush;
		} else {
			var l = $.ig.ColorUtil.prototype.getLightened(brush.color(), interpolation);
			return (function () {
				var $ret = new $.ig.Brush();
				$ret.color(l);
				return $ret;
			}());
		}
	}
	,
	getInterpolation: function (minimum, interpolation, maximum, interpolationMode) {
		var target = new $.ig.Brush();
		if (minimum == null && maximum == null) {
			target.__fill = "transparent";
			return target;
		}
		var minSolid = null, maxSolid = null;
		var minLinear = null, maxLinear = null;
		if (minimum == null) {
			var c = maximum._isGradient ? (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(255);
				$ret.g(255);
				$ret.b(255);
				return $ret;
			}()) : (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(maximum.color().r());
				$ret.g(maximum.color().g());
				$ret.b(maximum.color().b());
				return $ret;
			}());
			minSolid = (function () {
				var $ret = new $.ig.Brush();
				$ret.color(c);
				return $ret;
			}());
		} else {
			if (minimum._isGradient) {
				minLinear = minimum;
			} else {
				minSolid = minimum;
			}
		}
		if (maximum == null) {
			var c1 = minimum._isGradient ? (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(255);
				$ret.g(255);
				$ret.b(255);
				return $ret;
			}()) : (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(minimum.color().r());
				$ret.g(minimum.color().g());
				$ret.b(minimum.color().b());
				return $ret;
			}());
			maxSolid = (function () {
				var $ret = new $.ig.Brush();
				$ret.color(c1);
				return $ret;
			}());
		} else {
			if (maximum._isGradient) {
				maxLinear = maximum;
			} else {
				maxSolid = maximum;
			}
		}
		if (minSolid != null && maxSolid != null) {
			return $.ig.BrushUtil.prototype.solidSolid(minSolid, interpolation, maxSolid, interpolationMode);
		}
		if (minSolid != null && maxLinear != null) {
			return $.ig.BrushUtil.prototype.solidLinear(minSolid, interpolation, maxLinear, interpolationMode);
		}
		if (minLinear != null && maxSolid != null) {
			return $.ig.BrushUtil.prototype.solidLinear(maxSolid, 1 - interpolation, minLinear, interpolationMode);
		}
		if (minLinear != null && maxLinear != null) {
			return $.ig.BrushUtil.prototype.linearLinear(minLinear, interpolation, maxLinear, interpolationMode);
		}
		return target;
	}
	,
	solidSolid: function (min, p, max, interpolationMode) {
		var b = new $.ig.Brush();
		b.color($.ig.ColorUtil.prototype.getInterpolation(min.color(), p, max.color(), interpolationMode));
		return b;
	}
	,
	solidLinear: function (min, p, max, interpolationMode) {
		var b = new $.ig.LinearGradientBrush();
		b._gradientStops = $.ig.BrushUtil.prototype.gradientStops1(min.color(), p, max._gradientStops, interpolationMode);
		if (max._useCustomDirection) {
			b._useCustomDirection = true;
			b._startX = max._startX;
			b._startY = max._startY;
			b._endX = max._endX;
			b._endY = max._endY;
		}
		return b;
	}
	,
	linearLinear: function (min, p, max, interpolationMode) {
		var b = new $.ig.LinearGradientBrush();
		b._gradientStops = $.ig.BrushUtil.prototype.gradientStops(min._gradientStops, p, max._gradientStops, interpolationMode);
		if (min._useCustomDirection || max._useCustomDirection) {
			b._useCustomDirection = true;
			b._startX = min._startX + p * (max._startX - min._startX);
			b._startY = min._startY + p * (max._startY - min._startY);
			b._endX = (1 - p) * min._endX + p * max._endX;
			b._endY = (1 - p) * min._endY + p * max._endY;
		}
		return b;
	}
	,
	gradientStops1: function (min, p, max, interpolationMode) {
		var gradientStopCollection = new Array(max.length);
		for (var i = 0; i < max.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min, p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		return gradientStopCollection;
	}
	,
	gradientStops: function (min, p, max, interpolationMode) {
		var minimumCount = Math.min(min.length, max.length);
		var maxCount = Math.max(min.length, max.length);
		var gradientStopCollection = new Array(maxCount);
		var i = 0;
		for (; i < minimumCount; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[i]._offset + p * max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[i].color(), p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		for (; i < min.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[i]._offset + p * max[max.length - 1]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[i].color(), p, max[max.length - 1].color(), interpolationMode));
				return $ret;
			}());
		}
		for (; i < max.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[min.length - 1]._offset + p * max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[min.length - 1].color(), p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		return gradientStopCollection;
	}
	,
	isFullyTransparent: function (brush) {
		var linearBrush = $.ig.util.cast($.ig.LinearGradientBrush.prototype.$type, brush);
		if (linearBrush != null) {
			return $.ig.BrushUtil.prototype.isFullyTransparent2(linearBrush);
		}
		if (brush.color().a() == 0) {
			return true;
		}
		return false;
	}
	,
	isFullyTransparent2: function (brush) {
		var $t = brush._gradientStops;
		for (var i = 0; i < $t.length; i++) {
			var stop = $t[i];
			if (!$.ig.BrushUtil.prototype.isFullyTransparent1(stop)) {
				return false;
			}
		}
		return true;
	}
	,
	isFullyTransparent1: function (gradientStop) {
		if (gradientStop.color().a() == 0) {
			return true;
		}
		return false;
	}
	,
	getCssBrushColors: function (className, obj) {
		var brushes = new Array(2);
		obj.addClass(className);
		var fill = new $.ig.Brush();
		fill.__fill = obj.css("background-color");
		var outline = new $.ig.Brush();
		outline.__fill = obj.css("border-top-color");
		obj.removeClass(className);
		brushes[0] = fill;
		brushes[1] = outline;
		return brushes;
	}
	,
	getBrushCollection: function (palleteName_, container_, brushes, outlines, defaultColors) {
		brushes = new $.ig.BrushCollection();
		outlines = new $.ig.BrushCollection();
		var tempBrush;
		var names = new $.ig.List$1(String, 0);
		names.add("background-color");
		names.add("border-top-color");
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var palette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, "ui-" + palleteName_ + "-palette-", names);
		var numPaletteColors = palette.count();
		if (numPaletteColors == 0) {
			if (defaultColors == null) {
				defaultColors = [ "#B1BFC9", "#50a8be", "#798995", "#fc6754", "#4F606C", "#fec33c", "#374650", "#3c6399", "#162C3B", "#91af49" ];
			}
			for (var i = 0; i < defaultColors.length - 1; i += 2) {
				tempBrush = new $.ig.Brush();
				tempBrush.__fill = defaultColors[i];
				outlines.add(tempBrush);
				tempBrush = new $.ig.Brush();
				tempBrush.__fill = defaultColors[i + 1];
				brushes.add(tempBrush);
			}
		}
		for (var i1 = 0; i1 < numPaletteColors; i1++) {
			var fillBrush = new $.ig.Brush();
			fillBrush.__fill = palette.__inner[i1].__inner[0];
			var outlineBrush = new $.ig.Brush();
			outlineBrush.__fill = palette.__inner[i1].__inner[1];
			brushes.add(fillBrush);
			outlines.add(outlineBrush);
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return {
			p2: brushes,
			p3: outlines
		};
	}
	,
	getGradientBrushCollection: function (fillGradientPaletteName, outlineGradientPaletteName, paletteName, container_, brushes, outlines, defaultColors) {
		brushes = new $.ig.BrushCollection();
		outlines = new $.ig.BrushCollection();
		if (defaultColors == null) {
			defaultColors = [ "#B1BFC9", "#50a8be", "#798995", "#fc6754", "#4F606C", "#fec33c", "#374650", "#3c6399", "#162C3B", "#91af49" ];
		}
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var names = new $.ig.List$1(String, 0);
		names.add("background-image");
		var fillsPalette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, fillGradientPaletteName, names);
		var numFillsPaletteColors = fillsPalette.count();
		var outlinesPalette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, outlineGradientPaletteName, names);
		var numOutlinesPaletteColors = outlinesPalette.count();
		for (var i = 0; i < numFillsPaletteColors; i++) {
			brushes.add($.ig.CssGradientUtil.prototype.brushFromGradientString(fillsPalette.__inner[i].__inner[0]));
		}
		for (var i1 = 0; i1 < numOutlinesPaletteColors; i1++) {
			outlines.add($.ig.CssGradientUtil.prototype.brushFromGradientString(outlinesPalette.__inner[i1].__inner[0]));
		}
		names.clear();
		var fillIndex = 0;
		var outlineIndex = 0;
		var numPaletteColors = Math.min(numFillsPaletteColors, numOutlinesPaletteColors);
		var palette = null;
		if (numFillsPaletteColors == 0) {
			names.add("background-color");
		}
		if (numOutlinesPaletteColors == 0) {
			names.add("border-top-color");
			outlineIndex = numFillsPaletteColors == 0 ? 1 : 0;
		}
		if (names.count() > 0) {
			palette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, paletteName, names);
			numPaletteColors = palette.count();
		}
		if (numFillsPaletteColors == 0) {
			if (numPaletteColors > 0) {
				for (var i2 = 0; i2 < numPaletteColors; i2++) {
					var fillBrush = new $.ig.Brush();
					fillBrush.__fill = palette.__inner[i2].__inner[fillIndex];
					brushes.add(fillBrush);
				}
			} else {
				for (var i3 = 0; i3 < defaultColors.length - 1; i3 += 2) {
					var fillBrush1 = new $.ig.Brush();
					fillBrush1 = new $.ig.Brush();
					fillBrush1.__fill = defaultColors[i3 + 1];
					brushes.add(fillBrush1);
				}
			}
		}
		if (numOutlinesPaletteColors == 0) {
			if (numPaletteColors > 0) {
				for (var i4 = 0; i4 < numPaletteColors; i4++) {
					var outlineBrush = new $.ig.Brush();
					outlineBrush.__fill = palette.__inner[i4].__inner[outlineIndex];
					outlines.add(outlineBrush);
				}
			} else {
				for (var i5 = 0; i5 < defaultColors.length - 1; i5 += 2) {
					var outlineBrush1 = new $.ig.Brush();
					outlineBrush1.__fill = defaultColors[i5];
					outlines.add(outlineBrush1);
				}
			}
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return {
			p4: brushes,
			p5: outlines
		};
	}
	,
	getGradientBrush: function (gradientClassName, className, cssProp, container_, fallbackColor) {
		var b = null;
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var gradientString = $.ig.CssHelper.prototype.getPropertyValue(discovery, gradientClassName, "background-image");
		if (gradientString != null) {
			b = $.ig.CssGradientUtil.prototype.brushFromGradientString(gradientString);
		}
		if (b == null) {
			b = new $.ig.Brush();
			var fillColor = $.ig.CssHelper.prototype.getPropertyValue(discovery, className, cssProp);
			b.__fill = fillColor != null ? fillColor : fallbackColor;
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return b;
	}
	,
	$type: new $.ig.Type('BrushUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ColorUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	randomColor: function (alpha) {
		return $.ig.Color.prototype.fromArgb(alpha, $.ig.ColorUtil.prototype._r.next2(0, 255), $.ig.ColorUtil.prototype._r.next2(0, 255), $.ig.ColorUtil.prototype._r.next2(0, 255));
	}
	,
	randomHue: function (color) {
		var ahsv = $.ig.ColorUtil.prototype.getAHSV(color);
		return $.ig.ColorUtil.prototype.fromAHSV(ahsv[0], $.ig.ColorUtil.prototype._r.next2(0, 359), ahsv[2], ahsv[3]);
	}
	,
	getInterpolation: function (minimum, interpolation_, maximum_, interpolationMode) {
		var min_ = minimum;
		switch (interpolationMode) {
			case $.ig.InterpolationMode.prototype.hSV:
				{
					var b = $.ig.ColorUtil.prototype.getAHSV(minimum);
					var e = $.ig.ColorUtil.prototype.getAHSV(maximum_);
					var b1 = b[1] >= 0 ? b[1] : e[1];
					var e1 = e[1] >= 0 ? e[1] : b[1];
					if (b1 >= 0 && e1 >= 0 && Math.abs(e1 - b1) > 180) {
						if (e1 > b1) {
							b1 += 360;
						} else {
							e1 += 360;
						}
					}
					interpolation_ = Math.max(0, Math.min(1, interpolation_));
					return $.ig.ColorUtil.prototype.fromAHSV(b[0] + interpolation_ * (e[0] - b[0]), b1 + interpolation_ * (e1 - b1), b[2] + interpolation_ * (e[2] - b[2]), b[3] + interpolation_ * (e[3] - b[3]));
				}
			case $.ig.InterpolationMode.prototype.rGB: return $.ig.Color.prototype.fromArgb(min_.__a + interpolation_ * (maximum_.__a - min_.__a), min_.__r + interpolation_ * (maximum_.__r - min_.__r), min_.__g + interpolation_ * (maximum_.__g - min_.__g), min_.__b + interpolation_ * (maximum_.__b - min_.__b));
		}
		return minimum;
	}
	,
	getAHSVInterpolation: function (minimum, interpolation, maximum) {
		var b1 = minimum[1] >= 0 ? minimum[1] : maximum[1];
		var e1 = maximum[1] >= 0 ? maximum[1] : minimum[1];
		if (b1 >= 0 && e1 >= 0 && Math.abs(e1 - b1) > 180) {
			if (e1 > b1) {
				b1 += 360;
			} else {
				e1 += 360;
			}
		}
		interpolation = Math.max(0, Math.min(1, interpolation));
		return $.ig.ColorUtil.prototype.fromAHSV(minimum[0] + interpolation * (maximum[0] - minimum[0]), b1 + interpolation * (e1 - b1), minimum[2] + interpolation * (maximum[2] - minimum[2]), minimum[3] + interpolation * (maximum[3] - minimum[3]));
	}
	,
	getLightened: function (color, interpolation) {
		var ahsl = $.ig.ColorUtil.prototype.getAHSL(color);
		if (interpolation < 0) {
			return $.ig.ColorUtil.prototype.fromAHSL(ahsl[0], ahsl[1], ahsl[2], ahsl[3] * (1 - $.ig.MathUtil.prototype.clamp(-interpolation, 0, 1)));
		} else {
			return $.ig.ColorUtil.prototype.fromAHSL(ahsl[0], ahsl[1], ahsl[2], ahsl[3] + $.ig.MathUtil.prototype.clamp(interpolation, 0, 1) * (1 - ahsl[3]));
		}
	}
	,
	getAHSL: function (color) {
		var ahsl = new Array(4);
		var r = color.r() / 255;
		var g = color.g() / 255;
		var b = color.b() / 255;
		var min = Math.min(Math.min(r, g), b);
		var max = Math.max(Math.max(r, g), b);
		var delta = max - min;
		ahsl[0] = color.a() / 255;
		ahsl[3] = (max + min) / 2;
		if (delta == 0) {
			ahsl[1] = -1;
			ahsl[2] = 0;
		} else {
			ahsl[1] = $.ig.ColorUtil.prototype.h(max, delta, r, g, b);
			ahsl[2] = ahsl[3] < 0.5 ? delta / (max + min) : delta / (2 - max - min);
		}
		return ahsl;
	}
	,
	getAHSV: function (color) {
		var a = color.a() / 255;
		var r = color.r() / 255;
		var g = color.g() / 255;
		var b = color.b() / 255;
		var min = Math.min(r, Math.min(g, b));
		var max = Math.max(r, Math.max(g, b));
		var delta = max - min;
		var ahsv = new Array(4);
		ahsv[0] = a;
		ahsv[3] = max;
		if (delta == 0) {
			ahsv[1] = -1;
			ahsv[2] = 0;
		} else {
			ahsv[1] = $.ig.ColorUtil.prototype.h(max, delta, r, g, b);
			ahsv[2] = delta / max;
		}
		return ahsv;
	}
	,
	fromAHSL: function (alpha, hue, saturation, lightness) {
		var r;
		var g;
		var b;
		if (saturation == 0) {
			r = lightness;
			g = lightness;
			b = lightness;
		} else {
			var q = lightness < 0.5 ? lightness * (1 + saturation) : lightness + saturation - (lightness * saturation);
			var p = 2 * lightness - q;
			var hk = hue / 360;
			r = $.ig.ColorUtil.prototype.c(p, q, hk + 1 / 3);
			g = $.ig.ColorUtil.prototype.c(p, q, hk);
			b = $.ig.ColorUtil.prototype.c(p, q, hk - 1 / 3);
		}
		return $.ig.Color.prototype.fromArgb($.ig.truncate((alpha * 255)), $.ig.truncate((r * 255)), $.ig.truncate((g * 255)), $.ig.truncate((b * 255)));
	}
	,
	fromAHSV: function (alpha, hue, saturation, value) {
		var r;
		var g;
		var b;
		while (hue >= 360) {
			hue -= 360;
		}
		if (saturation == 0) {
			r = value;
			g = value;
			b = value;
		} else {
			hue /= 60;
			var i = Math.floor(hue);
			var f = hue - i;
			var p = value * (1 - saturation);
			var q = value * (1 - saturation * f);
			var t = value * (1 - saturation * (1 - f));
			switch ($.ig.truncate(i)) {
				case 0:
					r = value;
					g = t;
					b = p;
					break;
				case 1:
					r = q;
					g = value;
					b = p;
					break;
				case 2:
					r = p;
					g = value;
					b = t;
					break;
				case 3:
					r = p;
					g = q;
					b = value;
					break;
				case 4:
					r = t;
					g = p;
					b = value;
					break;
				default:
					r = value;
					g = p;
					b = q;
					break;
			}
		}
		return $.ig.Color.prototype.fromArgb($.ig.truncate((alpha * 255)), $.ig.truncate((r * 255)), $.ig.truncate((g * 255)), $.ig.truncate((b * 255)));
	}
	,
	h: function (max, delta, r, g, b) {
		var h = r == max ? (g - b) / delta : g == max ? 2 + (b - r) / delta : 4 + (r - g) / delta;
		h *= 60;
		if (h < 0) {
			h += 360;
		}
		return h;
	}
	,
	c: function (p, q, t) {
		t = t < 0 ? t + 1 : t > 1 ? t - 1 : t;
		if (t < 1 / 6) {
			return p + ((q - p) * 6 * t);
		}
		if (t < 1 / 2) {
			return q;
		}
		if (t < 2 / 3) {
			return p + ((q - p) * 6 * (2 / 3 - t));
		}
		return p;
	}
	,
	randomColors: function () {
		if ($.ig.ColorUtil.prototype.__randomColors == null) {
			$.ig.ColorUtil.prototype.__randomColors = new Array(100);
			$.ig.ColorUtil.prototype.__randomColors[0] = $.ig.Color.prototype.fromArgb(255, 70, 130, 180);
			$.ig.ColorUtil.prototype.__randomColors[1] = $.ig.Color.prototype.fromArgb(255, 65, 105, 225);
			$.ig.ColorUtil.prototype.__randomColors[2] = $.ig.Color.prototype.fromArgb(255, 100, 149, 237);
			$.ig.ColorUtil.prototype.__randomColors[3] = $.ig.Color.prototype.fromArgb(255, 176, 196, 222);
			$.ig.ColorUtil.prototype.__randomColors[4] = $.ig.Color.prototype.fromArgb(255, 123, 104, 238);
			$.ig.ColorUtil.prototype.__randomColors[5] = $.ig.Color.prototype.fromArgb(255, 106, 90, 205);
			$.ig.ColorUtil.prototype.__randomColors[6] = $.ig.Color.prototype.fromArgb(255, 72, 61, 139);
			$.ig.ColorUtil.prototype.__randomColors[7] = $.ig.Color.prototype.fromArgb(255, 25, 25, 112);
			for (var colorIndex = 8; colorIndex < 100; colorIndex++) {
				$.ig.ColorUtil.prototype.__randomColors[colorIndex] = $.ig.Color.prototype.fromArgb(255, $.ig.ColorUtil.prototype._r.next1(255), $.ig.ColorUtil.prototype._r.next1(255), $.ig.ColorUtil.prototype._r.next1(255));
			}
		}
		return $.ig.ColorUtil.prototype.__randomColors;
	}
	,
	getRandomColor: function (index) {
		index %= 100;
		return $.ig.ColorUtil.prototype.randomColors()[index];
	}
	,
	colorToInt: function (color) {
		var aa = color.a() / 255;
		var rr = $.ig.truncate((color.r() * aa));
		var gg = $.ig.truncate((color.g() * aa));
		var bb = $.ig.truncate((color.b() * aa));
		return color.a() << 24 | rr << 16 | gg << 8 | bb;
	}
	,
	getColor: function (brush) {
		return brush.color();
	}
	,
	$type: new $.ig.Type('ColorUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CssHelper', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	getDisoveryElement: function () {
		var discoveryStyle = $("#fakediscoveryelementstyle");
		if (discoveryStyle.length == 0) {
			var styleText = "fakediscoveryelement\n" + "{\n" + "\tdisplay: block;\n" + "   position: absolute;\n" + "   box-sizing: content-box;\n" + "   -moz-box-sizing: content-box;\n" + "\tmargin: " + $.ig.CssHelper.prototype.defaultMarginValue + ";\n" + "\tcolor: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   border-style: solid;\n" + "   border-color: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   background-color: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   background-image: " + $.ig.CssHelper.prototype.defaultBackgroundImageValue + ";\n" + "   border-width: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   border-radius: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   vertical-align: " + $.ig.CssHelper.prototype.defaultVerticalAlignValue + ";\n" + "   text-align: " + $.ig.CssHelper.prototype.defaultTextAlignValue + ";\n" + "   opacity: " + $.ig.CssHelper.prototype.defaultOpacityValue + ";\n" + "   visibility: " + $.ig.CssHelper.prototype.defaultVisibilityValue + ";\n" + "   width: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   height: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "}\n";
			discoveryStyle = $("<style id='fakediscoveryelementstyle'></style>");
			discoveryStyle.html(styleText);
			$("head").append(discoveryStyle);
		}
		return $("<fakediscoveryelement></fakediscoveryelement>");
	}
	,
	getDefaultValue: function (propertyName) {
		if (propertyName == "color" || propertyName == "border-color" || propertyName == "border-top-color" || propertyName == "border-left-color" || propertyName == "border-right-color" || propertyName == "border-bottom-color" || propertyName == "background-color") {
			return $.ig.CssHelper.prototype.defaultColorValue;
		} else if (propertyName == "margin-left" || propertyName == "margin-top" || propertyName == "margin-right" || propertyName == "margin-bottom") {
			return $.ig.CssHelper.prototype.defaultMarginValue;
		} else if (propertyName == "vertical-align") {
			return $.ig.CssHelper.prototype.defaultVerticalAlignValue;
		} else if (propertyName == "opacity") {
			return $.ig.CssHelper.prototype.defaultOpacityValue;
		} else if (propertyName == "background-image") {
			return $.ig.CssHelper.prototype.defaultBackgroundImageValue;
		} else if (propertyName == "text-align") {
			return $.ig.CssHelper.prototype.defaultTextAlignValue;
		} else if (propertyName == "visibility") {
			return $.ig.CssHelper.prototype.defaultVisibilityValue;
		} else if ($.ig.CssHelper.prototype.isWidthProperty(propertyName)) {
			return $.ig.CssHelper.prototype.defaultWidthHeightValue;
		}
		return "DEFAULT";
	}
	,
	numberOfClasses: function (discoveryElement, classPrefix, propertyName) {
		var defaultValue = $.ig.CssHelper.prototype.getDefaultValue(propertyName);
		var curr = 1;
		var done = false;
		while (!done && curr < $.ig.CssHelper.prototype.maxClasses) {
			var className = classPrefix + curr.toString();
			discoveryElement.addClass(className);
			var propValue = discoveryElement.css(propertyName);
			if (propValue == defaultValue) {
				break;
			}
			curr++;
		}
		return curr;
	}
	,
	getPropertyValue: function (discoveryElement, className, propertyName) {
		discoveryElement.addClass(className);
		var ret_ = discoveryElement.css(propertyName);
		discoveryElement.removeClass(className);
		if (propertyName == "opacity") {
			ret_ = Math.round(1000.0 * ret_) / 1000.0;
		}
		if ($.ig.CssHelper.prototype.isEqualToDefault(ret_, $.ig.CssHelper.prototype.getDefaultValue(propertyName), propertyName)) {
			return null;
		}
		return ret_;
	}
	,
	isEqualToDefault: function (propValue, defaultValue, propertyName) {
		if (propValue == defaultValue) {
			return true;
		}
		if ($.ig.CssHelper.prototype.isWidthProperty(propertyName) && propValue != null && propValue.contains("px")) {
			var num = $.ig.util.replace(propValue, "px", "");
			var val = parseFloat(num);
			if (Math.abs(Math.round(val) - 4321) < 2) {
				return true;
			}
			return false;
		} else if ($.ig.CssHelper.prototype.isMarginProperty(propertyName) && propValue != null && propValue.contains("px")) {
			var num1 = $.ig.util.replace(propValue, "px", "");
			var val1 = parseFloat(num1);
			if (Math.abs(Math.round(val1) + 4321) < 2) {
				return true;
			}
			return false;
		} else {
			return propValue == defaultValue;
		}
	}
	,
	isWidthProperty: function (propertyName) {
		return propertyName == "width" || propertyName == "height" || propertyName == "border-top-width" || propertyName == "border-left-width" || propertyName == "border-right-width" || propertyName == "border-bottom-width" || propertyName == "border-top-left-radius";
	}
	,
	isMarginProperty: function (propertyName) {
		return propertyName == "margin-top" || propertyName == "margin-left" || propertyName == "margin-right" || propertyName == "margin-bottom";
	}
	,
	getValuesForClassCollection: function (discoveryElement, classPrefix, propertyNames) {
		var ret = new $.ig.List$1($.ig.List$1.prototype.$type.specialize(String), 0);
		var curr = 1;
		var done = false;
		while (!done && curr < $.ig.CssHelper.prototype.maxClasses) {
			var className = classPrefix + curr.toString();
			discoveryElement.addClass(className);
			var row = new $.ig.List$1(String, 0);
			for (var i = 0; i < propertyNames.count(); i++) {
				var propertyName = propertyNames.__inner[i];
				var defaultValue = $.ig.CssHelper.prototype.getDefaultValue(propertyName);
				var propValue = discoveryElement.css(propertyName);
				if ($.ig.CssHelper.prototype.isEqualToDefault(propValue, defaultValue, propertyName)) {
					done = true;
					break;
				}
				row.add(propValue);
			}
			discoveryElement.removeClass(className);
			if (!done) {
				ret.add(row);
			}
			curr++;
		}
		return ret;
	}
	,
	$type: new $.ig.Type('CssHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('DoubleAnimator', 'Object', {
	needsFlush: function () {
		return this.transitionProgress() == 0;
	}
	,
	flush: function () {
		this.update(true);
	}
	,
	__transitionProgress: 0,
	transitionProgress: function (value) {
		if (arguments.length === 1) {
			this.__transitionProgress = value;
			if (this.propertyChanged != null) {
				this.propertyChanged(this, new $.ig.PropertyChangedEventArgs("TransitionProgress"));
			}
			return value;
		} else {
			return this.__transitionProgress;
		}
	}
	,
	__intervalMilliseconds: 0,
	intervalMilliseconds: function (value) {
		if (arguments.length === 1) {
			this.__intervalMilliseconds = value;
			return value;
		} else {
			return this.__intervalMilliseconds;
		}
	}
	,
	__easingFunction: null,
	easingFunction: function (value) {
		if (arguments.length === 1) {
			this.__easingFunction = value;
			return value;
		} else {
			return this.__easingFunction;
		}
	}
	,
	__from: 0,
	__to: 0,
	from: function (value) {
		if (arguments.length === 1) {
			this.__from = value;
			return value;
		} else {
			return this.__from;
		}
	}
	,
	to: function (value) {
		if (arguments.length === 1) {
			this.__to = value;
			return value;
		} else {
			return this.__to;
		}
	}
	,
	init: function (from, to, intervalMilliseconds) {
		this.__easingFunction = null;
		this.__from = 0;
		this.__to = 0;
		this.__active = false;
		this.__intervalId = -1;
		this.__lastRender = 0;
		$.ig.Object.prototype.init.call(this);
		this.__from = from;
		this.__to = to;
		this.__intervalMilliseconds = intervalMilliseconds;
		this.requestAnimationFrame(window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            window.msRequestAnimationFrame ||
            function(callback) {
                window.setTimeout(callback, 1000 / 60);
            });
	},
	_requestAnimationFrame: null,
	requestAnimationFrame: function (value) {
		if (arguments.length === 1) {
			this._requestAnimationFrame = value;
			return value;
		} else {
			return this._requestAnimationFrame;
		}
	}
	,
	__active: false,
	active: function (value) {
		if (arguments.length === 1) {
			this.__active = value;
			return value;
		} else {
			return this.__active;
		}
	}
	,
	start: function () {
		this.__transitionProgress = 0;
		this.__lastRender = 0;
		this.__startTime = $.ig.Date.prototype.now();
		if (!this.__active) {
			this.__active = true;
			this.requestAnimationFrame()(this.tick.runOn(this));
		}
	}
	,
	stop: function () {
		this.__active = false;
		this.__transitionProgress = 0;
		this.__lastRender = 0;
	}
	,
	__startTime: new Date(),
	__intervalId: 0,
	__lastRender: 0,
	tick: function () {
		this.update(false);
	}
	,
	update: function (flush) {
		if (!this.__active) {
			this.stop();
			return;
		}
		var currentTime = $.ig.Date.prototype.now();
		var ellapsedMilliseconds = currentTime.getTime() - this.__startTime.getTime();
		if (ellapsedMilliseconds > this.__intervalMilliseconds) {
			ellapsedMilliseconds = this.__intervalMilliseconds;
		}
		if ((ellapsedMilliseconds - this.__lastRender < 16 && ellapsedMilliseconds != this.__intervalMilliseconds) && !flush) {
			this.requestAnimationFrame()(this.tick.runOn(this));
			return;
		}
		this.__lastRender = ellapsedMilliseconds;
		var p = (ellapsedMilliseconds / this.__intervalMilliseconds);
		if (this.__easingFunction != null) {
			p = this.__easingFunction(p);
		}
		var interpolatedValue = this.__from + ((this.__to - this.__from) * p);
		if (!flush) {
			if (ellapsedMilliseconds == this.__intervalMilliseconds) {
				this.stop();
			} else {
				this.requestAnimationFrame()(this.tick.runOn(this));
			}
		}
		this.transitionProgress(interpolatedValue);
	}
	,
	getElapsedMilliseconds: function () {
		return this.__active ? $.ig.Date.prototype.now().getTime() - this.__startTime.getTime() : 0;
	}
	,
	animationActive: function () {
		return this.__active;
	}
	,
	propertyChanged: null,
	$type: new $.ig.Type('DoubleAnimator', $.ig.Object.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('BrushCollection', 'ObservableCollection$1', {
	init: function () {
		this._interpolationMode = $.ig.InterpolationMode.prototype.rGB;
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.Brush.prototype.$type, 0);
	},
	selectRandom: function () {
		return this.item($.ig.BrushCollection.prototype._random.next1(this.count()));
	}
	,
	interpolateRandom: function () {
		return this.getInterpolatedBrush($.ig.BrushCollection.prototype._random.nextDouble() * (this.count() - 1));
	}
	,
	interpolationMode: function (value) {
		if (arguments.length === 1) {
			if (this._interpolationMode != value) {
				this._interpolationMode = value;
				this.onCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset));
			}
			return value;
		} else {
			return this._interpolationMode;
		}
	}
	,
	_interpolationMode: 0,
	item: function (index, value) {
		if (arguments.length === 2) {
			$.ig.ObservableCollection$1.prototype.item.call(this, index, value);
			return value;
		} else {
			if (index < 0 || index >= this.count()) {
				return null;
			}
			return $.ig.ObservableCollection$1.prototype.item.call(this, index);
		}
	}
	,
	getInterpolatedBrush: function (index) {
		if ($.ig.util.isNaN(index)) {
			return null;
		}
		index = $.ig.MathUtil.prototype.clamp(index, 0, this.count() - 1);
		var i = $.ig.truncate(Math.floor(index));
		if (i == index) {
			return this.item(i);
		}
		return this.interpolateBrushes(index - i, this.item(i), this.item(i + 1), this.interpolationMode());
	}
	,
	interpolateBrushes: function (p, minBrush, maxBrush, InterpolationMode) {
		var minFill = minBrush.color();
		var maxFill = maxBrush.color();
		var interp = $.ig.ColorUtil.prototype.getInterpolation(minFill, p, maxFill, InterpolationMode);
		var b = new $.ig.Brush();
		b.color(interp);
		return b;
	}
	,
	equals: function (obj) {
		if (obj == null) {
			return false;
		}
		var bc = obj;
		if (bc.count() != this.count()) {
			return false;
		}
		for (var i = 0; i < bc.count(); i++) {
			if (!bc.item(i).equals(this.item(i))) {
				return false;
			}
		}
		return true;
	}
	,
	$type: new $.ig.Type('BrushCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.Brush.prototype.$type))
}, true);

$.ig.util.defType('FastItemsSourceEventArgs', 'EventArgs', {
	init: function (initNumber, action, position, count) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.EventArgs.prototype.init.call(this);
		this.action(action);
		this.position(position);
		this.count(count);
		this.propertyName(null);
	},
	init1: function (initNumber, position, propertyName) {
		$.ig.EventArgs.prototype.init.call(this);
		this.action($.ig.FastItemsSourceEventAction.prototype.change);
		this.position(position);
		this.count(1);
		this.propertyName(propertyName);
	},
	_action: 0,
	action: function (value) {
		if (arguments.length === 1) {
			this._action = value;
			return value;
		} else {
			return this._action;
		}
	}
	,
	_position: 0,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
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
	_propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this._propertyName = value;
			return value;
		} else {
			return this._propertyName;
		}
	}
	,
	$type: new $.ig.Type('FastItemsSourceEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('IPool$1', 'Object', {
	$type: new $.ig.Type('IPool$1', null)
}, true);

$.ig.util.defType('IIndexedPool$1', 'Object', {
	$type: new $.ig.Type('IIndexedPool$1', null, [$.ig.IPool$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('Pool$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.active(new $.ig.List$1(this.$t, 0));
		this.inactive(new $.ig.List$1(this.$t, 0));
	},
	_create: null,
	create: function (value) {
		if (arguments.length === 1) {
			this._create = value;
			return value;
		} else {
			return this._create;
		}
	}
	,
	_disactivate: null,
	disactivate: function (value) {
		if (arguments.length === 1) {
			this._disactivate = value;
			return value;
		} else {
			return this._disactivate;
		}
	}
	,
	_activate: null,
	activate: function (value) {
		if (arguments.length === 1) {
			this._activate = value;
			return value;
		} else {
			return this._activate;
		}
	}
	,
	_destroy: null,
	destroy: function (value) {
		if (arguments.length === 1) {
			this._destroy = value;
			return value;
		} else {
			return this._destroy;
		}
	}
	,
	item: function (index) {
		if (index >= this.count()) {
			this.count(index + 1);
		}
		return this.active().__inner[index];
	}
	,
	clear: function () {
		this.count(0);
	}
	,
	count: function (value) {
		if (arguments.length === 1) {
			var activeCount = this.active().count();
			while (value > this.active().count() && this.inactive().count() > 0) {
				var item = this.inactive().__inner[this.inactive().count() - 1];
				this.active().add(item);
				this.inactive().removeAt(this.inactive().count() - 1);
				if (this.activate() != null) {
					this.activate()(item);
				}
			}
			while (value > this.active().count() && this.inactive().count() == 0) {
				var item1 = this.create()();
				this.active().add(item1);
				if (this.activate() != null) {
					this.activate()(item1);
				}
			}
			var inactiveCount = 2;
			while (activeCount != 0) {
				activeCount >>= 1;
				inactiveCount <<= 1;
			}
			while (value < this.active().count()) {
				var item2 = this.active().__inner[this.active().count() - 1];
				this.active().removeAt(this.active().count() - 1);
				if (this.disactivate() != null) {
					this.disactivate()(item2);
				}
				if (this.inactive().count() < inactiveCount) {
					this.inactive().add(item2);
				} else {
					if (this.destroy() != null) {
						this.destroy()(item2);
					}
				}
			}
			if (inactiveCount < this.inactive().count()) {
				for (var i = inactiveCount; i < this.inactive().count(); ++i) {
					this.destroy()(this.inactive().__inner[i]);
				}
				this.inactive().removeRange(inactiveCount, this.inactive().count() - inactiveCount);
			}
			return value;
		} else {
			return this.active().count();
		}
	}
	,
	_active: null,
	active: function (value) {
		if (arguments.length === 1) {
			this._active = value;
			return value;
		} else {
			return this._active;
		}
	}
	,
	_inactive: null,
	inactive: function (value) {
		if (arguments.length === 1) {
			this._inactive = value;
			return value;
		} else {
			return this._inactive;
		}
	}
	,
	doToAll: function (action) {
		for (var i = 0; i < this.inactive().count(); i++) {
			action(this.inactive().__inner[i]);
		}
		for (var i1 = 0; i1 < this.active().count(); i1++) {
			action(this.active().__inner[i1]);
		}
	}
	,
	$type: new $.ig.Type('Pool$1', $.ig.Object.prototype.$type, [$.ig.IIndexedPool$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('RectUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	getCenter: function (rect) {
		if (rect.isEmpty()) {
			return { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		return { __x: 0.5 * (rect.left() + rect.right()), __y: 0.5 * (rect.bottom() + rect.top()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getArea: function (rect) {
		if (rect.isEmpty()) {
			return 0;
		}
		return rect.width() * rect.height();
	}
	,
	duplicate: function (rect) {
		if (rect.isEmpty()) {
			return rect;
		}
		return new $.ig.Rect(0, rect.left(), rect.top(), rect.width(), rect.height());
	}
	,
	getLeader: function (rect, anchor) {
		if (rect.containsPoint(anchor)) {
			return anchor;
		}
		var C = { __x: rect.left() + 0.5 * rect.width(), __y: rect.top() + 0.5 * rect.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var D = { __x: anchor.__x - C.__x, __y: anchor.__y - C.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var p;
		if (D.__x != 0) {
			p = (rect.left() - C.__x) / D.__x;
			var y = C.__y + p * D.__y;
			if (y > rect.top() && y < rect.bottom()) {
				return p > 0 ? { __x: rect.left(), __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName } : { __x: rect.right(), __y: C.__y - p * D.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		p = (rect.top() - C.__y) / D.__y;
		var x = C.__x + p * D.__x;
		return p > 0 ? { __x: x, __y: rect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName } : { __x: C.__x - p * D.__x, __y: rect.bottom(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getDistanceSquared1: function (rect, pt) {
		if (rect.isEmpty()) {
			return NaN;
		}
		return $.ig.RectUtil.prototype.getDistanceSquared(rect, pt.__x, pt.__y);
	}
	,
	getDistanceSquared2: function (rect, rc) {
		if (rect.isEmpty()) {
			return Number.POSITIVE_INFINITY;
		}
		var d2 = $.ig.RectUtil.prototype.getDistanceSquared(rect, rc.left(), rc.top());
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rect, rc.left(), rc.bottom()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rect, rc.right(), rc.bottom()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rect, rc.right(), rc.top()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rc, rect.left(), rect.top()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rc, rect.left(), rect.bottom()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rc, rect.right(), rect.bottom()));
		}
		if (d2 > 0) {
			d2 = Math.min(d2, $.ig.RectUtil.prototype.getDistanceSquared(rc, rect.right(), rect.top()));
		}
		return d2;
	}
	,
	getDistanceSquared: function (rc, X, Y) {
		var vs = X - rc.left();
		var vt = Y - rc.top();
		var s = rc.width() * vs;
		var t = rc.height() * vt;
		if (s > 0) {
			var s0 = rc.width() * rc.width();
			if (s < s0) {
				vs -= (s / s0) * rc.width();
			} else {
				vs -= rc.width();
			}
		}
		if (t > 0) {
			var t0 = rc.height() * rc.height();
			if (t < t0) {
				vt -= (t / t0) * rc.height();
			} else {
				vt -= rc.height();
			}
		}
		return vs * vs + vt * vt;
	}
	,
	contains: function (rect, rc) {
		if (rect.isEmpty() || rc.isEmpty()) {
			return false;
		}
		if (rect.left() > rc.left()) {
			return false;
		}
		if (rect.right() < rc.right()) {
			return false;
		}
		if (rect.top() > rc.top()) {
			return false;
		}
		if (rect.bottom() < rc.bottom()) {
			return false;
		}
		return true;
	}
	,
	intersectsWith: function (rect, rc) {
		if (rect.isEmpty() || rc.isEmpty()) {
			return false;
		}
		if (rect.right() < rc.left()) {
			return false;
		}
		if (rect.left() > rc.right()) {
			return false;
		}
		if (rect.top() > rc.bottom()) {
			return false;
		}
		if (rect.bottom() < rc.top()) {
			return false;
		}
		return true;
	}
	,
	intersectionArea: function (rect, rc) {
		if (rect.isEmpty() || rc.isEmpty()) {
			return 0;
		}
		var width = Math.min(rect.right(), rc.right()) - Math.max(rect.left(), rc.left());
		if (width <= 0) {
			return 0;
		}
		var height = Math.min(rect.bottom(), rc.bottom()) - Math.max(rect.top(), rc.top());
		if (height <= 0) {
			return 0;
		}
		return width * height;
	}
	,
	getInflated: function (rect, width, height) {
		if (rect.isEmpty()) {
			return rect;
		}
		return new $.ig.Rect(0, rect.x() - width, rect.y() - height, Math.max(0, rect.width() + 2 * width), Math.max(0, rect.height() + 2 * height));
	}
	,
	inflateBy: function (rect, dx, dy) {
		rect.x(rect.x() - dx);
		rect.y(rect.y() - dy);
		var width = rect.width() + 2 * dx;
		var height = rect.height() + 2 * dy;
		rect.width(width > 1 ? width : 1);
		rect.height(height > 1 ? height : 1);
		return rect;
	}
	,
	inflate1: function (rect, dx, dy) {
		rect.x(rect.x() - dx);
		rect.y(rect.y() - dy);
		var width = rect.width() + 2 * dx;
		var height = rect.height() + 2 * dy;
		rect.width(width > 1 ? width : 1);
		rect.height(height > 1 ? height : 1);
		return rect;
	}
	,
	inflate: function (rect, thickness) {
		var newX = rect.x() - thickness;
		var newY = rect.y() - thickness;
		var width = rect.width() + thickness + thickness;
		var height = rect.height() + thickness + thickness;
		var newWidth = width > 0 ? width : 0;
		var newHeight = height > 0 ? height : 0;
		return new $.ig.Rect(0, newX, newY, newWidth, newHeight);
	}
	,
	round: function (rect) {
		rect.x(Math.round(rect.x()));
		rect.y(Math.round(rect.y()));
		rect.width(Math.round(rect.width()));
		rect.height(Math.round(rect.height()));
		return rect;
	}
	,
	isNull: function (rect) {
		return rect.x() == 0 && rect.y() == 0 && rect.width() == 0 && rect.height() == 0;
	}
	,
	size: function (rect) {
		var value = new $.ig.Size(1, rect.width(), rect.height());
		return value;
	}
	,
	normalize: function (current, rect) {
		var normalizeRect = $.ig.RectUtil.prototype.normalize1(current, $.ig.RectUtil.prototype.size(rect));
		return normalizeRect;
	}
	,
	normalize1: function (current, size) {
		var normalizeRect = current;
		normalizeRect.x(current.x() / size.width());
		normalizeRect.y(current.y() / size.height());
		normalizeRect.width(current.width() / size.width());
		normalizeRect.height(current.height() / size.height());
		return normalizeRect;
	}
	,
	getTopLeft: function (rect) {
		var value = { __x: rect.x(), __y: rect.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return value;
	}
	,
	getTopRight: function (rect) {
		var value = { __x: rect.x() + rect.width(), __y: rect.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return value;
	}
	,
	getBottomLeft: function (rect) {
		var value = { __x: rect.x(), __y: rect.y() + rect.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return value;
	}
	,
	getBottomRight: function (rect) {
		var value = { __x: rect.x() + rect.width(), __y: rect.y() + rect.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return value;
	}
	,
	getCorners: function (rect) {
		var points = (function () {
			var $ret = new $.ig.List$1($.ig.Point.prototype.$type, 0);
			$ret.add($.ig.RectUtil.prototype.getTopLeft(rect));
			$ret.add($.ig.RectUtil.prototype.getTopRight(rect));
			$ret.add($.ig.RectUtil.prototype.getBottomRight(rect));
			$ret.add($.ig.RectUtil.prototype.getBottomLeft(rect));
			return $ret;
		}());
		return points;
	}
	,
	rotateAboutCenter: function (rect, angle) {
		angle = $.ig.GeometryUtil.prototype.simplifyAngle(angle);
		if (angle == 0 || angle == 180) {
			return new $.ig.Rect(0, rect.left(), rect.top(), rect.width(), rect.height());
		}
		var center = $.ig.RectUtil.prototype.getCenter(rect);
		if (angle == 90 || angle == 270) {
			var halfHeight = rect.height() / 2;
			var halfWidth = rect.width() / 2;
			return new $.ig.Rect(0, center.__x - halfHeight, center.__y - halfWidth, rect.height(), rect.width());
		}
		return $.ig.RectUtil.prototype.rotateAboutPoint(rect, center.__x, center.__y, angle);
	}
	,
	rotateAboutPoint: function (rect, x, y, angle) {
		var points = $.ig.RectUtil.prototype.getCorners(rect);
		var angleRadians = $.ig.MathUtil.prototype.radians(angle);
		var cos = Math.cos(angleRadians);
		var sin = Math.sin(angleRadians);
		var top = 1.7976931348623157E+308;
		var left = 1.7976931348623157E+308;
		var right = -1.7976931348623157E+308;
		var bottom = -1.7976931348623157E+308;
		var p;
		for (var i = 0; i < points.count(); i++) {
			p = points.__inner[i];
			var rotatedX = (cos * (p.__x - x) - sin * (p.__y - y) + x);
			var rotatedY = (sin * (p.__x - x) + cos * (p.__y - y) + y);
			top = Math.min(top, rotatedY);
			left = Math.min(left, rotatedX);
			right = Math.max(right, rotatedX);
			bottom = Math.max(bottom, rotatedY);
		}
		var ret = new $.ig.Rect(0, left, top, right - left, bottom - top);
		return ret;
	}
	,
	$type: new $.ig.Type('RectUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IVisualData', 'Object', {
	$type: new $.ig.Type('IVisualData', null)
}, true);

$.ig.util.defType('PrimitiveVisualData', 'Object', {
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
	},
	init1: function (initNumber, name) {
		$.ig.Object.prototype.init.call(this);
		this.name(name);
		this.tags(new $.ig.ShapeTags());
		this.appearance(new $.ig.PrimitiveAppearanceData());
	},
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	_tags: null,
	tags: function (value) {
		if (arguments.length === 1) {
			this._tags = value;
			return value;
		} else {
			return this._tags;
		}
	}
	,
	type: function () {
	}
	,
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.appearance().scaleByViewport(viewport);
	}
	,
	getPoints: function (settings) {
		var points = new $.ig.List$1($.ig.List$1.prototype.$type.specialize($.ig.Point.prototype.$type), 0);
		this.getPointsOverride(points, settings);
		return points;
	}
	,
	getPointsOverride: function (points, settings) {
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("appearance: " + (this.appearance() != null ? this.appearance().serialize() : "null") + ", ");
		sb.appendLine1("tags: [");
		for (var i = 0; i < this.tags().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("\"" + this.tags().__inner[i] + "\"");
		}
		sb.appendLine1("],");
		sb.appendLine1("type: \"" + this.type() + "\", ");
		sb.appendLine1("name: \"" + this.name() + "\", ");
		sb.appendLine1(this.serializeOverride());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('PrimitiveVisualData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('PointData', 'Object', {
	init: function (x, y) {
		$.ig.Object.prototype.init.call(this);
		this.x(x);
		this.y(y);
	},
	_x: 0,
	x: function (value) {
		if (arguments.length === 1) {
			this._x = value;
			return value;
		} else {
			return this._x;
		}
	}
	,
	_y: 0,
	y: function (value) {
		if (arguments.length === 1) {
			this._y = value;
			return value;
		} else {
			return this._y;
		}
	}
	,
	serialize: function () {
		return "{ x: " + this.x() + ", y: " + this.y() + "}";
	}
	,
	fromPoint: function (p) {
		var d = new $.ig.PointData(p.__x, p.__y);
		return d;
	}
	,
	toPoint: function () {
		var p = new $.ig.Point(0);
		p.__x = this.x();
		p.__y = this.y();
		return p;
	}
	,
	equals: function (obj) {
		var p = $.ig.util.cast($.ig.PointData.prototype.$type, obj);
		if (p == null) {
			return false;
		}
		return p.x() == this.x() && p.y() == this.y();
	}
	,
	$type: new $.ig.Type('PointData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('LabelAppearanceData', 'Object', {
	init: function () {
		this._labelBrush = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_text: null,
	text: function (value) {
		if (arguments.length === 1) {
			this._text = value;
			return value;
		} else {
			return this._text;
		}
	}
	,
	_horizontalAlignment: null,
	horizontalAlignment: function (value) {
		if (arguments.length === 1) {
			this._horizontalAlignment = value;
			return value;
		} else {
			return this._horizontalAlignment;
		}
	}
	,
	_verticalAlignment: null,
	verticalAlignment: function (value) {
		if (arguments.length === 1) {
			this._verticalAlignment = value;
			return value;
		} else {
			return this._verticalAlignment;
		}
	}
	,
	_textAlignment: null,
	textAlignment: function (value) {
		if (arguments.length === 1) {
			this._textAlignment = value;
			return value;
		} else {
			return this._textAlignment;
		}
	}
	,
	_textWrapping: null,
	textWrapping: function (value) {
		if (arguments.length === 1) {
			this._textWrapping = value;
			return value;
		} else {
			return this._textWrapping;
		}
	}
	,
	_textPosition: null,
	textPosition: function (value) {
		if (arguments.length === 1) {
			this._textPosition = value;
			return value;
		} else {
			return this._textPosition;
		}
	}
	,
	_labelBrush: null,
	labelBrush: function (value) {
		if (arguments.length === 1) {
			this._labelBrush = value;
			return value;
		} else {
			return this._labelBrush;
		}
	}
	,
	_labelBrushExtended: null,
	labelBrushExtended: function (value) {
		if (arguments.length === 1) {
			this._labelBrushExtended = value;
			return value;
		} else {
			return this._labelBrushExtended;
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
	_opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this._opacity = value;
			return value;
		} else {
			return this._opacity;
		}
	}
	,
	_visibility: false,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	_font: null,
	font: function (value) {
		if (arguments.length === 1) {
			this._font = value;
			return value;
		} else {
			return this._font;
		}
	}
	,
	_fontFamily: null,
	fontFamily: function (value) {
		if (arguments.length === 1) {
			this._fontFamily = value;
			return value;
		} else {
			return this._fontFamily;
		}
	}
	,
	_fontSize: 0,
	fontSize: function (value) {
		if (arguments.length === 1) {
			this._fontSize = value;
			return value;
		} else {
			return this._fontSize;
		}
	}
	,
	_fontWeight: null,
	fontWeight: function (value) {
		if (arguments.length === 1) {
			this._fontWeight = value;
			return value;
		} else {
			return this._fontWeight;
		}
	}
	,
	_fontStyle: null,
	fontStyle: function (value) {
		if (arguments.length === 1) {
			this._fontStyle = value;
			return value;
		} else {
			return this._fontStyle;
		}
	}
	,
	_fontStretch: null,
	fontStretch: function (value) {
		if (arguments.length === 1) {
			this._fontStretch = value;
			return value;
		} else {
			return this._fontStretch;
		}
	}
	,
	_marginLeft: 0,
	marginLeft: function (value) {
		if (arguments.length === 1) {
			this._marginLeft = value;
			return value;
		} else {
			return this._marginLeft;
		}
	}
	,
	_marginRight: 0,
	marginRight: function (value) {
		if (arguments.length === 1) {
			this._marginRight = value;
			return value;
		} else {
			return this._marginRight;
		}
	}
	,
	_marginTop: 0,
	marginTop: function (value) {
		if (arguments.length === 1) {
			this._marginTop = value;
			return value;
		} else {
			return this._marginTop;
		}
	}
	,
	_marginBottom: 0,
	marginBottom: function (value) {
		if (arguments.length === 1) {
			this._marginBottom = value;
			return value;
		} else {
			return this._marginBottom;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("text: \"" + (this.text() != null ? this.text() : "") + "\", ");
		if (this.textAlignment() != null) {
			sb.appendLine1("textAlignment: \"" + this.textAlignment() + "\", ");
		}
		if (this.textWrapping() != null) {
			sb.appendLine1("textWrapping: \"" + this.textWrapping() + "\", ");
		}
		sb.appendLine1("labelBrush: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.labelBrush()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.labelBrush()) : "null") + ", ");
		sb.appendLine1("labelBrushExtended: " + (this.labelBrushExtended() != null ? this.labelBrushExtended().serialize() : "null") + ", ");
		sb.appendLine1("angle: " + this.angle() + ", ");
		sb.appendLine1("marginLeft: " + this.marginLeft() + ", ");
		sb.appendLine1("marginRight: " + this.marginRight() + ", ");
		sb.appendLine1("marginTop: " + this.marginTop() + ", ");
		sb.appendLine1("marginBottom: " + this.marginBottom() + ", ");
		sb.appendLine1("opacity: " + this.opacity() + ", ");
		sb.appendLine1("visibility: " + (this.visibility() ? "true" : "false") + ", ");
		if (this.horizontalAlignment() != null) {
			sb.appendLine1("horizontalAlignment: \"" + this.horizontalAlignment() + "\", ");
		}
		if (this.verticalAlignment() != null) {
			sb.appendLine1("verticalAlignment: \"" + this.verticalAlignment() + "\", ");
		}
		if (this.font() != null) {
			sb.appendLine1("font: \"" + this.font() + "\",");
		}
		if (this.fontFamily() != null) {
			sb.appendLine1("fontFamily: \"" + $.ig.util.replace(this.fontFamily(), "\"", "'") + "\",");
		}
		if (this.fontWeight() != null) {
			sb.appendLine1("fontWeight: \"" + this.fontWeight() + "\",");
		}
		if (this.fontStyle() != null) {
			sb.appendLine1("fontStyle: \"" + this.fontStyle() + "\",");
		}
		if (this.fontStretch() != null) {
			sb.appendLine1("fontStretch: \"" + this.fontStretch() + "\",");
		}
		sb.appendLine1("fontSize: " + this.fontSize());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LabelAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('BrushAppearanceData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	serialize: function () {
		return "{ type: \"" + this.type() + "\", " + this.serializeOverride() + " }";
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('BrushAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('SolidBrushAppearanceData', 'BrushAppearanceData', {
	init: function () {
		this._colorValue = new $.ig.Color();
		$.ig.BrushAppearanceData.prototype.init.call(this);
	},
	type: function () {
		return "solid";
	}
	,
	_colorValue: null,
	colorValue: function (value) {
		if (arguments.length === 1) {
			this._colorValue = value;
			return value;
		} else {
			return this._colorValue;
		}
	}
	,
	serializeOverride: function () {
		return "colorValue: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.colorValue()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.colorValue()) : "null");
	}
	,
	$type: new $.ig.Type('SolidBrushAppearanceData', $.ig.BrushAppearanceData.prototype.$type)
}, true);

$.ig.util.defType('LinearGradientBrushAppearanceData', 'BrushAppearanceData', {
	init: function () {
		$.ig.BrushAppearanceData.prototype.init.call(this);
		this.stops(new $.ig.List$1($.ig.GradientStopAppearanceData.prototype.$type, 0));
	},
	type: function () {
		return "linear";
	}
	,
	_startX: 0,
	startX: function (value) {
		if (arguments.length === 1) {
			this._startX = value;
			return value;
		} else {
			return this._startX;
		}
	}
	,
	_startY: 0,
	startY: function (value) {
		if (arguments.length === 1) {
			this._startY = value;
			return value;
		} else {
			return this._startY;
		}
	}
	,
	_endX: 0,
	endX: function (value) {
		if (arguments.length === 1) {
			this._endX = value;
			return value;
		} else {
			return this._endX;
		}
	}
	,
	_endY: 0,
	endY: function (value) {
		if (arguments.length === 1) {
			this._endY = value;
			return value;
		} else {
			return this._endY;
		}
	}
	,
	_stops: null,
	stops: function (value) {
		if (arguments.length === 1) {
			this._stops = value;
			return value;
		} else {
			return this._stops;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.append5("startX: " + this.startX() + ", endX: " + this.endX() + ", startY: " + this.startY() + ", endY: " + this.endY());
		sb.append5(", stops: [");
		for (var i = 0; i < this.stops().count(); i++) {
			if (i > 0) {
				sb.append5(", ");
			}
			sb.append5(this.stops().__inner[i].serialize());
		}
		sb.append5("]");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LinearGradientBrushAppearanceData', $.ig.BrushAppearanceData.prototype.$type)
}, true);

$.ig.util.defType('GradientStopAppearanceData', 'Object', {
	init: function () {
		this._colorValue = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_colorValue: null,
	colorValue: function (value) {
		if (arguments.length === 1) {
			this._colorValue = value;
			return value;
		} else {
			return this._colorValue;
		}
	}
	,
	_offset: 0,
	offset: function (value) {
		if (arguments.length === 1) {
			this._offset = value;
			return value;
		} else {
			return this._offset;
		}
	}
	,
	serialize: function () {
		return "{ " + "colorValue: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.colorValue()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.colorValue()) : "null") + ", offset: " + this.offset() + " }";
	}
	,
	$type: new $.ig.Type('GradientStopAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('PrimitiveAppearanceData', 'Object', {
	init: function () {
		this._stroke = new $.ig.Color();
		this._fill = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_stroke: null,
	stroke: function (value) {
		if (arguments.length === 1) {
			this._stroke = value;
			return value;
		} else {
			return this._stroke;
		}
	}
	,
	_strokeExtended: null,
	strokeExtended: function (value) {
		if (arguments.length === 1) {
			this._strokeExtended = value;
			return value;
		} else {
			return this._strokeExtended;
		}
	}
	,
	_fill: null,
	fill: function (value) {
		if (arguments.length === 1) {
			this._fill = value;
			return value;
		} else {
			return this._fill;
		}
	}
	,
	_fillExtended: null,
	fillExtended: function (value) {
		if (arguments.length === 1) {
			this._fillExtended = value;
			return value;
		} else {
			return this._fillExtended;
		}
	}
	,
	_strokeThickness: 0,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this._strokeThickness = value;
			return value;
		} else {
			return this._strokeThickness;
		}
	}
	,
	_visibility: 0,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	_opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this._opacity = value;
			return value;
		} else {
			return this._opacity;
		}
	}
	,
	_canvasLeft: 0,
	canvasLeft: function (value) {
		if (arguments.length === 1) {
			this._canvasLeft = value;
			return value;
		} else {
			return this._canvasLeft;
		}
	}
	,
	_canvasTop: 0,
	canvasTop: function (value) {
		if (arguments.length === 1) {
			this._canvasTop = value;
			return value;
		} else {
			return this._canvasTop;
		}
	}
	,
	_canvaZIndex: 0,
	canvaZIndex: function (value) {
		if (arguments.length === 1) {
			this._canvaZIndex = value;
			return value;
		} else {
			return this._canvaZIndex;
		}
	}
	,
	_dashArray: null,
	dashArray: function (value) {
		if (arguments.length === 1) {
			this._dashArray = value;
			return value;
		} else {
			return this._dashArray;
		}
	}
	,
	_dashCap: 0,
	dashCap: function (value) {
		if (arguments.length === 1) {
			this._dashCap = value;
			return value;
		} else {
			return this._dashCap;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.canvasLeft((this.canvasLeft() - viewport.left()) / viewport.width());
		this.canvasTop((this.canvasTop() - viewport.top()) / viewport.height());
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("stroke: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.stroke()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.stroke()) : "null") + ", ");
		sb.appendLine1("fill: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.fill()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.fill()) : "null") + ", ");
		sb.appendLine1("strokeExtended: " + (this.strokeExtended() != null ? this.strokeExtended().serialize() : "null") + ", ");
		sb.appendLine1("fillExtended: " + (this.fillExtended() != null ? this.fillExtended().serialize() : "null") + ", ");
		sb.appendLine1("strokeThickness: " + this.strokeThickness() + ", ");
		sb.appendLine1("visibility: " + (this.visibility() == $.ig.Visibility.prototype.visible ? "true" : "false") + ", ");
		sb.appendLine1("opacity: " + this.opacity() + ", ");
		sb.appendLine1("canvasLeft: " + this.canvasLeft() + ", ");
		sb.appendLine1("canvasTop: " + this.canvasTop() + ", ");
		sb.appendLine1("canvasZIndex: " + this.canvaZIndex() + ", ");
		sb.appendLine1("dashArray: null, ");
		sb.appendLine1("dashCap: " + this.dashCap());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('PrimitiveAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('GetPointsSettings', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_ignoreFigureStartPoint: false,
	ignoreFigureStartPoint: function (value) {
		if (arguments.length === 1) {
			this._ignoreFigureStartPoint = value;
			return value;
		} else {
			return this._ignoreFigureStartPoint;
		}
	}
	,
	$type: new $.ig.Type('GetPointsSettings', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ShapeTags', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, String, 0);
	},
	$type: new $.ig.Type('ShapeTags', $.ig.List$1.prototype.$type.specialize(String))
}, true);

$.ig.util.defType('PathVisualData', 'PrimitiveVisualData', {
	type: function () {
		return "Path";
	}
	,
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, "path1");
		this.data(new $.ig.List$1($.ig.GeometryData.prototype.$type, 0));
	},
	init1: function (initNumber, name, path) {
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, name);
		this.data($.ig.AppearanceHelper.prototype.fromGeometry(path.data()));
		$.ig.AppearanceHelper.prototype.getShapeAppearance(this.appearance(), path);
	},
	init2: function (initNumber, name, line) {
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, name);
		this.data($.ig.AppearanceHelper.prototype.fromLineData(line));
		$.ig.AppearanceHelper.prototype.getShapeAppearance(this.appearance(), line);
	},
	_data: null,
	data: function (value) {
		if (arguments.length === 1) {
			this._data = value;
			return value;
		} else {
			return this._data;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("data: [");
		for (var i = 0; i < this.data().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5((this.data().__inner[i] != null ? this.data().__inner[i].serialize() : "null"));
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		$.ig.PrimitiveVisualData.prototype.scaleByViewport.call(this, viewport);
		var en = this.data().getEnumerator();
		while (en.moveNext()) {
			var data = en.current();
			data.scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		for (var i = 0; i < this.data().count(); i++) {
			var data = this.data().__inner[i];
			data.getPointsOverride(points, settings);
		}
	}
	,
	$type: new $.ig.Type('PathVisualData', $.ig.PrimitiveVisualData.prototype.$type)
}, true);

$.ig.util.defType('GeometryData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	scaleByViewport: function (viewport) {
	}
	,
	getPointsOverride: function (points, settings) {
	}
	,
	serialize: function () {
		return "{ type: \"" + this.type() + "\", " + this.serializeOverride() + "}";
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('GeometryData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('PathGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
		this.figures(new $.ig.List$1($.ig.PathFigureData.prototype.$type, 0));
	},
	type: function () {
		return "Path";
	}
	,
	_figures: null,
	figures: function (value) {
		if (arguments.length === 1) {
			this._figures = value;
			return value;
		} else {
			return this._figures;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("figures: [");
		for (var i = 0; i < this.figures().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.figures().__inner[i].serialize());
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		var en = this.figures().getEnumerator();
		while (en.moveNext()) {
			var figure = en.current();
			figure.scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		for (var i = 0; i < this.figures().count(); i++) {
			var figure = this.figures().__inner[i];
			figure.getPointsOverride(points, settings);
		}
	}
	,
	$type: new $.ig.Type('PathGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('LineGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Line";
	}
	,
	_x1: 0,
	x1: function (value) {
		if (arguments.length === 1) {
			this._x1 = value;
			return value;
		} else {
			return this._x1;
		}
	}
	,
	_y1: 0,
	y1: function (value) {
		if (arguments.length === 1) {
			this._y1 = value;
			return value;
		} else {
			return this._y1;
		}
	}
	,
	_x2: 0,
	x2: function (value) {
		if (arguments.length === 1) {
			this._x2 = value;
			return value;
		} else {
			return this._x2;
		}
	}
	,
	_y2: 0,
	y2: function (value) {
		if (arguments.length === 1) {
			this._y2 = value;
			return value;
		} else {
			return this._y2;
		}
	}
	,
	serializeOverride: function () {
		return "x1: " + this.x1() + ", y1: " + this.y1() + ", x2: " + this.x2() + ", y2:" + this.y2();
	}
	,
	scaleByViewport: function (viewport) {
		this.x1((this.x1() - viewport.left()) / viewport.width());
		this.y1((this.y1() - viewport.top()) / viewport.height());
		this.x2((this.x2() - viewport.left()) / viewport.width());
		this.y2((this.y2() - viewport.top()) / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.x1(), __y: this.y1(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x2(), __y: this.y2(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('LineGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('RectangleGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Rectangle";
	}
	,
	_x: 0,
	x: function (value) {
		if (arguments.length === 1) {
			this._x = value;
			return value;
		} else {
			return this._x;
		}
	}
	,
	_y: 0,
	y: function (value) {
		if (arguments.length === 1) {
			this._y = value;
			return value;
		} else {
			return this._y;
		}
	}
	,
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	serializeOverride: function () {
		return "x: " + this.x() + ", y: " + this.y() + ", width: " + this.width() + ", height: " + this.height();
	}
	,
	scaleByViewport: function (viewport) {
		this.x((this.x() - viewport.left()) / viewport.width());
		this.y((this.y() - viewport.top()) / viewport.height());
		this.width(this.width() / viewport.width());
		this.height(this.height() / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.x(), __y: this.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x() + this.width(), __y: this.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x() + this.width(), __y: this.y() + this.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x(), __y: this.y() + this.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('RectangleGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('EllipseGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Ellipse";
	}
	,
	_centerX: 0,
	centerX: function (value) {
		if (arguments.length === 1) {
			this._centerX = value;
			return value;
		} else {
			return this._centerX;
		}
	}
	,
	_centerY: 0,
	centerY: function (value) {
		if (arguments.length === 1) {
			this._centerY = value;
			return value;
		} else {
			return this._centerY;
		}
	}
	,
	_radiusX: 0,
	radiusX: function (value) {
		if (arguments.length === 1) {
			this._radiusX = value;
			return value;
		} else {
			return this._radiusX;
		}
	}
	,
	_radiusY: 0,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this._radiusY = value;
			return value;
		} else {
			return this._radiusY;
		}
	}
	,
	serializeOverride: function () {
		return "centerX: " + this.centerX() + ", centerY: " + this.centerY() + ", radiusX: " + this.radiusX() + ", radiusY: " + this.radiusY();
	}
	,
	scaleByViewport: function (viewport) {
		this.centerX((this.centerX() - viewport.left()) / viewport.width());
		this.centerX((this.centerY() - viewport.top()) / viewport.height());
		this.radiusX(this.radiusX() / viewport.width());
		this.radiusY(this.radiusY() / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.centerX(), __y: this.centerY(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('EllipseGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('PathFigureData', 'Object', {
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
		this.segments(new $.ig.List$1($.ig.SegmentData.prototype.$type, 0));
		this.startPoint(new $.ig.Point(0));
	},
	init1: function (initNumber, fig) {
		$.ig.Object.prototype.init.call(this);
		this.segments(new $.ig.List$1($.ig.SegmentData.prototype.$type, 0));
		this.startPoint(fig.__startPoint);
		for (var i = 0; i < fig.__segments.count(); i++) {
			var seg = fig.__segments.__inner[i];
			var newData = null;
			switch (seg.type()) {
				case $.ig.PathSegmentType.prototype.line:
					newData = new $.ig.LineSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.polyLine:
					newData = new $.ig.PolylineSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.arc:
					newData = new $.ig.ArcSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.polyBezier:
					newData = new $.ig.PolyBezierSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.bezier:
					newData = new $.ig.BezierSegmentData(1, seg);
					break;
			}
			this.segments().add(newData);
		}
	},
	_startPoint: null,
	startPoint: function (value) {
		if (arguments.length === 1) {
			this._startPoint = value;
			return value;
		} else {
			return this._startPoint;
		}
	}
	,
	_segments: null,
	segments: function (value) {
		if (arguments.length === 1) {
			this._segments = value;
			return value;
		} else {
			return this._segments;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		if ($.ig.Point.prototype.l_op_Inequality(this.startPoint(), null)) {
			sb.appendLine1("startPoint: { x: " + this.startPoint().__x + ", y: " + this.startPoint().__y + "}, ");
		}
		sb.appendLine1("segments: [");
		for (var i = 0; i < this.segments().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.segments().__inner[i].serialize());
		}
		sb.appendLine1("]");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		if ($.ig.Point.prototype.l_op_Inequality(this.startPoint(), null)) {
			this.startPoint({ __x: (this.startPoint().__x - viewport.left()) / viewport.width(), __y: (this.startPoint().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		for (var i = 0; i < this.segments().count(); i++) {
			this.segments().__inner[i].scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		if (!settings.ignoreFigureStartPoint()) {
			current.add({ __x: this.startPoint().__x, __y: this.startPoint().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		for (var i = 0; i < this.segments().count(); i++) {
			this.segments().__inner[i].getPointsOverride(current, settings);
		}
	}
	,
	$type: new $.ig.Type('PathFigureData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('SegmentData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	scaleByViewport: function (viewport) {
	}
	,
	getPointsOverride: function (current, settings) {
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("type: \"" + this.type() + "\", ");
		sb.appendLine1(this.serializeOverride());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('SegmentData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('LineSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.point(new $.ig.Point(0));
	},
	init1: function (initNumber, seg) {
		$.ig.SegmentData.prototype.init.call(this);
		this.point(seg.point());
	},
	type: function () {
		return "Line";
	}
	,
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
	serializeOverride: function () {
		return "point: { x: " + this.point().__x + ", y: " + this.point().__y + "}";
	}
	,
	scaleByViewport: function (viewport) {
		this.point({ __x: (this.point().__x - viewport.left()) / viewport.width(), __y: (this.point().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	getPointsOverride: function (current, settings) {
		current.add({ __x: this.point().__x, __y: this.point().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('LineSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('PolylineSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, poly) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		for (var i = 0; i < poly.__points.count(); i++) {
			this.points().add(poly.__points.__inner[i]);
		}
	},
	type: function () {
		return "Polyline";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('PolylineSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('BezierSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, segment) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.points().add(segment.point1());
		this.points().add(segment.point2());
		this.points().add(segment.point3());
	},
	type: function () {
		return "Bezier";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if ($.ig.Point.prototype.l_op_Equality(this.points().__inner[i], null)) {
				break;
			}
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			if ($.ig.Point.prototype.l_op_Equality(this.points().__inner[i], null)) {
				break;
			}
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('BezierSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('PolyBezierSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, poly) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		for (var i = 0; i < poly.points().count(); i++) {
			this.points().add(poly.points().__inner[i]);
		}
	},
	type: function () {
		return "PolyBezierSpline";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('PolyBezierSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('ArcSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.point(new $.ig.Point(0));
		this.isLargeArc(false);
		this.isCounterClockwise(true);
		this.rotationAngle(0);
	},
	init1: function (initNumber, arc) {
		$.ig.SegmentData.prototype.init.call(this);
		this.point(arc.point());
		this.isLargeArc(arc.isLargeArc());
		this.isCounterClockwise(arc.sweepDirection() == $.ig.SweepDirection.prototype.counterclockwise);
		this.sizeX(arc.size().width());
		this.sizeY(arc.size().height());
		this.rotationAngle(arc.rotationAngle());
	},
	type: function () {
		return "Arc";
	}
	,
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
	_isLargeArc: false,
	isLargeArc: function (value) {
		if (arguments.length === 1) {
			this._isLargeArc = value;
			return value;
		} else {
			return this._isLargeArc;
		}
	}
	,
	_isCounterClockwise: false,
	isCounterClockwise: function (value) {
		if (arguments.length === 1) {
			this._isCounterClockwise = value;
			return value;
		} else {
			return this._isCounterClockwise;
		}
	}
	,
	_sizeX: 0,
	sizeX: function (value) {
		if (arguments.length === 1) {
			this._sizeX = value;
			return value;
		} else {
			return this._sizeX;
		}
	}
	,
	_sizeY: 0,
	sizeY: function (value) {
		if (arguments.length === 1) {
			this._sizeY = value;
			return value;
		} else {
			return this._sizeY;
		}
	}
	,
	_rotationAngle: 0,
	rotationAngle: function (value) {
		if (arguments.length === 1) {
			this._rotationAngle = value;
			return value;
		} else {
			return this._rotationAngle;
		}
	}
	,
	serializeOverride: function () {
		return "point: { x: " + this.point().__x + ", y: " + this.point().__y + " }, isLargeArc: " + (this.isLargeArc() ? "true" : "false") + ", isCounterClockwise: " + (this.isCounterClockwise() ? "true" : "false") + ", sizeX: " + this.sizeX() + ", sizeY: " + this.sizeY() + ", rotationAngle: " + this.rotationAngle();
	}
	,
	scaleByViewport: function (viewport) {
		this.point({ __x: (this.point().__x - viewport.left()) / viewport.width(), __y: (this.point().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.sizeX(this.sizeX() / viewport.width());
		this.sizeY(this.sizeY() / viewport.height());
	}
	,
	getPointsOverride: function (current, settings) {
		current.add({ __x: this.point().__x, __y: this.point().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('ArcSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('AppearanceHelper', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	fromBrush: function (b) {
		if (b == null) {
			return $.ig.Color.prototype.fromArgb(0, 0, 0, 0);
		}
		if ($.ig.Color.prototype.l_op_Equality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, b.color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null))) {
			return $.ig.Color.prototype.fromArgb(0, 0, 0, 0);
		}
		return b.color();
	}
	,
	fromBrushExtended: function (b) {
		if (b == null) {
			return null;
		}
		if (b._isGradient) {
			var linear = new $.ig.LinearGradientBrushAppearanceData();
			var inLinear = b;
			linear.startX(inLinear._startX);
			linear.startY(inLinear._startY);
			linear.endX(inLinear._endX);
			linear.endY(inLinear._endY);
			var $t = inLinear._gradientStops;
			for (var i = 0; i < $t.length; i++) {
				var stop = $t[i];
				var newStop = new $.ig.GradientStopAppearanceData();
				newStop.colorValue(stop.color());
				newStop.offset(stop._offset);
				linear.stops().add(newStop);
			}
			return linear;
		} else if (b._isRadialGradient) {
			return null;
		} else {
			var solid = new $.ig.SolidBrushAppearanceData();
			solid.colorValue(b.color());
			return solid;
		}
		return null;
	}
	,
	getCanvasLeft: function (visual) {
		return visual.canvasLeft();
	}
	,
	getCanvasTop: function (visual) {
		return visual.canvasTop();
	}
	,
	getCanvasZIndex: function (line) {
		return line.canvasZIndex();
	}
	,
	fromPathData: function (path) {
		return $.ig.AppearanceHelper.prototype.fromGeometry(path.data());
	}
	,
	fromLineData: function (line) {
		var lineGeometry = new $.ig.LineGeometry();
		lineGeometry.endPoint({ __x: line.x2(), __y: line.y2(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		lineGeometry.startPoint({ __x: line.x1(), __y: line.y1(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		return $.ig.AppearanceHelper.prototype.fromGeometry(lineGeometry);
	}
	,
	fromGeometry: function (data) {
		if (data == null) {
			return new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		}
		if ($.ig.util.cast($.ig.GeometryGroup.prototype.$type, data) !== null) {
			var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
			var group = data;
			for (var i = 0; i < group.children().count(); i++) {
				var items = $.ig.AppearanceHelper.prototype.fromGeometry(group.children().__inner[i]);
				for (var j = 0; j < items.count(); j++) {
					ret.add(items.__inner[j]);
				}
			}
			return ret;
		} else if ($.ig.util.cast($.ig.PathGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromPathGeometry(data);
		} else if ($.ig.util.cast($.ig.LineGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromLineGeometry(data);
		} else if ($.ig.util.cast($.ig.RectangleGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromRectangleGeometry(data);
		} else if ($.ig.util.cast($.ig.EllipseGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromEllipseGeometry(data);
		} else {
			throw new $.ig.Error(1, "not supported");
		}
	}
	,
	fromEllipseGeometry: function (ellipseGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.EllipseGeometryData();
		ret.add(newData);
		newData.centerX(ellipseGeometry.center().__x);
		newData.centerY(ellipseGeometry.center().__y);
		newData.radiusX(ellipseGeometry.radiusX());
		newData.radiusY(ellipseGeometry.radiusY());
		return ret;
	}
	,
	fromRectangleGeometry: function (rectangleGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.RectangleGeometryData();
		ret.add(newData);
		newData.x(rectangleGeometry.rect().x());
		newData.y(rectangleGeometry.rect().y());
		newData.width(rectangleGeometry.rect().width());
		newData.height(rectangleGeometry.rect().height());
		return ret;
	}
	,
	fromLineGeometry: function (lineGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.LineGeometryData();
		ret.add(newData);
		newData.x1(lineGeometry.startPoint().__x);
		newData.y1(lineGeometry.startPoint().__y);
		newData.x2(lineGeometry.endPoint().__x);
		newData.y2(lineGeometry.endPoint().__y);
		return ret;
	}
	,
	fromPathGeometry: function (pathGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.PathGeometryData();
		ret.add(newData);
		for (var i = 0; i < pathGeometry.figures().count(); i++) {
			var figure = pathGeometry.figures().__inner[i];
			var f = new $.ig.PathFigureData(1, figure);
			newData.figures().add(f);
		}
		return ret;
	}
	,
	getShapeAppearance: function (appearance, path) {
		appearance.stroke($.ig.AppearanceHelper.prototype.fromBrush(path.__stroke));
		appearance.fill($.ig.AppearanceHelper.prototype.fromBrush(path.__fill));
		appearance.strokeExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(path.__stroke));
		appearance.fillExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(path.__fill));
		appearance.strokeThickness(path.strokeThickness());
		appearance.dashArray(null);
		if (path.strokeDashArray() != null) {
			appearance.dashArray(path.strokeDashArray().asArray());
		}
		appearance.dashCap(path.strokeDashCap());
		appearance.visibility(path.__visibility);
		appearance.opacity(path.__opacity);
		appearance.canvasLeft($.ig.AppearanceHelper.prototype.getCanvasLeft(path));
		appearance.canvasTop($.ig.AppearanceHelper.prototype.getCanvasTop(path));
		appearance.canvaZIndex($.ig.AppearanceHelper.prototype.getCanvasZIndex(path));
	}
	,
	fromTextElement: function (frameworkElement, fontInfo) {
		var lad = new $.ig.LabelAppearanceData();
		var tb = frameworkElement;
		lad.text(tb.text());
		lad.labelBrush($.ig.AppearanceHelper.prototype.fromBrush(tb.fill()));
		lad.labelBrushExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(tb.fill()));
		lad.visibility((tb.__visibility == $.ig.Visibility.prototype.visible) ? true : false);
		lad.opacity(tb.__opacity);
		if (fontInfo != null) {
			if (fontInfo.fontFamily() != null) {
				lad.fontFamily(fontInfo.fontFamily());
			}
			if (!$.ig.util.isNaN(fontInfo.fontSize())) {
				lad.fontSize(fontInfo.fontSize());
			}
			if (fontInfo.fontWeight() != null) {
				lad.fontWeight(fontInfo.fontWeight());
			}
			if (fontInfo.fontStyle() != null) {
				lad.fontStyle(fontInfo.fontStyle());
			}
			if (fontInfo.fontStretch() != null) {
				lad.fontStretch(fontInfo.fontStyle());
			}
		}
		var angle = 0;
		var xForm = tb.renderTransform();
		if ($.ig.util.cast($.ig.RotateTransform.prototype.$type, xForm) !== null) {
			var rt = $.ig.util.cast($.ig.RotateTransform.prototype.$type, xForm);
			angle = rt.angle();
		} else if ($.ig.util.cast($.ig.TransformGroup.prototype.$type, xForm) !== null) {
			var tg = $.ig.util.cast($.ig.TransformGroup.prototype.$type, xForm);
			var en = tg.children().getEnumerator();
			while (en.moveNext()) {
				var child = en.current();
				if ($.ig.util.cast($.ig.RotateTransform.prototype.$type, child) !== null) {
					var rt1 = $.ig.util.cast($.ig.RotateTransform.prototype.$type, child);
					angle = rt1.angle();
					break;
				}
			}
		}
		lad.angle(angle);
		return lad;
	}
	,
	serializeColor: function (color) {
		return "{ r: " + color.r() + ", g: " + color.g() + ", b: " + color.b() + ", a: " + color.a() + "}";
	}
	,
	serializeItems: function (sb, name, items, isFirst) {
		if (items != null) {
			if (!isFirst) {
				sb.append5(", ");
			}
			sb.append5(name);
			sb.append5(": [");
			var addedFirst = false;
			var en = items.getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				if (addedFirst) {
					sb.appendLine1(", ");
				} else {
					addedFirst = true;
				}
				sb.append5(item.serialize());
			}
			sb.appendLine1("]");
			return true;
		}
		return false;
	}
	,
	serializeItem: function (sb, name, item, isFirst) {
		if (item != null) {
			if (!isFirst) {
				sb.append5(", ");
			}
			sb.append5(name);
			sb.append5(": ");
			sb.appendLine1(item.serialize());
			return true;
		}
		return false;
	}
	,
	$type: new $.ig.Type('AppearanceHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.FastItemsSourceEventAction.prototype.remove = 0;
$.ig.FastItemsSourceEventAction.prototype.insert = 1;
$.ig.FastItemsSourceEventAction.prototype.replace = 2;
$.ig.FastItemsSourceEventAction.prototype.change = 3;
$.ig.FastItemsSourceEventAction.prototype.reset = 4;

$.ig.InterpolationMode.prototype.rGB = 0;
$.ig.InterpolationMode.prototype.hSV = 1;

$.ig.BaseDOMEventProxy.prototype.nullTimer = -1;
$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = false;
$.ig.BaseDOMEventProxy.prototype.pointerEnabled = false;
$.ig.BaseDOMEventProxy.prototype.tridentVersion = 0;
$.ig.BaseDOMEventProxy.prototype.edgeVersion = 0;

$.ig.DOMEventProxy.prototype.__proxyCount = 0;

$.ig.ColorUtil.prototype._r = new $.ig.Random(0);
$.ig.ColorUtil.prototype.__randomColors = null;

$.ig.CssHelper.prototype.defaultMarginValue = "-4321px";
$.ig.CssHelper.prototype.defaultColorValue = "rgb(3, 2, 1)";
$.ig.CssHelper.prototype.defaultBackgroundImageValue = "none";
$.ig.CssHelper.prototype.defaultTextAlignValue = "justify";
$.ig.CssHelper.prototype.defaultVerticalAlignValue = "baseline";
$.ig.CssHelper.prototype.defaultOpacityValue = "0.888";
$.ig.CssHelper.prototype.defaultVisibilityValue = "hidden";
$.ig.CssHelper.prototype.defaultWidthHeightValue = "4321px";
$.ig.CssHelper.prototype.maxClasses = 500;

$.ig.BrushCollection.prototype._random = new $.ig.Random(0);

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
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('MouseButtonAction', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Up";
			case 1: return "Down";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('MouseButtonAction', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('MouseButtonType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Left";
			case 1: return "Right";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('MouseButtonType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('OuterLabelAlignment', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Left";
			case 1: return "Right";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('OuterLabelAlignment', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('FunnelSliceDisplay', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Uniform";
			case 1: return "Weighted";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('FunnelSliceDisplay', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('OthersCategoryType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Number";
			case 1: return "Percent";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('OthersCategoryType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('LabelsPosition', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Center";
			case 2: return "InsideEnd";
			case 3: return "OutsideEnd";
			case 4: return "BestFit";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('LabelsPosition', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('LeaderLineType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Straight";
			case 1: return "Arc";
			case 2: return "Spline";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('LeaderLineType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('ItemsSourceAction', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Remove";
			case 1: return "Insert";
			case 2: return "Replace";
			case 3: return "Change";
			case 4: return "Reset";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ItemsSourceAction', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('LegendBase', 'ContentControl', {
	createView: function () {
		return new $.ig.LegendBaseView(this);
	}
	,
	onViewCreated: function (view) {
		this.view(view);
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	init: function () {
		$.ig.ContentControl.prototype.init.call(this);
		var view = this.createView();
		this.onViewCreated(view);
		view.onInit();
		this.children(new $.ig.ObservableCollection$1($.ig.UIElement.prototype.$type, 0));
	},
	addChildInOrder: function (legendItem, series) {
	}
	,
	createLegendItems: function (legendItems, itemsHost) {
	}
	,
	_children: null,
	children: function (value) {
		if (arguments.length === 1) {
			this._children = value;
			return value;
		} else {
			return this._children;
		}
	}
	,
	_seriesOwner: null,
	seriesOwner: function (value) {
		if (arguments.length === 1) {
			this._seriesOwner = value;
			return value;
		} else {
			return this._seriesOwner;
		}
	}
	,
	_chartOwner: null,
	chartOwner: function (value) {
		if (arguments.length === 1) {
			this._chartOwner = value;
			return value;
		} else {
			return this._chartOwner;
		}
	}
	,
	owner: function () {
		if (this.seriesOwner() != null) {
			return this.seriesOwner();
		} else {
			return this.chartOwner();
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	legendItemMouseLeftButtonDown: null,
	onLegendItemMouseLeftButtonDown: function (args) {
		if (this.legendItemMouseLeftButtonDown != null) {
			this.legendItemMouseLeftButtonDown(this, args);
		}
	}
	,
	legendItemMouseLeftButtonUp: null,
	onLegendItemMouseLeftButtonUp: function (args) {
		if (this.legendItemMouseLeftButtonUp != null) {
			this.legendItemMouseLeftButtonUp(this, args);
		}
	}
	,
	legendItemMouseEnter: null,
	onLegendItemMouseEnter: function (args) {
		if (this.legendItemMouseEnter != null) {
			this.legendItemMouseEnter(this, args);
		}
	}
	,
	legendItemMouseLeave: null,
	onLegendItemMouseLeave: function (args) {
		if (this.legendItemMouseLeave != null) {
			this.legendItemMouseLeave(this, args);
		}
	}
	,
	legendItemMouseMove: null,
	onLegendItemMouseMove: function (args) {
		if (this.legendItemMouseMove != null) {
			this.legendItemMouseMove(this, args);
		}
	}
	,
	provideContainer: function (container) {
		this.view().onContainerProvided(container);
	}
	,
	legendItemsListStyle: function (value) {
		if (arguments.length === 1) {
			this.view().legendItemsListStyle(value);
			return value;
		} else {
			return this.view().legendItemsListStyle();
		}
	}
	,
	legendItemStyle: function (value) {
		if (arguments.length === 1) {
			this.view().legendItemStyle(value);
			return value;
		} else {
			return this.view().legendItemStyle();
		}
	}
	,
	legendItemBadgeStyle: function (value) {
		if (arguments.length === 1) {
			this.view().legendItemBadgeStyle(value);
			return value;
		} else {
			return this.view().legendItemBadgeStyle();
		}
	}
	,
	legendItemTextStyle: function (value) {
		if (arguments.length === 1) {
			this.view().legendItemTextStyle(value);
			return value;
		} else {
			return this.view().legendItemTextStyle();
		}
	}
	,
	exportVisualData: function () {
		var legend = new $.ig.LegendVisualData();
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var child = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, child);
			var item = new $.ig.LegendItemVisualData();
			if (contentControl.content().getType() == $.ig.PieSliceDataContext.prototype.$type) {
				var context = ($.ig.util.cast($.ig.PieSliceDataContext.prototype.$type, contentControl.content()));
				item.label(context.legendLabel());
				item.appearance(new $.ig.PrimitiveAppearanceData());
				if (context.actualItemBrush() != null) {
					item.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(context.actualItemBrush()));
				} else {
					item.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(context.itemBrush()));
				}
			} else if (contentControl.content().getType() == $.ig.FunnelSliceDataContext.prototype.$type) {
				var context1 = ($.ig.util.cast($.ig.FunnelSliceDataContext.prototype.$type, contentControl.content()));
				item.label(context1.itemLabel());
				item.appearance(new $.ig.PrimitiveAppearanceData());
				item.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(context1.actualItemBrush()));
			} else if (contentControl.content().getType() == $.ig.DataContext.prototype.$type) {
				var context2 = ($.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content()));
				item.label(context2.itemLabel());
				item.appearance(new $.ig.PrimitiveAppearanceData());
				item.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(context2.actualItemBrush()));
			}
			legend.items().add(item);
		}
		return legend;
	}
	,
	$type: new $.ig.Type('LegendBase', $.ig.ContentControl.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('PieChartBaseView', 'Object', {
	init: function (model) {
		this.__dirty = false;
		this.__renderedRect = $.ig.Rect.prototype.empty();
		$.ig.Object.prototype.init.call(this);
		this.model(model);
		this.viewManager(new $.ig.PieChartViewManager(this));
	},
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	onInit: function () {
		this.fontBrush(null);
		this.toolTipContext(new $.ig.PieSliceDataContext());
		this.toolTipContext().series(this.model());
		this.viewport($.ig.Rect.prototype.empty());
		this.slices(new $.ig.List$1($.ig.Slice.prototype.$type, 0));
		this.labels(new $.ig.List$1($.ig.PieLabel.prototype.$type, 0));
		this.model().legendItemTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.pieLegendItemRender);
			$ret.measure($.ig.LegendTemplates.prototype.defaultLegendItemMeasure);
			return $ret;
		}()));
		this.model().legendItemBadgeTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.pieBadgeTemplate);
			$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
			return $ret;
		}()));
	}
	,
	setDefaultBrushes: function () {
		var brushes = new $.ig.BrushCollection();
		var outlines = new $.ig.BrushCollection();
		var fontBrush;
		var font;
		var $ret = this.viewManager().getDefaultStyle(brushes, outlines, fontBrush, font);
		brushes = $ret.p0;
		outlines = $ret.p1;
		fontBrush = $ret.p2;
		font = $ret.p3;
		if (this.model().brushes() == null) {
			this.model().brushes(brushes);
		}
		if (this.model().outlines() == null) {
			this.model().outlines(outlines);
		}
		this.fontBrush(fontBrush);
		if (this.model().textStyle() != null) {
			this.font(this.model().getFontInfo());
		} else {
			this.font(font);
		}
	}
	,
	_fontBrush: null,
	fontBrush: function (value) {
		if (arguments.length === 1) {
			this._fontBrush = value;
			return value;
		} else {
			return this._fontBrush;
		}
	}
	,
	_font: null,
	font: function (value) {
		if (arguments.length === 1) {
			this._font = value;
			return value;
		} else {
			return this._font;
		}
	}
	,
	_backgroundContext: null,
	backgroundContext: function (value) {
		if (arguments.length === 1) {
			this._backgroundContext = value;
			return value;
		} else {
			return this._backgroundContext;
		}
	}
	,
	_mainContext: null,
	mainContext: function (value) {
		if (arguments.length === 1) {
			this._mainContext = value;
			return value;
		} else {
			return this._mainContext;
		}
	}
	,
	_labelContext: null,
	labelContext: function (value) {
		if (arguments.length === 1) {
			this._labelContext = value;
			return value;
		} else {
			return this._labelContext;
		}
	}
	,
	_overlayContext: null,
	overlayContext: function (value) {
		if (arguments.length === 1) {
			this._overlayContext = value;
			return value;
		} else {
			return this._overlayContext;
		}
	}
	,
	_viewManager: null,
	viewManager: function (value) {
		if (arguments.length === 1) {
			this._viewManager = value;
			return value;
		} else {
			return this._viewManager;
		}
	}
	,
	onContainerProvided: function (container) {
		if (container == null) {
			this.__dirty = false;
			this.viewManager().onContainerProvided(null);
			this.backgroundContext(null);
			this.mainContext(null);
			this.labelContext(null);
			this.overlayContext(null);
			return;
		}
		this.viewManager().onContainerProvided(container);
		this.backgroundContext(this.viewManager().backgroundContext());
		this.mainContext(this.viewManager().mainContext());
		this.labelContext(this.viewManager().labelContext());
		this.overlayContext(this.viewManager().overlayContext());
	}
	,
	getContainerRect: function () {
		return this.viewport();
	}
	,
	getContainerOffsets: function () {
		return this.viewManager().getContainerOffsets();
	}
	,
	resize: function () {
		this.viewport(this.viewManager().resize());
		this.model().onSizeUpdated();
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
	__dirty: false,
	makeDirty: function () {
		if (!this.__dirty) {
			this.__dirty = true;
			this.viewManager().queueWork(this.undirty.runOn(this));
		}
	}
	,
	undirty: function () {
		if (this.__dirty) {
			this.__dirty = false;
			this.render();
		}
	}
	,
	__renderedRect: null,
	render: function () {
		this.ensureContextFont();
		if (!this.__renderedRect.isEmpty()) {
			if (this.overlayContext().shouldRender()) {
				this.overlayContext().clearRectangle(this.__renderedRect.left(), this.__renderedRect.top(), this.__renderedRect.width(), this.__renderedRect.height());
			}
			if (this.labelContext().shouldRender()) {
				this.labelContext().clearRectangle(this.__renderedRect.left(), this.__renderedRect.top(), this.__renderedRect.width(), this.__renderedRect.height());
			}
			if (this.mainContext().shouldRender()) {
				this.mainContext().clearRectangle(this.__renderedRect.left(), this.__renderedRect.top(), this.__renderedRect.width(), this.__renderedRect.height());
			}
			if (this.backgroundContext().shouldRender()) {
				this.backgroundContext().clearRectangle(this.__renderedRect.left(), this.__renderedRect.top(), this.__renderedRect.width(), this.__renderedRect.height());
			}
		}
		this.renderOverride();
	}
	,
	renderOverride: function () {
		this.__renderedRect = this.viewport();
		if (this.mainContext() != null && this.mainContext().shouldRender()) {
			var en = this.slices().getEnumerator();
			while (en.moveNext()) {
				var slice = en.current();
				if (slice.__visibility == $.ig.Visibility.prototype.visible) {
					var slicePath = slice.view().getSlicePath();
					this.viewManager().setDefaultSliceBrushes(slicePath);
					slicePath.strokeThickness(1);
					slicePath.__opacity = slice.__opacity;
					if (slice.background() != null) {
						slicePath.__fill = slice.background();
					}
					if (slice.borderBrush() != null) {
						slicePath.__stroke = slice.borderBrush();
					}
					if (slice.style() != null) {
						this.mainContext().applyStyle(slicePath, slice.style());
					}
					if (slicePath.renderTransform() != null) {
						this.mainContext().save();
						this.mainContext().applyTransform(slicePath.renderTransform());
					}
					var fillBrush = slicePath.__fill;
					var strokeBrush = slicePath.__stroke;
					var changed = this.centerRadialBrushes(slice, slicePath, this.mainContext());
					this.mainContext().renderPath(slicePath);
					if (changed) {
						slicePath.__fill = fillBrush;
						slicePath.__stroke = strokeBrush;
					}
					if (slicePath.renderTransform() != null) {
						this.mainContext().restore();
					}
				}
			}
		}
		if (this.labelContext() != null && this.labelContext().shouldRender()) {
			var font = this.font();
			if (this.model().textStyle() != null) {
				font = this.model().getFontInfo();
			}
			this.labelContext().setFontInfo(font);
			var en1 = this.labels().getEnumerator();
			while (en1.moveNext()) {
				var label = en1.current();
				if (label.__visibility == $.ig.Visibility.prototype.visible) {
					var txt = label.label();
					if (txt != null) {
						{
							txt.fill(this.fontBrush());
							this.labelContext().renderTextBlock(txt);
						}
					}
					var sliceBackground = label.slice().background();
					if (label.slice().style() != null) {
						var path = new $.ig.Path();
						this.mainContext().applyStyle(path, label.slice().style());
						sliceBackground = path.__fill;
					}
					if (this.model().leaderLineType() != $.ig.LeaderLineType.prototype.straight && label.leaderLinePath() != null) {
						label.leaderLinePath().__stroke = sliceBackground;
						label.leaderLinePath().strokeThickness(1);
						if (this.model().leaderLineStyle() != null) {
							this.labelContext().applyStyle(label.leaderLinePath(), this.model().leaderLineStyle());
						}
						this.labelContext().renderPath(label.leaderLinePath());
					}
					if (this.model().leaderLineType() == $.ig.LeaderLineType.prototype.straight && label.leaderLine() != null) {
						label.leaderLine().__stroke = sliceBackground;
						label.leaderLine().strokeThickness(1);
						if (this.model().leaderLineStyle() != null) {
							this.labelContext().applyStyle(label.leaderLine(), this.model().leaderLineStyle());
						}
						this.labelContext().renderLine(label.leaderLine());
					}
				}
			}
		}
	}
	,
	centerRadialBrushes: function (slice, slicePath, context) {
		return false;
	}
	,
	canvasMouseMove: function (p, onMouseMove, isFinger) {
		var prev = this.activeSlice();
		this.updateActiveSlice(p);
		if (this.activeSlice() == null && prev != null) {
			var me = (function () {
				var $ret = new $.ig.MouseEventArgs();
				$ret.position(p);
				return $ret;
			}());
			this.model().itemMouseLeft(this.activeSlice(), me);
		} else if (this.activeSlice() != null) {
			var me1 = (function () {
				var $ret = new $.ig.MouseEventArgs();
				$ret.position(p);
				return $ret;
			}());
			this.model().itemMouseMoved(this.activeSlice(), me1);
		}
	}
	,
	canvasMouseLeave: function (p) {
		var prev = this.activeSlice();
		this.updateActiveSlice(p);
		var me = (function () {
			var $ret = new $.ig.MouseEventArgs();
			$ret.position(p);
			return $ret;
		}());
		this.model().itemMouseLeft(prev, me);
	}
	,
	canvasMouseDown: function (p) {
		this.updateActiveSlice(p);
	}
	,
	canvasMouseUp: function (p) {
		var me = (function () {
			var $ret = new $.ig.MouseButtonEventArgs();
			$ret.position(p);
			return $ret;
		}());
		var shouldFireSliceClick = true;
		var labelClicked = null;
		var en = this.labels().getEnumerator();
		while (en.moveNext()) {
			var pl = en.current();
			var containsPoint = pl.bounds().containsPoint(p);
			if (containsPoint) {
				labelClicked = pl;
				shouldFireSliceClick = this.model().labelClicked(pl.slice(), me);
				break;
			}
		}
		this.updateActiveSlice(p);
		if (this.activeSlice() == null && shouldFireSliceClick && labelClicked != null) {
			this.model().sliceClicked(labelClicked.slice(), me);
		} else if (this.activeSlice() != null && shouldFireSliceClick) {
			this.model().sliceClicked(this.activeSlice(), me);
		}
	}
	,
	_activeSlice: null,
	activeSlice: function (value) {
		if (arguments.length === 1) {
			this._activeSlice = value;
			return value;
		} else {
			return this._activeSlice;
		}
	}
	,
	updateActiveSlice: function (p) {
		this.activeSlice(null);
		for (var i = 0; i < this.slices().count(); i++) {
			if (this.slices().__inner[i].__visibility == $.ig.Visibility.prototype.visible && this.slices().__inner[i].containsPoint(p)) {
				this.activeSlice(this.slices().__inner[i]);
				break;
			}
		}
	}
	,
	sizeUpdated: function () {
		this.model().onSizeUpdated();
	}
	,
	closeToolTip: function () {
		this.hideTooltip();
	}
	,
	updateToolTip: function (item, args) {
		var me = args;
		this.updateToolTip1(me.position(), item, args);
	}
	,
	_slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this._slices = value;
			return value;
		} else {
			return this._slices;
		}
	}
	,
	sliceCreate: function () {
		var slice = new $.ig.Slice();
		slice.owner(this.model());
		this.slices().add(slice);
		return slice;
	}
	,
	sliceActivate: function (slice) {
		slice.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	sliceDisactivate: function (slice) {
		slice.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	sliceDestroy: function (slice) {
		slice.owner(null);
		this.slices().remove(slice);
	}
	,
	_labels: null,
	labels: function (value) {
		if (arguments.length === 1) {
			this._labels = value;
			return value;
		} else {
			return this._labels;
		}
	}
	,
	labelCreate: function () {
		var label = new $.ig.PieLabel();
		if (this.model().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
			label.leaderLine(new $.ig.Line());
		} else {
			label.leaderLinePath(new $.ig.Path());
			var geom = new $.ig.PathGeometry();
			geom.figures().add(new $.ig.PathFigure());
			geom.figures().__inner[0].__segments.add(new $.ig.BezierSegment(0));
			label.leaderLinePath().data(geom);
		}
		this.labels().add(label);
		return label;
	}
	,
	labelActivate: function (label) {
		label.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	labelDisactivate: function (label) {
		label.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	labelDestroy: function (label) {
		this.labels().remove(label);
	}
	,
	updateLabelLeaderLine: function (label) {
		if ((this.model().leaderLineType() != $.ig.LeaderLineType.prototype.straight && label.leaderLinePath() != null) || (this.model().leaderLineType() == $.ig.LeaderLineType.prototype.straight && label.leaderLine() != null)) {
			return;
		}
		if (this.model().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
			label.leaderLinePath(null);
			label.leaderLine(new $.ig.Line());
		} else {
			label.leaderLine(null);
			label.leaderLinePath(new $.ig.Path());
			var geom = new $.ig.PathGeometry();
			geom.figures().add(new $.ig.PathFigure());
			geom.figures().__inner[0].__segments.add(new $.ig.BezierSegment(0));
			label.leaderLinePath().data(geom);
		}
	}
	,
	setSliceAppearance: function (slice) {
		if (this.model().othersCategoryStyle() != null && slice.isOthersSlice()) {
			slice.style(this.model().othersCategoryStyle());
		} else if (slice.isSelected() && this.model().allowSliceSelection() && this.model().selectedStyle() != null) {
			slice.style(this.model().selectedStyle());
		} else {
			slice.style(null);
			var background;
			var borderBrush;
			if (this.model().brushes() != null && slice.index() >= 0 && this.model().brushes().count() > 0) {
				background = this.model().brushes().item(slice.index() % this.model().brushes().count());
			} else {
				background = null;
			}
			if (this.model().outlines() != null && slice.index() >= 0 && this.model().outlines().count() > 0) {
				borderBrush = this.model().outlines().item(slice.index() % this.model().outlines().count());
			} else {
				borderBrush = null;
			}
			slice.background(background);
			slice.borderBrush(borderBrush);
		}
	}
	,
	getLabel: function (slice) {
		if (slice == null || slice.label() == null) {
			return this.model().labelMemberPath();
		}
		var pieLabel = slice.label();
		var label = pieLabel.label();
		if ($.ig.util.cast($.ig.TextBlock.prototype.$type, label) !== null) {
			return (label).text();
		}
		return label;
	}
	,
	getDesiredWidth: function (element) {
		var tb = $.ig.util.cast($.ig.TextBlock.prototype.$type, element);
		if (tb != null && tb.text() != null) {
			return this.labelContext().measureTextWidth(tb.text()) + $.ig.PieChartBaseView.prototype.tEXT_MARGIN;
		}
		return 0;
	}
	,
	updateCurrentFontHeight: function () {
		this.fontHeight($.ig.FontUtil.prototype.getCurrentFontHeight(this.viewManager().getOwnerFont()));
	}
	,
	_fontHeight: 0,
	fontHeight: function (value) {
		if (arguments.length === 1) {
			this._fontHeight = value;
			return value;
		} else {
			return this._fontHeight;
		}
	}
	,
	getDesiredHeight: function (element) {
		return this.fontHeight() + $.ig.PieChartBaseView.prototype.tEXT_MARGIN;
	}
	,
	getLabelBounds: function (label) {
		var lbl = label.label();
		if ($.ig.util.cast($.ig.TextBlock.prototype.$type, lbl) !== null) {
			var desiredWidth = this.getDesiredWidth(lbl);
			var desiredHeight = this.getDesiredHeight(lbl);
			return new $.ig.Rect(0, 0, 0, desiredWidth, desiredHeight);
		}
		return $.ig.Rect.prototype.empty();
	}
	,
	updatePieViewport: function () {
		return new $.ig.Size(1, this.viewport().width(), this.viewport().height());
	}
	,
	updateLabelPosition: function (label, x, y) {
		var txt = $.ig.util.cast($.ig.TextBlock.prototype.$type, label.label());
		if (txt != null) {
			txt.canvasLeft(x);
			txt.canvasTop(y);
		}
		this.makeDirty();
	}
	,
	updateToolTipContent: function (toolTip) {
		this.updateToolTipValue(toolTip);
	}
	,
	updateView: function () {
		this.makeDirty();
	}
	,
	labelPreMeasure: function () {
		this.ensureContextFont();
	}
	,
	ensureContextFont: function () {
		var font = this.font();
		if (this.model().textStyle() != null) {
			font = this.model().getFontInfo();
		}
		if (this.labelContext() != null) {
			this.labelContext().setFontInfo(font);
		}
	}
	,
	onContainerResized: function (width, height) {
		this.viewManager().onContainerResized(width, height);
	}
	,
	notifyContainerResized: function () {
		this.viewManager().notifyContainerResized();
	}
	,
	_toolTipPosition: null,
	toolTipPosition: function (value) {
		if (arguments.length === 1) {
			this._toolTipPosition = value;
			return value;
		} else {
			return this._toolTipPosition;
		}
	}
	,
	_toolTipItem: null,
	toolTipItem: function (value) {
		if (arguments.length === 1) {
			this._toolTipItem = value;
			return value;
		} else {
			return this._toolTipItem;
		}
	}
	,
	_toolTipVisible: false,
	toolTipVisible: function (value) {
		if (arguments.length === 1) {
			this._toolTipVisible = value;
			return value;
		} else {
			return this._toolTipVisible;
		}
	}
	,
	_toolTipContext: null,
	toolTipContext: function (value) {
		if (arguments.length === 1) {
			this._toolTipContext = value;
			return value;
		} else {
			return this._toolTipContext;
		}
	}
	,
	hideTooltip: function () {
		if (this.toolTipVisible() == true) {
			this.toolTipVisible(false);
			this.viewManager().hideTooltip(this.toolTipContext());
		}
	}
	,
	updateToolTipValue: function (p) {
		this.viewManager().updateTooltipValue(p);
	}
	,
	updateToolTip1: function (pt, item, data) {
		this.toolTipVisible(true);
		this.toolTipPosition(pt);
		if (item == null) {
			this.toolTipItem(null);
			this.toolTipContext().item(null);
		} else {
			this.toolTipItem((item).dataContext());
			this.toolTipContext().item((item).dataContext());
		}
		var offsets = this.model().getContainerOffsets();
		var pos_ = { __x: pt.__x + 15 + offsets.__x, __y: pt.__y + 15 + offsets.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var context_ = this.toolTipContext();
		this.viewManager().updateTooltipPosition(pos_, context_);
	}
	,
	styleUpdated: function () {
		this.setDefaultBrushes();
		this.updateCurrentFontHeight();
		this.model().renderChart();
	}
	,
	onIsSurfaceInteractionDisabledChanged: function () {
		this.viewManager().onIsSurfaceInteractionDisabledChanged(this.model().isSurfaceInteractionDisabled());
	}
	,
	getContainer: function () {
		return this.viewManager().getContainer();
	}
	,
	flush: function () {
		if (this.__dirty) {
			this.undirty();
		}
	}
	,
	ready: function () {
		return this.getContainer() != null;
	}
	,
	exportViewData: function (visualData) {
		this.viewManager().exportViewData(visualData);
	}
	,
	isValid: function () {
		return true;
	}
	,
	preRender: function () {
	}
	,
	getActualBackground: function (slice) {
		var bg = this.viewManager().getStyleBackground(slice.style());
		if (bg != null) {
			return bg;
		}
		return slice.background();
	}
	,
	getActualOutline: function (slice) {
		var outline = this.viewManager().getStyleOutline(slice.style());
		if (outline != null) {
			return outline;
		}
		return slice.borderBrush();
	}
	,
	getOriginalEvent: function (args) {
		return this.viewManager().eventProxy().originalEvent();
	}
	,
	$type: new $.ig.Type('PieChartBaseView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IOuterLabelWidthDecider', 'Object', {
	$type: new $.ig.Type('IOuterLabelWidthDecider', null)
}, true);

$.ig.util.defType('IFunnelLabelSizeDecider', 'Object', {
	$type: new $.ig.Type('IFunnelLabelSizeDecider', null)
}, true);

$.ig.util.defType('XamFunnelView', 'Object', {
	_messageHandler: null,
	__serviceProvider: null,
	serviceProvider: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__serviceProvider;
			this.__serviceProvider = value;
			this.onServiceProviderChanged(oldValue, this.__serviceProvider);
			return value;
		} else {
			return this.__serviceProvider;
		}
	}
	,
	_interactionMessages: null,
	canvasMouseLeave: function (p) {
		this._interactionMessages.sendMessage(new $.ig.MouseLeaveMessage());
	}
	,
	canvasMouseMove: function (p, onMouseMove, isFinger) {
		var m = new $.ig.MouseMoveMessage();
		m.position(p);
		this._interactionMessages.sendMessage(m);
	}
	,
	canvasMouseDown: function (p) {
		var m = new $.ig.MouseButtonMessage();
		m.position(p);
		m.action($.ig.MouseButtonAction.prototype.down);
		m.type(this._eventProxy.rightButton() ? $.ig.MouseButtonType.prototype.right : $.ig.MouseButtonType.prototype.left);
		m.modifiers(this._eventProxy.currentModifiers());
		this._interactionMessages.sendMessage(m);
	}
	,
	canvasMouseUp: function (p) {
		var m = new $.ig.MouseButtonMessage();
		m.position(p);
		m.action($.ig.MouseButtonAction.prototype.up);
		m.type(this._eventProxy.rightButton() ? $.ig.MouseButtonType.prototype.right : $.ig.MouseButtonType.prototype.left);
		m.modifiers(this._eventProxy.currentModifiers());
		this._interactionMessages.sendMessage(m);
	}
	,
	_viewport: null,
	_container: null,
	_backgroundCanvas: null,
	_mainCanvas: null,
	_overlayCanvas: null,
	_labelCanvas: null,
	_cssSpan: null,
	_backgroundContext: null,
	_mainContext: null,
	_overlayContext: null,
	_labelContext: null,
	_eventProxy: null,
	_fontBrush: null,
	_fontSliceBrush: null,
	_fontOuterLabel: null,
	_fontSlice: null,
	_fontHeight: 0,
	_fontOuterLabelHeight: 0,
	_fontSliceHeight: 0,
	clearMessageReceived: function (m) {
		var r = this._viewport;
		var c = this._mainContext;
		if (c == null || $.ig.Rect.prototype.l_op_Equality(r, null)) {
			return;
		}
		var x = r.left(), y = r.top(), w = r.width(), h = r.height();
		c.clearRectangle(x, y, w, h);
		c = this._backgroundContext;
		if (c != null) {
			c.clearRectangle(x, y, w, h);
		}
		c = this._labelContext;
		if (c != null) {
			c.clearRectangle(x, y, w, h);
		}
		c = this._overlayContext;
		if (c != null) {
			c.clearRectangle(x, y, w, h);
		}
	}
	,
	__leftWidth: 0,
	__rightWidth: 0,
	setAreaSizeMessageReceived: function (m) {
		var width = (m).width();
		var id = (m).areaID();
		if (id == "LeftPanel") {
			this.__leftWidth = width;
		}
		if (id == "RightPanel") {
			this.__rightWidth = width;
		}
	}
	,
	renderSliceMessageReceived: function (m) {
		var slice = (m).slice();
		var mc = this._mainContext;
		var p = new $.ig.Polygon();
		var pc = new $.ig.PointCollection(0);
		for (var i = 0; i < slice.points().count(); i++) {
			var point = slice.points().__inner[i];
			pc.add({ __x: point.__x + this.__leftWidth + slice.offset().__x, __y: point.__y + slice.offset().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		p.points(pc);
		p.strokeThickness((m)._outlineThickness);
		p.__opacity = slice.opacity();
		p.__fill = slice.fill();
		p.__stroke = (m)._outlineThickness < 1 ? slice.fill() : slice.outline();
		if (slice.style() != null) {
			mc.applyStyle(p, slice.style());
			slice.fill(p.__fill);
			slice.outline(p.__stroke);
			slice.outlineThickness(p.strokeThickness());
			slice.opacity(p.__opacity);
		}
		mc.renderPolygon(p);
		slice.outlineThickness(p.strokeThickness());
		if (slice.hasInnerLabel()) {
			var tb = new $.ig.TextBlock();
			if (this._fontSliceBrush == null) {
				this._fontSliceBrush = new $.ig.Brush();
				this._fontSliceBrush.__fill = this.cssValue("ui-funnel-slice", "color", -1);
			}
			tb.fill(this._fontSliceBrush);
			mc.setFontInfo(this.getFont());
			var text = slice.innerLabel().toString();
			var width = mc.measureTextWidth(text);
			var height = this.getFontHeight();
			tb.text(text);
			tb.canvasTop(slice.innerLabelPosition().__y + slice.offset().__y - (height / 2));
			tb.canvasLeft(slice.innerLabelPosition().__x + this.__leftWidth + slice.offset().__x - (width / 2));
			slice.innerLabelBounds(new $.ig.Rect(0, tb.canvasLeft(), tb.canvasTop(), width, height));
			slice.visibility(tb.__visibility);
			mc.renderTextBlock(tb);
		}
	}
	,
	renderOuterLabelMessageReceived: function (m) {
		var xPos = 0;
		if (this.__rightWidth > 0) {
			xPos = this._viewport.width() - this.__rightWidth;
		}
		var olm = m;
		var mc = this._labelContext;
		var tb = new $.ig.TextBlock();
		tb.text(olm.label().toString());
		mc.setFontInfo(this.getFontOuterLabel());
		if (this._fontBrush == null) {
			this._fontBrush = new $.ig.Brush();
			this._fontBrush.__fill = this._container.css("color");
		}
		tb.fill(this._fontBrush);
		var height = this.getFontOuterLabelHeight();
		var yPos = olm.position().__y - height / 2;
		var width = mc.measureTextWidth(tb.text());
		tb.canvasLeft(xPos);
		tb.canvasTop(yPos);
		olm.sliceInfo().slice().outerLabelBounds(new $.ig.Rect(0, xPos, yPos, width, height));
		mc.renderTextBlock(tb);
	}
	,
	__tooltip: null,
	tooltipValueChangedMessageReceived: function (m) {
		this.__tooltip = (m).value();
	}
	,
	tooltipUpdateMessageReceived: function (m) {
		this.doTooltip(m);
	}
	,
	clearTooltipMessageReceived: function (m) {
		this.doTooltip(null);
	}
	,
	doTooltip: function (m) {
		var x_ = 0, y_ = 0;
		var t_ = null, v_ = null, i_ = this._container;
		i_ = i_?i_.data('igFunnelChart'):null;
		if (i_ != null) {
			if (m != null) {
				t_ = this.__tooltip;
				x_ = m.position().__x + 6;
				y_ = m.position().__y + 6;
				v_ = (m.context()).item();
			}
			i_._fireTooltip(t_,v_,x_,y_);
		}
	}
	,
	propertyChangedMessageReceived: function (m) {
		var msg = m;
		var property = msg.propertyName();
		switch (property) {
			case "Container":
				this.onContainerProvided(msg.newValue());
				break;
			case "TextStyle":
				this._fontSlice = $.ig.FontUtil.prototype.toFontInfo(msg.newValue().toString());
				break;
			case "OuterLabelTextStyle":
				this._fontOuterLabel = $.ig.FontUtil.prototype.toFontInfo(msg.newValue().toString());
				break;
		}
	}
	,
	queueWork: function (work) {
		window.setTimeout(work, 0);
	}
	,
	onContainerProvided: function (theContainer) {
		if (theContainer == null) {
			if (this._eventProxy != null) {
				this._eventProxy.destroy();
			}
			this._container = this._backgroundCanvas = this._mainCanvas = this._labelCanvas = this._overlayCanvas = null;
			this._backgroundContext = this._mainContext = this._labelContext = this._overlayContext = null;
			this._eventProxy = null;
			return;
		}
		var container = theContainer;
		var cont = this._container = $(container);
		cont.css("position", "relative");
		for (var i = 0; i < 4; i++) {
			var jq = $("<canvas style='position:absolute' />");
			cont.append(jq);
			var rc = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (jq[0]).getContext("2d"));
			if (i == 0) {
				this._backgroundCanvas = jq;
				this._backgroundContext = rc;
			}
			if (i == 1) {
				this._mainCanvas = jq;
				this._mainContext = rc;
			}
			if (i == 2) {
				this._labelCanvas = jq;
				this._labelContext = rc;
			}
			if (i == 3) {
				this._overlayCanvas = jq;
				this._overlayContext = rc;
			}
		}
		this._cssSpan = $("<span style='position:absolute;display:none' />");
		cont.append(this._cssSpan);
		this._eventProxy = new $.ig.DOMEventProxy(this._overlayCanvas);
		var $t = this._eventProxy;
		$t.onMouseOver = $.ig.Delegate.prototype.combine($t.onMouseOver, this.canvasMouseMove.runOn(this));
		var $t1 = this._eventProxy;
		$t1.onMouseLeave = $.ig.Delegate.prototype.combine($t1.onMouseLeave, this.canvasMouseLeave.runOn(this));
		var $t2 = this._eventProxy;
		$t2.onMouseDown = $.ig.Delegate.prototype.combine($t2.onMouseDown, this.canvasMouseDown.runOn(this));
		var $t3 = this._eventProxy;
		$t3.onMouseUp = $.ig.Delegate.prototype.combine($t3.onMouseUp, this.canvasMouseUp.runOn(this));
		this.containerResized();
	}
	,
	getDefOutline: function () {
		var val = this.cssValue("ui-funnel-slice", "border-top-width", -1);
		return (val == null) ? -1 : $.ig.Number.prototype.parseInt(val);
	}
	,
	getFont: function () {
		var font = this._fontSlice;
		if (font == null) {
			var span = this._cssSpan;
			if (span != null) {
				span.addClass("ui-funnel-slice");
				this._fontSlice = font = $.ig.FontUtil.prototype.getFont(span);
				span.removeClass("ui-funnel-slice");
			}
		}
		return font;
	}
	,
	getFontOuterLabel: function () {
		var font = this._fontOuterLabel;
		if (font == null) {
			this._fontOuterLabel = font = $.ig.FontUtil.prototype.getFont(this._container);
		}
		return font;
	}
	,
	getFontHeight: function () {
		var height = this._fontSliceHeight;
		if (height < 0) {
			var font = this._fontSlice;
			height = $.ig.FontUtil.prototype.getCurrentFontHeight(font);
			this._fontSliceHeight = height;
		}
		return height;
	}
	,
	getFontOuterLabelHeight: function () {
		var height = this._fontOuterLabelHeight;
		if (height < 0) {
			var font = this._fontOuterLabel;
			height = $.ig.FontUtil.prototype.getCurrentFontHeight(font);
			this._fontOuterLabelHeight = height;
		}
		return height;
	}
	,
	_fillBrushes: null,
	_lineBrushes: null,
	getDefBrushes: function (back) {
		if (this._fillBrushes != null) {
			return back ? this._fillBrushes : this._lineBrushes;
		}
		var $ret = $.ig.BrushUtil.prototype.getBrushCollection("chart", this._container, this._fillBrushes, this._lineBrushes, null);
		this._fillBrushes = $ret.p2;
		this._lineBrushes = $ret.p3;
		return back ? this._fillBrushes : this._lineBrushes;
	}
	,
	cssValue: function (css, prop, palette) {
		var span = this._cssSpan;
		if (span == null) {
			return null;
		}
		span.addClass(css);
		var val = span.css(prop);
		if (val == null || val.length == 0 || val == "null" || val == "transparent") {
			val = null;
		}
		if (palette >= 0) {
			var brush = new $.ig.Brush();
			if (val != null && val.length != $.ig.util.replace(val, "(0, 0, 0, 0", "").length) {
				val = null;
			}
			brush.__fill = val == null ? $.ig.XamFunnelView.prototype._defFills[palette % $.ig.XamFunnelView.prototype._defFills.length] : val;
			this._fillBrushes.item(palette, brush);
			var line = null;
			if (val != null) {
				line = span.css("border-top-color");
				if (line == null || line.length == 0 || line == "null" || line == "transparent") {
					line = val;
				}
			}
			brush = new $.ig.Brush();
			brush.__fill = (line == null) ? $.ig.XamFunnelView.prototype._defLines[palette % $.ig.XamFunnelView.prototype._defLines.length] : line;
			this._lineBrushes.item(palette, brush);
		}
		span.removeClass(css);
		return val;
	}
	,
	sendPropertyMessageToController: function (propertyName, oldValue, newValue) {
		var m = new $.ig.PropertyChangedMessage();
		m.propertyName(propertyName);
		m.oldValue(oldValue);
		m.newValue(newValue);
		this._interactionMessages.sendMessage(m);
	}
	,
	init: function () {
		var $self = this;
		this._fontHeight = -1;
		this._fontOuterLabelHeight = -1;
		this._fontSliceHeight = -1;
		this.__leftWidth = 0;
		this.__rightWidth = 0;
		this.__tooltip = null;
		$.ig.Object.prototype.init.call(this);
		this._interactionMessages = new $.ig.MessageChannel();
		this.sendWidthDecider();
		this.sendSizeDecider();
		var mh = this._messageHandler = new $.ig.MessageHandler();
		mh.addHandler($.ig.ClearMessage.prototype.$type, this.clearMessageReceived.runOn(this));
		mh.addHandler($.ig.RenderSliceMessage.prototype.$type, this.renderSliceMessageReceived.runOn(this));
		mh.addHandler($.ig.RenderOuterLabelMessage.prototype.$type, this.renderOuterLabelMessageReceived.runOn(this));
		mh.addHandler($.ig.SetAreaSizeMessage.prototype.$type, this.setAreaSizeMessageReceived.runOn(this));
		mh.addHandler($.ig.TooltipValueChangedMessage.prototype.$type, this.tooltipValueChangedMessageReceived.runOn(this));
		mh.addHandler($.ig.TooltipUpdateMessage.prototype.$type, this.tooltipUpdateMessageReceived.runOn(this));
		mh.addHandler($.ig.ClearTooltipMessage.prototype.$type, this.clearTooltipMessageReceived.runOn(this));
		mh.addHandler($.ig.PropertyChangedMessage.prototype.$type, this.propertyChangedMessageReceived.runOn(this));
		mh.addHandler($.ig.ContainerSizeChangedMessage.prototype.$type, function (m) { $self.containerResized(); });
		this.containerResized();
	},
	messageReceived: function (m) {
		this._messageHandler.messageReceived(m);
	}
	,
	onServiceProviderChanged: function (oldValue, newValue) {
		if (oldValue != null) {
			var channel = $.ig.util.cast($.ig.MessageChannel.prototype.$type, oldValue.getService("RenderingMessages"));
			if (channel != null) {
				channel.detachTarget(this.messageReceived.runOn(this));
			}
			this._interactionMessages.detachFromNext();
		}
		if (newValue != null) {
			var newChannel = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("RenderingMessages"));
			if (newChannel != null) {
				newChannel.attachTarget(this.messageReceived.runOn(this));
			}
			var rendering = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("InteractionMessages"));
			this._interactionMessages.connectTo(rendering);
		}
	}
	,
	wH: function (jq, w, h) {
		jq.attr("width", w.toString());
		jq.attr("height", h.toString());
	}
	,
	containerResized: function () {
		if (this._container == null) {
			return;
		}
		var width = this._container.width();
		var height = this._container.height();
		this._viewport = new $.ig.Rect(0, 0, 0, width, height);
		if (this._eventProxy != null) {
			this._eventProxy.viewport(this._viewport);
		}
		this.wH(this._backgroundCanvas, width, height);
		this.wH(this._mainCanvas, width, height);
		this.wH(this._labelCanvas, width, height);
		this.wH(this._overlayCanvas, width, height);
		var m = new $.ig.ViewportChangedMessage();
		m.newWidth(width);
		m.newHeight(height);
		this._interactionMessages.sendMessage(m);
	}
	,
	onResize: function () {
		this.containerResized();
	}
	,
	sendWidthDecider: function () {
		var m = new $.ig.ViewPropertyChangedMessage();
		m.propertyName("OuterLabelWidthDecider");
		m.oldValue(null);
		m.newValue($.ig.util.cast($.ig.IOuterLabelWidthDecider.prototype.$type, this));
		this._interactionMessages.sendMessage(m);
	}
	,
	sendSizeDecider: function () {
		var m = new $.ig.ViewPropertyChangedMessage();
		m.propertyName("FunnelLabelSizeDecider");
		m.oldValue(null);
		m.newValue($.ig.util.cast($.ig.IFunnelLabelSizeDecider.prototype.$type, this));
		this._interactionMessages.sendMessage(m);
	}
	,
	decideWidth: function (labels) {
		var list = labels == null ? null : labels.values();
		if (list == null) {
			return 0;
		}
		var largestWidth = 0;
		this._labelContext.setFontInfo(this.getFontOuterLabel());
		var en = list.getEnumerator();
		while (en.moveNext()) {
			var label = en.current();
			if (label != null) {
				largestWidth = Math.max(this._labelContext.measureTextWidth(label.toString()), largestWidth);
			}
		}
		return largestWidth;
	}
	,
	decideLabelSize: function (sliceInfo, inner) {
		var width = 0;
		if (sliceInfo.slice().hasInnerLabel()) {
			this._labelContext.setFontInfo(this.getFont());
			width = this._labelContext.measureTextWidth(sliceInfo.slice().innerLabel().toString());
		}
		return new $.ig.Size(1, width, inner ? this.getFontHeight() : this.getFontOuterLabelHeight());
	}
	,
	formatLabels: function (col, f_) {
		if (f_ == null) {
			return true;
		}
		var vals = col.values();
		var c_ = this._container;
		c_ = c_?c_.data('igFunnelChart'):null;
		if (vals == null || c_ == null) {
			return false;
		}
		var i_ = vals.count();
		var a = new Array(i_);
		while (i_-- > 0) {
			var v_ = vals.item(i_);
			a[i_] = (f_)(v_, i_, c_);
		}
		col.setValues(a);
		return true;
	}
	,
	exportViewShapes: function () {
	}
	,
	preRender: function () {
	}
	,
	$type: new $.ig.Type('XamFunnelView', $.ig.Object.prototype.$type, [$.ig.IOuterLabelWidthDecider.prototype.$type, $.ig.IFunnelLabelSizeDecider.prototype.$type])
}, true);

$.ig.util.defType('LegendBaseViewManager', 'Object', {
	__owner: null,
	init: function (owner) {
		this.__gradientsEnabled = false;
		$.ig.Object.prototype.init.call(this);
		this.__owner = owner;
		this.items(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.JQueryObject.prototype.$type, 0));
	},
	onContainerProvided: function (container) {
		this.container($(container));
		this.list($("<table></table>"));
		this.list().addClass(this.__owner.legendItemsListStyle());
		this.container().append(this.list());
	}
	,
	_list: null,
	list: function (value) {
		if (arguments.length === 1) {
			this._list = value;
			return value;
		} else {
			return this._list;
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
	__gradientsEnabled: false,
	getVisual: function (item, container) {
		var legendItem = $.ig.util.cast($.ig.ContentControl.prototype.$type, item);
		var row = $("<tr></tr>");
		row.addClass(this.__owner.legendItemStyle());
		if (legendItem != null) {
			var dc = $.ig.util.cast($.ig.DataContext.prototype.$type, legendItem.content());
			if (dc != null && legendItem.contentTemplate() != null) {
				var passInfo = new $.ig.DataTemplatePassInfo();
				passInfo.context = row;
				passInfo.passID = "LegendItem";
				var measureInfo = new $.ig.DataTemplateMeasureInfo();
				measureInfo.passInfo = passInfo;
				measureInfo.width = NaN;
				measureInfo.height = NaN;
				measureInfo.data = dc;
				measureInfo.context = row;
				var renderInfo = new $.ig.DataTemplateRenderInfo();
				renderInfo.passInfo = passInfo;
				if (legendItem.contentTemplate().measure() != null) {
					legendItem.contentTemplate().measure()(measureInfo);
				}
				renderInfo.context = row;
				renderInfo.availableWidth = measureInfo.width;
				renderInfo.availableHeight = measureInfo.height;
				renderInfo.data = dc;
				renderInfo.xPosition = 0;
				renderInfo.yPosition = 0;
				legendItem.contentTemplate().render()(renderInfo);
				row.find(".ui-legend-item-badge").removeClass("ui-legend-item-badge").addClass(this.__owner.legendItemBadgeStyle());
				row.find(".ui-legend-item-text").removeClass("ui-legend-item-text").addClass(this.__owner.legendItemTextStyle());
			}
		}
		return row;
	}
	,
	_items: null,
	items: function (value) {
		if (arguments.length === 1) {
			this._items = value;
			return value;
		} else {
			return this._items;
		}
	}
	,
	addItemVisual: function (item, index) {
		var visual = this.getVisual(item, this.list());
		visual.mouseup(this.__owner.legendMouseUp.runOn(this.__owner));
		visual.mousedown(this.__owner.legendMouseDown.runOn(this.__owner));
		visual.mouseleave(this.__owner.legendMouseLeave.runOn(this.__owner));
		visual.mousemove(this.__owner.legendMouseOver.runOn(this.__owner));
		this.items().add(item, visual);
		if ((this.list().children().length == 0) || (index >= this.list().children().length)) {
			this.list().append(visual);
		} else {
			var insBefore = this.list().children().eq(index);
			insBefore.before(visual);
		}
	}
	,
	refreshLegendItem: function (index) {
	}
	,
	removeItemVisual: function (item) {
		var $self = this;
		var toRemove;
		if ((function () { var $ret = $self.items().tryGetValue(item, toRemove); toRemove = $ret.p1; return $ret.ret; }())) {
			toRemove.remove();
			this.items().remove(item);
		}
	}
	,
	getItem: function (visual_) {
		var en = this.items().getEnumerator();
		while (en.moveNext()) {
			var pair_ = en.current();
			if(pair_.value()[0]===visual_[0]){
                                    return pair_.key();
                                };
		}
		return null;
	}
	,
	containsContext: function (dataContext) {
		var en = this.items().keys().getEnumerator();
		while (en.moveNext()) {
			var element = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, element);
			if (contentControl != null && contentControl.content() != null) {
				var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				if (context != null && context.itemLabel() == dataContext.itemLabel() && context.series() == dataContext.series() && context.itemBrush() == dataContext.itemBrush()) {
					return true;
				}
			}
		}
		return false;
	}
	,
	_scaleCanvas: null,
	scaleCanvas: function (value) {
		if (arguments.length === 1) {
			this._scaleCanvas = value;
			return value;
		} else {
			return this._scaleCanvas;
		}
	}
	,
	_scaleContext: null,
	scaleContext: function (value) {
		if (arguments.length === 1) {
			this._scaleContext = value;
			return value;
		} else {
			return this._scaleContext;
		}
	}
	,
	getScaleContext: function (container) {
		var width = this.container().width();
		var height = this.container().height();
		var div = $("<div style=\"position : relative;\" />");
		this.scaleCanvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
		this.container().append(div);
		div.append(this.scaleCanvas());
		this.scaleContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.scaleCanvas()[0]).getContext("2d")));
		this.scaleCanvas().attr("width", width.toString());
		this.scaleCanvas().attr("height", height.toString());
		return this.scaleContext();
	}
	,
	getScaleContainerSize: function () {
		return new $.ig.Size(1, this.container().width(), Math.max(10, this.container().height() - 15));
	}
	,
	renderGradientShape: function (ScaleContext, polygon, currGradient, rect) {
		var cont = ScaleContext.getUnderlyingContext();
		cont.beginPath();
		cont.moveTo(polygon.points().__inner[0].__x, polygon.points().__inner[0].__y);
		for (var i = 1; i < polygon.points().count(); i++) {
			cont.lineTo(polygon.points().__inner[i].__x, polygon.points().__inner[i].__y);
		}
		cont.lineTo(polygon.points().__inner[0].__x, polygon.points().__inner[0].__y);
		var grad = cont.createLinearGradient(rect.left(), rect.top(), rect.left(), rect.top() + rect.height());
		var en = currGradient.gradientStops().getEnumerator();
		while (en.moveNext()) {
			var stop = en.current();
			grad.addColorStop(stop.offset(), stop.brush().__fill);
		}
		cont.fillStyle = grad;
		cont.fill();
	}
	,
	$type: new $.ig.Type('LegendBaseViewManager', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PieChartViewManager', 'Object', {
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
	_backgroundCanvas: null,
	backgroundCanvas: function (value) {
		if (arguments.length === 1) {
			this._backgroundCanvas = value;
			return value;
		} else {
			return this._backgroundCanvas;
		}
	}
	,
	_overlayCanvas: null,
	overlayCanvas: function (value) {
		if (arguments.length === 1) {
			this._overlayCanvas = value;
			return value;
		} else {
			return this._overlayCanvas;
		}
	}
	,
	_mainCanvas: null,
	mainCanvas: function (value) {
		if (arguments.length === 1) {
			this._mainCanvas = value;
			return value;
		} else {
			return this._mainCanvas;
		}
	}
	,
	_labelCanvas: null,
	labelCanvas: function (value) {
		if (arguments.length === 1) {
			this._labelCanvas = value;
			return value;
		} else {
			return this._labelCanvas;
		}
	}
	,
	__containerWidth: 0,
	__containerHeight: 0,
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
	_backgroundContext: null,
	backgroundContext: function (value) {
		if (arguments.length === 1) {
			this._backgroundContext = value;
			return value;
		} else {
			return this._backgroundContext;
		}
	}
	,
	_mainContext: null,
	mainContext: function (value) {
		if (arguments.length === 1) {
			this._mainContext = value;
			return value;
		} else {
			return this._mainContext;
		}
	}
	,
	_labelContext: null,
	labelContext: function (value) {
		if (arguments.length === 1) {
			this._labelContext = value;
			return value;
		} else {
			return this._labelContext;
		}
	}
	,
	_overlayContext: null,
	overlayContext: function (value) {
		if (arguments.length === 1) {
			this._overlayContext = value;
			return value;
		} else {
			return this._overlayContext;
		}
	}
	,
	__owner: null,
	init: function (owner) {
		this.__containerWidth = 0;
		this.__containerHeight = 0;
		this.__sizeChanged = false;
		this.__toolTipObject = null;
		this.__toolTipString = null;
		this.__tooltipDisconnected = true;
		$.ig.Object.prototype.init.call(this);
		this.__owner = owner;
	},
	__sizeChanged: false,
	notifyContainerResized: function () {
		var newWidth = this.container().width();
		var newHeight = this.container().height();
		this.onContainerResized(newWidth, newHeight);
	}
	,
	onContainerResized: function (width, height) {
		this.eventProxy().viewport(new $.ig.Rect(0, 0, 0, width, height));
		if (this.__containerWidth != width) {
			this.__sizeChanged = true;
		}
		this.__containerWidth = width;
		if (this.__containerHeight != height) {
			this.__sizeChanged = true;
		}
		this.__containerHeight = height;
		this.__owner.resize();
	}
	,
	resize: function () {
		this.viewport(new $.ig.Rect(0, 0, 0, this.__containerWidth, this.__containerHeight));
		this.backgroundCanvas().attr("width", this.__containerWidth.toString());
		this.backgroundCanvas().attr("height", this.__containerHeight.toString());
		this.mainCanvas().attr("width", this.__containerWidth.toString());
		this.mainCanvas().attr("height", this.__containerHeight.toString());
		this.labelCanvas().attr("width", this.__containerWidth.toString());
		this.labelCanvas().attr("height", this.__containerHeight.toString());
		this.overlayCanvas().attr("width", this.__containerWidth.toString());
		this.overlayCanvas().attr("height", this.__containerHeight.toString());
		return this.viewport();
	}
	,
	getOwnerFont: function () {
		return this.__owner.font();
	}
	,
	getContainerOffsets: function () {
		var offset = $.ig.DOMEventProxy.prototype.getOffset(this.container());
		return { __x: offset.left(), __y: offset.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getCssBrushColors: function (className, obj) {
		var brushes = new Array(2);
		obj.addClass(className);
		var fill = new $.ig.Brush();
		fill.__fill = obj.css("background-color");
		var outline = new $.ig.Brush();
		outline.__fill = obj.css("border-top-color");
		obj.removeClass(className);
		brushes[0] = fill;
		brushes[1] = outline;
		return brushes;
	}
	,
	queueWork: function (work) {
		window.setTimeout(work, 0);
	}
	,
	getDefaultStyle: function (brushes, outlines, fontBrush, font) {
		var $ret = $.ig.BrushUtil.prototype.getBrushCollection("chart", this.container(), brushes, outlines, null);
		brushes = $ret.p2;
		outlines = $ret.p3;
		fontBrush = new $.ig.Brush();
		fontBrush.__fill = this.container().css("color");
		font = $.ig.FontUtil.prototype.getFont(this.container());
		return {
			p0: brushes,
			p1: outlines,
			p2: fontBrush,
			p3: font
		};
	}
	,
	setDefaultSliceBrushes: function (slicePath) {
		slicePath.__stroke = (function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#000000");
			return $ret;
		}());
		slicePath.__fill = (function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#222222");
			return $ret;
		}());
	}
	,
	_eventProxy: null,
	eventProxy: function (value) {
		if (arguments.length === 1) {
			this._eventProxy = value;
			return value;
		} else {
			return this._eventProxy;
		}
	}
	,
	onContainerProvided: function (theContainer) {
		if (theContainer == null) {
			if (this.eventProxy() != null) {
				this.eventProxy().destroy();
			}
			this.container(null);
			this.backgroundCanvas(null);
			this.mainCanvas(null);
			this.labelCanvas(null);
			this.overlayCanvas(null);
			this.backgroundContext(null);
			this.mainContext(null);
			this.labelContext(null);
			this.overlayContext(null);
			this.eventProxy(null);
			return;
		}
		var container = theContainer;
		this.container($(container));
		this.__owner.setDefaultBrushes();
		this.__owner.updateCurrentFontHeight();
		this.container().css("position", "relative");
		this.container().addClass("ui-corner-all ui-widget-content");
		this.backgroundCanvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
		this.mainCanvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
		this.labelCanvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
		this.overlayCanvas($("<canvas style=\"position : absolute; top : 0; left : 0\" />"));
		this.container().append(this.backgroundCanvas());
		this.container().append(this.mainCanvas());
		this.container().append(this.labelCanvas());
		this.container().append(this.overlayCanvas());
		this.backgroundContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.backgroundCanvas()[0]).getContext("2d")));
		this.mainContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.mainCanvas()[0]).getContext("2d")));
		this.labelContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.labelCanvas()[0]).getContext("2d")));
		this.overlayContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), (this.overlayCanvas()[0]).getContext("2d")));
		this.eventProxy(new $.ig.DOMEventProxy(this.overlayCanvas()));
		var $t = this.eventProxy();
		$t.onMouseOver = $.ig.Delegate.prototype.combine($t.onMouseOver, this.__owner.canvasMouseMove.runOn(this.__owner));
		var $t1 = this.eventProxy();
		$t1.onMouseLeave = $.ig.Delegate.prototype.combine($t1.onMouseLeave, this.__owner.canvasMouseLeave.runOn(this.__owner));
		var $t2 = this.eventProxy();
		$t2.onMouseDown = $.ig.Delegate.prototype.combine($t2.onMouseDown, this.__owner.canvasMouseDown.runOn(this.__owner));
		var $t3 = this.eventProxy();
		$t3.onMouseUp = $.ig.Delegate.prototype.combine($t3.onMouseUp, this.__owner.canvasMouseUp.runOn(this.__owner));
		this.eventProxy().isInteractionDisabled(this.__owner.model().isSurfaceInteractionDisabled());
		this.onContainerResized(this.container().width(), this.container().height());
	}
	,
	addToContainer: function (_toolTipObject) {
		this.container().append(_toolTipObject);
	}
	,
	onIsSurfaceInteractionDisabledChanged: function (newValue) {
		if (this.eventProxy() == null) {
			return;
		}
		this.eventProxy().isInteractionDisabled(newValue);
	}
	,
	getContainer: function () {
		return this.container();
	}
	,
	exportViewData: function (visualData) {
		visualData.width(this.__containerWidth);
		visualData.height(this.__containerHeight);
	}
	,
	__toolTipObject: null,
	__toolTipString: null,
	__tooltipDisconnected: false,
	updateTooltipValue: function (p) {
		this.__toolTipString = null;
		this.__toolTipObject = null;
		if (typeof p === 'string') {
			this.__toolTipString = p;
		} else {
			this.__toolTipObject = p;
			if (this.__toolTipObject != null) {
				this.__toolTipObject.css("position", "fixed");
				this.__toolTipObject.css("top", "0");
				this.__toolTipObject.css("left", "0");
				this.__toolTipObject.css("z-index", "10000");
			}
		}
	}
	,
	updateTooltipPosition: function (pos_, context_) {
		if (this.__toolTipObject != null) {
			if (this.__tooltipDisconnected) {
				this.addToContainer(this.__toolTipObject);
				this.__tooltipDisconnected = false;
			}
			context_.hideOthers = true;
			if (this.__toolTipObject.updateToolTip) { this.__toolTipObject.updateToolTip(context_); };
			if (this.__toolTipObject.offset) { this.__toolTipObject.offset({ left: pos_.__x, top: pos_.__y }); };
		}
	}
	,
	hideTooltip: function (context) {
		if (this.__toolTipObject != null) {
			var context_ = context;
			if (this.__toolTipObject.hideToolTip) { this.__toolTipObject.hideToolTip(context_); };
		}
	}
	,
	getStyleBackground: function (style_) {
		var actualBrush = null;
		var fillColor_ = null;
		if (style_ != null) {
			if (style_.fill) { fillColor_ = style_.fill };
			if (fillColor_ != null) {
				actualBrush = $.ig.Brush.prototype.create(fillColor_);
			}
		}
		return actualBrush;
	}
	,
	getStyleOutline: function (style_) {
		var actualOutline = null;
		var outlineColor_ = null;
		if (style_ != null) {
			if (style_.stroke) { outlineColor_ = style_.stroke };
			if (outlineColor_ != null) {
				actualOutline = $.ig.Brush.prototype.create(outlineColor_);
			}
		}
		return actualOutline;
	}
	,
	$type: new $.ig.Type('PieChartViewManager', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LegendTemplates', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	defaultLegendItemRender: function (info) {
		var cont = info.data;
		var s = cont.series();
		var template = s.legendItemBadgeTemplate();
		var title = s.title();
		if (title == null) {
			title = "Series Title";
		}
		$.ig.LegendTemplates.prototype.coreLegendItemRender(info.context, info.xPosition, info.yPosition, info.availableWidth, info.availableHeight, info.data, title.toString(), template, info.passInfo);
	}
	,
	defaultDiscreteLegendItemRender: function (info) {
		var cont = info.data;
		var s = cont.series();
		var template = s.legendItemBadgeTemplate();
		var title = cont.itemLabel();
		if (title == null) {
			title = "Series Item";
		}
		$.ig.LegendTemplates.prototype.coreLegendItemRender(info.context, info.xPosition, info.yPosition, info.availableWidth, info.availableHeight, info.data, title.toString(), template, info.passInfo);
	}
	,
	pieLegendItemRender: function (info) {
		var cont = info.data;
		var s = cont.series();
		var template = s.legendItemBadgeTemplate();
		var title = cont.itemLabel();
		if (title == null) {
			title = "Pie Item";
		}
		$.ig.LegendTemplates.prototype.coreLegendItemRender(info.context, info.xPosition, info.yPosition, info.availableWidth, info.availableHeight, info.data, title.toString(), template, info.passInfo);
	}
	,
	doughnutLegendItemRender: function (info) {
		var cont = info.data;
		var s = cont.series();
		var template = s.legendItemBadgeTemplate();
		var title = cont.itemLabel();
		if (title == null) {
			title = "Doughnut Item";
		}
		$.ig.LegendTemplates.prototype.coreLegendItemRender(info.context, info.xPosition, info.yPosition, info.availableWidth, info.availableHeight, info.data, title.toString(), template, info.passInfo);
	}
	,
	funnelLegendItemRender: function (info) {
		var cont = info.data;
		var s = cont.series();
		var template = s.legendItemBadgeTemplate();
		var title = cont.itemLabel();
		if (title == null) {
			title = "Funnel Item";
		}
		$.ig.LegendTemplates.prototype.coreLegendItemRender(info.context, info.xPosition, info.yPosition, info.availableWidth, info.availableHeight, info.data, title.toString(), template, info.passInfo);
	}
	,
	coreLegendItemRender: function (context, xPosition, yPosition, availableWidth, availableHeight, data, title, template, passInfo) {
		var obj = context;
		var cont = data;
		if (cont != null && cont.series() != null) {
			if (template != null) {
				var badgeDom = $("<canvas></canvas>");
				badgeDom.attr("width", "18");
				badgeDom.attr("height", "16");
				var badgeCellDom = $("<td class='ui-legend-item-badge'></td>");
				badgeCellDom.append(badgeDom);
				var badgeContext = (badgeDom[0]).getContext("2d");
				var measureInfo = new $.ig.DataTemplateMeasureInfo();
				measureInfo.passInfo = passInfo;
				measureInfo.context = badgeContext;
				measureInfo.width = 18;
				measureInfo.height = 16;
				measureInfo.data = cont;
				template.measure()(measureInfo);
				var renderInfo = new $.ig.DataTemplateRenderInfo();
				renderInfo.availableWidth = $.ig.util.isNaN(measureInfo.width) ? 18 : Math.min(measureInfo.width, 18);
				renderInfo.availableHeight = $.ig.util.isNaN(measureInfo.height) ? 16 : Math.min(measureInfo.height, 16);
				renderInfo.passInfo = passInfo;
				renderInfo.context = badgeContext;
				renderInfo.data = cont;
				renderInfo.xPosition = 0;
				renderInfo.yPosition = 0;
				template.render()(renderInfo);
				obj.append(badgeCellDom);
			}
			var titleDom = $("<td class='ui-legend-item-text'><span>" + title + "</span></td>");
			obj.append(titleDom);
		}
	}
	,
	pieBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		var slice = dc.slice();
		var pieChart = dc.series();
		var itemBrush = pieChart.view().getActualBackground(slice);
		var itemOutline = pieChart.view().getActualOutline(slice);
		var rect = new $.ig.Rectangle();
		rect.width(info.availableWidth - (slice.strokeThickness() * 2) - 2);
		rect.height(info.availableHeight - (slice.strokeThickness() * 2));
		rect.canvasLeft(slice.strokeThickness() + 1);
		rect.canvasTop(slice.strokeThickness());
		rect.strokeThickness(slice.strokeThickness());
		rect.__fill = itemBrush;
		rect.__stroke = itemOutline;
		renderCont.renderRectangle(rect);
	}
	,
	doughnutBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		var slice = dc.slice();
		var pieChart = (dc.series()).ring().ringControl()._arcs.item(0);
		var itemBrush = pieChart.view().getActualBackground(slice);
		var itemOutline = pieChart.view().getActualOutline(slice);
		var rect = new $.ig.Rectangle();
		rect.width(info.availableWidth - (slice.strokeThickness() * 2) - 2);
		rect.height(info.availableHeight - (slice.strokeThickness() * 2));
		rect.canvasLeft(slice.strokeThickness() + 1);
		rect.canvasTop(slice.strokeThickness());
		rect.strokeThickness(slice.strokeThickness());
		rect.__fill = itemBrush;
		rect.__stroke = itemOutline;
		renderCont.renderRectangle(rect);
	}
	,
	funnelBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		}
		if (dc.itemOutline() != null) {
			dc.outline(dc.itemOutline());
		}
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		var strokeThickness = dc.thickness();
		if ($.ig.util.isNaN(strokeThickness)) {
			strokeThickness = 1;
		}
		var rect = new $.ig.Rectangle();
		rect.width(info.availableWidth - (strokeThickness * 2) - 2);
		rect.height(info.availableHeight - (strokeThickness * 2));
		rect.canvasLeft(strokeThickness + 1);
		rect.canvasTop(strokeThickness);
		rect.strokeThickness(strokeThickness);
		rect.__fill = dc.actualItemBrush();
		rect.__stroke = dc.outline();
		renderCont.renderRectangle(rect);
	}
	,
	lineBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var s = dc.series();
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			dc.actualItemBrush(s.actualBrush());
		}
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		var line = new $.ig.Line();
		line.x1(0);
		line.y1(info.availableHeight / 2);
		line.x2(info.availableWidth);
		line.y2(info.availableHeight / 2);
		line.strokeThickness(s.thickness());
		line.__stroke = dc.actualItemBrush();
		line.strokeDashArray(s.dashArray());
		renderCont.renderLine(line);
		var m = $.ig.util.cast($.ig.MarkerSeries.prototype.$type, dc.series());
		if (m != null && m.actualMarkerTemplate() != null && m.actualMarkerTemplate().render() != null) {
			dc.outline(m.actualMarkerOutline());
			dc.thickness($.ig.MarkerSeries.prototype.markerThicknessDefault);
			$.ig.LegendTemplates.prototype.renderMarkerTemplate(m.actualMarkerTemplate(), info);
		}
	}
	,
	renderMarkerTemplate: function (template, info) {
		var measureInfo = new $.ig.DataTemplateMeasureInfo();
		measureInfo.passInfo = info.passInfo;
		measureInfo.width = 11;
		measureInfo.height = 11;
		measureInfo.data = info.data;
		measureInfo.context = info.context;
		measureInfo.renderContext = info.renderContext;
		var dc = info.data;
		var oldActualFill = dc.actualItemBrush();
		var oldFill = dc.itemBrush();
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			if (dc.itemBrush() == null) {
				var ser = dc.series();
				dc.actualItemBrush(ser.getActualMarkerBrush());
			}
		}
		if (template.measure() != null) {
			template.measure()(measureInfo);
			if ($.ig.util.isNaN(measureInfo.width) || Number.isInfinity(measureInfo.width) || measureInfo.width > 11) {
				measureInfo.width = 11;
			}
			if ($.ig.util.isNaN(measureInfo.height) || Number.isInfinity(measureInfo.height) || measureInfo.height > 11) {
				measureInfo.height = 11;
			}
		}
		var renderInfo = new $.ig.DataTemplateRenderInfo();
		renderInfo.passInfo = info.passInfo;
		renderInfo.availableWidth = measureInfo.width;
		renderInfo.availableHeight = measureInfo.height;
		renderInfo.xPosition = info.availableWidth / 2;
		renderInfo.yPosition = info.availableHeight / 2;
		renderInfo.data = info.data;
		renderInfo.context = info.context;
		renderInfo.renderContext = info.renderContext;
		template.render()(renderInfo);
		dc.itemBrush(oldFill);
		dc.actualItemBrush(oldActualFill);
	}
	,
	updateItemBrush: function (dc) {
		var s = null;
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			s = dc.series();
		}
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			if (s != null) {
				dc.actualItemBrush(s.actualBrush());
			} else {
				dc.actualItemBrush(dc.itemBrush());
			}
		}
	}
	,
	defaultLegendItemMeasure: function (info) {
	}
	,
	defaultDiscreteLegendItemMeasure: function (info) {
	}
	,
	legendItemBadgeMeasure: function (info) {
	}
	,
	rectBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		$.ig.LegendTemplates.prototype.updateItemBrush(dc);
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		var p = new $.ig.Path();
		var pg = new $.ig.PathGeometry();
		var f = new $.ig.PathFigure();
		f.__isFilled = true;
		f.__startPoint = { __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f.__segments.add(new $.ig.LineSegment(0, { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 11, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 16, __y: 9, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pg.figures().add(f);
		p.data(pg);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p.strokeThickness((dc.series()).thickness());
			p.__stroke = (dc.series()).actualOutline();
			p.strokeDashArray((dc.series()).dashArray());
		}
		p.__fill = dc.actualItemBrush();
		renderCont.renderPath(p);
		var m = $.ig.util.cast($.ig.MarkerSeries.prototype.$type, dc.series());
		if (m != null && m.actualMarkerTemplate() != null && m.actualMarkerTemplate().render() != null) {
			if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
				dc.outline((dc.series()).actualOutline());
			}
			$.ig.LegendTemplates.prototype.renderMarkerTemplate(m.actualMarkerTemplate(), info);
		}
	}
	,
	markerlessRectBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		$.ig.LegendTemplates.prototype.updateItemBrush(dc);
		var p = new $.ig.Path();
		var pg = new $.ig.PathGeometry();
		var f = new $.ig.PathFigure();
		f.__isFilled = true;
		f.__startPoint = { __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f.__segments.add(new $.ig.LineSegment(0, { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 11, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 16, __y: 9, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pg.figures().add(f);
		p.data(pg);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p.strokeThickness((dc.series()).thickness());
			p.__stroke = (dc.series()).actualOutline();
			p.strokeDashArray((dc.series()).dashArray());
		}
		p.__fill = dc.actualItemBrush();
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		renderCont.renderPath(p);
	}
	,
	markerlessLineBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var s = dc.series();
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			dc.actualItemBrush(s.actualBrush());
		}
		var line = new $.ig.Line();
		line.x1(0);
		line.y1(info.availableHeight / 2);
		line.x2(info.availableWidth);
		line.y2(info.availableHeight / 2);
		line.strokeThickness(s.thickness());
		line.strokeDashArray(s.dashArray());
		line.__stroke = dc.actualItemBrush();
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		renderCont.renderLine(line);
	}
	,
	pointBadgeTemplate: function (info) {
		var dc = info.data;
		$.ig.LegendTemplates.prototype.updateItemBrush(dc);
		var m = $.ig.util.cast($.ig.MarkerSeries.prototype.$type, dc.series());
		if (m != null && m.actualMarkerTemplate() != null && m.actualMarkerTemplate().render() != null) {
			if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
				dc.outline((dc.series()).actualOutline());
			}
			$.ig.LegendTemplates.prototype.renderMarkerTemplate(m.actualMarkerTemplate(), info);
		}
	}
	,
	positiveNegativeBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var s = dc.series();
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			dc.actualItemBrush(s.actualBrush());
		}
		var p = new $.ig.Path();
		var pg = new $.ig.PathGeometry();
		var f = new $.ig.PathFigure();
		f.__isFilled = true;
		f.__startPoint = { __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f.__segments.add(new $.ig.LineSegment(0, { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pg.figures().add(f);
		p.data(pg);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p.strokeThickness((dc.series()).thickness());
			p.__stroke = (dc.series()).actualOutline();
			p.strokeDashArray((dc.series()).dashArray());
		}
		p.__fill = dc.actualItemBrush();
		var p2 = new $.ig.Path();
		var pg2 = new $.ig.PathGeometry();
		var f2 = new $.ig.PathFigure();
		f2.__isFilled = true;
		f2.__startPoint = { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 11, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f2.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 16, __y: 9, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		pg2.figures().add(f2);
		p2.data(pg2);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p2.strokeThickness((dc.series()).thickness());
			p2.__stroke = (dc.series()).actualOutline();
		}
		if ($.ig.util.cast($.ig.WaterfallSeries.prototype.$type, dc.series()) !== null) {
			p2.__fill = (dc.series()).negativeBrush();
		}
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		renderCont.renderPath(p);
		renderCont.renderPath(p2);
		var m = $.ig.util.cast($.ig.MarkerSeries.prototype.$type, dc.series());
		if (m != null && m.actualMarkerTemplate() != null && m.actualMarkerTemplate().render() != null) {
			if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
				dc.outline((dc.series()).actualOutline());
			}
			$.ig.LegendTemplates.prototype.renderMarkerTemplate(m.actualMarkerTemplate(), info);
		}
	}
	,
	financialBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var s = dc.series();
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			dc.actualItemBrush(s.actualBrush());
		}
		var p = new $.ig.Path();
		var pg = new $.ig.PathGeometry();
		var f = new $.ig.PathFigure();
		f.__isFilled = true;
		f.__startPoint = { __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f.__segments.add(new $.ig.LineSegment(0, { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pg.figures().add(f);
		p.data(pg);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p.strokeThickness((dc.series()).thickness());
			p.__stroke = (dc.series()).actualOutline();
			p.strokeDashArray((dc.series()).dashArray());
		}
		p.__fill = dc.actualItemBrush();
		var p2 = new $.ig.Path();
		var pg2 = new $.ig.PathGeometry();
		var f2 = new $.ig.PathFigure();
		f2.__isFilled = true;
		f2.__startPoint = { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 11, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f2.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 16, __y: 9, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		pg2.figures().add(f2);
		p2.data(pg2);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p2.strokeThickness((dc.series()).thickness());
			p2.__stroke = (dc.series()).actualOutline();
			p2.strokeDashArray((dc.series()).dashArray());
		}
		if ($.ig.util.cast($.ig.FinancialSeries.prototype.$type, dc.series()) !== null) {
			p2.__fill = (dc.series()).negativeBrush();
		}
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		renderCont.renderPath(p);
		renderCont.renderPath(p2);
	}
	,
	financialIndicatorBadgeTemplate: function (info) {
		var dc = info.data;
		var cont = info.context;
		var s = dc.series();
		if (dc.itemBrush() != null) {
			dc.actualItemBrush(dc.itemBrush());
		} else {
			dc.actualItemBrush(s.actualBrush());
		}
		var p = new $.ig.Path();
		var pg = new $.ig.PathGeometry();
		var f = new $.ig.PathFigure();
		f.__isFilled = true;
		f.__startPoint = { __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f.__segments.add(new $.ig.LineSegment(0, { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add(new $.ig.LineSegment(0, { __x: 5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 0, __y: 5, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		pg.figures().add(f);
		p.data(pg);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p.strokeThickness((dc.series()).thickness());
			p.__stroke = (dc.series()).actualOutline();
			p.strokeDashArray((dc.series()).dashArray());
		}
		p.__fill = dc.actualItemBrush();
		var p2 = new $.ig.Path();
		var pg2 = new $.ig.PathGeometry();
		var f2 = new $.ig.PathFigure();
		f2.__isFilled = true;
		f2.__startPoint = { __x: 0, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 11, __y: 14, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		f2.__segments.add((function () {
			var $ret = new $.ig.ArcSegment();
			$ret.size(new $.ig.Size(1, 5, 5));
			$ret.isLargeArc(false);
			$ret.point({ __x: 16, __y: 9, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return $ret;
		}()));
		f2.__segments.add(new $.ig.LineSegment(0, { __x: 16, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }));
		pg2.figures().add(f2);
		p2.data(pg2);
		if ($.ig.util.cast($.ig.Series.prototype.$type, dc.series()) !== null) {
			p2.strokeThickness((dc.series()).thickness());
			p2.__stroke = (dc.series()).actualOutline();
			p2.strokeDashArray((dc.series()).dashArray());
		}
		if ($.ig.util.cast($.ig.FinancialSeries.prototype.$type, dc.series()) !== null) {
			p2.__fill = (dc.series()).negativeBrush();
		}
		var renderCont = new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont);
		renderCont.renderPath(p);
		renderCont.renderPath(p2);
	}
	,
	$type: new $.ig.Type('LegendTemplates', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Bezier', 'Object', {
	__resolution: 0,
	resolution: function () {
		return this.__resolution;
	}
	,
	__p0: null,
	p0: function () {
		return this.__p0;
	}
	,
	__p1: null,
	p1: function () {
		return this.__p1;
	}
	,
	__p2: null,
	p2: function () {
		return this.__p2;
	}
	,
	__p3: null,
	p3: function () {
		return this.__p3;
	}
	,
	__threshold: 0,
	init: function (p0, p1, p2, p3, resolution, threshold) {
		this.__threshold = 1.7976931348623157E+308;
		this.__opStack = new $.ig.Stack$1($.ig.BezierOp.prototype.$type);
		$.ig.Object.prototype.init.call(this);
		this.__p0 = p0;
		this.__p1 = p1;
		this.__p2 = p2;
		this.__p3 = p3;
		this.__resolution = resolution;
		this.__threshold = threshold;
		this.valid(this.raseterize());
	},
	_valid: false,
	valid: function (value) {
		if (arguments.length === 1) {
			this._valid = value;
			return value;
		} else {
			return this._valid;
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
	_sortedPoints: null,
	sortedPoints: function (value) {
		if (arguments.length === 1) {
			this._sortedPoints = value;
			return value;
		} else {
			return this._sortedPoints;
		}
	}
	,
	evaluate: function (t) {
		var oneMinusT = 1 - t;
		var oneMinusT2 = oneMinusT * oneMinusT;
		var oneMinusT3 = oneMinusT2 * oneMinusT;
		var t2 = t * t;
		var t3 = t2 * t;
		var point = { __x: oneMinusT3 * this.__p0.__x + 3 * oneMinusT2 * t * this.__p1.__x + 3 * oneMinusT * t2 * this.__p2.__x + t3 * this.__p3.__x, __y: oneMinusT3 * this.__p0.__y + 3 * oneMinusT2 * t * this.__p1.__y + 3 * oneMinusT * t2 * this.__p2.__y + t3 * this.__p3.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var bp = new $.ig.BezierPoint();
		bp._point = point;
		bp._tValue = t;
		return bp;
	}
	,
	__opStack: null,
	raseterize: function () {
		this.points(new $.ig.List$1($.ig.BezierPoint.prototype.$type, 0));
		this.sortedPoints(new $.ig.List$1($.ig.BezierPoint.prototype.$type, 0));
		var op = new $.ig.BezierOp();
		op._start = 0;
		op._end = 1;
		this.__opStack.clear();
		this.__opStack.push(op);
		var index = 0;
		while (this.__opStack.count() > 0) {
			var curr = this.__opStack.pop();
			var p0 = this.evaluate(curr._start);
			var p1 = this.evaluate(curr._end);
			var distSquared = (p1._point.__x - p0._point.__x) * (p1._point.__x - p0._point.__x) + (p1._point.__y - p0._point.__y) * (p1._point.__y - p0._point.__y);
			if (distSquared < this.__resolution) {
				p0._index = index++;
				p1._index = index++;
				this.points().add(p0);
				this.points().add(p1);
				if (p0._point.__x > this.__threshold || p1._point.__x > this.__threshold) {
					return false;
				}
			} else {
				var mid = (curr._start + curr._end) / 2;
				var op1 = new $.ig.BezierOp();
				op1._start = curr._start;
				op1._end = mid;
				var op2 = new $.ig.BezierOp();
				op2._start = mid;
				op2._end = curr._end;
				this.__opStack.push(op2);
				this.__opStack.push(op1);
			}
		}
		var en = this.points().getEnumerator();
		while (en.moveNext()) {
			var point = en.current();
			this.sortedPoints().add(point);
		}
		this.sortPoints();
		return true;
	}
	,
	sortPoints: function () {
		this.sortedPoints().sort2(function (p1, p2) { return (p1._point.__y).compareTo(p2._point.__y); });
	}
	,
	getPointAt: function (y) {
		var index = this.binarySearch(function (p) {
			if (y < p._point.__y) {
				return -1;
			}
			if (y > p._point.__y) {
				return 1;
			}
			return 0;
		});
		if (index < 0) {
			index = ~index;
		}
		if (index < 0) {
			index = 0;
		}
		if (index > this.sortedPoints().count() - 1) {
			index = this.sortedPoints().count() - 1;
		}
		var dist1 = 100000001;
		var dist2 = 100000000;
		var dist3 = 100000002;
		dist2 = Math.abs((this.sortedPoints().__inner[index])._point.__y - y);
		if (index - 1 >= 0) {
			dist1 = Math.abs((this.sortedPoints().__inner[index - 1])._point.__y - y);
		}
		if (index + 1 < this.sortedPoints().count()) {
			dist3 = Math.abs((this.sortedPoints().__inner[index + 1])._point.__y - y);
		}
		if (dist2 <= dist1 && dist2 <= dist3) {
			return (this.sortedPoints().__inner[index]);
		}
		if (dist1 <= dist2 && dist1 <= dist3 && index - 1 > 0) {
			return (this.sortedPoints().__inner[index - 1]);
		}
		if (dist3 <= dist1 && dist3 <= dist2 && index + 1 < this.sortedPoints().count()) {
			return (this.sortedPoints().__inner[index + 1]);
		}
		return (this.sortedPoints().__inner[index]);
	}
	,
	binarySearch: function (comparisonFunction) {
		var currMin = 0;
		var currMax = this.sortedPoints().count() - 1;
		while (currMin <= currMax) {
			var currMid = (currMin + ((currMax - currMin) >> 1));
			var compResult = comparisonFunction(this.sortedPoints().__inner[currMid]);
			if (compResult < 0) {
				currMax = currMid - 1;
			} else if (compResult > 0) {
				currMin = currMid + 1;
			} else {
				return currMid;
			}
		}
		return ~currMin;
	}
	,
	$type: new $.ig.Type('Bezier', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BezierOp', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_start: 0,
	_end: 0,
	$type: new $.ig.Type('BezierOp', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BezierPoint', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_point: null,
	_tValue: 0,
	_index: 0,
	$type: new $.ig.Type('BezierPoint', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('DoubleColumn', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	setValues: function (values) {
		this.values(values);
	}
	,
	$type: new $.ig.Type('DoubleColumn', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ObjectColumn', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	setValues: function (values) {
		this.values(values);
	}
	,
	$type: new $.ig.Type('ObjectColumn', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IntColumn', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.values(new $.ig.List$1($.ig.Number.prototype.$type, 0));
	},
	populate: function (count) {
		for (var i = 0; i < count; i++) {
			this.values().add(i);
		}
	}
	,
	_values: null,
	values: function (value) {
		if (arguments.length === 1) {
			this._values = value;
			return value;
		} else {
			return this._values;
		}
	}
	,
	sort: function (comparison) {
		this.values().sort2(function (i1, i2) { return comparison(i1, i2); });
	}
	,
	getEnumerator: function () {
		return this.values().getEnumerator();
	}
	,
	$type: new $.ig.Type('IntColumn', $.ig.Object.prototype.$type, [$.ig.IEnumerable.prototype.$type])
}, true);

$.ig.util.defType('PointList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.Point.prototype.$type, 0);
	},
	$type: new $.ig.Type('PointList', $.ig.List$1.prototype.$type.specialize($.ig.Point.prototype.$type))
}, true);

$.ig.util.defType('LabelClickEventArgs', 'EventArgs', {
	init: function (slice, originalEvent) {
		this.__allowSliceClick = true;
		$.ig.EventArgs.prototype.init.call(this);
		this.slice(slice);
	},
	_slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this._slice = value;
			return value;
		} else {
			return this._slice;
		}
	}
	,
	item: function () {
		if (this.slice() != null) {
			return this.slice().dataContext();
		} else {
			return null;
		}
	}
	,
	allowSliceClick: function (value) {
		if (arguments.length === 1) {
			this.__allowSliceClick = value;
			return value;
		} else {
			return this.__allowSliceClick;
		}
	}
	,
	__allowSliceClick: false,
	$type: new $.ig.Type('LabelClickEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('PieChartBase', 'Control', {
	createView: function () {
		return new $.ig.PieChartBaseView(this);
	}
	,
	onViewCreated: function (view) {
		this.view(view);
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	init: function () {
		var $self = this;
		this.__brushesAttached = false;
		this.__outlinesAttached = false;
		this.__fastItemsSourceAttached = false;
		this.__selectedAttached = false;
		this.__explodedAttached = false;
		this.__explodedSlices = new $.ig.IndexCollection();
		this.__selectedSlices = new $.ig.IndexCollection();
		this.__textStyle = null;
		$.ig.Control.prototype.init.call(this);
		this.viewport($.ig.Rect.prototype.empty());
		var view = this.createView();
		this.onViewCreated(view);
		view.onInit();
		this.defaultStyleKey($.ig.PieChartBase.prototype.$type);
		this.valueIndices(new $.ig.List$1($.ig.Number.prototype.$type, 0));
		this.othersValueIndices(new $.ig.List$1($.ig.Number.prototype.$type, 0));
		this.others(new $.ig.List$1($.ig.Object.prototype.$type, 0));
		this.__propertyUpdatedOverride = function (o, e) {
			$self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue());
		};
		this.__brushesChangedOverride = function (o, e) {
			$self.renderSlices();
			$self.renderLegendItems();
		};
		this.__explodedIndicesChangedOverride = function (o, e) {
			if ($self.allowSliceExplosion()) {
				$self.prepareSlices();
				$self.prepareLabels();
				$self.renderSlices();
				$self.renderLabels();
			}
		};
		this.__selectedIndicesChangedOverride = function (o, e) {
			$self.selectedIndicesChangedOverride(e);
		};
		this._fastItemsSource_Event = function (o, e) {
			$self.dataUpdatedOverride(e.action(), e.position(), e.count(), e.propertyName());
		};
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, this.__propertyUpdatedOverride);
		var $t = this.selectedSlices();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, this.__selectedIndicesChangedOverride);
		this.__selectedAttached = true;
		var $t1 = this.explodedSlices();
		$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.__explodedIndicesChangedOverride);
		this.__explodedAttached = true;
		this._slices = (function () {
			var $ret = new $.ig.Pool$1($.ig.Slice.prototype.$type);
			$ret.create($self.view().sliceCreate.runOn($self.view()));
			$ret.activate($self.view().sliceActivate.runOn($self.view()));
			$ret.disactivate($self.view().sliceDisactivate.runOn($self.view()));
			$ret.destroy($self.view().sliceDestroy.runOn($self.view()));
			return $ret;
		}());
		this._labels = (function () {
			var $ret = new $.ig.Pool$1($.ig.PieLabel.prototype.$type);
			$ret.create($self.view().labelCreate.runOn($self.view()));
			$ret.activate($self.view().labelActivate.runOn($self.view()));
			$ret.disactivate($self.view().labelDisactivate.runOn($self.view()));
			$ret.destroy($self.view().labelDestroy.runOn($self.view()));
			return $ret;
		}());
	},
	__brushesAttached: false,
	__outlinesAttached: false,
	__fastItemsSourceAttached: false,
	__selectedAttached: false,
	__explodedAttached: false,
	__propertyUpdatedOverride: null,
	__brushesChangedOverride: null,
	__selectedIndicesChangedOverride: null,
	__explodedIndicesChangedOverride: null,
	_fastItemsSource_Event: null,
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.fastItemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.fastItemsSourceProperty);
		}
	}
	,
	__valueColumn: null,
	valueColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__valueColumn != value) {
				var oldValueColumn = this.__valueColumn;
				this.__valueColumn = value;
				this.raisePropertyChanged($.ig.PieChartBase.prototype.valueColumnPropertyName, oldValueColumn, this.__valueColumn);
			}
			return value;
		} else {
			return this.__valueColumn;
		}
	}
	,
	_totalSliceCount: 0,
	totalSliceCount: function (value) {
		if (arguments.length === 1) {
			this._totalSliceCount = value;
			return value;
		} else {
			return this._totalSliceCount;
		}
	}
	,
	_othersTotal: 0,
	othersTotal: function (value) {
		if (arguments.length === 1) {
			this._othersTotal = value;
			return value;
		} else {
			return this._othersTotal;
		}
	}
	,
	_total: 0,
	total: function (value) {
		if (arguments.length === 1) {
			this._total = value;
			return value;
		} else {
			return this._total;
		}
	}
	,
	_valueIndices: null,
	valueIndices: function (value) {
		if (arguments.length === 1) {
			this._valueIndices = value;
			return value;
		} else {
			return this._valueIndices;
		}
	}
	,
	_othersValueIndices: null,
	othersValueIndices: function (value) {
		if (arguments.length === 1) {
			this._othersValueIndices = value;
			return value;
		} else {
			return this._othersValueIndices;
		}
	}
	,
	_others: null,
	others: function (value) {
		if (arguments.length === 1) {
			this._others = value;
			return value;
		} else {
			return this._others;
		}
	}
	,
	_actualStartAngle: 0,
	actualStartAngle: function (value) {
		if (arguments.length === 1) {
			this._actualStartAngle = value;
			return value;
		} else {
			return this._actualStartAngle;
		}
	}
	,
	_legendItems: null,
	legendItems: function (value) {
		if (arguments.length === 1) {
			this._legendItems = value;
			return value;
		} else {
			return this._legendItems;
		}
	}
	,
	chartInnerExtent: function () {
		return this.innerExtent();
	}
	,
	itemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.itemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.itemsSourceProperty);
		}
	}
	,
	innerExtent: function (value) {
		if (arguments.length === 1) {
			var coercedValue = value;
			if ($.ig.util.isNaN(value) || Number.isInfinity(value)) {
				coercedValue = 0;
			}
			if (value < 0) {
				coercedValue = 0;
			}
			if (value > 100) {
				coercedValue = 100;
			}
			this.setValue($.ig.PieChartBase.prototype.innerExtentProperty, coercedValue);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.innerExtentProperty);
		}
	}
	,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.valueMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.valueMemberPathProperty);
		}
	}
	,
	labelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.labelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.labelMemberPathProperty);
		}
	}
	,
	legendLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.legendLabelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.legendLabelMemberPathProperty);
		}
	}
	,
	__labelColumn: null,
	labelColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__labelColumn != value) {
				var oldColumn = this.labelColumn();
				this.__labelColumn = value;
				this.raisePropertyChanged($.ig.PieChartBase.prototype.labelColumnPropertyName, oldColumn, this.labelColumn());
			}
			return value;
		} else {
			return this.__labelColumn;
		}
	}
	,
	__legendLabelColumn: null,
	legendLabelColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__legendLabelColumn != value) {
				var oldColumn = this.legendLabelColumn();
				this.__legendLabelColumn = value;
				this.raisePropertyChanged($.ig.PieChartBase.prototype.legendLabelColumnPropertyName, oldColumn, this.legendLabelColumn());
			}
			return value;
		} else {
			return this.__legendLabelColumn;
		}
	}
	,
	labelsPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.labelsPositionProperty, $.ig.LabelsPosition.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PieChartBase.prototype.labelsPositionProperty));
		}
	}
	,
	leaderLineVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.leaderLineVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PieChartBase.prototype.leaderLineVisibilityProperty));
		}
	}
	,
	leaderLineStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.leaderLineStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.leaderLineStyleProperty);
		}
	}
	,
	leaderLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.leaderLineTypeProperty, $.ig.LeaderLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PieChartBase.prototype.leaderLineTypeProperty));
		}
	}
	,
	leaderLineMargin: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.leaderLineMarginProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.leaderLineMarginProperty);
		}
	}
	,
	toolTip: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.toolTipProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.toolTipProperty);
		}
	}
	,
	othersCategoryThreshold: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.othersCategoryThresholdProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.othersCategoryThresholdProperty);
		}
	}
	,
	othersCategoryType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.othersCategoryTypeProperty, $.ig.OthersCategoryType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PieChartBase.prototype.othersCategoryTypeProperty));
		}
	}
	,
	othersCategoryText: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.othersCategoryTextProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.othersCategoryTextProperty);
		}
	}
	,
	explodedRadius: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.explodedRadiusProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.explodedRadiusProperty);
		}
	}
	,
	actualExplodedRadius: function () {
		var radius = this.explodedRadius();
		if ($.ig.util.isNaN(radius) || Number.isInfinity(radius) || radius < 0) {
			return 0;
		}
		if (radius > 1) {
			return 1;
		}
		return radius;
	}
	,
	radiusFactor: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.radiusFactorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.radiusFactorProperty);
		}
	}
	,
	actualRadiusFactor: function () {
		var radiusFactor = this.radiusFactor();
		if ($.ig.util.isNaN(radiusFactor) || Number.isInfinity(radiusFactor) || radiusFactor < 0) {
			return 0;
		}
		if (radiusFactor > 1) {
			return 1;
		}
		return radiusFactor;
	}
	,
	allowSliceSelection: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.allowSliceSelectionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.allowSliceSelectionProperty);
		}
	}
	,
	allowSliceExplosion: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.allowSliceExplosionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.allowSliceExplosionProperty);
		}
	}
	,
	explodedSlices: function (value) {
		if (arguments.length === 1) {
			var $t = this.__explodedSlices;
			$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.__explodedIndicesChangedOverride);
			this.__explodedAttached = false;
			this.__explodedSlices = value;
			if (this.__explodedSlices != null) {
				var $t1 = this.__explodedSlices;
				$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.__explodedIndicesChangedOverride);
				this.__explodedAttached = true;
			}
			if (this.allowSliceExplosion()) {
				this.prepareSlices();
				this.prepareLabels();
				this.renderSlices();
				this.renderLabels();
			}
			return value;
		} else {
			return this.__explodedSlices;
		}
	}
	,
	__explodedSlices: null,
	legend: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.legendProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.legendProperty);
		}
	}
	,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.labelExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.labelExtentProperty);
		}
	}
	,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.startAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.startAngleProperty);
		}
	}
	,
	sweepDirection: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.sweepDirectionProperty, $.ig.SweepDirection.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.PieChartBase.prototype.sweepDirectionProperty));
		}
	}
	,
	selectedSlices: function (value) {
		if (arguments.length === 1) {
			var $t = this.__selectedSlices;
			$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.__selectedIndicesChangedOverride);
			this.__selectedAttached = false;
			this.__selectedSlices = value;
			if (this.__selectedSlices != null) {
				var $t1 = this.__selectedSlices;
				$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.__selectedIndicesChangedOverride);
				this.__selectedAttached = true;
			}
			if (this.allowSliceSelection()) {
				this.prepareSlices();
				this.prepareLabels();
				this.renderSlices();
				this.renderLabels();
				this.renderLegendItems();
			}
			return value;
		} else {
			return this.__selectedSlices;
		}
	}
	,
	__selectedSlices: null,
	othersCategoryStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.othersCategoryStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.othersCategoryStyleProperty);
		}
	}
	,
	selectedStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.selectedStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.selectedStyleProperty);
		}
	}
	,
	toolTipStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.toolTipStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.toolTipStyleProperty);
		}
	}
	,
	brushes: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.brushesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.brushesProperty);
		}
	}
	,
	outlines: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.outlinesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.outlinesProperty);
		}
	}
	,
	legendItemTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.legendItemTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.legendItemTemplateProperty);
		}
	}
	,
	legendItemBadgeTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.legendItemBadgeTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.legendItemBadgeTemplateProperty);
		}
	}
	,
	labelTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.labelTemplateProperty, value);
			return value;
		} else {
			return $.ig.util.cast($.ig.DataTemplate.prototype.$type, this.getValue($.ig.PieChartBase.prototype.labelTemplateProperty));
		}
	}
	,
	isSurfaceInteractionDisabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.isSurfaceInteractionDisabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.isSurfaceInteractionDisabledProperty);
		}
	}
	,
	formatLabel: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.formatLabelProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.formatLabelProperty);
		}
	}
	,
	formatLegendLabel: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PieChartBase.prototype.formatLegendLabelProperty, value);
			return value;
		} else {
			return this.getValue($.ig.PieChartBase.prototype.formatLegendLabelProperty);
		}
	}
	,
	labelClick: null,
	onLabelClick: function (sender, e) {
		if (this.labelClick != null) {
			this.labelClick(sender, e);
		}
	}
	,
	sliceClick: null,
	onSliceClick: function (sender, e) {
		if (this.sliceClick != null) {
			this.sliceClick(sender, e);
		}
	}
	,
	_slices: null,
	_labels: null,
	explodeSlice: function (slice, explode) {
		if (!this.explodedSlices().contains(slice.index()) && explode) {
			this.explodedSlices().add(slice.index());
		}
		if (this.explodedSlices().contains(slice.index()) && !explode) {
			this.explodedSlices().remove(slice.index());
		}
	}
	,
	selectSlice: function (slice, shouldSelect) {
		if (!this.selectedSlices().contains(slice.index()) && shouldSelect) {
			this.selectedSlices().add(slice.index());
		}
		if (this.selectedSlices().contains(slice.index()) && !shouldSelect) {
			this.selectedSlices().remove(slice.index());
		}
	}
	,
	setSliceAppearance: function (slice) {
		this.view().setSliceAppearance(slice);
	}
	,
	getLabel: function (slice) {
		return this.view().getLabel(slice);
	}
	,
	getSliceInnerBounds: function (slice, position) {
		var bounds = new $.ig.Rect(0, 0, 0, 0, 0);
		return bounds;
	}
	,
	fitsInsideBounds: function (label, center) {
		var slice = label.slice();
		if (slice == null) {
			return false;
		}
		var origin = slice.getSliceOrigin();
		var startAngle = this.sweepDirection() == $.ig.SweepDirection.prototype.clockwise ? slice.startAngle() : slice.endAngle();
		var endAngle = this.sweepDirection() == $.ig.SweepDirection.prototype.clockwise ? slice.endAngle() : slice.startAngle();
		var useAngleOffset = false;
		var isCircle = false;
		var startPoint = $.ig.GeometryUtil.prototype.findRadialPoint(origin, startAngle, slice.radius());
		var endPoint = $.ig.GeometryUtil.prototype.findRadialPoint(origin, endAngle, slice.radius());
		startAngle = $.ig.PieChartBase.prototype.findAngle(startPoint.__x, origin.__x, startPoint.__y, origin.__y);
		endAngle = $.ig.PieChartBase.prototype.findAngle(endPoint.__x, origin.__x, endPoint.__y, origin.__y);
		if (this.isCircle(slice)) {
			isCircle = true;
		}
		var labelRadius;
		labelRadius = $.ig.MathUtil.prototype.hypot(label.bounds().right() - origin.__x, label.bounds().top() - origin.__y);
		if (labelRadius > slice.radius()) {
			return false;
		}
		labelRadius = $.ig.MathUtil.prototype.hypot(label.bounds().right() - origin.__x, label.bounds().bottom() - origin.__y);
		if (labelRadius > slice.radius()) {
			return false;
		}
		labelRadius = $.ig.MathUtil.prototype.hypot(label.bounds().left() - origin.__x, label.bounds().top() - origin.__y);
		if (labelRadius > slice.radius()) {
			return false;
		}
		labelRadius = $.ig.MathUtil.prototype.hypot(label.bounds().left() - origin.__x, label.bounds().bottom() - origin.__y);
		if (labelRadius > slice.radius()) {
			return false;
		}
		if (isCircle) {
			return true;
		}
		if (startAngle > endAngle) {
			startAngle = startAngle - 360;
			useAngleOffset = true;
		}
		var labelAngle;
		labelAngle = $.ig.PieChartBase.prototype.findAngle(label.bounds().right(), origin.__x, label.bounds().top(), origin.__y);
		if (useAngleOffset && labelAngle > 180 && labelAngle < 360) {
			labelAngle = labelAngle - 360;
		}
		if (labelAngle < startAngle || labelAngle > endAngle) {
			return false;
		}
		labelAngle = $.ig.PieChartBase.prototype.findAngle(label.bounds().right(), origin.__x, label.bounds().bottom(), origin.__y);
		if (useAngleOffset && labelAngle > 180 && labelAngle < 360) {
			labelAngle = labelAngle - 360;
		}
		if (labelAngle < startAngle || labelAngle > endAngle) {
			return false;
		}
		labelAngle = $.ig.PieChartBase.prototype.findAngle(label.bounds().left(), origin.__x, label.bounds().top(), origin.__y);
		if (useAngleOffset && labelAngle > 180 && labelAngle < 360) {
			labelAngle = labelAngle - 360;
		}
		if (labelAngle < startAngle || labelAngle > endAngle) {
			return false;
		}
		labelAngle = $.ig.PieChartBase.prototype.findAngle(label.bounds().left(), origin.__x, label.bounds().bottom(), origin.__y);
		if (useAngleOffset && labelAngle > 180 && labelAngle < 360) {
			labelAngle = labelAngle - 360;
		}
		if (labelAngle < startAngle || labelAngle > endAngle) {
			return false;
		}
		return true;
	}
	,
	roundAngle: function (angle) {
		var val = Math.round(angle * Math.pow(10, 5)) / Math.pow(10, 5);
		return val;
	}
	,
	isCircle: function (slice) {
		return $.ig.PieChartBase.prototype.roundAngle(Math.abs(slice.endAngle() - slice.startAngle())) == 360;
	}
	,
	findAngle: function (x, centerX, y, centerY) {
		var h = $.ig.MathUtil.prototype.hypot(x - centerX, y - centerY);
		var angle = Math.asin((y - centerY) / h) * 180 / Math.PI;
		if (x < centerX) {
			angle = 180 - angle;
		}
		if (x > centerX) {
			angle = 360 + angle;
		}
		if (angle == 360) {
			angle = 0;
		}
		return $.ig.GeometryUtil.prototype.simplifyAngle(angle);
	}
	,
	sortLabels: function (labels) {
		var anchors = new Array(labels.count());
		for (var i = 0; i < labels.count(); i++) {
			var currentLabel = labels.__inner[i];
			var currentSlice = currentLabel.slice();
			var pt = $.ig.GeometryUtil.prototype.findRadialPoint(currentSlice.origin(), currentLabel.angle(), currentSlice.radius());
			anchors[i] = pt.__y;
		}
		for (var i1 = 0; i1 < labels.count(); i1++) {
			for (var j = i1 + 1; j < labels.count(); j++) {
				if (anchors[i1] >= anchors[j]) {
					var temp = labels.__inner[i1];
					labels.__inner[i1] = labels.__inner[j];
					labels.__inner[j] = temp;
					var tempAnchor = anchors[i1];
					anchors[i1] = anchors[j];
					anchors[j] = tempAnchor;
				}
			}
		}
	}
	,
	resolveCollisions: function (labels) {
		if (labels.count() == 0) {
			return;
		}
		var renderWidth = this.view().viewport().width();
		var renderHeight = this.view().viewport().height();
		var count = labels.count();
		var radius = labels.__inner[0].slice().radius();
		var center = labels.__inner[0].slice().getSliceOrigin();
		var hasEnoughSpace = true;
		var collisions = 0;
		var minHeight = Number.POSITIVE_INFINITY;
		var maxHeight = Number.NEGATIVE_INFINITY;
		for (var i = 0; i < count - 1; i++) {
			var currentLabel = labels.__inner[i];
			var nextLabel = labels.__inner[i + 1];
			if (currentLabel.bounds().intersectsWith(nextLabel.bounds())) {
				collisions++;
			}
		}
		var totalHeight = 0;
		var en = labels.getEnumerator();
		while (en.moveNext()) {
			var label = en.current();
			minHeight = Math.min(minHeight, label.bounds().height());
			maxHeight = Math.max(maxHeight, label.bounds().height());
			totalHeight += label.bounds().height();
		}
		if (totalHeight > renderHeight) {
			hasEnoughSpace = false;
		}
		if (hasEnoughSpace && collisions > 0) {
			for (var i1 = 0; i1 < count - 1; i1++) {
				for (var j = i1 + 1; j < count; j++) {
					var currentLabel1 = labels.__inner[i1];
					var nextLabel1 = labels.__inner[j];
					if (currentLabel1.bounds().intersectsWith(nextLabel1.bounds())) {
						var bounds = nextLabel1.bounds();
						bounds.y(Math.min(currentLabel1.bounds().bottom() + 0.01, renderHeight - minHeight));
						var c = this.labelExtent() + radius;
						var b = Math.abs(center.__y - (bounds.y() + minHeight / 2));
						var x = Math.sqrt(Math.abs(c * c - b * b));
						var angle = $.ig.GeometryUtil.prototype.simplifyAngle(nextLabel1.angle());
						if (angle > 90 && angle < 270) {
							x = (bounds.width() + x) * -1;
						}
						bounds.x(center.__x + x);
						nextLabel1.bounds(bounds);
					}
				}
			}
			for (var i2 = count - 1; i2 > 0; i2--) {
				for (var j1 = i2 - 1; j1 >= 0; j1--) {
					var currentLabel2 = labels.__inner[i2];
					var nextLabel2 = labels.__inner[j1];
					if (currentLabel2.bounds().intersectsWith(nextLabel2.bounds())) {
						var bounds1 = nextLabel2.bounds();
						bounds1.y(Math.max(currentLabel2.bounds().top() - minHeight - 0.01, 0));
						var c1 = this.labelExtent() + radius;
						var b1 = Math.abs(center.__y - (bounds1.y() + minHeight / 2));
						var x1 = Math.sqrt(Math.abs(c1 * c1 - b1 * b1));
						var angle1 = $.ig.GeometryUtil.prototype.simplifyAngle(nextLabel2.angle());
						if (angle1 > 90 && angle1 < 270) {
							x1 = (bounds1.width() + x1) * -1;
						}
						bounds1.x(center.__x + x1);
						nextLabel2.bounds(bounds1);
					}
				}
			}
		}
		var en1 = labels.getEnumerator();
		while (en1.moveNext()) {
			var label1 = en1.current();
			var bounds2 = label1.bounds();
			if (bounds2.left() > renderWidth || bounds2.right() < 0) {
				label1.__visibility = $.ig.Visibility.prototype.collapsed;
				if (this.leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
					label1.leaderLine().__visibility = $.ig.Visibility.prototype.collapsed;
				} else {
					label1.leaderLinePath().__visibility = $.ig.Visibility.prototype.collapsed;
				}
			} else if (bounds2.left() < 0) {
				var offset = Math.abs(bounds2.x());
				bounds2.x(0);
				if (offset > bounds2.width()) {
					bounds2.width(0);
				} else {
				}
				label1.bounds(bounds2);
			} else if (bounds2.right() > renderWidth) {
				var offset1 = bounds2.right() - renderWidth;
				if (offset1 > bounds2.width()) {
					bounds2.width(0);
				} else {
					bounds2.width(bounds2.width() - offset1);
				}
				label1.bounds(bounds2);
			}
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		this.renderChart();
	}
	,
	renderChart: function () {
		this.prepareData();
		this.prepareSlices();
		this.prepareLabels();
		this.renderSlices();
		this.renderLabels();
		this.renderLegendItems();
		this.view().updateView();
	}
	,
	prepareData: function () {
		this.total(0);
		this.othersTotal(0);
		this.valueIndices().clear();
		this.othersValueIndices().clear();
		this.others().clear();
		if (this.itemsSource() == null || this.fastItemsSource() == null) {
			return;
		}
		if (this.valueColumn() == null || this.valueColumn().count() == 0) {
			return;
		}
		var en = this.valueColumn().getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			if ($.ig.util.isNaN(value) || Number.isInfinity(value) || value <= 0) {
				continue;
			}
			this.total(this.total() + value);
		}
		for (var i = 0; i < this.valueColumn().count(); i++) {
			var value1 = this.valueColumn().item(i);
			var isInvalidValue = value1 <= 0;
			if ($.ig.util.isNaN(value1) || Number.isInfinity(value1) || isInvalidValue) {
				continue;
			}
			var calculatedValue = this.othersCategoryType() == $.ig.OthersCategoryType.prototype.percent ? value1 / this.total() : value1;
			var calculatedThreshold = this.othersCategoryType() == $.ig.OthersCategoryType.prototype.percent ? this.othersCategoryThreshold() / 100 : this.othersCategoryThreshold();
			var useOthersSlice = true;
			if (calculatedValue <= calculatedThreshold && useOthersSlice) {
				this.othersTotal(this.othersTotal() + value1);
				this.othersValueIndices().add(i);
				this.others().add1(this.fastItemsSource().item(i));
			} else {
				this.valueIndices().add(i);
			}
		}
	}
	,
	prepareSlices: function () {
		if (this.itemsSource() == null || this.fastItemsSource() == null) {
			this._slices.count(0);
			return;
		}
		var totalSliceCount = this.valueIndices().count();
		var hasOtherSlice = this.othersValueIndices().count() > 0;
		var startAngle = $.ig.PieChartBase.prototype.roundAngle(this.actualStartAngle());
		var endAngle = $.ig.PieChartBase.prototype.roundAngle(this.actualStartAngle());
		if (hasOtherSlice) {
			totalSliceCount++;
		}
		this.totalSliceCount(totalSliceCount);
		for (var i = 0; i < totalSliceCount; i++) {
			var isOtherSlice = false;
			var value;
			if (i == totalSliceCount - 1 && hasOtherSlice) {
				value = this.othersTotal();
				isOtherSlice = true;
			} else {
				value = this.valueColumn().item(this.valueIndices().__inner[i]);
			}
			if (this.sweepDirection() == $.ig.SweepDirection.prototype.clockwise) {
				endAngle += $.ig.PieChartBase.prototype.roundAngle(Math.abs(value) * 360 / this.total());
			} else {
				endAngle -= $.ig.PieChartBase.prototype.roundAngle(Math.abs(value) * 360 / this.total());
			}
			var slice = this._slices.item(i);
			slice.suspendCreation(true);
			slice.startAngle(startAngle);
			slice.endAngle(endAngle);
			slice.innerExtentStart(slice.innerExtentEnd(this.chartInnerExtent()));
			slice.isOthersSlice(isOtherSlice);
			slice.explodedRadius(this.actualExplodedRadius());
			slice.index(i);
			slice.dataContext(isOtherSlice ? this.others() : this.fastItemsSource().item(this.valueIndices().__inner[i]));
			slice.isExploded(this.explodedSlices().contains(i));
			slice.isSelected(this.selectedSlices().contains(i));
			startAngle = endAngle;
			slice.suspendCreation(false);
		}
		this._slices.count(totalSliceCount);
	}
	,
	prepareLabels: function () {
		var $self = this;
		if (this.itemsSource() == null || this.fastItemsSource() == null) {
			this._labels.count(0);
			return;
		}
		if (this.labelColumn() == null || this.labelColumn().count() == 0 || this.labelsPosition() == $.ig.LabelsPosition.prototype.none) {
			this._labels.count(0);
			return;
		}
		var totalLabelCount = this.valueIndices().count();
		if (this.othersValueIndices().count() > 0) {
			totalLabelCount++;
		}
		this.view().labelPreMeasure();
		for (var i = 0; i < totalLabelCount; i++) {
			var labelString = String.empty();
			var isOthersLabel = false;
			var labelFromLabelColumn;
			var legendLabelFromLegendLabelColumn = null;
			if (i == totalLabelCount - 1 && this.othersValueIndices().count() > 0) {
				labelFromLabelColumn = this.othersCategoryText();
				isOthersLabel = true;
				if (this.formatLabel() != null) {
					var context = (function () {
						var $ret = new $.ig.PieSliceDataContext();
						$ret.series($self);
						$ret.item(isOthersLabel ? $self.others() : $self.fastItemsSource().item($self.valueIndices().__inner[i]));
						$ret.itemLabel(labelFromLabelColumn);
						$ret.legendLabel(labelFromLabelColumn);
						$ret.percentValue($self.getPercentValue($self._slices.item(i)));
						$ret.isOthersSlice(isOthersLabel);
						return $ret;
					}());
					var c = context;
					c = (c).flatten();
					labelFromLabelColumn = this.formatLabel()(c);
				}
				if (null != labelFromLabelColumn) {
					labelString = labelFromLabelColumn.toString();
				} else {
					labelString = this.othersCategoryText();
				}
			} else {
				labelFromLabelColumn = this.labelColumn().item(this.valueIndices().__inner[i]);
				legendLabelFromLegendLabelColumn = (this.legendLabelColumn() != null && this.legendLabelColumn().count() > 0) ? this.legendLabelColumn().item(this.valueIndices().__inner[i]) : null;
				if (this.formatLabel() != null) {
					var context1 = (function () {
						var $ret = new $.ig.PieSliceDataContext();
						$ret.series($self);
						$ret.item(isOthersLabel ? $self.others() : $self.fastItemsSource().item($self.valueIndices().__inner[i]));
						$ret.itemLabel(labelFromLabelColumn);
						$ret.legendLabel(legendLabelFromLegendLabelColumn != null ? legendLabelFromLegendLabelColumn : labelFromLabelColumn);
						$ret.percentValue($self.getPercentValue($self._slices.item(i)));
						$ret.isOthersSlice(isOthersLabel);
						return $ret;
					}());
					var c1 = context1;
					c1 = (c1).flatten();
					var formatedLabel_ = this.formatLabel()(c1);
					if (null == formatedLabel_) {
						labelFromLabelColumn = this.labelColumn().item(this.valueIndices().__inner[i]);
					} else {
						labelFromLabelColumn = formatedLabel_;
					}
				}
				var label_ = labelFromLabelColumn;
				if(label_ === undefined) { throw new Error($.ig.Chart.locale.invalidLabelBinding); };
				if (null != labelFromLabelColumn) {
					labelString = labelFromLabelColumn.toString();
				}
			}
			var label = this._labels.item(i);
			var slice = this._slices.item(i);
			slice.label(label);
			slice.legendLabel($.ig.util.cast(String, legendLabelFromLegendLabelColumn));
			label.angle($.ig.GeometryUtil.prototype.simplifyAngle((slice.startAngle() + slice.endAngle()) / 2));
			label.slice(slice);
			label.label((function () {
				var $ret = new $.ig.TextBlock();
				$ret.text(labelString);
				return $ret;
			}()));
			if (this.labelTemplate() == null) {
				label.dataContext(isOthersLabel ? this.others() : this.fastItemsSource().item(this.valueIndices().__inner[i]));
				label.createContent(this.view());
			} else {
			}
			label.__visibility = $.ig.Visibility.prototype.visible;
			label.bounds(this.view().getLabelBounds(label));
		}
		this._labels.count(totalLabelCount);
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
	renderSlices: function () {
		if (this.itemsSource() == null || this.fastItemsSource() == null) {
			return;
		}
		var pieCanvasSize = this.view().updatePieViewport();
		this.viewport(new $.ig.Rect(0, 0, 0, pieCanvasSize.width(), pieCanvasSize.height()));
		var center = { __x: pieCanvasSize.width() / 2, __y: pieCanvasSize.height() / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var radius = Math.min(pieCanvasSize.height() / 2, pieCanvasSize.width() / 2) * this.actualRadiusFactor();
		var en = this._slices.active().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			var explodedCenter = $.ig.GeometryUtil.prototype.findCenter(pieCanvasSize.width(), pieCanvasSize.height(), true, (slice.startAngle() + slice.endAngle()) / 2, radius * this.actualExplodedRadius());
			slice.suspendCreation(true);
			slice.innerExtentStart(slice.innerExtentEnd(this.chartInnerExtent()));
			slice.radius(radius);
			slice.explodedRadius(this.actualExplodedRadius());
			slice.origin(center);
			slice.explodedOrigin(explodedCenter);
			this.setSliceAppearance(slice);
			slice.suspendCreation(false);
		}
		this.view().updateView();
	}
	,
	renderLabels: function () {
		var renderWidth = this.view().viewport().width();
		var renderHeight = this.view().viewport().height();
		if (renderHeight == 0 || renderWidth == 0) {
			return;
		}
		if (this._labels.active().count() == 0 || this.labelsPosition() == $.ig.LabelsPosition.prototype.none) {
			this._labels.count(0);
			return;
		}
		var rightLabels = new $.ig.List$1($.ig.PieLabel.prototype.$type, 0);
		var leftLabels = new $.ig.List$1($.ig.PieLabel.prototype.$type, 0);
		var centerLabels = new $.ig.List$1($.ig.PieLabel.prototype.$type, 0);
		var insideEndLabels = new $.ig.List$1($.ig.PieLabel.prototype.$type, 0);
		var en = this._labels.active().getEnumerator();
		while (en.moveNext()) {
			var label = en.current();
			var slice = label.slice();
			if (slice == null) {
				continue;
			}
			var center = slice.getSliceOrigin();
			var width = label.bounds().width();
			var height = label.bounds().height();
			if (this.labelsPosition() == $.ig.LabelsPosition.prototype.center || this.labelsPosition() == $.ig.LabelsPosition.prototype.bestFit) {
				var innerRadius = slice.radius() * slice.innerExtentStart() / 100;
				var labelCenter = $.ig.GeometryUtil.prototype.findRadialPoint(center, label.angle(), slice.radius() - (slice.radius() - innerRadius) / 2);
				label.bounds(new $.ig.Rect(0, labelCenter.__x - width / 2, labelCenter.__y - height / 2, width, height));
				var fitsInCenter = this.fitsInsideBounds(label, labelCenter);
				if (fitsInCenter || this.labelsPosition() == $.ig.LabelsPosition.prototype.center) {
					centerLabels.add(label);
					label.actualLabelPosition($.ig.LabelsPosition.prototype.center);
					if (label.slice().owner().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
						label.leaderLine().__visibility = $.ig.Visibility.prototype.collapsed;
					} else {
						label.leaderLinePath().__visibility = $.ig.Visibility.prototype.collapsed;
					}
					if (!fitsInCenter && this.labelsPosition() == $.ig.LabelsPosition.prototype.center) {
						label.__visibility = $.ig.Visibility.prototype.collapsed;
					} else {
						label.__visibility = $.ig.Visibility.prototype.visible;
					}
					continue;
				}
			}
			if (this.labelsPosition() == $.ig.LabelsPosition.prototype.insideEnd || this.labelsPosition() == $.ig.LabelsPosition.prototype.bestFit) {
				var labelOffset = $.ig.PieChartBase.prototype.findLabelOffset(center, slice.radius(), label);
				var labelCenter1 = $.ig.GeometryUtil.prototype.findRadialPoint(center, label.angle(), labelOffset);
				label.bounds(new $.ig.Rect(0, labelCenter1.__x - width / 2, labelCenter1.__y - height / 2, width, height));
				var fitsOnInside = this.fitsInsideBounds(label, labelCenter1);
				if (fitsOnInside || this.labelsPosition() == $.ig.LabelsPosition.prototype.insideEnd) {
					insideEndLabels.add(label);
					label.actualLabelPosition($.ig.LabelsPosition.prototype.insideEnd);
					if (label.slice().owner().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
						label.leaderLine().__visibility = $.ig.Visibility.prototype.collapsed;
					} else {
						label.leaderLinePath().__visibility = $.ig.Visibility.prototype.collapsed;
					}
					if (!fitsOnInside && this.labelsPosition() == $.ig.LabelsPosition.prototype.insideEnd) {
						label.__visibility = $.ig.Visibility.prototype.collapsed;
					} else {
						label.__visibility = $.ig.Visibility.prototype.visible;
					}
					continue;
				}
			}
			var labelPoint = $.ig.GeometryUtil.prototype.findRadialPoint(center, label.angle(), slice.radius() + this.labelExtent());
			label.actualLabelPosition($.ig.LabelsPosition.prototype.outsideEnd);
			label.__visibility = $.ig.Visibility.prototype.visible;
			label.updateCascadingLeaderLineStroke();
			if (label.angle() < 90 && label.angle() >= 0) {
				label.bounds(new $.ig.Rect(0, labelPoint.__x, labelPoint.__y, width, height));
				rightLabels.add(label);
			} else if (label.angle() < 180 && label.angle() >= 90) {
				label.bounds(new $.ig.Rect(0, labelPoint.__x - width, labelPoint.__y, width, height));
				leftLabels.add(label);
			} else if (label.angle() < 270 && label.angle() >= 180) {
				label.bounds(new $.ig.Rect(0, labelPoint.__x - width, labelPoint.__y - height, width, height));
				leftLabels.add(label);
			} else {
				label.bounds(new $.ig.Rect(0, labelPoint.__x, labelPoint.__y - height, width, height));
				rightLabels.add(label);
			}
			if (label.bounds().y() < 0) {
				label.bounds(new $.ig.Rect(0, label.bounds().x(), 0, label.bounds().width(), label.bounds().height()));
			}
			if (label.bounds().bottom() > renderHeight) {
				label.bounds(new $.ig.Rect(0, label.bounds().x(), renderHeight - label.bounds().height(), label.bounds().width(), label.bounds().height()));
			}
		}
		$.ig.PieChartBase.prototype.sortLabels(rightLabels);
		this.resolveCollisions(rightLabels);
		$.ig.PieChartBase.prototype.sortLabels(leftLabels);
		this.resolveCollisions(leftLabels);
		var en1 = centerLabels.getEnumerator();
		while (en1.moveNext()) {
			var label1 = en1.current();
			this.view().updateLabelPosition(label1, label1.bounds().x(), label1.bounds().y());
		}
		var en2 = insideEndLabels.getEnumerator();
		while (en2.moveNext()) {
			var label2 = en2.current();
			this.view().updateLabelPosition(label2, label2.bounds().x(), label2.bounds().y());
		}
		var en3 = rightLabels.getEnumerator();
		while (en3.moveNext()) {
			var label3 = en3.current();
			this.view().updateLabelPosition(label3, label3.bounds().x(), label3.bounds().y());
			if (label3.slice().owner().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
				label3.updateLeaderLine();
			} else {
				label3.updateLeaderLinePath();
			}
		}
		var en4 = leftLabels.getEnumerator();
		while (en4.moveNext()) {
			var label4 = en4.current();
			this.view().updateLabelPosition(label4, label4.bounds().x(), label4.bounds().y());
			if (label4.slice().owner().leaderLineType() == $.ig.LeaderLineType.prototype.straight) {
				label4.updateLeaderLine();
			} else {
				label4.updateLeaderLinePath();
			}
		}
		this.view().updateView();
	}
	,
	findLabelOffset: function (center, radius, label) {
		var labelAngleRadians = label.angle() * Math.PI / 180;
		var testRadius = 1;
		var rectCenter = $.ig.GeometryUtil.prototype.findRadialPoint({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, label.angle(), testRadius);
		var collisionPoint = $.ig.PieChartBase.prototype.getCollisionPoint(label);
		var dist = Math.sqrt(collisionPoint.__x * collisionPoint.__x + collisionPoint.__y * collisionPoint.__y);
		var collisionVect = { __x: collisionPoint.__x / dist, __y: collisionPoint.__y / dist, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var angle = $.ig.PieChartBase.prototype.getAngleBetween(rectCenter, collisionVect);
		var p = dist / radius;
		var angleAtEnd = angle * p;
		var deltaAngle = angleAtEnd;
		var centAng = $.ig.GeometryUtil.prototype.getAngleTo({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, rectCenter);
		var collAng = $.ig.GeometryUtil.prototype.getAngleTo({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, collisionVect);
		if (collAng < centAng) {
			deltaAngle = -deltaAngle;
		}
		var collisionPointAtEnd = $.ig.GeometryUtil.prototype.findRadialPoint(center, (labelAngleRadians + deltaAngle) * 180 / Math.PI, radius);
		var centerAtEnd = $.ig.PieChartBase.prototype.getCenterFromCollisionPoint(label, collisionPointAtEnd);
		var distToCenter = Math.sqrt((centerAtEnd.__x - center.__x) * (centerAtEnd.__x - center.__x) + (centerAtEnd.__y - center.__y) * (centerAtEnd.__y - center.__y));
		var labelOffset = distToCenter - 5;
		return labelOffset;
	}
	,
	getCenterFromCollisionPoint: function (label, collisionPointAtEnd) {
		var angle = $.ig.GeometryUtil.prototype.simplifyAngle(label.angle());
		var halfWidth = label.bounds().width() / 2;
		var halfHeight = label.bounds().height() / 2;
		if (angle >= 0 && angle <= 90) {
			return { __x: collisionPointAtEnd.__x - halfWidth, __y: collisionPointAtEnd.__y - halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (angle >= 90 && angle <= 180) {
			return { __x: collisionPointAtEnd.__x + halfWidth, __y: collisionPointAtEnd.__y - halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (angle >= 180 && angle <= 270) {
			return { __x: collisionPointAtEnd.__x + halfWidth, __y: collisionPointAtEnd.__y + halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		return { __x: collisionPointAtEnd.__x - halfWidth, __y: collisionPointAtEnd.__y + halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getAngleBetween: function (rectCenter, collisionVect) {
		var prod = rectCenter.__x * collisionVect.__x + rectCenter.__y * collisionVect.__y;
		var magCenter = Math.sqrt(rectCenter.__x * rectCenter.__x + rectCenter.__y * rectCenter.__y);
		var magColl = Math.sqrt(collisionVect.__x * collisionVect.__x + collisionVect.__y * collisionVect.__y);
		var ang = Math.acos(prod / (magCenter * magColl));
		return ang;
	}
	,
	getCollisionPoint: function (label) {
		var angle = $.ig.GeometryUtil.prototype.simplifyAngle(label.angle());
		var halfWidth = label.bounds().width() / 2;
		var halfHeight = label.bounds().height() / 2;
		if (angle >= 0 && angle <= 90) {
			return { __x: halfWidth, __y: halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (angle >= 90 && angle <= 180) {
			return { __x: -halfWidth, __y: halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (angle >= 180 && angle <= 270) {
			return { __x: -halfWidth, __y: -halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		return { __x: halfWidth, __y: -halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	renderLegendItems: function () {
		var $self = this;
		var itemLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, this.legend());
		if (itemLegend == null) {
			return;
		}
		if ((this.labelColumn() == null || this.labelColumn().count() == 0) && (this.legendLabelColumn() == null || this.legendLabelColumn().count() == 0)) {
			itemLegend.clearLegendItems(this);
			return;
		}
		this.legendItems(new $.ig.List$1($.ig.UIElement.prototype.$type, 0));
		var en = this._slices.active().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			var item = new $.ig.ContentControl();
			var itemLabel = this.getLabel(slice);
			var itemBrush = this.view().getActualBackground(slice);
			item.content((function () {
				var $ret = new $.ig.PieSliceDataContext();
				$ret.series($self);
				$ret.slice(slice);
				$ret.item(slice.dataContext());
				$ret.itemBrush(itemBrush);
				$ret.itemLabel(itemLabel != null ? itemLabel.toString() : null);
				$ret.legendLabel(slice.isOthersSlice() ? $self.othersCategoryText() : slice.legendLabel() != null ? slice.legendLabel() : (itemLabel != null ? itemLabel.toString() : null));
				$ret.percentValue($self.getPercentValue(slice));
				$ret.isOthersSlice(slice.isOthersSlice());
				return $ret;
			}()));
			var dc = item.content();
			if (this.formatLegendLabel() != null) {
				var legendLabel = this.formatLegendLabel()(item.content());
				if (legendLabel != null) {
					dc.legendLabel(legendLabel.toString());
				}
			}
			if (this.legendItemTemplate() != null) {
				item.contentTemplate(this.legendItemTemplate());
			}
			this.legendItems().add(item);
		}
		itemLegend.createLegendItems(this.legendItems(), this);
	}
	,
	exportVisualData: function () {
		var chart = new $.ig.PieChartVisualData();
		chart.viewport(this.viewport());
		this.view().exportViewData(chart);
		for (var i = 0; i < this._slices.count(); i++) {
			var slice = this._slices.item(i).exportVisualData();
			chart.slices().add(slice);
			if (this._slices.item(i).isOthersSlice()) {
				chart.othersSlice(this._slices.item(i).exportVisualData());
			}
		}
		chart.others(this.others());
		chart.name(this.name());
		chart.radiusFactor(this.actualRadiusFactor());
		chart.leaderLineMargin(this.leaderLineMargin());
		return chart;
	}
	,
	registerDoubleColumn: function (memberPath) {
		var coercionMethod = null;
		return this.fastItemsSource().registerColumn(memberPath, coercionMethod, false);
	}
	,
	registerObjectColumn: function (memberPath) {
		var coercionMethod = null;
		return this.fastItemsSource().registerColumnObject(memberPath, coercionMethod, false);
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.PieChartBase.prototype.itemsSourcePropertyName:
				this.fastItemsSource((function () {
					var $ret = new $.ig.FastItemsSource();
					$ret.itemsSource(newValue);
					return $ret;
				}()));
				break;
			case $.ig.PieChartBase.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, oldValue) != null) {
					var $t = (oldValue);
					$t.event = $.ig.Delegate.prototype.remove($t.event, this._fastItemsSource_Event);
					this.__fastItemsSourceAttached = false;
					(oldValue).deregisterColumn(this.valueColumn());
					(oldValue).deregisterColumn(this.labelColumn());
					this.valueColumn(null);
					this.labelColumn(null);
					($.ig.util.cast($.ig.FastItemsSource.prototype.$type, oldValue)).deregisterColumn(this.legendLabelColumn());
					this.legendLabelColumn(null);
				}
				if ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, newValue) != null) {
					var $t1 = (newValue);
					$t1.event = $.ig.Delegate.prototype.combine($t1.event, this._fastItemsSource_Event);
					this.__fastItemsSourceAttached = true;
					this.valueColumn(this.registerDoubleColumn(this.valueMemberPath()));
					this.labelColumn(this.registerObjectColumn(this.labelMemberPath()));
					this.legendLabelColumn(this.registerObjectColumn(this.legendLabelMemberPath()));
				}
				this.renderChart();
				break;
			case $.ig.PieChartBase.prototype.valueMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.valueColumn());
					this.valueColumn(this.registerDoubleColumn(this.valueMemberPath()));
				}
				break;
			case $.ig.PieChartBase.prototype.legendLabelMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.legendLabelColumn());
					this.legendLabelColumn(this.registerObjectColumn(this.legendLabelMemberPath()));
					this.renderLegendItems();
				}
				break;
			case $.ig.PieChartBase.prototype.formatLegendLabelPropertyName:
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.formatLabelPropertyName:
			case $.ig.PieChartBase.prototype.labelMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.labelColumn());
					this.labelColumn(this.registerObjectColumn(this.labelMemberPath()));
					this.prepareLabels();
					this.renderLabels();
					this.renderLegendItems();
				}
				break;
			case $.ig.PieChartBase.prototype.startAnglePropertyName:
				this.actualStartAngle(newValue);
				this.prepareSlices();
				this.prepareLabels();
				this.renderSlices();
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.toolTipPropertyName:
				this.view().updateToolTipContent(this.toolTip());
				break;
			case $.ig.PieChartBase.prototype.legendPropertyName:
				var oldLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, oldValue);
				if (oldLegend != null) {
					oldLegend.clearLegendItems(this);
				}
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.legendItemBadgeTemplatePropertyName:
			case $.ig.PieChartBase.prototype.legendItemTemplatePropertyName:
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.radiusFactorPropertyName:
			case $.ig.PieChartBase.prototype.explodedRadiusPropertyName:
			case $.ig.PieChartBase.prototype.sweepDirectionPropertyName:
				this.prepareSlices();
				this.prepareLabels();
				this.renderSlices();
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.othersCategoryStylePropertyName:
			case $.ig.PieChartBase.prototype.selectedStylePropertyName:
				this.renderSlices();
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.brushesPropertyName:
				if (oldValue != null) {
					var ov = oldValue;
					ov.collectionChanged = $.ig.Delegate.prototype.remove(ov.collectionChanged, this.__brushesChangedOverride);
					this.__brushesAttached = false;
				}
				if (newValue != null) {
					var bc = newValue;
					bc.collectionChanged = $.ig.Delegate.prototype.combine(bc.collectionChanged, this.__brushesChangedOverride);
					this.__brushesAttached = true;
				}
				this.renderSlices();
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.outlinesPropertyName:
				if (oldValue != null) {
					var ov1 = oldValue;
					ov1.collectionChanged = $.ig.Delegate.prototype.remove(ov1.collectionChanged, this.__brushesChangedOverride);
					this.__outlinesAttached = false;
				}
				if (newValue != null) {
					var bc1 = newValue;
					bc1.collectionChanged = $.ig.Delegate.prototype.combine(bc1.collectionChanged, this.__brushesChangedOverride);
					this.__outlinesAttached = true;
				}
				this.renderSlices();
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.valueColumnPropertyName:
			case $.ig.PieChartBase.prototype.othersCategoryThresholdPropertyName:
			case $.ig.PieChartBase.prototype.othersCategoryTypePropertyName:
			case $.ig.PieChartBase.prototype.allowSliceExplosionPropertyName:
			case $.ig.PieChartBase.prototype.allowSliceSelectionPropertyName:
				this.renderChart();
				break;
			case $.ig.PieChartBase.prototype.labelsPositionPropertyName:
				this.prepareLabels();
				this.renderLabels();
				this.view().updateView();
				break;
			case $.ig.PieChartBase.prototype.labelExtentPropertyName:
				this.prepareLabels();
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.othersCategoryTextPropertyName:
				this.prepareLabels();
				this.renderLabels();
				this.renderLegendItems();
				break;
			case $.ig.PieChartBase.prototype.leaderLineVisibilityPropertyName:
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.leaderLineStylePropertyName:
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.textStylePropertyName:
				this.view().styleUpdated();
				break;
			case $.ig.PieChartBase.prototype._labelTemplatePropertyName:
				this.prepareLabels();
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.leaderLineTypePropertyName:
			case $.ig.PieChartBase.prototype.leaderLineMarginPropertyName:
				this._labels.doToAll(this.view().updateLabelLeaderLine.runOn(this.view()));
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.isSurfaceInteractionDisabledPropertyName:
				this.view().onIsSurfaceInteractionDisabledChanged();
				break;
			case $.ig.PieChartBase.prototype.innerExtentPropertyName:
				this.renderChart();
				break;
		}
	}
	,
	selectedIndicesChangedOverride: function (args) {
		if (this.allowSliceSelection()) {
			this.prepareSlices();
			this.renderSlices();
			this.renderLegendItems();
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	sliceClicked: function (slice, args) {
		this.sliceClicked1(slice, args, false);
	}
	,
	sliceClicked1: function (slice, args, fromLabel) {
		var sliceClickEventArgs = new $.ig.SliceClickEventArgs(slice, this.view().getOriginalEvent(args));
		this.onSliceClick(this, sliceClickEventArgs);
		if (!fromLabel) {
			this.view().updateToolTip(slice, args);
		}
	}
	,
	labelClicked: function (slice, args) {
		var labelClickEventArgs = new $.ig.LabelClickEventArgs(slice, this.view().getOriginalEvent(args));
		this.onLabelClick(this, labelClickEventArgs);
		return labelClickEventArgs.allowSliceClick();
	}
	,
	itemEntered: function (item, args) {
		this.view().updateToolTip(item, args);
	}
	,
	itemMouseMoved1: function (item, args, fromLabel) {
		this.view().updateToolTip(item, args);
	}
	,
	itemMouseMoved: function (item, args) {
		this.itemMouseMoved1(item, args, false);
	}
	,
	itemMouseLeft: function (o, e) {
		this.view().closeToolTip();
	}
	,
	onSizeUpdated: function () {
		this.renderChart();
	}
	,
	provideContainer: function (container) {
		this.view().onContainerProvided(container);
	}
	,
	onContainerResized: function (width, height) {
		this.view().onContainerResized(width, height);
	}
	,
	notifyContainerResized: function () {
		this.view().notifyContainerResized();
	}
	,
	getContainerRect: function () {
		return this.view().getContainerRect();
	}
	,
	getContainerOffsets: function () {
		return this.view().getContainerOffsets();
	}
	,
	destroy: function () {
		this.removeWidgetLevelDataSource();
		this.view().onContainerProvided(null);
	}
	,
	flush: function () {
		this.view().flush();
	}
	,
	__textStyle: null,
	textStyle: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__textStyle;
			this.__textStyle = value;
			this.raisePropertyChanged($.ig.PieChartBase.prototype.textStylePropertyName, oldValue, value);
			return value;
		} else {
			return this.__textStyle;
		}
	}
	,
	getPercentValue: function (slice) {
		if (slice == null || this.valueColumn() == null || this.valueIndices() == null) {
			return NaN;
		}
		if (slice.isOthersSlice()) {
			return this.othersTotal() / this.total() * 100;
		} else {
			return this.valueColumn().item(this.valueIndices().__inner[slice.index()]) / this.total() * 100;
		}
	}
	,
	notifySetItem: function (source_, index, oldItem, newItem) {
		if (source_.dataView && source_.dataSource) { source_ = source_.dataView() };
		if (!this.sourcesEqual(source_)) {
			return;
		}
		var itemsSource = this.fastItemsSource();
		if (itemsSource == null) {
			return;
		}
		itemsSource.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(2, $.ig.NotifyCollectionChangedAction.prototype.replace, newItem, oldItem, index));
	}
	,
	notifyClearItems: function (source_) {
		if (source_.dataView && source_.dataSource) { source_ = source_.dataView() };
		if (!this.sourcesEqual(source_)) {
			return;
		}
		var itemsSource = this.fastItemsSource();
		itemsSource.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset));
	}
	,
	sourcesEqual: function (source) {
		return $.ig.PieChartBase.prototype.sourcesEqual1(source, this.itemsSource());
	}
	,
	sourcesEqual1: function (source, itemsSource) {
		return source == itemsSource;
	}
	,
	notifyInsertItem: function (source_, index, newItem) {
		if (source_.dataView && source_.dataSource) { source_ = source_.dataView() };
		if (!this.sourcesEqual(source_)) {
			return;
		}
		var itemsSource = this.fastItemsSource();
		if (itemsSource == null) {
			return;
		}
		itemsSource.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, newItem, index));
	}
	,
	notifyRemoveItem: function (source_, index, oldItem) {
		if (source_.dataView && source_.dataSource) { source_ = source_.dataView() };
		if (!this.sourcesEqual(source_)) {
			return;
		}
		var itemsSource = this.fastItemsSource();
		if (itemsSource == null) {
			return;
		}
		itemsSource.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.remove, oldItem, index));
	}
	,
	setWidgetLevelDataSource: function (source_) {
		if (source_.dataView && source_.dataSource) { source_ = source_.dataView() };
		this.itemsSource(source_);
	}
	,
	removeWidgetLevelDataSource: function () {
		this.itemsSource(null);
	}
	,
	styleUpdated: function () {
		this.view().styleUpdated();
	}
	,
	getFontInfo: function () {
		return $.ig.FontUtil.prototype.toFontInfo(this.textStyle());
	}
	,
	$type: new $.ig.Type('PieChartBase', $.ig.Control.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('Slice', 'ContentControl', {
	createView: function () {
		return new $.ig.SliceView(this);
	}
	,
	onViewCreated: function (view) {
		this.view(view);
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	init: function () {
		this.__suspendCreation = false;
		$.ig.ContentControl.prototype.init.call(this);
		var view = this.createView();
		this.onViewCreated(view);
		view.onInit();
	},
	startAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.startAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.startAngleProperty);
		}
	}
	,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.endAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.endAngleProperty);
		}
	}
	,
	innerExtentStart: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.innerExtentStartProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.innerExtentStartProperty);
		}
	}
	,
	innerExtentEnd: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.innerExtentEndProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.innerExtentEndProperty);
		}
	}
	,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.isSelectedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.isSelectedProperty);
		}
	}
	,
	isExploded: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.isExplodedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.isExplodedProperty);
		}
	}
	,
	isOthersSlice: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.isOtherSliceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.isOtherSliceProperty);
		}
	}
	,
	origin: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.originProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.originProperty);
		}
	}
	,
	explodedOrigin: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.explodedOriginProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.explodedOriginProperty);
		}
	}
	,
	radius: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.radiusProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.radiusProperty);
		}
	}
	,
	explodedRadius: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.explodedRadiusProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.explodedRadiusProperty);
		}
	}
	,
	index: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.indexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.Slice.prototype.indexProperty));
		}
	}
	,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Slice.prototype.strokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Slice.prototype.strokeThicknessProperty);
		}
	}
	,
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
	_owner: null,
	owner: function (value) {
		if (arguments.length === 1) {
			this._owner = value;
			return value;
		} else {
			return this._owner;
		}
	}
	,
	_label: null,
	label: function (value) {
		if (arguments.length === 1) {
			this._label = value;
			return value;
		} else {
			return this._label;
		}
	}
	,
	_legendLabel: null,
	legendLabel: function (value) {
		if (arguments.length === 1) {
			this._legendLabel = value;
			return value;
		} else {
			return this._legendLabel;
		}
	}
	,
	_correctedExplodedBounds: null,
	correctedExplodedBounds: function (value) {
		if (arguments.length === 1) {
			this._correctedExplodedBounds = value;
			return value;
		} else {
			return this._correctedExplodedBounds;
		}
	}
	,
	_correctedExplodedOrigin: null,
	correctedExplodedOrigin: function (value) {
		if (arguments.length === 1) {
			this._correctedExplodedOrigin = value;
			return value;
		} else {
			return this._correctedExplodedOrigin;
		}
	}
	,
	_hasCorrecttedBounds: false,
	hasCorrecttedBounds: function (value) {
		if (arguments.length === 1) {
			this._hasCorrecttedBounds = value;
			return value;
		} else {
			return this._hasCorrecttedBounds;
		}
	}
	,
	onApplyTemplate: function () {
		$.ig.ContentControl.prototype.onApplyTemplate.call(this);
		this.view().onTemplateProvided();
	}
	,
	getActualRadius: function () {
		var circle = Math.abs($.ig.PieChartBase.prototype.roundAngle(this.endAngle() - this.startAngle())) == 360;
		if (circle) {
			return this.radius();
		}
		return this.radius();
	}
	,
	getSliceBounds: function () {
		var allowExploded = (this.owner() != null && this.owner().allowSliceExplosion()) ? true : false;
		var actualRadius = this.getActualRadius();
		if (this.isExploded() && allowExploded && this.owner().totalSliceCount() > 1) {
			var bounds = new $.ig.Rect(0, this.explodedOrigin().__x - actualRadius, this.explodedOrigin().__y - actualRadius, actualRadius * 2, actualRadius * 2);
			return bounds;
		}
		return new $.ig.Rect(0, this.origin().__x - actualRadius, this.origin().__y - actualRadius, actualRadius * 2, actualRadius * 2);
	}
	,
	getSliceOrigin: function () {
		if (this.isExploded() && this.owner() != null && this.owner().allowSliceExplosion() && this.owner().totalSliceCount() > 1) {
			if (this.hasCorrecttedBounds()) {
				return this.correctedExplodedOrigin();
			}
			return this.explodedOrigin();
		}
		return this.origin();
	}
	,
	__suspendCreation: false,
	suspendCreation: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__suspendCreation;
			this.__suspendCreation = value;
			if (oldValue && !this.__suspendCreation) {
				this.createShape();
			}
			return value;
		} else {
			return this.__suspendCreation;
		}
	}
	,
	createShape: function () {
		var $self = this;
		if (this.suspendCreation()) {
			return;
		}
		var slicePath = this.view().getSlicePath();
		var viewport = this.owner().viewport();
		if ($.ig.util.isNaN(this.startAngle()) || $.ig.util.isNaN(this.endAngle()) || $.ig.util.isNaN(this.radius()) || $.ig.util.isNaN(this.explodedRadius()) || this.index() < 0 || this.owner() == null || viewport.width() == 0 || viewport.height() == 0 || (this.origin().__x == 0 && this.origin().__y == 0) || (this.explodedOrigin().__x == 0 && this.explodedOrigin().__y == 0) || this.startAngle() == this.endAngle()) {
			return;
		}
		this.bounds(this.getSliceBounds());
		var ecc = $.ig.GeometryUtil.prototype.eccentricity(this.bounds());
		var halfHeight = this.bounds().height() / 2;
		var actualRadius = this.getActualRadius();
		var center = $.ig.RectUtil.prototype.getCenter(this.bounds());
		var startPointOuter = $.ig.Slice.prototype.ellipsePointAlternate($.ig.MathUtil.prototype.radians(this.startAngle()), ecc, center, halfHeight, 100);
		var endPointOuter = $.ig.Slice.prototype.ellipsePointAlternate($.ig.MathUtil.prototype.radians(this.endAngle()), ecc, center, halfHeight, 100);
		var endPointInner = $.ig.Slice.prototype.ellipsePointAlternate($.ig.MathUtil.prototype.radians(this.endAngle()), ecc, center, this.radius(), this.innerExtentEnd());
		var startPointInner = $.ig.Slice.prototype.ellipsePointAlternate($.ig.MathUtil.prototype.radians(this.startAngle()), ecc, center, this.radius(), this.innerExtentStart());
		var circle = Math.abs($.ig.PieChartBase.prototype.roundAngle(this.endAngle() - this.startAngle())) == 360;
		if (circle) {
			if (this.innerExtentEnd() == 0) {
				var circleGeometry = (function () {
					var $ret = new $.ig.EllipseGeometry();
					$ret.center(center);
					$ret.radiusX($self.radius());
					$ret.radiusY($self.radius());
					return $ret;
				}());
				slicePath.data(circleGeometry);
				return;
			} else if (this.innerExtentEnd() > 0) {
				var outerSize = new $.ig.Size(1, this.radius(), this.radius());
				var innerSize = new $.ig.Size(1, this.radius() * this.innerExtentEnd() / 100, this.radius() * this.innerExtentEnd() / 100);
				var leftOuterPoint = (function () {
					var $ret = new $.ig.Point(0);
					$ret.x(center.__x - $self.radius());
					$ret.y(center.__y);
					return $ret;
				}());
				var rightOuterPoint = (function () {
					var $ret = new $.ig.Point(0);
					$ret.x(center.__x + $self.radius());
					$ret.y(center.__y);
					return $ret;
				}());
				var leftInnerPoint = (function () {
					var $ret = new $.ig.Point(0);
					$ret.x(center.__x - $self.radius() * $self.innerExtentEnd() / 100);
					$ret.y(center.__y);
					return $ret;
				}());
				var rightInnerPoint = (function () {
					var $ret = new $.ig.Point(0);
					$ret.x(center.__x + $self.radius() * $self.innerExtentEnd() / 100);
					$ret.y(center.__y);
					return $ret;
				}());
				var firstOuterArc = (function () {
					var $ret = new $.ig.PathFigure();
					$ret.startPoint(leftOuterPoint);
					return $ret;
				}());
				var firstOuterArcSegments = new $.ig.PathSegmentCollection();
				var topOuterArc = (function () {
					var $ret = new $.ig.ArcSegment();
					$ret.size(outerSize);
					$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
					$ret.point(rightOuterPoint);
					return $ret;
				}());
				firstOuterArcSegments.add(topOuterArc);
				firstOuterArc.__segments = firstOuterArcSegments;
				var firstInnerArc = (function () {
					var $ret = new $.ig.PathFigure();
					$ret.startPoint(rightInnerPoint);
					return $ret;
				}());
				var firstInnerArcSegments = new $.ig.PathSegmentCollection();
				var topInnerArc = (function () {
					var $ret = new $.ig.ArcSegment();
					$ret.size(innerSize);
					$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
					$ret.point(leftInnerPoint);
					return $ret;
				}());
				firstInnerArcSegments.add(topInnerArc);
				firstInnerArc.__segments = firstInnerArcSegments;
				var secondOuterArc = (function () {
					var $ret = new $.ig.PathFigure();
					$ret.startPoint(leftOuterPoint);
					return $ret;
				}());
				var secondOuterArcSegments = new $.ig.PathSegmentCollection();
				var bottomOuterArc = (function () {
					var $ret = new $.ig.ArcSegment();
					$ret.size(outerSize);
					$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
					$ret.point(rightOuterPoint);
					return $ret;
				}());
				secondOuterArcSegments.add(bottomOuterArc);
				secondOuterArc.__segments = secondOuterArcSegments;
				var secondInnerArc = (function () {
					var $ret = new $.ig.PathFigure();
					$ret.startPoint(rightInnerPoint);
					return $ret;
				}());
				var secondInnerArcSegments = new $.ig.PathSegmentCollection();
				var bottomInnerArc = (function () {
					var $ret = new $.ig.ArcSegment();
					$ret.size(innerSize);
					$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
					$ret.point(leftInnerPoint);
					return $ret;
				}());
				secondInnerArcSegments.add(bottomInnerArc);
				secondInnerArc.__segments = secondInnerArcSegments;
				var figureCollection = (function () {
					var $ret = new $.ig.PathFigureCollection();
					$ret.add(firstOuterArc);
					$ret.add(firstInnerArc);
					$ret.add(secondOuterArc);
					$ret.add(secondInnerArc);
					return $ret;
				}());
				var pg = (function () {
					var $ret = new $.ig.PathGeometry();
					$ret.figures(figureCollection);
					return $ret;
				}());
				var gg = new $.ig.GeometryGroup();
				gg.children().add(pg);
				slicePath.data(gg);
				return;
			} else {
				var group = (function () {
					var $ret = new $.ig.GeometryGroup();
					$ret.fillRule($.ig.FillRule.prototype.evenOdd);
					return $ret;
				}());
				group.children().add((function () {
					var $ret = new $.ig.EllipseGeometry();
					$ret.center(center);
					$ret.radiusX($self.radius());
					$ret.radiusY($self.radius());
					return $ret;
				}()));
				group.children().add((function () {
					var $ret = new $.ig.EllipseGeometry();
					$ret.center(center);
					$ret.radiusX($self.radius() * $self.innerExtentEnd() / 100);
					$ret.radiusY($self.radius() * $self.innerExtentEnd() / 100);
					return $ret;
				}()));
				slicePath.data(group);
				return;
			}
		}
		var geometry = new $.ig.PathGeometry();
		slicePath.data(geometry);
		var figure = (function () {
			var $ret = new $.ig.PathFigure();
			$ret.isClosed(true);
			return $ret;
		}());
		geometry.figures(new $.ig.PathFigureCollection());
		geometry.figures().add(figure);
		figure.__startPoint = startPointOuter;
		var largeArc = Math.abs(this.endAngle() - this.startAngle()) > 180;
		var arcOuter = new $.ig.ArcSegment();
		arcOuter.point(endPointOuter);
		arcOuter.size(new $.ig.Size(1, this.bounds().width() / 2, this.bounds().height() / 2));
		arcOuter.isLargeArc(largeArc);
		if (this.endAngle() > this.startAngle()) {
			arcOuter.sweepDirection($.ig.SweepDirection.prototype.clockwise);
		} else {
			arcOuter.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
		}
		figure.__segments = new $.ig.PathSegmentCollection();
		figure.__segments.add(arcOuter);
		var connectEnd = new $.ig.LineSegment(1);
		connectEnd.point(endPointInner);
		figure.__segments.add(connectEnd);
		var lineLenght = Math.sqrt(Math.pow(endPointInner.__x - endPointOuter.__x, 2) + Math.pow(endPointInner.__y - endPointOuter.__y, 2));
		var innerRadius = actualRadius - lineLenght;
		var arcInner = new $.ig.ArcSegment();
		arcInner.point(startPointInner);
		if (innerRadius < 0) {
			innerRadius = 0.1;
		}
		arcInner.size(new $.ig.Size(1, innerRadius, innerRadius));
		arcInner.isLargeArc(arcOuter.isLargeArc());
		arcInner.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
		figure.__segments.add(arcInner);
		var bounds = this.getBounds(slicePath, startPointOuter, endPointOuter, this.startAngle(), this.endAngle(), center, actualRadius);
		var slicesWithinBounds = true;
		if (slicesWithinBounds && this.bounds().height() > 0 && this.bounds().width() > 0 && !viewport.containsRect(bounds)) {
			var sliceBounds = bounds;
			var chartBounds = new $.ig.Rect(0, 0, 0, viewport.width(), viewport.height());
			chartBounds.intersect(sliceBounds);
			var midAngle = $.ig.GeometryUtil.prototype.simplifyAngle((this.startAngle() + this.endAngle()) / 2);
			var midAngleRad = midAngle / 180 * Math.PI;
			var dx = Math.abs((sliceBounds.height() - chartBounds.height()) / Math.sin(midAngleRad));
			var dy = Math.abs((sliceBounds.width() - chartBounds.width()) / Math.cos(midAngleRad));
			if ($.ig.util.isNaN(dx) || Number.isInfinity(dx)) {
				dx = 0;
			}
			if ($.ig.util.isNaN(dy) || Number.isInfinity(dy)) {
				dy = 0;
			}
			var distance = Math.max(dx, dy);
			var explodedCenterNew = $.ig.GeometryUtil.prototype.findCenter(this.owner().viewport().width(), this.owner().viewport().height(), true, midAngle, this.radius() * this.owner().actualExplodedRadius() - distance);
			this.view().positionSlice(explodedCenterNew.__x - this.explodedOrigin().__x, explodedCenterNew.__y - this.explodedOrigin().__y);
			this.hasCorrecttedBounds(true);
			this.correctedExplodedOrigin(explodedCenterNew);
			this.correctedExplodedBounds(new $.ig.Rect(0, sliceBounds.x() - (explodedCenterNew.__x - this.explodedOrigin().__x), sliceBounds.y() - (explodedCenterNew.__y - this.explodedOrigin().__y), sliceBounds.width(), sliceBounds.height()));
		} else {
			this.hasCorrecttedBounds(false);
			this.view().resetSlicePosition();
		}
	}
	,
	containsPoint: function (p) {
		var viewport = this.owner().viewport();
		if (this.__visibility == $.ig.Visibility.prototype.collapsed) {
			return false;
		}
		if ($.ig.util.isNaN(this.startAngle()) || $.ig.util.isNaN(this.endAngle()) || $.ig.util.isNaN(this.radius()) || $.ig.util.isNaN(this.explodedRadius()) || this.index() < 0 || this.owner() == null || viewport.width() == 0 || viewport.height() == 0 || (this.origin().__x == 0 && this.origin().__y == 0) || (this.explodedOrigin().__x == 0 && this.explodedOrigin().__y == 0)) {
			return false;
		}
		var actualRadius = this.getActualRadius();
		var startRadius = ((this.innerExtentEnd() / 100) * actualRadius);
		var endRadius = actualRadius;
		var center = this.origin();
		if (this.isExploded() && this.owner().allowSliceExplosion() && this.owner().totalSliceCount() > 1) {
			center = this.explodedOrigin();
			if (this.hasCorrecttedBounds()) {
				center = this.correctedExplodedOrigin();
			}
		}
		var startRadiusSquared = startRadius * startRadius;
		var endRadiusSquared = endRadius * endRadius;
		var dist = Math.pow(p.__x - center.__x, 2) + Math.pow(p.__y - center.__y, 2);
		if (dist < startRadiusSquared || dist > endRadiusSquared) {
			return false;
		}
		var angle = Math.atan2(p.__y - center.__y, p.__x - center.__x);
		angle = angle * 180 / Math.PI;
		return this.containsAngle(angle);
	}
	,
	containsAngle: function (angle) {
		angle = $.ig.GeometryUtil.prototype.simplifyAngle(angle);
		var minAngle = Math.min(this.startAngle(), this.endAngle());
		var maxAngle = Math.max(this.startAngle(), this.endAngle());
		return (angle > minAngle && angle < maxAngle) || (angle - 360 > minAngle && angle - 360 < maxAngle) || (angle + 360 > minAngle && angle + 360 < maxAngle);
	}
	,
	getBounds: function (slicePath, outerStart, outerEnd, startAngle, endAngle, center, radius) {
		var points = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(outerStart);
		points.add(outerEnd);
		points.add(center);
		startAngle = $.ig.GeometryUtil.prototype.simplifyAngle(startAngle);
		endAngle = $.ig.GeometryUtil.prototype.simplifyAngle(endAngle);
		if ((0 > startAngle && 0 < endAngle) || (360 > startAngle && 360 < endAngle) || (endAngle < startAngle)) {
			points.add($.ig.GeometryUtil.prototype.findRadialPoint(center, 0, radius));
		}
		if (90 > startAngle && 90 < endAngle) {
			points.add($.ig.GeometryUtil.prototype.findRadialPoint(center, 90, radius));
		}
		if (180 > startAngle && 180 < endAngle) {
			points.add($.ig.GeometryUtil.prototype.findRadialPoint(center, 180, radius));
		}
		if (270 > startAngle && 270 < endAngle) {
			points.add($.ig.GeometryUtil.prototype.findRadialPoint(center, 270, radius));
		}
		var minX = 1.7976931348623157E+308;
		var minY = 1.7976931348623157E+308;
		var maxX = -1.7976931348623157E+308;
		var maxY = -1.7976931348623157E+308;
		var en = points.getEnumerator();
		while (en.moveNext()) {
			var point = en.current();
			minX = Math.min(minX, point.__x);
			minY = Math.min(minY, point.__y);
			maxX = Math.max(maxX, point.__x);
			maxY = Math.max(maxY, point.__y);
		}
		return new $.ig.Rect(0, minX, minY, maxX - minX, maxY - minY);
	}
	,
	exportVisualData: function () {
		var slice = new $.ig.PieSliceVisualData();
		slice.appearance(new $.ig.PrimitiveAppearanceData());
		slice.labelAppearance(new $.ig.LabelAppearanceData());
		slice.leaderLineAppearance(new $.ig.PrimitiveAppearanceData());
		slice.endAngle(this.endAngle());
		slice.explodedOrigin(this.explodedOrigin());
		slice.explodedRadius(this.explodedRadius());
		slice.index(this.index());
		slice.innerExtentEnd(this.innerExtentEnd());
		slice.innerExtentStart(this.innerExtentStart());
		slice.isExploded(this.isExploded());
		slice.isOthersSlice(this.isOthersSlice());
		slice.isSelected(this.isSelected());
		if (this.label() != null) {
			slice.labelAppearance().angle(this.label().angle());
			slice.labelBounds(this.label().bounds());
			slice.labelAngle(this.label().angle());
			if ($.ig.util.cast($.ig.TextBlock.prototype.$type, this.label().label()) !== null) {
				var label = $.ig.util.cast($.ig.TextBlock.prototype.$type, this.label().label());
				slice.label(label.text());
				var font = null;
				slice.labelAppearance().labelBrush($.ig.AppearanceHelper.prototype.fromBrush(label.fill()));
				slice.labelAppearance().text(label.text());
				if (this.owner() != null) {
					font = this.owner().getFontInfo();
					if (font == null) {
						font = this.owner().view().font();
					}
				}
				slice.labelAppearance($.ig.AppearanceHelper.prototype.fromTextElement(label, font));
				slice.labelAppearance().visibility(this.label().__visibility == $.ig.Visibility.prototype.visible);
			} else {
				slice.label(this.label().label().toString());
			}
			if (this.label().leaderLine() != null) {
				slice.leaderLineAppearance().fill($.ig.AppearanceHelper.prototype.fromBrush(this.label().leaderLine().__fill));
				slice.leaderLineAppearance().opacity(this.label().leaderLine().__opacity);
				slice.leaderLineAppearance().stroke($.ig.AppearanceHelper.prototype.fromBrush(this.label().leaderLine().__stroke));
				slice.leaderLineAppearance().strokeThickness(this.label().leaderLine().strokeThickness());
				slice.leaderLineAppearance().visibility(this.label().leaderLine().__visibility);
				slice.leaderLinePath(new $.ig.PathVisualData(2, "leaderLine", this.label().leaderLine()));
			} else if (this.label().leaderLinePath() != null) {
				slice.leaderLineAppearance().fill($.ig.AppearanceHelper.prototype.fromBrush(this.label().leaderLinePath().__fill));
				slice.leaderLineAppearance().opacity(this.label().leaderLinePath().__opacity);
				slice.leaderLineAppearance().stroke($.ig.AppearanceHelper.prototype.fromBrush(this.label().leaderLinePath().__stroke));
				slice.leaderLineAppearance().strokeThickness(this.label().leaderLinePath().strokeThickness());
				slice.leaderLineAppearance().visibility(this.label().leaderLinePath().__visibility);
				slice.leaderLinePath(new $.ig.PathVisualData(1, "leaderLine", this.label().leaderLinePath()));
			}
		}
		if (this.view() != null) {
			var slicePath = this.view().getSlicePath();
			slice.slicePath(new $.ig.PathVisualData(1, "slicePath", slicePath));
			slice.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(slicePath.__fill));
			slice.appearance().opacity(slicePath.__opacity);
			slice.appearance().stroke($.ig.AppearanceHelper.prototype.fromBrush(slicePath.__stroke));
			slice.appearance().strokeThickness(slicePath.strokeThickness());
			slice.appearance().visibility(slicePath.__visibility);
		}
		return slice;
	}
	,
	ellipsePointAlternate: function (theta, eccentricity, center, halfHeight, extent) {
		if (halfHeight == 0) {
			return center;
		}
		var cos = Math.cos(theta);
		var sin = Math.sin(theta);
		var r = Math.sqrt(halfHeight * halfHeight / (1 - (eccentricity * Math.pow(cos, 2))));
		r *= (extent / 100);
		return { __x: r * cos + center.__x, __y: r * sin + center.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	_background: null,
	background: function (value) {
		if (arguments.length === 1) {
			this._background = value;
			return value;
		} else {
			return this._background;
		}
	}
	,
	_borderBrush: null,
	borderBrush: function (value) {
		if (arguments.length === 1) {
			this._borderBrush = value;
			return value;
		} else {
			return this._borderBrush;
		}
	}
	,
	$type: new $.ig.Type('Slice', $.ig.ContentControl.prototype.$type)
}, true);

$.ig.util.defType('PieLabel', 'Control', {
	init: function () {
		$.ig.Control.prototype.init.call(this);
	},
	_slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this._slice = value;
			return value;
		} else {
			return this._slice;
		}
	}
	,
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
	_label: null,
	label: function (value) {
		if (arguments.length === 1) {
			this._label = value;
			return value;
		} else {
			return this._label;
		}
	}
	,
	_leaderLine: null,
	leaderLine: function (value) {
		if (arguments.length === 1) {
			this._leaderLine = value;
			return value;
		} else {
			return this._leaderLine;
		}
	}
	,
	_leaderLinePath: null,
	leaderLinePath: function (value) {
		if (arguments.length === 1) {
			this._leaderLinePath = value;
			return value;
		} else {
			return this._leaderLinePath;
		}
	}
	,
	_actualLabelPosition: 0,
	actualLabelPosition: function (value) {
		if (arguments.length === 1) {
			this._actualLabelPosition = value;
			return value;
		} else {
			return this._actualLabelPosition;
		}
	}
	,
	createContent: function (view) {
		if (view.model().labelsPosition() == $.ig.LabelsPosition.prototype.outsideEnd) {
			this.bounds(view.getLabelBounds(this));
			var textBlock = $.ig.util.cast($.ig.TextBlock.prototype.$type, this.label());
			var sampleText = textBlock.text();
			view.model().renderSlices();
			var origin = this.slice().isExploded() ? this.slice().explodedOrigin() : this.slice().origin();
			var bounds = $.ig.GeometryUtil.prototype.findRadialPoint(origin, this.angle(), this.slice().radius() + view.model().labelExtent());
			var containerWidth = view.viewport().width();
			if (view.getDesiredWidth(textBlock) + bounds.__x > containerWidth && bounds.__x < containerWidth) {
				while (sampleText.length > 0 && view.getDesiredWidth(textBlock) + bounds.__x > containerWidth) {
					sampleText = sampleText.substr(0, sampleText.length - 1);
					textBlock.text(sampleText);
				}
				if (sampleText.length > 3) {
					sampleText = sampleText.substr(0, sampleText.length - 3);
					sampleText = sampleText + "...";
				} else {
					if (view.getDesiredWidth("...") < this.bounds().width()) {
						sampleText = "...";
					} else {
						sampleText = String.empty();
					}
				}
			}
			var newLabel = new $.ig.TextBlock();
			newLabel.text(sampleText);
			this.label(newLabel);
		}
	}
	,
	updateCascadingLeaderLineStroke: function () {
	}
	,
	updateLeaderLine: function () {
		this.leaderLine().__visibility = this.slice().owner().leaderLineVisibility() != $.ig.Visibility.prototype.visible ? this.slice().owner().leaderLineVisibility() : this.slice().label().__visibility;
		var endPoint;
		var margin = this.slice().owner().leaderLineMargin();
		var startPoint = $.ig.GeometryUtil.prototype.findRadialPoint(this.slice().getSliceOrigin(), this.angle(), this.slice().radius());
		this.leaderLine().x1(startPoint.__x);
		this.leaderLine().y1(startPoint.__y);
		if (this.angle() < 90 || this.angle() >= 270) {
			endPoint = { __x: this.bounds().left(), __y: (this.bounds().top() + this.bounds().bottom()) / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			endPoint = { __x: this.bounds().right(), __y: (this.bounds().top() + this.bounds().bottom()) / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (margin > 0) {
			var lineLength = $.ig.GeometryUtil.prototype.getSegmentLength(startPoint, endPoint);
			if (margin < lineLength) {
				endPoint = $.ig.GeometryUtil.prototype.pointAtDistance(endPoint, startPoint, margin);
			} else {
				endPoint = startPoint;
			}
		}
		this.leaderLine().x2(endPoint.__x);
		this.leaderLine().y2(endPoint.__y);
	}
	,
	updateLeaderLinePath: function () {
		this.leaderLinePath().__visibility = this.slice().owner().leaderLineVisibility() != $.ig.Visibility.prototype.visible ? this.slice().owner().leaderLineVisibility() : this.slice().label().__visibility;
		var sliceOrigin = this.slice().getSliceOrigin();
		var figure = (this.leaderLinePath().data()).figures().__inner[0];
		var segment = (figure.__segments.__inner[0]);
		var margin = this.slice().owner().leaderLineMargin();
		var endPointY = (this.bounds().top() + this.bounds().bottom()) / 2;
		var startPoint = $.ig.GeometryUtil.prototype.findRadialPoint(sliceOrigin, this.angle(), this.slice().radius());
		var cp1 = $.ig.GeometryUtil.prototype.findRadialPoint(sliceOrigin, this.angle(), this.slice().radius() + this.slice().owner().labelExtent() / 2);
		var cp2;
		var actualEndPoint;
		var endPoint;
		var cpY = endPointY;
		if (this.angle() < 90 && this.angle() >= 0) {
			cp2 = { __x: this.bounds().left() - $.ig.PieChartBase.prototype.leaderLinePathControlPointOffset, __y: cpY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			endPoint = { __x: this.bounds().left(), __y: endPointY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else if (this.angle() < 180 && this.angle() >= 90) {
			cp2 = { __x: this.bounds().right() + $.ig.PieChartBase.prototype.leaderLinePathControlPointOffset, __y: cpY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			endPoint = { __x: this.bounds().right(), __y: endPointY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else if (this.angle() < 270 && this.angle() >= 180) {
			cp2 = { __x: this.bounds().right() + $.ig.PieChartBase.prototype.leaderLinePathControlPointOffset, __y: cpY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			endPoint = { __x: this.bounds().right(), __y: endPointY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			cp2 = { __x: this.bounds().left() - $.ig.PieChartBase.prototype.leaderLinePathControlPointOffset, __y: cpY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			endPoint = { __x: this.bounds().left(), __y: endPointY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (margin > 0) {
			var lineLength = $.ig.GeometryUtil.prototype.getSegmentLength(startPoint, endPoint);
			if (margin < lineLength) {
				actualEndPoint = $.ig.GeometryUtil.prototype.pointAtDistance(endPoint, startPoint, margin);
				var deltaX1 = cp1.__x - startPoint.__x;
				var deltaY1 = cp1.__y - startPoint.__y;
				cp1 = $.ig.GeometryUtil.prototype.pointAtDistance(cp1, startPoint, margin);
				var deltaX2 = cp1.__x - startPoint.__x;
				var deltaY2 = cp1.__y - startPoint.__y;
				if ((deltaX1 > 0 && deltaX2 < 0) || (deltaX1 < 0 && deltaX2 > 0) || (deltaY1 > 0 && deltaY2 < 0) || (deltaY1 < 0 && deltaY2 > 0)) {
					cp1 = startPoint;
				}
				cp2 = $.ig.GeometryUtil.prototype.pointAtDistance(cp2, startPoint, margin);
				var scaleFactor = $.ig.GeometryUtil.prototype.getSegmentLength(startPoint, actualEndPoint) / $.ig.GeometryUtil.prototype.getSegmentLength(startPoint, endPoint);
				cp2.__x = startPoint.__x + (cp2.__x - startPoint.__x) * scaleFactor;
				cp2.__y = startPoint.__y + (cp2.__y - startPoint.__y) * scaleFactor;
			} else {
				this.leaderLinePath().__visibility = $.ig.Visibility.prototype.collapsed;
				return;
			}
		} else {
			actualEndPoint = endPoint;
		}
		if (this.angle() < 180 && this.angle() >= 0) {
			if (cp1.__y > cp2.__y) {
				cp1.__y = startPoint.__y;
			}
		} else {
			if (cp1.__y < cp2.__y) {
				cp1.__y = startPoint.__y;
			}
		}
		figure.__startPoint = startPoint;
		if (this.slice().owner().leaderLineType() == $.ig.LeaderLineType.prototype.arc) {
			segment.point1(startPoint);
		} else {
			segment.point1(cp1);
		}
		segment.point2(cp2);
		segment.point3(actualEndPoint);
	}
	,
	$type: new $.ig.Type('PieLabel', $.ig.Control.prototype.$type)
}, true);

$.ig.util.defType('IndexCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.Number.prototype.$type, 0);
	},
	insertItem: function (index, item) {
		if (!this.contains(item)) {
			$.ig.ObservableCollection$1.prototype.insertItem.call(this, index, item);
		}
	}
	,
	setItem: function (index, item) {
		if (this.contains(item)) {
			this.remove(item);
			this.insertItem(index, item);
		} else {
			$.ig.ObservableCollection$1.prototype.setItem.call(this, index, item);
		}
	}
	,
	$type: new $.ig.Type('IndexCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.Number.prototype.$type))
}, true);

$.ig.util.defType('PropertyUpdatedEventArgs', 'EventArgs', {
	init: function (propertyName, oldValue, newValue) {
		$.ig.EventArgs.prototype.init.call(this);
		this.propertyName(propertyName);
		this.oldValue(oldValue);
		this.newValue(newValue);
	},
	_propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this._propertyName = value;
			return value;
		} else {
			return this._propertyName;
		}
	}
	,
	_oldValue: null,
	oldValue: function (value) {
		if (arguments.length === 1) {
			this._oldValue = value;
			return value;
		} else {
			return this._oldValue;
		}
	}
	,
	_newValue: null,
	newValue: function (value) {
		if (arguments.length === 1) {
			this._newValue = value;
			return value;
		} else {
			return this._newValue;
		}
	}
	,
	$type: new $.ig.Type('PropertyUpdatedEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('SliceClickEventArgs', 'EventArgs', {
	init: function (slice, originalEvent) {
		$.ig.EventArgs.prototype.init.call(this);
		this.slice(slice);
		this.originalEvent(originalEvent);
		if (slice == null) {
			return;
		}
		this.isSelected(slice.isSelected());
		this.isExploded(slice.isExploded());
	},
	_slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this._slice = value;
			return value;
		} else {
			return this._slice;
		}
	}
	,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this.__isSelected = value;
			this.slice().isSelected(value);
			return value;
		} else {
			return this.__isSelected;
		}
	}
	,
	__isSelected: false,
	isExploded: function (value) {
		if (arguments.length === 1) {
			this.__isExploded = value;
			this.slice().isExploded(value);
			return value;
		} else {
			return this.__isExploded;
		}
	}
	,
	__isExploded: false,
	isOthersSlice: function () {
		return this.slice().isOthersSlice();
	}
	,
	dataContext: function () {
		if (this.slice() != null) {
			return this.slice().dataContext();
		}
		return null;
	}
	,
	_originalEvent: null,
	originalEvent: function (value) {
		if (arguments.length === 1) {
			this._originalEvent = value;
			return value;
		} else {
			return this._originalEvent;
		}
	}
	,
	$type: new $.ig.Type('SliceClickEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('ItemLegend', 'LegendBase', {
	createView: function () {
		return new $.ig.ItemLegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.itemView(view);
	}
	,
	_itemView: null,
	itemView: function (value) {
		if (arguments.length === 1) {
			this._itemView = value;
			return value;
		} else {
			return this._itemView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.ItemLegend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.itemView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.itemView().addItemVisual(item1);
				}
			}
		});
	},
	addChildInOrder: function (legendItem, series) {
		if (!this.view().ready()) {
			return;
		}
		this.renderLegend(series);
	}
	,
	createLegendItems: function (legendItems, itemsHost) {
		this.clearLegendItems(itemsHost);
		if (itemsHost == null || legendItems == null || legendItems.count() == 0) {
			return;
		}
		var en = legendItems.getEnumerator();
		while (en.moveNext()) {
			var currentLegendItem = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, currentLegendItem);
			if (contentControl != null && contentControl.content() != null) {
				var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				if (context != null && !this.containsContext(context)) {
					this.children().add(currentLegendItem);
					var info = new $.ig.LegendItemInfo();
					info.dataContext(context);
					info.legendItem(currentLegendItem);
					info.series(itemsHost);
					if ($.ig.util.cast($.ig.PieSliceDataContext.prototype.$type, context) !== null) {
						info.text((context).legendLabel());
					} else {
						info.text(context.itemLabel());
					}
				}
			}
		}
	}
	,
	createLegendItemsInsert: function (legendItems, itemsHost) {
		var insertIndex = this.clearLegendItemsAndReturnInsertIndex(itemsHost);
		if (itemsHost == null || legendItems == null || legendItems.count() == 0) {
			return;
		}
		var en = legendItems.getEnumerator();
		while (en.moveNext()) {
			var currentLegendItem = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, currentLegendItem);
			if (contentControl != null && contentControl.content() != null) {
				var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				if (context != null && !this.containsContext(context)) {
					this.children().insert(insertIndex, currentLegendItem);
					insertIndex++;
					var info = new $.ig.LegendItemInfo();
					info.dataContext(context);
					info.legendItem(currentLegendItem);
					info.series(itemsHost);
					info.text(context.itemLabel());
				}
			}
		}
	}
	,
	renderLegend: function (itemsHost) {
		this.clearLegendItems(itemsHost);
		var bubbleSeries = $.ig.util.cast($.ig.BubbleSeries.prototype.$type, itemsHost);
		if (bubbleSeries != null && bubbleSeries.labelColumn() != null && bubbleSeries.legendItems() != null && bubbleSeries.legendItems().count() > 0) {
			var en = bubbleSeries.legendItems().getEnumerator();
			while (en.moveNext()) {
				var legendItem = en.current();
				var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
				if (contentControl != null && contentControl.content() != null) {
					var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
					if (context != null && !this.containsContext(context)) {
						this.children().add(legendItem);
						var info = new $.ig.LegendItemInfo();
						info.dataContext(context);
						info.legendItem(legendItem);
						info.series(itemsHost);
						info.text(context.itemLabel());
					}
				}
			}
		}
	}
	,
	clearLegendItems: function (itemsHost) {
		if (itemsHost == null || this.children() == null || this.children().count() == 0) {
			return;
		}
		var legendItems = new $.ig.ObservableCollection$1($.ig.UIElement.prototype.$type, 0);
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var existingLegendItem = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, existingLegendItem);
			if (contentControl != null && contentControl.content() != null) {
				var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				if (context != null && context.series() == itemsHost) {
					legendItems.add(existingLegendItem);
				}
			}
		}
		var en1 = legendItems.getEnumerator();
		while (en1.moveNext()) {
			var legendItem = en1.current();
			this.children().remove(legendItem);
		}
	}
	,
	clearLegendItemsAndReturnInsertIndex: function (itemsHost) {
		if (itemsHost == null || this.children() == null || this.children().count() == 0) {
			return 0;
		}
		var legendItems = new $.ig.ObservableCollection$1($.ig.UIElement.prototype.$type, 0);
		var insertIndex = -1;
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var existingLegendItem = en.current();
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, existingLegendItem);
			if (contentControl != null && contentControl.content() != null) {
				var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				if (context != null && context.series() == itemsHost) {
					if (insertIndex == -1) {
						insertIndex = index;
					}
					legendItems.add(existingLegendItem);
				}
			}
			index++;
		}
		var en1 = legendItems.getEnumerator();
		while (en1.moveNext()) {
			var legendItem = en1.current();
			this.children().remove(legendItem);
		}
		if (insertIndex == -1) {
			return this.children().count();
		}
		return insertIndex;
	}
	,
	containsContext: function (dataContext) {
		return this.itemView().containsContext(dataContext);
	}
	,
	_fillColumn: null,
	fillColumn: function (value) {
		if (arguments.length === 1) {
			this._fillColumn = value;
			return value;
		} else {
			return this._fillColumn;
		}
	}
	,
	$type: new $.ig.Type('ItemLegend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('LegendItemInfo', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_text: null,
	text: function (value) {
		if (arguments.length === 1) {
			this._text = value;
			return value;
		} else {
			return this._text;
		}
	}
	,
	_legendItem: null,
	legendItem: function (value) {
		if (arguments.length === 1) {
			this._legendItem = value;
			return value;
		} else {
			return this._legendItem;
		}
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
	_dataContext: null,
	dataContext: function (value) {
		if (arguments.length === 1) {
			this._dataContext = value;
			return value;
		} else {
			return this._dataContext;
		}
	}
	,
	$type: new $.ig.Type('LegendItemInfo', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PieChartVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.slices(new $.ig.PieSliceVisualDataList());
		this.others(new $.ig.List$1($.ig.Object.prototype.$type, 0));
		this.dipScalingRatio(1);
	},
	_slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this._slices = value;
			return value;
		} else {
			return this._slices;
		}
	}
	,
	_othersSlice: null,
	othersSlice: function (value) {
		if (arguments.length === 1) {
			this._othersSlice = value;
			return value;
		} else {
			return this._othersSlice;
		}
	}
	,
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
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
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	_others: null,
	others: function (value) {
		if (arguments.length === 1) {
			this._others = value;
			return value;
		} else {
			return this._others;
		}
	}
	,
	_radiusFactor: 0,
	radiusFactor: function (value) {
		if (arguments.length === 1) {
			this._radiusFactor = value;
			return value;
		} else {
			return this._radiusFactor;
		}
	}
	,
	_leaderLineMargin: 0,
	leaderLineMargin: function (value) {
		if (arguments.length === 1) {
			this._leaderLineMargin = value;
			return value;
		} else {
			return this._leaderLineMargin;
		}
	}
	,
	_isViewportScaled: false,
	isViewportScaled: function (value) {
		if (arguments.length === 1) {
			this._isViewportScaled = value;
			return value;
		} else {
			return this._isViewportScaled;
		}
	}
	,
	_dipScalingRatio: 0,
	dipScalingRatio: function (value) {
		if (arguments.length === 1) {
			this._dipScalingRatio = value;
			return value;
		} else {
			return this._dipScalingRatio;
		}
	}
	,
	scaleByViewport: function () {
		if (this.isViewportScaled()) {
			return;
		}
		this.isViewportScaled(true);
		for (var i = 0; i < this.slices().count(); i++) {
			this.slices().__inner[i].scaleByViewport(this.viewport());
		}
		if (this.othersSlice() != null) {
			this.othersSlice().scaleByViewport(this.viewport());
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("name: " + this.name() + ",");
		sb.appendLine1("leaderLineMargin: " + this.leaderLineMargin() + ",");
		sb.appendLine1("radiusFactor: " + this.radiusFactor() + ",");
		sb.appendLine1("width: " + this.width() + ",");
		sb.appendLine1("height: " + this.height() + ",");
		sb.appendLine1("isViewportScaled: " + (this.isViewportScaled() ? "true" : "false") + ", ");
		sb.appendLine1("slices: [");
		for (var i = 0; i < this.slices().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.slices().__inner[i].serialize());
		}
		sb.appendLine1("],");
		sb.appendLine1("viewport: { left: " + this.viewport().left() + ", top: " + this.viewport().top() + ", width: " + this.viewport().width() + ", height: " + this.viewport().height() + "}");
		sb.append5(", dipScalingRatio: " + this.dipScalingRatio().toString());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('PieChartVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FunnelChartVisualData', 'Object', {
	init: function () {
		this._backgroundColor = new $.ig.Color();
		this._foregroundColor = new $.ig.Color();
		this._borderBrush = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
		this.slices(new $.ig.FunnelSliceVisualDataList());
	},
	_slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this._slices = value;
			return value;
		} else {
			return this._slices;
		}
	}
	,
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
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
	_backgroundColor: null,
	backgroundColor: function (value) {
		if (arguments.length === 1) {
			this._backgroundColor = value;
			return value;
		} else {
			return this._backgroundColor;
		}
	}
	,
	_foregroundColor: null,
	foregroundColor: function (value) {
		if (arguments.length === 1) {
			this._foregroundColor = value;
			return value;
		} else {
			return this._foregroundColor;
		}
	}
	,
	_opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this._opacity = value;
			return value;
		} else {
			return this._opacity;
		}
	}
	,
	_borderBrush: null,
	borderBrush: function (value) {
		if (arguments.length === 1) {
			this._borderBrush = value;
			return value;
		} else {
			return this._borderBrush;
		}
	}
	,
	scaleByViewport: function () {
		var en = this.slices().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			slice.scaleByViewport(this.viewport());
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		if (this.name() != null) {
			sb.appendLine1("name: \"" + this.name() + "\", ");
		} else {
			sb.appendLine1("name: \"\", ");
		}
		if (this.slices() != null) {
			sb.appendLine1("slices: [");
			for (var i = 0; i < this.slices().count(); i++) {
				var slice = this.slices().__inner[i].serialize();
				sb.append5(slice);
				if (i < this.slices().count() - 1) {
					sb.appendLine1(", ");
				}
			}
			sb.appendLine1("]");
		}
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('FunnelChartVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PieSliceVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.labelBounds($.ig.Rect.prototype.empty());
	},
	_startAngle: 0,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this._startAngle = value;
			return value;
		} else {
			return this._startAngle;
		}
	}
	,
	_endAngle: 0,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this._endAngle = value;
			return value;
		} else {
			return this._endAngle;
		}
	}
	,
	_origin: null,
	origin: function (value) {
		if (arguments.length === 1) {
			this._origin = value;
			return value;
		} else {
			return this._origin;
		}
	}
	,
	_explodedOrigin: null,
	explodedOrigin: function (value) {
		if (arguments.length === 1) {
			this._explodedOrigin = value;
			return value;
		} else {
			return this._explodedOrigin;
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
	_explodedRadius: 0,
	explodedRadius: function (value) {
		if (arguments.length === 1) {
			this._explodedRadius = value;
			return value;
		} else {
			return this._explodedRadius;
		}
	}
	,
	_innerExtentStart: 0,
	innerExtentStart: function (value) {
		if (arguments.length === 1) {
			this._innerExtentStart = value;
			return value;
		} else {
			return this._innerExtentStart;
		}
	}
	,
	_innerExtentEnd: 0,
	innerExtentEnd: function (value) {
		if (arguments.length === 1) {
			this._innerExtentEnd = value;
			return value;
		} else {
			return this._innerExtentEnd;
		}
	}
	,
	_isSelected: false,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this._isSelected = value;
			return value;
		} else {
			return this._isSelected;
		}
	}
	,
	_isExploded: false,
	isExploded: function (value) {
		if (arguments.length === 1) {
			this._isExploded = value;
			return value;
		} else {
			return this._isExploded;
		}
	}
	,
	_isOthersSlice: false,
	isOthersSlice: function (value) {
		if (arguments.length === 1) {
			this._isOthersSlice = value;
			return value;
		} else {
			return this._isOthersSlice;
		}
	}
	,
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
	_label: null,
	label: function (value) {
		if (arguments.length === 1) {
			this._label = value;
			return value;
		} else {
			return this._label;
		}
	}
	,
	_labelAngle: 0,
	labelAngle: function (value) {
		if (arguments.length === 1) {
			this._labelAngle = value;
			return value;
		} else {
			return this._labelAngle;
		}
	}
	,
	_labelBounds: null,
	labelBounds: function (value) {
		if (arguments.length === 1) {
			this._labelBounds = value;
			return value;
		} else {
			return this._labelBounds;
		}
	}
	,
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	_labelAppearance: null,
	labelAppearance: function (value) {
		if (arguments.length === 1) {
			this._labelAppearance = value;
			return value;
		} else {
			return this._labelAppearance;
		}
	}
	,
	_leaderLineAppearance: null,
	leaderLineAppearance: function (value) {
		if (arguments.length === 1) {
			this._leaderLineAppearance = value;
			return value;
		} else {
			return this._leaderLineAppearance;
		}
	}
	,
	_slicePath: null,
	slicePath: function (value) {
		if (arguments.length === 1) {
			this._slicePath = value;
			return value;
		} else {
			return this._slicePath;
		}
	}
	,
	_leaderLinePath: null,
	leaderLinePath: function (value) {
		if (arguments.length === 1) {
			this._leaderLinePath = value;
			return value;
		} else {
			return this._leaderLinePath;
		}
	}
	,
	scaleByViewport: function (viewport) {
		if (this.slicePath() != null) {
			this.slicePath().scaleByViewport(viewport);
		}
		if (this.leaderLinePath() != null) {
			this.leaderLinePath().scaleByViewport(viewport);
		}
		if (!this.labelBounds().isEmpty()) {
			var left = (this.labelBounds().left() - viewport.left()) / viewport.width();
			var top = (this.labelBounds().top() - viewport.top()) / viewport.height();
			var right = (this.labelBounds().right() - viewport.left()) / viewport.width();
			var bottom = (this.labelBounds().bottom() - viewport.top()) / viewport.height();
			this.labelBounds(new $.ig.Rect(0, left, top, right - left, bottom - top));
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		if (this.label() != null) {
			sb.appendLine1("label: \"" + this.label() + "\", ");
		}
		if (this.appearance() != null) {
			sb.appendLine1("appearance: " + this.appearance().serialize() + ", ");
		}
		if (this.labelAppearance() != null) {
			sb.appendLine1("labelAppearance: " + this.labelAppearance().serialize() + ", ");
		}
		if (this.leaderLineAppearance() != null) {
			sb.appendLine1("leaderLineAppearance: " + this.leaderLineAppearance().serialize() + ", ");
		}
		if (this.slicePath() != null) {
			sb.appendLine1("slicePath: " + this.slicePath().serialize() + ", ");
		}
		if (this.leaderLinePath() != null) {
			var serializedLine = this.leaderLinePath().serialize();
			if (serializedLine != null) {
				sb.appendLine1("leaderLinePath: " + serializedLine + ", ");
			}
		}
		sb.appendLine1("labelAngle: " + this.labelAngle() + ", ");
		sb.appendLine1("labelBounds: { left: " + this.labelBounds().left() + ", top: " + this.labelBounds().top() + ", width: " + this.labelBounds().width() + ", height: " + this.labelBounds().height() + "}");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('PieSliceVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PieSliceVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.PieSliceVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('PieSliceVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.PieSliceVisualData.prototype.$type))
}, true);

$.ig.util.defType('FunnelSliceVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.appearance(new $.ig.PrimitiveAppearanceData());
		this.slicePoints(new Array(0));
	},
	_isSelected: false,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this._isSelected = value;
			return value;
		} else {
			return this._isSelected;
		}
	}
	,
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
	_sliceCenterPoint: null,
	sliceCenterPoint: function (value) {
		if (arguments.length === 1) {
			this._sliceCenterPoint = value;
			return value;
		} else {
			return this._sliceCenterPoint;
		}
	}
	,
	_upperLeft: null,
	upperLeft: function (value) {
		if (arguments.length === 1) {
			this._upperLeft = value;
			return value;
		} else {
			return this._upperLeft;
		}
	}
	,
	_lowerLeft: null,
	lowerLeft: function (value) {
		if (arguments.length === 1) {
			this._lowerLeft = value;
			return value;
		} else {
			return this._lowerLeft;
		}
	}
	,
	_upperRight: null,
	upperRight: function (value) {
		if (arguments.length === 1) {
			this._upperRight = value;
			return value;
		} else {
			return this._upperRight;
		}
	}
	,
	_lowerRight: null,
	lowerRight: function (value) {
		if (arguments.length === 1) {
			this._lowerRight = value;
			return value;
		} else {
			return this._lowerRight;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	_innerLabel: null,
	innerLabel: function (value) {
		if (arguments.length === 1) {
			this._innerLabel = value;
			return value;
		} else {
			return this._innerLabel;
		}
	}
	,
	_outerLabel: null,
	outerLabel: function (value) {
		if (arguments.length === 1) {
			this._outerLabel = value;
			return value;
		} else {
			return this._outerLabel;
		}
	}
	,
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	_innerLabelAppearance: null,
	innerLabelAppearance: function (value) {
		if (arguments.length === 1) {
			this._innerLabelAppearance = value;
			return value;
		} else {
			return this._innerLabelAppearance;
		}
	}
	,
	_outerLabelAppearance: null,
	outerLabelAppearance: function (value) {
		if (arguments.length === 1) {
			this._outerLabelAppearance = value;
			return value;
		} else {
			return this._outerLabelAppearance;
		}
	}
	,
	_slicePoints: null,
	slicePoints: function (value) {
		if (arguments.length === 1) {
			this._slicePoints = value;
			return value;
		} else {
			return this._slicePoints;
		}
	}
	,
	_innerLabelPosition: null,
	innerLabelPosition: function (value) {
		if (arguments.length === 1) {
			this._innerLabelPosition = value;
			return value;
		} else {
			return this._innerLabelPosition;
		}
	}
	,
	_outerLabelPosition: null,
	outerLabelPosition: function (value) {
		if (arguments.length === 1) {
			this._outerLabelPosition = value;
			return value;
		} else {
			return this._outerLabelPosition;
		}
	}
	,
	_outerLabelBounds: null,
	outerLabelBounds: function (value) {
		if (arguments.length === 1) {
			this._outerLabelBounds = value;
			return value;
		} else {
			return this._outerLabelBounds;
		}
	}
	,
	_innerLabelBounds: null,
	innerLabelBounds: function (value) {
		if (arguments.length === 1) {
			this._innerLabelBounds = value;
			return value;
		} else {
			return this._innerLabelBounds;
		}
	}
	,
	_visibility: 0,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	scaleByViewport: function (viewport) {
		if ($.ig.Rect.prototype.l_op_Inequality(this.outerLabelBounds(), null)) {
			var l = (this.outerLabelBounds().left() - viewport.left()) / viewport.width();
			var r = (this.outerLabelBounds().right() - viewport.left()) / viewport.width();
			var t = (this.outerLabelBounds().top() - viewport.top()) / viewport.height();
			var b = (this.outerLabelBounds().bottom() - viewport.top()) / viewport.height();
			this.outerLabelBounds(new $.ig.Rect(0, l, t, r - l, b - t));
		}
		if ($.ig.Rect.prototype.l_op_Inequality(this.innerLabelBounds(), null)) {
			var l1 = (this.innerLabelBounds().left() - viewport.left()) / viewport.width();
			var r1 = (this.innerLabelBounds().right() - viewport.left()) / viewport.width();
			var t1 = (this.innerLabelBounds().top() - viewport.top()) / viewport.height();
			var b1 = (this.innerLabelBounds().bottom() - viewport.top()) / viewport.height();
			this.innerLabelBounds(new $.ig.Rect(0, l1, t1, r1 - l1, b1 - t1));
		}
		var newCollection = new Array(this.slicePoints().length);
		var i = 0;
		var $t = this.slicePoints();
		for (var i1 = 0; i1 < $t.length; i1++) {
			var point = $t[i1];
			var x = (point.x() - viewport.left()) / viewport.width();
			var y = (point.y() - viewport.top()) / viewport.height();
			newCollection[i] = new $.ig.PointData(x, y);
			i++;
		}
		this.slicePoints(newCollection);
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("index: " + this.index() + ", ");
		if (this.innerLabel() != null) {
			sb.appendLine1("innerLabel: \"" + this.innerLabel() + "\", ");
		}
		if (this.outerLabel() != null) {
			sb.appendLine1("outerLabel: \"" + this.innerLabel() + "\", ");
		}
		if (this.appearance() != null) {
			sb.appendLine1("appearance: " + this.appearance().serialize() + ", ");
		}
		if (this.innerLabelAppearance() != null) {
			sb.appendLine1("innerLabelAppearance: " + this.innerLabelAppearance().serialize() + ", ");
		}
		if (this.outerLabelAppearance() != null) {
			sb.appendLine1("outerLabelAppearance: " + this.outerLabelAppearance().serialize() + ", ");
		}
		if (this.slicePoints() != null) {
			sb.appendLine1("slicePoints: [");
			for (var i = 0; i < this.slicePoints().length; i++) {
				sb.appendLine1("{ x: " + this.slicePoints()[i].x() + ", y: " + this.slicePoints()[i].y() + " }");
				if (i < this.slicePoints().length - 1) {
					sb.append5(",");
				}
			}
			sb.appendLine1("], ");
		}
		if ($.ig.Rect.prototype.l_op_Inequality(this.innerLabelBounds(), null)) {
			sb.appendLine1("innerLabelBounds: { left: " + this.innerLabelBounds().left() + ", top: " + this.innerLabelBounds().top() + ", width: " + this.innerLabelBounds().width() + ", height: " + this.innerLabelBounds().height() + "}, ");
		}
		if ($.ig.Rect.prototype.l_op_Inequality(this.outerLabelBounds(), null)) {
			sb.appendLine1("outerLabelBounds: { left: " + this.outerLabelBounds().left() + ", top: " + this.outerLabelBounds().top() + ", width: " + this.outerLabelBounds().width() + ", height: " + this.outerLabelBounds().height() + "}, ");
		}
		sb.appendLine1("isSelected: " + (this.isSelected() ? "true" : "false"));
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('FunnelSliceVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FunnelSliceVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.FunnelSliceVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('FunnelSliceVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.FunnelSliceVisualData.prototype.$type))
}, true);

$.ig.util.defType('LegendItemVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.labelBounds($.ig.Rect.prototype.empty());
	},
	_label: null,
	label: function (value) {
		if (arguments.length === 1) {
			this._label = value;
			return value;
		} else {
			return this._label;
		}
	}
	,
	_labelBounds: null,
	labelBounds: function (value) {
		if (arguments.length === 1) {
			this._labelBounds = value;
			return value;
		} else {
			return this._labelBounds;
		}
	}
	,
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	_labelAppearance: null,
	labelAppearance: function (value) {
		if (arguments.length === 1) {
			this._labelAppearance = value;
			return value;
		} else {
			return this._labelAppearance;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		if (this.label() != null) {
			sb.appendLine1("label: \"" + this.label() + "\", ");
		}
		if (this.appearance() != null) {
			sb.appendLine1("appearance: " + this.appearance().serialize() + ", ");
		}
		if (this.labelAppearance() != null) {
			sb.appendLine1("labelAppearance: " + this.labelAppearance().serialize() + ", ");
		}
		sb.appendLine1("labelBounds: { left: " + this.labelBounds().left() + ", top: " + this.labelBounds().top() + ", width: " + this.labelBounds().width() + ", height: " + this.labelBounds().height() + "}");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LegendItemVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LegendVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.LegendItemVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('LegendVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.LegendItemVisualData.prototype.$type))
}, true);

$.ig.util.defType('LegendVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.items(new $.ig.LegendVisualDataList());
	},
	_items: null,
	items: function (value) {
		if (arguments.length === 1) {
			this._items = value;
			return value;
		} else {
			return this._items;
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
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("width: " + this.width() + ",");
		sb.appendLine1("height: " + this.height() + ",");
		sb.appendLine1("items: [");
		for (var i = 0; i < this.items().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.items().__inner[i].serialize());
		}
		sb.appendLine1("],");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LegendVisualData', $.ig.Object.prototype.$type)
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

$.ig.util.defType('DataChartMouseButtonEventArgs', 'EventArgs', {
	init: function (chart, series, item, mouseButtonEventArgs) {
		$.ig.EventArgs.prototype.init.call(this);
		this.chart(chart);
		this.series(series);
		this.item(item);
		this.originalEvent(mouseButtonEventArgs);
	},
	toString: function () {
		return this.chart().name() + ", " + this.series().name() + ", " + (this.item() != null ? this.item().toString() : "") + ", " + this.getPosition(null).toString();
	}
	,
	_originalEvent: null,
	originalEvent: function (value) {
		if (arguments.length === 1) {
			this._originalEvent = value;
			return value;
		} else {
			return this._originalEvent;
		}
	}
	,
	handled: function (value) {
		if (arguments.length === 1) {
			this.originalEvent().handled(value);
			return value;
		} else {
			return this.originalEvent().handled();
		}
	}
	,
	getPosition: function (relativeTo) {
		return this.originalEvent().getPosition(relativeTo);
	}
	,
	originalSource: function () {
		return this.originalEvent().originalSource();
	}
	,
	_item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this._item = value;
			return value;
		} else {
			return this._item;
		}
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
	_chart: null,
	chart: function (value) {
		if (arguments.length === 1) {
			this._chart = value;
			return value;
		} else {
			return this._chart;
		}
	}
	,
	$type: new $.ig.Type('DataChartMouseButtonEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('DataChartLegendMouseButtonEventArgs', 'DataChartMouseButtonEventArgs', {
	init: function (chart, series, item, mouseButtonEventArgs, legendItem) {
		$.ig.DataChartMouseButtonEventArgs.prototype.init.call(this, chart, series, item, mouseButtonEventArgs);
		this.legendItem(legendItem);
	},
	_legendItem: null,
	legendItem: function (value) {
		if (arguments.length === 1) {
			this._legendItem = value;
			return value;
		} else {
			return this._legendItem;
		}
	}
	,
	$type: new $.ig.Type('DataChartLegendMouseButtonEventArgs', $.ig.DataChartMouseButtonEventArgs.prototype.$type)
}, true);

$.ig.util.defType('ChartMouseEventArgs', 'EventArgs', {
	init: function (chart, series, item, originalEvent) {
		$.ig.EventArgs.prototype.init.call(this);
		this.chart(chart);
		this.series(series);
		this.item(item);
		this.originalEvent(originalEvent);
	},
	toString: function () {
		return this.chart().name() + ", " + this.series().name() + ", " + (this.item() != null ? this.item().toString() : "") + ", " + this.getPosition(null).toString();
	}
	,
	_originalEvent: null,
	originalEvent: function (value) {
		if (arguments.length === 1) {
			this._originalEvent = value;
			return value;
		} else {
			return this._originalEvent;
		}
	}
	,
	getPosition: function (relativeTo) {
		return this.originalEvent().getPosition(relativeTo);
	}
	,
	originalSource: function () {
		return this.originalEvent().originalSource();
	}
	,
	_item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this._item = value;
			return value;
		} else {
			return this._item;
		}
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
	_chart: null,
	chart: function (value) {
		if (arguments.length === 1) {
			this._chart = value;
			return value;
		} else {
			return this._chart;
		}
	}
	,
	$type: new $.ig.Type('ChartMouseEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('ChartLegendMouseEventArgs', 'ChartMouseEventArgs', {
	init: function (chart, series, item, mouseEventArgs, legendItem) {
		$.ig.ChartMouseEventArgs.prototype.init.call(this, chart, series, item, mouseEventArgs);
		this.legendItem(legendItem);
	},
	_legendItem: null,
	legendItem: function (value) {
		if (arguments.length === 1) {
			this._legendItem = value;
			return value;
		} else {
			return this._legendItem;
		}
	}
	,
	$type: new $.ig.Type('ChartLegendMouseEventArgs', $.ig.ChartMouseEventArgs.prototype.$type)
}, true);

$.ig.util.defType('DataContext', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
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
	_item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this._item = value;
			return value;
		} else {
			return this._item;
		}
	}
	,
	_actualItemBrush: null,
	actualItemBrush: function (value) {
		if (arguments.length === 1) {
			this._actualItemBrush = value;
			return value;
		} else {
			return this._actualItemBrush;
		}
	}
	,
	_outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this._outline = value;
			return value;
		} else {
			return this._outline;
		}
	}
	,
	_itemLabel: null,
	itemLabel: function (value) {
		if (arguments.length === 1) {
			this._itemLabel = value;
			return value;
		} else {
			return this._itemLabel;
		}
	}
	,
	_itemBrush: null,
	itemBrush: function (value) {
		if (arguments.length === 1) {
			this._itemBrush = value;
			return value;
		} else {
			return this._itemBrush;
		}
	}
	,
	_thickness: 0,
	thickness: function (value) {
		if (arguments.length === 1) {
			this._thickness = value;
			return value;
		} else {
			return this._thickness;
		}
	}
	,
	flatten: function () {
		var series_ = this.series();
		var item_ = this.item();
		var actualItemBrush_ = this.actualItemBrush();
		var outline_ = this.outline();
		var itemLabel_ = this.itemLabel();
		var itemBrush_ = this.itemBrush();
		var thickness_ = this.thickness();
		return { item: item_, itemBrush: actualItemBrush_, outline: outline_, itemLabel: itemLabel_, thickness: thickness_ };
	}
	,
	$type: new $.ig.Type('DataContext', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PieSliceDataContext', 'DataContext', {
	init: function () {
		$.ig.DataContext.prototype.init.call(this);
	},
	_slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this._slice = value;
			return value;
		} else {
			return this._slice;
		}
	}
	,
	_percentValue: 0,
	percentValue: function (value) {
		if (arguments.length === 1) {
			this._percentValue = value;
			return value;
		} else {
			return this._percentValue;
		}
	}
	,
	_isOthersSlice: false,
	isOthersSlice: function (value) {
		if (arguments.length === 1) {
			this._isOthersSlice = value;
			return value;
		} else {
			return this._isOthersSlice;
		}
	}
	,
	_legendLabel: null,
	legendLabel: function (value) {
		if (arguments.length === 1) {
			this._legendLabel = value;
			return value;
		} else {
			return this._legendLabel;
		}
	}
	,
	flatten: function () {
		var ret_ = $.ig.DataContext.prototype.flatten.call(this);
		var percentValue_ = this.percentValue();
		var isOthersSlice_ = this.isOthersSlice();
		ret_.percentValue = percentValue_;
		ret_.isOthersSlice = isOthersSlice_;
		if (isOthersSlice_ && this.item() != null) {
			var items_ = [];
			var en = this.item().getEnumerator();
			while (en.moveNext()) {
				var currItem_ = en.current();
				items_.push(currItem_);
			}
			ret_.item = items_;
		}
		return ret_;
	}
	,
	$type: new $.ig.Type('PieSliceDataContext', $.ig.DataContext.prototype.$type)
}, true);

$.ig.util.defType('FunnelSliceDataContext', 'DataContext', {
	init: function () {
		$.ig.DataContext.prototype.init.call(this);
	},
	_itemOutline: null,
	itemOutline: function (value) {
		if (arguments.length === 1) {
			this._itemOutline = value;
			return value;
		} else {
			return this._itemOutline;
		}
	}
	,
	flatten: function () {
		var ret_ = $.ig.DataContext.prototype.flatten.call(this);
		var itemOutline_ = this.itemOutline();
		ret_.itemOutline = itemOutline_;
		return ret_;
	}
	,
	$type: new $.ig.Type('FunnelSliceDataContext', $.ig.DataContext.prototype.$type)
}, true);

$.ig.util.defType('LegendBaseView', 'Object', {
	init: function (model) {
		this._mouseOver = false;
		$.ig.Object.prototype.init.call(this);
		this.model(model);
		this.viewManager(new $.ig.LegendBaseViewManager(this));
	},
	_viewManager: null,
	viewManager: function (value) {
		if (arguments.length === 1) {
			this._viewManager = value;
			return value;
		} else {
			return this._viewManager;
		}
	}
	,
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	onInit: function () {
	}
	,
	isDiscrete: function () {
		return false;
	}
	,
	createMouseButtonArgs: function (legendItem) {
		var chart;
		var series;
		var item;
		var $ret = this.fetchLegendEnvironment(legendItem, chart, series, item);
		chart = $ret.p1;
		series = $ret.p2;
		item = $ret.p3;
		var be = new $.ig.MouseButtonEventArgs();
		var args = new $.ig.DataChartLegendMouseButtonEventArgs(chart, series, item, be, legendItem);
		return args;
	}
	,
	createMouseArgs: function (legendItem) {
		var chart;
		var series;
		var item;
		var $ret = this.fetchLegendEnvironment(legendItem, chart, series, item);
		chart = $ret.p1;
		series = $ret.p2;
		item = $ret.p3;
		var be = new $.ig.MouseEventArgs();
		var args = new $.ig.ChartLegendMouseEventArgs(chart, series, item, be, legendItem);
		return args;
	}
	,
	fetchLegendEnvironment: function (legendItem, chart, series, item) {
		chart = ($.ig.util.cast($.ig.SeriesViewer.prototype.$type, this.model().owner()));
		series = null;
		item = null;
		if (legendItem != null) {
			var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
			if (contentControl != null && contentControl.content() != null && $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content()) !== null) {
				var dc = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
				series = $.ig.util.cast($.ig.Series.prototype.$type, dc.series());
				if (series != null) {
					chart = series.seriesViewer();
				}
				item = dc.item();
			}
		}
		return {
			p1: chart,
			p2: series,
			p3: item
		};
	}
	,
	detachContent: function () {
	}
	,
	ready: function () {
		return true;
	}
	,
	attachItemEvents: function (uiElement) {
	}
	,
	removeItemEvents: function (uiElement) {
	}
	,
	onContainerProvided: function (container) {
		this.viewManager().onContainerProvided(container);
	}
	,
	removeItemVisual: function (item) {
		this.viewManager().removeItemVisual(item);
	}
	,
	addItemVisual: function (item) {
		var index = this.model().children().indexOf(item);
		this.viewManager().addItemVisual(item, index);
	}
	,
	_legendItemsListStyle: null,
	legendItemsListStyle: function (value) {
		if (arguments.length === 1) {
			this._legendItemsListStyle = value;
			return value;
		} else {
			return this._legendItemsListStyle;
		}
	}
	,
	_legendItemStyle: null,
	legendItemStyle: function (value) {
		if (arguments.length === 1) {
			this._legendItemStyle = value;
			return value;
		} else {
			return this._legendItemStyle;
		}
	}
	,
	_legendItemBadgeStyle: null,
	legendItemBadgeStyle: function (value) {
		if (arguments.length === 1) {
			this._legendItemBadgeStyle = value;
			return value;
		} else {
			return this._legendItemBadgeStyle;
		}
	}
	,
	_legendItemTextStyle: null,
	legendItemTextStyle: function (value) {
		if (arguments.length === 1) {
			this._legendItemTextStyle = value;
			return value;
		} else {
			return this._legendItemTextStyle;
		}
	}
	,
	_mouseOver: false,
	onSizeChanged: function () {
	}
	,
	item: function (e_) {
		var visual = $(e_.currentTarget);
		return this.viewManager().getItem(visual);
	}
	,
	legendMouseOver: function (e) {
		if (!this._mouseOver) {
			this.model().onLegendItemMouseEnter(this.createMouseArgs(this.item(e)));
		}
		this._mouseOver = true;
	}
	,
	legendMouseLeave: function (e) {
		if (this._mouseOver) {
			this.model().onLegendItemMouseLeave(this.createMouseArgs(this.item(e)));
		}
		this._mouseOver = false;
	}
	,
	legendMouseDown: function (e) {
		this.model().onLegendItemMouseLeftButtonDown(this.createMouseButtonArgs(this.item(e)));
	}
	,
	legendMouseUp: function (e) {
		this.model().onLegendItemMouseLeftButtonUp(this.createMouseButtonArgs(this.item(e)));
	}
	,
	$type: new $.ig.Type('LegendBaseView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ItemLegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.itemModel(model);
	},
	_itemModel: null,
	itemModel: function (value) {
		if (arguments.length === 1) {
			this._itemModel = value;
			return value;
		} else {
			return this._itemModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	isDiscrete: function () {
		return true;
	}
	,
	containsContext: function (dataContext) {
		return this.viewManager().containsContext(dataContext);
	}
	,
	$type: new $.ig.Type('ItemLegendView', $.ig.LegendBaseView.prototype.$type)
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

$.ig.util.defType('GradientData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.gradientStops(new $.ig.List$1($.ig.GradientStopData.prototype.$type, 0));
	},
	_gradientStops: null,
	gradientStops: function (value) {
		if (arguments.length === 1) {
			this._gradientStops = value;
			return value;
		} else {
			return this._gradientStops;
		}
	}
	,
	$type: new $.ig.Type('GradientData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('GradientStopData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_offset: 0,
	offset: function (value) {
		if (arguments.length === 1) {
			this._offset = value;
			return value;
		} else {
			return this._offset;
		}
	}
	,
	_brush: null,
	brush: function (value) {
		if (arguments.length === 1) {
			this._brush = value;
			return value;
		} else {
			return this._brush;
		}
	}
	,
	$type: new $.ig.Type('GradientStopData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SliceView', 'Object', {
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	init: function (model) {
		$.ig.Object.prototype.init.call(this);
		this.model(model);
	},
	_slicePath: null,
	slicePath: function (value) {
		if (arguments.length === 1) {
			this._slicePath = value;
			return value;
		} else {
			return this._slicePath;
		}
	}
	,
	onInit: function () {
		this.slicePath(new $.ig.Path());
		this.model().content(this.slicePath());
	}
	,
	onTemplateProvided: function () {
		this.model().createShape();
	}
	,
	getSlicePath: function () {
		return this.slicePath();
	}
	,
	positionSlice: function (x, y) {
		this.slicePath().renderTransform((function () {
			var $ret = new $.ig.TranslateTransform();
			$ret.x(x);
			$ret.y(y);
			return $ret;
		}()));
	}
	,
	resetSlicePosition: function () {
		this.slicePath().renderTransform(null);
	}
	,
	$type: new $.ig.Type('SliceView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FunnelDataContext', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	__item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this.__item = value;
			return value;
		} else {
			return this.__item;
		}
	}
	,
	__index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this.__index = value;
			return value;
		} else {
			return this.__index;
		}
	}
	,
	$type: new $.ig.Type('FunnelDataContext', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FunnelFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.slices(new $.ig.SliceInfoList());
	},
	__innerLabelsShown: false,
	innerLabelsShown: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelsShown = value;
			return value;
		} else {
			return this.__innerLabelsShown;
		}
	}
	,
	__outerLabelsShown: false,
	outerLabelsShown: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelsShown = value;
			return value;
		} else {
			return this.__outerLabelsShown;
		}
	}
	,
	__outerAlignedLeft: false,
	outerAlignedLeft: function (value) {
		if (arguments.length === 1) {
			this.__outerAlignedLeft = value;
			return value;
		} else {
			return this.__outerAlignedLeft;
		}
	}
	,
	__slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this.__slices = value;
			return value;
		} else {
			return this.__slices;
		}
	}
	,
	__outerLabelWidth: 0,
	outerLabelWidth: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelWidth = value;
			return value;
		} else {
			return this.__outerLabelWidth;
		}
	}
	,
	interpolate: function (interpolatedFrame, previousFrame, nextFrame, p) {
		var q = 1 - p;
		interpolatedFrame.innerLabelsShown(nextFrame.innerLabelsShown());
		interpolatedFrame.outerAlignedLeft(nextFrame.outerAlignedLeft());
		interpolatedFrame.outerLabelsShown(nextFrame.outerLabelsShown());
		interpolatedFrame.outerLabelWidth((previousFrame.outerLabelWidth() * q) + (nextFrame.outerLabelWidth() * p));
		var minCount = previousFrame.slices().count();
		var maxCount = nextFrame.slices().count();
		var count = Math.max(minCount, maxCount);
		if (interpolatedFrame.slices().count() < count) {
			interpolatedFrame.slices().insertRange(interpolatedFrame.slices().count(), new Array(count - interpolatedFrame.slices().count()));
		}
		if (interpolatedFrame.slices().count() > count) {
			interpolatedFrame.slices().removeRange(count, interpolatedFrame.slices().count() - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedFrame.slices().__inner[i] = $.ig.SliceInfo.prototype.interpolate(interpolatedFrame.slices().__inner[i], previousFrame.slices().__inner[i], nextFrame.slices().__inner[i], p, q);
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? previousFrame.slices().__inner[minCount - 1] : new $.ig.SliceInfo();
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedFrame.slices().__inner[i1] = $.ig.SliceInfo.prototype.interpolate(interpolatedFrame.slices().__inner[i1], mn, nextFrame.slices().__inner[i1], p, q);
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? nextFrame.slices().__inner[maxCount - 1] : new $.ig.SliceInfo();
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedFrame.slices().__inner[i2] = $.ig.SliceInfo.prototype.interpolate(interpolatedFrame.slices().__inner[i2], previousFrame.slices().__inner[i2], mx, p, q);
			}
		}
	}
	,
	$type: new $.ig.Type('FunnelFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IItemProvider', 'Object', {
	$type: new $.ig.Type('IItemProvider', null)
}, true);

$.ig.util.defType('SliceInfo', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.slice(new $.ig.SliceAppearance());
		this.outerLabelPosition({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	},
	__outerLabel: null,
	outerLabel: function (value) {
		if (arguments.length === 1) {
			this.__outerLabel = value;
			return value;
		} else {
			return this.__outerLabel;
		}
	}
	,
	__outerLabelPosition: null,
	outerLabelPosition: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelPosition = value;
			return value;
		} else {
			return this.__outerLabelPosition;
		}
	}
	,
	__index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this.__index = value;
			return value;
		} else {
			return this.__index;
		}
	}
	,
	__hasSlice: false,
	hasSlice: function (value) {
		if (arguments.length === 1) {
			this.__hasSlice = value;
			return value;
		} else {
			return this.__hasSlice;
		}
	}
	,
	__slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this.__slice = value;
			return value;
		} else {
			return this.__slice;
		}
	}
	,
	__hasOuterLabel: false,
	hasOuterLabel: function (value) {
		if (arguments.length === 1) {
			this.__hasOuterLabel = value;
			return value;
		} else {
			return this.__hasOuterLabel;
		}
	}
	,
	interpolate: function (interpolated, min, max, p, q) {
		if (interpolated == null) {
			interpolated = new $.ig.SliceInfo();
		}
		interpolated.hasOuterLabel(max.hasOuterLabel());
		interpolated.hasSlice(max.hasSlice());
		interpolated.index(max.index());
		interpolated.outerLabel(max.outerLabel());
		interpolated.outerLabelPosition({ __x: (min.outerLabelPosition().__x * q) + (max.outerLabelPosition().__x * p), __y: (min.outerLabelPosition().__y * q) + (max.outerLabelPosition().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		interpolated.slice($.ig.SliceAppearance.prototype.interpolate(interpolated.slice(), min.slice(), max.slice(), p, q));
		return interpolated;
	}
	,
	exportVisualData: function () {
		var visual = new $.ig.FunnelSliceVisualData();
		visual.appearance(new $.ig.PrimitiveAppearanceData());
		visual.appearance().fill($.ig.AppearanceHelper.prototype.fromBrush(this.slice().fill()));
		visual.appearance().stroke($.ig.AppearanceHelper.prototype.fromBrush(this.slice().outline()));
		visual.appearance().strokeThickness(this.slice().outlineThickness());
		visual.appearance().opacity(this.slice().opacity());
		visual.isSelected(this.slice().isSelected());
		visual.index(this.index());
		if (this.slice().innerLabel() == null) {
			visual.innerLabel(String.empty());
		} else {
			visual.innerLabel(this.slice().innerLabel().toString());
		}
		if (this.outerLabel() == null) {
			visual.outerLabel(String.empty());
		} else {
			visual.outerLabel(this.outerLabel().toString());
		}
		visual.upperLeft(this.slice().upperLeft());
		visual.upperRight(this.slice().upperRight());
		visual.lowerLeft(this.slice().lowerLeft());
		visual.lowerRight(this.slice().lowerRight());
		var points = new Array(this.slice().points().count());
		for (var i = 0; i < this.slice().points().count(); i++) {
			points[i] = new $.ig.PointData(this.slice().points().__inner[i].__x, this.slice().points().__inner[i].__y);
		}
		visual.slicePoints(points);
		visual.innerLabelPosition(this.__slice.innerLabelPosition());
		visual.outerLabelPosition(this.__outerLabelPosition);
		visual.outerLabelBounds(this.slice().outerLabelBounds());
		visual.innerLabelBounds(this.slice().innerLabelBounds());
		visual.visibility(this.slice().visibility());
		visual.innerLabelAppearance(new $.ig.LabelAppearanceData());
		visual.innerLabelAppearance().labelBrush($.ig.AppearanceHelper.prototype.fromBrush(this.slice().innerLabelBrush()));
		visual.innerLabelAppearance().text(visual.innerLabel());
		visual.innerLabelAppearance().visibility(this.slice().hasInnerLabel());
		visual.outerLabelAppearance(new $.ig.LabelAppearanceData());
		visual.outerLabelAppearance().labelBrush($.ig.AppearanceHelper.prototype.fromBrush(this.slice().outerLabelBrush()));
		visual.outerLabelAppearance().text(visual.outerLabel());
		visual.outerLabelAppearance().visibility(this.hasOuterLabel());
		return visual;
	}
	,
	$type: new $.ig.Type('SliceInfo', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SliceAppearance', 'Object', {
	init: function () {
		this.__opacity = 1;
		$.ig.Object.prototype.init.call(this);
		this.__lowerLeft = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__lowerRight = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__upperLeft = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__upperRight = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__offset = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	},
	__offset: null,
	offset: function (value) {
		if (arguments.length === 1) {
			this.__offset = value;
			return value;
		} else {
			return this.__offset;
		}
	}
	,
	__upperLeft: null,
	upperLeft: function (value) {
		if (arguments.length === 1) {
			this.__upperLeft = value;
			this.updatePoints();
			return value;
		} else {
			return this.__upperLeft;
		}
	}
	,
	__bezierPoints: null,
	bezierPoints: function (value) {
		if (arguments.length === 1) {
			this.__bezierPoints = value;
			this.updatePoints();
			return value;
		} else {
			return this.__bezierPoints;
		}
	}
	,
	__rightBezierPoints: null,
	rightBezierPoints: function (value) {
		if (arguments.length === 1) {
			this.__rightBezierPoints = value;
			this.updatePoints();
			return value;
		} else {
			return this.__rightBezierPoints;
		}
	}
	,
	__points: null,
	points: function (value) {
		if (arguments.length === 1) {
			this.__points = value;
			return value;
		} else {
			return this.__points;
		}
	}
	,
	updatePoints: function () {
	}
	,
	initPoints: function () {
		var points = new $.ig.PointCollection(0);
		if (this.rightBezierPoints() != null) {
			var en = this.rightBezierPoints().getEnumerator();
			while (en.moveNext()) {
				var point = en.current();
				points.add(point);
			}
		} else {
			points.add(this.upperLeft());
			points.add(this.upperRight());
		}
		if (this.bezierPoints() != null) {
			var en1 = this.bezierPoints().getEnumerator();
			while (en1.moveNext()) {
				var point1 = en1.current();
				points.add(point1);
			}
			if (this.rightBezierPoints() != null && this.rightBezierPoints().count() > 0) {
				points.add(this.rightBezierPoints().__inner[0]);
			}
		} else {
			points.add(this.lowerRight());
			points.add(this.lowerLeft());
			points.add(this.upperLeft());
		}
		var i = 0, count = Math.min($.ig.intDivide(points.count(), 2), 5);
		if (count > 1) {
			var p = points.__inner[0];
			var dif = 0, x = p.__x, y = p.__y;
			while (++i < count && dif < 2) {
				p = points.__inner[i];
				dif = Math.abs(p.__x - x) + Math.abs(p.__y - y);
				if (dif > 1) {
					points.add({ __x: p.__x, __y: p.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				}
			}
		}
		this.points(points);
	}
	,
	__upperRight: null,
	upperRight: function (value) {
		if (arguments.length === 1) {
			this.__upperRight = value;
			this.updatePoints();
			return value;
		} else {
			return this.__upperRight;
		}
	}
	,
	__lowerRight: null,
	lowerRight: function (value) {
		if (arguments.length === 1) {
			this.__lowerRight = value;
			this.updatePoints();
			return value;
		} else {
			return this.__lowerRight;
		}
	}
	,
	__lowerLeft: null,
	lowerLeft: function (value) {
		if (arguments.length === 1) {
			this.__lowerLeft = value;
			this.updatePoints();
			return value;
		} else {
			return this.__lowerLeft;
		}
	}
	,
	__style: null,
	style: function (value) {
		if (arguments.length === 1) {
			this.__style = value;
			return value;
		} else {
			return this.__style;
		}
	}
	,
	__fill: null,
	fill: function (value) {
		if (arguments.length === 1) {
			this.__fill = value;
			return value;
		} else {
			return this.__fill;
		}
	}
	,
	__outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this.__outline = value;
			return value;
		} else {
			return this.__outline;
		}
	}
	,
	__innerLabel: null,
	innerLabel: function (value) {
		if (arguments.length === 1) {
			this.__innerLabel = value;
			return value;
		} else {
			return this.__innerLabel;
		}
	}
	,
	__innerLabelPosition: null,
	innerLabelPosition: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelPosition = value;
			return value;
		} else {
			return this.__innerLabelPosition;
		}
	}
	,
	__hasInnerLabel: false,
	hasInnerLabel: function (value) {
		if (arguments.length === 1) {
			this.__hasInnerLabel = value;
			return value;
		} else {
			return this.__hasInnerLabel && this.__innerLabel != null;
		}
	}
	,
	__item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this.__item = value;
			return value;
		} else {
			return this.__item;
		}
	}
	,
	__index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this.__index = value;
			return value;
		} else {
			return this.__index;
		}
	}
	,
	__innerLabelBounds: null,
	innerLabelBounds: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelBounds = value;
			return value;
		} else {
			return this.__innerLabelBounds;
		}
	}
	,
	__outerLabelBounds: null,
	outerLabelBounds: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelBounds = value;
			return value;
		} else {
			return this.__outerLabelBounds;
		}
	}
	,
	__innerLabelBrush: null,
	innerLabelBrush: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelBrush = value;
			return value;
		} else {
			return this.__innerLabelBrush;
		}
	}
	,
	__outerLabelBrush: null,
	outerLabelBrush: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelBrush = value;
			return value;
		} else {
			return this.__outerLabelBrush;
		}
	}
	,
	__visibility: 0,
	visibility: function (value) {
		if (arguments.length === 1) {
			this.__visibility = value;
			return value;
		} else {
			return this.__visibility;
		}
	}
	,
	__isSelected: false,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this.__isSelected = value;
			return value;
		} else {
			return this.__isSelected;
		}
	}
	,
	_outlineThickness: 0,
	outlineThickness: function (value) {
		if (arguments.length === 1) {
			this._outlineThickness = value;
			return value;
		} else {
			return this._outlineThickness;
		}
	}
	,
	__opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this.__opacity = value;
			return value;
		} else {
			return this.__opacity;
		}
	}
	,
	interpolate: function (interpolated, min, max, p, q) {
		if (interpolated == null) {
			interpolated = new $.ig.SliceAppearance();
		}
		interpolated.fill(max.fill());
		interpolated.hasInnerLabel(max.hasInnerLabel());
		interpolated.innerLabel(max.innerLabel());
		if (min.hasInnerLabel() && max.hasInnerLabel()) {
			interpolated.innerLabelPosition({ __x: (min.innerLabelPosition().__x * q) + (max.innerLabelPosition().__x * p), __y: (min.innerLabelPosition().__y * q) + (max.innerLabelPosition().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		} else if (max.hasInnerLabel()) {
			interpolated.innerLabelPosition({ __x: max.innerLabelPosition().__x * p, __y: max.innerLabelPosition().__y * p, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		interpolated.lowerLeft({ __x: (min.lowerLeft().__x * q) + (max.lowerLeft().__x * p), __y: (min.lowerLeft().__y * q) + (max.lowerLeft().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		interpolated.lowerRight({ __x: (min.lowerRight().__x * q) + (max.lowerRight().__x * p), __y: (min.lowerRight().__y * q) + (max.lowerRight().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		interpolated.upperLeft({ __x: (min.upperLeft().__x * q) + (max.upperLeft().__x * p), __y: (min.upperLeft().__y * q) + (max.upperLeft().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		interpolated.upperRight({ __x: (min.upperRight().__x * q) + (max.upperRight().__x * p), __y: (min.upperRight().__y * q) + (max.upperRight().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		interpolated.offset({ __x: (min.offset().__x * q) + (max.offset().__x * p), __y: (min.offset().__y * q) + (max.offset().__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		if (max.bezierPoints() != null) {
			var minPoints = min.bezierPoints();
			if (minPoints == null) {
				minPoints = new $.ig.PointList();
				minPoints.add(min.upperLeft());
				minPoints.add(min.lowerLeft());
			}
			interpolated.bezierPoints($.ig.SliceAppearance.prototype.interpolatePoints(interpolated.bezierPoints(), minPoints, max.bezierPoints(), p, q));
		} else {
			interpolated.bezierPoints(null);
		}
		if (max.rightBezierPoints() != null) {
			var minPoints1 = min.rightBezierPoints();
			if (minPoints1 == null) {
				minPoints1 = new $.ig.PointList();
				minPoints1.add(min.lowerRight());
				minPoints1.add(min.upperRight());
			}
			interpolated.rightBezierPoints($.ig.SliceAppearance.prototype.interpolatePoints(interpolated.rightBezierPoints(), minPoints1, max.rightBezierPoints(), p, q));
		} else {
			interpolated.rightBezierPoints(null);
		}
		interpolated.outline(max.outline());
		interpolated.style(max.style());
		interpolated.item(max.item());
		interpolated.index(max.index());
		interpolated.initPoints();
		return interpolated;
	}
	,
	interpolatePoints: function (interpolatedPoints, minPoints, maxPoints, p, q) {
		if (interpolatedPoints == null) {
			interpolatedPoints = new $.ig.PointList();
		}
		if (minPoints == null) {
			minPoints = new $.ig.PointList();
		}
		var minCount = minPoints.count();
		var maxCount = maxPoints.count();
		var count = Math.max(minCount, maxCount);
		if (interpolatedPoints.count() < count) {
			interpolatedPoints.insertRange(interpolatedPoints.count(), new Array(count - interpolatedPoints.count()));
		}
		if (interpolatedPoints.count() > count) {
			interpolatedPoints.removeRange(count, interpolatedPoints.count() - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedPoints.__inner[i] = { __x: (minPoints.__inner[i].__x * q) + (maxPoints.__inner[i].__x * p), __y: (minPoints.__inner[i].__y * q) + (maxPoints.__inner[i].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minPoints.__inner[minCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedPoints.__inner[i1] = { __x: (mn.__x * q) + (maxPoints.__inner[i1].__x * p), __y: (mn.__y * q) + (maxPoints.__inner[i1].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxPoints.__inner[maxCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedPoints.__inner[i2] = { __x: (minPoints.__inner[i2].__x * q) + (mx.__x * p), __y: (minPoints.__inner[i2].__y * q) + (mx.__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		return interpolatedPoints;
	}
	,
	$type: new $.ig.Type('SliceAppearance', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SliceInfoList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.SliceInfo.prototype.$type, 0);
	},
	indexSort: function () {
		this.sort2(function (s1, s2) {
			if (s1.index() < s2.index()) {
				return -1;
			}
			if (s1.index() > s2.index()) {
				return 1;
			}
			return 0;
		});
	}
	,
	sortByY: function () {
		this.sort2(function (s1, s2) {
			if (s1.slice().offset().__y < s2.slice().offset().__y) {
				return -1;
			}
			if (s1.slice().offset().__y > s2.slice().offset().__y) {
				return 1;
			}
			return 0;
		});
	}
	,
	getByYValue: function (yValue) {
		this.sortByY();
		var index = this.binarySearch2(function (si) {
			if (yValue < (si.slice().upperLeft().__y + si.slice().offset().__y)) {
				return -1;
			}
			if (yValue > (si.slice().lowerLeft().__y + si.slice().offset().__y)) {
				return 1;
			} else {
				return 0;
			}
		});
		if (index >= 0) {
			index = (this.__inner[index]).index();
		} else {
			index = -1;
		}
		this.indexSort();
		if (index >= 0) {
			return index;
		}
		return -1;
	}
	,
	binarySearch2: function (comparisonFunction) {
		var currMin = 0;
		var currMax = this.count() - 1;
		while (currMin <= currMax) {
			var currMid = (currMin + ((currMax - currMin) >> 1));
			var compResult = comparisonFunction(this.__inner[currMid]);
			if (compResult < 0) {
				currMax = currMid - 1;
			} else if (compResult > 0) {
				currMin = currMid + 1;
			} else {
				return currMid;
			}
		}
		return ~currMin;
	}
	,
	$type: new $.ig.Type('SliceInfoList', $.ig.List$1.prototype.$type.specialize($.ig.SliceInfo.prototype.$type))
}, true);

$.ig.util.defType('SliceSelectionManager', 'Object', {
	init: function () {
		this.__selected = new $.ig.Dictionary$2($.ig.Number.prototype.$type, $.ig.Object.prototype.$type, 0);
		$.ig.Object.prototype.init.call(this);
	},
	__selected: null,
	toggleSelection: function (index, item) {
		if (this.__selected.containsKey(index)) {
			this.__selected.remove(index);
		} else {
			this.__selected.item(index, item);
		}
	}
	,
	isSelected: function (index) {
		return this.__selected.containsKey(index);
	}
	,
	any: function () {
		return this.__selected.count() > 0;
	}
	,
	isUnselected: function (index) {
		return !this.isSelected(index) && this.any();
	}
	,
	dataUpdated: function (m, data) {
		switch (m.action()) {
			case $.ig.ItemsSourceAction.prototype.change: break;
			case $.ig.ItemsSourceAction.prototype.insert:
				this.shiftUp(m.position(), m.count());
				break;
			case $.ig.ItemsSourceAction.prototype.remove:
				this.remove(m.position(), m.count());
				break;
			case $.ig.ItemsSourceAction.prototype.replace:
				for (var i = 0; i < m.count(); i++) {
					if (this.__selected.containsKey(i)) {
						this.__selected.item(i, data.values().item(i));
					}
				}
				break;
			case $.ig.ItemsSourceAction.prototype.reset:
				this.clearSelection();
				break;
		}
	}
	,
	remove: function (startingPosition, count) {
		var toShift = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var toShiftObjects = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var en = this.__selected.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (key >= startingPosition) {
				toShift.add(key);
				toShiftObjects.add1(this.__selected.item(key));
			}
		}
		var i = 0;
		var en1 = toShift.getEnumerator();
		while (en1.moveNext()) {
			var value = en1.current();
			this.__selected.remove(value);
			if (value - count > startingPosition) {
				this.__selected.item(value - count, toShiftObjects.__inner[i]);
			}
			i++;
		}
	}
	,
	shiftUp: function (startingPosition, count) {
		var toShift = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var toShiftObjects = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var en = this.__selected.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (key >= startingPosition) {
				toShift.add(key);
				toShiftObjects.add1(this.__selected.item(key));
			}
		}
		var i = 0;
		var en1 = toShift.getEnumerator();
		while (en1.moveNext()) {
			var value = en1.current();
			this.__selected.remove(value);
			this.__selected.item(value + count, toShiftObjects.__inner[i]);
			i++;
		}
	}
	,
	clearSelection: function () {
		this.__selected.clear();
	}
	,
	getSelectedItems: function () {
		var items = new Array(this.__selected.count());
		var i = 0;
		var en = this.__selected.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			items[i] = key;
			i++;
		}
		return items;
	}
	,
	setSelectedItems: function (indexes, values) {
		this.clearSelection();
		for (var i = 0; i < indexes.length; i++) {
			var index = indexes[i];
			this.toggleSelection(index, values.values().item(index));
		}
	}
	,
	$type: new $.ig.Type('SliceSelectionManager', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamFunnelController', 'Object', {
	_slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this._slices = value;
			return value;
		} else {
			return this._slices;
		}
	}
	,
	_funnelView: null,
	_innerLabels: null,
	_outerLabels: null,
	_formatInnerLabel: null,
	_formatOuterLabel: null,
	_innerLabelsFormatted: false,
	_outerLabelsFormatted: false,
	init: function () {
		var $self = this;
		this._innerLabelsFormatted = false;
		this._outerLabelsFormatted = false;
		this.__outlineThickness = -1;
		this.__effectiveUseBezierCurve = false;
		$.ig.Object.prototype.init.call(this);
		this.doubleAnimator(new $.ig.DoubleAnimator(0, 1, 2000));
		var $t = this.doubleAnimator();
		$t.propertyChanged = $.ig.Delegate.prototype.combine($t.propertyChanged, this.doubleAnimator_PropertyChanged.runOn(this));
		this.previousFrame(new $.ig.FunnelFrame());
		this.interpolatedFrame(new $.ig.FunnelFrame());
		this.currentFrame(new $.ig.FunnelFrame());
		this.sliceSelectionManager(new $.ig.SliceSelectionManager());
		this.valueColumn(new $.ig.DoubleColumn());
		this.innerLabelColumn(new $.ig.ObjectColumn());
		this.outerLabelColumn(new $.ig.ObjectColumn());
		this.renderingMessages(new $.ig.MessageChannel());
		this.modelUpdateMessages(new $.ig.MessageChannel());
		this.messageHandler(new $.ig.MessageHandler());
		this.messageHandler().addHandler($.ig.PropertyChangedMessage.prototype.$type, this.propertyChangedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.DataUpdatedMessage.prototype.$type, this.dataUpdatedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.ViewportChangedMessage.prototype.$type, this.viewportChangedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.ViewPropertyChangedMessage.prototype.$type, this.viewPropertyChangedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.MouseMoveMessage.prototype.$type, this.mouseMoveMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.MouseButtonMessage.prototype.$type, this.mouseButtonMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.MouseLeaveMessage.prototype.$type, this.mouseLeaveMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.UserSelectedItemsChangedMessage.prototype.$type, this.userSelectedItemsChangedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.LabelSizeChangedMessage.prototype.$type, this.labelSizeChangedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.ContainerSizeChangedMessage.prototype.$type, function (m) { $self.renderingMessages().sendMessage(m); });
	},
	doubleAnimator_PropertyChanged: function (sender, e) {
		this.transitionProgress(this.doubleAnimator().transitionProgress());
		if (this.transitionProgress() == 1) {
			this.animationInProgress(false);
			this.displayFrame(this.currentFrame());
		}
		if (this.animationInProgress()) {
			var q = 1 - this.transitionProgress();
			$.ig.FunnelFrame.prototype.interpolate(this.interpolatedFrame(), this.previousFrame(), this.currentFrame(), this.transitionProgress());
			this.displayFrame(this.interpolatedFrame());
		}
	}
	,
	__doubleAnimator: null,
	doubleAnimator: function (value) {
		if (arguments.length === 1) {
			this.__doubleAnimator = value;
			return value;
		} else {
			return this.__doubleAnimator;
		}
	}
	,
	__bezier: null,
	bezier: function (value) {
		if (arguments.length === 1) {
			this.__bezier = value;
			return value;
		} else {
			return this.__bezier;
		}
	}
	,
	__messageHandler: null,
	messageHandler: function (value) {
		if (arguments.length === 1) {
			this.__messageHandler = value;
			return value;
		} else {
			return this.__messageHandler;
		}
	}
	,
	__valueColumn: null,
	valueColumn: function (value) {
		if (arguments.length === 1) {
			this.__valueColumn = value;
			return value;
		} else {
			return this.__valueColumn;
		}
	}
	,
	__innerLabelColumn: null,
	innerLabelColumn: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelColumn = value;
			return value;
		} else {
			if (!this._innerLabelsFormatted && this._funnelView != null) {
				this._innerLabelsFormatted = this._funnelView.formatLabels(this.__innerLabelColumn, this._formatInnerLabel);
			}
			return this.__innerLabelColumn;
		}
	}
	,
	__outerLabelColumn: null,
	outerLabelColumn: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelColumn = value;
			return value;
		} else {
			if (!this._outerLabelsFormatted && this._funnelView != null) {
				this._outerLabelsFormatted = this._funnelView.formatLabels(this.__outerLabelColumn, this._formatOuterLabel);
			}
			return this.__outerLabelColumn;
		}
	}
	,
	__valueMemberPath: null,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this.__valueMemberPath = value;
			return value;
		} else {
			return this.__valueMemberPath;
		}
	}
	,
	__outerLabelMemberPath: null,
	outerLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelMemberPath = value;
			return value;
		} else {
			return this.__outerLabelMemberPath;
		}
	}
	,
	__innerLabelMemberPath: null,
	innerLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelMemberPath = value;
			return value;
		} else {
			return this.__innerLabelMemberPath;
		}
	}
	,
	__refreshRequired: false,
	refreshRequired: function (value) {
		if (arguments.length === 1) {
			this.__refreshRequired = value;
			return value;
		} else {
			return this.__refreshRequired;
		}
	}
	,
	__viewportWidth: 0,
	viewportWidth: function (value) {
		if (arguments.length === 1) {
			this.__viewportWidth = value;
			return value;
		} else {
			return this.__viewportWidth;
		}
	}
	,
	__viewportHeight: 0,
	viewportHeight: function (value) {
		if (arguments.length === 1) {
			this.__viewportHeight = value;
			return value;
		} else {
			return this.__viewportHeight;
		}
	}
	,
	__renderingMessages: null,
	renderingMessages: function (value) {
		if (arguments.length === 1) {
			this.__renderingMessages = value;
			return value;
		} else {
			return this.__renderingMessages;
		}
	}
	,
	__modelUpdateMessages: null,
	modelUpdateMessages: function (value) {
		if (arguments.length === 1) {
			this.__modelUpdateMessages = value;
			return value;
		} else {
			return this.__modelUpdateMessages;
		}
	}
	,
	__widthDecider: null,
	widthDecider: function (value) {
		if (arguments.length === 1) {
			this.__widthDecider = value;
			return value;
		} else {
			return this.__widthDecider;
		}
	}
	,
	__sizeDecider: null,
	sizeDecider: function (value) {
		if (arguments.length === 1) {
			this.__sizeDecider = value;
			return value;
		} else {
			return this.__sizeDecider;
		}
	}
	,
	__innerLabelsVisibility: false,
	innerLabelVisibility: function (value) {
		if (arguments.length === 1) {
			this.__innerLabelsVisibility = value;
			return value;
		} else {
			return this.__innerLabelsVisibility;
		}
	}
	,
	__outerLabelsVisibility: false,
	outerLabelVisibility: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelsVisibility = value;
			return value;
		} else {
			return this.__outerLabelsVisibility;
		}
	}
	,
	__transitionProgress: 0,
	transitionProgress: function (value) {
		if (arguments.length === 1) {
			this.__transitionProgress = value;
			return value;
		} else {
			return this.__transitionProgress;
		}
	}
	,
	__isSplined: false,
	isSplined: function (value) {
		if (arguments.length === 1) {
			this.__isSplined = value;
			return value;
		} else {
			return this.__isSplined;
		}
	}
	,
	__bottomEdgeWidth: 0,
	bottomEdgeWidth: function (value) {
		if (arguments.length === 1) {
			this.__bottomEdgeWidth = value;
			return value;
		} else {
			return this.__bottomEdgeWidth;
		}
	}
	,
	__brushes: null,
	brushes: function (value) {
		if (arguments.length === 1) {
			this.__brushes = value;
			return value;
		} else {
			if ((this.__brushes == null || this.__brushes.count() < 1) && this._funnelView != null) {
				this.__brushes = this._funnelView.getDefBrushes(true);
			}
			return this.__brushes;
		}
	}
	,
	__outlines: null,
	outlines: function (value) {
		if (arguments.length === 1) {
			this.__outlines = value;
			return value;
		} else {
			if ((this.__outlines == null || this.__outlines.count() < 1) && this._funnelView != null) {
				this.__outlines = this._funnelView.getDefBrushes(false);
			}
			return this.__outlines;
		}
	}
	,
	__outerLabelAlignment: 0,
	outerLabelAlignment: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelAlignment = value;
			return value;
		} else {
			return this.__outerLabelAlignment;
		}
	}
	,
	__funnelSliceDisplay: 0,
	funnelSliceDisplay: function (value) {
		if (arguments.length === 1) {
			this.__funnelSliceDisplay = value;
			return value;
		} else {
			return this.__funnelSliceDisplay;
		}
	}
	,
	__animationInProgress: false,
	animationInProgress: function (value) {
		if (arguments.length === 1) {
			this.__animationInProgress = value;
			return value;
		} else {
			return this.__animationInProgress;
		}
	}
	,
	__transitionDuration: 0,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this.__transitionDuration = value;
			return value;
		} else {
			return this.__transitionDuration;
		}
	}
	,
	__useBezierCurve: false,
	useBezierCurve: function (value) {
		if (arguments.length === 1) {
			this.__useBezierCurve = value;
			return value;
		} else {
			return this.__useBezierCurve;
		}
	}
	,
	__upperBezierControlPoint: null,
	upperBezierControlPoint: function (value) {
		if (arguments.length === 1) {
			this.__upperBezierControlPoint = value;
			return value;
		} else {
			return this.__upperBezierControlPoint;
		}
	}
	,
	__lowerBezierControlPoint: null,
	lowerBezierControlPoint: function (value) {
		if (arguments.length === 1) {
			this.__lowerBezierControlPoint = value;
			return value;
		} else {
			return this.__lowerBezierControlPoint;
		}
	}
	,
	__allowSliceSelection: false,
	allowSliceSelection: function (value) {
		if (arguments.length === 1) {
			this.__allowSliceSelection = value;
			return value;
		} else {
			return this.__allowSliceSelection;
		}
	}
	,
	__hoveredSliceIndex: 0,
	hoveredSliceIndex: function (value) {
		if (arguments.length === 1) {
			this.__hoveredSliceIndex = value;
			return value;
		} else {
			return this.__hoveredSliceIndex;
		}
	}
	,
	__pressedSliceIndex: 0,
	pressedSliceIndex: function (value) {
		if (arguments.length === 1) {
			this.__pressedSliceIndex = value;
			return value;
		} else {
			return this.__pressedSliceIndex;
		}
	}
	,
	__itemProvider: null,
	itemProvider: function (value) {
		if (arguments.length === 1) {
			this.__itemProvider = value;
			return value;
		} else {
			return this.__itemProvider;
		}
	}
	,
	__serviceProvider: null,
	serviceProvider: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__serviceProvider;
			this.__serviceProvider = value;
			this.onServiceProviderChanged(oldValue, this.__serviceProvider);
			return value;
		} else {
			return this.__serviceProvider;
		}
	}
	,
	__sliceSelectionManager: null,
	sliceSelectionManager: function (value) {
		if (arguments.length === 1) {
			this.__sliceSelectionManager = value;
			return value;
		} else {
			return this.__sliceSelectionManager;
		}
	}
	,
	__previousFrame: null,
	previousFrame: function (value) {
		if (arguments.length === 1) {
			this.__previousFrame = value;
			return value;
		} else {
			return this.__previousFrame;
		}
	}
	,
	__currentFrame: null,
	currentFrame: function (value) {
		if (arguments.length === 1) {
			this.__currentFrame = value;
			return value;
		} else {
			return this.__currentFrame;
		}
	}
	,
	__interpolatedFrame: null,
	interpolatedFrame: function (value) {
		if (arguments.length === 1) {
			this.__interpolatedFrame = value;
			return value;
		} else {
			return this.__interpolatedFrame;
		}
	}
	,
	__selectedSliceStyle: null,
	selectedSliceStyle: function (value) {
		if (arguments.length === 1) {
			this.__selectedSliceStyle = value;
			return value;
		} else {
			return this.__selectedSliceStyle;
		}
	}
	,
	__unselectedSliceStyle: null,
	unselectedSliceStyle: function (value) {
		if (arguments.length === 1) {
			this.__unselectedSliceStyle = value;
			return value;
		} else {
			return this.__unselectedSliceStyle;
		}
	}
	,
	__useUnselectedStyle: false,
	useUnselectedStyle: function (value) {
		if (arguments.length === 1) {
			this.__useUnselectedStyle = value;
			return value;
		} else {
			return this.__useUnselectedStyle;
		}
	}
	,
	__isInverted: false,
	isInverted: function (value) {
		if (arguments.length === 1) {
			this.__isInverted = value;
			return value;
		} else {
			return this.__isInverted;
		}
	}
	,
	__userOuterLabelsForLegend: false,
	userOuterLabelsForLegend: function (value) {
		if (arguments.length === 1) {
			this.__userOuterLabelsForLegend = value;
			return value;
		} else {
			return this.__userOuterLabelsForLegend;
		}
	}
	,
	__hasTooltip: false,
	hasTooltip: function (value) {
		if (arguments.length === 1) {
			this.__hasTooltip = value;
			return value;
		} else {
			return this.__hasTooltip;
		}
	}
	,
	__textStyle: null,
	textStyle: function (value) {
		if (arguments.length === 1) {
			this.__textStyle = value;
			return value;
		} else {
			return this.__textStyle;
		}
	}
	,
	__outerLabelTextStyle: null,
	outerLabelTextStyle: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelTextStyle = value;
			return value;
		} else {
			return this.__outerLabelTextStyle;
		}
	}
	,
	__outlineThickness: 0,
	outlineThickness: function () {
		var d = this.__outlineThickness;
		if (d < 0 && this._funnelView != null) {
			d = this.__outlineThickness = this._funnelView.getDefOutline();
		}
		return d < 0 ? 1 : d;
	}
	,
	onServiceProviderChanged: function (oldValue, newValue) {
		if (oldValue != null) {
			var channel = $.ig.util.cast($.ig.MessageChannel.prototype.$type, oldValue.getService("ConfigurationMessages"));
			if (channel != null) {
				channel.detachTarget(this.messageReceived.runOn(this));
			}
			channel = $.ig.util.cast($.ig.MessageChannel.prototype.$type, oldValue.getService("InteractionMessages"));
			if (channel != null) {
				channel.detachTarget(this.messageReceived.runOn(this));
			}
			this.renderingMessages().detachFromNext();
			this.modelUpdateMessages().detachFromNext();
		}
		if (newValue != null) {
			var channel1 = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("ConfigurationMessages"));
			if (channel1 != null) {
				channel1.attachTarget(this.messageReceived.runOn(this));
			}
			channel1 = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("InteractionMessages"));
			if (channel1 != null) {
				channel1.attachTarget(this.messageReceived.runOn(this));
			}
			var rendering = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("RenderingMessages"));
			this.renderingMessages().connectTo(rendering);
			var modelUpdates = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("ModelUpdateMessages"));
			this.modelUpdateMessages().connectTo(modelUpdates);
		}
		this.refreshRequired(true);
	}
	,
	messageReceived: function (m) {
		this.messageHandler().messageReceived(m);
		var msg = $.ig.util.cast($.ig.RenderingMessage.prototype.$type, m);
		if (msg != null) {
			this.refreshRequired(msg.refreshRequired());
		}
		this.refreshVisuals();
	}
	,
	sizeValid: function () {
		return this.viewportHeight() > 0 && this.viewportWidth() > 0;
	}
	,
	isValid: function () {
		return this.valueColumn() != null && this.valueColumn().values() != null && this.valueColumn().values().count() > 0 && this.widthDecider() != null && this.sizeDecider() != null && this.brushes() != null && this.brushes().count() > 0 && this.outlines() != null && this.outlines().count() > 0 && this.sizeValid();
	}
	,
	refreshVisuals: function () {
		if (!this.refreshRequired()) {
			return;
		}
		if (!this.isValid()) {
			this.clearRendering();
			return;
		}
		this.refreshRequired(false);
		var outerLabelWidth = this.widthDecider().decideWidth(this.getOuterLabels());
		if (outerLabelWidth > this.viewportWidth()) {
			outerLabelWidth = 0;
		}
		if (!this.outerLabelVisibility()) {
			outerLabelWidth = 0;
		}
		var plotAreaWidth = this.viewportWidth() - outerLabelWidth;
		var plotOuterLabels = outerLabelWidth > 0;
		var plotInnerLabels = this.innerLabelVisibility();
		var plotSlices = plotAreaWidth > 0;
		if (plotSlices || plotOuterLabels) {
			this.plot(plotSlices, plotOuterLabels, plotInnerLabels, plotAreaWidth, outerLabelWidth);
		}
	}
	,
	clearRendering: function () {
		if (this.shouldAnimate() && this.transitionProgress() > 0) {
			this.doubleAnimator().stop();
		}
		this.sendClearMessage("LeftLabels");
		this.sendClearMessage("RightLabels");
		this.sendClearMessage("SliceArea");
		this.cleanupUnused();
		this.currentFrame().slices().clear();
		this.interpolatedFrame().slices().clear();
		this.previousFrame().slices().clear();
	}
	,
	cleanupUnused: function () {
		var frc = new $.ig.FrameRenderCompleteMessage();
		this.renderingMessages().sendMessage(frc);
	}
	,
	plot: function (plotSlices, plotOuterLabels, plotInnerLabels, plotAreaWidth, outerLabelWidth) {
		if (this.animationInProgress()) {
			var frame = this.previousFrame();
			this.previousFrame(this.interpolatedFrame());
			this.interpolatedFrame(frame);
		} else {
			var frame1 = this.previousFrame();
			this.previousFrame(this.currentFrame());
			this.currentFrame(frame1);
		}
		this.plotFrame(this.currentFrame(), plotSlices, plotOuterLabels, plotInnerLabels, plotAreaWidth, outerLabelWidth);
		this.doubleAnimator().stop();
		this.transitionProgress(0);
		if (this.shouldAnimate()) {
			$.ig.FunnelFrame.prototype.interpolate(this.interpolatedFrame(), this.previousFrame(), this.currentFrame(), this.transitionProgress());
			this.displayFrame(this.interpolatedFrame());
			this.animationInProgress(true);
			this.doubleAnimator().start();
		} else {
			this.displayFrame(this.currentFrame());
		}
	}
	,
	safeGetValue: function (value) {
		if ($.ig.util.isNaN(value)) {
			return 0;
		}
		return Math.abs(value);
	}
	,
	__effectiveUseBezierCurve: false,
	plotFrame: function (frame, plotSlices, plotOuterLabels, plotInnerLabels, plotAreaWidth, outerLabelWidth) {
		var plotAreaCenter = plotAreaWidth / 2;
		this.__effectiveUseBezierCurve = this.useBezierCurve();
		if (this.__effectiveUseBezierCurve) {
			this.initializeBezier(plotAreaWidth, plotAreaCenter);
		} else {
			this.bezier(null);
		}
		var doWeighting = this.funnelSliceDisplay() == $.ig.FunnelSliceDisplay.prototype.weighted;
		var indices = this.getSortedIndices();
		var totalValue = 0;
		var en = indices.values().getEnumerator();
		while (en.moveNext()) {
			var index = en.current();
			totalValue += this.safeGetValue(this.valueColumn().values().item(index));
		}
		if (totalValue == 0) {
			doWeighting = false;
		}
		var unweightedHeight = this.viewportHeight() / indices.values().count();
		var currentTop = 0;
		var shift = 0;
		shift = this.outlineThickness() / 2;
		frame.outerLabelWidth(outerLabelWidth);
		frame.innerLabelsShown(plotInnerLabels);
		frame.outerLabelsShown(plotOuterLabels);
		frame.outerAlignedLeft(this.outerLabelAlignment() == $.ig.OuterLabelAlignment.prototype.left);
		frame.slices(new $.ig.SliceInfoList());
		var i = 0;
		var en1 = indices.values().getEnumerator();
		while (en1.moveNext()) {
			var index1 = en1.current();
			var currentHeight;
			if (doWeighting) {
				currentHeight = (this.safeGetValue(this.valueColumn().values().item(index1)) / totalValue) * this.viewportHeight();
			} else {
				currentHeight = unweightedHeight;
			}
			var topWidth = this.getWidthAt(plotAreaWidth, currentTop + shift);
			var currentBottom = currentTop + currentHeight;
			var bottomWidth = this.getWidthAt(plotAreaWidth, currentBottom - shift);
			var halfTopWidth = topWidth / 2;
			var halfBottomWidth = bottomWidth / 2;
			var info = new $.ig.SliceInfo();
			var slice = info.slice();
			var offsetX = 0;
			if (halfBottomWidth > halfTopWidth) {
				offsetX = plotAreaCenter - halfBottomWidth;
			} else {
				offsetX = plotAreaCenter - halfTopWidth;
			}
			var offsetY = currentTop;
			slice.fill(this.getSliceFill(index1));
			slice.outline(this.getSliceOutline(index1));
			slice.style(this.getSliceStyle(index1));
			if (plotSlices) {
				info.hasSlice(true);
				slice.upperLeft({ __x: plotAreaCenter - halfTopWidth - offsetX + shift, __y: currentTop - offsetY + shift, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				slice.upperRight({ __x: plotAreaCenter + halfTopWidth - offsetX - shift, __y: currentTop - offsetY + shift, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				slice.lowerLeft({ __x: plotAreaCenter - halfBottomWidth - offsetX + shift, __y: currentBottom - offsetY - shift, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				slice.lowerRight({ __x: plotAreaCenter + halfBottomWidth - offsetX - shift, __y: currentBottom - offsetY - shift, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				if (this.__effectiveUseBezierCurve) {
					this.addBezierPoints(slice, currentTop + shift, currentBottom - shift, plotAreaCenter, offsetX - shift, offsetY);
				}
				slice.initPoints();
			}
			if (plotInnerLabels) {
				slice.hasInnerLabel(true);
				slice.innerLabelPosition({ __x: plotAreaCenter - offsetX, __y: (currentTop + currentBottom) / 2 - offsetY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				slice.innerLabel(this.getInnerLabel(index1));
				var innerLabelSize = this.sizeDecider().decideLabelSize(info, true);
				if (innerLabelSize.width() > plotAreaWidth) {
					slice.hasInnerLabel(false);
				}
				if (innerLabelSize.height() > currentHeight) {
					slice.hasInnerLabel(false);
				}
			} else {
				slice.hasInnerLabel(false);
				slice.innerLabel(this.getInnerLabel(index1));
			}
			if (plotInnerLabels || plotSlices) {
				slice.offset({ __x: offsetX, __y: offsetY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				slice.item(this.valueColumn().values().item(index1));
				slice.index(index1);
			}
			if (plotOuterLabels) {
				info.hasOuterLabel(true);
				info.outerLabelPosition({ __x: 0, __y: (currentTop + currentBottom) / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				info.outerLabel(this.getOuterLabel(index1));
				var outerLabelSize = this.sizeDecider().decideLabelSize(info, false);
				if (outerLabelSize.height() > currentHeight) {
					info.hasOuterLabel(false);
				}
			} else {
				info.hasOuterLabel(false);
				info.outerLabel(this.getOuterLabel(index1));
			}
			info.index(index1);
			frame.slices().add(info);
			currentTop += currentHeight;
			i++;
		}
		this.renderLegend(frame);
		frame.slices().indexSort();
	}
	,
	addBezierPoints: function (sliceAppearance, currentTop, currentBottom, plotAreaCenter, offsetx, offsety) {
		var top = this.bezier().getPointAt(currentTop);
		var bottom = this.bezier().getPointAt(currentBottom);
		var points = new $.ig.PointList();
		var rightPoints = new $.ig.PointList();
		var startIndex = top._index;
		var endIndex = bottom._index;
		for (var i = startIndex; i <= endIndex; i++) {
			points.add({ __x: (this.bezier().points().__inner[i])._point.__x - offsetx, __y: (this.bezier().points().__inner[i])._point.__y - offsety, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		offsetx += this.outlineThickness();
		for (var i1 = endIndex; i1 >= startIndex; i1--) {
			var p = (this.bezier().points().__inner[i1])._point;
			rightPoints.add({ __x: plotAreaCenter + plotAreaCenter - p.__x - offsetx, __y: p.__y - offsety, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		sliceAppearance.bezierPoints(points);
		sliceAppearance.rightBezierPoints(rightPoints);
	}
	,
	pointDiffers: function (p1, p2) {
		if (p1.__x != p2.__x || p1.__y != p2.__y) {
			return true;
		}
		return false;
	}
	,
	initializeBezier: function (plotAreaWidth, plotAreaCenter) {
		var p0;
		var p3;
		var top = 0, bottom = this.viewportHeight();
		top = this.outlineThickness() / 2;
		bottom -= top;
		if (this.isInverted()) {
			p0 = { __x: plotAreaCenter - (plotAreaWidth * this.bottomEdgeWidth() / 2), __y: top, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			p3 = { __x: plotAreaCenter - (plotAreaWidth / 2), __y: bottom, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			p0 = { __x: plotAreaCenter - (plotAreaWidth / 2), __y: top, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			p3 = { __x: plotAreaCenter - (plotAreaWidth * this.bottomEdgeWidth() / 2), __y: bottom, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		var upperBezier = this.upperBezierControlPoint().__y < this.lowerBezierControlPoint().__y ? this.upperBezierControlPoint() : this.lowerBezierControlPoint();
		var lowerBezier = this.lowerBezierControlPoint().__y > this.upperBezierControlPoint().__y ? this.lowerBezierControlPoint() : this.upperBezierControlPoint();
		if (upperBezier.__y < 0) {
			upperBezier.__y = 0;
		}
		if (lowerBezier.__y > 1) {
			lowerBezier.__y = 1;
		}
		if (this.isInverted()) {
			var swap = lowerBezier.__x;
			lowerBezier.__x = upperBezier.__x;
			upperBezier.__x = swap;
			swap = upperBezier.__y;
			upperBezier.__y = 1 - lowerBezier.__y;
			lowerBezier.__y = 1 - swap;
		}
		var p1 = { __x: plotAreaWidth * upperBezier.__x, __y: this.viewportHeight() * upperBezier.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var p2 = { __x: plotAreaWidth * lowerBezier.__x, __y: this.viewportHeight() * lowerBezier.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.bezier() == null || this.pointDiffers(p0, this.bezier().p0()) || this.pointDiffers(p1, this.bezier().p1()) || this.pointDiffers(p2, this.bezier().p2()) || this.pointDiffers(p3, this.bezier().p3())) {
			this.bezier(new $.ig.Bezier(p0, p1, p2, p3, 2, plotAreaCenter));
		}
		if (this.bezier() == null) {
			this.__effectiveUseBezierCurve = false;
		} else {
			this.__effectiveUseBezierCurve = this.bezier().valid();
		}
	}
	,
	getOuterLabel: function (index) {
		var c = this.outerLabelColumn();
		var list = c == null ? null : c.values();
		return list == null || index >= list.count() ? null : list.item(index);
	}
	,
	getInnerLabel: function (index) {
		var c = this.innerLabelColumn();
		var list = c == null ? null : c.values();
		return list == null || index >= list.count() ? null : list.item(index);
	}
	,
	getSliceStyle: function (index) {
		if (this.sliceSelectionManager().isSelected(index)) {
			return this.selectedSliceStyle();
		}
		if (this.sliceSelectionManager().isUnselected(index) && this.useUnselectedStyle()) {
			return this.unselectedSliceStyle();
		}
		return null;
	}
	,
	getSliceOutline: function (index) {
		return this.outlines().item(index % this.outlines().count());
	}
	,
	getSliceFill: function (index) {
		return this.brushes().item(index % this.brushes().count());
	}
	,
	getWidthAt: function (plotAreaWidth, currentTop) {
		var bottomWidth = plotAreaWidth * this.bottomEdgeWidth();
		if (this.__effectiveUseBezierCurve) {
			var x = this.bezier().getPointAt(currentTop)._point.__x;
			return ((plotAreaWidth / 2) - x) * 2;
		} else {
			if (this.isInverted()) {
				return plotAreaWidth - ((plotAreaWidth - bottomWidth) * ((this.viewportHeight() - currentTop) / this.viewportHeight()));
			} else {
				return plotAreaWidth - ((plotAreaWidth - bottomWidth) * (currentTop / this.viewportHeight()));
			}
		}
	}
	,
	getSortedIndices: function () {
		var $self = this;
		var column = new $.ig.IntColumn();
		column.populate(this.valueColumn().values().count());
		column.sort(function (i1, i2) {
			if ($self.valueColumn().values().item(i1) < $self.valueColumn().values().item(i2)) {
				return $self.isInverted() ? -1 : 1;
			}
			if ($self.valueColumn().values().item(i1) > $self.valueColumn().values().item(i2)) {
				return $self.isInverted() ? 1 : -1;
			}
			return 0;
		});
		return column;
	}
	,
	displayFrame: function (frame) {
		this.sendClearMessage("LeftLabels");
		this.sendClearMessage("RightLabels");
		this.sendClearMessage("SliceArea");
		if (frame.outerAlignedLeft()) {
			this.updatePanelWidth("LeftPanel", frame.outerLabelWidth());
			this.updatePanelWidth("RightPanel", 0);
		} else {
			this.updatePanelWidth("LeftPanel", 0);
			this.updatePanelWidth("RightPanel", frame.outerLabelWidth());
		}
		this.slices(frame.slices());
		var en = frame.slices().getEnumerator();
		while (en.moveNext()) {
			var info = en.current();
			if (info.hasSlice()) {
				var m = new $.ig.RenderSliceMessage();
				m.areaID("SliceArea");
				m.slice(info.slice());
				m._outlineThickness = this.outlineThickness();
				this.renderingMessages().sendMessage(m);
			}
		}
		var en1 = frame.slices().getEnumerator();
		while (en1.moveNext()) {
			var info1 = en1.current();
			if (info1.hasOuterLabel()) {
				var m1 = new $.ig.RenderOuterLabelMessage();
				if (frame.outerAlignedLeft()) {
					m1.areaID("LeftLabels");
				} else {
					m1.areaID("RightLabels");
				}
				m1.label(info1.outerLabel());
				m1.position(info1.outerLabelPosition());
				m1.outerLabelWidth(frame.outerLabelWidth());
				m1.sliceInfo(info1);
				this.renderingMessages().sendMessage(m1);
			}
		}
		var frc = new $.ig.FrameRenderCompleteMessage();
		this.renderingMessages().sendMessage(frc);
	}
	,
	updatePanelWidth: function (panelName, width) {
		var sasm = new $.ig.SetAreaSizeMessage();
		sasm.areaID(panelName);
		sasm.settingWidth(true);
		sasm.width(width);
		this.renderingMessages().sendMessage(sasm);
	}
	,
	shouldAnimate: function () {
		return this.transitionDuration() > 0;
	}
	,
	sendClearMessage: function (areaID) {
		var cm = new $.ig.ClearMessage();
		cm.areaID(areaID);
		this.renderingMessages().sendMessage(cm);
	}
	,
	getOuterLabels: function () {
		return this.outerLabelColumn();
	}
	,
	dataUpdatedMessageReceived: function (m) {
		this.refreshRequired(true);
	}
	,
	convertToInt: function (obj) {
		return $.ig.Convert.prototype.toInt32(obj);
	}
	,
	propertyChangedMessageReceived: function (m) {
		var pm = m;
		var val = pm.newValue();
		switch (pm.propertyName()) {
			case "TextStyle":
				this.textStyle(val != null ? val.toString() : "");
				var msg = new $.ig.PropertyChangedMessage();
				msg.propertyName(pm.propertyName());
				msg.oldValue(pm.oldValue());
				msg.newValue(val);
				this.renderingMessages().sendMessage(msg);
				this.refreshRequired(true);
				break;
			case "OuterLabelTextStyle":
				this.outerLabelTextStyle(val != null ? val.toString() : "");
				var msg1 = new $.ig.PropertyChangedMessage();
				msg1.propertyName(pm.propertyName());
				msg1.oldValue(pm.oldValue());
				msg1.newValue(val);
				this.renderingMessages().sendMessage(msg1);
				this.refreshRequired(true);
				break;
			case "ValueColumn":
				this.valueColumn().setValues(val);
				this.refreshRequired(true);
				break;
			case "InnerLabelColumn":
				this._innerLabelsFormatted = false;
				this._innerLabels = val;
				this.innerLabelColumn().setValues(val);
				this.refreshRequired(true);
				break;
			case "InnerLabelVisibility":
				this.innerLabelVisibility(val);
				this.refreshRequired(true);
				break;
			case "OuterLabelVisibility":
				this.outerLabelVisibility(val);
				this.refreshRequired(true);
				break;
			case "OuterLabelColumn":
				this._outerLabelsFormatted = false;
				this._outerLabels = val;
				this.outerLabelColumn().setValues(val);
				this.refreshRequired(true);
				break;
			case "ValueMemberPath":
				this.valueMemberPath($.ig.util.cast(String, val));
				break;
			case "OuterLabelMemberPath":
				this.outerLabelMemberPath($.ig.util.cast(String, val));
				break;
			case "InnerLabelMemberPath":
				this.innerLabelMemberPath($.ig.util.cast(String, val));
				break;
			case "BottomEdgeWidth":
				this.bottomEdgeWidth(val);
				if (this.bottomEdgeWidth() > 1) {
					this.bottomEdgeWidth(1);
				}
				if (this.bottomEdgeWidth() < 0.001) {
					this.bottomEdgeWidth(0.001);
				}
				this.refreshRequired(true);
				break;
			case "Brushes":
				this.brushes($.ig.util.cast($.ig.BrushCollection.prototype.$type, val));
				this.refreshRequired(true);
				break;
			case "Outlines":
				this.outlines($.ig.util.cast($.ig.BrushCollection.prototype.$type, val));
				this.refreshRequired(true);
				break;
			case "OuterLabelAlignment":
				this.outerLabelAlignment($.ig.util.getEnumValue(val));
				this.refreshRequired(true);
				break;
			case "FunnelSliceDisplay":
				this.funnelSliceDisplay($.ig.util.getEnumValue(val));
				this.refreshRequired(true);
				break;
			case "FormatInnerLabel":
				this.innerLabelColumn().setValues(this._innerLabels);
				this._innerLabelsFormatted = false;
				this._formatInnerLabel = val;
				this.refreshRequired(true);
				break;
			case "FormatOuterLabel":
				this.outerLabelColumn().setValues(this._outerLabels);
				this._formatOuterLabel = val;
				this._outerLabelsFormatted = false;
				this.refreshRequired(true);
				break;
			case "IsInverted":
				this.isInverted(val);
				this.refreshRequired(true);
				break;
			case "TransitionDuration":
				this.doubleAnimator().intervalMilliseconds(this.convertToInt(val));
				this.transitionDuration(this.doubleAnimator().intervalMilliseconds());
				break;
			case "AllowSliceSelection":
				this.allowSliceSelection(val);
				break;
			case "SelectedSliceStyle":
				this.selectedSliceStyle(val);
				this.refreshRequired(true);
				break;
			case "UnselectedSliceStyle":
				this.unselectedSliceStyle(val);
				this.refreshRequired(true);
				break;
			case "UseUnselectedStyle":
				this.useUnselectedStyle(val);
				this.refreshRequired(true);
				break;
			case "UseBezierCurve":
				this.useBezierCurve(val);
				this.refreshRequired(true);
				break;
			case "UpperBezierControlPoint":
				this.upperBezierControlPoint(val);
				this.refreshRequired(true);
				break;
			case "LowerBezierControlPoint":
				this.lowerBezierControlPoint(val);
				this.refreshRequired(true);
				break;
			case "ItemProvider":
				this.itemProvider(val);
				break;
			case "ToolTip":
				var tvm = new $.ig.TooltipValueChangedMessage();
				if (val != null) {
					this.hasTooltip(true);
				} else {
					this.hasTooltip(false);
					var ctm = new $.ig.ClearTooltipMessage();
					this.renderingMessages().sendMessage(ctm);
				}
				tvm.value(val);
				this.renderingMessages().sendMessage(tvm);
				break;
			case "Legend":
				this.onLegendChanged(val);
				this.refreshRequired(true);
				break;
			case "LegendItemTemplate":
				this.legendItemTemplate(val);
				this.refreshRequired(true);
				break;
			case "LegendItemBadgeTemplate":
				this.legendItemBadgeTemplate(val);
				this.refreshRequired(true);
				break;
			case "UseOuterLabelsForLegend":
				this.userOuterLabelsForLegend(val);
				this.refreshRequired(true);
				break;
			case "OutlineThickness":
				this.__outlineThickness = val;
				this.refreshRequired(true);
				this.renderingMessages().sendMessage(pm);
				break;
			case "Container":
				var vpm = new $.ig.PropertyChangedMessage();
				vpm.propertyName(pm.propertyName());
				vpm.oldValue(pm.oldValue());
				vpm.newValue(val);
				this.renderingMessages().sendMessage(vpm);
				break;
		}
	}
	,
	_legend: null,
	legend: function (value) {
		if (arguments.length === 1) {
			this._legend = value;
			return value;
		} else {
			return this._legend;
		}
	}
	,
	__legendItemTemplate: null,
	legendItemTemplate: function (value) {
		if (arguments.length === 1) {
			this.__legendItemTemplate = value;
			return value;
		} else {
			return this.__legendItemTemplate;
		}
	}
	,
	__legendItemBadgeTemplate: null,
	legendItemBadgeTemplate: function (value) {
		if (arguments.length === 1) {
			this.__legendItemBadgeTemplate = value;
			return value;
		} else {
			return this.__legendItemBadgeTemplate;
		}
	}
	,
	onLegendChanged: function (p) {
		if (this.legend() != null) {
			this.legend().clearLegendItems(this.serviceProvider().getService("Model"));
		}
		this.legend(p);
	}
	,
	renderLegend: function (frame) {
		var $self = this;
		var legendItems = new $.ig.List$1($.ig.UIElement.prototype.$type, 0);
		var en = frame.slices().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			var item = new $.ig.ContentControl();
			var itemLabel = "";
			if (slice.slice().innerLabel() != null) {
				if (this.userOuterLabelsForLegend() && slice.outerLabel() != null) {
					itemLabel = slice.outerLabel().toString();
				} else {
					itemLabel = slice.slice().innerLabel().toString();
				}
			}
			var itemBrush = null;
			var itemOutline = null;
			if (slice.slice().fill() != null) {
				itemBrush = slice.slice().fill();
			}
			if (slice.slice().outline() != null) {
				itemOutline = slice.slice().outline();
			}
			var dataItem = null;
			if (this.itemProvider() != null) {
				dataItem = this.itemProvider().getItem(slice.slice().index());
			}
			item.content((function () {
				var $ret = new $.ig.FunnelSliceDataContext();
				$ret.series($self.serviceProvider().getService("Model"));
				$ret.item(dataItem);
				$ret.itemBrush(itemBrush);
				$ret.itemLabel(itemLabel);
				$ret.thickness($self.outlineThickness());
				$ret.itemOutline(itemOutline);
				return $ret;
			}()));
			item.contentTemplate(this.legendItemTemplate());
			legendItems.add(item);
		}
		if (this.legend() != null) {
			this.legend().createLegendItems(legendItems, this.serviceProvider().getService("Model"));
		}
	}
	,
	viewPropertyChangedMessageReceived: function (m) {
		var pm = m;
		switch (pm.propertyName()) {
			case "OuterLabelWidthDecider":
				this.widthDecider($.ig.util.cast($.ig.IOuterLabelWidthDecider.prototype.$type, pm.newValue()));
				this.refreshRequired(true);
				break;
			case "FunnelLabelSizeDecider":
				this.sizeDecider($.ig.util.cast($.ig.IFunnelLabelSizeDecider.prototype.$type, pm.newValue()));
				this.refreshRequired(true);
				break;
		}
	}
	,
	viewportChangedMessageReceived: function (m) {
		var vm = m;
		this.viewportWidth(vm.newWidth());
		this.viewportHeight(vm.newHeight());
		this.refreshRequired(true);
	}
	,
	mouseButtonMessageReceived: function (m) {
		var mbm = m;
		if (mbm.type() == $.ig.MouseButtonType.prototype.right) {
			return;
		}
		if (mbm.action() == $.ig.MouseButtonAction.prototype.down) {
			this.pressedSliceIndex(this.hoveredSliceIndex());
		} else if (mbm.action() == $.ig.MouseButtonAction.prototype.up) {
			var pressed = this.pressedSliceIndex();
			this.pressedSliceIndex(-1);
			if (mbm.modifiers() == $.ig.ModifierKeys.prototype.none) {
			} else if (mbm.modifiers() != $.ig.ModifierKeys.prototype.control) {
				return;
			}
			if (pressed == this.hoveredSliceIndex()) {
				this.onSliceClicked(pressed);
			}
		}
	}
	,
	onSliceClicked: function (index) {
		if (index < 0 || index > this.valueColumn().values().count() - 1) {
			return;
		}
		if (this.allowSliceSelection()) {
			this.sliceSelectionManager().toggleSelection(index, this.valueColumn().values().item(index));
			var selectedItems = this.sliceSelectionManager().getSelectedItems();
			var m = new $.ig.SelectedItemsChangedMessage();
			m.selectedItems(selectedItems);
			this.modelUpdateMessages().sendMessage(m);
			this.refreshRequired(true);
		}
		var scm = new $.ig.SliceClickedMessage();
		scm.index(index);
		scm.item(this.valueColumn().values().item(index));
		this.modelUpdateMessages().sendMessage(scm);
	}
	,
	mouseMoveMessageReceived: function (m) {
		var mm = m;
		this.hoveredSliceIndex(this.getHoveredSliceIndex(mm.position()));
		if (this.hasTooltip()) {
			this.updateToolTip(mm.position());
		}
	}
	,
	mouseLeaveMessageReceived: function (m) {
		var ctm = new $.ig.ClearTooltipMessage();
		this.renderingMessages().sendMessage(ctm);
	}
	,
	updateToolTip: function (position) {
		var context = new $.ig.FunnelDataContext();
		if (this.itemProvider() != null && this.hoveredSliceIndex() >= 0 && this.hoveredSliceIndex() < this.itemProvider().count()) {
			context.item(this.itemProvider().getItem(this.hoveredSliceIndex()));
		}
		context.index(this.hoveredSliceIndex());
		if (context.index() < 0) {
			var cm = new $.ig.ClearTooltipMessage();
			this.renderingMessages().sendMessage(cm);
			return;
		}
		var tm = new $.ig.TooltipUpdateMessage();
		tm.context(context);
		tm.position(this.getTooltipPosition(position, context));
		this.renderingMessages().sendMessage(tm);
	}
	,
	getTooltipPosition: function (position, context) {
		var placement = { __x: position.__x + 10, __y: position.__y + 10, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return placement;
	}
	,
	getHoveredSliceIndex: function (point) {
		if (!this.isValid()) {
			return -1;
		}
		var yVal = point.__y;
		var xVal = point.__x;
		var currentFrame = this.getCurrentFunnelFrame();
		if (currentFrame.outerAlignedLeft()) {
			xVal -= currentFrame.outerLabelWidth();
		}
		var slice = this.getSliceByYValue(yVal);
		if (slice == null) {
			return -1;
		}
		var plotAreaWidth = this.getPlotAreaWidth();
		var width = this.getWidthAt(plotAreaWidth, yVal);
		var halfWidth = width / 2;
		var center = plotAreaWidth / 2;
		if (xVal >= (center - halfWidth) && xVal <= (center + halfWidth)) {
			return slice.index();
		} else {
			return -1;
		}
	}
	,
	getPlotAreaWidth: function () {
		var current = this.getCurrentFunnelFrame();
		return this.viewportWidth() - current.outerLabelWidth();
	}
	,
	getSliceByYValue: function (yVal) {
		var current = this.getCurrentFunnelFrame();
		var index = current.slices().getByYValue(yVal);
		if (index >= 0 && index < current.slices().count()) {
			return current.slices().__inner[index];
		}
		return null;
	}
	,
	getCurrentFunnelFrame: function () {
		var current = null;
		if (this.animationInProgress()) {
			current = this.interpolatedFrame();
		} else {
			current = this.currentFrame();
		}
		return current;
	}
	,
	userSelectedItemsChangedMessageReceived: function (m) {
		var um = m;
		this.sliceSelectionManager().setSelectedItems(um.selectedItems(), this.valueColumn());
		this.refreshRequired(true);
	}
	,
	labelSizeChangedMessageReceived: function (m) {
		this.checkLabelSizes(m);
	}
	,
	checkLabelSizes: function (m) {
		var current = this.getCurrentFunnelFrame();
		if (m.sliceIndex() == -1) {
			this.refreshRequired(true);
		} else {
			var si = current.slices().__inner[m.sliceIndex()];
			if (m.isOuter()) {
				if (m.newSize().width() > current.outerLabelWidth()) {
					this.refreshRequired(true);
				}
				if (m.newSize().height() != m.oldSize().height()) {
					this.refreshRequired(true);
				}
				if (m.newSize().height() > si.slice().lowerRight().__y - si.slice().upperRight().__y) {
					this.refreshRequired(true);
				}
			} else {
				if (m.newSize().height() > si.slice().lowerRight().__y - si.slice().upperRight().__y) {
					this.refreshRequired(true);
				}
			}
		}
	}
	,
	$type: new $.ig.Type('XamFunnelController', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Message', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	toString: function () {
		return this.getType().toString();
	}
	,
	$type: new $.ig.Type('Message', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('InteractionMessage', 'Message', {
	init: function () {
		$.ig.Message.prototype.init.call(this);
	},
	$type: new $.ig.Type('InteractionMessage', $.ig.Message.prototype.$type)
}, true);

$.ig.util.defType('LabelSizeChangedMessage', 'InteractionMessage', {
	init: function () {
		this._newSize = new $.ig.Size();
		this._oldSize = new $.ig.Size();
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	_sliceIndex: 0,
	sliceIndex: function (value) {
		if (arguments.length === 1) {
			this._sliceIndex = value;
			return value;
		} else {
			return this._sliceIndex;
		}
	}
	,
	_newSize: null,
	newSize: function (value) {
		if (arguments.length === 1) {
			this._newSize = value;
			return value;
		} else {
			return this._newSize;
		}
	}
	,
	_oldSize: null,
	oldSize: function (value) {
		if (arguments.length === 1) {
			this._oldSize = value;
			return value;
		} else {
			return this._oldSize;
		}
	}
	,
	_isOuter: false,
	isOuter: function (value) {
		if (arguments.length === 1) {
			this._isOuter = value;
			return value;
		} else {
			return this._isOuter;
		}
	}
	,
	$type: new $.ig.Type('LabelSizeChangedMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('RenderingMessage', 'Message', {
	init: function () {
		$.ig.Message.prototype.init.call(this);
	},
	__areaId: null,
	areaID: function (value) {
		if (arguments.length === 1) {
			this.__areaId = value;
			return value;
		} else {
			return this.__areaId;
		}
	}
	,
	__refreshRequired: false,
	refreshRequired: function (value) {
		if (arguments.length === 1) {
			this.__refreshRequired = value;
			return value;
		} else {
			return this.__refreshRequired;
		}
	}
	,
	$type: new $.ig.Type('RenderingMessage', $.ig.Message.prototype.$type)
}, true);

$.ig.util.defType('RenderOuterLabelMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	__label: null,
	label: function (value) {
		if (arguments.length === 1) {
			this.__label = value;
			return value;
		} else {
			return this.__label;
		}
	}
	,
	__position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	__outerLabelWidth: 0,
	outerLabelWidth: function (value) {
		if (arguments.length === 1) {
			this.__outerLabelWidth = value;
			return value;
		} else {
			return this.__outerLabelWidth;
		}
	}
	,
	toString: function () {
		return "RenderOuterLabelMessage[" + this.label().toString() + ", " + this.position().toString() + ", " + this.outerLabelWidth().toString() + "]";
	}
	,
	_sliceInfo: null,
	sliceInfo: function (value) {
		if (arguments.length === 1) {
			this._sliceInfo = value;
			return value;
		} else {
			return this._sliceInfo;
		}
	}
	,
	$type: new $.ig.Type('RenderOuterLabelMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('RenderSliceMessage', 'RenderingMessage', {
	init: function () {
		this._outlineThickness = 1;
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	__slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this.__slice = value;
			return value;
		} else {
			return this.__slice;
		}
	}
	,
	_outlineThickness: 0,
	toString: function () {
		return "RenderSliceMessage[" + this.slice().toString() + "]";
	}
	,
	$type: new $.ig.Type('RenderSliceMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('ModelUpdateMessage', 'Message', {
	init: function () {
		$.ig.Message.prototype.init.call(this);
	},
	$type: new $.ig.Type('ModelUpdateMessage', $.ig.Message.prototype.$type)
}, true);

$.ig.util.defType('SelectedItemsChangedMessage', 'ModelUpdateMessage', {
	init: function () {
		$.ig.ModelUpdateMessage.prototype.init.call(this);
	},
	__selectedItems: null,
	selectedItems: function (value) {
		if (arguments.length === 1) {
			this.__selectedItems = value;
			return value;
		} else {
			return this.__selectedItems;
		}
	}
	,
	toString: function () {
		var ret = "SelectedItemsChangedMessage[";
		var $t = this.selectedItems();
		for (var i = 0; i < $t.length; i++) {
			var item = $t[i];
			ret += item.toString();
		}
		ret += "]";
		return ret;
	}
	,
	$type: new $.ig.Type('SelectedItemsChangedMessage', $.ig.ModelUpdateMessage.prototype.$type)
}, true);

$.ig.util.defType('SliceClickedMessage', 'ModelUpdateMessage', {
	init: function () {
		$.ig.ModelUpdateMessage.prototype.init.call(this);
	},
	__index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this.__index = value;
			return value;
		} else {
			return this.__index;
		}
	}
	,
	__item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this.__item = value;
			return value;
		} else {
			return this.__item;
		}
	}
	,
	toString: function () {
		return "SliceClickedMessage[" + this.index().toString() + ", " + this.item().toString() + "]";
	}
	,
	$type: new $.ig.Type('SliceClickedMessage', $.ig.ModelUpdateMessage.prototype.$type)
}, true);

$.ig.util.defType('ConfigurationMessage', 'Message', {
	init: function () {
		$.ig.Message.prototype.init.call(this);
	},
	$type: new $.ig.Type('ConfigurationMessage', $.ig.Message.prototype.$type)
}, true);

$.ig.util.defType('UserSelectedItemsChangedMessage', 'ConfigurationMessage', {
	init: function () {
		$.ig.ConfigurationMessage.prototype.init.call(this);
	},
	__selectedItems: null,
	selectedItems: function (value) {
		if (arguments.length === 1) {
			this.__selectedItems = value;
			return value;
		} else {
			return this.__selectedItems;
		}
	}
	,
	toString: function () {
		var ret = "UserSelectedItemsChanged[";
		var $t = this.selectedItems();
		for (var i = 0; i < $t.length; i++) {
			var item = $t[i];
			ret += item.toString();
		}
		ret += "]";
		return ret;
	}
	,
	$type: new $.ig.Type('UserSelectedItemsChangedMessage', $.ig.ConfigurationMessage.prototype.$type)
}, true);

$.ig.util.defType('FunnelSliceClickedEventArgs', 'EventArgs', {
	init: function () {
		$.ig.EventArgs.prototype.init.call(this);
	},
	__index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this.__index = value;
			return value;
		} else {
			return this.__index;
		}
	}
	,
	__item: null,
	item: function (value) {
		if (arguments.length === 1) {
			this.__item = value;
			return value;
		} else {
			return this.__item;
		}
	}
	,
	$type: new $.ig.Type('FunnelSliceClickedEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('XamFunnelChart', 'Control', {
	init: function () {
		var $self = this;
		this.__selectedItems = new $.ig.ObservableCollection$1($.ig.Object.prototype.$type, 0);
		this.__selectedItemsDict = new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.Object.prototype.$type, 0);
		this.__container = null;
		$.ig.Control.prototype.init.call(this);
		var $t = this.__selectedItems;
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, this.selectedItems_CollectionChanged.runOn(this));
		this.messageHandler(new $.ig.MessageHandler());
		this.messageHandler().addHandler($.ig.SliceClickedMessage.prototype.$type, this.sliceClickedMessageReceived.runOn(this));
		this.messageHandler().addHandler($.ig.SelectedItemsChangedMessage.prototype.$type, this.selectedItemsChangedMessageReceived.runOn(this));
		this.configurationMessages(new $.ig.MessageChannel());
		this._fastItemsSource_Event = function (o, e) {
			$self.dataUpdated(e.action(), e.position(), e.count(), e.propertyName());
		};
		this.sendDefaults();
		this.defaultStyleKey($.ig.XamFunnelChart.prototype.$type);
		this.connector(new $.ig.XamFunnelConnector(new $.ig.XamFunnelView(), this));
		this.legendItemTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.funnelLegendItemRender);
			$ret.measure($.ig.LegendTemplates.prototype.defaultLegendItemMeasure);
			return $ret;
		}()));
		this.legendItemBadgeTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.funnelBadgeTemplate);
			$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
			return $ret;
		}()));
	},
	__messageHandler: null,
	messageHandler: function (value) {
		if (arguments.length === 1) {
			this.__messageHandler = value;
			return value;
		} else {
			return this.__messageHandler;
		}
	}
	,
	sendDefaults: function () {
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.bottomEdgeWidthPropertyName, 0, this.bottomEdgeWidth());
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.innerLabelVisibilityPropertyName, $.ig.Visibility.prototype.getBox(this.innerLabelVisibility()), $.ig.Visibility.prototype.getBox(this.innerLabelVisibility()));
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelAlignmentPropertyName, $.ig.OuterLabelAlignment.prototype.getBox(this.outerLabelAlignment()), $.ig.OuterLabelAlignment.prototype.getBox(this.outerLabelAlignment()));
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.funnelSliceDisplayPropertyName, $.ig.FunnelSliceDisplay.prototype.getBox(this.funnelSliceDisplay()), $.ig.FunnelSliceDisplay.prototype.getBox(this.funnelSliceDisplay()));
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.upperBezierControlPointPropertyName, this.upperBezierControlPoint(), this.upperBezierControlPoint());
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.lowerBezierControlPointPropertyName, this.lowerBezierControlPoint(), this.lowerBezierControlPoint());
		this.sendItemProvider();
	}
	,
	sendItemProvider: function () {
		var pm = new $.ig.PropertyChangedMessage();
		pm.propertyName("ItemProvider");
		pm.oldValue(null);
		pm.newValue(this);
		this.configurationMessages().sendMessage(pm);
	}
	,
	__serviceProvider: null,
	serviceProvider: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__serviceProvider;
			this.__serviceProvider = value;
			this.onServiceProviderChanged(oldValue, this.__serviceProvider);
			return value;
		} else {
			return this.__serviceProvider;
		}
	}
	,
	_configurationMessages: null,
	configurationMessages: function (value) {
		if (arguments.length === 1) {
			this._configurationMessages = value;
			return value;
		} else {
			return this._configurationMessages;
		}
	}
	,
	_connector: null,
	connector: function (value) {
		if (arguments.length === 1) {
			this._connector = value;
			return value;
		} else {
			return this._connector;
		}
	}
	,
	onServiceProviderChanged: function (oldValue, newValue) {
		if (oldValue != null) {
			var channel = $.ig.util.cast($.ig.MessageChannel.prototype.$type, oldValue.getService("ModelUpdateMessages"));
			if (channel != null) {
				channel.detachTarget(this.messageReceived.runOn(this));
			}
			this.configurationMessages().detachFromNext();
		}
		if (newValue != null) {
			var channel1 = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("ModelUpdateMessages"));
			if (channel1 != null) {
				channel1.attachTarget(this.messageReceived.runOn(this));
			}
			var rendering = $.ig.util.cast($.ig.MessageChannel.prototype.$type, newValue.getService("ConfigurationMessages"));
			this.configurationMessages().connectTo(rendering);
		}
	}
	,
	messageReceived: function (m) {
		this.messageHandler().messageReceived(m);
	}
	,
	itemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.itemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.itemsSourceProperty);
		}
	}
	,
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.fastItemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.fastItemsSourceProperty);
		}
	}
	,
	_fastItemsSource_Event: null,
	dataUpdated: function (action, position, count, propertyName) {
		var m = (function () {
			var $ret = new $.ig.DataUpdatedMessage();
			$ret.position(position);
			$ret.count(count);
			$ret.propertyName(propertyName);
			return $ret;
		}());
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.change:
				m.action($.ig.ItemsSourceAction.prototype.change);
				break;
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				m.action($.ig.ItemsSourceAction.prototype.insert);
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				m.action($.ig.ItemsSourceAction.prototype.remove);
				break;
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				m.action($.ig.ItemsSourceAction.prototype.replace);
				break;
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				m.action($.ig.ItemsSourceAction.prototype.reset);
				break;
		}
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.valueColumnPropertyName, this.valueColumn(), this.valueColumn());
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.innerLabelColumnPropertyName, this.innerLabelColumn(), this.innerLabelColumn());
		this.onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelColumnPropertyName, this.outerLabelColumn(), this.outerLabelColumn());
		this.configurationMessages().sendMessage(m);
	}
	,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.valueMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.valueMemberPathProperty);
		}
	}
	,
	valueColumn: function (value) {
		if (arguments.length === 1) {
			if (this._valueColumn != value) {
				var oldValueColumn = this._valueColumn;
				this._valueColumn = value;
				this.onPropertyChanged($.ig.XamFunnelChart.prototype.valueColumnPropertyName, oldValueColumn, this._valueColumn);
			}
			return value;
		} else {
			return this._valueColumn;
		}
	}
	,
	_valueColumn: null,
	brushes: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.brushesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.brushesProperty);
		}
	}
	,
	outlines: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.outlinesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.outlinesProperty);
		}
	}
	,
	bottomEdgeWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.bottomEdgeWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.bottomEdgeWidthProperty);
		}
	}
	,
	innerLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.innerLabelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.innerLabelMemberPathProperty);
		}
	}
	,
	innerLabelColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__innerLabelColumn != value) {
				var oldInnerLabelColumn = this.__innerLabelColumn;
				this.__innerLabelColumn = value;
				this.onPropertyChanged($.ig.XamFunnelChart.prototype.innerLabelColumnPropertyName, oldInnerLabelColumn, this.innerLabelColumn());
			}
			return value;
		} else {
			return this.__innerLabelColumn;
		}
	}
	,
	__innerLabelColumn: null,
	outerLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.outerLabelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.outerLabelMemberPathProperty);
		}
	}
	,
	outerLabelColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__outerLabelColumn != value) {
				var oldOuterLabelColumn = this.__outerLabelColumn;
				this.__outerLabelColumn = value;
				this.onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelColumnPropertyName, oldOuterLabelColumn, this.outerLabelColumn());
			}
			return value;
		} else {
			return this.__outerLabelColumn;
		}
	}
	,
	__outerLabelColumn: null,
	innerLabelVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.innerLabelVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamFunnelChart.prototype.innerLabelVisibilityProperty));
		}
	}
	,
	outerLabelVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.outerLabelVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamFunnelChart.prototype.outerLabelVisibilityProperty));
		}
	}
	,
	outerLabelAlignment: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.outerLabelAlignmentProperty, $.ig.OuterLabelAlignment.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamFunnelChart.prototype.outerLabelAlignmentProperty));
		}
	}
	,
	funnelSliceDisplay: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.funnelSliceDisplayProperty, $.ig.FunnelSliceDisplay.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamFunnelChart.prototype.funnelSliceDisplayProperty));
		}
	}
	,
	__formatInnerLabel: null,
	formatInnerLabel: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__formatInnerLabel;
			this.__formatInnerLabel = value;
			this.onPropertyChanged($.ig.XamFunnelChart.prototype.formatInnerLabelPropertyName, oldValue, value);
			return value;
		} else {
			return this.__formatInnerLabel;
		}
	}
	,
	__formatOuterLabel: null,
	formatOuterLabel: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__formatOuterLabel;
			this.__formatOuterLabel = value;
			this.onPropertyChanged($.ig.XamFunnelChart.prototype.formatOuterLabelPropertyName, oldValue, value);
			return value;
		} else {
			return this.__formatOuterLabel;
		}
	}
	,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.transitionDurationProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.XamFunnelChart.prototype.transitionDurationProperty));
		}
	}
	,
	isInverted: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.isInvertedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.isInvertedProperty);
		}
	}
	,
	upperBezierControlPoint: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.upperBezierControlPointProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.upperBezierControlPointProperty);
		}
	}
	,
	lowerBezierControlPoint: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.lowerBezierControlPointProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.lowerBezierControlPointProperty);
		}
	}
	,
	useBezierCurve: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.useBezierCurveProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.useBezierCurveProperty);
		}
	}
	,
	allowSliceSelection: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.allowSliceSelectionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.allowSliceSelectionProperty);
		}
	}
	,
	useUnselectedStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.useUnselectedStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.useUnselectedStyleProperty);
		}
	}
	,
	selectedSliceStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.selectedSliceStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.selectedSliceStyleProperty);
		}
	}
	,
	unselectedSliceStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.unselectedSliceStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.unselectedSliceStyleProperty);
		}
	}
	,
	toolTip: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.toolTipProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.toolTipProperty);
		}
	}
	,
	__selectedItems: null,
	__selectedItemsDict: null,
	selectedItems: function () {
		return this.__selectedItems;
	}
	,
	legend: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.legendProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.legendProperty);
		}
	}
	,
	legendItemTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.legendItemTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.legendItemTemplateProperty);
		}
	}
	,
	legendItemBadgeTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.legendItemBadgeTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.legendItemBadgeTemplateProperty);
		}
	}
	,
	useOuterLabelsForLegend: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.useOuterLabelsForLegendProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.useOuterLabelsForLegendProperty);
		}
	}
	,
	__textStyle: null,
	textStyle: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__textStyle;
			this.__textStyle = value;
			this.onPropertyChanged($.ig.XamFunnelChart.prototype.textStylePropertyName, oldValue, value);
			return value;
		} else {
			return this.__textStyle;
		}
	}
	,
	__outerLabelTextStyle: null,
	outerLabelTextStyle: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__outerLabelTextStyle;
			this.__outerLabelTextStyle = value;
			this.onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelTextStylePropertyName, oldValue, value);
			return value;
		} else {
			return this.__outerLabelTextStyle;
		}
	}
	,
	onPropertyChanged: function (propertyName, oldValue, newValue) {
		if (this.handleItemsSourceChanges(propertyName, oldValue, newValue) || this.handleBrushChanges(propertyName, oldValue, newValue) || this.handleVisibilityChanges(propertyName, oldValue, newValue) || this.handleTimespans(propertyName, oldValue, newValue)) {
			return;
		}
		this.configurationMessages().sendMessage((function () {
			var $ret = new $.ig.PropertyChangedMessage();
			$ret.propertyName(propertyName);
			$ret.oldValue(oldValue);
			$ret.newValue(newValue);
			return $ret;
		}()));
	}
	,
	handleBrushChanges: function (propertyName, oldValue, newValue) {
		if (propertyName == $.ig.XamFunnelChart.prototype.brushesPropertyName || propertyName == $.ig.XamFunnelChart.prototype.outlinesPropertyName) {
			this.configurationMessages().sendMessage((function () {
				var $ret = new $.ig.PropertyChangedMessage();
				$ret.propertyName(propertyName);
				$ret.oldValue(oldValue == null ? null : (oldValue));
				$ret.newValue(newValue == null ? null : (newValue));
				return $ret;
			}()));
			return true;
		}
		return false;
	}
	,
	handleItemsSourceChanges: function (propertyName, oldValue, newValue) {
		var $self = this;
		if (propertyName == $.ig.XamFunnelChart.prototype.itemsSourcePropertyName) {
			if (oldValue != null) {
				this.fastItemsSource(null);
			}
			if (newValue != null) {
				this.fastItemsSource((function () {
					var $ret = new $.ig.FastItemsSource();
					$ret.itemsSource($self.itemsSource());
					return $ret;
				}()));
			}
			return true;
		} else if (propertyName == $.ig.XamFunnelChart.prototype.fastItemsSourcePropertyName) {
			if (oldValue != null) {
				var $t = (oldValue);
				$t.event = $.ig.Delegate.prototype.remove($t.event, this._fastItemsSource_Event);
			}
			if (newValue != null) {
				var $t1 = (newValue);
				$t1.event = $.ig.Delegate.prototype.combine($t1.event, this._fastItemsSource_Event);
			}
			this.handleValueMemberPathChange(propertyName, oldValue, newValue);
			return true;
		} else if (propertyName == $.ig.XamFunnelChart.prototype.valueMemberPathPropertyName || propertyName == $.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName || propertyName == $.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName) {
			this.handleValueMemberPathChange(propertyName, oldValue, newValue);
			return false;
		}
		return false;
	}
	,
	registerDoubleColumn: function (itemsSource, columnName) {
		return itemsSource.registerColumn(columnName, null, false);
	}
	,
	registerObjectColumn: function (itemsSource, columnName) {
		return itemsSource.registerColumnObject(columnName, null, false);
	}
	,
	handleValueMemberPathChange: function (propertyName, oldValue, newValue) {
		if (this.fastItemsSource() == null) {
			var oldSource = $.ig.util.cast($.ig.FastItemsSource.prototype.$type, oldValue);
			if (oldSource != null) {
				if (this.valueColumn() != null) {
					oldSource.deregisterColumn(this.valueColumn());
				}
				if (this.innerLabelColumn() != null) {
					oldSource.deregisterColumn(this.innerLabelColumn());
				}
				if (this.outerLabelColumn() != null) {
					oldSource.deregisterColumn(this.outerLabelColumn());
				}
				this.valueColumn(null);
				this.innerLabelColumn(null);
				this.outerLabelColumn(null);
			}
			return;
		}
		if (oldValue != null && typeof oldValue === 'string') {
			switch (propertyName) {
				case $.ig.XamFunnelChart.prototype.valueMemberPathPropertyName:
					this.fastItemsSource().deregisterColumn(this.valueColumn());
					this.valueColumn(null);
					break;
				case $.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName:
					this.fastItemsSource().deregisterColumn(this.innerLabelColumn());
					this.innerLabelColumn(null);
					break;
				case $.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName:
					this.fastItemsSource().deregisterColumn(this.outerLabelColumn());
					this.outerLabelColumn(null);
					break;
			}
		}
		if (newValue != null && $.ig.util.cast($.ig.FastItemsSource.prototype.$type, newValue) !== null) {
			if (this.valueMemberPath() != null) {
				this.valueColumn(this.registerDoubleColumn(newValue, this.valueMemberPath()));
			}
			if (this.innerLabelMemberPath() != null) {
				this.innerLabelColumn(this.registerObjectColumn(newValue, this.innerLabelMemberPath()));
			}
			if (this.outerLabelMemberPath() != null) {
				this.outerLabelColumn(this.registerObjectColumn(newValue, this.outerLabelMemberPath()));
			}
		}
		if (newValue != null && typeof newValue === 'string') {
			switch (propertyName) {
				case $.ig.XamFunnelChart.prototype.valueMemberPathPropertyName:
					this.valueColumn(this.registerDoubleColumn(this.fastItemsSource(), this.valueMemberPath()));
					break;
				case $.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName:
					this.innerLabelColumn(this.registerObjectColumn(this.fastItemsSource(), this.innerLabelMemberPath()));
					break;
				case $.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName:
					this.outerLabelColumn(this.registerObjectColumn(this.fastItemsSource(), this.outerLabelMemberPath()));
					break;
			}
		}
	}
	,
	handleVisibilityChanges: function (propertyName, oldValue, newValue) {
		if (propertyName == $.ig.XamFunnelChart.prototype.innerLabelVisibilityPropertyName || propertyName == $.ig.XamFunnelChart.prototype.outerLabelVisibilityPropertyName) {
			var old = $.ig.util.getEnumValue(oldValue) == $.ig.Visibility.prototype.visible;
			var newVal = $.ig.util.getEnumValue(newValue) == $.ig.Visibility.prototype.visible;
			this.configurationMessages().sendMessage((function () {
				var $ret = new $.ig.PropertyChangedMessage();
				$ret.propertyName(propertyName);
				$ret.newValue(newVal);
				$ret.oldValue(old);
				return $ret;
			}()));
			return true;
		}
		return false;
	}
	,
	handleTimespans: function (propertyName, oldValue, newValue) {
		if (propertyName == $.ig.XamFunnelChart.prototype.transitionDurationPropertyName) {
			var pm = new $.ig.PropertyChangedMessage();
			pm.propertyName(propertyName);
			if (oldValue != null) {
				pm.oldValue(oldValue);
			}
			if (newValue != null) {
				pm.newValue(newValue);
			}
			this.configurationMessages().sendMessage(pm);
			return true;
		}
		return false;
	}
	,
	sliceClickedMessageReceived: function (m) {
		var scm = m;
		if (this.sliceClicked != null) {
			var args = new $.ig.FunnelSliceClickedEventArgs();
			args.index(scm.index());
			if (this.fastItemsSource().count() > scm.index() && scm.index() >= 0) {
				args.item(this.fastItemsSource().item(scm.index()));
			}
			this.sliceClicked(this, args);
		}
	}
	,
	sliceClicked: null,
	selectedItemsChangedMessageReceived: function (m) {
	}
	,
	selectedItems_CollectionChanged: function (sender, e) {
	}
	,
	mergeItems: function (listA, dictA, listB, dictB) {
		var toRemove = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var en = listA.getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			if (!dictB.containsKey(item)) {
				toRemove.add1(item);
			}
		}
		var en1 = toRemove.getEnumerator();
		while (en1.moveNext()) {
			var item1 = en1.current();
			listA.remove(item1);
			dictA.remove(item1);
		}
		var en2 = listB.getEnumerator();
		while (en2.moveNext()) {
			var item2 = en2.current();
			if (!dictA.containsKey(item2)) {
				listA.add(item2);
				dictA.add(item2, item2);
			}
		}
	}
	,
	getItem: function (index) {
		return this.fastItemsSource().item(index);
	}
	,
	count: function () {
		return this.fastItemsSource() != null ? this.fastItemsSource().count() : 0;
	}
	,
	__container: null,
	provideContainer: function (container) {
		var oldContainer = this.__container;
		this.__container = container;
		this.onPropertyChanged("Container", oldContainer, this.__container);
	}
	,
	notifyResized: function () {
		this.configurationMessages().sendMessage(new $.ig.ContainerSizeChangedMessage());
	}
	,
	notifySetItem: function (source_, index, oldItem, newItem) {
		this.notifyData(source_, new $.ig.NotifyCollectionChangedEventArgs(2, $.ig.NotifyCollectionChangedAction.prototype.replace, newItem, oldItem, index));
	}
	,
	notifyClearItems: function (source_) {
		this.notifyData(source_, new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset));
	}
	,
	notifyInsertItem: function (source_, index, newItem) {
		this.notifyData(source_, new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, newItem, index));
	}
	,
	notifyRemoveItem: function (source_, index, oldItem) {
		this.notifyData(source_, new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.remove, oldItem, index));
	}
	,
	notifyData: function (s_, args) {
		if (s_.dataView && s_.dataSource) { s_ = s_.dataView(); };
		if (s_ == this.itemsSource()) {
			var itemsSource = this.fastItemsSource();
			if (itemsSource != null) {
				itemsSource.handleCollectionChanged(args);
			}
		}
	}
	,
	outlineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamFunnelChart.prototype.outlineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamFunnelChart.prototype.outlineThicknessProperty);
		}
	}
	,
	cntrl: function () {
		var con = this.connector();
		return con == null ? null : con.controller();
	}
	,
	ssm: function () {
		var con = this.cntrl();
		return con == null ? null : con.sliceSelectionManager();
	}
	,
	selectedIndexes: function (value) {
		if (arguments.length === 1) {
			var ssm = this.ssm();
			if (ssm == null) {
				return;
			}
			var contr = this.cntrl();
			ssm.clearSelection();
			var i = value == null ? 0 : value.length;
			while (i-- > 0) {
				ssm.toggleSelection(value[i], contr.valueColumn().values().item(value[i]));
			}
			contr.refreshRequired(true);
			contr.refreshVisuals();
			return value;
		} else {
			var ssm = this.ssm();
			return ssm == null ? null : ssm.getSelectedItems();
		}
	}
	,
	toggleSelection: function (index) {
		var ssm = this.ssm();
		if (ssm == null) {
			return;
		}
		var contr = this.cntrl();
		var item = contr.valueColumn().values().item(index);
		if (item != null) {
			contr.sliceSelectionManager().toggleSelection(index, item);
			contr.refreshRequired(true);
			contr.refreshVisuals();
		}
	}
	,
	exportVisualData: function () {
		var chart = new $.ig.FunnelChartVisualData();
		var slices = this.connector().controller().slices();
		if (slices != null) {
			for (var i = 0; i < slices.count(); i++) {
				var slice = slices.__inner[i].exportVisualData();
				chart.slices().add(slice);
			}
		}
		chart.opacity(this.__opacity);
		chart.isInverted(this.isInverted());
		chart.name(this.name());
		chart.viewport(new $.ig.Rect(0, 0, 0, this.connector().controller().viewportWidth(), this.connector().controller().viewportHeight()));
		return chart;
	}
	,
	$type: new $.ig.Type('XamFunnelChart', $.ig.Control.prototype.$type, [$.ig.IItemProvider.prototype.$type])
}, true);

$.ig.util.defType('XamFunnelConnector', 'Object', {
	__view: null,
	__controller: null,
	controller: function () {
		return this.__controller;
	}
	,
	__model: null,
	init: function (view, model) {
		$.ig.Object.prototype.init.call(this);
		var provider = new $.ig.ServiceProvider();
		provider.addService("ConfigurationMessages", new $.ig.MessageChannel());
		provider.addService("RenderingMessages", new $.ig.MessageChannel());
		provider.addService("InteractionMessages", new $.ig.MessageChannel());
		provider.addService("ModelUpdateMessages", new $.ig.MessageChannel());
		var cont = new $.ig.XamFunnelController();
		cont._funnelView = view;
		cont.serviceProvider(provider);
		view.serviceProvider(provider);
		this.__controller = cont;
		this.__view = view;
		model.serviceProvider(provider);
		this.__model = model;
		provider.addService("Model", this.__model);
	},
	reconnectView: function (view) {
		this.__view = view;
		var provider = this.__controller.serviceProvider();
		this.__controller.serviceProvider(null);
		this.__controller.serviceProvider(provider);
		this.__view.serviceProvider(provider);
	}
	,
	disconnectView: function () {
		if (this.__view != null) {
			this.__view.serviceProvider(null);
		}
	}
	,
	$type: new $.ig.Type('XamFunnelConnector', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ClearMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	$type: new $.ig.Type('ClearMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('ClearTooltipMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	$type: new $.ig.Type('ClearTooltipMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('DataUpdatedMessage', 'ConfigurationMessage', {
	init: function () {
		$.ig.ConfigurationMessage.prototype.init.call(this);
	},
	__action: 0,
	action: function (value) {
		if (arguments.length === 1) {
			this.__action = value;
			return value;
		} else {
			return this.__action;
		}
	}
	,
	__position: 0,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	__count: 0,
	count: function (value) {
		if (arguments.length === 1) {
			this.__count = value;
			return value;
		} else {
			return this.__count;
		}
	}
	,
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	toString: function () {
		return "DataUpdatedMessage[" + $.ig.ItemsSourceAction.prototype.$getName(this.action()) + "," + this.position().toString() + ", " + this.count().toString() + ", " + this.propertyName().toString() + "]";
	}
	,
	$type: new $.ig.Type('DataUpdatedMessage', $.ig.ConfigurationMessage.prototype.$type)
}, true);

$.ig.util.defType('FrameRenderCompleteMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	$type: new $.ig.Type('FrameRenderCompleteMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('MessageChannel', 'Object', {
	init: function () {
		this.__sendQueue = new $.ig.Queue$1($.ig.Message.prototype.$type);
		$.ig.Object.prototype.init.call(this);
	},
	__sendQueue: null,
	sendMessage: function (message) {
		if (this.messageSent != null) {
			this.messageSent(message);
		} else {
			this.__sendQueue.enqueue(message);
		}
	}
	,
	attachTarget: function (m) {
		this.messageSent = $.ig.Delegate.prototype.combine(this.messageSent, m);
		while (this.__sendQueue.count() > 0) {
			var mess = this.__sendQueue.dequeue();
			this.messageSent(mess);
		}
	}
	,
	messageSent: null,
	detachTarget: function (m) {
		this.messageSent = $.ig.Delegate.prototype.remove(this.messageSent, m);
	}
	,
	__connectedTo: null,
	connectTo: function (m) {
		this.__connectedTo = m;
		this.attachTarget(this.sendToNext.runOn(this));
	}
	,
	detachFromNext: function () {
		if (this.__connectedTo == null) {
			return;
		}
		this.detachTarget(this.sendToNext.runOn(this));
		this.__connectedTo = null;
	}
	,
	sendToNext: function (m) {
		if (this.__connectedTo != null) {
			this.__connectedTo.sendMessage(m);
		}
	}
	,
	toString: function () {
		return "MessageQueue";
	}
	,
	$type: new $.ig.Type('MessageChannel', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MessageHandler', 'Object', {
	init: function () {
		this.__dictionary = new $.ig.Dictionary$2($.ig.Type.prototype.$type, $.ig.MessageHandlerEventHandler.prototype.$type, 0);
		$.ig.Object.prototype.init.call(this);
	},
	__dictionary: null,
	addHandler: function (messageType, handler) {
		this.__dictionary.add(messageType, handler);
	}
	,
	messageReceived: function (m) {
		var $self = this;
		var handler;
		if ((function () { var $ret = $self.__dictionary.tryGetValue(m.getType(), handler); handler = $ret.p1; return $ret.ret; }())) {
			handler(m);
		}
	}
	,
	$type: new $.ig.Type('MessageHandler', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MouseButtonMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	__action: 0,
	action: function (value) {
		if (arguments.length === 1) {
			this.__action = value;
			return value;
		} else {
			return this.__action;
		}
	}
	,
	__type: 0,
	type: function (value) {
		if (arguments.length === 1) {
			this.__type = value;
			return value;
		} else {
			return this.__type;
		}
	}
	,
	__position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	toString: function () {
		return "MouseButtonMessage[" + $.ig.MouseButtonAction.prototype.$getName(this.action()) + ", " + $.ig.MouseButtonType.prototype.$getName(this.type()) + ", " + this.position().toString() + "]";
	}
	,
	_modifiers: 0,
	modifiers: function (value) {
		if (arguments.length === 1) {
			this._modifiers = value;
			return value;
		} else {
			return this._modifiers;
		}
	}
	,
	$type: new $.ig.Type('MouseButtonMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('MouseLeaveMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	__position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	toString: function () {
		return "MouseLeaveMessage[" + this.position().toString() + "]";
	}
	,
	$type: new $.ig.Type('MouseLeaveMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('MouseMoveMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	__position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	toString: function () {
		return "MouseMoveMessage[" + this.position().toString() + "]";
	}
	,
	$type: new $.ig.Type('MouseMoveMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('PropertyChangedMessage', 'ConfigurationMessage', {
	init: function () {
		$.ig.ConfigurationMessage.prototype.init.call(this);
	},
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	__oldValue: null,
	oldValue: function (value) {
		if (arguments.length === 1) {
			this.__oldValue = value;
			return value;
		} else {
			return this.__oldValue;
		}
	}
	,
	__newValue: null,
	newValue: function (value) {
		if (arguments.length === 1) {
			this.__newValue = value;
			return value;
		} else {
			return this.__newValue;
		}
	}
	,
	toString: function () {
		var oldValue = "null";
		var newVAlue = "null";
		if (this.oldValue() != null) {
			oldValue = this.oldValue().toString();
		}
		if (this.newValue() != null) {
			newVAlue = this.newValue().toString();
		}
		return "PropertyChangedMessage[" + this.propertyName().toString() + ", " + oldValue + ", " + newVAlue + "]";
	}
	,
	$type: new $.ig.Type('PropertyChangedMessage', $.ig.ConfigurationMessage.prototype.$type)
}, true);

$.ig.util.defType('ServiceProvider', 'Object', {
	init: function () {
		this.__dict = new $.ig.Dictionary(0);
		$.ig.Object.prototype.init.call(this);
	},
	__dict: null,
	addService: function (key, service) {
		this.__dict.item(key, service);
	}
	,
	getService: function (key) {
		return this.__dict.item(key);
	}
	,
	$type: new $.ig.Type('ServiceProvider', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SetAreaSizeMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	__settingHeight: false,
	settingHeight: function (value) {
		if (arguments.length === 1) {
			this.__settingHeight = value;
			return value;
		} else {
			return this.__settingHeight;
		}
	}
	,
	__height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this.__height = value;
			return value;
		} else {
			return this.__height;
		}
	}
	,
	__settingWidth: false,
	settingWidth: function (value) {
		if (arguments.length === 1) {
			this.__settingWidth = value;
			return value;
		} else {
			return this.__settingWidth;
		}
	}
	,
	__width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this.__width = value;
			return value;
		} else {
			return this.__width;
		}
	}
	,
	toString: function () {
		return "SetAreaSizeMessage[" + this.settingHeight().toString() + ", " + this.height().toString() + ", " + this.settingWidth().toString() + ", " + this.width().toString() + "]";
	}
	,
	$type: new $.ig.Type('SetAreaSizeMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('TooltipUpdateMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	__position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this.__position = value;
			return value;
		} else {
			return this.__position;
		}
	}
	,
	__context: null,
	context: function (value) {
		if (arguments.length === 1) {
			this.__context = value;
			return value;
		} else {
			return this.__context;
		}
	}
	,
	toString: function () {
		return "TooltipUpdateMessage[" + this.position().toString() + ", " + this.context().toString() + "]";
	}
	,
	$type: new $.ig.Type('TooltipUpdateMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('TooltipValueChangedMessage', 'RenderingMessage', {
	init: function () {
		$.ig.RenderingMessage.prototype.init.call(this);
	},
	__value: null,
	value: function (value) {
		if (arguments.length === 1) {
			this.__value = value;
			return value;
		} else {
			return this.__value;
		}
	}
	,
	toString: function () {
		return "TooltipValueChangedMessage[" + this.value().toString() + "]";
	}
	,
	$type: new $.ig.Type('TooltipValueChangedMessage', $.ig.RenderingMessage.prototype.$type)
}, true);

$.ig.util.defType('ViewportChangedMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	__newWidth: 0,
	newWidth: function (value) {
		if (arguments.length === 1) {
			this.__newWidth = value;
			return value;
		} else {
			return this.__newWidth;
		}
	}
	,
	__newHeight: 0,
	newHeight: function (value) {
		if (arguments.length === 1) {
			this.__newHeight = value;
			return value;
		} else {
			return this.__newHeight;
		}
	}
	,
	toString: function () {
		return "ViewportChangedMessage[" + this.newWidth().toString() + ", " + this.newHeight().toString() + "]";
	}
	,
	$type: new $.ig.Type('ViewportChangedMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('ContainerSizeChangedMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	$type: new $.ig.Type('ContainerSizeChangedMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.util.defType('ViewPropertyChangedMessage', 'InteractionMessage', {
	init: function () {
		$.ig.InteractionMessage.prototype.init.call(this);
	},
	__propertyName: null,
	propertyName: function (value) {
		if (arguments.length === 1) {
			this.__propertyName = value;
			return value;
		} else {
			return this.__propertyName;
		}
	}
	,
	__oldValue: null,
	oldValue: function (value) {
		if (arguments.length === 1) {
			this.__oldValue = value;
			return value;
		} else {
			return this.__oldValue;
		}
	}
	,
	__newValue: null,
	newValue: function (value) {
		if (arguments.length === 1) {
			this.__newValue = value;
			return value;
		} else {
			return this.__newValue;
		}
	}
	,
	toString: function () {
		return "ViewPropertyChangedMessage[" + this.propertyName().toString() + ", " + this.oldValue().toString() + ", " + this.newValue().toString() + "]";
	}
	,
	$type: new $.ig.Type('ViewPropertyChangedMessage', $.ig.InteractionMessage.prototype.$type)
}, true);

$.ig.MouseButtonAction.prototype.up = 0;
$.ig.MouseButtonAction.prototype.down = 1;

$.ig.MouseButtonType.prototype.left = 0;
$.ig.MouseButtonType.prototype.right = 1;

$.ig.OuterLabelAlignment.prototype.left = 0;
$.ig.OuterLabelAlignment.prototype.right = 1;

$.ig.FunnelSliceDisplay.prototype.uniform = 0;
$.ig.FunnelSliceDisplay.prototype.weighted = 1;

$.ig.OthersCategoryType.prototype.number = 0;
$.ig.OthersCategoryType.prototype.percent = 1;

$.ig.LabelsPosition.prototype.none = 0;
$.ig.LabelsPosition.prototype.center = 1;
$.ig.LabelsPosition.prototype.insideEnd = 2;
$.ig.LabelsPosition.prototype.outsideEnd = 3;
$.ig.LabelsPosition.prototype.bestFit = 4;

$.ig.LeaderLineType.prototype.straight = 0;
$.ig.LeaderLineType.prototype.arc = 1;
$.ig.LeaderLineType.prototype.spline = 2;

$.ig.ItemsSourceAction.prototype.remove = 0;
$.ig.ItemsSourceAction.prototype.insert = 1;
$.ig.ItemsSourceAction.prototype.replace = 2;
$.ig.ItemsSourceAction.prototype.change = 3;
$.ig.ItemsSourceAction.prototype.reset = 4;

$.ig.PieChartBaseView.prototype.tEXT_MARGIN = 0;

$.ig.XamFunnelView.prototype._defFills = [ "#7000ff", "#70ff00", "#00f0f0", "#ff0070", "#ff00ff" ];
$.ig.XamFunnelView.prototype._defLines = [ "#7000dd", "#70dd00", "#00d0d0", "#dd0070", "#dd00dd" ];

$.ig.PieChartBase.prototype._contentPresenterName = "ContentPresenter";
$.ig.PieChartBase.prototype.fastItemsSourcePropertyName = "FastItemsSource";
$.ig.PieChartBase.prototype.valueColumnPropertyName = "ValueColumn";
$.ig.PieChartBase.prototype.leaderLinePathControlPointOffset = 30;
$.ig.PieChartBase.prototype.itemsSourcePropertyName = "ItemsSource";
$.ig.PieChartBase.prototype.innerExtentPropertyName = "InnerExtent";
$.ig.PieChartBase.prototype.valueMemberPathPropertyName = "ValueMemberPath";
$.ig.PieChartBase.prototype.labelMemberPathPropertyName = "LabelMemberPath";
$.ig.PieChartBase.prototype.legendLabelMemberPathPropertyName = "LegendLabelMemberPath";
$.ig.PieChartBase.prototype.labelColumnPropertyName = "LabelColumn";
$.ig.PieChartBase.prototype.legendLabelColumnPropertyName = "LegendLabelColumn";
$.ig.PieChartBase.prototype.labelsPositionPropertyName = "LabelsPosition";
$.ig.PieChartBase.prototype.leaderLineVisibilityPropertyName = "LeaderLineVisibility";
$.ig.PieChartBase.prototype.leaderLineStylePropertyName = "LeaderLineStyle";
$.ig.PieChartBase.prototype.leaderLineTypePropertyName = "LeaderLineType";
$.ig.PieChartBase.prototype.leaderLineMarginPropertyName = "LeaderLineMargin";
$.ig.PieChartBase.prototype.toolTipPropertyName = "ToolTip";
$.ig.PieChartBase.prototype.othersCategoryThresholdPropertyName = "OthersCategoryThreshold";
$.ig.PieChartBase.prototype.othersCategoryTypePropertyName = "OthersCategoryType";
$.ig.PieChartBase.prototype.othersCategoryTextPropertyName = "OthersCategoryText";
$.ig.PieChartBase.prototype.explodedRadiusPropertyName = "ExplodedRadius";
$.ig.PieChartBase.prototype.radiusFactorPropertyName = "RadiusFactor";
$.ig.PieChartBase.prototype.allowSliceSelectionPropertyName = "AllowSliceSelection";
$.ig.PieChartBase.prototype.allowSliceExplosionPropertyName = "AllowSliceExplosion";
$.ig.PieChartBase.prototype.legendPropertyName = "Legend";
$.ig.PieChartBase.prototype.labelExtentPropertyName = "LabelExtent";
$.ig.PieChartBase.prototype.startAnglePropertyName = "StartAngle";
$.ig.PieChartBase.prototype.sweepDirectionPropertyName = "SweepDirection";
$.ig.PieChartBase.prototype.othersCategoryStylePropertyName = "OthersCategoryStyle";
$.ig.PieChartBase.prototype.selectedStylePropertyName = "SelectedStyle";
$.ig.PieChartBase.prototype.toolTipStylePropertyName = "ToolTipStyle";
$.ig.PieChartBase.prototype.brushesPropertyName = "Brushes";
$.ig.PieChartBase.prototype.outlinesPropertyName = "Outlines";
$.ig.PieChartBase.prototype.legendItemTemplatePropertyName = "LegendItemTemplate";
$.ig.PieChartBase.prototype.legendItemBadgeTemplatePropertyName = "LegendItemBadgeTemplate";
$.ig.PieChartBase.prototype._labelTemplatePropertyName = "LabelTemplate";
$.ig.PieChartBase.prototype.isSurfaceInteractionDisabledPropertyName = "IsSurfaceInteractionDisabled";
$.ig.PieChartBase.prototype.formatLabelPropertyName = "FormatLabel";
$.ig.PieChartBase.prototype.formatLegendLabelPropertyName = "FormatLegendLabel";
$.ig.PieChartBase.prototype.textStylePropertyName = "TextStyle";
$.ig.PieChartBase.prototype.fastItemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.fastItemsSourcePropertyName, $.ig.FastItemsSource.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.fastItemsSourcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.itemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.itemsSourcePropertyName, $.ig.IEnumerable.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.itemsSourcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.innerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.innerExtentPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.innerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.valueMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.valueMemberPathPropertyName, String, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.valueMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.labelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.labelMemberPathPropertyName, String, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.labelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.legendLabelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.legendLabelMemberPathPropertyName, String, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	($.ig.util.cast($.ig.PieChartBase.prototype.$type, o)).raisePropertyChanged($.ig.PieChartBase.prototype.legendLabelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.labelsPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.labelsPositionPropertyName, $.ig.LabelsPosition.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LabelsPosition.prototype.getBox($.ig.LabelsPosition.prototype.center), function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.labelsPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.leaderLineVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.leaderLineVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.leaderLineVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.leaderLineStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.leaderLineStylePropertyName, $.ig.Style.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.leaderLineStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.leaderLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.leaderLineTypePropertyName, $.ig.LeaderLineType.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LeaderLineType.prototype.getBox($.ig.LeaderLineType.prototype.straight), function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.leaderLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.leaderLineMarginProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.leaderLineMarginPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 6, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.leaderLineMarginPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.toolTipProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.toolTipPropertyName, $.ig.Object.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.toolTipPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.othersCategoryThresholdProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.othersCategoryThresholdPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 3, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.othersCategoryThresholdPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.othersCategoryTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.othersCategoryTypePropertyName, $.ig.OthersCategoryType.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.OthersCategoryType.prototype.getBox($.ig.OthersCategoryType.prototype.percent), function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.othersCategoryTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.othersCategoryTextProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.othersCategoryTextPropertyName, String, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, "Others", function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.othersCategoryTextPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.explodedRadiusProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.explodedRadiusPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 0.2, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.explodedRadiusPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.radiusFactorProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.radiusFactorPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 0.9, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.radiusFactorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.allowSliceSelectionProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.allowSliceSelectionPropertyName, $.ig.Boolean.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, true, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.allowSliceSelectionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.allowSliceExplosionProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.allowSliceExplosionPropertyName, $.ig.Boolean.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, true, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.allowSliceExplosionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.legendProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.legendPropertyName, $.ig.LegendBase.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.legendPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.labelExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.labelExtentPropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.labelExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.startAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.startAnglePropertyName, Number, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.startAnglePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.sweepDirectionProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.sweepDirectionPropertyName, $.ig.SweepDirection.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.SweepDirection.prototype.getBox($.ig.SweepDirection.prototype.clockwise), function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.sweepDirectionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.othersCategoryStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.othersCategoryStylePropertyName, $.ig.Style.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.othersCategoryStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.selectedStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.selectedStylePropertyName, $.ig.Style.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.selectedStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.toolTipStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.toolTipStylePropertyName, $.ig.Style.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).raisePropertyChanged($.ig.PieChartBase.prototype.toolTipStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.brushesProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.brushesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.brushesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.outlinesProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.outlinesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.outlinesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.legendItemTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.legendItemTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.legendItemTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.legendItemBadgeTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.legendItemBadgeTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.legendItemBadgeTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.labelTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype._labelTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype._labelTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.isSurfaceInteractionDisabledProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.isSurfaceInteractionDisabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.isSurfaceInteractionDisabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.formatLabelProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.formatLabelPropertyName, $.ig.PieChartFormatLabelHandler.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PieChartBase.prototype.formatLabelPropertyName, e.oldValue(), e.newValue());
}));
$.ig.PieChartBase.prototype.formatLegendLabelProperty = $.ig.DependencyProperty.prototype.register($.ig.PieChartBase.prototype.formatLegendLabelPropertyName, $.ig.PieChartFormatLabelHandler.prototype.$type, $.ig.PieChartBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	($.ig.util.cast($.ig.PieChartBase.prototype.$type, sender)).raisePropertyChanged($.ig.PieChartBase.prototype.formatLegendLabelPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Slice.prototype.startAnglePropertyName = "StartAngle";
$.ig.Slice.prototype.endAnglePropertyName = "EndAngle";
$.ig.Slice.prototype.innerExtentStartPropertyName = "InnerExtentStart";
$.ig.Slice.prototype.innerExtentEndPropertyName = "InnerExtentEnd";
$.ig.Slice.prototype.isSelectedPropertyName = "IsSelected";
$.ig.Slice.prototype.isExplodedPropertyName = "IsExploded";
$.ig.Slice.prototype.isOtherSlicePropertyName = "IsOtherSlice";
$.ig.Slice.prototype.originPropertyName = "Origin";
$.ig.Slice.prototype.explodedOriginPropertyName = "ExplodedOrigin";
$.ig.Slice.prototype.radiusPropertyName = "Radius";
$.ig.Slice.prototype.explodedRadiusPropertyName = "ExplodedRadius";
$.ig.Slice.prototype.indexPropertyName = "Index";
$.ig.Slice.prototype.strokeThicknessPropertyName = "StrokeThickness";
$.ig.Slice.prototype.startAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.startAnglePropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.endAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.endAnglePropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.innerExtentStartProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.innerExtentStartPropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.innerExtentEndProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.innerExtentEndPropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.isSelectedProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.isSelectedPropertyName, $.ig.Boolean.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	var slice = $.ig.util.cast($.ig.Slice.prototype.$type, o);
	var shouldSelect = e.newValue();
	if (slice.owner() != null) {
		slice.owner().selectSlice(slice, shouldSelect);
	}
}));
$.ig.Slice.prototype.isExplodedProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.isExplodedPropertyName, $.ig.Boolean.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	var slice = $.ig.util.cast($.ig.Slice.prototype.$type, o);
	var explode = e.newValue();
	if (slice.owner() != null) {
		slice.owner().explodeSlice(slice, explode);
	}
}));
$.ig.Slice.prototype.isOtherSliceProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.isOtherSlicePropertyName, $.ig.Boolean.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.originProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.originPropertyName, $.ig.Point.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, new $.ig.Point(0), function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.explodedOriginProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.explodedOriginPropertyName, $.ig.Point.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, new $.ig.Point(0), function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.radiusProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.radiusPropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.explodedRadiusProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.explodedRadiusPropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.indexProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.indexPropertyName, $.ig.Number.prototype.$type, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, -1, function (o, e) { (o).createShape(); }));
$.ig.Slice.prototype.strokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Slice.prototype.strokeThicknessPropertyName, Number, $.ig.Slice.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) { (o).createShape(); }));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

$.ig.XamFunnelChart.prototype.itemsSourcePropertyName = "ItemsSource";
$.ig.XamFunnelChart.prototype.fastItemsSourcePropertyName = "FastItemsSource";
$.ig.XamFunnelChart.prototype.valueMemberPathPropertyName = "ValueMemberPath";
$.ig.XamFunnelChart.prototype.valueColumnPropertyName = "ValueColumn";
$.ig.XamFunnelChart.prototype.brushesPropertyName = "Brushes";
$.ig.XamFunnelChart.prototype.outlinesPropertyName = "Outlines";
$.ig.XamFunnelChart.prototype.bottomEdgeWidthPropertyName = "BottomEdgeWidth";
$.ig.XamFunnelChart.prototype.bottomEdgeWidth_Default = 0.35;
$.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName = "InnerLabelMemberPath";
$.ig.XamFunnelChart.prototype.innerLabelColumnPropertyName = "InnerLabelColumn";
$.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName = "OuterLabelMemberPath";
$.ig.XamFunnelChart.prototype.outerLabelColumnPropertyName = "OuterLabelColumn";
$.ig.XamFunnelChart.prototype.innerLabelVisibilityPropertyName = "InnerLabelVisibility";
$.ig.XamFunnelChart.prototype.outerLabelVisibilityPropertyName = "OuterLabelVisibility";
$.ig.XamFunnelChart.prototype.outerLabelAlignmentPropertyName = "OuterLabelAlignment";
$.ig.XamFunnelChart.prototype.funnelSliceDisplayPropertyName = "FunnelSliceDisplay";
$.ig.XamFunnelChart.prototype.formatInnerLabelPropertyName = "FormatInnerLabel";
$.ig.XamFunnelChart.prototype.formatOuterLabelPropertyName = "FormatOuterLabel";
$.ig.XamFunnelChart.prototype.transitionDurationPropertyName = "TransitionDuration";
$.ig.XamFunnelChart.prototype.isInvertedPropertyName = "IsInverted";
$.ig.XamFunnelChart.prototype.upperBezierControlPointPropertyName = "UpperBezierControlPoint";
$.ig.XamFunnelChart.prototype.lowerBezierControlPointPropertyName = "LowerBezierControlPoint";
$.ig.XamFunnelChart.prototype.useBezierCurvePropertyName = "UseBezierCurve";
$.ig.XamFunnelChart.prototype.allowSliceSelectionPropertyName = "AllowSliceSelection";
$.ig.XamFunnelChart.prototype.useUnselectedStylePropertyName = "UseUnselectedStyle";
$.ig.XamFunnelChart.prototype.selectedSliceStylePropertyName = "SelectedSliceStyle";
$.ig.XamFunnelChart.prototype.unselectedSliceStylePropertyName = "UnselectedSliceStyle";
$.ig.XamFunnelChart.prototype.toolTipPropertyName = "ToolTip";
$.ig.XamFunnelChart.prototype.legendPropertyName = "Legend";
$.ig.XamFunnelChart.prototype.legendItemTemplatePropertyName = "LegendItemTemplate";
$.ig.XamFunnelChart.prototype.legendItemBadgeTemplatePropertyName = "LegendItemBadgeTemplate";
$.ig.XamFunnelChart.prototype.useOuterLabelsForLegendPropertyName = "UseOuterLabelsForLegend";
$.ig.XamFunnelChart.prototype.textStylePropertyName = "TextStyle";
$.ig.XamFunnelChart.prototype.outerLabelTextStylePropertyName = "OuterLabelTextStyle";
$.ig.XamFunnelChart.prototype.outlineThicknessPropertyName = "OutlineThickness";
$.ig.XamFunnelChart.prototype.outlineThickness_Default = -1;
$.ig.XamFunnelChart.prototype.itemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.itemsSourcePropertyName, $.ig.IEnumerable.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.itemsSourcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.fastItemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.fastItemsSourcePropertyName, $.ig.FastItemsSource.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.fastItemsSourcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.valueMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.valueMemberPathPropertyName, String, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.valueMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.brushesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.brushesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).onPropertyChanged($.ig.XamFunnelChart.prototype.brushesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.outlinesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.outlinesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).onPropertyChanged($.ig.XamFunnelChart.prototype.outlinesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.bottomEdgeWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.bottomEdgeWidthPropertyName, Number, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamFunnelChart.prototype.bottomEdgeWidth_Default, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.bottomEdgeWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.innerLabelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName, String, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.innerLabelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.outerLabelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName, String, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.innerLabelVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.innerLabelVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.innerLabelVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.outerLabelVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.outerLabelVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.collapsed), function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.outerLabelAlignmentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.outerLabelAlignmentPropertyName, $.ig.OuterLabelAlignment.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.OuterLabelAlignment.prototype.getBox($.ig.OuterLabelAlignment.prototype.left), function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.outerLabelAlignmentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.funnelSliceDisplayProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.funnelSliceDisplayPropertyName, $.ig.FunnelSliceDisplay.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.FunnelSliceDisplay.prototype.getBox($.ig.FunnelSliceDisplay.prototype.uniform), function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.funnelSliceDisplayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.transitionDurationProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.transitionDurationPropertyName, $.ig.Number.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.transitionDurationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.isInvertedProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.isInvertedPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.isInvertedPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.upperBezierControlPointProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.upperBezierControlPointPropertyName, $.ig.Point.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0.5, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.upperBezierControlPointPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.lowerBezierControlPointProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.lowerBezierControlPointPropertyName, $.ig.Point.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, { __x: 0.5, __y: 1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.lowerBezierControlPointPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.useBezierCurveProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.useBezierCurvePropertyName, $.ig.Boolean.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.useBezierCurvePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.allowSliceSelectionProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.allowSliceSelectionPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.allowSliceSelectionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.useUnselectedStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.useUnselectedStylePropertyName, $.ig.Boolean.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.useUnselectedStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.selectedSliceStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.selectedSliceStylePropertyName, $.ig.Style.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.selectedSliceStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.unselectedSliceStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.unselectedSliceStylePropertyName, $.ig.Style.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.unselectedSliceStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.toolTipProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.toolTipPropertyName, $.ig.Object.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.toolTipPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.legendProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.legendPropertyName, $.ig.ItemLegend.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.legendPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.legendItemTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.legendItemTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.legendItemTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.legendItemBadgeTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.legendItemBadgeTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.legendItemBadgeTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.useOuterLabelsForLegendProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.useOuterLabelsForLegendPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.useOuterLabelsForLegendPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamFunnelChart.prototype.outlineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamFunnelChart.prototype.outlineThicknessPropertyName, Number, $.ig.XamFunnelChart.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamFunnelChart.prototype.outlineThickness_Default, function (o, e) {
	(o).onPropertyChanged($.ig.XamFunnelChart.prototype.outlineThicknessPropertyName, e.oldValue(), e.newValue());
}));

} (jQuery));


