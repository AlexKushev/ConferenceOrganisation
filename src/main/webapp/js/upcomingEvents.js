$(document).ready(function() {

	var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
  						"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

  	var days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

	$.getJSON('rest/events', function(response) {
		var eventsData = response.event;

		console.log(eventsData);

		var i, len = eventsData.length;
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
				var monthName = monthNames[date.getMonth()];
				var dayName = days[date.getDay()];

				var eventHtml = '<article id=' + id + ' class="event">' +
					'<a href="event.html" class="clear">' +
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
	}).done(function() {
		var loadMoreButton = $('#load-more-button');

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
	});
});