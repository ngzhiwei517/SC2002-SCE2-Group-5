package entity;

public interface IApplicationProcessor {
    void approveApplication(String applicationId);
    void rejectApplication(String applicationId);
    void approveWithdrawalRequest(String applicationId);
}
