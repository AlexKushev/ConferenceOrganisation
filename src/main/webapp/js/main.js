 $(document).ready(function() {

     var navigation = '<div class="topline clear">' +
         '<div class="topline__left">' +
         '<nav id="desktop-menu" class="desktopMenu clear">' +
         '<a href="index.html">Home</a>' +
         '</nav>' +
         '<nav class="mobileMenu">' +
         '<a href="#" class="mobileMenu__btn"></a>' +
         '<div id="mobile-menu" class="mobileMenu__items">' +
         '<a href="index.html">Home</a>' +
         '</div>' +
         '</nav>' +
         '</div>' +
         '<div class="topline__right profileMenu">' +
         '<a id="account" href="#" class="profileMenu__btn profileMenu__btn--account" role="button" data-toggle="modal" data-target="#login-modal">Account</a>' +
         '<button id="logout-button" type="button" class="profileMenu__btn profileMenu__btn--logout">Logout</button>' +
         '</div>' +
         '</div>';

     $('.toplineWrap').append(navigation);

     $('.profileMenu__btn--logout').hide();
     $('#account').attr("data-toggle", "modal");
     $('#account').attr("data-target", "#login-modal");
     $('#account').text('Account');

     authUser();

     var registerButton = $('#register-button'),
         loginButton = $('#login-button'),
         logoutButton = $('#logout-button');

     registerButton.on('click', function() {
         register();
     });

     loginButton.on('click', function() {
         login();
     });

     logoutButton.on('click', function(e) {
         logout();
     });

     $(window).scroll(function() {
         if ($(this).scrollTop() > 350) {
             $('.scrollTop').show();
         } else {
             $('.scrollTop').hide();
         }
     });

     $('.scrollTop').click(function() {
         $("html, body").animate({
             scrollTop: 0
         }, 600);
         return false;
     });

 });

 function login() {
     var userEmail = $('#login_email').val(),
         userPassword = $('#login_password').val();


     var userData = {
         user: {
             email: userEmail,
             password: userPassword
         }
     };

     $.ajax({
         type: 'POST',
         url: 'rest/user/login',
         contentType: 'application/json',
         data: JSON.stringify(userData)
     }).done(function() {
         // destroy modal login 
         $('#login-modal').modal('toggle');

         // set current user
         authUser();

         alert("Successfully logged in!");
         location.reload();

     }).fail(function() {
         alert("Wrong e-mail or password!");
     }).always(function() {

     });
 }

 function register() {
     var userEmail = $('#register_email').val(),
         userPassword = $('#register_password').val(),
         userFirstName = $('#register_firstName').val(),
         userLastName = $('#register_lastName').val();

     var userData = {
         user: {
             email: userEmail,
             password: userPassword,
             firstName: userFirstName,
             lastName: userLastName
         }
     };

     if (!validateUserData(userEmail, userPassword, userFirstName, userLastName)) {
         alert('Invalid data!');
         return;
     }

     $.ajax({
         type: 'POST',
         url: 'rest/user/register',
         contentType: 'application/json',
         data: JSON.stringify(userData)
     }).done(function() {
         alert('Successfully registered!');
     }).fail(function() {
         alert('Invalid data or user with this e-mail already exists!');
     }).always(function() {
         // submit
     });
 }

 function validateUserData(userEmail, userPassword, userFirstName, userLastName) {
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

     if (!validateEmail(userEmail) || validateIfEmpty(userPassword) || !validateLength(userPassword, 5, 20) || validateIfEmpty(userFirstName) || !validateLength(userFirstName, 2, 20) || validateIfEmpty(userLastName) || !validateLength(userLastName, 2, 20)) {
         return false;
     }

     return true;
 }

 function authUser() {
     $.getJSON('rest/user/current', function(response) {
         if (!response) {
             $('.profileMenu__btn--logout').hide();
             $('#not-logged-in-header').addClass('active');
             return;
         } else {
             var currentUser = response.user;

             $('#filter-line-tabs').append('<a href="addevent.html">Add New</a>');
             var currentPage = document.location.pathname.match(/[^\/]+$/)[0];
             if (currentPage.indexOf('addevent') != -1) {
                 $('#filter-line-tabs a:last-child').addClass('active');
             }

             $('#logged-in-header').addClass('active');
             $('.profileMenu__btn--logout').show();
             $('#account').removeAttr("data-toggle");
             $('#account').removeAttr("data-target");
             $('#account').text(currentUser.firstName);

             $('#desktop-menu').append(' <a href="eventmanager.html">Event Manager</a>');
             $('#mobile-menu').append(' <a href="eventmanager.html">Event Manager</a>');

             // if user is admin add this -> <a href="adminpanel.html">Admin Panel</a>
         }
     });
 }

 function logout() {
     $.ajax({
         type: 'GET',
         url: 'rest/user/logout',
         statusCode: {
             204: function() {
                 alert("Successfully logged out!");
                 window.location.replace('index.html');
             }
         }
     });
 }

 // Mobile Menu Toggle
 $(document).on('click', '.mobileMenu__btn', function() {
     var menu = $('.mobileMenu__items');
     menu.toggleClass('active');
     $(this).toggleClass('active');
 });
