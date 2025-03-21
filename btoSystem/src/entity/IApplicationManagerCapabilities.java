package entity;

public interface IApplicationManagerCapabilities extends IApplicationOfficerCapabilities,IApplicationProcessor{
    void assignApplicationToOfficer(String applicationId, HDBOfficer officer);
}
