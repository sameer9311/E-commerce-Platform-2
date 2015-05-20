package test;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.choc.controller.LoginControl;
import com.choc.dao.CartDao;
import com.choc.dao.PackingTypeDao;
import com.choc.dao.ProductDao;
import com.choc.dao.WishListDao;
import com.choc.model.PackingType;
import com.choc.model.Product;
import com.choc.model.ProductCategory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static com.choc.dao.DbSchema.*;
public class DAOTest {

	public DAOTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws JSONException, org.json.JSONException {
		
	//	WishListDao.getInstance().removeProductFromWishList("USER_10", "P2");
	//	List<Product> pids = WishListDao.getInstance().getProductsFromWishlist("USER_5");
		
		log(PackingTypeDao.getInstance().getPackingTypeByID("CH_REG"));
		CartDao.getInstance().insertIntoCart("USER_9", "P2", "S3", "CH_DLP", 12);
	}

}
