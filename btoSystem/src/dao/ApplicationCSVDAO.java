package dao;

import controller.UserController;
import entity.*;

import java.io.*;
import java.util.HashMap;

public class ApplicationCSVDAO implements ApplicationDAO {
    private UserDAO userDAO;
    private ProjectDAO projectDAO;
    private HashMap<Integer, Application> applications = new HashMap<>();
    private final String path = "ApplicationList.csv";

    @Override
    public void injectDAO(UserDAO userDAO, ProjectDAO projectDAO) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public HashMap<Integer, Application> get() {
        return applications;
    }

    @Override
    public boolean read() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                int applicant_id = Integer.parseInt(data[1]);
                int project_id = Integer.parseInt(data[2]);
                int flat_id = Integer.parseInt(data[3]);
                Application.Type type = Application.Type.valueOf(data[4]);
                Application.Status status = Application.Status.valueOf(data[5]);

                Applicant user = (Applicant) userDAO.getUser(applicant_id);
                Project project = projectDAO.getProject(project_id);

                Flat flat = flat_id != -1 ? project.getFlat(flat_id) : null;

                Application application = new Application(id, user, project,flat,status,type);
                applications.put(id, application);
                System.out.println("Reading");
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write("a.id,u.id,p.id,f.id,type,status");
            bw.newLine();
            //form the line application string here
            for(int key : applications.keySet()) {
                Application app = applications.get(key);
                String applicationString = "";
                applicationString += app.getId() + ",";
                applicationString += app.getUser().getID() + ",";
                applicationString += app.getProject().getProjectID() + ",";
                applicationString += (app.getFlat() != null ? app.getFlat().getId() : "-1") + ",";
                applicationString += app.getType().toString() + ",";
                applicationString += app.getStatus().toString() + ",";
                bw.write(applicationString);
                bw.newLine();
                System.out.println("Writing");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean add(Application application) {
        if(!applications.containsKey(application.getId()))
        {
            applications.put(application.getId(), application);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Application application) {
        if(applications.containsKey(application.getId()))
        {
            applications.remove(application.getId());
            return true;
        }
        return false;
    }
}
