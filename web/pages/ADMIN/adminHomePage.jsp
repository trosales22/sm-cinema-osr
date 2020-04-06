<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
<!-- head -->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome, <%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %>! (<%= (String)request.getSession().getAttribute("COOKIE_USER_TYPE") %>)</title>      
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <link href="../../Resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../Resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="../../Resources/css/prettyPhoto.css" rel="stylesheet">
    <link href="../../Resources/css/animate.css" rel="stylesheet">
    <link href="../../Resources/css/main.css" rel="stylesheet">
    <link href="../../Resources/css/responsive.css" rel="stylesheet">
    <link href="../../Resources/css/formStyle.css" rel="stylesheet">
    
    <style>body{padding-top: 60px;}</style>
    <!--
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
    -->
    <link rel="stylesheet" href="../../Resources/Remodal/remodal.css">
    <link rel="stylesheet" href="../../Resources/Remodal/remodal-default-theme.css">
    <link href="../../Resources/css/responsiveTabs.css" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    
    
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    
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
    
    <!--
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    -->
    <script src="../../Resources/js/sweetalert.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
</head>

<!-- body -->
<body>
    <!-- detector if user forfeits reservation -->
    <script>
        $(document).ready(function() {
            $.get('${pageContext.servletContext.contextPath}/DetectIfUserForfeitsReservation', {
            });
        });
    </script>
    
    <!--header-->
    <header id="header">
	<div class="header_top"><!--header_top-->
            <div class="container">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="contactinfo">
                            <ul class="nav nav-pills">
				<li><a href=""><i class="fa fa-phone"></i> (+632) 8298816</a></li>
				<li><a href=""><i class="fa fa-envelope"></i> tristanrosales0@gmail.com</a></li>
                            </ul>
			</div>
                    </div>
                    
                    <div class="col-sm-6">
			<div class="social-icons pull-right">
                            <ul class="nav navbar-nav">
				<li><a href=""><i class="fa fa-facebook"></i></a></li>
				<li><a href=""><i class="fa fa-twitter"></i></a></li>
				<li><a href=""><i class="fa fa-linkedin"></i></a></li>
				<li><a href=""><i class="fa fa-dribbble"></i></a></li>
				<li><a href=""><i class="fa fa-google-plus"></i></a></li>
                            </ul>
                        </div>
                    </div>
		</div>
            </div>
        </div><!--/header_top-->
		
	<div class="header-middle"><!--header-middle-->
            <div class="container">
		<div class="row">
                    <div class="col-sm-4">
			<div class="logo pull-left">
                            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp"><img src="../../Resources/images/home/logo.jpg"/></a>
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
                                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                                    }
                                %>
                                
                                <li><a href="#"><i class="fa fa-user"></i> Welcome, <%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %>! <b>(<%= (String)request.getSession().getAttribute("COOKIE_USER_TYPE") %>)</b></a></li>
                                <li><a href="${pageContext.request.contextPath}/pages/ADMIN/adminMyAccount.jsp?user_emailAddress=<%=user_emailAddress %>"><i class="fa fa-user"></i> My Account</a></li>
                                <li>
                                    <a id="btnLogoutAdmin" href="${pageContext.request.contextPath}/LogoutUser"><i class="fa fa-lock"></i> Logout</a>
                                    <!-- btnLogoutAdmin javascript -->
                                    <script type="text/javascript">
                                        $('#btnLogoutAdmin').click(function (event){ 
                                            event.preventDefault(); 
                                             
                                            swal(
                                                {
                                                    title: "Are you sure you want to logout?",
                                                    text: "You will not be able to undo this action!",
                                                    type: "warning",
                                                    showCancelButton: true,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Yes, I'm Sure!",
                                                    cancelButtonText: "No, not sure yet!",
                                                    closeOnConfirm: false
                                                },
                                                function(){
                                                    $.ajax({
                                                        url: $(this).attr('href'),
                                                        success: function(){
                                                            location.replace("${pageContext.request.contextPath}/LogoutUser");
                                                        },
                                                        error: function(jqXHR, textStatus, errorThrown){
                                                            alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                        }
                                                    });
                                                });
                                        });
                                    </script>                    
                                </li>
                            </ul>
			</div>
                    </div>
                </div>
            </div>
        </div><!--/header-middle-->
		
	<div class="header-bottom"><!--header-bottom-->
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
                        
			<div class="mainmenu pull-left">
                            <ul class="nav navbar-nav collapse navbar-collapse">
                                <li class="dropdown" id="menuOrDropdown"><a href="#">Add<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="#addAccountUser">Account User</a></li>
					<li><a href="#addMovie">Movie</a></li>
                                    </ul>
                                </li>
                                
                                <!-- detector if user is an IT Administrator -->
                                <script type="text/javascript">
                                    var userType = "<%= (String)request.getSession().getAttribute("COOKIE_USER_TYPE") %>";
                                    var menuOrDropdown = $('#menuOrDropdown');
                                    
                                    if(userType != "IT Administrator"){
                                        menuOrDropdown.css("display", "none");
                                    }
                                </script>
                            </ul>
			</div>
                    </div>
		</div>
            </div>
	</div><!--/header-bottom-->
    </header>
    
    <!--showMovieRating-->
    <div class="remodal" data-remodal-id="showMovieRating" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>		    
            <hr width="100%" style="border: groove;">
            <h3 id="modal1Title" style="font-family: Arial Black; color: orange;">
                Movie and Television Review and Classification Board (MTRCB)
            </h3>
            <hr width="100%" style="border: groove;">
                
            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp#addMovie" style="color: white;"><button class="button">Back</button></a>
            
            <br><br>
                
            <div class="table-responsive cart_info">
                <table class="table table-condensed" style="font-family: 'Roboto', sans-serif; text-align: center;">
                    <thead>
                        <tr class="cart_menu">
                            <td style="color: white;"><b>Rating</b></td>
                            <td style="color: white;"><b>Description</b></td>
                        </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td style="background-color: green;"><p style="color: white; font-weight: bold; font-family: Arial Black;">G</p></td>
                            <td style="text-align: left; color: white;"><p>Viewers of all ages are admitted.</p></td>
                        </tr>

                        <tr>
                            <td style="background-color: blue;"><p style="color: white; font-weight: bold; font-family: Arial Black;">PG</p></td>
                            <td style="text-align: left; color: white;"><p>Viewers below 13 years old must be accompanied by a parent or supervising adult.</p></td>
                        </tr>

                        <tr>
                            <td style="background-color: yellow;"><p style="color: white; font-weight: bold; font-family: Arial Black;">R-13</p></td>
                            <td style="text-align: left; color: white;"><p>Only viewers who are 13 years old and above can be admitted.</p></td>
                        </tr>

                        <tr>
                            <td style="background-color: orange;"><p style="color: white; font-weight: bold; font-family: Arial Black;">R-16</p></td>
                            <td style="text-align: left; color: white;"><p>Only viewers who are 16 years old and above can be admitted.</p></td>
                        </tr>

                        <tr>
                            <td style="background-color: red;"><p style="color: white; font-weight: bold; font-family: Arial Black;">R-18</p></td>
                            <td style="text-align: left; color: white;"><p>Only viewers who are 18 years old and above can be admitted.</p></td>
                        </tr>

                        <tr>
                            <td style="background-color: black;"><p style="color: white; font-weight: bold; font-family: Arial Black;">X</p></td>
                            <td style="text-align: left; color: white;"><p>"X-rated" films are not suitable for public exhibition.</p></td>
                        </tr>

                        <tr>
                            <td><p style="color: white;">Link:</p></td>
                            <td style="text-align: left;">
                                <p>
                                <a style="color: #199ac1; text-decoration: none;" target="_blank" 
                                    href="https://en.m.wikipedia.org/wiki/Movie_and_Television_Review_and_Classification_Board">
                                    https://en.m.wikipedia.org/wiki/Movie_and_Television_Review_and_Classification_Board
                                </a>
                                </p>
                            </td>
                        </tr>
                    </tbody>
                </table> 
            </div>
                
            <button data-remodal-action="cancel" class="button">Cancel</button>
            <button data-remodal-action="confirm" class="button">OK</button>
        </div><br>
    </div>
                    
    <!--addAccountUser-->   
    <div class="remodal" data-remodal-id="addAccountUser" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <hr width="100%" style="border: groove;">
            <h3 id="modal1Title" style="font-family: Arial Black; color: orange;">
                Add Account User
            </h3>
            <hr width="100%" style="border: groove;">
		    
            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp" style="color: white;"><button class="button">Back</button></a>
            
            <br><br>
            
            <!-- frmAddUserOrAdmin -->
            <form id="frmAddUserOrAdmin" method="post" action="${pageContext.request.contextPath}/AddAdministrator">
                <input type="text" name="txtFirstname" id="txtFirstname" placeholder="Firstname" required="required" maxlength="15" oncopy="return false" onpaste="return false">
                <input type="text" name="txtLastname" id="txtLastname" placeholder="Lastname" required="required" maxlength="15" oncopy="return false" onpaste="return false">
  				
                <br><br>
  				
                <input type="email" name="txtEmailAddress" placeholder="Email address" required="required">
				
                <input type="password" name="txtPassword" id="txtPassword" placeholder="Password" required="required" maxlength="16" oncopy="return false" onpaste="return false">
                
                <br><br>
                
                <select id="txtAccountType" name="txtAccountType" required="required">
                    <option disabled selected>---SELECT ACCOUNT TYPE---</option>
                    <option value="IT Administrator">IT Administrator</option>
                    <option value="Operations Supervisor">Operations Supervisor</option>
                    <option value="Ticket Seller">Ticket Seller</option>
                </select>
                
                <br><br>
                
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
                </select>

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
                
                <button type="reset" class="button">Reset</button>
                <button type="submit" class="button">Save</button>
            </form>
                
            <!-- frmAddUserOrAdmin javascript -->
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
                                        
                showAreYouSureYouWantToAddUserOrAdminMessage();
                
                function showAreYouSureYouWantToAddUserOrAdminMessage() {
                    $("#frmAddUserOrAdmin").submit(function(event){ 
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
                                    if(data.isAddingOfUserOrAdminSuccess == "accessDenied"){
                                        showAccessDeniedForAddingUserAndAdminMessage();
                                    }else if(data.isAddingOfUserOrAdminSuccess == "emailExists"){
                                        showEmailExistsForAddingUserAndAdminMessage();
                                    }else if(data.isAddingOfUserOrAdminSuccess){
                                        showSuccessAddingUserOrAdminMessage();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            });
                        });
                    });  
                }
                
                function showSuccessAddingUserOrAdminMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "You've successfully added a user!",
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
                
                function showAccessDeniedForAddingUserAndAdminMessage() {
                    swal(
                        {
                            title: "Invalid!",
                            text: "You do not have access to this option!",
                            type: "error",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Close",
                            closeOnConfirm: true 
                        }
                    );
                        
                }
            
                function showEmailExistsForAddingUserAndAdminMessage() {
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
        </div><br>  
    </div>
    
    <!--addMovie-->
    <div class="remodal" data-remodal-id="addMovie" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <hr width="100%" style="border: groove;">
                
            <h3 id="modal1Title" style="font-family: Arial Black; color: orange;">ADD MOVIE</h3>
            <sub>
                <a href="#showMovieRating" style="font-size: 15px; color: white;">
                    Movie and Television Review and Classification Board (MTRCB)
                </a>
            </sub>
                
            <hr width="100%" style="border: groove;">
                    
            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp" style="color: white;"><button class="button">Back</button></a>
            
            <br><br>
                
            <!-- frmAddMovie -->
            <form id="frmAddMovie" method="post" action="${pageContext.request.contextPath}/AddMovie" enctype="multipart/form-data" html="{:multipart=>true}" data-remote="true" accept-charset="UTF-8">    	
		<h4 style="color: white;">Choose movie poster (220 x 250)</h4>
		    	
                <input type="file" name="moviePoster" required="required" onchange="readURL(this);" accept="image/*" max-file-size=16177215>
		    	
                <br><br>
		    	
		<img id="moviePoster" src="#" alt="Movie poster (220 x 250)" style="color: white;"/>
		    	
		<script>
		    function readURL(input) {
		        if (input.files && input.files[0]) {
		            var reader = new FileReader();

		            reader.onload = function (e) {
		                $('#moviePoster')
		                .attr('src', e.target.result)
		                .width(220)
		                .height(250);
		            };

		            reader.readAsDataURL(input.files[0]);
		        }
		    }
		</script>
		    	
		<br><br>
                
                <input type="text" name="txtMovieReference" id="txtMovieReference" placeholder="Movie reference (ex. CDBF)" required="required" maxlength="5" oncopy="return false" onpaste="return false">
                
                <br><br>
                
                <input type="text" name="txtMovieName" id="txtMovieName" placeholder="Movie name" required="required" maxlength="50" oncopy="return false" onpaste="return false">
  				
                <select name="txtMovieYear" required="required">
                    <option disabled selected>---SELECT MOVIE YEAR---</option>
                    <script>
                        var myDate = new Date();
                        var year = myDate.getFullYear();
                        for(var i = 1900; i < year+1; i++){
                            document.write('<option value="'+i+'">'+i+'</option>');
                        }
                    </script>
                </select>
				
                <br><br>
                    
                <input type="text" name="txtMovieDirector" id="txtMovieDirector" placeholder="Movie Director" required="required" maxlength="50" oncopy="return false" onpaste="return false">
                <input type="text" name="txtMovieReleasedBy" id="txtMovieReleasedBy" placeholder="Released by" required="required" maxlength="50" oncopy="return false" onpaste="return false">
                    
                <br><br>
                
                <input type="text" name="txtMovieTrailerLink" placeholder="Movie trailer link" required="required">
                
                <br><br>
						
                <h4 style="color: white;">Movie Duration</h4>
                
                <p style="font-size: 16px; color: white;">Hour (s): 
                    <input type="number" name="txtMovieDuration_hours" id="txtMovieDuration_hours" min="1" max="5" maxlength="5" required="required" style="color: black;" oncopy="return false" onpaste="return false">
                    <br>
                    Minute (s): <input type="number" name="txtMovieDuration_minutes" id="txtMovieDuration_minutes" min="1" max="59" required="required" style="color: black;" oncopy="return false" onpaste="return false">
                </p>
                
                <select name="txtMoviePrice" required="required">
                    <option disabled selected>---SELECT MOVIE PRICE---</option>
                    <option value="50.00">&#8369;50.00</option>
                    <option value="100.00">&#8369;100.00</option>
                    <option value="150.00">&#8369;150.00</option>
                    <option value="180.00">&#8369;180.00</option>
                    <option value="200.00">&#8369;200.00</option>
                    <option value="220.00">&#8369;220.00</option>
                    <option value="240.00">&#8369;240.00</option>
                </select>
                    
                <select name="txtMovieTypeOfSeating" required="required">
                    <option disabled selected>---SELECT TYPE OF SEATING---</option>
                    <option value="Free Seating">Free Seating</option>
                    <option value="Preferred Seating">Preferred Seating</option>
                </select>
                    
                <br><br>
  				
                <textarea name="txtMovieCast" placeholder="Movie cast" required="required"></textarea>
  				
                <br><br>
  				
                <textarea name="txtMovieSynopsis" placeholder="Movie Synopsis" required="required"></textarea>
  				
                <br><br>
  				
                <select name="txtMTRCB_Rating" required="required">
                    <option disabled selected>---SELECT MTRCB RATING---</option>
                    <option value="G">G</option>
                    <option value="PG">PG</option>
                    <option value="R-13">R-13</option>
                    <option value="R-16">R-16</option>
                    <option value="R-18">R-18</option>
                    <option value="X">X</option>
                </select>
				
                <select name="txtMovieGenre" required="required">
                    <option disabled selected>---SELECT MOVIE GENRE---</option>
                    <option value="Action">Action</option>
                    <option value="Adventure">Adventure</option>
                    <option value="Animation">Animation</option>
                    <option value="Comedy">Comedy</option>
                    <option value="Crime and Gangster">Crime and Gangster</option>
                    <option value="Drama">Drama</option>
                    <option value="Epics/Historical">Epics/Historical</option>
                    <option value="Horror">Horror</option>
                    <option value="Musicals (Dance)">Musicals (Dance)</option>
                    <option value="Science Fiction">Science Fiction</option>
                    <option value="War (Anti-War)">War (Anti-War)</option>
                    <option value="Westerns">Westerns</option>
                </select>
				
                <br><br>

                <script type="text/javascript" src="../../Resources/js/dynamicAddingAndRemovingSchedule.js"></script>
                <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
                <script src="https://code.jquery.com/ui/1.11.0/jquery-ui.js" 
                    integrity="sha256-z8sq+fwXy6xX1HLBJZ5doyrWmFBhQ9lG3p/AKoipKKs="
                    crossorigin="anonymous">            
                </script>
                

                <script>
                $(document).ready(function() {
                        $.get('${pageContext.servletContext.contextPath}/GetAllMovieBranches',
                        function(response) {
                            var select = $('#txtMovieBranches');
                            $.each(response, function(index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                        });
                        
                        $.get('${pageContext.servletContext.contextPath}/GetAllMovieAuditorium',
                        function(response) {
                            var select = $('#txtMovieAuditorium');
                            $.each(response, function(index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                        });
                });
                </script>
                
                
                <select name="txtMovieAuditorium" id="txtMovieAuditorium" required="required">
                    <option disabled selected>---SELECT MOVIE AUDITORIUM---</option>
                </select>
                
                <br><br>
                
                <div id="CinemaGroup">
                    <div id="Schedule1">
                        <label style="color: white;">Schedule #1 : </label><input type="time" name="schedule" id="schedule1" value="">
                    </div>
                </div>
                
                <br><br> 
                
                <input type="text" name="txtMovieSchedules" id="txtMovieSchedules" readonly="readonly">
                
                <br><br>
                
                <input type="button" class="button" value="Add schedule" id="addScheduleBtn">
                <input type="button" class="button" value="Remove schedule" id="removeScheduleBtn">
                <input type="button" class="button" value="Show all schedule" id="showAllScheduleBtn">

                <br><br><br><br>
                
                <select name="txtMovieBranches" id="txtMovieBranches" required="required">
                    <option disabled selected>---SELECT BRANCHES---</option>
                </select>
				
                <br><br><br>
                
                <p style="font-size: 16px; color: white;">MOVIE RELEASE DATE: <input type="date" name="txtMovieReleaseDate" required="required" style="color: black;"></p>
                
                <p style="font-size: 16px; color: white;">MOVIE CLOSE DATE: <input type="date" name="txtMovieCloseDate" required="required" style="color: black;"></p>
                
                <br><br>
                
                <div style="width: auto; background-color: white; border: solid;">
                    <p style="font-size: 16px; font-family: Arial Black; color: red;">
                        Please make sure that all information entered are correct and final. 
                        You will not be able to edit this. Thank you!
                    </p>
                </div><br><br>
                
                <button class="button" type="reset">Reset</button>
                <button class="button" type="submit">Add</button>
            </form>
                    
            <!-- frmAddMovie javascript -->
            <script type="text/javascript">
                $('#txtMovieReference').keypress(function (e) {
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
                
                $('#txtMovieName').keypress(function (e) {
                    var regex = new RegExp("^[a-zA-Z0-9 ]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter letters and numbers only!", "error");
                        return false;
                    }
                });
                
                $('#txtMovieDirector').keypress(function (e) {
                    var regex = new RegExp("^[a-zA-Z ]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter letters only!", "error");
                        return false;
                    }
                });
                
                $('#txtMovieReleasedBy').keypress(function (e) {
                    var regex = new RegExp("^[a-zA-Z ]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter letters only!", "error");
                        return false;
                    }
                });
                
                $('#txtMovieDuration_hours').keypress(function (e) {
                    var regex = new RegExp("^[0-9]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter numbers only!", "error");
                        return false;
                    }
                });
                
                $('#txtMovieDuration_minutes').keypress(function (e) {
                    var regex = new RegExp("^[0-9]+$");
                    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                    if (regex.test(str)) {
                        return true;
                    }else{
                        e.preventDefault();
                        swal("Oops! We're sorry!", "Please enter numbers only!", "error");
                        return false;
                    }
                });

                showAreYouSureYouWantToAddMovieMessage();

                function showAreYouSureYouWantToAddMovieMessage(){
                    $('#frmAddMovie').submit(function(event) {
                        event.preventDefault();
                        var formEl = $(this)[0];
                        var data = new FormData(formEl);
                        
                        swal(
                        {
                            title: "Are you sure?",
                            text: "Please make sure that all information entered are correct and final. \n\
                            You will not be able to edit this. Thank you!",
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
                                processData: false,
                                contentType: false,
                                cache: false,
                                enctype: 'multipart/form-data',
                                //url: formEl.prop('action'),
                                //data: formEl.serialize(),
                                url: '${pageContext.request.contextPath}/AddMovie',
                                data: data,
                                //if received a response from the server
                                success: function( data, textStatus, jqXHR) {
                                    if(data.isAddingMovieSuccess == "accessDenied"){
                                        showAccessDeniedForAddingMovieMessage();
                                    }else if(data.isAddingMovieSuccess){
                                        showSuccessAddingMovieMessage();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            }); 
                        });
                    });
                }
                
                function showSuccessAddingMovieMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "You've successfully added a movie!",
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
                
                function showAccessDeniedForAddingMovieMessage() {
                    swal(
                        {
                            title: "Invalid!",
                            text: "You do not have access to this option!",
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
           
    <!-- updateStatusOfUserInReservation -->   
    <div class="remodal" data-remodal-id="updateStatusOfUserInReservation" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <hr width="100%" style="border: groove;">
            <h3 id="modal1Title" style="font-family: Arial Black; color: #199ac1;">
                Update Status
            </h3>
            <hr width="100%" style="border: groove;">
                
            <div style="float: center;">
                <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                    <button type="button" class="button">Back</button>
                </a>
            </div>
                    
            <br><br><br><br>
		    
            <!-- frmUpdateStatusOfUserInReservation -->
            <form id="frmUpdateStatusOfUserInReservation" method="post" action="${pageContext.request.contextPath}/UpdateStatusOfUserInReservation">                
                <div class="table-responsive cart_info">
                    <table class="table table-condensed" style="text-align: center;">
                        <thead>
                            <tr style="background-color: #199ac1; color: white; font-size: 18px;">
                                <td>Reference Number</td>
                            </tr>
                            
                            <tr style="background-color: orange; color: white; font-size: 18px;">
                                <td><%= (String)request.getSession().getAttribute("referenceNumber") %></td>
                            </tr>
                        </thead>
                    </table>
                </div>
                
                <select name="txtStatusOption" required="required">
                    <option disabled selected>---CHOOSE STATUS---</option>
                    <option value="Paid">Paid</option>
                    <option value="Cancelled">Cancel</option>
                </select>
                
                <br><br>
                
                <button type="submit" class="button">Update</button>
            </form>
                            
            <!-- frmUpdateStatusOfUserInReservation javascript -->
            <script type="text/javascript">
                showAreYouSureYouWantToUpdateStatusOfUserInReservationMessage();

                function showAreYouSureYouWantToUpdateStatusOfUserInReservationMessage(){
                    $('#frmUpdateStatusOfUserInReservation').submit(function(event) {
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
                                    if(data.isUpdatingStatusOfUserInReservationSuccess == "accessDenied"){
                                        showAccessDeniedForUpdatingStatusOfUserInReservationMessage();
                                    }else if(data.isUpdatingStatusOfUserInReservationSuccess){
                                        showUpdateStatusOfUserInReservationSuccessMessage();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            });  
                        });
                    });
                }
                
                function showUpdateStatusOfUserInReservationSuccessMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "Successfully updated users' reservation status!",
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
                
                function showAccessDeniedForUpdatingStatusOfUserInReservationMessage() {
                    swal(
                        {
                            title: "Invalid!",
                            text: "You do not have access to this option!",
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
    
    <!-- showMovieDetails -->
    <div class="remodal" data-remodal-id="showMovieDetails" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <div class="table-responsive cart_info">
                <table class="table table-condensed">   
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                                <button type="button" class="btn btn-danger">Back</button>
                            </a>
                        </td>
                        
                        <td>
                            <!-- frmDeleteSelectedMovie -->
                            <form id="frmDeleteSelectedMovie" method="POST" action="${pageContext.request.contextPath}/DeleteSelectedMovie">
                                <button type="submit" id="btnDeleteSelectedMovie" class="btn btn-info">Delete</button>
                            </form> 
                                    
                            <!-- frmDeleteSelectedMovie javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToDeleteMovieMessage();

                                function showAreYouSureYouWantToDeleteMovieMessage(){
                                    $('#frmDeleteSelectedMovie').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);
                                        
                                        swal(
                                        {
                                            title: "Are you sure?",
                                            text: "You will not be able to undo this action. \n\
                                                Please take note that this will permanently remove in the database!",
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
                                                    if(data.isDeletingMovieSuccess == "accessDenied"){
                                                        showAccessDeniedForDeletingMovieMessage();
                                                    }else if(data.isDeletingMovieSuccess == "reservationIsInProgress"){
                                                        showReservationIsInProgressForDeletingMovieMessage();
                                                    }else if(data.isDeletingMovieSuccess){
                                                        showSuccessDeleteMovieMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            });  
                                        });    
                                    });
                                }
                                
                                function showSuccessDeleteMovieMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully deleted this movie.",
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

                                function showAccessDeniedForDeletingMovieMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                                
                                function showReservationIsInProgressForDeletingMovieMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "No changes can be made because a reservation is in progress.",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                        
                        <td>
                            <!-- frmArchiveSelectedMovie -->
                            <form id="frmArchiveSelectedMovie" method="POST" action="${pageContext.request.contextPath}/ArchiveSelectedMovie">
                                <button type="submit" id="btnArchiveSelectedMovie" class="btn btn-success">Archive</button>
                            </form>

                            <!-- frmArchiveSelectedMovie javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToArchiveMovieMessage();

                                function showAreYouSureYouWantToArchiveMovieMessage(){
                                    $('#frmArchiveSelectedMovie').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);
                                        
                                        swal(
                                        {
                                            title: "Are you sure?",
                                            text: "You will not be able to undo this action. \n\
                                                Please take note that this will just change the status to Deactivated!",
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
                                                    if(data.isArchiveMovieSuccess == "accessDenied"){
                                                        showAccessDeniedForArchiveMovieMessage();
                                                    }else if(data.isArchiveMovieSuccess == "reservationIsInProgress"){
                                                        showReservationIsInProgressForArchiveMovieMessage();
                                                    }else if(data.isArchiveMovieSuccess){
                                                        showArchiveMovieSuccessMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            });
                                        });  
                                    });
                                }

                                function showArchiveMovieSuccessMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully deleted this movie.",
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

                                function showAccessDeniedForArchiveMovieMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                                
                                function showReservationIsInProgressForArchiveMovieMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "No changes can be made because a reservation is in progress.",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                        
                        <!-- detector if user is an IT Administrator -->
                        <script type="text/javascript">      
                            var userType = "<%= (String)request.getSession().getAttribute("COOKIE_USER_TYPE") %>";
                            var btnDeleteSelectedMovie = $('#btnDeleteSelectedMovie');
                            var btnArchiveSelectedMovie = $('#btnArchiveSelectedMovie');

                            if(userType != "IT Administrator"){
                                btnDeleteSelectedMovie.css("display", "none");
                                btnArchiveSelectedMovie.css("display", "none");
                            }
                        </script>
                    </tr>
                </table>
            </div>
            
            <!-- table for movie details -->
            <div class="table-responsive cart_info">
                <table class="table table-condensed" style="font-family: 'Roboto', sans-serif;">                        
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <h3 style="color: #199ac1;">
                                <%= (String)request.getSession().getAttribute("MOVIE_NAME") %> 
                                (<%= (String)request.getSession().getAttribute("MOVIE_YEAR") %>)
                            </h3>
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
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">MTRCB:</font>
                                <%= (String)request.getSession().getAttribute("MTRCB_RATING") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Director:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_DIRECTOR") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Released by:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_RELEASEDBY") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Cast:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_CAST") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                            <font style="color: #199ac1;">Genre:</font> 
                            <%= (String)request.getSession().getAttribute("MOVIE_GENRE") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Synopsis:</font><br><br>
                                <%= (String)request.getSession().getAttribute("MOVIE_SYNOPSIS") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Running Time:</font><br><br>
                                <%= (String)request.getSession().getAttribute("MOVIE_DURATION") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Price:</font> 
                                &#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>
                                <br>
                                <font style="color: #199ac1;">Type Of Seating:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>
                                <br>
                                <font style="color: #199ac1;">Auditorium:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_AUDITORIUM") %>
                            </h6>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h6 style="color: white;">
                                <font style="color: #199ac1;">Release Date:</font> 
                                <%= (String)request.getSession().getAttribute("MOVIE_RELEASEDATE") %>
                                <br><br>
                                <font style="color: #199ac1;">Close Date:</font>
                                <%= (String)request.getSession().getAttribute("MOVIE_CLOSEDATE") %>
                                <br><br>
                            </h6>
                        </td>
                    </tr>
                </table>
            </div>
            
            <br>
        </div>
    </div>
                                
    <!-- showAdministratorDetails -->
    <div class="remodal" data-remodal-id="showAdministratorDetails" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <!-- table for button holders -->
            <div class="table-responsive cart_info">
                <table class="table table-condensed">   
                    <tr>
                        <td style="float: right;">
                            <!-- frmDeleteSelectedAdministrator -->
                            <form id="frmDeleteSelectedAdministrator" method="POST" action="${pageContext.request.contextPath}/DeleteSelectedAdministrator">
                                <button type="submit" class="btn btn-info">Delete</button>
                            </form> 
                                    
                            <!-- frmDeleteSelectedAdministrator javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToDeleteAdministratorMessage();

                                function showAreYouSureYouWantToDeleteAdministratorMessage(){
                                    $('#frmDeleteSelectedAdministrator').submit(function(event) {
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
                                                    if(data.isDeletingAdministratorSuccess == "accessDenied"){
                                                        showAccessDeniedForDeletingAdministratorMessage();
                                                    }else if(data.isDeletingAdministratorSuccess){
                                                        showSuccessDeleteAdministratorMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            }); 
                                        });     
                                    });
                                }

                                function showSuccessDeleteAdministratorMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully deleted this user.",
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

                                function showAccessDeniedForDeletingAdministratorMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                        
                        <td style="float: right;">
                            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                                <button type="button" class="btn btn-danger">Back</button>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
            
            <!-- table for account user or admin information -->
            <div class="table-responsive cart_info">
                <table class="table table-condensed" style="font-family: 'Roboto', sans-serif;">                        
                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Fullname:</font>
                                <%= (String)request.getSession().getAttribute("USER_FULLNAME") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">Email Address:</font> 
                                <%= (String)request.getSession().getAttribute("USER_EMAILADDRESS") %>
                            </h4>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">User Type:</font> 
                                <%= (String)request.getSession().getAttribute("USER_TYPE") %>
                            </h4>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: white;">
                                <font style="color: #199ac1;">User Status:</font> 
                                <%= (String)request.getSession().getAttribute("USER_STATUS") %>
                            </h4>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align:left;">
                            <!-- frmUpdateStatusOfAccountUserOrAdmin -->
                            <form id="frmUpdateStatusOfAccountUserOrAdmin" method="POST" action="${pageContext.request.contextPath}/UpdateStatusOfAccountUserOrAdmin">
                                <h4 style="color: white;">
                                    <font style="color: #199ac1;">Change User Type:</font><br>
                                    <select name="txtUserType" style="color: black;" required="required">
                                        <option disabled selected>---CHOOSE USER TYPE---</option>
                                        <option value="Operations Supervisor">Operations Supervisor</option>
                                        <option value="Ticket Seller">Ticket Seller</option>
                                    </select>
                                    
                                    <br><br>
                                    
                                    <font style="color: #199ac1;">Change Account Status:</font><br>
                                    <select name="txtUserStatus" style="color: black;" required="required">
                                        <option selected disabled>---SELECT ACCOUNT STATUS---</option>
                                        <option value="Verified">Verified</option>
                                        <option value="Unverified">Unverified</option>
                                    </select>
                                    <button type="submit" class="button">Update</button>
                                </h4> 
                            </form>
                            
                            <!-- frmUpdateStatusOfAccountUserOrAdmin javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToUpdateStatusOfAccountUserOrAdminMessage();

                                function showAreYouSureYouWantToUpdateStatusOfAccountUserOrAdminMessage(){
                                    $('#frmUpdateStatusOfAccountUserOrAdmin').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);
                                        
                                        swal(
                                        {
                                            title: "Are you sure?",
                                            text: "You will not be able to undo this action.",
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
                                                    if(data.isUpdateStatusOfAccountUserOrAdminSuccess == "accessDenied"){
                                                        showAccessDeniedForUpdateStatusOfAccountUserOrAdminMessage();
                                                    }else if(data.isUpdateStatusOfAccountUserOrAdminSuccess){
                                                        showUpdateStatusOfAccountUserOrAdminSuccessMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            });  
                                        }); 
                                    });
                                }
                                
                                function showUpdateStatusOfAccountUserOrAdminSuccessMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully updated status and type of this account user!",
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

                                function showAccessDeniedForUpdateStatusOfAccountUserOrAdminMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                    </tr>
                </table>
            </div>
            
            <br>
        </div>
    </div>
                                        
    <!-- showMovieAuditoriumDetails -->
    <div class="remodal" data-remodal-id="showMovieAuditoriumDetails" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
        <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
        <div>
            <div class="table-responsive cart_info">
                <table class="table table-condensed">   
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                                <button type="button" class="btn btn-danger">Back</button>
                            </a>
                        </td>
                        
                        <td>
                            <!-- frmShowOrBlockIndividualSeats -->
                            <form id="frmShowOrBlockIndividualSeats" method="POST" action="${pageContext.request.contextPath}/BlockIndividualSeats?movieAuditoriumName=<%= (String)request.getSession().getAttribute("MOVIE_AUDITORIUMNAME") %>">
                                <button type="submit" class="btn btn-success">Show / Block Individual Seats</button>
                            </form> 
                                    
                            <!-- frmShowOrBlockIndividualSeats javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToShowOrBlockIndividualSeatsMessage();

                                function showAreYouSureYouWantToShowOrBlockIndividualSeatsMessage(){
                                    $('#frmShowOrBlockIndividualSeats').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);

                                        $.ajax({
                                            type: 'POST',
                                            dataType : 'JSON',
                                            url: formEl.prop('action'),
                                            data: formEl.serialize(),
                                            //if received a response from the server
                                            success: function( data, textStatus, jqXHR) {
                                                if(data.isShowOrBlockIndividualSeatsSuccess == "accessDenied"){
                                                    showAccessDeniedForShowOrBlockIndividualSeatsMessage();
                                                }else if(data.isShowOrBlockIndividualSeatsSuccess){
                                                    location.replace("${pageContext.request.contextPath}/pages/ADMIN/adminShowAndEditSeatingPlanDesigner.jsp?movieAuditoriumName=<%= (String)request.getSession().getAttribute("MOVIE_AUDITORIUMNAME") %>");
                                                }
                                            },
                                            error: function(jqXHR, textStatus, errorThrown){
                                                alert('Error: ' + textStatus + ' - ' + errorThrown);
                                            }
                                        });      
                                    });
                                }

                                function showAccessDeniedForShowOrBlockIndividualSeatsMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                        
                        <td>
                            <!-- frmResetDatabaseForBlockedSeats -->
                            <form id="frmResetDatabaseForBlockedSeats" method="POST" action="${pageContext.request.contextPath}/ResetDatabaseForBlockedSeats?movieAuditoriumName=<%= (String)request.getSession().getAttribute("MOVIE_AUDITORIUMNAME") %>">
                                <button type="submit" class="btn btn-warning">Reset Database</button>
                            </form> 
                                    
                            <!-- frmResetDatabaseForBlockedSeats javascript -->
                            <script type="text/javascript">
                                showAreYouSureYouWantToResetDatabaseForBlockedSeatsMessage();

                                function showAreYouSureYouWantToResetDatabaseForBlockedSeatsMessage(){
                                    $('#frmResetDatabaseForBlockedSeats').submit(function(event) {
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
                                                    if(data.isResetDatabaseForBlockedSeatsSuccess == "accessDenied"){
                                                        showAccessDeniedForResetDatabaseForBlockedSeatsMessage();
                                                    }else if(data.isResetDatabaseForBlockedSeatsSuccess){
                                                        showSuccessResetDatabaseForBlockedSeatsMessage();
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            }); 
                                        });     
                                    });
                                }

                                function showSuccessResetDatabaseForBlockedSeatsMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully reset database of this movie auditorium.",
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

                                function showAccessDeniedForResetDatabaseForBlockedSeatsMessage() {
                                    swal(
                                        {
                                            title: "Invalid!",
                                            text: "You do not have access to this option!",
                                            type: "error",
                                            showCancelButton: false,
                                            confirmButtonColor: "#199ac1",
                                            confirmButtonText: "Close",
                                            closeOnConfirm: true 
                                        });
                                }
                            </script>
                        </td>
                    </tr>
                </table>
            </div>
            
            <div class="table-responsive cart_info">
                <table class="table table-condensed" style="font-family: 'Roboto', sans-serif;">                        
                    <tr>
                        <td style="text-align:left;">
                            <h4 style="color: #199ac1;">
                                <font style="color: white;">Auditorium Name: </font>
                                <%= (String)request.getSession().getAttribute("MOVIE_AUDITORIUMNAME") %>   
                            </h4>
                        </td>
                    </tr>
                </table>
            </div>
            
            <br>
        </div>
    </div>
    
    <!--
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    -->
    
    <script type="text/javascript">
        $("#txtMTRCB_Rating option").val(function(idx, val) {
            $(this).siblings("[value='"+ val +"']").remove();
        });
        
        $("#txtMovieGenre option").val(function(idx, val) {
            $(this).siblings("[value='"+ val +"']").remove();
        });
        
        $("#txtMoviePrice option").val(function(idx, val) {
            $(this).siblings("[value='"+ val +"']").remove();
        });
        
        $("#txtMovieTypeOfSeating option").val(function(idx, val) {
            $(this).siblings("[value='"+ val +"']").remove();
        });
        
        $("#txtMovieStatus option").val(function(idx, val) {
            $(this).siblings("[value='"+ val +"']").remove();
        });
    </script>
	
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.1.0/material.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.15/css/dataTables.material.min.css">
    
    <script type="text/javascript" src="//code.jquery.com/jquery-1.12.4.js"></script>

    <script type="text/javascript" src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript" src="https://cdn.datatables.net/1.10.15/js/dataTables.material.min.js"></script>    
    
    <!--tabs-->
    <div class="main" style="font-family: 'Roboto', sans-serif;">
        <!--
        <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
        -->
        
        <input id="tab1" type="radio" name="tabs" checked>
        <label for="tab1">Customer (s)</label>

        <input id="tab2" type="radio" name="tabs">
        <label for="tab2">Account User(s)</label>

        <input id="tab3" type="radio" name="tabs">
        <label for="tab3">Movie (s)</label>

        <input id="tab4" type="radio" name="tabs">
        <label for="tab4">Movie Auditorium (s)</label>

        <input id="tab5" type="radio" name="tabs">
        <label for="tab5">Reservations Report (s)</label>
        
        <input id="tab6" type="radio" name="tabs">
        <label for="tab6">Sales Report</label>
        
        <!--content-->
        <div class="content">

            <!-- CUSTOMERS -->
            <div id="content1">       
                <script type="text/javascript">                   
                    $.ajax({
                        url: '${pageContext.servletContext.contextPath}/ShowAllUsers',
                        dataType: 'json',
                        success: function(data) {
                            for (var i=0; i<data.USERS.length; i++) {
                                var row = $('<tr style="color: black; font-size: 16px;">\n\
                                <td>' + data.USERS[i].fullName + '</td>\n\
                                <td>' + data.USERS[i].emailAddress + '</td>\n\
                                <td>' + data.USERS[i].fullBirthDay + '</td>\n\
                                <td>' + data.USERS[i].accountType + '</td>\n\
                                <td>' + data.USERS[i].dateAndTimeCreated + '</td></tr>');
                                $('#TBLUSERS').append(row);                
                            } 
                            $('#TBLUSERS').DataTable();
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                </script>
                
                <div class="table-responsive cart_info">
                    <table id="TBLUSERS" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
                        <thead>
                            <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                <td>Full name</td>
                                <td>Email address</td>
                                <td>Birthday</td>
                                <td>Account Type</td>
                                <td>Date & Time Created</td>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>

            <!-- ADMINS -->
            <div id="content2">
                <script type="text/javascript">
                $.ajax({
                    url: '${pageContext.servletContext.contextPath}/ShowAllAdministrators',
                    dataType: 'json',
                    success: function(data) {
                        for (var i=0; i<data.ADMINISTRATORS.length; i++) {
                            var row = $('<tr style="color: black; font-size: 16px;">\n\
                            <td>' + data.ADMINISTRATORS[i].fullName + '</td>\n\
                            <td>' + data.ADMINISTRATORS[i].emailAddress + '</td>\n\
                            <td>' + data.ADMINISTRATORS[i].fullBirthDay + '</td>\n\
                            <td>' + data.ADMINISTRATORS[i].accountType + '</td>\n\
                            <td>' + data.ADMINISTRATORS[i].dateAndTimeCreated + '</td>\n\
                            <td>' + data.ADMINISTRATORS[i].action + '</td></tr>');
                            $('#TBLADMINISTRATORS').append(row);  
                        } 
                        $('#TBLADMINISTRATORS').DataTable();
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        //alert('Error: ' + textStatus + ' - ' + errorThrown);
                        console.log('Error: ' + textStatus + ' - ' + errorThrown);
                    }
                });
                </script>

                <div class="table-responsive cart_info">
                    <table id="TBLADMINISTRATORS" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
                        <thead>
                            <tr style="background-color: #199ac1; color: white; font-size: 16px;">
                                <td>Full name</td>
                                <td>Email address</td>
                                <td>Birthday</td>
                                <td>Account Type</td>
                                <td>Date & Time Created</td>
                                <td>Action</td>
                            </tr>
                        </thead>
                    </table>
                </div> 
            </div>

            <!--MOVIES-->
            <div id="content3">         
                <script type="text/javascript">
                $.ajax({
                    url: '${pageContext.servletContext.contextPath}/ShowAllAvailableMovies',
                    dataType: 'json',
                    success: function(data) {
                        for (var i=0; i<data.MOVIES.length; i++) {
                            var row = $('<tr style="color: black; font-size: 16px;">\n\
                            <td>' + data.MOVIES[i].movieName + ' (' + data.MOVIES[i].movieYear + ')</td>\n\
                            <td><a href="${pageContext.servletContext.contextPath}/ShowDetailsOfSelectedMovieForAdmin?movie_id=' + data.MOVIES[i].movieID + '"><button type="button" class="btn btn-danger">Details</button></a></td></tr>');
                            $('#TBLMOVIECINEMA').append(row);
                        }
                        $('#TBLMOVIECINEMA').DataTable();
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        //alert('Error: ' + textStatus + ' - ' + errorThrown);
                        console.log('Error: ' + textStatus + ' - ' + errorThrown);
                    }
                });
                </script>

                <div class="table-responsive cart_info">
                        <table id="TBLMOVIECINEMA" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
                            <thead>
                                <tr style="background-color: #199ac1; color: white; font-size: 16px;">
                                    <td>Movie Name & Year</td>
                                    <td>Action</td>
                                </tr>
                            </thead>
                        </table>
                    </div>
            </div>

            <!-- MOVIE AUDITORIUM -->
            <div id="content4">
                <script type="text/javascript">
                    $.ajax({
                        url: '${pageContext.servletContext.contextPath}/ShowAllMovieAuditorium',
                        dataType: 'JSON',
                        success: function(data) {
                            for (var i=0; i<data.MOVIE_AUDITORIUM.length; i++) {
                                var row = $('<tr style="color: black; font-size: 16px;">\n\
                                <td>' + data.MOVIE_AUDITORIUM[i].movieAuditoriumName + '</td>\n\
                                <td>' + data.MOVIE_AUDITORIUM[i].movieAuditoriumName_dateAndTimeCreated + '</td>\n\
                                <td>' + data.MOVIE_AUDITORIUM[i].action + '</td></tr>');
                                $('#TBLMOVIEAUDITORIUM').append(row);   
                            } 
                            $('#TBLMOVIEAUDITORIUM').DataTable();
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                </script>
    
                <div class="table-responsive cart_info">
                    <table id="TBLMOVIEAUDITORIUM" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
                        <thead>
                            <tr style="background-color: #199ac1; color: white; font-size: 16px;">
                                <td>Auditorium Name</td>
                                <td>Date & Time Created</td>
                                <td>Action</td>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>

            <!-- RESERVATIONS REPORT -->
            <div id="content5">
                <script type="text/javascript">
                $.ajax({
                    url: '${pageContext.servletContext.contextPath}/ShowAllReservationsReport',
                    dataType: 'JSON',
                    success: function(data) {
                        for (var i=0; i<data.RESERVATIONSREPORT.length; i++) {
                            var row = $('<tr style="color: black; font-size: 16px;">\n\\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_transactionDate + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieBranchName + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieName + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieAuditoriumName + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieReferenceNumber + '</td>\n\
                            <td>&#8369;' + data.RESERVATIONSREPORT[i].reservation_movieAmount + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieQuantity + '</td>\n\
                            <td>&#8369;' + data.RESERVATIONSREPORT[i].reservation_totalAmount + '.00</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_movieSchedule + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_paymentDue + '</td>\n\
                            <td id="statusOfUserInReservation">' + data.RESERVATIONSREPORT[i].reservation_status + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].reservation_reservedBy + '</td>\n\
                            <td>' + data.RESERVATIONSREPORT[i].action + '</td></tr>');
                            $('#TBLRESERVATIONSREPORT').append(row);   
                        } 
                        $('#TBLRESERVATIONSREPORT').DataTable();
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        //alert('Error: ' + textStatus + ' - ' + errorThrown);
                        console.log('Error: ' + textStatus + ' - ' + errorThrown);
                    }
                });
                </script>

                <div class="table-responsive cart_info">
                    <table id="TBLRESERVATIONSREPORT" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
                        <thead>
                            <tr style="background-color: #199ac1; color: white; font-size: 16px;">
                                <td>Transaction Date</td>
                                <td>Branch</td>
                                <td>Movie</td>
                                <td>Auditorium</td>
                                <td>Reference Number</td>
                                <td>Ticket Price</td>
                                <td>Quantity</td>
                                <td>Total Amount</td>
                                <td>Schedule</td>
                                <td>Payment Due</td>
                                <td>Status</td>
                                <td>Reserved By</td>
                                <td>Action</td>                        
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
                    
            <!-- SALES REPORT -->
            <div id="content6">
                <!-- frmShowSalesBasedOnCategory -->
                <form id="frmShowSalesBasedOnCategory" method="POST" action="${pageContext.servletContext.contextPath}/ShowSalesBasedOnCategory">
                    <select name="txtCategory" id="txtCategory" required="required">
                        <option disabled selected>---CHOOSE CATEGORY---</option>
                        <option value="Daily">Daily</option>
                        <option value="Monthly">Monthly</option>
                        <option value="Yearly">Yearly</option>
                    </select> 
                    
                    <button type="submit" class="button">Search</button>
                </form>
                    
                <!-- frmShowSalesBasedOnCategory javascript -->
                <script type="text/javascript">
                    $(function() {
                        $('#frmShowSalesBasedOnCategory').submit(function(event) {
                            event.preventDefault();
                            var formEl = $(this);
                            var category = $('#txtCategory').val();
                            var headerForDailySales,headerForMonthlySales,headerForYearlySales;
                            var rowForDailySales,rowForMonthlySales,rowForYearlySales;
                            
                            $.ajax({
                                type: 'POST',
                                dataType : 'JSON',
                                url: formEl.prop('action'),
                                data: formEl.serialize(),
                                success: function(data) {
                                    if(data.isShowSalesOfSelectedMovieSuccess == "accessDenied"){
                                        swal("Oops! We're sorry!", "You do not have access to this option.", "error");
                                    }else{   
                                        $('#TBLSALESREPORT').empty();
                                        if(category == "Daily"){    
                                            headerForDailySales = $('<thead><tr style="background-color: #199ac1; color: white; font-size: 16px;">\n\
                                            <td>Year</td><td>Day</td><td>Month</td><td>Subtotal</td><td>Total Sales</td></tr></thead>');
                                            $('#TBLSALESREPORT').append(headerForDailySales); 
                                                        
                                            for (var i=0; i<data.SALES.length; i++) {
                                                rowForDailySales = $('<tr style="color: black; font-size: 16px;">\n\
                                                <td>' + data.SALES[i].year + '</td>\n\
                                                <td>' + data.SALES[i].day + '</td>\n\
                                                <td>' + data.SALES[i].month + '</td>\n\
                                                <td>&#8369;' + data.SALES[i].subtotal + '</td>\n\
                                                <td>' + data.SALES[i].orders + '</td></tr>');
                                                $('#TBLSALESREPORT').append(rowForDailySales);      
                                            } 
                                            $('#TBLSALESREPORT').DataTable();
                                        }else if(category == "Monthly"){
                                            headerForMonthlySales = $('<thead><tr style="background-color: #199ac1; color: white; font-size: 16px;">\n\
                                            <td>Year</td><td>Month</td><td>Subtotal</td><td>Total Sales</td></tr></thead>');
                                            $('#TBLSALESREPORT').append(headerForMonthlySales); 
                                            
                                            for (var i=0; i<data.SALES.length; i++) {
                                                rowForMonthlySales = $('<tr style="color: black; font-size: 16px;">\n\
                                                <td>' + data.SALES[i].year + '</td>\n\
                                                <td>' + data.SALES[i].month + '</td>\n\
                                                <td>&#8369;' + data.SALES[i].subtotal + '</td>\n\
                                                <td>' + data.SALES[i].orders + '</td></tr>');
                                                $('#TBLSALESREPORT').append(rowForMonthlySales);      
                                            } 
                                            $('#TBLSALESREPORT').DataTable();
                                        }else if(category == "Yearly"){
                                            headerForYearlySales = $('<thead><tr style="background-color: #199ac1; color: white; font-size: 16px;">\n\
                                            <td>Year</td><td>Subtotal</td><td>Total Sales</td></tr></thead>');
                                            $('#TBLSALESREPORT').append(headerForYearlySales); 
                                            
                                            for (var i=0; i<data.SALES.length; i++) {
                                                rowForYearlySales = $('<tr style="color: black; font-size: 16px;">\n\
                                                <td>' + data.SALES[i].year + '</td>\n\
                                                <td>&#8369;' + data.SALES[i].subtotal + '</td>\n\
                                                <td>' + data.SALES[i].orders + '</td></tr>');
                                                $('#TBLSALESREPORT').append(rowForYearlySales);      
                                            } 
                                            $('#TBLSALESREPORT').DataTable();
                                        }     
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    swal("Oops! We're sorry!", "Please choose an option!", "error");
                                    //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                    //console.log('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            });
                        });
                    });
                </script>
                
                <div class="table-responsive cart_info">
                    <table id="TBLSALESREPORT" class="mdl-data-table" cellspacing="0" width="100%" style="font-family: 'Roboto', sans-serif; text-align: center;">
          
                    </table>
                </div>
            </div>

        </div>
    </div>
                        
    <!--Footer-->
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
                            <img src="../../Resources/images/home/map.png" alt="" />
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
				<li><a href="../../pages/USER/termsAndConditions.jsp" target="_blank">Terms and Conditions</a></li>
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
    
    <!--
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    -->
    
    <script>window.jQuery || document.write('<script src="../../Resources/Remodal/remodal.min.js"><\/script>')</script>
    <script src="../../Resources/Remodal/remodal.js"></script>

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
        
    <!--
    <script src="../../Resources/js/jquery.js"></script>
    -->
    
    <script src="../../Resources/js/bootstrap.min.js"></script>
    <script src="../../Resources/js/jquery.scrollUp.min.js"></script>
    <script src="../../Resources/js/jquery.prettyPhoto.js"></script>
    <script src="../../Resources/js/main.js"></script>
</body>
</html>

