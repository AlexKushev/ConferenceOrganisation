$(document).ready(function() {

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
            } else if (currentPage.indexOf('pastevents') != -1) {
                dateCondition = date.getTime() < currentDate.getTime();
            }

            if (dateCondition) {
                eventsList.push(eventsData[i]);
                monthName = monthNames[date.getMonth()];
                dayName = days[date.getDay()];

                eventHtml = '<article class="event">' +
                    '<a href="event.html?id=' + id + '" class="clear">' +
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
                    '<span class="eventCTA__btn">Tickets</span>' +
                    '<span class="eventCTA__arrow"></span>' +
                    '</div>' +
                    '</a>' +
                    '</article>';

                $('#events-container').append(eventHtml);
            }
        }

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
