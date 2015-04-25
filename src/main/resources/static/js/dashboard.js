$(document).ready(function() {
	if($('.panel-political-parties').css('display') !== 'none'){
		resumeParties();	
	}
	setMap();
	setupTimeline();
	setupDial();
});

function setupTimeline() {
	$.get('timeline', function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
	});	

	$.get('timelineFacebook', function(data) {
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

function resumeParties(){
	$.ajax({
		url: 'resumeParties',
		method: 'GET',
		async:false,
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
		zoomMax : 1,
		series : {
			regions : [ {
				attribute : 'fill'
			} ]
		},
		markerStyle: {
			initial: {
				fill: '#428BCA'
			}
		}
	});
	map.series.regions[0].setValues(generateColors());

	$.get('geolocalize', function(data) {
		var max = data.length;
		var markers = [], latLng;
		for(var i = 0; i< max; i++){
			latLng = [data[i].geoLocation.latitude, data[i].geoLocation.longitude];
			markers[i] = {
					latLng : latLng,
					name : "@" + data[i].user.screenName
			}
		}
		map.addMarkers(markers);
	});	
}