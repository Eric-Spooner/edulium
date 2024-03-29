package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the TableDAO interface
 */
@PreAuthorize("isAuthenticated()")
class DBTableDAO implements DAO<Table> {
    private static final Logger LOGGER = LogManager.getLogger(DBTableDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "userDAO")
    private DAO<User> userDAO;
    @Resource(name = "sectionDAO")
    private DAO<Section> sectionDAO;
    @Resource(name = "tableValidator")
    private Validator<Table> validator;

    /**
     * writes the object into the database and sets the identity parameter of
     * table
     *
     * @param table object to store
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public void create(Table table) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + table);

        validator.validateForCreate(table);

        if (!find(Table.withIdentity(table.getSection(), table.getNumber())).isEmpty()) {
            LOGGER.error("inserting table into database failed, table exists already");
            throw new DAOException("inserting table into database failed, table exists already");
        }

        final String query = "MERGE INTO RestaurantTable (section_ID, number, seats, tableRow, tableColumn, user_ID, disabled) " +
                "KEY (section_ID, number) " +
                "VALUES (?, ?, ?, ?, ?, ?, false)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getSection().getIdentity());
            stmt.setLong(2, table.getNumber());
            stmt.setInt(3, table.getSeats());
            stmt.setInt(4, table.getRow());
            stmt.setInt(5, table.getColumn());
            stmt.setString(6, table.getUser() != null ? table.getUser().getIdentity() : null); // optional

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("inserting table into database failed", e);
            throw new DAOException("inserting table into database failed", e);
        }

        generateHistory(table);
    }

    /**
     * updates the object in the database
     *
     * @param table object to update
     * @throws DAOException if an error accessing the database ocurred or if the
     *                      dataset was not found in the database
     */
    @Override
    public void update(Table table) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + table);

        validator.validateForUpdate(table);

        final String query = "UPDATE RestaurantTable SET " +
                "seats = ?, tableRow = ?, tableColumn = ?, user_ID = ? WHERE number = ? AND section_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setInt(1, table.getSeats());
            stmt.setInt(2, table.getRow());
            stmt.setInt(3, table.getColumn());
            if(table.getUser() == null){
                stmt.setObject(4,null);
            }else {
                stmt.setString(4, table.getUser().getIdentity());
            }
            stmt.setLong(5, table.getNumber());
            stmt.setLong(6, table.getSection().getIdentity());

            if (stmt.executeUpdate() == 0) {
                throw new DAOException("updating table failed: dataset not found");
            }

        } catch (SQLException e) {
            LOGGER.error("updating table in database failed", e);
            throw new DAOException("updating table in database failed", e);
        }

        generateHistory(table);
    }

    /**
     * removes the object from the database
     *
     * @param table object to remove
     * @throws DAOException if an error accessing the database occurred or if
     *                      the dataset was not found in the database
     */
    @Override
    public void delete(Table table) throws DAOException, ValidationException {
        LOGGER.debug("entering delete with parameters " + table);

        validator.validateForDelete(table);

        final String query = "UPDATE RestaurantTable SET disabled = true WHERE number = ? AND section_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getNumber());
            stmt.setLong(2, table.getSection().getIdentity());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("delete table failed: dataset not found");
            }

        } catch (SQLException e) {
            LOGGER.error("deleting table failed", e);
            throw new DAOException("deleting table failed", e);
        }

        generateHistory(table);
    }

    /**
     * returns all objects from the database which parameters match the
     * parameters of the object table
     * all parameters with value NULL will not be used for matching
     *
     * @param table object used for matching
     * @return returns a list of objects from the database which match the criteria
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Table> find(Table table) throws DAOException {
        LOGGER.debug("entering find with parameters " + table);

        if (table == null) {
            return new ArrayList<>();
        }

        String query = "SELECT * FROM RestaurantTable WHERE number = ISNULL(?, number) " +
                "AND seats = ISNULL(?, seats) AND section_ID = ISNULL(?, section_ID)" +
                "AND tableRow = ISNULL(?, tableRow) AND tableColumn = ISNULL(?, tableColumn)" +
                "AND CASE WHEN (? IS NULL) THEN TRUE ELSE user_ID = ? END AND disabled = false";

        final List<Table> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, table.getNumber());
            stmt.setObject(2, table.getSeats());
            stmt.setObject(3, table.getSection() != null ? table.getSection().getIdentity() : null);
            stmt.setObject(4, table.getRow());
            stmt.setObject(5, table.getColumn());
            if (table.getUser() == null) {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
            } else {
                stmt.setObject(6, table.getUser().getIdentity());
                stmt.setObject(7, table.getUser().getIdentity());
            }

            stmt.execute();

            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                try {
                    objects.add(parseResult(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("searching for table failed", e);
            throw new DAOException("searching for table failed", e);
        }

        return objects;
    }

    /**
     * @return returns all objects in the database
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Table> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM RestaurantTable WHERE disabled = false";
        final List<Table> objects = new ArrayList<>();

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            stmt.execute(query);

            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                try {
                    objects.add(parseResult(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("searching for all tables failed", e);
            throw new DAOException("searching for all tables failed", e);
        }

        return objects;
    }

    /**
     * @param table object to get the history for
     * @return returns the history of changes for the table object
     * @throws DAOException        if the data couldn't be retrieved
     * @throws ValidationException if the table object parameters are
     *                             not valid for this action
     */
    @Override
    public List<History<Table>> getHistory(Table table) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + table);

        validator.validateIdentity(table);

        List<History<Table>> history = new ArrayList<>();
        final String query = "SELECT * FROM TableHistory WHERE number = ? ORDER BY changeNr";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getNumber());
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                history.add(parseHistoryEntry(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history failed", e);
            throw new DAOException("retrieving history failed", e);
        }

        return history;
    }

    @Override
    public List<Table> populate(List<Table> tables) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + tables);

        if (tables == null || tables.isEmpty()) {
            return new ArrayList<>();
        }

        for (Table table : tables) {
            validator.validateIdentity(table);
        }

        final String query = "SELECT * FROM RestaurantTable WHERE (section_ID, number) IN (" +
                tables.stream().map(t -> "(?, ?)").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<Table> populatedTables = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (Table table : tables) {
                stmt.setLong(index++, table.getSection().getIdentity());
                stmt.setLong(index++, table.getNumber());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedTables.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating tables failed", e);
            throw new DAOException("Populating tables failed", e);
        }

        return populatedTables;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     *
     * @param table updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(Table table) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + table);

        final String query = "INSERT INTO TableHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM TableHistory WHERE number = ?) " +
                "FROM RestaurantTable WHERE number = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, table.getNumber());          // dataset id
            stmt.setLong(3, table.getNumber());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a object
     *
     * @param result database output
     * @return Table object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Table parseResult(ResultSet result) throws SQLException, ValidationException, DAOException {
        Table table = new Table();
        table.setNumber(result.getLong("number"));
        table.setSeats(result.getInt("seats"));
        table.setRow(result.getInt("tableRow"));
        table.setColumn(result.getInt("tableColumn"));

        // get user
        final String userId = result.getString("user_ID");
        if (userId != null) {  // optional
            List<User> storedUsers = userDAO.populate(Collections.singletonList(User.withIdentity(userId)));
            if (storedUsers.size() != 1) {
                throw new DAOException("user not found");
            }
            table.setUser(storedUsers.get(0));
        }

        // get section
        final long sectionId = result.getLong("section_ID");
        List<Section> storedSections = sectionDAO.populate(Collections.singletonList(Section.withIdentity(sectionId)));
        if (storedSections.size() != 1) {
            throw new DAOException("section not found");
        }
        table.setSection(storedSections.get(0));

        return table;
    }

    /**
     * converts the database query output into a history entry object
     *
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Table> parseHistoryEntry(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = userDAO.populate(Collections.singletonList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            throw new DAOException("user not found");
        }

        // create history entry
        History<Table> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("disabled"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResult(result));

        return historyEntry;
    }
}