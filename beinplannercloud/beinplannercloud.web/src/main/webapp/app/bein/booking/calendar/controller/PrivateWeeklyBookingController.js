ptBossApp.controller('PrivateWeeklyBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.instructor;
	$scope.instructors;
	$scope.sTimeObjs;
	$scope.dayDuration=7;
	$scope.scheduleTimeObjs;
	$scope.dateOfQuery=new Date();
	$scope.timeFormat="HH:mm";
	
	$scope.init=function(){
		$scope.findInstructors().then(function(instructors){
			$scope.instructors=instructors;
		})
	}
	
	$scope.sendMail=function(){
		var mailObj=new Object();
		mailObj.toPerson=$scope.instructor.userEmail;
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
	}
	
	$scope.changeInstructor=function(){
		var schCalObj=new Object();
		schCalObj.calendarDate=new Date($scope.dateOfQuery);
		schCalObj.dayDuration=$scope.dayDuration;
		schCalObj.staffId=$scope.instructor.userId;
		$scope.findAllPlanByDate(schCalObj);
	}
	
	
	
	$scope.changeQueryDate=function(){
		var schCalObj=new Object();
		schCalObj.calendarDate=new Date($scope.dateOfQuery);
		schCalObj.dayDuration=$scope.dayDuration;
		schCalObj.staffId=$scope.instructor.userId;
		$scope.findAllPlanByDate(schCalObj);
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
	
	
	$scope.findAllPlanByDate=function(schCalObj){
		 
		$(".splash").css("display",'');
		
		$http({
			  method:'POST',
			  url: "/bein/private/booking/weekly/findAllPlanByDate",
			  data:angular.toJson(schCalObj)
			}).then(function successCallback(response) {
				$scope.sTimeObjs=response.data;
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$location.path("/login");
				$(".splash").css("display",'none');
			});
	}
	
	
});