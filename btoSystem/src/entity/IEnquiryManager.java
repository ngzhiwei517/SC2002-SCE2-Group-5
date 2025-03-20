package entity;

import java.util.List;

//For manager only
public interface IEnquiryManager extends IEnquiryHandler{
    void viewAllEnquiries(List<Enquiry> enquiries);
}
