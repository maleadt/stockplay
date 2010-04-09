var Flot = function(container, from, to) {
	this.init(container, from, to);
}

$.extend(Flot.prototype, {

	init: function(container, from, to) {
		this.containerName = container;
		this.container = $(container+' div');
		this.data = this.getData(from, to);
		this.options = new Options();
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
		this.draw();
		this.addEvents();
		this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
		this.history = new Array();
	},

	getData: function(from, to) {
		return this.getDummyData(from, to);
	},
	
	getDummyData: function(from, to) {
		//alert("punten opvragen");
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
		this.plot = $.plot(this.container, this.data, this.options);
	},

	pushHistory: function() {
		this.history.push(this.lastView);
		this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
	},
	
    getMax: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var max;
    	var data;
		for (line in this.data) {
			data = this.data[line]['data'];
			for(i=0;i<data.length;i++)
		    	if (data[i][0] <= to && data[i][0] >= from) {
		        	if (max === undefined)
	    	    		max = data[i][1];
	        		if (data[i][1] > max )
		    			max =  data[i][1]; // Zou het niet sneller zijn om enkel de index van het grooste element bij te houden?
		    	}
		}
    	return max;
    },

    getMin: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var min;
		for (line in this.data) {
			data = this.data[line]['data'];
			for(i=0;i<this.data[0]['data'].length;i++)
		    	if (this.data[0]['data'][i][0] <= to && this.data[0]['data'][i][0] >= from) {
		        	if (min === undefined)
		        		min = this.data[0]['data'][i][1];
		        	if (this.data[0]['data'][i][1] < min )
		        		min =  this.data[0]['data'][i][1];
		       	}
		}       	
    	return min;
    },

    setView: function(from, to) {
		this.options.setXRange(from, to);
		this.options.setYRange(this.getMin(from, to), this.getMax(from, to));
    },
    
    addEvents: function() {
    
		this.container.bind('plotzoom', {me: this}, function (event, plot) {
			var ranges = plot.getAxes().xaxis;
			var me = event.data.me;
			me.setView(ranges.min, ranges.max);
			me.draw();
			me.pushHistory();
		});

	    this.container.bind("plotselected", {me: this}, function (event, ranges) {
			var me = event.data.me;
			me.setView(ranges.xaxis.from, ranges.xaxis.to);
			me.draw();
			me.pushHistory();
		});

		this.container.bind('plotpan', {me: this}, function (event, plot) {
			var ranges = plot.getAxes().xaxis;
			var me = event.data.me;
			me.setView(ranges.min, ranges.max);
			me.draw();
			me.pushHistory();
		});
		
		$(this.containerName+' li.selection').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			me.options.setSelectionMode();
			me.draw();
			$(me.containerName+' .selection').hide();
			$(me.containerName+' .pan').show();
			me.pushHistory();
			return false;
		});

		$(this.containerName+' li.pan').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			me.options.setPanningMode();
			me.draw();
			$(me.containerName+' .pan').hide();
			$(me.containerName+' .selection').show();
			me.pushHistory();
			return false;
		});

		$(this.containerName+' li.zoomOut').bind('click', {me: this}, function(event) {
			event.data.me.plot.zoomOut();
			return false;
		});

		$(this.containerName+' li.zoomIn').bind('click', {me: this}, function(event) {
			event.data.me.plot.zoom();
			return false;
		});

		$(this.containerName+' li.reset').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			delete me.options;
			me.options = new Options();
			me.draw();
			me.setView(me.plot.getAxes().xaxis.min, me.plot.getAxes().xaxis.max);
			// dubbel tekenen vervangen door from en max functie
			me.draw();
			me.history.length = 0;
			var ranges = me.plot.getAxes().xaxis;
			me.lastView = [ranges.min, ranges.max];
			return false;
		});

		$(this.containerName+' li.last').bind('click', {me: this}, function(event) {
			var me = event.data.me;
	   		if (me.history.length == 0)
	   			return false;
	   		var range = me.history.pop();
			me.setView(range[0], range[1]);
			me.draw();
	   		if (me.history.length == 0) {
				var ranges = me.plot.getAxes().xaxis;
				me.lastView = [ranges.min, ranges.max];
			}
			return false;
		});
    }
    
});


$(function (){
	var flot1 = new Flot('#plotTest', 2, 11);
});