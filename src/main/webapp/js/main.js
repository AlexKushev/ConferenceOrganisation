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
         logoutButton = $('#logout-button'),
         resetPasswordButton = $('#reset_password_button');

     registerButton.on('click', function() {
         register();
     });

     loginButton.on('click', function() {
         login();
     });

     logoutButton.on('click', function(e) {
         logout();
     });
     
     resetPasswordButton.on('click', function() {
    	 resetPassword();
     });

     $('#login_email').keypress(function(e) {
        if (e.which == 13) {
            login();
        }
     });

     $('#login_password').keypress(function(e) {
        if(e.which == 13) {
            login();
        }
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
         data: JSON.stringify(userData),
         statusCode: {
            200: function() {
                // destroy modal login 
                $('#login-modal').modal('toggle');

                // set current user
                authUser();

                toastr.success('Successfully logged in!');
            },
            401: function() {
                toastr.error('Wrong e-mail or password!');
            }
         }
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
         toastr.error('Invalid data!');
         return;
     }

     $.ajax({
         type: 'POST',
         url: 'rest/user/register',
         contentType: 'application/json',
         data: JSON.stringify(userData)
     }).done(function() {
        $('#login-modal').modal('toggle');
        toastr.success('Successfully registered!');
     }).fail(function() {
        toastr.error('Invalid data or user with this e-mail already exists!');
     });
 }
 
 function resetPassword() {
	 var userEmail = $('#lost_email').val();
	 
	 $.ajax({
		type: 'POST',
		url: 'rest/user/resetPassword?email=' + userEmail
	 }).done(function() {
        $('#login-modal').modal('toggle');
		toastr.success('We send you e-mail with your new password!');
	 }).fail(function() {
		toastr.error('Failed to reset password! Invalid e-mail!');
	 })

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
            $('#account').attr('href', '#');
            return;
        }
        else {
            var currentUser = response.user;

            $('#filter-line-tabs').append('<a href="addevent.html">Add New</a>');
            var currentPage = document.location.pathname.match(/[^\/]+$/)[0];
            if (currentPage.indexOf('addevent') != -1) {
                $('#filter-line-tabs a:last-child').addClass('active');
            }

            $('#logged-in-header').addClass('active');
            $('#not-logged-in-header').removeClass('active');
            $('.profileMenu__btn--logout').show();

            $('#account').attr('href', 'userpanel.html');
            $('#account').removeAttr("data-toggle");
            $('#account').removeAttr("data-target");
            $('#account').text(currentUser.firstName);

            $('#desktop-menu').append(' <a href="eventmanager.html">Event Manager</a>');
            $('#mobile-menu').append(' <a href="eventmanager.html">Event Manager</a>');

            if (currentUser.isAdmin === 1) {
                $('#desktop-menu').append(' <a href="adminpanel.html">Admin Panel</a>');
                $('#mobile-menu').append(' <a href="adminpanel.html">Admin Panel</a>');
            }
        }       
    });
 }

function logout() {
    $.ajax({
        type: 'GET',
        url: 'rest/user/logout',
        statusCode: {
            204: function() {
            	toastr.success("Successfully logged out!");
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

 /* #####################################################################
    #
    #   Project       : Modal Login with jQuery Effects
    #   Author        : Rodrigo Amarante (rodrigockamarante)
    #   Version       : 1.0
    #   Created       : 07/29/2015
    #   Last Change   : 08/04/2015
    #
    ##################################################################### */

 $(function() {

     var $formLogin = $('#login-form');
     var $formLost = $('#lost-form');
     var $formRegister = $('#register-form');
     var $divForms = $('#div-forms');
     var $modalAnimateTime = 300;
     var $msgAnimateTime = 150;
     var $msgShowTime = 2000;

     // $("form").submit(function() {
     //     switch (this.id) {
     //         case "login-form":
     //             var $lg_email = $('#login_email').val();
     //             var $lg_password = $('#login_password').val();
     //             if ($lg_email == "ERROR") {
     //                 msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "Login error");
     //             } else {
     //                 msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Login OK");
     //             }
     //             return false;
     //         case "lost-form":
     //             var $ls_email = $('#lost_email').val();
     //             if ($ls_email == "ERROR") {
     //                 msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "error", "glyphicon-remove", "Send error");
     //             } else {
     //                 msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "success", "glyphicon-ok", "Send OK");
     //             }
     //             return false;
     //         case "register-form":
     //             var $rg_email = $('#register_email').val();
     //             var $rg_password = $('#register_password').val();
     //             if ($rg_email == "ERROR") {
     //                 msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "error", "glyphicon-remove", "Register error");
     //             } else {
     //                 msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "success", "glyphicon-ok", "Register OK");
     //             }
     //             return false;
     //         default:
     //             return false;
     //     }
     //     return false;
     // });

     $('#login_register_btn').click(function() { modalAnimate($formLogin, $formRegister); });
     $('#register_login_btn').click(function() { modalAnimate($formRegister, $formLogin); });
     $('#login_lost_btn').click(function() { modalAnimate($formLogin, $formLost); });
     $('#lost_login_btn').click(function() { modalAnimate($formLost, $formLogin); });
     $('#lost_register_btn').click(function() { modalAnimate($formLost, $formRegister); });
     $('#register_lost_btn').click(function() { modalAnimate($formRegister, $formLost); });

     function modalAnimate($oldForm, $newForm) {
         var $oldH = $oldForm.height();
         var $newH = $newForm.height();
         $divForms.css("height", $oldH);
         $oldForm.fadeToggle($modalAnimateTime, function() {
             $divForms.animate({ height: $newH }, $modalAnimateTime, function() {
                 $newForm.fadeToggle($modalAnimateTime);
             });
         });
     }

     function msgFade($msgId, $msgText) {
         $msgId.fadeOut($msgAnimateTime, function() {
             $(this).text($msgText).fadeIn($msgAnimateTime);
         });
     }

     function msgChange($divTag, $iconTag, $textTag, $divClass, $iconClass, $msgText) {
         var $msgOld = $divTag.text();
         msgFade($textTag, $msgText);
         $divTag.addClass($divClass);
         $iconTag.removeClass("glyphicon-chevron-right");
         $iconTag.addClass($iconClass + " " + $divClass);
         setTimeout(function() {
             msgFade($textTag, $msgOld);
             $divTag.removeClass($divClass);
             $iconTag.addClass("glyphicon-chevron-right");
             $iconTag.removeClass($iconClass + " " + $divClass);
         }, $msgShowTime);
     }
 });
