package com.beinplanner.test.income;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import tr.com.beinplanner.bonus.service.UserBonusPaymentClassService;
import tr.com.beinplanner.bonus.service.UserBonusPaymentPersonalService;
import tr.com.beinplanner.dashboard.businessEntity.TodayPayment;
import tr.com.beinplanner.income.dao.PtExpenses;
import tr.com.beinplanner.income.service.PtExpensesService;
import tr.com.beinplanner.packetpayment.service.PacketPaymentService;
import tr.com.beinplanner.packetsale.service.PacketSaleService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.beinplanner","tr.com.beinplanner"})
@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@RunWith(SpringRunner.class)
@SpringBootTest 
public class IncomeTest {

	
	
	@Configuration
    static class ContextConfiguration {
      
		@Bean
        public PacketPaymentService packetPaymentService() {
			PacketPaymentService packetPaymentService = new PacketPaymentService();
            // set properties, etc.
            return packetPaymentService;
        }
		
        @Bean
        public PacketSaleService packetSaleService() {
        	PacketSaleService packetSaleService = new PacketSaleService();
            // set properties, etc.
            return packetSaleService;
        }
   }
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PtExpensesService ptExpensesService;
	
	@Autowired
	UserBonusPaymentPersonalService userBonusPaymentPersonalService;
	
	@Autowired
	UserBonusPaymentClassService userBonusPaymentClassService;
	
	@Test
	public void testTodayIncome() {
		Date startDate=OhbeUtil.getTodayDate();
		Date endDate=OhbeUtil.getDateForNextDate(OhbeUtil.getTodayDate(), 1);
		
		double income=packetPaymentService.findTotalIncomePaymentInDate(startDate,endDate,1);
		
		List<PtExpenses> ptExpenses=ptExpensesService.findPtExpensesForDate(startDate, endDate, 1);
		
		double expenseGeneral=0;
		double incomeGeneral=0;
		
		for (PtExpenses ptExpenses2 : ptExpenses) {
			if(ptExpenses2.getPeInOut()==0)
				expenseGeneral+=ptExpenses2.getPeAmount();
			else
				incomeGeneral+=ptExpenses2.getPeAmount();
		}
		
		income=income+incomeGeneral;
		
		double totalBonusPaymentPersonal=userBonusPaymentPersonalService.findTotalOfDateBonusPayment(1, startDate, endDate);
		double totalBonusPaymentClass=userBonusPaymentClassService.findTotalOfDateBonusPayment(1, startDate, endDate);
		
		double expense=totalBonusPaymentPersonal+totalBonusPaymentClass+expenseGeneral;
				
		TodayPayment todayPayment=new TodayPayment();
		todayPayment.setDayName(DateTimeUtil.getDayNames(startDate));
		todayPayment.setExpenseAmount(expense);
		todayPayment.setIncomeAmount(income);
		todayPayment.setMonthName(DateTimeUtil.getMonthNames(startDate));
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(todayPayment);
	}
}
