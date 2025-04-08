package entity;
public class Enquiry {
    public enum Status {
        PENDING,
        CLOSED,
        DELETED
    }

    private static int next_id = 0;
    private int e_id;
    private Project project;
    private Applicant user;
    private Status status;
    private String title;
    private String text;
    private User responder;
    private String response;

    public Enquiry(int e_id, Project project, Applicant user, String status, String title, String text, User responder, String response) {
        this.e_id = e_id;
        if(e_id >= next_id)
        {
            next_id = ++e_id;
        }
        this.project = project;
        this.user = user;
        this.status = Status.valueOf(status);
        this.title = title;
        this.text = text;
        this.responder = responder;
        this.response = response;

        project.addEnquiry(this);
        user.addEnquiry(this);
    }

    public Enquiry(Project project, Applicant user, Status status, String title, String text) { //used for new enquiry
        this.e_id = next_id++;
        this.project = project;
        this.user = user;
        this.status = status;
        this.title = title;
        this.text = text;

        project.addEnquiry(this);
        user.addEnquiry(this);
    }

    public void print()
    {
        System.out.print("Enquiry ID" + e_id + ": ");
        System.out.print(title);
        System.out.println();
        System.out.println(text);


        if(responder != null) {
            System.out.println("..................................");
            System.out.println("Response by: " + responder.getName());
            System.out.println(response);
        }

        System.out.println("==================================");
    }

    public boolean respond(User user, String response)
    {
        if(user instanceof Officer || user instanceof Manager) {
            this.responder = user;
            this.response = response;
            this.status = Status.CLOSED;
            return true;
        }
        return false;
    }

    public void setText(String text){ this.text = text; }
    public void setTitle(String title){ this.title = title; }
    public void setResponder(User responder){ this.responder = responder; }
    public void setResponse(String response){ this.response = response; }

    public int getEnquiryId() { return e_id; }
    public Project getProject() { return project; }
    public Applicant getUser() { return user; }
    public Status getStatus() { return status; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public User getResponder() { return responder; }
    public String getResponse() { return response; }

    public void delete(){
        this.user.removeEnquiry(this);
        this.project.removeEnquiry(this);
        this.status = Status.DELETED;
    }

}
