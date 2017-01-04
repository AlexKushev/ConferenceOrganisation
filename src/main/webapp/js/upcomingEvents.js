$(document).ready(function() {

  	var selectedOption= $('#filter-by-city').val();

  	if (selectedOption === 'All') {
  		getAllEvents();
  	}
  	else {
  		filterByCity(selectedOption);
  	}

  	$('#filter-by-city').change(function() {
  		selectedOption = $('#filter-by-city').val();

  		if (selectedOption ==='All') {
  			getAllEvents();
  		}
  		else {
  			filterByCity(selectedOption);
  		}
  	});
});

function createEventsHtml(eventsData) {
	$('#events-container').empty();
	$('#events-container article').hide();

	var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
  						"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

  	var days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

	var i, len = eventsData.length;

	if (len === 0) {
		$('#events-container').text('No upcoming events!');
	}
	else {
		var upcomingEvents = [];
		for (i = 0; i < len; i++) {
			var id = eventsData[i].eventId;
			var title = eventsData[i].title;
			var price = eventsData[i].price;
			var location = eventsData[i].hall.location;
			var datetime = eventsData[i].date.split(' ');
			var year = datetime[0].split('-')[0];
			var month = datetime[0].split('-')[1];
			var day = datetime[0].split('-')[2];
			var hours = datetime[1].split(':')[0];
			var minutes = datetime[1].split(':')[1];
			var date = new Date(year, month, day, hours, minutes, 0, 0);

			var currentDate = new Date();

			if (date.getTime() >= currentDate.getTime()) {
				upcomingEvents.push(eventsData[i]);
				var monthName = monthNames[date.getMonth()];
				var dayName = days[date.getDay()];

				var eventHtml = '<article class="event">' +
					'<a href="event.html?id=' + id +'" class="clear">' +
                    	'<div class="event__date eventDate">' +
                        	'<div class="eventDate__day">' + day + '</div>' +
                        	'<div class="eventDate__month">' + monthName.toUpperCase() + '</div>' +
                    	'</div>' +
                    	'<div class="event__title eventTitle">' +
                        	'<h1>' + title + '</h1>' +
                        	'<div class="eventTitle__details eventTitle__details--block eventTitle__details--location">' + location + '</div>' +
                        	'<div class="eventTitle__details eventTitle__details--block eventTitle__details--price">' + price + ' BGN</div>' +
                    	'</div>' +
                    	'<div class="event__dateTime">' +
                        	day + ' ' + monthName + ' ' + '(' + dayName + ')' + //12 Dec (Tue)
                        	'<br /> at ' + hours + ':' + minutes + //20:00
                    	'</div>' +
                    	'<div class="eventCTA clear">' +
                        	'<span class="eventCTA__btn">Tickets</span>' +
                        	'<span class="eventCTA__arrow"></span>' +
                    	'</div>' +
                	'</a>' +
            	'</article>';

            	$('#events-container').append(eventHtml);
			}	
		}

		if (upcomingEvents.length === 0) {
			$('#events-container').text('No upcoming events!');
		}
	}	
}

function filterByCity(city) {
	$.getJSON('rest/events/eventsByCity?city=' + city, function(response) {
		var eventsData = response.event;

		createEventsHtml(eventsData);

	}).done(function() {
		loadMore();
	});
}

function getAllEvents() {
	$.getJSON('rest/events', function(response) {
		var eventsData = response.event;

		createEventsHtml(eventsData);

	}).done(function() {
		loadMore();
	});
}

function loadMore() {
	var loadMoreButton = $('#load-more-button');
	loadMoreButton.show();

	var articlesSize = $("#events-container article").size();
    var itemsToShow = 10;

    $('#events-container article:lt(' + itemsToShow + ')').show();

    if (itemsToShow >= articlesSize) {
        loadMoreButton.hide();
    }

    loadMoreButton.click(function () {
        itemsToShow = (itemsToShow + 10 <= articlesSize) ? itemsToShow + 10 : articlesSize;
        $('#events-container article:lt(' + itemsToShow + ')').show();

        if (itemsToShow == articlesSize) {
        	loadMoreButton.hide();
        }
    });
}