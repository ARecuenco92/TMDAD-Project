var stomptClient = null;
var subscription = null;
var markers = [];
var dict = {};

$(document).ready(function() {
	setupDial();
	setupTimeline();
	setMap();
	connect();
});

function setupTimeline() {
	$.get('twitter/timeline', function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
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

function connect() {
	var socket = new SockJS("/political");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
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