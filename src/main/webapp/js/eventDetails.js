var lat;
var lng;

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
                        '<span class="ticketPrice">' + price + ' BGN </span>' +
                        '<button type="button" class="btn btn-yellow" data-toggle="modal" data-target="#myModal">Get Ticket</button>' +
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

        var buyTicketsPopup = '<div class="modal fade" id="myModal" role="dialog">' +
            '<div class="modal-dialog modal-sm buyTickets">' +
                '<div class="modal-content">' +
                    '<div class="modal-header">' +
                        '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
                        '<h4 class="modal-title">Buy Tickets</h4>' +
                    '</div>' +
                    '<div class="modal-body">' +
                    '<p>' +
                        '<strong>Number of Tickets:</strong>' +
                        '<input id="ticketsNumber" type="number" name="ticketsNumber" value="1" class="ticketsNumber" />' +
                        '<span class="totalTicketPrice">' + price + ' BGN</span>' +
                    '</p>' +
                    '<p>' +
                        '<strong>Payment Methods:</strong><br />' +
                        'Bank Transfer - IBAN: 0000 0000 0000 0000' +
                    '</p>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                        '<button id="buy-ticket-button" type="button" class="btn btn-yellow">Buy Now</button>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>';
        $('.wrap').append(buyTicketsPopup);
        
    	var splittedAddress = address.split(" ");
    	$.getJSON('https://maps.googleapis.com/maps/api/geocode/json?address=' + splittedAddress[2] + '+' + splittedAddress[0] + '+' + splittedAddress[1], + '+' + city + '&key=AIzaSyA3QcohwAtZCgvMNzaaOH-Wga_C6ca-T6Q', function(response) {    		
    		var splittedAddress = address.split(" ");
    		
    		lat = response.results[0].geometry.location.lat;
    		lng = response.results[0].geometry.location.lng;   
    		
    		var script = document.createElement("script");
    	    script.type = "text/javascript"; 
    	    document.getElementsByTagName("head")[0].appendChild(script);
    	    script.src = "https://maps.googleapis.com/maps/api/js?key=AIzaSyA3QcohwAtZCgvMNzaaOH-Wga_C6ca-T6Q&callback=initMap";
    	});
        
        $('#ticketsNumber').on('change', function () {
            var ticketsNumber = $('#ticketsNumber').val();
            $('.totalTicketPrice').text(price * ticketsNumber + ' BGN');
        });

        var buyTicketButton = $('#buy-ticket-button');
        buyTicketButton.on('click', function() {
            buyTicket();
        });
	});
});

function buyTicket() {
    var eventId = sessionStorage.getItem('detailsConferenceId');

    $.ajax({
        type: 'POST',
        url: 'rest/user/bookTicket?eventId=' + eventId
    }).done(function() {
        alert('Successfully bought ticket!');
    }).fail(function() {
        alert('Failed to buy ticket!');
    });
}

function initMap() {
    var uluru = {
        lat: lat,
        lng: lng
    };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12,
        center: uluru
    });
    var marker = new google.maps.Marker({
        position: uluru,
        map: map
    });
}
