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
			filter['radius'] = latitude: circle.getRadius();
			filter['latitude'] = latitude: circle.getLatLng().lat;
			filter['longitude'] = latitude: circle.getLatLng().lng;
		}
		$.ajax({
			url : 'search',
			data : JSON.stringify(filter),
			method: "POST",
			contentType : "application/json; charset=utf-8",
			success: function(data) {
				var template = $('#twitterMsn').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, data);
				$('#results').html(rendered);
			}
		});
	}
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
	}
}