var stomptClient = null;
var subscription = null;
var dict = {};
var politicalParty;

$(document).ready(function() {
	if(window.location.hash) {
		politicalParty = window.location.hash.substring(1);
		displayPoliticalParty();
	}
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
		displayPoliticalParty();
	}
}

function displayPoliticalParty(){
	$("[id='"+politicalParty+"']").parent().addClass('active');
	clear();
	setupDial();
	setupTimeline();
	connect();
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
	if (subscription) {
		subscription.unsubscribe();
	}
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