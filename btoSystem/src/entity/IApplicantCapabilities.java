package entity;

public interface IApplicantCapabilities {
    void viewEligibleProjects();
    void applyForProject(Project project,String flatType);
    void viewApplication();
    void requestWithdrawal();
    boolean canApplyForProject(Project project);


}
