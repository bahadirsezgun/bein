ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSales;
	
	
	$scope.init=function(){
		$scope.userId=$routeParams.userId;
		
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetSaleUserProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			
			$scope.getMember($routeParams.userId);
			$scope.getMemberPacketSale($routeParams.userId);
		});
		
		commonService.getRestriction().then(function(restriction){
			$scope.restriction=restriction;
		});
	}
	
	
	$scope.getMember=function(userId){
		$http({method:"POST", url:"/bein/member/findById/"+userId}).then(function(response){
			$scope.member=response.data.resultObj;
		});
	}
	
	$scope.getMemberPacketSale=function(userId){
		$http({method:"POST", url:"/bein/packetsale/findUserBoughtPackets/"+userId}).then(function(response){
			$scope.packetSales=response.data;
			getDataToGraph()
		});
	}
	
	
	
	
   function getDataToGraph(){
		
	   var data1=[];
	   var data2=[];
	   
	   
	   for (var i = 0, len = $scope.packetSales.length; i < len; i++) {
		 
		   var darr1=[];
		   darr1.push(i);
		   darr1.push($scope.packetSales[i].packetPrice)
		   
		   var darr2=[];
		   darr2.push(i);
		   darr2.push($scope.packetSales[i].packetPrice)
		   
		   data1.push(darr1);
		   data2.push(darr2);
		   
	    }
	   
	
        var chartUsersOptions = {
            series: {
                splines: {
                    show: true,
                    tension: 0.4,
                    lineWidth: 1,
                    fill: 0.4
                },
            },
            grid: {
                tickColor: "#f0f0f0",
                borderWidth: 1,
                borderColor: 'f0f0f0',
                color: '#6a6c6f'
            },
            colors: [ "#62cb31", "#fedde4"],
        };

        $.plot($("#flot-line-chart"), [data1, data2], chartUsersOptions);
        
	}
	
	
});
