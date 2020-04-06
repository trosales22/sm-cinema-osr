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
    <link href="../../Resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../Resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="../../Resources/css/prettyPhoto.css" rel="stylesheet">
    <link href="../../Resources/css/animate.css" rel="stylesheet">
    <link href="../../Resources/css/main.css" rel="stylesheet">
    <link href="../../Resources/css/responsive.css" rel="stylesheet">
    
    <style>body{padding-top: 60px;}</style>
	
    <link href="../../Resources/bootstrap3/css/bootstrap.css" rel="stylesheet" />
 
    <link href="../../Resources/css/login-register.css" rel="stylesheet" />
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
	
    <link href="../../Resources/css/formStyle.css" rel="stylesheet">
    <link href="../../Resources/css/responsiveTabs.css" rel="stylesheet">
    
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
	
	
    <script src="../../Resources/jquery/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="../../Resources/bootstrap3/js/bootstrap.js" type="text/javascript"></script>
    <script src="../../Resources/js/login-register-forgotPassword.js" type="text/javascript"></script>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="../../Resources/js/sweetalert.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
   
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    
    <title>Checkout | SM Cinema Online Seat Reservation</title>
</head>

<!-- body -->
<body>
    <!--header-->
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
                            <a href="${pageContext.request.contextPath}/pages/USER/home.jsp"><img src="../../Resources/images/home/logo.jpg"/></a>
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
                                
                                <li><a href="${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>"><i class="fa fa-user"></i> My Account</a></li>
                                <li>
                                    <a id="btnLogoutUser" href="${pageContext.request.contextPath}/LogoutUser"><i class="fa fa-lock"></i> Logout</a>
                                    <!-- btnLogoutUser javascript -->
                                    <script type="text/javascript">
                                        $('#btnLogoutUser').click(function (event){ 
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
    
    <!-- center -->
    <center>
        <!-- frmReserveTicketViaOnSitePayment -->
        <form id="frmReserveTicketViaOnSitePayment" method="post" action="${pageContext.request.contextPath}/ReserveTicketViaOnSitePayment?user_emailAddress=<%=user_emailAddress %>">
            <table border="1">
                <tr>
                    <td>
                        <div style="width: 100%; vertical-align: top;">
                            <!-- movieScreeningDetails -->
                            <div class="table-responsive cart_info">
                                <table class="table table-condensed" style="text-align: center; width: 100%;">
                                    <thead>
                                        <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                            <td colspan="2">Movie Screening Details</td>
                                        </tr>
                                    </thead>

                                    <tbody style="text-align: left;">
                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Movie</p></td>
                                            <input type="hidden" name="movieName" value="<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_NAME") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Branch</p></td>
                                            <input type="hidden" name="movieBranchName" value="<%= (String)request.getSession().getAttribute("MOVIE_BRANCHNAME") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_BRANCHNAME") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Auditorium</p></td>
                                            <input type="hidden" name="movieAuditoriumName" value="<%= (String)request.getSession().getAttribute("selected_movieAuditorium") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieAuditorium") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Seat (s)</p></td>
                                            <input type="hidden" name="movieSelectedSeats" value="<%= (String)request.getSession().getAttribute("selectedSeats_improvedName") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selectedSeats_improvedName") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Type</p></td>
                                            <input type="hidden" name="movieTypeOfSeating" value="<%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Screening Date</p></td>
                                            <input type="hidden" name="movieScreeningDate" value="<%= (String)request.getSession().getAttribute("selected_movieAvailableDay") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieAvailableDay") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Start Time</p></td>
                                            <input type="hidden" name="movieStartTime" value="<%= (String)request.getSession().getAttribute("selected_movieSchedule") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieSchedule") %></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Ticket Price</p></td>
                                            <input type="hidden" name="moviePrice" value="<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>" />
                                            <td style="font-size: 16px;">&#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div><br>
                    </td>
                    
                    <br><br><br><br><br><br>
                    
                    <td>
                        <div style="width: 100%; vertical-align: top;">
                            <!-- purchaseDetails -->
                            <div class="table-responsive cart_info">
                                <table class="table table-condensed" style="text-align: center; width: 100%;">
                                    <thead>
                                        <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                            <td colspan="4">Purchase Details</td>
                                        </tr>
                                    </thead>

                                    <tbody style="text-align: left;">
                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Seat Quantity</p></td>
                                            <input type="hidden" name="movieSeatQuantity" value="<%= (String)request.getSession().getAttribute("selected_seatQuantity") %>" />
                                            <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></td>
                                        </tr>

                                        <tr>
                                            <td style="font-size: 16px;" colspan="4"><p></p></td>
                                        </tr>

                                        <tr>
                                            <td></td>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Qty</p></td>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Price</p></td>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Total</p></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Ticket</p></td>
                                            <td style="font-size: 16px;"><p id="txtQuantity"><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></p></td>
                                            <td style="font-size: 16px;"><p id="txtPrice">&#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %></p></td>
                                            <td style="font-size: 16px;"><p id="txtTotalTicketPrice"></p></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Convenience Fee</p></td>
                                            <td style="font-size: 16px;"><p><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></p></td>
                                            <td style="font-size: 16px;"><p>&#8369;20.00</p></td>
                                            <td style="font-size: 16px;"><p id="txtTotalConvenienceFee"></p></td>
                                        </tr>

                                        <tr>
                                            <td style="color: #199ac1; font-size: 16px;"><p>Grand Total</p></td>
                                            <td colspan="2" style="font-size: 16px;"><p></p></td>
                                            <td style="font-size: 16px;"><p id="txtGrandTotal_total"></p></td>
                                        </tr>
                                    </tbody>

                                    <!-- script for computation -->
                                    <script type="text/javascript">
                                        $(document).ready(function() {
                                            var moviePrice,movieQuantity,totalTicketPrice,totalConvenienceFee,
                                            grandTotal_total;
                                            moviePrice = <%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>;
                                            movieQuantity = <%= (String)request.getSession().getAttribute("selected_seatQuantity") %>;

                                            totalTicketPrice = (moviePrice * movieQuantity);
                                            $('#txtTotalTicketPrice').append("&#8369;" + totalTicketPrice + ".00");

                                            totalConvenienceFee = (movieQuantity * 20);
                                            $('#txtTotalConvenienceFee').append("&#8369;" + totalConvenienceFee + ".00");

                                            grandTotal_total = (totalTicketPrice + totalConvenienceFee);
                                            $('#txtGrandTotal_total').append("&#8369;" + grandTotal_total + ".00");
                                        });
                                    </script>
                                </table>
                            </div>                       
                        </div><br><br><br><br><br><br>
                    </td>      
                </tr>
                
                <tr>
                    <td colspan="2">
                        <!-- choosePaymentOption -->
                        <div class="table-responsive cart_info">
                            <table class="table table-condensed" style="text-align: center; width: 100%;">
                                <thead>
                                    <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                        <td colspan="2">Choose Payment Option</td>
                                    </tr>
                                </thead>

                                <tbody>
                                    <tr colspan="2">
                                        <td>
                                            <h3>On-Site Payment</h3>
                                            <input type="hidden" name="txtPaymentOption" value="On-Site Payment">
                                        </td>
                                    </tr>

                                    <tr colspan="2">
                                        <td>
                                           <p style="font-size: 16px;">
                                                We encourage all SM Cinema patrons to come earlier than the 
                                                preferred screening time to avoid any inconvenience.<br>
                                                <b style="color: red;">Note: Changing of seats or schedule will only be possible if it is paid.
                                                If paid, the E-Ticket will be considered final and conclusive. Thank you!</b>
                                            </p> 
                                        </td>
                                    </tr>

                                    <tr colspan="2">
                                        <td>
                                            <p style="font-size: 16px;">
                                                <input type="checkbox" required="required">I agree to the 
                                                <a href="termsAndConditions.jsp" target="_blank">SM Cinema Terms & Conditions</a>
                                            </p>  
                                        </td>
                                    </tr>

                                    <tr colspan="2">
                                        <td>
                                            <button class="button" type="submit"><i class="fa fa-shopping-cart"></i> Book Now!</button>
                                        </td> 
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- frmReserveTicketViaOnSitePayment javascript -->
                        <script type="text/javascript">
                            submitInformationAboutReserveTicketViaOnSitePayment();

                            function submitInformationAboutReserveTicketViaOnSitePayment(){
                                $('#frmReserveTicketViaOnSitePayment').submit(function(event) {
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
                                                success: function( data, textStatus, jqXHR) {
                                                    if(data.status == "failedBecauseOfRecordOfCancellation"){
                                                        swal("Invalid!", "Failed to reserve your ticket because of excessive cancellation. Please try again!", "error");
                                                    }else if(data.status == "oneReservationPerDay"){
                                                        swal("Oops! We're sorry!", "You are only allowed one (1) reservation / transaction for today. Thankyou!", "error");
                                                    }else if(data.status == "successToReserved"){
                                                        successfullyReserveTicketViaOnSitePaymentMessage();
                                                    }else{
                                                        swal("Invalid!", "Failed to reserve your ticket. Please try again!", "error");
                                                    }
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                    console.log('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            });
                                        }, 3000);               
                                    });
                                });
                            }

                            function successfullyReserveTicketViaOnSitePaymentMessage() {
                                swal(
                                    {
                                        title: "Successful!",
                                        text: "You've successfully reserved your ticket. \n\
                                        Let's go print it now! Go to My Account->Reservations History->View",
                                        type: "success",
                                        showCancelButton: false,
                                        confirmButtonColor: "#199ac1",
                                        confirmButtonText: "Continue",
                                        closeOnConfirm: false 
                                    },
                                function(){
                                    location.replace("${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>");
                                });
                            }
                        </script>     
                    </td>
                </tr>
            </table>
        </form>
        
        <div style="width: 100%; vertical-align: top;">
            <!-- paypal form -->
            <form method="POST" action="${initParam['posturl']}">
                <hr width="100%" style="border: dotted;">
                <h2>Or</h2>
                <hr width="100%" style="border: dotted;"><br>

                <h3>PayPal</h3>
                <input type="hidden" name="txtPaymentOption" value="PayPal">

                <br><br>

                <input type="hidden" name="currency_code" value="PHP">
                <input type="hidden" name="cmd" value="_cart">
                <input type="hidden" name="hosted_button_id" value="LKXDT8UYG88Z6">
                <input type="hidden" name="upload" value="1">
                <input type="hidden" name="return" value="${initParam['returnurl']}">
                <input type="hidden" name="business" value="${initParam['business']}">

                <input type="hidden" name="item_name_1" value="<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>" />

                <input type="hidden" name="amount_1" value="<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>"/>
                <input type="hidden" name="quantity_1" value="<%= (String)request.getSession().getAttribute("selected_seatQuantity") %>" />

                <input type="hidden" name="item_name_2" value="Convenience Fee" />
                <input type="hidden" name="amount_2" value="20"/>
                <input type="hidden" name="quantity_2" value="<%= (String)request.getSession().getAttribute("selected_seatQuantity") %>" />

                <p style="font-size: 16px;">
                    We encourage all SM Cinema patrons to come earlier than the 
                    preferred screening time to avoid any inconvenience.<br>
                    <b style="color: red;">Note: Changing of seats or schedule will only be possible if it is paid.
                    If paid, the E-Ticket will be considered final and conclusive. Thank you!</b>
                </p>

                <p style="font-size: 16px;">
                    <input type="checkbox" required="required">I agree to the 
                    <a href="termsAndConditions.jsp" target="_blank">SM Cinema Terms & Conditions</a>
                </p>

                <br>

                <button class="button" type="submit">
                    <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
                    <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
                </button>

                <br><br><br><br><br>
            </form>
        </div>                    
    </center>
                  
    <!--footer-->
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
                                <li><a href="termsAndConditions.jsp" target="_blank">Terms and Conditions</a></li>
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

    <script src="../../Resources/js/jquery.js"></script>
    <script src="../../Resources/js/bootstrap.min.js"></script>
    <script src="../../Resources/js/jquery.scrollUp.min.js"></script>
    <script src="../../Resources/js/jquery.prettyPhoto.js"></script>
    <script src="../../Resources/js/main.js"></script>
</body>
</html>

