// TODO: Fix user authentication!

$(document).ready(function() {
	isAuthUser();

	loadLectureData();

	var editLectureButton = $('#edit-lecture-button');

	editLectureButton.on('click', function() {
		editLecture();
	});
});

function isAuthUser() {
	$.getJSON('rest/user/current', function(response) {
		if (!response) {
			window.location.replace('index.html');
		}
		else {
			var currentUser = response.user;
			
			$('.profileMenu__btn--logout').show();
            $('#account').removeAttr("data-toggle");
            $('#account').removeAttr("data-target");
            $('#account').text(currentUser.firstName);
		}
	});
}

function loadLectureData() {
	alert('Not implemented (Waiting for Alex to write the service)');
}

function editLectureData() {
	var lectureTitle = $('#lectureTopic').val(),
		lectureDescription = $('#lectureDescription').val(),
		lecturerName = $('#lecturerName').val(),
		lecturerDescription = $('#lecturerInfo').val(),
		lecturerEmail = $('#lecturerEmail').val();

	var lectureId = sessionStorage.getItem('editLectureId');

	var lectureData = {
		lecture: {
			lectureId: lectureId,
			title: lectureTitle,
			description: lectureDescription,
			lecturerName: lecturerName,
			lecturerDescription: lecturerDescription,
			lecturerEmail: lecturerEmail
		}
	};

	console.log(JSON.stringify(lectureData));

	if (!validateLectureData(lectureData)) {
        alert('Invalid data!');
        return;
    }

	$.ajax({
		type: 'POST',
		url: 'rest/lectures/edit',
		contentType: 'application/json',
		data: JSON.stringify(lectureData)
	}).done(function() {
		alert('Successfully edited lecture!')
		window.location.reload();
	}).fail(function() {
		alert('Invalid data! Cannot edit lecture!');
	});
}

function validateLectureData(lectureData) {
	var lecture = lectureData.lecture;

    function validateLength(str, min, max) {
        return str.length >= min && str.length <= max;
    }

    function validateIfEmpty(str) {
        if (str.trim() === null || str.trim() === '' || str.trim() === ' ') {
            return true;
        }

        return false;
    }

    function validateEmail(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }

    if (validateIfEmpty(lecture.title) || !validateLength(lecture.title, 3, 50) || validateIfEmpty(lecture.description) || !validateLength(lecture.description, 5, 5000) || validateIfEmpty(lecture.lecturerName) || !validateLength(lecture.lecturerName, 2, 35) || validateIfEmpty(lecture.lecturerDescription) || !validateLength(lecture.lecturerDescription, 5, 5000) || !validateEmail(lecture.lecturerEmail)) {
        return false;
    }

    return true;
 }