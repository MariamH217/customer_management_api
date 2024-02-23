
import java.util.List;
import java.util.Scanner;

public class UserInputCustomerDAO {
    private CustomerDAO customerDAO;

    public UserInputCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }


    public void findCustomerByName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the customer:");
        String name = scanner.nextLine();

        List<Customer> customers = customerDAO.findCustomersByName(name);

        if (customers.isEmpty()) {
            System.out.println("No customer is found.");
        } else {
            System.out.println("List of customers:");
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }

    public void updateCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Id of customer:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the new name:");
        String name = scanner.nextLine();


        System.out.println("Enter the new email:");
        String email = scanner.nextLine();

        System.out.println("Enter the new address:");
        String address = scanner.nextLine();


        Customer customer = new Customer(id, name, email, address);
        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of customer you want to delete");
        Integer id = scanner.nextInt();
        customerDAO.deleteCustomer(id);


    }

    public void addCustomer() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name:");
        String name = scanner.nextLine();

        System.out.println("Enter the email:");
        String email = scanner.nextLine();

        System.out.println("Enter the address:");
        String address = scanner.nextLine();


        Customer customer = new Customer(name, email, address);
        customerDAO.addCustomer(customer);

    }

    public void printAllCustomers() {
        List<Customer> customers = customerDAO.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("List of customers:");
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }


}

