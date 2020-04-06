<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
<!-- head -->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Tristan Rosales">
    <link href="Resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="Resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="Resources/css/prettyPhoto.css" rel="stylesheet">
    <link href="Resources/css/animate.css" rel="stylesheet">
    <link href="Resources/css/main.css" rel="stylesheet">
    <link href="Resources/css/responsive.css" rel="stylesheet">
    
    <style>body{padding-top: 60px;}</style>
	
    <link href="Resources/bootstrap3/css/bootstrap.css" rel="stylesheet" />
 
    <link href="Resources/css/login-register.css" rel="stylesheet" />
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
	
    <link rel="stylesheet" href="Resources/Remodal/remodal.css">
    <link rel="stylesheet" href="Resources/Remodal/remodal-default-theme.css">
    <link href="Resources/css/formStyle.css" rel="stylesheet">
    
    <style>
	    .remodal-bg.with-red-theme.remodal-is-opening,
	    .remodal-bg.with-red-theme.remodal-is-opened {
	      filter: none;
	    }
	
	    .remodal-overlay.with-red-theme {
	      background-color: #f44336;
	    }
	
	    .remodal.with-red-theme {
	      background: #fff;
	    }
    </style>
	
    <script src="Resources/jquery/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="Resources/bootstrap3/js/bootstrap.js" type="text/javascript"></script>
    <script src="Resources/js/login-register-forgotPassword.js" type="text/javascript"></script>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src='https://www.google.com/recaptcha/api.js'></script>
    <script src="Resources/js/sweetalert.min.js"></script>
    <link rel="stylesheet" type="text/css" href="Resources/css/sweetalert.css">
    
    <title>SM Cinema Online Seat Reservation</title>
</head>

<!-- body -->
<body>
    <!-- auto detector of movie close date javascript -->
    <script>
        $(document).ready(function() {
            $.get('${pageContext.servletContext.contextPath}/DetectIfMovieReachTheCloseDate', {
            });
        });
    </script> 
     
    <!-- container -->
    <div class="container">
	<div class="modal fade login" id="loginModal">
            <div class="modal-dialog login animated">
                <!-- modal content -->
    		<div class="modal-content">
                    <!-- modal header -->
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Login with</h4>
                    </div>
                    
                    <!-- modal body -->
                    <div class="modal-body">  
                        <!-- login form -->
                        <div class="box">
                            <div class="content">         
                                <div class="error"></div>
                                
                                <div class="form loginBox">
                                    <!-- frmLoginUser -->
                                    <form id="frmLoginUser" action="${pageContext.request.contextPath}/LoginUser" method="post" accept-charset="UTF-8">
                                        <input name="txtEmailAddress" id="txtEmailAddress" class="form-control" type="email" placeholder="Email" required="required">
                                        <input name="txtPassword" class="form-control" type="password" placeholder="Password" required="required" maxlength="16">
                                        <input class="btn btn-default btn-login" type="submit" value="Login">
                                    </form>
                                        
                                    <!-- frmLoginUser javascript -->
                                    <script type="text/javascript">
                                        $(function() {
                                            $('#frmLoginUser').submit(function(event) {
                                              event.preventDefault();
                                              var formEl = $(this);

                                              $.ajax({
                                                type: 'POST',
                                                dataType : 'JSON',
                                                url: formEl.prop('action'),
                                                data: formEl.serialize(),
                                                //if received a response from the server
                                                success: function( data, textStatus, jqXHR) {
                                                    if(data.isLoginSuccess == "userSide"){
                                                        showSuccessMessageForUsers();
                                                    }else if(data.isLoginSuccess == "adminSide"){
                                                        showSuccessMessageForAdmin();
                                                    }else{
                                                        showIncorrectEmailOrPasswordMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                              });
                                            });
                                        });
                                        
                                        function showSuccessMessageForUsers() {
                                            swal(
                                                {
                                                    title: "Congratulations!",
                                                    text: "You may now booked your seat (s)!",
                                                    type: "success",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Continue",
                                                    closeOnConfirm: false 
                                                },
                                            function(){
                                                location.replace("${pageContext.request.contextPath}/pages/USER/home.jsp");
                                            });
                                        }
                                        
                                        function showSuccessMessageForAdmin() {
                                            swal(
                                                {
                                                    title: "Congratulations!",
                                                    text: "Welcome Home, " + $('#txtEmailAddress').val() + "!",
                                                    type: "success",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Continue",
                                                    closeOnConfirm: false 
                                                },
                                            function(){
                                                location.replace("${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp");
                                            });
                                        }
                                        
                                        function showIncorrectEmailOrPasswordMessage() {
                                            swal(
                                                {
                                                    title: "Oops! We're sorry!",
                                                    text: "Incorrect password or member not found. Please try again!",
                                                    type: "error",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Continue",
                                                    closeOnConfirm: true 
                                                }
                                            );
                                        }
                                    </script>
                                </div>
                            </div>
                        </div>
                        
                        <!-- register user form -->
                        <div class="box">
                            <div class="content registerBox" style="display:none;">
                                <div class="form">
                                    <form id="frmRegisterUser" method="post" action="${pageContext.request.contextPath}/AddUser">
                                        <input name="txtEmailAddress" class="form-control" type="email" placeholder="Email" required="required">
                                        <input name="txtPassword" id="txtPassword" class="form-control" type="password" placeholder="Password" required="required" maxlength="16" oncopy="return false" onpaste="return false">
                                        <!--<input id="password_confirmation" class="form-control" type="password" placeholder="Repeat Password" name="password_confirmation">-->
                                        <input name="txtFirstname" id="txtFirstname" class="form-control" type="text" placeholder="Firstname" required="required" maxlength="15" oncopy="return false" onpaste="return false">
                                        <input name="txtLastname" id="txtLastname" class="form-control" type="text" placeholder="Lastname" required="required" maxlength="20" oncopy="return false" onpaste="return false">
                                        Birthday:            
                                        <select name="txtBirthMonth" required="required">
                                            <option disabled selected>---BIRTH MONTH---</option>
                                            <option value="January">January</option>
                                            <option value="February">February</option>
                                            <option value="March">March</option>
                                            <option value="April">April</option>
                                            <option value="May">May</option>
                                            <option value="June">June</option>
                                            <option value="July">July</option>
                                            <option value="August">August</option>
                                            <option value="September">September</option>
                                            <option value="October">October</option>
                                            <option value="November">November</option>
                                            <option value="December">December</option>                   
                                        </select><br><br>
                                        
                                        <select name="txtBirthDay" required="required">
                                            <option disabled selected>---BIRTH DAY---</option>
                                            <script>
                                                for(var i = 1; i <= 31; i++){
                                                    document.write('<option value="'+i+'">'+i+'</option>');
                                                }
                                            </script>
                                        </select><br><br>
                                        
                                        <select name="txtBirthYear" required="required">
                                            <option disabled selected>---BIRTH YEAR---</option>
                                            <script>
                                                var myDate = new Date();
                                                var year = myDate.getFullYear();
                                                for(var i = 1950; i < year+1; i++){
                                                    document.write('<option value="'+i+'">'+i+'</option>');
                                                }
                                            </script>
                                        </select>

                                        <br><br>
                                        <div class="g-recaptcha" data-sitekey="6Ld4DCcUAAAAAJKf2jLziHN-2dm00nb8xCU7G9IO"></div>
                                        <br>
                                        By clicking the "Create Account" button, you acknowledge that you have read and agree to the
                                        <a href="pages/USER/termsAndConditions.jsp" target="_blank" style="color: #199ac1; text-decoration: none;">SM Cinema Terms & Conditions</a>.
                                        <br><br>
                                        
                                        <input class="btn btn-default btn-register" type="submit" value="Create account" name="commit">
                                    </form>
                                        
                                    <!-- frmRegisterUser javascript -->
                                    <script type="text/javascript">
                                        $('#txtFirstname').keypress(function (e) {
                                            var regex = new RegExp("^[a-zA-Z\s]+$");
                                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                                            if (regex.test(str)) {
                                                return true;
                                            }else{
                                                e.preventDefault();
                                                swal("Oops! We're sorry!", "Please enter letters only!", "error");
                                                return false;
                                            }
                                        });
                                                   
                                        $('#txtLastname').keypress(function (e) {
                                            var regex = new RegExp("^[a-zA-Z\s]+$");
                                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                                            if (regex.test(str)) {
                                                return true;
                                            }else{
                                                e.preventDefault();
                                                swal("Oops! We're sorry!", "Please enter letters only!", "error");
                                                return false;
                                            }
                                        });
                                        
                                        $('#txtPassword').keypress(function (e) {
                                            var regex = new RegExp("^[a-zA-Z0-9]+$");
                                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                                            if (regex.test(str)) {
                                                return true;
                                            }else{
                                                e.preventDefault();
                                                swal("Oops! We're sorry!", "Please enter letters and numbers only!", "error");
                                                return false;
                                            }
                                        });
                                        
                                        showAreYouSureYouWantToRegisterMessage();

                                        function showAreYouSureYouWantToRegisterMessage(){
                                            $('#frmRegisterUser').submit(function(event) {
                                                event.preventDefault();
                                                var formEl = $(this);

                                                swal(
                                                {
                                                    title: "Are you sure?",
                                                    text: "You will not be able to undo this action!",
                                                    type: "warning",
                                                    showCancelButton: true,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Yes, I'm Sure!",
                                                    cancelButtonText: "No, not sure yet!",
                                                    closeOnConfirm: false,
                                                    showLoaderOnConfirm: true
                                                },
                                                function(){
                                                    setTimeout(function(){
                                                        $.ajax({
                                                            type: 'POST',
                                                            dataType : 'JSON',
                                                            url: formEl.prop('action'),
                                                            data: formEl.serialize(),
                                                            //if received a response from the server
                                                            success: function( data, textStatus, jqXHR) {
                                                                if(data.isRegisterOfUserSuccess == "emailExists"){
                                                                    showEmailExistsForRegisterUserMessage();
                                                                }else if(data.isRegisterOfUserSuccess == "notEligible"){
                                                                    swal("Invalid", "You must be 18 years of age or older to enter this site.", "error");
                                                                }else if(data.isRegisterOfUserSuccess){
                                                                    showRegisterSuccessMessage();
                                                                }
                                                            },
                                                            error: function(jqXHR, textStatus, errorThrown){
                                                                alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                            }
                                                        });
                                                    }, 3000);               
                                                });      
                                            });
                                        }

                                        function showRegisterSuccessMessage() {
                                            swal(
                                                {
                                                    title: "Successful!",
                                                    text: "You've successfully created your account. Please login your account!",
                                                    type: "success",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Continue",
                                                    closeOnConfirm: false 
                                                },
                                            function(){
                                                location.replace("${pageContext.request.contextPath}");
                                            });
                                        }

                                        function showEmailExistsForRegisterUserMessage() {
                                            swal(
                                                {
                                                    title: "Invalid!",
                                                    text: "You've entered an existing email address. Please try another!",
                                                    type: "warning",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Close",
                                                    closeOnConfirm: true 
                                                });
                                        }
                                    </script>
                                </div>
                            </div>
                        </div>
                        
                        <!-- forgot password form -->
                        <div class="box">                            
                            <div class="content forgotPasswordBox" style="display:none;">
                                <div class="form">
                                    <form id="frmDetectIfEmailAddressIsExisting" method="post" action="${pageContext.request.contextPath}/DetectIfEmailAddressIsExisting">
                                        <input name="txtEmailAddress" id="ID_txtEmailAddress" class="form-control" type="email" placeholder="Email address" required="required">
                                        <!--<input name="txtNameOfYourFavoritePet" class="form-control" type="text" placeholder="Name of your favorite pet?" required="required">-->
                                        <input class="btn btn-default btn-checkEmail" type="submit" value="Check email address" name="commit">
                                    </form>
                                        
                                    <!-- frmDetectIfEmailAddressIsExisting javascript -->
                                    <script type="text/javascript">
                                        submitInformationAboutForgotPassword();

                                        function submitInformationAboutForgotPassword(){
                                            $('#frmDetectIfEmailAddressIsExisting').submit(function(event) {
                                                event.preventDefault();
                                                var formEl = $(this);
                                                
                                                $.ajax({
                                                    type: 'POST',
                                                    dataType : 'JSON',
                                                    url: formEl.prop('action'),
                                                    data: formEl.serialize(),
                                                    success: function( data, textStatus, jqXHR) {
                                                        if(data.isExisting){
                                                            showSuccessfulBecauseEmailExistsMessage();    
                                                        }else{
                                                            showFailedBecauseEmailDoesntExistsMessage();
                                                        }
                                                    },
                                                    error: function(jqXHR, textStatus, errorThrown){
                                                        alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                    }
                                                });
                                            });
                                        }

                                        function showFailedBecauseEmailDoesntExistsMessage() {
                                            swal(
                                                {
                                                    title: "Invalid!",
                                                    text: $('#ID_txtEmailAddress').val() + " does not exist in the database.\nPlease try again!",
                                                    type: "error",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Close",
                                                    closeOnConfirm: true
                                                }
                                            );
                                        }

                                        function showSuccessfulBecauseEmailExistsMessage() {
                                            swal(
                                                {
                                                    title: "Congratulations!",
                                                    text: $('#ID_txtEmailAddress').val() + " exists in the database.",
                                                    type: "success",
                                                    showCancelButton: false,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Continue",
                                                    closeOnConfirm: false 
                                                },
                                            function(){
                                                location.replace("${pageContext.request.contextPath}/index.jsp?user_emailAddress=" + $('#ID_txtEmailAddress').val() + "#forgotPassword");
                                            });
                                        }
                                    </script>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- modal footer -->
                    <div class="modal-footer">
                        <div class="forgot login-footer">
                            <span>Looking to 
                                <a href="javascript: showRegisterForm();">create an account</a>
                            ?</span>
                            
                            <br>
                            
                            <span> 
                                <a href="javascript: showForgotPasswordForm();">Forgot password</a>?
                            </span>
                        </div>
                        
                        <div class="forgot register-footer" style="display:none">
                            <span>Already have an account?</span>
                            <a href="javascript: showLoginForm();">Login</a>
                        </div>
                        
                        <div class="forgot forgotPassword-footer" style="display:none">
                            <span>Already have an account?</span>
                            <a href="javascript: showLoginForm();">Login</a>
                            
                            <br>
                            
                            <span>Looking to 
                                <a href="javascript: showRegisterForm();">create an account</a>
                            ?</span>
                        </div>
                        
                    </div>        
    		</div>
            </div>
        </div>
    </div>
    
    <!-- header -->
    <header id="header">
        <!--header_top-->
	<div class="header_top">
            <div class="container">
		<div class="row">
                    <div class="col-sm-6">
			<div class="contactinfo">
			    <ul class="nav nav-pills">
				<li><a href=""><i class="fa fa-phone"></i>  (+632) 8298816</a></li>
				<li><a href=""><i class="fa fa-envelope"></i>  tristanrosales0@gmail.com</a></li>
			    </ul>
			</div>
                    </div>
                    
                    <div class="col-sm-6">
			<div class="social-icons pull-right">
                            <ul class="nav navbar-nav">
				<li><a href="#"><i class="fa fa-facebook"></i></a></li>
				<li><a href="#"><i class="fa fa-twitter"></i></a></li>
				<li><a href="#"><i class="fa fa-linkedin"></i></a></li>
				<li><a href="#"><i class="fa fa-dribbble"></i></a></li>
				<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
			    </ul>
			</div>
                    </div>
		</div>
            </div>
	</div>
		
        <!--header-middle-->
	<div class="header-middle">
            <div class="container">
		<div class="row">
                    <div class="col-sm-4">
			<div class="logo pull-left">
                            <a href="${pageContext.request.contextPath}"><img src="Resources/images/home/logo.jpg" alt=""/></a>
			</div>
                    </div>
                    
                    <div class="col-sm-8">
			<div class="shop-menu pull-right">
                            <ul class="nav navbar-nav">			
                                <%
                                String user_emailAddress = null;
                                Cookie[] cookies = request.getCookies();
                                if(cookies !=null){
                                    for(Cookie cookie : cookies){
                                        if(cookie.getName().equals("user_emailAddress")){
                                            user_emailAddress = cookie.getValue();
                                        }
                                    }
                                }
                                
                                if(user_emailAddress == null){
                                    user_emailAddress = "Guest";
                                }else{
                                    response.sendRedirect(request.getContextPath() + "/pages/USER/home.jsp");
                                }
                                %>
                                
                                <li><a href=""><i class="fa fa-user"></i> Welcome <%=user_emailAddress %></a></li>
                                <li><a href="javascript:void(0)" onclick="openLoginModal();"><i class="fa fa-lock"></i> Login / Register</a></li>
                            </ul>
			</div>
                    </div>
		</div>
            </div>
	</div>
	
        <!--header-bottom-->
	<div class="header-bottom">
            <div class="container">
		<div class="row">
                    <div class="col-sm-9">
			<div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
                            </button>
			</div>
                    </div>
		</div>
            </div>
	</div>
    </header>

    <!-- reserveTickets -->   
    <div class="remodal" data-remodal-id="reserveTickets" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <hr width="100%" style="border: groove;">
            <h2 id="modal1Title" style="font-family: Arial Black; color: #199ac1;">
                Reserve Movie
            </h2>
            <hr width="100%" style="border: groove;">
                   
            <!-- frmReserveTicket -->
            <form id="frmReserveTicket" method="post" action="${pageContext.request.contextPath}/ReserveTicket">
                <script>
                    $(document).ready(function() {
                        var movieBranch = $("#txtSelectMovieBranch").val();
                            $.get('${pageContext.servletContext.contextPath}/GetMovieBranches?movieName=<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>', {
                                movieName : "<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>"
                            },
                            function(response) {
                                var select = $('#txtSelectMovieBranch');
                                $.each(response, function(index, value) {
                                    $('<option>').val(value).text(value).appendTo(select);
                                });
                            });

                        $('#txtSelectMovieBranch').change(function(event) {
                            var movieBranchName = $("#txtSelectMovieBranch").val();
                            $.get('${pageContext.servletContext.contextPath}/GetMovieAuditorium', {
                                movieName : "<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>",
                                movieBranchName : movieBranchName
                            }, 
                            function(response) {
                                var selectMovieAuditorium = $('#txtSelectMovieAuditorium');
                                $.each(response, function(index, value) {
                                    $('<option>').val(value).text(value).appendTo(selectMovieAuditorium);
                                });
                            });
                        });

                        $('#txtSelectMovieAuditorium').change(function(event) {
                            var movieBranchName = $("#txtSelectMovieBranch").val();
                            var movieAuditorium = $("#txtSelectMovieAuditorium").val();
                            $.get('${pageContext.servletContext.contextPath}/GetMovieSchedule', {
                                movieName : "<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>",
                                movieBranchName : movieBranchName,
                                movieAuditorium: movieAuditorium
                            }, 
                            function(response) {
                                var selectMovieSchedule = $('#txtSelectMovieSchedule');
                                $.each(response, function(index, value) {
                                    $('<option>').val(value).text(value).appendTo(selectMovieSchedule);
                                });
                            });
                        }); 

                        $('#txtSelectMovieAuditorium').change(function(event) {
                            var movieBranchName = $("#txtSelectMovieBranch").val();
                            var movieAuditorium = $("#txtSelectMovieAuditorium").val();
                            $.get('${pageContext.servletContext.contextPath}/GetMovieAvailableDay', {
                                movieName : "<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>",
                                movieBranchName : movieBranchName,
                                movieAuditorium: movieAuditorium
                            }, 
                            function(response) {
                                var selectMovieAvailableDay = $('#txtSelectMovieAvailableDay');
                                $.each(response, function(index, value) {
                                    $('<option>').val(value).text(value).appendTo(selectMovieAvailableDay);
                                });
                            });
                        });
                    });
                </script>

                <input type="hidden" name="movie_name" value="<%= (String)request.getSession().getAttribute("MOVIE_NAME") %> 
                                    (<%= (String)request.getSession().getAttribute("MOVIE_YEAR") %>)">
                <input type="hidden" name="movie_typeOfSeating" 
                       value="<%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>">

                <br>

                <select id="txtSelectMovieBranch" name="txtSelectMovieBranch" required="required">
                    <option disabled selected>---SELECT BRANCH---</option>
                </select>

                <select id="txtSelectMovieAuditorium" name="txtSelectMovieAuditorium" required="required">
                    <option disabled selected>---SELECT AUDITORIUM---</option>
                </select>

                <br><br>

                <select id="txtSelectMovieAvailableDay" name="txtSelectMovieAvailableDay" required="required">
                    <option disabled selected>---SELECT AVAILABLE DAY---</option>            
                </select>

                <select id="txtSelectMovieSchedule" name="txtSelectMovieSchedule" required="required">
                    <option disabled selected>---SELECT SCHEDULE---</option>            
                </select>

                <br><br>

                <select name="txtChooseSeatQuantity" required="required" required="required">
                    <option disabled selected>---CHOOSE SEAT QUANTITY---</option>
                    <script>
                        for(var i = 1; i <= 10; i++){
                            document.write('<option value="'+i+'">'+i+'</option>');
                        }
                    </script>
                </select>

                <br><br>
                
                <a href="${pageContext.request.contextPath}">
                    <button type="button" class="button">Back</button>
                </a>
                
                <button class="button" type="submit" id="btnSubmit"><i class="fa fa-shopping-cart"></i></button>
            </form>
            
            <!-- frmReserveTicket javascript -->
            <script type="text/javascript">
                var movie_typeOfSeating = "<%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>";

                if(movie_typeOfSeating == "Free Seating"){
                    $('#btnSubmit').text("Buy Tickets");
                }else if(movie_typeOfSeating == "Preferred Seating"){
                    $('#btnSubmit').text("Select Seat (s)");
                }
                
                submitInformationAboutReserveTicket();

                function submitInformationAboutReserveTicket(){
                    $('#frmReserveTicket').submit(function(event) {
                        event.preventDefault();

                        var formEl = $(this);

                        $.ajax({
                            type: 'POST',
                            dataType : 'JSON',
                            url: formEl.prop('action'),
                            data: formEl.serialize(),
                            //if received a response from the server
                            success: function( data, textStatus, jqXHR) {
                                if(!data.isUserLoggedIn){
                                    showLoginRequiredMessage();
                                }
                            },
                            error: function(jqXHR, textStatus, errorThrown){
                                alert('Error: ' + textStatus + ' - ' + errorThrown);
                            }
                        });  
                    });
                }
                
                function showLoginRequiredMessage() {
                    swal(
                        {
                            title: "Login Required!",
                            text: "Please login your account to reserve a seat. Thankyou!!",
                            type: "warning",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Continue",
                            closeOnConfirm: false 
                        },
                    function(){
                        location.replace("${pageContext.request.contextPath}");
                    });
                }
            </script>
            
            <br><br>
                 
            <!-- table for selected movie -->
            <div class="table-responsive cart_info">
                <table class="table table-condensed" style="font-family: 'Roboto', sans-serif;">                        
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <h2 style="color: #199ac1;">
                                <%= (String)request.getSession().getAttribute("MOVIE_NAME") %> 
                                (<%= (String)request.getSession().getAttribute("MOVIE_YEAR") %>)
                            </h2>
                        </td>                  
                    </tr>

                    <tr>
                        <td rowspan="9">
                            <img src="${pageContext.servletContext.contextPath}/RetrieveMoviePoster?movie_id=<%= (String)request.getSession().getAttribute("MOVIE_ID") %>" height="250px;" width="220px;">
                            <br>
                            <a href="<%= (String)request.getSession().getAttribute("MOVIE_TRAILERLINK") %>" target="_blank" style="text-decoration: none;">
                                <button style="height: 40px; width: 220px; border-radius: 5px; border: none; background-color: #199ac1; font-weight: bold; color: white;">
                                    Play Trailer
                                </button>
                            </a>
                        </td>

                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">MTRCB:</font>
                                <%= (String)request.getSession().getAttribute("MTRCB_RATING") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Director:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_DIRECTOR") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Released by:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_RELEASEDBY") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Cast:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_CAST") %>
                            </</h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                            <font style="color: #199ac1;">Genre:</font> 
                            <%= (String)request.getSession().getAttribute("MOVIE_GENRE") %>
                            </</h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Synopsis:</font><br><br>
                                <%= (String)request.getSession().getAttribute("MOVIE_SYNOPSIS") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Running Time:</font><br><br>
                                <%= (String)request.getSession().getAttribute("MOVIE_DURATION") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Price:</font> 
                                &#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>
                                <br>
                                <font style="color: #199ac1;">Type Of Seating:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Release Date:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_RELEASEDATE") %>
                                <br><br>
                                <font style="color: #199ac1;">Close Date:</font>
                                <%= (String)request.getSession().getAttribute("MOVIE_CLOSEDATE") %>
                                <br><br>
                            </h4>
                        </td>
                    </tr>
                </table>
            </div>
            
            <br>
        </div><br>  
    </div>
                            
    <!-- forgotPassword -->   
    <div class="remodal" data-remodal-id="forgotPassword" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <hr width="100%" style="border: groove;">
            <h2 id="modal1Title" style="font-family: Arial Black; color: white;">
                Forgot Password?
            </h2>
            <hr width="100%" style="border: groove;">
            
            <a href="${pageContext.request.contextPath}">
                <button type="button" class="button">Go Back</button>
            </a><br><br>
                
            <!-- frmCheckAndUpdatePassword -->
            <form id="frmCheckAndUpdatePassword" method="post" action="${pageContext.request.contextPath}/CheckAnswerForQuestionThenUpdatePassword?user_emailAddress=<%= (String)request.getSession().getAttribute("user_emailAddress") %>">
                <div class="table-responsive cart_info">
                    <table class="table table-condensed" border="1">
                        <tr>
                            <td colspan="2" style="text-align:left; color: white;">
                                <h4>
                                    <font style="color: black;">Hello,</font> <%= (String)request.getSession().getAttribute("user_fullname") %>
                                    <sub style="color: skyblue;"><%= (String)request.getSession().getAttribute("user_emailAddress") %></sub>
                                </h4>
                            </td>                  
                        </tr>
                        
                        <tr>
                            <td colspan="2" style="text-align: center;">
                                <h4><font style="color: white;">Secret Question:</font> What is the name of your favorite pet?</h4>
                                
                                <input type="text" name="txtNameOfFavoritePet" id="txtNameOfFavoritePet" placeholder="Name of your favorite pet" required="required" maxlength="15" oncopy="return false" onpaste="return false">
                                
                                <br><br>
                                
                                <input type="password" name="txtNewPassword" id="txtNewPassword" placeholder="Enter new password" required="required" maxlength="16" oncopy="return false" onpaste="return false"><br><br>
                                <button class="button" type="submit">Check and Update</button>
                            </td>                  
                        </tr>
                    </table>
                </div>
            </form>
                  
            <!-- frmCheckAndUpdatePassword javascript -->
            <script type="text/javascript">
                $('#txtNameOfFavoritePet').keypress(function (e) {
                    var regex = new RegExp("^[a-zA-Z\s]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter letters only!", "error");
                        return false;
                    }
                });
                
                $('#txtNewPassword').keypress(function (e) {
                    var regex = new RegExp("^[a-zA-Z0-9]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter letters and numbers only!", "error");
                        return false;
                    }
                });
                
                submitInformationAboutChangingPassword();

                function submitInformationAboutChangingPassword(){
                    $('#frmCheckAndUpdatePassword').submit(function(event) {
                        event.preventDefault();
                        var formEl = $(this);

                        swal(
                        {
                            title: "Are you sure?",
                            text: "You will not be able to recover your old password!",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Yes, I'm Sure!",
                            cancelButtonText: "No, not sure yet!",
                            closeOnConfirm: false 
                        },
                        function(){
                            $.ajax({
                                type: 'POST',
                                dataType : 'JSON',
                                url: formEl.prop('action'),
                                data: formEl.serialize(),
                                //if received a response from the server
                                success: function( data, textStatus, jqXHR) {
                                    if(data.isChangingPasswordSuccess){
                                        showSuccessUpdatePasswordMessage();
                                    }else{
                                        showErrorUpdatePasswordMessage();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            });  
                        });
                    });
                }
                
                function showSuccessUpdatePasswordMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "You've successfully changed your password! Please login your account!",
                            type: "success",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Continue",
                            closeOnConfirm: false 
                        },
                    function(){
                        location.replace("${pageContext.request.contextPath}");
                    });
                }

                function showErrorUpdatePasswordMessage() {
                    swal(
                        {
                            title: "Invalid!",
                            text: "Name of your favorite pet doesn't matched in the database. Please try again!",
                            type: "error",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Close",
                            closeOnConfirm: true 
                        });
                }
            </script>
            
        </div><br>  
    </div>
	
    <!-- section -->
    <section>
	<div class="container">
            <div class="row">
		<div class="col-sm-3">
                    <div class="left-sidebar">
                        <!--category-products-->
			<h2><font color="#199ac1">Category</font></h2>
                        
			<div class="panel-group category-products" id="accordian">
                            <!-- FREE SEATING -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordian" href="#freeSeatingMovies">
                                            <span class="badge pull-right"><i class="fa fa-plus"></i></span>
                                            Free Seating
                                        </a>
                                    </h4>
				</div>
                                
                                <script type="text/javascript">
                                    $.ajax({
                                        url: '${pageContext.servletContext.contextPath}/GetAllTheFreeSeatingMovies',
                                        dataType: 'JSON',
                                        success: function(data) {
                                            for (var i=0; i<data.FREESEATING_MOVIES.length; i++) {
                                                var row = $('<li><a style="color: #199ac1;" href="${pageContext.servletContext.contextPath}/ShowInfoOfSelectedMovie?movie_id=' + data.FREESEATING_MOVIES[i].movieID + '#reserveTickets">' + data.FREESEATING_MOVIES[i].movieName + "(" + data.FREESEATING_MOVIES[i].movieYear + ')</a></li>');
                                                $('#listOfFreeSeatingMovies').append(row);
                                            } 
                                        },
                                        error: function(jqXHR, textStatus, errorThrown){
                                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                                        }
                                    });
                                </script>
                                
				<div id="freeSeatingMovies" class="panel-collapse collapse">
                                    <div class="panel-body">
					<ul id="listOfFreeSeatingMovies">
                                            
					</ul>
                                    </div>
				</div>
                            </div>
                            
                            <!-- PREFERRED SEATING -->
                            <div class="panel panel-default">
				<div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordian" href="#preferredSeatingMovies">
                                            <span class="badge pull-right"><i class="fa fa-plus"></i></span>
                                            Preferred Seating
                                        </a>
                                    </h4>
				</div>
                                
                                <script type="text/javascript">
                                    $.ajax({
                                        url: '${pageContext.servletContext.contextPath}/GetAllThePreferredSeatingMovies',
                                        dataType: 'JSON',
                                        success: function(data) {
                                            for (var i=0; i<data.PREFERREDSEATING_MOVIES.length; i++) {
                                                var row = $('<li><a style="color: #199ac1;" href="${pageContext.servletContext.contextPath}/ShowInfoOfSelectedMovie?movie_id=' + data.PREFERREDSEATING_MOVIES[i].movieID + '#reserveTickets">' + data.PREFERREDSEATING_MOVIES[i].movieName + "(" + data.PREFERREDSEATING_MOVIES[i].movieYear + ')</a></li>');
                                                $('#listOfPreferredSeatingMovies').append(row);
                                            } 
                                        },
                                        error: function(jqXHR, textStatus, errorThrown){
                                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                                        }
                                    });
                                </script>
                                
                                <div id="preferredSeatingMovies" class="panel-collapse collapse">
                                    <div class="panel-body">
                                        <ul id="listOfPreferredSeatingMovies">
                                            
                                        </ul>
                                    </div>
                                </div>
                            </div>

			</div><!--/category-products-->
                    </div>
		</div>
				
		<div class="col-sm-9 padding-right">
                    <div class="features_items"><!--features_items-->
                        <h2 class="title text-center"><font color="#199ac1">Cinema Movie Schedules</font></h2> 
                        
                        <sql:setDataSource var="DB_SMCinema" driver="org.apache.derby.jdbc.ClientDriver"
                            url="jdbc:derby://localhost:1527/DB_SMCinema"
                            user="tristanRosales" password="javalover22"/>
			
                        <sql:query dataSource = "${DB_SMCinema}" var = "showAllAvailableMovies">
                            SELECT * FROM TRISTANROSALES.TBLMOVIECINEMA WHERE MOVIE_STATUS='Activated'
                        </sql:query>
                            
                        <c:forEach var="row" items="${showAllAvailableMovies.rows}">
                            <div class="col-sm-4">
                                <div class="product-image-wrapper">
                                    <div class="single-products">
					<div class="productinfo text-center">
                                            <img src="${pageContext.servletContext.contextPath}/RetrieveMoviePoster?movie_id=${row.movie_id}" height="250px;" width="220px;">
                                            <h4 style="color: #199ac1;"><c:out value = "${row.movie_name}"/></h4>
                                            <h5 style="color: red;">(<c:out value = "${row.movie_typeOfSeating}"/>)</h5> 
                                            <a href="${pageContext.servletContext.contextPath}/ShowInfoOfSelectedMovie?movie_id=${row.movie_id}"><button class="button"><i class="fa fa-star"></i> Reserve Tickets</button></a>
                                            <br><br>
                                        </div>
                                       
                                        <img src="Resources/images/home/new.png" class="new" alt="" />
                                    </div>
				</div>
                            </div>		           
         		</c:forEach>
                    </div><!--features_items-->
            </div>
            </div>
        </div>
    </section>
	
    <!-- footer -->
    <footer id="footer">
	<div class="footer-top">
            <div class="container">
		<div class="row">    
                    <div class="col-sm-2">
			<div class="companyinfo">
                            <h2 style="color: orange;"><span style="color: #199ac1;">SM</span> Cinema</h2>
                            <h5><b>"Happy to Serve."</b></h5>
			</div>
                    </div>
                    
                    <div class="col-sm-3">
                        <div class="address">
                            <img src="Resources/images/home/map.png" alt="" />
                            <p>Dr. Santos Ave. corner Carlos P. Garcia Avenue Extension, San Dionisio, Parañaque City, Philippines</p>
			</div>
                    </div>
		</div>
            </div>
	</div>
		
	<div class="footer-widget">
            <div class="container">
		<div class="row">
                    <div class="col-sm-2">
			<div class="single-widget">
                            <h2>Contact Us</h2>
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="#">tristanrosales0@gmail.com</a></li>
                            </ul>
			</div>
                    </div>
                    
                    <div class="col-sm-2">
			<div class="single-widget">
                            <h2>Policies</h2>
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="pages/USER/termsAndConditions.jsp" target="_blank">Terms and Conditions</a></li>
                            </ul>
			</div>
                    </div>		
		</div>
            </div>
	</div>
		
	<div class="footer-bottom">
            <div class="container">
                <div class="row">
                    <p class="pull-left">Copyright © 2017 SM Cinema. All rights reserved.</p>
                    <p class="pull-right">Designed by <span><a target="_blank" href="http://www.themeum.com">Themeum</a><br>Developed by: Tristan Rosales</span></p>
                </div>
            </div>
	</div>		
    </footer>
	
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="Resources/Remodal/remodal.min.js"><\/script>');</script>
    <script src="Resources/Remodal/remodal.js"></script>

    <!-- Events -->
    <script>
      $(document).on('opening', '.remodal', function () {
        console.log('opening');
      });

      $(document).on('opened', '.remodal', function () {
        console.log('opened');
      });

      $(document).on('closing', '.remodal', function (e) {
        console.log('closing' + (e.reason ? ', reason: ' + e.reason : ''));
      });

      $(document).on('closed', '.remodal', function (e) {
        console.log('closed' + (e.reason ? ', reason: ' + e.reason : ''));
      });

      $(document).on('confirmation', '.remodal', function () {
        console.log('confirmation');
      });

      $(document).on('cancellation', '.remodal', function () {
        console.log('cancellation');
      });

    //  Usage:
    //  $(function() {
    //
    //    // In this case the initialization function returns the already created instance
    //    var inst = $('[data-remodal-id=modal]').remodal();
    //
    //    inst.open();
    //    inst.close();
    //    inst.getState();
    //    inst.destroy();
    //  });

      //  The second way to initialize:
      $('[data-remodal-id=modal2]').remodal({
        modifier: 'with-red-theme'
      });
    </script>

    <script src="Resources/js/jquery.js"></script>
    <script src="Resources/js/bootstrap.min.js"></script>
    <script src="Resources/js/jquery.scrollUp.min.js"></script>
    <script src="Resources/js/jquery.prettyPhoto.js"></script>
    <script src="Resources/js/main.js"></script> 
</body>
</html>
