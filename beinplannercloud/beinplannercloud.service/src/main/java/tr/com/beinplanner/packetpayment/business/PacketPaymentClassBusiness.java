package tr.com.beinplanner.packetpayment.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClass;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentDetailFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentMembershipDetail;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentClassDetail;
import tr.com.beinplanner.packetpayment.facade.IPacketPaymentFacade;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentClassDetailRepository;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentClassRepository;
import tr.com.beinplanner.packetsale.dao.PacketSaleClass;
import tr.com.beinplanner.packetsale.repository.PacketSaleClassRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("packetPaymentClassBusiness")
public class PacketPaymentClassBusiness implements IPacketPayment {


	@Autowired
	PacketPaymentClassRepository packetPaymentClassRepository;
	
	@Autowired
	PacketPaymentClassDetailRepository packetPaymentClassDetailRepository;
	
	
	
	@Autowired
	PacketSaleClassRepository packetSaleClassRepository;
	
	@Autowired
	@Qualifier("packetPaymentMembershipBusiness")
	IPacketPayment iPacketPayment;
	
	@Autowired
	@Qualifier("packetPaymentClassFacade")
	IPacketPaymentFacade iPacketPaymentFacade;
	
	
	@Override
	public HmiResultObj saveIt(PacketPaymentFactory packetPaymentFactory) {
		
		PacketPaymentClass packetPaymentClass=(PacketPaymentClass)packetPaymentFactory;
		PacketPaymentClass ppc=null;
		
		
		PacketPaymentClassDetail packetPaymentClassDetail=new PacketPaymentClassDetail();
		packetPaymentClassDetail.setPayAmount(packetPaymentClass.getPayAmount());
		packetPaymentClassDetail.setPayComment(packetPaymentClass.getPayComment());
		packetPaymentClassDetail.setPayDate(packetPaymentClass.getPayDate());
		packetPaymentClassDetail.setPayType(packetPaymentClass.getPayType());
		
		if(packetPaymentClass.getPayId()!=0) {
		
			PacketPaymentClass ppf=packetPaymentClassRepository.findOne(packetPaymentClass.getPayId());
			if(ppf!=null) {
				
				List<PacketPaymentClassDetail> ppcd=packetPaymentClassDetailRepository.findByPayId(ppf.getPayId());
				ppf.setPacketPaymentDetailFactories(ppcd);
				
				
				double totalPayment=   ppf.getPacketPaymentDetailFactories().stream().mapToDouble(ppdf->{return ppdf.getPayAmount();}).sum();
				packetPaymentClass.setPayAmount(packetPaymentClass.getPayAmount()+totalPayment);
			}else {
				packetPaymentClass.setPayId(0);
			}
			
			ppc=  packetPaymentClassRepository.save(packetPaymentClass);
			packetPaymentClassDetail.setPayId(ppc.getPayId());
			packetPaymentClassDetailRepository.save(packetPaymentClassDetail);
		
		}else {
			
			ppc=  packetPaymentClassRepository.save(packetPaymentClass);
			packetPaymentClassDetail.setPayId(ppc.getPayId());
			packetPaymentClassDetailRepository.save(packetPaymentClassDetail);
			
		}
		
		ppc=packetPaymentClassRepository.findOne(ppc.getPayId());
		
		List<PacketPaymentClassDetail> pppd=packetPaymentClassDetailRepository.findByPayId(ppc.getPayId());
		ppc.setPacketPaymentDetailFactories(pppd);
		
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		hmiResultObj.setResultObj(ppc);
		
		return hmiResultObj;
	}


	@Override
	public HmiResultObj deleteAll(PacketPaymentFactory packetPaymentFactory) {
		HmiResultObj hmiResult= iPacketPaymentFacade.canPaymentDelete(packetPaymentFactory);
		
		if(hmiResult.resultStatu.equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			packetPaymentClassRepository.delete((PacketPaymentClass)packetPaymentFactory);
			hmiResult.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResult.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		}
		
		return hmiResult;
	}


	@Override
	public HmiResultObj deleteDetail(PacketPaymentDetailFactory ppdf) {
		HmiResultObj hmiResult= iPacketPaymentFacade.canPaymentDetailDelete(ppdf);
		PacketPaymentClass ppc=null;
		if(hmiResult.resultStatu.equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			packetPaymentClassDetailRepository.delete((PacketPaymentClassDetail)ppdf);
			ppc=(PacketPaymentClass)packetPaymentClassRepository.findByPayId(((PacketPaymentClassDetail)ppdf).getPayId());
			if(ppc.getPacketPaymentDetailFactories()==null) {
				packetPaymentClassRepository.delete(ppc);
			}else {
				
				double totalPayment=   ppc.getPacketPaymentDetailFactories().stream().mapToDouble(ppdfl->{return ppdfl.getPayAmount();}).sum();
				ppc.setPayAmount(totalPayment);
				ppc=  packetPaymentClassRepository.save(ppc);
				
			}
			
			hmiResult.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResult.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		}
		
		return hmiResult;
	}


	@Override
	public PacketPaymentFactory findPacketPaymentById(long id) {
		
		PacketPaymentClass ppf=packetPaymentClassRepository.findBySaleId(id);
		if(ppf!=null) {
			List<PacketPaymentClassDetail> pppd=packetPaymentClassDetailRepository.findByPayId(ppf.getPayId());
			ppf.setPacketPaymentDetailFactories(pppd);
		}
		return ppf;
		
		
	}


	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		PacketPaymentClass ppf=packetPaymentClassRepository.findBySaleId(saleId);
		if(ppf!=null) {
			List<PacketPaymentClassDetail> pppd=packetPaymentClassDetailRepository.findByPayId(ppf.getPayId());
			ppf.setPacketPaymentDetailFactories(pppd);
		}
		return ppf;
	}


	@Override
	public double findTotalIncomePaymentInDate(Date startDate, Date endDate, int firmId) {
		 double totalPayment=iPacketPayment.findTotalIncomePaymentInDate(startDate, endDate, firmId);
		 List<PacketPaymentClass> packetPaymentClasses=packetPaymentClassRepository.findPacketPaymentClassForDate(startDate,endDate,firmId);
		 double totalPaymentForClass=packetPaymentClasses.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		 return totalPayment+totalPaymentForClass;
	}


	@Override
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId) {
		List<PacketPaymentFactory> packetPaymentFactories=iPacketPayment.findLast5packetPaymentsInChain(firmId);
		packetPaymentFactories.addAll(packetPaymentClassRepository.findLast5packetPayments(firmId));
		return packetPaymentFactories;
	}


	@Override
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId) {
		LeftPaymentInfo leftPaymentInfo=iPacketPayment.findLeftPacketPaymentsInChain(firmId);
		
		List<PacketPaymentClass> packetPaymentClasss= packetPaymentClassRepository.findLeftPacketPaymentClass(firmId);
		double leftPP=packetPaymentClasss
				.stream()
				.mapToDouble(ppp->{
					             	PacketSaleClass packetSaleClass=packetSaleClassRepository.findOne(ppp.getSaleId());              
					             	double payment= packetSaleClass.getPacketPrice()-ppp.getPayAmount();
					             	return payment;
							    }
				).sum();
		
		leftPaymentInfo.setLeftPayment(leftPaymentInfo.getLeftPayment()+leftPP);
		leftPaymentInfo.setLeftPaymentCount(packetPaymentClasss.size()
											+leftPaymentInfo.getLeftPaymentCount());

		List<PacketSaleClass> packetSaleClasss=packetSaleClassRepository.findPacketSaleClassWithNoPayment(firmId);
		
		double leftPPN=packetSaleClasss
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN+leftPaymentInfo.getNoPayment());
		leftPaymentInfo.setNoPaymentCount(leftPaymentInfo.getNoPaymentCount()
				+packetSaleClasss.size());
		
		return leftPaymentInfo;
	}

}
