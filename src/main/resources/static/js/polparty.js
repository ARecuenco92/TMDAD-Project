var stomptClient = null;
var twitterSubscription = null
var facebookSubscription = null;
var dict = {};
var politicalParty;

$(document).ready(function() {
	if(window.location.hash) {
		politicalParty = window.location.hash.substring(1);
		displayPoliticalParty();
	}
	connect();
	$(".party").click(changePoliticalParty);
});

function changePoliticalParty(event){
	event.preventDefault();
	if($("#navbar").attr('aria-expanded')){
		$("#navbar").collapse('hide');	
	}
	var nextParty = $(this).attr('id');
	if(nextParty != politicalParty){
		$("[id='"+politicalParty+"']").parent().removeClass('active');
		politicalParty = nextParty;
		document.location.hash = politicalParty;
		twitterSubscribe(politicalParty);
		facebookSubscribe(politicalParty);
		displayPoliticalParty();
	}
}

function displayPoliticalParty(){
	$("[id='"+politicalParty+"']").parent().addClass('active');
	clear();
	setupDial();
	fullParty();
	getDoughnut();
	setChartParty();
	setupTimeline();
}

function clear(){
	$('#timelineTwitter').html('<div class="text-center panel-knob"><input class="knob" data-max="50" data-displayInput="false"/></div>');
	$('#timelineFacebook').html('<div class="text-center panel-knob"><input class="knob" data-max="50" data-displayInput="false"/></div>');
	$('#twitterTrends').html("");
	dict = {};
}

function setupTimeline() {	
	$.get('twitter/timeline/'+politicalParty, function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
		getTrends(data.tweets);
	});	

	$.get('facebook/timeline/'+politicalParty, function(data) {
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

function connect() {
	var socket = new SockJS("/political");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		twitterSubscribe(politicalParty);
		facebookSubscribe(politicalParty);
	});
}

function twitterSubscribe(party) {
	if (twitterSubscription) {
		twitterSubscription.unsubscribe();
	}
	stompClient.send("/app/twitter/"+party);
	twitterSubscription = stompClient.subscribe("/queue/twitter/"+party, function(data) {
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
	if (facebookSubscription) {
		facebookSubscription.unsubscribe();
	}
	stompClient.send("/app/facebook/"+party);
	facebookSubscription = stompClient.subscribe("/queue/facebook/"+party, function(data) {
		var post = JSON.parse(data.body);
		var template = $('#facebookBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, [post]);
		$('#timelineFacebook').prepend(rendered);
	});
}

function fullParty(){
	var p;
	if(politicalParty == "pp"){
		p = "pp";
	}
	else if(politicalParty == "psoe"){
		p = "psoe";
	}
	else if(politicalParty == "podemos"){
		p = "269212336568846";
	}
	else{
		p = "Cs.Ciudadanos";
	}
	$.get("http://graph.facebook.com/"+p+"?fields=location",function(info){
		$.ajax({
			data:info,
			dataType: 'json',
			url: '/polparty/fullParty/'+politicalParty,
			method: 'POST',
			async:false,
			success: function(data) {
				var template = $('#partyInfoBlock').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, data);
				$('#partyInfo').html(rendered);
			},
			error: function(data) {
	            var data = {"name": "Podemos",
	            			"color": "purple",
	            			"logo": "http://graph.facebook.com/269212336568846/picture",
	            			"webpage": "http://www.podemos.info/",
	            			"email": "null",
	            			"location":{"country": "Spain",
	            						"city": "Madrid",
	            						"street": "Calle Zurita, 21",
	            						"zip": "28005"}}
	            var template = $('#partyInfoBlock').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, data);
				$('#partyInfo').html(rendered);
	        }
		});	
	});
}

function setChartParty(){
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

	$.get('/chart/evolution/absolute', function(data) {
		var partido;
		var charts = data.data;

		if(politicalParty == "pp"){
			pp.data = charts[1].dataSet;
			partido = pp;
		}
		else if(politicalParty == "psoe"){
			psoe.data = charts[2].dataSet;
			partido = psoe;
		}
		else if(politicalParty == "podemos"){
			podemos.data = charts[0].dataSet;
			partido = podemos;
		}
		else{
			ciudadanos.data = charts[3].dataSet;
			partido = ciudadanos;
		}
		var chartData = {
				labels: charts[0].labels,
				datasets: [partido]
		};
		var options = {
				scaleLabel : "<%= value + ' seguidores' %>",
				multiTooltipTemplate: "<%if (datasetLabel){%><%=datasetLabel%>: <%}%><%= Number(value).toFixed(2) + ' %'%>"
		};
		var ctx = $("#evolutionChart").get(0).getContext("2d");
		var myLineChart = new Chart(ctx).Line(chartData, options);
	});
}

function getDoughnut(segPodemos,segPp,segPsoe,segCiudadanos){
	var podemos = {
			color:"rgba(106,32,95, 0.7)",
	        highlight: "rgba(106,32,95, 1)",
	        label: "Podemos"
	}
	var pp = {
			color:"rgba(0,110,198,0.7)",
	        highlight: "rgba(0,110,198, 1)",
	        label: "PP"
	}
	
	var psoe = {
			color:"rgba(210,8,4,0.7)",
	        highlight: "rgba(210,8,4, 1)",
	        label: "PSOE"	
	}
	
	var ciudadanos = {
			color:"rgba(243,135,37,0.7)",
	        highlight: "rgba(243,135,37, 1)",
	        label: "Ciudadanos"	
	}
	
	$.get('/chart/evolution/absolute', function(data) {
		var charts = data.data;
		
		var num = charts[0].dataSet.length;
		podemos.value = charts[0].dataSet[num-1];
		pp.value = charts[1].dataSet[num-1];
		psoe.value = charts[2].dataSet[num-1];
		ciudadanos.value = charts[3].dataSet[num-1];
	
		var data = [podemos,pp,psoe,ciudadanos];
		
		var ctx = $("#apollosChart").get(0).getContext("2d");
		var myDoughnutChart = new Chart(ctx).Doughnut(data,{
		    animateScale: true
		});
	});
}

