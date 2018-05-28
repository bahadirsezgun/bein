ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$filter,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	$scope.disabled=true;
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSales;
	
	$scope.infoSection=true;
	$scope.saleSection=false;
	$scope.freezeSection=false;
	
	$scope.lineShowNo=0;
	
	
	
	$scope.userType;
	$scope.staffs;
	
	$scope.progType="0";
	$scope.progId="0";
	
	
	$scope.programSelected=false;
	$scope.newSaleFlag=true;
	
	$scope.membershipFreezePage="";
	$scope.packetPaymentPage="";
	
	$scope.psf;
	
	
	$scope.infoSection=true;
	
	$scope.showDetail=function(saleId){
		if($scope.lineShowNo==saleId){
			$scope.lineShowNo=0;
		}else{
			$scope.lineShowNo=saleId;
		}
	}
	
	
	$scope.showDetailIn=function(packetSale){
		$scope.psf=packetSale;
		$scope.psf.progId=""+$scope.psf.progId;
		$scope.psf.staffId=""+$scope.psf.staffId;
		$scope.progType=$scope.psf.programFactory.type;
		$scope.psf.smpStartDate=new Date($scope.psf.smpStartDate);
		
	}
	
	$scope.init=function(){
		
		$("#barOptions").attr("width",$("#cnvPanel").width())
		
		$scope.userId=$routeParams.userId;
		
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetSaleUserProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		$scope.membershipFreezePage="/bein/booking/membership/booking.html";
		$scope.packetPaymentPage="/bein/packetpayment/payment.html";
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.getMember($routeParams.userId);
			$scope.getMemberPacketSale($routeParams.userId);
		});
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
		
		commonService.getUser().then(function(user){
			$scope.userType=user.userType;
			if($scope.userType==6){
				$scope.disabled=false;
			}
				
		});
		
		
		$scope.psf=new Object();
		$scope.psf.saleId=0;
		$scope.psf.progType="0";
		$scope.psf.staffId="0";
		$scope.psf.progId="0";
		$scope.psf.salesDate=new Date();
		$scope.psf.smpStartDate=new Date();
		$scope.psf.salesComment="";
		$scope.psf.bonusPayedFlag=0;
		$scope.psf.progCount=0;
		$scope.psf.saleStatu=0;
		$scope.psf.packetPrice=0;
		$scope.psf.userId=$scope.userId;
	}
	
	$scope.sportPageShow=false;
	
	$scope.addSportProgram=function(packetSale){
		$scope.packetSale=packetSale;
		$scope.sportPage="/bein/sport/addSportProgram.html";
		 $('#sportPage').modal('show');
		
		
	}
	
	
	$scope.sendMailToMember=function(packetSale){
		$scope.packetSale=packetSale;
		setTimeout(function(){
			
			var mailObj=new Object();
			mailObj.toPerson=$scope.member.userEmail;
			mailObj.subject=$translate.instant("weeklyBooking");
			mailObj.content="";//$scope.sendMailPerson+", "+$scope.mailContent;
			mailObj.htmlContent=$("#mailTable").html();
			
			$http({
				method:'POST',
				  url: "/bein/mail/sendPlanningMail",
				  data:angular.toJson(mailObj),
				}).then(function successCallback(response) {
					toastr.success($translate.instant("success"));
					
			}, function errorCallback(response) {
				toastr.error($translate.instant("error"));
			});
		},500)
		
		
		
	}
	
	
	$scope.getMember=function(userId){
		$http({method:"POST", url:"/bein/member/findById/"+userId}).then(function successCallback(response){
			$scope.member=response.data.resultObj;
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}
	
	$scope.getMemberPacketSale=function(userId){
		return $http({method:"POST", url:"/bein/packetsale/findUserBoughtPackets/"+userId}).then(function successCallback(response){
			$scope.packetSales=response.data;
			
			calculatePacket();
			getDataToGraph();
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}
	
	
	
	$scope.changePrice=function(){
		if($scope.disabled){
			$scope.disabled=false;
		}else{
			
			if($scope.psf.saleId>0){
			
			$http({method:"POST"
				   , url:"/bein/packetsale/sale/changePrice"
				   ,data:angular.toJson($scope.psf)
				   }).then(function successCallback(response){
					   
					   var res=response.data;
					   if(res.resultStatu=="success"){
						   toastr.success($translate.instant(res.resultMessage));
						   $scope.getMemberPacketSale($scope.userId);
					   }else{
						   toastr.error($translate.instant(res.resultMessage)); 
					   }
					}, function errorCallback(response) {
						$location.path("/login");
					});
		
			}else{
				toastr.error($translate.instant("pleaseSaveSaleFirst"));
			}
		}
	}
	
	$scope.saleIt=function(){
		
		if(controlSaleAttributes()){
			$http({method:"POST"
				   , url:"/bein/packetsale/sale"
				   ,data:angular.toJson($scope.psf)
				   }).then(function successCallback(response){
					   
					   var res=response.data;
					   if(res.resultStatu=="success"){
						   toastr.success($translate.instant(res.resultMessage));
						   $scope.getMemberPacketSale($scope.userId);
					   }else{
						   toastr.error($translate.instant(res.resultMessage)); 
					   }
					}, function errorCallback(response) {
						$location.path("/login");
					});
		}
		
	}
	
	

	
	
	$scope.deletePS=function(packetSale){
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStateComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: false },
        function (isConfirm) {
            if (isConfirm) {
            	$http({method:"POST"
            		, url:"/bein/packetsale/deletePacketSale"
            		,data:angular.toJson(packetSale)})
            		.then(function successCallback(response){
	        			
            			if(response.data.resultStatu==globals.RESULT_SUCCESS_STR){
            				toastr.success($translate.instant(response.data.resultMessage));
            				$scope.getMemberPacketSale($scope.userId);
            			}else{
            				toastr.error($translate.instant(response.data.resultMessage));
            			}
            		}, function errorCallback(response) {
        				$location.path("/login");
        			});
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
	}
	
	
	$scope.editPS=function(packetSale){
		
		findInstructors().then(function(staffs){
			$scope.staffs=staffs;
			$scope.psf=packetSale;
			$scope.psf.salesDate=new Date($scope.psf.salesDate);
			if(packetSale.progType=="psp")
			  $scope.progType="pp";
			else if(packetSale.progType=="psc")
				  $scope.progType="pc";
			else if(packetSale.progType=="psm")
				  $scope.progType="pm";
			
			
			$scope.newSaleFlag=false;
			
			
			$scope.psf.progId=""+$scope.psf.progId;
			$scope.psf.staffId=""+$scope.psf.staffId;
			
			$scope.psf.smpStartDate=new Date($scope.psf.smpStartDate);
			
			$scope.infoSection=false;
			$scope.saleSection=true;
			$scope.freezeSection=false;
			$scope.paymentSection=false;
			
			
			$scope.programSelected=true;
		});
		
	}
	
	$scope.paymentPS=function(packetSale){
		//$scope.psf=packetSale;
		$scope.$broadcast("payToPacket",packetSale);
		$scope.infoSection=false;
		$scope.saleSection=false;
		$scope.paymentSection=true;
		$scope.freezeSection=false;
	}
		
	$scope.freezePS=function(packetSale){
		//$scope.psf=packetSale;
		$scope.$broadcast("freezeToPacket",packetSale);
		
		$scope.infoSection=false;
		$scope.saleSection=false;
		$scope.freezeSection=true;
		$scope.paymentSection=false;
		
		
		
	}
	
	
	$scope.cancelUserInTimePlan=function (scutp){
		
		 swal({
	            title: $translate.instant("areYouSureToCancel"),
	            text: $translate.instant("cancelUserInTimePlanComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yesDelete"),
	            cancelButtonText: $translate.instant("noDelete"),
	            closeOnConfirm: true,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
		 
		 
					 $http({
						method:'POST',
						url: "/bein/private/booking/cancelUserInTimePlan",
						data:angular.toJson(scutp)
					}).then(function successCallback(response) {
						var res=response.data;
						if(res.resultStatu=="success"){
							toastr.success($translate.instant(res.resultMessage));
							$scope.getMemberPacketSale($scope.userId);
						}else{
							toastr.fail($translate.instant(res.resultMessage));
						}
					}, function errorCallback(response) {
						$location.path("/login");
					});
					 
	            }
	            });
		
		
	}
	
	
	function controlSaleAttributes(){
		if($scope.psf.progId=="0"){
			toastr.error($translate.instant("noProgramSelectedForSale"));
			return false;
		}
		
		if($scope.progType=="0"){
			toastr.error($translate.instant("noProgramSelectedForSale"));
			return false;
		}
		
		if($scope.progCount=="0" || $scope.progCount=="" ){
			toastr.error($translate.instant("participationMoreThanZero"));
			return false;
		}
		
		return true;
	}
	
	
	$scope.saleNewPacket=function(){
		$scope.progType="0";
		$scope.programSelected=false;
		$scope.psf.saleId=0;
		$scope.psf.progType="0";
		$scope.psf.staffId="0";
		$scope.psf.progId="0";
		$scope.psf.salesDate=new Date();
		$scope.psf.smpStartDate=new Date();
		$scope.psf.salesComment="";
		$scope.psf.bonusPayedFlag=0;
		$scope.psf.progCount=0;
		$scope.psf.saleStatu=0;
		$scope.psf.packetPrice=0;
		
		findInstructors().then(function(staffs){
			$scope.staffs=staffs;
			$scope.newSaleFlag=true;
			
			$.each($scope.staffs,function(i,data){
				$scope.staffs[i].userId=""+data.userId;
			});
			$scope.infoSection=false;
			$scope.saleSection=true;
			
		});
	};
	
	
	$scope.changeTypeOfProgram=function(){
		if($scope.progType=="0"){
			$scope.programSelected=false;
		}else{
			$http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType}).then(function(response){
				$scope.programs=response.data.resultObj;
				$.each($scope.programs,function(i,data){
					$scope.programs[i].progId=""+data.progId;
				});
				
				$scope.programSelected=true;
				$scope.psf.progId="0";
			});
			
			if($scope.progType=="pp"){
				$scope.psf.progType="psp";
			}else if($scope.progType=="pc"){
				$scope.psf.progType="psc";
			}else if($scope.progType=="pm"){
				$scope.psf.progType="psm";
			}
			
			
		}
	}
	
	
	
	
	
	$scope.programChanged=function(){
		if($scope.psf.progId=="0"){
			$scope.programSelected=false;
		}else{
			
			$http({method:"POST", url:"/bein/program/findProgramById/"+$scope.progType+"/"+$scope.psf.progId}).then(function(response){
				$scope.program=response.data.resultObj;
				$scope.psf.progId=""+$scope.program.progId;
				if($scope.progType=='pm'){
					$scope.psf.packetPrice=($scope.program.progPrice);
					
				}else{
					$scope.psf.packetPrice=($scope.program.progPrice*$scope.program.progCount);
					
				}
				$scope.psf.progCount=$scope.program.progCount;
				$scope.programSelected=true;
			});
			
		}
	}
	
	 function findInstructors(){
		   return  $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				return response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	  }
	

	 $scope.turnBackToInfo=function(){
		 $scope.infoSection=true;
		 $scope.saleSection=false;
		 
		 $scope.getMemberPacketSale($routeParams.userId).then(function(){
			 $("#barOptions").attr("width",$("#cnvPanel").width());
			 $("#barOptions").attr("height",240);
			 $("#barOptions").css({"width":$("#cnvPanel").width(),"height":"240"});
		 });
		 /*
		 setTimeout(function(){
			 
			 getDataToGraph();
		 },100);
		*/
	 };
	 
	 
	 $scope.unFreeze=function(smtpId,smpId){
		
		 swal({
	            title: $translate.instant("areYouSureToCancel"),
	            text: $translate.instant("cancelFreezeComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yesDelete"),
	            cancelButtonText: $translate.instant("noDelete"),
	            closeOnConfirm: true,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
		 
		 
					 $http({
						method:'POST',
						url: "/bein/membership/booking/unFreezeSchedule/"+smtpId+"/"+smpId,
					}).then(function successCallback(response) {
						var res=response.data;
						if(res.resultStatu=="success"){
							toastr.success($translate.instant(res.resultMessage));
							$scope.getMemberPacketSale($scope.userId);
						}else{
							toastr.error($translate.instant(res.resultMessage));
						}
					}, function errorCallback(response) {
						$location.path("/login");
					});
					 
	            }
	            });
					 
	}
	 
	 
	 $scope.totalIncome=0;
	 $scope.totalDept=0;
	 
	 function calculatePacket(){
		 $scope.totalDept=0;
		 $scope.totalIncome=0;
		 $.each($scope.packetSales,function(i,data){
			
				 $scope.totalIncome=$scope.totalIncome+data.packetPrice;
			 
			 
			 if(data.packetPaymentFactory!=null){
			  $scope.totalDept=$scope.totalDept+data.packetPaymentFactory.payAmount;
			 }
		 });
		 
		 $scope.totalDept=$scope.totalIncome-$scope.totalDept;
	 }
	 
	 
	 
   function getDataToGraph(){
		
	   var dates=[];
	   var data1=[];
	   var data2=[];
	   
	   $.each($scope.packetSales,function(i,ps){
		   dates.push($filter('date')(ps.salesDate,$scope.dateFormat));
		   data1.push(ps.packetPrice);
		   if(ps.packetPaymentFactory!=null){
			   data2.push(ps.packetPaymentFactory.payAmount);
			 }else{
			   data2.push(0);
		     }
		});
	   
	   /**
        * Options for Bar chart
        */
       var barOptions = {
    		   showDatasetLabels : true,
           scaleBeginAtZero : true,
           scaleShowGridLines : true,
           scaleGridLineColor : "rgba(0,0,0,.05)",
           scaleGridLineWidth : 1,
           barShowStroke : true,
           barStrokeWidth : 1,
           barValueSpacing : 5,
           barDatasetSpacing : 1,
           responsive:true
       };
	   
	   /**
        * Data for Bar chart
        */
       var barData = {
           labels: dates,
           datasets: [
               {
                   label: $translate.instant('packetPrice'),
                   fillColor: "rgba(220,220,220,0.5)",
                   strokeColor: "rgba(220,220,220,0.8)",
                   highlightFill: "rgba(220,220,220,0.75)",
                   highlightStroke: "rgba(220,220,220,1)",
                   data: data1
               },
               {
                   label: $translate.instant("payAmount"),
                   fillColor: "rgba(98,203,49,0.5)",
                   strokeColor: "rgba(98,203,49,0.8)",
                   highlightFill: "rgba(98,203,49,0.75)",
                   highlightStroke: "rgba(98,203,49,1)",
                   data: data2
               }
           ]
       };

       var ctx = document.getElementById("barOptions").getContext("2d");
       var myNewChart = new Chart(ctx).Bar(barData, barOptions);
        
	}
	
   
   
   
   function getPrograms(progType){
		return $http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType}).then(function(response){
			return response.data.resultObj;
			
		});
		
	}
   
   
   $scope.selectedUserList=new Array();
   
   
   function initBookPlanModal(){
	   $scope.selectedUserList=new Array();
	    $scope.scheduleTimePlan=new Object();
	    $scope.scheduleTimePlan.planStartDateTime="";
	    $scope.scheduleTimePlan.schId=0;
	    $scope.scheduleTimePlan.tpComment="";
	    $scope.schtId=0;
	   
	    $scope.selectedTime=new Date();
		$scope.selectedStaff=new Object();
		$scope.selectedStaff.userId="0";
		
		$scope.searchUsername="";
		$scope.selectedUser=new Object();
		$scope.selectedUser.userId=0;
		
		$scope.monday="0";
	    $scope.tuesday="0";
	    $scope.wednesday="0";
	    $scope.thursday="0";
	    $scope.friday="0";
	    $scope.saturday="0";
	    $scope.sunday="0";
	    
	    $scope.periodCount=1;
	    $scope.period=false;
		$scope.showPeriod=false;
		$scope.progName="";
		
		$('.i-checks').iCheck('check'); 
		
   }
   
   $scope.makePlan=function(){
	   initBookPlanModal();
	   $('#bookedModel').modal('show');
	   
	   $scope.progId=$scope.psf.progId;
	   
	   
	   
	   $.each($scope.programs,function(i,data){
		  if(data.progId==$scope.psf.progId){
			  $scope.progName=data.progName; 
		  } 
	   });
	   $scope.schtId=0;
	   
	   $scope.findTimes().then(function(times){
			$scope.times=times;
		});
	   
	   $scope.selectedUser=$scope.member;
	   var userFound=false;
	   
	   $.each($scope.selectedUserList,function(i,data){
			if(data.userId==$scope.selectedUser.userId){
				toastr.error($translate.instant("memberAlreadyAdded"));
				userFound=true;
			}
		});
	   
	   if(!userFound){
		   $scope.selectedUserList.push($scope.selectedUser);
	   }
	   
   }
   
   
   
   $scope.continuePlan=function(packetSale){
	   initBookPlanModal();
	   $('#oldBookedModel').modal('show');
	   $scope.progName=packetSale.programFactory.progName; 
	   $scope.progId=packetSale.programFactory.progId; 
	   $scope.progType=packetSale.programFactory.type;
	   $scope.findTimes().then(function(times){
			$scope.times=times;
		});
	   
	   $scope.psf=packetSale;
	   
	   
	   
	   $scope.selectedUser=$scope.member;
	   
	   getPrograms($scope.progType).then(function(programs){
		   
	   $scope.programs=programs;
	   
	   findInstructors().then(function(staffs){
		   $scope.staffs=staffs;
		   $http({
				  method:'POST',
				  url: "/bein/member/findUserForBookingBySaleId"+"/"+packetSale.programFactory.type+"/"+packetSale.saleId,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					
					var scheduleTimePlan=response.data.resultObj;
					
					$scope.schtId=0;
					$scope.scheduleTimePlan.schId=scheduleTimePlan.schId;
					
					$.each(scheduleTimePlan.scheduleFactories,function(i,scf){
						   scf.user.saleId=packetSale.saleId;
						   $scope.selectedUserList.push(scf.user);
					});
					
					
					 if($scope.selectedUserList.length==0){
						   $scope.selectedUser=$scope.member;
						   $scope.selectedUser.saleId=packetSale.saleId;
						   $scope.selectedUserList.push($scope.selectedUser);
						   $scope.schtId=0;
					 }
					 
				}, function errorCallback(response) {
					$location.path("/login");
				}); 
		   
		   
		  
		   
		   
	   });
	   
	   })
	   
	  
	   
   }
   
    $scope.scheduleTimePlan=new Object();
    $scope.scheduleTimePlan.planStartDateTime="";
    $scope.scheduleTimePlan.schId=0;
    $scope.scheduleTimePlan.tpComment="";
    $scope.schtId=0;
    
    $scope.selectedTime=new Date();
	$scope.selectedStaff=new Object();
	$scope.selectedStaff.userId="0";
	$scope.selectedUser;
	$scope.monday="0";
    $scope.tuesday="0";
    $scope.wednesday="0";
    $scope.thursday="0";
    $scope.friday="0";
    $scope.saturday="0";
    $scope.sunday="0";
    
    $scope.periodCount=1;
    $scope.period=false;
	$scope.showPeriod=false;
	$scope.progName="";
	
   
	$scope.findTimes=function(){
		
		return $http({
			  method:'POST',
			  url: "/bein/private/booking/findTimes",
			}).then(function successCallback(response) {
				return response.data;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
   
	
	$scope.removeSelectedUser=function(user){
		$.each($scope.selectedUserList,function(i,data){
			if(data.userId==user.userId){
				
				$scope.selectedUserList.splice(i,1);
				return ;
			}
		})
	}
	
	$scope.searchUserBy=function(){
		
		
		if($scope.searchUsername.length>1){
		var user=new Object();
		user.userName=$scope.searchUsername;
		user.userSurname="";
			$http({
				  method:'POST',
				  url: "/bein/member/findUserForBookingByFreeSale/"+$scope.progId+"/"+$scope.progType,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		
		}
		
	}
	
	$scope.searchUser=function(event){
		
		$scope.addUserReady=false;
		$scope.selectedUser=new Object();
		$scope.selectedUser.userId=0;
		/*
		if($scope.searchUsername.length>1){
		var user=new Object();
		user.userName=$scope.searchUsername+event.key;
		user.userSurname="";
			$http({
				  method:'POST',
				  url: "/bein/member/findUserForBookingByFreeSale/"+$scope.progId+"/"+$scope.progType,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		
		}
		*/
	}
   
	$scope.selectUser=function(user){
		$scope.searchUsername=user.userName+" "+user.userSurname;
		$scope.selectedUser=user;
		$scope.addUserReady=true;
	    $scope.userList=new Array();
	}
	
	$scope.addUser=function(){
		var userFound=false;
		$.each($scope.selectedUserList,function(i,data){
			if(data.userId==$scope.selectedUser.userId){
				toastr.error($translate.instant("memberAlreadyAdded"));
				userFound=true;
				return false;
			}
		});
		if(!userFound){
		$scope.selectedUserList.push($scope.selectedUser);
		$scope.noSaledFlag=true;
		$scope.addNewUser=false;
				$scope.addNewUser=true;
				$scope.retryFlag=true;
		}
	}
   
	$scope.addMoreUser=function(){
		if($scope.psf.progId=="0"){
			toastr.error($translate.instant("pleaseSelectProgramToAddMember"));
		}else{
			$scope.addNewUser=true;
			$scope.noSaledFlag=false;
		}
	}
   
	 $('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
	    
	    $('.i-checks').on('ifChanged', function(event) {
			if(event.target.checked){
				$scope.period=false;
				$scope.showPeriod=false;
				$scope.days=new Array();
			}else{
				$scope.period=true;
				$scope.showPeriod=true;
			}
			$scope.$apply();
	    });
		
	    $scope.days=new Array();
		
	    $scope.setPeriodCancel=function(){
	    	$scope.period=false;
			$scope.showPeriod=false;
			$scope.days=new Array();
			
			$scope.monday="0";
		    $scope.tuesday="0";
		    $scope.wednesday="0";
		    $scope.thursday="0";
		    $scope.friday="0";
		    $scope.saturday="0";
		    $scope.sunday="0";
		    
		    $('.i-checks').iCheck('check'); 
		    
		    
	    }
	    
	    $scope.setPeriod=function(){
	    	$scope.days=generatePersonalDetailProgram();
	    	$scope.showPeriod=false;
	    }
	    
	    
	    
	    
	    function generatePersonalDetailProgram(){
	  	   var days=new Array();
	  		if($scope.monday!="0"){
	  			var mondayObj=new Object();
	  			mondayObj.progDay=1;
	  			mondayObj.progStartTime=$scope.monday;
	  			days.push(mondayObj);
	  		}
	  		if($scope.tuesday!="0"){
	  			var tuesdayObj=new Object();
	  			tuesdayObj.progDay=2;
	  			tuesdayObj.progStartTime=$scope.tuesday;
	  			
	  			days.push(tuesdayObj);
	  		}
	  		if($scope.wednesday!="0"){
	  			var wednesdayObj=new Object();
	  			wednesdayObj.progDay=3;
	  			wednesdayObj.progStartTime=$scope.wednesday;
	  			days.push(wednesdayObj);
	  		}
	  		if($scope.thursday!="0"){
	  			var thursdayObj=new Object();
	  			thursdayObj.progDay=4;
	  			thursdayObj.progStartTime=$scope.thursday;
	  			days.push(thursdayObj);
	  		}
	  		if($scope.friday!="0"){
	  			var fridayObj=new Object();
	  			fridayObj.progDay=5;
	  			fridayObj.progStartTime=$scope.friday;
	  			days.push(fridayObj);
	  		}
	  		if($scope.saturday!="0"){
	  			var saturdayObj=new Object();
	  			saturdayObj.progDay=6;
	  			saturdayObj.progStartTime=$scope.saturday;
	  			
	  			days.push(saturdayObj);
	  		}
	  		if($scope.sunday!="0"){
	  			var sundayObj=new Object();
	  			sundayObj.progDay=7;
	  			sundayObj.progStartTime=$scope.sunday;
	  			days.push(sundayObj);
	  		}
	  		
	  		return days;
	  		
	  	}
	 	
	 	
	     
	     
	     
	       var toUTCDate = function(date){
	         var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
	         return _utc;
	       };

	       var millisToUTCDate = function(millis){
	         return toUTCDate(new Date(millis));
	       };
	       
	       var localeStr = function(milis){
	           return (new Date(millis).toLocaleString());
	         };
	         

	       $scope.toUTCDate = toUTCDate;
	       $scope.millisToUTCDate = millisToUTCDate;
	       $scope.toLocaleStr=localeStr;
	       
	       
	    
	    
	       
	$scope.letsCreatePlan=function(){
	   		
		if($scope.scheduleTimePlan.planStartDateTime=="" && !$scope.period){
			toastr.error($translate.instant("noTimeSelected"));
   			return false;
		}
		
		if($scope.selectedStaff.userId=="0"){
			toastr.error($translate.instant("chooseInstructor"));
   			return false;
		}
		
		    
		$(".splash").css("display",'');
		   
	   		$scope.scheduleTimePlan.planStartDate=new Date($scope.selectedTime);
	   	   if(!$scope.period){
	   		var hour=parseInt( $scope.scheduleTimePlan.planStartDateTime.split(":")[0]);
		    var min=parseInt( $scope.scheduleTimePlan.planStartDateTime.split(":")[1]);
		    $scope.scheduleTimePlan.planStartDate.setHours(hour, min, 0, 0)
	   	   }
	   		
	   		$scope.scheduleTimePlan.schtStaffId=$scope.selectedStaff.userId;
	   		$scope.scheduleTimePlan.scheduleFactories=new Array();
	   		
	   		if($scope.period){
	   			$scope.scheduleTimePlan.period=1;
	   			$scope.scheduleTimePlan.periodCount=$scope.periodCount;
	   			$scope.scheduleTimePlan.periodicTimePlans=generatePersonalDetailProgram();
	   		}else{
	   			$scope.scheduleTimePlan.period=0;
	   			$scope.scheduleTimePlan.periodCount=0;
	   			$scope.scheduleTimePlan.periodicTimePlans=new Array();
	   		}
	   		
	   		
	   		if($scope.selectedUserList.length==0){
	   			toastr.error($translate.instant("noMemberSelected"));
	   			return false;
	   		}
	   		
	   		
	   		$.each($scope.programs,function(i,data){
	   			if($scope.programs[i].progId==$scope.psf.progId){
	   				$scope.scheduleTimePlan.programFactory=$scope.programs[i];
	   			}
	   		});
	   		
	   		
	   		$.each($scope.selectedUserList,function(i,user){
	   			var scheduleFactories=new Object();
	   			scheduleFactories.suppId=0;
	   			scheduleFactories.schtId=$scope.schtId;
	   			scheduleFactories.userId=user.userId;
	   			scheduleFactories.saleId=user.saleId;
	   			if($scope.scheduleTimePlan.programFactory.type=="pp")
	   			   scheduleFactories.type="supp";
	   			else
	   				 scheduleFactories.type="sucp";
	   				
	   			$scope.scheduleTimePlan.scheduleFactories.push(scheduleFactories);
	   		});
	   	
	   		
	   		
	   		
	   		$http({method:"POST"
	   			, url:"/bein/private/booking/createScheduleTimePlan"
	   			,data:angular.toJson($scope.scheduleTimePlan)
	   			}).then(function successCallback(response) {
	   				
	   				var resultList=response.data;
	   				
	   				$.each(resultList,function(i,data){
	   					if(data.resultStatu=="success"){
	   						toastr.success($translate.instant(data.resultMessage));
	   					}else{
	   						var sctp=data.resultObj;
	   						toastr.error($translate.instant(data.resultMessage)+' '+new Date(sctp.planStartDate).toLocaleDateString());
	   					}
	   				});
	   				
	   				
	   				$scope.getMemberPacketSale($scope.member.userId);
	   				$scope.turnBackToInfo();
	   				$(".splash").css("display",'none');
	   				$('#bookedModel').modal('hide');
	   				$('#oldBookedModel').modal('hide');
	   			}, function errorCallback(response) {
					$location.path("/login");
					$(".splash").css("display",'none');
				});
	   		
	   	}
	    
	    
	    
	    
	    
});
