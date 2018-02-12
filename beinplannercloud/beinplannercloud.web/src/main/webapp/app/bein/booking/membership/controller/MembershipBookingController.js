ptBossApp.controller('MembershipBookingController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.packetSale;
	
	$scope.scheduleFactory;
	$scope.scheduleTimePlans;
	$scope.freeze=false;
	
	$scope.freezeStartDate=new Date();
	$scope.freezeComment;
	
	
	$scope.initMembershipBooking=function(){
		$.fn.editable.defaults.mode = 'inline';
		
		
		
	}
	
	
	$scope.$on('freezeToPacket', function(event, packetSale) {
		$scope.packetSale=packetSale;
		if($scope.packetSale.programFactory.progDurationType==1){
			$scope.progDurationTypeStr=$translate.instant('daily');
		}else if($scope.packetSale.programFactory.progDurationType==2){
			$scope.progDurationTypeStr=$translate.instant('weekly');
		}else if($scope.packetSale.programFactory.progDurationType==3){
			$scope.progDurationTypeStr=$translate.instant('monthly');
		}
		findSmpPlaning();
	});
	
	
	
	$scope.openFreezeModal=function(){
		$scope.freeze=true;
	}
	
	$scope.saveFreeze=function(){
		var freezeObj=new Object();
		freezeObj.smpStartDate=$scope.freezeStartDate;
		freezeObj.smpId=$scope.smpId;
		freezeObj.smpComment=$scope.freezeComment;
		freezeObj.type=$scope.smp;
		
		$http({
			  method:'POST',
			  url: "/bein/membership/booking/freezeSchedule",
			  data: angular.toJson(freezeObj),
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					toastr.success($translate.instant("success"));
					findSmpPlaning();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.unFreeze=function(smtpId){
		$http({
			method:'POST',
			  url: "/bein/membership/booking/unFreezeSchedule/"+smtpId+"/"+$scope.smpId,
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					toastr.success($translate.instant(res.resultMessage));
					findSmpPlaning();
				}else{
					toastr.fail($translate.instant(res.resultMessage));
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