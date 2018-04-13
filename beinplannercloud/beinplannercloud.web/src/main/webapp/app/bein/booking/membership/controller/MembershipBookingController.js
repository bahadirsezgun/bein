ptBossApp.controller('MembershipBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.packetSale;
	
	$scope.scheduleFactory;
	$scope.scheduleTimePlans;
	$scope.freeze=false;
	
	$scope.freezeStartDate=new Date();
	$scope.freezeComment="";
	
	
	$scope.progDuration;
	
	$scope.initMembershipBooking=function(){
		$.fn.editable.defaults.mode = 'inline';
	}
	
	
	$scope.$on('freezeToPacket', function(event, packetSale) {
		$scope.packetSale=packetSale;
		$scope.progDuration=$scope.packetSale.programFactory.freezeDuration;
		
		if($scope.packetSale.programFactory.freezeDurationType==0){
			$scope.progDurationTypeStr=$translate.instant('daily');
		}else if($scope.packetSale.programFactory.freezeDurationType==1){
			$scope.progDurationTypeStr=$translate.instant('weekly');
		}else if($scope.packetSale.programFactory.freezeDurationType==2){
			$scope.progDurationTypeStr=$translate.instant('monthly');
		}
		
		
		$scope.scheduleFactory=$scope.packetSale.scheduleFactory;
		$scope.scheduleTimePlans=$scope.scheduleFactory.scheduleMembershipTimePlans;
		
		
	});
	
	
	
	
	$scope.saveFreeze=function(){
		var smp=new Object();
		smp.smpStartDate=$scope.freezeStartDate;
		smp.smpId=$scope.packetSale.scheduleFactory[0].smpId;
		smp.smpComment=$scope.freezeComment;
	
		
		$http({
			  method:'POST',
			  url: "/bein/membership/booking/freezeSchedule",
			  data: angular.toJson(smp)
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					toastr.success($translate.instant("success"));
					//findSmpPlaning();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	
	
	function findSmpPlaning(){
		$http({
			method:'POST',
			  url: "/bein/membership/booking/findScheduleFactoryPlanBySaleId/"+$scope.packetSale.saleId,
			}).then(function successCallback(response) {
				var res=response.data;
				if(res!=null){
					$scope.scheduleFactory=res;
					$scope.scheduleTimePlans=res.scheduleMembershipTimePlans;
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
});