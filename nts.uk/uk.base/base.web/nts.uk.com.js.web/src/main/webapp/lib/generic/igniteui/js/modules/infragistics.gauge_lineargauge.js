/*!@license
* Infragistics.Web.ClientUI infragistics.gauge_lineargauge.js 16.1.20161.2145
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
"Dictionary_EnumerableCollection$3:ch", 
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
"MouseEventArgs:g3"]);


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
"StackPool$1:ia", 
"Func$1:ib", 
"StringBuilder:ih", 
"Environment:ii", 
"DoubleValueChangedEventArgs:ir", 
"DoubleValueChangedEventHandler:is", 
"InterpolationUtil:jj", 
"Func$5:jk", 
"PolygonUtil:jo", 
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
"PathVisualData:ko", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


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

$.ig.util.defType('DoubleValueChangedEventArgs', 'EventArgs', {
	init: function (oldValue, newValue) {
		$.ig.EventArgs.prototype.init.call(this);
		this.oldValue(oldValue);
		this.newValue(newValue);
	},
	_oldValue: 0,
	oldValue: function (value) {
		if (arguments.length === 1) {
			this._oldValue = value;
			return value;
		} else {
			return this._oldValue;
		}
	}
	,
	_newValue: 0,
	newValue: function (value) {
		if (arguments.length === 1) {
			this._newValue = value;
			return value;
		} else {
			return this._newValue;
		}
	}
	,
	$type: new $.ig.Type('DoubleValueChangedEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('InterpolationUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	interpolatePoints: function (interpolatedPoints, p, minPoints, maxPoints) {
		var q = 1 - p;
		if (interpolatedPoints == null) {
			interpolatedPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		if (minPoints == null) {
			minPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		var minCount = minPoints.count();
		var maxCount = maxPoints.count();
		var count = Math.max(minCount, maxCount);
		var interpolatedCount = interpolatedPoints.count();
		if (interpolatedCount < count) {
			interpolatedPoints.insertRange(interpolatedCount, new Array(count - interpolatedCount));
		}
		if (interpolatedCount > count) {
			interpolatedPoints.removeRange(count, interpolatedCount - count);
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
				interpolatedPoints.__inner[i2] = interpolatedPoints.__inner[i2] = { __x: (minPoints.__inner[i2].__x * q) + (mx.__x * p), __y: (minPoints.__inner[i2].__y * q) + (mx.__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		return interpolatedPoints;
	}
	,
	interpolateValues$1: function ($t, interpolatedValues, p, minValues, maxValues, createEmpty, interpolate) {
		var q = 1 - p;
		if (interpolatedValues == null) {
			interpolatedValues = new $.ig.List$1($t, 0);
		}
		if (minValues == null) {
			minValues = new $.ig.List$1($t, 0);
		}
		var minCount = minValues.count();
		var maxCount = maxValues.count();
		var count = Math.max(minCount, maxCount);
		if (interpolatedValues.count() < count) {
			var capacity = count - interpolatedValues.count();
			var range = new Array(capacity);
			interpolatedValues.insertRange(interpolatedValues.count(), range);
		}
		if (interpolatedValues.count() > count) {
			interpolatedValues.removeRange(count, interpolatedValues.count() - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedValues.__inner[i] = interpolate(p, q, minValues.__inner[i], maxValues.__inner[i]);
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minValues.__inner[minCount - 1] : createEmpty();
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedValues.__inner[i1] = interpolate(p, q, mn, maxValues.__inner[i1]);
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxValues.__inner[maxCount - 1] : createEmpty();
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedValues.__inner[i2] = interpolate(p, q, minValues.__inner[i2], mx);
			}
		}
		return interpolatedValues;
	}
	,
	$type: new $.ig.Type('InterpolationUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolygonUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	polygonContainsPoint: function (points, pt) {
		if (points == null) {
			return false;
		}
		var pointsCount = points.count();
		if (pointsCount < 4) {
			return false;
		}
		var c = false;
		for (var i = 0, j = pointsCount - 1; i < pointsCount; j = i++) {
			if (((points.item(i).__y > pt.__y) != (points.item(j).__y > pt.__y)) && (pt.__x < (points.item(j).__x - points.item(i).__x) * (pt.__y - points.item(i).__y) / (points.item(j).__y - points.item(i).__y) + points.item(i).__x)) {
				c = !c;
			}
		}
		return c;
	}
	,
	$type: new $.ig.Type('PolygonUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('StackPool$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this._deferDisactivate = false;
		this._active = new $.ig.Dictionary$2(this.$t, $.ig.Object.prototype.$type, 0);
		this._limbo = new $.ig.Stack$1(this.$t);
		this._inactive = new $.ig.Stack$1(this.$t);
		$.ig.Object.prototype.init.call(this);
	},
	pop: function () {
		var t;
		if (this._limbo.count() != 0) {
			t = this._limbo.pop();
		} else {
			t = this._inactive.count() != 0 ? this._inactive.pop() : this.create()();
			this.activate()(t);
		}
		this._active.add(t, null);
		return t;
	}
	,
	push: function (t) {
		this._active.remove(t);
		if (this.deferDisactivate()) {
			this._limbo.push(t);
		} else {
			this.deactivate()(t);
			var inactiveCount = $.ig.StackPool$1.prototype.roundUp(this.$t, this._active.count());
			if (this._inactive.count() < inactiveCount) {
				this.destroy()(t);
			} else {
				this._inactive.push(t);
			}
		}
	}
	,
	deferDisactivate: function (value) {
		if (arguments.length === 1) {
			if (this._deferDisactivate != value) {
				this._deferDisactivate = value;
				if (!this._deferDisactivate) {
					var inactiveCount = $.ig.StackPool$1.prototype.roundUp(this.$t, this._active.count());
					while (this._limbo.count() > 0 && this._inactive.count() <= inactiveCount) {
						var t = this._limbo.pop();
						this.deactivate()(t);
						this._inactive.push(t);
					}
					while (this._limbo.count() > 0) {
						var t1 = this._limbo.pop();
						this.deactivate()(t1);
						this.destroy()(t1);
					}
					while (this._inactive.count() > inactiveCount) {
						this.destroy()(this._inactive.pop());
					}
				}
			}
			return value;
		} else {
			return this._deferDisactivate;
		}
	}
	,
	_deferDisactivate: false,
	activeCount: function () {
		return this._active.count();
	}
	,
	inactiveCount: function () {
		return this._inactive.count();
	}
	,
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
	_deactivate: null,
	deactivate: function (value) {
		if (arguments.length === 1) {
			this._deactivate = value;
			return value;
		} else {
			return this._deactivate;
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
	roundUp: function ($t, a) {
		var p = 2;
		while (a > p) {
			p = p << 1;
		}
		return p;
	}
	,
	_active: null,
	_limbo: null,
	_inactive: null,
	$type: new $.ig.Type('StackPool$1', $.ig.Object.prototype.$type)
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
$.ig.util.bulkDefine(["MorphSegment:a", 
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
"List$1:ba", 
"IList$1:bb", 
"ICollection$1:bc", 
"IArray:bd", 
"Script:be", 
"IArrayList:bf", 
"Array:bg", 
"CompareCallback:bh", 
"MulticastDelegate:bi", 
"IntPtr:bj", 
"Func$3:bk", 
"Action$1:bl", 
"Comparer$1:bm", 
"IComparer:bn", 
"IComparer$1:bo", 
"DefaultComparer$1:bp", 
"Comparison$1:bq", 
"ReadOnlyCollection$1:br", 
"Predicate$1:bs", 
"NotImplementedException:bt", 
"Point:bu", 
"GeometryUtil:bv", 
"Math:bw", 
"Rect:bx", 
"Size:by", 
"Tuple$2:bz", 
"PathFigure:b0", 
"PathSegmentCollection:b1", 
"ArcSegment:b2", 
"PathSegment:b3", 
"PathSegmentType:b4", 
"SweepDirection:b5", 
"PolyLineSegment:b6", 
"PointCollection:b7", 
"InterpolationUtil:b8", 
"Func$1:b9", 
"Func$5:ca", 
"XamLinearGaugeView:cb", 
"XamLinearGauge:cc", 
"Control:cd", 
"FrameworkElement:ce", 
"UIElement:cf", 
"DependencyObject:cg", 
"Dictionary:ch", 
"DependencyProperty:ci", 
"PropertyMetadata:cj", 
"PropertyChangedCallback:ck", 
"DependencyPropertyChangedEventArgs:cl", 
"DependencyPropertiesCollection:cm", 
"UnsetValue:cn", 
"Binding:co", 
"PropertyPath:cp", 
"Transform:cq", 
"Visibility:cr", 
"Style:cs", 
"Thickness:ct", 
"HorizontalAlignment:cu", 
"VerticalAlignment:cv", 
"DoubleAnimator:cw", 
"INotifyPropertyChanged:cx", 
"PropertyChangedEventHandler:cy", 
"PropertyChangedEventArgs:cz", 
"EasingFunctionHandler:c0", 
"Callback:c1", 
"Delegate:c2", 
"Interlocked:c3", 
"LinearGaugeFrame:c4", 
"LinearGraphFrame:c5", 
"LinearGraphScaleFrame:c6", 
"LinearGraphRectFrame:c7", 
"PathGeometry:c8", 
"Geometry:c9", 
"GeometryType:da", 
"PathFigureCollection:db", 
"LinearScaleOrientation:dc", 
"LineSegment:dd", 
"Brush:de", 
"Color:df", 
"ArgumentException:dg", 
"BrushUtil:dh", 
"LinearGradientBrush:di", 
"GradientStop:dj", 
"ColorUtil:dk", 
"Random:dl", 
"InterpolationMode:dm", 
"MathUtil:dn", 
"RuntimeHelpers:dp", 
"RuntimeFieldHandle:dq", 
"JQueryObject:dr", 
"Element:ds", 
"ElementAttributeCollection:dt", 
"ElementCollection:du", 
"WebStyle:dv", 
"ElementNodeType:dw", 
"Document:dx", 
"EventListener:dy", 
"IElementEventHandler:dz", 
"ElementEventHandler:d0", 
"ElementAttribute:d1", 
"JQueryPosition:d2", 
"JQueryCallback:d3", 
"JQueryEvent:d4", 
"JQueryUICallback:d5", 
"BrushCollection:d6", 
"ObservableCollection$1:d7", 
"INotifyCollectionChanged:d8", 
"NotifyCollectionChangedEventHandler:d9", 
"NotifyCollectionChangedEventArgs:ea", 
"EventArgs:eb", 
"NotifyCollectionChangedAction:ec", 
"CssHelper:ed", 
"JQuery:ee", 
"JQueryDeferred:ef", 
"JQueryPromise:eg", 
"Action:eh", 
"CssGradientUtil:ei", 
"LinearGraphNeedleFrame:ej", 
"LinearGraphBackingFrame:ek", 
"LinearGaugeScaleFrame:el", 
"LinearGraphScaler:em", 
"LinearGraphRangeCollection:en", 
"LinearGraphNeedleShape:eo", 
"StackPool$1:ep", 
"Dictionary$2:eq", 
"IDictionary$2:er", 
"IDictionary:es", 
"Func$2:et", 
"KeyValuePair$2:eu", 
"Enumerable:ev", 
"Thread:ew", 
"ThreadStart:ex", 
"IOrderedEnumerable$1:ey", 
"SortedList$1:ez", 
"ArgumentNullException:e0", 
"IEqualityComparer$1:e1", 
"EqualityComparer$1:e2", 
"IEqualityComparer:e3", 
"DefaultEqualityComparer$1:e4", 
"InvalidOperationException:e5", 
"Stack$1:e6", 
"ReverseArrayEnumerator$1:e7", 
"Path:e8", 
"Shape:e9", 
"DoubleCollection:fa", 
"TextBlock:fb", 
"FormatLinearGraphLabelHandler:fc", 
"FormatLinearGraphLabelEventArgs:fd", 
"AlignLinearGraphLabelHandler:fe", 
"AlignLinearGraphLabelEventArgs:ff", 
"DoubleValueChangedEventHandler:fg", 
"DoubleValueChangedEventArgs:fh", 
"PolygonUtil:fi", 
"LinearGaugeVisualData:fj", 
"LinearGraphVisualData:fk", 
"PrimitiveVisualData:fl", 
"IVisualData:fm", 
"PrimitiveAppearanceData:fn", 
"BrushAppearanceData:fo", 
"StringBuilder:fp", 
"Environment:fq", 
"AppearanceHelper:fr", 
"LinearGradientBrushAppearanceData:fs", 
"GradientStopAppearanceData:ft", 
"SolidBrushAppearanceData:fu", 
"GeometryData:fv", 
"GetPointsSettings:fw", 
"Line:fx", 
"LineGeometry:fy", 
"GeometryGroup:fz", 
"GeometryCollection:f0", 
"FillRule:f1", 
"RectangleGeometry:f2", 
"EllipseGeometry:f3", 
"EllipseGeometryData:f4", 
"RectangleGeometryData:f5", 
"LineGeometryData:f6", 
"PathGeometryData:f7", 
"PathFigureData:f8", 
"SegmentData:f9", 
"LineSegmentData:ga", 
"PolylineSegmentData:gb", 
"ArcSegmentData:gc", 
"PolyBezierSegment:gd", 
"PolyBezierSegmentData:ge", 
"BezierSegment:gf", 
"BezierSegmentData:gg", 
"LabelAppearanceData:gh", 
"FontInfo:gi", 
"RotateTransform:gj", 
"TransformGroup:gk", 
"TransformCollection:gl", 
"ShapeTags:gm", 
"LinearScaleVisualData:gn", 
"LinearGraphScaleLabelVisualDataList:go", 
"LinearGraphScaleTickmarkVisualDataList:gp", 
"LinearGraphRangeVisualDataList:gq", 
"LinearGraphToolTipVisualData:gr", 
"ToolTipItemVisualData:gs", 
"LinearGraphScaleLabelVisualData:gt", 
"LinearGraphScaleTickmarkVisualData:gu", 
"LinearGraphRangeVisualData:gv", 
"LinearNeedleVisualData:gw", 
"ToolTipInfo:gx", 
"LinearGraphTooltipDataContext:gy", 
"PathVisualData:gz", 
"XamLinearGraphRange:g0", 
"LinearGraphNeedleParameters:g1", 
"LinearGraphNeedlePreparer:g2", 
"FontUtil:g3", 
"RenderingContext:g4", 
"IRenderer:g5", 
"Rectangle:g6", 
"Polygon:g7", 
"Polyline:g8", 
"DataTemplateRenderInfo:g9", 
"DataTemplatePassInfo:ha", 
"ContentControl:hb", 
"DataTemplate:hc", 
"DataTemplateRenderHandler:hd", 
"DataTemplateMeasureHandler:he", 
"DataTemplateMeasureInfo:hf", 
"DataTemplatePassHandler:hg", 
"EventProxy:hh", 
"ModifierKeys:hi", 
"MouseWheelHandler:hj", 
"GestureHandler:hk", 
"ZoomGestureHandler:hl", 
"FlingGestureHandler:hm", 
"ContactHandler:hn", 
"TouchHandler:ho", 
"MouseOverHandler:hp", 
"MouseHandler:hq", 
"KeyHandler:hr", 
"Key:hs", 
"BaseDOMEventProxy:ht", 
"DOMEventProxy:hu", 
"MSGesture:hv", 
"window:hw", 
"MouseEventArgs:hx", 
"DivElement:hy", 
"CanvasElement:hz", 
"CanvasContext:h0", 
"CanvasViewRenderer:h1", 
"CanvasContext2D:h2", 
"TextMetrics:h3", 
"ImageData:h4", 
"Gradient:h5", 
"TranslateTransform:h6", 
"ScaleTransform:h7", 
"AbstractEnumerable:ia", 
"AbstractEnumerator:ib", 
"GenericEnumerable$1:ic", 
"GenericEnumerator$1:id"]);


$.ig.util.defType('LinearScaleOrientation', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Horizontal";
			case 1: return "Vertical";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('LinearScaleOrientation', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphNeedleShape', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Custom";
			case 1: return "Rectangle";
			case 2: return "Triangle";
			case 3: return "Needle";
			case 4: return "Trapezoid";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('LinearGraphNeedleShape', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('MorphSegment', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this._segmentPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		this._tweenPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
	},
	_segmentPoints: null,
	_tweenPoints: null,
	_isArcPhase: false,
	_arcRadius: 0,
	_isTween: false,
	_hasTweenPoints: false,
	_isLargeArc: false,
	_isClockwise: false,
	generateTween: function (otherArc) {
		this._tweenPoints.clear();
		var numPoints = 20;
		if (this._isArcPhase) {
			var p;
			var startPoint;
			var endPoint;
			if (this._segmentPoints.count() == 0) {
				startPoint = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				endPoint = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			} else {
				startPoint = this._segmentPoints.__inner[0];
				endPoint = this._segmentPoints.__inner[1];
			}
			var center = $.ig.GeometryUtil.prototype.getCenterFromArcSegment(startPoint, endPoint, this._arcRadius, !this._isClockwise, this._isLargeArc);
			var startAngle = $.ig.GeometryUtil.prototype.getAngleTo(center, startPoint);
			var endAngle = $.ig.GeometryUtil.prototype.getAngleTo(center, endPoint);
			while (this._isClockwise && endAngle < startAngle) {
				endAngle += Math.PI * 2;
			}
			while (!this._isClockwise && endAngle > startAngle) {
				endAngle -= Math.PI * 2;
			}
			var currAngle = 0;
			for (var i = 0; i < numPoints; i++) {
				p = i / numPoints;
				if (this._isClockwise) {
					currAngle = startAngle + (endAngle - startAngle) * p;
				} else {
					currAngle = startAngle - (startAngle - endAngle) * p;
				}
				this._tweenPoints.add({ __x: center.__x + Math.cos(currAngle) * this._arcRadius, __y: center.__y + Math.sin(currAngle) * this._arcRadius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		} else {
			var p1;
			var startPoint1;
			var endPoint1;
			if (this._segmentPoints.count() == 0) {
				startPoint1 = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				endPoint1 = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			} else {
				startPoint1 = this._segmentPoints.__inner[0];
				endPoint1 = this._segmentPoints.__inner[1];
			}
			var startX = startPoint1.__x;
			var startY = startPoint1.__y;
			var endX = endPoint1.__x;
			var endY = endPoint1.__y;
			for (var i1 = 0; i1 < numPoints; i1++) {
				p1 = i1 / numPoints;
				this._tweenPoints.add({ __x: startX + (endX - startX) * p1, __y: startY + (endY - startY) * p1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
		this._hasTweenPoints = true;
	}
	,
	renderSegment: function (lastPoint, pf, radius, centerX, centerY) {
		var segment = this;
		var seg = null;
		var segPoints = segment._segmentPoints;
		var segmentPointsCount = segPoints.count();
		if (segmentPointsCount == 0) {
			return lastPoint;
		}
		if (pf.__segments.count() == 0) {
			var startPoint = segPoints.__inner[0];
			pf.__startPoint = { __x: centerX + radius * startPoint.__x, __y: centerY + radius * startPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (segment._isArcPhase) {
			if (segmentPointsCount > 1) {
				var arc = new $.ig.ArcSegment();
				seg = arc;
				var endPoint = segPoints.__inner[1];
				arc.point({ __x: centerX + radius * endPoint.__x, __y: centerY + radius * endPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				arc.isLargeArc(segment._isLargeArc);
				arc.sweepDirection(segment._isClockwise ? $.ig.SweepDirection.prototype.clockwise : $.ig.SweepDirection.prototype.counterclockwise);
				arc.size(new $.ig.Size(1, segment._arcRadius * radius, segment._arcRadius * radius));
				lastPoint = arc.point();
			}
		} else {
			var pl = new $.ig.PolyLineSegment();
			seg = pl;
			if (segmentPointsCount > 1) {
				var currPoint;
				var pts = pl.__points;
				for (var i = 1; i < segmentPointsCount; i++) {
					currPoint = segPoints.__inner[i];
					pts.add({ __x: centerX + radius * currPoint.__x, __y: centerY + radius * currPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				}
				lastPoint = pl.__points.__inner[pl.__points.count() - 1];
			}
		}
		pf.__segments.add(seg);
		return lastPoint;
	}
	,
	interpolateSegment: function (p, previous, next) {
		var target = this;
		var minPoints = previous._segmentPoints;
		var minIsRaster = false;
		var maxPoints = next._segmentPoints;
		var maxIsRaster = false;
		if (minPoints.count() == 0 && maxPoints.count() == 0) {
			return;
		}
		target._isArcPhase = false;
		target._isTween = false;
		target._hasTweenPoints = false;
		target._isLargeArc = false;
		target._isClockwise = false;
		if (previous._isTween) {
			minIsRaster = true;
		}
		if (next._isTween) {
			maxIsRaster = true;
		}
		if (previous._isArcPhase != next._isArcPhase || previous._isTween || next._isTween || previous._isLargeArc != next._isLargeArc || previous._isClockwise != next._isClockwise) {
			minIsRaster = true;
			maxIsRaster = true;
			target._isTween = true;
		}
		if (minIsRaster && !previous._isTween) {
			if (!previous._hasTweenPoints) {
				previous.generateTween(next._arcRadius);
			}
			minPoints = previous._tweenPoints;
		}
		if (maxIsRaster && !next._isTween) {
			if (!next._hasTweenPoints) {
				next.generateTween(previous._arcRadius);
			}
			maxPoints = next._tweenPoints;
		}
		if (previous._isTween) {
			minPoints = previous._tweenPoints;
		}
		if (next._isTween) {
			maxPoints = next._tweenPoints;
		}
		target._tweenPoints.clear();
		if (previous._isArcPhase && next._isArcPhase) {
			target._isArcPhase = true;
		}
		$.ig.InterpolationUtil.prototype.interpolatePoints(target._segmentPoints, p, minPoints, maxPoints);
		if (target._isTween) {
			var point;
			var count = target._segmentPoints.count();
			for (var i = 0; i < count; i++) {
				point = target._segmentPoints.__inner[i];
				target._tweenPoints.add({ __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
			target._hasTweenPoints = true;
		}
		if (previous._isLargeArc == next._isLargeArc) {
			target._isLargeArc = next._isLargeArc;
		}
		if (previous._isClockwise == next._isClockwise) {
			target._isClockwise = next._isClockwise;
		}
		if (target._isArcPhase) {
			target._arcRadius = previous._arcRadius + (next._arcRadius - previous._arcRadius) * p;
		}
	}
	,
	reset: function () {
		this._segmentPoints.clear();
		this._tweenPoints.clear();
		this._isTween = false;
		this._hasTweenPoints = false;
		this._isArcPhase = false;
	}
	,
	$type: new $.ig.Type('MorphSegment', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamLinearGaugeView', 'Object', {
	__canvasWidth: 0,
	__canvasHeight: 0,
	init: function (linearGauge) {
		this.__userRangeBrushes = null;
		this.__userRangeOutlines = null;
		this.__ignoreBrushChanged = false;
		this.__brushNames = [ "needle", "backing", "scale", "tick", "minortick", "label" ];
		this.__tooltipType = null;
		this.__tooltipIndex = -1;
		this.__labelBrush = (function () {
			var $ret = new $.ig.Brush();
			$ret.fill("black");
			return $ret;
		}());
		this.__scheduled = false;
		this.__font = null;
		$.ig.Object.prototype.init.call(this);
		this.linearGauge(linearGauge);
		this.__actualBrushes = new $.ig.Dictionary$2(String, $.ig.Dictionary$2.prototype.$type.specialize(String, $.ig.Brush.prototype.$type), 0);
		this.__needlePath = new $.ig.Path();
		this.__scalePath = new $.ig.Path();
		this.__backingPath = new $.ig.Path();
		this.activeRangePaths(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		this.activePaths(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		this.activeLabels(new $.ig.List$1($.ig.TextBlock.prototype.$type, 0));
		this.activeTitles(new $.ig.List$1($.ig.TextBlock.prototype.$type, 0));
	},
	_linearGauge: null,
	linearGauge: function (value) {
		if (arguments.length === 1) {
			this._linearGauge = value;
			return value;
		} else {
			return this._linearGauge;
		}
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
	_canvas: null,
	canvas: function (value) {
		if (arguments.length === 1) {
			this._canvas = value;
			return value;
		} else {
			return this._canvas;
		}
	}
	,
	_context: null,
	context: function (value) {
		if (arguments.length === 1) {
			this._context = value;
			return value;
		} else {
			return this._context;
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
	_cssSpan: null,
	_activeRangePaths: null,
	activeRangePaths: function (value) {
		if (arguments.length === 1) {
			this._activeRangePaths = value;
			return value;
		} else {
			return this._activeRangePaths;
		}
	}
	,
	_activePaths: null,
	activePaths: function (value) {
		if (arguments.length === 1) {
			this._activePaths = value;
			return value;
		} else {
			return this._activePaths;
		}
	}
	,
	_activeLabels: null,
	activeLabels: function (value) {
		if (arguments.length === 1) {
			this._activeLabels = value;
			return value;
		} else {
			return this._activeLabels;
		}
	}
	,
	_activeTitles: null,
	activeTitles: function (value) {
		if (arguments.length === 1) {
			this._activeTitles = value;
			return value;
		} else {
			return this._activeTitles;
		}
	}
	,
	onInit: function () {
		this.toolTipContext(new $.ig.LinearGraphTooltipDataContext());
		this.__showTooltipTimeout = this.linearGauge().showToolTipTimeout();
	}
	,
	__needlePath: null,
	__scalePath: null,
	__backingPath: null,
	ready: function () {
		return true;
	}
	,
	getNeedlePath: function () {
		return this.__needlePath;
	}
	,
	getScalePath: function () {
		return this.__scalePath;
	}
	,
	getBackingPath: function () {
		return this.__backingPath;
	}
	,
	__userNeedleBrush: null,
	__userNeedleOutline: null,
	__userBackingBrush: null,
	__userBackingOutline: null,
	__userScaleBrush: null,
	__userScaleOutline: null,
	__userTickBrush: null,
	__userMinorTickBrush: null,
	__rangeBrushes: null,
	__rangeOutlines: null,
	__userRangeBrushes: null,
	__userRangeOutlines: null,
	__ignoreBrushChanged: false,
	__actualBrushes: null,
	__brushNames: null,
	getDefaultColors: function () {
		this.__ignoreBrushChanged = true;
		var clrs = [ "#778899", "#EBEDEF", "#e0e0e0", "#617583", "rgba(100,107,114,.74)", "rgba(100,107,114,.74)" ];
		var outline = [ "#778899", "#B2B9C0", "#e0e0e0", null, null, null ];
		var defaults = (function () {
			var $ret = new $.ig.List$1(Array, 0);
			$ret.add(clrs);
			$ret.add(outline);
			return $ret;
		}());
		this.__actualBrushes.clear();
		var namesCount = this.__brushNames.length;
		for (var i = 0; i < namesCount; i++) {
			var name = this.__brushNames[i];
			var className = "ui-lineargauge-" + name;
			var fillGradientClassName = "ui-lineargauge-" + name + "-fill";
			var outlineGradientClassName = "ui-lineargauge-" + name + "-outline";
			var fillBrush;
			if (!(function () { var $ret = $.ig.XamLinearGaugeView.prototype._gradientBrushes.tryGetValue(fillGradientClassName, fillBrush); fillBrush = $ret.p1; return $ret.ret; }())) {
				fillBrush = $.ig.BrushUtil.prototype.getGradientBrush(fillGradientClassName, className, "background-color", this.container(), clrs[i]);
				$.ig.XamLinearGaugeView.prototype._gradientBrushes.add(fillGradientClassName, fillBrush);
			}
			var outlineBrush;
			if (!(function () { var $ret = $.ig.XamLinearGaugeView.prototype._gradientBrushes.tryGetValue(outlineGradientClassName, outlineBrush); outlineBrush = $ret.p1; return $ret.ret; }())) {
				outlineBrush = $.ig.BrushUtil.prototype.getGradientBrush(outlineGradientClassName, className, "border-top-color", this.container(), outline[i]);
				$.ig.XamLinearGaugeView.prototype._gradientBrushes.add(outlineGradientClassName, outlineBrush);
			}
			var dict = new $.ig.Dictionary$2(String, $.ig.Brush.prototype.$type, 0);
			dict.item("fill", fillBrush);
			dict.item("outline", outlineBrush);
			this.__actualBrushes.item(this.__brushNames[i], dict);
		}
		this.updateBrushes();
		if ($.ig.XamLinearGaugeView.prototype._rangeBrushes == null || $.ig.XamLinearGaugeView.prototype._rangeOutlines == null) {
			var $ret = $.ig.BrushUtil.prototype.getGradientBrushCollection("ui-lineargauge-range-fill-palette-", "ui-lineargauge-range-outline-palette-", "ui-lineargauge-range-palette-", this.container(), $.ig.XamLinearGaugeView.prototype._rangeBrushes, $.ig.XamLinearGaugeView.prototype._rangeOutlines, [ "#216E99", "#216E99", "#44acd6", "#44acd6", "#cecece", "#cecece" ]);
			$.ig.XamLinearGaugeView.prototype._rangeBrushes = $ret.p4;
			$.ig.XamLinearGaugeView.prototype._rangeOutlines = $ret.p5;
		}
		this.__rangeBrushes = $.ig.XamLinearGaugeView.prototype._rangeBrushes;
		this.__rangeOutlines = $.ig.XamLinearGaugeView.prototype._rangeOutlines;
		if (this.__userRangeBrushes == null) {
			this.linearGauge().rangeBrushes(this.__rangeBrushes);
		}
		if (this.__userRangeOutlines == null) {
			this.linearGauge().rangeOutlines(this.__rangeOutlines);
		}
		this.__ignoreBrushChanged = false;
	}
	,
	updateBrushes: function () {
		this.__ignoreBrushChanged = true;
		var namesCount = this.__brushNames.length;
		for (var i = 0; i < namesCount; i++) {
			var name = this.__brushNames[i];
			this.updateBrush(name, this.__actualBrushes.item(name));
		}
		this.__ignoreBrushChanged = false;
	}
	,
	updateBrush: function (name, dict) {
		var colorBrush = dict.item("fill");
		var outlineBrush = dict.item("outline");
		switch (name) {
			case "needle":
				if (this.__userNeedleBrush == null) {
					this.linearGauge().needleBrush(colorBrush);
				}
				if (this.__userNeedleOutline == null) {
					this.linearGauge().needleOutline(outlineBrush);
				}
				break;
			case "scale":
				if (this.__userScaleBrush == null) {
					this.linearGauge().scaleBrush(colorBrush);
				}
				if (this.__userScaleOutline == null) {
					this.linearGauge().scaleOutline(outlineBrush);
				}
				break;
			case "backing":
				if (this.__userBackingBrush == null) {
					this.linearGauge().backingBrush(colorBrush);
				}
				if (this.__userBackingOutline == null) {
					this.linearGauge().backingOutline(outlineBrush);
				}
				break;
			case "tick":
				if (this.__userTickBrush == null) {
					this.linearGauge().tickBrush(colorBrush);
				}
				break;
			case "minortick":
				if (this.__userMinorTickBrush == null) {
					this.linearGauge().minorTickBrush(colorBrush);
				}
				break;
			case "label":
				this.__labelBrush = colorBrush;
				break;
		}
	}
	,
	onBrushChanged: function (propertyName, oldValue, newValue) {
		var oldBrush = oldValue;
		var newBrush = newValue;
		if (!this.__ignoreBrushChanged) {
			switch (propertyName) {
				case $.ig.XamLinearGauge.prototype.needleBrushPropertyName:
					this.__userNeedleBrush = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.needleOutlinePropertyName:
					this.__userNeedleOutline = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.backingBrushPropertyName:
					this.__userBackingBrush = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.backingOutlinePropertyName:
					this.__userBackingOutline = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.scaleBrushPropertyName:
					this.__userScaleBrush = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.scaleOutlinePropertyName:
					this.__userScaleOutline = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.tickBrushPropertyName:
					this.__userTickBrush = newBrush;
					break;
				case $.ig.XamLinearGauge.prototype.minorTickBrushPropertyName:
					this.__userMinorTickBrush = newBrush;
					break;
			}
			if (newBrush == null) {
				this.updateBrushes();
			}
		}
	}
	,
	onRangeBrushesChanged: function (oldValue, newValue) {
		if (!this.__ignoreBrushChanged) {
			this.__userRangeBrushes = newValue;
			if (this.__userRangeBrushes == null) {
				this.linearGauge().rangeBrushes(this.__rangeBrushes);
			}
		}
	}
	,
	onRangeOutlinesChanged: function (oldValue, newValue) {
		if (!this.__ignoreBrushChanged) {
			this.__userRangeOutlines = newValue;
			if (this.__userRangeOutlines == null) {
				this.linearGauge().rangeOutlines(this.__rangeOutlines);
			}
		}
	}
	,
	cssValue: function (css, prop) {
		var span = this._cssSpan;
		if (span == null) {
			span = this._cssSpan = $("<span style='position:absolute;display:none' />");
			$("body").append(this._cssSpan);
		}
		var i = prop.length, i0 = i;
		var old = new Array(i0);
		while (i0-- > 0) {
			old[i0] = prop[i0] == "font" ? null : span.css(prop[i0]);
		}
		span.addClass(css);
		while (i-- > 0) {
			var p = prop[i];
			p = span.css(p);
			prop[i] = (p == null || p.length == 0 || p == "null" || p == "transparent" || p == old[i]) ? null : p;
		}
		span.removeClass(css);
		return prop;
	}
	,
	styleUpdated: function () {
		this.getDefaultColors();
		this.updateStyle();
		this.scheduleArrange();
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
	_toolTipShowPending: false,
	toolTipShowPending: function (value) {
		if (arguments.length === 1) {
			this._toolTipShowPending = value;
			return value;
		} else {
			return this._toolTipShowPending;
		}
	}
	,
	__tooltip: null,
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
	__hideTooltipTimeoutId: 0,
	__showTooltipTimeoutId: 0,
	pointToClient: function (p) {
		var cRect = $.ig.DOMEventProxy.prototype.getOffset(this.container());
		return { __x: p.__x - cRect.left(), __y: p.__y - cRect.top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	__tooltipType: null,
	__tooltipIndex: 0,
	__tooltipPoint: null,
	__showTooltipTimeout: 0,
	showTooltip: function (p, data, index, type) {
		this.__tooltipPoint = p;
		this.__tooltipIndex = index;
		this.__tooltipType = type;
		this.resetTooltip();
		this.__showTooltipTimeoutId = window.setTimeout(this.updateTooltip.runOn(this), this.__showTooltipTimeout);
		this.toolTipShowPending(true);
	}
	,
	hideTooltip: function () {
		if (this.toolTipShowPending()) {
			window.clearTimeout(this.__showTooltipTimeoutId);
			this.toolTipShowPending(false);
		}
		if (this.toolTipVisible()) {
			var tooltip = this.linearGauge().toolTip();
			window.clearTimeout(this.__hideTooltipTimeoutId);
			tooltip.hide();
			this.toolTipVisible(false);
			this.toolTipContext(null);
		}
		this.__showTooltipTimeout = this.linearGauge().showToolTipTimeout();
	}
	,
	resetTooltip: function () {
		if (this.toolTipShowPending()) {
			window.clearTimeout(this.__showTooltipTimeoutId);
		}
		if (this.toolTipVisible()) {
			var tooltip = this.linearGauge().toolTip();
			window.clearTimeout(this.__hideTooltipTimeoutId);
			tooltip.hide();
			this.toolTipVisible(false);
			this.__showTooltipTimeout = $.ig.intDivide(this.linearGauge().showToolTipTimeout(), 2);
		}
	}
	,
	updateTooltip: function () {
		var $self = this;
		if (this.toolTipShowPending()) {
			window.clearTimeout(this.__showTooltipTimeoutId);
			this.toolTipShowPending(false);
		}
		var index = this.__tooltipIndex;
		var type = this.__tooltipType;
		var p = this.__tooltipPoint;
		this.__hideTooltipTimeoutId = window.setTimeout(this.hideTooltip.runOn(this), 10 * this.linearGauge().showToolTipTimeout());
		var range = this.linearGauge().ranges().__inner[index];
		if (this.__tooltip != this.linearGauge().toolTip()) {
			if (this.__tooltip != null) {
				this.__tooltip.remove();
			}
			this.__tooltip = this.linearGauge().toolTip();
			this.__tooltip.css("position", "absolute");
			this.container().append(this.__tooltip);
		}
		this.toolTipContext(this.resolveTooltipContext(type, index));
		var context_ = this.toolTipContext();
		var tooltip_ = this.__tooltip;
		var tooltipType_ = type;
		if (tooltip_.updateToolTip) { tooltip_.updateToolTip(context_, tooltipType_); };
		var templateObj = this.__tooltip.find(".ui-lineargauge-" + type + "-tooltip");
		if (templateObj != null && templateObj.length > 0) {
			var cssNames = [ "border-color", "border-width", "border-style", "border-radius", "background-color", "padding", "font-weight" ];
			var defaults = (function () {
				var $ret = new $.ig.List$1(String, 0);
				$ret.add($self.toolTipContext().brush().color().colorString());
				$ret.add("2px");
				$ret.add("solid");
				$ret.add("4px");
				$ret.add("rgba(230, 230, 230, 0.74)");
				$ret.add("4px");
				$ret.add("bold");
				return $ret;
			}());
			var actualValues = new $.ig.Dictionary$2(String, String, 0);
			var cssValues = new Array(cssNames.length);
			$.ig.util.arrayCopy1(cssNames, 0, cssValues, 0, cssNames.length);
			this.cssValue("ui-lineargauge-" + type + "-tooltip", cssValues);
			for (var j = 0; j < cssValues.length; j++) {
				templateObj.css(cssNames[j], (cssValues[j] == null) ? defaults.__inner[j] : cssValues[j]);
			}
		}
		this.__tooltip.css("top", (p.__y + 15).toString() + "px").css("left", (p.__x + 15).toString() + "px");
		this.__tooltip.show();
		this.toolTipVisible(true);
	}
	,
	resolveTooltipContext: function (tooltipType, index) {
		var context = new $.ig.LinearGraphTooltipDataContext();
		switch (tooltipType) {
			case "range":
				var range = this.linearGauge().ranges().__inner[index];
				context.item(range);
				context.itemName(range.name());
				context.label(range.name() + ": " + range.startValue().toString() + "-" + range.endValue().toString());
				var rangeBrush = range.brush();
				var rangeOutline = range.outline();
				context.brush(rangeBrush != null ? rangeBrush : this.linearGauge().getBrushByIndex(index, this.__rangeBrushes));
				context.outline(rangeOutline != null ? rangeOutline : this.linearGauge().getBrushByIndex(index, this.__rangeOutlines));
				context.thickness(range.strokeThickness());
				break;
			case "needle":
				context.item(this.linearGauge().value());
				context.itemName(this.linearGauge().needleName());
				context.label(this.linearGauge().needleName() != null ? this.linearGauge().needleName() + ": " + this.linearGauge().value().toString() : this.linearGauge().value().toString());
				context.brush(this.linearGauge().needleBrush());
				context.outline(this.linearGauge().needleOutline());
				context.thickness(this.linearGauge().needleStrokeThickness());
				break;
		}
		return context;
	}
	,
	onContainerProvided: function (container) {
		if (container == null) {
			this.canvas(this.container(this._cssSpan = this.__tooltip = null));
			this.context(null);
			this.cleanUpEventProxy();
			return;
		}
		var cont = $(container);
		this.container(cont);
		var width = Math.round(cont.width());
		var height = Math.round(cont.height());
		this.container().css("position", "relative");
		var canv = $("<canvas style='position:absolute'></canvas>");
		this.container().append(canv);
		this.__canvasWidth = width;
		this.__canvasHeight = height;
		canv.attr("width", width.toString());
		canv.attr("height", height.toString());
		if (this._cssSpan == null) {
			this._cssSpan = $("<span style='position:absolute;display:none' />");
		}
		this.container().append(this._cssSpan);
		this.linearGauge().viewport(new $.ig.Rect(0, 0, 0, width, height));
		this.linearGauge().backingViewport(new $.ig.Rect(0, 0, 0, width, height));
		this.canvas(canv);
		var context = (this.canvas()[0]).getContext("2d");
		this.context(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), context));
		this.getDefaultColors();
		this.updateStyle();
		this.context().setFontInfo(this.__font);
		this.cleanUpEventProxy();
		this.eventProxy(new $.ig.DOMEventProxy(this.container()));
		var $t = this.eventProxy();
		$t.onMouseOver = $.ig.Delegate.prototype.combine($t.onMouseOver, this.eventProxy_OnMouseOver.runOn(this));
		var $t1 = this.eventProxy();
		$t1.onMouseLeave = $.ig.Delegate.prototype.combine($t1.onMouseLeave, this.eventProxy_OnMouseLeave.runOn(this));
		var $t2 = this.eventProxy();
		$t2.onContactStarted = $.ig.Delegate.prototype.combine($t2.onContactStarted, this.eventProxy_OnContactStarted.runOn(this));
		var $t3 = this.eventProxy();
		$t3.onContactMoved = $.ig.Delegate.prototype.combine($t3.onContactMoved, this.eventProxy_OnContactMoved.runOn(this));
		var $t4 = this.eventProxy();
		$t4.onContactCompleted = $.ig.Delegate.prototype.combine($t4.onContactCompleted, this.eventProxy_OnContactCompleted.runOn(this));
		this.render();
	}
	,
	eventProxy_OnContactCompleted: function (point, isFinger) {
		if (this.linearGauge() != null) {
			this.linearGauge().onMouseUp(point);
		}
	}
	,
	eventProxy_OnContactMoved: function (point, isFinger) {
		if (this.linearGauge() != null) {
			this.linearGauge().onMouseMove(point, isFinger);
		}
	}
	,
	eventProxy_OnContactStarted: function (point, isFinger) {
		if (this.linearGauge() != null) {
			this.linearGauge().onMouseDown(point, isFinger);
		}
	}
	,
	eventProxy_OnMouseOver: function (point, onMouseMove, isFinger) {
		if (this.linearGauge() != null) {
			this.linearGauge().onMouseOver(point, onMouseMove, isFinger);
		}
	}
	,
	eventProxy_OnMouseLeave: function (point) {
		if (this.linearGauge() != null) {
			this.linearGauge().onMouseLeave();
		}
	}
	,
	cleanUpEventProxy: function () {
		if (this.eventProxy() != null) {
			var $t = this.eventProxy();
			$t.onMouseOver = $.ig.Delegate.prototype.remove($t.onMouseOver, this.eventProxy_OnMouseOver.runOn(this));
			var $t1 = this.eventProxy();
			$t1.onMouseLeave = $.ig.Delegate.prototype.remove($t1.onMouseLeave, this.eventProxy_OnMouseLeave.runOn(this));
			var $t2 = this.eventProxy();
			$t2.onContactStarted = $.ig.Delegate.prototype.remove($t2.onContactStarted, this.eventProxy_OnContactStarted.runOn(this));
			var $t3 = this.eventProxy();
			$t3.onContactMoved = $.ig.Delegate.prototype.remove($t3.onContactMoved, this.eventProxy_OnContactMoved.runOn(this));
			var $t4 = this.eventProxy();
			$t4.onContactCompleted = $.ig.Delegate.prototype.remove($t4.onContactCompleted, this.eventProxy_OnContactCompleted.runOn(this));
			this.eventProxy().destroy();
		}
	}
	,
	onContainerResized: function () {
		var width = Math.round(this.container().width());
		var height = Math.round(this.container().height());
		this.linearGauge().viewport(new $.ig.Rect(0, 0, 0, width, height));
		this.linearGauge().backingViewport(new $.ig.Rect(0, 0, 0, width, height));
		if (this.eventProxy() != null) {
			this.eventProxy().viewport(this.linearGauge().viewport());
		}
	}
	,
	arrangeComplete: function () {
		this.render();
	}
	,
	__rangesInOrder: null,
	setRangeRenderOrder: function (ranges) {
		this.__rangesInOrder = ranges;
	}
	,
	render: function () {
		if (this.__canvasWidth != this.linearGauge().viewport().width() || this.__canvasHeight != this.linearGauge().viewport().height()) {
			this.canvas().attr("width", this.linearGauge().viewport().width().toString());
			this.canvas().attr("height", this.linearGauge().viewport().height().toString());
			this.__canvasWidth = $.ig.truncate(Math.round(this.linearGauge().viewport().width()));
			this.__canvasHeight = $.ig.truncate(Math.round(this.linearGauge().viewport().height()));
		}
		this.context().clearRectangle(this.linearGauge().viewport().left(), this.linearGauge().viewport().top(), this.linearGauge().viewport().width(), this.linearGauge().viewport().height());
		this.context().renderPath(this.__backingPath);
		this.context().renderPath(this.__scalePath);
		if (this.__rangesInOrder != null) {
			for (var i = 0; i < this.__rangesInOrder.count(); i++) {
				this.context().renderPath(this.__rangesInOrder.__inner[i]);
			}
		}
		for (var i1 = 0; i1 < this.activeLabels().count(); i1++) {
			this.context().setFontInfo(this.__font);
			this.context().renderTextBlock(this.activeLabels().__inner[i1]);
		}
		for (var i2 = 0; i2 < this.activePaths().count(); i2++) {
			this.context().renderPath(this.activePaths().__inner[i2]);
		}
		this.context().renderPath(this.__needlePath);
	}
	,
	updateStyle: function () {
		if (this.linearGauge().font() != null) {
			this.__font = this.linearGauge().getFontInfo();
		} else {
			this.__font = $.ig.FontUtil.prototype.getFont(this.container());
		}
		this.__currentFontHeight = this.getCurrentFontHeight(this.__font);
		if (this.linearGauge().fontBrush() == null) {
			this.__fontBrush = new $.ig.Brush();
			this.__fontBrush.__fill = this.container().css("color");
			this.linearGauge().fontBrush(this.__fontBrush);
		}
		if (this.context() != null) {
			this.context().setFontInfo(this.__font);
		}
	}
	,
	getDesiredWidth: function (element) {
		var tb = $.ig.util.cast($.ig.TextBlock.prototype.$type, element);
		if (tb != null && tb.text() != null) {
			return this.context().measureTextWidth(tb.text()) + $.ig.XamLinearGaugeView.prototype._tEXT_MARGIN;
		}
		return 0;
	}
	,
	getLabelSize: function (tb) {
		if ($.ig.util.isNaN(this.__currentFontHeight)) {
			this.__currentFontHeight = this.getCurrentFontHeight(null);
		}
		var width = this.getDesiredWidth(tb);
		var height = this.__currentFontHeight;
		return new $.ig.Size(1, width, height);
	}
	,
	__labelBrush: null,
	setTextBrush: function (tb, b) {
		if (b != null) {
			tb.fill(b);
		} else {
			tb.fill(this.__labelBrush);
		}
	}
	,
	positionLabel: function (tb, x, y) {
		tb.canvasLeft(x);
		tb.canvasTop(y);
	}
	,
	positionDrawingCanvas: function (x, y) {
	}
	,
	positionTitlesCanvas: function (x, y) {
	}
	,
	getCurrentFontHeight: function (font) {
		var tempSpan_ = $("<span>M</span>");
		var doc = $('body');
		doc.append(tempSpan_);
		if (font != null) {
			tempSpan_.css("font", font.fontString());
		}
		var offset_ = tempSpan_.attr("offsetHeight");
		if (isNaN(offset_)) { offset_ = tempSpan_[0].offsetHeight; };
		tempSpan_.remove();
		return parseInt(offset_);
	}
	,
	__scheduled: false,
	__font: null,
	__fontBrush: null,
	__currentFontHeight: 0,
	scheduleArrange: function () {
		if (!this.__scheduled) {
			this.__scheduled = true;
			window.setTimeout(this.arrange.runOn(this), 0);
		}
	}
	,
	arrange: function () {
		if (this.__scheduled) {
			this.__scheduled = false;
			this.linearGauge().arrangeGauge();
		}
	}
	,
	flush: function () {
		if (this.__scheduled) {
			this.arrange();
		}
	}
	,
	exportViewShapes: function (linearGauge) {
		linearGauge.scalePath(new $.ig.PathVisualData(1, "Scale", this.__scalePath));
		linearGauge.needlePath(new $.ig.PathVisualData(1, "Needle", this.__needlePath));
		linearGauge.backingPath(new $.ig.PathVisualData(1, "Backing", this.__backingPath));
		var font = this.__font;
		var en = this.activeLabels().getEnumerator();
		while (en.moveNext()) {
			var label = en.current();
			if (label.__visibility == $.ig.Visibility.prototype.visible) {
				var fontInfo = $.ig.FontUtil.prototype.getFontInfo(label, font);
				var labelVisual = new $.ig.LinearGraphScaleLabelVisualData();
				labelVisual.labelValue(label.text());
				labelVisual.labelPosition({ __x: label.canvasLeft(), __y: label.canvasTop(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				labelVisual.labelSize(this.getLabelSize(label));
				labelVisual.appearance($.ig.AppearanceHelper.prototype.fromTextElement(label, fontInfo));
				linearGauge.scaleLabels().add(labelVisual);
			}
		}
		var en1 = this.activePaths().getEnumerator();
		while (en1.moveNext()) {
			var path = en1.current();
			if (path.__visibility == $.ig.Visibility.prototype.visible) {
				var tickVisual = new $.ig.LinearGraphScaleTickmarkVisualData();
				tickVisual.tickPath(new $.ig.PathVisualData(1, "tickmarks", path));
				linearGauge.scaleTickmarks().add(tickVisual);
			}
		}
		var en2 = this.__rangesInOrder.getEnumerator();
		while (en2.moveNext()) {
			var path1 = en2.current();
			if (path1.__visibility == $.ig.Visibility.prototype.visible) {
				var rangeVisual = new $.ig.LinearGraphRangeVisualData();
				var rangePath = new $.ig.PathVisualData(1, "range", path1);
				rangeVisual.rangePath(rangePath);
				linearGauge.ranges().add(rangeVisual);
			}
		}
	}
	,
	rangePathActivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	rangePathDeactivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	rangePathDestroy: function (p) {
		this.activeRangePaths().remove(p);
	}
	,
	rangePathCreate: function () {
		var p = new $.ig.Path();
		this.activeRangePaths().add(p);
		return p;
	}
	,
	titleActivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	titleDeactivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	titleDestroy: function (tb) {
		this.activeTitles().remove(tb);
	}
	,
	titleCreate: function () {
		var tb = new $.ig.TextBlock();
		this.activeTitles().add(tb);
		return tb;
	}
	,
	pathActivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	pathDeactivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	pathDestroy: function (p) {
		this.activePaths().remove(p);
	}
	,
	pathCreate: function () {
		var p = new $.ig.Path();
		this.activePaths().add(p);
		return p;
	}
	,
	labelActivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	labelDeactivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	labelDestroy: function (tb) {
		this.activeLabels().remove(tb);
	}
	,
	labelCreate: function () {
		var tb = new $.ig.TextBlock();
		this.activeLabels().add(tb);
		return tb;
	}
	,
	getToolTipInfo: function () {
		var $self = this;
		if (this.toolTipContext() == null) {
			return null;
		}
		return (function () {
			var $ret = new $.ig.ToolTipInfo();
			$ret.index($self.__tooltipIndex);
			$ret.point($self.__tooltipPoint);
			$ret.type($self.__tooltipType);
			$ret.dataContext($self.toolTipContext());
			return $ret;
		}());
	}
	,
	$type: new $.ig.Type('XamLinearGaugeView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphBackingFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
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
	_outerExtent: 0,
	outerExtent: function (value) {
		if (arguments.length === 1) {
			this._outerExtent = value;
			return value;
		} else {
			return this._outerExtent;
		}
	}
	,
	_innerExtent: 0,
	innerExtent: function (value) {
		if (arguments.length === 1) {
			this._innerExtent = value;
			return value;
		} else {
			return this._innerExtent;
		}
	}
	,
	getPathGeometry: function (breadth, length, orientation) {
		var $self = this;
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var tl, tr, br, bl;
		if (orientation == $.ig.LinearScaleOrientation.prototype.vertical) {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: breadth * $self.innerExtent(), __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: breadth * $self.outerExtent(), __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: breadth * $self.outerExtent(), __y: length, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: breadth * $self.innerExtent(), __y: length, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		} else {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: 0, __y: breadth * (1 - $self.outerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: length, __y: breadth * (1 - $self.outerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: length, __y: breadth * (1 - $self.innerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: 0, __y: breadth * (1 - $self.innerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		}
		pf.__startPoint = tl.point();
		pf.__segments.add(tr);
		pf.__segments.add(br);
		pf.__segments.add(bl);
		pf.__isClosed = true;
		pg.figures().add(pf);
		return pg;
	}
	,
	interpolate: function (p, previous, next) {
		this.strokeThickness(previous.strokeThickness() + (next.strokeThickness() - previous.strokeThickness()) * p);
		this.brush($.ig.BrushUtil.prototype.getInterpolation(previous.brush(), p, next.brush(), $.ig.InterpolationMode.prototype.rGB));
		this.outline($.ig.BrushUtil.prototype.getInterpolation(previous.outline(), p, next.outline(), $.ig.InterpolationMode.prototype.rGB));
		this.outerExtent(previous.outerExtent() + (next.outerExtent() - previous.outerExtent()) * p);
		this.innerExtent(previous.innerExtent() + (next.innerExtent() - previous.innerExtent()) * p);
	}
	,
	$type: new $.ig.Type('LinearGraphBackingFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.backingFrame(new $.ig.LinearGraphBackingFrame());
		this.needleFrame(new $.ig.LinearGraphRectFrame());
	},
	scaleFrame: function (value) {
	}
	,
	_backingFrame: null,
	backingFrame: function (value) {
		if (arguments.length === 1) {
			this._backingFrame = value;
			return value;
		} else {
			return this._backingFrame;
		}
	}
	,
	_needleFrame: null,
	needleFrame: function (value) {
		if (arguments.length === 1) {
			this._needleFrame = value;
			return value;
		} else {
			return this._needleFrame;
		}
	}
	,
	interpolate: function (p, previous, next) {
		this.needleFrame($.ig.LinearGraphRectFrame.prototype.interpolate(p, 1 - p, previous.needleFrame(), next.needleFrame()));
		this.scaleFrame().interpolate(p, previous.scaleFrame(), next.scaleFrame());
		this.backingFrame().interpolate(p, previous.backingFrame(), next.backingFrame());
	}
	,
	$type: new $.ig.Type('LinearGraphFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphNeedleFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.innerBase(new $.ig.MorphSegment());
		this.innerPoint(new $.ig.MorphSegment());
		this.feature(new $.ig.MorphSegment());
		this.outerPoint(new $.ig.MorphSegment());
		this.outerBase(new $.ig.MorphSegment());
		this.outerPointUnderside(new $.ig.MorphSegment());
		this.featureUnderside(new $.ig.MorphSegment());
		this.innerPointUnderside(new $.ig.MorphSegment());
		this.cutout(new $.ig.MorphSegment());
		this.cutoutUnderside(new $.ig.MorphSegment());
	},
	_needleStartExtent: 0,
	needleStartExtent: function (value) {
		if (arguments.length === 1) {
			this._needleStartExtent = value;
			return value;
		} else {
			return this._needleStartExtent;
		}
	}
	,
	_needleEndExtent: 0,
	needleEndExtent: function (value) {
		if (arguments.length === 1) {
			this._needleEndExtent = value;
			return value;
		} else {
			return this._needleEndExtent;
		}
	}
	,
	_needleWidth: 0,
	needleWidth: function (value) {
		if (arguments.length === 1) {
			this._needleWidth = value;
			return value;
		} else {
			return this._needleWidth;
		}
	}
	,
	_needleValue: 0,
	needleValue: function (value) {
		if (arguments.length === 1) {
			this._needleValue = value;
			return value;
		} else {
			return this._needleValue;
		}
	}
	,
	_needleAngle: 0,
	needleAngle: function (value) {
		if (arguments.length === 1) {
			this._needleAngle = value;
			return value;
		} else {
			return this._needleAngle;
		}
	}
	,
	_needleBrush: null,
	needleBrush: function (value) {
		if (arguments.length === 1) {
			this._needleBrush = value;
			return value;
		} else {
			return this._needleBrush;
		}
	}
	,
	_needleOutline: null,
	needleOutline: function (value) {
		if (arguments.length === 1) {
			this._needleOutline = value;
			return value;
		} else {
			return this._needleOutline;
		}
	}
	,
	_needleStrokeThickness: 0,
	needleStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._needleStrokeThickness = value;
			return value;
		} else {
			return this._needleStrokeThickness;
		}
	}
	,
	_innerBase: null,
	innerBase: function (value) {
		if (arguments.length === 1) {
			this._innerBase = value;
			return value;
		} else {
			return this._innerBase;
		}
	}
	,
	_innerPoint: null,
	innerPoint: function (value) {
		if (arguments.length === 1) {
			this._innerPoint = value;
			return value;
		} else {
			return this._innerPoint;
		}
	}
	,
	_feature: null,
	feature: function (value) {
		if (arguments.length === 1) {
			this._feature = value;
			return value;
		} else {
			return this._feature;
		}
	}
	,
	_outerPoint: null,
	outerPoint: function (value) {
		if (arguments.length === 1) {
			this._outerPoint = value;
			return value;
		} else {
			return this._outerPoint;
		}
	}
	,
	_outerBase: null,
	outerBase: function (value) {
		if (arguments.length === 1) {
			this._outerBase = value;
			return value;
		} else {
			return this._outerBase;
		}
	}
	,
	_outerPointUnderside: null,
	outerPointUnderside: function (value) {
		if (arguments.length === 1) {
			this._outerPointUnderside = value;
			return value;
		} else {
			return this._outerPointUnderside;
		}
	}
	,
	_featureUnderside: null,
	featureUnderside: function (value) {
		if (arguments.length === 1) {
			this._featureUnderside = value;
			return value;
		} else {
			return this._featureUnderside;
		}
	}
	,
	_innerPointUnderside: null,
	innerPointUnderside: function (value) {
		if (arguments.length === 1) {
			this._innerPointUnderside = value;
			return value;
		} else {
			return this._innerPointUnderside;
		}
	}
	,
	_cutout: null,
	cutout: function (value) {
		if (arguments.length === 1) {
			this._cutout = value;
			return value;
		} else {
			return this._cutout;
		}
	}
	,
	_cutoutUnderside: null,
	cutoutUnderside: function (value) {
		if (arguments.length === 1) {
			this._cutoutUnderside = value;
			return value;
		} else {
			return this._cutoutUnderside;
		}
	}
	,
	interpolate: function (p, q, min, max) {
		var ret = new $.ig.LinearGraphNeedleFrame();
		ret.needleStartExtent(min.needleStartExtent() + (max.needleStartExtent() - min.needleStartExtent()) * p);
		ret.needleEndExtent(min.needleEndExtent() + (max.needleEndExtent() - min.needleEndExtent()) * p);
		ret.needleWidth(min.needleWidth() + (max.needleWidth() - min.needleWidth()) * p);
		ret.needleAngle(min.needleAngle() + (max.needleAngle() - min.needleAngle()) * p);
		ret.needleValue(min.needleValue() + (max.needleValue() - min.needleValue()) * p);
		ret.needleBrush($.ig.BrushUtil.prototype.getInterpolation(min.needleBrush(), p, max.needleBrush(), $.ig.InterpolationMode.prototype.rGB));
		ret.needleOutline($.ig.BrushUtil.prototype.getInterpolation(min.needleOutline(), p, max.needleOutline(), $.ig.InterpolationMode.prototype.rGB));
		ret.needleStrokeThickness(min.needleStrokeThickness() + (max.needleStrokeThickness() - min.needleStrokeThickness()) * p);
		ret.innerBase().interpolateSegment(p, min.innerBase(), max.innerBase());
		ret.innerPoint().interpolateSegment(p, min.innerPoint(), max.innerPoint());
		ret.feature().interpolateSegment(p, min.feature(), max.feature());
		ret.outerPoint().interpolateSegment(p, min.outerPoint(), max.outerPoint());
		ret.outerBase().interpolateSegment(p, min.outerBase(), max.outerBase());
		ret.outerPointUnderside().interpolateSegment(p, min.outerPointUnderside(), max.outerPointUnderside());
		ret.featureUnderside().interpolateSegment(p, min.featureUnderside(), max.featureUnderside());
		ret.innerPointUnderside().interpolateSegment(p, min.innerPointUnderside(), max.innerPointUnderside());
		ret.cutout().interpolateSegment(p, min.cutout(), max.cutout());
		ret.cutoutUnderside().interpolateSegment(p, min.cutoutUnderside(), max.cutoutUnderside());
		return ret;
	}
	,
	getPathGeometry: function (width, centerX, centerY) {
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var lastPoint = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		lastPoint = this.outerBase().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.outerPoint().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.feature().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.innerPoint().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.innerBase().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.innerPointUnderside().renderSegment(lastPoint, pf, width, centerX, centerY);
		lastPoint = this.featureUnderside().renderSegment(lastPoint, pf, width, centerX, centerY);
		pf.__isClosed = true;
		pg.figures().add(pf);
		var cutoutFigure = new $.ig.PathFigure();
		if (this.cutout()._segmentPoints.count() > 0 || this.cutoutUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.cutout().renderSegment(lastPoint, cutoutFigure, width, centerX, centerY);
			lastPoint = this.cutoutUnderside().renderSegment(lastPoint, cutoutFigure, width, centerX, centerY);
			cutoutFigure.__isClosed = true;
			pg.figures().add(cutoutFigure);
		}
		return pg;
	}
	,
	$type: new $.ig.Type('LinearGraphNeedleFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphRectFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_startInnerExtent: 0,
	_startOuterExtent: 0,
	_endInnerExtent: 0,
	_endOuterExtent: 0,
	_startValue: 0,
	_endValue: 0,
	_brush: null,
	_outline: null,
	_strokeThickness: 0,
	getPathGeometry: function (startPoint, breadth, length, orientation) {
		var $self = this;
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var tl, tr, br, bl;
		if (orientation == $.ig.LinearScaleOrientation.prototype.vertical) {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: startPoint.__x + breadth * $self._endInnerExtent, __y: length - $self._endValue, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: startPoint.__x + breadth * $self._endOuterExtent, __y: length - $self._endValue, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: startPoint.__x + breadth * $self._startOuterExtent, __y: length - $self._startValue, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: startPoint.__x + breadth * $self._startInnerExtent, __y: length - $self._startValue, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		} else {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: $self._startValue, __y: breadth * (1 - $self._startOuterExtent), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: $self._endValue, __y: breadth * (1 - $self._endOuterExtent), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: $self._endValue, __y: breadth * (1 - $self._endInnerExtent), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: $self._startValue, __y: breadth * (1 - $self._startInnerExtent), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		}
		pf.__startPoint = tl.point();
		pf.__segments.add(tr);
		pf.__segments.add(br);
		pf.__segments.add(bl);
		pf.__isClosed = true;
		pg.figures().add(pf);
		return pg;
	}
	,
	interpolate: function (p, q, min, max) {
		var ret = new $.ig.LinearGraphRectFrame();
		ret._brush = $.ig.BrushUtil.prototype.getInterpolation(min._brush, p, max._brush, $.ig.InterpolationMode.prototype.rGB);
		ret._outline = $.ig.BrushUtil.prototype.getInterpolation(min._outline, p, max._outline, $.ig.InterpolationMode.prototype.rGB);
		ret._strokeThickness = q * min._strokeThickness + p * max._strokeThickness;
		ret._startInnerExtent = q * min._startInnerExtent + p * max._startInnerExtent;
		ret._startOuterExtent = q * min._startOuterExtent + p * max._startOuterExtent;
		ret._startValue = q * min._startValue + p * max._startValue;
		ret._endValue = q * min._endValue + p * max._endValue;
		ret._endInnerExtent = q * min._endInnerExtent + p * max._endInnerExtent;
		ret._endOuterExtent = q * min._endOuterExtent + p * max._endOuterExtent;
		return ret;
	}
	,
	$type: new $.ig.Type('LinearGraphRectFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphScaleFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.ranges(new $.ig.List$1($.ig.LinearGraphRectFrame.prototype.$type, 0));
		this.ticks(new Array(0));
		this.minorTicks(new Array(0));
		this.labelValues(new Array(0));
		this.labels(new Array(0));
		this.labelWidths(new Array(0));
		this.labelHeights(new Array(0));
		this.labelXOffsets(new Array(0));
		this.labelYOffsets(new Array(0));
	},
	_ranges: null,
	ranges: function (value) {
		if (arguments.length === 1) {
			this._ranges = value;
			return value;
		} else {
			return this._ranges;
		}
	}
	,
	_scaleStartExtent: 0,
	scaleStartExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleStartExtent = value;
			return value;
		} else {
			return this._scaleStartExtent;
		}
	}
	,
	_scaleEndExtent: 0,
	scaleEndExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleEndExtent = value;
			return value;
		} else {
			return this._scaleEndExtent;
		}
	}
	,
	_scaleInnerExtent: 0,
	scaleInnerExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleInnerExtent = value;
			return value;
		} else {
			return this._scaleInnerExtent;
		}
	}
	,
	_scaleOuterExtent: 0,
	scaleOuterExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleOuterExtent = value;
			return value;
		} else {
			return this._scaleOuterExtent;
		}
	}
	,
	_ticks: null,
	ticks: function (value) {
		if (arguments.length === 1) {
			this._ticks = value;
			return value;
		} else {
			return this._ticks;
		}
	}
	,
	_tickStartExtent: 0,
	tickStartExtent: function (value) {
		if (arguments.length === 1) {
			this._tickStartExtent = value;
			return value;
		} else {
			return this._tickStartExtent;
		}
	}
	,
	_tickEndExtent: 0,
	tickEndExtent: function (value) {
		if (arguments.length === 1) {
			this._tickEndExtent = value;
			return value;
		} else {
			return this._tickEndExtent;
		}
	}
	,
	_tickBrush: null,
	tickBrush: function (value) {
		if (arguments.length === 1) {
			this._tickBrush = value;
			return value;
		} else {
			return this._tickBrush;
		}
	}
	,
	_tickStrokeThickness: 0,
	tickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._tickStrokeThickness = value;
			return value;
		} else {
			return this._tickStrokeThickness;
		}
	}
	,
	_minorTicks: null,
	minorTicks: function (value) {
		if (arguments.length === 1) {
			this._minorTicks = value;
			return value;
		} else {
			return this._minorTicks;
		}
	}
	,
	_minorTickStartExtent: 0,
	minorTickStartExtent: function (value) {
		if (arguments.length === 1) {
			this._minorTickStartExtent = value;
			return value;
		} else {
			return this._minorTickStartExtent;
		}
	}
	,
	_minorTickEndExtent: 0,
	minorTickEndExtent: function (value) {
		if (arguments.length === 1) {
			this._minorTickEndExtent = value;
			return value;
		} else {
			return this._minorTickEndExtent;
		}
	}
	,
	_minorTickBrush: null,
	minorTickBrush: function (value) {
		if (arguments.length === 1) {
			this._minorTickBrush = value;
			return value;
		} else {
			return this._minorTickBrush;
		}
	}
	,
	_minorTickStrokeThickness: 0,
	minorTickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._minorTickStrokeThickness = value;
			return value;
		} else {
			return this._minorTickStrokeThickness;
		}
	}
	,
	_labelValues: null,
	labelValues: function (value) {
		if (arguments.length === 1) {
			this._labelValues = value;
			return value;
		} else {
			return this._labelValues;
		}
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
	_labelWidths: null,
	labelWidths: function (value) {
		if (arguments.length === 1) {
			this._labelWidths = value;
			return value;
		} else {
			return this._labelWidths;
		}
	}
	,
	_labelHeights: null,
	labelHeights: function (value) {
		if (arguments.length === 1) {
			this._labelHeights = value;
			return value;
		} else {
			return this._labelHeights;
		}
	}
	,
	_labelXOffsets: null,
	labelXOffsets: function (value) {
		if (arguments.length === 1) {
			this._labelXOffsets = value;
			return value;
		} else {
			return this._labelXOffsets;
		}
	}
	,
	_labelYOffsets: null,
	labelYOffsets: function (value) {
		if (arguments.length === 1) {
			this._labelYOffsets = value;
			return value;
		} else {
			return this._labelYOffsets;
		}
	}
	,
	_labelExtent: 0,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this._labelExtent = value;
			return value;
		} else {
			return this._labelExtent;
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
	getPathGeometry: function (scaleStartPoint, breadth, length, orientation) {
		var $self = this;
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var scaleStartX = scaleStartPoint.__x;
		var scaleStartY = scaleStartPoint.__y;
		var tl, tr, br, bl;
		if (orientation == $.ig.LinearScaleOrientation.prototype.vertical) {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + breadth * $self.scaleInnerExtent(), __y: scaleStartY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + breadth * $self.scaleOuterExtent(), __y: scaleStartY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + breadth * $self.scaleOuterExtent(), __y: scaleStartY + length, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + breadth * $self.scaleInnerExtent(), __y: scaleStartY + length, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		} else {
			tl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX, __y: breadth * (1 - $self.scaleOuterExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			tr = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + length, __y: breadth * (1 - $self.scaleOuterExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			br = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX + length, __y: breadth * (1 - $self.scaleInnerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			bl = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: scaleStartX, __y: breadth * (1 - $self.scaleInnerExtent()), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
		}
		pf.__startPoint = tl.point();
		pf.__segments.add(tr);
		pf.__segments.add(br);
		pf.__segments.add(bl);
		pf.__isClosed = true;
		pg.figures().add(pf);
		return pg;
	}
	,
	interpolate: function (p, previous, next) {
		this.tickBrush($.ig.BrushUtil.prototype.getInterpolation(previous.tickBrush(), p, next.tickBrush(), $.ig.InterpolationMode.prototype.rGB));
		this.minorTickBrush($.ig.BrushUtil.prototype.getInterpolation(previous.minorTickBrush(), p, next.minorTickBrush(), $.ig.InterpolationMode.prototype.rGB));
		if (previous.fontBrush() == null && next.fontBrush() == null) {
			this.fontBrush(null);
		} else {
			this.fontBrush($.ig.BrushUtil.prototype.getInterpolation(previous.fontBrush(), p, next.fontBrush(), $.ig.InterpolationMode.prototype.rGB));
		}
		this.scaleStartExtent(previous.scaleStartExtent() + (next.scaleStartExtent() - previous.scaleStartExtent()) * p);
		this.scaleEndExtent(previous.scaleEndExtent() + (next.scaleEndExtent() - previous.scaleEndExtent()) * p);
		this.scaleInnerExtent(previous.scaleInnerExtent() + (next.scaleInnerExtent() - previous.scaleInnerExtent()) * p);
		this.scaleOuterExtent(previous.scaleOuterExtent() + (next.scaleOuterExtent() - previous.scaleOuterExtent()) * p);
		this.labelExtent(previous.labelExtent() + (next.labelExtent() - previous.labelExtent()) * p);
		this.tickStartExtent(previous.tickStartExtent() + (next.tickStartExtent() - previous.tickStartExtent()) * p);
		this.tickEndExtent(previous.tickEndExtent() + (next.tickEndExtent() - previous.tickEndExtent()) * p);
		this.minorTickStartExtent(previous.minorTickStartExtent() + (next.minorTickStartExtent() - previous.minorTickStartExtent()) * p);
		this.minorTickEndExtent(previous.minorTickEndExtent() + (next.minorTickEndExtent() - previous.minorTickEndExtent()) * p);
		this.tickStrokeThickness(previous.tickStrokeThickness() + (next.tickStrokeThickness() - previous.tickStrokeThickness()) * p);
		this.minorTickStrokeThickness(previous.minorTickStrokeThickness() + (next.minorTickStrokeThickness() - previous.minorTickStrokeThickness()) * p);
		this.ticks(this.interpolateArray(p, this.ticks(), previous.ticks(), next.ticks()));
		this.minorTicks(this.interpolateArray(p, this.minorTicks(), previous.minorTicks(), next.minorTicks()));
		this.labelValues(this.interpolateArray(p, this.labelValues(), previous.labelValues(), next.labelValues()));
		this.labelWidths(this.interpolateArray(p, this.labelWidths(), previous.labelWidths(), next.labelWidths()));
		this.labelHeights(this.interpolateArray(p, this.labelHeights(), previous.labelHeights(), next.labelHeights()));
		this.labelXOffsets(this.interpolateArray(p, this.labelXOffsets(), previous.labelXOffsets(), next.labelXOffsets()));
		this.labelYOffsets(this.interpolateArray(p, this.labelYOffsets(), previous.labelYOffsets(), next.labelYOffsets()));
		this.labels(this.interpolateStringArray(p, this.labels(), previous.labels(), next.labels()));
		this.interpolateRectFrames(p, this.ranges(), previous.ranges(), next.ranges());
	}
	,
	interpolateArray: function (p, dest, prev, next) {
		var maxCount = Math.max(prev.length, next.length);
		var minCount = Math.min(prev.length, next.length);
		if (dest.length != maxCount) {
			dest = new Array(maxCount);
		}
		var lastPrev = 0;
		if (prev.length > 0) {
			lastPrev = prev[prev.length - 1];
		}
		var lastNext = 0;
		if (next.length > 0) {
			lastNext = next[next.length - 1];
		}
		var usePrev = prev.length > next.length;
		var i = 0;
		for (i = 0; i < minCount; i++) {
			dest[i] = prev[i] + (next[i] - prev[i]) * p;
		}
		var prevVal;
		var nextVal;
		for (i = minCount; i < maxCount; i++) {
			if (usePrev) {
				prevVal = prev[i];
				nextVal = lastNext;
			} else {
				prevVal = lastPrev;
				nextVal = next[i];
			}
			dest[i] = prevVal + (nextVal - prevVal) * p;
		}
		return dest;
	}
	,
	interpolateStringArray: function (p, dest, prev, next) {
		var maxCount = Math.max(prev.length, next.length);
		var minCount = Math.min(prev.length, next.length);
		if (dest.length != maxCount) {
			dest = new Array(maxCount);
		}
		var lastPrev = null;
		if (prev.length > 0) {
			lastPrev = prev[prev.length - 1];
		}
		var lastNext = null;
		if (next.length > 0) {
			lastNext = next[next.length - 1];
		}
		var usePrev = prev.length > next.length;
		var i = 0;
		for (i = 0; i < minCount; i++) {
			dest[i] = next[i];
		}
		var prevVal;
		var nextVal;
		for (i = minCount; i < maxCount; i++) {
			if (usePrev) {
				prevVal = prev[i];
				nextVal = lastNext;
			} else {
				prevVal = lastPrev;
				nextVal = next[i];
			}
			if (nextVal != null) {
				dest[i] = nextVal;
			} else {
				dest[i] = prevVal;
			}
		}
		return dest;
	}
	,
	interpolateRectFrames: function (p, interpolatedFrames, previousFrames, nextFrames) {
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.LinearGraphRectFrame.prototype.$type, interpolatedFrames, p, previousFrames, nextFrames, function () { return new $.ig.LinearGraphRectFrame(); }, $.ig.LinearGraphRectFrame.prototype.interpolate);
	}
	,
	interpolateNeedleFrames: function (p, interpolatedFrames, previousFrames, nextFrames) {
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.LinearGraphNeedleFrame.prototype.$type, interpolatedFrames, p, previousFrames, nextFrames, function () { return new $.ig.LinearGraphNeedleFrame(); }, $.ig.LinearGraphNeedleFrame.prototype.interpolate);
	}
	,
	$type: new $.ig.Type('LinearGraphScaleFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphNeedlePreparer', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_parameters: null,
	parameters: function (value) {
		if (arguments.length === 1) {
			this._parameters = value;
			return value;
		} else {
			return this._parameters;
		}
	}
	,
	prepareNeedle: function (frame) {
		this.prepareNeedleHelper(frame);
	}
	,
	prepareNeedleHelper: function (frame) {
		this.resetFrame(frame);
		var p = this.getNeedleParameters();
		frame.needleStartExtent(p.outerExtent());
		frame.needleEndExtent(p.innerExtent());
		frame.needleWidth(0);
		if (!$.ig.util.isNaN(p.outerBaseWidth())) {
			frame.needleWidth(Math.max(frame.needleWidth(), p.outerBaseWidth()));
		}
		if (!$.ig.util.isNaN(p.innerBaseWidth())) {
			frame.needleWidth(Math.max(frame.needleWidth(), p.innerBaseWidth()));
		}
		var segmentsCount = 7;
		var segments = new Array(segmentsCount);
		var points = new Array(segmentsCount + 1);
		this.prepareSegmentsAndPoints(frame, segments, points, p);
		for (var i = 0; i < segmentsCount; i++) {
			var currPoints = segments[i]._segmentPoints;
			currPoints.add(points[i]);
			currPoints.add(points[i + 1]);
		}
	}
	,
	prepareSegmentsAndPoints: function (frame, segments, segmentPoints, p) {
		var innerExtent = p.innerExtent();
		var outerExtent = p.outerExtent();
		var innerPointExtent = p.innerPointExtent();
		var outerPointExtent = p.outerPointExtent();
		if (p.scaleOrientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			innerExtent = 1 - innerExtent;
			outerExtent = 1 - outerExtent;
			innerPointExtent = 1 - innerPointExtent;
			outerPointExtent = 1 - outerPointExtent;
			segments[0] = frame.outerBase();
			segments[1] = frame.outerPoint();
			segments[2] = frame.feature();
			segments[3] = frame.innerPoint();
			segments[4] = frame.innerBase();
			segments[5] = frame.innerPointUnderside();
			segments[6] = frame.featureUnderside();
			segmentPoints[0] = { __x: (-1) * p.outerBaseWidth(), __y: outerExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[1] = { __x: p.outerBaseWidth(), __y: outerExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[2] = { __x: p.outerPointWidth(), __y: outerPointExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[3] = { __x: p.innerPointWidth(), __y: innerPointExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[4] = { __x: p.innerBaseWidth(), __y: innerExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[5] = { __x: (-1) * p.innerBaseWidth(), __y: innerExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[6] = { __x: (-1) * p.innerPointWidth(), __y: innerPointExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[7] = { __x: (-1) * p.outerPointWidth(), __y: outerPointExtent, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		} else {
			segments[0] = frame.outerBase();
			segments[1] = frame.outerPoint();
			segments[2] = frame.feature();
			segments[3] = frame.innerPoint();
			segments[4] = frame.innerBase();
			segments[5] = frame.innerPointUnderside();
			segments[6] = frame.featureUnderside();
			segmentPoints[0] = { __x: outerExtent, __y: (-1) * p.outerBaseWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[1] = { __x: outerExtent, __y: p.outerBaseWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[2] = { __x: outerPointExtent, __y: p.outerPointWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[3] = { __x: innerPointExtent, __y: p.innerPointWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[4] = { __x: innerExtent, __y: p.innerBaseWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[5] = { __x: innerExtent, __y: (-1) * p.innerBaseWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[6] = { __x: innerPointExtent, __y: (-1) * p.innerPointWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			segmentPoints[7] = { __x: outerPointExtent, __y: (-1) * p.outerPointWidth(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getNeedleParameters: function () {
		var p = this.parameters();
		var defaultOuterExtent = 0.85;
		var defaultInnerExtent = 0.25;
		var defaultNeedleInnerPointExtent = 0.45;
		var defaultWidth = p.defaultWidth();
		var defaultTrapezoidInnerBaseWidthRatio = 0.5;
		var innerExtent = p.innerExtent();
		var outerExtent = p.outerExtent();
		var innerPointExtent = p.innerPointExtent();
		var outerPointExtent = p.outerPointExtent();
		var innerBaseWidth = p.innerBaseWidth();
		var outerBaseWidth = p.outerBaseWidth();
		var innerPointWidth = p.innerPointWidth();
		var outerPointWidth = p.outerPointWidth();
		if ($.ig.util.isNaN(innerExtent)) {
			innerExtent = defaultInnerExtent;
		}
		if ($.ig.util.isNaN(outerExtent)) {
			outerExtent = defaultOuterExtent;
		}
		if ($.ig.util.isNaN(innerPointExtent)) {
			if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.needle) {
				innerPointExtent = defaultNeedleInnerPointExtent;
			} else {
				innerPointExtent = defaultInnerExtent;
			}
		}
		if ($.ig.util.isNaN(outerPointExtent)) {
			outerPointExtent = defaultOuterExtent;
		}
		if ($.ig.util.isNaN(outerBaseWidth)) {
			outerBaseWidth = defaultWidth;
		}
		if ($.ig.util.isNaN(innerBaseWidth)) {
			if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.trapezoid) {
				innerBaseWidth = outerBaseWidth * defaultTrapezoidInnerBaseWidthRatio;
			} else {
				innerBaseWidth = defaultWidth;
			}
		}
		if ($.ig.util.isNaN(innerPointWidth)) {
			innerPointWidth = defaultWidth;
		}
		if ($.ig.util.isNaN(outerPointWidth)) {
			outerPointWidth = defaultWidth;
		}
		switch (p.needleShape()) {
			case $.ig.LinearGraphNeedleShape.prototype.rectangle:
			case $.ig.LinearGraphNeedleShape.prototype.triangle:
			case $.ig.LinearGraphNeedleShape.prototype.trapezoid:
				outerPointExtent = outerExtent;
				innerPointExtent = innerExtent;
				break;
		}
		if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.needle) {
			innerBaseWidth = 0;
			innerPointWidth = outerBaseWidth;
			outerPointWidth = outerBaseWidth;
			outerPointExtent = outerExtent;
		}
		if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.triangle) {
			innerBaseWidth = 0;
			innerPointWidth = 0;
			outerPointWidth = outerBaseWidth;
		}
		if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.trapezoid) {
			innerPointWidth = innerBaseWidth;
			outerPointWidth = outerBaseWidth;
		}
		if (p.needleShape() == $.ig.LinearGraphNeedleShape.prototype.rectangle) {
			var max = Math.max(innerBaseWidth, outerBaseWidth);
			innerBaseWidth = max;
			outerBaseWidth = max;
			innerPointWidth = max;
			outerPointWidth = max;
		}
		p.innerExtent(innerExtent);
		p.outerExtent(outerExtent);
		p.innerPointExtent(innerPointExtent);
		p.outerPointExtent(outerPointExtent);
		p.innerBaseWidth(innerBaseWidth);
		p.outerBaseWidth(outerBaseWidth);
		p.innerPointWidth(innerPointWidth);
		p.outerPointWidth(outerPointWidth);
		return p;
	}
	,
	resetFrame: function (frame) {
		frame.innerBase().reset();
		frame.innerPoint().reset();
		frame.feature().reset();
		frame.outerPoint().reset();
		frame.outerBase().reset();
		frame.outerPointUnderside().reset();
		frame.featureUnderside().reset();
		frame.innerPointUnderside().reset();
		frame.cutout().reset();
		frame.cutoutUnderside().reset();
	}
	,
	$type: new $.ig.Type('LinearGraphNeedlePreparer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphNeedleParameters', 'Object', {
	init: function () {
		this.__minExtent = NaN;
		this.__maxExtent = NaN;
		this.__innerBaseWidth = NaN;
		this.__outerBaseWidth = NaN;
		this.__innerPointWidth = NaN;
		this.__outerPointWidth = NaN;
		this.__innerPointExtent = NaN;
		this.__outerPointExtent = NaN;
		this.__defaultWidth = NaN;
		this.__needleShape = $.ig.LinearGraphNeedleShape.prototype.needle;
		$.ig.Object.prototype.init.call(this);
	},
	_scaleOrientation: 0,
	scaleOrientation: function (value) {
		if (arguments.length === 1) {
			this._scaleOrientation = value;
			return value;
		} else {
			return this._scaleOrientation;
		}
	}
	,
	_isScaleInverted: false,
	isScaleInverted: function (value) {
		if (arguments.length === 1) {
			this._isScaleInverted = value;
			return value;
		} else {
			return this._isScaleInverted;
		}
	}
	,
	__minExtent: 0,
	outerExtent: function (value) {
		if (arguments.length === 1) {
			if (this.__minExtent != value) {
				this.__minExtent = value;
				this.onPropertyChanged("OuterExtent");
			}
			return value;
		} else {
			return this.__minExtent;
		}
	}
	,
	__maxExtent: 0,
	innerExtent: function (value) {
		if (arguments.length === 1) {
			if (this.__maxExtent != value) {
				this.__maxExtent = value;
				this.onPropertyChanged("InnerExtent");
			}
			return value;
		} else {
			return this.__maxExtent;
		}
	}
	,
	__innerBaseWidth: 0,
	innerBaseWidth: function (value) {
		if (arguments.length === 1) {
			if (this.__innerBaseWidth != value) {
				this.__innerBaseWidth = value;
				this.onPropertyChanged("InnerBaseWidth");
			}
			return value;
		} else {
			return this.__innerBaseWidth;
		}
	}
	,
	__outerBaseWidth: 0,
	outerBaseWidth: function (value) {
		if (arguments.length === 1) {
			if (this.__outerBaseWidth != value) {
				this.__outerBaseWidth = value;
				this.onPropertyChanged("OuterBaseWidth");
			}
			return value;
		} else {
			return this.__outerBaseWidth;
		}
	}
	,
	__innerPointWidth: 0,
	innerPointWidth: function (value) {
		if (arguments.length === 1) {
			if (this.__innerPointWidth != value) {
				this.__innerPointWidth = value;
				this.onPropertyChanged("InnerPointWidth");
			}
			return value;
		} else {
			return this.__innerPointWidth;
		}
	}
	,
	__outerPointWidth: 0,
	outerPointWidth: function (value) {
		if (arguments.length === 1) {
			if (this.__outerPointWidth != value) {
				this.__outerPointWidth = value;
				this.onPropertyChanged("OuterPointWidth");
			}
			return value;
		} else {
			return this.__outerPointWidth;
		}
	}
	,
	__innerPointExtent: 0,
	innerPointExtent: function (value) {
		if (arguments.length === 1) {
			if (this.__innerPointExtent != value) {
				this.__innerPointExtent = value;
				this.onPropertyChanged("InnerPointExtent");
			}
			return value;
		} else {
			return this.__innerPointExtent;
		}
	}
	,
	__outerPointExtent: 0,
	outerPointExtent: function (value) {
		if (arguments.length === 1) {
			if (this.__outerPointExtent != value) {
				this.__outerPointExtent = value;
				this.onPropertyChanged("OuterPointExtent");
			}
			return value;
		} else {
			return this.__outerPointExtent;
		}
	}
	,
	__defaultWidth: 0,
	defaultWidth: function (value) {
		if (arguments.length === 1) {
			if (this.__defaultWidth != value) {
				this.__defaultWidth = value;
				this.onPropertyChanged("DefaultWidth");
			}
			return value;
		} else {
			return this.__defaultWidth;
		}
	}
	,
	__needleShape: 0,
	needleShape: function (value) {
		if (arguments.length === 1) {
			if (this.__needleShape != value) {
				this.__needleShape = value;
				this.onPropertyChanged("NeedleShape");
			}
			return value;
		} else {
			return this.__needleShape;
		}
	}
	,
	clone: function () {
		var ret = new $.ig.LinearGraphNeedleParameters();
		ret.outerExtent(this.outerExtent());
		ret.innerExtent(this.innerExtent());
		ret.innerBaseWidth(this.innerBaseWidth());
		ret.outerBaseWidth(this.outerBaseWidth());
		ret.innerPointExtent(this.innerPointExtent());
		ret.outerPointExtent(this.outerPointExtent());
		ret.innerPointWidth(this.innerPointWidth());
		ret.outerPointWidth(this.outerPointWidth());
		ret.needleShape(this.needleShape());
		ret.defaultWidth(this.defaultWidth());
		return ret;
	}
	,
	onPropertyChanged: function (propertyName) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
	}
	,
	propertyChanged: null,
	$type: new $.ig.Type('LinearGraphNeedleParameters', $.ig.Object.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('LinearGraphScaler', 'Object', {
	__scaleStartValue: 0,
	__scaleEndValue: 0,
	__minimumValue: 0,
	__maximumValue: 0,
	__isScaleInverted: false,
	__startValue: 0,
	__endValue: 0,
	startValue: function () {
		return this.__startValue;
	}
	,
	endValue: function () {
		return this.__endValue;
	}
	,
	init: function (minimumValue, maximumValue, scaleStartValue, scaleEndValue, isScaleInverted) {
		$.ig.Object.prototype.init.call(this);
		this.__scaleStartValue = scaleStartValue;
		this.__scaleEndValue = scaleEndValue;
		this.__isScaleInverted = isScaleInverted;
		this.__minimumValue = minimumValue;
		this.__maximumValue = maximumValue;
		this.__startValue = scaleStartValue;
		this.__endValue = scaleEndValue;
	},
	scale: function (value) {
		var scale = (value - this.__minimumValue) / (this.__maximumValue - this.__minimumValue);
		if (this.__isScaleInverted) {
			scale = 1 - scale;
		}
		var scaledValue = this.__startValue + (this.__endValue - this.__startValue) * scale;
		scaledValue = Math.round(scaledValue * 100) / 100;
		return scaledValue;
	}
	,
	unscale: function (value) {
		var scale = (value - this.__startValue) / (this.__endValue - this.__startValue);
		if (this.__isScaleInverted) {
			scale = 1 - scale;
		}
		var unscaled = this.__minimumValue + (this.__maximumValue - this.__minimumValue) * scale;
		return unscaled;
	}
	,
	$type: new $.ig.Type('LinearGraphScaler', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_scalePath: null,
	scalePath: function (value) {
		if (arguments.length === 1) {
			this._scalePath = value;
			return value;
		} else {
			return this._scalePath;
		}
	}
	,
	_backingPath: null,
	backingPath: function (value) {
		if (arguments.length === 1) {
			this._backingPath = value;
			return value;
		} else {
			return this._backingPath;
		}
	}
	,
	_underlayPath: null,
	underlayPath: function (value) {
		if (arguments.length === 1) {
			this._underlayPath = value;
			return value;
		} else {
			return this._underlayPath;
		}
	}
	,
	_overlayPath: null,
	overlayPath: function (value) {
		if (arguments.length === 1) {
			this._overlayPath = value;
			return value;
		} else {
			return this._overlayPath;
		}
	}
	,
	_scale: null,
	scale: function (value) {
		if (arguments.length === 1) {
			this._scale = value;
			return value;
		} else {
			return this._scale;
		}
	}
	,
	_scaleLabels: null,
	scaleLabels: function (value) {
		if (arguments.length === 1) {
			this._scaleLabels = value;
			return value;
		} else {
			return this._scaleLabels;
		}
	}
	,
	_scaleTickmarks: null,
	scaleTickmarks: function (value) {
		if (arguments.length === 1) {
			this._scaleTickmarks = value;
			return value;
		} else {
			return this._scaleTickmarks;
		}
	}
	,
	_ranges: null,
	ranges: function (value) {
		if (arguments.length === 1) {
			this._ranges = value;
			return value;
		} else {
			return this._ranges;
		}
	}
	,
	_toolTip: null,
	toolTip: function (value) {
		if (arguments.length === 1) {
			this._toolTip = value;
			return value;
		} else {
			return this._toolTip;
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
	scaleByViewport: function () {
		if (this.scalePath() != null) {
			this.scalePath().scaleByViewport(this.viewport());
		}
		if (this.backingPath() != null) {
			this.backingPath().scaleByViewport(this.viewport());
		}
		if (this.underlayPath() != null) {
			this.underlayPath().scaleByViewport(this.viewport());
		}
		if (this.overlayPath() != null) {
			this.overlayPath().scaleByViewport(this.viewport());
		}
		if (this.scaleLabels() != null) {
			var en = this.scaleLabels().getEnumerator();
			while (en.moveNext()) {
				var label = en.current();
				label.scaleByViewport(this.viewport());
			}
		}
		if (this.scaleTickmarks() != null) {
			var en1 = this.scaleTickmarks().getEnumerator();
			while (en1.moveNext()) {
				var tickmark = en1.current();
				if (tickmark.tickPath() != null) {
					tickmark.tickPath().scaleByViewport(this.viewport());
				}
			}
		}
		if (this.ranges() != null) {
			var en2 = this.ranges().getEnumerator();
			while (en2.moveNext()) {
				var range = en2.current();
				if (range.rangePath() != null) {
					range.rangePath().scaleByViewport(this.viewport());
				}
			}
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.scalePath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("scalePath: ");
			sb.appendLine1(this.scalePath().serialize());
		}
		if (this.backingPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("backingPath: ");
			sb.appendLine1(this.backingPath().serialize());
		}
		if (this.underlayPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("underlayPath: ");
			sb.appendLine1(this.underlayPath().serialize());
		}
		if (this.overlayPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("overlayPath: ");
			sb.appendLine1(this.overlayPath().serialize());
		}
		if (this.scaleLabels() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst = true;
			sb.append5("scaleLabels: [");
			var en = this.scaleLabels().getEnumerator();
			while (en.moveNext()) {
				var label = en.current();
				if (subFirst) {
					subFirst = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(label.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.scaleTickmarks() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst1 = true;
			sb.append5("scaleTickmarks: [");
			var en1 = this.scaleTickmarks().getEnumerator();
			while (en1.moveNext()) {
				var tickmark = en1.current();
				if (subFirst1) {
					subFirst1 = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(tickmark.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.ranges() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst2 = true;
			sb.append5("ranges: [");
			var en2 = this.ranges().getEnumerator();
			while (en2.moveNext()) {
				var range = en2.current();
				if (subFirst2) {
					subFirst2 = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(range.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.name() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("name: '");
			sb.append5(this.name());
			sb.appendLine1("'");
		}
		if (this.toolTip() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("toolTip: ");
			sb.append5(this.toolTip().serialize());
		}
		if (first) {
			first = false;
		} else {
			sb.append5(", ");
		}
		sb.append5("viewport: {");
		sb.append5("left: " + this.viewport().left() + ", top: " + this.viewport().top() + ", width: " + this.viewport().width() + ", height: " + this.viewport().height());
		sb.appendLine1("}");
		sb.append5("}");
		return sb.toString();
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
	$type: new $.ig.Type('LinearGraphVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearScaleVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	$type: new $.ig.Type('LinearScaleVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphScaleLabelVisualData', 'Object', {
	init: function () {
		this._labelSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
	},
	_labelValue: null,
	labelValue: function (value) {
		if (arguments.length === 1) {
			this._labelValue = value;
			return value;
		} else {
			return this._labelValue;
		}
	}
	,
	_labelSize: null,
	labelSize: function (value) {
		if (arguments.length === 1) {
			this._labelSize = value;
			return value;
		} else {
			return this._labelSize;
		}
	}
	,
	_labelPosition: null,
	labelPosition: function (value) {
		if (arguments.length === 1) {
			this._labelPosition = value;
			return value;
		} else {
			return this._labelPosition;
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
	scaleByViewport: function (viewport) {
		this.labelPosition({ __x: (this.labelPosition().__x - viewport.left()) / viewport.width(), __y: (this.labelPosition().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.labelSize(new $.ig.Size(1, (this.labelSize().width()) / viewport.width(), (this.labelSize().height()) / viewport.height()));
	}
	,
	serialize: function () {
		return "{ labelValue: '" + this.labelValue() + "', labelSize: { width: " + this.labelSize().width() + ", height: " + this.labelSize().height() + "}, labelPosition: { x: " + this.labelPosition().__x + ", y: " + this.labelPosition().__y + "}, appearance: " + this.appearance().serialize() + "}";
	}
	,
	$type: new $.ig.Type('LinearGraphScaleLabelVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphScaleLabelVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.LinearGraphScaleLabelVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('LinearGraphScaleLabelVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.LinearGraphScaleLabelVisualData.prototype.$type))
}, true);

$.ig.util.defType('LinearGraphScaleTickmarkVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_tickPath: null,
	tickPath: function (value) {
		if (arguments.length === 1) {
			this._tickPath = value;
			return value;
		} else {
			return this._tickPath;
		}
	}
	,
	serialize: function () {
		return "{ tickPath: " + this.tickPath().serialize() + "}";
	}
	,
	$type: new $.ig.Type('LinearGraphScaleTickmarkVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphScaleTickmarkVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.LinearGraphScaleTickmarkVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('LinearGraphScaleTickmarkVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.LinearGraphScaleTickmarkVisualData.prototype.$type))
}, true);

$.ig.util.defType('ToolTipItemVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	serialize: function () {
	}
	,
	$type: new $.ig.Type('ToolTipItemVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphRangeVisualData', 'ToolTipItemVisualData', {
	init: function () {
		$.ig.ToolTipItemVisualData.prototype.init.call(this);
	},
	_rangePath: null,
	rangePath: function (value) {
		if (arguments.length === 1) {
			this._rangePath = value;
			return value;
		} else {
			return this._rangePath;
		}
	}
	,
	serialize: function () {
		return "{ rangePath: " + this.rangePath().serialize() + ", type: '" + this.type() + "'}";
	}
	,
	type: function () {
		return "range";
	}
	,
	$type: new $.ig.Type('LinearGraphRangeVisualData', $.ig.ToolTipItemVisualData.prototype.$type)
}, true);

$.ig.util.defType('LinearGraphRangeVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.LinearGraphRangeVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('LinearGraphRangeVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.LinearGraphRangeVisualData.prototype.$type))
}, true);

$.ig.util.defType('LinearGraphToolTipVisualData', 'Object', {
	init: function () {
		this._outline = new $.ig.Color();
		this._itemBrush = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
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
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.append5("{ item: ");
		if (this.item() != null) {
			sb.append5(this.item().serialize());
		}
		sb.append5(", ");
		sb.append5("outline: ");
		sb.append5($.ig.AppearanceHelper.prototype.serializeColor(this.outline()));
		sb.append5(", ");
		sb.append5("itemBrush: ");
		sb.append5($.ig.AppearanceHelper.prototype.serializeColor(this.itemBrush()));
		sb.append5(", ");
		sb.append5("thickness: ");
		sb.append5(this.thickness().toString());
		sb.append5("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LinearGraphToolTipVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamLinearGraphRange', 'DependencyObject', {
	init: function () {
		$.ig.DependencyObject.prototype.init.call(this);
	},
	propertyUpdateOverride: function (propertyName, oldValue, newValue) {
	}
	,
	onPropertyChanged: function (propertyName, oldValue, newValue) {
		this.propertyUpdateOverride(propertyName, oldValue, newValue);
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
	}
	,
	propertyChanged: null,
	name: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.nameProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.nameProperty);
		}
	}
	,
	brush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.brushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.brushProperty);
		}
	}
	,
	outline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.outlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.outlineProperty);
		}
	}
	,
	startValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.startValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.startValueProperty);
		}
	}
	,
	endValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.endValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.endValueProperty);
		}
	}
	,
	innerStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.innerStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.innerStartExtentProperty);
		}
	}
	,
	innerEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.innerEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.innerEndExtentProperty);
		}
	}
	,
	outerStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.outerStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.outerStartExtentProperty);
		}
	}
	,
	outerEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.outerEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.outerEndExtentProperty);
		}
	}
	,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGraphRange.prototype.strokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGraphRange.prototype.strokeThicknessProperty);
		}
	}
	,
	$type: new $.ig.Type('XamLinearGraphRange', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('LinearGraphRangeCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.XamLinearGraphRange.prototype.$type, 0);
	},
	$type: new $.ig.Type('LinearGraphRangeCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.XamLinearGraphRange.prototype.$type))
}, true);

$.ig.util.defType('LinearGraphTooltipDataContext', 'DependencyObject', {
	init: function () {
		$.ig.DependencyObject.prototype.init.call(this);
	},
	item: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.itemProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.itemProperty);
		}
	}
	,
	itemName: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.itemNameProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.itemNameProperty);
		}
	}
	,
	outline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.outlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.outlineProperty);
		}
	}
	,
	brush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.itemBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.itemBrushProperty);
		}
	}
	,
	thickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.thicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.thicknessProperty);
		}
	}
	,
	label: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LinearGraphTooltipDataContext.prototype.labelProperty, value);
			return value;
		} else {
			return this.getValue($.ig.LinearGraphTooltipDataContext.prototype.labelProperty);
		}
	}
	,
	raisePropertyChanged: function (propertyName, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
	}
	,
	propertyChanged: null,
	$type: new $.ig.Type('LinearGraphTooltipDataContext', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('ToolTipInfo', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_type: null,
	type: function (value) {
		if (arguments.length === 1) {
			this._type = value;
			return value;
		} else {
			return this._type;
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
	$type: new $.ig.Type('ToolTipInfo', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FormatLinearGraphLabelEventArgs', 'EventArgs', {
	init: function () {
		$.ig.EventArgs.prototype.init.call(this);
	},
	actualMinimumValue: 0,
	actualMaximumValue: 0,
	value: 0,
	label: null,
	$type: new $.ig.Type('FormatLinearGraphLabelEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('AlignLinearGraphLabelEventArgs', 'FormatLinearGraphLabelEventArgs', {
	init: function () {
		$.ig.FormatLinearGraphLabelEventArgs.prototype.init.call(this);
	},
	width: 0,
	height: 0,
	offsetX: 0,
	offsetY: 0,
	$type: new $.ig.Type('AlignLinearGraphLabelEventArgs', $.ig.FormatLinearGraphLabelEventArgs.prototype.$type)
}, true);

$.ig.util.defType('LinearGaugeFrame', 'LinearGraphFrame', {
	init: function () {
		$.ig.LinearGraphFrame.prototype.init.call(this);
		this.scaleFrame(new $.ig.LinearGaugeScaleFrame());
	},
	_scaleFrame: null,
	scaleFrame: function (value) {
		if (arguments.length === 1) {
			this._scaleFrame = value;
			return value;
		} else {
			return this._scaleFrame;
		}
	}
	,
	$type: new $.ig.Type('LinearGaugeFrame', $.ig.LinearGraphFrame.prototype.$type)
}, true);

$.ig.util.defType('LinearGaugeScaleFrame', 'LinearGraphScaleFrame', {
	init: function () {
		$.ig.LinearGraphScaleFrame.prototype.init.call(this);
		this.needle(new $.ig.LinearGraphNeedleFrame());
	},
	_needle: null,
	needle: function (value) {
		if (arguments.length === 1) {
			this._needle = value;
			return value;
		} else {
			return this._needle;
		}
	}
	,
	interpolate: function (p, previous, next) {
		$.ig.LinearGraphScaleFrame.prototype.interpolate.call(this, p, previous, next);
		this.needle($.ig.LinearGraphNeedleFrame.prototype.interpolate(p, 1 - p, (previous).needle(), (next).needle()));
	}
	,
	$type: new $.ig.Type('LinearGaugeScaleFrame', $.ig.LinearGraphScaleFrame.prototype.$type)
}, true);

$.ig.util.defType('LinearNeedleVisualData', 'ToolTipItemVisualData', {
	init: function () {
		$.ig.ToolTipItemVisualData.prototype.init.call(this);
	},
	_value: 0,
	value: function (value) {
		if (arguments.length === 1) {
			this._value = value;
			return value;
		} else {
			return this._value;
		}
	}
	,
	serialize: function () {
		return "{ value: " + this.value().toString() + ", type: '" + this.type() + "'}";
	}
	,
	type: function () {
		return "needle";
	}
	,
	$type: new $.ig.Type('LinearNeedleVisualData', $.ig.ToolTipItemVisualData.prototype.$type)
}, true);

$.ig.util.defType('LinearGaugeVisualData', 'LinearGraphVisualData', {
	init: function () {
		$.ig.LinearGraphVisualData.prototype.init.call(this);
	},
	_needlePath: null,
	needlePath: function (value) {
		if (arguments.length === 1) {
			this._needlePath = value;
			return value;
		} else {
			return this._needlePath;
		}
	}
	,
	_needle: null,
	needle: function (value) {
		if (arguments.length === 1) {
			this._needle = value;
			return value;
		} else {
			return this._needle;
		}
	}
	,
	scaleByViewport: function () {
		$.ig.LinearGraphVisualData.prototype.scaleByViewport.call(this);
		if (this.needlePath() != null) {
			this.needlePath().scaleByViewport(this.viewport());
		}
	}
	,
	serialize: function () {
		var baseSerialize = $.ig.LinearGraphVisualData.prototype.serialize.call(this);
		var sb = new $.ig.StringBuilder(0);
		var first = baseSerialize.length == 0;
		if (this.needlePath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("needlePath: ");
			sb.appendLine1(this.needlePath().serialize());
		}
		if (this.needle() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("needle: ");
			sb.appendLine1(this.needle().serialize());
		}
		if (first) {
			return baseSerialize;
		}
		sb.append5("}");
		return baseSerialize.substr(0, baseSerialize.length - 1) + sb.toString();
	}
	,
	$type: new $.ig.Type('LinearGaugeVisualData', $.ig.LinearGraphVisualData.prototype.$type)
}, true);

$.ig.util.defType('XamLinearGauge', 'Control', {
	init: function () {
		var $self = this;
		this.__transitionProgress = 0;
		this.__actualMinimumValue = 0;
		this.__actualMaximumValue = 100;
		this.__isDragging = false;
		this.__displayedLabels = new $.ig.List$1($.ig.TextBlock.prototype.$type, 0);
		this.__displayedTitles = new $.ig.List$1($.ig.TextBlock.prototype.$type, 0);
		this.__displayedTicks = new $.ig.List$1($.ig.Path.prototype.$type, 0);
		this.__displayedRanges = new $.ig.List$1($.ig.Path.prototype.$type, 0);
		$.ig.Control.prototype.init.call(this);
		this.view(new $.ig.XamLinearGaugeView(this));
		this.view().onInit();
		this.__viewport = $.ig.Rect.prototype.empty();
		this.__scaleViewport = $.ig.Rect.prototype.empty();
		this.__backingViewport = $.ig.Rect.prototype.empty();
		this.previousFrame(new $.ig.LinearGaugeFrame());
		this.nextFrame(new $.ig.LinearGaugeFrame());
		this.currentFrame(new $.ig.LinearGaugeFrame());
		this.ranges(new $.ig.LinearGraphRangeCollection());
		this.isDirty(true);
		this.animator(new $.ig.DoubleAnimator(0, 1, this.transitionDuration()));
		var $t = this.animator();
		$t.propertyChanged = $.ig.Delegate.prototype.combine($t.propertyChanged, this.animator_PropertyChanged.runOn(this));
		this.defaultStyleKey($.ig.XamLinearGauge.prototype.$type);
		this.rangeShapes((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().rangePathActivate.runOn($self.view()));
			$ret.deactivate($self.view().rangePathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().rangePathDestroy.runOn($self.view()));
			$ret.create($self.view().rangePathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.ticks((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().pathActivate.runOn($self.view()));
			$ret.deactivate($self.view().pathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().pathDestroy.runOn($self.view()));
			$ret.create($self.view().pathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.minorTicks((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().pathActivate.runOn($self.view()));
			$ret.deactivate($self.view().pathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().pathDestroy.runOn($self.view()));
			$ret.create($self.view().pathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.labels((function () {
			var $ret = new $.ig.StackPool$1($.ig.TextBlock.prototype.$type);
			$ret.activate($self.view().labelActivate.runOn($self.view()));
			$ret.deactivate($self.view().labelDeactivate.runOn($self.view()));
			$ret.destroy($self.view().labelDestroy.runOn($self.view()));
			$ret.create($self.view().labelCreate.runOn($self.view()));
			return $ret;
		}()));
		this.titles((function () {
			var $ret = new $.ig.StackPool$1($.ig.TextBlock.prototype.$type);
			$ret.activate($self.view().titleActivate.runOn($self.view()));
			$ret.deactivate($self.view().titleDeactivate.runOn($self.view()));
			$ret.destroy($self.view().titleDestroy.runOn($self.view()));
			$ret.create($self.view().titleCreate.runOn($self.view()));
			return $ret;
		}()));
	},
	_animator: null,
	animator: function (value) {
		if (arguments.length === 1) {
			this._animator = value;
			return value;
		} else {
			return this._animator;
		}
	}
	,
	animator_PropertyChanged: function (sender, e) {
		this.transitionProgress(this.animator().transitionProgress());
	}
	,
	animating: function () {
		return this.animator().animationActive();
	}
	,
	__transitionProgress: 0,
	transitionProgress: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__transitionProgress;
			this.__transitionProgress = value;
			this.onPropertyChanged($.ig.XamLinearGauge.prototype.transitionProgressPropertyName, oldValue, this.__transitionProgress);
			return value;
		} else {
			return this.__transitionProgress;
		}
	}
	,
	startAnimation: function () {
		this.__transitionProgress = 0;
		this.animator().stop();
		this.animator().start();
	}
	,
	shouldAnimate: function () {
		return this.transitionDuration() > 0;
	}
	,
	formatLabel: null,
	alignLabel: null,
	valueChanged: null,
	_previousFrame: null,
	previousFrame: function (value) {
		if (arguments.length === 1) {
			this._previousFrame = value;
			return value;
		} else {
			return this._previousFrame;
		}
	}
	,
	_nextFrame: null,
	nextFrame: function (value) {
		if (arguments.length === 1) {
			this._nextFrame = value;
			return value;
		} else {
			return this._nextFrame;
		}
	}
	,
	_currentFrame: null,
	currentFrame: function (value) {
		if (arguments.length === 1) {
			this._currentFrame = value;
			return value;
		} else {
			return this._currentFrame;
		}
	}
	,
	_scaler: null,
	scaler: function (value) {
		if (arguments.length === 1) {
			this._scaler = value;
			return value;
		} else {
			return this._scaler;
		}
	}
	,
	orientation: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.orientationProperty, $.ig.LinearScaleOrientation.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamLinearGauge.prototype.orientationProperty));
		}
	}
	,
	ranges: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.rangesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.rangesProperty);
		}
	}
	,
	rangeBrushes: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.rangeBrushesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.rangeBrushesProperty);
		}
	}
	,
	rangeOutlines: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.rangeOutlinesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.rangeOutlinesProperty);
		}
	}
	,
	minimumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minimumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minimumValueProperty);
		}
	}
	,
	__actualMinimumValue: 0,
	actualMinimumValue: function (value) {
		if (arguments.length === 1) {
			this.__actualMinimumValue = value;
			return value;
		} else {
			return this.__actualMinimumValue;
		}
	}
	,
	maximumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.maximumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.maximumValueProperty);
		}
	}
	,
	__actualMaximumValue: 0,
	actualMaximumValue: function (value) {
		if (arguments.length === 1) {
			this.__actualMaximumValue = value;
			return value;
		} else {
			return this.__actualMaximumValue;
		}
	}
	,
	value: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.valueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.valueProperty);
		}
	}
	,
	needleShape: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleShapeProperty, $.ig.LinearGraphNeedleShape.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamLinearGauge.prototype.needleShapeProperty));
		}
	}
	,
	needleName: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleNameProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleNameProperty);
		}
	}
	,
	rangeInnerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.rangeInnerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.rangeInnerExtentProperty);
		}
	}
	,
	scaleInnerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleInnerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleInnerExtentProperty);
		}
	}
	,
	rangeOuterExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.rangeOuterExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.rangeOuterExtentProperty);
		}
	}
	,
	scaleOuterExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleOuterExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleOuterExtentProperty);
		}
	}
	,
	needleInnerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleInnerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleInnerExtentProperty);
		}
	}
	,
	needleOuterExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleOuterExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleOuterExtentProperty);
		}
	}
	,
	needleInnerBaseWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleInnerBaseWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleInnerBaseWidthProperty);
		}
	}
	,
	needleOuterBaseWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleOuterBaseWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleOuterBaseWidthProperty);
		}
	}
	,
	needleInnerPointWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleInnerPointWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleInnerPointWidthProperty);
		}
	}
	,
	needleOuterPointWidth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleOuterPointWidthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleOuterPointWidthProperty);
		}
	}
	,
	needleInnerPointExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleInnerPointExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleInnerPointExtentProperty);
		}
	}
	,
	needleOuterPointExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleOuterPointExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleOuterPointExtentProperty);
		}
	}
	,
	interval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.intervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.intervalProperty);
		}
	}
	,
	ticksPostInitial: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.ticksPostInitialProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.ticksPostInitialProperty);
		}
	}
	,
	ticksPreTerminal: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.ticksPreTerminalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.ticksPreTerminalProperty);
		}
	}
	,
	labelInterval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.labelIntervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.labelIntervalProperty);
		}
	}
	,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.labelExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.labelExtentProperty);
		}
	}
	,
	labelsPostInitial: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.labelsPostInitialProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.labelsPostInitialProperty);
		}
	}
	,
	labelsPreTerminal: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.labelsPreTerminalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.labelsPreTerminalProperty);
		}
	}
	,
	minorTickCount: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minorTickCountProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minorTickCountProperty);
		}
	}
	,
	tickStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.tickStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.tickStartExtentProperty);
		}
	}
	,
	tickEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.tickEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.tickEndExtentProperty);
		}
	}
	,
	tickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.tickStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.tickStrokeThicknessProperty);
		}
	}
	,
	tickBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.tickBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.tickBrushProperty);
		}
	}
	,
	fontBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.fontBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.fontBrushProperty);
		}
	}
	,
	needleBreadth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleBreadthProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleBreadthProperty);
		}
	}
	,
	needleBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleBrushProperty);
		}
	}
	,
	needleOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleOutlineProperty);
		}
	}
	,
	needleStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.needleStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.needleStrokeThicknessProperty);
		}
	}
	,
	minorTickStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minorTickStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minorTickStartExtentProperty);
		}
	}
	,
	minorTickEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minorTickEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minorTickEndExtentProperty);
		}
	}
	,
	minorTickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minorTickStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minorTickStrokeThicknessProperty);
		}
	}
	,
	minorTickBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.minorTickBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.minorTickBrushProperty);
		}
	}
	,
	isScaleInverted: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.isScaleInvertedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.isScaleInvertedProperty);
		}
	}
	,
	backingBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.backingBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.backingBrushProperty);
		}
	}
	,
	backingOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.backingOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.backingOutlineProperty);
		}
	}
	,
	backingStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.backingStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.backingStrokeThicknessProperty);
		}
	}
	,
	backingInnerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.backingInnerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.backingInnerExtentProperty);
		}
	}
	,
	backingOuterExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.backingOuterExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.backingOuterExtentProperty);
		}
	}
	,
	scaleStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleStartExtentProperty);
		}
	}
	,
	scaleEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleEndExtentProperty);
		}
	}
	,
	scaleBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleBrushProperty);
		}
	}
	,
	scaleOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleOutlineProperty);
		}
	}
	,
	scaleStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.scaleStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.scaleStrokeThicknessProperty);
		}
	}
	,
	mergeViewPorts: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype._mergeViewPortsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype._mergeViewPortsProperty);
		}
	}
	,
	__isDragging: false,
	isNeedleDraggingEnabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledProperty);
		}
	}
	,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.transitionDurationProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.XamLinearGauge.prototype.transitionDurationProperty));
		}
	}
	,
	showToolTipTimeout: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.showToolTipTimeoutProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.XamLinearGauge.prototype.showToolTipTimeoutProperty));
		}
	}
	,
	toolTip: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.toolTipProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.toolTipProperty);
		}
	}
	,
	_showToolTip: false,
	showToolTip: function (value) {
		if (arguments.length === 1) {
			this._showToolTip = value;
			return value;
		} else {
			return this._showToolTip;
		}
	}
	,
	font: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamLinearGauge.prototype.fontProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamLinearGauge.prototype.fontProperty);
		}
	}
	,
	_ticks: null,
	ticks: function (value) {
		if (arguments.length === 1) {
			this._ticks = value;
			return value;
		} else {
			return this._ticks;
		}
	}
	,
	_minorTicks: null,
	minorTicks: function (value) {
		if (arguments.length === 1) {
			this._minorTicks = value;
			return value;
		} else {
			return this._minorTicks;
		}
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
	_titles: null,
	titles: function (value) {
		if (arguments.length === 1) {
			this._titles = value;
			return value;
		} else {
			return this._titles;
		}
	}
	,
	_rangeShapes: null,
	rangeShapes: function (value) {
		if (arguments.length === 1) {
			this._rangeShapes = value;
			return value;
		} else {
			return this._rangeShapes;
		}
	}
	,
	getValueForPoint: function (point) {
		var v1 = 0;
		var v2 = point.__x;
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.vertical) {
			v1 = point.__y;
			v2 = this.viewport().height();
		}
		return this.scaler().unscale(v2 - v1);
	}
	,
	__needleBoundingPoints: null,
	__needleBoundingRect: null,
	needleContainsPoint: function (point) {
		if (this.__needleBoundingPoints == null) {
			return false;
		}
		return $.ig.PolygonUtil.prototype.polygonContainsPoint(this.__needleBoundingPoints, point);
	}
	,
	needleContainsPoint1: function (point, isFinger) {
		if (this.__needleBoundingPoints == null) {
			return false;
		}
		return isFinger ? this.__needleBoundingRect.containsPoint(point) : $.ig.PolygonUtil.prototype.polygonContainsPoint(this.__needleBoundingPoints, point);
	}
	,
	dragNeedle: function (point, isFinger) {
		var value = this.getValueForPoint(point);
		value = Math.max(Math.min(value, this.actualMaximumValue()), this.actualMinimumValue());
		if (value != this.value()) {
			this.value(value);
		}
	}
	,
	exportVisualData: function () {
		var $self = this;
		var visualData = new $.ig.LinearGaugeVisualData();
		visualData.viewport(new $.ig.Rect(0, this.viewport().left(), this.viewport().top(), this.viewport().width(), this.viewport().height()));
		visualData.scaleLabels(new $.ig.LinearGraphScaleLabelVisualDataList());
		visualData.scaleTickmarks(new $.ig.LinearGraphScaleTickmarkVisualDataList());
		visualData.ranges(new $.ig.LinearGraphRangeVisualDataList());
		visualData.needle((function () {
			var $ret = new $.ig.LinearNeedleVisualData();
			$ret.value($self.value());
			return $ret;
		}()));
		this.view().exportViewShapes(visualData);
		var tooltipInfo = this.view().getToolTipInfo();
		if (tooltipInfo != null) {
			var toolTipItemVisualData = null;
			switch (tooltipInfo.type()) {
				case "range":
					toolTipItemVisualData = visualData.ranges().__inner[tooltipInfo.index()];
					break;
				case "needle":
					toolTipItemVisualData = visualData.needle();
					break;
			}
			visualData.toolTip((function () {
				var $ret = new $.ig.LinearGraphToolTipVisualData();
				$ret.item(toolTipItemVisualData);
				$ret.itemBrush($.ig.AppearanceHelper.prototype.fromBrush(tooltipInfo.dataContext().brush()));
				$ret.outline($.ig.AppearanceHelper.prototype.fromBrush(tooltipInfo.dataContext().outline()));
				$ret.thickness(tooltipInfo.dataContext().thickness());
				return $ret;
			}()));
		}
		return visualData;
	}
	,
	destroy: function () {
		this.provideContainer(null);
	}
	,
	provideContainer: function (container) {
		this.view().onContainerProvided(container);
	}
	,
	onMouseOver: function (point, onMouseMove, isFinger) {
		if (this.showToolTip()) {
			this.showTooltips(point, null);
		}
	}
	,
	onMouseLeave: function () {
		if (this.showToolTip()) {
			this.showTooltips({ __x: -10000, __y: -10000, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, null);
		}
	}
	,
	onMouseMove: function (point, isFinger) {
		if (this.__isDragging) {
			this.dragNeedle(point, isFinger);
		}
	}
	,
	onMouseDown: function (point, isFinger) {
		if (this.isNeedleDraggingEnabled() && !this.__isDragging && this.needleContainsPoint1(point, isFinger)) {
			this.__isDragging = true;
		}
	}
	,
	onMouseUp: function (point) {
		if (this.__isDragging) {
			this.__isDragging = false;
		}
	}
	,
	containerResized: function () {
		this.view().onContainerResized();
	}
	,
	styleUpdated: function () {
		this.view().styleUpdated();
	}
	,
	flush: function () {
		this.view().flush();
	}
	,
	showTooltips: function (point, data) {
		var needlePoints = this.__needleBoundingPoints;
		if (needlePoints != null && needlePoints.count() > 0) {
			var hit = $.ig.PolygonUtil.prototype.polygonContainsPoint(needlePoints, point);
			if (hit == true) {
				this.view().showTooltip(point, data, -1, "needle");
				return;
			}
		}
		for (var i = 0; i < this.__displayedRanges.count(); i++) {
			var rangePath = new $.ig.PathVisualData(1, "range", this.__displayedRanges.__inner[i]);
			var rangePoints = rangePath.getPoints(new $.ig.GetPointsSettings());
			if (rangePoints.count() > 0) {
				var hit1 = $.ig.PolygonUtil.prototype.polygonContainsPoint(rangePoints.__inner[0], point);
				if (hit1 == true) {
					this.view().showTooltip(point, data, i, "range");
					return;
				}
			}
		}
		this.view().hideTooltip();
	}
	,
	refreshViewport: function () {
		if (!this.isDirty()) {
			return;
		}
		var frame = this.nextFrame().scaleFrame();
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			var labelsMaxHeight = 0;
			for (var i = 0; i < frame.labelHeights().length; i++) {
				labelsMaxHeight = Math.max(labelsMaxHeight, frame.labelHeights()[i]);
			}
			var portHeight = this.viewport().height() >= labelsMaxHeight ? this.viewport().height() - labelsMaxHeight : this.viewport().height();
			this.scaleViewport(new $.ig.Rect(0, 0, 0, this.viewport().width(), portHeight));
		} else {
			var labelsMaxWidth = 0;
			for (var i1 = 0; i1 < frame.labelWidths().length; i1++) {
				labelsMaxWidth = Math.max(labelsMaxWidth, frame.labelWidths()[i1]);
			}
			var portWidth = this.viewport().width() >= labelsMaxWidth ? this.viewport().width() - labelsMaxWidth : this.viewport().width();
			this.scaleViewport(new $.ig.Rect(0, labelsMaxWidth, 0, portWidth, this.viewport().height()));
		}
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
	__viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			var oldViewport = this.__viewport;
			this.__viewport = value;
			if (!oldViewport.equals1(this.__viewport)) {
				this.onViewportChanged(oldViewport, this.__viewport);
			}
			return value;
		} else {
			return this.__viewport;
		}
	}
	,
	__scaleViewport: null,
	scaleViewport: function (value) {
		if (arguments.length === 1) {
			this.__scaleViewport = value;
			return value;
		} else {
			return this.__scaleViewport;
		}
	}
	,
	__backingViewport: null,
	backingViewport: function (value) {
		if (arguments.length === 1) {
			this.__backingViewport = value;
			return value;
		} else {
			return this.__backingViewport;
		}
	}
	,
	isValid: function () {
		return !$.ig.util.isNaN(this.actualMinimumValue()) && !$.ig.util.isNaN(this.actualMaximumValue()) && !this.viewport().isEmpty() && this.view().ready();
	}
	,
	arrangeGauge: function () {
		if (!this.isValid()) {
			return;
		}
		var scaleStart = this.scaleStart();
		this.scaler(new $.ig.LinearGraphScaler(this.actualMinimumValue(), this.actualMaximumValue(), scaleStart, scaleStart + this.scaleLength(), this.isScaleInverted()));
		if (this.isDirty()) {
			if (this.animating()) {
				if (this.animator().needsFlush()) {
					this.animator().flush();
				}
				var previousFrame = this.previousFrame();
				this.previousFrame(this.currentFrame());
				this.currentFrame(previousFrame);
			} else {
				var previousFrame1 = this.previousFrame();
				this.previousFrame(this.nextFrame());
				this.nextFrame(previousFrame1);
			}
		} else if (this.animating()) {
			this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
			this.renderBacking(this.currentFrame());
			this.renderScale(this.currentFrame());
			this.renderNeedle(this.currentFrame());
		}
		if (this.isDirty()) {
			this.prepareLabels();
			this.refreshViewport();
			this.prepareNeedle();
			this.prepareScale();
			this.prepareBacking();
		}
		if (this.isDirty() && this.shouldAnimate()) {
			this.startAnimation();
			this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
			this.renderBacking(this.currentFrame());
			this.renderScale(this.currentFrame());
			this.renderNeedle(this.currentFrame());
			this.renderTicks(this.currentFrame());
			this.renderLabels(this.currentFrame());
		} else {
			if (this.isDirty()) {
				this.renderBacking(this.nextFrame());
				this.renderScale(this.nextFrame());
				this.renderNeedle(this.nextFrame());
				this.renderTicks(this.nextFrame());
				this.renderLabels(this.nextFrame());
			}
		}
		this.isDirty(false);
		this.view().arrangeComplete();
	}
	,
	__displayedLabels: null,
	__displayedTitles: null,
	__displayedTicks: null,
	__displayedRanges: null,
	prepareScale: function () {
		var frame = this.nextFrame();
		var scaleFrame = frame.scaleFrame();
		var scaler = this.scaler();
		var minorTickCount = this.minorTickCount();
		var viewport = this.scaleViewport();
		scaleFrame.scaleStartExtent(this.scaleStartExtent());
		scaleFrame.scaleEndExtent(this.scaleEndExtent());
		scaleFrame.scaleInnerExtent(this.scaleInnerExtent());
		scaleFrame.scaleOuterExtent(this.scaleOuterExtent());
		scaleFrame.ranges().clear();
		for (var i = 0; i < this.ranges().count(); i++) {
			var range = this.ranges().__inner[i];
			if (range.endValue() <= this.actualMinimumValue() || range.startValue() > this.actualMaximumValue()) {
				continue;
			}
			var startValue = range.startValue();
			var endValue = range.endValue();
			if (startValue < this.actualMinimumValue()) {
				startValue = this.actualMinimumValue();
			}
			if (endValue > this.actualMaximumValue()) {
				endValue = this.actualMaximumValue();
			}
			var rangeFrame = new $.ig.LinearGraphRectFrame();
			rangeFrame._brush = range.brush() != null ? range.brush() : this.getBrushByIndex(i, this.rangeBrushes());
			rangeFrame._outline = range.outline() != null ? range.outline() : this.getBrushByIndex(i, this.rangeOutlines());
			rangeFrame._strokeThickness = range.strokeThickness();
			rangeFrame._startValue = scaler.scale(startValue);
			rangeFrame._endValue = scaler.scale(endValue);
			rangeFrame._startInnerExtent = range.innerStartExtent();
			rangeFrame._endInnerExtent = range.innerEndExtent();
			rangeFrame._startOuterExtent = range.outerStartExtent();
			rangeFrame._endOuterExtent = range.outerEndExtent();
			if ($.ig.util.isNaN(rangeFrame._startInnerExtent)) {
				rangeFrame._startInnerExtent = this.rangeInnerExtent();
			}
			rangeFrame._endInnerExtent = range.innerEndExtent();
			if ($.ig.util.isNaN(rangeFrame._endInnerExtent)) {
				rangeFrame._endInnerExtent = this.rangeInnerExtent();
			}
			rangeFrame._startOuterExtent = range.outerStartExtent();
			if ($.ig.util.isNaN(rangeFrame._startOuterExtent)) {
				rangeFrame._startOuterExtent = this.rangeOuterExtent();
			}
			rangeFrame._endOuterExtent = range.outerEndExtent();
			if ($.ig.util.isNaN(rangeFrame._endOuterExtent)) {
				rangeFrame._endOuterExtent = this.rangeOuterExtent();
			}
			scaleFrame.ranges().add(rangeFrame);
		}
		scaleFrame.fontBrush(this.fontBrush());
		scaleFrame.tickBrush(this.tickBrush());
		scaleFrame.tickStrokeThickness(this.tickStrokeThickness());
		scaleFrame.tickStartExtent(this.tickStartExtent());
		scaleFrame.tickEndExtent(this.tickEndExtent());
		scaleFrame.minorTickBrush(this.minorTickBrush());
		scaleFrame.minorTickStrokeThickness(this.minorTickStrokeThickness());
		scaleFrame.minorTickStartExtent(this.minorTickStartExtent());
		scaleFrame.minorTickEndExtent(this.minorTickEndExtent());
		var interval = this.interval();
		if ($.ig.util.isNaN(interval)) {
			interval = (this.actualMaximumValue() - this.actualMinimumValue()) / 10;
		}
		if (interval == 0) {
			interval = this.actualMaximumValue() - this.actualMinimumValue();
		}
		var ticks = new $.ig.List$1(Number, 0);
		var minorTicks = new $.ig.List$1(Number, 0);
		if (interval > 0) {
			for (var i1 = this.actualMinimumValue() + this.ticksPostInitial(); i1 <= this.actualMaximumValue() - this.ticksPreTerminal() || this.valueCloseEnough(i1, this.actualMaximumValue() - this.ticksPreTerminal(), interval); i1 = i1 + interval) {
				ticks.add(scaler.scale(i1));
				for (var j = 0; j < minorTickCount; j++) {
					var val = (interval / (minorTickCount + 1)) * (j + 1);
					if (val + i1 > this.actualMaximumValue() - this.ticksPreTerminal()) {
						continue;
					}
					minorTicks.add(scaler.scale(val + i1));
				}
			}
		}
		if (scaleFrame.ticks().length != ticks.count() || scaleFrame.minorTicks().length != minorTicks.count()) {
			scaleFrame.ticks(new Array(ticks.count()));
			scaleFrame.minorTicks(new Array(minorTicks.count()));
		}
		for (var i2 = 0; i2 < ticks.count(); i2++) {
			scaleFrame.ticks()[i2] = ticks.__inner[i2];
		}
		for (var i3 = 0; i3 < minorTicks.count(); i3++) {
			scaleFrame.minorTicks()[i3] = minorTicks.__inner[i3];
		}
	}
	,
	measureLabels: function () {
		var interval = this.interval();
		var labelInterval = this.labelInterval();
		if ($.ig.util.isNaN(interval)) {
			interval = (this.actualMaximumValue() - this.actualMinimumValue()) / 10;
		}
		if ($.ig.util.isNaN(labelInterval)) {
			labelInterval = interval;
		}
		if (labelInterval == 0) {
			labelInterval = this.actualMaximumValue() - this.actualMinimumValue();
		}
		var labels = new $.ig.List$1(Number, 0);
		if (labelInterval > 0) {
			for (var i = this.labelsPostInitial() + this.actualMinimumValue(); i <= this.actualMaximumValue() - this.labelsPreTerminal() || this.valueCloseEnough(i, this.actualMaximumValue() - this.ticksPreTerminal(), interval); i += labelInterval) {
				labels.add(i);
			}
		}
		var labelWidths = new Array(labels.count());
		var labelHeights = new Array(labels.count());
		for (var i1 = 0; i1 < labels.count(); i1++) {
			var value = labels.__inner[i1];
			var label = value.toString();
			var args = new $.ig.FormatLinearGraphLabelEventArgs();
			args.value = value;
			args.label = label;
			if (this.formatLabel != null) {
				this.formatLabel(this, args);
			}
			label = args.label;
			var size = this.view().getLabelSize((function () {
				var $ret = new $.ig.TextBlock();
				$ret.text(label);
				return $ret;
			}()));
			labelWidths[i1] = size.width();
			labelHeights[i1] = size.height();
		}
		var width = 0;
		var height = 0;
		var extents = 1 - this.scaleEndExtent() + this.scaleStartExtent();
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			for (var i2 = 0; i2 < labelHeights.length; i2++) {
				height = Math.max(height, labelHeights[i2]);
			}
			for (var i3 = 0; i3 < labelWidths.length; i3++) {
				width += labelWidths[i3];
			}
			if (labelWidths.length > 0) {
				width += labelWidths[0] / 2;
			}
			if (labelWidths.length > 1) {
				width += labelWidths[labelWidths.length - 1] / 2;
			}
			if (labelWidths.length > 0) {
				width += (labelWidths.length - 1) * 5;
			}
			if (extents > 0) {
				width = width / Math.abs(1 - extents);
			}
		} else {
			for (var i4 = 0; i4 < labelWidths.length; i4++) {
				width = Math.max(width, labelWidths[i4]);
			}
			for (var i5 = 0; i5 < labelHeights.length; i5++) {
				height += labelHeights[i5];
			}
			if (labelHeights.length > 0) {
				height -= labelHeights[0] / 2;
			}
			if (labelHeights.length > 1) {
				height -= labelHeights[labelHeights.length - 1] / 2;
			}
			if (extents > 0) {
				height = height / Math.abs(1 - extents);
			}
			if (labelHeights.length > 0) {
				height += (labelHeights.length - 1) * 5;
			}
		}
		return new $.ig.Size(1, width, height);
	}
	,
	prepareLabels: function () {
		var frame = this.nextFrame();
		var scaleFrame = frame.scaleFrame();
		var scaler = this.scaler();
		var interval = this.interval();
		var labelInterval = this.labelInterval();
		var minorTickCount = this.minorTickCount();
		if ($.ig.util.isNaN(interval)) {
			interval = (this.actualMaximumValue() - this.actualMinimumValue()) / 10;
		}
		if ($.ig.util.isNaN(labelInterval)) {
			labelInterval = interval;
		}
		if (labelInterval == 0) {
			labelInterval = this.actualMaximumValue() - this.actualMinimumValue();
		}
		var labels = new $.ig.List$1($.ig.Tuple$2.prototype.$type.specialize(Number, Number), 0);
		if (labelInterval > 0) {
			for (var i = this.labelsPostInitial() + this.actualMinimumValue(); i <= this.actualMaximumValue() - this.labelsPreTerminal() || this.valueCloseEnough(i, this.actualMaximumValue() - this.labelsPreTerminal(), labelInterval); i = i + labelInterval) {
				labels.add(new $.ig.Tuple$2(Number, Number, i, scaler.scale(i)));
			}
		}
		if (scaleFrame.labels().length != labels.count()) {
			scaleFrame.labelValues(new Array(labels.count()));
			scaleFrame.labels(new Array(labels.count()));
			scaleFrame.labelWidths(new Array(labels.count()));
			scaleFrame.labelHeights(new Array(labels.count()));
			scaleFrame.labelXOffsets(new Array(labels.count()));
			scaleFrame.labelYOffsets(new Array(labels.count()));
		}
		scaleFrame.labelExtent(this.labelExtent());
		var args = new $.ig.FormatLinearGraphLabelEventArgs();
		args.actualMinimumValue = this.actualMinimumValue();
		args.actualMaximumValue = this.actualMaximumValue();
		var alignArgs = new $.ig.AlignLinearGraphLabelEventArgs();
		alignArgs.actualMinimumValue = this.actualMinimumValue();
		alignArgs.actualMaximumValue = this.actualMaximumValue();
		for (var i1 = 0; i1 < labels.count(); i1++) {
			var value = labels.__inner[i1].item1();
			var label = (value).toString();
			args.value = value;
			alignArgs.value = value;
			args.label = label;
			if (this.formatLabel != null) {
				this.formatLabel(this, args);
			}
			label = args.label;
			alignArgs.label = label;
			var size = this.view().getLabelSize((function () {
				var $ret = new $.ig.TextBlock();
				$ret.text(label);
				return $ret;
			}()));
			alignArgs.width = size.width();
			alignArgs.height = size.height();
			if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
				alignArgs.offsetX = -1 * (size.width() / 2);
				alignArgs.offsetY = -1 * (size.height());
			} else {
				alignArgs.offsetX = -1 * size.width();
				alignArgs.offsetY = -1 * (size.height() / 2);
			}
			if (this.alignLabel != null) {
				this.alignLabel(this, alignArgs);
			}
			scaleFrame.labelWidths()[i1] = alignArgs.width;
			scaleFrame.labelHeights()[i1] = alignArgs.height;
			scaleFrame.labelXOffsets()[i1] = alignArgs.offsetX;
			;
			scaleFrame.labelYOffsets()[i1] = alignArgs.offsetY;
			scaleFrame.labels()[i1] = label;
			scaleFrame.labelValues()[i1] = labels.__inner[i1].item2();
		}
	}
	,
	valueCloseEnough: function (val1, val2, interval) {
		var delta = interval / 10000;
		if ($.ig.util.isNaN(delta)) {
			delta = 1E-05;
		}
		var diff = Math.abs(val1 - val2);
		if (diff < delta) {
			return true;
		}
		return false;
	}
	,
	prepareBacking: function () {
		var frame = this.nextFrame();
		var f = frame.backingFrame();
		f.brush(this.backingBrush());
		f.outline(this.backingOutline());
		f.strokeThickness(this.backingStrokeThickness());
		f.innerExtent(this.backingInnerExtent());
		f.outerExtent(this.backingOuterExtent());
	}
	,
	prepareNeedle: function () {
		var needle = this.value();
		if ($.ig.util.isNaN(needle)) {
			return;
		}
		var frame = this.nextFrame().scaleFrame();
		var scaler = this.scaler();
		var p = new $.ig.LinearGraphNeedleParameters();
		p.needleShape(this.needleShape());
		p.isScaleInverted(this.isScaleInverted());
		p.scaleOrientation(this.orientation());
		p.outerExtent(this.needleOuterExtent());
		p.innerExtent(this.needleInnerExtent());
		p.outerPointExtent(this.needleOuterPointExtent());
		p.innerPointExtent(this.needleInnerPointExtent());
		p.outerBaseWidth(this.needleOuterBaseWidth());
		p.innerBaseWidth(this.needleInnerBaseWidth());
		p.innerPointWidth(this.needleInnerPointWidth());
		p.outerPointWidth(this.needleOuterPointWidth());
		p.defaultWidth(this.needleBreadth() * 0.5 / this.scaleBreadth());
		var needleFrame = new $.ig.LinearGraphNeedleFrame();
		needleFrame.needleBrush(this.needleBrush());
		needleFrame.needleOutline(this.needleOutline());
		needleFrame.needleStrokeThickness(this.needleStrokeThickness());
		var needlePreparer = new $.ig.LinearGraphNeedlePreparer();
		needlePreparer.parameters(p);
		needlePreparer.prepareNeedle(needleFrame);
		needleFrame.needleAngle(-90);
		var value = scaler.scale(needle);
		needleFrame.needleValue(value);
		frame.needle(needleFrame);
	}
	,
	getBrushByIndex: function (index, brushes) {
		if (brushes != null && brushes.count() > 0) {
			return brushes.item(index % brushes.count());
		} else {
			return null;
		}
	}
	,
	renderTicks: function (frame) {
		var viewport = this.scaleViewport();
		var scaleFrame = frame.scaleFrame();
		var ticks = this.ticks();
		var scaleTicks = scaleFrame.ticks();
		var tickStartExtent = scaleFrame.tickStartExtent();
		var tickEndExtent = scaleFrame.tickEndExtent();
		var tickBrush = scaleFrame.tickBrush();
		var tickStrokeThickness = scaleFrame.tickStrokeThickness();
		var minorTicks = this.minorTicks();
		var scaleMinorTicks = scaleFrame.minorTicks();
		var minorTickStartExtent = scaleFrame.minorTickStartExtent();
		var minorTickEndExtent = scaleFrame.minorTickEndExtent();
		var minorTickBrush = scaleFrame.minorTickBrush();
		var minorTickStrokeThickness = scaleFrame.minorTickStrokeThickness();
		ticks.deferDisactivate(true);
		for (var i = 0; i < this.__displayedTicks.count(); i++) {
			ticks.push(this.__displayedTicks.__inner[i]);
		}
		this.__displayedTicks.clear();
		var breadth = this.scaleBreadth();
		var length = this.viewportLength();
		for (var i1 = 0; i1 < scaleTicks.length; i1++) {
			var tx1, ty1, tx2, ty2;
			if (this.orientation() == $.ig.LinearScaleOrientation.prototype.vertical) {
				tx1 = viewport.left() + breadth * tickStartExtent;
				ty1 = length - scaleTicks[i1];
				tx2 = viewport.left() + breadth * tickEndExtent;
				ty2 = ty1;
			} else {
				tx1 = viewport.left() + scaleTicks[i1];
				ty1 = breadth * (1 - tickStartExtent);
				tx2 = tx1;
				ty2 = breadth * (1 - tickEndExtent);
			}
			var tick = ticks.pop();
			var tpg = new $.ig.PathGeometry();
			var tpf = new $.ig.PathFigure();
			tpf.__startPoint = { __x: tx1, __y: ty1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			tpf.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: tx2, __y: ty2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			tpg.figures().add(tpf);
			tick.data(tpg);
			tick.__stroke = tickBrush;
			tick.strokeThickness(tickStrokeThickness);
			this.__displayedTicks.add(tick);
		}
		for (var i2 = 0; i2 < scaleMinorTicks.length; i2++) {
			var tx11, ty11, tx21, ty21;
			if (this.orientation() == $.ig.LinearScaleOrientation.prototype.vertical) {
				tx11 = viewport.left() + breadth * minorTickStartExtent;
				ty11 = length - scaleMinorTicks[i2];
				tx21 = viewport.left() + breadth * minorTickEndExtent;
				ty21 = ty11;
			} else {
				tx11 = scaleMinorTicks[i2];
				ty11 = breadth * (1 - minorTickStartExtent);
				tx21 = tx11;
				ty21 = breadth * (1 - minorTickEndExtent);
			}
			var tick1 = ticks.pop();
			var tpg1 = new $.ig.PathGeometry();
			var tpf1 = new $.ig.PathFigure();
			tpf1.__startPoint = { __x: tx11, __y: ty11, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			tpf1.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: tx21, __y: ty21, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			tpg1.figures().add(tpf1);
			tick1.data(tpg1);
			tick1.__stroke = minorTickBrush;
			tick1.strokeThickness(minorTickStrokeThickness);
			this.__displayedTicks.add(tick1);
		}
		ticks.deferDisactivate(false);
	}
	,
	renderLabels: function (frame) {
		var scaleViewport = this.scaleViewport();
		var scaleFrame = frame.scaleFrame();
		var labelValues = scaleFrame.labelValues();
		var labelStrings = scaleFrame.labels();
		var labelXOffsets = scaleFrame.labelXOffsets();
		var labelYOffsets = scaleFrame.labelYOffsets();
		var labelWidths = scaleFrame.labelWidths();
		var labelHeights = scaleFrame.labelHeights();
		var labelExtent = scaleFrame.labelExtent();
		var labels = this.labels();
		labels.deferDisactivate(true);
		for (var i = 0; i < this.__displayedLabels.count(); i++) {
			labels.push(this.__displayedLabels.__inner[i]);
		}
		this.__displayedLabels.clear();
		var fontBrush = scaleFrame.fontBrush();
		var breadth = this.viewportBreadth();
		var length = this.viewportLength();
		for (var i1 = 0; i1 < labelStrings.length; i1++) {
			var lx, ly;
			if (this.orientation() == $.ig.LinearScaleOrientation.prototype.vertical) {
				lx = scaleViewport.left() + breadth * labelExtent;
				ly = scaleViewport.top() + length - labelValues[i1];
			} else {
				lx = scaleViewport.left() + labelValues[i1];
				ly = scaleViewport.top() + breadth * (1 - labelExtent);
			}
			var tb = labels.pop();
			tb.text(labelStrings[i1]);
			this.view().setTextBrush(tb, fontBrush);
			var labelWidth = labelWidths[i1];
			var labelHeight = labelHeights[i1];
			var xOffset = labelXOffsets[i1];
			var yOffset = labelYOffsets[i1];
			this.view().positionLabel(tb, lx + xOffset, ly + yOffset);
			this.__displayedLabels.add(tb);
		}
		labels.deferDisactivate(false);
	}
	,
	renderScale: function (frame) {
		var scaleFrame = frame.scaleFrame();
		var ranges = this.rangeShapes();
		ranges.deferDisactivate(true);
		for (var i = 0; i < this.__displayedRanges.count(); i++) {
			ranges.push(this.__displayedRanges.__inner[i]);
		}
		this.__displayedRanges.clear();
		var breadth = this.scaleBreadth();
		var length = this.viewportLength();
		var scaleStartPoint = this.scaleStartPoint();
		var scalePath = this.view().getScalePath();
		if ($.ig.util.isNaN(scaleFrame.scaleInnerExtent()) || $.ig.util.isNaN(scaleFrame.scaleOuterExtent()) || $.ig.util.isNaN(scaleFrame.scaleStartExtent()) || $.ig.util.isNaN(scaleFrame.scaleEndExtent())) {
			scalePath.data(null);
		} else {
			var scaleLength = this.scaleLength();
			scalePath.data(scaleFrame.getPathGeometry(scaleStartPoint, breadth, scaleLength, this.orientation()));
			scalePath.__fill = this.scaleBrush();
			scalePath.__stroke = this.scaleOutline();
			scalePath.strokeThickness(this.scaleStrokeThickness());
		}
		for (var i1 = 0; i1 < scaleFrame.ranges().count(); i1++) {
			var range = scaleFrame.ranges().__inner[i1];
			var currRange = ranges.pop();
			currRange.data(range.getPathGeometry(scaleStartPoint, breadth, length, this.orientation()));
			currRange.__fill = range._brush;
			currRange.__stroke = range._outline;
			currRange.strokeThickness(range._strokeThickness);
			this.__displayedRanges.add(currRange);
		}
		this.view().setRangeRenderOrder(this.__displayedRanges);
		ranges.deferDisactivate(false);
	}
	,
	renderBacking: function (frame) {
		var viewport = this.backingViewport();
		var backingPath = this.view().getBackingPath();
		var backingFrame = frame.backingFrame();
		var breadth = this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal ? viewport.height() : viewport.width();
		var length = this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal ? viewport.width() : viewport.height();
		var geo = backingFrame.getPathGeometry(breadth, length, this.orientation());
		backingPath.data(geo);
		backingPath.__fill = backingFrame.brush();
		backingPath.__stroke = backingFrame.outline();
		backingPath.strokeThickness(backingFrame.strokeThickness());
	}
	,
	renderNeedle: function (frame) {
		this.__needleBoundingPoints = null;
		var needlePath = this.view().getNeedlePath();
		var needle = this.value();
		if ($.ig.util.isNaN(needle) || needle < this.actualMinimumValue() || needle > this.actualMaximumValue()) {
			needlePath.data(null);
			return;
		}
		var scaleFrame = frame.scaleFrame();
		var needleFrame = scaleFrame.needle();
		var scaleBreadth = this.scaleBreadth();
		var scaleLength = this.viewportLength();
		var needleLength = scaleBreadth;
		var centerX = needleFrame.needleValue();
		var centerY = 0;
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.vertical) {
			centerX = this.scaleViewport().left();
			centerY = scaleLength - needleFrame.needleValue();
		}
		needlePath.data(needleFrame.getPathGeometry(needleLength, centerX, centerY));
		needlePath.__fill = needleFrame.needleBrush();
		needlePath.__stroke = needleFrame.needleOutline();
		needlePath.strokeThickness(needleFrame.needleStrokeThickness());
		var needleVisualData = new $.ig.PathVisualData(1, "needle", needlePath);
		var needlePoints = needleVisualData.getPoints(new $.ig.GetPointsSettings());
		if (needlePoints.count() > 0) {
			this.__needleBoundingPoints = needlePoints.__inner[0];
			var minX = 1.7976931348623157E+308, minY = 1.7976931348623157E+308;
			var maxX = -1.7976931348623157E+308, maxY = -1.7976931348623157E+308;
			for (var i = 0; i < this.__needleBoundingPoints.count(); i++) {
				minX = this.__needleBoundingPoints.__inner[i].__x < minX ? this.__needleBoundingPoints.__inner[i].__x : minX;
				minY = this.__needleBoundingPoints.__inner[i].__y < minY ? this.__needleBoundingPoints.__inner[i].__y : minY;
				maxX = this.__needleBoundingPoints.__inner[i].__x > maxX ? this.__needleBoundingPoints.__inner[i].__x : maxX;
				maxY = this.__needleBoundingPoints.__inner[i].__y > maxY ? this.__needleBoundingPoints.__inner[i].__y : maxY;
			}
			this.__needleBoundingRect = new $.ig.Rect(2, { __x: minX, __y: minY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: maxX, __y: maxY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	onViewportChanged: function (oldViewport, newViewport) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	onLabelsViewportChanged: function (oldViewport, newViewport) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	scaleBreadth: function () {
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			return this.mergeViewPorts() ? this.viewport().height() : this.scaleViewport().height();
		}
		return this.mergeViewPorts() ? this.viewport().width() : this.scaleViewport().width();
	}
	,
	scaleLength: function () {
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			return this.viewport().width() * (this.scaleEndExtent() - this.scaleStartExtent());
		}
		return this.viewport().height() * (this.scaleEndExtent() - this.scaleStartExtent());
	}
	,
	viewportBreadth: function () {
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			return this.viewport().height();
		}
		return this.viewport().width();
	}
	,
	viewportLength: function () {
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			return this.viewport().width();
		}
		return this.viewport().height();
	}
	,
	scaleStart: function () {
		var scaleStartExtent = this.scaleStartExtent();
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			if (this.isScaleInverted()) {
				scaleStartExtent = 1 - this.scaleEndExtent();
			}
			return this.viewport().left() + this.viewport().width() * scaleStartExtent;
		}
		if (this.isScaleInverted()) {
			scaleStartExtent = 1 - this.scaleEndExtent();
		}
		return this.viewport().top() + this.viewport().height() * scaleStartExtent;
	}
	,
	scaleStartPoint: function () {
		var leftOffset = this.scaleViewport().left();
		if (this.mergeViewPorts()) {
			leftOffset = 0;
		}
		var scaleOffset = this.scaleStartExtent();
		if (this.orientation() == $.ig.LinearScaleOrientation.prototype.horizontal) {
			if (this.isScaleInverted()) {
				scaleOffset = 1 - this.scaleEndExtent();
			}
			return { __x: leftOffset + this.scaleViewport().width() * scaleOffset, __y: this.scaleViewport().top(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (!this.isScaleInverted()) {
			scaleOffset = 1 - this.scaleEndExtent();
		}
		return { __x: leftOffset, __y: this.scaleViewport().top() + this.scaleViewport().height() * scaleOffset, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	onPropertyChanged: function (propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.XamLinearGauge.prototype.backingBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.backingOutlinePropertyName:
			case $.ig.XamLinearGauge.prototype.scaleBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleOutlinePropertyName:
			case $.ig.XamLinearGauge.prototype.tickBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.needleBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOutlinePropertyName:
				this.view().onBrushChanged(propertyName, oldValue, newValue);
				break;
			case $.ig.XamLinearGauge.prototype.rangeBrushesPropertyName:
				this.view().onRangeBrushesChanged(oldValue, newValue);
				break;
			case $.ig.XamLinearGauge.prototype.rangeOutlinesPropertyName:
				this.view().onRangeOutlinesChanged(oldValue, newValue);
				break;
		}
		switch (propertyName) {
			case $.ig.XamLinearGauge.prototype.valuePropertyName:
				if (this.valueChanged != null) {
					this.valueChanged(this, new $.ig.DoubleValueChangedEventArgs(oldValue, newValue));
				}
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamLinearGauge.prototype.transitionDurationPropertyName:
				this.animator().intervalMilliseconds(this.transitionDuration());
				this.view().scheduleArrange();
				break;
			case $.ig.XamLinearGauge.prototype.transitionProgressPropertyName:
				if (this.transitionProgress() >= 1) {
					this.animator().stop();
					var swap = this.nextFrame();
					this.nextFrame(this.previousFrame());
					this.previousFrame(swap);
					this.prepareLabels();
					this.prepareNeedle();
					this.prepareScale();
					this.prepareBacking();
					this.renderBacking(this.nextFrame());
					this.renderScale(this.nextFrame());
					this.renderNeedle(this.nextFrame());
					this.renderTicks(this.nextFrame());
					this.renderLabels(this.nextFrame());
				}
				this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
				this.renderBacking(this.currentFrame());
				this.renderScale(this.currentFrame());
				this.renderNeedle(this.currentFrame());
				this.renderTicks(this.currentFrame());
				this.renderLabels(this.currentFrame());
				this.view().arrangeComplete();
				break;
			case $.ig.XamLinearGauge.prototype.needleBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.needleNamePropertyName:
			case $.ig.XamLinearGauge.prototype.needleBreadthPropertyName:
			case $.ig.XamLinearGauge.prototype.needleShapePropertyName:
			case $.ig.XamLinearGauge.prototype.needleInnerExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOuterExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.needleInnerPointExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOuterPointExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.needleInnerBaseWidthPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOuterBaseWidthPropertyName:
			case $.ig.XamLinearGauge.prototype.needleInnerPointWidthPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOuterPointWidthPropertyName:
			case $.ig.XamLinearGauge.prototype.needleStrokeThicknessPropertyName:
			case $.ig.XamLinearGauge.prototype.needleOutlinePropertyName:
			case $.ig.XamLinearGauge.prototype.backingBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.backingOutlinePropertyName:
			case $.ig.XamLinearGauge.prototype.backingStrokeThicknessPropertyName:
			case $.ig.XamLinearGauge.prototype.backingInnerExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.backingOuterExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleOutlinePropertyName:
			case $.ig.XamLinearGauge.prototype.scaleStrokeThicknessPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleInnerExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleOuterExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.tickBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.rangeBrushesPropertyName:
			case $.ig.XamLinearGauge.prototype.rangeOutlinesPropertyName:
			case $.ig.XamLinearGauge.prototype.rangeInnerExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.rangeOuterExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.intervalPropertyName:
			case $.ig.XamLinearGauge.prototype.labelIntervalPropertyName:
			case $.ig.XamLinearGauge.prototype.labelExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.labelsPostInitialPropertyName:
			case $.ig.XamLinearGauge.prototype.labelsPreTerminalPropertyName:
			case $.ig.XamLinearGauge.prototype.ticksPostInitialPropertyName:
			case $.ig.XamLinearGauge.prototype.ticksPreTerminalPropertyName:
			case $.ig.XamLinearGauge.prototype.tickStartExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.tickEndExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.tickStrokeThicknessPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickStartExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickEndExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickCountPropertyName:
			case $.ig.XamLinearGauge.prototype.minorTickStrokeThicknessPropertyName:
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamLinearGauge.prototype.rangesPropertyName:
				var oldValues = oldValue;
				var newValues = newValue;
				if (oldValues != null) {
					var $t = (oldValues);
					$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.ranges_CollectionChanged.runOn(this));
				}
				if (newValues != null) {
					var $t1 = (newValues);
					$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.ranges_CollectionChanged.runOn(this));
				}
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamLinearGauge.prototype.orientationPropertyName:
			case $.ig.XamLinearGauge.prototype.isScaleInvertedPropertyName:
			case $.ig.XamLinearGauge.prototype.minimumValuePropertyName:
			case $.ig.XamLinearGauge.prototype.maximumValuePropertyName:
			case $.ig.XamLinearGauge.prototype.scaleEndExtentPropertyName:
			case $.ig.XamLinearGauge.prototype.scaleStartExtentPropertyName:
				this.actualMinimumValue(Math.min(this.minimumValue(), this.maximumValue()));
				this.actualMaximumValue(Math.max(this.minimumValue(), this.maximumValue()));
				var scaleStart = this.scaleStart();
				this.scaler(new $.ig.LinearGraphScaler(this.actualMinimumValue(), this.actualMaximumValue(), scaleStart, scaleStart + this.scaleLength(), this.isScaleInverted()));
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamLinearGauge.prototype.fontBrushPropertyName:
			case $.ig.XamLinearGauge.prototype.fontPropertyName:
				this.view().updateStyle();
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
		}
	}
	,
	ranges_CollectionChanged: function (sender, e) {
		if (e.oldItems() != null) {
			var en = e.oldItems().getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				item.propertyChanged = $.ig.Delegate.prototype.remove(item.propertyChanged, this.range_PropertyChanged.runOn(this));
			}
		}
		if (e.newItems() != null) {
			var en1 = e.newItems().getEnumerator();
			while (en1.moveNext()) {
				var item1 = en1.current();
				item1.propertyChanged = $.ig.Delegate.prototype.combine(item1.propertyChanged, this.range_PropertyChanged.runOn(this));
			}
		}
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	range_PropertyChanged: function (sender, e) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	needle_PropertyChanged: function (sender, e) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	_isDirty: false,
	isDirty: function (value) {
		if (arguments.length === 1) {
			this._isDirty = value;
			return value;
		} else {
			return this._isDirty;
		}
	}
	,
	refresh: function () {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	getFontInfo: function () {
		return $.ig.FontUtil.prototype.toFontInfo(this.font());
	}
	,
	$type: new $.ig.Type('XamLinearGauge', $.ig.Control.prototype.$type)
}, true);

$.ig.LinearScaleOrientation.prototype.horizontal = 0;
$.ig.LinearScaleOrientation.prototype.vertical = 1;

$.ig.LinearGraphNeedleShape.prototype.custom = 0;
$.ig.LinearGraphNeedleShape.prototype.rectangle = 1;
$.ig.LinearGraphNeedleShape.prototype.triangle = 2;
$.ig.LinearGraphNeedleShape.prototype.needle = 3;
$.ig.LinearGraphNeedleShape.prototype.trapezoid = 4;

$.ig.XamLinearGaugeView.prototype._tEXT_MARGIN = 0;
$.ig.XamLinearGaugeView.prototype._gradientBrushes = new $.ig.Dictionary$2(String, $.ig.Brush.prototype.$type, 0);
$.ig.XamLinearGaugeView.prototype._rangeBrushes = null;
$.ig.XamLinearGaugeView.prototype._rangeOutlines = null;

$.ig.XamLinearGraphRange.prototype.namePropertyName = "Name";
$.ig.XamLinearGraphRange.prototype.brushPropertyName = "Brush";
$.ig.XamLinearGraphRange.prototype.outlinePropertyName = "Outline";
$.ig.XamLinearGraphRange.prototype.startValuePropertyName = "StartValue";
$.ig.XamLinearGraphRange.prototype.endValuePropertyName = "EndValue";
$.ig.XamLinearGraphRange.prototype.innerStartExtentPropertyName = "InnerStartExtent";
$.ig.XamLinearGraphRange.prototype.innerEndExtentPropertyName = "InnerEndExtent";
$.ig.XamLinearGraphRange.prototype.outerStartExtentPropertyName = "OuterStartExtent";
$.ig.XamLinearGraphRange.prototype.outerEndExtentPropertyName = "OuterEndExtent";
$.ig.XamLinearGraphRange.prototype.strokeThicknessPropertyName = "StrokeThickness";
$.ig.XamLinearGraphRange.prototype.nameProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.namePropertyName, String, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.namePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.brushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.brushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.brushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.outlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.outlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.outlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.startValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.startValuePropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.startValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.endValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.endValuePropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.endValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.innerStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.innerStartExtentPropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.innerStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.innerEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.innerEndExtentPropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.innerEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.outerStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.outerStartExtentPropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.outerStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.outerEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.outerEndExtentPropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.outerEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGraphRange.prototype.strokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGraphRange.prototype.strokeThicknessPropertyName, Number, $.ig.XamLinearGraphRange.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGraphRange.prototype.strokeThicknessPropertyName, e.oldValue(), e.newValue());
}));

$.ig.LinearGraphTooltipDataContext.prototype.thicknessPropertyName = "Thickness";
$.ig.LinearGraphTooltipDataContext.prototype.outlinePropertyName = "Outline";
$.ig.LinearGraphTooltipDataContext.prototype.labelProperty = $.ig.DependencyProperty.prototype.register("Label", String, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) { ($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, o)).raisePropertyChanged("Label", e.oldValue(), e.newValue()); }));
$.ig.LinearGraphTooltipDataContext.prototype.itemProperty = $.ig.DependencyProperty.prototype.register("Item", $.ig.Object.prototype.$type, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) { ($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, o)).raisePropertyChanged("Item", e.oldValue(), e.newValue()); }));
$.ig.LinearGraphTooltipDataContext.prototype.itemNameProperty = $.ig.DependencyProperty.prototype.register("ItemName", $.ig.Object.prototype.$type, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) { ($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, o)).raisePropertyChanged("ItemName", e.oldValue(), e.newValue()); }));
$.ig.LinearGraphTooltipDataContext.prototype.itemBrushProperty = $.ig.DependencyProperty.prototype.register("ItemBrush", $.ig.Brush.prototype.$type, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) { ($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, o)).raisePropertyChanged("ItemBrush", e.oldValue(), e.newValue()); }));
$.ig.LinearGraphTooltipDataContext.prototype.thicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.LinearGraphTooltipDataContext.prototype.thicknessPropertyName, Number, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) { ($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, o)).raisePropertyChanged($.ig.LinearGraphTooltipDataContext.prototype.thicknessPropertyName, e.oldValue(), e.newValue()); }));
$.ig.LinearGraphTooltipDataContext.prototype.outlineProperty = $.ig.DependencyProperty.prototype.register($.ig.LinearGraphTooltipDataContext.prototype.outlinePropertyName, $.ig.Brush.prototype.$type, $.ig.LinearGraphTooltipDataContext.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.LinearGraphTooltipDataContext.prototype.$type, sender)).raisePropertyChanged($.ig.LinearGraphTooltipDataContext.prototype.outlinePropertyName, e.oldValue(), e.newValue());
}));

$.ig.XamLinearGauge.prototype.transitionProgressPropertyName = "TransitionProgress";
$.ig.XamLinearGauge.prototype.orientationPropertyName = "Orientation";
$.ig.XamLinearGauge.prototype.rangesPropertyName = "Ranges";
$.ig.XamLinearGauge.prototype.rangeBrushesPropertyName = "RangeBrushes";
$.ig.XamLinearGauge.prototype.rangeOutlinesPropertyName = "RangeOutlines";
$.ig.XamLinearGauge.prototype.minimumValuePropertyName = "MinimumValue";
$.ig.XamLinearGauge.prototype.maximumValuePropertyName = "MaximumValue";
$.ig.XamLinearGauge.prototype.valuePropertyName = "Value";
$.ig.XamLinearGauge.prototype.needleShapePropertyName = "NeedleShape";
$.ig.XamLinearGauge.prototype.needleNamePropertyName = "NeedleName";
$.ig.XamLinearGauge.prototype.rangeInnerExtentPropertyName = "RangeInnerExtent";
$.ig.XamLinearGauge.prototype.scaleInnerExtentPropertyName = "ScaleInnerExtent";
$.ig.XamLinearGauge.prototype.rangeOuterExtentPropertyName = "RangeOuterExtent";
$.ig.XamLinearGauge.prototype.scaleOuterExtentPropertyName = "ScaleOuterExtent";
$.ig.XamLinearGauge.prototype.needleInnerExtentPropertyName = "NeedleInnerExtent";
$.ig.XamLinearGauge.prototype.needleOuterExtentPropertyName = "NeedleOuterExtent";
$.ig.XamLinearGauge.prototype.needleInnerBaseWidthPropertyName = "NeedleInnerBaseWidth";
$.ig.XamLinearGauge.prototype.needleOuterBaseWidthPropertyName = "NeedleOuterBaseWidth";
$.ig.XamLinearGauge.prototype.needleInnerPointWidthPropertyName = "NeedleInnerPointWidth";
$.ig.XamLinearGauge.prototype.needleOuterPointWidthPropertyName = "NeedleOuterPointWidth";
$.ig.XamLinearGauge.prototype.needleInnerPointExtentPropertyName = "NeedleInnerPointExtent";
$.ig.XamLinearGauge.prototype.needleOuterPointExtentPropertyName = "NeedleOuterPointExtent";
$.ig.XamLinearGauge.prototype.intervalPropertyName = "Interval";
$.ig.XamLinearGauge.prototype.ticksPostInitialPropertyName = "TicksPostInitial";
$.ig.XamLinearGauge.prototype.ticksPreTerminalPropertyName = "TicksPreTerminal";
$.ig.XamLinearGauge.prototype.labelIntervalPropertyName = "LabelInterval";
$.ig.XamLinearGauge.prototype.labelExtentPropertyName = "LabelExtent";
$.ig.XamLinearGauge.prototype.labelsPostInitialPropertyName = "LabelsPostInitial";
$.ig.XamLinearGauge.prototype.labelsPreTerminalPropertyName = "LabelsPreTerminal";
$.ig.XamLinearGauge.prototype.minorTickCountPropertyName = "MinorTickCount";
$.ig.XamLinearGauge.prototype.tickStartExtentPropertyName = "TickStartExtent";
$.ig.XamLinearGauge.prototype.tickEndExtentPropertyName = "TickEndExtent";
$.ig.XamLinearGauge.prototype.tickStrokeThicknessPropertyName = "TickStrokeThickness";
$.ig.XamLinearGauge.prototype.tickBrushPropertyName = "TickBrush";
$.ig.XamLinearGauge.prototype.fontBrushPropertyName = "FontBrush";
$.ig.XamLinearGauge.prototype.needleBreadthPropertyName = "NeedleBreadth";
$.ig.XamLinearGauge.prototype.needleBrushPropertyName = "NeedleBrush";
$.ig.XamLinearGauge.prototype.needleOutlinePropertyName = "NeedleOutline";
$.ig.XamLinearGauge.prototype.needleStrokeThicknessPropertyName = "NeedleStrokeThickness";
$.ig.XamLinearGauge.prototype.minorTickStartExtentPropertyName = "MinorTickStartExtent";
$.ig.XamLinearGauge.prototype.minorTickEndExtentPropertyName = "MinorTickEndExtent";
$.ig.XamLinearGauge.prototype.minorTickStrokeThicknessPropertyName = "MinorTickStrokeThickness";
$.ig.XamLinearGauge.prototype.minorTickBrushPropertyName = "MinorTickBrush";
$.ig.XamLinearGauge.prototype.isScaleInvertedPropertyName = "IsScaleInverted";
$.ig.XamLinearGauge.prototype.backingBrushPropertyName = "BackingBrush";
$.ig.XamLinearGauge.prototype.backingOutlinePropertyName = "BackingOutline";
$.ig.XamLinearGauge.prototype.backingStrokeThicknessPropertyName = "BackingStrokeThickness";
$.ig.XamLinearGauge.prototype.backingInnerExtentPropertyName = "BackingInnerExtent";
$.ig.XamLinearGauge.prototype.backingOuterExtentPropertyName = "BackingOuterExtent";
$.ig.XamLinearGauge.prototype.scaleStartExtentPropertyName = "ScaleStartExtent";
$.ig.XamLinearGauge.prototype.scaleEndExtentPropertyName = "ScaleEndExtent";
$.ig.XamLinearGauge.prototype.scaleBrushPropertyName = "ScaleBrush";
$.ig.XamLinearGauge.prototype.scaleOutlinePropertyName = "ScaleOutline";
$.ig.XamLinearGauge.prototype.scaleStrokeThicknessPropertyName = "ScaleStrokeThickness";
$.ig.XamLinearGauge.prototype.mergeViewPortsPropertyName = "MergeViewPorts";
$.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledPropertyName = "IsNeedleDraggingEnabled";
$.ig.XamLinearGauge.prototype.transitionDurationPropertyName = "TransitionDuration";
$.ig.XamLinearGauge.prototype.showToolTipTimeoutPropertyName = "ShowToolTipTimeout";
$.ig.XamLinearGauge.prototype.toolTipPropertyName = "ToolTip";
$.ig.XamLinearGauge.prototype.fontPropertyName = "Font";
$.ig.XamLinearGauge.prototype.orientationProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.orientationPropertyName, $.ig.LinearScaleOrientation.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LinearScaleOrientation.prototype.getBox($.ig.LinearScaleOrientation.prototype.horizontal), function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.orientationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.rangesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.rangesPropertyName, $.ig.LinearGraphRangeCollection.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.rangesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.rangeBrushesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.rangeBrushesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.rangeBrushesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.rangeOutlinesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.rangeOutlinesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.rangeOutlinesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minimumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minimumValuePropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minimumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.maximumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.maximumValuePropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 100, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.maximumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.valueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.valuePropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.valuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleShapeProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleShapePropertyName, $.ig.LinearGraphNeedleShape.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LinearGraphNeedleShape.prototype.getBox($.ig.LinearGraphNeedleShape.prototype.needle), function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleShapePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleNameProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleNamePropertyName, String, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleNamePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.rangeInnerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.rangeInnerExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.05, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.rangeInnerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleInnerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleInnerExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleInnerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.rangeOuterExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.rangeOuterExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.65, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.rangeOuterExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleOuterExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleOuterExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleOuterExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleInnerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleInnerExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleInnerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleOuterExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleOuterExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleOuterExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleInnerBaseWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleInnerBaseWidthPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleInnerBaseWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleOuterBaseWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleOuterBaseWidthPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleOuterBaseWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleInnerPointWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleInnerPointWidthPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleInnerPointWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleOuterPointWidthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleOuterPointWidthPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleOuterPointWidthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleInnerPointExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleInnerPointExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleInnerPointExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleOuterPointExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleOuterPointExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleOuterPointExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.intervalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.intervalPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.intervalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.ticksPostInitialProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.ticksPostInitialPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.ticksPostInitialPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.ticksPreTerminalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.ticksPreTerminalPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.ticksPreTerminalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.labelIntervalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.labelIntervalPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.labelIntervalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.labelExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.labelExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.labelExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.labelsPostInitialProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.labelsPostInitialPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.labelsPostInitialPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.labelsPreTerminalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.labelsPreTerminalPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.labelsPreTerminalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minorTickCountProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minorTickCountPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 4, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minorTickCountPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.tickStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.tickStartExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.05, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.tickStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.tickEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.tickEndExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.65, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.tickEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.tickStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.tickStrokeThicknessPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.tickStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.tickBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.tickBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.tickBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.fontBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.fontBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.fontBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleBreadthProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleBreadthPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 6, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleBreadthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.needleStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.needleStrokeThicknessPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.needleStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minorTickStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minorTickStartExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.05, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minorTickStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minorTickEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minorTickEndExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.35, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minorTickEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minorTickStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minorTickStrokeThicknessPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minorTickStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.minorTickBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.minorTickBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.minorTickBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.isScaleInvertedProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.isScaleInvertedPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.isScaleInvertedPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.backingBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.backingBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.backingBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.backingOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.backingOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.backingOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.backingStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.backingStrokeThicknessPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.backingStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.backingInnerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.backingInnerExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.backingInnerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.backingOuterExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.backingOuterExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.backingOuterExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleStartExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.05, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleEndExtentPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.95, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.scaleStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.scaleStrokeThicknessPropertyName, Number, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.scaleStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype._mergeViewPortsProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.mergeViewPortsPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.mergeViewPortsPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.isNeedleDraggingEnabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.transitionDurationProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.transitionDurationPropertyName, $.ig.Number.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.transitionDurationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.showToolTipTimeoutProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.showToolTipTimeoutPropertyName, $.ig.Number.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, 500, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.showToolTipTimeoutPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.toolTipProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.toolTipPropertyName, $.ig.Object.prototype.$type, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.toolTipPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamLinearGauge.prototype.fontProperty = $.ig.DependencyProperty.prototype.register($.ig.XamLinearGauge.prototype.fontPropertyName, String, $.ig.XamLinearGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamLinearGauge.prototype.fontPropertyName, e.oldValue(), e.newValue());
}));

} (jQuery));


