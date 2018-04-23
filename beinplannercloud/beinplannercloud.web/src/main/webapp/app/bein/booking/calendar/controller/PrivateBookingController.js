ptBossApp.controller('PrivateBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	//Takvim saatleri plan satiri bulma 798 ($scope.findAllPlanByDate(param))
    // init plan satiri  216
    
	$scope.time;
	$scope.times;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	$scope.timeFormat="HH:mm";
	
	$scope.instructorCount=1;
	$scope.restriction;
	$scope.retryFlag=false;
	
	$scope.scheduleTimeObjs;
	$scope.dayDuration="1";
	$scope.dateOfQuery=new Date();
	
	$scope.timePlan=false;
	$scope.timePlanTop=0;
	$scope.timePlanLeft=0;
	
	$scope.noSaledFlag=true;
	
	$scope.progId="0";
	$scope.progName="";
	$scope.progType="0";
	$scope.programSelected=false;
	$scope.newSaleFlag=false;
	$scope.addNewUser=true;
	
	$scope.saleTitle="noSaleAdd";
	
	$scope.programs=new Array();
	
	$scope.schId=0;
	$scope.schtId=0;
	
	$scope.scheduleTimePlan=new Object();
	$scope.scheduleTimePlan.schtId=0;
	$scope.scheduleTimePlan.schId=0;
	$scope.scheduleTimePlan.planStartDate=new Date();
	$scope.scheduleTimePlan.statuTp=0;
	$scope.scheduleTimePlan.schtStaffId=0;
	$scope.scheduleTimePlan.tpComment="";
	
	
	$scope.scheduleTimePlan.scheduleFactories=new Array();
	$scope.scheduleTimePlan.programFactory=new Object();
	
	$scope.duration="60";
	$scope.calPeriod="60";
	
	$scope.spanSize="0";
	
	$scope.tempScrollTop;
	
	
	
	
	
	$scope.letsCreatePlan=function(){
		
		$scope.scheduleTimePlan.planStartDate=new Date($scope.selectedTime);
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
			if($scope.programs[i].progId==$scope.progId){
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
			}).then(function(response){
				
				var resultList=response.data;
				
				$.each(resultList,function(i,data){
					if(data.resultStatu=="success"){
						toastr.success($translate.instant(data.resultMessage));
					}else{
						var sctp=data.resultObj;
						toastr.error($translate.instant(data.resultMessage)+' '+new Date(sctp.planStartDate).toLocaleDateString());
					}
				});
				
				var schCalObj=new Object();
				schCalObj.calendarDate=new Date($scope.dateOfQuery);
				schCalObj.dayDuration=$scope.dayDuration;
				
				$scope.findAllPlanByDate(schCalObj);
				$('#myModal').modal('hide');
				$('#oldBookedModel').modal('hide');
			});
	}
	
	
	
	
	
	$scope.continiueSchedulePlan=function(psf){
		
		$scope.selectedUserList=new Array();
		$scope.noSaledFlag=false;
		$http({method:"POST"
			, url:"/bein/private/booking/generateScheduleTPBySaledPacket"
			,data:angular.toJson(psf)
			}).then(function(response){
			
				var rstp=response.data;
				$scope.scheduleTimePlan=new Object();
				$scope.scheduleTimePlan.schId=rstp.schId;
				$scope.schId=rstp.schId;
				$scope.scheduleTimePlan.tpComment="";
				
				
				$.each(rstp.scheduleFactories,function(i,scf){
					$scope.selectedUserList.push(scf.user);
				});
				
				if(psf.progType=="psp"){
					$scope.progType="pp";
				}else if(psf.progType=="psc"){
					$scope.progType="pc";
				}
				
				
				$http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType}).then(function(response){
					$scope.programs=response.data.resultObj;
					$scope.newSaleFlag=true;
					$scope.programSelected=true;
					setTimeout(function(){
						$scope.progId=""+psf.progId;
						$scope.noSaledFlag=false;
						$scope.$apply();
					},1000);
					
					$scope.saleTitle="startToBooking";
					$scope.saledPackets=new Array();
					
				});
			});
			
	}
	
	
	$scope.initTimePlan=function(){
		$scope.schId=0;
		$scope.schtId=0;
		$scope.retryFlag=false;
		$scope.scheduleTimePlan=new Object();
		$scope.scheduleTimePlan.schtId=0;
		$scope.scheduleTimePlan.schId=0;
		$scope.scheduleTimePlan.planStartDate=new Date();
		$scope.scheduleTimePlan.statuTp=0;
		$scope.scheduleTimePlan.schtStaffId=0;
		$scope.scheduleTimePlan.tpComment="";
		
		$scope.scheduleTimePlan.scheduleFactories=new Array();
		$scope.scheduleTimePlan.programFactory=new Object();
		
		$scope.period=false;
		
		$scope.selectedUserList=new Array();
		$scope.progId="0";
		$scope.progType="0";
		$scope.progName="";
		$scope.programs=new Array();
		$scope.saledPackets=null;
		$scope.noSaledFlag=true;
		$scope.addNewUser=true;
		$scope.programSelected=false;
		$scope.saleTitle="startToBookingWithNoSale";
	
		
	}
	
	$scope.addMoreUser=function(){
		if($scope.progId=="0"){
			toastr.error($translate.instant("pleaseSelectProgramToAddMember"));
		}else{
			$scope.addNewUser=true;
			$scope.noSaledFlag=false;
		}
	}
	
	$scope.init=function(){
		/*
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
			
			if($scope.restriction.multiRestriction==0){
				if($scope.restriction.personalRestriction==1){
					$scope.progType="pp";
		        }else if($scope.restriction.groupRestriction==1){
		        	$scope.progType="pc";
		        }
				
				$http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType}).then(function(response){
					$scope.programs=response.data.resultObj;
					$.each($scope.programs,function(i,data){
						$scope.programs[i].progId=""+data.progId;
					});
					$scope.programSelected=true;
					$scope.progId="0";
				});
			}
			
		});*/
		
		$scope.mobileInstructor;
		$scope.instructors;
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			
			$scope.dateOfQuery=new Date();
			$scope.ptCurrency=global.ptCurrency;
			
			
			$scope.findDefCalInfos().then(function(times){
			
				$scope.spanSize=parseInt($scope.duration)/parseInt($scope.calPeriod);
				
				$scope.findTimes().then(function(times){
					$scope.times=times;
					$scope.findInstructors().then(function(instructor){
						$scope.instructorCount=instructor.length;
						$scope.instructors=instructor;
						$scope.mobileInstructor=instructor[0];
						
						
						
						var schCalObj=new Object();
						schCalObj.calendarDate=new Date($scope.dateOfQuery);
						schCalObj.dayDuration=$scope.dayDuration;
						$scope.findAllPlanByDate(schCalObj);
						
					});
				});
			});
		});	
	}
	
	$scope.changeDayDuration=function(){
		var schCalObj=new Object();
		schCalObj.calendarDate=new Date($scope.dateOfQuery);
		schCalObj.dayDuration=$scope.dayDuration;
		$scope.findAllPlanByDate(schCalObj);
	}
	
	$scope.changeQueryDate=function(){
		var schCalObj=new Object();
		schCalObj.calendarDate=new Date($scope.dateOfQuery);
		schCalObj.dayDuration=$scope.dayDuration;
		$scope.findAllPlanByDate(schCalObj);
	}
	
	
	
	
	
	
	
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
				$scope.progId="0";
			});
		}
	}
	
	
	$scope.isDragging=false;
	$scope.$on('draggable:start', function (data) {
		$scope.isMouseOver=false;
	    isDragging=true;
	});
	
	$scope.onDropComplete = function(data, evt,sctp) {
		
		  $scope.tempScrollTop = evt.y-100;
		
		  setOldPlan(data,sctp);
		  
	      $scope.letsCreatePlan();
	     
	}
	
	
	
	$scope.onDropCompleteForMobile = function(data, evt,sctp) {
		  setOldPlan(data,sctp);
	      $scope.letsCreatePlan();
	}
	
	
	var setOldPlan=function(sctp,newSctp){
		$scope.scheduleTimePlan=sctp;
		$scope.retryFlag=false;
		$scope.newSaleFlag=true;
		$scope.selectedTime=new Date(newSctp.planStartDate);
		$scope.selectedStaff=newSctp.staff;
		$scope.saleTitle="booking";
		$scope.addNewUser=false;
		$scope.saledPackets=new Array();
		$scope.progType=""+sctp.programFactory.type;
		$scope.progId=""+sctp.programFactory.progId;
		$scope.progName=""+sctp.programFactory.progName;
		$scope.schId=sctp.schId;
		$scope.schtId=sctp.schtId;
		
		$scope.period=false;
		$scope.showPeriod=false;
		$scope.programSelected=true;
		
		$scope.selectedUserList=new Array();
		
		
		
		$.each(sctp.scheduleFactories,function(i,scf){
			$scope.selectedUserList.push(scf.user);
		});
	}
	
	
	$scope.selectedTime=new Date();
	$scope.selectedStaff=new Object();
	$scope.selectedStaffId="0";
	
	$scope.showTimePlan=function(sctp,$event){
		initBookPlanModal();
		
		if(!$scope.isDragging){
			 $scope.tempScrollTop = $event.y-100;
			$scope.selectedTime=new Date(sctp.planStartDate);
			$scope.selectedStaff=sctp.staff;
			$scope.initTimePlan();
			
		}else{
			$scope.isDragging=false;	
		}
		
	}
	
	$scope.cancelBtn="cancel"
	$scope.postponeBtn="postpone"
	$scope.cancelTimePlan="cancelTimePlan";
	$scope.postponeTimePlan="postponeTimePlan";
	
	
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
	
	
	$scope.showOldTimePlan=function(sctp,$event){
		initBookPlanModal();
		
		$scope.tempScrollTop = $event.y-100;
		
		$scope.scheduleTimePlan=sctp;
		$scope.retryFlag=false;
		$scope.newSaleFlag=true;
		$scope.selectedTime=new Date(sctp.planStartDate);
		$scope.selectedStaff=sctp.staff;
		$scope.saleTitle="booking";
		$scope.addNewUser=false;
		$scope.saledPackets=new Array();
		$scope.progType=""+sctp.programFactory.type;
		$scope.progId=""+sctp.programFactory.progId;
		$scope.progName=""+sctp.programFactory.progName;
		$scope.schId=sctp.schId;
		$scope.schtId=sctp.schtId;
		
		$scope.period=false;
		$scope.showPeriod=false;
		$scope.programSelected=true;
		
		$scope.selectedUserList=new Array();
		
		
		if(sctp.statuTp==1){
			$scope.cancelBtn="backToCancel"
			$scope.postponeBtn="postpone"
			$scope.cancelTimePlan="cancelCancelTimePlan";
			$scope.postponeTimePlan="postponeTimePlan";
			
		}else if(sctp.statuTp==2){
			$scope.cancelBtn="cancel"
			$scope.postponeBtn="backToCancel"
			$scope.cancelTimePlan="cancelCancelTimePlan";
			$scope.postponeTimePlan="cancelPostponeTimePlan";
		}else{
			$scope.cancelBtn="cancel"
			$scope.postponeBtn="postpone"
			$scope.cancelTimePlan="cancelTimePlan";
			$scope.postponeTimePlan="postponeTimePlan";
		}
			
		
		
		$.each(sctp.scheduleFactories,function(i,scf){
			$scope.selectedUserList.push(scf.user);
		});
	}
	
	
	$scope.cancelTP=function(){
		$http({method:"POST"
			, url:"/bein/private/booking/cancelScheduleTimePlan"
			,data:angular.toJson($scope.scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$('#myModal').modal('hide');
			});
	}
	
	$scope.postponeTP=function(){
		$http({method:"POST"
			, url:"/bein/private/booking/postponeScheduleTimePlan"
			,data:angular.toJson($scope.scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$('#myModal').modal('hide');
			});
	}

	$scope.deleteTP=function(){
		$http({method:"POST"
			, url:"/bein/private/booking/deleteScheduleTimePlan"
			,data:angular.toJson($scope.scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$('#myModal').modal('hide');
			});
	}
	
	
	$scope.scpd=null;
	
	$scope.findBookingByTimePlan=function(){
		$('#oldBookedModel').modal('hide');
		$http({
			  method:"POST"
			, url:"/bein/private/booking/findBookingByTimePlan/"+$scope.scheduleTimePlan.schtId
			}).then(function(response){
				$scope.scpd=response.data;
				
				for(var i=0;i<$scope.scpd.scheduleTimePlans.length;i++){
					var scpdDate=toUTCDate(new Date($scope.scpd.scheduleTimePlans[i].planStartDate));
					var scpdTime=(scpdDate.getHours()<10?'0'+scpdDate.getHours():scpdDate.getHours()) +":"+(scpdDate.getMinutes()<10?'0'+scpdDate.getMinutes():scpdDate.getMinutes());
					$scope.scpd.scheduleTimePlans[i].planStartDate=scpdDate;
					
					$scope.scpd.scheduleTimePlans[i].planStartDateTime=scpdTime;
					
				}
				
				
				$('#detailPlanModel').modal('show');
			});
	}
	
	$scope.findBookingByTimePlanL=function(schtId){
		$scope.scpd=null;
		$http({
			  method:"POST"
			, url:"/bein/private/booking/findBookingByTimePlan/"+schtId
			}).then(function(response){
				$scope.scpd=response.data;
				for(var i=0;i<$scope.scpd.scheduleTimePlans.length;i++){
					
					var scpdDate=toUTCDate(new Date($scope.scpd.scheduleTimePlans[i].planStartDate));
					var scpdTime=(scpdDate.getHours()<10?'0'+scpdDate.getHours():scpdDate.getHours()) +":"+(scpdDate.getMinutes()<10?'0'+scpdDate.getMinutes():scpdDate.getMinutes());
					$scope.scpd.scheduleTimePlans[i].planStartDate=scpdDate;
					$scope.scpd.scheduleTimePlans[i].planStartDateTime=scpdTime;
				}
			});
	}
	
	
	$scope.findBookingByTimePlanDeleteL=function(schId){
		$scope.scpd=null;
		$http({
			  method:"POST"
			, url:"/bein/private/booking/findBookingBySchPlan/"+schId
			}).then(function(response){
				$scope.scpd=response.data;
				for(var i=0;i<$scope.scpd.scheduleTimePlans.length;i++){
					var scpdDate=toUTCDate(new Date($scope.scpd.scheduleTimePlans[i].planStartDate));
					var scpdTime=(scpdDate.getHours()<10?'0'+scpdDate.getHours():scpdDate.getHours()) +":"+(scpdDate.getMinutes()<10?'0'+scpdDate.getMinutes():scpdDate.getMinutes());
					$scope.scpd.scheduleTimePlans[i].planStartDate=scpdDate;
					$scope.scpd.scheduleTimePlans[i].planStartDateTime=scpdTime;
				}
			});
	}
	
	$scope.saveTPL=function(scheduleTimePlan){
		scheduleTimePlan.programFactory=$scope.scheduleTimePlan.programFactory;
		var hour=parseInt(scheduleTimePlan.planStartDateTime.split(":")[0]);
		var minute=parseInt(scheduleTimePlan.planStartDateTime.split(":")[1]);
		
		scheduleTimePlan.planStartDate.setHours(hour);
		scheduleTimePlan.planStartDate.setMinutes(minute);
		
		
		
		$http({method:"POST"
			, url:"/bein/private/booking/updateScheduleTimePlan"
			,data:angular.toJson(scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.findBookingByTimePlanL(scheduleTimePlan.schtId);
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
			});
	}
	
	$scope.cancelTPL=function(scheduleTimePlan){
		scheduleTimePlan.programFactory=$scope.scheduleTimePlan.programFactory;
		$http({method:"POST"
			, url:"/bein/private/booking/cancelScheduleTimePlan"
			,data:angular.toJson(scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.findBookingByTimePlanL(scheduleTimePlan.schtId);
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
			});
	}
	
	$scope.postponeTPL=function(scheduleTimePlan){
		
		scheduleTimePlan.programFactory=$scope.scheduleTimePlan.programFactory;
		
		$http({method:"POST"
			, url:"/bein/private/booking/postponeScheduleTimePlan"
			,data:angular.toJson(scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.findBookingByTimePlanL(scheduleTimePlan.schtId);
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
			});
	}

	$scope.deleteTPL=function(scheduleTimePlan){
		scheduleTimePlan.programFactory=$scope.scheduleTimePlan.programFactory;
		$http({method:"POST"
			, url:"/bein/private/booking/deleteScheduleTimePlan"
			,data:angular.toJson(scheduleTimePlan)
			}).then(function(response){
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.findBookingByTimePlanDeleteL(scheduleTimePlan.schId);
					
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
			});
	}
	
	
	
	
	$scope.userList=new Array();
	$scope.selectedUserList=new Array();
	$scope.searchUsername="";
	
	
	
	$scope.searchUserBy=function(){
		if($scope.searchUsername.length>1){
		var user=new Object();
		user.userName=$scope.searchUsername;
		user.userSurname="";
		if($scope.progType=="0" || $scope.progId=="0"){
			
			if($scope.selectedUserList.length>0){
	          toastr.error($translate.instant("selectProgram"));
	          return false;
			}
			
			
			$http({
				  method:'POST',
				  url: "/bein/member/findByUsernameAndUsersurname",
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
			
		}else{
			$http({
				  method:'POST',
				  url: "/bein/member/findUserForBookingBySale/"+$scope.progId+"/"+$scope.progType+"/"+$scope.schId,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		}
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
		if($scope.progType=="0" || $scope.progId=="0"){
			
			if($scope.selectedUserList.length>0){
	          toastr.error($translate.instant("selectProgram"));
	          return false;
			}
			
			
			$http({
				  method:'POST',
				  url: "/bein/member/findByUsernameAndUsersurname",
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
			
		}else{
			$http({
				  method:'POST',
				  url: "/bein/member/findUserForBookingBySale/"+$scope.progId+"/"+$scope.progType+"/"+$scope.schId,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		}
		}*/
	}
	
	

	$scope.addUserReady=false;
	$scope.selectedUser=null;
	
	$scope.selectUser=function(user){
		$scope.searchUsername=user.userName+" "+user.userSurname;
		$scope.selectedUser=user;
		$scope.addUserReady=true;
	    $scope.userList=new Array();
	}
	
	$scope.saledPackets=null;
	
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
		if($scope.progType=="0" || $scope.progId=="0"){
			$scope.retryFlag=true;
			$scope.findAllSaledPacketsForUser($scope.selectedUser.userId).then(function(saledPackets){
				$scope.freePacket=true;
				$scope.saledPackets=saledPackets;
				$scope.newSaleFlag=true;
				$.each($scope.saledPackets,function(i,data){
					if(data.progCount-data.scheduleFactory.length>0){
						$scope.noSaledFlag=false;
						$scope.newSaleFlag=false;
						return false;
					}
					
				});
			});
		}else{
			
			if($scope.scheduleTimePlan.schtId>0){
				var scheduleFactories=new Object();
				scheduleFactories.suppId=0;
				scheduleFactories.schtId=$scope.schtId;
				scheduleFactories.userId=$scope.selectedUser.userId;
				scheduleFactories.saleId=$scope.selectedUser.saleId;
				if($scope.scheduleTimePlan.programFactory.type=="pp")
				   scheduleFactories.type="supp";
				else
					 scheduleFactories.type="sucp";
					
				$scope.addUserInScheduleTimePlan(scheduleFactories);
				
			}else{
				$scope.addNewUser=true;
				$scope.retryFlag=true;
			}
			
			
		}
		}
	}
	
	
	
	$scope.addUserInScheduleTimePlan=function(scf){
		$http({method:"POST"
			, url:"/bein/private/booking/addUserInScheduleTimePlan"
			,data:angular.toJson(scf)
			}).then(function(response){
				// SCF MUST BE UPDATED IN SELECTEDUSERLIST
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$('#myModal').modal('hide');
			});
	}
	
	
	$scope.removeSelectedUser=function(user){
		$.each($scope.selectedUserList,function(i,data){
			if(data.userId==user.userId){
				
				if($scope.progType=="pp"){
					
					if(data.suppId!=0){
						var suctp=new Object();
						suctp.suppId=data.suppId;
						suctp.schtId=$scope.schtId;
						suctp.userId=user.userId;
						suctp.type="supp";
						$scope.cancelUserInTimePlan(suctp);
						return false;
					}else{
						$scope.selectedUserList.splice(i,1);
					}
					
					
				}else if($scope.progType=="pc"){
					
					if(data.sucpId!=0){
						
						var suctp=new Object();
						suctp.sucpId=data.sucpId;
						suctp.schtId=$scope.schtId;
						suctp.userId=user.userId;
						suctp.type="sucp";
						$scope.cancelUserInTimePlan(suctp);
						return false;
					}else{
						$scope.selectedUserList.splice(i,1);
					}
					
				}
				return false;
			}
		})
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
							$.each($scope.selectedUserList,function(i,data){
								if(data.userId==scutp.userId){
									$scope.selectedUserList.splice(i,1);
									return false;
								}
							});
							
							
							var schCalObj=new Object();
							schCalObj.calendarDate=new Date($scope.dateOfQuery);
							schCalObj.dayDuration=$scope.dayDuration;
							$scope.findAllPlanByDate(schCalObj);
							
						}else{
							toastr.fail($translate.instant(res.resultMessage));
						}
					}, function errorCallback(response) {
						$location.path("/login");
					});
					 
	            }
	            });
		
		
	}
	
	
	$scope.findAllSaledPacketsForUser=function(userId){
		return $http({
			  method:'POST',
			  url: "/bein/packetsale/findUserBoughtPacketsForCalendar/"+userId,
			}).then(function successCallback(response) {
				return response.data;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	
	$scope.isMouseOver=false;
	$scope.overTimePlan=function(sctp){
		$scope.selectedTime=sctp.planStartDate;
		$scope.selectedStaff=sctp.staff;
		$scope.selectedStaffId=sctp.staff.userId;
		$scope.isMouseOver=true;
		
	}
	
	$scope.overTimePlanMobile=function(sctp){
		$scope.selectedTime=sctp.planStartDate;
	}
	
	
	$scope.mobileTimeObjs;
	/*
	 * Takvim planlama goruntuleme fonksiyonu
	 */
	$scope.findAllPlanByDate=function(schCalObj){
		 
		$(".splash").css("display",'');
		
		$http({
			  method:'POST',
			  url: "/bein/private/booking/findAllPlanByDate",
			  data:angular.toJson(schCalObj)
			}).then(function successCallback(response) {
				$scope.scheduleTimeObjs=response.data;
				
				for(var i=0;i<$scope.scheduleTimeObjs[0].staffs.length;i++){
					if($scope.scheduleTimeObjs[0].staffs[i].userId==$scope.mobileInstructor.userId){
						$scope.mobileTimeObjs=$scope.scheduleTimeObjs[0].staffs[i];
					}
				}
				
				$(window).scrollTop($scope.tempScrollTop);
				
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$location.path("/login");
				$(".splash").css("display",'none');
			});
	}
	
	$scope.changeStaffForMobile=function(){
		for(var i=0;i<$scope.scheduleTimeObjs[0].staffs.length;i++){	
			if($scope.scheduleTimeObjs[0].staffs[i].userId==$scope.mobileInstructor.userId){
				$scope.mobileTimeObjs=$scope.scheduleTimeObjs[0].staffs[i];
			}
		}
	}
	
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
	
	$scope.findInstructors=function(){
		   return  $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				return response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	  }
	
	
	$scope.findPrograms=function(){
		return $http({method:"POST", url:"/bein/program/findPrograms/"+$scope.progType})
		  .then(function successCallback( response){
			//$scope.programs=response.data.resultObj;
			return response.data.resultObj;
		}, function errorCallback(response) {
			$location.path("/login");
		});
	};
	
	
	/***************************************************************************************/
	/************Period of Booking**********************************************************/
	/***************************************************************************************/
	
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
    
    
    $scope.findDefCalInfos=function(){
		return $http({
			  method:'POST',
			  url: "/bein/definition/defCalendarTimes/find",
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res!=null){
					$scope.duration=""+res.duration;
					$scope.calPeriod=""+res.calPeriod;
					
				}else{
					$scope.duration="60";
					$scope.calPeriod="60";
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
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
      
      
      $scope.printPlan=function(){
    	  $('#printDetailPlanModel').printThis();
      }
      
    
      $scope.defFirm;
      
      $scope.find=function(){
      	$http({
  			  method:'POST',
  			  url: "/register/findMyFirm",
  			  data: angular.toJson($scope.defFirm),
  			}).then(function successCallback(response) {
  				if(response.data.resultStatu=="success"){
  					$scope.defFirm=response.data.resultObj;
  				}
  				
  				
  			});
      }
      
      
});
