package com.hyw.webSite;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcExcel {

    @Test
    public static void main(String[] args){

        String filePath = "d:\\webSiteUpload\\web_config_req.xlsx";
        ResultSet rs=null;
        Statement stmt = null;
        try{
            Connection conn = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ="+filePath+";READONLY=false");
            stmt=    conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs=stmt.executeQuery( "SELECT * FROM [sheet1$]" );
        }catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }finally{

        }
//
//        Statement stmnt = null;
//        try{
//            stmnt = connection.createStatement();
//            //sc为要查询的sheet名字，注意格式[sc$]
//            String query = "SELECT * FROM [sheet1$] ";
//            ResultSet rs = stmnt.executeQuery( query );
//            int i = 0;
//            while( rs.next() ) {
//                i++;
//                System.out.println( rs.getString( "用户到达清单" )+i );//用户到达清单为列名
//            }
//        }
//        catch( Exception e ) {
//            System.err.println( e );
//        }
//        finally {
//            try {
//                if(!Objects.isNull(stmnt)) stmnt.close();
//                if(!Objects.isNull(connection)) connection.close();
//            }
//            catch( Exception e ) {
//                System.err.println( e );
//            }
//        }
    }
}
