ptBossApp.controller('New_PacketSaleUserController', function($rootScope,$routeParams,$scope,$filter,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.userId;
	
	$scope.restriction;
	$scope.dateFormat;
	$scope.ptCurrency;
	$scope.dateTimeFormat;
	
	$scope.member;
	$scope.packetSales;
	
	$scope.infoSection=true;
	
	$scope.init=function(){
		
		$("#barOptions").attr("width",$("#cnvPanel").width())
		
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
		
	   var dates=[];
	   var data1=[];
	   var data2=[];
	   
	   $.each($scope.packetSales,function(i,ps){
		   dates.push($filter('date')(ps.salesDate,$scope.dateFormat));
		   data1.push(ps.packetPrice);
		   if(ps.packetPaymentFactory!=null){
			   data2.push(ps.packetPaymentFactory.payAmount);
			 }else{
			   data2.push(0);
		     }
		});
	   
	   /**
        * Options for Bar chart
        */
       var barOptions = {
    		   showDatasetLabels : true,
           scaleBeginAtZero : true,
           scaleShowGridLines : true,
           scaleGridLineColor : "rgba(0,0,0,.05)",
           scaleGridLineWidth : 1,
           barShowStroke : true,
           barStrokeWidth : 1,
           barValueSpacing : 5,
           barDatasetSpacing : 1,
           responsive:true
       };
	   
	   /**
        * Data for Bar chart
        */
       var barData = {
           labels: dates,
           datasets: [
               {
                   label: $translate.instant('packetPrice'),
                   fillColor: "rgba(220,220,220,0.5)",
                   strokeColor: "rgba(220,220,220,0.8)",
                   highlightFill: "rgba(220,220,220,0.75)",
                   highlightStroke: "rgba(220,220,220,1)",
                   data: data1
               },
               {
                   label: $translate.instant("payAmount"),
                   fillColor: "rgba(98,203,49,0.5)",
                   strokeColor: "rgba(98,203,49,0.8)",
                   highlightFill: "rgba(98,203,49,0.75)",
                   highlightStroke: "rgba(98,203,49,1)",
                   data: data2
               }
           ]
       };

       var ctx = document.getElementById("barOptions").getContext("2d");
       var myNewChart = new Chart(ctx).Bar(barData, barOptions);
        
	}
	
	
});
