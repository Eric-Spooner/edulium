package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

/**
 * Created by Administrator on 06.05.2015.
 */

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 Database Implementation of the section interface
 */
public class SectionDAOImpl implements DAO<Section> {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<Section> validator;

    /**
     * writes the object into the database and sets the identity parameter of
     * section
     * @param section object to store
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public void create(Section section) throws DAOException, ValidationException {
        assert(section != null);

        validator.validateForCreate(section);

        final String query = "INSERT INTO RestaurantSection (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, section.getName());
            stmt.executeUpdate();

            try (ResultSet key = stmt.getGeneratedKeys()) {
                key.next();
                section.setIdentity(key.getLong(1));
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(section.getIdentity()); TODO
    }

    /**
     * updates the object in the database
     * @param section object to update
     * @throws DAOException if an error accessing the database ocurred or if the
     *         dataset was not found in the database
     */
    @Override
    public void update(Section section) throws DAOException, ValidationException {
        assert(section != null);

        validator.validateForUpdate(section);

        final String query = "UPDATE RestaurantSection SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, section.getName());
            stmt.setLong(2, section.getIdentity());

            if (stmt.executeUpdate() == 0) {
                throw new DAOException("updating failed: dataset not found");
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(section.getIdentity()); TODO
    }

    /**
     * removes the object from the database
     * @param section object to remove
     * @throws DAOException if an error accessing the database occurred or if
     *         the dataset was not found in the database
     */
    @Override
    public void delete(Section section) throws DAOException, ValidationException {
        assert(section != null);

        validator.validateForDelete(section);

        final String query = "UPDATE RestaurantSection SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, section.getIdentity());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("delete failed: dataset not found");
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(section.getIdentity()); TODO
    }

    /**
     * returns all objects from the database which parameters match the
     * parameters of the object section
     * all parameters with value NULL will not be used for matching
     * @param section object used for matching
     * @return returns a list of objects from the database which match the criteria
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Section> find(Section section) throws DAOException {
        assert(section != null);
        String query = "SELECT * FROM RestaurantSection WHERE ID = ISNULL(?, ID) " +
                "AND name = ISNULL(?, name) AND deleted = false";

        final List<Section> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, section.getIdentity());
            stmt.setString(2, section.getName());
            stmt.execute();

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return objects;
    }

    /**
     * @return returns all objects in the database
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Section> getAll() throws DAOException {
        final String query = "SELECT * FROM RestaurantSection WHERE deleted = false";
        final List<Section> objects = new ArrayList<>();

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            stmt.execute(query);

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return objects;
    }

    @Override
    public List<History<Section>> getHistory(Section object) throws DAOException, ValidationException {
        return null;
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return Section object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Section parseResult(ResultSet result) throws SQLException {
        Section section = new Section();
        section.setIdentity(result.getLong(1));
        section.setName(result.getString(2));
        return section;
    }
}
