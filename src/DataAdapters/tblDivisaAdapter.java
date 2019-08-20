/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAdapters;

import Beans.Currency;
import Persistance.JDBCMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class tblDivisaAdapter {

    JDBCMySQL conn = new JDBCMySQL();
    Connection c = null;

    public List<Currency> Select() {

        try {
            List<Currency> rsDivisa = new ArrayList<>();
            c = conn.connectionDB();
            PreparedStatement verificarStmt
                    = c.prepareStatement("SELECT"
                            + "   ID   "
                            + "   ,Nombre"
                            + "   ,valor"
                            + "   FROM catdivisa");

            ResultSet rs = verificarStmt.executeQuery();

            while (rs.next()) {
                Currency cur = new Currency(rs.getInt("ID"), rs.getString("nombre"), rs.getDouble("valor"));
                /*cur.setValue(rs.getInt("valor"));*/
                //cur.setName(rs.getString("nombre"));
                rsDivisa.add(cur);

            }
            verificarStmt.close();
            rs.close();

            return rsDivisa;
        } catch (SQLException ex) {
            Logger.getLogger(tblDivisaAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            conn.disconnection(c);
        }
        return null;
    }

    public void Delete(int index_id) {

        try {
            c = conn.connectionDB();
            PreparedStatement verificarStmt
                    = c.prepareStatement("DELETE FROM catdivisa WHERE"
                            + "   id   "
                            + "   =    "
                            + index_id);

            verificarStmt.executeUpdate();

            verificarStmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(tblDivisaAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            conn.disconnection(c);
        }
    }

    public int Update(int index_id, String name, Double value) {
        try {
            int success = 0;
            c = conn.connectionDB();
            PreparedStatement verificarStmt
                    = c.prepareStatement("UPDATE catdivisa SET nombre = ?, valor = ? WHERE id = ?");
            verificarStmt.setString(1, name);
            verificarStmt.setDouble(2, value);
            verificarStmt.setInt(3, index_id);

            success = verificarStmt.executeUpdate();

            verificarStmt.close();
            return success;

        } catch (SQLException ex) {
            Logger.getLogger(tblDivisaAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            conn.disconnection(c);
        }
        return 0;
    }

    public int Insert(String name, Double value) {
        try {
            int success = 0;
            c = conn.connectionDB();
            PreparedStatement verificarStmt;

            verificarStmt = c.prepareStatement("INSERT INTO catdivisa(nombre, valor) VALUES (?,?);");

            verificarStmt.setString(1, name);
            verificarStmt.setDouble(2, value);
            success = verificarStmt.executeUpdate();

            verificarStmt.close();
            return success;
        } catch (SQLException ex) {
            Logger.getLogger(tblDivisaAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.disconnection(c);
        }
        return 0;
    }
}
