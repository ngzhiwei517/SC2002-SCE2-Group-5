package dao;

import entity.Flat;
import entity.Manager;
import entity.Officer;
import entity.Project;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectCSVDAO implements ProjectDAO {
    private UserDAO userDAO;
    private HashMap<Integer, Project> projects = new HashMap<>();
    private final String projectPath = "ProjectList.csv";
    private final String flatPath = "FlatList.csv";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    @Override
    public void injectDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public HashMap<Integer, Project> get() {
        return projects;
    }

    @Override
    public boolean read() {
        //process applicants
        try (BufferedReader br = new BufferedReader(new FileReader(projectPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int project_id = Integer.parseInt(values[0]);
                String projectName = values[1];
                String neighbourhood = values[2];
                String openingDateStr = values[3];
                String closingDateStr = values[4];
                int managerID = Integer.parseInt(values[5]);
                int officerSlots = Integer.parseInt(values[6]);
                String officerArr = values[7];
                Project.Status status = Project.Status.valueOf(values[8]);

                LocalDate openingDate = LocalDate.parse(openingDateStr, formatter);
                LocalDate closingDate = LocalDate.parse(closingDateStr, formatter);

                Manager assignedManager = null;
                if (userDAO.getManager(managerID) != null) {
                    assignedManager = userDAO.getManager(managerID);
                }

                List<Officer> officerList = new ArrayList<Officer>();
                if(!officerArr.equalsIgnoreCase("\"\"")) { //TODO:: FIX band-aid fix on empty list issue.
                    officerArr = officerArr.replaceAll("[\"']", "");
                    String[] officerStrings = officerArr.split(",");
                    for (String officerString : officerStrings) {
                        if (userDAO.getOfficer(Integer.parseInt(officerString)) != null) {
                            officerList.add(userDAO.getOfficer(Integer.parseInt(officerString)));
                        }
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
        catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(flatPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int f_id = Integer.parseInt(values[0]);
                int p_id = Integer.parseInt(values[1]);
                String type = values[2];
                float cost = Float.parseFloat(values[3]);
                int units = Integer.parseInt(values[4]);

                Project project = projects.get(p_id);
                if (project != null) {
                    Flat flat = new Flat(f_id, project, type, cost, units);
                    project.addFlat(flat);
                } else {
                    System.out.println("Invalid Project ID: " + p_id);
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(projectPath))){
            bw.write("p_id,Project Name,Neighborhood,opening_date,closing_date,manager_id,officer_slot,officer_list,status");
            bw.newLine();
            for(int key : projects.keySet()) {
                Project proj = projects.get(key);
                String projectString = "";
                projectString += proj.getProjectID() + ",";
                projectString += proj.getProjectName() + ",";
                projectString += proj.getNeighborhood() + ",";
                projectString += proj.getOpeningDate().format(DateTimeFormatter.ofPattern("M/d/yyyy")) + ",";
                projectString += proj.getClosingDate().format(DateTimeFormatter.ofPattern("M/d/yyyy")) + ",";
                projectString += proj.getManager().getID() + ",";
                projectString += proj.getOfficerSlots() + ",";
                StringBuilder officerSubstring = new StringBuilder();
                List<Officer> officerList = proj.getOfficers();
                for (Officer officer : officerList) {
                    officerSubstring.append(officer.getID()).append(",");
                }
                if (!officerSubstring.isEmpty()) {
                    officerSubstring.setLength(officerSubstring.length() - 1); // remove last comma
                }
                projectString += "\"" + officerSubstring.toString() + "\",";
                projectString += proj.getStatus().toString();
                bw.write(projectString);
                bw.newLine();
                //System.out.println("Writing"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(flatPath))){
            bw.write("f.id,p.id,type,cost,units");
            bw.newLine();
            for(int key : projects.keySet()) {
                Project proj = projects.get(key);
                for(Flat flat : proj.getFlats()) {
                    String flatString = "";
                    flatString += flat.getId() + ",";
                    flatString += proj.getProjectID() + ",";
                    flatString += flat.getStringType() + ",";
                    flatString += flat.getPrice() + ",";
                    flatString += flat.getUnits();
                    bw.write(flatString);
                    bw.newLine();
                    //System.out.println("Writing"); //TODO: REMOVE DEBUG IDENTIFIER.
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean add(Project project) {
        if(!projects.containsKey(project.getProjectID())) {
            projects.put(project.getProjectID(), project);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Project project) {
        if(projects.containsKey(project.getProjectID())) {
            projects.remove(project.getProjectID());
            return true;
        }
        return false;
    }

    @Override
    public Project getProject(int id) {
        if(projects.containsKey(id)) {
            return projects.get(id);
        }
        else
            return null;
    }
}
