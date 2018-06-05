package tr.com.beinplanner.zms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.zms.dao.ZmsStockOut;

@Repository
public interface ZmsStockOutRepository  extends CrudRepository<ZmsStockOut, Long>{

	public List<ZmsStockOut> findByProductId(long productId);
	
	@Query(value="SELECT b.PRODUCT_ID"
			+ "			,SUM(b.SELL_PRICE) SELL_PRICE "  
			+ "			,SUM(b.SELL_COUNT) SELL_COUNT "  
			+ "			,COUNT(b.USER_ID) USER_ID "  
			+ "			,MAX(b.SELL_OUT_DATE) SELL_OUT_DATE "  
			+ "			,COUNT(b.SELL_COMMENT) SELL_COMMENT "  
			+ "			,COUNT(b.SELL_STATU) SELL_STATU "  
			+ "			,COUNT(b.STAFF_ID) STAFF_ID "  
			+"				 FROM zms_stock_out b " + 
			"				 WHERE b.PRODUCT_ID IN (SELECT PRODUCT_ID FROM zms_product WHERE FIRM_ID=:firmId)"
			+ "                AND b.SELL_STATU=:statu "
			+ "				 GROUP BY b.product_id	",nativeQuery=true )
	public List<ZmsStockOut> findAllZmsStockInGroupByProduct(@Param("firmId") int firmId,@Param("statu") int statu);
	
	
	@Query(value="SELECT b.PRODUCT_ID"
			+ "			,SUM(b.SELL_PRICE) SELL_PRICE "  
			+ "			,SUM(b.SELL_COUNT) SELL_COUNT "  
			+ "			,COUNT(b.USER_ID) USER_ID "  
			+ "			,MAX(b.SELL_OUT_DATE) SELL_OUT_DATE "  
			+ "			,COUNT(b.SELL_COMMENT) SELL_COMMENT "  
			+ "			,COUNT(b.SELL_STATU) SELL_STATU "  
			+ "			,COUNT(b.STAFF_ID) STAFF_ID "  
			+"				 FROM zms_stock_out b " + 
			"				 WHERE b.PRODUCT_ID =:productId "
			+ "                AND b.SELL_STATU=:statu "
			+ "				 GROUP BY b.product_id	",nativeQuery=true )
	public ZmsStockOut findZmsStockOutByProduct(@Param("statu") int statu,@Param("productId") long productId);
	
}
