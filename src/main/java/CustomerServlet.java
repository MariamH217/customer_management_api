import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class CustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO(); // Initialize CustomerDAO

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            // Set response content type to JSON
            response.setContentType("application/json");

            // Retrieve list of customers from DAO
            List<Customer> customers = customerDAO.getAllCustomers();

            // Serialize list of customers to JSON
            StringBuilder jsonResponse = new StringBuilder("[");
            for (Customer customer : customers) {
                String jsonCustomer = customerDAO.serializeCustomerToJson(customer);
                jsonResponse.append(jsonCustomer).append(",");
            }
            if (!customers.isEmpty()) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Remove trailing comma
            }
            jsonResponse.append("]");

            // Write JSON data to response
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred while processing the request\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            // Set request and response content type to JSON
            req.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            // Read JSON data from request body
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Deserialize JSON data to Customer object
            Customer newCustomer = customerDAO.deserializeJsonToCustomer(requestBody.toString());

            // Add new customer to the database
            customerDAO.addCustomer(newCustomer);

            // Return details of the newly added customer in the response
            response.getWriter().write(customerDAO.serializeCustomerToJson(newCustomer));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred while processing the request\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            // Set response content type to JSON
            response.setContentType("application/json");

            // Retrieve id parameter from the request
            String idParameter = req.getParameter("id");

            if (idParameter != null && !idParameter.isEmpty()) {
                try {
                    // Convert id parameter to integer
                    int customerId = Integer.parseInt(idParameter);

                    // Delete customer from the database based on id
                    customerDAO.deleteCustomer(customerId);

                    // Return a success message or response code
                    response.getWriter().write("{\"message\": \"Customer deleted successfully\"}");
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Invalid id parameter
                    response.getWriter().write("{\"error\": \"Invalid id parameter\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Missing id parameter
                response.getWriter().write("{\"error\": \"Missing id parameter\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred while processing the request\"}");
            e.printStackTrace();
        }
    }
}
