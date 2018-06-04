ptBossApp.controller('MemberReportSaleController', function($rootScope,$routeParams,$scope,$filter,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	$scope.disabled=true;
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSale;
	$scope.userTests;
	$scope.measurements;
	
$scope.init=function(){
		
		
		$scope.userId=$routeParams.userId;
		$scope.saleId=$routeParams.saleId;
		$scope.saleType=$routeParams.saleType;
		
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetSaleUserProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.getMember($routeParams.userId);
			$scope.getMemberPacketSale($routeParams.userId,$routeParams.saleId,$routeParams.saleType);
		});
		
		
		
		
		
	}
	

	$scope.go_back = function() { 
		$location.path('/packetsale/saletouser/'+$scope.userId);
	};

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


	$scope.getMember=function(userId){
		$http({method:"POST", url:"/bein/member/findById/"+userId}).then(function successCallback(response){
			$scope.member=response.data.resultObj;
			$scope.findTest();
			$scope.findMeasurement();
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}

	$scope.getMemberPacketSale=function(userId,saleId,saleType){
		return $http({method:"POST", url:"/bein/packetsale/findUserBoughtPacketsForReportSale/"+userId+"/"+saleId+"/"+saleType}).then(function successCallback(response){
			$scope.packetSale=response.data;
			
		}, function errorCallback(response) {
			$location.path("/login");
		});
	}
	
	$scope.findTest =function(){

		
		 $http({
			  method: 'POST',
			  url: "/bein/userTest/usertest/find/"+$scope.userId,
			}).then(function successCallback(response) {
				$scope.userTests=response.data.resultObj;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	$scope.findMeasurement=function(){
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
			wobj[0]=i;
			wobj[1]=meas.kilo;
			data1[i]=wobj;
			
			var kobj=new Array();
			kobj[0]=i;
			kobj[1]=meas.kas;
			data2[i]=kobj;
			
			var yobj=new Array();
			yobj[0]=i;
			yobj[1]=meas.yag;
			data3[i]=yobj;
			
			
			
		});
		
		
		
	
        var chartUsersOptions = {
            series: {
                splines: {
                    show: false,
                    tension: 1,
                    lineWidth: 1
                },
            },
            grid: {
                tickColor: "#f0f0f0",
                borderWidth: 1,
                borderColor: 'f0f0f0',
                color: '#6a6c6f'
            },
            xaxis:{
            	tickDecimals:0,
            },
            
            colors: [ "#22ff00", "#ff0067","#0038ff"],
        };

        $.plot($("#meas-chart"), [data1, data2,data3], chartUsersOptions);
		}
	}
	
	
	

	$scope.sendMailToMember=function(){
		
		setTimeout(function(){
			
			var mailObj=new Object();
			mailObj.toPerson=$scope.member.userEmail;
			mailObj.subject=$translate.instant("membershipInfo");
			mailObj.content="";//$scope.sendMailPerson+", "+$scope.mailContent;
			mailObj.htmlContent=$("#report").html();
			
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

});