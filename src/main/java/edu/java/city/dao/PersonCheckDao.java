package edu.java.city.dao;

import edu.java.city.domain.PersonRequest;
import edu.java.city.domain.PersonResponse;
import edu.java.city.exception.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {

    public static final String SQL_REQUEST =
            "SELECT temporal FROM cr_address_person ap " +
            "INNER JOIN cr_person p ON p.person_id = ap.person_id " +
            "INNER JOIN cr_address a ON a.address_id = ap.address_id " +
            "WHERE " +
            "CURRENT_DATE >= ap.start_date AND (CURRENT_DATE <= ap.end_date OR ap.end_date IS NULL)" +
            "AND UPPER(p.sur_name) = UPPER(?) " +
            "AND UPPER(p.given_name) = UPPER(?) " +
            "AND UPPER(p.patronymic) = UPPER(?) " +
            "AND p.date_of_birth = ? " +
            "AND a.street_code = ? " +
            "AND UPPER(a.building) = UPPER(?) ";

    private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {
        PersonResponse response = new PersonResponse();

        String SQL = SQL_REQUEST;
        if (request.getExtension() != null) {
            SQL += "AND UPPER(a.extension) = UPPER(?) ";
        } else {
            SQL += "AND a.extension IS NULL ";
        }
        if (request.getApartment() != null) {
            SQL += "AND UPPER(a.apartment) = UPPER(?) ";
        } else {
            SQL += "AND a.apartment IS NULL ";
        }

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL)){

            int count = 1;
            stmt.setString(count++, request.getSurName());
            stmt.setString(count++, request.getGivenName());
            stmt.setString(count++, request.getPatronymic());
            stmt.setDate(count++, java.sql.Date.valueOf(request.getDateOfBirth()));
            stmt.setInt(count++, request.getStreetCode());
            stmt.setString(count++, request.getBuilding());
            if (request.getExtension() != null) {
                stmt.setString(count++, request.getExtension());
            }
            if (request.getApartment() != null) {
                stmt.setString(count++, request.getApartment());
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                response.setRegistered(true);
                response.setTemporal(rs.getBoolean("temporal"));
            }


        } catch (SQLException ex){
            throw new PersonCheckException(ex);
        }


        return response;
    }

    private Connection getConnection() throws SQLException {
        return connectionBuilder.getConnection();
    }
}
