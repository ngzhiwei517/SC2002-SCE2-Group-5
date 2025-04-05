package controller;

import entity.*;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectController {
    private static Project selectedProject = null;
    private static Map<Integer, Project> projects = new HashMap<Integer, Project>();
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

                if(!projects.containsKey(project_id)) {
                    projects.put(project_id, new Project(project_id,
                            projectName,
                            neighbourhood,
                            openingDate,
                            closingDate,
                            assignedManager,
                            officerSlots,
                            officerList, status));
                }
                else {
                    System.out.println("Duplicate Project ID" + project_id);
                }
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

            Project project = projects.get(p_id);
            if(project != null)
            {
                Flat flat = new Flat(f_id, project, type, cost, units);
                project.addFlat(flat);
            }
            System.out.println("Invalid Project ID: " + p_id);
            }
        }
        return true;
    }

    public boolean writeData() {
        return false;
    }

    public static Project addProject(String projectName, String neighbourhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots)
    {
        //get length of project + 1 as projectid
        Project new_project = new Project(projectName, neighbourhood,
                OpeningDate, ClosingDate, manager,
                officerSlots, new ArrayList<Officer>() {
        }, Project.Status.VISIBLE) ;

        if(!projects.containsKey(new_project.getProjectID())) {
            projects.put(new_project.getProjectID(), new_project);
        }
        return new_project;
    }

    public static void selectProject(Project project)
    {
        selectedProject = project;
    }

    public static Project getSelectedProject()
    {
        return selectedProject;
    }

    public static void clearSelectedProject()
    {
        selectedProject = null;
    }

    public static Project getProject(int id)
    {
        if(projects.containsKey(id))
        {
            return projects.get(id);
        }
        else
            return null;
    }


    public static List<Project> getProjects(List<Project.Status> filter, boolean rejectFilter)
    {
        List<Project> filteredList = new ArrayList<>();
        if(rejectFilter) {
            for (int key : projects.keySet()) {
                if(!filter.contains(projects.get(key).getStatus())) {
                    filteredList.add(projects.get(key));
                }
            }
        }
        else {
            for (int key : projects.keySet()) {
                if(filter.contains(projects.get(key).getStatus())) {
                    filteredList.add(projects.get(key));
                }
            }
        }
        return filteredList;
    }

    public static List<Project> getProjects(Officer officer)
    {
        List<Project> filtered = new ArrayList<Project>();
        for(int key : projects.keySet()) {
            if(projects.get(key).getOfficers().contains(officer)) {
                filtered.add(projects.get(key));
            }
        }
        return filtered;
    }

    public static List<Project> getProjects()
    {
        List<Project> ret = new ArrayList<Project>();
        for(int key : projects.keySet()) {
            ret.add(projects.get(key));
        }
        return ret;
    }

    public List<Project> getEligibleProjects(User user) {
        List<Project> remapped = new ArrayList<Project>();
        for (int key : projects.keySet()) {
            if(projects.get(key).isEligible(user)) {
                remapped.add(projects.get(key));
            }
        }
        return remapped;
    }

}
