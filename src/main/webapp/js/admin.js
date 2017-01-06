$(document).ready(function() {
	isAuthUser();

	getAllEvents();
});

function isAuthUser() {
	$.getJSON('rest/user/current', function(response) {
		if (!response) {
			window.location.replace('index.html');
		}
	});
}

function getAllEvents() {
	$.getJSON('rest/admin/events', function(response) {
		var eventsData = response.event;

		var i, len = eventsData.length;

		if (len === 0) {
			$('#manage-events-table').text('There are no events!');
		}
		else {
			for (i = 0; i < len; i++) {
				var	id = eventsData[i].eventId,
					title = eventsData[i].title,
					date = eventsData[i].date.split(' ')[0],
					status = eventsData[i].status;

				var statusLabelClass;

				// TODO: Remove NEW and NOT_APPROVED when get all events by admin is fixed!
				switch(status) {
    				case 'NEW':
    					statusLabelClass = 'label-warning';
        				break;
    				case 'NOT_APPROVED':
        				statusLabelClass = 'label-danger';
        				break;
        			case 'PUBLISHED':
        				statusLabelClass = 'label-success';
        				break;
    				default:
        				statusLabelClass = 'label-default';
				}

				if (status == 'NOT_APPROVED') {
					status = 'NOT APPROVED';
				}

				status = toTitleCase(status);

				var eventHtml = '<tr id="' + id + '">'+
                    '<td class="managerTable__title">' +
                        '<a href="#">' + title + '</a>' +
                        '<span class="label ' + statusLabelClass + '">' + status + '</span>' +
                    '</td>' +
                    '<td class="managerTable__date">' + date + '</td>' +
                    '<td class="managerTable__CTA"></td>' +
                '</tr>';

                $('#manage-events-table').append(eventHtml);

                if (status == 'Pending') {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-success btn-xs accept">Accept</button> <button class="btn btn-danger btn-xs decline">Decline</button> ');
                }
                else {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-success btn-xs accept" disabled>Accept</button> <button class="btn btn-danger btn-xs decline" disabled>Decline</button> ');
                }
			}
		}
	}).done(function() {
		loadMore();

		$('.accept').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			acceptEvent(eventId);
		});
		
		$('.decline').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			declineEvent(eventId);
		});
	});
}

function acceptEvent(eventId) {
	$.ajax({
		type: 'POST',
		url: 'rest/admin/acceptEvent?eventId=' + eventId
	}).done(function() {
		alert('Successfully approved event for publishing!');
		window.location.reload();
	}).fail(function() {
		alert('Failed to approve event for publishing!');
	});
}

function declineEvent(eventId) {
	$.ajax({
		type: 'POST',
		url: 'rest/admin/declineEvent?eventId=' + eventId
	}).done(function() {
		alert('Successfully declined event for publishing!');
		window.location.reload();
	}).fail(function() {
		alert('Failed to decline event for publishing!');
	});
}

function loadMore() {
    var loadMoreButton = $('#load-more-button');

    var eventsSize = $("#manage-events-table tr").size();
    var itemsToShow = 8;

    $('#manage-events-table tr:lt(' + itemsToShow + ')').show();

    if (itemsToShow < eventsSize) {
        loadMoreButton.addClass('active');
    } else {
        loadMoreButton.removeClass('active');
    }

    loadMoreButton.click(function() {
        itemsToShow = (itemsToShow + 10 <= eventsSize) ? itemsToShow + 10 : eventsSize;
        $('#manage-events-table tr:lt(' + itemsToShow + ')').show();

        if (itemsToShow == eventsSize) {
            loadMoreButton.removeClass('active');
        }
    });
}

function toTitleCase(str) {
    return str.replace(/\w\S*/g, function(txt) {
    	return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}