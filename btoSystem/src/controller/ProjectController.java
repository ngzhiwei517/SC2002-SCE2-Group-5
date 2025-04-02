package controller;

import entity.*;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private Project selectedProject = null;
    private List<Project> projects = new ArrayList<Project>();
    private static UserController userController;
    private final String projectPath = "ProjectList.csv";
    private final String flatPath = "FlatList.csv";

    public static void setUserController(UserController controller) {
        userController = controller;
    }

    public void init() {
        try {
            readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean readData() throws IOException {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(projectPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int project_id = Integer.parseInt(values[0]);
                String projectName = values[1];
                String neighbourhood = values[2];
                String openingDate = values[3];
                String closingDate = values[4];
                int managerID = Integer.parseInt(values[5]);
                int officerSlots = Integer.parseInt(values[6]);
                String officerArr = values[7];
                Project.Status status = Project.Status.valueOf(values[8]);

                Manager assignedManager = null;
                if (userController.getManager(managerID) != null) {
                    assignedManager = userController.getManager(managerID);
                }
                officerArr = officerArr.replaceAll("[\"']", "");
                String[] officerStrings = officerArr.split(",");
                List<Officer> officerList = new ArrayList<Officer>();
                for (String officerString : officerStrings) {
                    if (userController.getOfficer(Integer.parseInt(officerString)) != null) {
                        officerList.add(userController.getOfficer(Integer.parseInt(officerString)));
                    }
                }

                projects.add(new Project(project_id,
                        projectName,
                        neighbourhood,
                        openingDate,
                        closingDate,
                        assignedManager,
                        officerSlots,
                        officerList, status));
            }

        }
        try (BufferedReader br = new BufferedReader(new FileReader(flatPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
            String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            int f_id = Integer.parseInt(values[0]);
            int p_id = Integer.parseInt(values[1]);
            String type = values[2];
            int cost = Integer.parseInt(values[3]);
            int units = Integer.parseInt(values[4]);

            Flat flat = new Flat(f_id, p_id, type, cost, units);
            getProject(p_id).addFlat(flat);
            }
        }
        return true;
    }

    public void printProjectContents() { //debug function
        for (Project project : projects) {
            System.out.println("Project ID: " + project.getProjectID());
            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("Neighbourhood: " + project.getNeighborhood());
            System.out.println("Application Opening Date: " + project.getApplicationOpeningDate());
            System.out.println("Application Closing Date: " + project.getApplicationClosingDate());
            System.out.println("Manager: " + project.getManagerInCharge().getName());
            System.out.println("Officer Slots: " + project.getOfficerSlots());

            for (Officer officer : project.getOfficers()) {
                System.out.println("Officer: " + officer.getName());
            }
            System.out.println("----------------------------");
            for (Flat flat : project.getFlats()) {
                System.out.println("Flat Type: " + flat.getType());
                System.out.println("Flat Price: " + flat.getPrice());
                System.out.println("No. Units: " + flat.getUnits());
                System.out.println("----------------------------");
            }
            System.out.println("================================");
        }
    }

    public boolean writeData() {
        return false;
    }

    public Project getProject(String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }

    public Project addProject(String projectName, String neighbourhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots)
    {
        //get length of project + 1 as projectid
        Project newProject = new Project(projectName, neighbourhood,
                OpeningDate, ClosingDate, manager,
                officerSlots, new ArrayList<Officer>() {
        }, Project.Status.VISIBLE) ;
        projects.add(newProject);
        return newProject;
    }

    public void selectProject(Project project)
    {
        selectedProject = project;
    }

    public Project getSelectedProject()
    {
        return selectedProject;
    }

    public void clearSelectedProject()
    {
        selectedProject = null;
    }

    public Project getProject(int id)
    {
        for (Project project : projects) {
            if(project.getProjectID() == id)
            {
                return project;
            }
        }
        return null;
    }

    public List<Project> getProjects(List<Project.Status> filter, boolean rejectFilter)
    {
        List<Project> filteredList = new ArrayList<>();
        if(rejectFilter) {
            for (Project project : projects) {
                if(!filter.contains(project.getStatus())) {
                    filteredList.add(project);
                }
            }
        }
        else {
            for (Project project : projects) {
                if(filter.contains(project.getStatus())) {
                    filteredList.add(project);
                }
            }
        }
        return projects;
    }

    public List<Project> getEligibleProjects(User user)
    {
        List<Project> remapped = new ArrayList<Project>();
        for (Project project : projects) {
            if(user.getAge() >= 21 && user.isMarried()) //assert age conditions
            {
                remapped.add(project);
            }
            else if(user.getAge() >= 35)
            {
                for(Flat flat : project.getFlats()) {
                    if(flat.getType() == Flat.Type.TwoRoom)
                    {
                        remapped.add(project);
                    }
                }
            }
        }
        return remapped;
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
