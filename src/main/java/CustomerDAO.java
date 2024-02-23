import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgresdb";
    private static String USER = "postgres";
    private static String PASSWORD = "password1234";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Connection connection ;

    public CustomerDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL server");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }


    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                Customer customer = new Customer(id,name, email, address);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, email, address) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getAddress());

            preparedStatement.executeUpdate();
            System.out.println("Customer added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteCustomer(Integer customerId) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
            System.out.println("Customer deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, address = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Customer> findCustomersByName(String name) {
        String sql = "SELECT * FROM customers WHERE name = ?";
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                Customer customer = new Customer(id, customerName, email, address);
                customers.add(customer);
            }

            if (customers.isEmpty()) {
                System.out.println("No customers found with the name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching customers by name: " + e.getMessage());
            e.printStackTrace();
        }

        return customers;
    }



    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String serializeCustomerToJson(Customer customer) {
        try {
            return objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer deserializeJsonToCustomer(String json) {
        try {
            return objectMapper.readValue(json, Customer.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
