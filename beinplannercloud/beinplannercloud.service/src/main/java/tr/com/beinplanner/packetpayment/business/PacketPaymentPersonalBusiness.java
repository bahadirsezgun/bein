package tr.com.beinplanner.packetpayment.business;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LeftPaymentInfo;
import tr.com.beinplanner.packetpayment.comparator.PacketPaymentComparator;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentDetailFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentFactory;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonal;
import tr.com.beinplanner.packetpayment.dao.PacketPaymentPersonalDetail;
import tr.com.beinplanner.packetpayment.facade.IPacketPaymentFacade;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentPersonalDetailRepository;
import tr.com.beinplanner.packetpayment.repository.PacketPaymentPersonalRepository;
import tr.com.beinplanner.packetsale.dao.PacketSalePersonal;
import tr.com.beinplanner.packetsale.repository.PacketSalePersonalRepository;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("packetPaymentPersonalBusiness")
public class PacketPaymentPersonalBusiness implements IPacketPayment {

	
	@Autowired
	PacketPaymentPersonalRepository packetPaymentPersonalRepository;
	
	@Autowired
	PacketPaymentPersonalDetailRepository packetPaymentPersonalDetailRepository;
	
	
	@Autowired
	PacketSalePersonalRepository packetSalePersonalRepository;
	
	@Autowired
	@Qualifier("packetPaymentClassBusiness")
	IPacketPayment iPacketPayment;
	
	@Autowired
	@Qualifier("packetPaymentPersonalFacade")
	IPacketPaymentFacade iPacketPaymentFacade;
	
	
	@Override
	public HmiResultObj saveIt(PacketPaymentFactory packetPaymentFactory) {
		
		PacketPaymentPersonal packetPaymentPersonal=(PacketPaymentPersonal)packetPaymentFactory;
		PacketPaymentPersonal ppc=null;
		
		
		PacketPaymentPersonalDetail packetPaymentPersonalDetail=new PacketPaymentPersonalDetail();
		packetPaymentPersonalDetail.setPayAmount(packetPaymentPersonal.getPayAmount());
		packetPaymentPersonalDetail.setPayComment(packetPaymentPersonal.getPayComment());
		packetPaymentPersonalDetail.setPayDate(packetPaymentPersonal.getPayDate());
		packetPaymentPersonalDetail.setPayType(packetPaymentPersonal.getPayType());
		
		
		PacketPaymentPersonal ppf=packetPaymentPersonalRepository.findBySaleId(packetPaymentPersonal.getSaleId());
		
		
		if(ppf!=null) {
			double totalPayment=   ppf.getPacketPaymentDetailFactories().stream().mapToDouble(ppdf->{return ppdf.getPayAmount();}).sum();
			packetPaymentPersonal.setPayAmount(packetPaymentPersonal.getPayAmount()+totalPayment);
			packetPaymentPersonal.setPayId(ppf.getPayId());
		}
			
		ppc=  packetPaymentPersonalRepository.save(packetPaymentPersonal);
		packetPaymentPersonalDetail.setPayId(ppc.getPayId());
		packetPaymentPersonalDetailRepository.save(packetPaymentPersonalDetail);
	
		   
		
		
		
		
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
			packetPaymentPersonalRepository.delete((PacketPaymentPersonal)packetPaymentFactory);
			hmiResult.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResult.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		}
		
		return hmiResult;
	}


	@Override
	public HmiResultObj deleteDetail(PacketPaymentDetailFactory ppdf) {
		HmiResultObj hmiResult= iPacketPaymentFacade.canPaymentDetailDelete(ppdf);
		
		if(hmiResult.resultStatu.equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			packetPaymentPersonalDetailRepository.delete((PacketPaymentPersonalDetail)ppdf);
			PacketPaymentPersonal ppc=packetPaymentPersonalRepository.findOne(((PacketPaymentPersonalDetail)ppdf).getPayId());
			if(ppc.getPacketPaymentDetailFactories()==null) {
				packetPaymentPersonalRepository.delete(ppc);
			}
			
			hmiResult.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResult.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		}
		
		return hmiResult;
	}



	@Override
	public PacketPaymentFactory findPacketPaymentById(long id) {
		return packetPaymentPersonalRepository.findOne(id);
	}




	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		return packetPaymentPersonalRepository.findBySaleId(saleId);
	}




	@Override
	public double findTotalIncomePaymentInDate(Date startDate, Date endDate, int firmId) {
		 double totalPayment=iPacketPayment.findTotalIncomePaymentInDate(startDate, endDate, firmId);
		 List<PacketPaymentPersonal> packetPaymentPersonals=packetPaymentPersonalRepository.findPacketPaymentPersonalForDate(startDate,endDate,firmId);
		 double totalPaymentForPersonal=packetPaymentPersonals.stream().mapToDouble(ppp->ppp.getPayAmount()).sum();
		 return totalPayment+totalPaymentForPersonal;
	}




	@Override
	public List<PacketPaymentFactory> findLast5packetPaymentsInChain(int firmId) {
		List<PacketPaymentFactory> packetPaymentFactories=iPacketPayment.findLast5packetPaymentsInChain(firmId);
		packetPaymentFactories.addAll(packetPaymentPersonalRepository.findLast5packetPayments(firmId));
		Collections.sort(packetPaymentFactories, new PacketPaymentComparator());
		return packetPaymentFactories;
	}




	@Override
	public LeftPaymentInfo findLeftPacketPaymentsInChain(int firmId) {
		
		LeftPaymentInfo leftPaymentInfo=iPacketPayment.findLeftPacketPaymentsInChain(firmId);
		
		List<PacketPaymentPersonal> packetPaymentPersonals= packetPaymentPersonalRepository.findLeftPacketPaymentPersonal(firmId);
		double leftPP=packetPaymentPersonals
				.stream()
				.mapToDouble(ppp->{
					             	PacketSalePersonal packetSalePersonal=packetSalePersonalRepository.findOne(ppp.getSaleId());              
					             	double payment= packetSalePersonal.getPacketPrice()-ppp.getPayAmount();
					             	return payment;
							    }
				).sum();
		
		leftPaymentInfo.setLeftPayment(leftPaymentInfo.getLeftPayment()+leftPP);
		leftPaymentInfo.setLeftPaymentCount(packetPaymentPersonals.size()
											+leftPaymentInfo.getLeftPaymentCount());

		List<PacketSalePersonal> packetSalePersonals=packetSalePersonalRepository.findPacketSalePersonalWithNoPayment(firmId);
		
		double leftPPN=packetSalePersonals
				.stream()
				.mapToDouble(psp->psp.getPacketPrice()
				).sum();
		
		
		leftPaymentInfo.setNoPayment(leftPPN+leftPaymentInfo.getNoPayment());
		leftPaymentInfo.setNoPaymentCount(leftPaymentInfo.getNoPaymentCount()
				+packetSalePersonals.size());
		
		return leftPaymentInfo;
	}


}
