var map;
var circle;

$(document).ready(function() {
	$("#advanceSearch").css('visibility', 'hidden');
	$("#advanceSearch").collapse('hide');
	setTimeout(function(){$("#advanceSearch").css('visibility', 'visible')}, 1000);
	$("#search-btn").on("click", search);
	$("#remove-geo").on("click", removeGeo);
	setMap();
});


function search(event){
	event.preventDefault();
	var criteria = $("#key-words");
	if(criteria.val().trim() === ""){
		criteria.parent("div").addClass("has-error");
	}
	else{
		criteria.parent("div").removeClass("has-error");
		var filter = {};
		filter['politicalParties'] = $("[name='political-party']").val();
		filter['user'] = $("[name='user']").val();
		filter['keyWords'] = $("[name='key-words']").val();
		filter['sortBy'] = $("[name='sort-by']").val();
		if(circle){
			filter['radius'] =  circle.getRadius()/1000;
			filter['latitude'] = circle.getLatLng().lat;
			filter['longitude'] = circle.getLatLng().lng;
		}
		$.ajax({
			url : 'search',
			data : JSON.stringify(filter),
			method: "POST",
			contentType : "application/json; charset=utf-8",
			success: function(data) {
				data = eliminarDuplicados(data);
				var template = $('#twitterMsn').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, data);
				$('#results').html(rendered);
				moveToResults();
			}
		});
	}
}

function moveToResults(){
	var offset = $('#results').offset(); 
	offset.top -= 60;
	$('html, body').animate({
	    scrollTop: offset.top,
	});
}

function setMap(){
	map = L.map('map').setView([39.8, -3.71], 5);
	var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	var osm = new L.TileLayer(osmUrl, {minZoom: 5, maxZoom: 5});		
	map.addLayer(osm);
	map.on('click', function(e){
		removeGeo();
		var km = $("[name='radius']").val() || 100;
		circle = L.circle(e.latlng, 1000*km, {
			color: 'red',
			fillColor: '#f03',
			fillOpacity: 0.5
		});
		circle.addTo(map);
	});
	map.dragging.disable();
}

function removeGeo(event){
	if(event){
		event.preventDefault();	
	}
	if(circle){
		// Remove the last circle
		map.removeLayer(circle);
		circle = undefined;
	}
}

function eliminarDuplicados(data){
	var tweets = [];
	var i;
	for (i = 0; i < data.length; ++i) {
    	var j;
    	var repetido = false;
    	for(j = 0;j<tweets.length && !repetido;j++){
    		if(data[i].retweet == true && tweets[j].retweet == true){
    			if(tweets[j].retweetedStatus.id == data[i].retweetedStatus.id){
    				repetido = true;
    			}
    			else if(tweets[j].id == data[i].retweetedStatus.id ){
        			repetido = true;
        		}
    			else if(tweets[j].retweetedStatus.id == data[i].id ){
        			repetido = true;
        		}
    		}
    		else if(data[i].retweet == false && tweets[j].retweet == true){
    			if(tweets[j].retweetedStatus.id == data[i].id){
    				repetido = true;
    			}
    		}
    		else if(data[i].retweet == true && tweets[j].retweet == false){
    			if(tweets[j].id == data[i].retweetedStatus.id){
    				repetido = true;
    			}
    		}
    	}
    	if(repetido == false){
    		tweets.push(data[i]);
    	}
	    
	}
	return tweets;
}