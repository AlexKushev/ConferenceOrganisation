$(document).ready(function() {

	var eventId = sessionStorage.getItem('detailsConferenceId');

	var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
  						"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

	$.getJSON('rest/events/eventByEventId?eventId=' + eventId, function(response) {
		var eventData = response.event;

		var title = eventData.title,
			description = eventData.description,
			address = eventData.hall.location,
            city = eventData.hall.city,
			price = eventData.price,
            maxSeats = eventData.hall.capacity,
			availableSeats = maxSeats - eventData.availableSeats;

		var datetime = eventData.date.split(' '),
			date = datetime[0].split('-'),
			time = datetime[1].split(':'),
			year = date[0],
			month = date[1],
			day = date[2],
			hours = time[0],
			minutes = time[1];

		var dateObj = new Date(year, month, day, hours, minutes, 0),
			currentDate = new Date();

		var monthName = monthNames[dateObj.getMonth()];

        // if past event
		if (dateObj.getTime() < currentDate.getTime()) {
            $('#filter-line-tabs a:first-child').removeClass('active');
            $('#filter-line-tabs a:last-child').addClass('active');
			$('#single-event').addClass('pastEvent');
		}

		var eventHtml = '<div class="singleEvent__header">' +
                    '<div class="eventDate">' +
                        '<div class="eventDate__day">' + day + '</div>' +
                        '<div class="eventDate__month">' + monthName.toUpperCase() + '</div>' +
                    '</div>' +
                    '<div class="singleEvent__title eventTitle">' +
                        '<h1>' + title +'</h1>' +
                        '<div class="eventTitle__details eventTitle__details--inline eventTitle__details--time">' + hours + ':' + minutes + ' </div> ' +
                        '<div class="eventTitle__details eventTitle__details--inline eventTitle__details--location">' +
                            '<a href="http://maps.google.com/?q=' + address + ', ' + city + '" target="_blank">' + address + ', ' + city + '</a>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
                '<hr />' +
				'<div class="singleEvent__main">' +
                    '<h2>Description</h2>' +
                    '<p>' + description + '</p>' +
					'<div id="lectures-container"></div>' +
                '</div>' +
                '<hr />' +
                '<div class="singleEvent__footer  clear">' +
                    '<span><strong>Seats</strong>:' + availableSeats + '/' + maxSeats +'</span>' +
                    '<div>' +
                        '<span>' + price + ' BGN </span>' +
                        '<a href="#" class="btn btn-yellow">Get Ticket</a>' +
                    '</div>' +
                '</div>';

        $('#single-event').append(eventHtml);

        $.getJSON('rest/lectures/getByEventId?eventId=' + eventId, function(res) {
        	var lecturesData = res.lecture;

        	var len = lecturesData.length;

        	if (len === 0) {
        		$('#lectures-container').append('<div class="alert alert-info">Currently there are no lectures for this event!</div>');
        	}
        	else {
        		for (var i = 0; i < len; i++) {
        			var lectureTitle = lecturesData[i].title,
        				lectureDescription = lecturesData[i].description,
        				lecturerName = lecturesData[i].lecturerName,
        				lecturerDescription = lecturesData[i].lecturerDescription;

        			var lectureHtml = '<div class="lecture">' +
                            '<div class="lecture__item lecture__item--lecture">' +
                                '<h3>' + lectureTitle + '</h3>' +
                                '<p>' + lectureDescription + '</p>' +
                            '</div>' +
                            '<div class="lecture__item lecture__item--lecturer">' +
                                '<h3>' + lecturerName + '</h3>' +
                                '<p>' + lecturerDescription + '</p>' +
                            '</div>' +
                        '</div>';

                    $('#lectures-container').append(lectureHtml);
        		}
        	}
        });
	});

});