package controller;

import entity.*;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectController {
    private List<Project> projects = new ArrayList<Project>();
    private static UserController userController;
    private final String projectPath = "ProjectList.csv";

    public static void setUserController(UserController controller)
    {
        userController = controller;
    }

    public void init()
    {
        try{readData();}
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean readData() throws IOException
    {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(projectPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String projectName = values[0];
                String neighbourhood = values[1];
                String type1 = values[2];
                int type1units = Integer.parseInt(values[3]);
                int type1price = Integer.parseInt(values[4]);
                String type2 = values[5];
                int type2units = Integer.parseInt(values[6]);
                int type2price = Integer.parseInt(values[7]);
                String openingDate = values[8];
                String closingDate = values[9];
                String ManagerName = values[10];
                int officerSlots = Integer.parseInt(values[11]);
                String officerArr = values[12];

                Manager assignedManager = null;
                if(userController.getManager(ManagerName) != null)
                {
                    assignedManager = userController.getManager(ManagerName);
                }
                officerArr = officerArr.replaceAll("[\"']", "");
                String[] officerStrings = officerArr.split(",");
                List<Officer> officerList = new ArrayList<Officer>();
                for (String officerString : officerStrings) {
                    System.out.println(officerString);
                    if(userController.getOfficer(officerString) != null) {

                        officerList.add(userController.getOfficer(officerString));
                    }
                }

                projects.add(new Project(projectName,
                        neighbourhood,
                        type1,
                        type1units,
                        type1price,
                        type2,
                        type2units,
                        type2price,
                        openingDate,
                        closingDate,
                        assignedManager,
                        officerSlots,
                        officerList));

            }
            return true;
        }
    }

    public void printProjectContents()
    {
        for (Project project : projects) {
            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("Neighbourhood: " + project.getNeighborhood());
            System.out.println("Application Opening Date: " + project.getApplicationOpeningDate());
            System.out.println("Application Closing Date: " + project.getApplicationClosingDate());
            System.out.println("Manager: " + project.getManagerInCharge().getName());
            System.out.println("Officer Slots: " + project.getOfficerSlots());

            for(Officer officer : project.getOfficers()) {
                System.out.println("Officer: " + officer.getName());
            }
            System.out.println("----------------------------");
            for(Flat flat : project.getFlats()) {
                System.out.println("Flat Type: " + flat.getType());
                System.out.println("Flat Price: " + flat.getPrice());
                System.out.println("No. Units: " + flat.getUnits());
                System.out.println("----------------------------");
            }

            System.out.println("================================");

        }
    }

    public boolean writeData()
    {
        return false;
    }

    public Project getProject(String projectName)
    {
        for(Project project : projects)
        {
            if(project.getProjectName().equals(projectName))
            {
                return project;
            }
        }
        return null;
    }

    public void viewProjectList(User user)
    {
        //check for visibility
        if(user instanceof Officer)
        {

        }
        else if(user instanceof Applicant)
        {
            if(user.getAge() >= 21 && user.isMarried()) {             //married > 21y/o, can only apply for any type.
                for (Project project : projects) {
                    System.out.println(project.getProjectName());
                    for (Flat flat : project.getFlats()) {
                        System.out.println(flat.getType() + " " + flat.getUnits() + " " + flat.getPrice());
                    }
                }
            }
            else if(user.getAge() >= 35) {            //single > 35y/o, can only apply for 2 room
                for (Project project : projects) {
                    System.out.println(project.getProjectName());
                    for (Flat flat : project.getFlats()) {
                        if(flat.getType() == Flat.Type.TwoRoom) {
                            System.out.println(flat.getType() + " " + flat.getUnits() + " " + flat.getPrice());
                        }
                    }
                }
            }
            else {
                //not elligible;
            }
        }
        else
        {

        }


    }

    public void applyProject(User user, boolean asApplicant) {
        //check for visibility
        if (user instanceof Officer && !asApplicant) {

        } else if (user instanceof Applicant) {
            if (user.getAge() > 21 && user.isMarried()) {             //married > 21y/o, can only apply for any type.

            } else if (user.getAge() > 35) {            //single > 35y/o, can only apply for 2 room

            } else {
                //not elligible;
            }
        } else {

        }
    }

}
