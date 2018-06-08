ptBossApp.controller('ZmsStockDashboardController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.zmsStockOutOrder;
	$scope.zmsStocksInMonths;
	$scope.zmsStocksForStatus;
	$scope.zmsStocksForDeptors;
	$scope.dateFormat;
	$scope.year;
	$scope.prevYear;
	$scope.ptCurrency;
	
	
	$scope.thisYearIncome=0;
	$scope.showPayment=false;
	$scope.showDetail=false;
	$scope.showDash=true;
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
    ,{value:"1",name:$translate.instant("january")}
   ,{value:"2",name:$translate.instant("february")}
   ,{value:"3",name:$translate.instant("march")}
   ,{value:"4",name:$translate.instant("april")}
   ,{value:"5",name:$translate.instant("may")}
   ,{value:"6",name:$translate.instant("june")}
   ,{value:"7",name:$translate.instant("july")}
   ,{value:"8",name:$translate.instant("august")}
   ,{value:"9",name:$translate.instant("september")}
   ,{value:"10",name:$translate.instant("october")}
   ,{value:"11",name:$translate.instant("november")}
   ,{value:"12",name:$translate.instant("december")}
   );
	
	$scope.years=new Array();
	
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$('.animate-panel').animatePanel();
		
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		
		$scope.paymentDetailPage="";
		
		$scope.year=year;
		$scope.month=date.getMonth()+1;
		$scope.prevYear=year-1;
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			$(".splash").css("display",'');
			findStockStatusByYear();
			findStockByMonths();
			findStockOutForDeptors();
		});
	}
	
	$scope.yearChange=function(){
		findStockStatusByYear();
	}
	
	function findStockStatusByYear(){
		$http({
			  method:'POST',
			  url: "/bein/zms/dashboard/findStockStatusByYear/"+$scope.year,
			}).then(function successCallback(response) {
				$scope.zmsStockOutOrder=response.data.resultObj;
				
				$scope.thisYearIncome=$scope.zmsStockOutOrder.preOrderedPrice
				+$scope.zmsStockOutOrder.orderedPrice
				+$scope.zmsStockOutOrder.sendedPrice
				+$scope.zmsStockOutOrder.donePrice;
				
				$scope.totalOrderCount =$scope.zmsStockOutOrder.preOrderedCount
				+$scope.zmsStockOutOrder.orderedCount
				+$scope.zmsStockOutOrder.sendedCount
				+$scope.zmsStockOutOrder.doneCount;
				
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	function findStockByMonths(){
		$http({
			  method:'POST',
			  url: "/bein/zms/dashboard/findStockByMonths/"+$scope.year,
			}).then(function successCallback(response) {
				$scope.zmsStocksInMonths=response.data.resultObj;
				getDataToGraph();
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	
	$scope.findStockDetail=function(status){
		$http({
			  method:'POST',
			  url: "/bein/zms/dashboard/findZmsStockOutByStatusAndDate/"+$scope.year+"/0/"+status,
			}).then(function successCallback(response) {
				$scope.zmsStocksForStatus=response.data.resultObj;
				$scope.showPayment=false;
				$scope.showDetail=true;
				$scope.showDash=false;
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	
	function findStockOutForDeptors(){
		$http({
			  method:'POST',
			  url: "/bein/zms/dashboard/findStockOutForDeptors/",
			}).then(function successCallback(response) {
				$scope.zmsStocksForDeptors=response.data;
				
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	
	$scope.zmsStockOutDetail;
	
	$scope.payZmsStockOut=function(stockOut){
		$scope.zmsStockOutDetail=stockOut;
		$scope.paymentDetailPage="/bein/zms/zmsPayment.html";
		$scope.showPayment=true;
		$scope.showDetail=false;
		$scope.showDash=false;
	
	}
	
   function getDataToGraph(){
		
		var inOut=$scope.zmsStocksInMonths;
		
		var data1 = [ [1, inOut[0].stockInCount]
					, [2, inOut[1].stockInCount]
					, [3, inOut[2].stockInCount]
					, [4, inOut[3].stockInCount]
					, [5, inOut[4].stockInCount]
					, [6, inOut[5].stockInCount]
					, [7, inOut[6].stockInCount]
					, [8, inOut[7].stockInCount]
					, [9, inOut[8].stockInCount]
					, [10, inOut[9].stockInCount]
					, [11, inOut[10].stockInCount]
					, [12, inOut[11].stockInCount] ];
		
		var data2 = [ [1, inOut[0].stockOutCount]
					, [2, inOut[1].stockOutCount]
					, [3, inOut[2].stockOutCount]
					, [4, inOut[3].stockOutCount]
					, [5, inOut[4].stockOutCount]
					, [6, inOut[5].stockOutCount]
					, [7, inOut[6].stockOutCount]
					, [8, inOut[7].stockOutCount]
					, [9, inOut[8].stockOutCount]
					, [10, inOut[9].stockOutCount]
					, [11, inOut[10].stockOutCount]
					, [12, inOut[11].stockOutCount] ];
		
	
		
		
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
	
});