package tr.com.beinplanner.zms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStockIn;

@Repository
public interface ZmsStockInRepository  extends CrudRepository<ZmsStockIn, Long>{

	public List<ZmsStockIn> findByProductId(long productId);
	
	
	@Query(value="SELECT b.product_id,b.STK_IDX STK_IDX"
			+ "			,b.stock_in_date stock_in_date "  
			+ "			,b.stock_comment stock_comment "  
			+ "			,b.staff_id staff_id "  
			+ "			,b.stock_price stock_price "  
			+ "			,b.stock_count stock_count "  
			+"				 FROM zms_stock_in b " + 
			"				 WHERE b.PRODUCT_ID IN (SELECT PRODUCT_ID FROM zms_product WHERE FIRM_ID=:firmId)	",nativeQuery=true )
	public List<ZmsStockIn> findAllZmsStockInGroupByProduct(@Param("firmId") int firmId);
	
	
	
	@Query(value="SELECT b.product_id,b.STK_IDX STK_IDX"
			+ "			,b.stock_in_date stock_in_date "  
			+ "			,b.stock_comment stock_comment "  
			+ "			,b.staff_id staff_id "  
			+ "			,b.stock_price stock_price "  
			+ "			,b.stock_count stock_count "  
			+"				 FROM zms_stock_in b " + 
			"				 WHERE b.PRODUCT_ID IN (SELECT PRODUCT_ID FROM zms_product WHERE FIRM_ID=:firmId)"
			+ "                AND b.stock_in_date>=:startDate"
			+ "                AND b.stock_in_date<:endDate	"
			+ "                ORDER BY b.stock_in_date ",nativeQuery=true )
	public List<ZmsStockIn> findStockInMonth(@Param("firmId") int firmId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
	
	
}
