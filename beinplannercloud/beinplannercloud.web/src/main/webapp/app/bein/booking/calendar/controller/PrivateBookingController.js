ptBossApp.controller('PrivateBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.times;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.instructorCount=1;
	$scope.restriction;
	
	
	$scope.scheduleTimeObjs;
	$scope.dayDuration="1";
	$scope.dateOfQuery=new Date();
	
	$scope.timePlan=false;
	$scope.timePlanTop=0;
	$scope.timePlanLeft=0;
	
	
	$scope.progId="0";
	$scope.progType="0";
	$scope.programSelected=false;
	$scope.newSaleFlag=false;
	
	$scope.init=function(){
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
		
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.findTimes().then(function(times){
				$scope.times=times;
				$scope.findInstructors().then(function(instructor){
					$scope.instructorCount=instructor.length;
					
					var schCalObj=new Object();
					schCalObj.calendarDate=new Date($scope.dateOfQuery);
					schCalObj.dayDuration=$scope.dayDuration;
					$scope.findAllPlanByDate(schCalObj);
					
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
	
	
	
	$scope.onDropComplete = function(data, evt) {
	      console.log("drop success, data:", data);
	    /*  var index = $scope.droppedObjects.indexOf(data);
	      if (index == -1)
	        $scope.droppedObjects.push(data);*/
	}
	
	
	
	$scope.selectedTime=new Date();
	$scope.selectedStaff=new Object();
	$scope.selectedStaffId="0";
	
	$scope.showTimePlan=function(sctp,$event){
		$scope.selectedTime=new Date(sctp.planStartDate);
		$scope.selectedStaff=sctp.staff;
	}
	
	$scope.userList=new Array();
	$scope.selectedUserList=new Array();
	
	$scope.searchUsername="";
	
	$scope.searchUser=function(event){
		if($scope.searchUsername.length>1){
		var user=new Object();
		user.userName=$scope.searchUsername+event.key;
		user.userSurname="";
		if($scope.progType=="0" && $scope.progId=="0"){
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
				  url: "/bein/member/findUserForBookingBySale/"+$scope.progId+"/"+$scope.progType,
				  data:angular.toJson(user)
				}).then(function successCallback(response) {
					$scope.userList=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		}
		}
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
		$scope.selectedUserList.push($scope.selectedUser);
		
		$scope.findAllSaledPacketsForUser($scope.selectedUser.userId).then(function(saledPackets){
			$scope.freePacket=true;
			console.log(saledPackets);
			$scope.saledPackets=saledPackets;
			
			$scope.newSaleFlag=true;
			$.each($scope.saledPackets,function(i,data){
				if(data.progCount-data.scheduleFactory.length>0){
					$scope.newSaleFlag=false;
					return false;
				}
				
			});
			
			
			
		});
		
		
	}
	
	$scope.removeSelectedUser=function(user){
		$.each($scope.selectedUserList,function(i,data){
			if(data.userId==user.userId){
				$scope.selectedUserList.splice(i,1);
				return false;
			}
		})
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
	
	
	
	$scope.overTimePlan=function(sctp){
		$scope.selectedTime=sctp.planStartDate;
		$scope.selectedStaff=sctp.staff;
		$scope.selectedStaffId=sctp.staff.userId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	$scope.findAllPlanByDate=function(schCalObj){
		 $http({
			  method:'POST',
			  url: "/bein/private/booking/findAllPlanByDate",
			  data:angular.toJson(schCalObj)
			}).then(function successCallback(response) {
				$scope.scheduleTimeObjs=response.data;
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
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
	
	
	
	
	
	
	
	
	
});
