// Default options

var Options = function() {
	this.init();
}

$.extend(Options.prototype, {

	init: function() {
		
		this.legend = {
			show: true,
			position: "sw"
		};
		
		this.series = {
			lines: {
				show: true,
				fill: true,
				lineWidth: 2
			},
			points: { show: false }
		};
		
		this.x2axis = {
			ticks: 9,
			mode: "time"
		};
		
		this.yaxis = {
			ticks: 6,
			min: 0,
			max: 80
		};
		
		this.pan = {
			interactive: true
		};
		
		this.zoom = {
			interactive: true
		};
		
		this.grid = {
			borderWidth: 1,
			borderColor: "#d8d8d8",
			labelMargin: 8,
			color: "#444",
			backgroundColor: "#fafafa",
			borderColor: "#fff",
			tickColor: "#ddd",
			borderWidth: 0,
			hoverable: true,
			autoHighlight: true,
			mouseActiveRadius: 50
		};
	
		this.xaxis = {
			ticks: 9,
			mode: "time"
		};
	},
	
    // Option changers
	setSelectionMode: function() {
		this.selection = {
			mode: "x"
		};

		this.pan = {
			interactive: false
		};
	},

	setPanningMode: function() {
		this.selection = {};

		this.pan = {
			interactive: true
		};
	},

	setXRange: function(min, max) {
		this.xaxis.min = min;
		this.xaxis.max = max;
	},

	setYRange: function(min, max) {
		this.yaxis.min = min;
		this.yaxis.max = max;
	}	

});