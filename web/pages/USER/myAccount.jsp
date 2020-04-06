<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <!-- head -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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
	
        <link rel="stylesheet" href="../../Resources/Remodal/remodal.css">
        <link rel="stylesheet" href="../../Resources/Remodal/remodal-default-theme.css">
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
    
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="../../Resources/js/sweetalert.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
        
        <script src="../../Resources/js/html2canvas.js" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.4/jspdf.debug.js"></script>
        <title>My Account</title>
    </head>
    
    <!-- body -->
    <body>
        <!-- auto detector of user blocked javascript -->
        <script>
            $(document).ready(function() {
                $.ajax({
                    url: '${pageContext.servletContext.contextPath}/DetectIfUserIsBlocked?user_emailAddress=<%= (String)request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS") %>',
                    dataType: 'JSON',
                    success: function(data) {
                        if(data.isUserBlocked){
                            swal(
                                {
                                    title: "Oops! We're sorry!",
                                    text: "Your account has been suspended due to excessive cancellations.\n\
                                        Please back on " + data.USER_BLOCKEDUNTIL + ". Thankyou!",
                                    type: "error",
                                    showCancelButton: false,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Continue",
                                    closeOnConfirm: false 
                                },
                            function(){
                                location.replace("${pageContext.request.contextPath}/LogoutUser");
                            });
                        }   
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        //alert('Error: ' + textStatus + ' - ' + errorThrown);
                        console.log('Error: ' + textStatus + ' - ' + errorThrown);
                    }
                });
            });
        </script> 
        
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
                            <a href="home.jsp"><img src="../../Resources/images/home/logo.jpg"/></a>
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
                
        <!-- save as pdf javascript -->
        <script type="text/javascript">
            $(document).ready(function() {
                $('#btnSaveAsPDF').click(function () {
                    html2canvas($('#eTicket'),{
                       onrendered: function(canvas){
                           var img = canvas.toDataURL("image/png");
                           var doc = new jsPDF();
                           doc.addImage(img,'JPEG',20,20);
                           doc.save('<%= (String)request.getSession().getAttribute("reservation_movieReferenceNumber") %>.pdf');
                       } 
                    });
                });
            });
        </script>
                 
        <!-- print e-ticket javascript -->
        <script type="text/javascript">
            function printE_Ticket(printPage){
                var printContents = document.getElementById("eTicket").innerHTML;     
                var originalContents = document.body.innerHTML;       
                document.body.innerHTML = printContents;      
                window.print();
                document.body.innerHTML = originalContents;
            }
        </script>
           
        <!-- viewReservation -->
        <div class="remodal" data-remodal-id="viewReservation" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
            <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
             
            <!-- table for reservation information -->
            <div class="table-responsive cart_info">
                <table class="table table-condensed">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>">
                                <button type="button" class="btn btn-info">Back</button>
                            </a> 
                        </td>
                        
                        <td>
                            <a href="${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>#changeSchedule">
                                <button type="button" id="btnChangeSchedule" class="btn btn-success">Change Schedule</button>
                            </a> 
                        </td>
                        
                        <td>
                            <!-- frmChangeSeat -->
                            <form id="frmChangeSeat" method="POST" action="${pageContext.request.contextPath}/ChangeSeat">
                                <button type="submit" id="btnChangeSeat" class="btn btn-warning">Change Seat</button>
                            </form>
                                
                            <!-- frmChangeSeat javascript -->
                            <script type="text/javascript">
                                var typeOfSeating = "<%= (String)request.getSession().getAttribute("reservation_typeOfSeating") %>";
                                var btnChangeSeat = $('#btnChangeSeat');
                                
                                if(typeOfSeating == "Free Seating"){
                                    btnChangeSeat.css("display", "none");
                                }
                                
                                submitInformationAboutChangeSeat();
                                
                                function submitInformationAboutChangeSeat(){
                                    $('#frmChangeSeat').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);

                                        swal(
                                        {
                                            title: "Are you sure?",
                                            text: "You cannot undo this action.",
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
                                                success: function( data, textStatus, jqXHR) {
                                                    if(data.isChangeSeatSuccess == "preferredSeating"){
                                                        location.replace("${pageContext.request.contextPath}/pages/USER/changeSeatForCustomers.jsp");
                                                    }else if(data.isChangeSeatSuccess == "freeSeating"){
                                                        swal("Oops! We're sorry!", "You are not allowed to change your seat unless the movie is a Preferred Seating. Thankyou!", "error");
                                                    }else if(data.isChangeSeatSuccess == "accessDenied"){
                                                        swal("Oops! We're sorry!", "Changing of seats or schedule will only be possible if it is NOT paid.\n\
                                                            If paid, the E-Ticket will be considered final and conclusive. Thank you!", "error");
                                                    }
                                                        
                                                },
                                                error: function(jqXHR, textStatus, errorThrown){
                                                    //alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                    console.log('Error: ' + textStatus + ' - ' + errorThrown);
                                                }
                                            });
                                        });
                                    });
                                }
                                    
                                function successfullyChangeSeatMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully change your seat!",
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
                        
                        <td>
                            <!-- frmCancelReservation -->
                            <form id="frmCancelReservation" method="POST" action="${pageContext.request.contextPath}/CancelReservationForUsers?user_emailAddress=<%=user_emailAddress %>&reference_number=<%= (String)request.getSession().getAttribute("reservation_movieReferenceNumber") %>">
                                <button type="submit" id="btnCancelReservation" class="btn btn-danger">Cancel Reservation</button>
                            </form>
                                
                            <!-- frmCancelReservation javascript -->
                            <script type="text/javascript">                  
                                var status = "<%= (String)request.getSession().getAttribute("reservation_status") %>";
                                var btnChangeSchedule = $('#btnChangeSchedule');
                                var btnChangeSeat = $('#btnChangeSeat');
                                var btnCancelReservation = $('#btnCancelReservation');

                                if(status == "Cancelled"){
                                    btnChangeSchedule.css("display", "none");
                                    btnChangeSeat.css("display", "none");
                                    btnCancelReservation.css("display", "none");
                                }else if(status == "Paid"){
                                    btnChangeSchedule.css("display", "none");
                                    btnChangeSeat.css("display", "none");
                                }
                                
                                submitInformationAboutCancelReservation();
                                
                                function submitInformationAboutCancelReservation(){
                                    $('#frmCancelReservation').submit(function(event) {
                                        event.preventDefault();
                                        var formEl = $(this);

                                        swal(
                                        {
                                            title: "Are you sure?",
                                            text: "You will not be able to recover this reservation!\n\
                                                Please take note that cancelling of reservation thrice may result \n\
                                                to account suspension. Thankyou!",
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
                                                        if(data.isCancelReservationForUserSuccess){
                                                            successfullyCancelReservationMessage();
                                                        }else{
                                                            swal("Invalid!", "You cannot be able to cancel reservation for now. Please try again!", "error");
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
                                    
                                function successfullyCancelReservationMessage() {
                                    swal(
                                        {
                                            title: "Successful!",
                                            text: "You've successfully cancel your reservation!",
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
            </div>
            
            <br><br>
            
            <!-- reservationsDetails -->
            <div id="reservationDetails" class="table-responsive cart_info">
                <table class="table table-condensed" border="1">
                    <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                        <td colspan="2">Reservation Details</td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Reference Number</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieReferenceNumber") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Movie Name</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieName") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Branch</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieBranchName") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Auditorium</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Type</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_typeOfSeating") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Screening Date & Time</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieSchedule") %></td>
                    </tr>

                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Ticket Price</td>
                        <td style="background-color: #0277BD; color: white;">&#8369;<%= (String)request.getSession().getAttribute("reservation_movieAmount") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Quantity</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_movieQuantity") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Total Amount</td>
                        <td style="background-color: #0277BD; color: white;"><p id="txtReservation_totalAmount"></p></td>
                    </tr>
                                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Seat (s)</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_occupiedSeats") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Status</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_status") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Mode Of Payment</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_modeOfPayment") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Payment Due</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_paymentDue") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Transaction Date</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("reservation_transactionDate") %></td>
                    </tr>
                    
                    <tr>
                        <td style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">Reserved By</td>
                        <td style="background-color: #0277BD; color: white;"><%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %> <%= (String)request.getSession().getAttribute("COOKIE_USER_LASTNAME") %></td>
                    </tr>
                </table>
                    
                
                
                <div id="editor"></div>            
            </div>
                
            <!-- e-ticket -->
            <div id="eTicket" align="center">
                <table class="table table-condensed" border="1" style="background-color: white; width: 70%;">
                    <tr style="text-align: center;">
                        <td colspan="2"><img src="../../Resources/images/home/logo.jpg" width=150px;" height="80px;"/></td>
                    </tr>

                    <tr style="text-align: left; font-family: 'Roboto', sans-serif; font-weight: bold; font-size: 15px;">
                        <td>SM PRIME HOLDINGS, INC.<br>SM City Sucat Paranaque<br>Dr. Santos Ave. corner Carlos P. Garcia Avenue Extension, San Dionisio, Parañaque City, Philippines</td>
                    </tr>

                    <tr style="text-align: right; font-family: 'Roboto', sans-serif; font-weight: bold; font-size: 15px;">
                        <td colspan="2"><div style="border: solid; font-size: 18px; display: inline;"><%= (String)request.getSession().getAttribute("reservation_occupiedSeats") %></div><br>SEAT NO.</td>
                    </tr>

                    <tr style="text-align: center; font-family: 'Roboto', sans-serif; font-weight: bold; font-size: 15px;">
                        <td>
                            Reference Number: 
                            <div style="border: solid; font-size: 18px; display: inline;">
                                <%= (String)request.getSession().getAttribute("reservation_movieReferenceNumber") %>
                            </div>
                        </td>
                    </tr>

                    <tr style="text-align: left; font-family: 'Roboto', sans-serif; font-weight: bold; font-size: 18px;">
                        <td>
                            <%= (String)request.getSession().getAttribute("reservation_movieName") %> 
                            (&#8369;<%= (String)request.getSession().getAttribute("reservation_movieAmount") %>) |
                            Qty: <%= (String)request.getSession().getAttribute("reservation_movieQuantity") %><br>
                            <%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %> |
                            <%= (String)request.getSession().getAttribute("reservation_typeOfSeating") %><br>
                            <font style="text-decoration: underline; font-size: 14px;">
                                <%= (String)request.getSession().getAttribute("reservation_movieSchedule") %>
                            </font>
                        </td>
                    </tr>

                    <tr style="text-align: center; font-family: 'Roboto', sans-serif; font-weight: bold; font-size: 18px;">
                        <td><p id="txtReservation_totalAmount_2">Total Amount: </p></td>
                    </tr>

                </table>
            </div>
                           
            <button type="button" id="btnPrintE_Ticket" class="btn btn-warning" onClick="printE_Ticket('reservationDetails');">Print E-Ticket</button>
            <button type="button" id="btnSaveAsPDF" class="btn btn-danger">Save As PDF</button>
            
            <!-- detector if status of reservation is cancelled -->
            <script type="text/javascript">                  
                var status = "<%= (String)request.getSession().getAttribute("reservation_status") %>";
                var btnPrintE_Ticket = $('#btnPrintE_Ticket');
                var btnSaveAsPDF = $('#btnSaveAsPDF');

                if(status == "Cancelled"){
                    btnPrintE_Ticket.css("display", "none");
                    btnSaveAsPDF.css("display", "none");
                }
            </script>
            
            <!-- computation for total amount -->
            <script type="text/javascript">
                $(document).ready(function() {
                    var moviePrice,reservation_movieQuantity,totalTicketPrice;
                    moviePrice = <%= (String)request.getSession().getAttribute("reservation_movieAmount") %>;
                    reservation_movieQuantity = <%= (String)request.getSession().getAttribute("reservation_movieQuantity") %>;

                    totalTicketPrice = ((moviePrice * reservation_movieQuantity) + (20 * reservation_movieQuantity));
                    $('#txtReservation_totalAmount').append("&#8369;" + totalTicketPrice + ".00");
                    $('#txtReservation_totalAmount_2').append("&#8369;" + totalTicketPrice + ".00");
                });
            </script>
        </div>
            
        <!-- changeSchedule -->
        <div class="remodal" data-remodal-id="changeSchedule" role="dialog" aria-labelledby="modal1Title" aria-describedby="modal1Desc">
            <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
            
            <hr width="100%" style="border: groove;">
            <h2 id="modal1Title" style="font-family: Arial Black; color: orange;">
                Change Schedule
            </h2>
            <hr width="100%" style="border: groove;">
            
            <!-- frmChangeSchedule -->
            <form id="frmChangeSchedule" method="POST" action="${pageContext.request.contextPath}/ChangeSchedule?user_emailAddress=<%=user_emailAddress %>">
                <!-- change schedule javascript -->
                <script>
                    $(document).ready(function() {
                        $.get('${pageContext.servletContext.contextPath}/GetMovieAvailableDay', {
                            movieName : "<%= (String)request.getSession().getAttribute("reservation_movieName") %>",
                            movieBranchName : "<%= (String)request.getSession().getAttribute("reservation_movieBranchName") %>",
                            movieAuditorium: "<%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %>"
                        }, 
                        function(response) {
                            var selectMovieAvailableDay = $('#txtSelectMovieAvailableDay');
                            $.each(response, function(index, value) {
                                $('<option>').val(value).text(value).appendTo(selectMovieAvailableDay);
                            });
                        });

                        $.get('${pageContext.servletContext.contextPath}/GetMovieSchedule', {
                            movieName : "<%= (String)request.getSession().getAttribute("reservation_movieName") %>",
                            movieBranchName : "<%= (String)request.getSession().getAttribute("reservation_movieBranchName") %>",
                            movieAuditorium: "<%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %>"
                        }, 
                        function(response) {
                            var selectMovieSchedule = $('#txtSelectMovieSchedule');
                            $.each(response, function(index, value) {
                                $('<option>').val(value).text(value).appendTo(selectMovieSchedule);
                            });
                        });
                    });
                </script> 
                
                <!-- table for screening date and time -->
                <div class="table-responsive cart_info">
                    <table class="table table-condensed" border="1" style="font-size: 16px;">
                        <tr style="background-color: #199ac1; color: white;">
                            <td>Screening Date</td>
                            <td>Screening Time</td>
                        </tr>
                        
                        <tr style="background-color: orange; color: white;">
                            <td><%= (String)request.getSession().getAttribute("reservation_screeningDate") %> </td>
                            <td><%= (String)request.getSession().getAttribute("reservation_screeningTime") %> </td>
                        </tr>
                    </table>
                </div>
                         
                <select id="txtSelectMovieAvailableDay" name="txtSelectMovieAvailableDay" required="required">
                    <option disabled selected>---SELECT AVAILABLE DAY---</option>
                </select>

                <select id="txtSelectMovieSchedule" name="txtSelectMovieScheduleTime" required="required">
                    <option disabled selected>---SELECT SCHEDULE---</option>
                </select>
                
                <br><br>
                
                <a href="${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>">
                    <button type="button" class="btn btn-info">Close</button>
                </a>
                
                <button type="submit" class="btn btn-danger">Save Changes</button>
            </form>
                   
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
            <script type="text/javascript">
                $("#txtSelectMovieAvailableDay option").val(function(idx, val) {
                    $(this).siblings("[value='"+ val +"']").remove();
                });

                $("#txtSelectMovieSchedule option").val(function(idx, val) {
                    $(this).siblings("[value='"+ val +"']").remove();
                });
            </script>        
                    
            <!-- frmChangeSchedule submit form javascript -->
            <script type="text/javascript">
                submitInformationAboutChangingSchedule();
                
                function submitInformationAboutChangingSchedule(){
                    $('#frmChangeSchedule').submit(function(event) {
                        event.preventDefault();
                        var formEl = $(this);
                        
                        swal(
                        {
                            title: "Are you sure?",
                            text: "You will not be able to recover this schedule!",
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
                                success: function( data, textStatus, jqXHR) {
                                    if(data.isChangeScheduleSuccess == "accessDenied"){
                                        swal("Invalid!", "Changing of seats or schedule will only be possible if it is not paid.\n\
                                            If paid, the E-Ticket will be considered final and conclusive. Thankyou!", "error");
                                    }else{
                                        successfullyUpdateScheduleMessage();
                                    }
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    alert('Error: ' + textStatus + ' - ' + errorThrown);
                                }
                            });
                        });      
                    });
                }
                  
                function successfullyUpdateScheduleMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "You've successfully changed your schedule!",
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
        </div>
                    
        <!--tabs-->
        <div class="main">
            <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
  
            <input id="tab1" type="radio" name="tabs" checked>
            <label for="tab1">Profile</label>
	
            <input id="tab2" type="radio" name="tabs">
            <label for="tab2">Change Password</label>
	
            <input id="tab3" type="radio" name="tabs">
            <label for="tab3">Reservation History</label>
            
            <!-- content -->
            <div class="content">  
                <!--Profile-->
		<div id="content1">
                    <!-- frmUpdateUserInfo -->
                    <form id="frmUpdateUserInfo" method="post" action="${pageContext.request.contextPath}/UpdateUserInformation">
                        <div class="table-responsive cart_info">
                            <table class="table table-condensed" style="font-family: 'Roboto', sans-serif; text-align: left;">      
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Firstname:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %></b></font></td>
                                    <td><input type="text" name="txtFirstname" id="txtUSER_firstname" required="required" placeholder="Enter your firstname" maxlength="15" oncopy="return false" onpaste="return false"/></td>
                                </tr>
                                        
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Lastname:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_LASTNAME") %></b></font></td>
                                    <td><input type="text" name="txtLastname" id="txtUSER_lastname" required="required" placeholder="Enter your lastname" maxlength="20" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                            
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Email address:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS") %></b></font></td>
                                </tr>
                                            
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Name of favorite pet:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_NAMEOFFAVORITEPET") %></b></font></td>
                                    <td><input type="text" name="txtNameOfFavoritePet" id="txtUSER_nameOfFavoritePet" required="required" placeholder="Enter name of favorite pet" maxlength="15" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr style="text-align: center;">
                                    <td colspan="5">
                                        <a href="${pageContext.request.contextPath}/pages/USER/home.jsp">
                                            <button type="button" class="button">Back</button>
                                        </a>
                                            
                                        <button type="submit" class="button">Save</button>
                                    </td>
                                </tr>
                            </table>
                        </div>         
                    </form>  
                      
                    <!-- frmUpdateUserInfo javascript -->
                    <script type="text/javascript">
                        $('#txtUSER_firstname').keypress(function (e) {
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

                        $('#txtUSER_lastname').keypress(function (e) {
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
                        
                        $('#txtUSER_nameOfFavoritePet').keypress(function (e) {
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

                        $('#frmUpdateUserInfo').submit(function(event) {
                          event.preventDefault();

                          var formEl = $(this);

                          $.ajax({
                            type: 'POST',
                            dataType : 'html',
                            url: formEl.prop('action'),
                            data: formEl.serialize(),
                            //if received a response from the server
                            success: function( data, textStatus, jqXHR) {
                                showSuccessMessage();
                            },
                            error: function(jqXHR, textStatus, errorThrown){
                                alert('Error: ' + textStatus + ' - ' + errorThrown);
                            }
                          });
                        });
                        
                        function showSuccessMessage() {
                            swal(
                                {
                                    title: "Successful!",
                                    text: "You've successfully updated your information!",
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
                </div>
                
                <!--Change Password-->
                <div id="content2">
                    <!-- frmChangeUserPassword -->
                    <form id="frmChangeUserPassword" method="post" action="${pageContext.request.contextPath}/ChangeUserPassword?user_emailAddress=<%=user_emailAddress %>">
                        <div class="table-responsive cart_info">
                            <table class="table table-condensed" style="font-family: 'Roboto', sans-serif; text-align: center;">
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Old Password:</b></font></td>
                                    <td><input type="password" name="txtUSER_oldPassword" id="txtUSER_oldPassword" placeholder="Old Password" required="required" maxlength="16" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr>
                                    <td><font style="font-size: 16px;"><b>New Password:</b></font></td>
                                    <td><input type="password" name="txtUSER_newPassword" id="txtUSER_newPassword" placeholder="New Password" required="required" maxlength="16" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr style="text-align: center;">
                                    <td colspan="2">
                                        <a href="${pageContext.request.contextPath}/pages/USER/home.jsp">
                                            <button type="button" class="button">Back</button>
                                        </a>
                                        &nbsp;&nbsp;&nbsp;  
                                        <button type="submit" class="button">Save</button>
                                        &nbsp;&nbsp;&nbsp;
                                        <button type="reset" class="button">Reset</button>
                                    </td>
                                </tr>
                            </table>
                        </div>         
                    </form>  
                        
                    <!-- frmChangeUserPassword javascript -->
                    <script type="text/javascript">
                        $('#txtUSER_oldPassword').keypress(function (e) {
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
                        
                        $('#txtUSER_newPassword').keypress(function (e) {
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
                        
                        showAreYouSureYouWantToChangeUserPasswordMessage();
                        
                        function showAreYouSureYouWantToChangeUserPasswordMessage(){
                            $('#frmChangeUserPassword').submit(function(event) {
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
                                            if(data.isSuccess){
                                                showSuccessUpdatePasswordMessage();
                                            }else{
                                                showErrorUpdatePasswordMessage();
                                            }
                                        },
                                        error: function(jqXHR, textStatus, errorThrown){
                                            alert('Error: ' + textStatus + ' - ' + errorThrown);
                                        }
                                    }); 
                                });    
                            });
                        }
                        
                        function showSuccessUpdatePasswordMessage() {
                            swal(
                                {
                                    title: "Successful!",
                                    text: "You've successfully changed your password!",
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
                        
                        function showErrorUpdatePasswordMessage() {
                            swal(
                                {
                                    title: "Invalid!",
                                    text: "Old password doesn't match in the database! Please try again!",
                                    type: "error",
                                    showCancelButton: false,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Close",
                                    closeOnConfirm: true 
                                });
                        }
                    </script>
		</div>
	
                <!--Reservation History-->
		<div id="content3">
                    <script type="text/javascript">
                    $.ajax({
                        url: '${pageContext.servletContext.contextPath}/ShowReservationHistory?user_emailAddress=<%=user_emailAddress %>',
                        dataType: 'json',
                        success: function(data) {
                            for (var i=0; i<data.RESERVATIONSREPORT.length; i++) {
                                var row = $('<tr style="color: black; font-size: 16px; text-align: center;">\n\
                                <td>' + data.RESERVATIONSREPORT[i].reservation_movieReferenceNumber + '</td>\n\
                                <td>' + data.RESERVATIONSREPORT[i].reservation_transactionDate + '</td>\n\
                                <td>' + data.RESERVATIONSREPORT[i].reservation_status + '</td>\n\
                                <td><a href="${pageContext.servletContext.contextPath}/ShowDetailsOfSelectedReservation?user_emailAddress=<%=user_emailAddress %>&reference_number=' + data.RESERVATIONSREPORT[i].reservation_movieReferenceNumber + '#viewReservation"><button type="button" class="btn btn-warning">View</button></a></td></tr>');
                                $('#TBLRESERVATIONHISTORY').append(row);
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                    </script>
                                       
                    <div class="table-responsive cart_info">
                        <table id="TBLRESERVATIONHISTORY" class="table table-condensed" style="font-family: 'Roboto', sans-serif; text-align: center;">
                            <thead>
                                <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                    <td>Reference Number</td>
                                    <td>Transaction Date</td>
                                    <td>Status</td>
                                    <td>Action</td>
                                </tr>
                            </thead>
                        </table>
                    </div>
		</div>
            </div>
        </div>
    
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

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="../../Resources/Remodal/remodal.min.js"><\/script>');</script>
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

        <script src="../../Resources/js/jquery.js"></script>
        <script src="../../Resources/js/bootstrap.min.js"></script>
        <script src="../../Resources/js/jquery.scrollUp.min.js"></script>
        <script src="../../Resources/js/jquery.prettyPhoto.js"></script>
        <script src="../../Resources/js/main.js"></script>
    </body>
</html>
