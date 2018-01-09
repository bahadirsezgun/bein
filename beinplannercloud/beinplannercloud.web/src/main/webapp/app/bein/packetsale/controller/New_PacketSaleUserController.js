ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSales;
	
	
	$scope.init=function(){
		
		$("#barOptions").attr("width",$("#cnvPanel").width())
		alert($("#cnvPanel").width());
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
		
	   /**
        * Data for Bar chart
        */
       var barData = {
           labels: ["January", "February"],
           datasets: [
               {
                   label: "My First dataset",
                   fillColor: "rgba(220,220,220,0.5)",
                   strokeColor: "rgba(220,220,220,0.8)",
                   highlightFill: "rgba(220,220,220,0.75)",
                   highlightStroke: "rgba(220,220,220,1)",
                   data: [65, 59]
               },
               {
                   label: "My Second dataset",
                   fillColor: "rgba(98,203,49,0.5)",
                   strokeColor: "rgba(98,203,49,0.8)",
                   highlightFill: "rgba(98,203,49,0.75)",
                   highlightStroke: "rgba(98,203,49,1)",
                   data: [28, 48]
               }
           ]
       };

       var ctx = document.getElementById("barOptions").getContext("2d");
       var myNewChart = new Chart(ctx).Bar(barData, barOptions);
        
	}
	
	
});
