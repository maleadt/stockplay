Flot = function(container) {
	this.init(container);
}

$.extend(Flot.prototype, {

	// object variables
	widget_name: '',

	init: function(widget_name) {
		// do initialization here
		this.widget_name = widget_name;
	},

	doSomething: function() {
		alert('my name is '+this.widget_name);
	}

});


$(function (){

	var widget1 = new Flot('widget one');
	widget1.doSomething();

});