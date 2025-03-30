package boundary;

public class ManagerBoundary {






    public static void displayDashboard()
    {
        System.out.println("1. View BTO Project Listings"); //contains, RUD functions. should be able to filter visibility
        System.out.println("2. Create BTO Project Listing");
        System.out.println("3. View HDB Officer Registration");
        System.out.println("4. View Applicant Application");
        System.out.println("5. View Applicant Withdrawal Request");
        System.out.println("6. Generate Report");
        System.out.println("7. Enquiry Menu");
        System.out.println("8. Log Out");
    }

    private static void createBTOListing()
    {
        //request projname

        //request neighbourhood

        //request type1 details

        //request type2 details

        //rqeuest openingdate

        //request closingdate

        //request officerslots

        //manager will be passed in as user.loggeduser.
    }
}
