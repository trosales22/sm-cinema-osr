<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!-- head -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
        <meta name="keywords" content="Movie Ticket Booking Widget Responsive, Login form web template, Sign up Web Templates, Flat Web Templates, Login signup Responsive web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
        <!-- //for-mobile-apps -->
        <link href='//fonts.googleapis.com/css?family=Kotta+One' rel='stylesheet' type='text/css'>
        <link href='//fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
        <link href="../../Resources/movie_ticket_booking_widget/web/css/style.css" rel="stylesheet" type="text/css" media="all" />
        <script src="../../Resources/movie_ticket_booking_widget/web/js/jquery-1.11.0.min.js"></script>
        <script src="../../Resources/movie_ticket_booking_widget/web/js/jquery.seat-charts.js"></script>
        <script src="../../Resources/js/sweetalert.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
        
        <title><%= (String)request.getSession().getAttribute("reservation_movieName") %> | Change Seat</title>
    </head>
    
    <!-- body -->
    <body>
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
        
        <div class="content">
	<h1>Movie Ticket Booking Widget</h1>
            
        <!-- frmUpdateSeatOfUserInReservation -->
        <form id="frmUpdateSeatOfUserInReservation" method="POST" action="${pageContext.request.contextPath}/UpdateSeatOfUserInReservation" accept-charset="UTF-8">
	<div class="main">
		<h2><%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %> Showing Screen</h2>
                <h2><%= (String)request.getSession().getAttribute("reservation_movieName") %></h2>
		<div class="demo">
			<div id="seat-map">
				<div class="front">SCREEN</div>					
			</div>
			<div class="booking-details">
                                <ul class="book-left">
                                        <li>Auditorium</li>
                                        <li>Branch</li>
                                        <li>Schedule</li>       
                                        <br>
					<li>Quantity</li>
					<li>Seats :</li>
				</ul>
				<ul class="book-right">
					<li>: <%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %></li>
					<li>: <%= (String)request.getSession().getAttribute("reservation_movieBranchName") %></li>
                                        <li>: <%= (String)request.getSession().getAttribute("reservation_movieSchedule") %></li>
                                        
                                        <li>: <%= (String)request.getSession().getAttribute("reservation_movieQuantity") %></li>
                                        <li>: <span id="counter" style="display: none;">0</span></li>
				</ul>
				<div class="clear"></div>
				<ul id="selected-seats" class="scrollbar scrollbar1"></ul>
                                <ul id="occupied-seats" class="scrollbar scrollbar1"></ul>
                                
                                
                                <ul id="selected-seats_improvedName" class="scrollbar scrollbar1"></ul>
                                
                                <!--
                                <input type="text" id="txtCounter" name="txtCounter">
                                -->
                                
                                <input type="hidden" id="txtBlockedSeats" name="txtBlockedSeats">
                                
                                <input type="hidden" id="txtBlockedSeats_improvedName" name="txtBlockedSeats_improvedName">
                                
                                <a href="${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>">
                                    <button type="button" class="checkout-button">Back</button>
                                </a>
                                    
				<button type="submit" class="checkout-button">Change Seat</button>	
				<div id="legend"></div>
			</div>
			<div style="clear:both"></div>
	    </div>
                   
            <!-- seat charts -->
            <script type="text/javascript">
		var price = <%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>; //price
                $(document).ready(function() {   
                    var $cart = $('#selected-seats'), //Sitting Area
                    $selectedSeats_improved = $('#selected-seats_improvedName'),
                    $counter = $('#counter'), //Votes
                    $total = $('#total'); //Total money
					
                    var sc = $('#seat-map').seatCharts({
			map: [  //Seating chart
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            '__________','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa',
                            'aaaaaaaaaa','aaaaaaaaaa'        
			],
			naming : {
                            top : false,
                            getLabel : function (character, row, column) {
                                return column;
                            }
			},
			legend : { //Definition legend
                            node : $('#legend'),
                            items : [
                                [ 'a', 'available', 'Available' ],
                                [ 'a', 'unavailable', 'Sold'],
                                [ 'a', 'selected', 'Selected']
                            ]			
			},
                        click: function () { //Click event     
                            if (this.status() == 'available') { //optional seat
				//$('<li>Row'+(this.settings.row+1)+' Seat'+this.settings.label+'</li>')
         
                                $('<li>' + (this.settings.row+1) + '_' + this.settings.label + '</li>')
                                    .attr('id', 'cart-item-'+this.settings.id)
                                    .data('seatId', this.settings.id)
                                    .appendTo($cart);
                                    
                                $('<li>Row'+(this.settings.row+1)+' Seat'+this.settings.label+'</li>')
                                    .attr('id', 'cart-item1-'+this.settings.id)
                                    .data('seatId', this.settings.id)
                                    .appendTo($selectedSeats_improved);    
                                
                                    
                                
                                $counter.text(sc.find('selected').length+1);
                                
                                if($counter.text() > <%= (String)request.getSession().getAttribute("reservation_movieQuantity") %>){
                                    swal("Oops! We're sorry!", "Please make sure you have the same ticket quantity and seat selection(s). Thank you!", "error");
                                    $counter.text(sc.find('selected').length-1);
                                    
                                    //Delete reservation
                                    $('#cart-item-'+this.settings.id).remove();
                                    $('#cart-item1-'+this.settings.id).remove();
                                    
                                    return 'available';
                                }
                                
                                //$('#txtCounter').val(sc.find('selected').length+1);
                                $total.text(recalculateTotal(sc)+price);
							
                                var x = document.getElementById("selected-seats").getElementsByTagName("li");
                                var y = document.getElementById("selected-seats_improvedName").getElementsByTagName("li");
                                
                                var values = [];
                                for(var i=0;i < x.length; i++) {
                                    if (x[i].innerText.length) { 
                                        values.push((x[i].innerText.replace(/\s/g,''))); 
                                    }
                                }
                                
                                var values1 = [];
                                for(var i=0;i < y.length; i++) {
                                    if (y[i].innerText.length) { 
                                        values1.push((y[i].innerText.replace(/\s/g,''))); 
                                    }
                                }
                                
                                //alert(values.join(","));
                                //console.log(values.join(","));    
                                
                                $('#txtBlockedSeats').val(values.join(","));
                                $('#txtBlockedSeats_improvedName').val(values1.join(","));
                                
				return 'selected';
                            } else if (this.status() == 'selected') { //Checked
				//Update Number
				$counter.text(sc.find('selected').length-1);
				//update totalnum
				$total.text(recalculateTotal(sc)-price);
										
				//Delete reservation
				$('#cart-item-'+this.settings.id).remove();
                                $('#cart-item1-'+this.settings.id).remove();
				//optional
				return 'available';
                            } else if (this.status() == 'unavailable') { //sold
				return 'unavailable';
                            } else {
				return this.style();
                            }
			}
                    });
                                      
                    //blocked seat
                    $.ajax({
                        url: '${pageContext.request.contextPath}/GetAllTheBlockedSeats?ps_auditoriumName=<%= (String)request.getSession().getAttribute("reservation_movieAuditoriumName") %>',
                        success: function(data) {
                            sc.get(data).status('unavailable');
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                    
                    //reserved seats
                    $.ajax({
                        url: '${pageContext.request.contextPath}/GetAllTheReservedSeats?ps_auditoriumName=<%= (String)request.getSession().getAttribute("selected_movieAuditorium") %>',
                        success: function(data) {
                            sc.get(data).status('unavailable');
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            //alert('Error: ' + textStatus + ' - ' + errorThrown);
                            console.log('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                    
                    //sc.get(['1_2', '4_4','4_5','6_6','6_7','8_5','8_6','8_7','8_8', '10_1', '10_2']).status('unavailable');				
		});
                
		//sum total money
		function recalculateTotal(sc) {
                    var total = 0;
                    sc.find('selected').each(function () {
                        total += price;
                    });
							
                    return total;
		}
            </script>
	</div>
        </form>
                        
        <!-- frmUpdateSeatOfUserInReservation javascript -->
        <script type="text/javascript">
            showAreYouSureYouWantToUpdateSeatOfUserInReservationMessage();

            function showAreYouSureYouWantToUpdateSeatOfUserInReservationMessage() {
                $("#frmUpdateSeatOfUserInReservation").submit(function(event){ 
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
                                if(data.isUpdateSeatOfUserInReservationSuccess == "exceeds"){
                                    swal("Oops!", "Selected Seats exceeds Reservation Seat Quantity. Please try again!", "error");
                                }else{
                                    showUpdateSeatOfUserInReservationSuccessMessage();
                                }
                            },
                            error: function(jqXHR, textStatus, errorThrown){
                                alert('Error: ' + textStatus + ' - ' + errorThrown);
                            }
                        });
                    });
                });  
            }

            function showUpdateSeatOfUserInReservationSuccessMessage() {
                swal(
                    {
                        title: "Successful!",
                        text: "You've successfully changed your seats! Thank you!",
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
                                           
	<p class="copy_rights">&copy; 2017 Movie Ticket Booking Widget. All Rights Reserved | Design by  <a href="http://w3layouts.com/" target="_blank"> W3layouts</a></p>
        </div>
                  
        <script src="../../Resources/movie_ticket_booking_widget/web/js/jquery.nicescroll.js"></script>
        <script src="../../Resources/movie_ticket_booking_widget/web/js/scripts.js"></script>
    </body>
</html>
