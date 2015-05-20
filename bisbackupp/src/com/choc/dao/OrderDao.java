package com.choc.dao;

import static com.choc.dao.DbSchema.order_id;
import static com.choc.dao.DbSchema.order_state;
import static com.choc.dao.DbSchema.pkg_id;
import static com.choc.dao.DbSchema.product_base_cost;
import static com.choc.dao.DbSchema.product_discount;
import static com.choc.dao.DbSchema.product_id;
import static com.choc.dao.DbSchema.product_seller_quantity;
import static com.choc.dao.DbSchema.product_total_quantity;
import static com.choc.dao.DbSchema.seller_id;
import static com.choc.dao.DbSchema.table_orders;
import static com.choc.dao.DbSchema.table_orders_items;
import static com.choc.dao.DbSchema.table_products;
import static com.choc.dao.DbSchema.table_seller_products;
import static com.choc.dao.DbSchema.table_users_carts;
import static com.choc.dao.DbSchema.user_id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;
/*
import util.DbUtil;
*/

public class OrderDao
{
private static OrderDao dao;

private DBConnectionPool connectionPool;

private OrderDao() {
   connectionPool = DbUtils.getConnectionPool();
}
   public static OrderDao getInstance() {
      if(dao == null) {
         dao = new OrderDao();
      }
      return dao;
   }

   public boolean placeOrder(String userid, String payType)
   {
      boolean ret_val=false;
      Connection conn = null;
      String orderidstr, query1, query2, query3, query4,query5;
      PreparedStatement statement = null, statement1 = null,statement2 = null,statement3 = null,
            statement4 = null,statement5 = null,statement6 = null;
      ResultSet rs = null,rs1 = null,rs2 = null;
      int orderid;
      float total_cost = 0;
      try {
         //fetching max order id
         String query = "Select " + order_id + " from " + table_orders + " where " + order_id +
               ">=all(Select " + order_id + " from " + table_orders + ")";
         
         conn = connectionPool.getConnection();
         statement = conn.prepareStatement(query);
         rs = statement.executeQuery();
         //incrementing order id
         orderidstr = rs.getString(order_id);
         orderid = Integer.parseInt(orderidstr) + 1;
         orderidstr = String.valueOf(orderid);

         //fetching data from carts
         query1 = "Select " + product_id + "," + seller_id + "," + pkg_id + "," + product_seller_quantity +
               "from " + table_users_carts + "where " + user_id + "=?";
         
         statement1 = conn.prepareStatement(query1);
         rs1 = statement1.executeQuery();

         while(rs1.next())
         {
            //inserting the carts data into table_orders_items
            query2 = "Insert into " + table_orders_items + "values(?,?,?,?,?,?)";
           
            statement2 = conn.prepareStatement(query2);
            statement2.setString(1,orderidstr);
            statement2.setString(2,rs1.getString(product_id));
            statement2.setString(3,rs1.getString(seller_id));
            statement2.setString(4,rs1.getString(pkg_id));
            statement2.setInt(5,rs1.getInt(product_seller_quantity));
            statement2.execute();

            //fetching cost of each product in cart according to product id
            query3 = "Select " + product_base_cost + "," + product_discount + "from " + table_seller_products +
                  "where " + product_id + " = ?";
            
            statement3 = conn.prepareStatement(query3);
            statement3.setString(1,rs1.getString(product_id));
            rs2 = statement3.executeQuery();

            //obtaining total cost
            total_cost = total_cost + rs2.getFloat(product_base_cost) * ((100 - rs2.getFloat(product_discount))/100);

         //updating quantity in table_sellers_products
            query4 = "update " + table_seller_products + " set "
                     + product_seller_quantity + " = " + product_seller_quantity +" - ?"
                     + " where " + product_id + " =? AND " + seller_id + "= ?";
            
            statement4 = conn.prepareStatement(query4);
            statement4.setInt(1,rs1.getInt(product_seller_quantity));
            statement4.setString(2,rs1.getString(product_id));
            statement4.setString(3,rs1.getString(seller_id));
            statement4.executeQuery();

          //updating total_quantity in table_products
            query5 = "update " + table_products + " set "
                     + product_total_quantity + " = " + product_total_quantity + " - ?"
                     + " where " + product_id + " =? AND " + seller_id + "= ?";
            
            statement5 = conn.prepareStatement(query5);
            statement5.setInt(1,rs1.getInt(product_seller_quantity));
            statement5.setString(2,rs1.getString(product_id));
            statement5.setString(3,rs1.getString(seller_id));
            statement5.executeQuery();
         }

         //inserting in table_orders
         query1 = "Insert into " + table_orders + "values(?,?,?,?,?)"; //date insertion left
         
         statement6 = conn.prepareStatement(query1);
         statement6.setString(1,userid);
         statement6.setString(2,orderidstr);
         statement6.setFloat(3,total_cost);
         statement6.setString(4,payType);
         statement6.setString(5,"PLC");
         statement6.execute();
         ret_val = true;
      }
      catch (SQLException e) {
         e.printStackTrace();
         ret_val = false;
      }
      finally {
         DbUtils.close(rs2);
         DbUtils.close(rs1);
		   DbUtils.close(rs);
		   DbUtils.close(statement6);
		   DbUtils.close(statement5);
		   DbUtils.close(statement4);
		   DbUtils.close(statement3);
		   DbUtils.close(statement2);
		   DbUtils.close(statement1);
		   DbUtils.close(statement);
		   DbUtils.close(conn);
      }
      return ret_val;
   }

   public boolean updateOrderState(String orderState, String orderId)
   {
      boolean ret_val=false;
      Connection conn = null;
      PreparedStatement statement1 = null,statement2 = null,statement3 = null,statement4 = null;
      ResultSet rs = null;
      try{
         String query = "update "+ table_orders + "set "+ order_state + " = ?"
               +" where " + order_id +" = ?";
        
         conn = connectionPool.getConnection();
         
         statement1 = conn.prepareStatement(query);
         statement1.setString(1, orderState);
         statement1.setString(2, orderId);
         statement1.executeUpdate();
         ret_val=true;

         //Set Cancelled Bit =1 for CAN state... yet to be done!!!
         if(orderState == "CAN")
         {
            query = "Select " + product_seller_quantity + ","+ product_id +
                  "from " + table_orders_items + "where " + order_id + "=?";
            
            statement2 = conn.prepareStatement(query);
            statement2.setString(1, orderId);
            rs = statement2.executeQuery();

            while(rs.next())
            {
               query = "update " + table_products +
                     " set " +product_total_quantity + " = " +product_total_quantity+"+?"
                     + " where "+product_id +" =?";
              
               statement3 = conn.prepareStatement(query);
               statement3.setInt(1,rs.getInt(product_seller_quantity));
               statement3.setString(2,rs.getString(product_id));
               statement3.executeUpdate();


               query = "update " + table_seller_products +
                     " set " +product_seller_quantity + " = " +product_seller_quantity+"+?"
                     + " where "+product_id +" =? AND " + seller_id + "= ?";
               
               statement4 = conn.prepareStatement(query);
               statement4.setInt(1,rs.getInt(product_seller_quantity));
               statement4.setString(2,rs.getString(product_id));
               statement4.setString(3,rs.getString(seller_id));
               statement4.executeUpdate();
            }
         }
      }
      catch (SQLException e) {
         e.printStackTrace();
         ret_val = false;
      }
      finally {
          DbUtils.close(rs);
		    DbUtils.close(statement1);
		    DbUtils.close(statement2);
		    DbUtils.close(statement3);
		    DbUtils.close(statement4);
		    DbUtils.close(conn);
      }
      return ret_val;
   }

   public boolean deleteCancelledProduct()
   {
      boolean ret_val=false;
      PreparedStatement statement1 = null;
      PreparedStatement statement2 = null;
      ResultSet rs = null;
      Connection conn = null;
      
      try{

         String query = "select "+order_id + " from "+table_orders+" where "+order_state +"=CAN";
 
         conn = connectionPool.getConnection();
         statement1 = conn.prepareStatement(query);
         rs = statement1.executeQuery();

       //...updating table_orders_items
         while(rs.next())
         {
             query = "Delete from "+ table_orders_items + " where " + order_id +" =?";
         
             statement1 = conn.prepareStatement(query);
             statement1.setString(1,rs.getString(order_id));
             statement1.executeQuery();
         }

         //...updating table_orders
         query = "Delete from "+ table_orders+ " where " + order_id +" =?";
         
         statement2 = conn.prepareStatement(query);
         statement2.setString(1,rs.getString(order_id));
         statement2.executeQuery();

      }catch (SQLException e) {
         e.printStackTrace();
         ret_val = false;
      }
      finally {
         DbUtils.close(rs);
         DbUtils.close(statement1);
         DbUtils.close(statement2);
         DbUtils.close(conn);
      }
      return ret_val;
   }
}
