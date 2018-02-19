ptBossApp.controller('PrivateBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.times;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.instructorCount=1;
	
	$scope.scheduleTimeObjs;
	$scope.dayDuration="1";
	$scope.dateOfQuery=new Date();
	
	$scope.timePlan=false;
	$scope.timePlanTop=0;
	$scope.timePlanLeft=0;
	
	
	$scope.init=function(){
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
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
	
	$scope.onDropComplete = function(data, evt) {
	      console.log("drop success, data:", data);
	    /*  var index = $scope.droppedObjects.indexOf(data);
	      if (index == -1)
	        $scope.droppedObjects.push(data);*/
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
	
	$scope.selectedTime="0";
	$scope.selectedStaffId="0";
	
	
	$scope.showTimePlan=function(sctp,$event){
		$scope.timePlan=true;
		$scope.timePlanTop=400;
		
		//alert($($event.target).offset().left);
		var offsetDistance=$($event.target).parent().parent().width()/2;
		var leftPosition=$($event.target).offset().left;
		var widthDiv=$("#tpDiv").width();
		var ww= $(window).width()/2;
		var targetElementW=$($event.target).width();
		
		
		var leftPositionDiv=0;
		if(ww<leftPosition){
			leftPositionDiv=leftPosition-widthDiv-offsetDistance;
			
		}else{
			leftPositionDiv=leftPosition+offsetDistance+targetElementW;
		}
		
		
		var heightDiv=$("#tpDiv").height();
		var topPosition=$($event.target).offset().top;
		var targetElementH=$($event.target).height();
		var heightDiv=$("#tpDiv").height();
		var topPositionDiv=topPosition-heightDiv/2-targetElementH/2;
		
		
		
		$("#tpDiv").offset({ top: topPositionDiv, left: leftPositionDiv });
		
		
		
		
	}
	
	$scope.overTimePlan=function(sctp){
		$scope.selectedTime=sctp.planStartDate;
		$scope.selectedStaffId=sctp.schtStaffId;
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
