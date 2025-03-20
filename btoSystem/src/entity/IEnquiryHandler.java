package entity;//applicable for both HDB Manager and Officer

public interface IEnquiryHandler extends IEnquiryCreator{
    void replyToEnquiry(String enquiryId, String response);
}
