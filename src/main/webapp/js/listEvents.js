$(document).ready(function() {

    loadCities();

    var selectedOption = $('#filter-by-city').val();

    if (selectedOption === 'All') {
        getAllEvents();
    } else {
        filterByCity(selectedOption);
    }

    $('#filter-by-city').change(function() {
        selectedOption = $('#filter-by-city').val();

        if (selectedOption === 'All') {
            getAllEvents();
        } else {
            filterByCity(selectedOption);
        }
    });
});

function loadCities() {
    $.getJSON('rest/events/cities', function(response) {
        var cities = response.citiesContainer.cities;

        var city, 
            i, 
            len = cities.length;
        for (i = 0; i < len; i++) {
            city = cities[i];
            $('#filter-by-city').append('<option value="' + city + '">' + city + '</option>');
        }
    });
}

function createEventsHtml(eventsData) {
    $('#events-container').empty();
    $('#events-container article').hide();

    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    var days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    var i, len = eventsData.length;

    if (len === 0) {
        $('#events-container').text('No events to show!');
    } else {
        var id, title, price, location, city, datetime, year, month, day, hours, minutes, date;
        var monthName, dayName, eventHtml;
        var dateCondition;
        var eventsList = [];
        var currentDate = new Date();
        var eventButtonText;

        var currentPage = document.location.pathname.match(/[^\/]+$/)[0];

        for (i = 0; i < len; i++) {
            id = eventsData[i].eventId;
            title = eventsData[i].title;
            price = eventsData[i].price;
            location = eventsData[i].hall.location;
            city = eventsData[i].hall.city;
            datetime = eventsData[i].date.split(' ');
            year = datetime[0].split('-')[0];
            month = datetime[0].split('-')[1];
            day = datetime[0].split('-')[2];
            hours = datetime[1].split(':')[0];
            minutes = datetime[1].split(':')[1];
            date = new Date(year, month, day, hours, minutes, 0, 0);

            if (currentPage.indexOf('index') != -1) {
                dateCondition = date.getTime() >= currentDate.getTime();
                eventButtonText = 'Tickets';
            } else if (currentPage.indexOf('pastevents') != -1) {
                dateCondition = date.getTime() < currentDate.getTime();
                eventButtonText = 'Details';
            }

            if (dateCondition) {
                eventsList.push(eventsData[i]);
                monthName = monthNames[date.getMonth()];
                dayName = days[date.getDay()];

                eventHtml = '<article id="' + id + '" class="event">' +
                    '<a href="event.html" class="clear event-details">' +
                    '<div class="event__date eventDate">' +
                    '<div class="eventDate__day">' + day + '</div>' +
                    '<div class="eventDate__month">' + monthName.toUpperCase() + '</div>' +
                    '</div>' +
                    '<div class="event__title eventTitle">' +
                    '<h1>' + title + '</h1>' +
                    '<div class="eventTitle__details eventTitle__details--block eventTitle__details--location">' + location + ', ' + city + '</div>' +
                    '<div class="eventTitle__details eventTitle__details--block eventTitle__details--price">' + price + ' BGN</div>' +
                    '</div>' +
                    '<div class="event__dateTime">' +
                    day + ' ' + monthName + ' ' + '(' + dayName + ')' + //12 Dec (Tue)
                    '<br /> at ' + hours + ':' + minutes + //20:00
                    '</div>' +
                    '<div class="eventCTA clear">' +
                    '<span class="eventCTA__btn">' + eventButtonText + '</span>' +
                    '<span class="eventCTA__arrow"></span>' +
                    '</div>' +
                    '</a>' +
                    '</article>';

                $('#events-container').append(eventHtml);
            }
        }

        $('.event-details').on('click', function(e) {
                var target = e.currentTarget;
                var parent = $(target).parent();
                var conferenceId = $(parent).attr('id');
                sessionStorage.setItem('detailsConferenceId', conferenceId);
        });

        if (eventsList.length === 0) {
            $('#events-container').text('No events to show!');
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

    var articlesSize = $("#events-container article").size();
    var itemsToShow = 10;

    $('#events-container article:lt(' + itemsToShow + ')').show();

    if (itemsToShow < articlesSize) {
        loadMoreButton.addClass('active');
    } else {
        loadMoreButton.removeClass('active');
    }

    loadMoreButton.click(function() {
        itemsToShow = (itemsToShow + 10 <= articlesSize) ? itemsToShow + 10 : articlesSize;
        $('#events-container article:lt(' + itemsToShow + ')').show();

        if (itemsToShow == articlesSize) {
            loadMoreButton.removeClass('active');
        }
    });
}
