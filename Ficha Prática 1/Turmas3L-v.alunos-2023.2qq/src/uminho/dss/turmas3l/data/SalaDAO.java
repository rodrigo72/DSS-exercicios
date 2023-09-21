package uminho.dss.turmas3l.data;

import java.sql.*;
import java.util.*;

import uminho.dss.turmas3l.business.Aluno;
import uminho.dss.turmas3l.business.Sala;

public class SalaDAO implements Map<String, Sala> {
    private static SalaDAO singleton = null;
    private SalaDAO() {}

    public static SalaDAO getInstance() {
        if (SalaDAO.singleton == null) {
            SalaDAO.singleton = new SalaDAO();
        }
        return SalaDAO.singleton;
    }

    @Override
    public int size() {
        int res = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM salas")) {
            if (rs.next()) res = rs.getInt(1); // primeira coluna, primeira linha
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean res;
        try (
                Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT Num FROM salas WHERE Num='" + key.toString() + "'");
        ) {
            res = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean containsValue(Object value) {
        Sala s = (Sala) value;
        return this.containsKey(s.getNumero());
    }

    @Override
    public Sala get(Object key) {
        Sala s = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("Select * FROM salas WHERE Num='" + key.toString() + "'")
        ) {
            if (rs.next()) {
                s = new Sala(
                        rs.getString("Num"),
                        rs.getString("Edificio"),
                        rs.getInt("Capacidade")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    @Override
    public Sala put(String key, Sala s) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
        ) {
            stm.executeUpdate("INSERT INTO salas VALUES ('"
                    + s.getNumero() + "', '"
                    + s.getEdificio() + "', "
                    + s.getCapacidade() + ")"
                    + "ON DUPLICATE KEY UPDATE Edificio=Values(Edificio), " +
                    "Capacidade=Values(Capacidade)"
            );



        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return this.get(key);
    }

    @Override
    public Sala remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Sala> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Sala> values() {
        Collection<Sala> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM salas")) {
            while (rs.next()) {
                res.add(this.get(rs.getString("Num")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Set<Entry<String, Sala>> entrySet() {
        return null;
    }
}
