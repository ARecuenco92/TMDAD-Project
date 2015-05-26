$(document).ready(function() {
	resumeParties();
	setChart();
});

function resumeParties(){
	$.ajax({
		url: 'resumeParties',
		method: 'GET',
		success: function(data) {
			var template = $('#resumeBlock').html();
			Mustache.parse(template); 
			var rendered = Mustache.render(template, data);
			$('#resumeParties').append(rendered);
		}
	});	
}

function setChart(){
	var podemos = {
			label: "Podemos",
			fillColor: "rgba(106,32,95,0.2)",
			strokeColor: "rgba(106,32,95,1)",
			pointColor: "rgba(106,32,95,1)",
			pointStrokeColor: "#fff",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)"
	}

	var pp = {
			label: "Partido Popular",
			fillColor: "rgba(0,110,198,0.2)",
			strokeColor: "rgba(0,110,198,1)",
			pointColor: "rgba(0,110,198,1)",
			pointStrokeColor: "#fff",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)"
	}

	var ciudadanos = {
			label: "Ciudadanos",
			fillColor: "rgba(243,135,37,0.2)",
			strokeColor: "rgba(243,135,37,1)",
			pointColor: "rgba(243,135,37,1)",
			pointStrokeColor: "#fff",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)"
	}

	var psoe = {
			label: "PSOE",
			fillColor: "rgba(210,8,4,0.2)",
			strokeColor: "rgba(210,8,4,1)",
			pointColor: "rgba(210,8,4,1)",
			pointStrokeColor: "#fff",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(151,187,205,1)"
	}

	$.get('chart/evolution/percentage', function(data) {
		var charts = data.data;
		podemos.data = charts[0].dataSet;
		pp.data = charts[1].dataSet;
		psoe.data = charts[2].dataSet;
		ciudadanos.data = charts[3].dataSet;

		var chartData = {
				labels: charts[0].labels,
				datasets: [podemos, pp, ciudadanos, psoe]
		};
		var options = {
				scaleLabel : "<%= value + ' %' %>",
				multiTooltipTemplate: "<%if (datasetLabel){%><%=datasetLabel%>: <%}%><%= Number(value).toFixed(2) + ' %'%>"
		};
		var ctx = $("#evolutionChart").get(0).getContext("2d");
		var myLineChart = new Chart(ctx).Line(chartData, options);
	});

	$.get('chart/evolution/absolute', function(data) {
		var charts = data.data;
		podemos.data = charts[0].dataSet;
		pp.data = charts[1].dataSet;
		psoe.data = charts[2].dataSet;
		ciudadanos.data = charts[3].dataSet;

		var chartData = {
				labels: charts[0].labels,
				datasets: [podemos, pp, ciudadanos, psoe]
		};
		var options = {
				scaleLabel : "<%= value + ' seguidores' %>",
				multiTooltipTemplate: "<%if (datasetLabel){%><%=datasetLabel%>: <%}%><%= value + ' seguidores'%>"
		};

		var ctx = $("#evolutionChart2").get(0).getContext("2d");
		var myLineChart = new Chart(ctx).Line(chartData, options);
	});
}