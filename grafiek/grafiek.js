var Flot = function(container, from, to) {
	this.init(container, from, to);
}

$.extend(Flot.prototype, {

	// object variables
	container: '',
	containerName: '',
	data: {},
	options: null,

	init: function(container, from, to) {
		this.containerName = container;
		this.container = $(container+' div');
		this.data = this.getData(from, to);
		this.options = new Options();
		this.draw();
		this.addEvents();
	},

	getData: function(from, to) {
		return this.getDummyData(from, to);
	},
	
	getDummyData: function(from, to) {
		//alert("data opvragen");
		var d = [];
		for (var i = 0; i <= 250; ++i) {
			var x = from + i * (to - from) / 500;
			d.push([x*10000000, (Math.sin(x+4)+2)*25]);
		}

		return [
			{ label: "Beursverloop Tomtom", data: d, color: "lightblue" }
		];
	},
	
	draw: function() {
		$.plot(this.container, this.data, this.options);
	},
	
    getMax: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var max = null;
 		for(i=0;i<this.data[0]['data'].length;i++)
        	if (this.data[0]['data'][i][0] <= to && this.data[0]['data'][i][0] >= from) {
	        	if (max == null)
	        		max = this.data[0]['data'][i][1];
	        	if (this.data[0]['data'][i][1] > max )
    	    		max =  this.data[0]['data'][i][1];
    	    }
    	return max;
    },

    getMin: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var min = null; 
 		for(i=0;i<this.data[0]['data'].length;i++)
        	if (this.data[0]['data'][i][0] <= to && this.data[0]['data'][i][0] >= from) {
	        	if (min == null)
	        		min = this.data[0]['data'][i][1];
	        	if (this.data[0]['data'][i][1] < min )
	        		min =  this.data[0]['data'][i][1];
	       	}
    	return min;
    },
    
    addEvents: function() {
		this.container.bind('plotzoom', {me: this}, function (event, plot) {
			var ranges = plot.getAxes();
			event.data.me.options.setXRange(ranges.xaxis.min, ranges.xaxis.max);
			event.data.me.options.setYRange(event.data.me.getMin(ranges.xaxis.min, ranges.xaxis.max), event.data.me.getMax(ranges.xaxis.min, ranges.xaxis.max));
			event.data.me.draw();
		});
		
		$(this.containerName+' li.hand.selection').bind('click', {me: this}, function(event) {
			event.data.me.options.addSelection();
			event.data.me.draw();
			return false;
		});

    }
    
});


$(function (){
	var flot1 = new Flot('#plotTest', 2, 11);
});