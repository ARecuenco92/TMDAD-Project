var stomptClient = null;
var subscription = null;
var markers = [];
var dict = {};

$(document).ready(function() {
	if($('.panel-political-parties').css('display') !== 'none'){
		resumeParties();	
	}
	setMap();
	setupTimeline();
	setupDial();
	setChart();
});

function setupTimeline() {
	$.get('twitter/timeline', function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
		connect();
		getTrends(data.tweets);
	});	

	$.get('facebook/timeline', function(data) {
		var template = $('#facebookBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineFacebook').html(rendered);
	});	

	$('#twitter').click(function() {
		$('#timelineTwitter').css("display", "block");
		$('#timelineFacebook').css("display", "none");
	});

	$('#facebook').click(function() {
		$('#timelineTwitter').css("display", "none");
		$('#timelineFacebook').css("display", "block");
	});

}

function getTrends(tweets){
	var max = tweets.length;
	var trends;
	for(var i = 0; i < max; i++){
		addTrend(tweets[i]);
	}
	sortTrends();
}

function addTrend(tweet){
	var trends = tweet.entities.hashTags;
	for(var j = 0; j < trends.length; j++){
		dict[trends[j].text] = dict[trends[j].text] === undefined? 1: dict[trends[j].text] + 1;
	}
}

function sortTrends(){
	// Create items array
	var items = Object.keys(dict).map(function(key) {
		return [key, dict[key]];
	});

	// Sort the array based on the second element
	items.sort(function(first, second) {
		return second[1] - first[1];
	});

	var trends = items.slice(0, 5).map(function(item) {
		return {trend: item[0], count : item[1]};
	});

	var template = $('#trendsBlock').html();
	Mustache.parse(template); 
	var rendered = Mustache.render(template, trends);
	$('#twitterTrends').html(rendered);
}

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

function setupDial(){
	$('.knob').knob({
		value : 0,
		'readOnly' : true,
		'width' : 80,
		'height' : 80,
		'dynamicDraw' : true,
		'thickness' : 0.2,
		'tickColorizeValues' : true,
		'fgColor' : "#D3D4D2",
		'bgColor' : "#F8F9F7",
		'skin' : 'tron'
	});
	myDelay(0);
}

function myDelay(value) {
	$('.knob').val(value).trigger("change");
	if($('.knob').data('max') > value){
		setTimeout(myDelay, 50, value+1);
	}
	else{
		setTimeout(myDelay, 50, 0);
	}   
}

function setMap(){
	var generateColors = function() {
		var colors = {}, key;
		for (key in map.regions) {
			colors[key] = "#BAD6D9";
		}
		return colors;
	};

	var map;
	map = new jvm.Map({
		map : 'es_merc_en',
		backgroundColor : 'none',
		zoomButtons : false,
		container : $('#spain-map'),
		zoomMax : 6,
		markersSelectable: true,
		markersSelectableOne: true,
		markerStyle: {
			selected: {
				fill: '#75787B'
			}
		},
		series : {
			regions : [ {
				attribute : 'fill'
			} ]
		},
		onMarkerSelected: function(event, index, state){
			if(state){
				var tweet = markers[index].tweet;
				var template = $('#geoTweetBlock').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, tweet);
				$('#geoTweet').html(rendered);
			}
		}
	});
	map.series.regions[0].setValues(generateColors());

	$.get('twitter/geolocalize', function(data) {
		addMarkers(map, data);
	});

	$.get('facebook/geolocalize', function(data) {
		addMarkers(map, data);
	});	
}

function addMarkers(map, data){
	var max = data.length;
	var latLng;
	var mentions = [];
	var parties;
	for(var i = 0; i< max; i++){
		latLng = [data[i].latitude, data[i].longitude];
		mentions = data[i].mentions;
		parties = 0;
		for(var j = 0; j< mentions.length; j++){
			if(mentions[j] == 'ahorapodemos'){
				color = "#6A205F";
				parties ++;
			}
			if(mentions[j] == 'PPopular'){
				color = "#006EC6";
				parties ++;
			}
			if(mentions[j] == 'PSOE'){
				color = "#D20804";
				parties ++;
			}
			if(mentions[j] == 'CiudadanosCs'){
				color = "#F38725";
				parties ++;
			}
		}
		if(parties != 1){
			color = "#F3F3F3";
		}
		markers[i] = {
				latLng : latLng,
				name : "@" + data[i].screenName,
				tweet: data[i],
				style: {
					fill: color,
					stroke: '#DFDFDF',
					r: Math.min(data[i].relevance + 4, 8)
				}
		}
	}
	map.addMarkers(markers);
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

function connect() {
	var socket = new SockJS("/political");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		twitterSubscribe("podemos");
		twitterSubscribe("pp");
		twitterSubscribe("psoe");
		twitterSubscribe("ciudadanos");
		
		facebookSubscribe("podemos");
		facebookSubscribe("pp");
		facebookSubscribe("psoe");
		facebookSubscribe("ciudadanos");
	});
}

function twitterSubscribe(party) {
	stompClient.send("/app/twitter/"+party);
	subscription = stompClient.subscribe("/queue/twitter/"+party, function(data) {
		var tweet = JSON.parse(data.body);
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, {tweets: [tweet]});
		$('#timelineTwitter').prepend(rendered);
		addTrend(tweet);
		sortTrends();
	});
}

function facebookSubscribe(party){
	stompClient.send("/app/facebook/"+party);
	subscription = stompClient.subscribe("/queue/facebook/"+party, function(data) {
		var post = JSON.parse(data.body);
		var template = $('#facebookBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, [post]);
		$('#timelineFacebook').prepend(rendered);
	});
}