ptBossApp.controller('DefinitionCalendarTimesController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.defCalendarTimes=new Object();
	$scope.defCalendarTimes.startTime="0";
	$scope.defCalendarTimes.endTime="0";
	$scope.defCalendarTimes.duration="60";
	$scope.defCalendarTimes.calPeriod="60";
	
	
	$scope.startTimeOfPlan="0";
	$scope.endTimeOfPlan="0";
	
	
	$scope.timeStart=new Array("06:00","06:15","06:30","06:45","07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45"
			,"09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45","11:00","11:15","11:30","11:45","12:00");

	$scope.timeEnd=new Array("18:00","18:15","18:30","18:45","19:00","19:15","19:30","19:45","20:00","20:15","20:30","20:45"
			,"21:00","21:15","21:30","21:45","22:00","22:15","22:30","22:45","23:00","23:15","23:30","23:45","00:00");

	$scope.durationPeriod=new Array("30","45","60");

	$scope.calPeriods=new Array("15","30","45","60");

	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
		findDefCalInfos();
		$scope.levelDetail=false;
			commonService.pageName=$translate.instant("definitionCalendarTimes");
			commonService.pageComment=$translate.instant("definitionCalendarTimesComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
		   
	}
	
	
	
	function findDefCalInfos(){
		$http({
			  method:'POST',
			  url: "/bein/definition/defCalendarTimes/find",
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res!=null){
					$scope.defCalendarTimes=res;
					$scope.defCalendarTimes.duration=""+res.duration;
					$scope.defCalendarTimes.calPeriod=""+res.calPeriod;
					
				}else{
					$scope.defCalendarTimes.startTime="0";
					$scope.defCalendarTimes.endTime="0";
					$scope.defCalendarTimes.duration="60";
					$scope.defCalendarTimes.calPeriod="60";
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
	}
	
	
	
	
	
	
	
	
	$scope.createDefCalTimes =function(){
		
		   $http({
			  method:'POST',
			  url: "/bein/definition/defCalendarTimes/create",
			  data: angular.toJson($scope.defCalendarTimes),
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res.resultStatu=="successs"){
					toastr.success($translate.instant(res.resultMessage));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
		
	};
	
});