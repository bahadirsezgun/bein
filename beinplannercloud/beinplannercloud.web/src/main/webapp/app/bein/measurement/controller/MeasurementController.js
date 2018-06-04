ptBossApp.controller('MeasurementController', function($rootScope,$scope,$translate,homerService,commonService,$http) {

$scope.measurements;
$scope.measurement=new Object();
$scope.measurement.measDate=new Date();
$scope.measurement.fitAciklama="";

$scope.showMeas=false;

	$scope.initMEAS=function(){
		$scope.find();
	};
	
	$scope.find=function(){
		$(".splash").css("display",'');
		$http({
			  method:'POST',
			  url: "/bein/measurement/find/"+$scope.userId,
			}).then(function successCallback(response) {
				
				$scope.measurements=response.data;
				$(".splash").css("display",'none');
				getDataToGraph();
			}, function errorCallback(response) {
				toastr.error($translate.instant(response.data.resultMessage));
				$(".splash").css("display",'none');
			});
	};
	
	$scope.create=function(){
		$(".splash").css("display",'');
		$scope.measurement.userId=$scope.userId;
		$http({
			  method:'POST',
			  url: "/bein/measurement/create",
			  data:angular.toJson($scope.measurement)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.find();
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				toastr.error($translate.instant(response.data.resultMessage));
				$(".splash").css("display",'none');
			});
	};
	
	$scope.update=function(measurement){
		$scope.measurement=measurement;
		$scope.measurement.measDate=new Date(measurement.measDate);
		$scope.showMeas=false;
	}
	
	$scope.deleteMeas=function(measurement){
		$(".splash").css("display",'');
		
		$http({
			  method:'POST',
			  url: "/bein/measurement/delete",
			  data:angular.toJson(measurement)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					$scope.find();
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				toastr.error($translate.instant(response.data.resultMessage));
				$(".splash").css("display",'none');
			});
	};
	
    function getDataToGraph(){
		
		var inOut=$scope.measurements;
		
		var weightArr=new Array();
		var muscleArr=new Array();
		var i=1;
		if($scope.measurements.length>0){
			
			$scope.showMeas=true;
		
			
			var data1 = new Array();
			var data2 = new Array();
			var data3 = new Array();
		angular.forEach($scope.measurements,function(meas,i){
			var wobj=new Array();
			wobj[0]=i+1;
			wobj[1]=meas.kilo;
			data1[i]=wobj;
			
			var kobj=new Array();
			kobj[0]=i+1;
			kobj[1]=meas.kas;
			data2[i]=kobj;
			
			var yobj=new Array();
			yobj[0]=i+1;
			yobj[1]=meas.yag;
			data3[i]=yobj;
			
			
			
		});
		
		
		console.log(data1);
		console.log(data2);
		console.log(data3);
		
	
        var chartUsersOptions = {
            series: {
            	/*splines: {
                    show: false,
                    tension: 0.4,
                    lineWidth: 1
                },*/
                curvedLines: {  active: true }
            },
            grid: {
                tickColor: "#f0f0f0",
                borderWidth: 1,
                borderColor: 'f0f0f0',
                color: '#6a6c6f'
            },
            xaxis:{
            	tickDecimals:1,
            },
            
            colors: [ "#22ff00", "#ff0067","#0038ff"],
        };
        
         $.plot($("#meas-chart"), [{data: data1, lines: { show: true, lineWidth: 3}, curvedLines: {apply:true}}
         						 , {data: data2, lines: { show: true, lineWidth: 3}, curvedLines: {apply:true}}
         						  ,{data: data3, lines: { show: true, lineWidth: 3}, curvedLines: {apply:true}}], chartUsersOptions);
		}
	}
});