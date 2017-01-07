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
	});
}

function loadLectureData() {
	var lectureId = sessionStorage.getItem('editLectureId');
	$.getJSON('rest/lectures/getByLectureId?lectureId=' + lectureId, function(response) {
		var lectureData = response.lecture;

		var title = lectureData.title,
			description = lectureData.description,
			lecturerName = lectureData.lecturerName,
			lecturerDescription = lectureData.lecturerDescription,
			lecturerEmail = lectureData.lecturerEmail;

		$('#lectureTopic').val(title);
		$('#lectureDescription').val(description);
		$('#lecturerName').val(lecturerName);
		$('#lecturerInfo').val(lecturerDescription);
		$('#lecturerEmail').val(lecturerEmail);
	});
}

function editLecture() {
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

	if (!validateLectureData(lectureData)) {
		toastr.error('Invalid data!');
        return;
    }

	$.ajax({
		type: 'POST',
		url: 'rest/lectures/edit',
		contentType: 'application/json',
		data: JSON.stringify(lectureData)
	}).done(function() {
		toastr.success('Successfully edited lecture!');
		setTimeout(function() { 
			window.location.reload();
		}, 1000);
	}).fail(function() {
		toastr.error('Invalid data! Cannot edit lecture!');
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